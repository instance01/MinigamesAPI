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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Interface representing players, maybe inside arenas.
 * 
 * @author mepeisen
 */
public interface ArenaPlayerInterface
{
    
    // common methods (player info)
    
    /**
     * Returns the bukkit player (if this player is online).
     * 
     * @return bukkit player.
     */
    default Player getBukkitPlayer()
    {
        return this.getMcPlayer().getBukkitPlayer();
    }
    
    /**
     * Returns the name of the player.
     * 
     * @return name of the player.
     */
    default String getName()
    {
        return this.getMcPlayer().getName();
    }
    
    /**
     * Returns the bukkit offline player.
     * 
     * @return bukkit offline player.
     */
    default OfflinePlayer getOfflinePlayer()
    {
        return this.getMcPlayer().getOfflinePlayer();
    }
    
    /**
     * Returns the mclib player.
     * 
     * @return mclib player.
     */
    McPlayerInterface getMcPlayer();
    
    /**
     * Returns the players uuid.
     * 
     * @return uuid.
     */
    default UUID getPlayerUUID()
    {
        return this.getMcPlayer().getPlayerUUID();
    }
    
    // arena data
    
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
    
    /**
     * Joins a waiting queue for the next match.
     * 
     * @param queue Queue to join
     */
    void join(WaitQueue queue);
    
    // stubbing
    
    /**
     * Checks this player for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the player matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws McException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    McOutgoingStubbing<ArenaPlayerInterface> when(McPredicate<ArenaPlayerInterface> test) throws McException;
    
    /**
     * Returns a test function to check if the user is online on the current server.
     * 
     * @return predicate to return {@code true} if the arena player is online.
     */
    static McPredicate<ArenaPlayerInterface> isOnline()
    {
        return (pl) -> pl.getBukkitPlayer() != null;
    }
    
    /**
     * Returns a test function to check if the user is inside any arena on the current server.
     * 
     * @return predicate to return {@code true} if the player is inside any arena on the current server.
     */
    static McPredicate<ArenaPlayerInterface> isInArena()
    {
        return (pl) -> pl.getArena() != null;
    }
    
}
