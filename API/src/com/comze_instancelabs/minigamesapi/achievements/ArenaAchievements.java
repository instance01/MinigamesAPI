package com.comze_instancelabs.minigamesapi.achievements;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.IconMenu;

public class ArenaAchievements {

	JavaPlugin plugin;
	PluginInstance pli;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();

	public ArenaAchievements(PluginInstance pli, JavaPlugin plugin) {
		this.plugin = plugin;
		this.pli = pli;
	}

	public void openGUI(final String p, boolean sql) {
		IconMenu iconm;
		ArrayList<AAchievement> alist = loadPlayerAchievements(p, sql);
		int mincount = alist.size();
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu(MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().achievement_item, (9 > mincount - 1) ? 9 * 1 : Math.round(mincount / 9) * 9 + 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					event.setWillClose(true);
				}
			}, plugin);
		}

		int c = 0;
		for (AAchievement aa : alist) {
			ItemStack icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
			if (aa.isDone()) {
				icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			}
			iconm.setOption(c, icon, ChatColor.translateAlternateColorCodes('&', pli.getAchievementsConfig().getConfig().getString("config.achievements." + aa.getAchievementNameRaw() + ".name")), "Done: " + aa.isDone());
			c++;
		}

		iconm.open(Bukkit.getPlayerExact(p));
		lasticonm.put(p, iconm);
	}

	public ArrayList<AAchievement> loadPlayerAchievements(String playername, boolean sql) {
		ArrayList<AAchievement> ret = new ArrayList<AAchievement>();
		if (sql) {
			// TODO MySQL Support
		} else {
			for (String achievement : pli.getAchievementsConfig().getConfig().getConfigurationSection("config.achievements").getKeys(false)) {
				AAchievement ac = new AAchievement(achievement, playername, pli.getAchievementsConfig().getConfig().isSet("players." + playername + "." + achievement + ".done") ? pli.getAchievementsConfig().getConfig().getBoolean("players." + playername + "." + achievement + ".done") : false);
				ret.add(ac);
			}
		}
		return ret;
	}

	public void setAchievementDone(String playername, String achievement, boolean sql) {
		if (sql) {
			// TODO
		} else {
			if (!pli.getAchievementsConfig().getConfig().isSet("players." + playername + "." + achievement + ".done")) {
				pli.getAchievementsConfig().getConfig().set("players." + playername + "." + achievement + ".done", true);
				pli.getAchievementsConfig().saveConfig();
				ArrayList<AAchievement> alist = loadPlayerAchievements(playername, sql);
				boolean allDone = true;
				AAchievement a;
				for (AAchievement aac : alist) {
					if (aac.getAchievementNameRaw().equalsIgnoreCase(achievement)) {
						a = aac;
					}
					if (!aac.isDone() && !aac.getAchievementNameRaw().equalsIgnoreCase("achievement_guy")) {
						allDone = false;
					}
				}
				String base = "config.achievements." + achievement;
				pli.getRewardsInstance().giveAchievementReward(playername, pli.getAchievementsConfig().getConfig().getBoolean(base + ".reward.economy_reward"), pli.getAchievementsConfig().getConfig().getBoolean(base + ".reward.command_reward"), pli.getAchievementsConfig().getConfig().getInt(base + ".reward.econ_reward_amount"), pli.getAchievementsConfig().getConfig().getString(base + ".reward.cmd"));
				Bukkit.getPlayer(playername).sendMessage(pli.getMessagesConfig().you_got_the_achievement.replaceAll("<achievement>", ChatColor.translateAlternateColorCodes('&', pli.getAchievementsConfig().getConfig().getString("config.achievements." + achievement + ".name"))));

				if (allDone) {
					setAchievementDone(playername, "achievement_guy", sql);
				}
			}
		}
	}

	public void addDefaultAchievement(String internalname, String name, int default_money_reward) {
		pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".enabled", true);
		pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".name", name);
		pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.economy_reward", true);
		pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.econ_reward_amount", default_money_reward);
		pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.command_reward", false);
		pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.cmd", "tell <player> Good job!");
	}

	public boolean isEnabled() {
		return pli.getAchievementsConfig().getConfig().getBoolean("config.enabled");
	}

	public void setEnabled(boolean t) {
		pli.getAchievementsConfig().getConfig().set("config.enabled", t);
		pli.getAchievementsConfig().saveConfig();
	}

}
