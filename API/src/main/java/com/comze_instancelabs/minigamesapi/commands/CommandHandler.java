package com.comze_instancelabs.minigamesapi.commands;

import java.util.LinkedHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class CommandHandler {
	
	/**
	 * Handles the default commands needed for arena management.
	 * @param uber_permission Main setup permission. Example: Skywars.setup
	 * @param cmd The command. Example: /sw
	 * @param sender
	 * @param args
	 * @return
	 */
	public static boolean handleArgs(JavaPlugin plugin, String uber_permission, String cmd, CommandSender sender, String args[]){
		if(args.length > 0){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player)sender;
			String action = args[0];
			if(action.equalsIgnoreCase("setspawn")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 1){
					ArenaSetup.setSpawn(plugin, args[1], p.getLocation());
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			}else if(action.equalsIgnoreCase("setlobby")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 1){
					ArenaSetup.setLobby(plugin, args[1], p.getLocation());
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			}else if(action.equalsIgnoreCase("setmainlobby")){
				
			}else if(action.equalsIgnoreCase("setbounds")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 2){
					// TODO bounds setup command
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
				}
			}else if(action.equalsIgnoreCase("savearena")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 1){
					ArenaSetup.saveArena(plugin, args[1]);
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			}else if(action.equalsIgnoreCase("setmaxplayers")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 2){
					//TODO set max players
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
				}
			}else if(action.equalsIgnoreCase("setminplayers")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 2){
					//TODO set min players
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
				}
			}else if(action.equalsIgnoreCase("setarenavip")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 2){
					// TODO set arena vip
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
				}
			}else if(action.equalsIgnoreCase("join")){
				
			}else if(action.equalsIgnoreCase("leave")){
				
			}else if(action.equalsIgnoreCase("start")){
				
			}else if(action.equalsIgnoreCase("stop")){
				
			}else if(action.equalsIgnoreCase("removearena")){
				if(!sender.hasPermission(uber_permission + ".setup")){
					sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().no_perm);
					return true;
				}
				if(args.length > 1){
					//TODO remove arena
				}else{
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
				}
			}else if(action.equalsIgnoreCase("help")){
				// show help
				sendHelp(cmd, sender);
			}else if(action.equalsIgnoreCase("list")){
				
			}else if(action.equalsIgnoreCase("reload")){
				MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().reloadConfig();
				plugin.reloadConfig();
				MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().reloadConfig();
				MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().reloadConfig();
				sender.sendMessage(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().successfully_reloaded);
			}
		}else{
			// show help
			sendHelp(cmd, sender);
		}
		return true;
	}
	
	public static LinkedHashMap<String, String> cmddesc;
    static
    {
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
	
	public static void sendHelp(String cmd, CommandSender sender){
		sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Help" + ChatColor.DARK_GRAY + " -------");
		for(String k : cmddesc.keySet()){
			if(k.length() < 3){
				sender.sendMessage("");
				continue;
			}
			String v = cmddesc.get(k);
			sender.sendMessage(ChatColor.DARK_AQUA + cmd + " " + k + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + v);
		}
	}
	
}
