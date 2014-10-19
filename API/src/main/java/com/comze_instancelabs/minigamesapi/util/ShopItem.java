package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.PluginInstance;

import java.util.ArrayList;

public class ShopItem extends AClass {

	public ShopItem(JavaPlugin plugin, String name, String internalname, boolean enabled, ArrayList<ItemStack> items, ItemStack icon) {
		super(plugin, name, internalname, enabled, items, icon);
	}
	
	public boolean usesItems(PluginInstance pli){
		return pli.getShopConfig().getConfig().getBoolean("config.shop_items." + this.getInternalName() + ".uses_items");
	}

}
