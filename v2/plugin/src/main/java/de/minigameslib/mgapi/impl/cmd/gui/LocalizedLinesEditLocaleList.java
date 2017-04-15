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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
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
 * Click gui for editing localized lines; single locale and type
 * 
 * @author mepeisen
 */
public class LocalizedLinesEditLocaleList extends AbstractPage<String>
{
    
    /** consumer to display prev page */
    private ClickGuiItem.GuiItemHandler onPrev;
    
    /** consumer to delete language */
    private ClickGuiItem.GuiItemHandler onDelete;
    
    /** title */
    private Serializable title;

    /** the lines. */
    private List<String> lines;

    /** save function. */
    private McConsumer<String[]> onSave;
    
    /** string marker */
    private static final String DELETE_MARKER = "$DELETE$"; //$NON-NLS-1$
    
    /** string marker */
    private static final String CREATE_BEFORE_MARKER = "$CREATE-BEFORE$"; //$NON-NLS-1$
    
    /** string marker */
    private static final String CREATE_AFTER_MARKER = "$CREATE-AFTER"; //$NON-NLS-1$
    
    /**
     * @param title
     * @param lines 
     * @param onPrev
     * @param onDelete
     * @param save 
     */
    public LocalizedLinesEditLocaleList(Serializable title, String[] lines, GuiItemHandler onPrev, GuiItemHandler onDelete, McConsumer<String[]> save)
    {
        this.lines = Arrays.asList(lines);
        this.title = title;
        this.onPrev = onPrev;
        this.onDelete = onDelete;
        this.onSave = save;
    }

    @Override
    protected int count()
    {
        return this.lines.size() * ITEMS_PER_LINE;
    }

    @Override
    protected List<String> getElements(int start, int limit)
    {
        final List<String> result = new ArrayList<>();
        final int index = start / ITEMS_PER_LINE;
        if (index < this.lines.size())
        {
            result.add(CREATE_BEFORE_MARKER);
            result.add(this.lines.get(index));
            result.add(CREATE_AFTER_MARKER);
            result.add(null);
            result.add(DELETE_MARKER);
        }
        return result;
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, String elm)
    {
        if (elm == null)
        {
            return null;
        }
        int realLine = (index - col) / ITEMS_PER_LINE;
        if (elm == CREATE_BEFORE_MARKER)
        {
            return Main.itemNew((p, s, g) -> this.onCreateBefore(p, s, g, realLine), Messages.IconCreateBefore);
        }
        if (elm == CREATE_AFTER_MARKER)
        {
            return Main.itemNew((p, s, g) -> this.onCreateAfter(p, s, g, realLine), Messages.IconCreateAfter);
        }
        if (elm == DELETE_MARKER)
        {
            return Main.itemDelete((p, s, g) -> this.onDeleteLine(p, s, g, realLine), Messages.IconDeleteLine);
        }
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconEdit, (p, s, g) -> onEdit(p, s, g, realLine), realLine);
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                Main.itemHome(),
                null,
                this.itemPrevPage(),
                this.itemNextPage(),
                Main.itemBack(this.onPrev, Messages.IconBack),
                null,
                Main.itemDelete(this.onDelete, Messages.IconDeleteAll),
                null,
                Main.itemCloseGui()
        };
    }

    @Override
    public Serializable getPageName()
    {
        return this.title;
    }
    
    /**
     * insert before
     * @param player
     * @param session
     * @param gui
     * @param realLine
     * @throws McException 
     */
    private void onCreateBefore(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.add(realLine, "<text>"); //$NON-NLS-1$
        this.onSave.accept(this.lines.toArray(new String[this.lines.size()]));
        this.onEdit(player, session, gui, realLine);
    }
    
    /**
     * insert after
     * @param player
     * @param session
     * @param gui
     * @param realLine
     * @throws McException 
     */
    private void onCreateAfter(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.add(realLine + 1, "<text>"); //$NON-NLS-1$
        this.onSave.accept(this.lines.toArray(new String[this.lines.size()]));
        this.onEdit(player, session, gui, realLine + 1);
    }
    
    /**
     * delete line
     * @param player
     * @param session
     * @param gui
     * @param realLine
     * @throws McException 
     */
    private void onDeleteLine(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.remove(realLine);
        this.onRefresh(player, session, gui);
    }
    
    /**
     * edit line
     * @param player
     * @param session
     * @param gui
     * @param realLine
     * @throws McException 
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        final String text = this.lines.get(realLine);

        player.openAnvilGui(new QueryText(
                text,
                () -> {player.openClickGui(new Main(this));},
                (s) -> this.onEdit(player, session, gui, realLine, s),
                player.encodeMessage(Messages.EditTextDescription, this.title, realLine)));
    }
    
    /**
     * edit line
     * @param player
     * @param session
     * @param gui
     * @param realLine
     * @param content
     * @throws McException 
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine, String content) throws McException
    {
        this.lines.set(realLine, content == null ? "" : content); //$NON-NLS-1$
        this.onSave.accept(this.lines.toArray(new String[this.lines.size()]));
        player.openClickGui(new Main(this));
    }

    /**
     * Editor to create strings.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.line_edit_locale_list")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Back icon
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment("back icon")
        IconBack,

        /**
         * Create icon
         */
        @LocalizedMessage(defaultMessage = "Create new line before #%1$d")
        @MessageComment(value = "Create before icon", args=@Argument("line number"))
        IconCreateBefore,

        /**
         * Create icon
         */
        @LocalizedMessage(defaultMessage = "Create new line after #%1$d")
        @MessageComment(value = "Create after icon", args=@Argument("line number"))
        IconCreateAfter,

        /**
         * Delete icon
         */
        @LocalizedMessage(defaultMessage = "Delete line #%1$d")
        @MessageComment(value = "Delete line", args=@Argument("line number"))
        IconDeleteLine,

        /**
         * Delete all
         */
        @LocalizedMessage(defaultMessage = "Delete all")
        @MessageComment(value = "Delete all")
        IconDeleteAll,

        /**
         * Edit icon
         */
        @LocalizedMessage(defaultMessage = "Edit line #%1$d")
        @MessageComment(value = "Edit icon", args=@Argument("line number"))
        IconEdit,
        
        /**
         * Edit existing: text description
         */
        @LocalizedMessageList({"Edit text line for %1$s - %2$d.", "Use percent sign to create minecraft color codes.", "For example: '%%0' for black."})
        @MessageComment(value = "Edit existing: text description", args = {@Argument("title"), @Argument("line number")})
        EditTextDescription,
        
    }
    
}
