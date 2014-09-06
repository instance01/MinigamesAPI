package com.comze_instancelabs.minigamesapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.PartyMessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.guns.Guns;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;
import com.comze_instancelabs.minigamesapi.util.Metrics;
import com.comze_instancelabs.minigamesapi.util.Updater;
import com.comze_instancelabs.minigamesapi.util.Util;

public class MinigamesAPI extends JavaPlugin {

	static MinigamesAPI instance = null;
	public static Economy econ = null;
	public static boolean economy = true;
	public static boolean arcade = false;

	public HashMap<String, Party> global_party = new HashMap<String, Party>();
	public HashMap<String, ArrayList<Party>> global_party_invites = new HashMap<String, ArrayList<Party>>();

	public static HashMap<JavaPlugin, PluginInstance> pinstances = new HashMap<JavaPlugin, PluginInstance>();

	public PartyMessagesConfig partymessages;

	public void onEnable() {
		instance = this;

		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loaded MinigamesAPI. We're on " + version + ".");

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		if (economy) {
			if (!setupEconomy()) {
				getLogger().severe(String.format("[%s] - No Economy (Vault) dependency found! Disabling Economy.", getDescription().getName()));
				economy = false;
			}
		}

		getConfig().options().header("Want bugfree versions? Set this to true:");
		getConfig().addDefault("config.auto_updating", true);

		getConfig().options().copyDefaults(true);
		this.saveConfig();

		partymessages = new PartyMessagesConfig(this);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
		}

		if (getConfig().getBoolean("config.auto_updating")) {
			Updater updater = new Updater(this, 83025, this.getFile(), Updater.UpdateType.DEFAULT, false);
		}
	}

	public void onDisable() {
		for (PluginInstance pli : this.pinstances.values()) {
			for (Arena a : pli.getArenas()) {
				ArrayList<String> temp = new ArrayList<String>(a.getAllPlayers());
				for (String p_ : temp) {
					a.leavePlayer(p_, true);
				}
			}
		}
	}

	/**
	 * Sets up the API allowing to override all configs
	 * 
	 * @param plugin_
	 * @param arenaclass
	 * @param arenasconfig
	 * @param messagesconfig
	 * @param classesconfig
	 * @param statsconfig
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame, Class<?> arenaclass, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig, StatsConfig statsconfig, DefaultConfig defaultconfig, boolean customlistener) {
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>()));
		if (!customlistener) {
			ArenaListener al = new ArenaListener(plugin_, pinstances.get(plugin_), minigame);
			pinstances.get(plugin_).setArenaListener(al);
			Bukkit.getPluginManager().registerEvents(al, plugin_);
		}
		Classes.loadClasses(plugin_);
		Guns.loadGuns(plugin_);
		return instance;
	}

	public static void registerArenaListenerLater(JavaPlugin plugin_, ArenaListener arenalistener) {
		Bukkit.getPluginManager().registerEvents(arenalistener, plugin_);
	}

	public static void registerArenaSetup(JavaPlugin plugin_, ArenaSetup arenasetup) {
		pinstances.get(plugin_).arenaSetup = arenasetup;
	}

	public static void registerScoreboard(JavaPlugin plugin_, ArenaScoreboard board) {
		pinstances.get(plugin_).scoreboardManager = board;
	}

	/**
	 * Sets up the API, stuff won't work without that
	 * 
	 * @param plugin_
	 * @return
	 */
	// Allow loading of arenas with own extended arena class into
	// PluginInstance:
	// after this setup, get the PluginInstance, load the arenas by yourself
	// and add the loaded arenas w/ custom arena class into the PluginInstance
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame, Class<?> arenaclass) {
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		ClassesConfig classesconfig = new ClassesConfig(plugin_, false);
		StatsConfig statsconfig = new StatsConfig(plugin_, false);
		DefaultConfig.init(plugin_, false);
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>()));
		ArenaListener al = new ArenaListener(plugin_, pinstances.get(plugin_), minigame);
		pinstances.get(plugin_).setArenaListener(al);
		Bukkit.getPluginManager().registerEvents(al, plugin_);
		Classes.loadClasses(plugin_);
		Guns.loadGuns(plugin_);
		return instance;
	}

	/**
	 * Sets up the API, stuff won't work without that
	 * 
	 * @param plugin_
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame) {
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		ClassesConfig classesconfig = new ClassesConfig(plugin_, false);
		StatsConfig statsconfig = new StatsConfig(plugin_, false);
		DefaultConfig.init(plugin_, false);
		pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig));
		pinstances.get(plugin_).addLoadedArenas(Util.loadArenas(plugin_, arenasconfig));
		ArenaListener al = new ArenaListener(plugin_, pinstances.get(plugin_), minigame);
		pinstances.get(plugin_).setArenaListener(al);
		Bukkit.getPluginManager().registerEvents(al, plugin_);
		Classes.loadClasses(plugin_);
		Guns.loadGuns(plugin_);
		return instance;
	}

	public static MinigamesAPI getAPI() {
		return instance;
	}

	public static CommandHandler getCommandHandler() {
		return new CommandHandler();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("party")) {
			CommandHandler cmdhandler = this.getCommandHandler();
			if (!(sender instanceof Player)) {
				sender.sendMessage("Please execute this command ingame.");
				return true;
			}
			Player p = (Player) sender;
			if (args.length > 0) {
				String action = args[0];
				if (action.equalsIgnoreCase("invite")) {
					cmdhandler.partyInvite(sender, args, "minigamesapi.party", "/" + cmd.getName(), action, this, p);
				} else if (action.equalsIgnoreCase("accept")) {
					cmdhandler.partyAccept(sender, args, "minigamesapi.party", "/" + cmd.getName(), action, this, p);
				} else if (action.equalsIgnoreCase("kick")) {
					cmdhandler.partyKick(sender, args, "minigamesapi.party", "/" + cmd.getName(), action, this, p);
				} else if (action.equalsIgnoreCase("list")) {
					cmdhandler.partyList(sender, args, "minigamesapi.party", "/" + cmd.getName(), action, this, p);
				} else if (action.equalsIgnoreCase("disband")) {
					cmdhandler.partyDisband(sender, args, "minigamesapi.party", "/" + cmd.getName(), action, this, p);
				} else {
					// TODO show help
				}
			} else {
				// TODO show help
			}

		}
		return true;
	}

}
