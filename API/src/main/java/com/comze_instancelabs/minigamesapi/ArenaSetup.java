package com.comze_instancelabs.minigamesapi;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaSetup {

	// actually the most basic arena just needs a spawn and a lobby

	/**
	 * Sets the spawn for a single-spawn arena
	 * 
	 * @param arenaname
	 * @param l
	 *            Location of the spawn
	 */
	public void setSpawn(JavaPlugin plugin, String arenaname, Location l) {
		Util.saveComponentForArena(plugin, arenaname, "spawns.spawn0", l);
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
	public void setSpawn(JavaPlugin plugin, String arenaname, Location l, int count) {
		Util.saveComponentForArena(plugin, arenaname, "spawns.spawn" + Integer.toString(count), l);
	}

	/**
	 * Sets the waiting lobby for an arena
	 * 
	 * @param arenaname
	 * @param l
	 *            Location of the lobby
	 */
	public void setLobby(JavaPlugin plugin, String arenaname, Location l) {
		Util.saveComponentForArena(plugin, arenaname, "lobby", l);
	}

	/**
	 * Sets the main lobby
	 * 
	 * @param l
	 *            Location of the lobby
	 */
	public void setMainLobby(JavaPlugin plugin, Location l) {
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
	public void setBoundaries(JavaPlugin plugin, String arenaname, Location l, boolean low) {
		if (low) {
			Util.saveComponentForArena(plugin, arenaname, "bounds.low", l);
		} else {
			Util.saveComponentForArena(plugin, arenaname, "bounds.high", l);
		}
	}

	/**
	 * Saves a given arena if it was set up properly.
	 * 
	 * @return Arena or null if setup failed
	 */
	public Arena saveArena(JavaPlugin plugin, String arenaname) {
		if (!Validator.isArenaValid(plugin, arenaname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Arena " + arenaname + " appears to be invalid.");
			return null;
		}
		// TODO arena saving (to file too)
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		if (pli.getArenaByName(arenaname) != null) {
			pli.removeArenaByName(arenaname);
		}
		Arena a = Util.initArena(plugin, arenaname);
		if(a.getArenaType() == ArenaType.REGENERATION){
			if(Util.isComponentForArenaValid(plugin, arenaname, "bounds")){
				Util.saveArenaToFile(plugin, arenaname);
			}else{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save arena to file because boundaries were not set up.");
			}
		}
		this.setArenaVIP(plugin, arenaname, false);
		pli.addArena(a);
		return a;
	}

	public void setPlayerCount(JavaPlugin plugin, String arena, int count, boolean max) {
		String component = "max_players";
		if (!max) {
			component = "min_players";
		}
		String base = "arenas." + arena + "." + component;
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base, count);
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
	}

	public int getPlayerCount(JavaPlugin plugin, String arena, boolean max) {
		if (!max) {
			if (!MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().isSet("arenas." + arena + ".min_players")) {
				setPlayerCount(plugin, arena, plugin.getConfig().getInt("config.default_min_players"), max);
				return plugin.getConfig().getInt("config.default_min_players");
			}
			return MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".min_players");
		}
		if (!MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().isSet("arenas." + arena + ".max_players")) {
			setPlayerCount(plugin, arena, plugin.getConfig().getInt("config.default_max_players"), max);
			return plugin.getConfig().getInt("config.default_max_players");
		}
		return MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".max_players");
	}

	public void setArenaVIP(JavaPlugin plugin, String arena, boolean vip) {
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + arena + ".is_vip", vip);
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
	}

	public boolean getArenaVIP(JavaPlugin plugin, String arena) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getBoolean("arenas." + arena + ".is_vip");
	}
}
