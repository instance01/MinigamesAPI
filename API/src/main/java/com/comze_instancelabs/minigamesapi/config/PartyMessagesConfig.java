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

public class PartyMessagesConfig {

	private FileConfiguration messagesConfig = null;
	private File messagesFile = null;
	private JavaPlugin plugin = null;

	public PartyMessagesConfig(JavaPlugin plugin) {
		this.plugin = plugin;
		this.init();
	}

	public void init() {
		this.getConfig().addDefault("messages.cannot_invite_yourself", cannot_invite_yourself);
		this.getConfig().addDefault("messages.player_not_online", player_not_online);
		this.getConfig().addDefault("messages.you_invited", you_invited);
		this.getConfig().addDefault("messages.you_were_invited", you_were_invited);
		this.getConfig().addDefault("messages.not_invited_to_any_party", not_invited_to_any_party);
		this.getConfig().addDefault("messages.not_invited_to_players_party", not_invited_to_players_party);
		this.getConfig().addDefault("messages.player_not_in_party", player_not_in_party);
		this.getConfig().addDefault("messages.you_joined_party", you_joined_party);
		this.getConfig().addDefault("messages.player_joined_party", player_joined_party);
		this.getConfig().addDefault("messages.you_left_party", you_left_party);
		this.getConfig().addDefault("messages.player_left_party", player_left_party);
		this.getConfig().addDefault("messages.party_disbanded", party_disbanded);

		// save
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		// load
		this.cannot_invite_yourself = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.cannot_invite_yourself"));
		this.player_not_online = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player_not_online"));
		this.you_invited = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_invited"));
		this.you_were_invited = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_were_invited"));
		this.not_invited_to_any_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.not_invited_to_any_party"));
		this.not_invited_to_players_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.not_invited_to_players_party"));
		this.player_not_in_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player_not_in_party"));
		this.you_joined_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_joined_party"));
		this.player_joined_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player_joined_party"));
		this.you_left_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.you_left_party"));
		this.player_left_party = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player_left_party"));
		this.party_disbanded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.party_disbanded"));

	}

	public String cannot_invite_yourself = "&cYou cannot invite yourself!";
	public String player_not_online = "&4<player> &cis not online!";
	public String you_invited = "&aYou invited &2<player>&a!";
	public String you_were_invited = "&2<player> &ainvited you to join his/her party! Type &2/party accept <player> &ato accept.";
	public String not_invited_to_any_party = "&cYou are not invited to any party.";
	public String not_invited_to_players_party = "&cYou are not invited to the party of &4<player>&c.";
	public String player_not_in_party = "&4<player> &cis not in your party.";
	public String you_joined_party = "&7You joined the party of &8<player>&7.";
	public String player_joined_party = "&2<player> &ajoined the party.";
	public String you_left_party = "&7You left the party of &8<player>&7.";
	public String player_left_party = "&4<player> &cleft the party.";
	public String party_disbanded = "&cThe party was disbanded.";

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
			messagesFile = new File(plugin.getDataFolder(), "partymessages.yml");
		}
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

		InputStream defConfigStream = plugin.getResource("partymessages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			messagesConfig.setDefaults(defConfig);
		}
	}

}
