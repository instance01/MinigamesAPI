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
     * @return match started timestamp; timestamp the match itself was started
     */
    LocalDateTime getStarted();
    
    /**
     * Returns the match finished timestamp
     * @return match finished timestamp; {@code null} if the match did not finish
     */
    LocalDateTime getFinished();
    
    /**
     * Checks if match was aborted by admins or server crash
     * @return {@ode true} if match was aborted by admins or server crash
     */
    boolean isAborted();
    
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
     * Let the given user join the given team
     * @param player
     * @param team the team to join
     * @throws McException thrown if the current match is not a team match and if 
     */
    void join(ArenaPlayerInterface player, TeamIdType team) throws McException;
    
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
    
    // results
    
    /**
     * Returns the match participants
     * @return match participants; player uuids
     */
    Collection<UUID> getParticipants();
    
    /**
     * Returns the winners
     * @return match winners; player uuids
     */
    Collection<UUID> getWinners();
    
    /**
     * Returns the match losers
     * @return mosers; player uuids
     */
    Collection<UUID> getLosers();
    
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
     */
    void setStatistic(UUID player, MatchStatisticId statistic, int value);
    
    /**
     * Changes the match statistic for given team and statistic id.
     * @param team
     * @param statistic
     * @param value the new statistic value
     */
    void setStatistic(TeamIdType team, MatchStatisticId statistic, int value);
    
    /**
     * Adds the match statistic for given player and statistic id.
     * @param player
     * @param statistic
     * @param amount delta value; positive to increase statistics and negativ to decrease statistics
     */
    void addStatistic(UUID player, MatchStatisticId statistic, int amount);
    
    /**
     * Adds the match statistic for given team and statistic id.
     * @param team
     * @param statistic
     * @param amount delta value; positive to increase statistics and negativ to decrease statistics
     */
    void addStatistic(TeamIdType team, MatchStatisticId statistic, int amount);
    
    /**
     * Returns the play time of given player in seconds
     * @param player
     * @return play time in seconds
     */
    int getPlayTime(UUID player);
    
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
     * Returns the killer tracking; only works on pending matches
     * @param player
     * @return killer tracking or {@code null} if no killer tracking was registered for given player.
     */
    KillerTracking getKillerTracking(UUID player);
    
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
