package com.comze_instancelabs.minigamesapi;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
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
		reloadVariables();

		if (!MinigamesAPI.economy) {
			economyrewards = false;
			kill_economyrewards = false;
		}
	}
	
	public void reloadVariables(){
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

			MinigamesAPI.getAPI().getPluginInstance(plugin).getStatsInstance().win(p_, 10);
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

			MinigamesAPI.getAPI().getPluginInstance(plugin).getStatsInstance().addPoints(p_, reward);
			MinigamesAPI.getAPI().getPluginInstance(plugin).getStatsInstance().addKill(p_);
			MinigamesAPI.getAPI().getPluginInstance(plugin).getSQLInstance().updateWinnerStats(p_, reward, false);
		}
	}

	public void giveAchievementReward(String p_, boolean econ, boolean command, int money_reward, String cmd) {
		if (Validator.isPlayerOnline(p_)) {
			Player p = Bukkit.getPlayer(p_);

			if (econ && MinigamesAPI.economy) {
				MinigamesAPI.getAPI().econ.depositPlayer(p.getName(), money_reward);
			}
			if (command) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<player>", p_));
			}
		}
	}

	public void giveWinReward(String p_, Arena a) {
		giveWinReward(p_, a, 1);
	}

	public void giveWinReward(String p_, Arena a, int global_multiplier) {
		if (Validator.isPlayerOnline(p_)) {
			PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
			final Player p = Bukkit.getPlayer(p_);
			if (!pli.global_lost.containsKey(p_)) {
				String received_rewards_msg = pli.getMessagesConfig().you_received_rewards;
				if (economyrewards && MinigamesAPI.economy) {
					int multiplier = global_multiplier;
					if (pli.getShopHandler().hasItemBought(p_, "coin_boost2_solo")) {
						multiplier = 2;
					}
					if (pli.getShopHandler().hasItemBought(p_, "coin_boost3_solo")) {
						multiplier = 3;
					}
					MinigamesAPI.getAPI().econ.depositPlayer(p.getName(), econ_reward * multiplier);
					received_rewards_msg = received_rewards_msg.replaceAll("<economyreward>", Integer.toString(econ_reward * multiplier) + " " + MinigamesAPI.econ.currencyNamePlural());
				} else {
					received_rewards_msg = received_rewards_msg.replaceAll("<economyreward>", "");
				}
				if (itemrewards) {
					p.getInventory().addItem(items);
					p.updateInventory();
					String items_str = "";
					for (ItemStack i : items) {
						items_str += Integer.toString(i.getAmount()) + " " + Character.toUpperCase(i.getType().toString().charAt(0)) + i.getType().toString().toLowerCase().substring(1) + ", ";
					}
					if (items_str.length() > 2) {
						items_str = items_str.substring(0, items_str.length() - 2);
					}
					if (economyrewards && MinigamesAPI.economy) {
						received_rewards_msg += pli.getMessagesConfig().you_received_rewards_2;
						received_rewards_msg += pli.getMessagesConfig().you_received_rewards_3.replaceAll("<itemreward>", items_str);
					} else {
						received_rewards_msg += pli.getMessagesConfig().you_received_rewards_3.replaceAll("<itemreward>", items_str);
					}
				} else {
					received_rewards_msg += pli.getMessagesConfig().you_received_rewards_3.replaceAll("<itemreward>", "");
				}
				if (commandrewards) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<player>", p_));
				}

				pli.getStatsInstance().win(p_, 10);

				try {
					if (plugin.getConfig().getBoolean("config.broadcast_win")) {
						String msgs[] = pli.getMessagesConfig().server_broadcast_winner.replaceAll("<player>", p_).replaceAll("<arena>", a.getInternalName()).split(";");
						for (String msg : msgs) {
							Bukkit.getServer().broadcastMessage(msg);
						}
					} else {
						String msgs[] = pli.getMessagesConfig().server_broadcast_winner.replaceAll("<player>", p_).replaceAll("<arena>", a.getInternalName()).split(";");
						for (String msg : msgs) {
							p.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					System.out.println("Could not find arena for broadcast.");
				}

				Util.sendMessage(plugin, p, pli.getMessagesConfig().you_won);
				Util.sendMessage(plugin, p, received_rewards_msg);

				if (plugin.getConfig().getBoolean("config.spawn_fireworks_for_winners")) {
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						public void run() {
							Util.spawnFirework(p);
						}
					}, 20L);
				}
			} else {
				Util.sendMessage(plugin, p, MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().you_lost);
				MinigamesAPI.getAPI().getPluginInstance(plugin).getStatsInstance().lose(p_);
			}
		}
	}

}
