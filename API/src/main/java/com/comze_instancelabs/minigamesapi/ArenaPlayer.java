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

public class ArenaPlayer
{
    
    String                                      playername;
    ItemStack[]                                 inv;
    ItemStack[]                                 armor_inv;
    GameMode                                    original_gamemode = GameMode.SURVIVAL;
    int                                         original_xplvl    = 0;
    boolean                                     noreward          = false;
    Arena                                       currentArena;
    AClass                                      currentClass;
    
    private static HashMap<String, ArenaPlayer> players           = new HashMap<>();
    
    public static ArenaPlayer getPlayerInstance(final String playername)
    {
        if (!ArenaPlayer.players.containsKey(playername))
        {
            return new ArenaPlayer(playername);
        }
        else
        {
            return ArenaPlayer.players.get(playername);
        }
    }
    
    public ArenaPlayer(final String playername)
    {
        this.playername = playername;
        ArenaPlayer.players.put(playername, this);
    }
    
    public Player getPlayer()
    {
        return Bukkit.getPlayer(this.playername);
    }
    
    public void setInventories(final ItemStack[] inv, final ItemStack[] armor_inv)
    {
        this.inv = inv;
        this.armor_inv = armor_inv;
    }
    
    public ItemStack[] getInventory()
    {
        return this.inv;
    }
    
    public ItemStack[] getArmorInventory()
    {
        return this.armor_inv;
    }
    
    public GameMode getOriginalGamemode()
    {
        return this.original_gamemode;
    }
    
    public void setOriginalGamemode(final GameMode original_gamemode)
    {
        this.original_gamemode = original_gamemode;
    }
    
    public int getOriginalXplvl()
    {
        return this.original_xplvl;
    }
    
    public void setOriginalXplvl(final int original_xplvl)
    {
        this.original_xplvl = original_xplvl;
    }
    
    public boolean isNoReward()
    {
        return this.noreward;
    }
    
    public void setNoReward(final boolean noreward)
    {
        this.noreward = noreward;
    }
    
    public Arena getCurrentArena()
    {
        return this.currentArena;
    }
    
    public void setCurrentArena(final Arena currentArena)
    {
        this.currentArena = currentArena;
    }
    
    public AClass getCurrentClass()
    {
        return this.currentClass;
    }
    
    public void setCurrentClass(final AClass currentClass)
    {
        this.currentClass = currentClass;
    }
    
}
