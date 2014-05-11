package com.comze_instancelabs.minigamesapi.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class Util {

	public static void clearInv(Player p){
		p.getInventory().clear();
		p.updateInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.updateInventory();
	}

	public static void teleportPlayerFixed(Player p, Location l) {
		//TODO might need Runnable fix?
		p.teleport(l, TeleportCause.PLUGIN);
		l.getWorld().refreshChunk(l.getChunk().getX(), l.getChunk().getZ());
	}
	
	// singlespawn
	public static void teleportAllPlayers(ArrayList<String> players, final Location l){
		Long delay = 1L;
		for(String pl : players){
			if(!Validator.isPlayerOnline(pl)){
				continue;
			}
			final Player p = Bukkit.getPlayer(pl);
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable(){
				public void run(){
					Util.teleportPlayerFixed(p, l);
				}
			}, delay);
			delay++;
		}
	}
	
	// multispawn
	public static void teleportAllPlayers(ArrayList<String> players, ArrayList<Location> locs){
		//TODO 
	}
	
}
