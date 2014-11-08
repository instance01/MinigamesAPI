package com.comze_instancelabs.minigamesapi.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;

public class ArenaStartEvent extends ArenaEvent {

	public ArenaStartEvent(JavaPlugin plugin, Arena a) {
		super(plugin, a);
	}

}
