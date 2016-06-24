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

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AClass
{
    
    private final JavaPlugin     plugin;
    private final String         name;
    private final String         internalname;
    private ArrayList<ItemStack> items   = new ArrayList<>();
    private final ItemStack      icon;
    private boolean              enabled = true;
    
    @Deprecated
    public AClass(final JavaPlugin plugin, final String name, final ArrayList<ItemStack> items)
    {
        this(plugin, name, name, true, items, items.get(0));
    }
    
    public AClass(final JavaPlugin plugin, final String name, final String internalname, final ArrayList<ItemStack> items)
    {
        this(plugin, name, internalname, true, items, items.get(0));
    }
    
    public AClass(final JavaPlugin plugin, final String name, final String internalname, final boolean enabled, final ArrayList<ItemStack> items)
    {
        this(plugin, name, internalname, enabled, items, items.get(0));
    }
    
    public AClass(final JavaPlugin plugin, final String name, final String internalname, final boolean enabled, final ArrayList<ItemStack> items, final ItemStack icon)
    {
        this.plugin = plugin;
        this.name = name;
        this.items = items;
        this.icon = icon;
        this.enabled = enabled;
        this.internalname = internalname;
    }
    
    public ItemStack[] getItems()
    {
        final ItemStack[] ret = new ItemStack[this.items.size()];
        int c = 0;
        for (final ItemStack f : this.items)
        {
            ret[c] = f;
            c++;
        }
        return ret;
    }
    
    public ItemStack getIcon()
    {
        return this.icon;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getInternalName()
    {
        return this.internalname;
    }
    
    public boolean isEnabled()
    {
        return this.enabled;
    }
}
