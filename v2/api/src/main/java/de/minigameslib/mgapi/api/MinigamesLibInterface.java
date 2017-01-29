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

package de.minigameslib.mgapi.api;

import java.util.Collection;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.util.function.McBiFunction;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * Base interface to access the minigames API.
 * 
 * @author mepeisen
 */
public interface MinigamesLibInterface
{
    
    // common methods
    
    /**
     * Returns the library instance.
     * 
     * @return library instance.
     */
    static MinigamesLibInterface instance()
    {
        return MglibCache.get();
    }
    
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
     * the first api version, all versions up to first release, includes minecraft versions up to 1.11.
     */
    int APIVERSION_1_0_0 = 10000;
    
    /**
     * Returns the api version of MCLIB.
     * 
     * @return api version.
     */
    int getApiVersion();
    
    // initialization
    
    /**
     * Initializes the given minigame.
     * 
     * <p>
     * This method must be invoked in onEnable of the minigame plugin. Invoke after all enumerations are registered. Each plugin is only allowed to register one minigame.
     * </p>
     * 
     * @param plugin
     *            the owning plugin object
     * @param provider
     *            a provider describing the minigame.
     * @throws McException
     *             thrown if library is in wrong state
     */
    void initMinigame(Plugin plugin, MinigameProvider provider) throws McException;
    
    /**
     * Registers a new rule set to be used with minigames library; once a rule set is applied or changed the creator is asked to create
     * a new rule set instance. The instance lives as long as the underlying object lives or as long as it is not removed.
     * @param plugin
     * @param ruleset
     * @param creator
     */
    void registerRuleset(Plugin plugin, ArenaRuleSetType ruleset, McBiFunction<ArenaRuleSetType, ArenaInterface, ArenaRuleSetInterface> creator);
    
    /**
     * Registers a new rule set to be used with minigames library; once a rule set is applied or changed the creator is asked to create
     * a new rule set instance. The instance lives as long as the underlying object lives or as long as it is not removed.
     * @param plugin
     * @param ruleset
     * @param creator
     */
    void registerRuleset(Plugin plugin, ComponentRuleSetType ruleset, McBiFunction<ComponentRuleSetType, ArenaComponentHandler, ComponentRuleSetInterface> creator);
    
    /**
     * Registers a new rule set to be used with minigames library; once a rule set is applied or changed the creator is asked to create
     * a new rule set instance. The instance lives as long as the underlying object lives or as long as it is not removed.
     * @param plugin
     * @param ruleset
     * @param creator
     */
    void registerRuleset(Plugin plugin, SignRuleSetType ruleset, McBiFunction<SignRuleSetType, ArenaSignHandler, SignRuleSetInterface> creator);
    
    /**
     * Registers a new rule set to be used with minigames library; once a rule set is applied or changed the creator is asked to create
     * a new rule set instance. The instance lives as long as the underlying object lives or as long as it is not removed.
     * @param plugin
     * @param ruleset
     * @param creator
     */
    void registerRuleset(Plugin plugin, ZoneRuleSetType ruleset, McBiFunction<ZoneRuleSetType, ArenaZoneHandler, ZoneRuleSetInterface> creator);
    
    /**
     * Registers a new component with arena support.
     * @param plugin
     * @param type
     * @param creator
     */
    void registerArenaComponent(Plugin plugin, ComponentTypeId type, McSupplier<ArenaComponentHandler> creator);
    
    /**
     * Registers a new zone with arena support.
     * @param plugin
     * @param type
     * @param creator
     */
    void registerArenaZone(Plugin plugin, ZoneTypeId type, McSupplier<ArenaZoneHandler> creator);
    
    /**
     * Registers a new sign with arena support.
     * @param plugin
     * @param type
     * @param creator
     */
    void registerArenaSign(Plugin plugin, SignTypeId type, McSupplier<ArenaSignHandler> creator);
    
    /**
     * Returns the number of registered minigames.
     * 
     * @return number of registered minigames
     */
    int getMinigameCount();
    
    /**
     * Returns the number of registered minigames.
     * 
     * @param prefix
     *            the prefix for the minigames (filter)
     * @return number of registered minigames
     */
    int getMinigameCount(String prefix);
    
    /**
     * Returns the minigames
     * 
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of minigames to return
     * @return the minigames
     */
    Collection<MinigameInterface> getMinigames(int index, int limit);
    
    /**
     * Returns the minigames
     * 
     * @param prefix
     *            the prefix for the minigames (filter)
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of minigames to return
     * @return the minigames
     */
    Collection<MinigameInterface> getMinigames(String prefix, int index, int limit);
    
    /**
     * Returns minigame for given internal name.
     * 
     * @param name
     * @return {@code null} if minigame was not found
     */
    MinigameInterface getMinigame(String name);
    
    /**
     * Returns minigame for given plugin.
     * 
     * @param plugin
     * @return {@code null} if minigame was not found
     */
    MinigameInterface getMinigame(Plugin plugin);
    
    /**
     * Initializes the given extension.
     * 
     * <p>
     * This method must be invoked in onEnable of the extension plugin. Invoke after all enumerations are registered. Each plugin is only allowed to register one extension.
     * </p>
     * 
     * @param plugin
     *            the owning plugin object
     * @param provider
     *            a provider describing the extension.
     * @throws McException
     *             thrown if library is in wrong state
     */
    void initExtension(Plugin plugin, ExtensionProvider provider) throws McException;
    
    /**
     * Returns the number of registered extensions.
     * 
     * @return number of registered extensions
     */
    int getExtensionCount();
    
    /**
     * Returns the number of registered extensions.
     * 
     * @param prefix
     *            the prefix for the extensions (filter)
     * @return number of registered extensions
     */
    int getExtensionCount(String prefix);
    
    /**
     * Returns the extensions
     * 
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of extensions to return
     * @return the extensions
     */
    Collection<ExtensionInterface> getExtensions(int index, int limit);
    
    /**
     * Returns the extensions
     * 
     * @param prefix
     *            the prefix for the extensions (filter)
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of extensions to return
     * @return the extensions
     */
    Collection<ExtensionInterface> getExtensions(String prefix, int index, int limit);
    
    /**
     * Returns extension for given internal name.
     * 
     * @param name
     * @return {@code null} if extension was not found
     */
    ExtensionInterface getExtension(String name);
    
    // Arenas
    
    /**
     * Returns the number of registered arenas
     * 
     * @return number of arenas
     */
    int getArenaCount();
    
    /**
     * Returns the number of registered arenas
     * 
     * @param prefix
     *            name prefix filter
     * @return number of arenas
     */
    int getArenaCount(String prefix);
    
    /**
     * Returns the number of registered arenas
     * 
     * @param plugin
     *            plugin filter
     * @return number of arenas
     */
    int getArenaCount(Plugin plugin);
    
    /**
     * Returns the number of registered arenas
     * 
     * @param plugin
     *            plugin filter
     * @param prefix
     *            name prefix filter
     * @return number of arenas
     */
    int getArenaCount(Plugin plugin, String prefix);
    
    /**
     * Returns the number of registered arenas
     * 
     * @param type
     *            arena type filter
     * @return number of arenas
     */
    int getArenaCount(ArenaTypeInterface type);
    
    /**
     * Returns the number of registered arenas
     * 
     * @param type
     *            arena type filter
     * @param prefix
     *            name prefix filter
     * @return number of arenas
     */
    int getArenaCount(ArenaTypeInterface type, String prefix);
    
    /**
     * Returns the arenas
     * 
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of arenas to return
     * @return the arenas
     */
    Collection<ArenaInterface> getArenas(int index, int limit);
    
    /**
     * Returns the arenas
     * 
     * @param prefix
     *            name prefix filter
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of arenas to return
     * @return the arenas
     */
    Collection<ArenaInterface> getArenas(String prefix, int index, int limit);
    
    /**
     * Returns the arenas
     * 
     * @param plugin
     *            plugin filter
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of arenas to return
     * @return the arenas
     */
    Collection<ArenaInterface> getArenas(Plugin plugin, int index, int limit);
    
    /**
     * Returns the arenas
     * 
     * @param plugin
     *            plugin filter
     * @param prefix
     *            name prefix filter
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of arenas to return
     * @return the arenas
     */
    Collection<ArenaInterface> getArenas(Plugin plugin, String prefix, int index, int limit);
    
    /**
     * Returns the arenas
     * 
     * @param type
     *            arena type filter
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of arenas to return
     * @return the arenas
     */
    Collection<ArenaInterface> getArenas(ArenaTypeInterface type, int index, int limit);
    
    /**
     * Returns the arenas
     * 
     * @param type
     *            arena type filter
     * @param prefix
     *            name prefix filter
     * @param index
     *            starting index
     * @param limit
     *            maximum amount of arenas to return
     * @return the arenas
     */
    Collection<ArenaInterface> getArenas(ArenaTypeInterface type, String prefix, int index, int limit);
    
    /**
     * Returns the arena for given name
     * 
     * @param name
     * @return arena or {@code null} if the arena was not found
     */
    ArenaInterface getArena(String name);
    
    /**
     * Creates a new arena
     * 
     * @param name
     *            arena name
     * @param type
     *            arena type
     * @return the arena interface
     * @throws McException
     *             thrown if there are problems creating the arena.
     */
    ArenaInterface create(String name, ArenaTypeInterface type) throws McException;

    /**
     * Returns the arena player for given mclib player
     * @param player
     * @return arena player
     */
    ArenaPlayerInterface getPlayer(McPlayerInterface player);
    
}
