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

public class StatsGlobalConfig
{
    
    private FileConfiguration statsConfig = null;
    private File              statsFile   = null;
    private JavaPlugin        plugin      = null;

    public StatsGlobalConfig(final JavaPlugin plugin, final boolean custom)
    {
        this.plugin = plugin;
        if (!custom)
        {
            this.getConfig().options().header("Used for saving user statistics. Example user stats:");
            this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.wins", 1);
            this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.loses", 1);
            this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.points", 10);
            this.getConfig().addDefault("players.3c8c41ff-51f5-4b7a-8c2b-44df0beba03b.playername", "TheMrQuake");
        }
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public FileConfiguration getConfig()
    {
        if (this.statsConfig == null)
        {
            this.reloadConfig();
        }
        return this.statsConfig;
    }

    public void saveConfig()
    {
        if (this.statsConfig == null || this.statsFile == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.statsFile);
        }
        catch (final IOException ex)
        {
            // silently ignore
        }
    }

    public void reloadConfig()
    {
        if (this.statsFile == null)
        {
            this.statsFile = new File(this.plugin.getDataFolder(), "global_stats.yml");
        }
        this.statsConfig = YamlConfiguration.loadConfiguration(this.statsFile);
        
        final InputStream defConfigStream = this.plugin.getResource("global_stats.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.statsConfig.setDefaults(defConfig);
        }
    }

}
