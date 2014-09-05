package com.comze_instancelabs.minigamesapi;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Rewards {

	private JavaPlugin plugin = null;

	private boolean economyrewards;
	private boolean itemrewards;
	private boolean commandrewards;
	private boolean kill_economyrewards;
	private boolean kill_commandrewards;

	private int econ_reward = 0;
	private int kill_econ_reward = 0;
	private String command = "";
	private String kill_command = "";
	private ItemStack[] items = null;

	public Rewards(JavaPlugin plugin) {
		this.plugin = plugin;
		economyrewards = plugin.getConfig().getBoolean("config.rewards.economy");
		itemrewards = plugin.getConfig().getBoolean("config.rewards.item_reward");
		commandrewards = plugin.getConfig().getBoolean("config.rewards.command_reward");
		kill_economyrewards = plugin.getConfig().getBoolean("config.rewards.economy_for_kills");
		kill_commandrewards = plugin.getConfig().getBoolean("config.rewards.command_reward_for_kills");

		econ_reward = plugin.getConfig().getInt("config.rewards.economy_reward");
		command = plugin.getConfig().getString("config.rewards.command");
		items = Util.parseItems(plugin.getConfig().getString("config.rewards.item_reward_ids")).toArray(new ItemStack[0]);
		kill_econ_reward = plugin.getConfig().getInt("config.rewards.economy_reward_for_kills");
		kill_command = plugin.getConfig().getString("config.rewards.command_for_kills");

		if (!MinigamesAPI.economy) {
			economyrewards = false;
			kill_economyrewards = false;
		}
	}

	public void giveRewardsToWinners(Arena arena) {
		for (String p_ : arena.getAllPlayers()) {
			giveWinReward(p_, arena);
		}
	}

	public void giveReward(String p_) {
		if (Validator.isPlayerOnline(p_)) {
			Player p = Bukkit.getPlayer(p_);

			if (economyrewards) {
				MinigamesAPI.getAPI().econ.depositPlayer(p.getName(), econ_reward);
			}

			MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().win(p_, 10);
		}
	}

	public void giveKillReward(String p_, int reward) {
		if (Validator.isPlayerOnline(p_)) {
			Player p = Bukkit.getPlayer(p_);

			if (kill_economyrewards && MinigamesAPI.economy) {
				MinigamesAPI.getAPI().econ.depositPlayer(p.getName(), kill_econ_reward);
			}
			if (kill_commandrewards) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), kill_command.replaceAll("<player>", p_));
			}

			MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().addPoints(p_, reward);
			MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().updateWinnerStats(p_, reward, false);
		}
	}

	public void giveWinReward(String p_, Arena a) {
		if (Validator.isPlayerOnline(p_)) {
			PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
			Player p = Bukkit.getPlayer(p_);
			if (!pli.global_lost.containsKey(p_)) {
				if (economyrewards && MinigamesAPI.economy) {
					MinigamesAPI.getAPI().econ.depositPlayer(p.getName(), econ_reward);
				}
				if (itemrewards) {
					p.getInventory().addItem(items);
					p.updateInventory();
				}
				if (commandrewards) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<player>", p_));
				}

				pli.getStatsInstance().win(p_, 10);

				Util.sendMessage(p, pli.getMessagesConfig().you_won);

				try {
					if (plugin.getConfig().getBoolean("config.broadcast_win")) {
						plugin.getServer().broadcastMessage(pli.getMessagesConfig().server_broadcast_winner.replaceAll("<player>", p_).replaceAll("<arena>", a.getName()));
					}
				} catch (Exception e) {
					System.out.println("Could not find arena for broadcast.");
				}
			} else {
				Util.sendMessage(p, MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().you_lost);
				MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().lose(p_);
			}
		}
	}

}
