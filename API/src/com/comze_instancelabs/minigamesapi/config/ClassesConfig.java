package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClassesConfig {

	private FileConfiguration classesConfig = null;
	private File classesFile = null;
	private JavaPlugin plugin = null;

	public ClassesConfig(JavaPlugin plugin, boolean custom) {
		this.plugin = plugin;
		if (!custom) {
			this.getConfig().options().header("Used for saving classes. Default class:");
			this.getConfig().addDefault("config.kits.default.name", "default");
			this.getConfig().addDefault("config.kits.default.enabled", true);
			this.getConfig().addDefault("config.kits.default.items", "351:5#DAMAGE_ALL:1#KNOCKBACK*1");
			this.getConfig().addDefault("config.kits.default.icon", "351:5#DAMAGE_ALL:1#KNOCKBACK*1");
			this.getConfig().addDefault("config.kits.default.lore", "The default class.;Second line");
			this.getConfig().addDefault("config.kits.default.requires_money", false);
			this.getConfig().addDefault("config.kits.default.requires_permission", false);
			this.getConfig().addDefault("config.kits.default.money_amount", 100);
			this.getConfig().addDefault("config.kits.default.permission_node", "minigames.kits.default");
		}
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	public FileConfiguration getConfig() {
		if (classesConfig == null) {
			reloadConfig();
		}
		return classesConfig;
	}

	public void saveConfig() {
		if (classesConfig == null || classesFile == null) {
			return;
		}
		try {
			getConfig().save(classesFile);
		} catch (IOException ex) {

		}
	}

	public void reloadConfig() {
		if (classesFile == null) {
			classesFile = new File(plugin.getDataFolder(), "classes.yml");
		}
		classesConfig = YamlConfiguration.loadConfiguration(classesFile);

		InputStream defConfigStream = plugin.getResource("classes.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			classesConfig.setDefaults(defConfig);
		}
	}

}
