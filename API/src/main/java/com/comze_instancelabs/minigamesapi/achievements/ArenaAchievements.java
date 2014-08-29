package com.comze_instancelabs.minigamesapi.achievements;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class ArenaAchievements {

	JavaPlugin plugin;

	public ArenaAchievements(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public ArrayList<AAchievement> loadPlayerAchievements(String playername, boolean sql) {
		ArrayList<AAchievement> ret = new ArrayList<AAchievement>();
		if (sql) {
			// TODO MySQL Support
		} else {
			// for each achievement in achievement-config
			// check if player has it done, init new AAchievement and add it
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

}
