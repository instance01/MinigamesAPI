package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Party {

	String owner;
	ArrayList<String> players = new ArrayList<String>();

	public Party(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void addPlayer(String p) {
		if (!players.contains(p)) {
			players.add(p);
		}
		Bukkit.getPlayer(p).sendMessage("You joined " + owner + "s party.");
		tellAll(p + " joined the party.");
	}

	// TODO remove player when he leaves server

	public boolean removePlayer(String p) {
		if (players.contains(p)) {
			players.remove(p);
			Player p___ = Bukkit.getPlayer(p);
			if (p___ != null) {
				p___.sendMessage("You left " + owner + "s party.");
			}
			tellAll(p + " left the party.");
			return true;
		}
		return false;
	}

	public boolean containsPlayer(String p) {
		return players.contains(p);
	}

	public void disband() {
		tellAll("Your party was disbanded.");
		if (MinigamesAPI.getAPI().global_party.containsKey(owner)) {
			MinigamesAPI.getAPI().global_party.remove(owner);
		}
	}

	private void tellAll(String msg) {
		for (String p_ : this.getPlayers()) {
			Player p__ = Bukkit.getPlayer(p_);
			if (p__ != null) {
				p__.sendMessage(msg);
			}
		}
		Player p___ = Bukkit.getPlayer(this.getOwner());
		if (p___ != null) {
			p___.sendMessage(msg);
		}
	}

}
