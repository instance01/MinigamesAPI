package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class AClass {

	private JavaPlugin plugin;
	private String name;
	private ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	
	public AClass(JavaPlugin plugin, String name, ArrayList<ItemStack> items){
		this.plugin = plugin;
		this.name = name;
		this.items = items;
	}
	
	public ItemStack[] getItems(){
		ItemStack[] ret = new ItemStack[items.size()];
		int c = 0;
		for(ItemStack f : items){
			ret[c] = f;
			c++;
		}
		return ret;
	}
}
