package com.comze_instancelabs.minigamesapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class MinigamesAPI extends JavaPlugin {

	static MinigamesAPI instance = null;
	
	public static HashMap<String, Arena> global_players = new HashMap<String, Arena>();
	
	int lobby_countdown = 30;
	int ingame_countdown = 10;
	
	public void onEnable(){
		instance = this;

		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loaded MinigamesAPI. We're on " + version + ".");
	
		//TODO setup Config
		
		//TODO setup NMS
		
		//TODO setup Vault
		
		//TODO setup Updater and Metrics
		
	}
	
	
	public void onDisable(){
		
	}
	
	public static MinigamesAPI getAPI(){
		return instance;
	}
	
	
}
