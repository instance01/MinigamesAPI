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

package de.minigameslib.mgapi.api.arena;

import java.util.Collection;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;

/**
 * An interface for arena types.
 * 
 * @author mepeisen
 */
public interface ArenaTypeProvider
{
    
    /**
     * Returns a display name for the minigame.
     * @return minigame display name.
     */
    LocalizedMessageInterface getDisplayName();
    
    /**
     * Returns a short single-line description of the minigame
     * @return short single-line description
     */
    LocalizedMessageInterface getShortDescription();
    
    /**
     * Returns a multi-line description of the minigame
     * @return multi-line description
     */
    LocalizedMessageInterface getDescription();
    
    /**
     * Returns the fixed arena rule set types.
     * @return fixed arena rule sets.
     */
    Collection<ArenaRuleSetType> getFixedArenaRules();
    
    /**
     * Returns optional arena rule sets
     * @return optional arena rule sets.
     */
    Collection<ArenaRuleSetType> getOptionalArenaRules();
    
    /**
     * Creates a configuration with defaults for given rule set
     * @param ruleSet
     * @throws McException thrown for problems.
     */
    void configure(ArenaRuleSetInterface ruleSet) throws McException;
    
}
