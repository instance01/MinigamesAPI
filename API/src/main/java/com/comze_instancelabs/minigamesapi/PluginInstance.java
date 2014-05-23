package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;

public class PluginInstance {

	private ArenasConfig arenasconfig = null;
	private MessagesConfig messagesconfig = null;
	private JavaPlugin plugin = null;
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	
	public PluginInstance(JavaPlugin plugin, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ArrayList<Arena> arenas){
		this.arenasconfig = arenasconfig;
		this.messagesconfig = messagesconfig;
		this.arenas = arenas;
		this.plugin = plugin;
	}
	
	public ArenasConfig getArenasConfig(){
		return arenasconfig;
	}
	
	public MessagesConfig getMessagesConfig(){
		return messagesconfig;
	}
	
	public ArrayList<Arena> getArenas(){
		return arenas;
	}
	
	public void addArena(Arena arena){
		arenas.add(arena);
	}
	
	public boolean removeArena(Arena arena){
		if(arenas.contains(arena)){
			arenas.remove(arena);
			return true;
		}
		return false;
	}
	
	
}
