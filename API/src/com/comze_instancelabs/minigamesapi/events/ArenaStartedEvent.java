package com.comze_instancelabs.minigamesapi.events;

import com.comze_instancelabs.minigamesapi.Arena;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaStartedEvent extends ArenaEvent {

    public ArenaStartedEvent(JavaPlugin plugin, Arena a) {
        super(plugin, a);
    }

}
