package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class AClass {

	private JavaPlugin plugin;
	private String name;
	private ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	private ItemStack icon;
	private boolean enabled = true;

	public AClass(JavaPlugin plugin, String name, ArrayList<ItemStack> items) {
		this(plugin, name, true, items, items.get(0));
	}

	public AClass(JavaPlugin plugin, String name, boolean enabled, ArrayList<ItemStack> items) {
		this(plugin, name, enabled, items, items.get(0));
	}

	public AClass(JavaPlugin plugin, String name, boolean enabled, ArrayList<ItemStack> items, ItemStack icon) {
		this.plugin = plugin;
		this.name = name;
		this.items = items;
		this.icon = icon;
		this.enabled = enabled;
	}

	public ItemStack[] getItems() {
		ItemStack[] ret = new ItemStack[items.size()];
		int c = 0;
		for (ItemStack f : items) {
			ret[c] = f;
			c++;
		}
		return ret;
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	public String getName() {
		return this.name;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
