/*
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

package com.github.mce.minigames.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.MatchPhaseId;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.services.ExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;
import com.github.mce.minigames.api.team.TeamId;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.impl.comp.ComponentId;
import de.minigameslib.mgapi.api.LibState;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeProvider;

/**
 * Base interface to access the minigames API.
 * 
 * @author mepeisen
 */
public interface MglibInterface
{
    
    // initialization
    
    /**
     * Registers a new extension.
     * 
     * @param extension
     *            minigame extension to register.
     * @return the minigame extension
     * @throws McException
     *             thrown if the minigame with given name is already registered.
     */
    MinigameExtensionInterface register(MinigameExtensionProviderInterface extension) throws McException;
    
    /**
     * Registers a new minigame; should be called in {@link JavaPlugin#onEnable()}.
     * 
     * <p>
     * It is possible to let a plugin register multiple minigames at once. Simply call this method with multiple instances of providers.
     * </p>
     * 
     * @param provider
     *            the plugin provider class.
     * @return The minigame plugin interface, some kind of administrational interface.
     * 
     * @throws McException
     *             thrown if the minigame with given name is already registered.
     */
    MinigamePluginInterface register(PluginProviderInterface provider) throws McException;
    
    // main api
    
    /**
     * Returns the arena type provider for given arena type.
     * 
     * @param type
     *            the arena type
     * @return type provider or {@code null} if it was not defined.
     */
    ArenaTypeProvider getProviderFromArenaType(ArenaTypeInterface type);
    
    /**
     * Returns the arena type provider for given rule.
     * 
     * @param rule
     *            the rule
     * @return type provider or {@code null} if it was not defined.
     */
    ArenaTypeProvider getProviderFromRule(RuleId rule);
    
    /**
     * Returns the arena type provider for given team.
     * 
     * @param team
     *            the team
     * @return type provider or {@code null} if it was not defined.
     */
    ArenaTypeProvider getProviderFromTeam(TeamId team);
    
    /**
     * Returns the arena type provider for given component.
     * 
     * @param component
     *            the component
     * @return type provider or {@code null} if it was not defined.
     */
    ArenaTypeProvider getProviderFromComponent(ComponentId component);
    
    /**
     * Returns the arena type provider for given phase.
     * 
     * @param phase
     *            the phase
     * @return type provider or {@code null} if it was not defined.
     */
    ArenaTypeProvider getProviderFromMatch(MatchPhaseId phase);
    
    /**
     * Return the amount of installed extensions.
     * 
     * @return extensions count.
     */
    int getExtensionsCount();
    
    /**
     * Return the installed extensions.
     * 
     * @return extensions.
     */
    Iterable<ExtensionInterface> getExtensions();
    
    /**
     * Return the amount of installed minigames.
     * 
     * @return minigames count.
     */
    int getMinigamesCount();
    
    /**
     * Return the installed minigames.
     * 
     * @return minigames.
     */
    Iterable<MinigameInterface> getMinigames();
    
    /**
     * Returns the minigame with given name.
     * 
     * @param minigame
     *            the minigames name
     * 
     * @return the minigame or {@code null} if is not available.
     */
    MinigameInterface getMinigame(String minigame);
    
    // zone api
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    ZoneInterface findZone(Location location);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    Iterable<ZoneInterface> findZones(Location location);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutY(Location)
     */
    ZoneInterface findZoneWithoutY(Location location);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutY(Location)
     */
    Iterable<ZoneInterface> findZonesWithoutY(Location location);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutYD(Location)
     */
    ZoneInterface findZoneWithoutYD(Location location);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutYD(Location)
     */
    Iterable<ZoneInterface> findZonesWithoutYD(Location location);
    
    // player api
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player bukkit online player
     * @return arena player.
     */
    ArenaPlayerInterface getPlayer(Player player);
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player bukkit offline player
     * @return arena player.
     */
    ArenaPlayerInterface getPlayer(OfflinePlayer player);
    
    /**
     * Returns the player for given bukkit player uuid.
     * 
     * @param uuid player uuid
     * @return arena player.
     */
    ArenaPlayerInterface getPlayer(UUID uuid);
    
    // sign api
    
    /**
     * Returns all declared arena/join signs
     * 
     * @return arena signs.
     */
    Iterable<SignInterface> getSigns();
    
    /**
     * Returns the sign on given location.
     * 
     * @param l
     *            bukkit location
     * @return sign or {@code null} if there is no sign.
     */
    SignInterface getSignForLocation(Location l);
    
    /**
     * Returns all declared arena/join signs for given arena type.
     * 
     * @param type
     *            arena type.
     * 
     * @return arena signs.
     */
    Iterable<SignInterface> getSigns(ArenaTypeInterface type);
    
    /**
     * Returns all declared arena/join signs for given arena
     * 
     * @param arena
     *            the arena.
     * 
     * @return arena signs.
     */
    Iterable<SignInterface> getSigns(ArenaInterface arena);
    
    /**
     * Returns all declared arena/join signs for given minigame
     * 
     * @param minigame
     *            the minigame.
     * 
     * @return arena signs.
     */
    Iterable<SignInterface> getSigns(MinigameInterface minigame);
    
    // arena api
    
    /**
     * Returns all known arena types.
     * 
     * @return known arena types.
     */
    Iterable<ArenaTypeInterface> getArenaTypes();
    
    /**
     * Returns all declared arenas.
     * 
     * @return declared arenas.
     */
    Iterable<ArenaInterface> getArenas();
    
    /**
     * Return the amount of arenas.
     * 
     * @return amount of arenas.
     */
    int getArenaCount();
    
    /**
     * Returns all arenas of given type.
     * 
     * @param type
     *            arena type.
     * @return the arenas of given type.
     */
    Iterable<ArenaInterface> getArenas(ArenaTypeInterface type);
    
    /**
     * Tries to find an arena from location.
     * 
     * @param location
     *            bukkit location
     * @return arena or {@code null} if the location does not match an arena
     */
    ArenaInterface getArenaFromLocation(Location location);
    
    // common singleton getter
    
    /**
     * Singleton access.
     */
    public final class INSTANCE
    {
        
        /** cached instance. */
        private static MglibInterface CACHED;
        
        /**
         * hidden constructor.
         */
        private INSTANCE()
        {
            // hidden constructor
        }
        
        /**
         * Returns the minigames lib plugin.
         * 
         * @return minigames lib plugin.
         */
        public static MglibInterface get()
        {
            if (CACHED == null)
            {
                final Plugin mgplugin = Bukkit.getServer().getPluginManager().getPlugin("MinigamesLib2"); //$NON-NLS-1$
                if (!(mgplugin instanceof MglibInterface))
                {
                    throw new IllegalStateException("Invalid minigames lib or inactive plugin."); //$NON-NLS-1$
                }
                final MglibInterface mglib = (MglibInterface) mgplugin;
                CACHED = mglib;
            }
            return CACHED;
        }
    }
    
}
