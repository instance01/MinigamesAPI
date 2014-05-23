package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;

public class Validator {

	/***
	 * returns true if given player is online
	 * @param arena
	 * @return
	 */
	public static boolean isPlayerOnline(String player){
		Player p = Bukkit.getPlayer(player);
		if(p != null){
			return true;
		}
		return false;
	}
	
	/***
	 * returns true if given player is online and in arena
	 * @param arena
	 * @return
	 */
	public static boolean isPlayerValid(String player, Arena arena){
		return isPlayerValid(player, arena.getName());
	}
	
	/***
	 * returns true if given player is online and in arena
	 * @param arena
	 * @return
	 */
	public static boolean isPlayerValid(String player, String arena){
		if(!isPlayerOnline(player)){
			return false;
		}

		//TODO important, add that

		return false;
	}
	
	/***
	 * returns true if given arena was set up correctly
	 * @param arena
	 * @return
	 */
	@Deprecated
	public static boolean isArenaValid(JavaPlugin plugin, Arena arena){
		return isArenaValid(plugin, arena.getName());
	}

	/***
	 * returns true if given arena was set up correctly
	 * @param arena
	 * @return
	 */
	public static boolean isArenaValid(JavaPlugin plugin, String arena){

		//TODO important, add that

		return false;
	}
	
}
