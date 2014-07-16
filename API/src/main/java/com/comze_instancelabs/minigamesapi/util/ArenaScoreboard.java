package com.comze_instancelabs.minigamesapi.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class ArenaScoreboard {

	static Scoreboard board;
	static Objective objective;
	public static HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	public ArenaScoreboard() {

	}

	public void updateScoreboard(final JavaPlugin plugin, final Arena arena) {

		Bukkit.getScheduler().runTask(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				for (String p__ : arena.getAllPlayers()) {
					if (!Validator.isPlayerValid(plugin, p__, arena)) {
						return;
					}
					Player p = Bukkit.getPlayer(p__);
					if (board == null) {
						board = Bukkit.getScoreboardManager().getNewScoreboard();
					}
					if (objective == null) {
						objective = board.registerNewObjective("test", "dummy");
					}

					objective.setDisplaySlot(DisplaySlot.SIDEBAR);

					objective.setDisplayName("[" + arena.getName() + "]");

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
									objective.getScore(Bukkit.getOfflinePlayer("§a" + p_.getName())).setScore(score);
								} else {
									objective.getScore(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
								}
							} catch (Exception e) {
							}
						} else if (MinigamesAPI.getAPI().pinstances.get(plugin).global_lost.containsKey(p___)) {
							if (currentscore.containsKey(p___)) {
								int score = currentscore.get(p___);
								try {
									if (p_.getName().length() < 15) {
										board.resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName()));
										objective.getScore(Bukkit.getOfflinePlayer("§c" + p_.getName())).setScore(score);
									} else {
										board.resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3)));
										objective.getScore(Bukkit.getOfflinePlayer("§c" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
									}
								} catch (Exception e) {
								}
							}
						}
					}

					p.setScoreboard(board);
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
					board.resetScores(Bukkit.getOfflinePlayer("§c" + p.getName()));
					board.resetScores(Bukkit.getOfflinePlayer("§a" + p.getName()));
				} else {
					board.resetScores(Bukkit.getOfflinePlayer("§c" + p.getName().substring(0, p.getName().length() - 3)));
					board.resetScores(Bukkit.getOfflinePlayer("§a" + p.getName().substring(0, p.getName().length() - 3)));
				}

			} catch (Exception e) {
			}

			sc.clearSlot(DisplaySlot.SIDEBAR);
			p.setScoreboard(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
