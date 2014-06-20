package com.comze_instancelabs.minigamesapi.commands;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;

public class CommandHandler {

	/**
	 * Handles the default commands needed for arena management.
	 * 
	 * @param uber_permission
	 *            Main setup permission. Example: Skywars.setup
	 * @param cmd
	 *            The command. Example: /sw
	 * @param sender
	 * @param args
	 * @return
	 */
	public static boolean handleArgs(JavaPlugin plugin, String uber_permission, String cmd, CommandSender sender, String args[]) {
		if (args.length > 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Please execute this command ingame.");
				return true;
			}
			Player p = (Player) sender;
			String action = args[0];
			if (action.equalsIgnoreCase("setspawn")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 1) {
					ArenaSetup.setSpawn(plugin, args[1], p.getLocation());
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "spawn"));
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			} else if (action.equalsIgnoreCase("setlobby")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 1) {
					ArenaSetup.setLobby(plugin, args[1], p.getLocation());
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "waiting lobby"));
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			} else if (action.equalsIgnoreCase("setmainlobby")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				ArenaSetup.setMainLobby(plugin, p.getLocation());
				sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "main lobby"));
			} else if (action.equalsIgnoreCase("setbounds")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 2) {
					if (args[2].equalsIgnoreCase("low")) {
						ArenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true);
					}else if(args[2].equalsIgnoreCase("high")){
						ArenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false);
					}else{
						sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
						return true;
					}
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "boundary"));
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
				}
			} else if (action.equalsIgnoreCase("savearena")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 1) {
					Arena temp = ArenaSetup.saveArena(plugin, args[1]);
					if (temp != null) {
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_saved_arena.replaceAll("<arena>", args[1]));
					} else {
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().failed_saving_arena.replaceAll("<arena>", args[1]));
					}
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			} else if (action.equalsIgnoreCase("setmaxplayers")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 2) {
					if (!Util.isNumeric(args[2])) {
						return true;
					}
					ArenaSetup.setPlayerCount(plugin, args[1], Integer.parseInt(args[2]), true);
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "max players"));
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
				}
			} else if (action.equalsIgnoreCase("setminplayers")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 2) {
					if (!Util.isNumeric(args[2])) {
						return true;
					}
					ArenaSetup.setPlayerCount(plugin, args[1], Integer.parseInt(args[2]), false);
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "min players"));
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
				}
			} else if (action.equalsIgnoreCase("setarenavip")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 2) {
					if (!args[2].equalsIgnoreCase("true") || !args[2].equalsIgnoreCase("false")) {
						return true;
					}
					ArenaSetup.setArenaVIP(plugin, args[1], Boolean.parseBoolean(args[2]));
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_set.replaceAll("<component>", "vip value"));
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
				}
			} else if (action.equalsIgnoreCase("join")) {
				if (args.length > 1) {
					Arena temp = MinigamesAPI.getAPI().pinstances.get(plugin).getArenaByName(args[1]);
					if (temp != null) {
						temp.joinPlayerLobby(p.getName());
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "joined"));
					} else {
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
					}
				} else {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
				}
			} else if (action.equalsIgnoreCase("leave")) {
				if (MinigamesAPI.getAPI().global_players.containsKey(p.getName())) {
					MinigamesAPI.getAPI().global_players.get(p.getName()).leavePlayer(p.getName(), false);
				} else {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().not_in_arena);
				}
			} else if (action.equalsIgnoreCase("start")) {
				if (!sender.hasPermission(uber_permission + ".start")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 1) {
					Arena temp = MinigamesAPI.getAPI().pinstances.get(plugin).getArenaByName(args[1]);
					if (temp != null) {
						temp.start();
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "started"));
					} else {
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
					}
				} else {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
				}
			} else if (action.equalsIgnoreCase("stop")) {
				if (!sender.hasPermission(uber_permission + ".stop")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 1) {
					Arena temp = MinigamesAPI.getAPI().pinstances.get(plugin).getArenaByName(args[1]);
					if (temp != null) {
						temp.stop();
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "stopped"));
					} else {
						sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
					}
				} else {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
				}
			} else if (action.equalsIgnoreCase("removearena")) {
				if (!sender.hasPermission(uber_permission + ".setup")) {
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if (args.length > 1) {
					MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + args[1], null);
					MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
					// TODO remove arena file if present
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			} else if (action.equalsIgnoreCase("help")) {
				sendHelp(cmd, sender);
			} else if (action.equalsIgnoreCase("list")) {
				sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Arenas" + ChatColor.DARK_GRAY + " -------");
				for (Arena a : MinigamesAPI.getAPI().pinstances.get(plugin).getArenas()) {
					sender.sendMessage(ChatColor.GREEN + a.getName());
				}
			} else if (action.equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().reloadConfig();
				MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().reloadConfig();
				sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_reloaded);
			}
		} else {
			sendHelp(cmd, sender);
		}
		return true;
	}

	public static LinkedHashMap<String, String> cmddesc;
	static {
		cmddesc = new LinkedHashMap<String, String>();
		cmddesc.put("", "");
		cmddesc.put("setspawn <arena>", "Sets the spawn point.");
		cmddesc.put("setlobby <arena>", "Sets the lobby point.");
		cmddesc.put("setmainlobby", "Sets the main lobby point.");
		cmddesc.put("setbounds <arena> <low/high>", "Sets the low or high boundary point for later arena regeneration.");
		cmddesc.put("savearena <arena>", "Saves the arena.");
		cmddesc.put(" ", "");
		cmddesc.put("setmaxplayers <arena> <count>", "Sets the max players allowed to join to given count.");
		cmddesc.put("setminplayers <arena> <count>", "Sets the min players needed to start to given count.");
		cmddesc.put("setarenavip <arena> <true/false>", "Sets whether arena needs permission to join.");
		cmddesc.put("removearena <arena>", "Deletes an arena from config.");
		cmddesc.put("join <arena>", "Joins the arena.");
		cmddesc.put("leave", "Leaves the arena.");
		cmddesc.put("start <arena>", "Force-starts the arena.");
		cmddesc.put("stop <arena>", "Force-stop the arena.");
		cmddesc.put("list", "Lists all arenas.");
		cmddesc.put("reload", "Reloads the config.");
	}

	public static void sendHelp(String cmd, CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Help" + ChatColor.DARK_GRAY + " -------");
		for (String k : cmddesc.keySet()) {
			if (k.length() < 3) {
				sender.sendMessage("");
				continue;
			}
			String v = cmddesc.get(k);
			sender.sendMessage(ChatColor.DARK_AQUA + cmd + " " + k + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + v);
		}
	}

}
