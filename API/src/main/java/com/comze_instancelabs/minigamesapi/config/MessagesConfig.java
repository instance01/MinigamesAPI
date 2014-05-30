package com.comze_instancelabs.minigamesapi.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.ArenaState;

public class MessagesConfig {

    private FileConfiguration arenaConfig = null;
    private File arenaFile = null;
    private JavaPlugin plugin = null;

    public MessagesConfig(JavaPlugin plugin){
    	this.plugin = plugin;
    	for(int i = 0; i < 10; i++){
    		squares += Character.toString((char)0x25A0);
    	}
    	this.init();
    }

    private String squares = Character.toString((char)0x25A0);
    
    public String signs_join_0;
    public String signs_join_1;
    public String signs_join_2;
    public String signs_join_3;
    
    public void init(){
    	// all signs
    	for(String state : ArenaState.getAllStateNames()){
    		this.getConfig().addDefault("signs." + state.toLowerCase() + ".0", squares);
        	this.getConfig().addDefault("signs." + state.toLowerCase() + ".1", "<arena>");
        	this.getConfig().addDefault("signs." + state.toLowerCase() + ".2", "<count>/<maxcount>");
        	this.getConfig().addDefault("signs." + state.toLowerCase() + ".3", squares);
    	}

    	this.getConfig().addDefault("messages.no_perm", no_perm);
    	this.getConfig().addDefault("messages.successfully_reloaded", successfully_reloaded);
    	this.getConfig().addDefault("messages.successfully_set", successfully_set);
    	this.getConfig().addDefault("messages.successfully_saved_arena", successfully_saved_arena);
    	this.getConfig().addDefault("messages.arena_invalid", arena_invalid);
    	this.getConfig().addDefault("messages.failed_saving_arena", failed_saving_arena);
    	this.getConfig().addDefault("messages.broadcast_players_left", broadcast_players_left);
    	this.getConfig().addDefault("messages.player_died", player_died);
    	this.getConfig().addDefault("messages.arena_action", arena_action);
    	this.getConfig().addDefault("messages.not_in_arena", not_in_arena);



    	// save
    	this.getConfig().options().copyDefaults(true);
    	this.saveConfig();
    	
    	// load
    	this.no_perm = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no_perm"));
    	this.successfully_reloaded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_reloaded"));
    	this.successfully_set = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_set"));
    	this.successfully_saved_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.successfully_saved_arena"));
    	this.failed_saving_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.failed_saving_arena"));
    	this.arena_invalid = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arena_invalid"));
    	this.player_died = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player_died"));
    	this.broadcast_players_left = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.broadcast_players_left"));
    	this.arena_action = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.arena_action"));
    	this.not_in_arena = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.not_in_arena"));

    
    }
    
	public String no_perm = "&cYou don't have permission.";
	public String successfully_reloaded = "&aSuccessfully reloaded all configs.";
	public String successfully_set = "&aSuccessfully set &3<component>&a.";
	public String successfully_saved_arena = "&aSuccessfully saved &3<arena>&a.";
	public String failed_saving_arena = "&cFailed to save &3<arena>&c.";
	public String arena_invalid = "&3<arena> &cappears to be invalid.";
	public String broadcast_players_left = "&eThere are &4<count> &eplayers left!";
	public String player_died = "&c<player> died.";
	public String arena_action = "&aYou <action> arena &3<arena>&a!";
	public String not_in_arena = "&cYou don't seem to be in an arena right now.";
    
    public FileConfiguration getConfig() {
        if (arenaConfig == null) {
            reloadConfig();
        }
        return arenaConfig;
    }
    
    public void saveConfig() {
        if (arenaConfig == null || arenaFile == null) {
            return;
        }
        try {
            getConfig().save(arenaFile);
        } catch (IOException ex) {
            
        }
    }
    
    public void reloadConfig() {
        if (arenaFile == null) {
        	arenaFile = new File(plugin.getDataFolder(), "messages.yml");
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

        InputStream defConfigStream = plugin.getResource("messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            arenaConfig.setDefaults(defConfig);
        }
    }
    
    
}
