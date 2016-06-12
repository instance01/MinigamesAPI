package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class SpectatorManager {

	JavaPlugin plugin;
	private HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();

	public SpectatorManager(JavaPlugin plugin) {
		this.plugin = plugin;
		this.setup();
	}

	public void setup() {
		if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators") == null) {
			Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("spectators");
		}
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").setCanSeeFriendlyInvisibles(true);
		clear();
	}

	public void setSpectate(Player p, boolean spectate) {
		try {
			if (spectate) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 5), true);
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").addPlayer(p);
			} else {
				if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").hasPlayer(p)) {
					Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").removePlayer(p);
					p.removePotionEffect(PotionEffectType.INVISIBILITY);
				}
			}
		} catch (Exception e) {
		}
	}

	@Deprecated
	public static boolean isSpectating(Player p) {
		return Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").hasPlayer(p);
	}

	private void clear() {
		Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators");
		ArrayList<OfflinePlayer> offp_set = new ArrayList<OfflinePlayer>(t.getPlayers());
		for (OfflinePlayer offp : offp_set) {
			t.removePlayer(offp);
			
		}
	}

	public void openSpectatorGUI(final Player p, Arena a) {
		IconMenu iconm;
		int mincount = a.getAllPlayers().size();
		if (lasticonm.containsKey(p.getName())) {
			iconm = lasticonm.get(p.getName());
		} else {
			iconm = new IconMenu(MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().spectator_item, (9 > mincount - 1) ? 9 : Math.round(mincount / 9) * 9 + 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(p.getName())) {
						String d = event.getName();
						Player p = event.getPlayer();

						Player p_ = Bukkit.getPlayer(d);
						if (p_ != null && p != null) {
							Util.teleportPlayerFixed(p, new Location(p.getWorld(), p_.getLocation().getX(), p.getLocation().getY(), p_.getLocation().getZ()));
						}
					}
					event.setWillClose(true);
				}
			}, plugin);
		}

		iconm.clear();

		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		int c = 0;
		for (String p__ : a.getAllPlayers()) {
			Player p_ = Bukkit.getPlayer(p__);
			if (p_ != null) {
				if (pli.global_players.containsKey(p__) && !pli.global_lost.containsKey(p__)) {
					if (a.getInternalName().equalsIgnoreCase(pli.global_players.get(p__).getInternalName())) {
						iconm.setOption(c, Util.getCustomHead(p__), p__, "");
						c++;
					}
				}
			}
		}

		iconm.open(p);
		lasticonm.put(p.getName(), iconm);
	}

	HashMap<String, ArrayList<String>> pspecs = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String>> splayers = new HashMap<String, ArrayList<String>>();

	public void hideSpectator(Player spec, ArrayList<String> players) {
		for (String p_ : players) {
			if (Validator.isPlayerOnline(p_)) {
				Player p = Bukkit.getPlayer(p_);
				spec.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*600, 1));
				if (pspecs.containsKey(p_)) {
					ArrayList<String> t = pspecs.get(p_);
					t.add(spec.getName());
					pspecs.put(p_, t);
				} else {
					pspecs.put(p_, new ArrayList<String>(Arrays.asList(spec.getName())));
				}
			}
		}
		splayers.put(spec.getName(), players);
	}

	public void showSpectator(Player spec) {
		if (splayers.containsKey(spec.getName())) {
			for (String p_ : splayers.get(spec.getName())) {
				if (Validator.isPlayerOnline(p_)) {
					Player p = Bukkit.getPlayer(p_);
					if (pspecs.containsKey(p_)) {
						ArrayList<String> t = pspecs.get(p_);
						t.remove(spec.getName());
						spec.removePotionEffect(PotionEffectType.INVISIBILITY);
						pspecs.put(p_, t);
					}
				}
			}
			splayers.remove(spec.getName());
		}
	}

	public void showSpectators(Player p) {
		if (pspecs.containsKey(p.getName())) {
			for (String p_ : pspecs.get(p.getName())) {
				if (Validator.isPlayerOnline(p_)) {
					Player spec = Bukkit.getPlayer(p_);
					if (splayers.containsKey(p_)) {
						ArrayList<String> t = splayers.get(p_);
						t.remove(spec.getName());
						spec.removePotionEffect(PotionEffectType.INVISIBILITY);
						splayers.put(p_, t);
					}
				}
			}
			pspecs.remove(p.getName());
		}
	}
}
