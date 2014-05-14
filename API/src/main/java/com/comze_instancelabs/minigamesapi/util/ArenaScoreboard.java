package com.comze_instancelabs.minigamesapi.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class ArenaScoreboard {

	Scoreboard board;
	Objective objective;
	public HashMap<String, Integer> currentscore = new HashMap<String, Integer>();

	
	// TODO think about that
	// I'm not sure yet how to handle scoreboards, so let's just save some code from my old Arenasystem here
	
	/*public void updateScoreboard(final String arena) {

		Bukkit.getScheduler().runTask(MinigamesAPI.getAPI(), new Runnable(){
			public void run(){
				for (Player pl : arenap.keySet()) {
					if(!arenap.get(pl).equalsIgnoreCase(arena)){
						return;
					}
					Player p = pl;
					if (board == null) {
						board = Bukkit.getScoreboardManager().getNewScoreboard();
					}
					if (objective == null) {
						objective = board.registerNewObjective("test", "dummy");
					}

					objective.setDisplaySlot(DisplaySlot.SIDEBAR);

					objective.setDisplayName("[" + arenap.get(p) + "]");

					for (Player pl_ : arenap.keySet()) {
						Player p_ = pl_;
						if (!lost.containsKey(pl_)) {
							int score = 0;
							if (currentscore.containsKey(pl_.getName())) {
								int oldscore = currentscore.get(pl_.getName());
								if (score > oldscore) {
									currentscore.put(pl_.getName(), score);
								} else {
									score = oldscore;
								}
							} else {
								currentscore.put(pl_.getName(), score);
							}
							try{
								if(p_.getName().length() < 15){
									objective.getScore(Bukkit.getOfflinePlayer("§a" + p_.getName())).setScore(score);
								}else{
									objective.getScore(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
								}
							}catch(Exception e){
							}
						} else if (lost.containsKey(pl_)){
							if (currentscore.containsKey(pl_.getName())) {
								int score = currentscore.get(pl_.getName());
								try{
									if(p_.getName().length() < 15){
										board.resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName()));
										objective.getScore(Bukkit.getOfflinePlayer("§c" + p_.getName())).setScore(score);
									}else{
										board.resetScores(Bukkit.getOfflinePlayer("§a" + p_.getName().substring(0, p_.getName().length() - 3)));
										objective.getScore(Bukkit.getOfflinePlayer("§c" + p_.getName().substring(0, p_.getName().length() - 3))).setScore(score);
									}
								}catch(Exception e){
								}
							}
						}
					}

					p.setScoreboard(board);
				}
			}
		});
	}*/

	public void removeScoreboard(String arena, Player p) {
		try {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sc = manager.getNewScoreboard();
			try{
				if(p.getName().length() < 15){
					board.resetScores(Bukkit.getOfflinePlayer("§c" + p.getName()));
					board.resetScores(Bukkit.getOfflinePlayer("§a" + p.getName()));
				}else{
					board.resetScores(Bukkit.getOfflinePlayer("§c" + p.getName().substring(0, p.getName().length() - 3)));
					board.resetScores(Bukkit.getOfflinePlayer("§a" + p.getName().substring(0, p.getName().length() - 3)));
				}
				
			}catch(Exception e){}

			sc.clearSlot(DisplaySlot.SIDEBAR);
			p.setScoreboard(sc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
