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

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryManager
{
    
    private static HashMap<String, ItemStack[]> armourContents    = new HashMap<>();
    private static HashMap<String, ItemStack[]> inventoryContents = new HashMap<>();
    private static HashMap<String, Location>    locations         = new HashMap<>();
    private static HashMap<String, Integer>     xplevel           = new HashMap<>();
    private static HashMap<String, GameMode>    gamemode          = new HashMap<>();
    
    public static void saveInventory(final Player player)
    {
        InventoryManager.armourContents.put(player.getName(), player.getInventory().getArmorContents());
        InventoryManager.inventoryContents.put(player.getName(), player.getInventory().getContents());
        InventoryManager.locations.put(player.getName(), player.getLocation());
        InventoryManager.xplevel.put(player.getName(), player.getLevel());
        InventoryManager.gamemode.put(player.getName(), player.getGameMode());
        player.getInventory().clear();
    }
    
    public static void restoreInventory(final Player player)
    {
        player.getInventory().clear();
        player.teleport(InventoryManager.locations.get(player.getName()));
        
        player.getInventory().setContents(InventoryManager.inventoryContents.get(player.getName()));
        player.getInventory().setArmorContents(InventoryManager.armourContents.get(player.getName()));
        player.setLevel(InventoryManager.xplevel.get(player.getName()));
        player.setGameMode(InventoryManager.gamemode.get(player.getName()));
        
        InventoryManager.xplevel.remove(player.getName());
        InventoryManager.locations.remove(player.getName());
        InventoryManager.armourContents.remove(player.getName());
        InventoryManager.inventoryContents.remove(player.getName());
    }
}
