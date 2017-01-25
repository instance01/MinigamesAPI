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

package de.minigameslib.mgapi.api.rules;

import java.util.Collection;

import de.minigameslib.mclib.api.McException;

/**
 * Basic interface helper for classes owning rule sets.
 * 
 * @author mepeisen
 * @param <T> type of rule sets
 */
public interface RuleSetContainerInterface<T extends RuleSetType>
{
    
    /**
     * Returns the rule sets applied to this element.
     * The resulting collection contains fixed rule sets (not removable becuase the minigame code relies on it) or
     * optional rule sets.
     * @return applied rule sets
     */
    Collection<T> getAppliedRuleSets();
    
    /**
     * Returns the rule sets available to this element.
     * @return available rule sets
     */
    Collection<T> getAvailableRuleSets();
    
    /**
     * Checks if given rule set is fixed and thus cannot be removed
     * @param ruleset
     * @return true if given ruleset is fixed.
     */
    boolean isFixed(T ruleset);
    
    /**
     * Checks if given rule set is available
     * @param ruleset
     * @return true if given ruleset is not yet applied and if it is allowed to apply it to this element.
     */
    boolean isAvailable(T ruleset);
    
    /**
     * Adds rule sets to this element. The rule set can be removed later on.
     * @param rulesets new rule sets
     * @throws McException thrown if one of the rule sets already is applied,
     *     if the rule sets are illegal (not in available list),
     *     if the arena is not in maintainence mode
     *     or if the configuration cannot be saved.
     */
    void applyRuleSets(@SuppressWarnings("unchecked") T... rulesets) throws McException;
    
    /**
     * Removes rule sets from this element.
     * @param rulesets existing rule sets
     * @throws McException thrown if one of the rule sets is not applied,
     *     if the rule sets must not be removed (fixed rule set),
     *     if the arena is not in maintainence mode
     *     or if the configuration cannot be saved.
     */
    void removeRuleSets(@SuppressWarnings("unchecked") T... rulesets) throws McException;
    
}
