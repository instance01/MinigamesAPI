package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.util.Util;

public class MinigamesAPI extends JavaPlugin {

	static MinigamesAPI instance = null;
	public static Economy econ = null;
	public boolean economy = true;
	public boolean arcade = false;
	
	public static HashMap<JavaPlugin, PluginInstance> pinstances = new HashMap<JavaPlugin, PluginInstance>();

	public static HashMap<String, Arena> global_players = new HashMap<String, Arena>();
	public static HashMap<String, Arena> global_lost = new HashMap<String, Arena>();
	public static ArrayList<String> global_leftplayers = new ArrayList<String>();

	int lobby_countdown = 30;
	int ingame_countdown = 10;

	public void onEnable() {
		instance = this;

		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loaded MinigamesAPI. We're on " + version + ".");

		if (economy) {
			if (!setupEconomy()) {
				getLogger().severe(String.format("[%s] - No iConomy dependency found! Disabling Economy.", getDescription().getName()));
				economy = false;
			}
		}

		// TODO setup Updater and Metrics

	}

	public void onDisable() {

	}

	/**
	 * Sets up the API, stuff won't work without that
	 * 
	 * @param plugin_
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_) {
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		ClassesConfig classesconfig = new ClassesConfig(plugin_);
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, Util.loadArenas(plugin_)));
		Bukkit.getPluginManager().registerEvents(new ArenaListener(plugin_), plugin_);
		return instance;
	}

	public static MinigamesAPI getAPI() {
		return instance;
	}

	public static CommandHandler getCommandHandler() {
		return new CommandHandler();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
