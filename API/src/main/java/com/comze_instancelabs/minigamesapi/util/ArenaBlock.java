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
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

public class ArenaBlock implements Serializable
{
    private static final long   serialVersionUID = -1894759842709524780L;
    
    private final int           x, y, z;
    private final String        world;
    private final Material      m;
    private byte                data;
    private ArrayList<Material> item_mats;
    private ArrayList<Byte>     item_data;
    private ArrayList<Integer>  item_amounts;
    private ArrayList<String>   item_displaynames;
    
    // optional stuff
    private ArrayList<Boolean>  item_splash;
    
    private ItemStack[]         inv;
    
    public ArenaBlock(final Block b, final boolean c)
    {
        this.m = b.getType();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
        this.data = b.getData();
        this.world = b.getWorld().getName();
        if (c)
        {
            this.inv = ((Chest) b.getState()).getInventory().getContents();
            this.item_mats = new ArrayList<>();
            this.item_data = new ArrayList<>();
            this.item_amounts = new ArrayList<>();
            this.item_displaynames = new ArrayList<>();
            this.item_splash = new ArrayList<>();
            
            for (final ItemStack i : ((Chest) b.getState()).getInventory().getContents())
            {
                if (i != null)
                {
                    this.item_mats.add(i.getType());
                    this.item_data.add(i.getData().getData());
                    this.item_amounts.add(i.getAmount());
                    this.item_displaynames.add(i.getItemMeta().getDisplayName());
                    if (i.getType() == Material.POTION && i.getDurability() > 0 && i.getData().getData() > 0)
                    {
                        final Potion potion = Potion.fromDamage(i.getDurability() & 0x3F);
                        this.item_splash.add(potion.isSplash());
                    }
                    else
                    {
                        this.item_splash.add(false);
                    }
                }
            }
        }
    }
    
    public ArenaBlock(final Location l)
    {
        this.m = Material.AIR;
        this.x = l.getBlockX();
        this.y = l.getBlockY();
        this.z = l.getBlockZ();
        this.world = l.getWorld().getName();
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
    
    public ItemStack[] getInventory()
    {
        return this.inv;
    }
    
    public ArrayList<ItemStack> getNewInventory()
    {
        final int c = 0;
        final ArrayList<ItemStack> ret = new ArrayList<>();
        for (int i = 0; i < this.item_mats.size(); i++)
        {
            ItemStack item = new ItemStack(this.item_mats.get(i), this.item_amounts.get(i), this.item_data.get(i));
            final ItemMeta im = item.getItemMeta();
            im.setDisplayName(this.item_displaynames.get(i));
            item.setItemMeta(im);
            if (item.getType() == Material.POTION && item.getDurability() > 0)
            {
                final Potion potion = Potion.fromDamage(item.getDurability() & 0x3F);
                potion.setSplash(this.item_splash.get(i));
                item = potion.toItemStack(this.item_amounts.get(i));
            }
            ret.add(item);
        }
        return ret;
    }
    
    public static ItemStack getEnchantmentBook(final Map<Enchantment, Integer> t)
    {
        final ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = book.getItemMeta();
        final int i = 0;
        for (final Enchantment e : t.keySet())
        {
            meta.addEnchant(e, t.get(e), true);
        }
        book.setItemMeta(meta);
        return book;
    }
    
}
