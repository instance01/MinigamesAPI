package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaListener implements Listener {

	JavaPlugin plugin = null;

	public ArenaListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (MinigamesAPI.global_players.containsKey(p.getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (MinigamesAPI.global_players.containsKey(event.getEntity().getName())) {
			event.getEntity().setHealth(20D);
			final Player p = event.getEntity();

			MinigamesAPI.global_lost.put(p.getName(), MinigamesAPI.global_players.get(p.getName()));
			final Arena arena = MinigamesAPI.global_players.get(p.getName());
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						MinigamesAPI.global_players.get(p.getName()).spectate(p.getName());
						for (String p_ : arena.getAllPlayers()) {
							if (Validator.isPlayerOnline(p_)) {
								Player p__ = Bukkit.getPlayer(p_);
								p__.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().broadcast_players_left.replaceAll("<count>", arena.getPlayerCount()));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 5);

			int count = 0;

			for (String p_ : MinigamesAPI.global_players.keySet()) {
				if (Validator.isPlayerOnline(p_)) {
					if (MinigamesAPI.global_players.get(p_).getName().equalsIgnoreCase(arena.getName())) {
						if (!MinigamesAPI.global_lost.containsKey(p_)) {
							count++;
						}
					}
				}
			}

			if (count < 2) {
				// last man standing!
				arena.stop();
			}
		}
	}

	@EventHandler
	public void onSignUse(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
				final Sign s = (Sign) event.getClickedBlock().getState();
				// people will most likely do strange formats, so let's just try
				// to get signs by location rather than locally by reading the
				// sign
				Arena arena = Util.getArenaBySignLocation(plugin, event.getClickedBlock().getLocation());
				if (arena != null) {
					arena.joinPlayerLobby(event.getPlayer().getName());
				}
			}
		}

		// netherstar to open kit gui; needs customizable item id
		if (event.hasItem()) {
			final Player p = event.getPlayer();
			if (!MinigamesAPI.global_players.containsKey(p.getName())) {
				return;
			}
			if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.classes_selection_item")) {
				Classes.openGUI(plugin, p.getName());
			}
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (event.getLine(0).toLowerCase().equalsIgnoreCase("")) {
			if (event.getPlayer().hasPermission("mgapi.sign") || event.getPlayer().isOp()) {
				if (!event.getLine(1).equalsIgnoreCase("")) {
					String arena = event.getLine(1);
					if (Validator.isArenaValid(plugin, arena)) {
						MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + arena + ".sign.world", p.getWorld().getName());
						MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.x", event.getBlock().getLocation().getBlockX());
						MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.y", event.getBlock().getLocation().getBlockY());
						MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.z", event.getBlock().getLocation().getBlockZ());
						MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
						p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "arena sign"));
					} else {
						p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid);
						event.getBlock().breakNaturally();
					}

					Arena a = MinigamesAPI.getAPI().pinstances.get(plugin).getArenaByName(arena);
					// Sign s = (Sign) event.getBlock().getState();
					if (a != null) {
						Util.updateSign(plugin, a);
					} else {
						// TODO tell player that arena is not initialized (most
						// likely forgot to save)
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (MinigamesAPI.getAPI().global_leftplayers.contains(event.getPlayer().getName())) {
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					Util.teleportPlayerFixed(p, Util.getMainLobby(plugin));
					p.setFlying(false);
				}
			}, 5);
			MinigamesAPI.getAPI().global_leftplayers.remove(event.getPlayer().getName());
		}

		if (MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getBoolean("config.game_on_join")) {
			int c = 0;
			final List<String> arenas = new ArrayList<String>();
			for (String arena : MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getKeys(false)) {
				if (!arena.equalsIgnoreCase("mainlobby") && !arena.equalsIgnoreCase("strings") && !arena.equalsIgnoreCase("config")) {
					c++;
					arenas.add(arena);
				}
			}
			if (c < 1) {
				MinigamesAPI.getAPI().getLogger().severe("Couldn't find any arena even though game_on_join was turned on. Please setup an arena to fix this!");
				return;
			}

			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					MinigamesAPI.getAPI().pinstances.get(plugin).getArenas().get(0).joinPlayerLobby(p.getName());
				}
			}, 30L);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (MinigamesAPI.global_players.containsKey(event.getPlayer().getName())) {
			Arena arena = MinigamesAPI.global_players.get(event.getPlayer().getName());
			MinigamesAPI.getAPI().getLogger().info(arena.getName());
			int count = 0;
			for (String p_ : MinigamesAPI.global_players.keySet()) {
				if (MinigamesAPI.global_players.get(p_).getName().equalsIgnoreCase(arena.getName())) {
					count++;
				}
			}

			try {
				Util.updateSign(plugin, MinigamesAPI.getAPI().global_players.get(event.getPlayer().getName()));
			} catch (Exception e) {
				MinigamesAPI.getAPI().getLogger().warning("You forgot to set a sign for arena " + arena + "! This might lead to errors.");
			}

			arena.leavePlayer(event.getPlayer().getName(), true);
			MinigamesAPI.getAPI().global_leftplayers.add(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("/leave")) {
			if (MinigamesAPI.global_players.containsKey(event.getPlayer().getName())) {
				Arena arena = MinigamesAPI.global_players.get(event.getPlayer().getName());
				arena.leavePlayer(event.getPlayer().getName(), true);
				event.setCancelled(true);
				return;
			}
		}
		// TODO change that
		if (MinigamesAPI.global_players.containsKey(event.getPlayer().getName()) && !event.getPlayer().isOp()) {
			if (!event.getMessage().startsWith("/sw") && !event.getMessage().startsWith("/skywars")) {
				event.getPlayer().sendMessage("");
				event.setCancelled(true);
				return;
			}
		}
	}

}
