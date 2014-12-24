package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public enum ArenaState {

	JOIN, STARTING, INGAME, RESTARTING;

	public static ArrayList<String> getAllStateNames() {
		return new ArrayList<String>(Arrays.asList("JOIN", "STARTING", "INGAME", "RESTARTING"));
	}

	public static HashMap<String, String> getAllStateNameColors() {
		HashMap<String, String> ret = new HashMap<String, String>() {
			{
				put("JOIN", "&a");
				put("STARTING", "&a");
				put("INGAME", "&4");
				put("RESTARTING", "&e");
			}
		};
		return ret;
	}
}
