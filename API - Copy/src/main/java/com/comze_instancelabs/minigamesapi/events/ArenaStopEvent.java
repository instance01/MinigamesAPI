package com.comze_instancelabs.minigamesapi.events;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;

public class ArenaStopEvent extends ArenaEvent {

	public ArenaStopEvent(JavaPlugin plugin, Arena a) {
		super(plugin, a);
	}

}
