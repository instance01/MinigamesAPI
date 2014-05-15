package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Arrays;

public enum ArenaState {

	JOIN,
	STARTING,
	INGAME,
	RESTARTING;
	

	public static ArrayList<String> getAllStateNames(){
		return new ArrayList<String>(Arrays.asList("JOIN", "STARTING", "INGAME", "RESTARTING"));
	}
}
