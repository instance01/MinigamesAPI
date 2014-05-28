package com.comze_instancelabs.minigamesapi;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;

public class Classes {

	public static void openGUI(final JavaPlugin plugin, String p) {
		IconMenu iconm = new IconMenu("Classes", 27, new IconMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(IconMenu.OptionClickEvent event) {
				String d = event.getName();
				Player p = event.getPlayer();
				if (MinigamesAPI.getAPI().pinstances.get(plugin).aclasses.containsKey(d)) {
					Classes.setClass(plugin, d, p.getName());
				}
				event.setWillClose(true);
			}
		}, plugin);

		int c = 0;
		for (String ac : MinigamesAPI.getAPI().pinstances.get(plugin).aclasses.keySet()) {
			iconm.setOption(c, new ItemStack(Material.SLIME_BALL), ac, MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getString("config.kits." + ac + ".lore"));
			c++;
		}

		iconm.open(Bukkit.getPlayerExact(p));
	}

	public static void getClass(JavaPlugin plugin, String player) {
		AClass c = MinigamesAPI.getAPI().pinstances.get(plugin).pclass.get(player);
		Player p = Bukkit.getServer().getPlayer(player);
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.updateInventory();
		p.getInventory().setContents(c.getItems());
		p.updateInventory();
	}

	public static void setClass(JavaPlugin plugin, String classname, String player) {
		if (!kitPlayerHasPermission(plugin, classname, Bukkit.getPlayer(player))) {
			Bukkit.getPlayer(player).sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getString("strings.nokitperm"));
			return;
		}
		if (kitRequiresMoney(plugin, classname)) {
			kitTakeMoney(plugin, Bukkit.getPlayer(player), classname.toLowerCase());
		}
		MinigamesAPI.getAPI().pinstances.get(plugin).pclass.put(player, MinigamesAPI.getAPI().pinstances.get(plugin).aclasses.get(classname));
	}

	public static void loadClasses(JavaPlugin plugin) {
		if (MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().isSet("config.kits")) {
			for (String aclass : MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getConfigurationSection("config.kits.").getKeys(false)) {
				AClass n = new AClass(plugin, aclass, Util.parseItems(MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getString("config.kits." + aclass + ".items")));
				MinigamesAPI.getAPI().pinstances.get(plugin).aclasses.put(aclass, n);
				if (!MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().isSet("config.kits." + aclass + ".items") || !MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().isSet("config.kits." + aclass + ".lore")) {
					plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
				}
			}
		}
	}

	public static boolean kitRequiresMoney(JavaPlugin plugin, String kit) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_money");
	}

	public static boolean kitTakeMoney(JavaPlugin plugin, Player p, String kit) {
		if (!MinigamesAPI.getAPI().economy) {
			plugin.getLogger().warning("Economy is turned OFF. Turn it ON in the config.");
			return false;
		}
		if (MinigamesAPI.getAPI().econ.getBalance(p.getName()) >= MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount")) {
			EconomyResponse r = MinigamesAPI.getAPI().econ.withdrawPlayer(p.getName(), MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount"));
			if (!r.transactionSuccess()) {
				p.sendMessage(String.format("An error occured: %s", r.errorMessage));
			}
			return true;
		} else {
			p.sendMessage("§4You don't have enough money!");
			return false;
		}
	}

	public static boolean kitPlayerHasPermission(JavaPlugin plugin, String kit, Player p) {
		if (!MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_permission")) {
			return true;
		} else {
			if (p.hasPermission(MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getString("config.kits." + kit + ".permission_node"))) {
				return true;
			} else {
				return false;
			}
		}
	}

}
