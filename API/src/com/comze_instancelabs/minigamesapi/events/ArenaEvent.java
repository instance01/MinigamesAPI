package com.comze_instancelabs.minigamesapi.events;

import com.comze_instancelabs.minigamesapi.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private JavaPlugin plugin;

    public ArenaEvent(JavaPlugin plugin, Arena a) {
        this.arena = a;
        this.plugin = plugin;
    }

    public Arena getArena() {
        return arena;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
