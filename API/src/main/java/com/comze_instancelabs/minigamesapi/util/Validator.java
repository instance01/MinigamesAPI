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
package com.comze_instancelabs.minigamesapi.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

/**
 * Validation helpers.
 * 
 * @author instancelabs
 */
public class Validator
{
    
    /***
     * returns true if given player is online
     * 
     * @param player
     *            name of the player.
     * @return true if the player is online.
     */
    public static boolean isPlayerOnline(final String player)
    {
        final Player p = Bukkit.getPlayer(player);
        if (p != null)
        {
            return true;
        }
        return false;
    }
    
    /***
     * returns true if given player is online
     * 
     * @param player
     *            name of the player.
     * @return true if the player is online.
     */
    public static boolean isPlayerOnline(final UUID player)
    {
        final Player p = Bukkit.getPlayer(player);
        if (p != null)
        {
            return true;
        }
        return false;
    }
    
    /***
     * returns true if given player is online and in arena
     * 
     * @param plugin
     *            minigame java plugin.
     * @param player
     *            the player name
     * @param arena
     *            the arena
     * @return {@code true} if the player is online and in given arena.
     */
    public static boolean isPlayerValid(final JavaPlugin plugin, final String player, final Arena arena)
    {
        return Validator.isPlayerValid(plugin, player, arena.getInternalName());
    }
    
    /***
     * returns true if given player is online and in arena
     * 
     * @param plugin
     *            the minigame java plugin
     * @param player
     *            the player name
     * @param arena
     *            the arena name
     * @return {@code true} if the player is online and in given arena.
     */
    public static boolean isPlayerValid(final JavaPlugin plugin, final String player, final String arena)
    {
        if (!Validator.isPlayerOnline(player))
        {
            return false;
        }
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        if (!pli.global_players.containsKey(player))
        {
            return false;
        }
        if (!pli.global_players.get(player).getInternalName().equalsIgnoreCase(arena))
        {
            return false;
        }
        return true;
    }
    
    /***
     * returns true if given arena was set up correctly; if it contains a lobby and at leats one spawn.
     * 
     * @param plugin
     *            the minigame java plugin
     * @param arena
     *            the arena to test
     * @return {@code true} if the arena is valid
     */
    public static boolean isArenaValid(final JavaPlugin plugin, final Arena arena)
    {
        return Validator.isArenaValid(plugin, arena.getInternalName());
    }
    
    /***
     * returns true if given arena was set up correctly
     * 
     * @param plugin
     *            the minigame java plugin
     * @param arena
     *            the arena to test
     * @return {@code true} if the arena is valid
     */
    public static boolean isArenaValid(final JavaPlugin plugin, final String arena)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
        return isArenaValid(plugin, arena, config);
    }
    
    /***
     * returns true if given arena was set up correctly
     * 
     * @param plugin
     *            the minigame java plugin
     * @param arena
     *            the arena to test
     * @param cf
     *            the configuration to test
     * @return {@code true} if the arena is valid
     */
    public static boolean isArenaValid(final JavaPlugin plugin, final String arena, final FileConfiguration cf)
    {
        final FileConfiguration config = cf;
        final boolean hasLobby = config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".lobby");
        final boolean hasSpawn0 = config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".spawns.spawn0");
        if (!hasLobby || !hasSpawn0)
        {
            ArenaLogger.debug(ChatColor.AQUA + arena + " is invalid! lobby:" + hasLobby + " spawns.spawn0:" + hasSpawn0); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }
        return true;
    }
    
}
