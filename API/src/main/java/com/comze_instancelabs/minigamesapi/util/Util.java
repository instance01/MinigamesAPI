package com.comze_instancelabs.minigamesapi.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;

public class Util {

	public static void clearInv(Player p) {
		p.getInventory().clear();
		p.updateInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.updateInventory();
	}

	public static void teleportPlayerFixed(final Player p, Location l) {
		if (p.isInsideVehicle()) {
			Entity ent = p.getVehicle();
			p.leaveVehicle();
			ent.eject();
		}
		p.teleport(l, TeleportCause.PLUGIN);
		p.setFallDistance(-1F);
		p.setVelocity(new Vector(0D, 0D, 0D));
		l.getWorld().refreshChunk(l.getChunk().getX(), l.getChunk().getZ());
		p.setFireTicks(0);
		p.setHealth(20D);
	}

	public static void teleportAllPlayers(ArrayList<String> players, final Location l) {
		Long delay = 1L;
		for (String pl : players) {
			if (!Validator.isPlayerOnline(pl)) {
				continue;
			}
			final Player p = Bukkit.getPlayer(pl);
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					Util.teleportPlayerFixed(p, l);
				}
			}, delay);
			delay++;
		}
	}

	public static HashMap<String, Location> teleportAllPlayers(ArrayList<String> players, ArrayList<Location> locs) {
		HashMap<String, Location> pspawnloc = new HashMap<String, Location>();
		int currentid = 0;
		int locslength = locs.size();
		for (String p_ : players) {
			Player p = Bukkit.getPlayer(p_);
			Util.teleportPlayerFixed(p, locs.get(currentid));
			pspawnloc.put(p_, locs.get(currentid));
			currentid++;
			if (currentid > locslength - 1) {
				currentid = 0;
			}
		}
		return pspawnloc;
	}

	public static Location getComponentForArena(JavaPlugin plugin, String arenaname, String component, String count) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component + count;
			PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
			if (!pli.getArenasConfig().getConfig().isSet(base + ".world") || Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")) == null) {
				return null;
			}
			return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"), pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static Location getComponentForArena(JavaPlugin plugin, String arenaname, String component) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component;
			PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
			if (!pli.getArenasConfig().getConfig().isSet(base + ".world") || Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")) == null) {
				return null;
			}
			return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"), pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static Location getComponentForArenaRaw(JavaPlugin plugin, String arenaname, String component) {
		String base = "arenas." + arenaname + "." + component;
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		if (pli.getArenasConfig().getConfig().isSet(base)) {
			return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"), pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static boolean isComponentForArenaValid(JavaPlugin plugin, String arenaname, String component) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component;
			if (MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().isSet(base)) {
				return true;
			}
		}
		return false;
	}

	public static void saveComponentForArena(JavaPlugin plugin, String arenaname, String component, Location comploc) {
		String base = "arenas." + arenaname + "." + component;
		ArenasConfig config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig();
		config.getConfig().set(base + ".world", comploc.getWorld().getName());
		config.getConfig().set(base + ".location.x", comploc.getX());
		config.getConfig().set(base + ".location.y", comploc.getY());
		config.getConfig().set(base + ".location.z", comploc.getZ());
		config.getConfig().set(base + ".location.yaw", comploc.getYaw());
		config.getConfig().set(base + ".location.pitch", comploc.getPitch());
		config.saveConfig();
	}

	public static void saveMainLobby(JavaPlugin plugin, Location comploc) {
		String base = "mainlobby";
		ArenasConfig config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig();
		config.getConfig().set(base + ".world", comploc.getWorld().getName());
		config.getConfig().set(base + ".location.x", comploc.getX());
		config.getConfig().set(base + ".location.y", comploc.getY());
		config.getConfig().set(base + ".location.z", comploc.getZ());
		config.getConfig().set(base + ".location.yaw", comploc.getYaw());
		config.getConfig().set(base + ".location.pitch", comploc.getPitch());
		config.saveConfig();
	}

	public static Location getMainLobby(JavaPlugin plugin) {
		FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
		if (!config.isSet("mainlobby")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You forgot to set the mainlobby!");
		}
		return new Location(plugin.getServer().getWorld(config.getString("mainlobby.world")), config.getDouble("mainlobby.location.x"), config.getDouble("mainlobby.location.y"), config.getDouble("mainlobby.location.z"), (float) config.getDouble("mainlobby.location.yaw"), (float) config.getDouble("mainlobby.location.pitch"));
	}

	public static ArrayList<Location> getAllSpawns(JavaPlugin plugin, String arena) {
		FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
		ArrayList<Location> ret = new ArrayList<Location>();
		if (!config.isSet("arenas." + arena + ".spawns")) {
			return ret;
		}
		for (String spawn : config.getConfigurationSection("arenas." + arena + ".spawns.").getKeys(false)) {
			ret.add(getComponentForArena(plugin, arena, "spawns." + spawn));
		}
		return ret;
	}

	public static void saveArenaToFile(JavaPlugin plugin, String arena) {
		File f = new File(plugin.getDataFolder() + "/" + arena);
		Cuboid c = new Cuboid(Util.getComponentForArena(plugin, arena, "bounds.low"), Util.getComponentForArena(plugin, arena, "bounds.high"));
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();

		int width = end.getBlockX() - start.getBlockX();
		int length = end.getBlockZ() - start.getBlockZ();
		int height = end.getBlockY() - start.getBlockY();

		MinigamesAPI.getAPI().getLogger().info("BOUNDS: " + Integer.toString(width) + " " + Integer.toString(height) + " " + Integer.toString(length));
		MinigamesAPI.getAPI().getLogger().info("BLOCKS TO SAVE: " + Integer.toString(width * height * length));

		FileOutputStream fos;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f);
			oos = new BukkitObjectOutputStream(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= height; j++) {
				for (int k = 0; k <= length; k++) {
					Block change = c.getWorld().getBlockAt(start.getBlockX() + i, start.getBlockY() + j, start.getBlockZ() + k);

					// if(change.getType() != Material.AIR){
					ArenaBlock bl = change.getType() == Material.CHEST ? new ArenaBlock(change, true) : new ArenaBlock(change, false);

					try {
						oos.writeObject(bl);
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
					// }

				}
			}
		}

		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MinigamesAPI.getAPI().getLogger().info("saved");
	}

	public static void loadArenaFromFileSYNC(final JavaPlugin plugin, final Arena arena) {
		int failcount = 0;
		final ArrayList<ArenaBlock> failedblocks = new ArrayList<ArenaBlock>();

		File f = new File(plugin.getDataFolder() + "/" + arena.getInternalName());
		if (!f.exists()) {
			plugin.getLogger().warning("Could not find arena file for " + arena.getInternalName());
			arena.setArenaState(ArenaState.JOIN);
			Bukkit.getScheduler().runTask(plugin, new Runnable() {
				public void run() {
					Util.updateSign(plugin, arena);
				}
			});
			return;
		}
		FileInputStream fis = null;
		BukkitObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			ois = new BukkitObjectInputStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				Object b = null;
				try {
					b = ois.readObject();
				} catch (EOFException e) {
					MinigamesAPI.getAPI().getLogger().info("Finished restoring map for " + arena.getInternalName() + " with old reset method.");

					arena.setArenaState(ArenaState.JOIN);
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						public void run() {
							Util.updateSign(plugin, arena);
						}
					});
				} catch (ClosedChannelException e) {
					System.out.println("Something is wrong with your arena file and the reset might not be successful. Also, you're using an outdated reset method.");
				}

				if (b != null) {
					ArenaBlock ablock = (ArenaBlock) b;
					try {
						/*
						 * if (!ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).getType().toString().equalsIgnoreCase(ablock.
						 * getMaterial().toString())) {
						 * ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).setType(ablock.getMaterial()); }
						 */
						Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
						if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
							b_.setType(ablock.getMaterial());
							b_.setData(ablock.getData());
							// .setTypeIdAndData(ablock.getMaterial().getId(), ablock.getData(), false);
						}
						if (b_.getType() == Material.CHEST) {
							((Chest) b_.getState()).getInventory().setContents(ablock.getInventory());
							((Chest) b_.getState()).update();
						}
					} catch (IllegalStateException e) {
						failcount += 1;
						failedblocks.add(ablock);
					}
				} else {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				// restore spigot blocks!
				for (ArenaBlock ablock : failedblocks) {
					// Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x,
					// ablock.y, ablock.z)).setType(Material.WOOL);
					Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
					if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
						b_.setType(ablock.getMaterial());
						b_.setData(ablock.getData());
						// .setTypeIdAndData(ablock.getMaterial().getId(), ablock.getData(), false);
					}
					if (b_.getType() == Material.CHEST) {
						((Chest) b_.getState()).getInventory().setContents(ablock.getInventory());
						((Chest) b_.getState()).update();
					}
					// Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x,
					// ablock.y, ablock.z)).getTypeId();
					// Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x,
					// ablock.y, ablock.z)).setType(ablock.getMaterial());
				}
			}
		}, 40L);
		MinigamesAPI.getAPI().getLogger().info("Successfully finished!");

		return;
	}

	public static Sign getSignFromArena(JavaPlugin plugin, String arena) {
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		if (!pli.getArenasConfig().getConfig().isSet("arenas." + arena + ".sign.world")) {
			return null;
		}
		Location b_ = new Location(Bukkit.getServer().getWorld(pli.getArenasConfig().getConfig().getString("arenas." + arena + ".sign.world")), pli.getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.x"), pli.getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.y"), pli.getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.z"));
		if (b_ != null) {
			if (b_.getWorld() != null) {
				if (b_.getBlock().getState() != null) {
					BlockState bs = b_.getBlock().getState();
					Sign s_ = null;
					if (bs instanceof Sign) {
						s_ = (Sign) bs;
					}
					return s_;
				}
			}
		}
		return null;
	}

	public static Location getSignLocationFromArena(JavaPlugin plugin, String arena) {
		Sign s = getSignFromArena(plugin, arena);
		if (s != null) {
			return s.getBlock().getLocation();
		} else {
			return null;
		}
	}

	public static Arena getArenaBySignLocation(JavaPlugin plugin, Location sign) {
		for (Arena arena : MinigamesAPI.getAPI().getPluginInstance(plugin).getArenas()) {
			if (sign != null && arena.getArena().getSignLocation() != null) {
				if (sign.getWorld().getName().equalsIgnoreCase(arena.getSignLocation().getWorld().getName())) {
					if (sign.distance(arena.getArena().getSignLocation()) < 1) {
						return arena;
					}
				}
			}
		}
		return null;
	}

	public static void updateSign(JavaPlugin plugin, Arena arena) {
		Sign s = getSignFromArena(plugin, arena.getInternalName());
		int count = arena.getAllPlayers().size();
		int maxcount = arena.getMaxPlayers();
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		String state = arena.getArenaState().toString().toLowerCase();
		if (s != null) {
			s.setLine(0, pli.getMessagesConfig().getConfig().getString("signs." + state + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
			s.setLine(1, pli.getMessagesConfig().getConfig().getString("signs." + state + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
			s.setLine(2, pli.getMessagesConfig().getConfig().getString("signs." + state + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
			s.setLine(3, pli.getMessagesConfig().getConfig().getString("signs." + state + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
			s.update();
		}
		try {
			if (plugin.isEnabled()) {
				BungeeUtil.sendSignUpdateRequest(plugin, plugin.getName(), arena);
			}
		} catch (Exception e) {
			System.out.println("Failed sending bungee sign update: " + e.getMessage());
		}
	}

	public static void updateSign(JavaPlugin plugin, Arena arena, SignChangeEvent event) {
		int count = arena.getAllPlayers().size();
		int maxcount = arena.getMaxPlayers();
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		String arenastate = arena.getArenaState().toString().toLowerCase();
		event.setLine(0, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
		event.setLine(1, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
		event.setLine(2, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
		event.setLine(3, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName()).replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
	}

	// used for random sign
	public static void updateSign(JavaPlugin plugin, SignChangeEvent event) {
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		String arenastate = "random";
		event.setLine(0, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".0").replaceAll("&", "§").replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
		event.setLine(1, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".1").replaceAll("&", "§").replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
		event.setLine(2, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".2").replaceAll("&", "§").replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
		event.setLine(3, pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".3").replaceAll("&", "§").replace("[]", new String(MessagesConfig.squares_mid)).replace("[1]", new String(MessagesConfig.squares_full).replace("[2]", new String(MessagesConfig.squares_medium)).replace("[3]", new String(MessagesConfig.squares_light))));
	}

	public static ArrayList<Arena> loadArenas(JavaPlugin plugin, ArenasConfig cf) {
		ArrayList<Arena> ret = new ArrayList<Arena>();
		FileConfiguration config = cf.getConfig();
		if (!config.isSet("arenas")) {
			return ret;
		}
		for (String arena : config.getConfigurationSection("arenas.").getKeys(false)) {
			if (Validator.isArenaValid(plugin, arena, cf.getConfig())) {
				ret.add(initArena(plugin, arena));
			}
		}
		return ret;
	}

	public static Arena initArena(JavaPlugin plugin, String arena) {
		Arena a = new Arena(plugin, arena);
		ArenaSetup s = MinigamesAPI.getAPI().getPluginInstance(plugin).arenaSetup;
		a.init(getSignLocationFromArena(plugin, arena), getAllSpawns(plugin, arena), getMainLobby(plugin), getComponentForArena(plugin, arena, "lobby"), s.getPlayerCount(plugin, arena, true), s.getPlayerCount(plugin, arena, false), s.getArenaVIP(plugin, arena));
		return a;
	}

	public static boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	// example items: 351:6#ALL_DAMAGE:2#KNOCKBACK:2*1=NAME:LORE;267*1;3*64;3*64
	public static ArrayList<ItemStack> parseItems(String rawitems) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		try {
			String[] a = rawitems.split(";");

			for (String rawitem : a) {
				// crackshot support
				if (rawitem.startsWith("crackshot:")) {
					String[] guntype = rawitem.split(":");
					if (guntype.length > 1) {
						if (guntype[1].length() > 1) {
							ItemStack gun = new ItemStack(Material.WOOD_HOE);
							ItemMeta gunmeta = gun.getItemMeta();
							gunmeta.setDisplayName(rawitem);
							gun.setItemMeta(gunmeta);
							ret.add(gun);
						}
					}
					continue;
				}

				// Potioneffects support
				if (rawitem.startsWith("potioneffect:")) {
					String[] potioneffecttype = rawitem.split(":");
					if (potioneffecttype.length > 1) {
						if (potioneffecttype[1].length() > 1) {
							if (!potioneffecttype[1].contains(":")) {
								// duration
								rawitem += ":99999";
							}
							if (!potioneffecttype[1].contains("#")) {
								// level
								rawitem += "#1";
							}
							ItemStack gun = new ItemStack(Material.WOOD_HOE);
							ItemMeta gunmeta = gun.getItemMeta();
							gunmeta.setDisplayName(rawitem);
							gun.setItemMeta(gunmeta);
							ret.add(gun);
						}
					}
					continue;
				}

				int nameindex = rawitem.indexOf("=");
				String[] c = rawitem.split("\\*");
				int optional_armor_color_index = -1;
				String itemid = c[0];
				String itemdata = "0";
				String[] enchantments_ = itemid.split("#");
				String[] enchantments = new String[enchantments_.length - 1];
				if (enchantments_.length > 1) {
					for (int i = 1; i < enchantments_.length; i++) {
						enchantments[i - 1] = enchantments_[i];
					}
				}
				itemid = enchantments_[0];
				String[] d = itemid.split(":");
				if (d.length > 1) {
					itemid = d[0];
					itemdata = d[1];
				}
				String itemamount = "1";
				if (c.length > 1) {
					itemamount = c[1];
					optional_armor_color_index = c[1].indexOf("#");
					if(optional_armor_color_index > 0){
						itemamount = c[1].substring(0, optional_armor_color_index);
					}
				}
				if (nameindex > -1) {
					itemamount = c[1].substring(0, c[1].indexOf("="));
				}
				if (Integer.parseInt(itemid) < 1) {
					System.out.println("Invalid item id: " + itemid);
					continue;
				}
				ItemStack nitem = new ItemStack(Integer.parseInt(itemid), Integer.parseInt(itemamount), (short) Integer.parseInt(itemdata));
				ItemMeta m = nitem.getItemMeta();
				for (String enchant : enchantments) {

					String[] e = enchant.split(":");
					String ench = e[0];
					String lv = "1";
					if (e.length > 1) {
						lv = e[1];
					}
					if (Enchantment.getByName(ench) != null) {
						m.addEnchant(Enchantment.getByName(ench), Integer.parseInt(lv), true);
					}
				}

				if (nameindex > -1) {
					String namelore = rawitem.substring(nameindex + 1);
					String name = "";
					String lore = "";
					int i = namelore.indexOf(":");
					if (i > -1) {
						name = namelore.substring(0, i);
						lore = namelore.substring(i + 1);
					} else {
						name = namelore;
					}
					m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
					m.setLore(Arrays.asList(lore));
				}

				// RGB Color support for Armor
				if (optional_armor_color_index > -1) {
					m.setDisplayName(c[1].substring(optional_armor_color_index));
				}

				nitem.setItemMeta(m);
				ret.add(nitem);
			}
			if (ret == null || ret.size() < 1) {
				MinigamesAPI.getAPI().getLogger().severe("Found invalid class in config!");
			}
		} catch (Exception e) {
			ret.add(new ItemStack(Material.STAINED_GLASS_PANE));
			System.out.println("Failed to load class items: " + e.getMessage() + " at [1] " + e.getStackTrace()[1].getLineNumber() + " [0] " + e.getStackTrace()[0].getLineNumber());
			ItemStack rose = new ItemStack(Material.RED_ROSE);
			ItemMeta im = rose.getItemMeta();
			im.setDisplayName(ChatColor.RED + "Sowwy, failed to load class.");
			rose.setItemMeta(im);
			ret.add(rose);
		}
		return ret;
	}

	public static void giveLobbyItems(JavaPlugin plugin, Player p) {
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		ItemStack classes_item = new ItemStack(plugin.getConfig().getInt("config.classes_selection_item"));
		if (classes_item.getType() != Material.AIR) {
			ItemMeta cimeta = classes_item.getItemMeta();
			cimeta.setDisplayName(pli.getMessagesConfig().classes_item);
			classes_item.setItemMeta(cimeta);
		}

		if (!plugin.getConfig().getBoolean("config.bungee.game_on_join")) {
			ItemStack exit_item = new ItemStack(plugin.getConfig().getInt("config.exit_item"));
			if (exit_item.getType() != Material.AIR) {
				ItemMeta exitimeta = exit_item.getItemMeta();
				exitimeta.setDisplayName(pli.getMessagesConfig().exit_item);
				exit_item.setItemMeta(exitimeta);
			}
			p.getInventory().setItem(8, exit_item);
			p.updateInventory();
		}

		ItemStack achievement_item = new ItemStack(plugin.getConfig().getInt("config.achievement_item"));
		if (achievement_item.getType() != Material.AIR) {
			ItemMeta achievement_itemmeta = achievement_item.getItemMeta();
			achievement_itemmeta.setDisplayName(pli.getMessagesConfig().achievement_item);
			achievement_item.setItemMeta(achievement_itemmeta);
		}

		ItemStack shop_item = new ItemStack(plugin.getConfig().getInt("config.shop_selection_item"));
		if (shop_item.getType() != Material.AIR) {
			ItemMeta shop_itemmeta = shop_item.getItemMeta();
			shop_itemmeta.setDisplayName(pli.getMessagesConfig().shop_item);
			shop_item.setItemMeta(shop_itemmeta);
		}

		if (plugin.getConfig().getBoolean("config.classes_enabled")) {
			p.getInventory().addItem(classes_item);
		}
		if (pli.isAchievementGuiEnabled() && pli.getAchievementsConfig().getConfig().getBoolean("config.enabled")) {
			p.getInventory().addItem(achievement_item);
		}
		if (plugin.getConfig().getBoolean("config.shop_enabled")) {
			p.getInventory().addItem(shop_item);
		}
		p.updateInventory();

		// custom lobby item
		if (plugin.getConfig().getBoolean("config.extra_lobby_item.item0.enabled")) {
			ItemStack custom_item0 = new ItemStack(plugin.getConfig().getInt("config.extra_lobby_item.item0.item"));
			if (custom_item0.getType() != Material.AIR) {
				ItemMeta custom_item0meta = custom_item0.getItemMeta();
				custom_item0meta.setDisplayName(plugin.getConfig().getString("config.extra_lobby_item.item0.name"));
				custom_item0.setItemMeta(custom_item0meta);
			}
			p.getInventory().addItem(custom_item0);
			p.updateInventory();
		}
	}

	public static void giveSpectatorItems(JavaPlugin plugin, Player p) {
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		ItemStack s_item = new ItemStack(plugin.getConfig().getInt("config.spectator_item"));
		ItemMeta s_imeta = s_item.getItemMeta();
		s_imeta.setDisplayName(pli.getMessagesConfig().spectator_item);
		s_item.setItemMeta(s_imeta);

		ItemStack exit_item = new ItemStack(plugin.getConfig().getInt("config.exit_item"));
		ItemMeta exitimeta = exit_item.getItemMeta();
		exitimeta.setDisplayName(pli.getMessagesConfig().exit_item);
		exit_item.setItemMeta(exitimeta);

		p.getInventory().addItem(s_item);
		p.getInventory().setItem(8, exit_item);
		p.updateInventory();
	}

	public static void sendMessage(Player p, String arenaname, String msgraw) {
		String[] msgs = msgraw.replaceAll("<player>", p.getName()).replaceAll("<arena>", arenaname).split(";");
		for (String msg : msgs) {
			p.sendMessage(msgs);
		}
	}

	public static void sendMessage(JavaPlugin plugin, Player p, String msgraw) {
		if (msgraw.equalsIgnoreCase("")) {
			return;
		}
		String[] msgs = msgraw.replaceAll("<player>", p.getName()).replaceAll("<game>", plugin.getName()).split(";");
		for (String msg : msgs) {
			p.sendMessage(msg);
		}
	}

	public static ItemStack getCustomHead(String name) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
		skullmeta.setOwner(name);
		item.setItemMeta(skullmeta);
		return item;
	}

	public static void spawnPowerup(JavaPlugin plugin, Arena a, Location l, ItemStack item) {
		World w = l.getWorld();
		Chicken c = w.spawn(l, Chicken.class);
		c.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
		Item i = w.dropItem(l, item);
		c.setPassenger(i);
		if (plugin.getConfig().getBoolean("config.powerup_spawning.broadcast")) {
			for (String p_ : a.getAllPlayers()) {
				if (Validator.isPlayerOnline(p_)) {
					Player p = Bukkit.getPlayer(p_);
					p.sendMessage(MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().powerup_spawned);
				}
			}
		}
		if (plugin.getConfig().getBoolean("config.powerup_spawning.spawn_firework")) {
			spawnFirework(l);
		}
	}

	static Random r = new Random();

	public static void spawnFirework(Player p) {
		spawnFirework(p.getLocation());
	}

	public static void spawnFirework(Location l) {
		Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.AQUA).withFade(Color.ORANGE).with(Type.STAR).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	public static Color hexToRgb(String colorStr) {
		return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public static class ValueComparator implements Comparator<String> {
		Map<String, Double> base;

		public ValueComparator(Map<String, Double> base) {
			this.base = base;
		}

		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
