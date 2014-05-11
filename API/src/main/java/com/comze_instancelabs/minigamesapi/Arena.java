package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Arena {

	// multispawn
	private ArrayList<Location> multispawns = new ArrayList<Location>();
	private HashMap<String, ItemStack[]> pinv = new HashMap<String, ItemStack[]>();
	private HashMap<String, ItemStack[]> pinv_armor = new HashMap<String, ItemStack[]>();
	
	// singlespawn
	private Location spawn;
	
	private Location mainlobby;
	private Location waitinglobby;
	private Location signloc;
	
	private int max_players;
	private int min_players;
	
	private boolean viparena;
	private String permission_node;
	
	private ArrayList<String> players = new ArrayList<String>();
	
	private ArenaType type = ArenaType.SINGLESPAWN;
	private ArenaState currentstate = ArenaState.JOIN;
	private String name = "mainarena";
	
	private boolean shouldClearInventoryOnJoin = true;
	
	private Arena currentarena;
	
	/***
	 * Creates a normal singlespawn arena
	 * @param name
	 */
	public Arena(String name){
		currentarena = this;
		this.name = name;
	}
	
	/***
	 * Creates an arena of given arenatype
	 * @param name
	 * @param type
	 */
	public Arena(String name, ArenaType type) {
		this(name);
		this.type = type;
	}
	
	// This is for loading existing arenas
	public void init(Location signloc, Location spawn, Location mainlobby, Location waitinglobby, int max_players, int min_players, boolean viparena){
		this.signloc = signloc;
		this.spawn = spawn;
		this.mainlobby = mainlobby;
		this.waitinglobby = waitinglobby;
		this.viparena = viparena;
		this.min_players = min_players;
		this.max_players = max_players;
	}

	public Arena getArena(){
		return this;
	}
	
	public boolean isVIPArena(){
		return this.viparena;
	}
	
	public void setVIPArena(boolean t){
		this.viparena = t;
	}
	
	public ArrayList<String> getAllPlayers(){
		return this.players;
	}
	
	public boolean containsPlayer(String playername){
		return players.contains(playername);
	}
	
	public ArenaState getArenaState(){
		return this.currentstate;
	}
	
	public void setArenaState(ArenaState s){
		this.currentstate = s;
	}
	
	public ArenaType getArenaType(){
		return this.type;
	}
	
	/**
	 * Joins the waiting lobby of an arena
	 * @param playername
	 */
	public void joinPlayerLobby(String playername){
		MinigamesAPI.getAPI().global_players.put(playername, this);
		this.players.add(playername);
		if(Validator.isPlayerValid(playername, this)){
			final Player p = Bukkit.getPlayer(playername);
			if(shouldClearInventoryOnJoin){
				pinv.put(playername, p.getInventory().getContents());
				pinv_armor.put(playername, p.getInventory().getArmorContents());
				if(this.getArenaType() == ArenaType.JUMPNRUN){
					Util.teleportPlayerFixed(p, this.spawn);
				}else{
					Util.teleportPlayerFixed(p, this.waitinglobby);
				}
				Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable(){
					public void run(){
						Util.clearInv(p);
					}
				}, 10L);
				
			}
		}
	}
	
	/***
	 * Leaves the current arena, won't do anything if not present in any arena
	 * @param playername
	 * @param fullLeave Determines if player left only minigame or the server
	 */
	public void leavePlayer(String playername, boolean fullLeave){
		if(!this.containsPlayer(playername)){
			return;
		}
		this.players.remove(playername);
		MinigamesAPI.global_players.remove(playername);
		if(fullLeave){
			// TODO temporary save player to restore his stuff and teleport him into lobby when he joins back
			return;
		}
		Player p = Bukkit.getPlayer(playername);
		Util.clearInv(p);
		p.getInventory().setContents(pinv.get(playername));
		p.getInventory().setArmorContents(pinv_armor.get(playername));
		p.updateInventory();
		
		//TODO might need delay through runnable, will bring issues on laggier servers
		Util.teleportPlayerFixed(p, this.waitinglobby);
	}
	
	int currentlobbycount = 0;
	int currentingamecount = 0;
	int currenttaskid = 0;
	public void startLobby(){
		if(currentstate != ArenaState.JOIN){
			return;
		}
		this.setArenaState(ArenaState.STARTING);
		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable(){
			public void run(){
				currentlobbycount++;
				if(currentlobbycount > MinigamesAPI.getAPI().lobby_countdown){
					currentarena.getArena().start();
					try{
						Bukkit.getScheduler().cancelTask(currenttaskid);
					}catch(Exception e){}
				}
			}
		}, 5L, 20).getTaskId();
	}
	
	public void start(){
		if(currentarena.getArena().getArenaType() == ArenaType.SINGLESPAWN){
			Util.teleportAllPlayers(currentarena.getArena().getAllPlayers(), currentarena.getArena().spawn);
		}else if(currentarena.getArena().getArenaType() == ArenaType.MULTISPAWN){
			Util.teleportAllPlayers(currentarena.getArena().getAllPlayers(), currentarena.getArena().multispawns);
		}
		currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), new Runnable(){
			public void run(){
				currentingamecount++;
				if(currentingamecount > MinigamesAPI.getAPI().ingame_countdown){
					currentarena.getArena().setArenaState(ArenaState.INGAME);
					try{
						Bukkit.getScheduler().cancelTask(currenttaskid);
					}catch(Exception e){}
				}
			}
		}, 5L, 20).getTaskId();
	}
	
	public void stop(){
		
	}
	
	public void reset(){
		
	}
	
	
}
