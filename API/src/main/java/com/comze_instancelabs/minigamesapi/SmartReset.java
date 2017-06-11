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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.logging.Level;

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

import com.comze_instancelabs.minigamesapi.util.SmartArenaBlock;
import com.comze_instancelabs.minigamesapi.util.Util;

/**
 * The smart reset for resetting an arena to original state.
 * 
 * @author instancelabs
 */
public class SmartReset implements Runnable
{
    
    /** the changed blocks. */
    private final SmartBlockMap              changed      = new SmartBlockMap();
    
    /** the underlying arena. */
    private Arena                            a;
    
    /** the blocks that failed while resetting. */
    private final ArrayList<SmartArenaBlock> failedblocks = new ArrayList<>();
    
    /** time for reset progress. */
    private long                             time         = 0L;
    
    /**
     * Constructor.
     * 
     * @param a
     *            arena owner of this smart reset.
     */
    public SmartReset(final Arena a)
    {
        this.a = a;
    }
    
    /**
     * Adds changed block.
     * 
     * @param b
     *            block to be added
     * @return the smart arena block or {@code null} if the block already was added before
     */
    public SmartArenaBlock addChanged(final Block b)
    {
        if (!this.changed.hasBlock(b.getLocation()))
        {
            if (MinigamesAPI.debug)
            {
                MinigamesAPI.getAPI().getLogger().info("(1) adding changed block for location " + b.getLocation());
            }
            final SmartArenaBlock sablock = new SmartArenaBlock(b, b.getType() == Material.CHEST, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
            this.changed.putBlock(b.getLocation(), sablock);
            return sablock;
        }
        return null;
    }

    /**
     * Adds changed blocks
     * @param loc
     */
    public void addChanged(Block[] loc)
    {
        if (loc != null)
        {
            for (final Block b : loc)
            {
                this.addChanged(b);
            }
        }
    }

    /**
     * Adds changed block.
     * 
     * @param b
     *            block to be added
     * @param blockReplacedState
     *            state of the block
     * @return the smart arena block or {@code null} if the block already was added before
     */
    public SmartArenaBlock addChanged(Block b, BlockState blockReplacedState)
    {
        return this.addChanged(b.getLocation(), blockReplacedState.getType(), blockReplacedState.getData().getData());
    }
    
    /**
     * Adds changed block
     * 
     * @param l
     *            location of the block.
     * @param m
     *            original material.
     * @param data
     *            original data value.
     * @return the smart arena block or {@code null} if the block already was added before
     */
    public SmartArenaBlock addChanged(final Location l, final Material m, final byte data)
    {
        if (!this.changed.hasBlock(l))
        {
            if (MinigamesAPI.debug)
            {
                MinigamesAPI.getAPI().getLogger().info("(5) adding changed block for location " + l);
            }
            final SmartArenaBlock sab = new SmartArenaBlock(l, m, data);
            if (m == Material.CHEST)
            {
                sab.setInventory(((Chest)l.getBlock().getState()).getInventory());
            }
            this.changed.putBlock(l, sab);
            return sab;
        }
        return null;
    }
    
    @Override
    public void run()
    {
        int rolledBack = 0;
        
        // Rollback 70 blocks at a time
        final Iterator<SmartArenaBlock> it = this.changed.getBlocks().iterator();
        while (it.hasNext() && rolledBack <= 70)
        {
            final SmartArenaBlock ablock = it.next();
            
            try
            {
                if (MinigamesAPI.debug) MinigamesAPI.getAPI().getLogger().info("resetting block " + ablock.getBlock().getLocation());
                this.resetSmartResetBlock(ablock);
                it.remove();
            }
            catch (final Exception e)
            {
                if (MinigamesAPI.debug)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.INFO, "failed block " + ablock.getBlock().getLocation(), e);
                }
                this.failedblocks.add(ablock);
            }
            
            rolledBack++;
        }
        
        if (it.hasNext())
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
                MinigamesAPI.getAPI().getLogger().info("retrying failed block " + ablock.getBlock().getLocation());
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
        for (final SmartArenaBlock ablock : this.changed.getBlocks())
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
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back furnace inventory");
            ((Furnace) b_.getState()).getInventory().clear();
            ((Furnace) b_.getState()).update();
        }
        if (b_.getType() == Material.CHEST)
        {
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back chest inventory");
            ((Chest) b_.getState()).getBlockInventory().clear();
            ((Chest) b_.getState()).update();
        }
        if (b_.getType() == Material.DISPENSER)
        {
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back dispenser inventory");
            ((Dispenser) b_.getState()).getInventory().clear();
            ((Dispenser) b_.getState()).update();
        }
        if (b_.getType() == Material.DROPPER)
        {
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back dropper inventory");
            ((Dropper) b_.getState()).getInventory().clear();
            ((Dropper) b_.getState()).update();
        }
        if (b_.getType() == Material.BREWING_STAND)
        {
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back brewing stand inventory");
            ((BrewingStand) b_.getState()).getInventory().clear();
            ((BrewingStand) b_.getState()).update();
        }
        if (!b_.getType().equals(ablock.getMaterial()) || b_.getData() != ablock.getData())
        {
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back block material/data to " + ablock.getMaterial() + "/" + ablock.getData());
            b_.setType(ablock.getMaterial());
            b_.setData(ablock.getData());
        }
        else if (MinigamesAPI.debug)
        {
            MinigamesAPI.getAPI().getLogger().info("Skipping block rollback from " + b_.getType() + "/" + b_.getData() + " to " + ablock.getMaterial() + "/" + ablock.getData());
        }
        if (b_.getType() == Material.CHEST)
        {
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back chest");
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
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back dispenser");
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
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back dropper");
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
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back sign");
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
            if (MinigamesAPI.debug)
                MinigamesAPI.getAPI().getLogger().info("Rolling back skull");
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
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        
        for (final SmartArenaBlock bl : this.changed.getBlocks())
        {
            try
            {
                oos.writeObject(bl);
            }
            catch (final IOException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "io exception", e);
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
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
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
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Something is wrong with your SmartReset file and the reset might not be successful.", e);
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
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        catch (final ClassNotFoundException e)
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
        
        if (f.exists())
        {
            f.delete();
        }
    }
    
    /**
     * A map holding smart reset blocks per Y coordinate (level).
     */
    private static final class SmartBlockMap extends TreeMap<Integer, Map<Location, SmartArenaBlock>>
    {
        
        /**
         * serial version uid.
         */
        private static final long serialVersionUID = 8336943154139693760L;

        /**
         * Puts a block into map.
         * 
         * @param l
         *            location
         * @param block
         *            smart reset block
         */
        public void putBlock(Location l, SmartArenaBlock block)
        {
            this.computeIfAbsent(l.getBlockY(), (key) -> new HashMap<>()).put(l, block);
        }
        
        /**
         * Checks if given location is already present within map.
         * 
         * @param l
         *            location to check
         * @return {@code true} if location is known
         */
        public boolean hasBlock(Location l)
        {
            final Map<Location, SmartArenaBlock> map = this.get(l.getBlockY());
            if (map != null)
            {
                return map.containsKey(l);
            }
            return false;
        }
        
        /**
         * Returns an iterable over all blocks within this map.
         * 
         * @return iterable over all blocks.
         */
        public Iterable<SmartArenaBlock> getBlocks()
        {
            return new Iterable<SmartArenaBlock>() {
                
                @Override
                public Iterator<SmartArenaBlock> iterator()
                {
                    return new NestedIterator<>(SmartBlockMap.this.values().iterator());
                }
            };
        }
        
    }
    
    /**
     * Helper class for nesting iterators.
     * @author mepeisen
     *
     * @param <K>
     * @param <T>
     */
    public static final class NestedIterator<K, T> implements Iterator<T>
    {
        
        /** outer iterator. */
        private Iterator<Map<K, T>> outer = null;
        
        /** inner iterator. */
        private Iterator<T> inner = null;
        
        /** prev iterator for remove. */
        private Iterator<T> prev = null;
        
        /**
         * Constructor.
         * @param iter
         */
        public NestedIterator(Iterator<Map<K, T>> iter)
        {
            this.outer = iter;
            moveNext();
        }

        @Override
        public boolean hasNext()
        {
            // inner iterator existing and has an element?
            if (this.inner != null)
            {
                return this.inner.hasNext();
            }
            
            // no more elements
            return false;
        }

        @Override
        public T next()
        {
            if (this.inner == null)
            {
                throw new NoSuchElementException();
            }
            final T result = this.inner.next();
            this.prev = this.inner;
            if (!this.inner.hasNext())
            {
                this.inner = null;
                moveNext();
            }
            return result;
        }

        /**
         * Moves to next element
         */
        private void moveNext()
        {
            while (this.inner == null)
            {
                if (!this.outer.hasNext())
                {
                    // no elements found
                    break;
                }
                this.inner = this.outer.next().values().iterator();
                if (!this.inner.hasNext())
                {
                    this.inner = null;
                }
            }
        }

        @Override
        public void remove()
        {
            if (this.prev == null)
            {
                throw new IllegalStateException("no next called"); //$NON-NLS-1$
            }
            this.prev.remove();
        }

        
    }
    
}
