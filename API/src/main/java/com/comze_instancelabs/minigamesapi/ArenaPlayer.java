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

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.minigamesapi.util.AClass;

/**
 * Internal minigames representation of a player being present in any arena.
 * 
 * <p>
 * TODO: Remove from hashmap players as soon as a player leaves the server.
 * </p>
 * 
 * <p>
 * TODO: Clear inventories as soon as it was re set while leaving.
 * </p>
 * 
 * @author instancelabs
 */
public class ArenaPlayer
{
    
    /** The players name. */
    private String                                    playername;
    /** the inventory stack. */
    private ItemStack[]                               inv;
    /** the armor inventory stack. */
    private ItemStack[]                               armor_inv;
    /** the original game mode of the player. */
    private GameMode                                  original_gamemode = GameMode.SURVIVAL;
    /** the original xp level of the player. */
    private int                                       original_xplvl    = 0;
    /** the no-reward flag. */
    private boolean                                   noreward          = false;
    /** the current arena. */
    private Arena                                     currentArena;
    /** the current class. */
    private AClass                                    currentClass;
    
    /** the map holding the known arena player instances. */
    private static final HashMap<String, ArenaPlayer> players           = new HashMap<>();
    
    /**
     * Returns the player instance for given player name; creates it on demand.
     * 
     * @param playername
     *            name of the player.
     * @return arena player instance.
     */
    public static ArenaPlayer getPlayerInstance(final String playername)
    {
        if (!ArenaPlayer.players.containsKey(playername))
        {
            return new ArenaPlayer(playername);
        }
        return ArenaPlayer.players.get(playername);
    }
    
    /**
     * Constructor to create the arena player.
     * 
     * @param playername
     *            players name.
     */
    public ArenaPlayer(final String playername)
    {
        this.playername = playername;
        ArenaPlayer.players.put(playername, this);
    }
    
    /**
     * Returns the bukkit player.
     * 
     * @return bukkit player.
     */
    public Player getPlayer()
    {
        return Bukkit.getPlayer(this.playername);
    }
    
    /**
     * Sets the inventories being present befor the player joins the lobby.
     * 
     * @param inv
     *            inventory of the player.
     * @param armor_inv
     *            armor inventory of the player.
     */
    public void setInventories(final ItemStack[] inv, final ItemStack[] armor_inv)
    {
        this.inv = inv;
        this.armor_inv = armor_inv;
    }
    
    /**
     * Returns the players inventory before joining the lobby.
     * 
     * @return player inventory.
     */
    public ItemStack[] getInventory()
    {
        return this.inv;
    }
    
    /**
     * Returns the players armor inventory before joining the lobby.
     * 
     * @return players armo inventory.
     */
    public ItemStack[] getArmorInventory()
    {
        return this.armor_inv;
    }
    
    /**
     * Returns the original game mode before joining the lobby.
     * 
     * @return original game mode.
     */
    public GameMode getOriginalGamemode()
    {
        return this.original_gamemode;
    }
    
    /**
     * Sets the original game mode before joining the lobby.
     * 
     * @param original_gamemode
     *            the players original game mode.
     */
    public void setOriginalGamemode(final GameMode original_gamemode)
    {
        this.original_gamemode = original_gamemode;
    }
    
    /**
     * Returns the original xp level before joining the lobby.
     * 
     * @return original xp mode before joining the lobby.
     */
    public int getOriginalXplvl()
    {
        return this.original_xplvl;
    }
    
    /**
     * Sets the original xp level of the player.
     * 
     * @param original_xplvl
     *            cp level before joining the lobby.
     */
    public void setOriginalXplvl(final int original_xplvl)
    {
        this.original_xplvl = original_xplvl;
    }
    
    /**
     * Returns the is no reward flag.
     * 
     * @return {@code true} if the player does not receive rewards.
     */
    public boolean isNoReward()
    {
        return this.noreward;
    }
    
    /**
     * Sets the no-reward flag.
     * 
     * @param noreward
     *            {@code true} if the player does not receive rewards.
     */
    public void setNoReward(final boolean noreward)
    {
        this.noreward = noreward;
    }
    
    /**
     * Returns the current arena the player is located in.
     * 
     * @return current arena or {@code null} if the player is not in any arena.
     */
    public Arena getCurrentArena()
    {
        return this.currentArena;
    }
    
    /**
     * Sets the current arena the player is located in.
     * 
     * @param currentArena
     *            current arena.
     */
    public void setCurrentArena(final Arena currentArena)
    {
        this.currentArena = currentArena;
    }
    
    /**
     * Returns the current player class.
     * 
     * @return player class.
     */
    public AClass getCurrentClass()
    {
        return this.currentClass;
    }
    
    /**
     * Sets the current player class.
     * 
     * @param currentClass
     *            player class.
     */
    public void setCurrentClass(final AClass currentClass)
    {
        this.currentClass = currentClass;
    }
    
}
