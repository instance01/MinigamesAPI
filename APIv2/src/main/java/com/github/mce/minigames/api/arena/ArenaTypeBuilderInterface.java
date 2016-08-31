/*
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

package com.github.mce.minigames.api.arena;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.arena.rules.ArenaRuleSet;

/**
 * A builder to create arena types.
 * 
 * <p>
 * Get an instance of this object via {@link MinigamePluginInterface#createArenaType(String, ArenaTypeInterface, boolean)}.
 * </p>
 * 
 * @author mepeisen
 */
public interface ArenaTypeBuilderInterface
{
    
    /**
     * Applies rule sets to this arena type.
     * 
     * @param set
     *            the arena rule sets.
     * @return this object for chaining.
     * @throws MinigameException
     *             thrown if the arena rule set was invalid.
     */
    ArenaTypeBuilderInterface applyRulesets(ArenaRuleSet... set) throws MinigameException;
    
    /**
     * Inherits all arena rules from given arena; this method must be called at first before manipulating the rule sets.
     * 
     * @param type
     * @return this object for chaining.
     * @throws MinigameException
     *             thrown if this method is called twice or too late.
     */
    ArenaTypeBuilderInterface inheritRules(ArenaTypeInterface type) throws MinigameException;
    
    /**
     * Returns the currently applied rule sets.
     * 
     * @return rule sets; changes (remove) will be reflected back to this builder
     * @throws MinigameException thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
     */
    Iterable<ArenaRuleSet> getRuleSets() throws MinigameException;
    
    /**
     * Removes rule sets from this arena type.
     * 
     * <p>
     * If this method is called with rule sets that are not applied to this type this method
     * silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
     * </p>
     * 
     * @param set
     *            the arena rule sets.
     * @return this object for chaining.
     * @throws MinigameException
     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
     */
    ArenaTypeBuilderInterface removeRulesets(ArenaRuleSet... set) throws MinigameException;
    
}
