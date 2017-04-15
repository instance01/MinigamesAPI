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

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
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
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;
import de.minigameslib.mgapi.impl.cmd.tool.AdminToolHelper;

/**
 * Page with arena components
 * 
 * @author mepeisen
 */
public class ComponentsPage extends AbstractPage<ArenaComponentHandler>
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
    public ComponentsPage(ArenaInterface arena, ClickGuiPageInterface prev)
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
        return this.arena.getComponents().size();
    }

    @Override
    protected List<ArenaComponentHandler> getElements(int start, int limit)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.arena.getComponents().
                stream().
                map(osi::findComponent).
                map(s -> (ArenaComponentHandler) s.getHandler()).
                sorted((a, b) -> a.getName().compareTo(b.getName())).
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ArenaComponentHandler elm)
    {
        final ItemStack item = ItemServiceInterface.instance().createItem(CommonItems.App_Component);
        return new ClickGuiItem(item, Messages.IconComponent, (p, s, g) -> this.onEdit(p, s, g, elm), elm.getName());
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
                Main.itemNew(this::onNew, Messages.IconNewComponent),
                null,
                Main.itemCloseGui()
                };
    }
    
    /**
     * component edit
     * @param player
     * @param session
     * @param gui
     * @param component
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ArenaComponentHandler component)
    {
        session.setNewPage(new ComponentEdit(this.arena, component, this));
    }
    
    /**
     * new component
     * @param player
     * @param session
     * @param gui
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new ComponentsCreateChooseType(this.arena, (type, name) -> onNew(player, session, gui, type, name), this));
    }
    
    /**
     * new component
     * @param player
     * @param session
     * @param gui
     * @param type 
     * @param name 
     * @throws McException 
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ComponentTypeId type, String name) throws McException
    {
        @SuppressWarnings("cast")
        final Optional<ArenaComponentHandler> handler = this.arena.getComponents().stream().
                map(s -> (ArenaComponentHandler) this.arena.getHandler(s)).
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
        
        AdminToolHelper.onCreateComponent(player, this.arena, name, type, c -> player.openClickGui(new Main(new ComponentEdit(this.arena, c, this))));
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.components")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (components page)
         */
        @LocalizedMessage(defaultMessage = "Components for arena %1$s (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (components page)"}, args = {@Argument("arena name"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * back icon
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment({"back icon"})
        IconBack,
        
        /**
         * The new component
         */
        @LocalizedMessage(defaultMessage = "New component")
        @MessageComment({"new component icon"})
        IconNewComponent,
        
        /**
         * The component icon
         */
        @LocalizedMessage(defaultMessage = "component %1$s")
        @MessageComment(value = {"component icon"}, args=@Argument("component name"))
        IconComponent,
    }
    
}
