/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.cmd.gui;

import java.io.Serializable;

import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Click gui for asking a simple question
 * 
 * @author mepeisen
 */
public class YesNoQuestion implements ClickGuiPageInterface
{
    
    /** question */
    private Serializable question;
    
    /** details */
    private LocalizedMessageInterface details;
    
    /** details */
    private Serializable[] detailsArgs;
    
    /** yes action. */
    private ClickGuiItem.GuiItemHandler onYes;
    
    /** no action. */
    private ClickGuiItem.GuiItemHandler onNo;

    /**
     * @param question
     * @param onYes
     * @param onNo
     * @param details
     * @param detailsArgs 
     */
    public YesNoQuestion(Serializable question, ClickGuiItem.GuiItemHandler onYes, ClickGuiItem.GuiItemHandler onNo, LocalizedMessageInterface details, Serializable... detailsArgs)
    {
        this.onYes = onYes;
        this.onNo = onNo;
        this.details = details;
        this.detailsArgs = detailsArgs;
        this.question = question;
    }

    /**
     * @param question
     * @param onYes
     * @param onNo 
     */
    public YesNoQuestion(Serializable question, ClickGuiItem.GuiItemHandler onYes, ClickGuiItem.GuiItemHandler onNo)
    {
        this.onYes = onYes;
        this.onNo = onNo;
        this.question = question;
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(new ClickGuiItem[][]{
            {
                this.details == null ? null : new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Help), Messages.IconHelp, this::onHelp), 
            },
            null,
            null,
            {
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Yes), Messages.IconYes, this.onYes), 
                null,
                null,
                null,
                null,
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_No), Messages.IconNo, this.onNo), 
                null
            }
        }, 6);
    }

    /**
     * help
     * @param player
     * @param session
     * @param gui
     */
    private void onHelp(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        player.sendMessage(this.details, this.detailsArgs);
    }

    @Override
    public Serializable getPageName()
    {
        return this.question;
    }
    
    /**
     * The arena create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.yes_no")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * help icon
         */
        @LocalizedMessage(defaultMessage = "CLICK for help")
        @MessageComment("help icon")
        IconHelp,
        
        /**
         * yes icon
         */
        @LocalizedMessage(defaultMessage = "Yes")
        @MessageComment("yes icon")
        IconYes,
        
        /**
         * Gui title (arena edit page)
         */
        @LocalizedMessage(defaultMessage = "No")
        @MessageComment("no icon")
        IconNo,
    }
    
}
