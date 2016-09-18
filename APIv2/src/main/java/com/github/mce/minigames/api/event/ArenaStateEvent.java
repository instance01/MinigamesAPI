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

import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaState;

/**
 * @author mepeisen
 *
 */
public class ArenaStateEvent extends Event
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the arena that changes the state. */
    private final ArenaInterface     arena;
    
    /** the previous state. */
    private final ArenaState         oldState;
    
    /** the next state. */
    private final ArenaState         newState;
    
    /**
     * Constructor.
     * 
     * @param arena
     *            Target arena
     * @param oldState
     *            previous state
     * @param newState
     *            next state
     */
    public ArenaStateEvent(ArenaInterface arena, ArenaState oldState, ArenaState newState)
    {
        this.arena = arena;
        this.oldState = oldState;
        this.newState = newState;
    }
    
    /**
     * Returns the arena that changes the state.
     * 
     * @return the arena
     */
    public ArenaInterface getArena()
    {
        return this.arena;
    }
    
    /**
     * Returns the previous state.
     * 
     * @return the oldState
     */
    public ArenaState getOldState()
    {
        return this.oldState;
    }
    
    /**
     * Returns the new state.
     * 
     * @return the newState
     */
    public ArenaState getNewState()
    {
        return this.newState;
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
