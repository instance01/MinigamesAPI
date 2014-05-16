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

	//TODO add loading/saving into config
	public String no_perm = ChatColor.RED + "You don't have permission.";
	public String successfully_reloaded = ChatColor.GREEN + "Successfully reloaded";
	
    private FileConfiguration arenaConfig = null;
    private File arenaFile = null;
    private JavaPlugin plugin = null;
    
    public MessagesConfig(JavaPlugin plugin){
    	this.plugin = plugin;
    	for(int i = 0; i < 10; i++){
    		squares += Character.toString((char)254);
    	}
    	this.init();
    }
    
    private String squares = Character.toString((char)254);
    
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
    	
    	this.getConfig().options().copyDefaults(true);
    	this.saveConfig();
    }
    
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
