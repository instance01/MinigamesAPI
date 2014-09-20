package com.comze_instancelabs.minigamesapi.achievements;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ArenaAchievements {

	JavaPlugin plugin;
	PluginInstance pli;

	public ArenaAchievements(PluginInstance pli, JavaPlugin plugin) {
		this.plugin = plugin;
		this.pli = pli;
	}

	public ArrayList<AAchievement> loadPlayerAchievements(String playername, boolean sql) {
		ArrayList<AAchievement> ret = new ArrayList<AAchievement>();
		if (sql) {
			// TODO MySQL Support
		} else {
			for (String achievement : pli.getAchievementsConfig().getConfig().getConfigurationSection("config.achievements").getKeys(false)) {
				AAchievement ac = new AAchievement(achievement, playername, pli.getAchievementsConfig().getConfig().isSet("players." + playername + "." + achievement + ".done") ? pli.getAchievementsConfig().getConfig().getBoolean("players." + playername + "." + achievement + ".done") : false);
				ret.add(ac);
			}
		}
		return ret;
	}

	public void setAchievementDone(String playername, String achievement, boolean sql) {
		if (sql) {
			// TODO
		} else {
			pli.getAchievementsConfig().getConfig().set("players." + playername + "." + achievement + ".done", true);
			pli.getAchievementsConfig().saveConfig();
			Bukkit.getPlayer(playername).sendMessage(pli.getMessagesConfig().you_got_the_achievement.replaceAll("<achievement>", achievement));
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
