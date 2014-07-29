package com.comze_instancelabs.minigamesapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.StatsConfig;

public class Stats {

	// used for wins and points
	// you can get points for pretty much everything in the games,
	// but these points are just for top stats, nothing more

	// TODO Add MySQL support

	private JavaPlugin plugin = null;

	public Stats(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void win(String playername, int count) {
		addWin(playername);
		addPoints(playername, count);
		MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().updateWinnerStats(playername, count);
	}

	/**
	 * Gets called on player join to ensure file stats are up to date (with mysql stats)
	 * @param playername
	 */
	public void update(String playername) {
		if(plugin.getConfig().getBoolean("mysql.enabled")){
			setWins(playername, MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().getWins(playername));
			setPoints(playername, MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().getPoints(playername));
		}
	}

	private void setWins(String playername, int count) {
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig().set("players." + uuid + ".wins", count);
	}

	private void setPoints(String playername, int count) {
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig().set("players." + uuid + ".points", count);
	}

	private void addWin(String playername) {
		StatsConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig();
		int temp = 0;
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.getConfig().isSet("players." + uuid + ".wins")) {
			temp = config.getConfig().getInt("players." + uuid + ".wins");
		}
		config.getConfig().set("players." + uuid + ".wins", temp + 1);
		config.saveConfig();
	}

	private void addPoints(String playername, int count) {
		StatsConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig();
		int temp = 0;
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.getConfig().isSet("players." + uuid + ".points")) {
			temp = config.getConfig().getInt("players." + uuid + ".points");
		}
		config.getConfig().set("players." + uuid + ".points", temp + count);
		config.saveConfig();
	}

	public int getPoints(String playername) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.isSet("players." + uuid + ".points")) {
			return config.getInt("players." + uuid + ".points");
		}
		return 0;
	}

	public int getWins(String playername) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.isSet("players." + uuid + ".wins")) {
			return config.getInt("players." + uuid + ".wins");
		}
		return 0;
	}

	public void getTop(int count) {
		// TODO top list
	}

}
