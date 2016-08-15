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

import org.bukkit.configuration.ConfigurationSection;

/**
 * An object that can be configured through configuration options.
 * 
 * @author mepeisen
 */
public interface Configurable
{
    
    /**
     * Reads given object from config.
     * 
     * @param section
     *            configuration section to read from.
     */
    void readFromConfig(ConfigurationSection section);
    
    /**
     * Writes given object to config.
     * 
     * @param section
     *            configuration section to write to.
     */
    void writeToConfig(ConfigurationSection section);
    
}
