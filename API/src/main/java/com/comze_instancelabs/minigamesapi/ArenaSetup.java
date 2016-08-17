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
package com.comze_instancelabs.minigamesapi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * Arena setup helper.
 * 
 * @author instancelabs
 */
public class ArenaSetup
{
    
    // actually the most basic arena just needs a spawn and a lobby
    
    /**
     * Sets the spawn for a single-spawn arena
     * 
     * @param arenaname
     * @param l
     *            Location of the spawn
     */
    public void setSpawn(final JavaPlugin plugin, final String arenaname, final Location l)
    {
        Util.saveComponentForArena(plugin, arenaname, "spawns.spawn0", l);
    }
    
    /**
     * Sets a new spawn for a multi-spawn arena without the need of a given index
     * 
     * @param plugin
     * @param arenaname
     * @param l
     *            Location of the spawn
     * @return the automatically used index
     */
    public int autoSetSpawn(final JavaPlugin plugin, final String arenaname, final Location l)
    {
        final int count = Util.getAllSpawns(plugin, arenaname).size();
        Util.saveComponentForArena(plugin, arenaname, "spawns.spawn" + Integer.toString(count), l);
        return count;
    }
    
    /**
     * Sets one of multiple spawns for a multi-spawn arena
     * 
     * @param arenaname
     * @param l
     *            Location of the spawn
     * @param count
     *            Index of the spawn; if the given index is already set, the spawn location will be overwritten
     */
    public void setSpawn(final JavaPlugin plugin, final String arenaname, final Location l, final int count)
    {
        Util.saveComponentForArena(plugin, arenaname, "spawns.spawn" + Integer.toString(count), l);
    }
    
    /**
     * Removes a spawn at the given index
     * 
     * @param plugin
     * @param arenaname
     * @param count
     *            Index of the spawn
     */
    public boolean removeSpawn(final JavaPlugin plugin, final String arenaname, final int count)
    {
        final ArenasConfig config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig();
        final String path = ArenaConfigStrings.ARENAS_PREFIX + arenaname + ".spawns.spawn" + Integer.toString(count);
        boolean ret = false;
        if (config.getConfig().isSet(path))
        {
            ret = true;
        }
        config.getConfig().set(path, null);
        config.saveConfig();
        return ret;
    }
    
    /**
     * Sets the waiting lobby for an arena
     * 
     * @param arenaname
     * @param l
     *            Location of the lobby
     */
    public void setLobby(final JavaPlugin plugin, final String arenaname, final Location l)
    {
        Util.saveComponentForArena(plugin, arenaname, "lobby", l);
    }
    
    /**
     * Sets the main lobby
     * 
     * @param l
     *            Location of the lobby
     */
    public void setMainLobby(final JavaPlugin plugin, final Location l)
    {
        Util.saveMainLobby(plugin, l);
    }
    
    /**
     * Sets low and high boundaries for later blocks resetting
     * 
     * @param plugin
     * @param arenaname
     * @param l
     *            Location to save
     * @param low
     *            True if it's the low boundary, false if it's the high boundary
     */
    public void setBoundaries(final JavaPlugin plugin, final String arenaname, final Location l, final boolean low)
    {
        if (low)
        {
            Util.saveComponentForArena(plugin, arenaname, ArenaConfigStrings.BOUNDS_LOW, l);
        }
        else
        {
            Util.saveComponentForArena(plugin, arenaname, ArenaConfigStrings.BOUNDS_HIGH, l);
        }
    }
    
    /**
     * Sets low and high boundaries for later blocks resetting for a sub component
     * 
     * @param plugin
     * @param arenaname
     * @param l
     *            Location to save
     * @param low
     *            True if it's the low boundary, false if it's the high boundary
     * @param extra_component
     *            Sub component string
     */
    public void setBoundaries(final JavaPlugin plugin, final String arenaname, final Location l, final boolean low, final String extra_component)
    {
        if (low)
        {
            Util.saveComponentForArena(plugin, arenaname, extra_component + ".bounds.low", l);
        }
        else
        {
            Util.saveComponentForArena(plugin, arenaname, extra_component + ".bounds.high", l);
        }
    }
    
    /**
     * Saves a given arena if it was set up properly.
     * 
     * @return Arena or null if setup failed
     */
    public Arena saveArena(final JavaPlugin plugin, final String arenaname)
    {
        if (!Validator.isArenaValid(plugin, arenaname))
        {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Arena " + arenaname + " appears to be invalid.");
            return null;
        }
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        if (pli.getArenaByName(arenaname) != null)
        {
            pli.removeArenaByName(arenaname);
        }
        final Arena a = Util.initArena(plugin, arenaname);
        if (a.getArenaType() == ArenaType.REGENERATION)
        {
            if (Util.isComponentForArenaValid(plugin, arenaname, "bounds"))
            {
                Util.saveArenaToFile(plugin, arenaname);
            }
            else
            {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save arena to file because boundaries were not set up.");
            }
        }
        this.setArenaVIP(plugin, arenaname, false);
        pli.addArena(a);
        
        // experimental:
        final Class<? extends JavaPlugin> clazz = plugin.getClass();
        try
        {
            final Method method = clazz.getDeclaredMethod("loadArenas", JavaPlugin.class, pli.getArenasConfig().getClass());
            if (method != null)
            {
                method.setAccessible(true);
                final Object ret = method.invoke(this, plugin, pli.getArenasConfig());
                pli.clearArenas();
                pli.addLoadedArenas((ArrayList<Arena>) ret);
            }
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed to update Arena list, please reload the server.", e);
        }
        
        final String path = ArenaConfigStrings.ARENAS_PREFIX + arenaname + ArenaConfigStrings.DISPLAYNAME_SUFFIX;
        if (!pli.getArenasConfig().getConfig().isSet(path))
        {
            pli.getArenasConfig().getConfig().set(path, arenaname);
            pli.getArenasConfig().saveConfig();
        }
        
        return a;
    }
    
    public void setPlayerCount(final JavaPlugin plugin, final String arena, final int count, final boolean max)
    {
        String component = "max_players";
        if (!max)
        {
            component = "min_players";
        }
        final String base = ArenaConfigStrings.ARENAS_PREFIX + arena + "." + component;
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().set(base, count);
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().saveConfig();
    }
    
    public int getPlayerCount(final JavaPlugin plugin, final String arena, final boolean max)
    {
        if (!max)
        {
            if (!MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".min_players"))
            {
                this.setPlayerCount(plugin, arena, plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_DEFAULT_MIN_PLAYERS), max);
                return plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_DEFAULT_MIN_PLAYERS);
            }
            return MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".min_players");
        }
        if (!MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".max_players"))
        {
            this.setPlayerCount(plugin, arena, plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_DEFAULT_MAX_PLAYERS), max);
            return plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_DEFAULT_MAX_PLAYERS);
        }
        return MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".max_players");
    }
    
    public void setArenaVIP(final JavaPlugin plugin, final String arena, final boolean vip)
    {
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + arena + ".is_vip", vip);
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().saveConfig();
    }
    
    public boolean getArenaVIP(final JavaPlugin plugin, final String arena)
    {
        return MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().getBoolean(ArenaConfigStrings.ARENAS_PREFIX + arena + ".is_vip");
    }
    
    public void setArenaEnabled(final JavaPlugin plugin, final String arena, final boolean enabled)
    {
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + arena + ".enabled", enabled);
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().saveConfig();
    }
    
    public boolean getArenaEnabled(final JavaPlugin plugin, final String arena)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
        return config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".enabled") ? config.getBoolean(ArenaConfigStrings.ARENAS_PREFIX + arena + ".enabled") : true;
    }
    
    public void setShowScoreboard(final JavaPlugin plugin, final String arena, final boolean enabled)
    {
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + arena + ".showscoreboard", enabled);
        MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().saveConfig();
    }
    
    public boolean getShowScoreboard(final JavaPlugin plugin, final String arena)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
        return config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".showscoreboard") ? config.getBoolean(ArenaConfigStrings.ARENAS_PREFIX + arena + ".showscoreboard") : true;
    }
}
