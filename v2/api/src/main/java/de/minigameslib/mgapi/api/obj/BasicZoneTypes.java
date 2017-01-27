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

package de.minigameslib.mgapi.api.obj;

import de.minigameslib.mclib.api.objects.ZoneTypeId;

/**
 * The minigames arena default zone types.
 * 
 * @author mepeisen
 */
public enum BasicZoneTypes implements ZoneTypeId
{
    
    /**
     * Main zone for an arena.
     * @see MainZoneHandler
     */
    Main,

    /**
     * Battle zone (where the action goes on).
     * @see BattleZoneHandler
     */
    Battle,
    
    /**
     * Join zone to automatically join an arena if possible.
     * @see JoinZoneHandler 
     */
    Join,
    
    /**
     * Leave zone to automatically leave an arena.
     * @see LeaveZoneHandler
     */
    Leave,
    
    /**
     * Lobby zone where players can wait for matches.
     * @see LobbyZoneHandler
     */
    Lobby,
    
    /**
     * Zone allowed for spectators
     * @see SpectatorZoneHandler
     */
    Spectator,
    
    /**
     * Some empty zone that can be used with new rule sets for minigame developers.
     * @see EmptyZoneHandler
     */
    Empty,
    
    /**
     * A special generic zone used by administrators with individual rule sets.
     * @see GenericZoneHandler
     */
    Generic,
    
}
