package com.comze_instancelabs.minigamesapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.minigamesapi.util.AClass;

public class ArenaPlayer {

	String playername;
	ItemStack[] inv;
	ItemStack[] armor_inv;
	GameMode original_gamemode = GameMode.SURVIVAL;
	int original_xplvl = 0;
	boolean noreward = false;
	Arena currentArena;
	AClass currentClass;

	private static HashMap<String, ArenaPlayer> players = new HashMap<String, ArenaPlayer>();

	public static ArenaPlayer getPlayerInstance(String playername) {
		if (!players.containsKey(playername)) {
			return new ArenaPlayer(playername);
		} else {
			return players.get(playername);
		}
	}

	public ArenaPlayer(String playername) {
		this.playername = playername;
		players.put(playername, this);
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playername);
	}

	public void setInventories(ItemStack[] inv, ItemStack[] armor_inv) {
		this.inv = inv;
		this.armor_inv = armor_inv;
	}

	public ItemStack[] getInventory() {
		return inv;
	}

	public ItemStack[] getArmorInventory() {
		return armor_inv;
	}

	public GameMode getOriginalGamemode() {
		return original_gamemode;
	}

	public void setOriginalGamemode(GameMode original_gamemode) {
		this.original_gamemode = original_gamemode;
	}

	public int getOriginalXplvl() {
		return original_xplvl;
	}

	public void setOriginalXplvl(int original_xplvl) {
		this.original_xplvl = original_xplvl;
	}

	public boolean isNoReward() {
		return noreward;
	}

	public void setNoReward(boolean noreward) {
		this.noreward = noreward;
	}

	public Arena getCurrentArena() {
		return currentArena;
	}

	public void setCurrentArena(Arena currentArena) {
		this.currentArena = currentArena;
	}

	public AClass getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(AClass currentClass) {
		this.currentClass = currentClass;
	}

}
