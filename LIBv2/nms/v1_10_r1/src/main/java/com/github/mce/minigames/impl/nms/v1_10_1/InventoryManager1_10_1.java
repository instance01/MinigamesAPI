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

package com.github.mce.minigames.impl.nms.v1_10_1;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.mce.minigames.impl.nms.InventoryManagerInterface;

import net.minecraft.server.v1_10_R1.EntityPlayer;

/**
 * Inventory manager implementation.
 * 
 * @author mepeisen
 */
public class InventoryManager1_10_1 implements InventoryManagerInterface
{

    @Override
    public Inventory openInventory(Player player, String name, ItemStack[] items)
    {
        final Inventory inventory = Bukkit.createInventory(null, items.length, name);
        inventory.setContents(items);
        final EntityPlayer entity = ((CraftPlayer)player).getHandle();
        if (entity.activeContainer != entity.defaultContainer)
        {
            CraftEventFactory.handleInventoryCloseEvent(entity);
            entity.activeContainer = entity.defaultContainer;
        }
        player.openInventory(inventory);
        return inventory;
    }
    
}
