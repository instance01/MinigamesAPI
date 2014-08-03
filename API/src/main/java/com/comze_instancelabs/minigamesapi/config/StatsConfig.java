package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StatsConfig {

    private FileConfiguration statsConfig = null;
    private File statsFile = null;
    private JavaPlugin plugin = null;
    
    public StatsConfig(JavaPlugin plugin, boolean custom){
    	this.plugin = plugin;
    	if(!custom){
    		this.getConfig().options().header("Used for saving user statistics. Example user stats:");
        	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.wins", 1);
        	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.loses", 1);
        	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.points", 10);
        	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.playername", "InstanceLabs");
    	}
    	this.getConfig().options().copyDefaults(true);
    	this.saveConfig();
    }
    
    public FileConfiguration getConfig() {
        if (statsConfig == null) {
            reloadConfig();
        }
        return statsConfig;
    }
    
    public void saveConfig() {
        if (statsConfig == null || statsFile == null) {
            return;
        }
        try {
            getConfig().save(statsFile);
        } catch (IOException ex) {
            
        }
    }
    
    public void reloadConfig() {
        if (statsFile == null) {
        	statsFile = new File(plugin.getDataFolder(), "stats.yml");
        }
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);

        InputStream defConfigStream = plugin.getResource("stats.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            statsConfig.setDefaults(defConfig);
        }
    }
    
}
