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
				for (String playername : arena.getAllPlayers()) {
					if (!Validator.isPlayerValid(plugin, playername, arena)) {
						return;
					}
					Player p = Bukkit.getPlayer(playername);
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
						if (!ascore.containsKey(playername)) {
							ascore.put(playername, Bukkit.getScoreboardManager().getNewScoreboard());
						}
						if (!aobjective.containsKey(playername)) {
							aobjective.put(playername, ascore.get(playername).registerNewObjective(playername, "dummy"));
							aobjective.get(playername).setDisplaySlot(DisplaySlot.SIDEBAR);
							aobjective.get(playername).setDisplayName(pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getInternalName()));
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
									score = pli.getStatsInstance().getPoints(playername);
								} else if (score_identifier.equalsIgnoreCase("<wins>")) {
									score = pli.getStatsInstance().getWins(playername);
								} else if (score_identifier.equalsIgnoreCase("<money>")) {
									score = (int) MinigamesAPI.econ.getBalance(playername);
								}
								if (line_.length() < 15) {
									Util.resetScores(ascore.get(playername), ChatColor.GREEN + line_);
									Util.getScore(aobjective.get(playername), ChatColor.GREEN + line_).setScore(score);
								} else {
									Util.resetScores(ascore.get(playername), ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13)));
									Util.getScore(aobjective.get(playername), ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13))).setScore(score);
								}
							}

							if (ascore.get(playername) != null) {
								p.setScoreboard(ascore.get(playername));
							}
						} catch (Exception e) {
							System.out.println("Failed to set custom scoreboard: ");
							e.printStackTrace();
						}
					} else {
						for (String playername_ : arena.getAllPlayers()) {
							if (!Validator.isPlayerOnline(playername_)) {
								continue;
							}
							Player p_ = Bukkit.getPlayer(playername_);
							if (!pli.global_lost.containsKey(playername_)) {
								int score = 0;
								if (currentscore.containsKey(playername_)) {
									int oldscore = currentscore.get(playername_);
									if (score > oldscore) {
										currentscore.put(playername_, score);
									} else {
										score = oldscore;
									}
								} else {
									currentscore.put(playername_, score);
								}
								try {
									if (p_.getName().length() < 15) {
										Util.getScore(aobjective.get(arena.getInternalName()), ChatColor.GREEN + p_.getName()).setScore(0);
									} else {
										Util.getScore(aobjective.get(arena.getInternalName()), ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3)).setScore(0);
										;
									}
								} catch (Exception e) {
								}
							} else if (pli.global_lost.containsKey(playername_)) {
								try {
									if (currentscore.containsKey(playername_)) {
										int score = currentscore.get(playername_);
										if (p_.getName().length() < 15) {
											Util.resetScores(ascore.get(arena.getInternalName()), ChatColor.GREEN + p_.getName());
											Util.getScore(aobjective.get(arena.getInternalName()), ChatColor.RED + p_.getName()).setScore(0);
										} else {
											Util.resetScores(ascore.get(arena.getInternalName()), ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3));
											Util.getScore(aobjective.get(arena.getInternalName()), ChatColor.RED + p_.getName().substring(0, p_.getName().length() - 3)).setScore(0);
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
