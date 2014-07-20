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
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;
import com.comze_instancelabs.minigamesapi.util.Util;

public class MinigamesAPI extends JavaPlugin {

	static MinigamesAPI instance = null;
	public static Economy econ = null;
	public static boolean economy = true;
	public static boolean arcade = false;

	public static HashMap<JavaPlugin, PluginInstance> pinstances = new HashMap<JavaPlugin, PluginInstance>();

	//public static HashMap<String, Arena> global_players = new HashMap<String, Arena>();
	//public static HashMap<String, Arena> global_lost = new HashMap<String, Arena>();
	public static ArrayList<String> global_leftplayers = new ArrayList<String>();

	public void onEnable() {
		instance = this;

		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loaded MinigamesAPI. We're on " + version + ".");
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

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
	 * Sets up the API allowing to override all configs
	 * 
	 * @param plugin_
	 * @param arenaclass
	 * @param arenasconfig
	 * @param messagesconfig
	 * @param classesconfig
	 * @param statsconfig
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame, Class<?> arenaclass, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig, StatsConfig statsconfig, DefaultConfig defaultconfig, boolean customlistener) {
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>()));
		if (!customlistener) {
			Bukkit.getPluginManager().registerEvents(new ArenaListener(plugin_, pinstances.get(plugin_), minigame), plugin_);
		}
		Classes.loadClasses(plugin_);
		return instance;
	}

	public static void registerArenaListenerLater(JavaPlugin plugin_, ArenaListener arenalistener) {
		Bukkit.getPluginManager().registerEvents(arenalistener, plugin_);
	}

	public static void registerArenaSetup(JavaPlugin plugin_, ArenaSetup arenasetup) {
		pinstances.get(plugin_).arenaSetup = arenasetup;
	}

	public static void registerScoreboard(JavaPlugin plugin_, ArenaScoreboard board) {
		pinstances.get(plugin_).scoreboardManager = board;
	}

	/**
	 * Sets up the API, stuff won't work without that
	 * 
	 * @param plugin_
	 * @return
	 */
	// Allow loading of arenas with own extended arena class into
	// PluginInstance:
	// after this setup, get the PluginInstance, load the arenas by yourself
	// and add the loaded arenas w/ custom arena class into the PluginInstance
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame, Class<?> arenaclass) {
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		ClassesConfig classesconfig = new ClassesConfig(plugin_, false);
		StatsConfig statsconfig = new StatsConfig(plugin_, false);
		DefaultConfig.init(plugin_, false);
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>()));
		Bukkit.getPluginManager().registerEvents(new ArenaListener(plugin_, pinstances.get(plugin_), minigame), plugin_);
		Classes.loadClasses(plugin_);
		return instance;
	}

	/**
	 * Sets up the API, stuff won't work without that
	 * 
	 * @param plugin_
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame) {
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		ClassesConfig classesconfig = new ClassesConfig(plugin_, false);
		StatsConfig statsconfig = new StatsConfig(plugin_, false);
		DefaultConfig.init(plugin_, false);
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig));
		pinstances.get(plugin_).addLoadedArenas(Util.loadArenas(plugin_, arenasconfig));
		Bukkit.getPluginManager().registerEvents(new ArenaListener(plugin_, pinstances.get(plugin_), minigame), plugin_);
		Classes.loadClasses(plugin_);
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
