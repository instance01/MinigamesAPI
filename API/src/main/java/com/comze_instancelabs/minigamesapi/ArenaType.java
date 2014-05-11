package com.comze_instancelabs.minigamesapi;

public enum ArenaType {
	
	/**
	 * Players wait until enough players joined and get all teleported to different spawns 
	 */
	MULTISPAWN,
	
	/**
	 * Default Arena type; Players wait until enough players joined and get all teleported to one spawn 
	 */
	SINGLESPAWN,
	
	/**
	 * Players just join the game whenever they like, no lobby countdowns etc.
	 */
	JUMPNRUN

}
