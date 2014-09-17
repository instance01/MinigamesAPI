package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.minigamesapi.util.ArenaBlock;
import com.comze_instancelabs.minigamesapi.util.SmartArenaBlock;
import com.comze_instancelabs.minigamesapi.util.Util;

public class SmartReset {

	// will only reset broken/placed blocks

	HashMap<Location, SmartArenaBlock> changed = new HashMap<Location, SmartArenaBlock>();
	Arena a;

	public SmartReset(Arena a) {
		this.a = a;
	}

	public void addChanged(Block b, boolean c) {
		if (!changed.containsKey(b.getLocation())) {
			changed.put(b.getLocation(), new SmartArenaBlock(b, c));
		}
	}

	public void addChanged(Location l) {
		if (!changed.containsKey(l)) {
			changed.put(l, new SmartArenaBlock(l));
		}
	}

	public void reset() {
		System.out.println(changed.size() + " to reset.");

		final ArrayList<SmartArenaBlock> failedblocks = new ArrayList<SmartArenaBlock>();

		Bukkit.getScheduler().runTask(a.plugin, new Runnable() {
			public void run() {
				int failcount = 0;
				for (final SmartArenaBlock ablock : changed.values()) {
					try {
						final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
						if (b_.getType() == Material.FURNACE) {
							((Furnace) b_.getState()).getInventory().clear();
							((Furnace) b_.getState()).update();
						}
						if (b_.getType() == Material.CHEST) {
							((Chest) b_.getState()).getBlockInventory().clear();
							((Chest) b_.getState()).update();
						}
						if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
							b_.setType(ablock.getMaterial());
							b_.setData(ablock.getData());
						}
						if (b_.getType() == Material.CHEST) {
							((Chest) b_.getState()).getBlockInventory().clear();
							((Chest) b_.getState()).update();
							for (ItemStack i : ablock.getNewInventory()) {
								if (i != null) {
									((Chest) b_.getState()).getBlockInventory().addItem(i);
								}
							}
							((Chest) b_.getState()).update();
						}
					} catch (IllegalStateException e) {
						failcount += 1;
						failedblocks.add(ablock);
					}
				}

				changed.clear();
				a.setArenaState(ArenaState.JOIN);
				Bukkit.getScheduler().runTask(a.plugin, new Runnable() {
					public void run() {
						Util.updateSign(a.plugin, a);
					}
				});

				System.out.println(failcount + " to redo.");

				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MinigamesAPI.getAPI(), new Runnable() {
					public void run() {
						changed.clear();
						for (SmartArenaBlock ablock : failedblocks) {
							Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
							if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
								b_.setType(ablock.getMaterial());
								b_.setData(ablock.getData());
							}
							if (b_.getType() == Material.CHEST) {
								b_.setType(ablock.getMaterial());
								b_.setData(ablock.getData());
								((Chest) b_.getState()).getInventory().setContents(ablock.getInventory());
								((Chest) b_.getState()).update();
							}
						}
					}
				}, 40L);
				System.out.println("Done.");
			}
		});

	}

	public void resetRaw() {
		for (final SmartArenaBlock ablock : changed.values()) {
			try {
				final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
				if (b_.getType() == Material.FURNACE) {
					((Furnace) b_.getState()).getInventory().clear();
					((Furnace) b_.getState()).update();
				}
				if (b_.getType() == Material.CHEST) {
					((Chest) b_.getState()).getBlockInventory().clear();
					((Chest) b_.getState()).update();
				}
				if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())) {
					b_.setType(ablock.getMaterial());
					b_.setData(ablock.getData());
				}
				if (b_.getType() == Material.CHEST) {
					((Chest) b_.getState()).getBlockInventory().clear();
					((Chest) b_.getState()).update();
					for (ItemStack i : ablock.getNewInventory()) {
						if (i != null) {
							((Chest) b_.getState()).getBlockInventory().addItem(i);
						}
					}
					((Chest) b_.getState()).update();
				}
			} catch (IllegalStateException e) {

			}
		}

		changed.clear();
		a.setArenaState(ArenaState.JOIN);
		Util.updateSign(a.plugin, a);
	}
}
