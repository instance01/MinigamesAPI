package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig {

	public DefaultConfig(JavaPlugin plugin, boolean custom) {
		DefaultConfig.init(plugin, custom);
	}

	public static void init(JavaPlugin plugin, boolean custom) {
		FileConfiguration config = plugin.getConfig();
		if (!custom) {
			config.addDefault("config.classes_selection_item", 399);
			config.addDefault("config.exit_item", 152);
			config.addDefault("config.spectator_item", 345);
			config.addDefault("config.spectator_after_fall_or_death", true);
			config.addDefault("config.spectator_move_y_lock", true);
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

			config.addDefault("config.rewards.economy_for_kills", true);
			config.addDefault("config.rewards.economy_reward_for_kills", 5);
			config.addDefault("config.rewards.command_reward_for_kills", false);
			config.addDefault("config.rewards.command_for_kills", "pex user <player> add SKILLZ.*");

			config.addDefault("config.arcade.enabled", true);
			config.addDefault("config.arcade.min_players", 1);
			config.addDefault("config.arcade.max_players", 16);
			config.addDefault("config.arcade.arena_to_prefer.enabled", false);
			config.addDefault("config.arcade.arena_to_prefer.arena", "arena1");
			config.addDefault("config.arcade.lobby_countdown", 20);
			config.addDefault("config.arcade.show_each_lobby_countdown", false);
			config.addDefault("config.bungee.game_on_join", false);
			config.addDefault("config.bungee.teleport_all_to_server_on_stop.tp", false);
			config.addDefault("config.bungee.teleport_all_to_server_on_stop.server", "lobby");
			config.addDefault("config.bungee.whitelist_while_game_running", false);
			config.addDefault("config.execute_cmds_on_stop", false);
			config.addDefault("config.cmds", "say SERVER STOPPING;stop");
			config.addDefault("config.classes_gui_rows", 3);
			config.addDefault("config.map_rotation", false);
			config.addDefault("config.broadcast_win", true);
			config.addDefault("config.buy_classes_forever", true);
			config.addDefault("config.disable_commands_in_arena", true);
			config.addDefault("config.leave_command", "/leave");
			config.addDefault("config.spawn_fireworks_for_winners", true);
			config.addDefault("config.broadcast_powerup_spawning", false);
			config.addDefault("config.use_custom_scoreboard", false);

			config.addDefault("mysql.enabled", false);
			config.addDefault("mysql.host", "127.0.0.1");
			config.addDefault("mysql.user", "root");
			config.addDefault("mysql.pw", "root");
			config.addDefault("mysql.database", "mcminigames");
		}
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}

}
