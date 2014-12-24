package com.comze_instancelabs.minigamesapi.events;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;

public class PlayerLeaveArenaEvent extends PlayerEvent {

	public PlayerLeaveArenaEvent(Player p, JavaPlugin plugin, Arena a) {
		super(p, plugin, a);
	}

}
