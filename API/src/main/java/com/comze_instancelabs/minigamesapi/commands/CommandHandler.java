/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.comze_instancelabs.minigamesapi.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaPlayer;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.CommandStrings;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.Party;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.PrivateUtil;
import com.comze_instancelabs.minigamesapi.Stats;
import com.comze_instancelabs.minigamesapi.config.HologramsConfig;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class CommandHandler
{
    
    /**
     * Constructor.
     */
    public CommandHandler()
    {
        this.initCmdDesc();
    }
    
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
    public boolean handleArgs(final JavaPlugin plugin, final String uber_permission, final String cmd, final CommandSender sender, final String args[])
    {
        if (args.length > 0)
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage("Please execute this command ingame.");
                return true;
            }
            final Player p = (Player) sender;
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
            final String action = args[0];
            if (action.equalsIgnoreCase(CommandStrings.GAME_SET_SPAWN))
            {
                return this.setSpawn(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_SPEC_SPAWN))
            {
                return this.setSpecSpawn(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_LOBBY))
            {
                return this.setLobby(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_MAINLOBBY))
            {
                return this.setMainLobby(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_BOUNDS))
            {
                return this.setBounds(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_LOBBY_BOUNDS))
            {
                return this.setLobbyBounds(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_SPEC_BOUNDS))
            {
                return this.setSpecBounds(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SAVE_ARENA) || action.equalsIgnoreCase(CommandStrings.GAME_SAVE))
            {
                return this.saveArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_MAX_PLAYERS))
            {
                return this.setMaxPlayers(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_MIN_PLAYERS))
            {
                return this.setMinPlayers(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_ARENA_VIP) || action.equalsIgnoreCase(CommandStrings.GAME_SET_VIP))
            {
                return this.setArenaVIP(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_JOIN))
            {
                return this.joinArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_LEAVE))
            {
                return this.leaveArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_START))
            {
                return this.startArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_STOP))
            {
                return this.stopArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_STOP_ALL))
            {
                return this.stopAllArenas(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_REMOVE_ARENA))
            {
                return this.removeArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_REMOVE_SPAWN))
            {
                return this.removeSpawn(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_SKULL))
            {
                return this.setSkull(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_ENABLED))
            {
                return this.setEnabled(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_SHOW_SCOREBOARD))
            {
                return this.setShowScoreboard(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_RESET))
            {
                return this.resetArena(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_AUTHOR))
            {
                return this.setAuthor(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_DESCRIPTION))
            {
                return this.setDescription(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_DISPLAYNAME))
            {
                return this.setArenaDisplayName(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_KIT))
            {
                return this.setKit(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SPECTATE))
            {
                return this.spectate(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SHOP))
            {
                return this.openShop(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_LEADER_BOARDS) || action.equalsIgnoreCase(CommandStrings.GAME_LB) || action.equalsIgnoreCase(CommandStrings.GAME_TOP))
            {
                return this.getLeaderboards(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_STATS))
            {
                return this.getStats(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_SET_HOLOGRAM))
            {
                return this.setHologram(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_LIST_HOLOGRAMS))
            {
                return this.listHolograms(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_REMOVE_HOLOGRAM))
            {
                return this.removeHologram(pli, sender, args, uber_permission, cmd, action, plugin, p);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_HELP))
            {
                return this.sendHelp(cmd, sender);
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_LIST))
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Arenas" + ChatColor.DARK_GRAY + " -------");
                for (final Arena a : pli.getArenas())
                {
                    sender.sendMessage(ChatColor.GREEN + a.getInternalName());
                }
            }
            else if (action.equalsIgnoreCase(CommandStrings.GAME_RELOAD))
            {
                if (!sender.hasPermission(uber_permission + ".reload"))
                {
                    sender.sendMessage(pli.getMessagesConfig().no_perm);
                    return true;
                }
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
                pli.getStatsInstance().reloadVariables();
                pli.getAClasses().clear();
                pli.getClassesHandler().loadClasses();
                try
                {
                    pli.reloadAllArenas();
                }
                catch (final Exception e)
                {
                    pli.getPlugin().getLogger().log(Level.WARNING, "Looks like one arena is invalid, but most arenas should be reloaded just fine.", e);
                }
                sender.sendMessage(pli.getMessagesConfig().successfully_reloaded);
            }
            else
            {
                boolean cont = false;
                final ArrayList<String> cmds = new ArrayList<>();
                for (final String cmd_ : this.cmddesc.keySet())
                {
                    if (cmd_.toLowerCase().contains(action.toLowerCase()))
                    {
                        cmds.add(cmd_);
                        cont = true;
                    }
                }
                if (cont)
                {
                    this.sendHelp(cmd, sender);
                    for (final String cmd_ : cmds)
                    {
                        sender.sendMessage(ChatColor.RED + "Did you mean " + ChatColor.DARK_RED + cmd + " " + cmd_ + ChatColor.RED + "?");
                    }
                }
            }
        }
        else
        {
            return this.sendHelp(cmd, sender);
        }
        return true;
    }
    
    protected final LinkedHashMap<String, String> cmddesc = new LinkedHashMap<>();
    
    protected void initCmdDesc()
    {
        this.cmddesc.put("", null);
        this.cmddesc.put("setspawn <arena>", "Sets the spawn point.");
        this.cmddesc.put("setlobby <arena>", "Sets the lobby point.");
        this.cmddesc.put("setmainlobby", "Sets the main lobby point.");
        this.cmddesc.put("setbounds <arena> <low/high>", "Sets the low or high boundary point for later arena regeneration.");
        this.cmddesc.put("savearena <arena>", "Saves the arena.");
        this.cmddesc.put("", null);
        this.cmddesc.put("setmaxplayers <arena> <count>", "Sets the max players allowed to join to given count.");
        this.cmddesc.put("setminplayers <arena> <count>", "Sets the min players needed to start to given count.");
        this.cmddesc.put("setarenavip <arena> <true/false>", "Sets whether arena needs permission to join.");
        this.cmddesc.put("removearena <arena>", "Deletes an arena from config.");
        this.cmddesc.put("removespawn <arena> <count>", "Deletes a spawn from config.");
        this.cmddesc.put("setenabled", "Enables/Disables the arena.");
        this.cmddesc.put("", null);
        this.cmddesc.put("join <arena>", "Joins the arena.");
        this.cmddesc.put("leave", "Leaves the arena.");
        this.cmddesc.put("", "");
        this.cmddesc.put("start <arena>", "Forces the arena to start.");
        this.cmddesc.put("stop <arena>", "Forces the arena to stop.");
        this.cmddesc.put("list", "Lists all arenas.");
        this.cmddesc.put("reload", "Reloads the config.");
        this.cmddesc.put("reset <arena>", "Forces the arena to reset.");
        this.cmddesc.put("", null);
        this.cmddesc.put("setlobbybounds <arena> <low/high>", "Optional: Set lobby boundaries.");
        this.cmddesc.put("setspecbounds <arena> <low/high>", "Optional: Set extra spectator boundaries.");
        this.cmddesc.put("setauthor <arena> <author>", "Will always display the author of the map at join.");
        this.cmddesc.put("setdescription <arena> <description>", "Will always display a description of the map at join.");
        this.cmddesc.put("setdisplayname <arena> <displayname>", "Allows changing displayname of an arena (whitespaces and colors).");
        this.cmddesc.put("", null);
    }
    
    public boolean sendHelp(final String cmd, final CommandSender sender)
    {
        sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Help" + ChatColor.DARK_GRAY + " -------");
        for (final String k : this.cmddesc.keySet())
        {
            if (k.length() < 3)
            {
                sender.sendMessage("");
                continue;
            }
            final String v = this.cmddesc.get(k);
            sender.sendMessage(ChatColor.YELLOW + cmd + " " + k + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + v);
        }
        return true;
    }
    
    public static LinkedHashMap<String, String> cmdpartydesc;
    static
    {
        CommandHandler.cmdpartydesc = new LinkedHashMap<>();
        CommandHandler.cmdpartydesc.put("", "");
        CommandHandler.cmdpartydesc.put("invite <player>", "Invites a player to your party and creates one if you don't have one yet.");
        CommandHandler.cmdpartydesc.put("accept <player>", "Accepts an invitation to a party");
        CommandHandler.cmdpartydesc.put("disband", "Disbands the party");
        CommandHandler.cmdpartydesc.put("kick <player>", "Kicks a player from your party.");
        CommandHandler.cmdpartydesc.put("leave", "Leaves a party you're in.");
        CommandHandler.cmdpartydesc.put("list", "Lists all players and the owner of the party.");
    }
    
    public static void sendPartyHelp(final String cmd, final CommandSender sender)
    {
        sender.sendMessage(ChatColor.DARK_GRAY + "------- " + ChatColor.BLUE + "Help" + ChatColor.DARK_GRAY + " -------");
        for (final String k : CommandHandler.cmdpartydesc.keySet())
        {
            if (k.length() < 3)
            {
                sender.sendMessage("");
                continue;
            }
            final String v = CommandHandler.cmdpartydesc.get(k);
            sender.sendMessage(ChatColor.YELLOW + cmd + " " + k + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + v);
        }
    }
    
    public boolean setSpawn(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            pli.arenaSetup.setSpawn(plugin, args[1], p.getLocation());
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spawn"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
        }
        return true;
    }
    
    public boolean setSpecSpawn(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            Util.saveComponentForArena(plugin, args[1], ArenaConfigStrings.SPEC_SPAWN, p.getLocation());
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spectator spawn"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
        }
        return true;
    }
    
    public boolean setLobby(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            pli.arenaSetup.setLobby(plugin, args[1], p.getLocation());
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "waiting lobby"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
        }
        return true;
    }
    
    public boolean setMainLobby(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        pli.arenaSetup.setMainLobby(plugin, p.getLocation());
        sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "main lobby"));
        return true;
    }
    
    public boolean setBounds(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (args[2].equalsIgnoreCase("low"))
            {
                pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true);
            }
            else if (args[2].equalsIgnoreCase("high"))
            {
                pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false);
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
                return true;
            }
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "boundary"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
        }
        return true;
    }
    
    public boolean setLobbyBounds(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (args[2].equalsIgnoreCase("low"))
            {
                pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true, "lobbybounds");
            }
            else if (args[2].equalsIgnoreCase("high"))
            {
                pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false, "lobbybounds");
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
                return true;
            }
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "lobby boundary"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
        }
        return true;
    }
    
    public boolean setSpecBounds(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (args[2].equalsIgnoreCase("low"))
            {
                pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), true, "specbounds");
            }
            else if (args[2].equalsIgnoreCase("high"))
            {
                pli.arenaSetup.setBoundaries(plugin, args[1], p.getLocation(), false, "specbounds");
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
                return true;
            }
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spectator boundary"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <low/high>");
        }
        return true;
    }
    
    public boolean saveArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            if (Util.getMainLobby(plugin) == null)
            {
                sender.sendMessage(pli.getMessagesConfig().failed_saving_arena.replaceAll("<arena>", args[1]));
                sender.sendMessage(ChatColor.GRAY + "++ Debug Info ++");
                sender.sendMessage(ChatColor.GRAY + "MAIN LOBBY MISSING");
            }
            else
            {
                final Arena temp = pli.arenaSetup.saveArena(plugin, args[1]);
                if (temp != null)
                {
                    sender.sendMessage(pli.getMessagesConfig().successfully_saved_arena.replaceAll("<arena>", args[1]));
                }
                else
                {
                    sender.sendMessage(pli.getMessagesConfig().failed_saving_arena.replaceAll("<arena>", args[1]));
                    sender.sendMessage(ChatColor.GRAY + "++ Debug Info ++");
                    sender.sendMessage(ChatColor.GRAY + "LOBBY:" + Util.isComponentForArenaValidRaw(plugin, args[1], "lobby") + ChatColor.RED + ";" + ChatColor.GRAY + " SPAWN0:"
                            + Util.isComponentForArenaValidRaw(plugin, args[1], "spawns.spawn0") + ChatColor.RED + ";" + ChatColor.GRAY + " BOUNDARIES(possibly needed): low:"
                            + Util.isComponentForArenaValidRaw(plugin, args[1], ArenaConfigStrings.BOUNDS_LOW) + ", high:" + Util.isComponentForArenaValidRaw(plugin, args[1], ArenaConfigStrings.BOUNDS_HIGH));
                }
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
        }
        return true;
    }
    
    public boolean setMaxPlayers(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (!Util.isNumeric(args[2]))
            {
                return true;
            }
            pli.arenaSetup.setPlayerCount(plugin, args[1], Integer.parseInt(args[2]), true);
            if (pli.getArenaByName(args[1]) != null)
            {
                pli.getArenaByName(args[1]).setMaxPlayers(Integer.parseInt(args[2]));
            }
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "max players"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
        }
        return true;
    }
    
    public boolean setMinPlayers(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (!Util.isNumeric(args[2]))
            {
                return true;
            }
            pli.arenaSetup.setPlayerCount(plugin, args[1], Integer.parseInt(args[2]), false);
            if (pli.getArenaByName(args[1]) != null)
            {
                pli.getArenaByName(args[1]).setMinPlayers(Integer.parseInt(args[2]));
            }
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "min players"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
        }
        return true;
    }
    
    public boolean setArenaVIP(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false"))
            {
                return true;
            }
            pli.arenaSetup.setArenaVIP(plugin, args[1], Boolean.parseBoolean(args[2]));
            if (pli.getArenaByName(args[1]) != null)
            {
                pli.getArenaByName(args[1]).setVIPArena(Boolean.parseBoolean(args[2]));
            }
            sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "vip value"));
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
        }
        return true;
    }
    
    public boolean joinArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (args.length > 1)
        {
            String playername = p.getName();
            if (args.length > 2)
            {
                if (!sender.hasPermission(uber_permission + ".adminjoin"))
                {
                    sender.sendMessage(pli.getMessagesConfig().no_perm);
                    return true;
                }
                if (Validator.isPlayerOnline(args[2]))
                {
                    playername = args[2];
                }
            }
            final Arena temp = pli.getArenaByName(args[1]);
            if (temp != null)
            {
                if (!temp.containsPlayer(playername))
                {
                    temp.joinPlayerLobby(playername);
                }
                else
                {
                    sender.sendMessage(pli.getMessagesConfig().you_already_are_in_arena.replaceAll("<arena>", temp.getInternalName()));
                }
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
            }
        }
        else
        {
            Arena a_ = null;
            for (final Arena a : pli.getArenas())
            {
                if (a.getArenaState() != ArenaState.INGAME)
                {
                    a_ = a;
                }
            }
            if (a_ != null)
            {
                a_.joinPlayerLobby(p.getName());
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
            }
        }
        return true;
    }
    
    public boolean leaveArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (pli.global_players.containsKey(p.getName()))
        {
            String playername = p.getName();
            if (args.length > 1)
            {
                if (sender.hasPermission(uber_permission + ".kickplayer"))
                {
                    if (Validator.isPlayerOnline(args[1]))
                    {
                        playername = args[1];
                    }
                }
            }
            final Arena a = pli.global_players.get(playername);
            if (a.getArcadeInstance() != null)
            {
                a.getArcadeInstance().leaveArcade(playername, true);
            }
            a.leavePlayer(playername, false, false);
        }
        else
        {
            sender.sendMessage(pli.getMessagesConfig().not_in_arena);
        }
        return true;
    }
    
    public boolean startArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".start"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            final Arena temp = pli.getArenaByName(args[1]);
            if (temp != null)
            {
                temp.start(true);
                sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "started"));
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
            }
        }
        else
        {
            sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
        }
        return true;
    }
    
    public boolean stopArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".stop"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            final Arena temp = pli.getArenaByName(args[1]);
            if (temp != null)
            {
                temp.stopArena();
                sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "stopped"));
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
            }
        }
        else
        {
            if (pli.containsGlobalPlayer(p.getName()))
            {
                final Arena a = pli.global_players.get(p.getName());
                a.stopArena();
                sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", a.getInternalName()).replaceAll("<action>", "stopped"));
                return true;
            }
            sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
        }
        return true;
    }
    
    public boolean stopAllArenas(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".stop"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        for (final Arena a : pli.getArenas())
        {
            a.stopArena();
        }
        return true;
    }
    
    public boolean removeArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            pli.getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + args[1], null);
            pli.getArenasConfig().saveConfig();
            if (pli.removeArena(pli.getArenaByName(args[1])))
            {
                sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "removed"));
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().failed_removing_arena.replaceAll("<arena>", args[1]));
            }
            // TODO remove arena file if present
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
        }
        return true;
    }
    
    public boolean removeSpawn(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (Util.isNumeric(args[2]))
            {
                if (pli.arenaSetup.removeSpawn(plugin, args[1], Integer.parseInt(args[2])))
                {
                    sender.sendMessage(pli.getMessagesConfig().successfully_removed.replaceAll("<component>", "spawn " + args[2]));
                }
                else
                {
                    sender.sendMessage(pli.getMessagesConfig().failed_removing_component.replaceAll("<component>", "spawn " + args[2]).replaceAll("<cause>",
                            "Possibly the provided count couldn't be found: " + args[2]));
                }
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <count>");
        }
        return true;
    }
    
    // TODO Implement skulls
    public boolean setSkull(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        sender.sendMessage(ChatColor.GRAY + "This feature is not implemented yet.");
        if (true) return true; // TODO will be reimplemented in future versions, work is under progress
        if (args.length > 1)
        {
            if (Util.isNumeric(args[1]))
            {
                final int count = Integer.parseInt(args[1]);
                pli.getStatsInstance();
                p.getInventory().addItem(Stats.giveSkull(args[1]));
                p.updateInventory();
                pli.getStatsInstance().skullsetup.add(p.getName());
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <count>");
        }
        return true;
    }
    
    public boolean setEnabled(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))
            {
                pli.arenaSetup.setArenaEnabled(plugin, args[1], Boolean.parseBoolean(args[2]));
                sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "enabled state"));
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
        }
        return true;
    }
    
    public boolean setShowScoreboard(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))
            {
                pli.arenaSetup.setShowScoreboard(plugin, args[1], Boolean.parseBoolean(args[2]));
                sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "scoreboard state"));
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <true/false>");
        }
        return true;
    }
    
    public boolean resetArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".reset"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 1)
        {
            final Arena a = pli.getArenaByName(args[1]);
            if (a != null)
            {
                if (Validator.isArenaValid(plugin, a))
                {
                    Bukkit.getScheduler().runTask(plugin, () -> PrivateUtil.loadArenaFromFileSYNC(plugin, a));
                    sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", args[1]).replaceAll("<action>", "reset"));
                }
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
        }
        return true;
    }
    
    public boolean setAuthor(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            final String author = args[2];
            if (Validator.isArenaValid(plugin, args[1]))
            {
                pli.getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + args[1] + ArenaConfigStrings.AUTHOR_SUFFIX, author);
                pli.getArenasConfig().saveConfig();
                sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "author"));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <author>");
        }
        return true;
    }
    
    public boolean setDescription(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            final String desc = args[2];
            if (Validator.isArenaValid(plugin, args[1]))
            {
                pli.getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + args[1] + ArenaConfigStrings.DESCRIPTION_SUFFIX, desc);
                pli.getArenasConfig().saveConfig();
                sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "description"));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <description>");
        }
        return true;
    }
    
    public boolean setArenaDisplayName(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        if (args.length > 2)
        {
            final String displayname = args[2];
            if (Validator.isArenaValid(plugin, args[1]))
            {
                pli.getArenasConfig().getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + args[1] + ArenaConfigStrings.DISPLAYNAME_SUFFIX, displayname);
                pli.getArenasConfig().saveConfig();
                pli.reloadArena(args[1]);
                sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "displayname"));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <displayname>");
        }
        return true;
    }
    
    public boolean spectate(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        // TODO args > 1 and else leave current player arena
        if (args.length > 0)
        {
            String playername = p.getName();
            if (args.length > 2)
            {
                if (!sender.hasPermission(uber_permission + ".adminspectate"))
                {
                    sender.sendMessage(pli.getMessagesConfig().no_perm);
                    return true;
                }
                if (Validator.isPlayerOnline(args[2]))
                {
                    playername = args[2];
                }
            }
            else if (args.length > 1)
            {
                final Arena temp = pli.getArenaByName(args[1]);
                if (temp != null)
                {
                    if (temp.getArenaState() == ArenaState.INGAME)
                    {
                        if (!temp.containsPlayer(playername))
                        {
                            if (!sender.hasPermission(uber_permission + ".spectate"))
                            {
                                sender.sendMessage(pli.getMessagesConfig().no_perm);
                            }
                            else
                            {
                                temp.joinSpectate(p);
                            }
                        }
                        else
                        {
                            sender.sendMessage(pli.getMessagesConfig().you_already_are_in_arena.replaceAll("<arena>", temp.getInternalName()));
                        }
                    }
                    else
                    {
                        sender.sendMessage(pli.getMessagesConfig().no_game_started.replaceAll("<arena>", args[1]));
                    }
                }
                else
                {
                    sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", args[1]));
                }
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().arena_invalid.replaceAll("<arena>", "Arena"));
            }
        }
        return true;
    }
    
    public boolean setKit(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            Player player)
    {
        Player p = player;
        if (args.length > 1)
        {
            if (!plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_CLASSES_ENABLED))
            {
                return true;
            }
            if (args.length > 2)
            {
                if (!sender.hasPermission(uber_permission + ".adminkit"))
                {
                    sender.sendMessage(pli.getMessagesConfig().no_perm);
                    return true;
                }
                p = Bukkit.getPlayer(args[2]);
                if (p == null)
                {
                    return true;
                }
            }
            if (!plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_ALLOW_CLASSES_SELECTION_OUT_OF_ARENAS))
            {
                if (pli.global_players.containsKey(p.getName()))
                {
                    final Arena a = pli.global_players.get(p.getName());
                    if (a.getArenaState() == ArenaState.INGAME)
                    {
                        return true;
                    }
                }
                else
                {
                    sender.sendMessage(pli.getMessagesConfig().not_in_arena);
                    return true;
                }
            }
            
            final String kit = args[1];
            final AClass ac = pli.getClassesHandler().getClassByInternalname(kit);
            if (ac != null)
            {
                if (pli.getAClasses().containsKey(ac.getName()))
                {
                    if (ac.isEnabled())
                    {
                        pli.getClassesHandler().setClass(kit, p.getName(), MinigamesAPI.getAPI().economyAvailable());
                        return true;
                    }
                }
            }
            
            String all = "";
            for (final AClass k : pli.getAClasses().values())
            {
                if (k.isEnabled())
                {
                    if (!pli.show_classes_without_usage_permission)
                    {
                        if (!pli.getClassesHandler().kitPlayerHasPermission(k.getInternalName(), p))
                        {
                            continue;
                        }
                    }
                    all += k.getInternalName() + ", ";
                }
            }
            if (all.length() < 2)
            {
                all = "No kits found!  ";
            }
            all = all.substring(0, all.length() - 2);
            sender.sendMessage(pli.getMessagesConfig().possible_kits + all);
            
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <kit>");
            if (!plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_CLASSES_ENABLED))
            {
                return true;
            }
            if (pli.global_players.containsKey(p.getName()))
            {
                pli.getClassesHandler().openGUI(p.getName());
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().not_in_arena);
            }
        }
        return true;
    }
    
    public boolean openShop(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (args.length > 1)
        {
            if (!plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SHOP_ENABLED))
            {
                return true;
            }
            if (pli.global_players.containsKey(p.getName()))
            {
                final String shop_item = args[1];
                if (!pli.getShopHandler().buyByInternalName(p, shop_item))
                {
                    String all = "";
                    for (final String ac : pli.getShopHandler().shopitems.keySet())
                    {
                        all += ac + ", ";
                    }
                    if (all.length() < 2)
                    {
                        all = "No shop items found!  ";
                    }
                    all = all.substring(0, all.length() - 2);
                    sender.sendMessage(pli.getMessagesConfig().possible_shopitems + all);
                }
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().not_in_arena);
            }
        }
        else
        {
            if (!plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SHOP_ENABLED))
            {
                return true;
            }
            if (pli.global_players.containsKey(p.getName()))
            {
                pli.getShopHandler().openGUI(p.getName());
            }
            else
            {
                sender.sendMessage(pli.getMessagesConfig().not_in_arena);
            }
        }
        return true;
    }
    
    public boolean getStats(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        Util.sendStatsMessage(pli, p);
        return true;
    }
    
    public boolean getLeaderboards(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        boolean wins = true;
        int count = 10;
        if (args.length > 2)
        {
            if (Util.isNumeric(args[2]))
            {
                count = Integer.parseInt(args[2]);
                if (!args[1].equalsIgnoreCase("wins"))
                {
                    wins = false;
                }
            }
            else
            {
                if (Util.isNumeric(args[1]))
                {
                    count = Integer.parseInt(args[1]);
                }
                if (!args[2].equalsIgnoreCase("wins"))
                {
                    wins = false;
                }
            }
        }
        else if (args.length > 1 && args.length < 3)
        {
            if (Util.isNumeric(args[1]))
            {
                count = Integer.parseInt(args[1]);
            }
            else
            {
                if (!args[1].equalsIgnoreCase("wins"))
                {
                    wins = false;
                }
            }
        }
        this.sendLeaderboards(pli, sender, count, wins);
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
    private void sendLeaderboards(final PluginInstance pli, final CommandSender sender, final int count, final boolean wins)
    {
        int c = 0;
        if (wins)
        {
            sender.sendMessage(ChatColor.DARK_GREEN + "--- TOP WINS ---");
            final TreeMap<String, Double> sorted_wins = pli.getStatsInstance().getTop(count, true);
            for (final Map.Entry<String, Double> entry : sorted_wins.entrySet())
            {
                c++;
                if (c > count)
                {
                    break;
                }
                sender.sendMessage(ChatColor.GREEN + "" + Integer.toString((int) entry.getValue().doubleValue()) + ChatColor.DARK_GREEN + " - " + ChatColor.GREEN + "" + entry.getKey());
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GREEN + "-- TOP POINTS --");
            final TreeMap<String, Double> sorted_wins = pli.getStatsInstance().getTop(count, false);
            for (final Map.Entry<String, Double> entry : sorted_wins.entrySet())
            {
                c++;
                if (c > count)
                {
                    break;
                }
                sender.sendMessage(ChatColor.GREEN + "" + Integer.toString((int) entry.getValue().doubleValue()) + ChatColor.DARK_GREEN + " - " + ChatColor.GREEN + "" + entry.getKey());
            }
        }
    }
    
    // Hologram commands
    public boolean setHologram(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin,
            final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        final Location l = p.getLocation();
        pli.getHologramsHandler().addHologram(l);
        p.sendMessage(ChatColor.GREEN + "Successfully set hologram.");
        return true;
    }
    
    public boolean listHolograms(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        final HologramsConfig config = pli.getHologramsConfig();
        int c = 0;
        if (config.getConfig().isSet("holograms."))
        {
            for (final String str : config.getConfig().getConfigurationSection("holograms.").getKeys(false))
            {
                final String base = "holograms." + str;
                final Location l = new Location(Bukkit.getWorld(config.getConfig().getString(base + ".world")), config.getConfig().getDouble(base + ".location.x"),
                        config.getConfig().getDouble(base + ".location.y"), config.getConfig().getDouble(base + ".location.z"), (float) config.getConfig().getDouble(base + ".location.yaw"),
                        (float) config.getConfig().getDouble(base + ".location.pitch"));
                p.sendMessage(ChatColor.GRAY + " ~ " + "world:" + l.getWorld().getName() + ", x:" + l.getBlockX() + ", y:" + l.getBlockY() + ", z:" + l.getBlockZ());
                c++;
            }
        }
        if (c == 0)
        {
            p.sendMessage(ChatColor.RED + "No holograms found!");
        }
        return true;
    }
    
    public boolean removeHologram(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action,
            final JavaPlugin plugin, final Player p)
    {
        if (!sender.hasPermission(uber_permission + ".setup"))
        {
            sender.sendMessage(pli.getMessagesConfig().no_perm);
            return true;
        }
        final Location ploc = p.getLocation();
        final boolean foundHologram = pli.getHologramsHandler().removeHologram(ploc);
        if (foundHologram)
        {
            p.sendMessage(ChatColor.GREEN + "Successfully removed hologram from config. It won't be sent anymore from now on!");
            return true;
        }
        // No holograms found
        p.sendMessage(ChatColor.RED + "No near holograms found! Please stand in a maximum distance of 2 blocks to a hologram to remove it.");
        return true;
    }
    
    // Party commands
    public boolean partyInvite(final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
    {
        if (args.length > 1)
        {
            final Player target = Bukkit.getPlayer(args[1]);
            if (target == null)
            {
                p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
                return true;
            }
            
            if (p.getUniqueId().equals(target.getUniqueId()))
            {
                p.sendMessage(MinigamesAPI.getAPI().partymessages.cannot_invite_yourself);
                return true;
            }
            
            boolean isInParty = false;
            for (final Party party : MinigamesAPI.getAPI().getParties())
            {
                if (party.containsPlayer(p.getUniqueId()))
                {
                    isInParty = true;
                }
            }
            if (!isInParty)
            {
                if (!Validator.isPlayerOnline(args[1]))
                {
                    p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
                    return true;
                }
                Party party = null;
                if (!MinigamesAPI.getAPI().hasParty(p.getUniqueId()))
                {
                    party = MinigamesAPI.getAPI().createParty(p.getUniqueId());
                }
                else
                {
                    party = MinigamesAPI.getAPI().getParty(p.getUniqueId());
                }
                final Player invited = Bukkit.getPlayer(args[1]);
                MinigamesAPI.getAPI().addPartyInvite(invited.getUniqueId(), party);
                p.sendMessage(MinigamesAPI.getAPI().partymessages.you_invited.replaceAll("<player>", args[1]));
                invited.sendMessage(MinigamesAPI.getAPI().partymessages.you_were_invited.replaceAll("<player>", p.getName()));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <player>");
        }
        return true;
    }
    
    public boolean partyAccept(final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
    {
        if (args.length > 1)
        {
            if (!Validator.isPlayerOnline(args[1]))
            {
                p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
                return true;
            }
            if (!MinigamesAPI.getAPI().hasPartyInvites(p.getUniqueId()))
            {
                p.sendMessage(MinigamesAPI.getAPI().partymessages.not_invited_to_any_party);
                return true;
            }
            
            boolean isInParty = false;
            Party party_ = null;
            for (final Party party : MinigamesAPI.getAPI().getParties())
            {
                if (party.containsPlayer(p.getUniqueId()))
                {
                    isInParty = true;
                    party_ = party;
                }
            }
            if (isInParty)
            {
                if (party_ != null)
                {
                    party_.removePlayer(p.getUniqueId());
                }
            }
            if (MinigamesAPI.getAPI().hasParty(p.getUniqueId()))
            {
                MinigamesAPI.getAPI().getParty(p.getUniqueId()).disband();
            }
            
            Party party__ = null;
            for (final Party party : MinigamesAPI.getAPI().getPartyInvites(p.getUniqueId()))
            {
                if (party.getOwner().equals(Bukkit.getPlayer(args[1]).getUniqueId()))
                {
                    party__ = party;
                    break;
                }
            }
            if (party__ != null)
            {
                party__.addPlayer(p.getUniqueId());
                MinigamesAPI.getAPI().removePartyInvites(p.getUniqueId());
            }
            else
            {
                p.sendMessage(MinigamesAPI.getAPI().partymessages.not_invited_to_players_party.replaceAll("<player>", args[1]));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <player>");
        }
        return true;
    }
    
    public boolean partyKick(final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
    {
        if (args.length > 1)
        {
            if (!Validator.isPlayerOnline(args[1]))
            {
                p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_online.replaceAll("<player>", args[1]));
                return true;
            }
            if (MinigamesAPI.getAPI().hasParty(p.getUniqueId()))
            {
                final Party party = MinigamesAPI.getAPI().getParty(p.getUniqueId());
                final Player target = Bukkit.getPlayer(args[1]);
                if (party.containsPlayer(target.getUniqueId()))
                {
                    party.removePlayer(target.getUniqueId());
                }
                else
                {
                    p.sendMessage(MinigamesAPI.getAPI().partymessages.player_not_in_party.replaceAll("<player>", args[1]));
                }
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <player>");
        }
        return true;
    }
    
    public boolean partyList(final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
    {
        if (args.length > 0)
        {
            Party party_ = null;
            for (final Party party : MinigamesAPI.getAPI().getParties())
            {
                if (party.containsPlayer(p.getUniqueId()))
                {
                    party_ = party;
                }
            }
            if (MinigamesAPI.getAPI().hasParty(p.getUniqueId()))
            {
                party_ = MinigamesAPI.getAPI().getParty(p.getUniqueId());
            }
            if (party_ != null)
            {
                String ret = ChatColor.DARK_GREEN + Bukkit.getPlayer(party_.getOwner()).getName();
                for (final UUID p_ : party_.getPlayers())
                {
                    ret += ChatColor.GREEN + ", " + Bukkit.getPlayer(p_).getName();
                }
                p.sendMessage(ret);
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action);
        }
        return true;
    }
    
    public boolean partyDisband(final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
    {
        if (args.length > 0)
        {
            if (MinigamesAPI.getAPI().hasParty(p.getUniqueId()))
            {
                MinigamesAPI.getAPI().getParty(p.getUniqueId()).disband();
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action);
        }
        return true;
    }
    
    public boolean partyLeave(final CommandSender sender, final String[] args, final String uber_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
    {
        if (args.length > 0)
        {
            if (MinigamesAPI.getAPI().hasParty(p.getUniqueId()))
            {
                MinigamesAPI.getAPI().getParty(p.getUniqueId()).disband();
                return true;
            }
            Party party_ = null;
            for (final Party party : MinigamesAPI.getAPI().getParties())
            {
                if (party.containsPlayer(p.getUniqueId()))
                {
                    party_ = party;
                }
            }
            if (party_ != null)
            {
                party_.removePlayer(p.getUniqueId());
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action);
        }
        return true;
    }
    
}
