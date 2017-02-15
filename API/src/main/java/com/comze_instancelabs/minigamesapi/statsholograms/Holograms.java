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
package com.comze_instancelabs.minigamesapi.statsholograms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.HologramsConfig;

public class Holograms
{
    
    PluginInstance              pli;
    HashMap<Location, Hologram> holo = new HashMap<>();
    
    public Holograms(final PluginInstance pli)
    {
        this.pli = pli;
        this.loadHolograms();
    }
    
    public void loadHolograms()
    {
        final HologramsConfig config = this.pli.getHologramsConfig();
        if (config.getConfig().isSet("holograms."))
        {
            for (final String str : config.getConfig().getConfigurationSection("holograms.").getKeys(false))
            {
                final String base = "holograms." + str;
                try
                {
                    final Location l = new Location(Bukkit.getWorld(config.getConfig().getString(base + ".world")), config.getConfig().getDouble(base + ".location.x"),
                            config.getConfig().getDouble(base + ".location.y"), config.getConfig().getDouble(base + ".location.z"), (float) config.getConfig().getDouble(base + ".location.yaw"),
                            (float) config.getConfig().getDouble(base + ".location.pitch"));
                    if (l != null && l.getWorld() != null)
                    {
                        this.holo.put(l, new Hologram(this.pli, l));
                    }
                }
                catch (final Exception e)
                {
                    ArenaLogger.debug("Failed loading hologram as invalid location was found: " + e.getMessage());
                }
            }
        }
    }
    
    public void sendAllHolograms(final Player p)
    {
        for (final Hologram h : this.holo.values())
        {
            h.send(p);
        }
    }
    
    public void addHologram(final Location l)
    {
        final String base = "holograms." + Integer.toString((int) Math.round(Math.random() * 10000));
        final HologramsConfig config = this.pli.getHologramsConfig();
        config.getConfig().set(base + ".world", l.getWorld().getName());
        config.getConfig().set(base + ".location.x", l.getX());
        config.getConfig().set(base + ".location.y", l.getY());
        config.getConfig().set(base + ".location.z", l.getZ());
        config.getConfig().set(base + ".location.yaw", l.getYaw());
        config.getConfig().set(base + ".location.pitch", l.getPitch());
        config.saveConfig();
        final Hologram h = new Hologram(this.pli, l);
        this.holo.put(l, h);
    }
    
    public boolean removeHologram(final Location ploc)
    {
        final HologramsConfig config = this.pli.getHologramsConfig();
        if (config.getConfig().isSet("holograms."))
        {
            for (final String str : config.getConfig().getConfigurationSection("holograms.").getKeys(false))
            {
                final String base = "holograms." + str;
                final Location l = new Location(Bukkit.getWorld(config.getConfig().getString(base + ".world")), config.getConfig().getDouble(base + ".location.x"),
                        config.getConfig().getDouble(base + ".location.y"), config.getConfig().getDouble(base + ".location.z"), (float) config.getConfig().getDouble(base + ".location.yaw"),
                        (float) config.getConfig().getDouble(base + ".location.pitch"));
                if (l.distance(ploc) <= 2)
                {
                    config.getConfig().set("holograms." + str, null);
                    config.saveConfig();
                    if (this.holo.containsKey(l))
                    {
                        for (final Player p : Bukkit.getOnlinePlayers())
                        {
                            this.destroyHologram(p, this.holo.get(l));
                        }
                        this.holo.remove(l);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public void destroyHologram(final Player p, final Hologram h)
    {
        final String version = MinigamesAPI.getAPI().internalServerVersion;
        try
        {
            // TODO Do not use reflection :-(
            final Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
            final Field playerConnection = Class.forName("net.minecraft.server." + version + ".EntityPlayer").getField("playerConnection");
            playerConnection.setAccessible(true);
            final Method sendPacket = playerConnection.getType().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
            
            final Constructor<?> packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + version + ".PacketPlayOutEntityDestroy").getConstructor(int[].class);
            
            final Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance(this.convertIntegers(h.getIds()));
            sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(p)), destroyPacket);
            
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
    }
    
    public int[] convertIntegers(final ArrayList<Integer> integers)
    {
        final int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}
