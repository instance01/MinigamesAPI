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

package de.minigameslib.mgapi.api.arena;

import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.block.Sign;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicMatchRuleInterface;
import de.minigameslib.mgapi.api.rules.RuleSetContainerInterface;
import de.minigameslib.mgapi.api.team.ArenaTeamInterface;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Basic interface for arenas.
 * 
 * @author mepeisen
 */
public interface ArenaInterface extends RuleSetContainerInterface<ArenaRuleSetType, ArenaRuleSetInterface>
{
    
    /**
     * Returns the arena name.
     * 
     * @return internal arena name
     */
    String getInternalName();
    
    /**
     * Returns the arena type.
     * 
     * @return arena type.
     */
    ArenaTypeInterface getType();
    
    /**
     * Returns the display name of this arena
     * @return display name
     */
    LocalizedConfigString getDisplayName();
    
    /**
     * Returns a one line short description
     * @return short arena description
     */
    LocalizedConfigString getShortDescription();
    
    /**
     * Returns a multi-line description
     * @return description of the arena
     */
    LocalizedConfigLine getDescription();
    
    /**
     * Returns the how-to-play manual
     * @return arena manual
     */
    LocalizedConfigLine getManual();
    
    /**
     * Saves arena core data.
     * @throws McException thrown if arena is in wrong state or saving data failed.
     */
    void saveData() throws McException;
    
    /**
     * Returns the current arena state.
     * @return arena state.
     */
    ArenaState getState();
    
    /**
     * Let the given player leave the arena. Prints a text message to player on success.
     * @param player
     * @throws McException thrown if the player is not inside given arena
     */
    void leave(ArenaPlayerInterface player) throws McException;
    
    /**
     * Let the given player join the waiting lobby. Prints a text message to player on success.
     * @param player
     * @throws McException thrown if the arena is in any other state except {@link ArenaState#Join}
     */
    void join(ArenaPlayerInterface player) throws McException;
    
    /**
     * Let the given player join the spectation area. Prints a text message to player on success.
     * @param player
     * @throws McException thrown if the arena does not run games or is not in state {@link ArenaState#Join}
     */
    void spectate(ArenaPlayerInterface player) throws McException;
    
    /**
     * Sets enabled state; only allowed if arena is currently in states {@link ArenaState#Disabled} or {@link ArenaState#Maintenance}.
     * @throws McException thrown if arena is in invalid state
     */
    void setEnabledState() throws McException;

    /**
     * Sets arena to disabled state; only allowed in states {@link ArenaState#Starting}, {@link ArenaState#Join} or {@link ArenaState#Maintenance}.
     * If used in other situations the argument {@code force} must be set to {@code true}.
     * @param force forces arena disable; if a match is running all players are kicked and the match is aborted.
     * @throws McException thrown if arena is in invalid state or if config cannot be saved.
     */
    void setDisabledState(boolean force) throws McException;
    
    /**
     * Sets arena to maintenance state; only allowed in states {@link ArenaState#Starting}, {@link ArenaState#Join} or {@link ArenaState#Disabled}.
     * If there is a running match the arena will be switching to disabled mode as soon as the match ended.
     * @param force forces arena maintenance mode; if a match is running all players are kicked and the match is aborted.
     * @throws McException thrown if arena is in invalid state or if config cannot be saved.
     */
    void setMaintenance(boolean force) throws McException;
    
    /**
     * Checks if arena is in maintenance mode or is going to maintenance mode as soon as possible.
     * @return {@code true} if setMaintenance was called before or the arena is still in maintenance mode.
     */
    boolean isMaintenance();

    /**
     * Checks if arena is in disabled state mode or is going to disabled mode as soon as possible.
     * @return {@code true} if setDisabledState was called before or the arena is still in disabled mode.
     */
    boolean isDisabled();

    /**
     * Checks if arena is running a match.
     * @return {@code true} if arena is in one of the match states: PRE_MATCH, MATCH, POST_MATCH
     */
    boolean isMatch();

    /**
     * Checks if given player is playing a match or waiting for the main game (lobby)
     * @param player
     * @return {@code true} if player is playing
     */
    boolean isPlaying(McPlayerInterface player);

    /**
     * Checks if given player is spetating a match
     * @param player
     * @return {@code true} if player is spectating
     */
    boolean isSpectating(McPlayerInterface player);

    /**
     * Checks if given player is playing a match or waiting for the main game (lobby)
     * @param player
     * @return {@code true} if player is playing
     */
    boolean isPlaying(ArenaPlayerInterface player);

    /**
     * Checks if given player is spetating a match
     * @param player
     * @return {@code true} if player is spectating
     */
    boolean isSpectating(ArenaPlayerInterface player);

    /**
     * Checks for administration failures.
     * @return list of arena failures.
     */
    Collection<CheckFailure> check();

    /**
     * The minigame of the arena.
     * @return arena minigame; may be null for invalid arenas.
     */
    MinigameInterface getMinigame();

    /**
     * Delete arena
     * @throws McException thrown if arena could not be deleted or if arena is not in maintenance state
     */
    void delete() throws McException;

    /**
     * Starts a match (without countdown); will be invoked on game rules, f.e.{@link BasicMatchRuleInterface}
     * @throws McException thrown if arena is not in join state or if there is no player. 
     */
    void start() throws McException;

    /**
     * Forces a match start even if match rules may not allow this; will request the gaming rules to start countdown
     * @throws McException thrown if arena is not in JOIN state
     */
    void forceStart() throws McException;

    /**
     * Finish a match regularly
     * @throws McException thrown if arena is not in match state 
     */
    void finish() throws McException;

    /**
     * Abort a match
     * @throws McException thrown if arena is not in match state 
     */
    void abort() throws McException;

    /**
     * Sets test mode for a maintained arena; ensures that arena gets to maintenance mode one the match was finished.
     * @throws McException thrown if arena is not in maintenance state.
     */
    void setTestState() throws McException;

    /**
     * Returns the players being in the arena (excluding spectators)
     * @return player count
     */
    int getPlayerCount();

    /**
     * Returns the players playing the match
     * @return player count
     */
    int getActivePlayerCount();

    /**
     * Returns the spectators being in the arena
     * @return spectators count
     */
    int getSpectatorCount();

    /**
     * Returns the players being within waiting lobby or within match
     * @return current player list
     */
    Collection<ArenaPlayerInterface> getPlayers();

    /**
     * Returns the players playing the match
     * @return current player list
     */
    Collection<ArenaPlayerInterface> getActivePlayers();

    /**
     * Returns the spectators
     * @return spectators list
     */
    Collection<ArenaPlayerInterface> getSpectators();
    
    /**
     * Returns the internal object representing the arena.
     * @return arena object
     */
    ObjectInterface getObject();
    
    /**
     * Returns a logger to log arena messages
     * @return logger
     */
    Logger getLogger();
    
    // match
    
    /**
     * Returns the current arena match
     * @return arena match or {@code null} if arena is not in JOIN or MATCH state.
     */
    ArenaMatchInterface getCurrentMatch();
    
    // TODO historic matches
    
    // team support

    /**
     * Sets the single player mode (removes all teams)
     * @throws McException thrown if arena is not in maintenance state
     */
    void setSinglePlayerMode() throws McException;
    
    /**
     * Adds team with given name
     * @param team
     * @param name the team name to be used for display
     * @throws McException thrown if arena is not in maintenance state; thrown for special teams
     * @see TeamIdType#isSpecial()
     */
    void addTeam(TeamIdType team, LocalizedConfigString name) throws McException;
    
    /**
     * Removes given team
     * @param team
     * @throws McException thrown if arena is not in maintenance state
     */
    void removeTeam(TeamIdType team) throws McException;
    
    /**
     * Returns all preconfigured teams.
     * @return all preconfigured teams; empty list for single player arenas
     */
    Collection<TeamIdType> getTeams();
    
    /**
     * Returns the arena team object for given team id
     * @param team
     * @return arena team object or {@code null} if team is not configured via {@link #addTeam(TeamIdType, LocalizedConfigString)}
     */
    ArenaTeamInterface getTeam(TeamIdType team);
    
    // components and objects
        
    /**
     * Returns the components used in this arena
     * 
     * @return Components of this arena.
     */
    Collection<ComponentIdInterface> getComponents();
    
    /**
     * Returns the zones used in this arena
     * 
     * @return Zones of this arena.
     */
    Collection<ZoneIdInterface> getZones();
    
    /**
     * Returns the signs used in this arena
     * 
     * @return Signs of this arena.
     */
    Collection<SignIdInterface> getSigns();
    
    /**
     * Returns the entities used in this arena
     * 
     * @return entities of this arena.
     */
    Collection<EntityIdInterface> getEntities();
    
    /**
     * Returns the components used in this arena
     * 
     * @param types component types for filtering
     * @return Components of this arena.
     */
    Collection<ComponentIdInterface> getComponents(ComponentTypeId... types);
    
    /**
     * Returns the zones used in this arena
     * 
     * @param types zone types for filtering
     * @return Zones of this arena.
     */
    Collection<ZoneIdInterface> getZones(ZoneTypeId... types);
    
    /**
     * Returns the signs used in this arena
     * 
     * @param types sign types for filtering
     * @return Signs of this arena.
     */
    Collection<SignIdInterface> getSigns(SignTypeId... types);
    
    /**
     * Returns the entities used in this arena
     * 
     * @param types entity types for filtering
     * @return entities of this arena.
     */
    Collection<EntityIdInterface> getEntities(EntityTypeId... types);
    
    /**
     * Returns handler for given id.
     * @param id
     * @return handler for given id
     */
    <T extends ArenaComponentHandler> T getHandler(ComponentIdInterface id);
    
    /**
     * Returns handler for given id.
     * @param id
     * @return handler for given id
     */
    <T extends ArenaSignHandler> T getHandler(SignIdInterface id);
    
    /**
     * Returns handler for given id.
     * @param id
     * @return handler for given id
     */
    <T extends ArenaZoneHandler> T getHandler(ZoneIdInterface id);
    
    /**
     * Creates a new component.
     * 
     * @param location
     *            the initial location of the component.
     * @param type component type
     * @return created component
     * @throws McException
     *             thrown if the component could not be created
     */
    <T extends ArenaComponentHandler> T createComponent(Location location, ComponentTypeId type) throws McException;
    
    /**
     * Creates a new sign.
     * 
     * @param sign
     *            the initial bukkit sign
     * @param type sign type
     * @return created sign
     * @throws McException
     *             thrown if the sign could not be created
     */
    <T extends ArenaSignHandler> T createSign(Sign sign, SignTypeId type) throws McException;
    
    /**
     * Creates a new zone.
     * 
     * @param cuboid
     *            the initial cuboid of the zone.
     * @param type the zone type
     * @return created zone
     * @throws McException
     *             thrown if the zone could not be created
     */
    <T extends ArenaZoneHandler> T createZone(Cuboid cuboid, ZoneTypeId type) throws McException;

    /**
     * Checks if arena can be modified.
     * @throws McException thrown if arena is in invalid state for modifications
     */
    void checkModifications() throws McException;
    
    // TODO Entity support
//    /**
//     * Creates a new entity.
//     * 
//     * @param entity
//     *            the initial bukkit entity
//     * @param fixedRuleSets the fixed rule sets to apply to this entity
//     * @return created entity
//     * @throws McException
//     *             thrown if the entity could not be created
//     */
//    EntityInterface createEntity(Entity entity, EntityRuleSetType... fixedRuleSets) throws McException;

}
