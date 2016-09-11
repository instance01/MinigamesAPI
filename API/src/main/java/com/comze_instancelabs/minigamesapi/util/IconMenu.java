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
package com.comze_instancelabs.minigamesapi.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class IconMenu implements Listener
{
    
    private final String            name;
    private final int               size;
    private OptionClickEventHandler handler;
    private Plugin                  plugin;
    private Player                  player;
    
    private String[]                optionNames;
    private ItemStack[]             optionIcons;
    
    public IconMenu(final String name, final int size, final OptionClickEventHandler handler, final Plugin plugin)
    {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public IconMenu setOption(int pos, final ItemStack icon, final String name, final String... info)
    {
        int position = pos;
        if (position < 0)
        {
            position = 0;
        }
        if (this.optionNames == null)
        {
            this.optionNames = new String[this.size];
        }
        if (this.optionIcons == null)
        {
            this.optionIcons = new ItemStack[this.size];
        }
        this.optionNames[position] = name;
        this.optionIcons[position] = this.setItemNameAndLore(icon, name, info);
        return this;
    }
    
    public void setSpecificTo(final Player player)
    {
        this.player = player;
    }
    
    public boolean isSpecific()
    {
        return this.player != null;
    }
    
    public int getSize()
    {
        return this.size;
    }
    
    public void open(final Player player)
    {
        if (this.optionIcons != null && this.optionIcons.length > 0)
        {
            final Inventory inventory = Bukkit.createInventory(player, this.size, this.name);
            for (int i = 0; i < this.optionIcons.length; i++)
            {
                if (this.optionIcons[i] != null)
                {
                    inventory.setItem(i, this.optionIcons[i]);
                }
            }
            player.openInventory(inventory);
        }
    }
    
    public void destroy()
    {
        HandlerList.unregisterAll(this);
        this.handler = null;
        this.plugin = null;
        this.optionNames = null;
        this.optionIcons = null;
    }
    
    public void clear()
    {
        this.optionNames = null;
        this.optionIcons = null;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    void onInventoryClick(final InventoryClickEvent event)
    {
        if (event.getInventory().getTitle().equals(this.name) && (this.player == null || event.getWhoClicked() == this.player))
        {
            event.setCancelled(true);
            if (event.getClick() != ClickType.LEFT)
            {
                return;
            }
            final int slot = event.getRawSlot();
            if (slot >= 0 && slot < this.size && this.optionNames[slot] != null)
            {
                final Plugin plugin = this.plugin;
                final OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, this.optionNames[slot], this.optionIcons[slot]);
                this.handler.onOptionClick(e);
                ((Player) event.getWhoClicked()).updateInventory();
                if (e.willClose())
                {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.closeInventory());
                }
                if (e.willDestroy())
                {
                    this.destroy();
                }
            }
        }
    }
    
    public interface OptionClickEventHandler
    {
        public void onOptionClick(OptionClickEvent event);
    }
    
    public class OptionClickEvent
    {
        private final Player    player;
        private final int       position;
        private final String    name;
        private boolean         close;
        private boolean         destroy;
        private final ItemStack item;
        
        public OptionClickEvent(final Player player, final int position, final String name, final ItemStack item)
        {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
            this.item = item;
        }
        
        public Player getPlayer()
        {
            return this.player;
        }
        
        public int getPosition()
        {
            return this.position;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        public boolean willClose()
        {
            return this.close;
        }
        
        public boolean willDestroy()
        {
            return this.destroy;
        }
        
        public void setWillClose(final boolean close)
        {
            this.close = close;
        }
        
        public void setWillDestroy(final boolean destroy)
        {
            this.destroy = destroy;
        }
        
        public ItemStack getItem()
        {
            return this.item;
        }
    }
    
    private ItemStack setItemNameAndLore(final ItemStack item, final String name, final String[] lore)
    {
        final ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        final ArrayList<String> lore_lines = new ArrayList<>();
        for (final String l : lore)
        {
            lore_lines.add(ChatColor.translateAlternateColorCodes('&', l));
        }
        im.setLore(lore_lines);
        item.setItemMeta(im);
        return item;
    }
    
}
