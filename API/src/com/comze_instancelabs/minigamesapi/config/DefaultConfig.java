package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class DefaultConfig {

    JavaPlugin plugin;

    public DefaultConfig(JavaPlugin plugin, boolean custom) {
        this.plugin = plugin;
        DefaultConfig.init(plugin, custom);
    }

    public static void init(JavaPlugin plugin, boolean custom) {
        FileConfiguration config = plugin.getConfig();
        config.options().header("The default config. Check http://dev.bukkit.org/bukkit-plugins/instances-minigamesapi/#w-tutorials \n" + "or https://github.com/instance01/MinigamesAPI/wiki/Default-Config-and-Item-Markup \n" + "for more information if you don't understand a config entry. \n" + "You can find classes (kits) in classes.yml, all saved arenas in arenas.yml, all messages in messages.yml. \n" + "You can edit/disable achievements in achievements.yml.");
        if (!custom) {
            config.addDefault("config.selection_items.classes_selection_item", 399);
            config.addDefault("config.selection_items.exit_item", 152);
            config.addDefault("config.selection_items.achievement_item", 160);
            config.addDefault("config.selection_items.spectator_item", 345);
            config.addDefault("config.selection_items.shop_selection_item", 388);
            config.addDefault("config.GUI.classes_gui_rows", 3);
            config.addDefault("config.GUI.shop_gui_rows", 3);
            config.addDefault("config.spectator.spectator_after_fall_or_death", true);
            config.addDefault("config.spectator.spectator_move_y_lock", true);
            config.addDefault("config.defaults.default_max_players", 4);
            config.addDefault("config.defaults.default_min_players", 2);
            config.addDefault("config.defaults.default_max_game_time_in_minutes", 30);
            config.addDefault("config.countdowns.lobby_countdown", 30);
            config.addDefault("config.countdowns.ingame_countdown", 10);
            config.addDefault("config.countdowns.ingame_countdown_enabled", true);
            config.addDefault("config.countdowns.skip_lobby", false);
            config.addDefault("config.countdowns.clearinv_while_ingamecountdown", false);

            config.addDefault("config.classes_enabled", true);
            config.addDefault("config.shop_enabled", true);
            config.addDefault("config.use_credits_instead_of_money_for_kits", false);
            config.addDefault("config.reset_inventory_when_players_leave_server", true);
            config.addDefault("config.color_background_wool_of_signs", false);

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
            config.addDefault("config.rewards.economy_for_participation", false);
            config.addDefault("config.rewards.economy_reward_for_participation", 5);
            config.addDefault("config.rewards.command_reward_for_participation", false);
            config.addDefault("config.rewards.command_for_participation", "pex user <player> add SKILLZ.*");

            config.addDefault("config.stats.points_for_kill", 2);
            config.addDefault("config.stats.points_for_win", 10);

            config.addDefault("config.arcade.enabled", true);
            config.addDefault("config.arcade.min_players", 1);
            config.addDefault("config.arcade.max_players", 16);
            config.addDefault("config.arcade.arena_to_prefer.enabled", false);
            config.addDefault("config.arcade.arena_to_prefer.arena", "arena1");
            config.addDefault("config.arcade.lobby_countdown", 20);
            config.addDefault("config.arcade.show_each_lobby_countdown", false);
            config.addDefault("config.arcade.infinite_mode.enabled", false);
            config.addDefault("config.arcade.infinite_mode.seconds_to_new_round", 10);
            config.addDefault("config.bungee.game_on_join", false);
            config.addDefault("config.bungee.teleport_all_to_server_on_stop.tp", false);
            config.addDefault("config.bungee.teleport_all_to_server_on_stop.server", "lobby");
            config.addDefault("config.bungee.whitelist_while_game_running", false);
            config.addDefault("config.execute_cmds_on_stop", false);
            config.addDefault("config.cmds", "");
            config.addDefault("config.cmds_after", "say SERVER STOPPING;stop");
            config.addDefault("config.map_rotation", false);
            config.addDefault("config.broadcast_win", true);
            config.addDefault("config.buy_classes_forever", true);
            config.addDefault("config.disable_commands_in_arena", true);
            config.addDefault("config.command_whitelist", "/msg,/pm,/help");
            config.addDefault("config.leave_command", "/leave");
            config.addDefault("config.spawn_fireworks_for_winners", true);
            config.addDefault("config.powerup_spawning.broadcast", false);
            config.addDefault("config.powerup_spawning.spawn_firework", false);
            config.addDefault("config.use_custom_scoreboard", false);
            config.addDefault("config.delay.enabled", false);
            config.addDefault("config.delay.amount_seconds", 5);
            config.addDefault("config.send_game_started_msg", false);
            config.addDefault("config.auto_add_default_kit", true);
            config.addDefault("config.last_man_standing_wins", true);
            config.addDefault("config.effects.blood", true);
            config.addDefault("config.effects.damage_identifier_holograms", true);
            config.addDefault("config.effects.dead_in_fake_bed", true);
            config.addDefault("config.1_8_titles", true);
            config.addDefault("config.sounds.lobby_countdown", "none");
            config.addDefault("config.sounds.ingame_countdown", "SUCCESSFUL_HIT");
            config.addDefault("config.chat_per_arena_only", false);
            config.addDefault("config.chat_show_score_in_arena", false);
            config.addDefault("config.compass_tracking_enabled", true);
            config.addDefault("config.allow_classes_selection_out_of_arenas", false);
            config.addDefault("config.send_stats_on_stop", true);
            config.addDefault("config.use_xp_bar_level", true);

            config.addDefault("config.extra_lobby_item.item0.enabled", false);
            config.addDefault("config.extra_lobby_item.item0.item", 9);
            config.addDefault("config.extra_lobby_item.item0.name", "Custom Item");
            config.addDefault("config.extra_lobby_item.item0.command", "say This is a custom extra lobby item.");

            config.addDefault("mysql.enabled", false);
            config.addDefault("mysql.host", "127.0.0.1");
            config.addDefault("mysql.user", "root");
            config.addDefault("mysql.pw", "root");
            config.addDefault("mysql.database", "mcminigames");
        }
        config.options().copyDefaults(true);
        plugin.saveConfig();

        try {
            convert(plugin);
        } catch (Exception e) {

        }
    }

    public static void convert(JavaPlugin plugin) throws IOException {
        FileConfiguration config = plugin.getConfig();
        if (!config.isSet("config.version")) {
            // TODO new config
            config.set("config.selection_items.classes_selection_item", config.get("config.classes_selection_item"));
            config.set("config.selection_items.exit_item", config.get("config.exit_item"));
            config.set("config.selection_items.achievement_item", config.get("config.achievement_item"));
            config.set("config.selection_items.spectator_item", config.get("config.exit_item"));
            config.set("config.selection_items.shop_selection_item", config.get("config.shop_selection_item"));
            config.set("config.GUI.classes_gui_rows", config.get("config.classes_gui_rows"));
            config.set("config.GUI.shop_gui_rows", config.get("config.shop_gui_rows"));
            config.set("config.spectator.spectator_after_fall_or_death", config.get("config.spectator_after_fall_or_death"));
            config.set("config.spectator.spectator_move_y_lock", config.get("config.spectator_move_y_lock"));
            config.set("config.defaults.default_max_players", config.get("config.default_max_players"));
            config.set("config.defaults.default_min_players", config.get("config.default_min_players"));
            config.set("config.defaults.default_max_game_time_in_minutes", config.get("config.default_max_game_time_in_minutes"));
            config.set("config.countdowns.lobby_countdown", config.get("config.lobby_countdown"));
            config.set("config.countdowns.ingame_countdown", config.get("config.ingame_countdown"));
            config.set("config.countdowns.ingame_countdown_enabled", config.get("config.ingame_countdown_enabled"));
            config.set("config.countdowns.skip_lobby", config.get("config.skip_lobby"));
            config.set("config.countdowns.clearinv_while_ingamecountdown", config.get("config.clearinv_while_ingamecountdown"));

            config.set("config.classes_selection_item", null);
            config.set("config.exit_item", null);
            config.set("config.achievement_item", null);
            config.set("config.shop_selection_item", null);
            config.set("config.classes_gui_rows", null);
            config.set("config.shop_gui_rows", null);
            config.set("config.clearinv_while_ingamecountdown", null);
            config.set("config.spectator_after_fall_or_death", null);
            config.set("config.spectator_move_y_lock", null);
            config.set("config.default_max_players", null);
            config.set("config.default_min_players", null);
            config.set("config.default_max_game_time_in_minutes", null);
            config.set("config.lobby_countdown", null);
            config.set("config.ingame_countdown", null);
            config.set("config.ingame_countdown_enabled", null);
            config.set("config.skip_lobby", null);

            config.set("config.version", 1);
            plugin.saveConfig();
        }
    }

}
