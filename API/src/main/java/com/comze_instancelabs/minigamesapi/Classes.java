package com.comze_instancelabs.minigamesapi;

import java.util.HashMap;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;

public class Classes {

	JavaPlugin plugin;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();

	public Classes(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void openGUI(String p) {
		final Classes cl = this;
		IconMenu iconm;
		int mincount = MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().keySet().size();
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu("Classes", (9 * plugin.getConfig().getInt("config.classes_gui_rows") > mincount - 1) ? 9 * plugin.getConfig().getInt("config.classes_gui_rows") : Math.round(mincount / 9) * 9 + 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					String d = event.getName();
					Player p = event.getPlayer();
					if (MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().containsKey(d)) {
						cl.setClass(d, p.getName());
					}
					event.setWillClose(true);
				}
			}, plugin);
		}

		int c = 0;
		for (String ac : MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().keySet()) {
			AClass ac_ = MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().get(ac);
			if (ac_.isEnabled()) {
				iconm.setOption(c, ac_.getIcon(), ac_.getName(), MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getString("config.kits." + ac + ".lore").split(";"));
				c++;
			}
		}

		iconm.open(Bukkit.getPlayerExact(p));
		lasticonm.put(p, iconm);
	}

	public void getClass(String player) {
		AClass c = MinigamesAPI.getAPI().pinstances.get(plugin).getPClasses().get(player);
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

	public void setClass(String classname, String player) {
		if (!kitPlayerHasPermission(classname, Bukkit.getPlayer(player))) {
			Bukkit.getPlayer(player).sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
			return;
		}
		if (kitRequiresMoney(classname)) {
			kitTakeMoney(Bukkit.getPlayer(player), classname.toLowerCase());
		}
		MinigamesAPI.getAPI().pinstances.get(plugin).setPClass(player, MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().get(classname));
		Bukkit.getPlayer(player).sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().set_kit.replaceAll("<kit>", classname));
	}

	public boolean hasClass(String player) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getPClasses().containsKey(player);
	}

	public void loadClasses() {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig();
		if (config.isSet("config.kits")) {
			for (String aclass : config.getConfigurationSection("config.kits.").getKeys(false)) {
				AClass n;
				if (config.isSet("config.kits." + aclass + ".icon")) {
					n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
				} else {
					n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")));
				}
				MinigamesAPI.getAPI().pinstances.get(plugin).addAClass(aclass, n);
				if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore")) {
					plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
				}
			}
		}
	}

	/**
	 * Please use new Classes().loadClasses();
	 * 
	 * @param plugin
	 */
	@Deprecated
	public static void loadClasses(JavaPlugin plugin) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig();
		if (config.isSet("config.kits")) {
			for (String aclass : config.getConfigurationSection("config.kits.").getKeys(false)) {
				AClass n;
				if (config.isSet("config.kits." + aclass + ".icon")) {
					n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
				} else {
					n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")));
				}
				MinigamesAPI.getAPI().pinstances.get(plugin).addAClass(aclass, n);
				if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore")) {
					plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
				}
			}
		}
	}

	public boolean kitRequiresMoney(String kit) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_money");
	}

	public boolean kitTakeMoney(Player p, String kit) {
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

	public boolean kitPlayerHasPermission(String kit, Player p) {
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
