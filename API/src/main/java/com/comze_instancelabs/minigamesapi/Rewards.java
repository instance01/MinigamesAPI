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

	private int econ_reward = 0;
	private String command = "";
	private ItemStack[] items = null;

	public Rewards(JavaPlugin plugin) {
		this.plugin = plugin;
		economyrewards = plugin.getConfig().getBoolean("config.rewards.economy");
		itemrewards = plugin.getConfig().getBoolean("config.rewards.item_reward");
		commandrewards = plugin.getConfig().getBoolean("config.rewards.command_reward");

		econ_reward = plugin.getConfig().getInt("config.rewards.economy_reward");
		command = plugin.getConfig().getString("config.rewards.command");
		items = Util.parseItems(plugin.getConfig().getString("config.rewards.item_reward_ids")).toArray(new ItemStack[0]);

		if (!MinigamesAPI.economy) {
			economyrewards = false;
		}
	}

	public void giveRewardsToWinners(Arena arena) {
		for (String p_ : arena.getAllPlayers()) {
			giveReward(p_);
		}
	}

	public void giveReward(String p_) {
		if (!MinigamesAPI.getAPI().global_lost.containsKey(p_)) {
			if (Validator.isPlayerOnline(p_)) {
				Player p = Bukkit.getPlayer(p_);
				if (economyrewards) {
					MinigamesAPI.getAPI().econ.depositPlayer(p.getName(), econ_reward);
				}
				if (itemrewards) {
					p.getInventory().addItem(items);
					p.updateInventory();
				}
				if (commandrewards) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<player>", p_));
				}
				//TODO add custom point rewards!
				MinigamesAPI.getAPI().pinstances.get(plugin).getStatsInstance().win(p_, 10);
			}
		}
	}

}
