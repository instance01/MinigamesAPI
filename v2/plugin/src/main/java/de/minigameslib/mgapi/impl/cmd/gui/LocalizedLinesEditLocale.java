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
import java.util.Locale;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * Click gui for editing localized lines; single locale
 * 
 * @author mepeisen
 */
public class LocalizedLinesEditLocale implements ClickGuiPageInterface
{

    /** consumer to save strings */
    private McConsumer<LocalizedConfigLine> onSave;
    
    /** consumer to display prev page */
    private ClickGuiItem.GuiItemHandler onPrev;
    
    /** consumer to delete language */
    private ClickGuiItem.GuiItemHandler onDelete;
    
    /** title */
    private Serializable title;
    
    /** locale */
    private Locale locale;

    /** the config line. */
    private LocalizedConfigLine line;
    
    /**
     * @param title
     * @param locale
     * @param line
     * @param onSave
     * @param onPrev
     * @param onDelete
     */
    public LocalizedLinesEditLocale(Serializable title, Locale locale, LocalizedConfigLine line, McConsumer<LocalizedConfigLine> onSave, GuiItemHandler onPrev, GuiItemHandler onDelete)
    {
        this.title = title;
        this.locale = locale;
        this.line = line;
        this.onSave = onSave;
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
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_People), Messages.IconEditUser, this::onUserString, this.title, this.locale.toString()),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Boss), Messages.IconEditAdmin, this::onAdminString, this.title, this.locale.toString()),
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
        final String[] lines = this.line.getUnformattedUserMessageLine(this.locale);
        session.setNewPage(new LocalizedLinesEditLocaleList(
                this.title,
                lines,
                (p,  s, g) -> s.setNewPage(this),
                (p, s, g) -> {
                    this.line.setUserMessages(this.locale, null);
                    this.onSave.accept(this.line);
                    s.setNewPage(this);
                },
                (s) -> {
                    this.line.setUserMessages(this.locale, s);
                    this.onSave.accept(this.line);
                }));
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
        final String[] lines = this.line.getUnformattedAdminMessageLine(this.locale);
        session.setNewPage(new LocalizedLinesEditLocaleList(
                this.title,
                lines,
                (p,  s, g) -> s.setNewPage(this),
                (p, s, g) -> {
                    this.line.setAdminMessages(this.locale, null);
                    this.onSave.accept(this.line);
                    s.setNewPage(this);
                },
                (s) -> {
                    this.line.setAdminMessages(this.locale, s);
                    this.onSave.accept(this.line);
                }));
    }

    /**
     * Editor to create strings.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.line_edit_locale")
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
        
    }
    
}
