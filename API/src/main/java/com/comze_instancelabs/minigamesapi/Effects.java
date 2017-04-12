/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.comze_instancelabs.minigamesapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesapi.util.ParticleEffectNew;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * Particle/Animation helper.
 * 
 * @author instancelabs
 */
public class Effects
{
    
    /**
     * Shows the particles of a redstone block breaking, simulating a blood effect.
     * 
     * @param p
     *            target player.
     */
    public static void playBloodEffect(final Player p)
    {
        p.getWorld().playEffect(p.getLocation().add(0D, 1D, 0D), Effect.STEP_SOUND, 152);
    }
    
    /**
     * Plays an effect by name.
     * 
     * @param a
     *            arena
     * @param l
     *            target location
     * @param effectname
     *            effect name.
     */
    public static void playEffect(final Arena a, final Location l, final String effectname)
    {
        for (final String p_ : a.getAllPlayers())
        {
            if (Validator.isPlayerOnline(p_))
            {
                final Player p = Bukkit.getPlayer(p_);
                final ParticleEffectNew eff = ParticleEffectNew.valueOf(effectname);
                eff.setId(55);
                eff.animateReflected(p, l, 1F, 2);
            }
        }
    }
    
    /**
     * Places a fake bed on the current player position.
     * 
     * @param a
     *            arena
     * @param p
     *            target player
     * @return bukkit task that can be cancelled.
     */
    public static BukkitTask playFakeBed(final Arena a, final Player p)
    {
        return Effects.playFakeBed(a, p, p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
    }
    
    /**
     * Places a fake bed on the given position.
     * 
     * @param a
     *            arena
     * @param p
     *            target player
     * @param x
     *            x-position
     * @param y
     *            y-position
     * @param z
     *            z-position
     * @return bukkit task that can be cancelled.
     */
    public static BukkitTask playFakeBed(final Arena a, final Player p, final int x, final int y, final int z)
    {
        try
        {
            final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".entity.CraftPlayer").getMethod("getHandle");
            final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityPlayer").getField("playerConnection");
            playerConnection.setAccessible(true);
            final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Packet"));
            
            final Constructor<?> packetPlayOutNamedEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutNamedEntitySpawn")
                    .getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityHuman"));
            final Constructor<?> packetPlayOutBedConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutBed").getConstructor();
            
            final int id = -p.getEntityId() - 1000;
            
            final Object packetNamedEntity = packetPlayOutNamedEntityConstr.newInstance(getHandle.invoke(p));
            Effects.setValue(packetNamedEntity, "a", id);
            
            final Object packetFakeBed = packetPlayOutBedConstr.newInstance();
            Effects.setValue(packetFakeBed, "a", id);
            final Object packetFakeBed2 = packetPlayOutBedConstr.newInstance();
            Effects.setValue(packetFakeBed2, "a", id);
            if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_8_R1))
            {
                final Class<?> bpClazz = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".BlockPosition");
                final Constructor<?> ctor = bpClazz.getDeclaredConstructor(int.class, int.class, int.class);
                Effects.setValue(packetFakeBed, "b", ctor.newInstance(x, y, z));
                
                Effects.setValue(packetFakeBed2, "b", ctor.newInstance(0, 0, 0));
            }
            else
            {
                Effects.setValue(packetFakeBed, "b", x);
                Effects.setValue(packetFakeBed, "c", y);
                Effects.setValue(packetFakeBed, "d", z);
                Effects.setValue(packetFakeBed2, "b", 0);
                Effects.setValue(packetFakeBed2, "c", 0);
                Effects.setValue(packetFakeBed2, "d", 0);
            }
            
            for (final String p_ : a.getAllPlayers())
            {
                final Player p__ = Bukkit.getPlayer(p_);
                sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packetNamedEntity);
                sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packetFakeBed);
            }
            
            // Move the effect (fake player) to 0 0 0 after 4 seconds
            final ArrayList<String> tempp = new ArrayList<>(a.getAllPlayers());
            final World currentworld = p.getWorld();
            return Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
                try
                {
                    for (final String p_ : tempp)
                    {
                        if (Validator.isPlayerOnline(p_))
                        {
                            final Player p__ = Bukkit.getPlayer(p_);
                            if (p__.getWorld() == currentworld)
                            {
                                sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packetNamedEntity);
                                sendPacket.invoke(playerConnection.get(getHandle.invoke(p__)), packetFakeBed2);
                            }
                        }
                    }
                }
                catch (final Exception e)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                }
            }, 20L * 4);
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed playing fakebed effect", e);
        }
        return null;
    }
    
    /**
     * Sets private object value.
     * 
     * @param instance
     *            object instance
     * @param fieldName
     *            field name
     * @param value
     *            new value
     * @throws Exception
     *             thrown on problems setting the field.
     */
    private static void setValue(final Object instance, final String fieldName, final Object value) throws Exception
    {
        final Field field = instance.getClass().getDeclaredField(fieldName);
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
    public static void playRespawn(final Player p, final JavaPlugin plugin)
    {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try
            {
                final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".entity.CraftPlayer").getMethod("getHandle");
                final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityPlayer").getField("playerConnection");
                final Field minecraftServer = playerConnection.get(getHandle.invoke(p)).getClass().getDeclaredField("minecraftServer");
                minecraftServer.setAccessible(true);
                
                final Object nmsMcServer = minecraftServer.get(playerConnection.get(getHandle.invoke(p)));
                final Object playerlist = nmsMcServer.getClass().getDeclaredMethod("getPlayerList").invoke(nmsMcServer);
                final Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityPlayer"),
                        int.class, boolean.class);
                moveToWorld.invoke(playerlist, getHandle.invoke(p), 0, false);
            }
            catch (final Exception e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed additional respawn packet", e);
            }
        }, 1L);
    }
    
    /**
     * Plays a title/subtitle
     * 
     * @param player
     *            Player to play the title to
     * @param title
     *            The title string
     * @param eindex
     *            The enum index, can be 0 for title, 1 for subtitle, 4 for reset
     */
    public static void playTitle(final Player player, final String title, int eindex)
    {
        int enumindex = eindex;
        if (enumindex > 4)
        {
            enumindex = 0;
        }
        try
        {
            final Method getHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".entity.CraftPlayer").getMethod("getHandle");
            final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityPlayer").getField("playerConnection");
            playerConnection.setAccessible(true);
            final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Packet"));
            Class<?> enumClass = null;
            Object chatComp = null;
            if (MinigamesAPI.SERVER_VERSION.isBelow(MinecraftVersionsType.V1_8_R2))
            {
                Method a = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".ChatSerializer").getMethod("a", String.class);
                enumClass = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EnumTitleAction");
                chatComp = a.invoke(null, "{text:\"" + title + "\"}");
            }
            else
            {
                enumClass = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutTitle$EnumTitleAction");
                chatComp = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".ChatComponentText").getConstructor(String.class).newInstance(title);
            }
            
            final Constructor<?> packetPlayOutTitleConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutTitle").getConstructor();
            final Object packet = packetPlayOutTitleConstr.newInstance();
            Effects.setValue(packet, "a", enumClass.getEnumConstants()[enumindex]);
            Effects.setValue(packet, "b", chatComp);
            
            sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packet);
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed sending title packet", e);
        }
    }
    
    static HashMap<Integer, Integer> effectlocd        = new HashMap<>();
    static HashMap<Integer, Integer> effectlocd_taskid = new HashMap<>();
    
    /**
     * Sends a hologram to a player
     * 
     * @param p
     *            Player to send the hologram to
     * @param l
     *            Location where the hologram will spawn (and slowly move down)
     * @param text
     *            Hologram text
     * @param moveDown
     *            Whether to play a moving down animation
     * @param removeAfterCooldown
     *            Whether to remove the hologram after a few seconds or not
     * @return ids
     */
    public static ArrayList<Integer> playHologram(final Player p, final Location l, final String text, final boolean moveDown, final boolean removeAfterCooldown)
    {
        final ArrayList<Integer> ret = new ArrayList<>();
        if (MinigamesAPI.SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
        {
            try
            {
                final Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".entity.CraftPlayer").getMethod("getHandle");
                final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityPlayer").getField("playerConnection");
                playerConnection.setAccessible(true);
                final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Packet"));
                
                final Class<?> craftw = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".CraftWorld");
                final Class<?> w = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".World");
                final Class<?> entity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Entity");
                final Method getWorldHandle = craftw.getDeclaredMethod("getHandle");
                final Object worldServer = getWorldHandle.invoke(craftw.cast(l.getWorld()));
//                Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutSpawnEntity").getConstructor(entity, int.class);
                final Constructor<?> packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutSpawnEntityLiving")
                        .getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityLiving"));
//                Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutAttachEntity").getConstructor(int.class, entity, entity);
                final Constructor<?> packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutEntityDestroy")
                        .getConstructor(int[].class);
                final Constructor<?> packetPlayOutEntityVelocity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutEntityVelocity")
                        .getConstructor(int.class, double.class, double.class, double.class);
                
                // EntityArmorStand
                final Constructor<?> entityArmorStandConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityArmorStand").getConstructor(w);
                final Object entityArmorStand = entityArmorStandConstr.newInstance(worldServer);
                final Method setLoc2 = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
                setLoc2.invoke(entityArmorStand, l.getX(), l.getY() - 1D, l.getZ(), 0F, 0F);
                final Method setCustomName = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", String.class);
                setCustomName.invoke(entityArmorStand, text);
                final Method setCustomNameVisible = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", boolean.class);
                setCustomNameVisible.invoke(entityArmorStand, true);
                final Method getArmorStandId = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
                final int armorstandId = (Integer) (getArmorStandId.invoke(entityArmorStand));
                final Method setInvisble = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible", boolean.class);
                setInvisble.invoke(entityArmorStand, true);
                
                Effects.effectlocd.put(armorstandId, 12); // send move packet 12 times
                
                // Send EntityArmorStand packet
                final Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(entityArmorStand);
                sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), horsePacket);
                
                // Send velocity packets to move the entities slowly down
                if (moveDown)
                {
                    Effects.effectlocd_taskid.put(armorstandId, Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), () -> {
                        try
                        {
                            final int i = Effects.effectlocd.get(armorstandId);
                            final Object packet = packetPlayOutEntityVelocity.newInstance(armorstandId, 0D, -0.05D, 0D);
                            sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), packet);
                            if (i < -1)
                            {
                                final int taskid = Effects.effectlocd_taskid.get(armorstandId);
                                Effects.effectlocd_taskid.remove(armorstandId);
                                Effects.effectlocd.remove(armorstandId);
                                Bukkit.getScheduler().cancelTask(taskid);
                                return;
                            }
                            Effects.effectlocd.put(armorstandId, Effects.effectlocd.get(armorstandId) - 1);
                        }
                        catch (final Exception e)
                        {
                            if (MinigamesAPI.debug)
                            {
                                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                            }
                        }
                    }, 2L, 2L).getTaskId());
                }
                
                // Remove both entities (and thus the hologram) after 2 seconds
                if (removeAfterCooldown)
                {
                    Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
                        try
                        {
                            final Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance(new int[] { armorstandId });
                            sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);
                        }
                        catch (final Exception e)
                        {
                            if (MinigamesAPI.debug)
                            {
                                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                            }
                        }
                    }, 20L * 2);
                }
                
                ret.add(armorstandId);
                
            }
            catch (final Exception e)
            {
                if (MinigamesAPI.debug)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                }
            }
            return ret;
        }
        try
        {
            // If player is on 1.8, we'll have to use armor stands, otherwise just use the old 1.7 technique
            final boolean playerIs1_8 = MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_8);
            
            final Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".entity.CraftPlayer").getMethod("getHandle");
            final Field playerConnection = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityPlayer").getField("playerConnection");
            playerConnection.setAccessible(true);
            final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Packet"));
            
            final Class<?> craftw = Class.forName("org.bukkit.craftbukkit." + MinigamesAPI.getAPI().internalServerVersion + ".CraftWorld");
            final Class<?> w = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".World");
            final Class<?> entity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Entity");
            final Method getWorldHandle = craftw.getDeclaredMethod("getHandle");
            final Object worldServer = getWorldHandle.invoke(craftw.cast(l.getWorld()));
            final Constructor<?> packetPlayOutSpawnEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutSpawnEntity")
                    .getConstructor(entity, int.class);
            final Constructor<?> packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutSpawnEntityLiving")
                    .getConstructor(Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityLiving"));
            final Constructor<?> packetPlayOutAttachEntityConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutAttachEntity")
                    .getConstructor(int.class, entity, entity);
            final Constructor<?> packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutEntityDestroy")
                    .getConstructor(int[].class);
            final Constructor<?> packetPlayOutEntityVelocity = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutEntityVelocity")
                    .getConstructor(int.class, double.class, double.class, double.class);
            
            // WitherSkull
            final Constructor<?> witherSkullConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityWitherSkull").getConstructor(w);
            final Object witherSkull = witherSkullConstr.newInstance(worldServer);
            final Method setLoc = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            setLoc.invoke(witherSkull, l.getX(), l.getY() + 33D, l.getZ(), 0F, 0F);
            final Method getWitherSkullId = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
            final int witherSkullId = (Integer) (getWitherSkullId.invoke(witherSkull));
            
            // EntityHorse
            final Constructor<?> entityHorseConstr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".EntityHorse").getConstructor(w);
            final Object entityHorse = entityHorseConstr.newInstance(worldServer);
            final Method setLoc2 = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class,
                    double.class, double.class, float.class, float.class);
            setLoc2.invoke(entityHorse, l.getX(), l.getY() + (playerIs1_8 ? -1D : 33D), l.getZ(), 0F, 0F);
            final Method setAge = entityHorse.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setAge", int.class);
            setAge.invoke(entityHorse, -1000000);
            final Method setCustomName = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", String.class);
            setCustomName.invoke(entityHorse, text);
            final Method setCustomNameVisible = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", boolean.class);
            setCustomNameVisible.invoke(entityHorse, true);
            final Method getHorseId = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getId");
            final int horseId = (Integer) (getHorseId.invoke(entityHorse));
            
            if (playerIs1_8)
            {
                // Set horse (later armor stand) invisible
                final Method setInvisble = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible",
                        boolean.class);
                setInvisble.invoke(entityHorse, true);
            }
            
            Effects.effectlocd.put(horseId, 12); // send move packet 12 times
            
            // Send Witherskull+EntityHorse packet
            final Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(entityHorse);
            if (playerIs1_8)
            {
                // Set entity id to 30 (armor stand):
                Effects.setValue(horsePacket, "b", 30);
                // Fix datawatcher values to prevent crashes (ofc armor stands expect other data than horses):
                final Field datawatcher = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("datawatcher");
                datawatcher.setAccessible(true);
                final Object datawatcherInstance = datawatcher.get(entityHorse);
                final Field d = datawatcherInstance.getClass().getDeclaredField("d");
                d.setAccessible(true);
                final Map<?, ?> dmap = (Map<?, ?>) d.get(datawatcherInstance);
                dmap.remove(10);
                // These are the Rotation ones
                dmap.remove(11);
                dmap.remove(12);
                dmap.remove(13);
                dmap.remove(14);
                dmap.remove(15);
                dmap.remove(16);
                final Method a = datawatcherInstance.getClass().getDeclaredMethod("a", int.class, Object.class);
                a.invoke(datawatcherInstance, 10, (byte) 0);
            }
            sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), horsePacket);
            if (!playerIs1_8)
            {
                final Object witherPacket = packetPlayOutSpawnEntityConstr.newInstance(witherSkull, 64);
                sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), witherPacket);
            }
            
            // Send attach packet
            if (!playerIs1_8)
            {
                final Object attachPacket = packetPlayOutAttachEntityConstr.newInstance(0, entityHorse, witherSkull);
                sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), attachPacket);
            }
            
            // Send velocity packets to move the entities slowly down
            if (moveDown)
            {
                Effects.effectlocd_taskid.put(horseId, Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), () -> {
                    try
                    {
                        final int i = Effects.effectlocd.get(horseId);
                        final Object packet = packetPlayOutEntityVelocity.newInstance(horseId, 0D, -0.05D, 0D);
                        sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), packet);
                        if (!playerIs1_8)
                        {
                            final Object packet2 = packetPlayOutEntityVelocity.newInstance(witherSkullId, 0D, -0.05D, 0D);
                            sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), packet2);
                        }
                        if (i < -1)
                        {
                            final int taskid = Effects.effectlocd_taskid.get(horseId);
                            Effects.effectlocd_taskid.remove(horseId);
                            Effects.effectlocd.remove(horseId);
                            Bukkit.getScheduler().cancelTask(taskid);
                            return;
                        }
                        Effects.effectlocd.put(horseId, Effects.effectlocd.get(horseId) - 1);
                    }
                    catch (final Exception e)
                    {
                        if (MinigamesAPI.debug)
                        {
                            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                        }
                    }
                }, 2L, 2L).getTaskId());
            }
            
            // Remove both entities (and thus the hologram) after 2 seconds
            if (removeAfterCooldown)
            {
                Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
                    try
                    {
                        final Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance(new int[] { witherSkullId, horseId });
                        sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);
                    }
                    catch (final Exception e)
                    {
                        if (MinigamesAPI.debug)
                        {
                            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                        }
                    }
                }, 20L * 2);
            }
            
            ret.add(witherSkullId);
            ret.add(horseId);
            
        }
        catch (final Exception e)
        {
            if (MinigamesAPI.debug)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
        return ret;
    }
    
    /**
     * Sends a game mode change to given player.
     * 
     * @param p
     *            target player
     * @param gamemode
     *            new game mode.
     */
    public static void sendGameModeChange(final Player p, final int gamemode)
    {
        // NOT_SET(-1, ""), SURVIVAL(0, "survival"), CREATIVE(1, "creative"), ADVENTURE(2, "adventure"), SPECTATOR(3, "spectator");
        
        if (MinigamesAPI.SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7) && gamemode == 3)
        {
            return;
        }
        
        p.setGameMode(GameMode.getByValue(gamemode));
    }
}
