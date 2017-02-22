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

import de.minigameslib.mclib.api.objects.ComponentTypeId;

/**
 * The arenas default component types.
 * 
 * @author mepeisen
 */
public enum BasicComponentTypes implements ComponentTypeId
{
    
    /**
     * A generic spawn block.
     * @see SpawnComponentHandler
     */
    Spawn,
    
    /**
     * A generic spawn block used for spectators.
     * @see SpawnComponentHandler
     */
    SpectatorSpawn,
    
    /**
     * A generic spawn block used for join phase.
     * @see JoinSpawnComponentHandler
     */
    JoinSpawn,
    
    /**
     * A generic spawn block used after match.
     * @see MainLobbySpawnComponentHandler
     */
    MainLobbySpawn,
    
    /**
     * Some empty component that can be used with new rule sets for minigame developers.
     * @see EmptyComponentHandler
     */
    Empty,
    
    /**
     * A special generic component used by administrators with individual rule sets.
     * @see GenericComponentHandler
     */
    Generic,
    
}
