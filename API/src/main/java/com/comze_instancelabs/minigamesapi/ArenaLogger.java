package com.comze_instancelabs.minigamesapi;

import org.bukkit.Bukkit;

public class ArenaLogger {

	public static void debug(String msg) {
		if (MinigamesAPI.debug) {
			Bukkit.getConsoleSender().sendMessage("[" + System.currentTimeMillis() + " MGLIB-DBG] " + msg);
		}
	}

}
