package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.shampaggon.crackshot.CSUtility;

public class Classes {

	JavaPlugin plugin;
	PluginInstance pli;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();

	public Classes(JavaPlugin plugin) {
		this.plugin = plugin;
		this.pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
	}

	public Classes(PluginInstance pli, JavaPlugin plugin) {
		this.plugin = plugin;
		this.pli = pli;
	}

	public void openGUI(final String p) {
		final Classes cl = this;
		IconMenu iconm;
		int mincount = pli.getAClasses().keySet().size();
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu(pli.getMessagesConfig().classes_item, (9 * plugin.getConfig().getInt("config.GUI.classes_gui_rows") > mincount - 1) ? 9 * plugin.getConfig().getInt("config.GUI.classes_gui_rows") : Math.round(mincount / 9) * 9 + 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(p)) {
						if (pli.global_players.containsKey(p)) {
							if (pli.getArenas().contains(pli.global_players.get(p))) {
								String d = event.getName();
								Player p = event.getPlayer();
								if (pli.getAClasses().containsKey(d)) {
									cl.setClass(pli.getClassesHandler().getInternalNameByName(d), p.getName(), true);
								}
							}
						}
					}
					event.setWillClose(true);
				}
			}, plugin);
			
			int c = 0;
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
					System.out.println(ac_.getIcon().getType());
					iconm.setOption(slot, ac_.getIcon().clone(), ac_.getName(), pli.getClassesConfig().getConfig().getString("config.kits." + ac_.getInternalName() + ".lore").split(";"));
					c++;
				}
			}
		}

		iconm.open(Bukkit.getPlayerExact(p));
		lasticonm.put(p, iconm);
	}

	public void getClass(String player) {
		if (!pli.getPClasses().containsKey(player)) {
			ArenaLogger.debug(player + " didn't select any kit and the auto_add_default_kit option might be turned off, thus he won't get any starting items.");
			return;
		}
		AClass c = pli.getPClasses().get(player);
		final Player p = Bukkit.getServer().getPlayer(player);
		Util.clearInv(p);
		ArrayList<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(c.getItems()));
		ArrayList<ItemStack> temp = new ArrayList<ItemStack>(Arrays.asList(c.getItems()));
		ArrayList<String> tempguns = new ArrayList<String>();
		final ArrayList<PotionEffectType> temppotions = new ArrayList<PotionEffectType>();
		final ArrayList<Integer> temppotions_lv = new ArrayList<Integer>();
		final ArrayList<Integer> temppotions_duration = new ArrayList<Integer>();

		// crackshot support
		for (ItemStack item : temp) {
			if (item != null) {
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().startsWith("crackshot:")) {
							items.remove(item);
							tempguns.add(item.getItemMeta().getDisplayName().split(":")[1]);
						} else if (item.getItemMeta().getDisplayName().startsWith("potioneffect:")) {
							items.remove(item);
							String potioneffect = item.getItemMeta().getDisplayName().split(":")[1];
							String data = item.getItemMeta().getDisplayName().split(":")[2];
							Integer time = Integer.parseInt(data.substring(0, data.indexOf("#")));
							Integer lv = Integer.parseInt(data.split("#")[1]);
							if (PotionEffectType.getByName(potioneffect) != null) {
								temppotions.add(PotionEffectType.getByName(potioneffect));
								temppotions_lv.add(lv);
								temppotions_duration.add(time);
							}
						}
					}
				}
			}
		}

		for (ItemStack item : items) {
			if (item != null) {
				Color c_ = null;
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().startsWith("#") && item.getItemMeta().getDisplayName().length() == 7) {
							c_ = Util.hexToRgb(item.getItemMeta().getDisplayName());
						}
					}
				}
				if (item.getTypeId() == 298 || item.getTypeId() == 302 || item.getTypeId() == 306 || item.getTypeId() == 310 || item.getTypeId() == 314) {
					if (item.getTypeId() == 298) {
						LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
						if (c_ != null) {
							lam.setColor(c_);
						}
						item.setItemMeta(lam);
					}
					p.getInventory().setHelmet(item);
					continue;
				}
				if (item.getTypeId() == 299 || item.getTypeId() == 303 || item.getTypeId() == 307 || item.getTypeId() == 311 || item.getTypeId() == 315) {
					if (item.getTypeId() == 299) {
						LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
						if (c_ != null) {
							lam.setColor(c_);
						}
						item.setItemMeta(lam);
					}
					p.getInventory().setChestplate(item);
					continue;
				}
				if (item.getTypeId() == 300 || item.getTypeId() == 304 || item.getTypeId() == 308 || item.getTypeId() == 312 || item.getTypeId() == 316) {
					if (item.getTypeId() == 300) {
						LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
						if (c_ != null) {
							lam.setColor(c_);
						}
						item.setItemMeta(lam);
					}
					p.getInventory().setLeggings(item);
					continue;
				}
				if (item.getTypeId() == 301 || item.getTypeId() == 305 || item.getTypeId() == 309 || item.getTypeId() == 313 || item.getTypeId() == 317) {
					if (item.getTypeId() == 301) {
						LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
						if (c_ != null) {
							lam.setColor(c_);
						}
						item.setItemMeta(lam);
					}
					p.getInventory().setBoots(item);
					continue;
				}
				if (item.getType() != Material.AIR) {
					p.getInventory().addItem(item);
				}
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

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (p != null) {
					int index = 0;
					for (PotionEffectType t : temppotions) {
						p.addPotionEffect(new PotionEffect(t, temppotions_duration.get(index), temppotions_lv.get(index)));
						index++;
					}
				}
			}
		}, 10L);

	}

	/**
	 * Sets the current class of a player
	 * 
	 * @param classname
	 *            the INTERNAL classname
	 * @param player
	 */
	public void setClass(String internalname, String player, boolean money) {
		for (String c : pli.getAClasses().keySet()) {
			if (c.toLowerCase().equalsIgnoreCase(internalname.toLowerCase())) {
				internalname = c;
			}
		}
		if (!kitPlayerHasPermission(internalname, Bukkit.getPlayer(player))) {
			Bukkit.getPlayer(player).sendMessage(pli.getMessagesConfig().no_perm);
			return;
		}
		boolean continue_ = true;
		if (money) {
			if (kitRequiresMoney(internalname)) {
				continue_ = kitTakeMoney(Bukkit.getPlayer(player), internalname);
			}
		}
		if (continue_) {
			pli.setPClass(player, this.getClassByInternalname(internalname));
			Bukkit.getPlayer(player).sendMessage(pli.getMessagesConfig().set_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', getClassByInternalname(internalname).getName())));
		}
	}

	public String getInternalNameByName(String name) {
		for (AClass ac : pli.getAClasses().values()) {
			if (ac.getName().equalsIgnoreCase(name)) {
				return ac.getInternalName();
			}
		}
		return "default";
	}

	public AClass getClassByInternalname(String internalname) {
		for (AClass ac : pli.getAClasses().values()) {
			if (ac.getInternalName().equalsIgnoreCase(internalname)) {
				return ac;
			}
		}
		return null;
	}

	public boolean hasClass(String player) {
		return pli.getPClasses().containsKey(player);
	}

	public String getSelectedClass(String player) {
		if (hasClass(player)) {
			return pli.getPClasses().get(player).getInternalName();
		}
		return "default";
	}

	public void loadClasses() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				FileConfiguration config = pli.getClassesConfig().getConfig();
				if (config.isSet("config.kits")) {
					for (String aclass : config.getConfigurationSection("config.kits.").getKeys(false)) {
						AClass n;
						if (config.isSet("config.kits." + aclass + ".icon")) {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
						} else {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")));
						}
						pli.addAClass(config.getString("config.kits." + aclass + ".name"), n);
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
				FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getClassesConfig().getConfig();
				if (config.isSet("config.kits")) {
					for (String aclass : config.getConfigurationSection("config.kits.").getKeys(false)) {
						AClass n;
						if (config.isSet("config.kits." + aclass + ".icon")) {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
						} else {
							n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass, config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true, Util.parseItems(config.getString("config.kits." + aclass + ".items")));
						}
						// pli.addAClass(aclass, n);
						MinigamesAPI.getAPI().getPluginInstance(plugin).addAClass(config.getString("config.kits." + aclass + ".name"), n);
						if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore")) {
							plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
						}
					}
				}
			}
		}, 20L);
	}

	/**
	 * Returns whether the kit requires money to use it
	 * 
	 * @param kit
	 *            Internal name of the kit
	 * @return
	 */
	public boolean kitRequiresMoney(String kit) {
		return pli.getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_money");
	}

	/**
	 * Gives the player the kit if he has enough money to buy it
	 * 
	 * @param p
	 *            Player to give the kit to
	 * @param kit
	 *            Internal name of the kit
	 * @return
	 */
	public boolean kitTakeMoney(Player p, String kit) {
		// Credits
		if (plugin.getConfig().getBoolean("config.use_credits_instead_of_money_for_kits")) {
			String uuid = p.getUniqueId().toString();
			int points = 0;
			if (!MinigamesAPI.getAPI().statsglobal.getConfig().isSet("players." + uuid + ".points")) {
				points = pli.getStatsInstance().getPoints(p.getName());
				MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", points);
				MinigamesAPI.getAPI().statsglobal.saveConfig();
			} else {
				points = MinigamesAPI.getAPI().statsglobal.getConfig().getInt("players." + uuid + ".points");
			}
			if (plugin.getConfig().getBoolean("config.buy_classes_forever")) {
				ClassesConfig cl = pli.getClassesConfig();
				if (!cl.getConfig().isSet("players.bought_kits." + p.getName() + "." + kit)) {
					int money = pli.getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount");
					if (points >= money) {
						MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", points - money);
						MinigamesAPI.getAPI().statsglobal.saveConfig();
						cl.getConfig().set("players.bought_kits." + p.getName() + "." + kit, true);
						cl.saveConfig();
						p.sendMessage(pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', getClassByInternalname(kit).getName())).replaceAll("<money>", Integer.toString(money)));
					} else {
						p.sendMessage(pli.getMessagesConfig().not_enough_money);
						return false;
					}
				} else {
					return true;
				}
			} else {
				if (hasClass(p.getName())) {
					if (getSelectedClass(p.getName()).equalsIgnoreCase(kit)) {
						return false;
					}
				}
				ClassesConfig config = pli.getClassesConfig();
				int money = config.getConfig().getInt("config.kits." + kit + ".money_amount");
				if (points >= money) {
					MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", points - money);
					MinigamesAPI.getAPI().statsglobal.saveConfig();
					p.sendMessage(pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', getClassByInternalname(kit).getName())).replaceAll("<money>", Integer.toString(money)));
				} else {
					p.sendMessage(pli.getMessagesConfig().not_enough_money);
					return false;
				}
			}
			return true;
		}

		// Money (economy)
		if (!MinigamesAPI.getAPI().economy) {
			plugin.getLogger().warning("Economy is turned OFF. You can turn it on in the config.");
			return false;
		}
		if (MinigamesAPI.economy) {
			if (plugin.getConfig().getBoolean("config.buy_classes_forever")) {
				ClassesConfig cl = pli.getClassesConfig();
				if (!cl.getConfig().isSet("players.bought_kits." + p.getName() + "." + kit)) {
					int money = pli.getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount");
					if (MinigamesAPI.getAPI().econ.getBalance(p.getName()) >= money) {
						EconomyResponse r = MinigamesAPI.getAPI().econ.withdrawPlayer(p.getName(), money);
						if (!r.transactionSuccess()) {
							p.sendMessage(String.format("An error occured: %s", r.errorMessage));
							return false;
						}
						cl.getConfig().set("players.bought_kits." + p.getName() + "." + kit, true);
						cl.saveConfig();
						p.sendMessage(pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', getClassByInternalname(kit).getName())).replaceAll("<money>", Integer.toString(money)));
					} else {
						p.sendMessage(pli.getMessagesConfig().not_enough_money);
						return false;
					}
				} else {
					return true;
				}
			} else {
				if (hasClass(p.getName())) {
					if (getSelectedClass(p.getName()).equalsIgnoreCase(kit)) {
						return false;
					}
					if (kitRequiresMoney(kit)) {
						Util.sendMessage(plugin, p, pli.getMessagesConfig().kit_warning);
					}
				}
				ClassesConfig config = pli.getClassesConfig();
				int money = config.getConfig().getInt("config.kits." + kit + ".money_amount");
				if (MinigamesAPI.getAPI().econ.getBalance(p.getName()) >= money) {
					EconomyResponse r = MinigamesAPI.getAPI().econ.withdrawPlayer(p.getName(), money);
					if (!r.transactionSuccess()) {
						p.sendMessage(String.format("An error occured: %s", r.errorMessage));
						return false;
					}
					p.sendMessage(pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', getClassByInternalname(kit).getName())).replaceAll("<money>", Integer.toString(money)));
				} else {
					p.sendMessage(pli.getMessagesConfig().not_enough_money);
					return false;
				}
			}
			return true;
		} else {
			return false;
		}

	}

	public boolean kitPlayerHasPermission(String kit, Player p) {
		if (!pli.getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_permission")) {
			return true;
		} else {
			if (p.hasPermission(pli.getClassesConfig().getConfig().getString("config.kits." + kit + ".permission_node"))) {
				return true;
			} else {
				return false;
			}
		}
	}

}
