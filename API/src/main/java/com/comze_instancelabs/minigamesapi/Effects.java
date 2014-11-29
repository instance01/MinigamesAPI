package com.comze_instancelabs.minigamesapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesapi.util.ParticleEffectNew;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Effects {

	public static void playBloodEffect(Player p) {
		p.getWorld().playEffect(p.getLocation().add(0D, 1D, 0D), Effect.STEP_SOUND, 152);
	}

	public static void playEffect(Arena a, Location l, String effectname) {
		for (String p_ : a.getAllPlayers()) {
			if (Validator.isPlayerOnline(p_)) {
				Player p = Bukkit.getPlayer(p_);
				ParticleEffectNew eff = ParticleEffectNew.valueOf(effectname);
				eff.setId(152);
				eff.animateReflected(p, l, 1F, 3);
			}
		}
	}

	public static BukkitTask playFakeBed(Arena a, Player p) throws Exception {
		return playFakeBed(a, p, p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
	}

	public static BukkitTask playFakeBed(final Arena a, final Player p, int x, int y, int z) throws Exception {
		try {
			final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
			final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
			playerConnection.setAccessible(true);
			final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Packet"));

			Constructor packetPlayOutNamedEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutNamedEntitySpawn").getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityHuman"));
			Constructor packetPlayOutBedConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutBed").getConstructor();

			final int id = -p.getEntityId() - 1000;

			final Object packet = packetPlayOutNamedEntityConstr.newInstance(getHandle.invoke(p));
			setValue(packet, "a", id);

			final Object packet_ = packetPlayOutBedConstr.newInstance();
			setValue(packet_, "a", id);
			setValue(packet_, "b", x);
			setValue(packet_, "c", y);
			setValue(packet_, "d", z);

			for (String p_ : a.getAllPlayers()) {
				Player p__ = Bukkit.getPlayer(p_);
				sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packet);
				sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packet_);
			}
			final ArrayList<String> tempp = new ArrayList<String>(a.getAllPlayers());
			final World currentworld = p.getWorld();
			return Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						setValue(packet_, "a", id);
						setValue(packet_, "b", 0);
						setValue(packet_, "c", 0);
						setValue(packet_, "d", 0);
						for (String p_ : tempp) {
							if (Validator.isPlayerOnline(p_)) {
								Player p__ = Bukkit.getPlayer(p_);
								if (p__.getWorld() == currentworld) {
									sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packet);
									sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packet_);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 20L * 4);
		} catch (Exception e) {
			System.out.println("Failed playing fakebed effect: " + e.getMessage());
		}
		return null;
	}

	private static void setValue(Object instance, String fieldName, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	public static void playRespawn(final Player p, JavaPlugin plugin) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				try {
					final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
					final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
					final Field minecraftServer = playerConnection.get(getHandle.invoke(p)).getClass().getDeclaredField("minecraftServer");
					minecraftServer.setAccessible(true);

					Object nmsMcServer = minecraftServer.get(playerConnection.get(getHandle.invoke(p)));
					Object playerlist = nmsMcServer.getClass().getDeclaredMethod("getPlayerList").invoke(nmsMcServer);
					Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer"), int.class, boolean.class);
					moveToWorld.invoke(playerlist, getHandle.invoke(p), 0, false);
				} catch (Exception e) {
					System.out.println("Failed additional respawn packet: " + e.getMessage());
				}
			}
		}, 1L);
	}

	// TODO Unused right now
	public void playAura(Player p, int cr) {
		int cradius_s = cr * cr;
		Location start = p.getLocation();
		int x = start.getBlockX();
		int y = start.getBlockY();
		int z = start.getBlockZ();
		for (int x_ = -cr; x_ <= cr; x_++) {
			for (int z_ = -cr; z_ <= cr; z_++) {
				int t = (x_ * x_) + (z_ * z_);
				if (t >= cradius_s && t <= (cradius_s + 90)) {
					p.playEffect(new Location(start.getWorld(), x - x_, y, z - z_), Effect.POTION_BREAK, 5);
				}
			}
		}
	}

	public static void playTitle(Player player, String title) {
		try {
			final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
			final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
			playerConnection.setAccessible(true);
			final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Packet"));
			final Method a = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".ChatSerializer").getMethod("a", String.class);

			Constructor packetPlayOutTitleConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutTitle").getConstructor();

			final Object packet = packetPlayOutTitleConstr.newInstance();
			setValue(packet, "a", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EnumTitleAction").getEnumConstants()[0]);
			setValue(packet, "b", a.invoke(null, "{text:\"" + title + "\"}"));

			sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packet);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
