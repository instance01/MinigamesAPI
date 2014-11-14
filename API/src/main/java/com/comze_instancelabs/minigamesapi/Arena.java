package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesapi.arcade.ArcadeInstance;
import com.comze_instancelabs.minigamesapi.events.ArenaStartEvent;
import com.comze_instancelabs.minigamesapi.events.ArenaStartedEvent;
import com.comze_instancelabs.minigamesapi.events.ArenaStopEvent;
import com.comze_instancelabs.minigamesapi.events.PlayerJoinLobbyEvent;
import com.comze_instancelabs.minigamesapi.events.PlayerLeaveArenaEvent;
import com.comze_instancelabs.minigamesapi.util.BungeeUtil;
import com.comze_instancelabs.minigamesapi.util.Cuboid;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Arena {

	// Plugin the arena belongs to
	JavaPlugin plugin;
	PluginInstance pli;
	private ArcadeInstance ai;
	private boolean isArcadeMain = false;

	private ArrayList<Location> spawns = new ArrayList<Location>();
	HashMap<String, ItemStack[]> pinv = new HashMap<String, ItemStack[]>();
	HashMap<String, ItemStack[]> pinv_armor = new HashMap<String, ItemStack[]>();
	private HashMap<String, GameMode> pgamemode = new HashMap<String, GameMode>();
	private HashMap<String, Integer> pxplvl = new HashMap<String, Integer>();
	HashMap<String, Location> pspawnloc = new HashMap<String, Location>();

	/**
	 * Used when players leave with command, they shouldn't get rewards!
	 */
	private ArrayList<String> pnoreward = new ArrayList<String>();

	HashMap<String, String> lastdamager = new HashMap<String, String>();

	private Location mainlobby;
	private Location waitinglobby;
	private Location specspawn;
	private Location signloc;

	private int max_players;
	private int min_players;

	private boolean viparena;
	private String permission_node;

	private ArrayList<String> players = new ArrayList<String>();

	private ArenaType type = ArenaType.DEFAULT;
	private ArenaState currentstate = ArenaState.JOIN;
	String name = "mainarena";
	String displayname = "mainarena";

	private Arena currentarena;
	boolean started = false;
	boolean startedIngameCountdown = false;
	private boolean showArenascoreboard = true;
	private boolean alwaysPvP = false;

	SmartReset sr = null;

	Cuboid boundaries;
	Cuboid lobby_boundaries;
	Cuboid spec_boundaries;

	boolean temp_countdown = true;
	boolean skip_join_lobby = false;

	int currentspawn = 0;

	int global_coin_multiplier = 1;

	BukkitTask spectator_task;
	BukkitTask maximum_game_time;

	ArrayList<ItemStack> global_drops = new ArrayList<ItemStack>();

	/**
	 * Creates a normal singlespawn arena
	 * 
	 * @param plugin
	 *            JavaPlugin the arena belongs to
	 * @param name
	 *            name of the arena
	 */
	public Arena(JavaPlugin plugin, String name) {
		currentarena = this;
		this.plugin = plugin;
		this.name = name;
		sr = new SmartReset(this);
		this.pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
	}

	/**
	 * Creates an arena of given arenatype
	 * 
	 * @param name
	 *            name of the arena
	 * @param type
	 *            arena type
	 */
	public Arena(JavaPlugin plugin, String name, ArenaType type) {
		this(plugin, name);
		this.type = type;
	}

	// This is for loading existing arenas
	public void init(Location signloc, ArrayList<Location> spawns, Location mainlobby, Location waitinglobby, int max_players, int min_players, boolean viparena) {
		this.signloc = signloc;
		this.spawns = spawns;
		this.mainlobby = mainlobby;
		this.waitinglobby = waitinglobby;
		this.viparena = viparena;
		this.min_players = min_players;
		this.max_players = max_players;
		this.showArenascoreboard = pli.arenaSetup.getShowScoreboard(plugin, this.getInternalName());
		// if (this.getArenaType() == ArenaType.REGENERATION) {
		if (Util.isComponentForArenaValid(plugin, this.getInternalName(), "bounds.low") && Util.isComponentForArenaValid(plugin, this.getInternalName(), "bounds.high")) {
			try {
				Location low_boundary = Util.getComponentForArena(plugin, this.getInternalName(), "bounds.low");
				Location high_boundary = Util.getComponentForArena(plugin, this.getInternalName(), "bounds.high");
				if (low_boundary != null && high_boundary != null) {
					this.boundaries = new Cuboid(low_boundary, high_boundary);
				} else {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "The boundaries of an arena appear to be invalid (missing world?), please fix! Arena: " + this.getInternalName());
				}
			} catch (Exception e) {
				plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Failed to save arenas as you forgot to set boundaries or they could not be found. This will lead to major error flows later, please fix your setup. " + e.getMessage());
			}
		}

		if (Util.isComponentForArenaValid(plugin, this.getInternalName(), "lobbybounds.bounds.low") && Util.isComponentForArenaValid(plugin, this.getInternalName(), "lobbybounds.bounds.high")) {
			try {
				this.lobby_boundaries = new Cuboid(Util.getComponentForArena(plugin, this.getInternalName(), "lobbybounds.bounds.low"), Util.getComponentForArena(plugin, this.getInternalName(), "lobbybounds.bounds.high"));
			} catch (Exception e) {
				;
			}
		}

		if (Util.isComponentForArenaValid(plugin, this.getInternalName(), "specbounds.bounds.low") && Util.isComponentForArenaValid(plugin, this.getInternalName(), "specbounds.bounds.high")) {
			try {
				this.spec_boundaries = new Cuboid(Util.getComponentForArena(plugin, this.getInternalName(), "specbounds.bounds.low"), Util.getComponentForArena(plugin, this.getInternalName(), "specbounds.bounds.high"));
			} catch (Exception e) {
				;
			}
		}
		// }

		if (Util.isComponentForArenaValid(plugin, this.getInternalName(), "specspawn")) {
			this.specspawn = Util.getComponentForArena(plugin, this.getInternalName(), "specspawn");
		}

		String path = "arenas." + name + ".displayname";
		if (pli.getArenasConfig().getConfig().isSet(path)) {
			this.displayname = ChatColor.translateAlternateColorCodes('&', pli.getArenasConfig().getConfig().getString("arenas." + name + ".displayname"));
		} else {
			pli.getArenasConfig().getConfig().set(path, name);
			pli.getArenasConfig().saveConfig();
			this.displayname = name;
		}
	}

	// This is for loading existing arenas
	@Deprecated
	public Arena initArena(Location signloc, ArrayList<Location> spawn, Location mainlobby, Location waitinglobby, int max_players, int min_players, boolean viparena) {
		this.init(signloc, spawn, mainlobby, waitinglobby, max_players, min_players, viparena);
		return this;
	}

	public Arena getArena() {
		return this;
	}

	public SmartReset getSmartReset() {
		return this.sr;
	}

	public boolean getShowScoreboard() {
		return this.showArenascoreboard;
	}

	public boolean getAlwaysPvP() {
		return this.alwaysPvP;
	}

	public void setAlwaysPvP(boolean t) {
		this.alwaysPvP = t;
	}

	public Location getSignLocation() {
		return this.signloc;
	}

	public void setSignLocation(Location l) {
		this.signloc = l;
	}

	public ArrayList<Location> getSpawns() {
		return this.spawns;
	}

	public Cuboid getBoundaries() {
		return this.boundaries;
	}

	public Cuboid getLobbyBoundaries() {
		return this.lobby_boundaries;
	}

	public Cuboid getSpecBoundaries() {
		return this.spec_boundaries;
	}

	public String getInternalName() {
		return name;
	}

	public String getDisplayName() {
		return displayname;
	}

	/**
	 * Please use getInternalName() for the internal name and getDisplayName() for the optional displayname
	 * 
	 * @return Internal name of arena (same as getInternalName())
	 */
	@Deprecated
	public String getName() {
		return name;
	}

	public int getMaxPlayers() {
		return this.max_players;
	}

	public int getMinPlayers() {
		return this.min_players;
	}

	public void setMinPlayers(int i) {
		this.min_players = i;
	}

	public void setMaxPlayers(int i) {
		this.max_players = i;
	}

	public boolean isVIPArena() {
		return this.viparena;
	}

	public void setVIPArena(boolean t) {
		this.viparena = t;
	}

	public ArrayList<String> getAllPlayers() {
		return this.players;
	}

	public boolean containsPlayer(String playername) {
		return players.contains(playername);
	}

	/**
	 * Please do not use this function to add players
	 * 
	 * @param playername
	 * @return
	 */
	@Deprecated
	public boolean addPlayer(String playername) {
		return players.add(playername);
	}

	/**
	 * Please do not use this function to remove players
	 * 
	 * @param playername
	 * @return
	 */
	@Deprecated
	public boolean removePlayer(String playername) {
		return players.remove(playername);
	}

	public ArenaState getArenaState() {
		return this.currentstate;
	}

	public void setArenaState(ArenaState s) {
		this.currentstate = s;
	}

	public ArenaType getArenaType() {
		return this.type;
	}

	/**
	 * Joins the waiting lobby of an arena
	 * 
	 * @param playername
	 *            the playername
	 */
	public void joinPlayerLobby(String playername) {
		if (this.getArenaState() != ArenaState.JOIN && this.getArenaState() != ArenaState.STARTING) {
			// arena ingame or restarting
			return;
		}
		if (!pli.arenaSetup.getArenaEnabled(plugin, this.getInternalName())) {
			Util.sendMessage(plugin, Bukkit.getPlayer(playername), pli.getMessagesConfig().arena_disabled);
			return;
		}
		if (pli.containsGlobalPlayer(playername)) {
			Util.sendMessage(plugin, Bukkit.getPlayer(playername), pli.getMessagesConfig().already_in_arena);
			return;
		}
		if (ai == null && this.isVIPArena()) {
			if (Validator.isPlayerOnline(playername)) {
				if (!Bukkit.getPlayer(playername).hasPermission("arenas." + this.getInternalName()) && !Bukkit.getPlayer(playername).hasPermission("arenas.*")) {
					Util.sendMessage(plugin, Bukkit.getPlayer(playername), pli.getMessagesConfig().no_perm_to_join_arena.replaceAll("<arena>", this.getInternalName()));
					return;
				}
			}
		}
		if (ai == null && this.getAllPlayers().size() > this.max_players - 1) {
			// arena full

			// if player vip -> kick someone and continue
			System.out.println(playername + " is vip: " + Bukkit.getPlayer(playername).hasPermission("arenas.*"));
			if (!Bukkit.getPlayer(playername).hasPermission("arenas." + this.getInternalName()) && !Bukkit.getPlayer(playername).hasPermission("arenas.*")) {
				return;
			} else {
				// player has vip
				boolean noone_found = true;
				ArrayList<String> temp = new ArrayList<String>(this.getAllPlayers());
				for (String p_ : temp) {
					if (Validator.isPlayerOnline(p_)) {
						if (!Bukkit.getPlayer(p_).hasPermission("arenas." + this.getInternalName()) && !Bukkit.getPlayer(p_).hasPermission("arenas.*")) {
							this.leavePlayer(p_, false, true);
							Bukkit.getPlayer(p_).sendMessage(pli.getMessagesConfig().you_got_kicked_because_vip_joined);
							noone_found = false;
							break;
						}
					}
				}
				if (noone_found) {
					// apparently everyone is vip, can't join
					return;
				}
			}
		}

		if (MinigamesAPI.getAPI().global_party.containsKey(playername)) {
			Party party = MinigamesAPI.getAPI().global_party.get(playername);
			int playersize = party.getPlayers().size() + 1;
			if (this.getAllPlayers().size() + playersize > this.max_players) {
				Bukkit.getPlayer(playername).sendMessage(MinigamesAPI.getAPI().partymessages.party_too_big_to_join);
				return;
			} else {
				for (String p_ : party.getPlayers()) {
					if (Validator.isPlayerOnline(p_)) {
						boolean cont = true;
						for (PluginInstance pli_ : MinigamesAPI.getAPI().pinstances.values()) {
							// if (!pli_.getPlugin().getName().equalsIgnoreCase("MGArcade") && pli_.global_players.containsKey(p_)) {
							if (pli_.containsGlobalPlayer(p_)) {
								cont = false;
							}
						}
						if (cont) {
							this.joinPlayerLobby(p_);
						}
					}
				}
			}
		}

		if (this.getAllPlayers().size() == this.max_players - 1) {
			if (currentlobbycount > 16 && this.getArenaState() == ArenaState.STARTING) {
				currentlobbycount = 16;
			}
		}
		pli.global_players.put(playername, this);
		this.players.add(playername);

		if (Validator.isPlayerValid(plugin, playername, this)) {
			final Player p = Bukkit.getPlayer(playername);
			Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinLobbyEvent(p, plugin, this));
			Util.sendMessage(plugin, p, pli.getMessagesConfig().you_joined_arena.replaceAll("<arena>", this.getDisplayName()));
			Util.sendMessage(plugin, p, pli.getMessagesConfig().minigame_description);
			if (pli.getArenasConfig().getConfig().isSet("arenas." + this.getInternalName() + ".author")) {
				Util.sendMessage(plugin, p, pli.getMessagesConfig().author_of_the_map.replaceAll("<arena>", this.getDisplayName()).replaceAll("<author>", pli.getArenasConfig().getConfig().getString("arenas." + this.getInternalName() + ".author")));
			}
			if (pli.getArenasConfig().getConfig().isSet("arenas." + this.getInternalName() + ".description")) {
				Util.sendMessage(plugin, p, pli.getMessagesConfig().description_of_the_map.replaceAll("<arena>", this.getDisplayName()).replaceAll("<description>", pli.getArenasConfig().getConfig().getString("arenas." + this.getInternalName() + ".description")));
			}
			for (String p_ : this.getAllPlayers()) {
				if (Validator.isPlayerOnline(p_) && !p_.equalsIgnoreCase(p.getName())) {
					Player p__ = Bukkit.getPlayer(p_);
					int count = this.getAllPlayers().size();
					int maxcount = this.getMaxPlayers();
					Util.sendMessage(plugin, p__, pli.getMessagesConfig().broadcast_player_joined.replaceAll("<player>", p.getName()).replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)));
				}
			}
			Util.updateSign(plugin, this);

			pinv.put(playername, p.getInventory().getContents());
			pinv_armor.put(playername, p.getInventory().getArmorContents());
			if (this.getArenaType() == ArenaType.JUMPNRUN) {
				Util.teleportPlayerFixed(p, this.spawns.get(currentspawn));
				if (currentspawn < this.spawns.size() - 1) {
					currentspawn++;
				}
				Util.clearInv(p);
				pgamemode.put(p.getName(), p.getGameMode());
				pxplvl.put(p.getName(), p.getExpToLevel());
				p.setGameMode(GameMode.SURVIVAL);
				p.setHealth(20D);
				return;
			} else {
				if (startedIngameCountdown) {
					pli.scoreboardLobbyManager.removeScoreboard(this.getInternalName(), p);
					Util.teleportAllPlayers(currentarena.getArena().getAllPlayers(), currentarena.getArena().spawns);
					p.setFoodLevel(5);
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -7)); // -5
					Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
						public void run() {
							p.setWalkSpeed(0.0F);
						}
					}, 1L);
					Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
						public void run() {
							Util.clearInv(p);
							Util.giveLobbyItems(plugin, p);
							pgamemode.put(p.getName(), p.getGameMode());
							pxplvl.put(p.getName(), p.getLevel());
							p.setGameMode(GameMode.SURVIVAL);
						}
					}, 10L);
					pli.scoreboardManager.updateScoreboard(plugin, this);
					return;
				} else {
					pli.scoreboardLobbyManager.updateScoreboard(plugin, this);
					Util.teleportPlayerFixed(p, this.waitinglobby);
				}
			}
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					Util.clearInv(p);
					Util.giveLobbyItems(plugin, p);
					pgamemode.put(p.getName(), p.getGameMode());
					pxplvl.put(p.getName(), p.getLevel());
					p.setGameMode(GameMode.SURVIVAL);
					p.setHealth(20D);
				}
			}, 10L);
			if (!skip_join_lobby) {
				if (ai == null && this.getAllPlayers().size() > this.min_players - 1) {
					this.startLobby(temp_countdown);
				} else if (ai != null) {
					this.startLobby(temp_countdown);
				}
			}
		}
	}

	/**
	 * Primarily used for ArcadeInstance to join a waiting lobby without countdown
	 * 
	 * @param playername
	 * @param countdown
	 */
	public void joinPlayerLobby(String playername, boolean countdown) {
		temp_countdown = countdown;
		joinPlayerLobby(playername);
	}

	/**
	 * Joins the waiting lobby of an arena
	 * 
	 * @param playername
	 *            the playername
	 * @param ai
	 *            the ArcadeInstance
	 */
	public void joinPlayerLobby(String playername, ArcadeInstance ai, boolean countdown, boolean skip_lobby) {
		this.skip_join_lobby = skip_lobby;
		this.ai = ai;
		joinPlayerLobby(playername, countdown); // join playerlobby without lobby countdown
	}

	/**
	 * Leaves the current arena, won't do anything if not present in any arena
	 * 
	 * @param playername
	 * @param fullLeave
	 *            Determines if player left only minigame or the server
	 */
	@Deprecated
	public void leavePlayer(final String playername, boolean fullLeave) {
		this.leavePlayerRaw(playername, fullLeave);
	}

	public void leavePlayer(final String playername, boolean fullLeave, boolean endofGame) {
		if (!endofGame) {
			pnoreward.add(playername);
		}

		this.leavePlayer(playername, fullLeave);

		if (!endofGame) {
			if (this.getAllPlayers().size() < 2) {
				if (this.getArenaState() != ArenaState.JOIN) {
					if (this.getArenaState() == ArenaState.STARTING && !startedIngameCountdown) {
						// cancel starting
						this.setArenaState(ArenaState.JOIN);
						Util.updateSign(plugin, this);
						try {
							Bukkit.getScheduler().cancelTask(currenttaskid);
						} catch (Exception e) {
							;
						}
						for (String p_ : this.getAllPlayers()) {
							if (Validator.isPlayerOnline(p_)) {
								Util.sendMessage(plugin, Bukkit.getPlayer(p_), pli.getMessagesConfig().cancelled_starting);
							}
						}
						return;
					}
					this.stop();
				}
			}
		}
	}

	public void leavePlayerRaw(final String playername, boolean fullLeave) {
		if (!this.containsPlayer(playername)) {
			return;
		}
		this.players.remove(playername);
		if (pli.containsGlobalPlayer(playername)) {
			pli.global_players.remove(playername);
		}
		if (fullLeave) {
			plugin.getConfig().set("temp.left_players." + playername + ".name", playername);
			plugin.getConfig().set("temp.left_players." + playername + ".plugin", plugin.getName());
			for (ItemStack i : pinv.get(playername)) {
				if (i != null) {
					plugin.getConfig().set("temp.left_players." + playername + ".items." + Integer.toString((int) Math.round(Math.random() * 10000)) + i.getType().toString(), i);
				}
			}
			plugin.saveConfig();

			try {
				Player p = Bukkit.getPlayer(playername);
				if (pli.global_lost.containsKey(playername)) {
					pli.global_lost.remove(playername);
				}
				if (pli.global_arcade_spectator.containsKey(playername)) {
					pli.global_arcade_spectator.remove(playername);
				}
				if (p != null) {
					p.removePotionEffect(PotionEffectType.JUMP);
					Util.teleportPlayerFixed(p, this.mainlobby);
					p.setFireTicks(0);
					p.setFlying(false);
					if (!p.isOp()) {
						p.setAllowFlight(false);
					}
					if (pgamemode.containsKey(p.getName())) {
						p.setGameMode(pgamemode.get(p.getName()));
					}
					if (pxplvl.containsKey(p.getName())) {
						p.setLevel(0);
						p.setLevel(pxplvl.get(p.getName()));
					}
					p.getInventory().setContents(pinv.get(playername));
					p.getInventory().setArmorContents(pinv_armor.get(playername));
					p.updateInventory();

					p.setWalkSpeed(0.2F);
					p.setFoodLevel(20);
					p.setHealth(20D);
					p.removePotionEffect(PotionEffectType.JUMP);
					pli.getSpectatorManager().setSpectate(p, false);

				}
			} catch (Exception e) {
				System.out.println("Failed to log out player out of arena. " + e.getMessage());
			}
			return;
		}
		final Player p = Bukkit.getPlayer(playername);
		if (p.isDead()) {
			System.out.println("[SEVERE] " + p.getName() + " unexpectedly appeared dead! Health: " + p.getHealth() + " - This is a BUG, please report!");
			// return;
		}
		Util.clearInv(p);
		p.setWalkSpeed(0.2F);
		p.setFoodLevel(20);
		p.setHealth(20D);
		p.setFireTicks(0);
		p.removePotionEffect(PotionEffectType.JUMP);
		pli.getSpectatorManager().setSpectate(p, false);

		Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveArenaEvent(p, plugin, this));

		for (PotionEffect effect : p.getActivePotionEffects()) {
			if (effect != null) {
				p.removePotionEffect(effect.getType());
			}
		}

		for (Entity e : p.getNearbyEntities(50D, 50D, 50D)) {
			if (e.getType() == EntityType.DROPPED_ITEM || e.getType() == EntityType.SLIME || e.getType() == EntityType.ZOMBIE || e.getType() == EntityType.SKELETON || e.getType() == EntityType.SPIDER || e.getType() == EntityType.CREEPER) {
				e.remove();
			}
		}

		pli.global_players.remove(playername);
		if (pli.global_arcade_spectator.containsKey(playername)) {
			pli.global_arcade_spectator.remove(playername);
		}

		Util.updateSign(plugin, this);

		final String arenaname = this.getInternalName();
		final Arena a = this;
		final boolean started_ = started;
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (p != null) {
					if (ai == null || a.isArcadeMain()) {
						if (a.mainlobby != null) {
							Util.teleportPlayerFixed(p, a.mainlobby);
						} else if (a.waitinglobby != null) {
							Util.teleportPlayerFixed(p, a.waitinglobby);
						}
					}
					p.setFireTicks(0);
					p.setFlying(false);
					if (!p.isOp()) {
						p.setAllowFlight(false);
					}
					if (pgamemode.containsKey(p.getName())) {
						p.setGameMode(pgamemode.get(p.getName()));
					}
					if (pxplvl.containsKey(p.getName())) {
						p.setLevel(0);
						p.setLevel(pxplvl.get(p.getName()));
					}
					p.getInventory().setContents(pinv.get(playername));
					p.getInventory().setArmorContents(pinv_armor.get(playername));
					p.updateInventory();

					if (started_) {
						if (!pnoreward.contains(playername)) {
							pli.getRewardsInstance().giveWinReward(playername, a, global_coin_multiplier);
						} else {
							pnoreward.remove(playername);
						}
					}

					if (pli.global_lost.containsKey(playername)) {
						pli.global_lost.remove(playername);
					}

					try {
						pli.scoreboardManager.removeScoreboard(arenaname, p);
					} catch (Exception e) {
						//
					}
				}
			}
		}, 5L);

		if (plugin.getConfig().getBoolean("config.bungee.teleport_all_to_server_on_stop.tp")) {
			final String server = plugin.getConfig().getString("config.bungee.teleport_all_to_server_on_stop.server");
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					BungeeUtil.connectToServer(MinigamesAPI.getAPI(), p.getName(), server);
				}
			}, 30L);
			return;
		}
	}

	/**
	 * Spectates the game
	 * 
	 * @param playername
	 *            the playername
	 */
	public void spectate(String playername) {
		if (Validator.isPlayerValid(plugin, playername, this)) {
			this.onEliminated(playername);
			final Player p = Bukkit.getPlayer(playername);

			pli.global_lost.put(playername, this);
			if (plugin.getConfig().getBoolean("config.effects")) {
				Arena a = this;
				try {
					Effects.playFakeBed(a, p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			pli.getSpectatorManager().setSpectate(p, true);
			if (!plugin.getConfig().getBoolean("config.spectator_after_fall_or_death")) {
				this.leavePlayer(playername, false, false);
				pli.scoreboardManager.updateScoreboard(plugin, this);
				return;
			}
			Util.clearInv(p);
			p.setAllowFlight(true);
			p.setFlying(true);
			pli.scoreboardManager.updateScoreboard(plugin, this);
			if (!plugin.getConfig().getBoolean("config.last_man_standing_wins")) {
				if (this.getPlayerAlive() < 1) {
					final Arena a = this;
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						public void run() {
							a.stop();
						}
					}, 20L);
				} else {
					final Location temp = this.spawns.get(0);
					try {
						Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							public void run() {
								if (specspawn != null) {
									Util.teleportPlayerFixed(p, specspawn);
								} else {
									Util.teleportPlayerFixed(p, temp.clone().add(0D, 30D, 0D));
								}
							}
						}, 2L);
					} catch (Exception e) {
						if (specspawn != null) {
							Util.teleportPlayerFixed(p, specspawn);
						} else {
							Util.teleportPlayerFixed(p, temp.clone().add(0D, 30D, 0D));
						}
					}
				}
			} else {
				if (this.getPlayerAlive() < 2) {
					final Arena a = this;
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						public void run() {
							a.stop();
						}
					}, 20L);
				} else {
					final Location temp = this.spawns.get(0);
					try {
						Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							public void run() {
								if (specspawn != null) {
									Util.teleportPlayerFixed(p, specspawn);
								} else {
									Util.teleportPlayerFixed(p, temp.clone().add(0D, 30D, 0D));
								}
							}
						}, 2L);
					} catch (Exception e) {
						if (specspawn != null) {
							Util.teleportPlayerFixed(p, specspawn);
						} else {
							Util.teleportPlayerFixed(p, temp.clone().add(0D, 30D, 0D));
						}
					}
				}
			}
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					Util.clearInv(p);
					Util.giveSpectatorItems(plugin, p);
				}
			}, 3L);
		}
	}

	public void spectateArcade(String playername) {
		Player p = Bukkit.getPlayer(playername);
		pli.global_players.put(playername, currentarena);
		pli.global_arcade_spectator.put(playername, currentarena);
		Util.teleportPlayerFixed(p, currentarena.getSpawns().get(0).clone().add(0D, 30D, 0D));
		p.setAllowFlight(true);
		p.setFlying(true);
		pli.getSpectatorManager().setSpectate(p, true);
	}

	int currentlobbycount = 10;
	int currentingamecount = 10;
	int currenttaskid = 0;

	public void setTaskId(int id) {
		this.currenttaskid = id;
	}

	public int getTaskId() {
		return this.currenttaskid;
	}

	/**
	 * Starts the lobby countdown and the arena afterwards
	 * 
	 * You can insta-start an arena by using Arena.start();
	 */
	public void startLobby() {
		startLobby(true);
	}

	public void startLobby(final boolean countdown) {
		if (currentstate != ArenaState.JOIN) {
			return;
		}
		this.setArenaState(ArenaState.STARTING);
		Util.updateSign(plugin, this);
		currentlobbycount = pli.lobby_countdown;
		final Arena a = this;

		// skip countdown
		if (!countdown) {
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					currentarena.getArena().start(true);
				}
			}, 10L);
		}

		Sound lobbycountdown_sound_ = null;
		try {
			lobbycountdown_sound_ = Sound.valueOf(plugin.getConfig().getString("config.sounds.lobby_countdown"));
		} catch (Exception e) {
			;
		}
		final Sound lobbycountdown_sound = lobbycountdown_sound_;

		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				currentlobbycount--;
				if (currentlobbycount == 60 || currentlobbycount == 30 || currentlobbycount == 15 || currentlobbycount == 10 || currentlobbycount < 6) {
					for (String p_ : a.getAllPlayers()) {
						if (Validator.isPlayerOnline(p_)) {
							Player p = Bukkit.getPlayer(p_);
							if (countdown) {
								Util.sendMessage(plugin, p, pli.getMessagesConfig().teleporting_to_arena_in.replaceAll("<count>", Integer.toString(currentlobbycount)));
								if (lobbycountdown_sound != null) {
									p.playSound(p.getLocation(), lobbycountdown_sound, 1F, 0F);
								}
							}
						}
					}
				}
				for (String p_ : a.getAllPlayers()) {
					if (Validator.isPlayerOnline(p_)) {
						Player p = Bukkit.getPlayer(p_);
						p.setExp(1F * ((1F * currentlobbycount) / (1F * pli.lobby_countdown)));
					}
				}
				if (currentlobbycount < 1) {
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						public void run() {
							currentarena.getArena().start(true);
						}
					}, 10L);
					try {
						Bukkit.getScheduler().cancelTask(currenttaskid);
					} catch (Exception e) {
					}
				}
			}
		}, 5L, 20).getTaskId();
	}

	/**
	 * Instantly starts the arena, teleports players and udpates the arena
	 */
	public void start(boolean tp) {
		try {
			Bukkit.getScheduler().cancelTask(currenttaskid);
		} catch (Exception e) {
		}
		currentingamecount = pli.ingame_countdown;
		if (tp) {
			pspawnloc = Util.teleportAllPlayers(currentarena.getArena().getAllPlayers(), currentarena.getArena().spawns);
		}
		boolean clearinv = plugin.getConfig().getBoolean("config.clearinv_while_ingamecountdown");
		for (String p_ : currentarena.getArena().getAllPlayers()) {
			Player p = Bukkit.getPlayer(p_);
			p.setWalkSpeed(0.0F);
			p.setFoodLevel(5);
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -7)); // -5
			pli.scoreboardLobbyManager.removeScoreboard(this.getInternalName(), p);
			if (clearinv) {
				Util.clearInv(p);
			}
		}
		final Arena a = this;
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				pli.scoreboardManager.updateScoreboard(plugin, a);
			}
		}, 20L);
		startedIngameCountdown = true;
		if (!plugin.getConfig().getBoolean("config.ingame_countdown_enabled")) {
			startRaw(a);
			return;
		}

		Sound ingamecountdown_sound_ = null;
		try {
			ingamecountdown_sound_ = Sound.valueOf(plugin.getConfig().getString("config.sounds.ingame_countdown"));
		} catch (Exception e) {
			;
		}
		final Sound ingamecountdown_sound = ingamecountdown_sound_;

		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				currentingamecount--;
				if (currentingamecount == 60 || currentingamecount == 30 || currentingamecount == 15 || currentingamecount == 10 || currentingamecount < 6) {
					for (String p_ : a.getAllPlayers()) {
						if (Validator.isPlayerOnline(p_)) {
							Player p = Bukkit.getPlayer(p_);
							Util.sendMessage(plugin, p, pli.getMessagesConfig().starting_in.replaceAll("<count>", Integer.toString(currentingamecount)));
							if (ingamecountdown_sound != null) {
								p.playSound(p.getLocation(), ingamecountdown_sound, 1F, 0F);
							}
						}
					}
				}
				for (String p_ : a.getAllPlayers()) {
					if (Validator.isPlayerOnline(p_)) {
						Player p = Bukkit.getPlayer(p_);
						p.setExp(1F * ((1F * currentingamecount) / (1F * pli.ingame_countdown)));
					}
				}
				if (currentingamecount < 1) {
					startRaw(a);
				}
			}
		}, 5L, 20).getTaskId();

		for (final String p_ : this.getAllPlayers()) {
			if (pli.getShopHandler().hasItemBought(p_, "coin_boost2")) {
				global_coin_multiplier = 2;
				break;
			}
			if (pli.getShopHandler().hasItemBought(p_, "coin_boost3")) {
				global_coin_multiplier = 3;
				break;
			}
		}
	}

	public void startRaw(final Arena a) {
		currentarena.getArena().setArenaState(ArenaState.INGAME);
		startedIngameCountdown = false;
		Util.updateSign(plugin, a);
		Bukkit.getServer().getPluginManager().callEvent(new ArenaStartEvent(plugin, this));
		boolean send_game_started_msg = plugin.getConfig().getBoolean("config.send_game_started_msg");
		for (String p_ : a.getAllPlayers()) {
			try {
				if (!pli.global_lost.containsKey(p_)) {
					Player p = Bukkit.getPlayer(p_);
					if (plugin.getConfig().getBoolean("config.auto_add_default_kit")) {
						if (!pli.getClassesHandler().hasClass(p_)) {
							pli.getClassesHandler().setClass("default", p_, false);
						}
						pli.getClassesHandler().getClass(p_);
					} else {
						Util.clearInv(Bukkit.getPlayer(p_));
						pli.getClassesHandler().getClass(p_);
					}
					if (plugin.getConfig().getBoolean("config.shop_enabled")) {
						pli.getShopHandler().giveShopItems(p);
					}
					p.setFlying(false);
					p.setAllowFlight(false);
				}
			} catch (Exception e) {
				System.out.println("Failed to set class: " + e.getMessage() + " at [1] " + e.getStackTrace()[1].getLineNumber() + " [0] " + e.getStackTrace()[0].getLineNumber());
			}
			Player p = Bukkit.getPlayer(p_);
			p.setWalkSpeed(0.2F);
			p.setFoodLevel(20);
			p.removePotionEffect(PotionEffectType.JUMP);
			if (send_game_started_msg) {
				p.sendMessage(pli.getMessagesConfig().game_started);
			}
		}
		if (plugin.getConfig().getBoolean("config.bungee.whitelist_while_game_running")) {
			Bukkit.setWhitelist(true);
		}
		spectator_task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			public void run() {
				// check if any spectator is near a player
				try {
					for (String p_ : a.getAllPlayers()) {
						if (!pli.global_lost.containsKey(p_)) {
							continue;
						}
						Player p = Bukkit.getPlayer(p_);
						if (p != null) {
							for (String p__ : a.getAllPlayers()) {
								if (p_ != p__) {
									Player p2 = Bukkit.getPlayer(p__);
									if ((Math.abs(p.getLocation().getBlockX() - p2.getLocation().getBlockX()) < 4) && (Math.abs(p.getLocation().getBlockZ() - p2.getLocation().getBlockZ()) < 4) && (Math.abs(p.getLocation().getBlockY() - p2.getLocation().getBlockY()) < 4)) {
										Vector direction = p2.getLocation().add(0D, -0.5D, 0D).toVector().subtract(p.getLocation().toVector()).normalize().multiply(-1.15D);
										p.setVelocity(direction);
										if (p.isInsideVehicle()) {
											p.getVehicle().setVelocity(direction.multiply(2D));
										}
										break;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					if (spectator_task != null) {
						spectator_task.cancel();
					}
				}
			}
		}, 10L, 10L);
		started = true;
		Bukkit.getServer().getPluginManager().callEvent(new ArenaStartedEvent(plugin, this));
		started();
		try {
			Bukkit.getScheduler().cancelTask(currenttaskid);
		} catch (Exception e) {
		}

		// Maximum game time:
		maximum_game_time = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				for (String p_ : a.getAllPlayers()) {
					if (Validator.isPlayerValid(plugin, p_, a)) {
						Bukkit.getPlayer(p_).sendMessage(pli.getMessagesConfig().stop_cause_maximum_game_time);
					}
				}
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					public void run() {
						a.stop();
					}
				}, 5 * 20L);
			}
		}, 20L * 60L * (long) plugin.getConfig().getDouble("config.default_max_game_time_in_minutes") - 5 * 20L);
	}

	/**
	 * Gets executed after an arena started (after lobby countdown)
	 */
	public void started() {
		System.out.println(this.getInternalName() + " started.");
	}

	boolean temp_delay_stopped = false;

	/**
	 * Stops the arena and teleports all players to the mainlobby
	 */
	public void stop() {
		Bukkit.getServer().getPluginManager().callEvent(new ArenaStopEvent(plugin, this));
		final Arena a = this;
		if (spectator_task != null) {
			spectator_task.cancel();
		}
		if (maximum_game_time != null) {
			maximum_game_time.cancel();
		}
		if (!temp_delay_stopped) {
			if (plugin.getConfig().getBoolean("config.delay.enabled")) {
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					public void run() {
						temp_delay_stopped = true;
						a.stop();
					}
				}, plugin.getConfig().getInt("config.delay.amount_seconds") * 20L);
				this.setArenaState(ArenaState.RESTARTING);
				Util.updateSign(plugin, this);
				if (plugin.getConfig().getBoolean("config.spawn_fireworks_for_winners")) {
					if (this.getAllPlayers().size() > 0) {
						Util.spawnFirework(Bukkit.getPlayer(this.getAllPlayers().get(0)));
					}
				}
				return;
			}
		}
		temp_delay_stopped = false;

		try {
			Bukkit.getScheduler().cancelTask(currenttaskid);
		} catch (Exception e) {

		}

		this.setArenaState(ArenaState.RESTARTING);

		final ArrayList<String> temp = new ArrayList<String>(this.getAllPlayers());
		for (final String p_ : temp) {
			try {
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					public void run() {
						if (Validator.isPlayerOnline(p_)) {
							for (Entity e : Bukkit.getPlayer(p_).getNearbyEntities(50, 50, 50)) {
								if (e.getType() == EntityType.DROPPED_ITEM || e.getType() == EntityType.SLIME || e.getType() == EntityType.ZOMBIE || e.getType() == EntityType.SKELETON || e.getType() == EntityType.SPIDER || e.getType() == EntityType.CREEPER) {
									e.remove();
								}
							}
						}
					}
				}, 10L);
			} catch (Exception e) {
				System.out.println("Failed clearing entities.");
			}
			leavePlayer(p_, false, true);
		}

		try {
			for (ItemStack item : global_drops) {
				if (item != null) {
					item.setType(Material.AIR);
				}
			}
		} catch (Exception e) {
			System.out.println("Failed clearing items: " + e.getMessage());
		}

		if (a.getArenaType() == ArenaType.REGENERATION) {
			reset();
		} else {
			a.setArenaState(ArenaState.JOIN);
			Util.updateSign(plugin, a);
		}

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				players.clear();
				pinv.clear();
				pinv_armor.clear();
				pnoreward.clear();
				for (IconMenu im : pli.getClassesHandler().lasticonm.values()) {
					im.destroy();
				}
				pli.getClassesHandler().lasticonm.clear();
			}
		}, 10L);

		started = false;
		startedIngameCountdown = false;

		temp_countdown = true;
		skip_join_lobby = false;
		currentspawn = 0;

		try {
			pli.scoreboardManager.clearScoreboard(this.getInternalName());
			pli.scoreboardLobbyManager.clearScoreboard(this.getInternalName());
		} catch (Exception e) {
			//
		}

		/*
		 * try { pli.getStatsInstance().updateSkulls(); } catch (Exception e) {
		 * 
		 * }
		 */

		if (plugin.getConfig().getBoolean("config.execute_cmds_on_stop")) {
			String[] cmds = plugin.getConfig().getString("config.cmds").split(";");
			for (String cmd : cmds) {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}

		if (plugin.getConfig().getBoolean("config.bungee.teleport_all_to_server_on_stop.tp")) {
			final String server = plugin.getConfig().getString("config.bungee.teleport_all_to_server_on_stop.server");
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					for (Player p : Bukkit.getOnlinePlayers()) {
						BungeeUtil.connectToServer(MinigamesAPI.getAPI(), p.getName(), server);
					}
				}
			}, 30L);
			return;
		}
		if (plugin.getConfig().getBoolean("config.bungee.whitelist_while_game_running")) {
			Bukkit.setWhitelist(false);
		}

		if (ai != null) {
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					if (ai != null) {
						ai.nextMinigame();
						ai = null;
					}
				}
			}, 10L);
		} else {
			// Map rotation only works without Arcade
			// check if there is only one player or none left
			if (temp.size() < 2) {
				return;
			}
			if (plugin.getConfig().getBoolean("config.map_rotation")) {
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					public void run() {
						a.nextArenaOnMapRotation(temp);
					}
				}, 35L);
			}
		}

	}

	/**
	 * Rebuilds an arena from file (only for arenas of REGENERATION type)
	 */
	public void reset() {
		sr.reset();
	}

	/***
	 * Use this when someone got killed/pushed down/eliminated in some way by a player
	 * 
	 * @param playername
	 *            The player that got eliminated
	 */
	public void onEliminated(String playername) {
		if (lastdamager.containsKey(playername)) {
			Player killer = Bukkit.getPlayer(lastdamager.get(playername));
			if (killer != null) {
				pli.getRewardsInstance().giveKillReward(killer.getName(), 2);
				Util.sendMessage(plugin, killer, MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().you_got_a_kill.replaceAll("<player>", playername));
				for (String p_ : this.getAllPlayers()) {
					if (!p_.equalsIgnoreCase(killer.getName())) {
						if (Validator.isPlayerOnline(p_)) {
							Bukkit.getPlayer(p_).sendMessage(MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().player_was_killed_by.replaceAll("<player>", playername).replaceAll("<killer>", killer.getName()));
						}
					}
				}
			}
			lastdamager.remove(playername);
		}
	}

	/**
	 * Will shuffle all arenas and join the next available arena
	 * 
	 * @param players
	 */
	public void nextArenaOnMapRotation(ArrayList<String> players) {
		ArrayList<Arena> arenas = pli.getArenas();
		Collections.shuffle(arenas);
		for (Arena a : arenas) {
			if (a.getArenaState() == ArenaState.JOIN && a != this) {
				System.out.println(plugin.getName() + ": Next arena on map rotation: " + a.getInternalName());
				for (String p_ : players) {
					if (!a.containsPlayer(p_)) {
						a.joinPlayerLobby(p_, false);
					}
				}
			}
		}
	}

	public String getPlayerCount() {
		int alive = 0;
		for (String p_ : getAllPlayers()) {
			if (pli.global_lost.containsKey(p_)) {
				continue;
			} else {
				alive++;
			}
		}
		return Integer.toString(alive) + "/" + Integer.toString(getAllPlayers().size());
	}

	public int getPlayerAlive() {
		int alive = 0;
		for (String p_ : getAllPlayers()) {
			if (pli.global_lost.containsKey(p_)) {
				continue;
			} else {
				alive++;
			}
		}
		return alive;
	}

	public Location getWaitingLobbyTemp() {
		return this.waitinglobby;
	}

	public Location getMainLobbyTemp() {
		return this.mainlobby;
	}

	public ArcadeInstance getArcadeInstance() {
		return ai;
	}

	public boolean isArcadeMain() {
		return isArcadeMain;
	}

	public void setArcadeMain(boolean t) {
		isArcadeMain = t;
	}

	public HashMap<String, Location> getPSpawnLocs() {
		return pspawnloc;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public int getCurrentIngameCountdownTime() {
		return this.currentingamecount;
	}

	public int getCurrentLobbyCountdownTime() {
		return this.currentlobbycount;
	}

	public boolean getIngameCountdownStarted() {
		return this.startedIngameCountdown;
	}

}
