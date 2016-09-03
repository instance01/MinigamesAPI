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

import org.bukkit.inventory.ItemStack;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * A clickable gui item.
 * 
 * @author mepeisen
 */
public class ClickGuiItem
{
    
    /** the item stack used to display the gui item */
    private final ItemStack                 itemStack;
    
    /** the items name/ title. */
    private final LocalizedMessageInterface displayName;

    /** the click handler. */
    private GuiItemHandler handler;
    
    /**
     * Constructor to create a click item.
     * 
     * @param itemStack
     * @param displayName
     * @param handler
     */
    public ClickGuiItem(ItemStack itemStack, LocalizedMessageInterface displayName, GuiItemHandler handler)
    {
        this.itemStack = itemStack.clone();
        this.displayName = displayName;
        this.handler = handler;
    }
    
    /**
     * @return the itemStack
     */
    public ItemStack getItemStack()
    {
        return this.itemStack.clone();
    }
    
    /**
     * @return the displayName
     */
    public LocalizedMessageInterface getDisplayName()
    {
        return this.displayName;
    }
    
    /**
     * Handle gui event.
     * 
     * @param player
     *            player that clicked the item
     * @param session
     *            gui session.
     * @param guiInterface
     *            gui interface.
     * @throws MinigameException
     *             thrown if there are errors.
     */
    public void handle(ArenaPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws MinigameException
    {
        this.handler.handle(player, session, guiInterface);
    }
    
    /**
     * Gui item handler.
     * @author mepeisen
     */
    @FunctionalInterface
    public interface GuiItemHandler
    {
        /**
         * Handle gui event.
         * 
         * @param player
         *            player that clicked the item
         * @param session
         *            gui session.
         * @param guiInterface
         *            gui interface.
         * @throws MinigameException
         *             thrown if there are errors.
         */
        void handle(ArenaPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws MinigameException;
    }
    
}
