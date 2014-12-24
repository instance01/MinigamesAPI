package com.comze_instancelabs.minigamesapi;

public class ArenaLogger {

	public static void debug(String msg) {
		if (MinigamesAPI.debug) {
			System.out.println("[" + System.currentTimeMillis() + " MGLIB-DBG] " + msg);
		}
	}

}
