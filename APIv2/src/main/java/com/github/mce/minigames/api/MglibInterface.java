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

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.config.ConfigInterface;
import com.github.mce.minigames.api.config.ConfigurationValueInterface;
import com.github.mce.minigames.api.context.MinigameContext;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.services.MinigameExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;
import com.github.mce.minigames.api.sign.SignInterface;
import com.github.mce.minigames.api.zones.ZoneInterface;

/**
 * Base interface to access the minigames API.
 * 
 * @author mepeisen
 */
public interface MglibInterface extends MinigameContext
{
    
    /**
     * A pseudo minigame representing the library itself.
     * 
     * <p>
     * The core minigame is referred in various situations, for example in common error messages.
     * </p>
     */
    String CORE_MINIGAME = "core"; //$NON-NLS-1$
    
    // common methods
    
    /**
     * Checks for debug flag.
     * 
     * @return {@code true} if the library debugging is enabled.
     */
    boolean debug();
    
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
     * Returns the library version string.
     * 
     * @return library version string.
     */
    Serializable getLibVersionString();
    
    /**
     * Returns a logger for the library.
     * 
     * @return logger instance.
     */
    Logger getLogger();
    
    /**
     * Returns the default locale used in minigame lib.
     * 
     * @return default locale
     */
    Locale getDefaultLocale();
    
    // initialization
    
    /**
     * Registers a new extension.
     * 
     * @param extension
     *            minigame extension to register.
     * @return the minigame extension
     * @throws MinigameException
     *             thrown if the minigame with given name is already registered.
     */
    MinigameExtensionInterface register(MinigameExtensionProviderInterface extension) throws MinigameException;
    
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
    
    // main api
    
    /**
     * Returns the message api declaring the given message.
     * 
     * @param item
     *            the enumeration value; only works on classes that are returned by a plugin or extension provider during initialization.
     * 
     * @return message api or {@code null} if the class was not declared by any minigame or extension.
     */
    MessagesConfigInterface getMessagesFromMsg(LocalizedMessageInterface item);
    
    /**
     * Returns the configuration declaring the given configuration value.
     * 
     * @param item
     *            the configuration value; only works on classes that are returned by a plugin or extension provider during initialization.
     * 
     * @return config provider or {@code null} if the class was not declared by any minigame or extension.
     */
    ConfigInterface getConfigFromCfg(ConfigurationValueInterface item);
    
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
    Iterable<MinigameExtensionInterface> getExtensions();
    
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
    
    // player api
    
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
    
    // common singleton getter
    
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
