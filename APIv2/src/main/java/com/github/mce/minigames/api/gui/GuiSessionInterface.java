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

package com.github.mce.minigames.api.gui;

import com.github.mce.minigames.api.config.Configurable;
import com.github.mce.minigames.api.context.MinigameStorage;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * An interface for a gui session.
 * 
 * @author mepeisen
 */
public interface GuiSessionInterface extends Configurable
{
    
    /**
     * Returns the player that owns this gui session.
     * 
     * @return player owning the session.
     */
    ArenaPlayerInterface getPlayer();
    
    /**
     * Returns the current click gui reference.
     * 
     * @return current click gui.
     */
    ClickGuiInterface getGui();
    
    /**
     * Sets a new gui page or updates the client after changing the gui items of a page.
     * 
     * @param page new gui page.
     */
    void setNewPage(ClickGuiPageInterface page);
    
    /**
     * Closes the gui session.
     */
    void close();
    
    /**
     * Returns a gui storage for storing data while the gui is open.
     * 
     * @return a gui storage.
     */
    MinigameStorage getGuiStorage();
    
    /**
     * Returns a gui storage for storing data while the player is logged in.
     * 
     * @return gui storage.
     */
    MinigameStorage getPlayerSessionStorage();
    
    /**
     * Returns a gui storage for persistent data stored on disk
     * 
     * @return gui storage.
     */
    MinigameStorage getPlayerPersistentStorage();
    
}
