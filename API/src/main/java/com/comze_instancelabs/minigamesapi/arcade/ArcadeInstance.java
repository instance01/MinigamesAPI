package com.comze_instancelabs.minigamesapi.arcade;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArcadeInstance {

	// TODO Arcade Config
	// add minigames shuffle
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
		if (!players.contains(playername)) {
			players.add(playername);
		}
		if (players.size() >= min_players && !started) {
			startArcade();
			started = true;
		}
	}

	public void leaveArcade(final String playername) {
		if (players.contains(playername)) {
			players.remove(playername);
		}
		if (minigames.get(currentindex).getArenas().get(0).containsPlayer(playername)) {
			minigames.get(currentindex).getArenas().get(0).leavePlayer(playername, false);
		}
		Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				Util.teleportPlayerFixed(Bukkit.getPlayer(playername), arena.getMainLobbyTemp());
			}
		}, 20L);
		clean();
		if (players.size() < 2) {
			stopArcade();
		}
	}

	public void startArcade() {
		for (String p_ : players) {
			PluginInstance mg = minigames.get(currentindex);
			Arena a = mg.getArenas().get(0);
			if (a.getArenaState() == ArenaState.JOIN) {
				a.joinPlayerLobby(p_, this);
				Bukkit.getPlayer(p_).sendMessage(mg.getMessagesConfig().arcade_next_minigame.replaceAll("<minigame>", mg.getArenaListener().getName()));
			} else {
				this.nextMinigame();
			}
		}
	}

	public void stopArcade() {
		ArrayList<String> temp = new ArrayList<String>(players);
		for (String p_ : temp) {
			this.leaveArcade(p_);
		}
		players.clear();
		started = false;
		this.currentindex = 0;
	}

	public void nextMinigame() {
		// System.out.println(currentindex + " " + minigames.size());
		if (currentindex < minigames.size() - 1) {
			currentindex++;
		} else {
			stopArcade();
			return;
		}
		final ArcadeInstance ai = this;
		Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				for (String p_ : players) {
					PluginInstance mg = minigames.get(currentindex);
					mg.getArenas().get(0).joinPlayerLobby(p_, ai);
					Bukkit.getPlayer(p_).sendMessage(mg.getMessagesConfig().arcade_next_minigame.replaceAll("<minigame>", mg.getArenaListener().getName()));
				}
			}
		}, 30L);
	}

	public void clean() {
		ArrayList<String> rem = new ArrayList<String>();
		for (String p_ : this.players) {
			if (!Validator.isPlayerOnline(p_)) {
				rem.add(p_);
			}
		}
		for (String r : rem) {
			if (players.contains(r)) {
				players.remove(r);
			}
		}
	}

}
