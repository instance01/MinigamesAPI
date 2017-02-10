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

package de.minigameslib.mgapi.impl.arena;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Data class for teams being present in a match
 * 
 * @author mepeisen
 *
 */
class MatchTeam
{
    
    /** the team id. */
    private final TeamIdType teamId;
    
    /** registered team members. */
    private final Set<UUID> teamMembers = new HashSet<>();

    /**
     * Constructor
     * @param teamId
     */
    public MatchTeam(TeamIdType teamId)
    {
        this.teamId = teamId;
    }

    /**
     * @return the teamId
     */
    public TeamIdType getTeamId()
    {
        return this.teamId;
    }

    /**
     * @return the teamMembers
     */
    public Set<UUID> getTeamMembers()
    {
        return this.teamMembers;
    }
    
}
