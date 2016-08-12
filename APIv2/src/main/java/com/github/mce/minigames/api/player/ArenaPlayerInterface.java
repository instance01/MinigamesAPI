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

package com.github.mce.minigames.api.player;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.WaitQueue;

/**
 * Interface representing players, maybe inside arenas.
 * 
 * @author mepeisen
 */
public interface ArenaPlayerInterface
{
    
    /**
     * Returns the bukkit player (if this player is online).
     * 
     * @return bukkit player.
     */
    Player getBukkitPlayer();
    
    /**
     * Returns the name of the player.
     * 
     * @return name of the player.
     */
    String getName();
    
    /**
     * Returns the bukkit offline player.
     * 
     * @return bukkit offline player.
     */
    OfflinePlayer getOfflinePlayer();
    
    /**
     * Returns the players uuid.
     * 
     * @return uuid.
     */
    UUID getPlayerUUID();
    
    /**
     * Returns the arena this player is currently in; within a match.
     * 
     * @return arena or {@code null} if this player is currently not within any arena.
     */
    ArenaInterface getArena();
    
    /**
     * Get the waiting queues this player joined.
     * 
     * @return waiting queues.
     */
    Iterable<WaitQueue> getWaitingQueues();
    
}
