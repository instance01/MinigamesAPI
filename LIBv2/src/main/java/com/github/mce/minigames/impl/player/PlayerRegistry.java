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

package com.github.mce.minigames.impl.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * A helper class to register player interfaces.
 * 
 * @author mepeisen
 */
public class PlayerRegistry
{
    
    /** the well known players. */
    private final Map<UUID, ArenaPlayerImpl> players = new HashMap<>();
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     * @return arena player.
     */
    public ArenaPlayerImpl getPlayer(Player player)
    {
        final UUID uuid = player.getUniqueId();
        return this.players.computeIfAbsent(uuid, (key) -> new ArenaPlayerImpl(uuid));
    }
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     * @return arena player.
     */
    public ArenaPlayerImpl getPlayer(OfflinePlayer player)
    {
        final UUID uuid = player.getUniqueId();
        return this.players.computeIfAbsent(uuid, (key) -> new ArenaPlayerImpl(uuid));
    }
    
    /**
     * Returns the player for given bukkit player uuid.
     * 
     * @param uuid
     * @return arena player.
     */
    public ArenaPlayerImpl getPlayer(UUID uuid)
    {
        return this.players.computeIfAbsent(uuid, (key) -> new ArenaPlayerImpl(uuid));
    }

    /**
     * @param evt
     */
    public void onPlayerJoin(PlayerJoinEvent evt)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param evt
     */
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        // TODO Auto-generated method stub
        
    }
    
}
