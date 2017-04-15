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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
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
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;

/**
 * Page with arena zones; choose type for new zones
 * 
 * @author mepeisen
 */
public class ZonesCreateChooseType extends AbstractPage<ZoneTypeId>
{
    
    /** the arena */
    private McBiConsumer<ZoneTypeId, String> onSave;
    
    /** previous page */
    private ClickGuiPageInterface prev;

    /** the underlying arena. */
    private ArenaInterface arena;

    /**
     * @param arena 
     * @param onSave
     * @param prev
     */
    public ZonesCreateChooseType(ArenaInterface arena, McBiConsumer<ZoneTypeId, String> onSave, ClickGuiPageInterface prev)
    {
        this.arena = arena;
        this.onSave = onSave;
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
        return EnumServiceInterface.instance().getEnumValues(ZoneTypeId.class).size();
    }
    
    /**
     * Converts zone type to string
     * @param zoneType
     * @return zone type
     */
    private String toString(ZoneTypeId zoneType)
    {
        return zoneType.getPluginName() + "/" + zoneType.name(); //$NON-NLS-1$
    }

    @Override
    protected List<ZoneTypeId> getElements(int start, int limit)
    {
        final Set<ZoneTypeId> result = new TreeSet<>((a, b) -> toString(a).compareTo(toString(b)));
        result.addAll(EnumServiceInterface.instance().getEnumValues(ZoneTypeId.class));
        return result.
                stream().
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ZoneTypeId elm)
    {
        final ItemStack item = ItemServiceInterface.instance().createItem(CommonItems.App_Globe);
        return new ClickGuiItem(item, Messages.IconZone, (p, s, g) -> this.onChoose(p, s, g, elm), toString(elm));
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                null,
                null,
                this.itemPrevPage(),
                this.itemNextPage(),
                null,
                null,
                null,
                null,
                Main.itemCancel((p, s, g) -> s.setNewPage(this.prev), Messages.IconCancel)
                };
    }
    
    /**
     * Returns a free name with given prefix.
     * @param prefix
     * @return free name
     */
    private String getFreeName(String prefix)
    {
        int i = 1;
        while (true)
        {
            final String name = i == 1 ? prefix : prefix + "-" + i; //$NON-NLS-1$

            @SuppressWarnings("cast")
            final Optional<ArenaZoneHandler> handler = this.arena.getZones().stream().
                    map(s -> (ArenaZoneHandler) this.arena.getHandler(s)).
                    filter(s -> name.equals(s.getName())).
                    findFirst();
            
            if (!handler.isPresent())
            {
                return name;
            }
            
            i++;
        }
    }
    
    /**
     * type chosen
     * @param player
     * @param session
     * @param gui
     * @param type
     * @throws McException 
     */
    private void onChoose(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ZoneTypeId type) throws McException
    {
        final String text = this.getFreeName(type.name().toLowerCase());

        player.openAnvilGui(new QueryText(
                text,
                () -> {player.openClickGui(new Main(this.prev));},
                (s) -> this.onName(player, session, gui, type, s),
                player.encodeMessage(Messages.TextDescription)));
    }
    
    /**
     * name selected
     * @param player
     * @param session
     * @param gui
     * @param type
     * @param name 
     * @throws McException 
     */
    private void onName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ZoneTypeId type, String name) throws McException
    {
        this.onSave.accept(type, name);
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.zone_create_choose_type")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (zone types page)
         */
        @LocalizedMessage(defaultMessage = "Type for new zone in arena %1$s (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (zone types page)"}, args = {@Argument("arena name"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The Cancel
         */
        @LocalizedMessage(defaultMessage = "Cancel creation")
        @MessageComment({"cancel icon"})
        IconCancel,
        
        /**
         * The zone icon
         */
        @LocalizedMessage(defaultMessage = "type %1$s")
        @MessageComment(value = {"zone type icon"}, args=@Argument("type name"))
        IconZone,
        
        /**
         * Text description
         */
        @LocalizedMessageList({"Enter the name of the new zone.", "The name is only used internal."})
        @MessageComment("Text description for zone name")
        TextDescription,
    }
    
}
