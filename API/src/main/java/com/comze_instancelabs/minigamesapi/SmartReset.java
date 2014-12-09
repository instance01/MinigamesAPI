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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.comze_instancelabs.minigamesapi.util.ArenaBlock;
import com.comze_instancelabs.minigamesapi.util.ChangeCause;
import com.comze_instancelabs.minigamesapi.util.Cuboid;
import com.comze_instancelabs.minigamesapi.util.SmartArenaBlock;
import com.comze_instancelabs.minigamesapi.util.Util;

public class SmartReset {

	// will only reset broken/placed blocks

	HashMap<Location, SmartArenaBlock> changed = new HashMap<Location, SmartArenaBlock>();

	Arena a;

	public SmartReset(Arena a) {
		this.a = a;
	}

	public SmartArenaBlock addChanged(Block b) {
		if (!changed.containsKey(b.getLocation())) {
			SmartArenaBlock sablock = new SmartArenaBlock(b, b.getType() == Material.CHEST, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
			changed.put(b.getLocation(), sablock);
			return sablock;
		}
		return null;
	}

	public SmartArenaBlock addChanged(Block b, boolean c) {
		if (!changed.containsKey(b.getLocation())) {
			SmartArenaBlock sablock = new SmartArenaBlock(b, c, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
			changed.put(b.getLocation(), sablock);
			return sablock;
		}
		return null;
	}

	public SmartArenaBlock addChanged(Block b, boolean c, ChangeCause cause) {
		if (!changed.containsKey(b.getLocation())) {
			SmartArenaBlock sablock = new SmartArenaBlock(b, c, b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST);
			changed.put(b.getLocation(), sablock);
			return sablock;
		}
		return null;
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
						resetSmartResetBlock(ablock);
					} catch (IllegalStateException e) {
						failcount += 1;
						failedblocks.add(ablock);
					}
				}

				changed.clear();
				a.setArenaState(ArenaState.JOIN);
				Bukkit.getScheduler().runTask(a.plugin, new Runnable() {
					public void run() {
						a.setArenaState(ArenaState.JOIN);
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
				resetSmartResetBlock(ablock);
			} catch (IllegalStateException e) {

			}
		}

		changed.clear();
		a.setArenaState(ArenaState.JOIN);
		Util.updateSign(a.plugin, a);
	}

	public void resetSmartResetBlock(SmartArenaBlock ablock) {
		final Block b_ = ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation());
		if (b_.getType() == Material.FURNACE) {
			((Furnace) b_.getState()).getInventory().clear();
			((Furnace) b_.getState()).update();
		}
		if (b_.getType() == Material.CHEST) {
			((Chest) b_.getState()).getBlockInventory().clear();
			((Chest) b_.getState()).update();
		}
		if (!b_.getType().toString().equalsIgnoreCase(ablock.getMaterial().toString()) || b_.getData() != ablock.getData()) {
			b_.setType(ablock.getMaterial());
			b_.setData(ablock.getData());
		}
		if (b_.getType() == Material.CHEST) {
			if(ablock.isDoubleChest()){
				DoubleChest dc = ablock.getDoubleChest();
				System.out.println(dc.getLocation());
				HashMap<Integer, ItemStack> chestinv = ablock.getNewInventory();
				for (Integer i : chestinv.keySet()) {
					ItemStack item = chestinv.get(i);
					if (item != null) {
						dc.getInventory().setItem(i, item);
					}
				}
				((Chest) b_.getState()).update();
				return;
			}
			((Chest) b_.getState()).getBlockInventory().clear();
			((Chest) b_.getState()).update();
			HashMap<Integer, ItemStack> chestinv = ablock.getNewInventory();
			for (Integer i : chestinv.keySet()) {
				ItemStack item = chestinv.get(i);
				if (item != null) {
					((Chest) b_.getState()).getBlockInventory().setItem(i, item);
				}
			}
			((Chest) b_.getState()).update();
		}
		if (b_.getType() == Material.WALL_SIGN || b_.getType() == Material.SIGN_POST) {
			Sign sign = (Sign) b_.getState();
			if (sign != null) {
				int i = 0;
				for (String line : ablock.getSignLines()) {
					sign.setLine(i, line);
					i++;
					if (i > 3) {
						break;
					}
				}
				sign.update();
			}
		}
	}

	public void saveSmartBlocksToFile() {
		File f = new File(a.getPlugin().getDataFolder() + "/" + a.getInternalName() + "_smart");

		FileOutputStream fos;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f);
			oos = new BukkitObjectOutputStream(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (SmartArenaBlock bl : changed.values()) {
			try {
				oos.writeObject(bl);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MinigamesAPI.getAPI().getLogger().info("Saved SmartBlocks of " + a.getInternalName());
	}

	public void loadSmartBlocksFromFile() {
		File f = new File(a.getPlugin().getDataFolder() + "/" + a.getInternalName() + "_smart");
		if (!f.exists()) {
			return;
		}
		FileInputStream fis = null;
		BukkitObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			ois = new BukkitObjectInputStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				Object b = null;
				try {
					b = ois.readObject();
				} catch (EOFException e) {
					MinigamesAPI.getAPI().getLogger().info("Finished restoring SmartReset blocks for " + a.getInternalName() + ".");
				} catch (ClosedChannelException e) {
					System.out.println("Something is wrong with your SmartReset file and the reset might not be successful.");
				}

				if (b != null) {
					SmartArenaBlock ablock = (SmartArenaBlock) b;
					this.resetSmartResetBlock(ablock);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (f.exists()) {
			f.delete();
		}
	}
}
