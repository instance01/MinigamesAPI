package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.milkbowl.vault.economy.EconomyResponse;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.shampaggon.crackshot.CSUtility;

public class Classes {

	JavaPlugin plugin;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();

	public Classes(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void openGUI(final String p) {
		final Classes cl = this;
		IconMenu iconm;
		int mincount = MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().keySet().size();
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().classes_item, (9 * plugin.getConfig().getInt("config.classes_gui_rows") > mincount - 1) ? 9 * plugin.getConfig().getInt("config.classes_gui_rows") : Math.round(mincount / 9) * 9 + 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(p)) {
						PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
						if (pli.global_players.containsKey(p)) {
							if (pli.getArenas().contains(pli.global_players.get(p))) {
								String d = event.getName();
								Player p = event.getPlayer();
								if (pli.getAClasses().containsKey(d)) {
									cl.setClass(pli.getClassesHandler().getInternalNameByName(d), p.getName());
								}
							}
						}
					}
					event.setWillClose(true);
				}
			}, plugin);
		}

		int c = 0;
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		for (String ac : pli.getAClasses().keySet()) {
			AClass ac_ = pli.getAClasses().get(ac);
			if (ac_.isEnabled()) {
				int slot = c;
				if (pli.getClassesConfig().getConfig().isSet("config.kits." + ac_.getInternalName() + ".slot")) {
					slot = pli.getClassesConfig().getConfig().getInt("config.kits." + ac_.getInternalName() + ".slot");
					if (slot < 0 || slot > iconm.getSize() - 1) {
						slot = c;
					}
				}
				iconm.setOption(slot, ac_.getIcon().clone(), ac_.getName(), pli.getClassesConfig().getConfig().getString("config.kits." + ac_.getInternalName() + ".lore").split(";"));
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
		ArrayList<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(c.getItems()));
		ArrayList<ItemStack> temp = new ArrayList<ItemStack>(Arrays.asList(c.getItems()));
		ArrayList<String> tempguns = new ArrayList<String>();

		// crackshot support
		for (ItemStack item : temp) {
			if (item != null) {
				if (item.getItemMeta().hasDisplayName()) {
					if (item.getItemMeta().getDisplayName().startsWith("crackshot:")) {
						items.remove(item);
						tempguns.add(item.getItemMeta().getDisplayName().split(":")[1]);
					}
				}
			}
		}

		for (ItemStack item : items) {
			if (item != null) {
				if (item.getTypeId() == 298 || item.getTypeId() == 302 || item.getTypeId() == 306 || item.getTypeId() == 310 || item.getTypeId() == 314) {
					p.getInventory().setHelmet(item);
					continue;
				}
				if (item.getTypeId() == 299 || item.getTypeId() == 303 || item.getTypeId() == 307 || item.getTypeId() == 311 || item.getTypeId() == 315) {
					p.getInventory().setChestplate(item);
					continue;
				}
				if (item.getTypeId() == 300 || item.getTypeId() == 304 || item.getTypeId() == 308 || item.getTypeId() == 312 || item.getTypeId() == 316) {
					p.getInventory().setLeggings(item);
					continue;
				}
				if (item.getTypeId() == 301 || item.getTypeId() == 305 || item.getTypeId() == 309 || item.getTypeId() == 313 || item.getTypeId() == 317) {
					p.getInventory().setBoots(item);
					continue;
				}
				p.getInventory().addItem(item);
			}
		}
		// p.getInventory().setContents((ItemStack[]) items.toArray(new ItemStack[items.size()]));
		p.updateInventory();

		if (MinigamesAPI.getAPI().crackshot) {
			for (String t : tempguns) {
				CSUtility cs = new CSUtility();
				cs.giveWeapon(p, t, 1);
			}
		}
	}

	/**
	 * Sets the current class of a player
	 * 
	 * @param classname
	 *            the INTERNAL classname
	 * @param player
	 */
	public void setClass(String internalname, String player) {
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		if (!kitPlayerHasPermission(internalname, Bukkit.getPlayer(player))) {
			Bukkit.getPlayer(player).sendMessage(pli.getMessagesConfig().no_perm);
			return;
		}
		boolean continue_ = true;
		if (kitRequiresMoney(internalname)) {
			continue_ = kitTakeMoney(Bukkit.getPlayer(player), internalname);
		}
		if (continue_) {
			pli.setPClass(player, this.getClassByInternalname(internalname));
			Bukkit.getPlayer(player).sendMessage(pli.getMessagesConfig().set_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', getClassByInternalname(internalname).getName())));
			try {
				pli.scoreboardLobbyManager.updateScoreboard(plugin, pli.global_players.get(player));
			} catch (Exception e) {
				System.out.println("Failed updating scoreboard with new kit");
			}
		}
	}

	public String getInternalNameByName(String name) {
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		for (AClass ac : pli.getAClasses().values()) {
			if (ac.getName().equalsIgnoreCase(name)) {
				return ac.getInternalName();
			}
		}
		return "default";
	}

	public AClass getClassByInternalname(String internalname) {
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		for (AClass ac : pli.getAClasses().values()) {
			if (ac.getInternalName().equalsIgnoreCase(internalname)) {
				return ac;
			}
		}
		return null;
	}

	public boolean hasClass(String player) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getPClasses().containsKey(player);
	}

	public String getSelectedClass(String player) {
		if (hasClass(player)) {
			return MinigamesAPI.getAPI().pinstances.get(plugin).getPClasses().get(player).getInternalName();
		}
		return "default";
	}

	public void loadClasses() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig();
				if (config.isSet("config.kits")) {
					for (String aclass : config.getConfigurationSection("config.kits.").getKeys(false)) {
						AClass n;
						if (config.isSet("config.kits." + aclass + ".icon")) {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
						} else {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")));
						}
						MinigamesAPI.getAPI().pinstances.get(plugin).addAClass(config.getString("config.kits." + aclass + ".name"), n);
						if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore")) {
							plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
						}
					}
				}
			}
		}, 20L);
	}

	/**
	 * Please use new Classes().loadClasses();
	 * 
	 * @param plugin
	 */
	@Deprecated
	public static void loadClasses(final JavaPlugin plugin) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig();
				if (config.isSet("config.kits")) {
					for (String aclass : config.getConfigurationSection("config.kits.").getKeys(false)) {
						AClass n;
						if (config.isSet("config.kits." + aclass + ".icon")) {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
						} else {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")));
						}
						// MinigamesAPI.getAPI().pinstances.get(plugin).addAClass(aclass, n);
						MinigamesAPI.getAPI().pinstances.get(plugin).addAClass(config.getString("config.kits." + aclass + ".name"), n);
						if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore")) {
							plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
						}
					}
				}
			}
		}, 20L);
	}

	public boolean kitRequiresMoney(String kit) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_money");
	}

	public boolean kitTakeMoney(Player p, String kit) {
		if (!MinigamesAPI.getAPI().economy) {
			plugin.getLogger().warning("Economy is turned OFF. Turn it ON in the config.");
			return false;
		}
		if (MinigamesAPI.economy) {
			if (plugin.getConfig().getBoolean("config.buy_classes_forever")) {
				ClassesConfig cl = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig();
				if (!cl.getConfig().isSet("players.bought_kits." + p.getName() + "." + kit)) {
					int money = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount");
					if (MinigamesAPI.getAPI().econ.getBalance(p.getName()) >= money) {
						EconomyResponse r = MinigamesAPI.getAPI().econ.withdrawPlayer(p.getName(), money);
						if (!r.transactionSuccess()) {
							p.sendMessage(String.format("An error occured: %s", r.errorMessage));
							return false;
						}
						cl.getConfig().set("players.bought_kits." + p.getName() + "." + kit, true);
						cl.saveConfig();
						p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", kit).replaceAll("<money>", Integer.toString(money)));
					} else {
						p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().not_enough_money);
						return false;
					}
				} else {
					return true;
				}
			} else {
				ClassesConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig();
				int money = config.getConfig().getInt("config.kits." + kit + ".money_amount");
				if (MinigamesAPI.getAPI().econ.getBalance(p.getName()) >= money) {
					EconomyResponse r = MinigamesAPI.getAPI().econ.withdrawPlayer(p.getName(), money);
					if (!r.transactionSuccess()) {
						p.sendMessage(String.format("An error occured: %s", r.errorMessage));
						return false;
					}
					p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", kit).replaceAll("<money>", Integer.toString(money)));
				} else {
					p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().not_enough_money);
					return false;
				}
			}
			return true;
		} else {
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
