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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * A spawn component handler.
 * 
 * @author mepeisen
 */
public interface SpawnComponentHandler extends ArenaComponentHandler
{
    
    /**
     * Returns the team using this spawn.
     * @return team using this spawn.
     */
    TeamIdType getTeam();
    
    /**
     * Sets the team that uses this spawn
     * @param team
     * @throws McException thrown if arena is not in maintenance mode or the data could not be saved.
     */
    void setTeam(TeamIdType team) throws McException;
    
    /**
     * Return an (optional) ordering for this spawn; used in arenas with fixed spawn positions. 
     * @return spawn ordering.
     */
    int getOrdering();
    
    /**
     * sets an (optional) ordering for this spawn; used in arenas with fixed spawn positions. 
     * @param order
     * @throws McException thrown if arena is not in maintenance mode or the data could not be saved.
     */
    void setOrdering(int order) throws McException;
    
}
