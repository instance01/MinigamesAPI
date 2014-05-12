package com.comze_instancelabs.minigamesapi;

import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaSetup {

	// actually the most basic arena just needs a spawn and a lobby
	
	
	public static void setSpawn(String arenaname){
		
	}

	public static void setSpawn(String arenaname, int count){
		
	}
	
	public static void setLobby(String arenaname){
		
	}
	
	/**
	 * Saves a given arena if it was set up properly.
	 * @return Arena or null if setup failed
	 */
	public static Arena saveArena(String arenaname){
		if(Validator.isArenaValid(arenaname)){
			return null;
		}
		// TODO arena saving
		return new Arena(arenaname).initArena(null, null, null, null, 0, 0, false);
	}
	
}
