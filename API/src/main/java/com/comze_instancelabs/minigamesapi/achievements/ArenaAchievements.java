package com.comze_instancelabs.minigamesapi.achievements;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ArenaAchievements {

	JavaPlugin plugin;
	PluginInstance pli;

	public ArenaAchievements(JavaPlugin plugin) {
		this.plugin = plugin;
		pli = MinigamesAPI.getAPI().pinstances.get(plugin);
	}

	public ArrayList<AAchievement> loadPlayerAchievements(String playername, boolean sql) {
		ArrayList<AAchievement> ret = new ArrayList<AAchievement>();
		if (sql) {
			// TODO MySQL Support
		} else {
			// for each achievement in achievement-config
			// check if player has it done, init new AAchievement and add it
			for (String achievement : pli.getAchievementsConfig().getConfig().getConfigurationSection("config.achievements").getKeys(false)) {
				// AAchievement ac = new AAchievement();
			}
		}
		return ret;
	}

	public void setAchievementDone(String playername, String achievement, boolean sql) {
		if (sql) {
			// TODO
		} else {
			// save into achievement-config
			// message player
		}
	}

	public boolean isEnabled() {
		return pli.getAchievementsConfig().getConfig().getBoolean("config.enabled");
	}

	public void setEnabled(boolean t) {
		pli.getAchievementsConfig().getConfig().set("config.enabled", t);
		pli.getAchievementsConfig().saveConfig();
	}

}
