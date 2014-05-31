package com.comze_instancelabs.minigamesapi.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsConfig {

    private FileConfiguration arenaConfig = null;
    private File arenaFile = null;
    private JavaPlugin plugin = null;
    
    public StatsConfig(JavaPlugin plugin){
    	this.plugin = plugin;
    	this.getConfig().options().header("Used for saving user statistics. Example user stats:");
    	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.wins", 1);
    	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.points", 10);
    	this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.name", "InstanceLabs");
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
        	arenaFile = new File(plugin.getDataFolder(), "stats.yml");
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

        InputStream defConfigStream = plugin.getResource("stats.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            arenaConfig.setDefaults(defConfig);
        }
    }
    
}
