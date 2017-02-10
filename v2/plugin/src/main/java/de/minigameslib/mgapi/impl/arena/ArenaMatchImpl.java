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

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getPreferredTeam()
     */
    @Override
    public TeamIdType getPreferredTeam()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#join(de.minigameslib.mgapi.api.player.ArenaPlayerInterface, de.minigameslib.mgapi.api.team.TeamIdType)
     */
    @Override
    public void join(ArenaPlayerInterface player, TeamIdType team) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#isTeamMatch()
     */
    @Override
    public boolean isTeamMatch()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getTeam(java.util.UUID)
     */
    @Override
    public TeamIdType getTeam(UUID uuid)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getParticipants()
     */
    @Override
    public Collection<UUID> getParticipants()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getWinners()
     */
    @Override
    public Collection<UUID> getWinners()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getLosers()
     */
    @Override
    public Collection<UUID> getLosers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getResults()
     */
    @Override
    public Collection<MatchResult> getResults()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getResultCount()
     */
    @Override
    public int getResultCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getResult(int)
     */
    @Override
    public MatchResult getResult(int place)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getSpawn(java.util.UUID)
     */
    @Override
    public ComponentIdInterface getSpawn(UUID uuid)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#selectSpawn(java.util.UUID, de.minigameslib.mclib.api.objects.ComponentIdInterface)
     */
    @Override
    public void selectSpawn(UUID player, ComponentIdInterface spawn) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getStatistic(java.util.UUID, de.minigameslib.mgapi.api.match.MatchStatisticId)
     */
    @Override
    public int getStatistic(UUID player, MatchStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getStatistic(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.match.MatchStatisticId)
     */
    @Override
    public int getStatistic(TeamIdType team, MatchStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#setStatistic(java.util.UUID, de.minigameslib.mgapi.api.match.MatchStatisticId, int)
     */
    @Override
    public void setStatistic(UUID player, MatchStatisticId statistic, int value)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#setStatistic(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.match.MatchStatisticId, int)
     */
    @Override
    public void setStatistic(TeamIdType team, MatchStatisticId statistic, int value)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#addStatistic(java.util.UUID, de.minigameslib.mgapi.api.match.MatchStatisticId, int)
     */
    @Override
    public void addStatistic(UUID player, MatchStatisticId statistic, int amount)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#addStatistic(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.match.MatchStatisticId, int)
     */
    @Override
    public void addStatistic(TeamIdType team, MatchStatisticId statistic, int amount)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getPlayTime(java.util.UUID)
     */
    @Override
    public int getPlayTime(UUID player)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#getKillerTracking(java.util.UUID)
     */
    @Override
    public KillerTracking getKillerTracking(UUID player)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#trackDamageForKill(java.util.UUID, java.util.UUID)
     */
    @Override
    public void trackDamageForKill(UUID targetPlayer, UUID damager) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#setLoser(java.util.UUID[])
     */
    @Override
    public void setLoser(UUID... players) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#setWinner(java.util.UUID[])
     */
    @Override
    public void setWinner(UUID... players) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#setLoser(de.minigameslib.mgapi.api.team.TeamIdType[])
     */
    @Override
    public void setLoser(TeamIdType... teams) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.match.ArenaMatchInterface#setWinner(de.minigameslib.mgapi.api.team.TeamIdType[])
     */
    @Override
    public void setWinner(TeamIdType... teams) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
}
