package com.comze_instancelabs.minigamesapi.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.util.Validator;
import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ArenaLobbyScoreboard {

	// Scoreboard board;
	// Objective objective;
	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();
	HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	public ArenaLobbyScoreboard() {

	}

	public void updateScoreboard(final JavaPlugin plugin, final Arena arena) {
		if (!arena.getShowScoreboard()) {
			return;
		}

		final PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);

		Bukkit.getScheduler().runTask(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				for (String p__ : arena.getAllPlayers()) {
					if (!Validator.isPlayerValid(plugin, p__, arena)) {
						return;
					}
					Player p = Bukkit.getPlayer(p__);
					if (!ascore.containsKey(arena.getName())) {
						ascore.put(arena.getName(), Bukkit.getScoreboardManager().getNewScoreboard());
					}
					if (!aobjective.containsKey(arena.getName())) {
						aobjective.put(arena.getName(), ascore.get(arena.getName()).registerNewObjective(arena.getName(), "dummy"));
					}

					aobjective.get(arena.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);

					aobjective.get(arena.getName()).setDisplayName(pli.getMessagesConfig().scoreboard_lobby_title.replaceAll("<arena>", arena.getName()));

					try {
						if (pli.getMessagesConfig().getConfig().isSet("messages.custom_lobby_scoreboard.")) {
							for (String configline : pli.getMessagesConfig().getConfig().getConfigurationSection("messages.custom_lobby_scoreboard.").getKeys(false)) {
								String line = ChatColor.translateAlternateColorCodes('&', pli.getMessagesConfig().getConfig().getString("messages.custom_lobby_scoreboard." + configline));
								String[] line_arr = line.split(":");
								String line_ = line_arr[0];
								String score_identifier = line_arr[line_arr.length - 1];
								if (line_arr.length > 2) {
									line_ += ":" + line_arr[1];
								}
								int score = 0;
								if (score_identifier.equalsIgnoreCase("<playercount>")) {
									score = arena.getAllPlayers().size();
								} else if (score_identifier.equalsIgnoreCase("<maxplayercount>")) {
									score = arena.getMaxPlayers();
								} else if (score_identifier.equalsIgnoreCase("<points>")) {
									score = pli.getStatsInstance().getPoints(p__);
								} else if (score_identifier.equalsIgnoreCase("<wins>")) {
									score = pli.getStatsInstance().getWins(p__);
								}
								if (line_.length() < 15) {
									ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_));
									aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_)).setScore(score);
								} else {
									ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13))));
									aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13)))).setScore(score);
								}
							}
						}
						p.setScoreboard(ascore.get(arena.getName()));
					} catch (Exception e) {
						System.out.println("Failed to set custom scoreboard: ");
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void removeScoreboard(String arena, Player p) {
		try {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sc = manager.getNewScoreboard();
			try {
				if (p.getName().length() < 15) {
					ascore.get(arena).resetScores(Bukkit.getOfflinePlayer("§c" + p.getName()));
					ascore.get(arena).resetScores(Bukkit.getOfflinePlayer("§a" + p.getName()));
				} else {
					ascore.get(arena).resetScores(Bukkit.getOfflinePlayer("§c" + p.getName().substring(0, p.getName().length() - 3)));
					ascore.get(arena).resetScores(Bukkit.getOfflinePlayer("§a" + p.getName().substring(0, p.getName().length() - 3)));
				}

			} catch (Exception e) {
			}

			sc.clearSlot(DisplaySlot.SIDEBAR);
			p.setScoreboard(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCurrentScoreMap(HashMap<String, Integer> newcurrentscore) {
		this.currentscore = newcurrentscore;
	}

	public void clearScoreboard(String arenaname) {
		if (ascore.containsKey(arenaname)) {
			ascore.remove(arenaname);
		}
		if (aobjective.containsKey(arenaname)) {
			aobjective.remove(arenaname);
		}

		// ascore.put(arenaname, Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
