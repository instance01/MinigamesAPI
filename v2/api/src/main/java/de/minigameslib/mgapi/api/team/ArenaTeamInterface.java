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

package de.minigameslib.mgapi.api.team;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mgapi.api.arena.ArenaInterface;

/**
 * Represents a team configured for an arena.
 * 
 * @author mepeisen
 */
public interface ArenaTeamInterface
{
    
    /**
     * Returns the underlying team id
     * @return team id
     */
    TeamIdType getId();
    
    /**
     * Returns the arena of this team
     * @return arena team
     */
    ArenaInterface getArena();
    
    /**
     * Returns the display name of the team
     * @return display name of the team
     */
    LocalizedConfigString getName();
    
    /**
     * Sets the team name
     * @param name
     * @throws McException thrown if arena is not in maintenance state
     */
    void setName(LocalizedConfigString name) throws McException;
    
}
