package com.comze_instancelabs.minigamesapi.events;

import com.comze_instancelabs.minigamesapi.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private JavaPlugin plugin;
    private Player player;

    public PlayerEvent(Player p, JavaPlugin plugin, Arena a) {
        this.arena = a;
        this.plugin = plugin;
        this.player = p;
    }

    public Arena getArena() {
        return arena;
    }

    public Player getPlayer() {
        return player;
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
