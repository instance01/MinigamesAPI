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

package com.github.mce.minigames.api.perms;

import de.minigameslib.mclib.api.perms.Permission;
import de.minigameslib.mclib.api.perms.Permissions;
import de.minigameslib.mclib.api.perms.PermissionsInterface;

/**
 * Common permissions within minigames lib.
 * 
 * @author mepeisen
 */
@Permissions("$PERM:MGLIB$")
public enum CommonPermissions implements PermissionsInterface
{
    
    /**
     * Permission to perform the start command.
     */
    @Permission("command.start")
    Start,
    
    /**
     * Permission to perform the info command.
     */
    @Permission("command.info.common")
    Info,
    
    /**
     * Permission to perform the info command.
     */
    @Permission("command.info.extensions")
    InfoExtensions,
    
    /**
     * Permission to perform the info command.
     */
    @Permission("command.info.minigames")
    InfoMinigames,
    
    /**
     * Permission to perform the info command.
     */
    @Permission("command.info.arenas")
    InfoArenas,
    
    /**
     * Permission to perform the create command.
     */
    @Permission("command.create")
    Create,
    
}
