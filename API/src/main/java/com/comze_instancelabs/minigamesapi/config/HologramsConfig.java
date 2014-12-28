package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HologramsConfig {

	private FileConfiguration holoConfig = null;
	private File holoFile = null;
	private JavaPlugin plugin = null;

	public HologramsConfig(JavaPlugin plugin, boolean custom) {
		this.plugin = plugin;
		if (!custom) {
			this.getConfig().options().header("Used for saving stats holograms.");
		}
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	public FileConfiguration getConfig() {
		if (holoConfig == null) {
			reloadConfig();
		}
		return holoConfig;
	}

	public void saveConfig() {
		if (holoConfig == null || holoFile == null) {
			return;
		}
		try {
			getConfig().save(holoFile);
		} catch (IOException ex) {

		}
	}

	public void reloadConfig() {
		if (holoFile == null) {
			holoFile = new File(plugin.getDataFolder(), "holograms.yml");
		}
		holoConfig = YamlConfiguration.loadConfiguration(holoFile);

		InputStream defConfigStream = plugin.getResource("holograms.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			holoConfig.setDefaults(defConfig);
		}
	}

}
