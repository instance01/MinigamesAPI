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

package de.minigameslib.mgapi.impl.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.rules.RuleSetInterface;
import de.minigameslib.mgapi.api.rules.RuleSetType;

/**
 * Abstract base class for rule set container
 * 
 * @author mepeisen
 * @param <T> rule set type
 * @param <Q> rule set
 */
public abstract class AbstractRuleSetContainer<T extends RuleSetType, Q extends RuleSetInterface<T>>
{
    
    /** the applied fixed rule sets that cannot be removed by administrators. */
    private Set<T> fixedRuleSets = new HashSet<>();
    
    /** the optional rule set being removable by administrators. */
    private Set<T> optionalRuleSets = new HashSet<>();
    
    /** the rule sets applied to this component */
    private Map<T, Q> ruleSets = new HashMap<>();
    
    /**
     * Checks the object of this container for possible modifications.
     * @throws McException thrown if modifications are not allowed.
     */
    protected abstract void checkModifications() throws McException;
    
    /**
     * Applies listeners
     * @param listeners
     */
    protected abstract void applyListeners(Q listeners);
    
    /**
     * Removes listeners
     * @param listeners
     */
    protected abstract void removeListeners(Q listeners);

    /**
     * Creates a ruleset from type
     * @param ruleset
     * @return rule set impl
     * @throws McException thrown if rule set is invalid
     */
    protected abstract Q create(T ruleset) throws McException;

    /**
     * Returns rule set for given type
     * @param type
     * @return rule set
     */
    public Q getRuleSet(T type)
    {
        return this.ruleSets.get(type);
    }

    /**
     * Checks for applied rule set
     * @param ruleset
     * @return true for applied ruleset
     */
    public boolean isApplied(T ruleset)
    {
        return this.isFixed(ruleset) || this.isOptional(ruleset);
    }

    /**
     * Gets applied rule set
     * @return applied rule set
     */
    public Collection<T> getAppliedRuleSetTypes()
    {
        final List<T> result = new ArrayList<>(this.fixedRuleSets);
        result.addAll(this.optionalRuleSets);
        return result;
    }

    /**
     * Checks for fixed rule set
     * @param ruleset
     * @return fixed rule set
     */
    public boolean isFixed(T ruleset)
    {
        return this.fixedRuleSets.contains(ruleset);
    }

    /**
     * Checks for optional rule set
     * @param ruleset
     * @return true if rulset is optional
     */
    public boolean isOptional(T ruleset)
    {
        return this.optionalRuleSets.contains(ruleset);
    }

    /**
     * Adds fixed rule sets.
     * @param rulesets
     * @throws McException
     */
    public void applyFixedRuleSets(@SuppressWarnings("unchecked") T... rulesets) throws McException
    {
        for (final T ruleset : rulesets)
        {
            this.fixedRuleSets.add(ruleset);
        }
    }
    
    /**
     * Clears all rulesets
     * @throws McException
     */
    public void clearRuleSets() throws McException
    {
        for (final T type : this.fixedRuleSets)
        {
            this.removeFixedRuleSet(type);
        }
        for (final T type : this.optionalRuleSets)
        {
            this.removeOptionalRuleSet(type);
        }
    }

    /**
     * Applies new rule set
     * @param ruleset
     * @return new rule set
     * @throws McException
     */
    public Q applyFixedRuleSet(T ruleset) throws McException
    {
        this.checkModifications();
        if (this.isApplied(ruleset)) return this.getRuleSet(ruleset);
        final Q result = create(ruleset);
        this.fixedRuleSets.add(ruleset);
        this.ruleSets.put(ruleset, result);
        this.applyListeners(result);
        return result;
    }

    /**
     * Applies new rule set
     * @param ruleset
     * @return new rule set
     * @throws McException
     */
    public Q applyOptionalRuleSet(T ruleset) throws McException
    {
        this.checkModifications();
        if (this.isApplied(ruleset)) return this.getRuleSet(ruleset);
        final Q result = create(ruleset);
        this.optionalRuleSets.add(ruleset);
        this.ruleSets.put(ruleset, result);
        this.applyListeners(result);
        return result;
    }
    
    /**
     * Re-applies given rule set(f.e. after config changes)
     * @param ruleset
     * @return new rule set
     * @throws McException
     */
    public Q reapplyRuleSet(T ruleset) throws McException
    {
        this.checkModifications();
        if (!this.isApplied(ruleset)) throw new McException(CommonMessages.InternalError, "Cannot reconfigure unapplied rule"); //$NON-NLS-1$
        final Q result = create(ruleset);
        this.removeListeners(this.ruleSets.get(ruleset));
        this.ruleSets.put(ruleset, result);
        this.applyListeners(result);
        return result;
    }

    /**
     * removes optional rule set
     * @param ruleset
     * @return rule set instance
     * @throws McException
     */
    public Q removeOptionalRuleSet(T ruleset) throws McException
    {
        this.checkModifications();
        if (!this.isOptional(ruleset)) return null;
        this.optionalRuleSets.remove(ruleset);
        final Q result = this.ruleSets.remove(ruleset);
        this.removeListeners(result);
        return result;
    }
    
    /**
     * removes fixed rule set
     * @param ruleset
     * @return rule set instance
     * @throws McException
     */
    public Q removeFixedRuleSet(T ruleset) throws McException
    {
        this.checkModifications();
        if (!this.isFixed(ruleset)) return null;
        this.fixedRuleSets.remove(ruleset);
        final Q result = this.ruleSets.remove(ruleset);
        this.removeListeners(result);
        return result;
    }
    
}
