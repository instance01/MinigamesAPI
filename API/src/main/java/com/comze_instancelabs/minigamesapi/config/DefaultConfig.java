package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig {

	public static void init(JavaPlugin plugin){
		FileConfiguration config = plugin.getConfig();
		config.addDefault("config.classes_selection_item", 399);
		config.addDefault("config.default_max_players", 4);
		config.addDefault("config.default_min_players", 2);
		config.addDefault("config.lobby_countdown", 30);
		config.addDefault("config.ingame_countdown", 10);
		
		config.options().copyDefaults(false);
		plugin.saveConfig();
	}
	
}
