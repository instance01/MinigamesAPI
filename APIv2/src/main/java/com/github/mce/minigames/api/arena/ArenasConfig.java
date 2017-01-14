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

import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationObject;
import de.minigameslib.mclib.api.config.ConfigurationString;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;

/**
 * The common arenas config file.
 * 
 * @author mepeisen
 */
@ConfigurationValues(path = "arenas.$CTX:ARENA:internalName$", file = "arenas.yml")
public enum ArenasConfig implements ConfigurationValueInterface
{
    
    /**
     * The arena display name.
     */
    @ConfigurationString(defaultValue = "")
    DisplayName,
    
    /**
     * The arena type.
     */
    @ConfigurationString(defaultValue = "")
    ArenaType,
    
    /**
     * Is the arena enabled?
     */
    @ConfigurationBool(defaultValue = false)
    Enabled,
    
    /**
     * Is the arena in maintenance?
     */
    @ConfigurationBool(defaultValue = false)
    Maintenance,
    
    /**
     * Author information of this arena.
     */
    @ConfigurationString(defaultValue = "")
    Author,
    
    /**
     * Short description of this arena.
     */
    @ConfigurationObject(clazz = LocalizedConfigString.class)
    ShortDescription,
    
    /**
     * Long (multi line) description of this arena.
     */
    @ConfigurationObject(clazz = LocalizedConfigLine.class)
    Description
    
}
