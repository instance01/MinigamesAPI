package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ShopItem extends AClass {

	public ShopItem(JavaPlugin plugin, String name, String internalname, boolean enabled, ArrayList<ItemStack> items, ItemStack icon) {
		super(plugin, name, internalname, enabled, items, icon);
	}

}
