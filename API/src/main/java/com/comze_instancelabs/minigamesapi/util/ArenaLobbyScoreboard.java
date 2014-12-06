package com.comze_instancelabs.minigamesapi.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ArenaLobbyScoreboard {

	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();

	int initialized = 0; // 0 = false; 1 = true;
	boolean custom = false;

	PluginInstance pli;

	ArrayList<String> loaded_custom_strings = new ArrayList<String>();

	public ArenaLobbyScoreboard(PluginInstance pli, JavaPlugin plugin) {
		custom = plugin.getConfig().getBoolean("config.use_custom_scoreboard");
		initialized = 1;
		this.pli = pli;
		if (pli.getMessagesConfig().getConfig().isSet("messages.custom_lobby_scoreboard.")) {
			for (String configline : pli.getMessagesConfig().getConfig().getConfigurationSection("messages.custom_lobby_scoreboard.").getKeys(false)) {
				String line = ChatColor.translateAlternateColorCodes('&', pli.getMessagesConfig().getConfig().getString("messages.custom_lobby_scoreboard." + configline));
				loaded_custom_strings.add(line);
			}
		}
	}

	public void updateScoreboard(final JavaPlugin plugin, final Arena arena) {
		if (!arena.getShowScoreboard()) {
			return;
		}

		if (pli == null) {
			pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		}

		Bukkit.getScheduler().runTask(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				for (String p__ : arena.getAllPlayers()) {
					if (!Validator.isPlayerValid(plugin, p__, arena)) {
						return;
					}
					Player p = Bukkit.getPlayer(p__);
					if (!ascore.containsKey(p__)) {
						ascore.put(p__, Bukkit.getScoreboardManager().getNewScoreboard());
					}
					if (!aobjective.containsKey(p__)) {
						aobjective.put(p__, ascore.get(p__).registerNewObjective(p__, "dummy"));
						aobjective.get(p__).setDisplaySlot(DisplaySlot.SIDEBAR);
						aobjective.get(p__).setDisplayName(pli.getMessagesConfig().scoreboard_lobby_title.replaceAll("<arena>", arena.getInternalName()));
					}

					try {
						if (loaded_custom_strings.size() < 1) {
							return;
						}
						for (String line : loaded_custom_strings) {
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
							} else if (score_identifier.equalsIgnoreCase("<money>")) {
								score = (int) MinigamesAPI.econ.getBalance(p__);
							}
							if (line_.length() < 15) {
								// ascore.get(arena.getInternalName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_));
								aobjective.get(p__).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_)).setScore(score);
							} else {
								// ascore.get(arena.getInternalName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0,
								// Math.min(line_.length() - 3, 13))));
								aobjective.get(p__).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13)))).setScore(score);
							}
						}
						p.setScoreboard(ascore.get(p__));
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

			p.setScoreboard(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearScoreboard(String arenaname) {
		// TODO
	}
}
