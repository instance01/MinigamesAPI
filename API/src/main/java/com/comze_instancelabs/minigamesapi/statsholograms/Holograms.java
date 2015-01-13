package com.comze_instancelabs.minigamesapi.statsholograms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.HologramsConfig;

public class Holograms {

	PluginInstance pli;
	HashMap<Location, Hologram> holo = new HashMap<Location, Hologram>();

	public Holograms(PluginInstance pli) {
		this.pli = pli;
		loadHolograms();
	}

	public void loadHolograms() {
		HologramsConfig config = pli.getHologramsConfig();
		if (config.getConfig().isSet("holograms.")) {
			for (String str : config.getConfig().getConfigurationSection("holograms.").getKeys(false)) {
				String base = "holograms." + str;
				try {
					Location l = new Location(Bukkit.getWorld(config.getConfig().getString(base + ".world")), config.getConfig().getDouble(base + ".location.x"), config.getConfig().getDouble(base + ".location.y"), config.getConfig().getDouble(base + ".location.z"), (float) config.getConfig().getDouble(base + ".location.yaw"), (float) config.getConfig().getDouble(base + ".location.pitch"));
					if (l != null && l.getWorld() != null) {
						holo.put(l, new Hologram(pli, l));
					}
				} catch (Exception e) {
					ArenaLogger.debug("Failed loading hologram as invalid location was found: " + e.getMessage());
				}
			}
		}
	}

	public void sendAllHolograms(Player p) {
		for (Hologram h : holo.values()) {
			h.send(p);
		}
	}

	public void addHologram(Location l) {
		String base = "holograms." + Integer.toString((int) Math.round(Math.random() * 10000));
		HologramsConfig config = pli.getHologramsConfig();
		config.getConfig().set(base + ".world", l.getWorld().getName());
		config.getConfig().set(base + ".location.x", l.getX());
		config.getConfig().set(base + ".location.y", l.getY());
		config.getConfig().set(base + ".location.z", l.getZ());
		config.getConfig().set(base + ".location.yaw", l.getYaw());
		config.getConfig().set(base + ".location.pitch", l.getPitch());
		config.saveConfig();
		Hologram h = new Hologram(pli, l);
		holo.put(l, h);
	}

	public boolean removeHologram(Location ploc) {
		HologramsConfig config = pli.getHologramsConfig();
		if (config.getConfig().isSet("holograms.")) {
			for (String str : config.getConfig().getConfigurationSection("holograms.").getKeys(false)) {
				String base = "holograms." + str;
				Location l = new Location(Bukkit.getWorld(config.getConfig().getString(base + ".world")), config.getConfig().getDouble(base + ".location.x"), config.getConfig().getDouble(base + ".location.y"), config.getConfig().getDouble(base + ".location.z"), (float) config.getConfig().getDouble(base + ".location.yaw"), (float) config.getConfig().getDouble(base + ".location.pitch"));
				if (l.distance(ploc) <= 2) {
					config.getConfig().set("holograms." + str, null);
					config.saveConfig();
					if (holo.containsKey(l)) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							destroyHologram(p, holo.get(l));
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	public void destroyHologram(final Player p, Hologram h) {
		String version = MinigamesAPI.getAPI().version;
		try {
			final Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
			final Field playerConnection = Class.forName("net.minecraft.server." + version + ".EntityPlayer").getField("playerConnection");
			playerConnection.setAccessible(true);
			final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));

			final Constructor packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + version + ".PacketPlayOutEntityDestroy").getConstructor(int[].class);

			Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance((Object) convertIntegers(h.getIds()));
			sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int[] convertIntegers(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}
}
