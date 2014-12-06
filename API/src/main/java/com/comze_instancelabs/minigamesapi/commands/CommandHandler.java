package com.comze_instancelabs.minigamesapi.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaPlayer;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.Party;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

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
	public boolean handleArgs(JavaPlugin plugin, String uber_permission, String cmd, CommandSender sender, String args[]) {
		if (args.length > 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Please execute this command ingame.");
				return true;
			}
			Player p = (Player) sender;
			PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
			String action = args[0];
			if (action.equalsIgnoreCase("setspawn")) {
				return this.setSpawn(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setspecspawn")) {
				return this.setSpecSpawn(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setlobby")) {
				return this.setLobby(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setmainlobby")) {
				return this.setMainLobby(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setbounds")) {
				return this.setBounds(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setlobbybounds")) {
				return this.setLobbyBounds(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setspecbounds")) {
				return this.setSpecBounds(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("savearena") || action.equalsIgnoreCase("save")) {
				return this.saveArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setmaxplayers")) {
				return this.setMaxPlayers(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setminplayers")) {
				return this.setMinPlayers(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setarenavip") || action.equalsIgnoreCase("setvip")) {
				return this.setArenaVIP(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("join")) {
				return this.joinArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("leave")) {
				return this.leaveArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("start")) {
				return this.startArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("stop")) {
				return this.stopArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("stopall")) {
				return this.stopAllArenas(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("removearena")) {
				return this.removeArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("removespawn")) {
				return this.removeSpawn(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setskull")) {
				return this.setSkull(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setenabled")) {
				return this.setEnabled(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setshowscoreboard")) {
				return this.setShowScoreboard(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("reset")) {
				return this.resetArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setauthor")) {
				return this.setAuthor(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setdescription")) {
				return this.setDescription(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("setdisplayname")) {
				return this.setArenaDisplayName(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("kit")) {
				return this.setKit(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("spectate")) {
				return this.spectate(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("shop")) {
				return this.openShop(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("leaderboards") || action.equalsIgnoreCase("lb")) {
				return this.getLeaderboards(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("stats")) {
				return this.getStats(pli, sender, args, uber_permission, cmd, action, plugin, p);
			} else if (action.equalsIgnoreCase("help")) {
				sendHelp(cmd, sender);
			} else if (action.equalsIgnoreCase("list")) {
				sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Arenas" + ChatColor.DARK_GRAY + " -------");
				for (Arena a : pli.getArenas()) {
					if (args.length > 1) {
						sender.sendMessage(ChatColor.GREEN + a.getInternalName() + "[" + a.getClass().getSimpleName().toString() + "]");
					} else {
						sender.sendMessage(ChatColor.GREEN + a.getInternalName());
					}
				}
			} else if (action.equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				pli.getMessagesConfig().reloadConfig();
				pli.getArenasConfig().reloadConfig();
				pli.getClassesConfig().reloadConfig();
				pli.getAchievementsConfig().reloadConfig();
				pli.getStatsConfig().reloadConfig();
				pli.getShopConfig().reloadConfig();
				pli.getMessagesConfig().init();
				pli.reloadVariables();
				pli.getRewardsInstance().reloadVariables();
				try {
					pli.reloadAllArenas();
				} catch (Exception e) {
					System.out.println("Looks like one arena is invalid, but most arenas should be reloaded just fine. " + e.getMessage());
				}
				sender.sendMessage(pli.getMessagesConfig().successfully_reloaded);
			} else {
				boolean cont = false;
				ArrayList<String> cmds = new ArrayList<String>();
				for (String cmd_ : cmddesc.keySet()) {
					if (cmd_.toLowerCase().contains(action.toLowerCase())) {
						cmds.add(cmd_);
						cont = true;
					}
				}
				if (cont) {
					sendHelp(cmd, sender);
					for (String cmd_ : cmds) {
						sender.sendMessage(ChatColor.RED + "Did you mean " + ChatColor.DARK_RED + cmd + " " + cmd_ + ChatColor.RED + "?");
					}
				}
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
		cmddesc.put("removespawn <arena> <count>", "Deletes a spawn from config.");
		cmddesc.put("setenabled", "Enables/Disables the arena.");
		cmddesc.put("join <arena>", "Joins the arena.");
		cmddesc.put("leave", "Leaves the arena.");
		cmddesc.put("start <arena>", "Forces the arena to start.");
		cmddesc.put("stop <arena>", "Forces the arena to stop.");
		cmddesc.put("list", "Lists all arenas.");
		cmddesc.put("reload", "Reloads the config.");
		cmddesc.put("reset <arena>", "Forces the arena to reset.");
		cmddesc.put("setlobbybounds <arena> <low/high>", "Optional: Set lobby boundaries.");
		cmddesc.put("setspecbounds <arena> <low/high>", "Optional: Set extra spectator boundaries.");
		cmddesc.put("setauthor <arena> <author>", "Will always display the author of the map at join.");
		cmddesc.put("setdescription <arena> <description>", "Will always display a description of the map at join.");
		cmddesc.put("setdisplayname <arena> <displayname>", "Allows changing displayname of an arena (whitespaces and colors).");
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

	public static LinkedHashMap<String, String> cmdpartydesc;
	static {
		cmdpartydesc = new LinkedHashMap<String, String>();
		cmdpartydesc.put("", "");
		cmdpartydesc.put("invite <player>", "Invites a player to your party and creates one if you don't have one yet.");
		cmdpartydesc.put("accept <player>", "Accepts an invitation to a party");
		cmdpartydesc.put("disband", "Disbands the party");
		cmdpartydesc.put("kick <player>", "Kicks a player from your party.");
		cmdpartydesc.put("leave", "Leaves a party you're in.");
		cmdpartydesc.put("list", "Lists all players and the owner of the party.");
	}

	public static void sendPartyHelp(String cmd, CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Help" + ChatColor.DARK_GRAY + " -------");
		for (String k : cmdpartydesc.keySet()) {
			if (k.length() < 3) {
				sender.sendMessage("");
				continue;
			}
			String v = cmdpartydesc.get(k);
			sender.sendMessage(ChatColor.DARK_AQUA + cmd + " " + k + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + v);
		}
	}

	public boolean setSpawn(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			pli.arenaSetup.setSpawn(plugin, args[1], p.getLocation());
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spawn"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean setSpecSpawn(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			Util.saveComponentForArena(plugin, args[1], "specspawn", p.getLocation());
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spectator spawn"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean setLobby(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			pli.arenaSetup.setLobby(plugin, args[1], p.getLocation());
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "waiting lobby"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean setMainLobby(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		pli.arenaSetup.setMainLobby(plugin, p.getLocation());
		sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "main lobby"));
		return true;
	}

	public boolean setBounds(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("low")) {
				pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true);
			} else if (args[2].equalsIgnoreCase("high")) {
				pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false);
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
				return true;
			}
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "boundary"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
		}
		return true;
	}

	public boolean setLobbyBounds(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("low")) {
				pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true, "lobbybounds");
			} else if (args[2].equalsIgnoreCase("high")) {
				pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false, "lobbybounds");
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
				return true;
			}
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "lobby boundary"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
		}
		return true;
	}

	public boolean setSpecBounds(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("low")) {
				pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true, "specbounds");
			} else if (args[2].equalsIgnoreCase("high")) {
				pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false, "specbounds");
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
				return true;
			}
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spectator boundary"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
		}
		return true;
	}

	public boolean saveArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			Arena temp = pli.arenaSetup.saveArena(plugin, args[1]);
			if (temp != null) {
				sender.sendMessage(pli.getMessagesConfig().successfully_saved_arena.replaceAll("<arena>", args[1]));
			} else {
				sender.sendMessage(pli.getMessagesConfig().failed_saving_arena.replaceAll("<arena>", args[1]));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean setMaxPlayers(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (!Util.isNumeric(args[2])) {
				return true;
			}
			pli.arenaSetup.setPlayerCount(plugin, args[1], Integer.parseInt(args[2]), true);
			if (pli.getArenaByName(args[1]) != null) {
				pli.getArenaByName(args[1]).setMaxPlayers(Integer.parseInt(args[2]));
			}
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "max players"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
		}
		return true;
	}

	public boolean setMinPlayers(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (!Util.isNumeric(args[2])) {
				return true;
			}
			pli.arenaSetup.setPlayerCount(plugin, args[1], Integer.parseInt(args[2]), false);
			if (pli.getArenaByName(args[1]) != null) {
				pli.getArenaByName(args[1]).setMinPlayers(Integer.parseInt(args[2]));
			}
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "min players"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
		}
		return true;
	}

	public boolean setArenaVIP(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
				return true;
			}
			pli.arenaSetup.setArenaVIP(plugin, args[1], Boolean.parseBoolean(args[2]));
			if (pli.getArenaByName(args[1]) != null) {
				pli.getArenaByName(args[1]).setVIPArena(Boolean.parseBoolean(args[2]));
			}
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "vip value"));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
		}
		return true;
	}

	public boolean joinArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (args.length > 1) {
			String playername = p.getName();
			if (args.length > 2) {
				if (Validator.isPlayerOnline(args[2])) {
					playername = args[2];
				}
			}
			Arena temp = pli.getArenaByName(args[1]);
			if (temp != null) {
				if (!temp.containsPlayer(playername)) {
					temp.joinPlayerLobby(playername);
				} else {
					sender.sendMessage(pli.getMessagesConfig().you_already_are_in_arena.replaceAll("<arena>", temp.getInternalName()));
				}
			} else {
				sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
			}
		} else {
			Arena a_ = null;
			for (Arena a : pli.getArenas()) {
				if (a.getArenaState() != ArenaState.INGAME) {
					a_ = a;
				}
			}
			if (a_ != null) {
				a_.joinPlayerLobby(p.getName());
			} else {
				sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
			}
		}
		return true;
	}

	public boolean leaveArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (pli.global_players.containsKey(p.getName())) {
			String playername = p.getName();
			if (args.length > 1) {
				if (Validator.isPlayerOnline(args[1])) {
					playername = args[1];
				}
			}
			Arena a = pli.global_players.get(playername);
			if (a.getArcadeInstance() != null) {
				a.getArcadeInstance().leaveArcade(playername, true);
			}
			a.leavePlayer(playername, false, false);
		} else {
			sender.sendMessage(pli.getMessagesConfig().not_in_arena);
		}
		return true;
	}

	public boolean startArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".start")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			Arena temp = pli.getArenaByName(args[1]);
			if (temp != null) {
				temp.start(true);
				sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "started"));
			} else {
				sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
			}
		} else {
			sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
		}
		return true;
	}

	public boolean stopArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".stop")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			Arena temp = pli.getArenaByName(args[1]);
			if (temp != null) {
				temp.stop();
				sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "stopped"));
			} else {
				sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
			}
		} else {
			sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
		}
		return true;
	}

	public boolean stopAllArenas(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".stop")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		for (Arena a : pli.getArenas()) {
			a.stop();
		}
		return true;
	}

	public boolean removeArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			pli.getArenasConfig().getConfig().set("arenas." + args[1], null);
			pli.getArenasConfig().saveConfig();
			if (pli.removeArena(pli.getArenaByName(args[1]))) {
				sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "removed"));
			} else {
				sender.sendMessage(pli.getMessagesConfig().failed_removing_arena.replaceAll("<arena>", args[1]));
			}
			// TODO remove arena file if present
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean removeSpawn(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (Util.isNumeric(args[2])) {
				if (pli.arenaSetup.removeSpawn(plugin, args[1], Integer.parseInt(args[2]))) {
					sender.sendMessage(pli.getMessagesConfig().successfully_removed.replaceAll("<component>", "spawn " + args[2]));
				} else {
					sender.sendMessage(pli.getMessagesConfig().failed_removing_component.replaceAll("<component>", "spawn " + args[2]).replaceAll("<cause>", "Possibly the provided count couldn't be found: " + args[2]));
				}
			} else {

			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
		}
		return true;
	}

	public boolean setSkull(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			if (Util.isNumeric(args[1])) {
				int count = Integer.parseInt(args[1]);
				p.getInventory().addItem(pli.getStatsInstance().giveSkull(args[1]));
				p.updateInventory();
				pli.getStatsInstance().skullsetup.add(p.getName());
			} else {

			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <count>");
		}
		return true;
	}

	public boolean setEnabled(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
				pli.arenaSetup.setArenaEnabled(plugin, args[1], Boolean.parseBoolean(args[2]));
				sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "enabled state"));
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
		}
		return true;
	}

	public boolean setShowScoreboard(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
				pli.arenaSetup.setShowScoreboard(plugin, args[1], Boolean.parseBoolean(args[2]));
				sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "scoreboard state"));
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
		}
		return true;
	}

	public boolean resetArena(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".reset")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			final Arena a = pli.getArenaByName(args[1]);
			if (a != null) {
				if (Validator.isArenaValid(plugin, a)) {
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						public void run() {
							Util.loadArenaFromFileSYNC(plugin, a);
						}
					});
					sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "reset"));
				}
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean setAuthor(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			String author = args[2];
			if (Validator.isArenaValid(plugin, args[1])) {
				pli.getArenasConfig().getConfig().set("arenas." + args[1] + ".author", author);
				pli.getArenasConfig().saveConfig();
				sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "author"));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <author>");
		}
		return true;
	}

	public boolean setDescription(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			String desc = args[2];
			if (Validator.isArenaValid(plugin, args[1])) {
				pli.getArenasConfig().getConfig().set("arenas." + args[1] + ".description", desc);
				pli.getArenasConfig().saveConfig();
				sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "description"));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <description>");
		}
		return true;
	}

	public boolean setArenaDisplayName(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			String displayname = args[2];
			if (Validator.isArenaValid(plugin, args[1])) {
				pli.getArenasConfig().getConfig().set("arenas." + args[1] + ".displayname", displayname);
				pli.getArenasConfig().saveConfig();
				pli.reloadArena(args[1]);
				sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "displayname"));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <displayname>");
		}
		return true;
	}

	public boolean spectate(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (args.length > 0) {
			String playername = p.getName();
			if (args.length > 2) {
				if (Validator.isPlayerOnline(args[2])) {
					playername = args[2];
				}
			}
			Arena temp = pli.getArenaByName(args[1]);
			if (temp != null) {
				if (temp.getArenaState() == ArenaState.INGAME) {
					if (!temp.containsPlayer(playername)) {
						temp.addPlayer(playername);
						ArenaPlayer ap = ArenaPlayer.getPlayerInstance(playername);
						ap.setNoReward(true);
						ap.setInventories(p.getInventory().getContents(), p.getInventory().getArmorContents());
						ap.setOriginalGamemode(p.getGameMode());
						ap.setOriginalXplvl(p.getLevel());
						pli.global_players.put(playername, temp);
						temp.spectateGame(playername);
					} else {
						sender.sendMessage(pli.getMessagesConfig().you_already_are_in_arena.replaceAll("<arena>", temp.getInternalName()));
					}
				}
			} else {
				sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
			}
		}
		return true;
	}

	public boolean setKit(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 1) {
			if (!plugin.getConfig().getBoolean("config.classes_enabled")) {
				return true;
			}
			if (!plugin.getConfig().getBoolean("config.allow_classes_selection_out_of_arenas")) {
				if (pli.global_players.containsKey(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					if (a.getArenaState() == ArenaState.INGAME) {
						return true;
					}
				} else {
					sender.sendMessage(pli.getMessagesConfig().not_in_arena);
					return true;
				}
			}

			String kit = args[1];
			AClass ac = pli.getClassesHandler().getClassByInternalname(kit);
			if (ac != null) {
				if (pli.getAClasses().containsKey(ac.getName())) {
					if (ac.isEnabled()) {
						pli.getClassesHandler().setClass(kit, p.getName(), true);
						return true;
					}
				}
			}

			String all = "";
			for (AClass k : pli.getAClasses().values()) {
				if (k.isEnabled()) {
					all += k.getInternalName() + ", ";
				}
			}
			if (all.length() < 2) {
				all = "No kits found!  ";
			}
			all = all.substring(0, all.length() - 2);
			sender.sendMessage(pli.getMessagesConfig().possible_kits + all);

		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <kit>");
			if (!plugin.getConfig().getBoolean("config.classes_enabled")) {
				return true;
			}
			if (pli.global_players.containsKey(p.getName())) {
				pli.getClassesHandler().openGUI(p.getName());
			} else {
				sender.sendMessage(pli.getMessagesConfig().not_in_arena);
			}
		}
		return true;
	}

	public boolean openShop(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 1) {
			if (!plugin.getConfig().getBoolean("config.shop_enabled")) {
				return true;
			}
			if (pli.global_players.containsKey(p.getName())) {
				String shop_item = args[1];
				if (!pli.getShopHandler().buyByInternalName(p, shop_item)) {
					String all = "";
					for (String ac : pli.getShopHandler().shopitems.keySet()) {
						all += ac + ", ";
					}
					if (all.length() < 2) {
						all = "No shop items found!  ";
					}
					all = all.substring(0, all.length() - 2);
					sender.sendMessage(pli.getMessagesConfig().possible_shopitems + all);
				}
			} else {
				sender.sendMessage(pli.getMessagesConfig().not_in_arena);
			}
		} else {
			if (!plugin.getConfig().getBoolean("config.shop_enabled")) {
				return true;
			}
			if (pli.global_players.containsKey(p.getName())) {
				pli.getShopHandler().openGUI(p.getName());
			} else {
				sender.sendMessage(pli.getMessagesConfig().not_in_arena);
			}
		}
		return true;
	}

	public boolean getStats(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		Util.sendStatsMessage(pli, p);
		// sender.sendMessage(ChatColor.DARK_GREEN + "--- Stats ---");
		// sender.sendMessage(ChatColor.DARK_AQUA + "Wins - " + pli.getStatsInstance().getWins(p.getName()));
		// sender.sendMessage(ChatColor.DARK_AQUA + "Kills - " + pli.getStatsInstance().getKills(p.getName()));
		// sender.sendMessage(ChatColor.DARK_AQUA + "Points - " + pli.getStatsInstance().getPoints(p.getName()));
		return true;
	}

	public boolean getLeaderboards(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		boolean wins = true;
		int count = 10;
		if (args.length > 2) {
			if (Util.isNumeric(args[2])) {
				count = Integer.parseInt(args[2]);
				if (!args[1].equalsIgnoreCase("wins")) {
					wins = false;
				}
			} else {
				if (Util.isNumeric(args[1])) {
					count = Integer.parseInt(args[1]);
				}
				if (!args[2].equalsIgnoreCase("wins")) {
					wins = false;
				}
			}
		} else if (args.length > 1 && args.length < 3) {
			if (Util.isNumeric(args[1])) {
				count = Integer.parseInt(args[1]);
			} else {
				if (!args[1].equalsIgnoreCase("wins")) {
					wins = false;
				}
			}
		}
		sendLeaderboards(pli, sender, count, wins);
		return true;
	}

	/**
	 * Send the leaderboards to a player
	 * 
	 * @param sender
	 *            Player to send the leaderboards to
	 * @param count
	 *            Amount of result items to show
	 * @param wins
	 *            whether to check for wins or points leaderboards
	 */
	private void sendLeaderboards(PluginInstance pli, CommandSender sender, int count, boolean wins) {
		int c = 0;
		if (wins) {
			sender.sendMessage(ChatColor.DARK_GREEN + "--- Leaderboards: Wins ---");
			TreeMap<String, Double> sorted_wins = pli.getStatsInstance().getTop(count, true);
			for (Map.Entry<String, Double> entry : sorted_wins.entrySet()) {
				c++;
				if (c > count) {
					break;
				}
				sender.sendMessage(ChatColor.GREEN + "" + Integer.toString((int) entry.getValue().doubleValue()) + ChatColor.DARK_GREEN + " - " + ChatColor.GREEN + "" + entry.getKey());
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GREEN + "-- Leaderboards: Points --");
			TreeMap<String, Double> sorted_wins = pli.getStatsInstance().getTop(count, false);
			for (Map.Entry<String, Double> entry : sorted_wins.entrySet()) {
				c++;
				if (c > count) {
					break;
				}
				sender.sendMessage(ChatColor.GREEN + "" + Integer.toString((int) entry.getValue().doubleValue()) + ChatColor.DARK_GREEN + " - " + ChatColor.GREEN + "" + entry.getKey());
			}
		}
	}

	// Party commands
	public boolean partyInvite(CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 1) {
			if (p.getName().equalsIgnoreCase(args[1])) {
				p.sendMessage(MinigamesAPI.getAPI().partymessages.cannot_invite_yourself);
				return true;
			}
			boolean isInParty = false;
			for (Party party : MinigamesAPI.getAPI().global_party.values()) {
				if (party.containsPlayer(p.getName())) {
					isInParty = true;
				}
			}
			if (!isInParty) {
				if (!Validator.isPlayerOnline(args[1])) {
					p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
					return true;
				}
				Party party = null;
				if (!MinigamesAPI.getAPI().global_party.containsKey(p.getName())) {
					party = new Party(p.getName());
					MinigamesAPI.getAPI().global_party.put(p.getName(), party);
				} else {
					party = MinigamesAPI.getAPI().global_party.get(p.getName());
				}
				ArrayList<Party> parties = new ArrayList<Party>();
				if (MinigamesAPI.getAPI().global_party_invites.containsKey(p.getName())) {
					parties.addAll(MinigamesAPI.getAPI().global_party_invites.get(p.getName()));
				}
				if (!parties.contains(party)) {
					parties.add(party);
				}
				MinigamesAPI.getAPI().global_party_invites.put(args[1], parties);
				p.sendMessage(MinigamesAPI.getAPI().partymessages.you_invited.replaceAll("<player>", args[1]));
				Bukkit.getPlayer(args[1]).sendMessage(MinigamesAPI.getAPI().partymessages.you_were_invited.replaceAll("<player>", p.getName()));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <player>");
		}
		return true;
	}

	public boolean partyAccept(CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 1) {
			if (!Validator.isPlayerOnline(args[1])) {
				p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
				return true;
			}
			if (!MinigamesAPI.getAPI().global_party_invites.containsKey(p.getName())) {
				p.sendMessage(MinigamesAPI.getAPI().partymessages.not_invited_to_any_party);
				return true;
			}

			boolean isInParty = false;
			Party party_ = null;
			for (Party party : MinigamesAPI.getAPI().global_party.values()) {
				if (party.containsPlayer(p.getName())) {
					isInParty = true;
					party_ = party;
				}
			}
			if (isInParty) {
				if (party_ != null) {
					party_.removePlayer(p.getName());
				}
			}
			if (MinigamesAPI.getAPI().global_party.containsKey(p.getName())) {
				MinigamesAPI.getAPI().global_party.get(p.getName()).disband();
			}

			Party party__ = null;
			for (Party party : MinigamesAPI.getAPI().global_party_invites.get(p.getName())) {
				if (party.getOwner().equalsIgnoreCase(args[1])) {
					party__ = party;
					break;
				}
			}
			if (party__ != null) {
				party__.addPlayer(p.getName());
				MinigamesAPI.getAPI().global_party_invites.remove(p.getName());
			} else {
				p.sendMessage(MinigamesAPI.getAPI().partymessages.not_invited_to_players_party.replaceAll("<player>", args[1]));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <player>");
		}
		return true;
	}

	public boolean partyKick(CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 1) {
			if (!Validator.isPlayerOnline(args[1])) {
				p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
				return true;
			}
			if (MinigamesAPI.getAPI().global_party.containsKey(p.getName())) {
				Party party = MinigamesAPI.getAPI().global_party.get(p.getName());
				if (party.containsPlayer(args[1])) {
					party.removePlayer(args[1]);
				} else {
					p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_in_party.replaceAll("<player>", args[1]));
				}
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <player>");
		}
		return true;
	}

	public boolean partyList(CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 0) {
			Party party_ = null;
			for (Party party : MinigamesAPI.getAPI().global_party.values()) {
				if (party.containsPlayer(p.getName())) {
					party_ = party;
				}
			}
			if (MinigamesAPI.getAPI().global_party.containsKey(p.getName())) {
				party_ = MinigamesAPI.getAPI().global_party.get(p.getName());
			}
			if (party_ != null) {
				String ret = ChatColor.DARK_GREEN + party_.getOwner();
				for (String p_ : party_.getPlayers()) {
					ret += ChatColor.GREEN + ", " + p_;
				}
				p.sendMessage(ret);
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action);
		}
		return true;
	}

	public boolean partyDisband(CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 0) {
			if (MinigamesAPI.getAPI().global_party.containsKey(p.getName())) {
				MinigamesAPI.getAPI().global_party.get(p.getName()).disband();
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action);
		}
		return true;
	}

	public boolean partyLeave(CommandSender sender, String[] args, String uber_permission, String cmd, String action, final JavaPlugin plugin, Player p) {
		if (args.length > 0) {
			if (MinigamesAPI.getAPI().global_party.containsKey(p.getName())) {
				MinigamesAPI.getAPI().global_party.get(p.getName()).disband();
				return true;
			}
			Party party_ = null;
			for (Party party : MinigamesAPI.getAPI().global_party.values()) {
				if (party.containsPlayer(p.getName())) {
					party_ = party;
				}
			}
			if (party_ != null) {
				party_.removePlayer(p.getName());
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action);
		}
		return true;
	}

}
