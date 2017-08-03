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

public class ClassesConfig
{
    
    private FileConfiguration classesConfig = null;
    private File              classesFile   = null;
    private JavaPlugin        plugin        = null;
    
    public ClassesConfig(final JavaPlugin plugin, final boolean custom)
    {
        this.plugin = plugin;
        if (!custom)
        {
            this.getConfig().options().header("Used for saving classes. Default class:");
            this.getConfig().addDefault("config.kits.default.name", "default");
            this.getConfig().addDefault("config.kits.default.enabled", true);
            this.getConfig().addDefault("config.kits.default.items", "351:5#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.kits.default.icon", "351:5#DAMAGE_ALL:1#KNOCKBACK*1");
            this.getConfig().addDefault("config.kits.default.lore", "The default class.;Second line");
            this.getConfig().addDefault("config.kits.default.requires_money", false);
            this.getConfig().addDefault("config.kits.default.requires_permission", false);
            this.getConfig().addDefault("config.kits.default.money_amount", 100);
            this.getConfig().addDefault("config.kits.default.permission_node", MinigamesAPI.getAPI().getPermissionKitPrefix() + ".default");
        }
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
    public FileConfiguration getConfig()
    {
        if (this.classesConfig == null)
        {
            this.reloadConfig();
        }
        return this.classesConfig;
    }
    
    public void saveConfig()
    {
        if (this.classesConfig == null || this.classesFile == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.classesFile);
        }
        catch (final IOException ex)
        {
            // silently ignore
        }
    }
    
    public void reloadConfig()
    {
        if (this.classesFile == null)
        {
            this.classesFile = new File(this.plugin.getDataFolder(), "classes.yml");
        }
        this.classesConfig = YamlConfiguration.loadConfiguration(this.classesFile);
        
        final InputStream defConfigStream = this.plugin.getResource("classes.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.classesConfig.setDefaults(defConfig);
        }
    }
    
}
