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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;

import com.comze_instancelabs.minigamesapi.util.ArenaBlock;
import com.comze_instancelabs.minigamesapi.util.Util;

/**
 * Helper class that is invisible in public API
 * 
 * <p>
 * NOTE: THIS IS INTERNAL API. It can change from one build to another. Do never use this Util directly. 
 * </p>
 * 
 * @author mepeisen
 */
public class PrivateUtil
{

    public static void loadArenaFromFileSYNC(final JavaPlugin plugin, final Arena arena)
    {
        int failcount = 0;
        final ArrayList<ArenaBlock> failedblocks = new ArrayList<>();
        
        final File f = new File(plugin.getDataFolder() + "/" + arena.getInternalName());
        if (!f.exists())
        {
            plugin.getLogger().warning("Could not find arena file for " + arena.getInternalName());
            arena.setArenaState(ArenaState.JOIN);
            Bukkit.getScheduler().runTask(plugin, () -> Util.updateSign(plugin, arena));
            return;
        }
        FileInputStream fis = null;
        BukkitObjectInputStream ois = null;
        try
        {
            fis = new FileInputStream(f);
            ois = new BukkitObjectInputStream(fis);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        
        try
        {
            while (true)
            {
                Object b = null;
                try
                {
                    b = ois.readObject();
                }
                catch (final EOFException e)
                {
                    MinigamesAPI.getAPI().getLogger().info("Finished restoring map for " + arena.getInternalName() + " with old reset method.");
                    
                    arena.setArenaState(ArenaState.JOIN);
                    Bukkit.getScheduler().runTask(plugin, () -> Util.updateSign(plugin, arena));
                }
                catch (final ClosedChannelException e)
                {
                    System.out.println("Something is wrong with your arena file and the reset might not be successful. Also, you're using an outdated reset method.");
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                    arena.setArenaState(ArenaState.JOIN);
                    Bukkit.getScheduler().runTask(plugin, () -> Util.updateSign(plugin, arena));
                }
                
                if (b != null)
                {
                    final ArenaBlock ablock = (ArenaBlock) b;
                    try
                    {
                        final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
                        if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString()))
                        {
                            b_.setType(ablock.getMaterial());
                            b_.setData(ablock.getData());
                        }
                        if (b_.getType() == Material.CHEST)
                        {
                            ((Chest) b_.getState()).getInventory().setContents(ablock.getInventory());
                            ((Chest) b_.getState()).update();
                        }
                    }
                    catch (final IllegalStateException e)
                    {
                        failcount += 1;
                        failedblocks.add(ablock);
                    }
                }
                else
                {
                    break;
                }
            }
            
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            ois.close();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MinigamesAPI.getAPI(), () -> {
            for (final ArenaBlock ablock : failedblocks)
            {
                final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
                if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString()))
                {
                    b_.setType(ablock.getMaterial());
                    b_.setData(ablock.getData());
                }
                if (b_.getType() == Material.CHEST)
                {
                    ((Chest) b_.getState()).getInventory().setContents(ablock.getInventory());
                    ((Chest) b_.getState()).update();
                }
            }
        }, 40L);
        MinigamesAPI.getAPI().getLogger().info("Successfully finished!");
        
        return;
    }
    
}
