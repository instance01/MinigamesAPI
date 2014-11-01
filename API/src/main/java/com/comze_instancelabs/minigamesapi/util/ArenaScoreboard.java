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

public class ArenaScoreboard {

	// Scoreboard board;
	// Objective objective;
	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();
	HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	int initialized = 0; // 0 = false; 1 = true;
	boolean custom = false;

	PluginInstance pli;

	ArrayList<String> loaded_custom_strings = new ArrayList<String>();

	public ArenaScoreboard() {

	}

	public ArenaScoreboard(PluginInstance pli, JavaPlugin plugin) {
		custom = plugin.getConfig().getBoolean("config.use_custom_scoreboard");
		initialized = 1;
		this.pli = pli;
		if (pli.getMessagesConfig().getConfig().isSet("messages.custom_scoreboard.")) {
			for (String configline : pli.getMessagesConfig().getConfig().getConfigurationSection("messages.custom_scoreboard.").getKeys(false)) {
				String line = ChatColor.translateAlternateColorCodes('&', pli.getMessagesConfig().getConfig().getString("messages.custom_scoreboard." + configline));
				loaded_custom_strings.add(line);
			}
		}
	}

	public void updateScoreboard(final JavaPlugin plugin, final Arena arena) {
		if (!arena.getShowScoreboard()) {
			return;
		}

		if (initialized != 1) {
			custom = plugin.getConfig().getBoolean("config.use_custom_scoreboard");
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
					if (!ascore.containsKey(arena.getName())) {
						ascore.put(arena.getName(), Bukkit.getScoreboardManager().getNewScoreboard());
					}
					if (!aobjective.containsKey(arena.getName())) {
						aobjective.put(arena.getName(), ascore.get(arena.getName()).registerNewObjective(arena.getName(), "dummy"));
					}

					aobjective.get(arena.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);

					aobjective.get(arena.getName()).setDisplayName(pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getName()));

					if (custom) {
						try {
							for (String line : loaded_custom_strings) {
								String[] line_arr = line.split(":");
								String line_ = line_arr[0];
								String score_identifier = line_arr[1];
								int score = 0;
								if (score_identifier.equalsIgnoreCase("<playercount>")) {
									score = arena.getAllPlayers().size();
								} else if (score_identifier.equalsIgnoreCase("<lostplayercount>")) {
									score = arena.getAllPlayers().size() - arena.getPlayerAlive();
								} else if (score_identifier.equalsIgnoreCase("<playeralivecount>")) {
									score = arena.getPlayerAlive();
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

							if (ascore.get(arena.getName()) != null) {
								p.setScoreboard(ascore.get(arena.getName()));
							}
						} catch (Exception e) {
							System.out.println("Failed to set custom scoreboard: ");
							e.printStackTrace();
						}
						continue;
					}

					for (String p___ : arena.getAllPlayers()) {
						if (!Validator.isPlayerOnline(p___)) {
							continue;
						}
						Player p_ = Bukkit.getPlayer(p___);
						if (!pli.global_lost.containsKey(p___)) {
							int score = 0;
							if (currentscore.containsKey(p___)) {
								int oldscore = currentscore.get(p___);
								if (score > oldscore) {
									currentscore.put(p___, score);
								} else {
									score = oldscore;
								}
							} else {
								currentscore.put(p___, score);
							}
							try {
								if (p_.getName().length() < 15) {
									aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName())).setScore(score);
								} else {
									aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
								}
							} catch (Exception e) {
							}
						} else if (pli.global_lost.containsKey(p___)) {
							if (currentscore.containsKey(p___)) {
								int score = currentscore.get(p___);
								try {
									if (p_.getName().length() < 15) {
										ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName()));
										aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.RED + p_.getName())).setScore(score);
									} else {
										ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3)));
										aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.RED + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
									}
								} catch (Exception e) {
								}
							}
						}
					}

					if (ascore.get(arena.getName()) != null) {
						p.setScoreboard(ascore.get(arena.getName()));
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
