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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Arena Match implementation
 * 
 * @author mepeisen
 */
public class ArenaMatchImpl implements ArenaMatchInterface
{
    
    /** creation timestamp. */
    private final LocalDateTime created = LocalDateTime.now();
    
    /** match start timestamp. */
    private LocalDateTime started;
    
    /** match finish timestamp. */
    private LocalDateTime finished;
    
    /** aborted flag. */
    private boolean aborted;
    
    /** players. */
    private Map<UUID, MatchPlayer> players = new HashMap<>();
    
    /** teams. */
    private Map<TeamIdType, MatchTeam> teams = new HashMap<>();

    /**
     * Constructor
     */
    public ArenaMatchImpl()
    {
        this.teams.put(CommonTeams.Unknown, new MatchTeam(CommonTeams.Unknown));
        this.teams.put(CommonTeams.Spectators, new MatchTeam(CommonTeams.Spectators));
        this.teams.put(CommonTeams.Losers, new MatchTeam(CommonTeams.Losers));
        this.teams.put(CommonTeams.Winners, new MatchTeam(CommonTeams.Winners));
    }

    @Override
    public LocalDateTime getCreated()
    {
        return this.created;
    }

    @Override
    public LocalDateTime getStarted()
    {
        return this.started;
    }

    @Override
    public LocalDateTime getFinished()
    {
        return this.finished;
    }

    @Override
    public boolean isAborted()
    {
        return this.aborted;
    }
    
    /**
     * Starts this match
     */
    public void start()
    {
        this.started = LocalDateTime.now();
    }
    
    /**
     * Fnishes this match (regular finish)
     */
    public void finish()
    {
        this.finished = LocalDateTime.now();
    }
    
    /**
     * Aborts this match
     */
    public void abort()
    {
        this.finished = LocalDateTime.now();
        this.aborted = true;
    }

    @Override
    public Collection<UUID> getTeamMembers(TeamIdType team)
    {
        return this.teams.computeIfAbsent(team, MatchTeam::new).getTeamMembers();
    }

    @Override
    public Collection<TeamIdType> getTeams()
    {
        return this.teams.keySet();
    }

    @Override
    public TeamIdType getPreferredTeam()
    {
        // TODO implement team mode
        return null;
    }

    @Override
    public void join(ArenaPlayerInterface player, TeamIdType team) throws McException
    {
        // TODO implement joins
    }

    @Override
    public boolean isTeamMatch()
    {
        // TODO implement team mode
        return false;
    }

    @Override
    public TeamIdType getTeam(UUID uuid)
    {
        // TODO implement team mode
        return null;
    }

    @Override
    public Collection<UUID> getParticipants()
    {
        // TODO implement participants
        return null;
    }

    @Override
    public Collection<UUID> getWinners()
    {
        // TODO implement winners/losers
        return null;
    }

    @Override
    public Collection<UUID> getLosers()
    {
        // TODO implement winners/losers
        return null;
    }

    @Override
    public Collection<MatchResult> getResults()
    {
        // TODO implement match results
        return null;
    }

    @Override
    public int getResultCount()
    {
        // TODO implement match results
        return 0;
    }

    @Override
    public MatchResult getResult(int place)
    {
        // TODO implement match results
        return null;
    }

    @Override
    public ComponentIdInterface getSpawn(UUID uuid)
    {
        // TODO implement spawn selector
        return null;
    }

    @Override
    public void selectSpawn(UUID player, ComponentIdInterface spawn) throws McException
    {
        // TODO implement spawn selector
    }

    @Override
    public int getStatistic(UUID player, MatchStatisticId statistic)
    {
        // TODO implement statistics
        return 0;
    }

    @Override
    public int getStatistic(TeamIdType team, MatchStatisticId statistic)
    {
        // TODO implement statistics
        return 0;
    }

    @Override
    public void setStatistic(UUID player, MatchStatisticId statistic, int value)
    {
        // TODO implement statistics
    }

    @Override
    public void setStatistic(TeamIdType team, MatchStatisticId statistic, int value)
    {
        // TODO implement statistics
    }

    @Override
    public void addStatistic(UUID player, MatchStatisticId statistic, int amount)
    {
        // TODO implement statistics
    }

    @Override
    public void addStatistic(TeamIdType team, MatchStatisticId statistic, int amount)
    {
        // TODO implement statistics
    }

    @Override
    public int getPlayTime(UUID player)
    {
        // TODO implement played time
        return 0;
    }

    @Override
    public KillerTracking getKillerTracking(UUID player)
    {
        // TODO implement killer tracker
        return null;
    }

    @Override
    public void trackDamageForKill(UUID targetPlayer, UUID damager) throws McException
    {
        // TODO implement dmg tracker
    }

    @Override
    public void setLoser(UUID... players) throws McException
    {
        // TODO implement setting losers
    }

    @Override
    public void setWinner(UUID... players) throws McException
    {
        // TODO implement setting winners
    }

    @Override
    public void setLoser(TeamIdType... teams) throws McException
    {
        // TODO implement setting losers
    }

    @Override
    public void setWinner(TeamIdType... teams) throws McException
    {
        // TODO implement setting winners
    }
    
}
