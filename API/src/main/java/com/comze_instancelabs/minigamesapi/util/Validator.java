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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class Validator
{
    
    /***
     * returns true if given player is online
     * 
     * @param arena
     * @return
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
     * returns true if given player is online and in arena
     * 
     * @param arena
     * @return
     */
    public static boolean isPlayerValid(final JavaPlugin plugin, final String player, final Arena arena)
    {
        return Validator.isPlayerValid(plugin, player, arena.getInternalName());
    }
    
    /***
     * returns true if given player is online and in arena
     * 
     * @param arena
     * @return
     */
    public static boolean isPlayerValid(final JavaPlugin plugin, final String player, final String arena)
    {
        if (!Validator.isPlayerOnline(player))
        {
            return false;
        }
        if (!MinigamesAPI.getAPI().getPluginInstance(plugin).global_players.containsKey(player))
        {
            return false;
        }
        if (!MinigamesAPI.getAPI().getPluginInstance(plugin).global_players.get(player).getInternalName().equalsIgnoreCase(arena))
        {
            return false;
        }
        return true;
    }
    
    /***
     * returns true if given arena was set up correctly
     * 
     * @param arena
     * @return
     */
    public static boolean isArenaValid(final JavaPlugin plugin, final Arena arena)
    {
        return Validator.isArenaValid(plugin, arena.getInternalName());
    }
    
    /***
     * returns true if given arena was set up correctly
     * 
     * @param arena
     * @return
     */
    public static boolean isArenaValid(final JavaPlugin plugin, final String arena)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
        if (!config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".lobby") || !config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".spawns.spawn0"))
        {
            ArenaLogger.debug(ChatColor.AQUA + arena + " is invalid! lobby:" + config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".lobby") + " spawns.spawn0:" + config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".spawns.spawn0"));
            return false;
        }
        return true;
    }
    
    /***
     * returns true if given arena was set up correctly
     * 
     * @param arena
     * @return
     */
    public static boolean isArenaValid(final JavaPlugin plugin, final String arena, final FileConfiguration cf)
    {
        final FileConfiguration config = cf;
        if (!config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".lobby") || !config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".spawns.spawn0"))
        {
            return false;
        }
        return true;
    }
    
}
