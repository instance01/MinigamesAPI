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

package com.github.mce.minigames.api.config;

import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationString;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;

/**
 * Common permissions within minigames lib.
 * 
 * @author mepeisen
 */
@ConfigurationValues(path = "config")
public enum CommonConfig implements ConfigurationValueInterface
{
    
    // command options
    
    /**
     * Is party command enabled?
     */
    @ConfigurationBool(name = "party_command_enabled", defaultValue = true)
    PartyCommandEnabled,
    
    // permission prefix
    
    /**
     * Prefix for core permissions.
     */
    @ConfigurationString(name = "permissions_prefix", defaultValue = "ancient.core")
    PermissionPrefix,
    
    /**
     * Prefix for core permissions.
     */
    @ConfigurationString(name = "permissions_kits_prefix", defaultValue = "ancient.core.kits")
    PermissionKitsPrefix,
    
    /**
     * Prefix for core permissions.
     */
    @ConfigurationString(name = "permissions_gun_prefix", defaultValue = "ancient.core.guns")
    PermissionGunsPrefix,
    
    /**
     * Prefix for core permissions.
     */
    @ConfigurationString(name = "permissions_shop_prefix", defaultValue = "ancient.core.shop")
    PermissionShopsPrefix,
    
    /**
     * Prefix for core permissions.
     */
    @ConfigurationString(name = "permissions_game_prefix", defaultValue = "ancient")
    PermissionGamesPrefix,
    
    // common options
    
    /**
     * Is debug mode enabled?
     */
    @ConfigurationBool(name = "debug", defaultValue = false)
    DebugEnabled,
    
}
