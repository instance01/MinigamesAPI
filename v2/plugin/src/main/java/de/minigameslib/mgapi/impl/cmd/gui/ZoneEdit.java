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
import java.util.Optional;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
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
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;
import de.minigameslib.mgapi.impl.cmd.marker.MarkerColorInterface;
import de.minigameslib.mgapi.impl.cmd.tool.AdminToolHelper;
import de.minigameslib.mgapi.impl.cmd.tool.MarkerToolHelper;

/**
 * Click gui for editing zones.
 * 
 * @author mepeisen
 */
public class ZoneEdit implements ClickGuiPageInterface
{

    /** arena to be edited. */
    private ArenaInterface arena;
    
    /** previous page. */
    private ClickGuiPageInterface prevPage;

    /** zone to be edited. */
    private ArenaZoneHandler zone;

    /**
     * @param arena
     * @param zone
     * @param prevPage
     */
    public ZoneEdit(ArenaInterface arena, ArenaZoneHandler zone, ClickGuiPageInterface prevPage)
    {
        this.arena = arena;
        this.zone = zone;
        this.prevPage = prevPage;
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(new ClickGuiItem[][]{
            {
                Main.itemHome(),
                Main.itemBack(this::onBack, Messages.IconBack),
                null,
                Main.itemDelete(this::onDelete, Messages.IconDelete),
                null,
                null,
                null,
                null,
                Main.itemCloseGui()
            },
            null,
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Info), Messages.IconInfo, this::onInfo), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconName, this::onName), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Target), Messages.IconDisplayMarker, this::onDisplayMarker), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Trackback), Messages.IconTeleportLower, this::onTeleportLower), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Trackback), Messages.IconTeleportHigher, this::onTeleportHigher),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Pinion), Messages.IconRelocateLower, this::onRelocateLower), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Pinion), Messages.IconRelocateHigher, this::onRelocateHigher),
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Script), Messages.IconRules, this::onRules)
            }
            // TODO export/import zones
            // TODO copy&paste zones
            // TODO move zones with content/ without content
        }, 6);
    }
    
    /**
     * info
     * @param player
     * @param session
     * @param gui
     */
    private void onInfo(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        player.sendMessage(Messages.InfoOutput,
                this.arena.getDisplayName().toArg(),
                this.arena.getShortDescription().toArg(),
                this.zone.getName(),
                this.zone.getZone().getTypeId().getPluginName(),
                this.zone.getZone().getTypeId().name());
    }
    
    /**
     * name
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.openAnvilGui(new QueryText(
                this.zone.getName(),
                () -> {player.openClickGui(new Main(this));},
                (s) -> this.onName(player, session, gui, s),
                player.encodeMessage(Messages.NameDescription)));
    }
    
    /**
     * name
     * @param player
     * @param session
     * @param gui
     * @param name 
     * @throws McException 
     */
    private void onName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, String name) throws McException
    {
        if (name.equals(this.zone.getName()))
        {
            player.openClickGui(new Main(this));
            return;
        }
        
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
        
        this.zone.setName(name);
        this.zone.getZone().saveConfig();
        player.openClickGui(new Main(this));
    }
    
    /**
     * display marker
     * @param player
     * @param session
     * @param gui
     */
    private void onDisplayMarker(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (!player.hasSmartGui())
        {
            player.sendMessage(CommonMessages.NoSmartGui);
        }
        else
        {
            session.setNewPage(new SelectMarkerPage(player, this.zone.getZone(), this));
        }
    }
    
    /**
     * relocate
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onRelocateLower(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        if (!this.arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        session.close();
        AdminToolHelper.onRelocateZoneLower(player, this.zone, z -> {
            // change (=re-create) marker if displayed
            if (player.hasSmartGui())
            {
                final MarkerToolHelper helper = MarkerToolHelper.instance(player);
                final MarkerColorInterface color = helper.getColor(z.getZone());
                if (color != null)
                {
                    helper.clearMarker(z.getZone());
                    helper.createMarker(z.getZone(), color);
                }
            }
            player.openClickGui(new Main(this));
        });
    }
    
    /**
     * relocate
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onRelocateHigher(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        if (!this.arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        session.close();
        AdminToolHelper.onRelocateZoneHigher(player, this.zone, z -> {
            // change (=re-create) marker if displayed
            if (player.hasSmartGui())
            {
                final MarkerToolHelper helper = MarkerToolHelper.instance(player);
                final MarkerColorInterface color = helper.getColor(z.getZone());
                if (color != null)
                {
                    helper.clearMarker(z.getZone());
                    helper.createMarker(z.getZone(), color);
                }
            }
            player.openClickGui(new Main(this));
        });
    }
    
    /**
     * teleport
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onTeleportLower(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.getBukkitPlayer().teleport(this.zone.getZone().getCuboid().getLowLoc());
        player.openClickGui(new Main(this));
    }
    
    /**
     * teleport
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onTeleportHigher(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.getBukkitPlayer().teleport(this.zone.getZone().getCuboid().getHighLoc());
        player.openClickGui(new Main(this));
    }
    
    /**
     * rules
     * @param player
     * @param session
     * @param gui
     */
    private void onRules(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new RulesPage<>(this.getPageName(), this.zone, this, rt -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this.zone);
            McLibInterface.instance().setContext(ZoneRuleSetInterface.class, this.zone.getRuleSet(rt));
        }));
    }
    
    /**
     * delete
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onDelete(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        if (!this.arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        this.zone.getZone().delete();
        session.setNewPage(this.prevPage);
    }
    
    /**
     * back to previous gui
     * @param player
     * @param session
     * @param gui
     */
    private void onBack(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this.prevPage);
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.arena.getInternalName(), this.zone.getName());
    }
    
    /**
     * The zone create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.zones_edit")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (zone edit page)
         */
        @LocalizedMessage(defaultMessage = "Zone %1$s - %2$s")
        @MessageComment(value = {"Gui title (zone edit)"}, args = {@Argument("arena internal name"), @Argument("zone name")})
        Title,
        
        /**
         * back to arenas
         */
        @LocalizedMessage(defaultMessage = "Back to zones list")
        @MessageComment({"back to zones"})
        IconBack,
        
        /**
         * info
         */
        @LocalizedMessage(defaultMessage = "Zone info")
        @MessageComment({"info"})
        IconInfo,
        
        /**
         * name
         */
        @LocalizedMessage(defaultMessage = "Zone name")
        @MessageComment({"zone name"})
        IconName,
        
        /**
         * display marker
         */
        @LocalizedMessage(defaultMessage = "Show display marker")
        @MessageComment({"display marker"})
        IconDisplayMarker,
        
        /**
         * teleport
         */
        @LocalizedMessage(defaultMessage = "Teleport to zone (lower bounds)")
        @MessageComment({"teleport"})
        IconTeleportLower,
        
        /**
         * teleport
         */
        @LocalizedMessage(defaultMessage = "Teleport to zone (higher bounds)")
        @MessageComment({"teleport"})
        IconTeleportHigher,
        
        /**
         * relocate
         */
        @LocalizedMessage(defaultMessage = "Relocate lower bounds")
        @MessageComment({"relocate"})
        IconRelocateLower,
        
        /**
         * relocate
         */
        @LocalizedMessage(defaultMessage = "Relocate higher bounds")
        @MessageComment({"relocate"})
        IconRelocateHigher,
        
        /**
         * delete
         */
        @LocalizedMessage(defaultMessage = "Delete")
        @MessageComment({"delete"})
        IconDelete,
        
        /**
         * rules
         */
        @LocalizedMessage(defaultMessage = "Rules")
        @MessageComment({"rules"})
        IconRules,
        
        /**
         * The info
         */
        @LocalizedMessageList({
            "arena: %1$s - %2$s",
            "zone-name: %3$s",
            "zone-type: %4$s/%5$s"
        })
        @MessageComment(value = {
            "The info"
        },args = {
                @Argument("arena display name"),
                @Argument("arena short description"),
                @Argument("zone name"),
                @Argument("zone type plugin"),
                @Argument("zone type name"),
                })
        InfoOutput,
        
        /**
         * Name description
         */
        @LocalizedMessageList({"Enter the name of the zone.", "The name is only used internal."})
        @MessageComment("Text description for zone name")
        NameDescription,
    }
    
}
