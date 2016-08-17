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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class SmartArenaBlock implements Serializable
{
    private static final long                    serialVersionUID = -1894759842709524780L;
    
    private final int                            x, y, z;
    private final String                         world;
    private final Material                       m;
    private byte                                 data;
    private ArrayList<Material>                  item_mats;
    private ArrayList<Byte>                      item_data;
    private ArrayList<Integer>                   item_amounts;
    private ArrayList<String>                    item_displaynames;
    private HashMap<Integer, ArrayList<Integer>> item_enchid;
    private HashMap<Integer, ArrayList<Integer>> item_enchid_lv;
    private HashMap<Integer, ArrayList<Integer>> enchbook_id;
    private HashMap<Integer, ArrayList<Integer>> enchbook_id_lv;
    private ArrayList<Short>                     item_durability;
    private ArrayList<Integer>                   item_pos;
    
    // Sign lines
    private final ArrayList<String>              sign_lines       = new ArrayList<>();
    
    // Sign lines
    private String                               skull_owner      = "";
    private BlockFace                            skull_rotation   = BlockFace.SELF;
    
    // optional stuff
    private ArrayList<Boolean>                   item_splash;
    
    private ItemStack[]                          inv;
    
    boolean                                      isDoubleChest    = false;
    DoubleChest                                  doubleChest      = null;
    
    public SmartArenaBlock(final Block b, final boolean isChest, final boolean isSign)
    {
        this.m = b.getType();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
        this.data = b.getData();
        this.world = b.getWorld().getName();
        if (this.m.equals(Material.SKULL))
        {
            if (b.getState() instanceof Skull)
            {
                this.skull_owner = ((Skull) b.getState()).getOwner();
                this.skull_rotation = ((Skull) b.getState()).getRotation();
            }
        }
        if (this.m.equals(Material.DROPPER))
        {
            if (b.getState() instanceof Dropper)
            {
                this.setInventory(((Dropper) b.getState()).getInventory());
            }
        }
        if (this.m.equals(Material.DISPENSER))
        {
            if (b.getState() instanceof Dispenser)
            {
                this.setInventory(((Dispenser) b.getState()).getInventory());
            }
        }
        if (isSign)
        {
            final Sign sign = (Sign) b.getState();
            if (sign != null)
            {
                this.sign_lines.addAll(Arrays.asList(sign.getLines()));
            }
        }
        else if (isChest)
        {
            final Chest chest = (Chest) b.getState();
            this.setInventory(chest.getInventory());
        }
        if (MinigamesAPI.debug)
        {
            MinigamesAPI.getAPI().getLogger().fine("Added smart arena block @ " + this.x + "/" + this.y + "/" + this.z + " with material " + this.m);
        }
    }
    
    public SmartArenaBlock(final BlockState b, final boolean isChest, final boolean isSign)
    {
        this.m = b.getType();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
        this.data = b.getData().getData();
        this.world = b.getWorld().getName();
        if (this.m.equals(Material.SKULL))
        {
            if (b instanceof Skull)
            {
                this.skull_owner = ((Skull) b).getOwner();
                this.skull_rotation = ((Skull) b).getRotation();
            }
        }
        if (this.m.equals(Material.DROPPER))
        {
            if (b instanceof Dropper)
            {
                this.setInventory(((Dropper) b).getInventory());
            }
        }
        if (this.m.equals(Material.DISPENSER))
        {
            if (b instanceof Dispenser)
            {
                this.setInventory(((Dispenser) b).getInventory());
            }
        }
        if (isSign)
        {
            final Sign sign = (Sign) b;
            if (sign != null)
            {
                this.sign_lines.addAll(Arrays.asList(sign.getLines()));
            }
        }
        else if (isChest)
        {
            final Chest chest = (Chest) b;
            this.setInventory(chest.getInventory());
        }
    }
    
    public SmartArenaBlock(final Location l, final Material m, final byte data)
    {
        this.m = m;
        this.x = l.getBlockX();
        this.y = l.getBlockY();
        this.z = l.getBlockZ();
        this.world = l.getWorld().getName();
        this.data = data;
    }
    
    public Block getBlock()
    {
        final World w = Bukkit.getWorld(this.world);
        if (w == null)
        {
            return null;
        }
        final Block b = w.getBlockAt(this.x, this.y, this.z);
        return b;
    }
    
    public Material getMaterial()
    {
        return this.m;
    }
    
    public Byte getData()
    {
        return this.data;
    }
    
    public void setData(final byte data)
    {
        this.data = data;
    }
    
    public ItemStack[] getInventory()
    {
        return this.inv;
    }
    
    public HashMap<Integer, ItemStack> getNewInventory()
    {
        final HashMap<Integer, ItemStack> ret = new HashMap<>();
        for (int i = 0; i < this.item_mats.size(); i++)
        {
            ItemStack item = new ItemStack(this.item_mats.get(i), this.item_amounts.get(i), this.item_data.get(i));
            item.setDurability(this.item_durability.get(i));
            final ItemMeta im = item.getItemMeta();
            im.setDisplayName(this.item_displaynames.get(i));
            
            if (this.item_enchid.size() > i && this.item_enchid.get(i) != null)
            {
                int c = 0;
                for (final Integer ench : this.item_enchid.get(i))
                {
                    im.addEnchant(Enchantment.getById(ench), this.item_enchid_lv.get(i).get(c), true);
                    c++;
                }
            }
            
            item.setItemMeta(im);
            if (item.getType() == Material.POTION && item.getDurability() > 0)
            {
                final Potion potion = Potion.fromDamage(item.getDurability() & 0x3F);
                if (this.item_splash.size() > i)
                {
                    potion.setSplash(this.item_splash.get(i));
                }
            }
            else if (item.getType() == Material.ENCHANTED_BOOK)
            {
                final ItemStack neww = new ItemStack(Material.ENCHANTED_BOOK);
                final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) neww.getItemMeta();
                int c_ = 0;
                if (this.enchbook_id.size() > i)
                {
                    for (final Integer ench : this.enchbook_id.get(i))
                    {
                        try
                        {
                            meta.addStoredEnchant(Enchantment.getById(ench), this.enchbook_id_lv.get(i).get(c_), true);
                        }
                        catch (final Exception e)
                        {
                            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "Failed applying enchantment to enchantment book at reset.", e);
                        }
                        c_++;
                    }
                }
                neww.setItemMeta(meta);
                item = neww;
            }
            int pos = i;
            if (i < this.item_pos.size())
            {
                pos = this.item_pos.get(i);
            }
            ret.put(pos, item);
        }
        return ret;
    }
    
    public void setInventory(final Inventory inventory)
    {
        this.inv = inventory.getContents();
        this.item_mats = new ArrayList<>();
        this.item_data = new ArrayList<>();
        this.item_amounts = new ArrayList<>();
        this.item_displaynames = new ArrayList<>();
        this.item_splash = new ArrayList<>();
        this.item_pos = new ArrayList<>();
        this.item_enchid = new HashMap<>();
        this.item_enchid_lv = new HashMap<>();
        this.enchbook_id = new HashMap<>();
        this.enchbook_id_lv = new HashMap<>();
        this.item_durability = new ArrayList<>();
        
        if (inventory.getHolder() instanceof DoubleChest)
        {
            this.isDoubleChest = true;
            this.doubleChest = (DoubleChest) inventory.getHolder();
        }
        
        int pos = 0;
        for (final ItemStack i : inventory.getContents())
        {
            if (i != null)
            {
                this.item_mats.add(i.getType());
                this.item_data.add(i.getData().getData());
                this.item_amounts.add(i.getAmount());
                this.item_displaynames.add(i.getItemMeta().getDisplayName());
                this.item_durability.add(i.getDurability());
                if (i.getType() == Material.POTION && i.getDurability() > 0)
                {
                    final Potion potion = Potion.fromDamage(i.getDurability() & 0x3F);
                    this.item_splash.add(potion.isSplash());
                }
                else if (i.getType() == Material.ENCHANTED_BOOK)
                {
                    final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
                    final ArrayList<Integer> tempid = new ArrayList<>();
                    final ArrayList<Integer> templv = new ArrayList<>();
                    for (final Enchantment ench : meta.getStoredEnchants().keySet())
                    {
                        tempid.add(ench.getId());
                        templv.add(meta.getStoredEnchants().get(ench));
                    }
                    this.enchbook_id.put(pos, tempid);
                    this.enchbook_id_lv.put(pos, templv);
                    this.item_splash.add(false);
                }
                else
                {
                    this.item_splash.add(false);
                }
                this.item_pos.add(pos);
                if (i.getItemMeta().getEnchants().size() > 0)
                {
                    final ArrayList<Integer> tempid = new ArrayList<>();
                    final ArrayList<Integer> templv = new ArrayList<>();
                    for (final Enchantment ench : i.getItemMeta().getEnchants().keySet())
                    {
                        tempid.add(ench.getId());
                        templv.add(i.getItemMeta().getEnchants().get(ench));
                    }
                    this.item_enchid.put(pos, tempid);
                    this.item_enchid_lv.put(pos, templv);
                }
                else
                {
                    this.item_enchid.put(pos, new ArrayList<Integer>());
                    this.item_enchid_lv.put(pos, new ArrayList<Integer>());
                }
            }
            pos++;
        }
    }
    
    public ArrayList<String> getSignLines()
    {
        return this.sign_lines;
    }
    
    public boolean isDoubleChest()
    {
        return this.isDoubleChest;
    }
    
    public DoubleChest getDoubleChest()
    {
        return this.doubleChest;
    }
    
    public String getSkullOwner()
    {
        return this.skull_owner;
    }
    
    public BlockFace getSkullORotation()
    {
        return this.skull_rotation;
    }
    
}
