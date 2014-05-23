package com.comze_instancelabs.minigamesapi;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaSetup {

	// actually the most basic arena just needs a spawn and a lobby
	
	/**
	 * Sets the spawn for a single-spawn arena
	 * @param arenaname
	 * @param l Location of the spawn
	 */
	public static void setSpawn(JavaPlugin plugin, String arenaname, Location l){
		Util.saveComponentForArena(plugin, arenaname, "spawn0", l); //TODO test
	}

	/**
	 * Sets one of multiple spawns for a multi-spawn arena
	 * @param arenaname
	 * @param l Location of the spawn
	 * @param count Index of the spawn; if the given index is already set, the spawn location will be overwritten
	 */
	public static void setSpawn(JavaPlugin plugin, String arenaname, Location l, int count){
		Util.saveComponentForArena(plugin, arenaname, "spawn" + Integer.toString(count), l);
	}
	
	/**
	 * Sets the waiting lobby for an arena
	 * @param arenaname
	 * @param l Location of the lobby
	 */
	public static void setLobby(JavaPlugin plugin, String arenaname, Location l){
		Util.saveComponentForArena(plugin, arenaname, "lobby", l);
	}
	
	/**
	 * Sets the main lobby
	 * @param l Location of the lobby
	 */
	public static void setMainLobby(JavaPlugin plugin, Location l){
		Util.saveMainLobby(plugin, l);
	}
	
	
	//TODO bounds setup
	public static void setBoundaries(){
		
	}
	
	
	/**
	 * Saves a given arena if it was set up properly.
	 * @return Arena or null if setup failed
	 */
	public static Arena saveArena(JavaPlugin plugin, String arenaname){
		if(!Validator.isArenaValid(arenaname)){
			return null;
		}
		// TODO arena saving
		return new Arena(plugin, arenaname).initArena(null, null, null, null, 0, 0, false);
	}
	
}
