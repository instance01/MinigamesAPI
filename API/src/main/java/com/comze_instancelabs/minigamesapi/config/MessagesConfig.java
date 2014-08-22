package com.comze_instancelabs.minigamesapi.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.ArenaState;

public class MessagesConfig {

	private FileConfiguration messagesConfig = null;
	private File messagesFile = null;
	private JavaPlugin plugin = null;

	public MessagesConfig(JavaPlugin plugin) {
		this.plugin = plugin;
		for (int i = 0; i < 10; i++) {
			squares += Character.toString((char) 0x25A0);
		}
		this.init();
	}

	public static String squares = Character.toString((char) 0x25A0);

	public void init() {
		// all signs
		this.getConfig().options().header("Contains all messages for easy translation.");
		HashMap<String, String> namecol = ArenaState.getAllStateNameColors();
		for (String state : namecol.keySet()) {
			String color = namecol.get(state);
			this.getConfig().addDefault("signs." + state.toLowerCase() + ".0", color + "[]");
			this.getConfig().addDefault("signs." + state.toLowerCase() + ".1", color + "<arena>");
			this.getConfig().addDefault("signs." + state.toLowerCase() + ".2", color + "<count>/<maxcount>");
			this.getConfig().addDefault("signs." + state.toLowerCase() + ".3", color + "[]");
		}

		// Arcade sign
		this.getConfig().addDefault("signs.arcade.0", "[]");
		this.getConfig().addDefault("signs.arcade.1", "&cArcade");
		this.getConfig().addDefault("signs.arcade.2", "<count>/<maxcount>");
		this.getConfig().addDefault("signs.arcade.3", "[]");

		this.getConfig().addDefault("messages.no_perm", no_perm);
		this.getConfig().addDefault("messages.successfully_reloaded", successfully_reloaded);
		this.getConfig().addDefault("messages.successfully_set", successfully_set);
		this.getConfig().addDefault("messages.successfully_saved_arena", successfully_saved_arena);
		this.getConfig().addDefault("messages.arena_invalid", arena_invalid);
		this.getConfig().addDefault("messages.failed_saving_arena", failed_saving_arena);
		this.getConfig().addDefault("messages.broadcast_players_left", broadcast_players_left);
		this.getConfig().addDefault("messages.broadcast_player_joined", broadcast_player_joined);
		this.getConfig().addDefault("messages.player_died", player_died);
		this.getConfig().addDefault("messages.arena_action", arena_action);
		this.getConfig().addDefault("messages.you_already_are_in_arena", you_already_are_in_arena);
		this.getConfig().addDefault("messages.you_joined_arena", you_joined_arena);
		this.getConfig().addDefault("messages.not_in_arena", not_in_arena);
		this.getConfig().addDefault("messages.teleporting_to_arena_in", teleporting_to_arena_in);
		this.getConfig().addDefault("messages.starting_in", starting_in);
		this.getConfig().addDefault("messages.failed_removing_arena", failed_removing_arena);
		this.getConfig().addDefault("messages.successfully_removed", successfully_removed);
		this.getConfig().addDefault("messages.failed_removing_component", failed_removing_component);
		this.getConfig().addDefault("messages.joined_arena", joined_arena);
		this.getConfig().addDefault("messages.you_won", you_won);
		this.getConfig().addDefault("messages.you_lost", you_lost);
		this.getConfig().addDefault("messages.you_got_a_kill", you_got_a_kill);
		this.getConfig().addDefault("messages.arena_not_initialized", arena_not_initialized);
		this.getConfig().addDefault("messages.guns.attributelevel_increased", attributelevel_increased);
		this.getConfig().addDefault("messages.guns.not_enough_credits", not_enough_credits);
		this.getConfig().addDefault("messages.guns.too_many_main_guns", too_many_main_guns);
		this.getConfig().addDefault("messages.guns.successfully_set_main_gun", successfully_set_main_gun);
		this.getConfig().addDefault("messages.guns.all_guns", all_guns);
		this.getConfig().addDefault("messages.arcade_next_minigame", arcade_next_minigame);
		this.getConfig().addDefault("messages.arcade_joined_waiting", arcade_joined_waiting);
		this.getConfig().addDefault("messages.arena_disabled", arena_disabled);
		this.getConfig().addDefault("messages.you_can_leave_with", you_can_leave_with);
		this.getConfig().addDefault("messages.no_perm_to_join_arena", no_perm_to_join_arena);
		this.getConfig().addDefault("messages.set_kit", set_kit);

		// save
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		// load
		this.no_perm = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no_perm"));
		this.successfully_reloaded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_reloaded"));
		this.successfully_set = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_set"));
		this.successfully_saved_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_saved_arena"));
		this.failed_saving_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.failed_saving_arena"));
		this.arena_invalid = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arena_invalid"));
		this.player_died = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player_died"));
		this.broadcast_players_left = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.broadcast_players_left"));
		this.broadcast_player_joined = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.broadcast_player_joined"));
		this.arena_action = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arena_action"));
		this.you_already_are_in_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_already_are_in_arena"));
		this.you_joined_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_joined_arena"));
		this.not_in_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.not_in_arena"));
		this.teleporting_to_arena_in = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.teleporting_to_arena_in"));
		this.starting_in = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.starting_in"));
		this.failed_removing_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.failed_removing_arena"));
		this.successfully_removed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_removed"));
		this.failed_removing_component = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.failed_removing_component"));
		this.joined_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.joined_arena"));
		this.you_won = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_won"));
		this.you_lost = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_lost"));
		this.you_got_a_kill = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_got_a_kill"));
		this.arena_not_initialized = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arena_not_initialized"));
		this.arcade_next_minigame = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arcade_next_minigame"));
		this.arena_disabled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arena_disabled"));
		this.you_can_leave_with = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_can_leave_with"));
		this.arcade_joined_waiting = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arcade_joined_waiting"));
		this.no_perm_to_join_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no_perm_to_join_arena"));
		this.set_kit = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.set_kit"));

		this.attributelevel_increased = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.guns.attributelevel_increased"));
		this.not_enough_credits = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.guns.not_enough_credits"));
		this.too_many_main_guns = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.guns.too_many_main_guns"));
		this.successfully_set_main_gun = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.guns.successfully_set_main_gun"));
		this.all_guns = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.guns.all_guns"));

	}

	public String no_perm = "&cYou don't have permission.";
	public String successfully_reloaded = "&aSuccessfully reloaded all configs.";
	public String successfully_set = "&aSuccessfully set &3<component>&a.";
	public String successfully_saved_arena = "&aSuccessfully saved &3<arena>&a.";
	public String failed_saving_arena = "&cFailed to save &3<arena>&c.";
	public String failed_removing_arena = "&cFailed to remove &3<arena>&c.";
	public String arena_invalid = "&3<arena> &cappears to be invalid.";
	public String broadcast_players_left = "&eThere are &4<count> &eplayers left!";
	public String broadcast_player_joined = "&2<player> &ajoined the arena! (<count>/<maxcount>)";
	public String player_died = "&c<player> died.";
	public String arena_action = "&aYou <action> arena &3<arena>&a!";
	public String you_joined_arena = "&aYou joined arena &3<arena>&a!";
	public String you_already_are_in_arena = "&aYou already seem to be in arena &3<arena>&a!";
	public String arena_not_initialized = "&cThe arena appears to be not initialized, did you save the arena?";
	public String not_in_arena = "&cYou don't seem to be in an arena right now.";
	public String teleporting_to_arena_in = "&7Teleporting to arena in <count>.";
	public String starting_in = "&aStarting in <count>!";
	public String successfully_removed = "&cSuccessfully removed &3<component>&c!";
	public String failed_removing_component = "&cFailed removing &3<component>&c. <cause>.";
	public String joined_arena = "&aYou joined &3<arena>&a.";
	public String you_won = "&aYou &2won &athe game!";
	public String you_lost = "&cYou &4lost &cthe game.";
	public String you_got_a_kill = "&aYou killed &2<player>! &2+10";
	public String attributelevel_increased = "&aThe <attribute> level was increased successfully!";
	public String not_enough_credits = "&cThe max level of 3 was reached or you don't have enough credits. Needed: <credits>";
	public String too_many_main_guns = "&cYou already have 2 main guns, remove one first.";
	public String successfully_set_main_gun = "&aSuccessfully set a main gun (of a maximum of two).";
	public String arcade_next_minigame = "&6Next Arcade game: &4<minigame>&6!";
	public String arena_disabled = "&cThe arena is disabled thus you can't join.";
	public String all_guns = "&aYour current main guns: &2<guns>";
	public String you_can_leave_with = "&cYou can leave with /leave!";
	public String arcade_joined_waiting = "&6You joined Arcade! Waiting for <count> more players to start.";
	public String no_perm_to_join_arena = "&cYou don't have permission (arenas.<arena>) to join this arena as it's vip!";
	public String set_kit = "&aSuccessfully set &2<kit>&a!";

	public FileConfiguration getConfig() {
		if (messagesConfig == null) {
			reloadConfig();
		}
		return messagesConfig;
	}

	public void saveConfig() {
		if (messagesConfig == null || messagesFile == null) {
			return;
		}
		try {
			getConfig().save(messagesFile);
		} catch (IOException ex) {

		}
	}

	public void reloadConfig() {
		if (messagesFile == null) {
			messagesFile = new File(plugin.getDataFolder(), "messages.yml");
		}
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

		InputStream defConfigStream = plugin.getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			messagesConfig.setDefaults(defConfig);
		}
	}

}
