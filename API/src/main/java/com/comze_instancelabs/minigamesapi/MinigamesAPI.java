package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.util.Util;

public class MinigamesAPI extends JavaPlugin {

	static MinigamesAPI instance = null;
	
	public static HashMap<JavaPlugin, PluginInstance> pinstances = new HashMap<JavaPlugin, PluginInstance>();
	
	public static HashMap<String, Arena> global_players = new HashMap<String, Arena>();
	public static HashMap<String, Arena> global_lost = new HashMap<String, Arena>();
	public static ArrayList<String> global_leftplayers = new ArrayList<String>();
	
	int lobby_countdown = 30;
	int ingame_countdown = 10;

	public void onEnable(){
		instance = this;

		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loaded MinigamesAPI. We're on " + version + ".");

		//TODO setup Vault
		
		//TODO setup Updater and Metrics
		
	}
	
	
	public void onDisable(){
		
	}
	
	/**
	 * Sets up the API, stuff won't work without that
	 * @param plugin_
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_){
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, Util.loadArenas(plugin_)));
		Bukkit.getPluginManager().registerEvents(new ArenaListener(plugin_), plugin_);
		return instance;
	}
	
	public static MinigamesAPI getAPI(){
		return instance;
	}
	
	public static CommandHandler getCommandHandler(){
		return new CommandHandler();
	}

	
}
