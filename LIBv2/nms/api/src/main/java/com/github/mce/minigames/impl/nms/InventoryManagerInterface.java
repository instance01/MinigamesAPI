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

package com.github.mce.minigames.impl.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * An nms inventory manager.
 * 
 * @author mepeisen
 */
public interface InventoryManagerInterface
{
    
    /**
     * Opens an inventory.
     * 
     * @param player the player that is opening the inventory
     * @param name the name of the inventory
     * @param items the items within the inventory
     * @return inventory reference.
     */
    Inventory openInventory(Player player, String name, ItemStack[] items);
    
}
