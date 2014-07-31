package com.comze_instancelabs.minigamesapi.guns;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Classes;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.AClass;
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
	public double level_multiplier = 1.5D;

	// attribute base costs
	public int speed_cost = 80;
	public int durability_cost = 60;
	public int shoot_amount_cost = 150;
	public int knockback_multiplier_cost = 190;

	public static void openGUI(final JavaPlugin plugin, String p) {
		IconMenu iconm = new IconMenu("Guns", 36, new IconMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(IconMenu.OptionClickEvent event) {
				String d = event.getName();
				Player p = event.getPlayer();
				if (MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().containsKey(d)) {
					Classes.setClass(plugin, d, p.getName());
				}
				event.setWillClose(true);
			}
		}, plugin);

		int c = 0;
		for (String ac : MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().keySet()) {
			AClass ac_ = MinigamesAPI.getAPI().pinstances.get(plugin).getAClasses().get(ac);
			iconm.setOption(c, ac_.getIcon(), ac, MinigamesAPI.getAPI().pinstances.get(plugin).getClassesConfig().getConfig().getString("config.kits." + ac + ".lore"));
			c++;
		}

		iconm.open(Bukkit.getPlayerExact(p));
	}

	public static void openUpgradeGUI(final JavaPlugin plugin, String p) {
		//TODO
	}

	public static void loadGuns(JavaPlugin plugin) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getGunsConfig().getConfig();
		if (config.isSet("config.guns")) {
			for (String gun : config.getConfigurationSection("config.guns.").getKeys(false)) {
				String path = "config.guns." + gun + ".";
				Gun n = new Gun(plugin, config.getDouble(path + "speed"), config.getInt(path + "durability"), config.getInt(path + "shoot_amount"), config.getDouble(path + "knockback_multiplier"), Egg.class, Util.parseItems(config.getString(path + "items")), Util.parseItems(config.getString(path + "icon")));
				MinigamesAPI.getAPI().pinstances.get(plugin).addGun(gun, n);
			}
		}
	}
}
