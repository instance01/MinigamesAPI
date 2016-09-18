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

package com.github.mce.minigames.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.mce.minigames.api.arena.WaitQueue;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * An event fired after a player leaves a waiting queue.
 * 
 * @author mepeisen
 *
 */
public class ArenaPlayerLeavesQueueEvent extends Event
{
    
    /** handlers list. */
    private static final HandlerList   handlers = new HandlerList();
    
    /** the queue the player joined. */
    private final WaitQueue            queue;
    
    /** the player that joined the arena. */
    private final ArenaPlayerInterface player;
    
    /**
     * Constructor.
     * 
     * @param queue
     *            Target queue
     * @param player
     *            Player leaving the queue
     */
    public ArenaPlayerLeavesQueueEvent(WaitQueue queue, ArenaPlayerInterface player)
    {
        this.queue = queue;
        this.player = player;
    }
    
    /**
     * Returns the queue that the player joined
     * 
     * @return the queue the player joined.
     */
    public WaitQueue getQueue()
    {
        return this.queue;
    }
    
    /**
     * Returns the joining player
     * 
     * @return the player
     */
    public ArenaPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
}
