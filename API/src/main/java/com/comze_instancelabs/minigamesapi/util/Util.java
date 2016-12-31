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
package com.comze_instancelabs.minigamesapi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinecraftVersionsType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;

public class Util
{
    
    public static HashMap<String, ItemStack[]> armourContents    = new HashMap<>();
    public static HashMap<String, ItemStack[]> inventoryContents = new HashMap<>();
    public static HashMap<String, Location>    locations         = new HashMap<>();
    public static HashMap<String, Integer>     xplevel           = new HashMap<>();
    public static HashMap<String, GameMode>    gamemode          = new HashMap<>();
    
    public static void clearInv(final Player p)
    {
        if (p != null)
        {
            ArenaLogger.debug("Clearing inventory of " + p.getName());
            p.getInventory().clear();
            p.updateInventory();
            p.getInventory().setHelmet(null);
            p.getInventory().setChestplate(null);
            p.getInventory().setLeggings(null);
            p.getInventory().setBoots(null);
            p.updateInventory();
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void teleportPlayerFixed(final Player p, final Location l)
    {
        ArenaLogger.debug("Teleporting " + p.getName());
        if (p.isInsideVehicle())
        {
            final Entity ent = p.getVehicle();
            p.leaveVehicle();
            ent.eject();
        }
        if (l != null)
        {
            if (l.getWorld() == null)
            {
                return;
            }
            p.teleport(l, TeleportCause.PLUGIN);
            p.setFallDistance(-1F);
            p.setVelocity(new Vector(0D, 0D, 0D));
            
            final Chunk chunk = l.getChunk();
            if (MinigamesAPI.SERVER_VERSION.isBelow(MinecraftVersionsType.V1_8))
            {
                l.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
            }
            else
            {
                try
                {
                    final Method getChunkHandle = chunk.getClass().getMethod("getHandle");
                    final Method getPlayerHandle = p.getClass().getMethod("getHandle");
                    final Object handle = getPlayerHandle.invoke(p);
                    final Field playerConnection = handle.getClass().getField("playerConnection");
                    playerConnection.setAccessible(true);
                    final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Packet"));
                    final Class<?> chunkClazz = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".Chunk");
                    final Object packet;
                    if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_9_R2))
                    {
                        final Constructor<?> constr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutMapChunk").getConstructor(chunkClazz, int.class);
                        packet = constr.newInstance(getChunkHandle.invoke(chunk), 20);
                    }
                    else
                    {
                        final Constructor<?> constr = Class.forName("net.minecraft.server." + MinigamesAPI.getAPI().internalServerVersion + ".PacketPlayOutMapChunk").getConstructor(chunkClazz, boolean.class, int.class);
                        packet = constr.newInstance(getChunkHandle.invoke(chunk), false, 20);
                    }
                    sendPacket.invoke(playerConnection.get(handle), packet);
                    
                    // ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(((CraftChunk)chunk).getHandle(), true, 65535));
                    chunk.unload(true);
                    chunk.load();
                }
                catch (Exception ex)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", ex);
                }
            }
        }
        else
        {
            MinigamesAPI.getAPI().getLogger().warning("Couldn't teleport Player " + p.getName() + ", the location was not valid. Probably forgot to set a spawn/lobby?");
        }
        p.setFireTicks(0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 30));
    }
    
    public static void teleportAllPlayers(final ArrayList<String> players, final Location l)
    {
        Long delay = 1L;
        for (final String pl : players)
        {
            if (!Validator.isPlayerOnline(pl))
            {
                continue;
            }
            final Player p = Bukkit.getPlayer(pl);
            Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> Util.teleportPlayerFixed(p, l), delay);
            delay++;
        }
    }
    
    public static HashMap<String, Location> teleportAllPlayers(final ArrayList<String> players, final ArrayList<Location> locs)
    {
        final HashMap<String, Location> pspawnloc = new HashMap<>();
        int currentid = 0;
        final int locslength = locs.size();
        for (final String p_ : players)
        {
            final Player p = Bukkit.getPlayer(p_);
            Util.teleportPlayerFixed(p, locs.get(currentid));
            pspawnloc.put(p_, locs.get(currentid));
            currentid++;
            if (currentid > locslength - 1)
            {
                currentid = 0;
            }
        }
        return pspawnloc;
    }
    
    public static Location getComponentForArena(final JavaPlugin plugin, final String arenaname, final String component, final String count)
    {
        if (Validator.isArenaValid(plugin, arenaname))
        {
            final String base = ArenaConfigStrings.ARENAS_PREFIX + arenaname + "." + component + count;
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
            if (!pli.getArenasConfig().getConfig().isSet(base + ".world") || Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")) == null)
            {
                return null;
            }
            return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"),
                    pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"),
                    (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
        }
        return null;
    }
    
    public static Location getComponentForArena(final JavaPlugin plugin, final String arenaname, final String component)
    {
        if (Validator.isArenaValid(plugin, arenaname))
        {
            final String base = ArenaConfigStrings.ARENAS_PREFIX + arenaname + "." + component;
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
            if (!pli.getArenasConfig().getConfig().isSet(base + ".world") || Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")) == null)
            {
                return null;
            }
            return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"),
                    pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"),
                    (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
        }
        return null;
    }
    
    public static Location getComponentForArenaRaw(final JavaPlugin plugin, final String arenaname, final String component)
    {
        final String base = ArenaConfigStrings.ARENAS_PREFIX + arenaname + "." + component;
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        if (pli.getArenasConfig().getConfig().isSet(base))
        {
            return new Location(Bukkit.getWorld(pli.getArenasConfig().getConfig().getString(base + ".world")), pli.getArenasConfig().getConfig().getDouble(base + ".location.x"),
                    pli.getArenasConfig().getConfig().getDouble(base + ".location.y"), pli.getArenasConfig().getConfig().getDouble(base + ".location.z"),
                    (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.yaw"), (float) pli.getArenasConfig().getConfig().getDouble(base + ".location.pitch"));
        }
        return null;
    }
    
    public static boolean isComponentForArenaValid(final JavaPlugin plugin, final String arenaname, final String component)
    {
        if (Validator.isArenaValid(plugin, arenaname))
        {
            return Util.isComponentForArenaValidRaw(plugin, arenaname, component);
        }
        return false;
    }
    
    public static boolean isComponentForArenaValidRaw(final JavaPlugin plugin, final String arenaname, final String component)
    {
        final String base = ArenaConfigStrings.ARENAS_PREFIX + arenaname + "." + component;
        return MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig().isSet(base);
    }
    
    public static void saveComponentForArena(final JavaPlugin plugin, final String arenaname, final String component, final Location comploc)
    {
        final String base = ArenaConfigStrings.ARENAS_PREFIX + arenaname + "." + component;
        final ArenasConfig config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig();
        config.getConfig().set(base + ".world", comploc.getWorld().getName());
        config.getConfig().set(base + ".location.x", comploc.getX());
        config.getConfig().set(base + ".location.y", comploc.getY());
        config.getConfig().set(base + ".location.z", comploc.getZ());
        config.getConfig().set(base + ".location.yaw", comploc.getYaw());
        config.getConfig().set(base + ".location.pitch", comploc.getPitch());
        config.saveConfig();
    }
    
    public static void saveMainLobby(final JavaPlugin plugin, final Location comploc)
    {
        final String base = "mainlobby";
        final ArenasConfig config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig();
        config.getConfig().set(base + ".world", comploc.getWorld().getName());
        config.getConfig().set(base + ".location.x", comploc.getX());
        config.getConfig().set(base + ".location.y", comploc.getY());
        config.getConfig().set(base + ".location.z", comploc.getZ());
        config.getConfig().set(base + ".location.yaw", comploc.getYaw());
        config.getConfig().set(base + ".location.pitch", comploc.getPitch());
        config.saveConfig();
    }
    
    public static Location getMainLobby(final JavaPlugin plugin)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
        if (!config.isSet("mainlobby"))
        {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You forgot to set the mainlobby!");
        }
        return new Location(plugin.getServer().getWorld(config.getString("mainlobby.world")), config.getDouble("mainlobby.location.x"), config.getDouble("mainlobby.location.y"),
                config.getDouble("mainlobby.location.z"), (float) config.getDouble("mainlobby.location.yaw"), (float) config.getDouble("mainlobby.location.pitch"));
    }
    
    public static ArrayList<Location> getAllSpawns(final JavaPlugin plugin, final String arena)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getArenasConfig().getConfig();
        final ArrayList<Location> ret = new ArrayList<>();
        if (config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".spawns"))
        {
            for (final String spawn : config.getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + arena + ".spawns.").getKeys(false))
            {
                ret.add(Util.getComponentForArena(plugin, arena, "spawns." + spawn));
            }
        }
        return ret;
    }
    
    public static void saveArenaToFile(final JavaPlugin plugin, final String arena)
    {
        final File f = new File(plugin.getDataFolder() + "/" + arena);
        final Cuboid c = new Cuboid(Util.getComponentForArena(plugin, arena, ArenaConfigStrings.BOUNDS_LOW), Util.getComponentForArena(plugin, arena, ArenaConfigStrings.BOUNDS_HIGH));
        final Location start = c.getLowLoc();
        final Location end = c.getHighLoc();
        
        final int width = end.getBlockX() - start.getBlockX();
        final int length = end.getBlockZ() - start.getBlockZ();
        final int height = end.getBlockY() - start.getBlockY();
        
        MinigamesAPI.getAPI().getLogger().info("Bounds: " + Integer.toString(width) + " " + Integer.toString(height) + " " + Integer.toString(length));
        MinigamesAPI.getAPI().getLogger().info("Blocks to save: " + Integer.toString(width * height * length));
        
        FileOutputStream fos;
        ObjectOutputStream oos = null;
        try
        {
            fos = new FileOutputStream(f);
            oos = new BukkitObjectOutputStream(fos);
        }
        catch (final IOException e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        
        for (int i = 0; i <= width; i++)
        {
            for (int j = 0; j <= height; j++)
            {
                for (int k = 0; k <= length; k++)
                {
                    final Block change = c.getWorld().getBlockAt(start.getBlockX() + i, start.getBlockY() + j, start.getBlockZ() + k);
                    
                    final ArenaBlock bl = new ArenaBlock(change, change.getType().equals(Material.CHEST));
                    
                    try
                    {
                        oos.writeObject(bl);
                    }
                    catch (final IOException e)
                    {
                        MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "io error", e);
                    }
                }
            }
        }
        
        try
        {
            oos.close();
        }
        catch (final IOException e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        
        MinigamesAPI.getAPI().getLogger().info("saved");
    }
    
    public static Sign getSignFromArena(final JavaPlugin plugin, final String arena)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        if (!pli.getArenasConfig().getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".sign.world"))
        {
            return null;
        }
        final Location b_ = new Location(Bukkit.getServer().getWorld(pli.getArenasConfig().getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + arena + ".sign.world")),
                pli.getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".sign.loc.x"), pli.getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".sign.loc.y"),
                pli.getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".sign.loc.z"));
        if (b_ != null)
        {
            if (b_.getWorld() != null)
            {
                if (b_.getBlock().getState() != null)
                {
                    final BlockState bs = b_.getBlock().getState();
                    Sign s_ = null;
                    if (bs instanceof Sign)
                    {
                        s_ = (Sign) bs;
                    }
                    return s_;
                }
            }
        }
        return null;
    }
    
    public static Sign getSpecSignFromArena(final JavaPlugin plugin, final String arena)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        if (!pli.getArenasConfig().getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".specsign.world"))
        {
            return null;
        }
        final Location b_ = new Location(Bukkit.getServer().getWorld(pli.getArenasConfig().getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + arena + ".specsign.world")),
                pli.getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".specsign.loc.x"), pli.getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".specsign.loc.y"),
                pli.getArenasConfig().getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + arena + ".specsign.loc.z"));
        if (b_ != null)
        {
            if (b_.getWorld() != null)
            {
                if (b_.getBlock().getState() != null)
                {
                    final BlockState bs = b_.getBlock().getState();
                    Sign s_ = null;
                    if (bs instanceof Sign)
                    {
                        s_ = (Sign) bs;
                    }
                    return s_;
                }
            }
        }
        return null;
    }
    
    public static Location getSignLocationFromArena(final JavaPlugin plugin, final String arena)
    {
        final Sign s = Util.getSignFromArena(plugin, arena);
        if (s != null)
        {
            return s.getBlock().getLocation();
        }
        else
        {
            return null;
        }
    }
    
    public static Arena getArenaBySignLocation(final JavaPlugin plugin, final Location sign)
    {
        for (final Arena arena : MinigamesAPI.getAPI().getPluginInstance(plugin).getArenas())
        {
            if (sign != null && arena.getSignLocation() != null)
            {
                if (sign.getWorld().getName().equalsIgnoreCase(arena.getSignLocation().getWorld().getName()))
                {
                    if (sign.distance(arena.getSignLocation()) < 1)
                    {
                        return arena;
                    }
                }
            }
        }
        return null;
    }
    
    public static Arena getArenaBySpecSignLocation(final JavaPlugin plugin, final Location sign)
    {
        for (final Arena arena : MinigamesAPI.getAPI().getPluginInstance(plugin).getArenas())
        {
            if (sign != null && arena.getSpecSignLocation() != null)
            {
                if (sign.getWorld().getName().equalsIgnoreCase(arena.getSpecSignLocation().getWorld().getName()))
                {
                    if (sign.distance(arena.getSpecSignLocation()) < 1)
                    {
                        return arena;
                    }
                }
            }
        }
        return null;
    }
    
    public static void updateSign(final JavaPlugin plugin, final Arena arena)
    {
        if (arena == null)
        {
            return;
        }
        
        Sign s = Util.getSignFromArena(plugin, arena.getInternalName());
        if (s != null)
        {
            ArenaLogger.debug("Updating sign for arena " + arena.getInternalName() + " in " + plugin.getName());
            final int count = arena.getAllPlayers().size();
            final int maxcount = arena.getMaxPlayers();
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
            final String state = arena.getArenaState().toString().toLowerCase();
            if (pli.cached_sign_states.containsKey(state))
            {
                s.setLine(0,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(0).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
                s.setLine(1,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(1).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
                s.setLine(2,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(2).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
                s.setLine(3,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(3).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
            }
            else
            {
                s.setLine(0,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
                s.setLine(1,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
                s.setLine(2,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
                s.setLine(3,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
            }
            s.getBlock().getChunk().load();
            s.update();
            if (pli.color_background_wool_of_signs)
            {
                final org.bukkit.material.Sign s_ = (org.bukkit.material.Sign) s.getBlock().getState().getData();
                final Block attachedBlock = s.getBlock().getRelative(s_.getAttachedFace());
                byte data = (byte) 5;
                if (arena.getArenaState() == ArenaState.INGAME)
                {
                    data = (byte) 14;
                }
                else if (arena.getArenaState() == ArenaState.RESTARTING)
                {
                    data = (byte) 4;
                }
                attachedBlock.setData(data);
            }
        }
        try
        {
            if (plugin.isEnabled())
            {
                BungeeUtil.sendSignUpdateRequest(plugin, plugin.getName(), arena);
            }
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed sending bungee sign update: ", e);
        }
        
        s = Util.getSpecSignFromArena(plugin, arena.getInternalName());
        if (s != null)
        {
            ArenaLogger.debug("Updating spectator sign for arena " + arena.getInternalName() + " in " + plugin.getName());
            final int count = arena.getAllPlayers().size();
            final int maxcount = arena.getMaxPlayers();
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
            final String state = "spec";
            if (pli.cached_sign_states.containsKey(state))
            {
                s.setLine(0,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(0).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
                s.setLine(1,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(1).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
                s.setLine(2,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(2).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
                s.setLine(3,
                        Signs.replaceSquares(pli.cached_sign_states.get(state).get(3).replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                                .replace("<arena>", arena.getDisplayName())));
            }
            else
            {
                s.setLine(0,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
                s.setLine(1,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
                s.setLine(2,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
                s.setLine(3,
                        Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + state + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                                .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
            }
            s.getBlock().getChunk().load();
            s.update();
        }
        try
        {
            if (plugin.isEnabled())
            {
                BungeeUtil.sendSignUpdateRequest(plugin, plugin.getName(), arena);
            }
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed sending bungee sign update: ", e);
        }
    }
    
    public static void updateSign(final JavaPlugin plugin, final Arena arena, final SignChangeEvent event)
    {
        final int count = arena.getAllPlayers().size();
        final int maxcount = arena.getMaxPlayers();
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        final String arenastate = arena.getArenaState().toString().toLowerCase();
        event.setLine(0,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        event.setLine(1,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        event.setLine(2,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        event.setLine(3,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        if (pli.color_background_wool_of_signs)
        {
            final org.bukkit.material.Sign s_ = (org.bukkit.material.Sign) event.getBlock().getState().getData();
            final Block attachedBlock = event.getBlock().getRelative(s_.getAttachedFace());
            byte data = (byte) 5;
            if (arena.getArenaState() == ArenaState.INGAME)
            {
                data = (byte) 14;
            }
            else if (arena.getArenaState() == ArenaState.RESTARTING)
            {
                data = (byte) 4;
            }
            attachedBlock.setData(data);
        }
    }
    
    public static void updateSpecSign(final JavaPlugin plugin, final Arena arena, final SignChangeEvent event)
    {
        final int count = arena.getAllPlayers().size();
        final int maxcount = arena.getMaxPlayers();
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        event.setLine(0,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs.spec.0").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        event.setLine(1,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs.spec.1").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        event.setLine(2,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs.spec.2").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        event.setLine(3,
                Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs.spec.3").replaceAll("&", "§").replace("<count>", Integer.toString(count))
                        .replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getDisplayName())));
        if (pli.color_background_wool_of_signs)
        {
            final org.bukkit.material.Sign s_ = (org.bukkit.material.Sign) event.getBlock().getState().getData();
            final Block attachedBlock = event.getBlock().getRelative(s_.getAttachedFace());
            byte data = (byte) 5;
            if (arena.getArenaState() == ArenaState.INGAME)
            {
                data = (byte) 14;
            }
            else if (arena.getArenaState() == ArenaState.RESTARTING)
            {
                data = (byte) 4;
            }
            attachedBlock.setData(data);
        }
    }
    
    // used for random and leave sign
    public static void updateSign(final JavaPlugin plugin, final SignChangeEvent event, final String arenastate)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        event.setLine(0, Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".0").replaceAll("&", "§")));
        event.setLine(1, Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".1").replaceAll("&", "§")));
        event.setLine(2, Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".2").replaceAll("&", "§")));
        event.setLine(3, Signs.replaceSquares(pli.getMessagesConfig().getConfig().getString("signs." + arenastate + ".3").replaceAll("&", "§")));
    }
    
    public static ArrayList<Arena> loadArenas(final JavaPlugin plugin, final ArenasConfig cf)
    {
        final ArrayList<Arena> ret = new ArrayList<>();
        final FileConfiguration config = cf.getConfig();
        if (!config.isSet("arenas"))
        {
            return ret;
        }
        for (final String arena : config.getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false))
        {
            if (Validator.isArenaValid(plugin, arena, cf.getConfig()))
            {
                ret.add(Util.initArena(plugin, arena));
            }
        }
        return ret;
    }
    
    public static Arena initArena(final JavaPlugin plugin, final String arena)
    {
        final Arena a = new Arena(plugin, arena);
        final ArenaSetup s = MinigamesAPI.getAPI().getPluginInstance(plugin).arenaSetup;
        a.init(Util.getSignLocationFromArena(plugin, arena), Util.getAllSpawns(plugin, arena), Util.getMainLobby(plugin), Util.getComponentForArena(plugin, arena, "lobby"),
                s.getPlayerCount(plugin, arena, true), s.getPlayerCount(plugin, arena, false), s.getArenaVIP(plugin, arena));
        return a;
    }
    
    public static boolean isNumeric(final String s)
    {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
    
    // example items: 351:6#ALL_DAMAGE:2#KNOCKBACK:2*1=NAME:LORE;267*1;3*64;3*64
    public static ArrayList<ItemStack> parseItems(final String rawitems)
    {
        final ArrayList<ItemStack> ret = new ArrayList<>();
        
        try
        {
            final String[] a = rawitems.split(";");
            
            for (String rawitem : a)
            {
                // crackshot support
                if (rawitem.startsWith("crackshot:"))
                {
                    final String[] guntype = rawitem.split(":");
                    if (guntype.length > 1)
                    {
                        if (guntype[1].length() > 1)
                        {
                            final ItemStack gun = new ItemStack(Material.WOOD_HOE);
                            final ItemMeta gunmeta = gun.getItemMeta();
                            gunmeta.setDisplayName(rawitem);
                            gun.setItemMeta(gunmeta);
                            ret.add(gun);
                        }
                    }
                    continue;
                }
                
                // Potioneffects support
                if (rawitem.startsWith("potioneffect:"))
                {
                    final String[] potioneffecttype = rawitem.split(":");
                    if (potioneffecttype.length > 1)
                    {
                        String str = potioneffecttype[1];
                        if (potioneffecttype.length > 2)
                        {
                            str += ":" + potioneffecttype[2];
                        }
                        if (str.length() > 1)
                        {
                            if (!str.contains(":"))
                            {
                                // duration
                                rawitem += ":99999";
                            }
                            if (!str.contains("#"))
                            {
                                // level
                                rawitem += "#1";
                            }
                            final ItemStack gun = new ItemStack(Material.WOOD_HOE);
                            final ItemMeta gunmeta = gun.getItemMeta();
                            gunmeta.setDisplayName(rawitem);
                            gun.setItemMeta(gunmeta);
                            ret.add(gun);
                        }
                    }
                    continue;
                }
                
                final int nameindex = rawitem.indexOf("=");
                final String[] c = rawitem.split("\\*");
                int optional_armor_color_index = -1;
                String itemid = c[0];
                String itemdata = "0";
                final String[] enchantments_ = itemid.split("#");
                final String[] enchantments = new String[enchantments_.length - 1];
                if (enchantments_.length > 1)
                {
                    for (int i = 1; i < enchantments_.length; i++)
                    {
                        enchantments[i - 1] = enchantments_[i];
                    }
                }
                itemid = enchantments_[0];
                final String[] d = itemid.split(":");
                if (d.length > 1)
                {
                    itemid = d[0];
                    itemdata = d[1];
                }
                String itemamount = "1";
                if (c.length > 1)
                {
                    itemamount = c[1];
                    optional_armor_color_index = c[1].indexOf("#");
                    if (optional_armor_color_index > 0)
                    {
                        itemamount = c[1].substring(0, optional_armor_color_index);
                    }
                }
                if (nameindex > -1)
                {
                    itemamount = c[1].substring(0, c[1].indexOf("="));
                }
                final int itemid_int = Util.isNumeric(itemid) ? Integer.parseInt(itemid) : 0;
                if (itemid_int < 1)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Invalid item id: " + itemid);
                    continue;
                }
                final int itemamount_int = Util.isNumeric(itemamount) ? Integer.parseInt(itemamount) : 1;
                final int itemdata_int = Util.isNumeric(itemdata) ? Integer.parseInt(itemdata) : 0;
                final ItemStack nitem = new ItemStack(itemid_int, itemamount_int, (short) itemdata_int);
                final ItemMeta m = nitem.getItemMeta();
                if (nitem.getType() != Material.ENCHANTED_BOOK)
                {
                    for (final String enchant : enchantments)
                    {
                        final String[] e = enchant.split(":");
                        final String ench = e[0];
                        String lv = "1";
                        if (e.length > 1)
                        {
                            lv = e[1];
                        }
                        if (Enchantment.getByName(ench) != null)
                        {
                            m.addEnchant(Enchantment.getByName(ench), Integer.parseInt(lv), true);
                        }
                    }
                }
                
                if (nameindex > -1)
                {
                    final String namelore = rawitem.substring(nameindex + 1);
                    String name = "";
                    String lore = "";
                    final int i = namelore.indexOf(":");
                    if (i > -1)
                    {
                        name = namelore.substring(0, i);
                        lore = namelore.substring(i + 1);
                    }
                    else
                    {
                        name = namelore;
                    }
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                    m.setLore(Arrays.asList(lore));
                }
                
                // RGB Color support for Armor
                if (optional_armor_color_index > -1)
                {
                    m.setDisplayName(c[1].substring(optional_armor_color_index));
                }
                
                nitem.setItemMeta(m);
                if (nitem.getType() == Material.ENCHANTED_BOOK)
                {
                    try
                    {
                        final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) nitem.getItemMeta();
                        for (final String enchant : enchantments)
                        {
                            final String[] e = enchant.split(":");
                            final String ench = e[0];
                            String lv = "1";
                            if (e.length > 1)
                            {
                                lv = e[1];
                            }
                            if (Enchantment.getByName(ench) != null)
                            {
                                meta.addStoredEnchant(Enchantment.getByName(ench), Integer.parseInt(lv), true);
                            }
                        }
                        nitem.setItemMeta(meta);
                    }
                    catch (final Exception e)
                    {
                        MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed parsing enchanted book. ", e);
                    }
                }
                ret.add(nitem);
            }
            if (ret == null || ret.size() < 1)
            {
                MinigamesAPI.getAPI().getLogger().severe("Found invalid class in config!");
            }
        }
        catch (final Exception e)
        {
            ret.add(new ItemStack(Material.STAINED_GLASS_PANE));
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed to load class items", e);
            final ItemStack rose = new ItemStack(Material.RED_ROSE);
            final ItemMeta im = rose.getItemMeta();
            im.setDisplayName(ChatColor.RED + "Sowwy, failed to load class.");
            rose.setItemMeta(im);
            ret.add(rose);
        }
        return ret;
    }
    
    public static void giveLobbyItems(final JavaPlugin plugin, final Player p)
    {
        ArenaLogger.debug("Giving lobby items to " + p.getName());
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        final ItemStack classes_item = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_CLASS_SELECTION_ITEM));
        if (classes_item.getType() != Material.AIR)
        {
            final ItemMeta cimeta = classes_item.getItemMeta();
            cimeta.setDisplayName(pli.getMessagesConfig().classes_item);
            classes_item.setItemMeta(cimeta);
        }
        
        if (!plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUNGEE_GAME_ON_JOIN))
        {
            final ItemStack exit_item = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_EXIT_ITEM));
            if (exit_item.getType() != Material.AIR)
            {
                final ItemMeta exitimeta = exit_item.getItemMeta();
                exitimeta.setDisplayName(pli.getMessagesConfig().exit_item);
                exit_item.setItemMeta(exitimeta);
            }
            p.getInventory().setItem(8, exit_item);
            p.updateInventory();
        }
        
        final ItemStack achievement_item = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_ACHIEVEMENT_ITEMS));
        if (achievement_item.getType() != Material.AIR)
        {
            final ItemMeta achievement_itemmeta = achievement_item.getItemMeta();
            achievement_itemmeta.setDisplayName(pli.getMessagesConfig().achievement_item);
            achievement_item.setItemMeta(achievement_itemmeta);
        }
        
        final ItemStack shop_item = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_SHOP_SELECTION_ITEM));
        if (shop_item.getType() != Material.AIR)
        {
            final ItemMeta shop_itemmeta = shop_item.getItemMeta();
            shop_itemmeta.setDisplayName(pli.getMessagesConfig().shop_item);
            shop_item.setItemMeta(shop_itemmeta);
        }
        
        if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_CLASSES_ENABLED))
        {
            p.getInventory().addItem(classes_item);
        }
        if (pli.isAchievementGuiEnabled() && pli.getAchievementsConfig().getConfig().getBoolean("config.enabled"))
        {
            p.getInventory().addItem(achievement_item);
        }
        if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SHOP_ENABLED))
        {
            p.getInventory().addItem(shop_item);
        }
        p.updateInventory();
        
        // custom lobby item
        if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_ENABLED_SUFFIX))
        {
            final ItemStack custom_item0 = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_ITEM_SUFFIX));
            if (custom_item0.getType() != Material.AIR)
            {
                final ItemMeta custom_item0meta = custom_item0.getItemMeta();
                custom_item0meta.setDisplayName(plugin.getConfig().getString(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_NAME_SUFFIX));
                custom_item0.setItemMeta(custom_item0meta);
            }
            p.getInventory().addItem(custom_item0);
            p.updateInventory();
        }
    }
    
    public static void giveSpectatorItems(final JavaPlugin plugin, final Player p)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        final ItemStack s_item = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_SPECTATOR_ITEM));
        final ItemMeta s_imeta = s_item.getItemMeta();
        s_imeta.setDisplayName(pli.getMessagesConfig().spectator_item);
        s_item.setItemMeta(s_imeta);
        
        final ItemStack exit_item = new ItemStack(plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_EXIT_ITEM));
        final ItemMeta exitimeta = exit_item.getItemMeta();
        exitimeta.setDisplayName(pli.getMessagesConfig().exit_item);
        exit_item.setItemMeta(exitimeta);
        
        p.getInventory().addItem(s_item);
        p.getInventory().setItem(8, exit_item);
        p.updateInventory();
    }
    
    public static void sendMessage(final Player p, final String arenaname, final String msgraw)
    {
        final String[] msgs = msgraw.replaceAll("<player>", p.getName()).replaceAll("<arena>", arenaname).split(";");
        for (final String msg : msgs)
        {
            p.sendMessage(msgs);
        }
    }
    
    public static void sendMessage(final JavaPlugin plugin, final Player p, final String msgraw)
    {
        if (msgraw.equalsIgnoreCase(""))
        {
            return;
        }
        final String[] msgs = msgraw.replace("<player>", p.getName()).replace("<game>", plugin.getName()).split(";");
        for (final String msg : msgs)
        {
            p.sendMessage(msg);
        }
    }
    
    public static ItemStack getCustomHead(final String name)
    {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        final SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
        skullmeta.setOwner(name);
        item.setItemMeta(skullmeta);
        return item;
    }
    
    public static void spawnPowerup(final JavaPlugin plugin, final Arena a, final Location l, final ItemStack item)
    {
        final World w = l.getWorld();
        final Chicken c = w.spawn(l, Chicken.class);
        c.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
        final Item i = w.dropItem(l, item);
        c.setPassenger(i);
        if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_POWERUP_BROADCAST))
        {
            for (final String p_ : a.getAllPlayers())
            {
                if (Validator.isPlayerOnline(p_))
                {
                    final Player p = Bukkit.getPlayer(p_);
                    p.sendMessage(MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().powerup_spawned);
                }
            }
        }
        if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_POWERUP_FIREWORKS))
        {
            Util.spawnFirework(l);
        }
    }
    
    static Random r = new Random();
    
    public static void spawnFirework(final Player p)
    {
        Util.spawnFirework(p.getLocation());
    }
    
    public static void spawnFirework(final Location l)
    {
        final Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        final FireworkMeta fwm = fw.getFireworkMeta();
        final FireworkEffect effect = FireworkEffect.builder().flicker(Util.r.nextBoolean()).withColor(Color.AQUA).withFade(Color.ORANGE).with(Type.BURST).trail(Util.r.nextBoolean()).build();
        fwm.addEffect(effect);
        final int rp = Util.r.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }
    
    public static Color hexToRgb(final String colorStr)
    {
        return Color.fromRGB(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }
    
    public static class ValueComparator implements Comparator<String>
    {
        Map<String, Double> base;
        
        public ValueComparator(final Map<String, Double> base)
        {
            this.base = base;
        }
        
        @Override
        public int compare(final String a, final String b)
        {
            if (this.base.get(a) >= this.base.get(b))
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }
    
    public static class CompassPlayer
    {
        Player p = null;
        Double d = null;
        
        public CompassPlayer(final Player p, final Double d)
        {
            this.p = p;
            this.d = d;
        }
        
        public Player getPlayer()
        {
            return this.p;
        }
        
        public Double getDistance()
        {
            return this.d;
        }
    }
    
    public static CompassPlayer getNearestPlayer(final Player p, final Arena a)
    {
        CompassPlayer ret = null;
        double distance = 10000;
        for (final String p_ : a.getAllPlayers())
        {
            if (!p_.equalsIgnoreCase(p.getName()) && !MinigamesAPI.getAPI().getPluginInstance(a.getPlugin()).containsGlobalLost(p_))
            {
                if (Validator.isPlayerOnline(p_))
                {
                    final double newdist = Bukkit.getPlayer(p_).getLocation().distance(p.getLocation());
                    if (newdist < distance)
                    {
                        distance = newdist;
                        ret = new CompassPlayer(Bukkit.getPlayer(p_), distance);
                    }
                }
            }
        }
        return ret;
    }
    
    public static void sendStatsMessage(final PluginInstance pli, final Player p)
    {
        if (pli.getMessagesConfig().getConfig().isSet("messages.stats"))
        {
            final int kills_ = pli.getStatsInstance().getKills(p.getName());
            final int deaths_ = pli.getStatsInstance().getDeaths(p.getName());
            int money_ = 0;
            if (MinigamesAPI.getAPI().economyAvailable())
            {
                money_ = (int) MinigamesAPI.econ.getBalance(p.getName());
            }
            
            final String wins = Integer.toString(pli.getStatsInstance().getWins(p.getName()));
            final String loses = Integer.toString(pli.getStatsInstance().getLoses(p.getName()));
            final String kills = Integer.toString(kills_);
            final String deaths = Integer.toString(deaths_);
            final String money = Integer.toString(money_);
            final String points = Integer.toString(pli.getStatsInstance().getPoints(p.getName()));
            final String kdr = Integer.toString(Math.max(kills_, 1) / Math.max(deaths_, 1));
            for (final String key : pli.getMessagesConfig().getConfig().getConfigurationSection("messages.stats").getKeys(false))
            {
                // Each line from the config gets checked for variables like <wins> or <money> and these get replaced by the values calculated above
                final String msg = pli.getMessagesConfig().getConfig().getString("messages.stats." + key).replaceAll("<wins>", wins).replaceAll("<loses>", loses).replaceAll("<alltime_kills>", kills)
                        .replaceAll("<alltime_deaths>", deaths).replaceAll("<points>", points).replaceAll("<kdr>", kdr).replaceAll("<money>", money);
                Util.sendMessage(pli.getPlugin(), p, ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
    
    public static void pushBack(final Location l, final Player p)
    {
        final Vector direction = l.toVector().subtract(p.getLocation().toVector()).normalize();
        p.setVelocity(direction);
        if (p.isInsideVehicle())
        {
            p.getVehicle().setVelocity(direction.multiply(2.2D));
        }
        if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_9))
        {
            p.spawnParticle(Particle.PORTAL, p.getLocation(), 10);
        }
    }
    
    public static Score getScore(final Objective obj, final String text)
    {
        Score s = null;
        Method getScore_ = null;
        try
        {
            if (MinigamesAPI.SERVER_VERSION.isBelow(MinecraftVersionsType.V1_7_R4))
            {
                getScore_ = obj.getClass().getDeclaredMethod("getScore", OfflinePlayer.class);
                getScore_.setAccessible(true);
                s = (Score) getScore_.invoke(obj, Bukkit.getOfflinePlayer(text));
            }
            else
            {
                getScore_ = obj.getClass().getDeclaredMethod("getScore", String.class);
                getScore_.setAccessible(true);
                s = (Score) getScore_.invoke(obj, text);
            }
        }
        catch (final Exception e)
        {
            if (MinigamesAPI.debug)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
        return s;
    }
    
    public static void resetScores(final Scoreboard obj, final String text)
    {
        Method resetScores_ = null;
        try
        {
            if (MinigamesAPI.SERVER_VERSION.isBelow(MinecraftVersionsType.V1_7_R4))
            {
                resetScores_ = obj.getClass().getDeclaredMethod("resetScores", OfflinePlayer.class);
                resetScores_.setAccessible(true);
                resetScores_.invoke(obj, Bukkit.getOfflinePlayer(text));
            }
            else
            {
                resetScores_ = obj.getClass().getDeclaredMethod("resetScores", String.class);
                resetScores_.setAccessible(true);
                resetScores_.invoke(obj, text);
            }
        }
        catch (final Exception e)
        {
            if (MinigamesAPI.debug)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
    }
    
    public static void saveInventory(final Player player)
    {
        Util.armourContents.put(player.getName(), player.getInventory().getArmorContents());
        Util.inventoryContents.put(player.getName(), player.getInventory().getContents());
        Util.locations.put(player.getName(), player.getLocation());
        Util.xplevel.put(player.getName(), player.getLevel());
        Util.gamemode.put(player.getName(), player.getGameMode());
        player.getInventory().clear();
    }
    
    public static void restoreInventory(final Player player)
    {
        player.getInventory().clear();
        player.teleport(Util.locations.get(player.getName()));
        
        player.getInventory().setContents(Util.inventoryContents.get(player.getName()));
        player.getInventory().setArmorContents(Util.armourContents.get(player.getName()));
        player.setLevel(Util.xplevel.get(player.getName()));
        player.setGameMode(Util.gamemode.get(player.getName()));
        
        Util.xplevel.remove(player.getName());
        Util.locations.remove(player.getName());
        Util.armourContents.remove(player.getName());
        Util.inventoryContents.remove(player.getName());
    }

    /**
     * @param boundaries
     */
    public static void clearDrops(Cuboid boundaries)
    {
        clearEntites(boundaries, e -> e instanceof Item);
    }

    /**
     * @param boundaries
     */
    public static void clearEntites(Cuboid boundaries, Predicate<Entity> predicate)
    {
        if (boundaries != null && boundaries.getLowLoc() != null && boundaries.getHighLoc() != null)
        {
            // iterate through nearby entities
            final Chunk lowChunk = boundaries.getLowLoc().getChunk();
            final Chunk highChunk = boundaries.getHighLoc().getChunk();
            final World world = boundaries.getWorld();
            for (int x = lowChunk.getX(); x <= highChunk.getX(); x++)
            {
                for (int z = lowChunk.getZ(); z <= highChunk.getZ(); z++)
                {
                    final Chunk chunk = world.getChunkAt(x, z);
                    final Entity[] entities = chunk.getEntities();
                    if (entities != null)
                    {
                        for (final Entity entity : entities)
                        {
                            if (predicate.test(entity) && boundaries.containsLoc(entity.getLocation()))
                            {
                                entity.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}
