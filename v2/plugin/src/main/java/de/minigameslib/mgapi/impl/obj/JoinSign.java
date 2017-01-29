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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.JoinSignInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class JoinSign extends AbstractBaseArenaObjectHandler<SignRuleSetType, SignRuleSetInterface, JoinSignData> implements JoinSignInterface
{
    
    /** the underlying sign. */
    protected SignInterface sign;

    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        super.initArena(a);
        this.dataFile = new File(MinigamesPlugin.instance().getDataFolder(), "arenas/" + this.arena.getInternalName() + '/' + this.sign.getSignId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (this.dataFile.exists())
        {
            this.loadData();
        }
        else
        {
            this.saveData();
        }
    }

    @Override
    public void onCreate(SignInterface c) throws McException
    {
        this.sign = c;
    }

    @Override
    public void onResume(SignInterface c) throws McException
    {
        this.sign = c;
    }

    @Override
    public void onPause(SignInterface c)
    {
        // do nothing
    }

    @Override
    public void canDelete() throws McException
    {
        this.checkModifications();
    }

    @Override
    public void onDelete()
    {
        if (this.dataFile.exists())
        {
            this.dataFile.delete();
        }
    }

    @Override
    public void read(DataSection section)
    {
        // no additional data in mclib files
    }

    @Override
    public void write(DataSection section)
    {
        // no additional data in mclib files
    }

    @Override
    public boolean test(DataSection section)
    {
        // no additional data in mclib files
        return true;
    }

    @Override
    protected Class<JoinSignData> getDataClass()
    {
        return JoinSignData.class;
    }

    @Override
    protected JoinSignData createData()
    {
        return new JoinSignData();
    }

    @Override
    protected void applyListeners(SignRuleSetInterface listeners)
    {
        this.sign.registerHandlers(MinigamesPlugin.instance(), listeners);
    }

    @Override
    protected void removeListeners(SignRuleSetInterface listeners)
    {
        this.sign.unregisterHandlers(MinigamesPlugin.instance(), listeners);
    }

    @Override
    protected SignRuleSetInterface create(SignRuleSetType ruleset) throws McException
    {
        return MinigamesPlugin.instance().creator(ruleset).apply(ruleset, this);
    }
    
}
