package com.comze_instancelabs.minigamesapi.util;
 
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
 
public class InventoryManager {
 
    private static HashMap<String, ItemStack[]> armourContents = new HashMap<String, ItemStack[]>();
    private static HashMap<String, ItemStack[]> inventoryContents = new HashMap<String, ItemStack[]>();
    private static HashMap<String, Location> locations = new HashMap<String, Location>();
    private static HashMap<String, Integer> xplevel = new HashMap<String, Integer>();
    private static HashMap<String, GameMode> gamemode = new HashMap<String, GameMode>();
 
    public static void saveInventory(Player player){
        armourContents.put(player.getName(), player.getInventory().getArmorContents());
        inventoryContents.put(player.getName(), player.getInventory().getContents());
        locations.put(player.getName(), player.getLocation());
        xplevel.put(player.getName(), player.getLevel());
        gamemode.put(player.getName(), player.getGameMode());
        player.getInventory().clear();
    }
 
    public static void restoreInventory(Player player){
        player.getInventory().clear();
        player.teleport(locations.get(player.getName()));
 
        player.getInventory().setContents(inventoryContents.get(player.getName()));
        player.getInventory().setArmorContents(armourContents.get(player.getName()));
        player.setLevel(xplevel.get(player.getName()));
        player.setGameMode(gamemode.get(player.getName()));
 
        xplevel.remove(player.getName());
        locations.remove(player.getName());
        armourContents.remove(player.getName());
        inventoryContents.remove(player.getName());
    }
}