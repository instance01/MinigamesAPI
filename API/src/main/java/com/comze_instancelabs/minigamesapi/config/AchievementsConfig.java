/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.comze_instancelabs.minigamesapi.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AchievementsConfig
{
    
    private FileConfiguration arenaConfig = null;
    private File              arenaFile   = null;
    private JavaPlugin        plugin      = null;
    
    public AchievementsConfig(final JavaPlugin plugin)
    {
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
    
    public FileConfiguration getConfig()
    {
        if (this.arenaConfig == null)
        {
            this.reloadConfig();
        }
        return this.arenaConfig;
    }
    
    public void saveConfig()
    {
        if (this.arenaConfig == null || this.arenaFile == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.arenaFile);
        }
        catch (final IOException ex)
        {
            // silently ignore
        }
    }
    
    public void reloadConfig()
    {
        if (this.arenaFile == null)
        {
            this.arenaFile = new File(this.plugin.getDataFolder(), "achievements.yml");
        }
        this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
        
        final InputStream defConfigStream = this.plugin.getResource("achievements.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.arenaConfig.setDefaults(defConfig);
        }
    }
    
}
