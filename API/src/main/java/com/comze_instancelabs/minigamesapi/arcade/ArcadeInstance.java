package com.comze_instancelabs.minigamesapi.arcade;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArcadeInstance {

	public ArrayList<PluginInstance> minigames = new ArrayList<PluginInstance>();
	int currentindex = 0;
	public ArrayList<String> players = new ArrayList<String>();
	Arena arena;
	JavaPlugin plugin;

	boolean started;

	public ArcadeInstance(JavaPlugin plugin, ArrayList<PluginInstance> minigames, Arena arena) {
		this.minigames = minigames;
		this.arena = arena;
		this.plugin = plugin;
	}

	// TODO max 16 players!
	public void joinArcade(String playername) {
		if (!players.contains(playername)) {
			players.add(playername);
		}
		if (players.size() >= plugin.getConfig().getInt("config.arcade.min_players")) {
			if (!started) {
				startArcade();
			}
			Bukkit.getPlayer(playername).sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arcade_joined_waiting.replaceAll("<count>", "0"));
		} else {
			Bukkit.getPlayer(playername).sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arcade_joined_waiting.replaceAll("<count>", Integer.toString(plugin.getConfig().getInt("config.arcade.min_players") - players.size())));
		}
	}

	public void leaveArcade(final String playername) {
		if (players.contains(playername)) {
			players.remove(playername);
		}
		if (minigames.get(currentindex).getArenas().size() > 0) {
			if (minigames.get(currentindex).getArenas().get(0).containsPlayer(playername)) {
				minigames.get(currentindex).getArenas().get(0).leavePlayer(playername, false, false);
			}
		}
		Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				Player p = Bukkit.getPlayer(playername);
				if (p != null) {
					Util.teleportPlayerFixed(p, arena.getMainLobbyTemp());
				}
			}
		}, 20L);
		clean();
		if (players.size() < 2) {
			stopArcade();
		}
	}

	int currentlobbycount = 31;
	int currenttaskid = 0;

	public void startArcade() {
		if (started) {
			return;
		}
		started = true;
		Collections.shuffle(minigames);

		currentlobbycount = plugin.getConfig().getInt("config.arcade.lobby_countdown") + 1;
		final ArcadeInstance ai = this;
		final PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);

		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				currentlobbycount--;
				if (currentlobbycount == 60 || currentlobbycount == 30 || currentlobbycount == 15 || currentlobbycount == 10 || currentlobbycount < 6) {
					for (String p_ : ai.players) {
						if (Validator.isPlayerOnline(p_)) {
							Player p = Bukkit.getPlayer(p_);
							p.sendMessage(pli.getMessagesConfig().starting_in.replaceAll("<count>", Integer.toString(currentlobbycount)));
						}
					}
				}
				if (currentlobbycount < 1) {
					currentindex--;
					ai.nextMinigame();
					try {
						Bukkit.getScheduler().cancelTask(currenttaskid);
					} catch (Exception e) {
					}
				}
			}
		}, 5L, 20).getTaskId();
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

	public void stopCurrentMinigame() {
		if (currentindex < minigames.size()) {
			if (minigames.get(currentindex).getArenas().size() > 0) {
				minigames.get(currentindex).getArenas().get(0).stop();
			}
		}
	}

	public void nextMinigame() {
		nextMinigame(30L);
	}

	public void nextMinigame(long delay) {
		if (currentindex < minigames.size() - 1) {
			currentindex++;
		} else {
			stopArcade();
			return;
		}
		//System.out.println(delay + " " + currentindex);
		final ArcadeInstance ai = this;
		Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				ArrayList<String> temp = new ArrayList<String>(players);

				PluginInstance mg = minigames.get(currentindex);
				if (mg.getPlugin().getConfig().getBoolean("config.arcade.enabled")) {
					Arena a = null;
					if (mg.getPlugin().getConfig().getBoolean("config.arcade.arena_to_prefer.enabled")) {
						String arenaname = mg.getPlugin().getConfig().getString("config.arcade.arena_to_prefer.arena");
						a = mg.getArenaByName(arenaname);
						if (a == null) {
							for (Arena a_ : mg.getArenas()) {
								if (a_.getArenaState() == ArenaState.JOIN || a_.getArenaState() == ArenaState.STARTING) {
									a = a_;
									break;
								}
							}
						}
					} else {
						for (Arena a_ : mg.getArenas()) {
							if (a_.getArenaState() == ArenaState.JOIN || a_.getArenaState() == ArenaState.STARTING) {
								a = a_;
								break;
							}
						}
					}
					if (a != null) {
						for (String p_ : temp) {
							a.joinPlayerLobby(p_, ai);
							Bukkit.getPlayer(p_).sendMessage(mg.getMessagesConfig().arcade_next_minigame.replaceAll("<minigame>", mg.getArenaListener().getName()));
						}
					} else {
						nextMinigame(5L);
					}
				} else {
					nextMinigame(5L);
				}
			}
		}, delay);
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
