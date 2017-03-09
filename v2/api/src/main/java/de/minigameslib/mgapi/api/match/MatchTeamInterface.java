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

package de.minigameslib.mgapi.api.match;

import java.util.Set;
import java.util.UUID;

import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Interface representing a team in a match.
 * 
 * @author mepeisen
 *
 */
public interface MatchTeamInterface
{

    /**
     * @return the teamId
     */
    TeamIdType getTeamId();

    /**
     * Returns a read only list of team members.
     * @return the teamMembers (player uuids)
     */
    Set<UUID> getMembers();
    
    /**
     * Statistic function
     * @param statistic
     * @return current statistic
     */
    int getStatistic(MatchStatisticId statistic);
    
    /**
     * Statistic function
     * @param statistic
     * @param newValue
     */
    void setStatistic(MatchStatisticId statistic, int newValue);
    
    /**
     * Adds the match statistic for given statistic id.
     * @param statistic
     * @param amount delta value
     * @return new value
     */
    int addStatistic(MatchStatisticId statistic, int amount);
    
    /**
     * Decrements the match statistic for given statistic id.
     * @param statistic
     * @param amount delta value
     * @return new value
     */
    int decStatistic(MatchStatisticId statistic, int amount);
    
}
