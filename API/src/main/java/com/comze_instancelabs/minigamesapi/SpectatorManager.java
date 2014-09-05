package com.comze_instancelabs.minigamesapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpectatorManager {

	public static void setup() {
		if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators") == null) {
			Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("spectators");
		}
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").setCanSeeFriendlyInvisibles(true);
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

}
