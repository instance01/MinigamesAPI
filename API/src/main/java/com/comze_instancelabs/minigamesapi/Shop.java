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
package com.comze_instancelabs.minigamesapi;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ShopConfig;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.ShopItem;
import com.comze_instancelabs.minigamesapi.util.Util;

import net.milkbowl.vault.economy.EconomyResponse;

public class Shop
{
    
    // Allows buying extra stuff for the games like traits, coin boosters, extra weapons
    // You can change whether the particular item is persistent or just for one game
    
    JavaPlugin                             plugin;
    PluginInstance                         pli;
    public HashMap<String, IconMenu>       lasticonm = new HashMap<>();
    public LinkedHashMap<String, ShopItem> shopitems = new LinkedHashMap<>();
    
    public Shop(final PluginInstance pli, final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.pli = pli;
    }
    
    public void openGUI(final String p)
    {
        IconMenu iconm;
        final int mincount = this.pli.getAClasses().keySet().size();
        if (this.lasticonm.containsKey(p))
        {
            iconm = this.lasticonm.get(p);
        }
        else
        {
            iconm = new IconMenu(this.pli.getMessagesConfig().shop_item,
                    (9 * this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_SHOP_GUI_ROWS) > mincount - 1) ? 9 * this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_SHOP_GUI_ROWS) : Math.round(mincount / 9) * 9 + 9,
                    event -> {
                        if (event.getPlayer().getName().equalsIgnoreCase(p))
                        {
                            if (Shop.this.pli.global_players.containsKey(p))
                            {
                                if (Shop.this.pli.getArenas().contains(Shop.this.pli.global_players.get(p)))
                                {
                                    final String d = event.getName();
                                    final Player p1 = event.getPlayer();
                                    Shop.this.buy(p1, d);
                                }
                            }
                        }
                        event.setWillClose(true);
                    }, this.plugin);
            
            final ShopConfig shopConfig = this.pli.getShopConfig();
            int c = 0;
            for (final String ac : this.shopitems.keySet())
            {
                final ShopItem ac_ = this.shopitems.get(ac);
                if (ac_.isEnabled())
                {
                    int slot = c;
                    if (this.pli.getShopConfig().getConfig().isSet("config.shop_items." + ac_.getInternalName() + ".slot"))
                    {
                        slot = this.pli.getShopConfig().getConfig().getInt("config.shop_items." + ac_.getInternalName() + ".slot");
                        if (slot < 0 || slot > iconm.getSize() - 1)
                        {
                            slot = c;
                        }
                    }
                    String color = ChatColor.GREEN + "";
                    if (shopConfig.getConfig().isSet("players.bought." + p + "." + ac_.getInternalName()))
                    {
                        color = ChatColor.RED + "";
                    }
                    iconm.setOption(slot, ac_.getIcon().clone(), color + ac_.getName(),
                            this.pli.getShopConfig().getConfig().getString("config.shop_items." + ac_.getInternalName() + ".lore").split(";"));
                    c++;
                }
            }
        }
        
        iconm.open(Bukkit.getPlayerExact(p));
        this.lasticonm.put(p, iconm);
    }
    
    public void loadShopItems()
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final FileConfiguration config = Shop.this.pli.getShopConfig().getConfig();
            if (config.isSet("config.shop_items"))
            {
                for (final String aclass : config.getConfigurationSection("config.shop_items.").getKeys(false))
                {
                    final ShopItem n = new ShopItem(Shop.this.plugin, config.getString("config.shop_items." + aclass + ".name"), aclass,
                            config.isSet("config.shop_items." + aclass + ".enabled") ? config.getBoolean("config.shop_items." + aclass + ".enabled") : true,
                            Util.parseItems(config.getString("config.shop_items." + aclass + ".items")), Util.parseItems(config.getString("config.shop_items." + aclass + ".icon")).get(0));
                    Shop.this.shopitems.put(aclass, n);
                }
            }
        }, 20L);
    }
    
    /***
     * Buy a shop item
     * 
     * @param p
     *            Player who buys it
     * @param item_displayname
     *            Displayname of the item
     * @return
     */
    public boolean buy(final Player p, final String item_displayname)
    {
        for (final String ac : this.shopitems.keySet())
        {
            final ShopItem ac_ = this.shopitems.get(ac);
            if (ac_.getName().equalsIgnoreCase(ChatColor.stripColor(item_displayname)))
            {
                this.takeMoney(p, ac_.getInternalName());
                // true -> player has item already or just bought it successfully
                return true;
            }
        }
        return false;
    }
    
    public boolean buyByInternalName(final Player p, final String item_name)
    {
        for (final String ac : this.shopitems.keySet())
        {
            final ShopItem ac_ = this.shopitems.get(ac);
            if (ac_.getInternalName().equalsIgnoreCase(ChatColor.stripColor(item_name)))
            {
                this.takeMoney(p, ac_.getInternalName());
                // true -> player has item already or just bought it successfully
                return true;
            }
        }
        return false;
    }
    
    public boolean hasItemBought(final String p, final String item)
    {
        return this.pli.getShopConfig().getConfig().isSet("players.bought." + p + "." + item);
    }
    
    public boolean requiresMoney(final String item)
    {
        return this.pli.getShopConfig().getConfig().getBoolean("config.shop_items." + item + ".requires_money");
    }
    
    public boolean takeMoney(final Player p, final String item)
    {
        MinigamesAPI.getAPI();
        if (!MinigamesAPI.economy)
        {
            this.plugin.getLogger().warning("Economy is turned OFF. Turn it ON in the config.");
            return false;
        }
        if (!this.requiresMoney(item))
        {
            return false;
        }
        if (MinigamesAPI.economy)
        {
            final ShopConfig shopConfig = this.pli.getShopConfig();
            if (!shopConfig.getConfig().isSet("players.bought." + p.getName() + "." + item))
            {
                final int money = shopConfig.getConfig().getInt("config.shop_items." + item + ".money_amount");
                MinigamesAPI.getAPI();
                if (MinigamesAPI.econ.getBalance(p.getName()) >= money)
                {
                    MinigamesAPI.getAPI();
                    final EconomyResponse r = MinigamesAPI.econ.withdrawPlayer(p.getName(), money);
                    if (!r.transactionSuccess())
                    {
                        p.sendMessage(String.format("An error occured: %s", r.errorMessage));
                        return false;
                    }
                    shopConfig.getConfig().set("players.bought." + p.getName() + "." + item, true);
                    shopConfig.saveConfig();
                    p.sendMessage(
                            this.pli.getMessagesConfig().successfully_bought_shopitem.replaceAll("<shopitem>", this.shopitems.get(item).getName()).replaceAll("<money>", Integer.toString(money)));
                }
                else
                {
                    p.sendMessage(this.pli.getMessagesConfig().not_enough_money);
                    return false;
                }
            }
            else
            {
                p.sendMessage(this.pli.getMessagesConfig().already_bought_shopitem.replaceAll("<shopitem>", this.shopitems.get(item).getName()));
                return true;
            }
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    public void giveShopItems(final Player p)
    {
        for (final ShopItem ac : this.shopitems.values())
        {
            if (ac.usesItems(this.pli))
            {
                for (final ItemStack i : ac.getItems())
                {
                    p.getInventory().addItem(i);
                }
                p.updateInventory();
            }
        }
    }
    
}
