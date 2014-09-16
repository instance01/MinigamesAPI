package com.comze_instancelabs.minigamesapi.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
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

	public static void teleportAllPlayers(ArrayList<String> players, ArrayList<Location> locs) {
		int currentid = 0;
		int locslength = locs.size();
		for (String p_ : players) {
			Player p = Bukkit.getPlayer(p_);
			Util.teleportPlayerFixed(p, locs.get(currentid));
			currentid++;
			if (currentid > locslength - 1) {
				currentid = 0;
			}
		}
	}

	public static Location getComponentForArena(JavaPlugin plugin, String arenaname, String component, String count) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component + count;
			PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
			return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"), pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig()
					.getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static Location getComponentForArena(JavaPlugin plugin, String arenaname, String component) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component;
			PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
			return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"), pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig()
					.getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static boolean isComponentForArenaValid(JavaPlugin plugin, String arenaname, String component) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component;
			if (MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().isSet(base)) {
				return true;
			}
		}
		return false;
	}

	public static void saveComponentForArena(JavaPlugin plugin, String arenaname, String component, Location comploc) {
		String base = "arenas." + arenaname + "." + component;
		ArenasConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig();
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
		ArenasConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig();
		config.getConfig().set(base + ".world", comploc.getWorld().getName());
		config.getConfig().set(base + ".location.x", comploc.getX());
		config.getConfig().set(base + ".location.y", comploc.getY());
		config.getConfig().set(base + ".location.z", comploc.getZ());
		config.getConfig().set(base + ".location.yaw", comploc.getYaw());
		config.getConfig().set(base + ".location.pitch", comploc.getPitch());
		config.saveConfig();
	}

	public static Location getMainLobby(JavaPlugin plugin) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig();
		if (!config.isSet("mainlobby")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You forgot to set the mainlobby!");
		}
		return new Location(plugin.getServer().getWorld(config.getString("mainlobby.world")), config.getDouble("mainlobby.location.x"), config.getDouble("mainlobby.location.y"), config.getDouble("mainlobby.location.z"), (float) config.getDouble("mainlobby.location.yaw"), (float) config.getDouble("mainlobby.location.pitch"));
	}

	public static ArrayList<Location> getAllSpawns(JavaPlugin plugin, String arena) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig();
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

		File f = new File(plugin.getDataFolder() + "/" + arena.getName());
		if (!f.exists()) {
			plugin.getLogger().warning("Could not find arena file for " + arena.getName());
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
					MinigamesAPI.getAPI().getLogger().info("Finished restoring map for " + arena.getName() + ".");

					arena.setArenaState(ArenaState.JOIN);
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						public void run() {
							Util.updateSign(plugin, arena);
						}
					});

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

	@SuppressWarnings("unused")
	public static Sign getSignFromArena(JavaPlugin plugin, String arena) {
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		if (!pli.getArenasConfig().getConfig().isSet("arenas." + arena + ".sign.world")) {
			return null;
		}
		Location b_ = new Location(Bukkit.getServer().getWorld(pli.getArenasConfig().getConfig().getString("arenas." + arena + ".sign.world")), pli.getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.x"), pli.getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.y"), pli.getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.z"));
		if (b_ != null) {
			BlockState bs = b_.getBlock().getState();
			Sign s_ = null;
			if (bs instanceof Sign) {
				s_ = (Sign) bs;
			}
			return s_;
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
		for (Arena arena : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
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
		Sign s = getSignFromArena(plugin, arena.getName());
		int count = arena.getAllPlayers().size();
		int maxcount = arena.getMaxPlayers();
		// System.out.println(s.getLine(0) + " " + s.getLocation());
		if (s != null) {
			s.setLine(0, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
			s.setLine(1, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
			s.setLine(2, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
			s.setLine(3, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
			s.update();
		}
	}

	public static void updateSign(JavaPlugin plugin, Arena arena, SignChangeEvent event) {
		int count = arena.getAllPlayers().size();
		int maxcount = arena.getMaxPlayers();
		event.setLine(0, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
		event.setLine(1, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
		event.setLine(2, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
		event.setLine(3, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().getConfig().getString("signs." + arena.getArenaState().toString().toLowerCase() + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()).replace("[]", MessagesConfig.squares));
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
		ArenaSetup s = MinigamesAPI.getAPI().pinstances.get(plugin).arenaSetup;
		a.init(getSignLocationFromArena(plugin, arena), getAllSpawns(plugin, arena), getMainLobby(plugin), getComponentForArena(plugin, arena, "lobby"), s.getPlayerCount(plugin, arena, true), s.getPlayerCount(plugin, arena, false), s.getArenaVIP(plugin, arena));
		return a;
	}

	public static boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	// example items: 351:6#ALL_DAMAGE:2#KNOCKBACK:2*1=NAME:LORE;267*1;3*64;3*64
	@SuppressWarnings("unused")
	public static ArrayList<ItemStack> parseItems(String rawitems) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		try {
			String[] a = rawitems.split(";");

			for (String b : a) {
				int nameindex = b.indexOf("=");
				String[] c = b.split("\\*");
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
				String itemamount = c[1];
				if (nameindex > -1) {
					itemamount = c[1].substring(0, c[1].indexOf("="));
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
					String namelore = b.substring(nameindex + 1);
					String name = "";
					String lore = "";
					int i = namelore.indexOf(":");
					if (i > -1) {
						name = namelore.substring(0, i);
						lore = namelore.substring(i + 1);
					} else {
						name = namelore;
					}
					m.setDisplayName(name);
					m.setLore(Arrays.asList(lore));
				}
				nitem.setItemMeta(m);
				ret.add(nitem);
			}
			if (ret == null) {
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
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		ItemStack classes_item = new ItemStack(plugin.getConfig().getInt("config.classes_selection_item"));
		ItemMeta cimeta = classes_item.getItemMeta();
		cimeta.setDisplayName(pli.getMessagesConfig().classes_item);
		classes_item.setItemMeta(cimeta);

		ItemStack exit_item = new ItemStack(plugin.getConfig().getInt("config.exit_item"));
		ItemMeta exitimeta = exit_item.getItemMeta();
		exitimeta.setDisplayName(pli.getMessagesConfig().exit_item);
		exit_item.setItemMeta(exitimeta);

		p.getInventory().addItem(classes_item);
		p.getInventory().setItem(8, exit_item);
		p.updateInventory();
	}

	public static void giveSpectatorItems(JavaPlugin plugin, Player p) {
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
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

	public static void sendMessage(Player p, String msgraw) {
		String[] msgs = msgraw.replaceAll("<player>", p.getName()).split(";");
		for (String msg : msgs) {
			p.sendMessage(msgs);
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
		if (plugin.getConfig().getBoolean("config.broadcast_powerup_spawning")) {
			for (String p_ : a.getAllPlayers()) {
				if (Validator.isPlayerOnline(p_)) {
					Player p = Bukkit.getPlayer(p_);
					p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().powerup_spawned);
				}
			}
		}
	}

	static Random r = new Random();

	public static void spawnFirework(Player p) {
		Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.AQUA).withFade(Color.ORANGE).with(Type.STAR).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
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
