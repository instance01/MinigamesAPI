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
import de.minigameslib.mclib.api.config.ConfigurationEnum;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * Basic pvp configurations
 * 
 * @author mepeisen
 * 
 * @see BasicZoneRuleSets#PvPMode
 */
@ConfigurationValues(path = "core")
public enum BasicPvpModeConfig implements ConfigurationValueInterface
{
    
    /**
     * The pvp option
     */
    @ConfigurationEnum(clazz = PvpModes.class)
    @ConfigComment({"The pvp option"})
    PvpOption;
    
    /**
     * Enumeration for type of pvp modes
     */
    public enum PvpModes implements EnumerationValue
    {
        /**
         * No Pvp at all
         */
        NoPvp,
        
        /**
         * Pvp only during match but no damage
         */
        PvpDuringMatch,
        
        /**
         * Normal pvp with damage
         */
        Normal,
    }
    
}
