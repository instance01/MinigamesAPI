package com.comze_instancelabs.minigamesapi;

import java.awt.geom.Arc2D.Double;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

	/**
	 * Shows the particles of a redstone block breaking
	 * 
	 * @param p
	 */
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

			// Move the effect (fake player) to 0 0 0 after 4 seconds
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

	/**
	 * Respawns a player using moveToWorld (which among others also sends a respawn packet)
	 * 
	 * @param p
	 *            Player to send it to
	 * @param plugin
	 */
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

	/**
	 * Plays a title/subtitle
	 * 
	 * @param player
	 *            Player to play the title to
	 * @param title
	 *            The title string
	 * @param enumindex
	 *            The enum index, can be 0 for title, 1 for subtitle, 4 for reset
	 */
	public static void playTitle(Player player, String title, int enumindex) {
		if (enumindex > 4) {
			enumindex = 0;
		}
		try {
			final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
			final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
			playerConnection.setAccessible(true);
			final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Packet"));
			final Method a = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".ChatSerializer").getMethod("a", String.class);

			Constructor packetPlayOutTitleConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutTitle").getConstructor();

			final Object packet = packetPlayOutTitleConstr.newInstance();
			setValue(packet, "a", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EnumTitleAction").getEnumConstants()[enumindex]);
			setValue(packet, "b", a.invoke(null, "{text:\"" + title + "\"}"));

			sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packet);
		} catch (Exception e) {
			System.out.println("Failed sending title packet: " + e.getMessage());
		}
	}

	// TODO Use for damage indicators!
	public static void playHologram(final Player p, Location l, String text) {
		try {
			final Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
			final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
			playerConnection.setAccessible(true);
			final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Packet"));

			Class craftw = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".CraftWorld");
			Class w = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".World");
			Class entity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Entity");
			Method getWorldHandle = craftw.getDeclaredMethod("getHandle");
			Object worldServer = getWorldHandle.invoke(craftw.cast(l.getWorld()));
			Constructor packetPlayOutSpawnEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutSpawnEntity").getConstructor(entity, int.class);
			Constructor packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutSpawnEntityLiving").getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityLiving"));
			Constructor packetPlayOutAttachEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutAttachEntity").getConstructor(int.class, entity, entity);
			final Constructor packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutEntityDestroy").getConstructor(int[].class);

			// WitherSkull
			Constructor witherSkullConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityWitherSkull").getConstructor(w);
			Object witherSkull = witherSkullConstr.newInstance(worldServer);
			Method setLoc = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
			setLoc.invoke(witherSkull, l.getX(), l.getY() + 36D, l.getZ(), 0F, 0F);
			Method getWitherSkullId = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
			final int witherSkullId = (Integer) (getWitherSkullId.invoke(witherSkull));

			// EntityHorse
			Constructor entityHorseConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityHorse").getConstructor(w);
			Object entityHorse = entityHorseConstr.newInstance(worldServer);
			Method setLoc2 = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
			setLoc2.invoke(entityHorse, l.getX(), l.getY() + 36D, l.getZ(), 0F, 0F);
			Method setAge = entityHorse.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setAge", int.class);
			setAge.invoke(entityHorse, -1000000);
			Method setCustomName = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", String.class);
			setCustomName.invoke(entityHorse, text);
			Method setCustomNameVisible = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", boolean.class);
			setCustomNameVisible.invoke(entityHorse, true);
			Method getHorseId = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
			final int horseId = (Integer) (getHorseId.invoke(entityHorse));

			// Send Witherskull+EntityHorse packet
			Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(entityHorse);
			sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), horsePacket);
			Object witherPacket = packetPlayOutSpawnEntityConstr.newInstance(witherSkull, 64);
			sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), witherPacket);

			// Send attach packet
			Object attachPacket = packetPlayOutAttachEntityConstr.newInstance(0, entityHorse, witherSkull);
			sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), attachPacket);

			// Remove both entities (and thus the hologram) after 4 seconds
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance((Object) new int[] { witherSkullId, horseId });
						sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 20L * 4);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
