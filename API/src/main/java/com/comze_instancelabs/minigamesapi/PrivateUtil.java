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
import java.util.logging.Level;

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

    /**
     * Old arena regeneration, developed by instancelabs
     * @param plugin
     * @param arena
     * @deprecated will be removed in 1.4.10
     */
    @Deprecated
    public static void loadArenaFromFileSYNC(final JavaPlugin plugin, final Arena arena)
    {
        @SuppressWarnings("unused")
        int failcount = 0;
        final ArrayList<ArenaBlock> failedblocks = new ArrayList<>();
        
        final File f = new File(plugin.getDataFolder() + "/" + arena.getInternalName()); //$NON-NLS-1$
        if (!f.exists())
        {
            plugin.getLogger().warning("Could not find arena file for " + arena.getInternalName()); //$NON-NLS-1$
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
        catch (final IOException ex)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Something is wrong with your arena file and the reset might not be successful. Also, you're using an outdated reset method.", ex); //$NON-NLS-1$
            return;
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
                catch (@SuppressWarnings("unused") final EOFException ex)
                {
                    MinigamesAPI.getAPI().getLogger().info("Finished restoring map for " + arena.getInternalName() + " with old reset method.");  //$NON-NLS-1$//$NON-NLS-2$
                    
                    arena.setArenaState(ArenaState.JOIN);
                    Bukkit.getScheduler().runTask(plugin, () -> Util.updateSign(plugin, arena));
                }
                catch (final ClosedChannelException ex)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Something is wrong with your arena file and the reset might not be successful. Also, you're using an outdated reset method.", ex); //$NON-NLS-1$
                }
                catch (final Exception e)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
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
                    catch (@SuppressWarnings("unused") final IllegalStateException ex)
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
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        
        try
        {
            ois.close();
        }
        catch (final IOException e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
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
        MinigamesAPI.getAPI().getLogger().info("Successfully finished!"); //$NON-NLS-1$
    }
    
}
