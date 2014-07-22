package com.comze_instancelabs.minigamesapi.arcade;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Util;

public class ArcadeInstance {

	// TODO Arcade Config
	public ArrayList<PluginInstance> minigames = new ArrayList<PluginInstance>();
	int currentindex = 0;
	int min_players = 1;
	int max_players = 16;
	public ArrayList<String> players = new ArrayList<String>();
	Arena arena;

	boolean started;

	public ArcadeInstance(ArrayList<PluginInstance> minigames, Arena arena) {
		this.minigames = minigames;
		this.arena = arena;
	}

	public void joinArcade(String playername) {
		players.add(playername);
		if (players.size() >= min_players) {
			startArcade();
		}
	}

	public void leaveArcade(String playername) {
		if (players.contains(playername)) {
			players.remove(playername);
		}
		Util.teleportPlayerFixed(Bukkit.getPlayer(playername), arena.getMainLobbyTemp());
		if(minigames.get(currentindex).getArenas().get(0).containsPlayer(playername)){
			minigames.get(currentindex).getArenas().get(0).leavePlayer(playername, false);
		}
	}

	public void startArcade() {
		for (String p_ : players) {
			Arena a = minigames.get(currentindex).getArenas().get(0);
			if (a.getArenaState() == ArenaState.JOIN) {
				a.joinPlayerLobby(p_, this);
			} else {
				this.nextMinigame();
			}
		}
	}

	public void stopArcade() {
		Util.teleportAllPlayers(players, arena.getMainLobbyTemp());
		players.clear();
	}

	public void nextMinigame() {
		if (currentindex < minigames.size() - 1) {
			currentindex++;
		} else {
			stopArcade();
		}
		Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				for (String p_ : players) {
					minigames.get(currentindex).getArenas().get(0).joinPlayerLobby(p_);
				}
			}
		}, 30L);
	}

}
