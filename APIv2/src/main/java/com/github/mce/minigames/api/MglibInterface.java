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
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.zones.ZoneInterface;

/**
 * Base interface to access the minigames API.
 * 
 * @author mepeisen
 */
public interface MglibInterface
{
    
    /**
     * A pseudo minigame representing the library itself.
     * 
     * <p>
     * The core minigame is referred in various situations, for example in common error messages.
     * </p>
     */
    String CORE_MINIGAME = "core"; //$NON-NLS-1$
    
    /**
     * Returns the current library state.
     * 
     * @return library state.
     */
    LibState getState();
    
    /**
     * Returns the minecraft version we are running on.
     * 
     * @return minecraft server version.
     */
    MinecraftVersionsType getMinecraftVersion();
    
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
     * @throws MinigameException
     *             thrown if the minigame with given name is already registered.
     */
    MinigamePluginInterface register(PluginProviderInterface provider) throws MinigameException;
    
    /**
     * Returns the minigame with given name.
     * 
     * @param minigame
     *            the minigames name
     * 
     * @return the minigame or {@code null} if is not available.
     */
    MinigameInterface getMinigame(String minigame);
    
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
     * @param location
     * 
     * @return Zone or {@code null} if no zone was found.
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
     * @param location
     * 
     * @return Zone or {@code null} if no zone was found.
     */
    Iterable<ZoneInterface> findZones(Location location);
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     * @return arena player.
     */
    ArenaPlayerInterface getPlayer(Player player);
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     * @return arena player.
     */
    ArenaPlayerInterface getPlayer(OfflinePlayer player);
    
    /**
     * Returns the player for given bukkit player uuid.
     * 
     * @param uuid
     * @return arena player.
     */
    ArenaPlayerInterface getPlayer(UUID uuid);
    
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
     * Returns all arenas of given type.
     * 
     * @param type
     *            arena type.
     * @return the arenas of given type.
     */
    Iterable<ArenaInterface> getArenas(ArenaTypeInterface type);
    
    /**
     * Singleton access.
     */
    public final class INSTANCE
    {
        
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
            final Plugin mgplugin = Bukkit.getServer().getPluginManager().getPlugin("MinigamesLib2"); //$NON-NLS-1$
            if (!(mgplugin instanceof MglibInterface))
            {
                throw new IllegalStateException("Invalid minigames lib or inactive plugin."); //$NON-NLS-1$
            }
            final MglibInterface mglib = (MglibInterface) mgplugin;
            return mglib;
        }
    }
    
}
