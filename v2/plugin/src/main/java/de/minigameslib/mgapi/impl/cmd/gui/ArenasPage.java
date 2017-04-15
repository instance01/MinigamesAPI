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
import java.util.List;

import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;

/**
 * Page with arena options
 * 
 * @author mepeisen
 */
public class ArenasPage extends AbstractPage<ArenaInterface>
{
    
    /**
     * Constructor to create the first page
     */
    public ArenasPage()
    {
        // empty
    }
    
    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        return MinigamesLibInterface.instance().getArenaCount();
    }

    @Override
    protected List<ArenaInterface> getElements(int start, int limit)
    {
        return MinigamesLibInterface.instance().getArenas(start, limit);
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ArenaInterface elm)
    {
        return Main.itemArena(elm, (p, s, g) -> onArena(p, s, g, elm));
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                Main.itemHome(),
                Main.itemRefresh(this::onRefresh),
                this.itemPrevPage(),
                this.itemNextPage(),
                null,
                Main.itemNew(this::onNew, Messages.IconNewArena),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Save), Messages.IconImport, this::onImport),
                null,
                Main.itemCloseGui()
                };
    }
    
    /**
     * arena import
     * @param player
     * @param session
     * @param gui
     */
    private void onImport(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support import
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * arena
     * @param player
     * @param session
     * @param gui
     * @param arena
     */
    private void onArena(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ArenaInterface arena)
    {
        session.setNewPage(new ArenaEdit(arena, this));
    }
    
    /**
     * new arena
     * @param player
     * @param session
     * @param gui
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new ArenaCreateChooseMinigame((type, name) -> {
                final ArenaInterface arena = MinigamesLibInterface.instance().create(name, type);
                player.openClickGui(new Main(new ArenaEdit(arena, this)));
            },this));
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.arenas")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (arenas page)
         */
        @LocalizedMessage(defaultMessage = "Arenas (page %1$d from %2$d)")
        @MessageComment(value = {"Gui title (arenas page)"}, args = {@Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The new icon
         */
        @LocalizedMessage(defaultMessage = "New arena")
        @MessageComment({"new arena icon"})
        IconNewArena,
        
        /**
         * The arena icon
         */
        @LocalizedMessage(defaultMessage = "arena %1$s")
        @MessageComment(value = {"arena icon"}, args=@Argument("arena display name"))
        IconArena,
        
        /**
         * The arena import
         */
        @LocalizedMessage(defaultMessage = "import")
        @MessageComment({"arena import"})
        IconImport,
    }
    
}
