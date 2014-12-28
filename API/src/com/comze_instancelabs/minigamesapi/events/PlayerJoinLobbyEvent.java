package com.comze_instancelabs.minigamesapi.events;

import com.comze_instancelabs.minigamesapi.Arena;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoinLobbyEvent extends PlayerEvent {

    public PlayerJoinLobbyEvent(Player p, JavaPlugin plugin, Arena a) {
        super(p, plugin, a);
    }

}
