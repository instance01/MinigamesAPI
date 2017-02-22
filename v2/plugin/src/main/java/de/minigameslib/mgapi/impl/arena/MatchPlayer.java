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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Data class for players being present in a match
 * 
 * @author mepeisen
 *
 */
class MatchPlayer implements ArenaMatchInterface.KillerTracking
{
    
    /** join timestamp */
    private LocalDateTime joined = LocalDateTime.now();
    
    /** the players uuid. */
    private final UUID playerUuid;
    
    /** left timestamp */
    private LocalDateTime left;
    
    /** the associated team. */
    private TeamIdType team = CommonTeams.Unknown;
    
    /**
     * the match statistics.
     */
    private final Map<MatchStatisticId, Integer> statistics = new HashMap<>();
    
    /**
     * the selected spawn.
     */
    private ComponentIdInterface spawn;

    /**
     * Last damager for killer tracking
     */
    private UUID lastDamager;

    /**
     * Last damage timestamp
     */
    private LocalDateTime damageTimestamp;

    /**
     * Constructor
     * @param playerUuid
     */
    public MatchPlayer(UUID playerUuid)
    {
        this.playerUuid = playerUuid;
    }

    /**
     * @return the joined
     */
    public LocalDateTime getJoined()
    {
        return this.joined;
    }

    /**
     * @param joined the joined to set
     */
    public void setJoined(LocalDateTime joined)
    {
        this.joined = joined;
    }

    /**
     * @return the left
     */
    public LocalDateTime getLeft()
    {
        return this.left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(LocalDateTime left)
    {
        this.left = left;
    }

    /**
     * @return the playerUuid
     */
    public UUID getPlayerUuid()
    {
        return this.playerUuid;
    }

    /**
     * @return the spawn
     */
    public ComponentIdInterface getSpawn()
    {
        return this.spawn;
    }

    /**
     * @param spawn the spawn to set
     */
    public void setSpawn(ComponentIdInterface spawn)
    {
        this.spawn = spawn;
    }

    /**
     * Statistic function
     * @param statistic
     * @return current statistic
     */
    public int getStatistic(MatchStatisticId statistic)
    {
        final Integer result = this.statistics.get(statistic);
        return result == null ? 0 : result.intValue();
    }
    
    /**
     * Statistic function
     * @param statistic
     * @param newValue
     */
    public void setStatistic(MatchStatisticId statistic, int newValue)
    {
        this.statistics.put(statistic, Integer.valueOf(newValue));
    }
    
    /**
     * Adds the match statistic for given statistic id.
     * @param statistic
     * @param amount delta value
     */
    void addStatistic(MatchStatisticId statistic, int amount)
    {
        this.setStatistic(statistic, this.getStatistic(statistic) + amount);
    }
    
    /**
     * Decrements the match statistic for given statistic id.
     * @param statistic
     * @param amount delta value
     */
    void decStatistic(MatchStatisticId statistic, int amount)
    {
        this.setStatistic(statistic, this.getStatistic(statistic) - amount);
    }

    @Override
    public UUID getPlayer()
    {
        return this.playerUuid;
    }

    @Override
    public UUID getLastDamager()
    {
        return this.lastDamager;
    }

    @Override
    public LocalDateTime getDamageTimestamp()
    {
        return this.damageTimestamp;
    }
    
    /**
     * Sets the killer tracking
     * @param damager
     */
    public void setKillerTracking(UUID damager)
    {
        this.lastDamager = damager;
        this.damageTimestamp = LocalDateTime.now();
    }

    /**
     * @return the team
     */
    public TeamIdType getTeam()
    {
        return this.team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(TeamIdType team)
    {
        this.team = team;
    }
    
}
