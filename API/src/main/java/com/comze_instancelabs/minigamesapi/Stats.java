package com.comze_instancelabs.minigamesapi;

import org.bukkit.plugin.java.JavaPlugin;

public class Stats {

	// will be used for wins and points
	// you can get points for pretty much everything in the games
	// but these points are just for top stats, nothing more
	
	//TODO add StatsConfig
	
	private JavaPlugin plugin = null;
	
	public Stats(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	public void addWin(String playername){
		// save into stats config
	}
	
	public void addPoints(String playername){
		// save into stats config
	}
	
	public void getPoints(String playername){
		
	}
	
	public void getWins(String playername){
		
	}
	
	public void getTop(int count){
		
	}
	
}
