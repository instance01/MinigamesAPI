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
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * An event fired after a player joins an arena.
 * 
 * @author mepeisen
 */
public class ArenaPlayerJoinedEvent extends Event
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the arena the player joined. */
    private final ArenaInterface     arena;

    /** the player that joined the arena. */
    private final ArenaPlayerInterface player;

    /**
     * Constructor.
     * @param arena
     * @param player
     */
    public ArenaPlayerJoinedEvent(ArenaInterface arena, ArenaPlayerInterface player)
    {
        this.arena = arena;
        this.player = player;
    }
    
    /**
     * Returns the arena that the player joined
     * 
     * @return the arenaa the player joined.
     */
    public ArenaInterface getArena()
    {
        return this.arena;
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
