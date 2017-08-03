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

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class GunsConfig
{
    
    private FileConfiguration arenaConfig = null;
    private File              arenaFile   = null;
    private JavaPlugin        plugin      = null;
    
    // TODO add bullet option
    
    public GunsConfig(final JavaPlugin plugin, final boolean custom)
    {
        this.plugin = plugin;
        if (!custom)
        {
            this.getConfig().options().header("Be aware that this config is barely used in any minigame. \nUsed for saving gun classes. Default:");
            this.getConfig().addDefault("config.guns.pistol.name", "Pistol");
            this.getConfig().addDefault("config.guns.pistol.items", "256#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.pistol.icon", "256#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.pistol.lore", "The Pistol.");
            this.getConfig().addDefault("config.guns.pistol.speed", 1D);
            this.getConfig().addDefault("config.guns.pistol.durability", 50);
            this.getConfig().addDefault("config.guns.pistol.shoot_amount", 1);
            this.getConfig().addDefault("config.guns.pistol.knockback_multiplier", 1.1D);
            this.getConfig().addDefault("config.guns.pistol.permission_node", MinigamesAPI.getAPI().getPermissionGunPrefix() + ".pistol");
            
            this.getConfig().addDefault("config.guns.sniper.name", "Sniper");
            this.getConfig().addDefault("config.guns.sniper.items", "292#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.sniper.icon", "292#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.sniper.lore", "The Sniper.");
            this.getConfig().addDefault("config.guns.sniper.speed", 0.5D);
            this.getConfig().addDefault("config.guns.sniper.durability", 10);
            this.getConfig().addDefault("config.guns.sniper.shoot_amount", 1);
            this.getConfig().addDefault("config.guns.sniper.knockback_multiplier", 3D);
            this.getConfig().addDefault("config.guns.sniper.permission_node", MinigamesAPI.getAPI().getPermissionGunPrefix() + ".sniper");
            
            this.getConfig().addDefault("config.guns.grenade.name", "Grenade Launcher");
            this.getConfig().addDefault("config.guns.grenade.items", "257#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.grenade.icon", "257#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.grenade.lore", "The Grenade Launcher.");
            this.getConfig().addDefault("config.guns.grenade.speed", 0.1D);
            this.getConfig().addDefault("config.guns.grenade.durability", 10);
            this.getConfig().addDefault("config.guns.grenade.shoot_amount", 1);
            this.getConfig().addDefault("config.guns.grenade.knockback_multiplier", 2.5D);
            this.getConfig().addDefault("config.guns.grenade.permission_node", MinigamesAPI.getAPI().getPermissionGunPrefix() + ".grenade");
            
            this.getConfig().addDefault("config.guns.freeze.name", "Freeze Gun");
            this.getConfig().addDefault("config.guns.freeze.items", "258#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.freeze.icon", "258#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.guns.freeze.lore", "The Freeze Gun.");
            this.getConfig().addDefault("config.guns.freeze.speed", 0.8D);
            this.getConfig().addDefault("config.guns.freeze.durability", 5);
            this.getConfig().addDefault("config.guns.freeze.shoot_amount", 1);
            this.getConfig().addDefault("config.guns.freeze.knockback_multiplier", 0.5D);
            this.getConfig().addDefault("config.guns.freeze.permission_node", MinigamesAPI.getAPI().getPermissionGunPrefix() + ".freeze");
        }
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
            this.arenaFile = new File(this.plugin.getDataFolder(), "guns.yml");
        }
        this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
        
        final InputStream defConfigStream = this.plugin.getResource("guns.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.arenaConfig.setDefaults(defConfig);
        }
    }
    
}
