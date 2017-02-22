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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;
import de.minigameslib.mgapi.impl.arena.ArenaData.TeamData;

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
     * the current results.
     * The first entry represents the first place.
     */
    private final List<MatchResultImpl> results = new ArrayList<>();    
    
    /**
     * the position of the first loser.
     */
    private int firstLoser;
    
    /**
     * flag for team match
     */
    private boolean teamMatch;

    /**
     * Constructor
     * @param teamMatch flag for team matches
     */
    public ArenaMatchImpl(boolean teamMatch)
    {
        this.getOrCreate(CommonTeams.Unknown);
        this.getOrCreate(CommonTeams.Spectators);
        this.getOrCreate(CommonTeams.Losers);
        this.getOrCreate(CommonTeams.Winners);
        this.teamMatch = teamMatch;
    }

    /**
     * @param team
     */
    public void createTeam(TeamData team)
    {
        this.getOrCreate(team.getId());
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
        final Optional<MatchTeam> team = this.teams.values().stream()
            .filter(t -> !t.getTeamId().isSpecial())
            .min((t1, t2) -> Integer.compare(t1.getTeamMembers().size(), t2.getTeamMembers().size()));
        return team.isPresent() ? team.get().getTeamId() : null;
    }

    @Override
    public void join(ArenaPlayerInterface player, TeamIdType team) throws McException
    {
        this.getOrCreate(player.getPlayerUUID()).setTeam(team);
    }

    @Override
    public boolean isTeamMatch()
    {
        return this.teamMatch;
    }

    @Override
    public TeamIdType getTeam(UUID uuid)
    {
        final MatchPlayer player = this.players.get(uuid);
        if (player != null)
        {
            return player.getTeam();
        }
        return null;
    }

    @Override
    public Collection<UUID> getParticipants()
    {
        return this.players.keySet();
    }

    @Override
    public Collection<UUID> getWinners()
    {
        return this.teams.get(CommonTeams.Winners).getTeamMembers();
    }

    @Override
    public Collection<UUID> getLosers()
    {
        return this.teams.get(CommonTeams.Losers).getTeamMembers();
    }

    @Override
    public Collection<MatchResult> getResults()
    {
        return new ArrayList<>(this.results);
    }

    @Override
    public int getResultCount()
    {
        return this.results.size();
    }

    @Override
    public MatchResult getResult(int place)
    {
        int pos = place - 1;
        return this.results.size() >= pos ? null : this.results.get(pos);
    }

    @Override
    public ComponentIdInterface getSpawn(UUID uuid)
    {
        final MatchPlayer player = this.players.get(uuid);
        return player == null ? null : player.getSpawn();
    }
    
    /**
     * Get or creates a match player.
     * @param uuid
     * @return match player.
     */
    private MatchPlayer getOrCreate(UUID uuid)
    {
        return this.players.computeIfAbsent(uuid, MatchPlayer::new);
    }

    @Override
    public void selectSpawn(UUID player, ComponentIdInterface spawn) throws McException
    {
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            p.setSpawn(spawn);
        }
    }

    @Override
    public int getStatistic(UUID player, MatchStatisticId statistic)
    {
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            return p.getStatistic(statistic);
        }
        return 0;
    }
    
    /**
     * Get or creates a match player.
     * @param team
     * @return match player.
     */
    private MatchTeam getOrCreate(TeamIdType team)
    {
        return this.teams.computeIfAbsent(team, MatchTeam::new);
    }

    @Override
    public int getStatistic(TeamIdType team, MatchStatisticId statistic)
    {
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            return p.getStatistic(statistic);
        }
        return 0;
    }

    @Override
    public void setStatistic(UUID player, MatchStatisticId statistic, int value)
    {
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            p.setStatistic(statistic, value);
        }
    }

    @Override
    public void setStatistic(TeamIdType team, MatchStatisticId statistic, int value)
    {
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            p.setStatistic(statistic, value);
        }
    }

    @Override
    public void addStatistic(UUID player, MatchStatisticId statistic, int amount)
    {
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            p.addStatistic(statistic, amount);
        }
    }

    @Override
    public void addStatistic(TeamIdType team, MatchStatisticId statistic, int amount)
    {
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            p.addStatistic(statistic, amount);
        }
    }

    @Override
    public void decStatistic(UUID player, MatchStatisticId statistic, int amount)
    {
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            p.decStatistic(statistic, amount);
        }
    }

    @Override
    public void decStatistic(TeamIdType team, MatchStatisticId statistic, int amount)
    {
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            p.decStatistic(statistic, amount);
        }
    }

    @Override
    public long getPlayTime(UUID player)
    {
        if (this.started != null)
        {
            return 0;
        }
        final MatchPlayer p = this.players.get(player);
        if (player != null)
        {
            if (p.getLeft() != null)
            {
                return this.started.until(p.getLeft(), ChronoUnit.SECONDS);
            }
            return this.started.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        }
        return 0;
    }

    @Override
    public KillerTracking getKillerTracking(UUID player)
    {
        return this.players.get(player);
    }

    @Override
    public void trackDamageForKill(UUID targetPlayer, UUID damager) throws McException
    {
        final MatchPlayer p = this.players.get(targetPlayer);
        if (p != null)
        {
            p.setKillerTracking(damager);
        }
    }

    @Override
    public void setLoser(UUID... players) throws McException
    {
        if (players != null && players.length > 0)
        {
            final MatchTeam team = this.teams.get(CommonTeams.Losers);
            for (final UUID uuid : players)
            {
                final MatchPlayer p = this.players.get(uuid);
                if (p != null)
                {
                    p.setTeam(CommonTeams.Losers);
                    p.setLeft(LocalDateTime.now());
                    team.getTeamMembers().add(uuid);
                }
            }
            final MatchResultImpl result = new MatchResultImpl(false, players);
            this.results.add(this.firstLoser, result);
            for (int i = this.firstLoser; i < this.results.size(); i++)
            {
                this.results.get(i).setPlace(i + 1);
            }
        }
    }

    @Override
    public void setWinner(UUID... players) throws McException
    {
        if (players != null && players.length > 0)
        {
            final MatchTeam team = this.teams.get(CommonTeams.Winners);
            for (final UUID uuid : players)
            {
                final MatchPlayer p = this.players.get(uuid);
                if (p != null)
                {
                    p.setTeam(CommonTeams.Winners);
                    p.setLeft(LocalDateTime.now());
                    team.getTeamMembers().add(uuid);
                }
            }
            final MatchResultImpl result = new MatchResultImpl(true, players);
            this.results.add(this.firstLoser, result);
            for (int i = this.firstLoser; i < this.results.size(); i++)
            {
                this.results.get(i).setPlace(i + 1);
            }
            this.firstLoser++;
        }
    }

    @Override
    public void setLoser(TeamIdType... teams) throws McException
    {
        if (teams != null && teams.length > 0)
        {
            final Set<UUID> playerSet = new HashSet<>();
            for (final TeamIdType team : teams)
            {
                final MatchTeam t = this.teams.get(team);
                if (t != null)
                {
                    playerSet.addAll(t.getTeamMembers());
                }
            }
            if (playerSet.size() > 0)
            {
                final MatchTeam team = this.teams.get(CommonTeams.Losers);
                for (final UUID uuid : playerSet)
                {
                    final MatchPlayer p = this.players.get(uuid);
                    if (p != null)
                    {
                        p.setTeam(CommonTeams.Losers);
                        p.setLeft(LocalDateTime.now());
                        team.getTeamMembers().add(uuid);
                    }
                }
                final MatchResultImpl result = new MatchResultImpl(false, playerSet.toArray(new UUID[playerSet.size()]));
                this.results.add(this.firstLoser, result);
                for (int i = this.firstLoser; i < this.results.size(); i++)
                {
                    this.results.get(i).setPlace(i + 1);
                }
            }
        }
    }

    @Override
    public void setWinner(TeamIdType... teams) throws McException
    {
        if (teams != null && teams.length > 0)
        {
            final Set<UUID> playerSet = new HashSet<>();
            for (final TeamIdType team : teams)
            {
                final MatchTeam t = this.teams.get(team);
                if (t != null)
                {
                    playerSet.addAll(t.getTeamMembers());
                }
            }
            if (playerSet.size() > 0)
            {
                final MatchTeam team = this.teams.get(CommonTeams.Winners);
                for (final UUID uuid : playerSet)
                {
                    final MatchPlayer p = this.players.get(uuid);
                    if (p != null)
                    {
                        p.setTeam(CommonTeams.Winners);
                        p.setLeft(LocalDateTime.now());
                        team.getTeamMembers().add(uuid);
                    }
                }
                final MatchResultImpl result = new MatchResultImpl(true, playerSet.toArray(new UUID[playerSet.size()]));
                this.results.add(this.firstLoser, result);
                for (int i = this.firstLoser; i < this.results.size(); i++)
                {
                    this.results.get(i).setPlace(i + 1);
                }
                this.firstLoser++;
            }
        }
    }
    
    /**
     * Implementation of match results.
     */
    private final static class MatchResultImpl implements MatchResult
    {
        
        /** the place this result represents. */
        private int place;
        
        /** winning flag. */
        private final boolean isWin;
        
        /** players on this place. */
        private final List<UUID> players = new ArrayList<>();

        /**
         * @param isWin
         * @param uuids
         */
        public MatchResultImpl(boolean isWin, UUID... uuids)
        {
            this.isWin = isWin;
            for (final UUID player : uuids)
            {
                this.players.add(player);
            }
        }

        /**
         * @param place the place to set
         */
        public void setPlace(int place)
        {
            this.place = place;
        }

        @Override
        public int getPlace()
        {
            return this.place;
        }

        @Override
        public Collection<UUID> getPlayers()
        {
            return this.players;
        }

        @Override
        public boolean isWin()
        {
            return this.isWin;
        }
        
    }
    
}
