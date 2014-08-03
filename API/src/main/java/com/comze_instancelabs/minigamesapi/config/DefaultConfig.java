package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig {

	public DefaultConfig(JavaPlugin plugin, boolean custom){
		DefaultConfig.init(plugin, custom);
	}
	
	
	public static void init(JavaPlugin plugin, boolean custom){
		FileConfiguration config = plugin.getConfig();
		if(!custom){
			config.addDefault("config.classes_selection_item", 399);
			config.addDefault("config.default_max_players", 4);
			config.addDefault("config.default_min_players", 2);
			config.addDefault("config.lobby_countdown", 30);
			config.addDefault("config.ingame_countdown", 10);
			config.addDefault("config.rewards.economy", true);
			config.addDefault("config.rewards.economy_reward", 10);
			config.addDefault("config.rewards.item_reward", false);
			config.addDefault("config.rewards.item_reward_ids", "264*1;11*1");
			config.addDefault("config.rewards.command_reward", false);
			config.addDefault("config.rewards.command", "pex user <player> add SKILLZ.*");
			config.addDefault("config.arcade.min_players", 1);
			config.addDefault("config.arcade.max_players", 16);
		}
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
}
