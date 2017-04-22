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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * Click gui for editing localized strings; single locale
 * 
 * @author mepeisen
 */
public class LocalizedStringEditor implements ClickGuiPageInterface
{

    /** consumer to save user strings */
    private McConsumer<String> onSaveUserString;

    /** consumer to save admin strings */
    private McConsumer<String> onSaveAdminString;
    
    /** consumer to display prev page */
    private ClickGuiItem.GuiItemHandler onPrev;
    
    /** consumer to delete language */
    private ClickGuiItem.GuiItemHandler onDelete;
    
    /** current admin string */
    private String adminString;
    
    /** current user string */
    private String userString;
    
    /** title */
    private Serializable title;
    
    /** locale */
    private String locale;
    
    /**
     * @param title
     * @param locale
     * @param userString
     * @param adminString
     * @param onSaveUserString
     * @param onSaveAdminString
     * @param onPrev
     * @param onDelete
     */
    public LocalizedStringEditor(Serializable title, String locale, String userString, String adminString, McConsumer<String> onSaveUserString, McConsumer<String> onSaveAdminString, GuiItemHandler onPrev,
            GuiItemHandler onDelete)
    {
        this.title = title;
        this.locale = locale;
        this.userString = userString;
        this.adminString = adminString;
        this.onSaveUserString = onSaveUserString;
        this.onSaveAdminString = onSaveAdminString;
        this.onPrev = onPrev;
        this.onDelete = onDelete;
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(new ClickGuiItem[][]{
            {
                Main.itemHome(),
                Main.itemBack(this.onPrev, Messages.IconBack),
                null,
                Main.itemDelete(this.onDelete, Messages.IconDelete),
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_People), Messages.IconEditUser, this::onUserString, this.title, this.locale),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Boss), Messages.IconEditAdmin, this::onAdminString, this.title, this.locale),
                null,
                Main.itemCloseGui()
            },
        }, 6);
    }

    @Override
    public Serializable getPageName()
    {
        return this.title;
    }
    
    /**
     * edit user string
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onUserString(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.openAnvilGui(new QueryText(
                this.userString == null ? "" : this.userString, //$NON-NLS-1$
                () -> {player.openClickGui(new Main(this));},
                (s) -> this.onUserString(player, session, gui, s),
                player.encodeMessage(Messages.EditTextDescription, this.title, this.locale)));
    }
    
    /**
     * edit user string
     * @param player
     * @param session
     * @param gui
     * @param content
     * @throws McException 
     */
    private void onUserString(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, String content) throws McException
    {
        this.userString = content != null && content.length() == 0 ? null : content;
        this.onSaveUserString.accept(this.userString);
        player.openClickGui(new Main(this));
    }
    
    /**
     * edit admin string
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onAdminString(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.openAnvilGui(new QueryText(
                this.adminString == null ? "" : this.adminString, //$NON-NLS-1$
                () -> {player.openClickGui(new Main(this));},
                (s) -> this.onAdminString(player, session, gui, s),
                player.encodeMessage(Messages.EditAdminDescription, this.title, this.locale)));
    }
    
    /**
     * edit admin string
     * @param player
     * @param session
     * @param gui
     * @param content
     * @throws McException 
     */
    private void onAdminString(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, String content) throws McException
    {
        this.adminString = content != null && content.length() == 0 ? null : content;
        this.onSaveAdminString.accept(this.adminString);
        player.openClickGui(new Main(this));
    }

    /**
     * Editor to create strings.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.string_edit")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Back icon
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment("back icon")
        IconBack,

        /**
         * Delete icon
         */
        @LocalizedMessage(defaultMessage = "Delete language")
        @MessageComment("Delete icon")
        IconDelete,

        /**
         * Edit icon
         */
        @LocalizedMessage(defaultMessage = "Edit user text for %1$s - %2$s")
        @MessageComment(value = "Edit icon", args = {@Argument("title"),@Argument("locale name")})
        IconEditUser,

        /**
         * Edit icon
         */
        @LocalizedMessage(defaultMessage = "Edit admin text for %1$s - %2$s")
        @MessageComment(value = "Edit icon", args = {@Argument("title"),@Argument("locale name")})
        IconEditAdmin,
        
        /**
         * Edit existing: user text description
         */
        @LocalizedMessageList({"Edit user text for %1$s - %2$s.", "Use percent sign to create minecraft color codes.", "For example: '%%0' for black."})
        @MessageComment(value = "Edit existing: user text description", args = {@Argument("title"), @Argument("locale")})
        EditTextDescription,
        
        /**
         * Edit existing: admin text description
         */
        @LocalizedMessageList({"Edit admin text for %1$s - %2$s.", "Use percent sign to create minecraft color codes.", "For example: '%%0' for black."})
        @MessageComment(value = "Edit existing: admin text description", args = {@Argument("title"), @Argument("locale")})
        EditAdminDescription,
        
    }
    
}
