package com.comze_instancelabs.minigamesapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

	public static int getClientProtocolVersion(Player p) {
		int ret = 0;
		try {
			if (MinigamesAPI.getAPI().version.equalsIgnoreCase("v1_8_r1")) {
				Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
				Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
				playerConnection.setAccessible(true);
				Object playerConInstance = playerConnection.get(getHandle.invoke(p));
				Field networkManager = playerConInstance.getClass().getField("networkManager");
				networkManager.setAccessible(true);
				Object networkManagerInstance = networkManager.get(playerConInstance);
				Method getVersion = networkManagerInstance.getClass().getMethod("getVersion");
				Object version = getVersion.invoke(networkManagerInstance);
				ret = (Integer) version;
			}
		} catch (Exception e) {
			if (MinigamesAPI.debug) {
				e.printStackTrace();
			}
		}
		return ret;
	}

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

	public static void setValue(Object instance, String fieldName, Object value) throws Exception {
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

	static HashMap<Integer, Integer> effectlocd = new HashMap<Integer, Integer>();
	static HashMap<Integer, Integer> effectlocd_taskid = new HashMap<Integer, Integer>();

	/**
	 * Sends a hologram to a player
	 * 
	 * @param p
	 *            Player to send the hologram to
	 * @param l
	 *            Location where the hologram will spawn (and slowly move down)
	 * @param text
	 *            Hologram text
	 */
	public static void playHologram(final Player p, final Location l, String text) {
		if (MinigamesAPI.getAPI().version.equalsIgnoreCase("v1_8_r1")) {
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
				final Constructor packetPlayOutSpawnEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutSpawnEntity").getConstructor(entity, int.class);
				final Constructor packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutSpawnEntityLiving").getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityLiving"));
				final Constructor packetPlayOutAttachEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutAttachEntity").getConstructor(int.class, entity, entity);
				final Constructor packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutEntityDestroy").getConstructor(int[].class);
				final Constructor packetPlayOutEntityVelocity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutEntityVelocity").getConstructor(int.class, double.class, double.class, double.class);

				// EntityArmorStand
				Constructor entityArmorStandConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityArmorStand").getConstructor(w);
				final Object entityArmorStand = entityArmorStandConstr.newInstance(worldServer);
				final Method setLoc2 = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
				setLoc2.invoke(entityArmorStand, l.getX(), l.getY() - 1D, l.getZ(), 0F, 0F);
				Method setCustomName = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", String.class);
				setCustomName.invoke(entityArmorStand, text);
				Method setCustomNameVisible = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", boolean.class);
				setCustomNameVisible.invoke(entityArmorStand, true);
				Method getArmorStandId = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
				final int armorstandId = (Integer) (getArmorStandId.invoke(entityArmorStand));
				Method setInvisble = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible", boolean.class);
				setInvisble.invoke(entityArmorStand, true);

				effectlocd.put(armorstandId, 12); // send move packet 12 times

				// Send EntityArmorStand packet
				Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(entityArmorStand);
				sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), horsePacket);

				// Send velocity packets to move the entities slowly down
				effectlocd_taskid.put(armorstandId, Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
					public void run() {
						try {
							int i = effectlocd.get(armorstandId);
							Object packet = packetPlayOutEntityVelocity.newInstance(armorstandId, 0D, -0.05D, 0D);
							sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), packet);
							if (i < -1) {
								int taskid = effectlocd_taskid.get(armorstandId);
								effectlocd_taskid.remove(armorstandId);
								effectlocd.remove(armorstandId);
								Bukkit.getScheduler().cancelTask(taskid);
								return;
							}
							effectlocd.put(armorstandId, effectlocd.get(armorstandId) - 1);
						} catch (Exception e) {
							if (MinigamesAPI.debug) {
								e.printStackTrace();
							}
						}
					}
				}, 2L, 2L).getTaskId());

				// Remove both entities (and thus the hologram) after 2 seconds
				Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
					public void run() {
						try {
							Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance((Object) new int[] { armorstandId });
							sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);
						} catch (Exception e) {
							if (MinigamesAPI.debug) {
								e.printStackTrace();
							}
						}
					}
				}, 20L * 2);

			} catch (Exception e) {
				if (MinigamesAPI.debug) {
					e.printStackTrace();
				}
			}
			return;
		}
		try {
			// If player is on 1.8, we'll have to use armor stands, otherwise just use the old 1.7 technique
			final boolean playerIs1_8 = getClientProtocolVersion(p) > 5;

			final Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".entity.CraftPlayer").getMethod("getHandle");
			final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityPlayer").getField("playerConnection");
			playerConnection.setAccessible(true);
			final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Packet"));

			Class craftw = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().version + ".CraftWorld");
			Class w = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".World");
			Class entity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".Entity");
			Method getWorldHandle = craftw.getDeclaredMethod("getHandle");
			Object worldServer = getWorldHandle.invoke(craftw.cast(l.getWorld()));
			final Constructor packetPlayOutSpawnEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutSpawnEntity").getConstructor(entity, int.class);
			final Constructor packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutSpawnEntityLiving").getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityLiving"));
			final Constructor packetPlayOutAttachEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutAttachEntity").getConstructor(int.class, entity, entity);
			final Constructor packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutEntityDestroy").getConstructor(int[].class);
			final Constructor packetPlayOutEntityVelocity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".PacketPlayOutEntityVelocity").getConstructor(int.class, double.class, double.class, double.class);

			// WitherSkull
			Constructor witherSkullConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityWitherSkull").getConstructor(w);
			final Object witherSkull = witherSkullConstr.newInstance(worldServer);
			final Method setLoc = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
			setLoc.invoke(witherSkull, l.getX(), l.getY() + 33D, l.getZ(), 0F, 0F);
			Method getWitherSkullId = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
			final int witherSkullId = (Integer) (getWitherSkullId.invoke(witherSkull));

			// EntityHorse
			Constructor entityHorseConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().version + ".EntityHorse").getConstructor(w);
			final Object entityHorse = entityHorseConstr.newInstance(worldServer);
			final Method setLoc2 = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
			setLoc2.invoke(entityHorse, l.getX(), l.getY() + (playerIs1_8 ? -1D : 33D), l.getZ(), 0F, 0F);
			Method setAge = entityHorse.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setAge", int.class);
			setAge.invoke(entityHorse, -1000000);
			Method setCustomName = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", String.class);
			setCustomName.invoke(entityHorse, text);
			Method setCustomNameVisible = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", boolean.class);
			setCustomNameVisible.invoke(entityHorse, true);
			Method getHorseId = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
			final int horseId = (Integer) (getHorseId.invoke(entityHorse));

			if (playerIs1_8) {
				// Set horse (later armor stand) invisible
				Method setInvisble = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible", boolean.class);
				setInvisble.invoke(entityHorse, true);
			}

			effectlocd.put(horseId, 12); // send move packet 12 times

			// Send Witherskull+EntityHorse packet
			Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(entityHorse);
			if (playerIs1_8) {
				// Set entity id to 30 (armor stand):
				setValue(horsePacket, "b", 30);
				// Fix datawatcher values to prevent crashes (ofc armor stands expect other data than horses):
				Field datawatcher = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("datawatcher");
				datawatcher.setAccessible(true);
				Object datawatcherInstance = datawatcher.get(entityHorse);
				Field d = datawatcherInstance.getClass().getDeclaredField("d");
				d.setAccessible(true);
				Map dmap = (Map) d.get(datawatcherInstance);
				dmap.remove(10);
				// These are the Rotation ones
				dmap.remove(11);
				dmap.remove(12);
				dmap.remove(13);
				dmap.remove(14);
				dmap.remove(15);
				dmap.remove(16);
				Method a = datawatcherInstance.getClass().getDeclaredMethod("a", int.class, Object.class);
				a.invoke(datawatcherInstance, 10, (byte) 0);
			}
			sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), horsePacket);
			if (!playerIs1_8) {
				Object witherPacket = packetPlayOutSpawnEntityConstr.newInstance(witherSkull, 64);
				sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), witherPacket);
			}

			// Send attach packet
			if (!playerIs1_8) {
				Object attachPacket = packetPlayOutAttachEntityConstr.newInstance(0, entityHorse, witherSkull);
				sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), attachPacket);
			}

			// Send velocity packets to move the entities slowly down
			effectlocd_taskid.put(horseId, Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						int i = effectlocd.get(horseId);
						Object packet = packetPlayOutEntityVelocity.newInstance(horseId, 0D, -0.05D, 0D);
						sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), packet);
						if (!playerIs1_8) {
							Object packet2 = packetPlayOutEntityVelocity.newInstance(witherSkullId, 0D, -0.05D, 0D);
							sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), packet2);
						}
						if (i < -1) {
							int taskid = effectlocd_taskid.get(horseId);
							effectlocd_taskid.remove(horseId);
							effectlocd.remove(horseId);
							Bukkit.getScheduler().cancelTask(taskid);
							return;
						}
						effectlocd.put(horseId, effectlocd.get(horseId) - 1);
					} catch (Exception e) {
						if (MinigamesAPI.debug) {
							e.printStackTrace();
						}
					}
				}
			}, 2L, 2L).getTaskId());

			// Remove both entities (and thus the hologram) after 2 seconds
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance((Object) new int[] { witherSkullId, horseId });
						sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);
					} catch (Exception e) {
						if (MinigamesAPI.debug) {
							e.printStackTrace();
						}
					}
				}
			}, 20L * 2);

		} catch (Exception e) {
			if (MinigamesAPI.debug) {
				e.printStackTrace();
			}
		}
	}
}
