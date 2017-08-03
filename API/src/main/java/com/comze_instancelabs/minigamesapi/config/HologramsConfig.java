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

public class HologramsConfig
{
    
    private FileConfiguration holoConfig = null;
    private File              holoFile   = null;
    private JavaPlugin        plugin     = null;
    
    public HologramsConfig(final JavaPlugin plugin, final boolean custom)
    {
        this.plugin = plugin;
        if (!custom)
        {
            this.getConfig().options().header("Used for saving stats holograms.");
        }
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
    public FileConfiguration getConfig()
    {
        if (this.holoConfig == null)
        {
            this.reloadConfig();
        }
        return this.holoConfig;
    }
    
    public void saveConfig()
    {
        if (this.holoConfig == null || this.holoFile == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.holoFile);
        }
        catch (final IOException ex)
        {
            // silently ignore
        }
    }
    
    public void reloadConfig()
    {
        if (this.holoFile == null)
        {
            this.holoFile = new File(this.plugin.getDataFolder(), "holograms.yml");
        }
        this.holoConfig = YamlConfiguration.loadConfiguration(this.holoFile);
        
        final InputStream defConfigStream = this.plugin.getResource("holograms.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.holoConfig.setDefaults(defConfig);
        }
    }
    
}
