package com.comze_instancelabs.minigamesapi.util;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ArenaBlock implements Serializable {
    private static final long serialVersionUID = -1894759842709524780L;

    private int x, y, z;
    private String world;
    private Material m;
    private byte data;
    private ArrayList<Material> item_mats;
    private ArrayList<Byte> item_data;
    private ArrayList<Integer> item_amounts;
    private ArrayList<String> item_displaynames;

    // optional stuff
    private ArrayList<Boolean> item_splash;

    private ItemStack[] inv;

    public ArenaBlock(Block b, boolean c) {
        m = b.getType();
        x = b.getX();
        y = b.getY();
        z = b.getZ();
        data = b.getData();
        world = b.getWorld().getName();
        if (c) {
            inv = ((Chest) b.getState()).getInventory().getContents();
            item_mats = new ArrayList<Material>();
            item_data = new ArrayList<Byte>();
            item_amounts = new ArrayList<Integer>();
            item_displaynames = new ArrayList<String>();
            item_splash = new ArrayList<Boolean>();

            for (ItemStack i : ((Chest) b.getState()).getInventory().getContents()) {
                if (i != null) {
                    item_mats.add(i.getType());
                    item_data.add(i.getData().getData());
                    item_amounts.add(i.getAmount());
                    item_displaynames.add(i.getItemMeta().getDisplayName());
                    if (i.getType() == Material.POTION && i.getDurability() > 0 && i.getData().getData() > 0) {
                        Potion potion = Potion.fromDamage(i.getDurability() & 0x3F);
                        item_splash.add(potion.isSplash());
                    } else {
                        item_splash.add(false);
                    }
                }
            }
        }
    }

    public ArenaBlock(Location l) {
        m = Material.AIR;
        x = l.getBlockX();
        y = l.getBlockY();
        z = l.getBlockZ();
        world = l.getWorld().getName();
    }

    public Block getBlock() {
        World w = Bukkit.getWorld(world);
        if (w == null)
            return null;
        Block b = w.getBlockAt(x, y, z);
        return b;
    }

    public Material getMaterial() {
        return m;
    }

    public Byte getData() {
        return data;
    }

    public ItemStack[] getInventory() {
        return inv;
    }

    public ArrayList<ItemStack> getNewInventory() {
        int c = 0;
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        for (int i = 0; i < item_mats.size(); i++) {
            ItemStack item = new ItemStack(item_mats.get(i), item_amounts.get(i), item_data.get(i));
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(item_displaynames.get(i));
            item.setItemMeta(im);
            if (item.getType() == Material.POTION && item.getDurability() > 0) {
                Potion potion = Potion.fromDamage(item.getDurability() & 0x3F);
                potion.setSplash(item_splash.get(i));
                item = potion.toItemStack(item_amounts.get(i));
            }
            ret.add(item);
        }
        return ret;
    }

    public static ItemStack getEnchantmentBook(Map<Enchantment, Integer> t) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = book.getItemMeta();
        int i = 0;
        for (Enchantment e : t.keySet()) {
            meta.addEnchant(e, t.get(e), true);
        }
        book.setItemMeta(meta);
        return book;
    }

}