package com.comze_instancelabs.minigamesapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comze_instancelabs.minigamesapi.util.ChangeCause;
import com.comze_instancelabs.minigamesapi.util.Cuboid;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Util.CompassPlayer;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaListener implements Listener {

	JavaPlugin plugin = null;
	PluginInstance pli = null;
	SpectatorManager sm;
	
	private String minigame = "minigame";

	
	
	private ArrayList<String> cmds = new ArrayList<String>();
	private String leave_cmd = "/leave";

	public int loseY = 4;

	public ArenaListener(JavaPlugin plugin, PluginInstance pinstance, String minigame) {
		this.plugin = plugin;
		this.pli = pinstance;
		this.setName(minigame);
		this.leave_cmd = plugin.getConfig().getString("config.leave_command");
	}

	public ArenaListener(JavaPlugin plugin, PluginInstance pinstance, String minigame, ArrayList<String> cmds) {
		this(plugin, pinstance, minigame);
		this.cmds = cmds;
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (pli.containsGlobalPlayer(event.getPlayer().getName())) {
			Arena a = pli.global_players.get(event.getPlayer().getName());
			if (a != null) {
				if (a.getArenaState() != ArenaState.INGAME && a.getArcadeInstance() == null && !a.isArcadeMain()) {
					event.setCancelled(true);
				}
				if (a.getArenaState() == ArenaState.INGAME && pli.containsGlobalLost(event.getPlayer().getName())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		// spectators shall not pick up items
		if (pli.containsGlobalLost(event.getPlayer().getName()) || pli.getSpectatorManager().isSpectating(event.getPlayer())) {
			Arena a = pli.global_lost.get(event.getPlayer().getName());
			if (a != null) {
				if (a.getArenaState() == ArenaState.INGAME && a.getArcadeInstance() == null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player p = (Player) event.getWhoClicked();
			if (pli.containsGlobalPlayer(p.getName())) {
				Arena a = pli.global_players.get(p.getName());
				if (a != null) {
					if (a.getArenaState() == ArenaState.STARTING && a.getArcadeInstance() == null && !a.isArcadeMain()) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	  private List<Entity> getEntitiesByLocation(Location loc, double d)
	  {
	    List<Entity> ent = new ArrayList();
	    for (Entity e : loc.getWorld().getEntities()) {
	      if (e.getLocation().distanceSquared(loc) <= d) {
	        ent.add(e);
	      }
	    }
	    return ent;
	  }	
	
	@EventHandler
	public void Space(PlayerMoveEvent event) {
			Player p = (Player) event.getPlayer();
			if (pli.containsGlobalPlayer(p.getName())) {
				Arena a = pli.global_players.get(p.getName());
				if (a != null) {
					if (a.getArenaState() == ArenaState.INGAME) {
						  if (!isSpectating(p)) {
						for (Entity e : getEntitiesByLocation(p.getLocation(), 30D)) {
						
						if (e instanceof Player) {
				        Player sp = (Player) e;			
				
			          if (isSpectating(sp)) {
			        	  sp.setVelocity(sp.getLocation().getDirection().setY(0.05D));
			        	  sp.setVelocity(sp.getLocation().getDirection().multiply(-2));
			          }

					}
			          }
					}
				}
			}
		}
	}
			
		

	@Deprecated
	public static boolean isSpectating(Player p) {
		return Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spectators").hasPlayer(p);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		try {
			final Player p = event.getPlayer();
			if (pli.containsGlobalPlayer(p.getName())) {
				final Arena a = pli.global_players.get(p.getName());
				if (!pli.containsGlobalLost(p.getName()) && !pli.global_arcade_spectator.containsKey(p.getName())) {
					if (a.getArenaState() == ArenaState.INGAME) {
						if (p.getLocation().getBlockY() + loseY < a.getSpawns().get(0).getBlockY()) {
							if (a.getArenaType() == ArenaType.JUMPNRUN) {
								Util.teleportPlayerFixed(p, a.getSpawns().get(0));
							} else {
								a.spectate(p.getName());
							}
							return;
						}
						if (a.getBoundaries() != null) {
							if (!a.getBoundaries().containsLocWithoutY(p.getLocation())) {
								Util.pushBack(a.getSpawns().get(0), p);
							}
						}
					} else if (a.getArenaState() == ArenaState.STARTING || a.getArenaState() == ArenaState.JOIN) {
						if (!a.isArcadeMain()) {
							if (!a.startedIngameCountdown) {
								if (p.getLocation().getBlockY() < 0) {
									try {
										Util.teleportPlayerFixed(p, a.getWaitingLobbyTemp());
									} catch (Exception e) {
										System.out.println("Waiting lobby for arena " + a.getInternalName() + " missing, please fix by setting it. " + e.getMessage());
									}
								}
								if (a.getLobbyBoundaries() != null && !a.skip_join_lobby) {
									if (!a.getLobbyBoundaries().containsLocWithoutY(p.getLocation())) {
										Util.pushBack(a.getWaitingLobbyTemp(), p);
									}
								}
							}
						}
					}
				} else {
					if (a.getArenaState() == ArenaState.INGAME) {
						if (pli.spectator_move_y_lock && event.getPlayer().getLocation().getBlockY() < (a.getSpawns().get(0).getBlockY() + 30D) || event.getPlayer().getLocation().getBlockY() > (a.getSpawns().get(0).getBlockY() + 30D)) {
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

						if (a.getSpecBoundaries() != null) {
							if (!a.getSpecBoundaries().containsLocWithoutY(p.getLocation())) {
								Util.pushBack(a.getSpawns().get(0).clone().add(0D, 30D, 0D), p);
							}
							return;
						}
						if (a.getBoundaries() != null) {
							if (!a.getBoundaries().containsLocWithoutY(p.getLocation())) {
								Util.pushBack(a.getSpawns().get(0).clone().add(0D, 30D, 0D), p);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (MinigamesAPI.debug) {
				e.printStackTrace();
			}
		}

	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (pli.containsGlobalPlayer(p.getName())) {
				if (!pli.global_players.get(p.getName()).isArcadeMain()) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (pli.containsGlobalPlayer(event.getEntity().getName())) {
			event.setDeathMessage(null);
			final Player p = event.getEntity();
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 3, 50));

			final Arena arena = pli.global_players.get(p.getName());
			if (arena.getArenaState() == ArenaState.JOIN || (arena.getArenaState() == ArenaState.STARTING && !arena.startedIngameCountdown)) {
				if (arena.isArcadeMain()) {
					Util.teleportPlayerFixed(p, arena.getWaitingLobbyTemp());
				}
			}

			arena.global_drops.addAll(event.getDrops());

			arena.spectate(p.getName());

			pli.global_lost.put(p.getName(), arena);

			int count = 0;
			// for (String p_ : pli.global_players.keySet()) {
			// if (Validator.isPlayerOnline(p_)) {
			// if (pli.global_players.get(p_).getInternalName().equalsIgnoreCase(arena.getInternalName())) {
			// if (!pli.containsGlobalLost(p_)) {
			// count++;
			// }
			// }
			// }
			// }
			for (String p_ : arena.getAllPlayers()) {
				if (Validator.isPlayerOnline(p_)) {
					if (!pli.containsGlobalLost(p_)) {
						count++;
					}
				}
			}
			final int count_ = count;

			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					if (pli.containsGlobalPlayer(p.getName()) && count_ > 1) {
						arena.spectate(p.getName());
					}
					for (String p_ : arena.getAllPlayers()) {
						if (Validator.isPlayerOnline(p_)) {
							Player p__ = Bukkit.getPlayer(p_);
							Util.sendMessage(plugin, p__, pli.getMessagesConfig().broadcast_players_left.replaceAll("<count>", arena.getPlayerCount()));
						}
					}
				}
			}, 5);

			if (pli.last_man_standing) {
				if (count < 2) {
					// last man standing
					arena.stop();
				}
			}
		}
	}
	


		   @EventHandler
		   public void NoDamageEntityInLobby(EntityDamageByEntityEvent event){
		   if (event.getDamager() instanceof Player) {
			   Player p = (Player) event.getDamager();
			if (pli.containsGlobalPlayer(p.getName())) {
				final Arena arena = pli.global_players.get(p.getName());
			if (arena.getArenaState() == ArenaState.JOIN || (arena.getArenaState() == ArenaState.STARTING)) {
				   Entity e = event.getEntity();
				   if (e instanceof ArmorStand || e instanceof ItemFrame || e instanceof Painting || e instanceof Minecart) {
					   event.setCancelled(false);
				   }
			}
			}
		   }
		   }
			
		   
		   @EventHandler
		   public void NoClickEntityInLobby(PlayerInteractEntityEvent event) throws IOException{
		   Player p = event.getPlayer();
		   Entity e = event.getRightClicked();
		   if(!(e instanceof Player)){
				final Arena arena = pli.global_players.get(p.getName());
				if (arena.getArenaState() == ArenaState.JOIN || (arena.getArenaState() == ArenaState.STARTING)) {
				   if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)
					   || event.getRightClicked().getType().equals(EntityType.MINECART)
					   || event.getRightClicked().getType().equals(EntityType.MINECART_CHEST)
					   || event.getRightClicked().getType().equals(EntityType.MINECART_HOPPER)
					   || event.getRightClicked().getType().equals(EntityType.ITEM_FRAME)
					   || event.getRightClicked().getType().equals(EntityType.PAINTING)) {
					   event.setCancelled(true);
					   return;
				   }
				}
		   }
		   }
		   
		   
		   
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (pli.containsGlobalPlayer(p.getName()) && pli.containsGlobalLost(p.getName())) {
				Arena a = pli.global_players.get(p.getName());
				if (a.getArenaState() == ArenaState.INGAME && a.getArcadeInstance() == null && !a.getAlwaysPvP()) {
					event.setCancelled(true);
				}
			}
			if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() != ArenaState.INGAME && a.getArcadeInstance() == null && !a.getAlwaysPvP()) {
						// System.out.println(pli.getPlugin().getName() + " disallowed a pvp action.");
						event.setCancelled(true);
					}
					if (pli.blood_effects && (a.getArenaState() == ArenaState.INGAME || a.getAlwaysPvP()) && !a.isArcadeMain()) {
						Effects.playBloodEffect(p);
					}
				}
				if (pli.containsGlobalLost(p.getName()) || pli.getSpectatorManager().isSpectating(p)) {
					// System.out.println(pli.getPlugin().getName() + " disallowed a pvp action.");
					event.setCancelled(true);
				}
			} else if (event.getCause().equals(DamageCause.FALL)) {
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() != ArenaState.INGAME && a.getArcadeInstance() != null) {
						event.setCancelled(true);
					}
				}
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
				if (pli.containsGlobalPlayer(p.getName()) && pli.containsGlobalPlayer(attacker.getName())) {
					if (pli.getSpectatorManager().isSpectating(p)) {
						if (event.getDamager() instanceof Arrow) {
							p.teleport(p.getLocation().add(0, 3D, 0));

							Arrow arr = attacker.launchProjectile(Arrow.class);
							arr.setShooter(attacker);
							arr.setVelocity(((Arrow) event.getDamager()).getVelocity());
							arr.setBounce(false);

							event.setCancelled(true);
							event.getDamager().remove();
						} else if (event.getDamager() instanceof Egg) {
							p.teleport(p.getLocation().add(0, 3D, 0));

							Egg egg = attacker.launchProjectile(Egg.class);
							egg.setShooter(attacker);
							egg.setVelocity(((Egg) event.getDamager()).getVelocity());
							egg.setBounce(false);

							event.setCancelled(true);
							event.getDamager().remove();
						} else if (event.getDamager() instanceof Snowball) {
							p.teleport(p.getLocation().add(0, 3D, 0));

							Snowball sb = attacker.launchProjectile(Snowball.class);
							sb.setShooter(attacker);
							sb.setVelocity(((Snowball) event.getDamager()).getVelocity());
							sb.setBounce(false);

							event.setCancelled(true);
							event.getDamager().remove();
						} else if (event.getDamager() instanceof EnderPearl) {
							p.teleport(p.getLocation().add(0, 3D, 0));

							EnderPearl sb = attacker.launchProjectile(EnderPearl.class);
							sb.setShooter(attacker);
							sb.setVelocity(((EnderPearl) event.getDamager()).getVelocity());
							sb.setBounce(false);

							event.setCancelled(true);
							event.getDamager().remove();
						}
						event.setCancelled(true);
						return;
					}
					if (pli.containsGlobalLost(attacker.getName())) {
						event.setCancelled(true);
						return;
					}
					Arena a = (Arena) pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.lastdamager.put(p.getName(), attacker.getName());
						if (pli.damage_identifier_effects) {
							ChatColor c = ChatColor.YELLOW;
							if (event.getDamage() >= 5D) {
								c = ChatColor.GOLD;
							}
							if (event.getDamage() >= 9D) {
								c = ChatColor.RED;
							}
							Effects.playHologram(attacker, p.getLocation(), c + Double.toString(event.getDamage()), true, true);
						}
					} else if (a.getArenaState() == ArenaState.RESTARTING) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPaintingBreak(HangingBreakByEntityEvent event) {
		if (event.getRemover() instanceof Player) {
			String p_ = ((Player) event.getRemover()).getName();
			if (pli.containsGlobalPlayer(p_)) {
				event.setCancelled(true);
			}
		}

	}
	

	@EventHandler(priority = EventPriority.HIGH)
	public void onExplode(EntityExplodeEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = a.getBoundaries();
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

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockFromTo(BlockFromToEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = a.getBoundaries();
				if (c != null) {
					if (c.containsLocWithoutYD(event.getBlock().getLocation())) {
						if (a.getArenaState() == ArenaState.INGAME) {
							a.getSmartReset().addChanged(event.getToBlock(), event.getToBlock().getType().equals(Material.CHEST), ChangeCause.FROM_TO);
						} else if (a.getArenaState() == ArenaState.RESTARTING) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockFade(BlockFadeEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION && a.getArenaState() == ArenaState.INGAME) {
				Cuboid c = a.getBoundaries();
				if (c != null) {
					if (c.containsLocWithoutY(event.getBlock().getLocation())) {
						a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST), ChangeCause.FADE);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onLeavesDecay(LeavesDecayEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION && a.getArenaState() == ArenaState.INGAME) {
				Cuboid c = a.getBoundaries();
				if (c != null) {
					if (c.containsLocWithoutY(event.getBlock().getLocation())) {
						a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST));
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = a.getBoundaries();
				if (c != null) {
					if (a.getArenaState() == ArenaState.INGAME) {
						if (c.containsLocWithoutY(event.getBlock().getLocation())) {
							if (event.getChangedType() == Material.CARPET || event.getChangedType() == Material.BED_BLOCK) {
								return;
							}
							a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST), ChangeCause.PHYSICS);
						}
					} else if (a.getArenaState() == ArenaState.RESTARTING) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = a.getBoundaries();
				if (c != null) {
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getBlock(), false);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		// disallow fire spread while the arena restarts
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = a.getBoundaries();
				if (c != null) {
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getBlock().getLocation(), Material.AIR, (byte) 0);
					} else if (a.getArenaState() == ArenaState.RESTARTING) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof Enderman) {
			for (Arena a : pli.getArenas()) {
				if (a.getArenaType() == ArenaType.REGENERATION) {
					Cuboid c = a.getBoundaries();
					if (c != null) {
						if (c.containsLocWithoutY(event.getEntity().getLocation())) {
							a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST), ChangeCause.ENTITY_CHANGE);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if (pli.containsGlobalPlayer(p.getName())) {
			Arena a = pli.global_players.get(p.getName());
			if (a.getArenaState() != ArenaState.INGAME || pli.containsGlobalLost(p.getName())) {
				event.setCancelled(true);
				return;
			}
			if (pli.getSpectatorManager().isSpectating(p)) {
				event.setCancelled(true);
				return;
			}
			a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST), ChangeCause.BREAK);
			if (event.getBlock().getType() == Material.DOUBLE_PLANT) {
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, -1D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, -1D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock().getType().equals(Material.CHEST));
			}
			if (event.getBlock().getType() == Material.SNOW || event.getBlock().getType() == Material.SNOW_BLOCK) {
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +3D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +3D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +2D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +2D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock().getType().equals(Material.CHEST));
			}
			if (event.getBlock().getType() == Material.CARPET) {
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +3D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +3D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +2D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +2D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock().getType().equals(Material.CHEST));
			}
			if (event.getBlock().getType() == Material.CACTUS) {
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +4D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +4D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +3D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +3D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +2D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +2D, 0D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock(), event.getBlock().getLocation().clone().add(0D, +1D, 0D).getBlock().getType().equals(Material.CHEST));
			}
			if (event.getBlock().getType() == Material.BED_BLOCK) {
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(1D, 0D, 0D).getBlock(), event.getBlock().getLocation().clone().add(1D, 0D, 1D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(-1D, 0D, 0D).getBlock(), event.getBlock().getLocation().clone().add(1D, 0D, -1D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, 0D, 1D).getBlock(), event.getBlock().getLocation().clone().add(-1D, 0D, 1D).getBlock().getType().equals(Material.CHEST));
				a.getSmartReset().addChanged(event.getBlock().getLocation().clone().add(0D, 0D, -1D).getBlock(), event.getBlock().getLocation().clone().add(-1D, 0D, -1D).getBlock().getType().equals(Material.CHEST));
			}
		}
		if (event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.WALL_SIGN) {
			Arena arena = Util.getArenaBySignLocation(plugin, event.getBlock().getLocation());
			if (arena != null) {
				pli.getArenasConfig().getConfig().set("arenas." + arena.getInternalName() + ".sign", null);
				pli.getArenasConfig().saveConfig();
			}
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		for (Arena a : pli.getArenas()) {
			if (Validator.isArenaValid(plugin, a) && a.getArenaType() == ArenaType.REGENERATION) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getInternalName(), "bounds.low"), Util.getComponentForArena(plugin, a.getInternalName(), "bounds.high"));
				if (c != null) {
					if (c.containsLocWithoutY(event.getBlock().getLocation())) {
						a.getSmartReset().addChanged(event.getBlock(), event.getBlock().getType().equals(Material.CHEST), ChangeCause.BURN);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (pli.containsGlobalPlayer(event.getPlayer().getName())) {
			Arena a = pli.global_players.get(event.getPlayer().getName());
			Block start = event.getBlockClicked();
			if (!a.getBoundaries().containsLocWithoutY(start.getLocation())) {
				event.setCancelled(true);
				return;
			}
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

	@EventHandler
	public void onStructureGrow(StructureGrowEvent event) {
		for (Arena a : pli.getArenas()) {
			if (a.getArenaType() == ArenaType.REGENERATION && a.getArenaState() == ArenaState.INGAME) {
				Cuboid c = new Cuboid(Util.getComponentForArena(plugin, a.getInternalName(), "bounds.low"), Util.getComponentForArena(plugin, a.getInternalName(), "bounds.high"));
				if (c != null) {
					Location start = event.getLocation();
					if (c.containsLocWithoutY(start)) {
						a.getSmartReset().addChanged(start.getBlock(), false);
						for (BlockState bs : event.getBlocks()) {
							Block b = bs.getBlock();
							a.getSmartReset().addChanged(b.getLocation(), Material.AIR, (byte) 0);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak2(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if (pli.containsGlobalPlayer(p.getName())) {
			Arena a = pli.global_players.get(p.getName());
			if (event.getBlock().getType() != Material.AIR) {
				a.getSmartReset().addChanged(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
			}
		}
	}

	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if (pli.containsGlobalPlayer(p.getName())) {
			Arena a = pli.global_players.get(p.getName());
			if (a.getArenaState() != ArenaState.INGAME || pli.containsGlobalLost(p.getName()) || pli.getSpectatorManager().isSpectating(p)) {
				event.setCancelled(true);
				return;
			}
			if (event.getBlockReplacedState().getType() != Material.AIR) {
				a.getSmartReset().addChanged(event.getBlock().getLocation(), event.getBlockReplacedState().getType(), event.getBlockReplacedState().getData().getData());
			} else {

			}
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
			if (event.getClickedBlock().getType() == Material.SIGN_POST
					|| event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN) {
				if (event.getClickedBlock().getType() == Material.FIRE) {
					return;
				}
				if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
					return;
				}
				final Sign s = (Sign) event.getClickedBlock().getState();
				// people will most likely do strange formats, so let's just try
				// to get signs by location rather than locally by reading the sign
				Arena arena = Util.getArenaBySignLocation(plugin, event.getClickedBlock().getLocation());
				if (arena != null) {
					Player p = event.getPlayer();
					if (!arena.containsPlayer(p.getName())) {
						arena.joinPlayerLobby(p.getName());
						Util.updateSign(plugin, arena);
					} else {
						Util.sendMessage(plugin, p, pli.getMessagesConfig().you_already_are_in_arena.replaceAll("<arena>", arena.getInternalName()));
					}
				} else {
					// try getting random sign
					Location l = Util.getComponentForArenaRaw(plugin, "random", "sign");
					if (l != null) {
						if (l.getWorld() != null) {
							if (l.getWorld().getName().equalsIgnoreCase(s.getLocation().getWorld().getName())) {
								if (l.distance(s.getLocation()) < 1) {
									for (Arena a : pli.getArenas()) {
										if (a.getArenaState() == ArenaState.JOIN || a.getArenaState() == ArenaState.STARTING) {
											if (!a.containsPlayer(event.getPlayer().getName())) {
												a.joinPlayerLobby(event.getPlayer().getName());
												Util.updateSign(plugin, arena);
												return;
											}
										}
									}
								}
							}
						}
					}
					// try getting leave sign
					if (pli.containsGlobalPlayer(event.getPlayer().getName())) {
						int count = 0;
						if (pli.getArenasConfig().getConfig().isSet("arenas.leave")) {
							for (String str : pli.getArenasConfig().getConfig().getConfigurationSection("arenas.leave.").getKeys(false)) {
								Location loc = Util.getComponentForArenaRaw(plugin, "leave." + str, "sign");
								if (loc != null) {
									if (loc.getWorld() != null) {
										if (loc.getWorld().getName().equalsIgnoreCase(s.getLocation().getWorld().getName())) {
											if (loc.distance(s.getLocation()) < 1) {
												pli.global_players.get(event.getPlayer().getName()).leavePlayer(event.getPlayer().getName(), false, false);
												Util.updateSign(plugin, arena);
												return;
											}
										}
									}
								}
							}
						}
					}
				}
			} else if (event.getClickedBlock().getType() == Material.CHEST) {
				Player p = event.getPlayer();
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getClickedBlock(), true);
					}
				}
			} else if (event.getClickedBlock().getType() == Material.TNT) {
				Player p = event.getPlayer();
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getClickedBlock(), false);
						// TODO maybe add radius of blocks around this tnt manually
					}
				}
			} else if (event.getPlayer().getItemInHand().getType() == Material.WATER_BUCKET || event.getPlayer().getItemInHand().getType() == Material.WATER || event.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET || event.getPlayer().getItemInHand().getType() == Material.LAVA) {
				Player p = event.getPlayer();
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (!a.getBoundaries().containsLocWithoutY(event.getClickedBlock().getLocation())) {
						event.setCancelled(true);
						return;
					}
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getClickedBlock(), event.getClickedBlock().getType().equals(Material.CHEST));
						// a.getSmartReset().addChanged(event.getClickedBlock().getLocation().add(0D, 1D, 0D));
					}
				}
			} else if (event.getClickedBlock().getType() == Material.DISPENSER || event.getClickedBlock().getType() == Material.DROPPER) {
				Player p = event.getPlayer();
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						a.getSmartReset().addChanged(event.getClickedBlock(), false);
					}
				}
			}
		}

		if (pli.containsGlobalLost(event.getPlayer().getName()) || pli.getSpectatorManager().isSpectating(event.getPlayer())) {
			event.setCancelled(true);
		}

		if (event.hasItem()) {
			final Player p = event.getPlayer();
			if (!pli.containsGlobalPlayer(p.getName())) {
				return;
			}
			Arena a = pli.global_players.get(p.getName());
			if (a.isArcadeMain()) {
				return;
			}
			if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.selection_items.classes_selection_item")) {
				if (a.getArenaState() != ArenaState.INGAME) {
					pli.getClassesHandler().openGUI(p.getName());
					event.setCancelled(true);
				}
			} else if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.selection_items.exit_item")) {
				if (a.getArenaState() != ArenaState.INGAME) {
					a.leavePlayer(p.getName(), false, false);
					event.setCancelled(true);
				} else {
					if (pli.containsGlobalLost(p.getName())) {
						a.leavePlayer(p.getName(), false, false);
						event.setCancelled(true);
					}
				}
			} else if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.selection_items.spectator_item")) {
				if (pli.containsGlobalLost(p.getName())) {
					pli.getSpectatorManager().openSpectatorGUI(p, a);
					event.setCancelled(true);
				}
			} else if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.selection_items.achievement_item")) {
				if (pli.isAchievementGuiEnabled()) {
					if (a.getArenaState() != ArenaState.INGAME) {
						pli.getArenaAchievements().openGUI(p.getName(), false);
						event.setCancelled(true);
					}
				}
			} else if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.selection_items.shop_selection_item")) {
				if (a.getArenaState() != ArenaState.INGAME) {
					pli.getShopHandler().openGUI(p.getName());
					event.setCancelled(true);
				}
			} else if (event.getItem().getTypeId() == plugin.getConfig().getInt("config.extra_lobby_item.item0.item")) {
				if (plugin.getConfig().getBoolean("config.extra_lobby_item.item0.enabled")) {
					if (a.getArenaState() != ArenaState.INGAME) {
						// Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("config.extra_lobby_item.item0.command"));
						p.performCommand(plugin.getConfig().getString("config.extra_lobby_item.item0.command"));
					}
				}
			}
			if (event.getItem().getType() == Material.COMPASS) {
				if (a.getArenaState() == ArenaState.INGAME) {
					if (plugin.getConfig().getBoolean("config.compass_tracking_enabled")) {
						CompassPlayer temp = Util.getNearestPlayer(p, a);
						if (temp.getPlayer() != null) {
							p.sendMessage(pli.getMessagesConfig().compass_player_found.replaceAll("<player>", temp.getPlayer().getName()).replaceAll("<distance>", Integer.toString((int) Math.round(temp.getDistance()))));
							p.setCompassTarget(temp.getPlayer().getLocation());
						} else {
							p.sendMessage(pli.getMessagesConfig().compass_no_player_found);
						}
					}
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
					if (arena.equalsIgnoreCase("random")) {
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.world", p.getWorld().getName());
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.location.x", event.getBlock().getLocation().getBlockX());
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.location.y", event.getBlock().getLocation().getBlockY());
						pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.location.z", event.getBlock().getLocation().getBlockZ());
						pli.getArenasConfig().saveConfig();
						Util.sendMessage(plugin, p, pli.getMessagesConfig().successfully_set.replaceAll("<component>", "arena (random) sign"));
						Util.updateSign(plugin, event, arena);
					} else if (arena.equalsIgnoreCase("leave")) {
						int count = 0;
						if (pli.getArenasConfig().getConfig().isSet("arenas.leave")) {
							for (String s : pli.getArenasConfig().getConfig().getConfigurationSection("arenas.leave.").getKeys(false)) {
								count++;
							}
						}
						pli.getArenasConfig().getConfig().set("arenas." + arena + "." + count + ".sign.world", p.getWorld().getName());
						pli.getArenasConfig().getConfig().set("arenas." + arena + "." + count + ".sign.location.x", event.getBlock().getLocation().getBlockX());
						pli.getArenasConfig().getConfig().set("arenas." + arena + "." + count + ".sign.location.y", event.getBlock().getLocation().getBlockY());
						pli.getArenasConfig().getConfig().set("arenas." + arena + "." + count + ".sign.location.z", event.getBlock().getLocation().getBlockZ());
						pli.getArenasConfig().saveConfig();
						Util.sendMessage(plugin, p, pli.getMessagesConfig().successfully_set.replaceAll("<component>", "arena (leave) sign"));
						Util.updateSign(plugin, event, arena);
					} else {
						if (Validator.isArenaValid(plugin, arena)) {
							pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.world", p.getWorld().getName());
							pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.x", event.getBlock().getLocation().getBlockX());
							pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.y", event.getBlock().getLocation().getBlockY());
							pli.getArenasConfig().getConfig().set("arenas." + arena + ".sign.loc.z", event.getBlock().getLocation().getBlockZ());
							pli.getArenasConfig().saveConfig();
							Util.sendMessage(plugin, p, pli.getMessagesConfig().successfully_set.replaceAll("<component>", "arena sign"));
						} else {
							Util.sendMessage(plugin, p, pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", arena));
							event.getBlock().breakNaturally();
						}

						Arena a = pli.getArenaByName(arena);
						if (a != null) {
							a.setSignLocation(event.getBlock().getLocation());
							Util.updateSign(plugin, a, event);
						} else {
							Util.sendMessage(plugin, p, pli.getMessagesConfig().arena_not_initialized);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		pli.getStatsInstance().update(p.getName());
		if (pli.containsGlobalPlayer(p.getName())) {
			pli.global_players.remove(p.getName());
		}
		if (pli.containsGlobalLost(p.getName())) {
			pli.global_lost.remove(p.getName());
		}
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
						Arena arena = pli.global_players.get(p.getName());
						p.updateInventory();
						p.setWalkSpeed(0.2F);
						p.removePotionEffect(PotionEffectType.JUMP);
						p.removePotionEffect(PotionEffectType.INVISIBILITY);
						p.setGameMode(GameMode.SURVIVAL);
						Util.updateSign(plugin, arena);
						pli.getSpectatorManager().setSpectate(p, false);
					} catch (Exception e) {
						e.printStackTrace();
						Util.sendMessage(plugin, p, ChatColor.RED + "Failed restoring your stuff. Did the server restart/reload while you were offline?");
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
						Arena arena = pli.global_players.get(p.getName());
						Util.updateSign(plugin, arena);
					}
				}
			}, 30L);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (pli.containsGlobalPlayer(event.getPlayer().getName())) {
			Arena arena = pli.global_players.get(event.getPlayer().getName());
			MinigamesAPI.getAPI().getLogger().info(event.getPlayer().getName() + " quit while in arena " + arena.getInternalName() + ".");
			int count = 0;
			for (String p_ : pli.global_players.keySet()) {
				if (pli.global_players.get(p_).getInternalName().equalsIgnoreCase(arena.getInternalName())) {
					count++;
				}
			}

			arena.leavePlayer(event.getPlayer().getName(), true, false);

			try {
				Util.updateSign(plugin, arena);
			} catch (Exception e) {
				MinigamesAPI.getAPI().getLogger().warning("Error occurred while refreshing sign. " + e.getMessage());
				if (MinigamesAPI.debug) {
					e.printStackTrace();
				}
			}
		}
		if (MinigamesAPI.getAPI().global_party.containsKey(event.getPlayer().getName())) {
			MinigamesAPI.getAPI().global_party.get(event.getPlayer().getName()).disband();
		}
		Party party_ = null;
		for (Party party : MinigamesAPI.getAPI().global_party.values()) {
			if (party.containsPlayer(event.getPlayer().getName())) {
				party_ = party;
				break;
			}
		}
		if (party_ != null) {
			party_.removePlayer(event.getPlayer().getName());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(final AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (!pli.chat_enabled) {
			if (pli.containsGlobalPlayer(p.getName())) {
				event.setCancelled(true);
				return;
			}
		}
		if (plugin.getConfig().getBoolean("config.chat_show_score_in_arena")) {
			if (pli.containsGlobalPlayer(event.getPlayer().getName())) {
				event.setFormat(ChatColor.GRAY + "[" + ChatColor.GREEN + pli.getStatsInstance().getPoints(event.getPlayer().getName()) + ChatColor.GRAY + "] " + event.getFormat());
			}
		}
		if (plugin.getConfig().getBoolean("config.chat_per_arena_only")) {
			if (pli.containsGlobalPlayer(p.getName())) {
				String msg = String.format(event.getFormat(), p.getName(), event.getMessage());
				for (Player receiver : event.getRecipients()) {
					if (pli.containsGlobalPlayer(receiver.getName())) {
						if (pli.global_players.get(receiver.getName()) == pli.global_players.get(p.getName())) {
							receiver.sendMessage("§7" + msg);
						}
					}
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase(leave_cmd) || event.getMessage().equalsIgnoreCase("/l")) {
			if (pli.containsGlobalPlayer(event.getPlayer().getName())) {
				Arena arena = pli.global_players.get(event.getPlayer().getName());
				arena.leavePlayer(event.getPlayer().getName(), false, false);
				event.setCancelled(true);
				return;
			}
		}
		if (pli.containsGlobalPlayer(event.getPlayer().getName()) && !event.getPlayer().isOp()) {
			if (!plugin.getConfig().getBoolean("config.disable_commands_in_arena")) {
				return;
			}
			if (plugin.getConfig().getString("config.command_whitelist").toLowerCase().contains(event.getMessage().toLowerCase())) {
				return;
			}
			boolean cont = false;
			for (String cmd : cmds) {
				if (event.getMessage().toLowerCase().startsWith(cmd.toLowerCase())) {
					cont = true;
				}
			}
			if (!cont) {
				Util.sendMessage(plugin, event.getPlayer(), pli.getMessagesConfig().you_can_leave_with.replaceAll("<cmd>", leave_cmd));
				event.setCancelled(true);
				return;
			}
		}
	}

	// TP Fix start
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getCause().equals(TeleportCause.UNKNOWN) && pli.spectator_mode_1_8) {
			// Don't hide/show players when 1.8 spectator mode is enabled
			return;
		}
		final Player player = event.getPlayer();
		if (pli.containsGlobalPlayer(player.getName())) {
			final int visibleDistance = 16;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					final List<Player> nearby = getPlayersWithin(player, visibleDistance);
					updateEntities(nearby, false);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							updateEntities(nearby, true);
						}
					}, 1);
				}
			}, 5L);
		}
	}

	private void updateEntities(List<Player> players, boolean visible) {
		for (Player observer : players) {
			for (Player player : players) {
				if (observer.getEntityId() != player.getEntityId()) {
					if (visible && !pli.containsGlobalLost(player.getName()))
						observer.showPlayer(player);
					else
						observer.hidePlayer(player);
				}
			}
		}
	}

	private List<Player> getPlayersWithin(Player player, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
				res.add(p);
			}
		}
		return res;
	}


	public String getName() {
		return minigame;
	}

	public void setName(String minigame) {
		this.minigame = minigame;
	}

}
