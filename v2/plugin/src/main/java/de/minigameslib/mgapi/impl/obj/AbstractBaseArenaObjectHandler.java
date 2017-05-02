/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mgapi.impl.obj;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.BaseArenaObjectHandler;
import de.minigameslib.mgapi.api.rules.RuleSetInterface;
import de.minigameslib.mgapi.api.rules.RuleSetType;
import de.minigameslib.mgapi.impl.arena.ArenaImpl.Messages;
import de.minigameslib.mgapi.impl.rules.AbstractRuleSetContainer;

/**
 * base implementation of arena objects
 * 
 * @author mepeisen
 * @param <T> rule set type
 * @param <Q> rule set interface
 * @param <D> rule set data type
 */
public abstract class AbstractBaseArenaObjectHandler<
    T extends RuleSetType,
    Q extends RuleSetInterface<T>,
    D extends AbstractObjectData<T>> extends AbstractRuleSetContainer<T, Q> implements BaseArenaObjectHandler<T, Q>
{
    
    /**
     * Returns the data class to be used for persistent data.
     * @return data class
     */
    protected abstract Class<D> getDataClass();
    
    /**
     * Creates a new empty data value.
     * @return data value.
     */
    protected abstract D createData();
    
    /**
     * persistent data
     */
    protected D data;
    
    /**
     * the data file to store persistent data
     */
    protected File dataFile;
    
    /**
     * the associated arena.
     */
    protected ArenaInterface arena;
    
    /**
     * Saves persistent object data
     * @throws McException
     */
    public void saveData() throws McException
    {
        final DataSection section = new MemoryDataSection();
        section.set("data", this.data); //$NON-NLS-1$
        try
        {
            McLibInterface.instance().saveYmlFile(section, this.dataFile);
        }
        catch (IOException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
    }
    
    /**
     * Loads persistent data
     * @throws McException
     */
    public void loadData() throws McException
    {
        try
        {
            final DataSection section = McLibInterface.instance().readYmlFile(this.dataFile);
            this.data = section.getFragment(this.getDataClass(), "data"); //$NON-NLS-1$
            if (this.data == null)
            {
                this.data = this.createData();
            }
            
            this.resumeRuleSets();
        }
        catch (IOException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
    }
    
    /**
     * Resumes the rule sets after loading the config
     * @throws McException
     */
    private void resumeRuleSets() throws McException
    {
        for (final T ruleset : this.data.getFixedRules())
        {
            this.applyFixedRuleSet(ruleset);
        }
        for (final T ruleset : this.data.getOptionalRules())
        {
            this.applyOptionalRuleSet(ruleset);
        }
    }

    @Override
    public Collection<T> getAvailableRuleSetTypes()
    {
        // TODO implement available rule sets
        return Collections.emptyList();
    }

    @Override
    public boolean isAvailable(T ruleset)
    {
        // TODO implement available rule sets
        return false;
    }

    @Override
    public void reconfigureRuleSets(@SuppressWarnings("unchecked") T... rulesets) throws McException
    {
        for (final T t : rulesets)
        {
            this.reapplyRuleSet(t);
        }
    }

    @Override
    public void reconfigureRuleSet(T t) throws McException
    {
        this.reapplyRuleSet(t);
    }

    @Override
    public void applyRuleSets(@SuppressWarnings("unchecked") T... rulesets) throws McException
    {
        for (final T t : rulesets)
        {
            if (!this.isApplied(t))
            {
                this.applyOptionalRuleSet(t);
                this.data.getOptionalRules().add(t);
                this.saveData();
            }
        }
    }

    @Override
    public void applyRuleSet(T t) throws McException
    {
        if (!this.isApplied(t))
        {
            this.applyOptionalRuleSet(t);
            this.data.getOptionalRules().add(t);
            this.saveData();
        }
    }

    @Override
    public void removeRuleSets(@SuppressWarnings("unchecked") T... rulesets) throws McException
    {
        for (final T t : rulesets)
        {
            if (this.isOptional(t))
            {
                this.removeOptionalRuleSet(t);
                this.data.getOptionalRules().remove(t);
                this.saveData();
            }
        }
    }

    @Override
    public void removeRuleSet(T t) throws McException
    {
        if (this.isOptional(t))
        {
            this.removeOptionalRuleSet(t);
            this.data.getOptionalRules().remove(t);
            this.saveData();
        }
    }

    @Override
    public String getName()
    {
        return this.data.getName();
    }

    @Override
    public void setName(String newName) throws McException
    {
        this.checkModifications();
        this.data.setName(newName);
        this.saveData();
    }

    @Override
    public ArenaInterface getArena()
    {
        return this.arena;
    }

    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        this.arena = a;
        this.data = this.createData();
    }

    @Override
    protected void checkModifications() throws McException
    {
        if (this.arena != null && this.arena.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
    }
    
}
