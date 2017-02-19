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

import de.minigameslib.mclib.api.perms.Permission;
import de.minigameslib.mclib.api.perms.Permissions;
import de.minigameslib.mclib.api.perms.PermissionsInterface;

/**
 * Permissions for minigames library
 * 
 * @author mepeisen
 */
@Permissions("mg2")
public enum MglibPerms implements PermissionsInterface
{
    
    /**
     * Permission for info command.
     */
    @Permission("command.info")
    CommandInfo,
    
    /**
     * Permission for arenas command.
     */
    @Permission("command.arenas")
    CommandArenas,
    
    /**
     * Permission for arena command.
     */
    @Permission("command.arena")
    CommandArena,
    
    /**
     * Permission for join command.
     */
    @Permission("command.join")
    CommandJoin,
    
    /**
     * Permission for spectate command.
     */
    @Permission("command.spectate")
    CommandSpectate,
    
    /**
     * Permission for manual command.
     */
    @Permission("command.manual")
    CommandManual,
    
    /**
     * Permission for leave command.
     */
    @Permission("command.leave")
    CommandLeave,
    
    /**
     * Permission for admin command.
     */
    @Permission("command.admin")
    CommandAdmin,
    
    /**
     * Permission for admin create command.
     */
    @Permission("command.admin.create")
    CommandAdminCreate,
    
    /**
     * Permission for admin delete command.
     */
    @Permission("command.admin.delete")
    CommandAdminDelete,
    
    /**
     * Permission for admin enable command.
     */
    @Permission("command.admin.enable")
    CommandAdminEnable,
    
    /**
     * Permission for admin disable command.
     */
    @Permission("command.admin.disable")
    CommandAdminDisable,
    
    /**
     * Permission for admin check command.
     */
    @Permission("command.admin.check")
    CommandAdminCheck,
    
    /**
     * Permission for admin maintain command.
     */
    @Permission("command.admin.maintain")
    CommandAdminMaintain,
    
    /**
     * Permission for admin start command.
     */
    @Permission("command.admin.start")
    CommandAdminStart,
    
    /**
     * Permission for admin stop command.
     */
    @Permission("command.admin.stop")
    CommandAdminStop,
    
    /**
     * Permission for admin test command.
     */
    @Permission("command.admin.test")
    CommandAdminTest,
    
    /**
     * Permission for admin invite command.
     */
    @Permission("command.admin.invite")
    CommandAdminInvite,
    
    /**
     * Permission for admin signs.
     */
    @Permission("command.admin.sign")
    CommandAdminSign,
    
    /**
     * Permission for admin zones.
     */
    @Permission("command.admin.zone")
    CommandAdminZone,
    
    /**
     * Permission for admin components.
     */
    @Permission("command.admin.comp")
    CommandAdminComp,
    
    /**
     * Permission for admin gui command.
     */
    @Permission("command.admin.gui")
    CommandAdminGui,
    
    /**
     * Permission for admin sgui command.
     */
    @Permission("command.admin.sgui")
    CommandAdminSGui
    
}
