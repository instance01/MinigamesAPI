package com.comze_instancelabs.minigamesapi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

public class SmartArenaBlock implements Serializable {
	private static final long serialVersionUID = -1894759842709524780L;

	private int x, y, z;
	private String world;
	private Material m;
	private byte data;
	private ArrayList<Material> item_mats;
	private ArrayList<Byte> item_data;
	private ArrayList<Integer> item_amounts;
	private ArrayList<String> item_displaynames;
	private ArrayList<Integer> item_enchid;
	private ArrayList<Integer> item_enchid_lv;

	// unused
	private ArrayList<Integer> item_pos;

	// optional stuff
	private ArrayList<Boolean> item_splash;

	private ItemStack[] inv;

	public SmartArenaBlock(Block b, boolean c) {
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
			item_pos = new ArrayList<Integer>();
			item_enchid = new ArrayList<Integer>();
			item_enchid_lv = new ArrayList<Integer>();

			int pos = 0;
			for (ItemStack i : ((Chest) b.getState()).getInventory().getContents()) {
				if (i != null) {
					System.out.println(i);
					item_mats.add(i.getType());
					item_data.add(i.getData().getData());
					item_amounts.add(i.getAmount());
					item_displaynames.add(i.getItemMeta().getDisplayName());
					if (i.getType() == Material.POTION) {
						Potion potion = Potion.fromDamage(i.getDurability() & 0x3F);
						item_splash.add(potion.isSplash());
					} else {
						item_splash.add(false);
					}
					item_pos.add(pos);
					if (i.getItemMeta().getEnchants().size() > 0) {
						for (Enchantment ench : i.getItemMeta().getEnchants().keySet()) {
							item_enchid.add(ench.getId());
							item_enchid_lv.add(i.getItemMeta().getEnchants().get(ench));
							break;
						}
					} else {
						item_enchid.add(-1);
						item_enchid_lv.add(-1);
					}
				}
				pos++;
			}
		}
	}

	public SmartArenaBlock(Location l) {
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
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		for (int i = 0; i < item_mats.size(); i++) {
			ItemStack item = new ItemStack(item_mats.get(i), item_amounts.get(i), item_data.get(i));
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(item_displaynames.get(i));
			if (item_enchid.get(i) > -1) {
				im.addEnchant(Enchantment.getById(item_enchid.get(i)), item_enchid_lv.get(i), true);
				item.addEnchantment(Enchantment.getById(item_enchid.get(i)), item_enchid_lv.get(i));
				item.addUnsafeEnchantment(Enchantment.getById(item_enchid.get(i)), item_enchid_lv.get(i));
			}
			item.setItemMeta(im);
			if (item.getType() == Material.POTION) {
				Potion potion = Potion.fromDamage(item.getDurability() & 0x3F);
				potion.setSplash(item_splash.get(i));
				item = potion.toItemStack(item_amounts.get(i));
			}
			System.out.println(item);
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