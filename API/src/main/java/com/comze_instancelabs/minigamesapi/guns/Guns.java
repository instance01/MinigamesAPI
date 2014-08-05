package com.comze_instancelabs.minigamesapi.guns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;

public class Guns {

	// Four gun types:
	// - default pistol
	// - sniper
	// - freeze gun
	// - grenade launcher

	// Four upgradable attributes:
	// - speed
	// - durability
	// - shoot_amount
	// - knockback_multiplier

	// each attribute has 3 levels
	// that gives us 4*4*3 = 48 levels

	// how much more the next level of an attribute will cost
	public double level_multiplier = 3D;

	// attribute base costs
	public int speed_cost = 40;
	public int durability_cost = 30;
	public int shoot_amount_cost = 70;
	public int knockback_multiplier_cost = 100;

	public HashMap<String, IconMenu> lastmainiconm = new HashMap<String, IconMenu>();
	public HashMap<String, IconMenu> lastmainediticonm = new HashMap<String, IconMenu>();
	public HashMap<String, IconMenu> lastupgradeiconm = new HashMap<String, IconMenu>();

	// TODO this means only for one plugin for now
	public HashMap<String, HashMap<Gun, int[]>> pgunattributes = new HashMap<String, HashMap<Gun, int[]>>();

	public JavaPlugin plugin;

	public Guns(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void openGUI(String p) {
		final int credits = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().getPoints(p);
		IconMenu iconm;
		if (lastmainiconm.containsKey(p)) {
			iconm = lastmainiconm.get(p);
		} else {
			iconm = new IconMenu("Gun Upgrades (Credits: " + credits + ")", 36, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					String d = event.getName();
					Player p = event.getPlayer();
					if (MinigamesAPI.getAPI().pinstances.get(plugin).getAllGuns().containsKey(d)) {
						openGunMainEditGUI(p.getName(), d);
					} else {
						String raw = event.getItem().getItemMeta().getLore().get(0);
						String gun = raw.substring(0, raw.indexOf(" "));
						Gun g = MinigamesAPI.getAPI().pinstances.get(plugin).getAllGuns().get(gun);
						if (g != null) {
							int[] pattributes = getPlayerGunAttributeLevels(plugin, p.getName(), g);
							HashMap<Gun, int[]> t;
							if (pgunattributes.containsKey(p.getName())) {
								t = pgunattributes.get(p.getName());
								t.put(g, pattributes);
							} else {
								t = new HashMap<Gun, int[]>();
								t.put(g, pattributes);
							}
							pgunattributes.put(p.getName(), t);
							boolean done = false;
							double cost = 0.0D;
							if (d.startsWith("Speed")) {
								int i = pattributes[0];
								cost = Math.pow(level_multiplier, i) * speed_cost;
								if (i < 3 && credits >= cost) {
									openUpgradeGUI(p.getName(), gun, "speed", pattributes[0] + 1, cost);
									done = true;
									// setPlayerGunLevel(plugin, p.getName(), gun, "speed", pattributes[0] + 1);
								}
							} else if (d.startsWith("Durability")) {
								int i = pattributes[1];
								cost = Math.pow(level_multiplier, i) * durability_cost;
								if (i < 3 && credits >= cost) {
									openUpgradeGUI(p.getName(), gun, "durability", pattributes[1] + 1, cost);
									done = true;
								}
							} else if (d.startsWith("Shoot")) {
								int i = pattributes[2];
								cost = Math.pow(level_multiplier, i) * shoot_amount_cost;
								if (i < 3 && credits >= cost) {
									openUpgradeGUI(p.getName(), gun, "shoot", pattributes[2] + 1, cost);
									done = true;
								}
							} else if (d.startsWith("Knockback")) {
								int i = pattributes[3];
								cost = Math.pow(level_multiplier, i) * knockback_multiplier_cost;
								if (i < 3 && credits >= cost) {
									openUpgradeGUI(p.getName(), gun, "knockback", pattributes[3] + 1, cost);
									done = true;
								}
							}
							if (!done) {
								p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().not_enough_credits.replaceAll("<credits>", Double.toString(cost)));
							}
						}
					}
					event.setWillClose(false);
				}
			}, plugin);
			lastmainiconm.put(p, iconm);
		}

		int c = 0;
		for (String ac : MinigamesAPI.getAPI().pinstances.get(plugin).getAllGuns().keySet()) {
			Gun ac_ = MinigamesAPI.getAPI().pinstances.get(plugin).getAllGuns().get(ac);
			int[] pattributes = getPlayerGunAttributeLevels(plugin, p, ac_);
			iconm.setOption(c, ac_.icon.get(0), ac, MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig().getString("config.guns." + ac + ".lore"));
			iconm.setOption(c + 2, new ItemStack(Material.SUGAR), "Speed Lv " + ChatColor.DARK_RED + pattributes[0], ac + " Speed Upgrade");
			iconm.setOption(c + 3, new ItemStack(Material.DIAMOND), "Durability Lv " + ChatColor.DARK_RED + pattributes[1], ac + " Durability Upgrade");
			iconm.setOption(c + 4, new ItemStack(Material.EGG), "Shoot Lv " + ChatColor.DARK_RED + pattributes[2], ac + " Shoot amount Upgrade");
			iconm.setOption(c + 5, new ItemStack(Material.STICK), "Knockback Lv " + ChatColor.DARK_RED + pattributes[3], ac + " Knockback Upgrade");
			c += 9;
		}

		iconm.open(Bukkit.getPlayerExact(p));
	}

	public int[] getPlayerGunAttributeLevels(JavaPlugin plugin, String p, Gun g) {
		int[] ret = new int[4];
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		String path = "players." + p + "." + g.name + ".";
		ret[0] = config.isSet(path + "speed") ? config.getInt(path + "speed") : 0;
		ret[1] = config.isSet(path + "durability") ? config.getInt(path + "durability") : 0;
		ret[2] = config.isSet(path + "shoot") ? config.getInt(path + "shoot") : 0;
		ret[3] = config.isSet(path + "knockback") ? config.getInt(path + "knockback") : 0;
		HashMap<Gun, int[]> t;
		if (pgunattributes.containsKey(p)) {
			t = pgunattributes.get(p);
			t.put(g, ret);
		} else {
			t = new HashMap<Gun, int[]>();
			t.put(g, ret);
		}
		pgunattributes.put(p, t);
		return ret;
	}

	public void setPlayerGunLevel(JavaPlugin plugin, String p, String g, String attribute, int level, double cost) {
		int credits = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().getPoints(p);
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		String path = "players." + p + "." + g + ".";
		config.set(path + attribute, level);
		MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().saveConfig();
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().setPoints(p, (int) (credits - cost));
	}

	public void setPlayerGunMain(JavaPlugin plugin, String p, String g, boolean val) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		String path = "players." + p + "." + g + ".main";
		if (getPlayerAllMainGunsCount(plugin, p) > 1 && val) {
			Bukkit.getPlayer(p).sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().too_many_main_guns);
			return;
		}
		config.set(path, val);
		MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().saveConfig();
	}

	public int getPlayerAllMainGunsCount(JavaPlugin plugin, String p) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		String path = "players." + p + ".";
		int ret = 0;
		if (config.isSet(path)) {
			for (String g : config.getConfigurationSection(path).getKeys(false)) {
				if (config.isSet(path + g + ".main")) {
					if (config.getBoolean(path + g + ".main")) {
						ret++;
					}
				}
			}
		}
		return ret;
	}

	public void openGunMainEditGUI(String p, final String g) {
		IconMenu iconm;
		Player p_ = Bukkit.getPlayer(p);
		String guns = "";
		for (String gun : getAllMainGuns(p_)) {
			guns += gun + ", ";
		}
		if(guns.equalsIgnoreCase("")){
			guns = "-";
		}
		guns = guns.substring(0, guns.length() - 2);
		p_.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().all_guns.replaceAll("<guns>", guns));
		
		if (lastmainediticonm.containsKey(p)) {
			iconm = lastmainediticonm.get(p);
		} else {
			iconm = new IconMenu("Set Main Gun", 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					String d = event.getName();
					Player p = event.getPlayer();
					if (d.startsWith("Set")) {
						setPlayerGunMain(plugin, p.getName(), g, true);
					} else if (d.startsWith("Remove")) {
						setPlayerGunMain(plugin, p.getName(), g, false);
					}
					openGUI(p.getName());
					event.setWillClose(false);
					event.setWillDestroy(true);
				}
			}, plugin);
		}

		iconm.setOption(0, new ItemStack(Material.WOOL, 1, (short) 5), "Set " + g + " as Main/Secondary Gun", "");
		iconm.setOption(8, new ItemStack(Material.WOOL, 1, (short) 14), "Remove " + g + " as Main/Secondary Gun", "");

		iconm.open(Bukkit.getPlayerExact(p));
	}

	public void openUpgradeGUI(String p, final String g, final String attribute, final int level, final double cost) {
		IconMenu iconm;
		if (lastupgradeiconm.containsKey(p)) {
			iconm = lastupgradeiconm.get(p);
		} else {
			iconm = new IconMenu("Upgrade?", 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					String d = event.getName();
					Player p = event.getPlayer();
					if (d.startsWith("Buy")) {
						setPlayerGunLevel(plugin, p.getName(), g, attribute, level, cost);
						p.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().attributelevel_increased.replaceAll("<attribute>", attribute));
					}
					openGUI(p.getName());
					event.setWillClose(false);
					event.setWillDestroy(true);
				}
			}, plugin);
		}

		iconm.setOption(0, new ItemStack(Material.WOOL, 1, (short) 5), "Buy " + attribute + " Upgrade", "");
		iconm.setOption(8, new ItemStack(Material.WOOL, 1, (short) 14), "DON'T buy " + attribute + " Upgrade", "");

		iconm.open(Bukkit.getPlayerExact(p));
	}

	public static void loadGuns(JavaPlugin plugin) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		if (config.isSet("config.guns")) {
			for (String gun : config.getConfigurationSection("config.guns.").getKeys(false)) {
				String path = "config.guns." + gun + ".";
				Gun n = new Gun(plugin, gun, config.getDouble(path + "speed"), config.getInt(path + "durability"), config.getInt(path + "shoot_amount"), config.getDouble(path + "knockback_multiplier"), Egg.class, Util.parseItems(config.getString(path + "items")), Util.parseItems(config.getString(path + "icon")));
				MinigamesAPI.getAPI().pinstances.get(plugin).addGun(gun, n);
			}
		}
	}

	public ArrayList<String> getAllMainGuns(Player p) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		ArrayList<String> ret = new ArrayList<String>();
		for (String gun : config.getConfigurationSection("players." + p.getName() + ".").getKeys(false)) {
			String path = "players." + p.getName() + "." + gun + ".main";
			if (config.isSet(path)) {
				if(config.getBoolean(path)){
					ret.add(gun);
				}
			}
		}
		return ret;
	}

	public void giveMainGuns(Player p) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		if (config.isSet("players." + p.getName() + ".")) {
			int count = 0;
			for (String gun : config.getConfigurationSection("players." + p.getName() + ".").getKeys(false)) {
				String path = "players." + p.getName() + "." + gun + ".main";
				if (config.isSet(path)) {
					if (config.getBoolean(path)) {
						// main gun
						Gun g = MinigamesAPI.getAPI().pinstances.get(plugin).getAllGuns().get(gun);
						if (g != null) {
							p.updateInventory();
							for (ItemStack i : g.items) {
								ItemStack temp = i;
								ItemMeta itemm = temp.getItemMeta();
								itemm.setDisplayName(gun);
								System.out.println(itemm.getDisplayName());
								temp.setItemMeta(itemm);
								p.getInventory().addItem(temp);
							}
							p.updateInventory();
							count++;
						}
					}
				}
			}
			if (count < 1) {
				// player doesn't have any main, give random gun
				HashMap<String, Gun> guns = MinigamesAPI.getAPI().pinstances.get(plugin).getAllGuns();
				List<String> t = new ArrayList<String>(guns.keySet());
				String gun = t.get((new Random()).nextInt(t.size()));
				Gun g = guns.get(gun);
				if (g != null) {
					p.updateInventory();
					for (ItemStack i : g.items) {
						ItemStack temp = i;
						ItemMeta itemm = temp.getItemMeta();
						itemm.setDisplayName(gun);
						temp.setItemMeta(itemm);
						p.getInventory().addItem(temp);
					}
					p.updateInventory();
					count++;
				}
			}
		}
	}

}
