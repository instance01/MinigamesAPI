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
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;
import de.minigameslib.mgapi.impl.cmd.tool.AdminToolHelper;

/**
 * Page with arena signs
 * 
 * @author mepeisen
 */
public class SignsPage extends AbstractPage<ArenaSignHandler>
{
    
    /** the arena */
    private ArenaInterface arena;
    
    /** previous page. */
    private ClickGuiPageInterface prev;

    /**
     * Constructor to create the first page
     * @param arena 
     * @param prev 
     */
    public SignsPage(ArenaInterface arena, ClickGuiPageInterface prev)
    {
        this.arena = arena;
        this.prev = prev;
    }
    
    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.arena.getInternalName(), this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        return this.arena.getSigns().size();
    }

    @Override
    protected List<ArenaSignHandler> getElements(int start, int limit)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.arena.getSigns().
                stream().
                map(osi::findSign).
                map(s -> (ArenaSignHandler) s.getHandler()).
                sorted((a, b) -> a.getName().compareTo(b.getName())).
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ArenaSignHandler elm)
    {
        final ItemStack item = new ItemStack(Material.SIGN);
        return new ClickGuiItem(item, Messages.IconSign, (p, s, g) -> this.onEdit(p, s, g, elm), elm.getName());
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                Main.itemHome(),
                Main.itemBack((c,  s,  g) -> s.setNewPage(this.prev), Messages.IconBack),
                Main.itemRefresh(this::onRefresh),
                this.itemPrevPage(),
                this.itemNextPage(),
                null,
                Main.itemNew(this::onNew, Messages.IconNewSign),
                null,
                Main.itemCloseGui()
                };
    }
    
    /**
     * sign edit
     * @param player
     * @param session
     * @param gui
     * @param sign
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ArenaSignHandler sign)
    {
        session.setNewPage(new SignEdit(this.arena, sign, this));
    }
    
    /**
     * new sign
     * @param player
     * @param session
     * @param gui
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new SignsCreateChooseType(this.arena, (type, name) -> onNew(player, session, gui, type, name), this));
    }
    
    /**
     * new sign
     * @param player
     * @param session
     * @param gui
     * @param type 
     * @param name 
     * @throws McException 
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, SignTypeId type, String name) throws McException
    {
        @SuppressWarnings("cast")
        final Optional<ArenaSignHandler> handler = this.arena.getSigns().stream().
                map(s -> (ArenaSignHandler) this.arena.getHandler(s)).
                filter(s -> name.equals(s.getName())).
                findFirst();
        if (handler.isPresent())
        {
            throw new McException(Mg2Command.Messages.ComponentAlreadyExists, name);
        }
        if (!this.arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        AdminToolHelper.onCreateSign(player, this.arena, name, type, s -> player.openClickGui(new Main(new SignEdit(this.arena, s, this))));
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.signs")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (signs page)
         */
        @LocalizedMessage(defaultMessage = "Signs for arena %1$s (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (signs page)"}, args = {@Argument("arena name"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * back icon
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment({"back icon"})
        IconBack,
        
        /**
         * The new sign
         */
        @LocalizedMessage(defaultMessage = "New sign")
        @MessageComment({"new sign icon"})
        IconNewSign,
        
        /**
         * The sign icon
         */
        @LocalizedMessage(defaultMessage = "sign %1$s")
        @MessageComment(value = {"sign icon"}, args=@Argument("sign name"))
        IconSign,
    }
    
}
