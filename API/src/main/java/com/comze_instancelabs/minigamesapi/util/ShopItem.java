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

import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ShopItem extends AClass
{
    
    public ShopItem(final JavaPlugin plugin, final String name, final String internalname, final boolean enabled, final ArrayList<ItemStack> items, final ItemStack icon)
    {
        super(plugin, name, internalname, enabled, items, icon);
    }
    
    public boolean usesItems(final PluginInstance pli)
    {
        return pli.getShopConfig().getConfig().getBoolean("config.shop_items." + this.getInternalName() + ".uses_items");
    }
    
}
