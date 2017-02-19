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

/**
 * Common arena state.
 * 
 * @author mepeisen
 */
public enum ArenaState
{
    
    /**
     * The arena is booting. This is only an internal state during server startup.
     */
    Booting,
    
    /**
     * The arena is disabled. Cannot be used.
     */
    Disabled,
    
    /**
     * The arena is starting. Arena is checked. This is the common state if an arena
     * is enabled or maintenance mode is stopped. Will be the first state after
     * server restart.
     */
    Starting,
    
    /**
     * Players are ready to join.
     */
    Join,
    
    /**
     * A match is prepared, there are enough players.
     */
    PreMatch,
    
    /**
     * A match is running.
     */
    Match,
    
    /**
     * There is a winner; match is stopped
     */
    PostMatch,
    
    /**
     * The arena is restarting and will be switching to Join as soon as the reset is finished.
     */
    Restarting,
    
    /**
     * The arena is under maintenance. Administrators may change the arena.
     */
    Maintenance,
    
}
