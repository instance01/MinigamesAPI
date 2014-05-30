package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Arena {

	// Plugin the arena belongs to
	JavaPlugin plugin;

	private ArrayList<Location> spawns = new ArrayList<Location>();
	private HashMap<String, ItemStack[]> pinv = new HashMap<String, ItemStack[]>();
	private HashMap<String, ItemStack[]> pinv_armor = new HashMap<String, ItemStack[]>();
	private HashMap<String, GameMode> pgamemode = new HashMap<String, GameMode>();

	private Location mainlobby;
	private Location waitinglobby;
	private Location signloc;

	private int max_players;
	private int min_players;

	private boolean viparena;
	private String permission_node;

	private ArrayList<String> players = new ArrayList<String>();

	private ArenaType type = ArenaType.DEFAULT;
	private ArenaState currentstate = ArenaState.JOIN;
	String name = "mainarena";

	private boolean shouldClearInventoryOnJoin = true;

	private Arena currentarena;

	/**
	 * Creates a normal singlespawn arena
	 * 
	 * @param name
	 */
	public Arena(JavaPlugin plugin, String name) {
		currentarena = this;
		this.plugin = plugin;
		this.name = name;
	}

	/**
	 * Creates an arena of given arenatype
	 * 
	 * @param name
	 * @param type
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
	}

	// This is for loading existing arenas
	public Arena initArena(Location signloc, ArrayList<Location> spawn, Location mainlobby, Location waitinglobby, int max_players, int min_players, boolean viparena) {
		this.init(signloc, spawn, mainlobby, waitinglobby, max_players, min_players, viparena);
		return this;
	}

	public Arena getArena() {
		return this;
	}

	public Location getSignLocation() {
		return this.signloc;
	}

	public String getName() {
		return name;
	}

	public int getMaxPlayers() {
		return this.max_players;
	}

	public int getMinPlayers() {
		return this.min_players;
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
	 */
	public void joinPlayerLobby(String playername) {
		if (this.getArenaState() != ArenaState.JOIN && this.getArenaState() != ArenaState.STARTING) {
			// arena ingame or restarting
			return;
		}
		if (this.getAllPlayers().size() > this.max_players - 1) {
			// arena full
			return;
		}
		MinigamesAPI.getAPI().global_players.put(playername, this);
		this.players.add(playername);
		if (Validator.isPlayerValid(playername, this)) {
			final Player p = Bukkit.getPlayer(playername);
			if (shouldClearInventoryOnJoin) {
				pinv.put(playername, p.getInventory().getContents());
				pinv_armor.put(playername, p.getInventory().getArmorContents());
				if (this.getArenaType() == ArenaType.JUMPNRUN) {
					Util.teleportPlayerFixed(p, this.spawns.get(0));
				} else {
					Util.teleportPlayerFixed(p, this.waitinglobby);
				}
				Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
					public void run() {
						Util.clearInv(p);
						p.getInventory().addItem(new ItemStack(plugin.getConfig().getInt("config.classes_selection_item")));
						p.updateInventory();
						pgamemode.put(p.getName(), p.getGameMode());
						p.setGameMode(GameMode.SURVIVAL);
					}
				}, 10L);
				if (this.getAllPlayers().size() > this.min_players - 1) {
					this.startLobby();
				}
			}
		}
	}

	/**
	 * Leaves the current arena, won't do anything if not present in any arena
	 * 
	 * @param playername
	 * @param fullLeave
	 *            Determines if player left only minigame or the server
	 */
	public void leavePlayer(String playername, boolean fullLeave) {
		if (!this.containsPlayer(playername)) {
			return;
		}
		this.players.remove(playername);
		MinigamesAPI.global_players.remove(playername);
		if (fullLeave) {
			// TODO temporary save player to restore his stuff and teleport him
			// into lobby when he joins back
			return;
		}
		final Player p = Bukkit.getPlayer(playername);
		Util.clearInv(p);
		p.getInventory().setContents(pinv.get(playername));
		p.getInventory().setArmorContents(pinv_armor.get(playername));
		p.updateInventory();

		MinigamesAPI.getAPI().pinstances.get(plugin).getRewardsInstance().giveReward(p.getName());

		// TODO might need delay through runnable, will bring issues on laggier
		// servers
		Util.teleportPlayerFixed(p, this.mainlobby);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				p.setFlying(false);
				if (!p.isOp()) {
					p.setAllowFlight(false);
				}
				if (pgamemode.containsKey(p.getName())) {
					p.setGameMode(pgamemode.get(p.getName()));
				}
			}
		}, 5L);
	}

	public void spectate(String playername) {
		if (Validator.isPlayerValid(playername, this)) {
			Player p = Bukkit.getPlayer(playername);
			MinigamesAPI.getAPI().global_lost.put(playername, this);
			p.setAllowFlight(true);
			p.setFlying(true);
			Util.teleportPlayerFixed(p, this.spawns.get(0).add(0D, 30D, 0D));
		}
	}

	int currentlobbycount = 0;
	int currentingamecount = 0;
	int currenttaskid = 0;

	/**
	 * Starts the lobby countdown and the arena afterwards You can insta-start
	 * an arena by using Arena.start();
	 */
	public void startLobby() {
		if (currentstate != ArenaState.JOIN) {
			return;
		}
		this.setArenaState(ArenaState.STARTING);
		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				currentlobbycount++;
				if (currentlobbycount > MinigamesAPI.getAPI().lobby_countdown) {
					currentarena.getArena().start();
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
	public void start() {
		Util.teleportAllPlayers(currentarena.getArena().getAllPlayers(), currentarena.getArena().spawns);
		final Arena a = this;
		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				currentingamecount++;
				if (currentingamecount > MinigamesAPI.getAPI().ingame_countdown) {
					currentarena.getArena().setArenaState(ArenaState.INGAME);
					for (String p_ : a.getAllPlayers()) {
						if (!Classes.hasClass(plugin, p_)) {
							Classes.setClass(plugin, "default", p_);
						}
						Classes.getClass(plugin, p_);
					}
					try {
						Bukkit.getScheduler().cancelTask(currenttaskid);
					} catch (Exception e) {
					}
				}
			}
		}, 5L, 20).getTaskId();
	}

	/**
	 * Stops the arena and teleports all players to the mainlobby
	 */
	public void stop() {
		this.setArenaState(ArenaState.RESTARTING);
		for(String p_ : this.getAllPlayers()){
			leavePlayer(p_, false);
		}
		//Util.teleportAllPlayers(players, mainlobby);
		if (this.getArenaType() == ArenaType.REGENERATION) {
			reset();
		} else {
			this.setArenaState(ArenaState.JOIN);
		}

		// TODO possibly run that stuff later to avoid lag related bugs
		players.clear();
		pinv.clear();
		pinv_armor.clear();
	}

	public void reset() {
		Runnable r = new Runnable() {
			public void run() {
				Util.loadArenaFromFileSYNC(plugin, currentarena);
			}
		};
		new Thread(r).start();
	}

	public String getPlayerCount() {
		int alive = 0;
		for (String p_ : getAllPlayers()) {
			if (MinigamesAPI.getAPI().global_lost.containsKey(p_)) {
				continue;
			} else {
				alive++;
			}
		}
		return Integer.toString(alive) + "/" + Integer.toString(getAllPlayers().size());
	}

}
