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
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;
import de.minigameslib.mgapi.impl.cmd.tool.AdminToolHelper;

/**
 * Page with arena zones
 * 
 * @author mepeisen
 */
public class ZonesPage extends AbstractPage<ArenaZoneHandler>
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
    public ZonesPage(ArenaInterface arena, ClickGuiPageInterface prev)
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
        return this.arena.getZones().size();
    }

    @Override
    protected List<ArenaZoneHandler> getElements(int start, int limit)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.arena.getZones().
                stream().
                map(osi::findZone).
                map(s -> (ArenaZoneHandler) s.getHandler()).
                sorted((a, b) -> a.getName().compareTo(b.getName())).
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ArenaZoneHandler elm)
    {
        final ItemStack item = ItemServiceInterface.instance().createItem(CommonItems.App_Globe);
        return new ClickGuiItem(item, Messages.IconZone, (p, s, g) -> this.onEdit(p, s, g, elm), elm.getName());
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
                Main.itemNew(this::onNew, Messages.IconNewZone),
                null,
                Main.itemCloseGui()
                };
    }
    
    /**
     * zone edit
     * @param player
     * @param session
     * @param gui
     * @param zone
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ArenaZoneHandler zone)
    {
        session.setNewPage(new ZoneEdit(this.arena, zone, this));
    }
    
    /**
     * new zone
     * @param player
     * @param session
     * @param gui
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new ZonesCreateChooseType(this.arena, (type, name) -> onNew(player, session, gui, type, name), this));
    }
    
    /**
     * new zone
     * @param player
     * @param session
     * @param gui
     * @param type 
     * @param name 
     * @throws McException 
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ZoneTypeId type, String name) throws McException
    {
        @SuppressWarnings("cast")
        final Optional<ArenaZoneHandler> handler = this.arena.getZones().stream().
                map(s -> (ArenaZoneHandler) this.arena.getHandler(s)).
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
        
        AdminToolHelper.onCreateZone(player, this.arena, name, type, z -> player.openClickGui(new Main(new ZoneEdit(this.arena, z, this))));
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.zones")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (zones page)
         */
        @LocalizedMessage(defaultMessage = "Zones for arena %1$s (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (zones page)"}, args = {@Argument("arena name"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * back icon
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment({"back icon"})
        IconBack,
        
        /**
         * The new zone
         */
        @LocalizedMessage(defaultMessage = "New zone")
        @MessageComment({"new zone icon"})
        IconNewZone,
        
        /**
         * The zone icon
         */
        @LocalizedMessage(defaultMessage = "zone %1$s")
        @MessageComment(value = {"zone icon"}, args=@Argument("zone name"))
        IconZone,
    }
    
}
