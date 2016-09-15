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

import com.github.mce.minigames.api.gui.ClickGuiInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * @author mepeisen
 *
 */
public class PlayerCloseGuiEvent extends Event
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the gui the player closed. */
    private final ClickGuiInterface    gui;
    
    /** the player. */
    private final ArenaPlayerInterface player;
    
    /**
     * Constructor.
     * 
     * @param gui
     * @param player
     */
    public PlayerCloseGuiEvent(ClickGuiInterface gui, ArenaPlayerInterface player)
    {
        this.gui = gui;
        this.player = player;
    }
    
    /**
     * Returns the gui that the player closed
     * 
     * @return the gui the player closed.
     */
    public ClickGuiInterface getGui()
    {
        return this.gui;
    }
    
    /**
     * Returns the player
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
