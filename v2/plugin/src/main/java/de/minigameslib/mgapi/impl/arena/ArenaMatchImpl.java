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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaLoseEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedTeamEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftTeamEvent;
import de.minigameslib.mgapi.api.events.ArenaWinEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchPlayerInterface;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.match.MatchTeamInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;
import de.minigameslib.mgapi.impl.arena.ArenaData.TeamData;
import de.minigameslib.mgapi.impl.arena.ArenaImpl.Messages;

/**
 * Arena Match implementation
 * 
 * @author mepeisen
 */
public class ArenaMatchImpl implements ArenaMatchInterface
{
    
    // TODO fire bukkit events for statistics
    
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
     * the associated arena
     */
    private ArenaInterface arena;

    /**
     * Constructor
     * @param arena the associated arena
     * @param teamMatch flag for team matches
     */
    public ArenaMatchImpl(ArenaInterface arena, boolean teamMatch)
    {
        this.teams.computeIfAbsent(CommonTeams.Unknown, MatchTeam::new);
        this.teams.computeIfAbsent(CommonTeams.Spectators, MatchTeam::new);
        this.teams.computeIfAbsent(CommonTeams.Losers, MatchTeam::new);
        this.teams.computeIfAbsent(CommonTeams.Winners, MatchTeam::new);
        this.teamMatch = teamMatch;
        this.arena = arena;
    }

    /**
     * @return the associated arena
     */
    private ArenaInterface getArena()
    {
        return this.arena;
    }

    /**
     * @param team
     */
    void createTeam(TeamData team)
    {
        this.teams.computeIfAbsent(team.getId(), MatchTeam::new);
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
     * @throws McException thrown if match was already started
     */
    public void start() throws McException
    {
        if (this.started != null)
        {
            throw new McException(ArenaImpl.Messages.StartWrongState, this.arena.getDisplayName());
        }
        this.started = LocalDateTime.now();
    }
    
    /**
     * Fnishes this match (regular finish)
     * @throws McException thrown if match is not pending
     */
    public void finish() throws McException
    {
        checkMatchPending();
        this.finished = LocalDateTime.now();
    }

    /**
     * @throws McException thrown if match is not pending
     */
    private void checkMatchPending() throws McException
    {
        this.checkMatchStarted();
        this.checkMatchNotFinished();
    }

    /**
     * @throws McException thrown if match is not finsihed
     */
    private void checkMatchNotFinished() throws McException
    {
        if (this.finished != null)
        {
            throw new McException(ArenaImpl.Messages.InvalidModificationAfterFinish, this.arena.getDisplayName());
        }
    }

    /**
     * @throws McException thrown if match is started
     */
    private void checkMatchStarted() throws McException
    {
        if (this.started == null)
        {
            throw new McException(ArenaImpl.Messages.InvalidModificationBeforeStart, this.arena.getDisplayName());
        }
    }

    /**
     * @throws McException thrown if match is not a team match
     */
    private void checkTeamMatch() throws McException
    {
        if (!this.teamMatch)
        {
            throw new McException(ArenaImpl.Messages.InvalidTeamActionOnSinglePlayerMatch, this.arena.getDisplayName());
        }
    }
    
    /**
     * Aborts this match
     * @throws McException thrown if match is not pending
     */
    public void abort() throws McException
    {
        checkMatchPending();
        this.finished = LocalDateTime.now();
        this.aborted = true;
    }

    @Override
    public Collection<UUID> getTeamMembers(TeamIdType team)
    {
        final MatchTeamInterface t = this.get(team);
        return t == null ? Collections.emptyList() : t.getMembers();
    }

    @Override
    public Collection<TeamIdType> getTeams()
    {
        return this.teams.keySet().stream().filter(t -> !t.isSpecial()).collect(Collectors.toList());
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
    public void spectate(ArenaPlayerInterface player) throws McException
    {
        checkMatchNotFinished();
        
        if (player.inArena() && player.getArena() != this.getArena())
        {
            throw new McException(Messages.AlreadyInArena, player.getArena().getDisplayName());
        }
        ((ArenaPlayerImpl)player).switchArenaOrMode(this.getArena().getInternalName(), false);
        
        final MatchPlayer mplayer = this.players.computeIfAbsent(player.getPlayerUUID(), MatchPlayer::new);
        
        if (mplayer.getTeam() == null)
        {
            // player never played, only spectating
            mplayer.setTeam(CommonTeams.Spectators);
            mplayer.setLeft(mplayer.getJoined());
            mplayer.setPlaying(false);
            mplayer.setSpec(true);
            
            final ArenaPlayerJoinSpectatorsEvent joinEvent = new ArenaPlayerJoinSpectatorsEvent(this.getArena(), player, false);
            Bukkit.getPluginManager().callEvent(joinEvent);
            if (joinEvent.isCancelled())
            {
                this.players.remove(player.getPlayerUUID());
                throw new McException(joinEvent.getVetoReason(), joinEvent.getVetoReasonArgs());
            }
        }
        else
        {
            // player was playing within the match
            mplayer.setLeft(LocalDateTime.now());
            mplayer.setPlaying(false);
            mplayer.setSpec(true);
            
            final ArenaPlayerJoinSpectatorsEvent joinEvent = new ArenaPlayerJoinSpectatorsEvent(this.getArena(), player, true);
            Bukkit.getPluginManager().callEvent(joinEvent);
            if (joinEvent.isCancelled())
            {
                throw new McException(joinEvent.getVetoReason(), joinEvent.getVetoReasonArgs());
            }
        }
        this.teams.get(CommonTeams.Spectators).getTeamMembers().add(player.getPlayerUUID());
        
        final ArenaPlayerJoinedSpectatorsEvent joinEvent = new ArenaPlayerJoinedSpectatorsEvent(this.getArena(), player, mplayer.getTeam() != null);
        Bukkit.getPluginManager().callEvent(joinEvent);
    }

    @Override
    public void leave(ArenaPlayerInterface player) throws McException
    {
        checkMatchNotFinished();
        
        if (player.getArena() != this.getArena())
        {
            throw new McException(Messages.CannotLeaveNotInArena, this.getArena().getDisplayName());
        }
        ((ArenaPlayerImpl)player).switchArenaOrMode(null, false);
        
        final MatchPlayer mplayer = this.players.get(player.getPlayerUUID());
        if (mplayer != null)
        {
            if (mplayer.isPlaying() && this.started != null)
            {
                // match was started, mark player as loser
                final MatchTeam losers = this.teams.get(CommonTeams.Losers);

                mplayer.setTeam(CommonTeams.Losers);
                mplayer.setLeft(LocalDateTime.now());
                losers.getTeamMembers().add(player.getPlayerUUID());
                
                final MatchResultImpl result = new MatchResultImpl(false, new UUID[]{player.getPlayerUUID()});
                this.results.add(this.firstLoser, result);
                for (int i = this.firstLoser; i < this.results.size(); i++)
                {
                    this.results.get(i).setPlace(i + 1);
                }
                mplayer.setSpec(false);
                mplayer.setPlaying(false);
                
                final ArenaPlayerLeftEvent leftEvent = new ArenaPlayerLeftEvent(this.getArena(), player);
                Bukkit.getPluginManager().callEvent(leftEvent);
                
                final ArenaLoseEvent loseEvent = new ArenaLoseEvent(this.getArena(), result);
                Bukkit.getPluginManager().callEvent(loseEvent);
            }
            else if (mplayer.getLeft() == null)
            {
                mplayer.setSpec(false);
                mplayer.setPlaying(false);
                mplayer.setLeft(LocalDateTime.now());
                
                if (mplayer.getTeam() == CommonTeams.Spectators)
                {
                    final ArenaPlayerLeftSpectatorsEvent leftEvent = new ArenaPlayerLeftSpectatorsEvent(this.getArena(), player);
                    Bukkit.getPluginManager().callEvent(leftEvent);
                }
                else
                {
                    final ArenaPlayerLeftEvent leftEvent = new ArenaPlayerLeftEvent(this.getArena(), player);
                    Bukkit.getPluginManager().callEvent(leftEvent);
                }
            }
        }
        else
        {
            // non playing users will leave silently
        }
    }

    @Override
    public void join(ArenaPlayerInterface player) throws McException
    {
        checkMatchNotFinished();
        
        if (player.inArena())
        {
            throw new McException(Messages.AlreadyInArena, player.getArena().getDisplayName());
        }
        ((ArenaPlayerImpl)player).switchArenaOrMode(this.getArena().getInternalName(), false);
        
        TeamIdType preTeam = this.isTeamMatch() ? this.getPreferredTeam() : CommonTeams.Unknown;
        if (preTeam == null) preTeam = CommonTeams.Unknown;
        
        final MatchPlayer mplayer = this.players.computeIfAbsent(player.getPlayerUUID(), MatchPlayer::new);
        if (mplayer.getTeam() != null && mplayer.getTeam() != CommonTeams.Spectators)
        {
            throw new McException(ArenaImpl.Messages.CannotRejoin, this.arena.getDisplayName());
        }
        
        final ArenaPlayerJoinEvent joinEvent = new ArenaPlayerJoinEvent(this.getArena(), player, preTeam);
        Bukkit.getPluginManager().callEvent(joinEvent);
        if (joinEvent.isCancelled())
        {
            this.players.remove(player.getPlayerUUID());
            throw new McException(joinEvent.getVetoReason(), joinEvent.getVetoReasonArgs());
        }
        mplayer.setPlaying(true);
        
        if (mplayer.isSpec())
        {
            this.teams.get(CommonTeams.Spectators).getTeamMembers().remove(player.getPlayerUUID());
        }

        if (this.isTeamMatch())
        {
            if (joinEvent.getPreSelectedTeam() == null || joinEvent.getPreSelectedTeam().isSpecial())
            {
                mplayer.setTeam(preTeam);
            }
            else
            {
                mplayer.setTeam(joinEvent.getPreSelectedTeam());
            }
        }
        else
        {
            mplayer.setTeam(CommonTeams.Unknown);
        }
        
        this.teams.computeIfAbsent(mplayer.getTeam(), MatchTeam::new).getTeamMembers().add(player.getPlayerUUID());
        
        final ArenaPlayerJoinedEvent joinedEvent = new ArenaPlayerJoinedEvent(this.getArena(), player);
        Bukkit.getPluginManager().callEvent(joinedEvent);

        final ArenaPlayerJoinedTeamEvent join2Event = new ArenaPlayerJoinedTeamEvent(this.getArena(), player, mplayer.getTeam());
        Bukkit.getPluginManager().callEvent(join2Event);
    }

    @Override
    public void join(ArenaPlayerInterface player, TeamIdType team) throws McException
    {
        if (!this.players.containsKey(player.getPlayerUUID()))
        {
            this.join(player);
            if (team != this.getTeam(player.getPlayerUUID()))
            {
                this.switchTeam(player, team);
            }
            return;
        }
        checkMatchNotFinished();
        checkTeamMatch();
        if (team.isSpecial())
        {
            throw new McException(ArenaImpl.Messages.InvalidJoinAction, this.arena.getDisplayName());
        }
        
        final MatchPlayer mplayer = this.players.get(player.getPlayerUUID());
        if (mplayer.getTeam() == team || mplayer.getAdditionalTeams().contains(team))
        {
            // already joined. do nothing
            return;
        }
        if (mplayer.isSpec())
        {
            // remove from spectators
            this.teams.get(CommonTeams.Spectators).getTeamMembers().remove(player.getPlayerUUID());
            mplayer.setSpec(false);
        }
        mplayer.setPlaying(true);
        mplayer.setTeam(team);
        this.teams.computeIfAbsent(team, MatchTeam::new).getTeamMembers().add(player.getPlayerUUID());
        
        final ArenaPlayerJoinedTeamEvent joinEvent = new ArenaPlayerJoinedTeamEvent(this.getArena(), player, team);
        Bukkit.getPluginManager().callEvent(joinEvent);
    }

    @Override
    public void leave(ArenaPlayerInterface player, TeamIdType team) throws McException
    {
        if (!this.teams.containsKey(player.getPlayerUUID()))
        {
            throw new McException(ArenaImpl.Messages.CannotLeaveNotInArena, this.arena.getDisplayName());
        }
        checkMatchNotFinished();
        checkTeamMatch();
        if (team.isSpecial())
        {
            throw new McException(ArenaImpl.Messages.InvalidLeaveAction, this.arena.getDisplayName());
        }
        
        final MatchPlayer mplayer = this.players.get(player.getPlayerUUID());
        mplayer.getAdditionalTeams().remove(team); // remove from additional teams on demand
        this.teams.get(team).getTeamMembers().remove(player.getPlayerUUID());
        if (mplayer.getTeam() == team)
        {
            // removed from primary team
            if (mplayer.getAdditionalTeams().isEmpty())
            {
                // switch to unkown
                this.teams.get(CommonTeams.Unknown).getTeamMembers().add(player.getPlayerUUID());
                mplayer.setTeam(CommonTeams.Unknown);
            }
            else
            {
                // switch to first additional team
                mplayer.setTeam(mplayer.getAdditionalTeams().iterator().next());
                mplayer.getAdditionalTeams().remove(mplayer.getTeam());
            }
        }
        
        if (mplayer.isSpec())
        {
            // remove from spectators
            this.teams.get(CommonTeams.Spectators).getTeamMembers().remove(player.getPlayerUUID());
            mplayer.setSpec(false);
        }
        
        final ArenaPlayerLeftTeamEvent leftEvent = new ArenaPlayerLeftTeamEvent(this.getArena(), player, team);
        Bukkit.getPluginManager().callEvent(leftEvent);
    }

    @Override
    public void switchTeam(ArenaPlayerInterface player, TeamIdType team) throws McException
    {
        checkMatchNotFinished();
        checkTeamMatch();
        if (team.isSpecial())
        {
            throw new McException(ArenaImpl.Messages.InvalidTeamSwitch, this.arena.getDisplayName());
        }
        final MatchPlayer mplayer = this.players.get(player.getPlayerUUID());
        if (mplayer == null)
        {
            throw new McException(ArenaImpl.Messages.InvalidTeamSwitch, this.arena.getDisplayName());
        }
        else if (mplayer.getTeam() == CommonTeams.Winners || mplayer.getTeam() == CommonTeams.Losers)
        {
            throw new McException(ArenaImpl.Messages.InvalidTeamSwitch, this.arena.getDisplayName());
        }
        else
        {
            final ArenaPlayerLeftTeamEvent leftEvent = new ArenaPlayerLeftTeamEvent(this.getArena(), player, mplayer.getTeam());
            Bukkit.getPluginManager().callEvent(leftEvent);
            
            this.teams.get(mplayer.getTeam()).getTeamMembers().remove(player.getPlayerUUID());
            mplayer.setPlaying(true);
            mplayer.setTeam(team);
            this.teams.computeIfAbsent(team, MatchTeam::new).getTeamMembers().add(player.getPlayerUUID());
            
            final ArenaPlayerJoinedTeamEvent joinEvent = new ArenaPlayerJoinedTeamEvent(this.getArena(), player, team);
            Bukkit.getPluginManager().callEvent(joinEvent);
        }
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
    public Collection<UUID> getParticipants(boolean returnSpectators)
    {
        if (returnSpectators)
        {
            return this.players.keySet();
        }
        return this.players.entrySet().stream()
                .filter(p -> p.getValue().getTeam() != CommonTeams.Spectators)
                .map(p -> p.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public int getParticipantCount(boolean returnSpectators)
    {
        if (returnSpectators)
        {
            return this.players.size();
        }
        return (int) this.players.values().stream()
                .filter(p -> p.getTeam() != CommonTeams.Spectators)
                .count();
    }

    @Override
    public Collection<UUID> getPlayers()
    {
        return this.players.entrySet().stream()
                .filter(p -> p.getValue().isPlaying())
                .filter(p -> p.getValue().getTeam() != CommonTeams.Spectators)
                .map(p -> p.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public int getPlayerCount()
    {
        return (int) this.players.entrySet().stream()
                .filter(p -> p.getValue().isPlaying())
                .filter(p -> p.getValue().getTeam() != CommonTeams.Spectators)
                .count();
    }

    @Override
    public Collection<UUID> getSpectators()
    {
        return this.players.entrySet().stream()
                .filter(p -> p.getValue().isSpec())
                .map(p -> p.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public int getSpectatorCount()
    {
        return (int) this.players.entrySet().stream()
                .filter(p -> p.getValue().isSpec())
                .count();
    }

    @Override
    public int getWinnerCount()
    {
        return this.teams.get(CommonTeams.Winners).getTeamMembers().size();
    }

    @Override
    public int getLoserCount()
    {
        return this.teams.get(CommonTeams.Losers).getTeamMembers().size();
    }

    @Override
    public Collection<UUID> getWinners()
    {
        return this.teams.get(CommonTeams.Winners).getMembers();
    }

    @Override
    public Collection<UUID> getLosers()
    {
        return this.teams.get(CommonTeams.Losers).getMembers();
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
        return pos < 0 || this.results.size() <= pos ? null : this.results.get(pos);
    }

    @Override
    public ComponentIdInterface getSpawn(UUID uuid)
    {
        final MatchPlayer player = this.players.get(uuid);
        return player == null ? null : player.getSpawn();
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

    @Override
    public MatchPlayerInterface get(UUID uuid)
    {
        return this.players.get(uuid);
    }

    @Override
    public MatchTeamInterface get(TeamIdType team)
    {
        return this.teams.get(team);
    }
    
    @Override
    public MatchTeamInterface getOrCreate(TeamIdType team) throws McException
    {
        this.checkMatchNotFinished();
        this.checkTeamMatch();
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
    public void setStatistic(UUID player, MatchStatisticId statistic, int value) throws McException
    {
        this.checkMatchNotFinished();
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            p.setStatistic(statistic, value);
        }
    }

    @Override
    public void setStatistic(TeamIdType team, MatchStatisticId statistic, int value) throws McException
    {
        this.checkMatchNotFinished();
        this.checkTeamMatch();
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            p.setStatistic(statistic, value);
        }
    }

    @Override
    public int addStatistic(UUID player, MatchStatisticId statistic, int amount) throws McException
    {
        this.checkMatchNotFinished();
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            return p.addStatistic(statistic, amount);
        }
        return 0;
    }

    @Override
    public int addStatistic(TeamIdType team, MatchStatisticId statistic, int amount) throws McException
    {
        this.checkMatchNotFinished();
        this.checkTeamMatch();
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            return p.addStatistic(statistic, amount);
        }
        return 0;
    }

    @Override
    public int decStatistic(UUID player, MatchStatisticId statistic, int amount) throws McException
    {
        this.checkMatchNotFinished();
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            return p.decStatistic(statistic, amount);
        }
        return 0;
    }

    @Override
    public int decStatistic(TeamIdType team, MatchStatisticId statistic, int amount) throws McException
    {
        this.checkMatchNotFinished();
        this.checkTeamMatch();
        final MatchTeam p = this.teams.get(team);
        if (p != null)
        {
            return p.decStatistic(statistic, amount);
        }
        return 0;
    }

    @Override
    public long getPlayTime(UUID player)
    {
        if (this.started == null)
        {
            return 0;
        }
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            LocalDateTime start = p.getJoined().isBefore(this.started) ? this.started : p.getJoined();
            long result = 0;
            if (p.getLeft() != null)
            {
                result = start.until(p.getLeft(), ChronoUnit.MILLIS);
            }
            else
            {
                result = start.until(LocalDateTime.now(), ChronoUnit.MILLIS);
            }
            return result < 0 ? 0 : result;
        }
        return 0;
    }

    @Override
    public KillerTracking getKillerTracking(UUID player)
    {
        return this.players.get(player);
    }

    @Override
    public void resetKillerTracking(UUID player) throws McException
    {
        this.checkMatchPending();
        final MatchPlayer p = this.players.get(player);
        if (p != null)
        {
            p.setKillerTracking(null);
        }
    }

    @Override
    public void trackDamageForKill(UUID targetPlayer, UUID damager) throws McException
    {
        this.checkMatchPending();
        final MatchPlayer p = this.players.get(targetPlayer);
        if (p != null)
        {
            p.setKillerTracking(damager);
        }
    }

    @Override
    public void setLoser(UUID... players) throws McException
    {
        this.checkMatchPending();
        if (players != null && players.length > 0)
        {
            final MatchTeam losers = this.teams.get(CommonTeams.Losers);
            final MatchTeam spectators = this.teams.get(CommonTeams.Spectators);
            for (final UUID uuid : players)
            {
                final MatchPlayer p = this.players.get(uuid);
                if (p != null)
                {
                    p.setTeam(CommonTeams.Losers);
                    // TODO Think about the following lines. If a player left before (f.e. a disconnect) this may override statistics
                    p.setLeft(LocalDateTime.now());
                    p.setSpec(true);
                    p.setPlaying(false);
                    losers.getTeamMembers().add(uuid);
                    spectators.getTeamMembers().add(uuid);
                }
            }
            final MatchResultImpl result = new MatchResultImpl(false, players);
            this.results.add(this.firstLoser, result);
            for (int i = this.firstLoser; i < this.results.size(); i++)
            {
                this.results.get(i).setPlace(i + 1);
            }
            final ArenaLoseEvent event = new ArenaLoseEvent(this.getArena(), result);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void setWinner(UUID... players) throws McException
    {
        this.checkMatchPending();
        if (players != null && players.length > 0)
        {
            final MatchTeam winners = this.teams.get(CommonTeams.Winners);
            final MatchTeam spectators = this.teams.get(CommonTeams.Spectators);
            for (final UUID uuid : players)
            {
                final MatchPlayer p = this.players.get(uuid);
                if (p != null)
                {
                    p.setTeam(CommonTeams.Winners);
                    // TODO Think about the following lines. If a player left before (f.e. a disconnect) this may override statistics
                    p.setLeft(LocalDateTime.now());
                    p.setSpec(true);
                    p.setPlaying(false);
                    winners.getTeamMembers().add(uuid);
                    spectators.getTeamMembers().add(uuid);
                }
            }
            final MatchResultImpl result = new MatchResultImpl(true, players);
            this.results.add(this.firstLoser, result);
            for (int i = this.firstLoser; i < this.results.size(); i++)
            {
                this.results.get(i).setPlace(i + 1);
            }
            this.firstLoser++;
            final ArenaWinEvent event = new ArenaWinEvent(this.getArena(), result);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void setLoser(TeamIdType... teams) throws McException
    {
        this.checkMatchPending();
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
                final MatchTeam losers = this.teams.get(CommonTeams.Losers);
                final MatchTeam spectators = this.teams.get(CommonTeams.Spectators);
                for (final UUID uuid : playerSet)
                {
                    final MatchPlayer p = this.players.get(uuid);
                    if (p != null)
                    {
                        p.setTeam(CommonTeams.Losers);
                        // TODO Think about the following lines. If a player left before (f.e. a disconnect) this may override statistics
                        p.setLeft(LocalDateTime.now());
                        p.setSpec(true);
                        p.setPlaying(false);
                        losers.getTeamMembers().add(uuid);
                        spectators.getTeamMembers().add(uuid);
                    }
                }
                final MatchResultImpl result = new MatchResultImpl(false, playerSet.toArray(new UUID[playerSet.size()]));
                this.results.add(this.firstLoser, result);
                for (int i = this.firstLoser; i < this.results.size(); i++)
                {
                    this.results.get(i).setPlace(i + 1);
                }
                final ArenaLoseEvent event = new ArenaLoseEvent(this.getArena(), result);
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    @Override
    public void setWinner(TeamIdType... teams) throws McException
    {
        this.checkMatchPending();
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
                final MatchTeam winners = this.teams.get(CommonTeams.Winners);
                final MatchTeam spectators = this.teams.get(CommonTeams.Spectators);
                for (final UUID uuid : playerSet)
                {
                    final MatchPlayer p = this.players.get(uuid);
                    if (p != null)
                    {
                        p.setTeam(CommonTeams.Winners);
                        // TODO Think about the following lines. If a player left before (f.e. a disconnect) this may override statistics
                        p.setLeft(LocalDateTime.now());
                        p.setSpec(true);
                        p.setPlaying(false);
                        winners.getTeamMembers().add(uuid);
                        spectators.getTeamMembers().add(uuid);
                    }
                }
                final MatchResultImpl result = new MatchResultImpl(true, playerSet.toArray(new UUID[playerSet.size()]));
                this.results.add(this.firstLoser, result);
                for (int i = this.firstLoser; i < this.results.size(); i++)
                {
                    this.results.get(i).setPlace(i + 1);
                }
                this.firstLoser++;
                final ArenaWinEvent event = new ArenaWinEvent(this.getArena(), result);
                Bukkit.getPluginManager().callEvent(event);
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
