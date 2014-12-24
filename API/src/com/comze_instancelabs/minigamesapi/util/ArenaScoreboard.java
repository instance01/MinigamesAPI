package com.comze_instancelabs.minigamesapi.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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

	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();
	HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	int initialized = 0; // 0 = false; 1 = true;
	boolean custom = false;

	PluginInstance pli;

	ArrayList<String> loaded_custom_strings = new ArrayList<String>();

	public ArenaScoreboard() {
		//
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
					if (!custom) {
						if (!ascore.containsKey(arena.getInternalName())) {
							ascore.put(arena.getInternalName(), Bukkit.getScoreboardManager().getNewScoreboard());
						}
						if (!aobjective.containsKey(arena.getInternalName())) {
							aobjective.put(arena.getInternalName(), ascore.get(arena.getInternalName()).registerNewObjective(arena.getInternalName(), "dummy"));
							aobjective.get(arena.getInternalName()).setDisplaySlot(DisplaySlot.SIDEBAR);
							aobjective.get(arena.getInternalName()).setDisplayName(pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getInternalName()));
						}
					} else {
						if (!ascore.containsKey(p__)) {
							ascore.put(p__, Bukkit.getScoreboardManager().getNewScoreboard());
						}
						if (!aobjective.containsKey(p__)) {
							aobjective.put(p__, ascore.get(p__).registerNewObjective(p__, "dummy"));
							aobjective.get(p__).setDisplaySlot(DisplaySlot.SIDEBAR);
							aobjective.get(p__).setDisplayName(pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getInternalName()));
						}
					}

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
								} else if (score_identifier.equalsIgnoreCase("<money>")) {
									score = (int) MinigamesAPI.econ.getBalance(p__);
								}
								if (line_.length() < 15) {
									ascore.get(p__).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_));
									aobjective.get(p__).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_)).setScore(score);
								} else {
									ascore.get(p__).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13))));
									aobjective.get(p__).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13)))).setScore(score);
								}
							}

							if (ascore.get(p__) != null) {
								p.setScoreboard(ascore.get(p__));
							}
						} catch (Exception e) {
							System.out.println("Failed to set custom scoreboard: ");
							e.printStackTrace();
						}
					} else {
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
										aobjective.get(arena.getInternalName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName())).setScore(0);
									} else {
										aobjective.get(arena.getInternalName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3))).setScore(0);
									}
								} catch (Exception e) {
								}
							} else if (pli.global_lost.containsKey(p___)) {
								try {
									if (currentscore.containsKey(p___)) {
										int score = currentscore.get(p___);
										if (p_.getName().length() < 15) {
											ascore.get(arena.getInternalName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName()));
											aobjective.get(arena.getInternalName()).getScore(Bukkit.getOfflinePlayer(ChatColor.RED + p_.getName())).setScore(0);
										} else {
											ascore.get(arena.getInternalName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3)));
											aobjective.get(arena.getInternalName()).getScore(Bukkit.getOfflinePlayer(ChatColor.RED + p_.getName().substring(0, p_.getName().length() - 3))).setScore(0);
										}
									}
								} catch (Exception e) {
								}
							}
						}

						if (ascore.get(arena.getInternalName()) != null) {
							p.setScoreboard(ascore.get(arena.getInternalName()));
						}
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
		if (ascore.containsKey(arenaname)) {
			try {
				Scoreboard sc = ascore.get(arenaname);
				for (OfflinePlayer player : sc.getPlayers()) {
					sc.resetScores(player);
				}
			} catch (Exception e) {
				if (MinigamesAPI.debug) {
					e.printStackTrace();
				}
			}
			ascore.remove(arenaname);
		}
		if (aobjective.containsKey(arenaname)) {
			aobjective.remove(arenaname);
		}

		// ascore.put(arenaname, Bukkit.getScoreboardManager().getNewScoreboard());
	}

	public void setCurrentScoreMap(HashMap<String, Integer> newcurrentscore) {
		this.currentscore = newcurrentscore;
	}
}
