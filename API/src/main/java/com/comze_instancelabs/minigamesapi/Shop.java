package com.comze_instancelabs.minigamesapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.ShopItem;
import com.comze_instancelabs.minigamesapi.util.Util;

public class Shop {

	// Allows buying extra stuff for the games like traits, coin boosters, extra weapons
	// You can change whether the particular item is persistent or just for one game

	JavaPlugin plugin;
	PluginInstance pli;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();
	public HashMap<String, ShopItem> shopitems = new HashMap<String, ShopItem>();

	public Shop(PluginInstance pli, JavaPlugin plugin) {
		this.plugin = plugin;
		this.pli = pli;
	}

	public void openGUI(final String p) {
		IconMenu iconm;
		int mincount = pli.getAClasses().keySet().size();
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu(pli.getMessagesConfig().shop_item, (9 * plugin.getConfig().getInt("config.shop_gui_rows") > mincount - 1) ? 9 * plugin.getConfig().getInt("config.shop_gui_rows") : Math.round(mincount / 9) * 9 + 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(p)) {
						if (pli.global_players.containsKey(p)) {
							if (pli.getArenas().contains(pli.global_players.get(p))) {
								String d = event.getName();
								Player p = event.getPlayer();
								// TODO:
								// buy selected extra item and save into config
							}
						}
					}
					event.setWillClose(true);
				}
			}, plugin);
		}

		int c = 0;
		for (String ac : shopitems.keySet()) {
			ShopItem ac_ = shopitems.get(ac);
			if (ac_.isEnabled()) {
				int slot = c;
				if (pli.getShopConfig().getConfig().isSet("config.shop_items." + ac_.getInternalName() + ".slot")) {
					slot = pli.getShopConfig().getConfig().getInt("config.shop_items." + ac_.getInternalName() + ".slot");
					if (slot < 0 || slot > iconm.getSize() - 1) {
						slot = c;
					}
				}
				iconm.setOption(slot, ac_.getIcon().clone(), ac_.getName(), pli.getShopConfig().getConfig().getString("config.shop_items." + ac_.getInternalName() + ".lore").split(";"));
				c++;
			}
		}

		iconm.open(Bukkit.getPlayerExact(p));
		lasticonm.put(p, iconm);
	}

	public void loadShopItems() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				FileConfiguration config = pli.getShopConfig().getConfig();
				if (config.isSet("config.shop_items")) {
					for (String aclass : config.getConfigurationSection("config.shop_items.").getKeys(false)) {
						ShopItem n = new ShopItem(plugin, config.getString("config.shop_items." + aclass + ".name"), aclass, config.isSet("config.shop_items." + aclass + ".enabled") ? config.getBoolean("config.shop_items." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.shop_items." + aclass + ".items")), Util.parseItems(config.getString("config.shop_items." + aclass + ".icon")).get(0));
						shopitems.put(config.getString("config.shop_items." + aclass + ".name"), n);
					}
				}
			}
		}, 20L);
	}

	public void buy() {

	}

	public void giveShopItems(Player p) {

	}

}
