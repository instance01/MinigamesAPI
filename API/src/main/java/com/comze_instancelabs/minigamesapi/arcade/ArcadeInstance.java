package com.comze_instancelabs.minigamesapi.arcade;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArcadeInstance {

	public ArrayList<PluginInstance> minigames = new ArrayList<PluginInstance>();
	int currentindex = 0;
	public ArrayList<String> players = new ArrayList<String>();

	boolean started;

	public ArcadeInstance(ArrayList<PluginInstance> minigames) {
		this.minigames = minigames;
	}

	public void joinArcade(String playername) {
		minigames.get(currentindex).getArenas().get(0).joinPlayerLobby(playername);
	}

	// TODO Arcade needs a mainlobby!

	public void startArcade() {
		// TODO autostart arena if enough players
		// TODO Arcade Config
	}

	public void stopArcade() {
		// TODO tp to mainlobby
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
