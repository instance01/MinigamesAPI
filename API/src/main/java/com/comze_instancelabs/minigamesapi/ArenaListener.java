package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesapi.util.Cuboid;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaListener implements Listener {

	JavaPlugin plugin = null;
	PluginInstance pli = null;
	private String minigame = "minigame";

	private ArrayList<String> cmds = new ArrayList<String>();

	public int loseY = 4;

	public ArenaListener(JavaPlugin plugin, PluginInstance pinstance, String minigame) {
		this.plugin = plugin;
		this.pli = pinstance;
		this.setName(minigame);
	}

	public ArenaListener(JavaPlugin plugin, PluginInstance pinstance, String minigame, ArrayList<String> cmds) {
		this.plugin = plugin;
		this.pli = pinstance;
		this.setName(minigame);
		this.cmds = cmds;
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (pli.global_players.containsKey(event.getPlayer().getName())) {
			Arena a = pli.global_players.get(event.getPlayer().getName());
			if (a.getArenaState() == ArenaState.STARTING) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player p = (Player) event.getWhoClicked();
			if (pli.global_players.containsKey(p.getName())) {
				Arena a = pli.global_players.get(p.getName());
				if (a.getArenaState() == ArenaState.STARTING) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		try {
			final Player p = event.getPlayer();
			if (pli.global_players.containsKey(p.getName())) {
				final Arena a = pli.global_players.get(p.getName());
				if (!pli.global_lost.containsKey(p.getName()) && !pli.global_arcade_spectator.containsKey(p.getName())) {
					if (a.getArenaState() == ArenaState.INGAME) {
						if (p.getLocation().getBlockY() + loseY < a.getSpawns().get(0).getBlockY()) {
							if (a.getArenaType() == ArenaType.JUMPNRUN) {
								Util.teleportPlayerFixed(p, a.getSpawns().get(0));
							} else {
								a.spectate(p.getName());
							}
							return;
						}
						if (a.getArenaType() == ArenaType.REGENERATION) {
							if (a.getBoundaries() != null) {
								if (!a.getBoundaries().containsLocWithoutY(p.getLocation())) {
									Vector direction = a.getSpawns().get(0).toVector().subtract(p.getLocation().toVector()).normalize();
									p.setVelocity(direction);
									if (p.isInsideVehicle()) {
										p.getVehicle().setVelocity(direction);
									}
									p.playEffect(p.getLocation(), Effect.POTION_BREAK, 5);
								}
							}
						}
					}
				} else {
					if (a.getArenaState() == ArenaState.INGAME) {
						if (event.getPlayer().getLocation().getBlockY() < (a.getSpawns().get(0).getBlockY() + 30D) || event.getPlayer().getLocation().getBlockY() > (a.getSpawns().get(0).getBlockY() + 30D)) {
							final float b = p.getLocation().getYaw();
							final float c = p.getLocation().getPitch();
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									p.setAllowFlight(true);
									p.setFlying(true);
									if (p.isInsideVehicle()) {
										Entity ent = p.getVehicle();
										p.leaveVehicle();
										ent.eject();
									}
									p.teleport(new Location(p.getWorld(), p.getLocation().getBlockX(), (a.getSpawns().get(0).getBlockY() + 30D), p.getLocation().getBlockZ(), b, c));
								}
							}, 1);
							return;
						}
					}
				}

			}
		} catch (Exception e) {
			for (StackTraceElement et : e.getStackTrace()) {
				System.out.println(et);
			}
		}

	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (pli.global_players.containsKey(p.getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (pli.global_players.containsKey(event.getEntity().getName())) {
			event.getEntity().setHealth(20D);
			final Player p = event.getEntity();

			final Arena arena = pli.global_players.get(p.getName());
			if (arena.getArenaState() == ArenaState.JOIN) {
				Util.teleportPlayerFixed(p, arena.getWaitingLobbyTemp());
				return;
			}

			arena.onEliminated(p.getName());

			pli.global_lost.put(p.getName(), pli.global_players.get(p.getName()));
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						if (pli.global_players.containsKey(p.getName())) {
							pli.global_players.get(p.getName()).spectate(p.getName());
						}
						for (String p_ : arena.getAllPlayers()) {
							if (Validator.isPlayerOnline(p_)) {
								Player p__ = Bukkit.getPlayer(p_);
								Util.sendMessage(p__, pli.getMessagesConfig().broadcast_players_left.replaceAll("<count>", arena.getPlayerCount()));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 5);

			int count = 0;

			for (String p_ : pli.global_players.keySet()) {
				if (Validator.isPlayerOnline(p_)) {
					if (pli.global_players.get(p_).getName().equalsIgnoreCase(arena.getName())) {
						if (!pli.global_lost.containsKey(p_)) {
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
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (event.getCause().equals(DamageCause.ENTITY_ATTACK) && pli.global_lost.containsKey(p.getName())) {
				// disable entity damage for spectators
				System.out.println(pli.getPlugin().getName() + " disallowed a pvp action.");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Player attacker = null;
			if (event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				if (projectile.getShooter() instanceof Player) {
					attacker = (Player) projectile.getShooter();
				}
			} else if (event.getDamager() instanceof Player) {
				attacker = (Player) event.getDamager();
			} else {
				return;
			}

			if (p != null && attacker != null) {
				if (pli.global_players.containsKey(p.getName()) && pli.global_players.containsKey(attacker.getName())) {
					Arena a = (Arena) pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.lastdamager.put(p.getName(), attacker.getName());
					}
				}
			}
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
				if (c != null) {
					if (event.getEntity() != null) {
						if (c.containsLocWithoutY(event.getEntity().getLocation())) {
							for (Block b : event.blockList()) {
								a.getSmartReset().addChanged(b, b.getType().equals(Material.CHEST));
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
				if (c != null && a.getArenaState() == ArenaState.INGAME) {
					if (c.containsLocWithoutY(event.getToBlock().getLocation())) {
						/*
						 * BlockFace face = event.getFace(); int x = event.getBlock().getX(); int y = event.getBlock().getY(); int z =
						 * event.getBlock().getZ(); Block b = event.getBlock().getWorld().getBlockAt(x + face.getModX(), y + face.getModY(), z +
						 * face.getModZ()); a.getSmartReset().addChanged(b, b.getType().equals(Material.CHEST));
						 * a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
						 * a.getSmartReset().addChanged(event.getToBlock(), event.getToBlock().getType().equals(Material.CHEST));
						 */
						// a.getSmartReset().addChanged(event.getBlock().getLocation());
						// a.getSmartReset().addChanged(event.getToBlock().getLocation());

						// a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
						a.getSmartReset().addChanged(event.getToBlock(), event.getToBlock().getType().equals(Material.CHEST));
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockFade(BlockFadeEvent event) {
		for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
				if (c != null && a.getArenaState() == ArenaState.INGAME) {
					if (c.containsLocWithoutY(event.getBlock().getLocation())) {
						a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof Enderman) {
			for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
				if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
					Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
					if (c != null) {
						if (c.containsLocWithoutY(event.getEntity().getLocation())) {
							a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			Arena a = pli.global_players.get(p.getName());
			if (a.getArenaState() != ArenaState.INGAME) {
				event.setCancelled(true);
				return;
			}
			a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
			if (event.getBlock().getType() == Material.DOUBLE_PLANT) {
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, -1D, 0D).getBlock(), event.getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock(), event.getBlock().getType().equals(Material.CHEST));
			}
		}
		if (event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.WALL_SIGN) {
			Arena arena = Util.getArenaBySignLocation(plugin, event.getBlock().getLocation());
			if (arena != null) {
				pli.getArenasConfig().getConfig().set("arenas." + arena.getName() + ".sign", null);
				pli.getArenasConfig().saveConfig();
			}
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
				if (c != null) {
					if (c.containsLocWithoutY(event.getBlock().getLocation())) {
						a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
				if (c != null) {
					Block start = event.getBlockClicked();
					if (c.containsLocWithoutY(start.getLocation())) {
						// System.out.println("t");
						for (int x = -2; x < 2; x++) {
							for (int y = -2; y < 2; y++) {
								for (int z = -2; z < 2; z++) {
									Block b = start.getLocation().clone().add(x, y, z).getBlock();
									a.getSmartReset().addChanged(b, b.getType().equals(Material.CHEST));
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onStructureGrow(StructureGrowEvent event) {
		for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getName(), "bounds.low"), Util.getComponentForArena(plugin, a.getName(), "bounds.high"));
				if (c != null) {
					Location start = event.getLocation();
					a.getSmartReset().addChanged(start.getBlock(), false);
					if (c.containsLocWithoutY(start)) {
						for (BlockState bs : event.getBlocks()) {
							Block b = bs.getBlock();
							a.getSmartReset().addChanged(b.getLocation());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			Arena a = pli.global_players.get(p.getName());
			if (a.getArenaState() != ArenaState.INGAME) {
				event.setCancelled(true);
				return;
			}
			a.getSmartReset().addChanged(event.getBlock().getLocation());
		}
		if (pli.getStatsInstance().skullsetup.contains(p.getName())) {
			if (event.getBlock().getType() == Material.SKULL_ITEM || event.getBlock().getType() == Material.SKULL) {
				if (event.getItemInHand().hasItemMeta()) {
					pli.getStatsInstance().saveSkull(event.getBlock().getLocation(), Integer.parseInt(event.getItemInHand().getItemMeta().getDisplayName()));
					pli.getStatsInstance().skullsetup.remove(p.getName());
				}
			}
		}
	}

	@EventHandler
	public void onSignUse(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
				if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
					return;
				}
				final Sign s = (Sign) event.getClickedBlock().getState();
				// people will most likely do strange formats, so let's just try
				// to get signs by location rather than locally by reading the
				// sign
				Arena arena = Util.getArenaBySignLocation(plugin, event.getClickedBlock().getLocation());
				if (arena != null) {
					Player p = event.getPlayer();
					if (!arena.containsPlayer(p.getName())) {
						arena.joinPlayerLobby(p.getName());
					} else {
						Util.sendMessage(p, pli.getMessagesConfig().you_already_are_in_arena.replaceAll("<arena>", arena.getName()));
					}
				}
			} else if (event.getClickedBlock().getType() == Material.CHEST) {
				Player p = event.getPlayer();
				if (pli.global_players.containsKey(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getClickedBlock(), event.getClickedBlock().getType().equals(Material.CHEST));
					}
				}
			} else if (event.getPlayer().getItemInHand().getType() == Material.WATER_BUCKET || event.getPlayer().getItemInHand().getType() == Material.WATER || event.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET || event.getPlayer().getItemInHand().getType() == Material.LAVA) {
				Player p = event.getPlayer();
				if (pli.global_players.containsKey(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getClickedBlock(), event.getClickedBlock().getType().equals(Material.CHEST));
						// a.getSmartReset().addChanged(event.getClickedBlock().getLocation().add(0D, 1D, 0D));
					}
				}
			}
		}

		if (event.hasItem()) {
			final Player p = event.getPlayer();
			if (!pli.global_players.containsKey(p.getName())) {
				return;
			}
			if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.classes_selection_item")) {
				pli.getClassesHandler().openGUI(p.getName());
				event.setCancelled(true);
			} else if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.exit_item")) {
				if (pli.global_players.get(p.getName()).getArenaState() != ArenaState.INGAME) {
					pli.global_players.get(p.getName()).leavePlayer(p.getName(), false, false);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (event.getLine(0).toLowerCase().equalsIgnoreCase(getName())) {
			if (event.getPlayer().hasPermission("mgapi.sign") || event.getPlayer().isOp()) {
				if (!event.getLine(1).equalsIgnoreCase("")) {
					String arena = event.getLine(1);
					if (Validator.isArenaValid(plugin, arena)) {
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.world", p.getWorld().getName());
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.x", event.getBlock().getLocation().getBlockX());
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.y", event.getBlock().getLocation().getBlockY());
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.z", event.getBlock().getLocation().getBlockZ());
						pli.getArenasConfig().saveConfig();
						Util.sendMessage(p, pli.getMessagesConfig().successfully_set.replaceAll("<component>", "arena sign"));
					} else {
						Util.sendMessage(p, pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", arena));
						event.getBlock().breakNaturally();
					}

					Arena a = pli.getArenaByName(arena);
					// Sign s = (Sign) event.getBlock().getState();
					if (a != null) {
						a.setSignLocation(event.getBlock().getLocation());
						Util.updateSign(plugin, a, event);
					} else {
						Util.sendMessage(p, pli.getMessagesConfig().arena_not_initialized);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		pli.getStatsInstance().update(p.getName());
		if (plugin.getConfig().isSet("temp.left_players." + p.getName())) {
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					Util.teleportPlayerFixed(p, Util.getMainLobby(plugin));
					p.setFlying(false);
					try {
						p.getInventory().clear();
						p.updateInventory();
						if (plugin.getConfig().isSet("temp.left_players." + p.getName() + ".items")) {
							for (String key : plugin.getConfig().getConfigurationSection("temp.left_players." + p.getName() + ".items").getKeys(false)) {
								p.getInventory().addItem(plugin.getConfig().getItemStack("temp.left_players." + p.getName() + ".items." + key));
							}
						}
						p.updateInventory();
						p.setWalkSpeed(0.2F);
						p.removePotionEffect(PotionEffectType.JUMP);
					} catch (Exception e) {
						e.printStackTrace();
						Util.sendMessage(p, ChatColor.RED + "Failed restoring your stuff. Did the server restart/reload while you were offline?");
					}
					plugin.getConfig().set("temp.left_players." + p.getName(), null);
					plugin.saveConfig();
				}
			}, 5);
		}

		if (plugin.getConfig().getBoolean("config.bungee.game_on_join")) {
			int c = 0;
			final List<String> arenas = new ArrayList<String>();
			for (String arena : pli.getArenasConfig().getConfig().getKeys(false)) {
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
					if (p != null) {
						pli.getArenas().get(0).joinPlayerLobby(p.getName());
					}
				}
			}, 30L);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (pli.global_players.containsKey(event.getPlayer().getName())) {
			Arena arena = pli.global_players.get(event.getPlayer().getName());
			MinigamesAPI.getAPI().getLogger().info(arena.getName());
			int count = 0;
			for (String p_ : pli.global_players.keySet()) {
				if (pli.global_players.get(p_).getName().equalsIgnoreCase(arena.getName())) {
					count++;
				}
			}

			try {
				Util.updateSign(plugin, pli.global_players.get(event.getPlayer().getName()));
			} catch (Exception e) {
				MinigamesAPI.getAPI().getLogger().warning("You forgot to set a sign for arena " + arena + "! This might lead to errors.");
			}

			arena.leavePlayer(event.getPlayer().getName(), true, false);
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("/leave") || event.getMessage().equalsIgnoreCase("/l")) {
			if (pli.global_players.containsKey(event.getPlayer().getName())) {
				Arena arena = pli.global_players.get(event.getPlayer().getName());
				arena.leavePlayer(event.getPlayer().getName(), false, false);
				event.setCancelled(true);
				return;
			}
		}
		if (pli.global_players.containsKey(event.getPlayer().getName()) && !event.getPlayer().isOp()) {
			if (!plugin.getConfig().getBoolean("config.disable_commands_in_arena")) {
				return;
			}
			boolean cont = false;
			for (String cmd : cmds) {
				if (event.getMessage().toLowerCase().startsWith(cmd.toLowerCase())) {
					cont = true;
				}
			}
			if (!cont) {
				Util.sendMessage(event.getPlayer(), pli.getMessagesConfig().you_can_leave_with);
				event.setCancelled(true);
				return;
			}
		}
	}

	public String getName() {
		return minigame;
	}

	public void setName(String minigame) {
		this.minigame = minigame;
	}

}
