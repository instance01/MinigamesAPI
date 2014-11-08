package com.comze_instancelabs.minigamesapi.events;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;

public class ArenaStartedEvent extends ArenaEvent {

	public ArenaStartedEvent(JavaPlugin plugin, Arena a) {
		super(plugin, a);
	}

}
