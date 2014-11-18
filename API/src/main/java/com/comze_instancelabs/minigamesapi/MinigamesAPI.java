package com.comze_instancelabs.minigamesapi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.PartyMessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.config.StatsGlobalConfig;
import com.comze_instancelabs.minigamesapi.guns.Guns;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;
import com.comze_instancelabs.minigamesapi.util.BungeeUtil;
import com.comze_instancelabs.minigamesapi.util.Metrics;
import com.comze_instancelabs.minigamesapi.util.ParticleEffectNew;
import com.comze_instancelabs.minigamesapi.util.Updater;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class MinigamesAPI extends JavaPlugin implements PluginMessageListener {

	static MinigamesAPI instance = null;
	public static Economy econ = null;
	public static boolean economy = true;
	public boolean crackshot = false;
	public static boolean debug = false;

	public HashMap<String, Party> global_party = new HashMap<String, Party>();
	public HashMap<String, ArrayList<Party>> global_party_invites = new HashMap<String, ArrayList<Party>>();

	public static HashMap<JavaPlugin, PluginInstance> pinstances = new HashMap<JavaPlugin, PluginInstance>();

	public PartyMessagesConfig partymessages;
	public StatsGlobalConfig statsglobal;

	public String version = "";

	public void onEnable() {
		instance = this;

		this.version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loaded MinigamesAPI. We're on " + version + ".");

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		if (economy) {
			if (!setupEconomy()) {
				getLogger().severe(String.format("[%s] - No Economy (Vault) dependency found! Disabling Economy.", getDescription().getName()));
				economy = false;
			}
		}

		getConfig().options().header("Want bugfree versions? Set this to true:");
		getConfig().addDefault("config.auto_updating", true);
		getConfig().addDefault("config.party_command_enabled", true);
		getConfig().addDefault("config.debug", false);

		getConfig().options().copyDefaults(true);
		this.saveConfig();

		partymessages = new PartyMessagesConfig(this);
		statsglobal = new StatsGlobalConfig(this, false);

		this.debug = getConfig().getBoolean("config.debug");

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
		}

		if (getConfig().getBoolean("config.auto_updating")) {
			Updater updater = new Updater(this, 83025, this.getFile(), Updater.UpdateType.DEFAULT, false);
		}

		if (getServer().getPluginManager().getPlugin("CrackShot") != null) {
			crackshot = true;
		}

		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			public void run() {
				int i = 0;
				for (PluginInstance pli : MinigamesAPI.getAPI().pinstances.values()) {
					for (Arena a : pli.getArenas()) {
						if (a != null) {
							if (a.isSuccessfullyInit()) {
								Util.updateSign(pli.getPlugin(), a);
							} else {
								System.out.println(a.getName() + " not initialized at onEnable.");
							}
						}
						i++;
					}
				}
				System.out.println("Found " + i + " arenas.");
			}
		}, 50L);
	}

	public void onDisable() {
		for (PluginInstance pli : this.pinstances.values()) {
			for (Arena a : pli.getArenas()) {
				if (a != null) {
					if (a.isSuccessfullyInit()) {
						ArrayList<String> temp = new ArrayList<String>(a.getAllPlayers());
						for (String p_ : temp) {
							a.leavePlayer(p_, true);
						}
						try {
							a.getSmartReset().resetRaw();
						} catch (Exception e) {
							System.out.println("Failed resetting arena at onDisable. " + e.getMessage());
						}
					} else {
						System.out.println(a.getName() + " not initialized thus not reset at onDisable.");
					}
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
		pinstances.get(plugin_).getShopHandler().loadShopItems();
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
		setupRaw(plugin_, minigame);
		return instance;
	}

	/**
	 * Sets up the API, stuff won't work without that
	 * 
	 * @param plugin_
	 * @return
	 */
	public static MinigamesAPI setupAPI(JavaPlugin plugin_, String minigame) {
		PluginInstance pli = setupRaw(plugin_, minigame);
		pli.addLoadedArenas(Util.loadArenas(plugin_, pli.getArenasConfig()));
		return instance;
	}

	public static PluginInstance setupRaw(JavaPlugin plugin_, String minigame) {
		ArenasConfig arenasconfig = new ArenasConfig(plugin_);
		MessagesConfig messagesconfig = new MessagesConfig(plugin_);
		ClassesConfig classesconfig = new ClassesConfig(plugin_, false);
		StatsConfig statsconfig = new StatsConfig(plugin_, false);
		DefaultConfig.init(plugin_, false);
		PluginInstance pli = new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig);
		pinstances.put(plugin_, pli);
		ArenaListener al = new ArenaListener(plugin_, pinstances.get(plugin_), minigame);
		pinstances.get(plugin_).setArenaListener(al);
		Bukkit.getPluginManager().registerEvents(al, plugin_);
		Classes.loadClasses(plugin_);
		pli.getShopHandler().loadShopItems();
		Guns.loadGuns(plugin_);
		return pli;
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
		if (cmd.getName().equalsIgnoreCase("start")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Please execute this command ingame.");
				return true;
			}
			if (!sender.hasPermission("minigamesapi.start")) {
				// TODO no_perm message
				return true;
			}
			Player p = (Player) sender;
			for (PluginInstance pli : MinigamesAPI.getAPI().pinstances.values()) {
				if (pli.containsGlobalPlayer(p.getName())) {
					Arena a = pli.global_players.get(p.getName());
					System.out.println(a.getName());
					if (a.getArenaState() == ArenaState.JOIN || (a.getArenaState() == ArenaState.STARTING && !a.getIngameCountdownStarted())) {
						a.start(true);
						sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll("<arena>", a.getDisplayName()).replaceAll("<action>", "started"));
						break;
					}
				}
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("party")) {
			if (!getConfig().getBoolean("config.party_command_enabled")) {
				return true;
			}
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
				} else if (action.equalsIgnoreCase("leave")) {
					cmdhandler.partyLeave(sender, args, "minigamesapi.party", "/" + cmd.getName(), action, this, p);
				} else {
					cmdhandler.sendPartyHelp("/" + cmd.getName(), sender);
				}
			} else {
				cmdhandler.sendPartyHelp("/" + cmd.getName(), sender);
			}
		} else {
			if (args.length > 1) {
				if (args[0].equalsIgnoreCase("info")) {
					String p = args[1];
					sender.sendMessage("Debug info about " + p);
					sender.sendMessage("~ global_players: ");
					for (PluginInstance pli : pinstances.values()) {
						if (pli.global_players.containsKey(p)) {
							sender.sendMessage(" " + pli.getPlugin().getName());
						}
					}
					sender.sendMessage("~ global_lost: ");
					for (PluginInstance pli : pinstances.values()) {
						if (pli.global_lost.containsKey(p)) {
							sender.sendMessage(" " + pli.getPlugin().getName());
						}
					}
					sender.sendMessage("~ SpectatorManager: ");
					for (PluginInstance pli : pinstances.values()) {
						if (pli.getSpectatorManager().isSpectating(Bukkit.getPlayer(p))) {
							sender.sendMessage(" " + pli.getPlugin().getName());
						}
					}
					sender.sendMessage("~ Arenas: ");
					for (PluginInstance pli : pinstances.values()) {
						if (pli.global_players.containsKey(p)) {
							sender.sendMessage(" " + pli.global_players.get(p).getInternalName() + " - " + pli.global_players.get(p).getArenaState());
						}
					}
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("debug")) {
				debug = !debug;
				this.getConfig().set("config.debug", debug);
				this.saveConfig();
				sender.sendMessage(ChatColor.GOLD + "Debug mode is now: " + debug);
				return true;
			}
			if (sender instanceof Player && args.length > 0) {
				Player p = (Player) sender;
				boolean cont = false;
				for (ParticleEffectNew f : ParticleEffectNew.values()){
					if(f.name().equalsIgnoreCase(args[0])){
						cont = true;
					}
				}
				if(!cont){
					sender.sendMessage(ChatColor.RED + "Couldn't find particle effect.");
					return true;
				}
				ParticleEffectNew eff = ParticleEffectNew.valueOf(args[0]);
				eff.setId(152);

				for (float i = 0; i < 10; i++) {
					eff.animateReflected(p, p.getLocation().clone().add(i / 5F, i / 5F, i / 5F), 1F, 2);
				}

				p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 152);
				p.getWorld().playEffect(p.getLocation().add(0D, 1D, 0D), Effect.STEP_SOUND, 152);
			}
		}
		return true;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		System.out.println(subchannel);
		if (subchannel.equals("MinigamesLibBack")) {
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);

			DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
			try {
				final String playerData = msgin.readUTF();
				final String plugin_ = playerData.split(":")[0];
				final String arena = playerData.split(":")[1];
				final String playername = playerData.split(":")[2];
				System.out.println(plugin_ + " -> " + arena);
				JavaPlugin plugin = null;
				for (JavaPlugin pl : this.pinstances.keySet()) {
					if (pl.getName().contains(plugin_)) {
						plugin = pl;
						break;
					}
				}
				if (plugin != null) {
					final Arena a = pinstances.get(plugin).getArenaByName(arena);
					if (a != null) {
						if (a.getArenaState() != ArenaState.INGAME && a.getArenaState() != ArenaState.RESTARTING && !a.containsPlayer(playername)) {
							Bukkit.getScheduler().runTaskLater(this, new Runnable() {
								public void run() {
									if (!a.containsPlayer(playername)) {
										a.joinPlayerLobby(playername);
									}
								}
							}, 20L);
						}
					} else {
						System.out.println("Arena " + arena + " couldn't be found, please fix your setup.");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (subchannel.equals("MinigamesLibRequest")) { // Lobby requests sign data
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);

			DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
			try {
				final String requestData = msgin.readUTF();
				final String plugin_ = requestData.split(":")[0];
				final String arena = requestData.split(":")[1];
				System.out.println(plugin_ + " -> " + arena);
				for (JavaPlugin pl : this.pinstances.keySet()) {
					if (pl.getName().contains(plugin_)) {
						Arena a = pinstances.get(pl).getArenaByName(arena);
						if (a != null) {
							BungeeUtil.sendSignUpdateRequest(pl, pl.getName(), a);
						} else {
							System.out.println("Arena " + arena + " couldn't be found, please fix your setup.");
						}
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public PluginInstance getPluginInstance(JavaPlugin plugin) {
		return pinstances.get(plugin);
	}

}
