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
	
	
	
}
