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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * A pending arena match
 * 
 * @author mepeisen
 */
public interface ArenaMatchInterface
{
    
    // match common data
    
    /**
     * Returns the match created timestamp
     * @return match creation timestamp; timestamp the first player joined
     */
    LocalDateTime getCreated();
    
    /**
     * Returns the match started timestamp
     * @return match started timestamp; {@code null} for non-started matches.
     */
    LocalDateTime getStarted();
    
    /**
     * Returns the match finished timestamp
     * @return match finished timestamp; {@code null} if the match did not finish
     */
    LocalDateTime getFinished();
    
    /**
     * Checks if match was aborted by admins or server crash
     * @return {@code true} if match was aborted by admins or server crash
     */
    boolean isAborted();
    
    /**
     * Returns the match player for given player uuid
     * @param uuid
     * @return player or {@code null} if player is not registered in match
     */
    MatchPlayerInterface get(UUID uuid);
    
    // teams
    
    /**
     * Returns members of given team.
     * @param team
     * @return team members.
     */
    Collection<UUID> getTeamMembers(TeamIdType team);
    
    /**
     * Returns the available teams. Will return {@link CommonTeams#Unknown} for single player arenas.
     * Will never return {@link CommonTeams#Winners}, {@link CommonTeams#Losers} or {@code CommonTeams#Spectators} because
     * these are very special teams not representing an "ingame" team.
     * @return list of available teams.
     */
    Collection<TeamIdType> getTeams();
    
    /**
     * Returns a preferred team for new players. The preferred team is chosen for new joining players. 
     * @return the preferred team to join a new user
     */
    TeamIdType getPreferredTeam();
    
    /**
     * Let the given user join the match; on team matches the best team will be selected
     * @param player
     * @throws McException thrown if the current match is already finished
     */
    void join(ArenaPlayerInterface player) throws McException;
    
    /**
     * Let the given user join the given team; does NOT remove it from previous/current team; will remove from UNKNOWN team
     * @param player
     * @param team the team to join; must not be used with special teams
     * @throws McException thrown if the current match is not a team match or if match is already finished
     */
    void join(ArenaPlayerInterface player, TeamIdType team) throws McException;
    
    /**
     * Let the given user leave the given team; will join the UNKNOWN team if the player leaves the last team
     * @param player
     * @param team the team to join; must not be used with special teams
     * @throws McException thrown if the current match is not a team match or if match is already finished
     */
    void leave(ArenaPlayerInterface player, TeamIdType team) throws McException;
    
    /**
     * Let the given user join the given team; removes from previous/current team
     * @param player
     * @param team the team to join; must not be used with special teams
     * @throws McException thrown if the current match is not a team match or if match is already finished
     */
    void switchTeam(ArenaPlayerInterface player, TeamIdType team) throws McException;
    
    /**
     * Let the user join spectators. NOTE: Does not mark the player as winner or loser. If the player wins or loses you should
     * invoke the methods setWinner or setLoser instead.
     * @param player
     * @throws McException thrown if the match is already finished
     */
    void spectate(ArenaPlayerInterface player) throws McException;
    
    /**
     * Let the user leave the match. Player will automatically use when playing and leaving during match
     * @param player
     * @throws McException thrown if the match is already finished
     */
    void leave(ArenaPlayerInterface player) throws McException;
    
    /**
     * Returns {@code true} if this match is a team match
     * @return {@code true} for team matches; {@code false} for single player matches
     */
    boolean isTeamMatch();
    
    /**
     * Returns the team of given player. Notes on special teams:
     * <ul>
     * <li>Will only return {@link CommonTeams#Spectators} if player did only spectate the game and did not participate as a player.</li>
     * <li>Will return {@link CommonTeams#Unknown} if player is in waiting lobby and a team was not chosen or this is a single player match.</li>
     * <li>Will return {@link CommonTeams#Winners} if player is already marked as a winner.</li>
     * <li>Will return {@link CommonTeams#Losers} if player is already marked as a loser.</li>
     * <li>Will return {@link CommonTeams#Unknown} if the match is pending and the player did not win or lose.</li>
     * </ul>
     * @param uuid player uuid
     * @return team or {@code null} if player is not involved in this match
     */
    TeamIdType getTeam(UUID uuid);
    
    /**
     * Return the match team interface for given team
     * @param team
     * @return team or {@code null} if team is not registered in match
     */
    MatchTeamInterface get(TeamIdType team);
    
    /**
     * Get or creates a match player.
     * @param team
     * @return match player.
     * @throws McException thrown if the current match is not a team match or if match is already finished
     */
    MatchTeamInterface getOrCreate(TeamIdType team) throws McException;
    
    // results
    
    /**
     * Returns the match participants/ active players
     * @return match participants/ active players; player uuids
     */
    Collection<UUID> getPlayers();
    
    /**
     * Returns the number of match participants/ active players
     * @return match participants count
     */
    int getPlayerCount();
    
    /**
     * Returns the current spectators
     * @return spectators
     */
    Collection<UUID> getSpectators();
    
    /**
     * Returns the number of current spectators
     * @return match spectators count
     */
    int getSpectatorCount();
    
    /**
     * Returns the match participants (players having played or spectated the game).
     * This method includes players already marked as winners or losers.
     * @param returnSpectators {@code true} to return spectators not being active within the game, {@code false} to filter and only return players being active
     * @return match participants; player uuids
     */
    Collection<UUID> getParticipants(boolean returnSpectators);
    
    /**
     * Returns the number of remaining players
     * @param returnSpectators {@code true} to return spectators not being active within the game, {@code false} to filter and only return players being active
     * @return match participants count
     */
    int getParticipantCount(boolean returnSpectators);
    
    /**
     * Returns the winners
     * @return match winners; player uuids
     */
    Collection<UUID> getWinners();
    
    /**
     * Returns the number of match winners
     * @return match winner count
     */
    int getWinnerCount();
    
    /**
     * Returns the match losers
     * @return mosers; player uuids
     */
    Collection<UUID> getLosers();
    
    /**
     * Returns the number of match loser players
     * @return match loser count
     */
    int getLoserCount();
    
    /**
     * Returns the match results, first entry is the "first place".
     * @return match results.
     */
    Collection<MatchResult> getResults();
    
    /**
     * Returns the result count
     * @return count of match results or places
     */
    int getResultCount();
    
    /**
     * Returns the match results for given place
     * @param place the place starting with 1 for the best winner
     * @return match result or {@code null} if place number is invalid
     */
    MatchResult getResult(int place);
    
    /**
     * Match result interface.
     * 
     * Each invocation of {@link ArenaMatchInterface#setWinner(UUID...)} or {@link ArenaMatchInterface#setLoser(UUID...)} will create a new place
     * and match result. The first winner will be on place #1 followed by later on winners and the first loser will be on last place preceded by
     * later losers.
     */
    interface MatchResult
    {
        
        /**
         * Returns the place number
         * @return the place starting with 1 for the best winner
         */
        int getPlace();
        
        /**
         * Players sharing this place
         * @return the players sharing this place
         */
        Collection<UUID> getPlayers();
        
        /**
         * Returns {@code true} for a winning place
         * @return {@code true} for winning place.
         */
        boolean isWin();
        
    }
    
    // spawns
    
    /**
     * Returns the spawn for given player. Only works on pending games.
     * @param uuid
     * @return player spawn.
     */
    ComponentIdInterface getSpawn(UUID uuid);
    
    /**
     * Selects the spawn for given player.
     * @param player
     * @param spawn
     * @throws McException thrown if match is not pending.
     */
    void selectSpawn(UUID player, ComponentIdInterface spawn) throws McException;
    
    // match statistics
    
    /**
     * Returns the match statistic for given player and statistic id.
     * @param player
     * @param statistic
     * @return statistics number
     */
    int getStatistic(UUID player, MatchStatisticId statistic);
    
    /**
     * Returns the match statistic for given team and statistic id.
     * @param team
     * @param statistic
     * @return statistics number
     */
    int getStatistic(TeamIdType team, MatchStatisticId statistic);
    
    /**
     * Changes the match statistic for given player and statistic id.
     * @param player
     * @param statistic
     * @param value the new statistic value
     * @throws McException thrown if match is not pending.
     */
    void setStatistic(UUID player, MatchStatisticId statistic, int value) throws McException;
    
    /**
     * Changes the match statistic for given team and statistic id.
     * @param team
     * @param statistic
     * @param value the new statistic value
     * @throws McException thrown if the current match is not a team match or if match is not pending.
     */
    void setStatistic(TeamIdType team, MatchStatisticId statistic, int value) throws McException;
    
    /**
     * Adds the match statistic for given player and statistic id.
     * @param player
     * @param statistic
     * @param amount delta value
     * @return the new statistic value
     * @throws McException thrown if match is not pending.
     */
    int addStatistic(UUID player, MatchStatisticId statistic, int amount) throws McException;
    
    /**
     * Adds the match statistic for given team and statistic id.
     * @param team
     * @param statistic
     * @param amount delta value
     * @return the new statistic value
     * @throws McException thrown if the current match is not a team match or if match is not pending.
     */
    int addStatistic(TeamIdType team, MatchStatisticId statistic, int amount) throws McException;
    
    /**
     * Decrement the match statistic for given player and statistic id.
     * @param player
     * @param statistic
     * @param amount delta value
     * @return the new statistic value
     * @throws McException thrown if match is not pending.
     */
    int decStatistic(UUID player, MatchStatisticId statistic, int amount) throws McException;
    
    /**
     * Decrement the match statistic for given team and statistic id.
     * @param team
     * @param statistic
     * @param amount delta value
     * @return the new statistic value
     * @throws McException thrown if the current match is not a team match or if match is not pending.
     */
    int decStatistic(TeamIdType team, MatchStatisticId statistic, int amount) throws McException;
    
    /**
     * Returns the play time of given player in milli seconds
     * @param player
     * @return play time in milli seconds
     */
    long getPlayTime(UUID player);
    
    // killer tracking
    
    /**
     * Interface for tracking the last damager
     */
    interface KillerTracking
    {
        /**
         * UUId of the player that was damaged or killed
         * @return player that was killed
         */
        UUID getPlayer();
        
        /**
         * The last damager
         * @return last damager
         */
        UUID getLastDamager();
        
        /**
         * The timestamp the last damage was recorded
         * @return last damage
         */
        LocalDateTime getDamageTimestamp();
    }
    
    /**
     * Returns the killer tracking for given target player; only works on pending matches
     * @param player
     * @return killer tracking or {@code null} if player is not registered in this arena match
     */
    KillerTracking getKillerTracking(UUID player);
    
    /**
     * Resets the killer tracking for given target player
     * @param player
     * @throws McException thrown if match is not pending.
     */
    void resetKillerTracking(UUID player) throws McException;
    
    /**
     * Tracks last damage for killer detection
     * @param targetPlayer the player that was hit
     * @param damager the last damager
     * @throws McException thrown if match is not pending.
     */
    void trackDamageForKill(UUID targetPlayer, UUID damager) throws McException;
    
    // winning and losing
    
    /**
     * Marks one or more players for losing the game
     * @param players
     * @throws McException thrown if match is not pending.
     */
    void setLoser(UUID... players) throws McException;
    
    /**
     * Marks one or more players for winning the game
     * @param players
     * @throws McException thrown if match is not pending.
     */
    void setWinner(UUID... players) throws McException;
    
    /**
     * Marks one or more teams for losing the game
     * @param teams
     * @throws McException thrown if match is not pending.
     */
    void setLoser(TeamIdType... teams) throws McException;
    
    /**
     * Marks one or more teams for winning the game
     * @param teams
     * @throws McException thrown if match is not pending.
     */
    void setWinner(TeamIdType... teams) throws McException;
    
}
