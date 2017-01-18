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

package de.minigameslib.mgapi.impl;

import de.minigameslib.mclib.api.config.ConfigComment;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationStringList;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;

/**
 * Common configuration
 * 
 * @author mepeisen
 */
@ConfigurationValues(path = "core")
public enum MglibConfig implements ConfigurationValueInterface
{
 
    /**
     * Debug flag.
     */
    @ConfigurationBool(defaultValue = false)
    @ConfigComment({
        "Set this to true to enable some debugging output within minigames.",
        "This can be useful to find problems and errors.",
        "But be aware that debugging slows down the system."
        })
    Debug,
    
    /**
     * Arena names.
     */
    @ConfigurationStringList
    @ConfigComment({
        "Contains the internal/ technical names of arenas. Each arena is listed here.",
        "The arena core data is stored within arenas configuration folder."
    })
    Arenas,
    
}
