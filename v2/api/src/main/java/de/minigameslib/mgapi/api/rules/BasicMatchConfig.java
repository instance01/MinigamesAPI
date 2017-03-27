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

import de.minigameslib.mclib.api.config.ConfigComment;
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;

/**
 * Basic match configurations
 * 
 * @author mepeisen
 * 
 * @see BasicArenaRuleSets#BasicMatch
 */
@ConfigurationValues(path = "core")
public enum BasicMatchConfig implements ConfigurationValueInterface
{
    
    /**
     * The minimum players to start a match
     */
    @ConfigurationInt(defaultValue = 2)
    @ConfigComment({"The minimum players to start a match"})
    MinPlayers,
    
    /**
     * The maximum players inside an arena
     */
    @ConfigurationInt(defaultValue = 10)
    @ConfigComment({"The maximum players allowed per match"})
    MaxPlayers,
    
    /**
     * The seconds for lobby count down before match starts
     */
    @ConfigurationInt(defaultValue = 30)
    @ConfigComment({"The seconds for lobby count down before match starts"})
    LobbyCountdown
    
}
