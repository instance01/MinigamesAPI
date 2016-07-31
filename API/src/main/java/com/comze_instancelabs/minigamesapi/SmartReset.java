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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.comze_instancelabs.minigamesapi.util.ChangeCause;
import com.comze_instancelabs.minigamesapi.util.SmartArenaBlock;
import com.comze_instancelabs.minigamesapi.util.Util;

/**
 * The smart reset for resetting an arena to original state.
 * 
 * @author instancelabs
 */
public class SmartReset implements Runnable
{
    
    // will only reset broken/placed blocks
    
    HashMap<Location, SmartArenaBlock>       changed      = new HashMap<>();
    
    Arena                                    a;
    private final ArrayList<SmartArenaBlock> failedblocks = new ArrayList<>();
    long                                     time         = 0L;
    
    public SmartReset(final Arena a)
    {
        this.a = a;
    }
    
    public SmartArenaBlock addChanged(final Block b)
    {
        if (!this.changed.containsKey(b.getLocation()))
        {
            if (MinigamesAPI.debug)
            {
                System.out.println("(1) adding changed block for location " + b.getLocation());
            }
            final SmartArenaBlock sablock = new SmartArenaBlock(b, b.getType() == Material.CHEST, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
            this.changed.put(b.getLocation(), sablock);
            return sablock;
        }
        return null;
    }

    /**
     * @param b
     * @param blockReplacedState
     */
    public SmartArenaBlock addChanged(Block b, BlockState blockReplacedState)
    {
        if (!this.changed.containsKey(b.getLocation()))
        {
            if (MinigamesAPI.debug)
            {
                System.out.println("(1.1) adding changed block for location " + b.getLocation());
            }
            final SmartArenaBlock sablock = new SmartArenaBlock(blockReplacedState, b.getType() == Material.CHEST, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
            this.changed.put(b.getLocation(), sablock);
            return sablock;
        }
        return null;
    }
    
    public SmartArenaBlock addChanged(final Block b, final boolean isChest)
    {
        if (!this.changed.containsKey(b.getLocation()))
        {
            if (MinigamesAPI.debug)
            {
                System.out.println("(2) adding changed block for location " + b.getLocation());
            }
            final SmartArenaBlock sablock = new SmartArenaBlock(b, isChest, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
            this.changed.put(b.getLocation(), sablock);
            return sablock;
        }
        return null;
    }
    
    public SmartArenaBlock addChanged(final Block b, final boolean isChest, final ChangeCause cause)
    {
        if (!this.changed.containsKey(b.getLocation()))
        {
            if (MinigamesAPI.debug)
            {
                System.out.println("(3) adding changed block for location " + b.getLocation());
            }
            final SmartArenaBlock sablock = new SmartArenaBlock(b, isChest, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
            this.changed.put(b.getLocation(), sablock);
            return sablock;
        }
        return null;
    }
    
    @Deprecated
    public void addChanged(final Location l)
    {
        if (!this.changed.containsKey(l))
        {
            if (MinigamesAPI.debug)
            {
                System.out.println("(4) adding changed block for location " + l);
            }
            this.changed.put(l, new SmartArenaBlock(l, Material.AIR, (byte) 0));
        }
    }
    
    public void addChanged(final Location l, final Material m, final byte data)
    {
        if (!this.changed.containsKey(l))
        {
            if (MinigamesAPI.debug)
            {
                System.out.println("(5) adding changed block for location " + l);
            }
            this.changed.put(l, new SmartArenaBlock(l, m, data));
        }
    }
    
    @Override
    public void run()
    {
        int rolledBack = 0;
        
        // Rollback 70 blocks at a time
        final Iterator<Entry<Location, SmartArenaBlock>> it = this.changed.entrySet().iterator();
        while (it.hasNext() && rolledBack <= 70)
        {
            final SmartArenaBlock ablock = it.next().getValue();
            
            try
            {
                System.out.println("resetting block " + ablock.getBlock().getLocation());
                this.resetSmartResetBlock(ablock);
                it.remove();
            }
            catch (final Exception e)
            {
                if (MinigamesAPI.debug)
                {
                    System.out.println("failed block " + ablock.getBlock().getLocation());
                    e.printStackTrace();
                }
                this.failedblocks.add(ablock);
            }
            
            rolledBack++;
        }
        
        if (this.changed.size() != 0)
        {
            Bukkit.getScheduler().runTaskLater(this.a.getPlugin(), this, 2L);
            return;
        }
        
        this.a.setArenaState(ArenaState.JOIN);
        Util.updateSign(this.a.getPlugin(), this.a);
        
        ArenaLogger.debug(this.failedblocks.size() + " to redo.");
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MinigamesAPI.getAPI(), () -> {
            SmartReset.this.changed.clear();
            for (final SmartArenaBlock ablock : SmartReset.this.failedblocks)
            {
                System.out.println("retrying failed block " + ablock.getBlock().getLocation());
                final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
                if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString()))
                {
                    b_.setType(ablock.getMaterial());
                    b_.setData(ablock.getData());
                }
                if (b_.getType() == Material.CHEST)
                {
                    b_.setType(ablock.getMaterial());
                    b_.setData(ablock.getData());
                    ((Chest) b_.getState()).getInventory().setContents(ablock.getInventory());
                    ((Chest) b_.getState()).update();
                }
            }
        }, 25L);
        
        ArenaLogger.debug("Reset time: " + (System.currentTimeMillis() - this.time) + "ms");
    }
    
    /**
     * Resets all changed blocks in tasks each 70 blocks
     */
    public void reset()
    {
        this.time = System.currentTimeMillis();
        this.a.getPlugin().getLogger().info(this.changed.size() + " to reset for arena " + this.a.getInternalName() + ".");
        Bukkit.getScheduler().runTask(this.a.getPlugin(), this);
    }
    
    /**
     * Resets the raw changed blocks on the main thread
     */
    public void resetRaw()
    {
        for (final SmartArenaBlock ablock : this.changed.values())
        {
            try
            {
                this.resetSmartResetBlock(ablock);
            }
            catch (final Exception e)
            {
                this.a.setArenaState(ArenaState.JOIN);
                Util.updateSign(this.a.getPlugin(), this.a);
            }
        }
        
        this.changed.clear();
        this.a.setArenaState(ArenaState.JOIN);
        Util.updateSign(this.a.getPlugin(), this.a);
    }
    
    public void resetSmartResetBlock(final SmartArenaBlock ablock)
    {
        final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
        if (b_.getType() == Material.FURNACE)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back furnace inventory");
            ((Furnace) b_.getState()).getInventory().clear();
            ((Furnace) b_.getState()).update();
        }
        if (b_.getType() == Material.CHEST)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back chest inventory");
            ((Chest) b_.getState()).getBlockInventory().clear();
            ((Chest) b_.getState()).update();
        }
        if (b_.getType() == Material.DISPENSER)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back dispenser inventory");
            ((Dispenser) b_.getState()).getInventory().clear();
            ((Dispenser) b_.getState()).update();
        }
        if (b_.getType() == Material.DROPPER)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back dropper inventory");
            ((Dropper) b_.getState()).getInventory().clear();
            ((Dropper) b_.getState()).update();
        }
        if (b_.getType() == Material.BREWING_STAND)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back brewing stand inventory");
            ((BrewingStand) b_.getState()).getInventory().clear();
            ((BrewingStand) b_.getState()).update();
        }
        if (!b_.getType().equals(ablock.getMaterial()) || b_.getData() != ablock.getData())
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back block material/data to " + ablock.getMaterial() + "/" + ablock.getData());
            b_.setType(ablock.getMaterial());
            b_.setData(ablock.getData());
        }
        else if (MinigamesAPI.debug)
        {
            System.out.println("Skipping block rollback from " + b_.getType() + "/" + b_.getData() + " to " + ablock.getMaterial() + "/" + ablock.getData());
        }
        if (b_.getType() == Material.CHEST)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back chest");
            if (ablock.isDoubleChest())
            {
                final DoubleChest dc = ablock.getDoubleChest();
                final HashMap<Integer, ItemStack> chestinv = ablock.getNewInventory();
                for (final Integer i : chestinv.keySet())
                {
                    final ItemStack item = chestinv.get(i);
                    if (item != null)
                    {
                        dc.getInventory().setItem(i, item);
                    }
                }
                ((Chest) b_.getState()).update();
                return;
            }
            ((Chest) b_.getState()).getBlockInventory().clear();
            ((Chest) b_.getState()).update();
            final HashMap<Integer, ItemStack> chestinv = ablock.getNewInventory();
            for (final Integer i : chestinv.keySet())
            {
                final ItemStack item = chestinv.get(i);
                if (item != null)
                {
                    if (i < 27)
                    {
                        ((Chest) b_.getState()).getBlockInventory().setItem(i, item);
                    }
                }
            }
            ((Chest) b_.getState()).update();
        }
        if (b_.getType() == Material.DISPENSER)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back dispenser");
            final Dispenser d = (Dispenser) b_.getState();
            d.getInventory().clear();
            final HashMap<Integer, ItemStack> chestinv = ablock.getNewInventory();
            for (final Integer i : chestinv.keySet())
            {
                final ItemStack item = chestinv.get(i);
                if (item != null)
                {
                    if (i < 9)
                    {
                        d.getInventory().setItem(i, item);
                    }
                }
            }
            d.getInventory().setContents(ablock.getInventory());
            d.update();
        }
        if (b_.getType() == Material.DROPPER)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back dropper");
            final Dropper d = (Dropper) b_.getState();
            d.getInventory().clear();
            final HashMap<Integer, ItemStack> chestinv = ablock.getNewInventory();
            for (final Integer i : chestinv.keySet())
            {
                final ItemStack item = chestinv.get(i);
                if (item != null)
                {
                    if (i < 9)
                    {
                        d.getInventory().setItem(i, item);
                    }
                }
            }
            d.update();
        }
        if (b_.getType() == Material.WALL_SIGN || b_.getType() == Material.SIGN_POST)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back sign");
            final Sign sign = (Sign) b_.getState();
            if (sign != null)
            {
                int i = 0;
                for (final String line : ablock.getSignLines())
                {
                    sign.setLine(i, line);
                    i++;
                    if (i > 3)
                    {
                        break;
                    }
                }
                sign.update();
            }
        }
        if (b_.getType() == Material.SKULL)
        {
            if (MinigamesAPI.debug) System.out.println("Rolling back skull");
            b_.setData((byte) 0x1);
            b_.getState().setType(Material.SKULL);
            if (b_.getState() instanceof Skull)
            {
                final Skull s = (Skull) b_.getState();
                s.setSkullType(SkullType.PLAYER);
                s.setOwner(ablock.getSkullOwner());
                s.setRotation(ablock.getSkullORotation());
                s.update();
            }
        }
    }
    
    public void saveSmartBlocksToFile()
    {
        final File f = new File(this.a.getPlugin().getDataFolder() + "/" + this.a.getInternalName() + "_smart");
        
        FileOutputStream fos;
        ObjectOutputStream oos = null;
        try
        {
            fos = new FileOutputStream(f);
            oos = new BukkitObjectOutputStream(fos);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        
        for (final SmartArenaBlock bl : this.changed.values())
        {
            try
            {
                oos.writeObject(bl);
            }
            catch (final IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
        
        try
        {
            oos.close();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        
        MinigamesAPI.getAPI().getLogger().info("Saved SmartBlocks of " + this.a.getInternalName());
    }
    
    public void loadSmartBlocksFromFile()
    {
        final File f = new File(this.a.getPlugin().getDataFolder() + "/" + this.a.getInternalName() + "_smart");
        if (!f.exists())
        {
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
                    MinigamesAPI.getAPI().getLogger().info("Finished restoring SmartReset blocks for " + this.a.getInternalName() + ".");
                }
                catch (final ClosedChannelException e)
                {
                    System.out.println("Something is wrong with your SmartReset file and the reset might not be successful.");
                }
                
                if (b != null)
                {
                    final SmartArenaBlock ablock = (SmartArenaBlock) b;
                    this.resetSmartResetBlock(ablock);
                }
                else
                {
                    break;
                }
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        catch (final ClassNotFoundException e)
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
        
        if (f.exists())
        {
            f.delete();
        }
    }
    
}
