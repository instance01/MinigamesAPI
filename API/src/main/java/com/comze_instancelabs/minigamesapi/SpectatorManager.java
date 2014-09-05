package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

public class SpectatorManager {

	public static void setup() {
		if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators") == null) {
			Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("spectators");
		}
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").setCanSeeFriendlyInvisibles(true);
		clear();
	}

	public static void setSpectate(Player p, boolean spectate) {
		if (spectate) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 5), true);
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").addPlayer(p);
		} else {
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").hasPlayer(p)) {
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").removePlayer(p);
			}
		}
	}

	private static void clear() {
		Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators");
		ArrayList<OfflinePlayer> offp_set = new ArrayList<OfflinePlayer>(t.getPlayers());
		for (OfflinePlayer offp : offp_set) {
			t.removePlayer(offp);
		}
	}

}
