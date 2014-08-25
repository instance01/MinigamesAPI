package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class AClass {

	private JavaPlugin plugin;
	private String name;
	private String internalname;
	private ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	private ItemStack icon;
	private boolean enabled = true;

	@Deprecated
	public AClass(JavaPlugin plugin, String name, ArrayList<ItemStack> items) {
		this(plugin, name, name, true, items, items.get(0));
	}
	
	public AClass(JavaPlugin plugin, String name, String internalname, ArrayList<ItemStack> items) {
		this(plugin, name, internalname, true, items, items.get(0));
	}

	public AClass(JavaPlugin plugin, String name, String internalname, boolean enabled, ArrayList<ItemStack> items) {
		this(plugin, name, internalname, enabled, items, items.get(0));
	}

	public AClass(JavaPlugin plugin, String name, String internalname, boolean enabled, ArrayList<ItemStack> items, ItemStack icon) {
		this.plugin = plugin;
		this.name = name;
		this.items = items;
		this.icon = icon;
		this.enabled = enabled;
		this.internalname = internalname;
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
	
	public String getInternalName() {
		return this.internalname;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
