package com.comze_instancelabs.minigamesapi.util;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ArenaBlock implements Serializable {
	private static final long serialVersionUID = -1894759842709524780L;

	public int x, y, z;
	public String world;
	public Material m;
	public byte data;
	public ItemStack[] inv;

	public ArenaBlock(Block b, boolean c) {
		m = b.getType();
		x = b.getX();
		y = b.getY();
		z = b.getZ();
		data = b.getData();
		world = b.getWorld().getName();
		if (c) {
			inv = ((Chest) b.getState()).getInventory().getContents();
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

}