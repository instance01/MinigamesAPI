package com.comze_instancelabs.minigamesapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AchievementsConfig {

    private FileConfiguration arenaConfig = null;
    private File arenaFile = null;
    private JavaPlugin plugin = null;

    public AchievementsConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.getConfig().options().header("Used for saving achievements details.");

        this.getConfig().addDefault("config.enabled", true);

        this.getConfig().addDefault("config.achievements.first_blood.enabled", true);
        this.getConfig().addDefault("config.achievements.first_blood.name", "First Blood!");
        this.getConfig().addDefault("config.achievements.first_blood.reward.economy_reward", true);
        this.getConfig().addDefault("config.achievements.first_blood.reward.econ_reward_amount", 20);
        this.getConfig().addDefault("config.achievements.first_blood.reward.command_reward", false);
        this.getConfig().addDefault("config.achievements.first_blood.reward.cmd", "tell <player> Good job!");

        this.getConfig().addDefault("config.achievements.ten_kills.enabled", true);
        this.getConfig().addDefault("config.achievements.ten_kills.name", "Ten Kills!");
        this.getConfig().addDefault("config.achievements.ten_kills.reward.economy_reward", true);
        this.getConfig().addDefault("config.achievements.ten_kills.reward.econ_reward_amount", 50);
        this.getConfig().addDefault("config.achievements.ten_kills.reward.command_reward", false);
        this.getConfig().addDefault("config.achievements.ten_kills.reward.cmd", "tell <player> Good job!");

        this.getConfig().addDefault("config.achievements.hundred_kills.enabled", true);
        this.getConfig().addDefault("config.achievements.hundred_kills.name", "Hundred Kills!");
        this.getConfig().addDefault("config.achievements.hundred_kills.reward.economy_reward", true);
        this.getConfig().addDefault("config.achievements.hundred_kills.reward.econ_reward_amount", 1000);
        this.getConfig().addDefault("config.achievements.hundred_kills.reward.command_reward", false);
        this.getConfig().addDefault("config.achievements.hundred_kills.reward.cmd", "tell <player> Good job!");

        this.getConfig().addDefault("config.achievements.first_win.enabled", true);
        this.getConfig().addDefault("config.achievements.first_win.name", "Your first win!");
        this.getConfig().addDefault("config.achievements.first_win.reward.economy_reward", true);
        this.getConfig().addDefault("config.achievements.first_win.reward.econ_reward_amount", 30);
        this.getConfig().addDefault("config.achievements.first_win.reward.command_reward", false);
        this.getConfig().addDefault("config.achievements.first_win.reward.cmd", "tell <player> Good job!");

        this.getConfig().addDefault("config.achievements.ten_wins.enabled", true);
        this.getConfig().addDefault("config.achievements.ten_wins.name", "Ten wins!");
        this.getConfig().addDefault("config.achievements.ten_wins.reward.economy_reward", true);
        this.getConfig().addDefault("config.achievements.ten_wins.reward.econ_reward_amount", 30);
        this.getConfig().addDefault("config.achievements.ten_wins.reward.command_reward", false);
        this.getConfig().addDefault("config.achievements.ten_wins.reward.cmd", "tell <player> Good job!");

        this.getConfig().addDefault("config.achievements.achievement_guy.enabled", true);
        this.getConfig().addDefault("config.achievements.achievement_guy.name", "All achievement guy!");
        this.getConfig().addDefault("config.achievements.achievement_guy.reward.economy_reward", true);
        this.getConfig().addDefault("config.achievements.achievement_guy.reward.econ_reward_amount", 30);
        this.getConfig().addDefault("config.achievements.achievement_guy.reward.command_reward", false);
        this.getConfig().addDefault("config.achievements.achievement_guy.reward.cmd", "tell <player> Good job!");

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
            arenaFile = new File(plugin.getDataFolder(), "achievements.yml");
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

        InputStream defConfigStream = plugin.getResource("achievements.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            arenaConfig.setDefaults(defConfig);
        }
    }

}
