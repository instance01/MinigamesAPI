package com.comze_instancelabs.minigamesapi.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
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

public class ArenaScoreboard {

	// Scoreboard board;
	// Objective objective;
	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();
	HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	public ArenaScoreboard() {

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

					aobjective.get(arena.getName()).setDisplayName(pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getName()));

					for (String p___ : arena.getAllPlayers()) {
						if (!Validator.isPlayerOnline(p___)) {
							continue;
						}
						Player p_ = Bukkit.getPlayer(p___);
						if (!MinigamesAPI.getAPI().pinstances.get(plugin).global_lost.containsKey(p___)) {
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
									aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer("§a" + p_.getName())).setScore(score);
								} else {
									aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
								}
							} catch (Exception e) {
							}
						} else if (MinigamesAPI.getAPI().pinstances.get(plugin).global_lost.containsKey(p___)) {
							if (currentscore.containsKey(p___)) {
								int score = currentscore.get(p___);
								try {
									if (p_.getName().length() < 15) {
										ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName()));
										aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer("§c" + p_.getName())).setScore(score);
									} else {
										ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3)));
										aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer("§c" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
									}
								} catch (Exception e) {
								}
							}
						}
					}

					p.setScoreboard(ascore.get(arena.getName()));
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
}
