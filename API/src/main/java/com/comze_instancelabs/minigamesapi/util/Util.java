package com.comze_instancelabs.minigamesapi.util;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaListener;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.ArrayList;

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
		// TODO might need Runnable fix?
		p.teleport(l, TeleportCause.PLUGIN);
		p.setFallDistance(0F);
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
		// TODO
		Util.teleportAllPlayers(players, locs.get(0));
	}

	public static Location getComponentForArena(JavaPlugin plugin, String arenaname, String component, String count) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component + count;
			return new Location(Bukkit.getWorld(MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getString(base + ".world")), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt(base + ".location.x"), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt(base + ".location.y"), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig()
					.getInt(base + ".location.z"), (float) MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static Location getComponentForArena(JavaPlugin plugin, String arenaname, String component) {
		if (Validator.isArenaValid(plugin, arenaname)) {
			String base = "arenas." + arenaname + "." + component;
			return new Location(Bukkit.getWorld(MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getString(base + ".world")), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt(base + ".location.x"), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt(base + ".location.y"), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig()
					.getInt(base + ".location.z"), (float) MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
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
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".world", comploc.getWorld().getName());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.x", comploc.getBlockX());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.y", comploc.getBlockY());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.z", comploc.getBlockZ());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.yaw", comploc.getYaw());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.pitch", comploc.getPitch());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
	}

	public static void saveMainLobby(JavaPlugin plugin, Location comploc) {
		String base = "mainlobby";
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".world", comploc.getWorld().getName());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.x", comploc.getBlockX());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.y", comploc.getBlockY());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.z", comploc.getBlockZ());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.yaw", comploc.getYaw());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base + ".location.pitch", comploc.getPitch());
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
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
					ArenaBlock bl = new ArenaBlock(change);

					try {
						oos.writeObject(bl);
					} catch (IOException e) {
						e.printStackTrace();
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
						if (!ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
							ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).setType(ablock.getMaterial());
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
					Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).setType(Material.WOOL);
					Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).getTypeId();
					Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).setType(ablock.getMaterial());
				}
			}
		}, 40L);
		MinigamesAPI.getAPI().getLogger().info("Successfully finished!");

		return;
	}

	public static Sign getSignFromArena(JavaPlugin plugin, String arena) {
		if (!MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().isSet("arenas." + arena + ".sign.world")) {
			return null;
		}
		Location b_ = new Location(Bukkit.getServer().getWorld(MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getString("arenas." + arena + ".sign.world")), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.x"), MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.y"), MinigamesAPI.getAPI().pinstances
				.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".sign.loc.z"));
		System.out.println(b_);
		BlockState bs = b_.getBlock().getState();
		Sign s_ = null;
		if (bs instanceof Sign) {
			s_ = (Sign) bs;
		} else {
		}
		return s_;
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
				if (sign.distance(arena.getArena().getSignLocation()) < 2) {
					return arena;
				}
			}
		}
		return null;
	}

	public static void updateSign(JavaPlugin plugin, Arena arena) {
		Sign s = getSignFromArena(plugin, arena.getName());
		int count = arena.getAllPlayers().size();
		int maxcount = arena.getMaxPlayers();
		System.out.println(s.getLine(0) + " " + s.getLocation());
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

	// example items: 351:6#ALL_DAMAGE:2#KNOCKBACK:2*1;267*1;3*64;3*64
	@SuppressWarnings("unused")
	public static ArrayList<ItemStack> parseItems(String rawitems) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		String[] a = rawitems.split(";");

		for (String b : a) {
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
			nitem.setItemMeta(m);
			ret.add(nitem);
		}
		if (ret == null) {
			MinigamesAPI.getAPI().getLogger().severe("Found invalid class in config!");
		}
		return ret;
	}
}
