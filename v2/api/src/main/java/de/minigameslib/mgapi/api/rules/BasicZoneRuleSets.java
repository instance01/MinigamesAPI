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

/**
 * Basic zone rule sets
 * 
 * @author mepeisen
 */
public enum BasicZoneRuleSets implements ZoneRuleSetType
{
    
    /**
     * No free spawn of mobs; only forced spawns in maintenance mode or from minigames code 
     */
    NoWorldMobs,
    
    /**
     * No free spawn of pets; only forced spawns in maintenance mode or from minigames code 
     */
    NoWorldPets,
    
    /**
     * mobs are not allowed to target players inside the zone 
     */
    NoMobTargets,
    
    /**
     * Players are not allowed to enter the zone from outside except they join the arena  
     */
    PlayerNoEntry,
    
    /**
     * Leaving the zone causes to automatically lose the game
     */
    LoseOnLeave,
    
    /**
     * Leaving the zone causes to automatically die; can be used for games where players have multiple lives.
     */
    DieOnLeave,
    
    /**
     * Pvp-Rules
     * @see PvPModeRuleInterface
     */
    @RuleSetConfigurable(config = BasicPvpModeConfig.class)
    PvPMode,
    
}
