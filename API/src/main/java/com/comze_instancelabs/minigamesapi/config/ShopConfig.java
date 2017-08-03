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

public class ShopConfig
{
    
    private FileConfiguration shopConfig = null;
    private File              shopFile   = null;
    private JavaPlugin        plugin     = null;
    
    public ShopConfig(final JavaPlugin plugin, final boolean custom)
    {
        this.plugin = plugin;
        if (!custom)
        {
            this.getConfig().options().header("Used for saving shop items. Default shop items:");
            this.getConfig().addDefault("config.shop_items.coin_boost2.name", "Coin boost * 2");
            this.getConfig().addDefault("config.shop_items.coin_boost2.enabled", true);
            this.getConfig().addDefault("config.shop_items.coin_boost2.uses_items", false);
            this.getConfig().addDefault("config.shop_items.coin_boost2.items", "388*1");
            this.getConfig().addDefault("config.shop_items.coin_boost2.icon", "388*1");
            this.getConfig().addDefault("config.shop_items.coin_boost2.lore", "Will give all winners a double money reward boost.");
            this.getConfig().addDefault("config.shop_items.coin_boost2.requires_money", true);
            this.getConfig().addDefault("config.shop_items.coin_boost2.requires_permission", false);
            this.getConfig().addDefault("config.shop_items.coin_boost2.money_amount", 1500);
            this.getConfig().addDefault("config.shop_items.coin_boost2.permission_node", MinigamesAPI.getAPI().getPermissionShopPrefix() + ".coin_boost2");
            
            this.getConfig().addDefault("config.shop_items.coin_boost3.name", "Coin boost * 3");
            this.getConfig().addDefault("config.shop_items.coin_boost3.enabled", true);
            this.getConfig().addDefault("config.shop_items.coin_boost3.uses_items", false);
            this.getConfig().addDefault("config.shop_items.coin_boost3.items", "388*2");
            this.getConfig().addDefault("config.shop_items.coin_boost3.icon", "388*2");
            this.getConfig().addDefault("config.shop_items.coin_boost3.lore", "Will give all winners a triple money reward boost.");
            this.getConfig().addDefault("config.shop_items.coin_boost3.requires_money", true);
            this.getConfig().addDefault("config.shop_items.coin_boost3.requires_permission", false);
            this.getConfig().addDefault("config.shop_items.coin_boost3.money_amount", 3000);
            this.getConfig().addDefault("config.shop_items.coin_boost3.permission_node", MinigamesAPI.getAPI().getPermissionShopPrefix() + ".coin_boost3");
            
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.name", "Coin boost * 2 Solo");
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.enabled", true);
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.uses_items", false);
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.items", "264*1");
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.icon", "264*1");
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.lore", "Will give you a double money reward boost.");
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.requires_money", true);
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.requires_permission", false);
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.money_amount", 1000);
            this.getConfig().addDefault("config.shop_items.coin_boost2_solo.permission_node", MinigamesAPI.getAPI().getPermissionShopPrefix() + ".coin_boost2_solo");
            
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.name", "Coin boost * 3 Solo");
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.enabled", true);
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.uses_items", false);
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.items", "264*2");
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.icon", "264*2");
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.lore", "Will give you a triple money reward boost.");
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.requires_money", true);
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.requires_permission", false);
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.money_amount", 2000);
            this.getConfig().addDefault("config.shop_items.coin_boost3_solo.permission_node", MinigamesAPI.getAPI().getPermissionShopPrefix() + ".coin_boost3_solo");
        }
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
    public FileConfiguration getConfig()
    {
        if (this.shopConfig == null)
        {
            this.reloadConfig();
        }
        return this.shopConfig;
    }
    
    public void saveConfig()
    {
        if (this.shopConfig == null || this.shopFile == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.shopFile);
        }
        catch (final IOException ex)
        {
            // silently ignore
        }
    }
    
    public void reloadConfig()
    {
        if (this.shopFile == null)
        {
            this.shopFile = new File(this.plugin.getDataFolder(), "shop.yml");
        }
        this.shopConfig = YamlConfiguration.loadConfiguration(this.shopFile);
        
        final InputStream defConfigStream = this.plugin.getResource("shop.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.shopConfig.setDefaults(defConfig);
        }
    }
    
}
