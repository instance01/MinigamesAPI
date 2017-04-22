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
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;

/**
 * Click gui for editing components.
 * 
 * @author mepeisen
 */
public class ComponentEdit implements ClickGuiPageInterface
{

    /** arena to be edited. */
    private ArenaInterface arena;
    
    /** previous page. */
    private ClickGuiPageInterface prevPage;

    /** component to be edited. */
    private ArenaComponentHandler component;

    /**
     * @param arena
     * @param component
     * @param prevPage
     */
    public ComponentEdit(ArenaInterface arena, ArenaComponentHandler component, ClickGuiPageInterface prevPage)
    {
        this.arena = arena;
        this.component = component;
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
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Trackback), Messages.IconTeleport, this::onTeleport), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Script), Messages.IconRules, this::onRules)
            }
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
                this.component.getName(),
                this.component.getComponent().getTypeId().getPluginName(),
                this.component.getComponent().getTypeId().name());
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
                this.component.getName(),
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
        if (name.equals(this.component.getName()))
        {
            player.openClickGui(new Main(this));
            return;
        }
        
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
        
        this.component.setName(name);
        this.component.getComponent().saveConfig();
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
            session.setNewPage(new SelectMarkerPage(player, this.component.getComponent(), this));
        }
    }
    
    /**
     * teleport
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onTeleport(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.getBukkitPlayer().teleport(this.component.getComponent().getLocation());
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
        session.setNewPage(new RulesPage<>(this.getPageName(), this.component, this, rt -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaComponentHandler.class, this.component);
            McLibInterface.instance().setContext(ComponentRuleSetInterface.class, this.component.getRuleSet(rt));
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
        this.component.getComponent().delete();
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
        return Messages.Title.toArg(this.arena.getInternalName(), this.component.getName());
    }
    
    /**
     * The component create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.components_edit")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (component edit page)
         */
        @LocalizedMessage(defaultMessage = "Component %1$s - %2$s")
        @MessageComment(value = {"Gui title (component edit)"}, args = {@Argument("arena internal name"), @Argument("component name")})
        Title,
        
        /**
         * back to arenas
         */
        @LocalizedMessage(defaultMessage = "Back to components list")
        @MessageComment({"back to components"})
        IconBack,
        
        /**
         * info
         */
        @LocalizedMessage(defaultMessage = "Component info")
        @MessageComment({"info"})
        IconInfo,
        
        /**
         * name
         */
        @LocalizedMessage(defaultMessage = "Component name")
        @MessageComment({"component name"})
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
        @LocalizedMessage(defaultMessage = "Teleport to component")
        @MessageComment({"teleport"})
        IconTeleport,
        
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
            "component-name: %3$s",
            "component-type: %4$s/%5$s"
        })
        @MessageComment(value = {
            "The info"
        },args = {
                @Argument("arena display name"),
                @Argument("arena short description"),
                @Argument("component name"),
                @Argument("component type plugin"),
                @Argument("component type name"),
                })
        InfoOutput,
        
        /**
         * Name description
         */
        @LocalizedMessageList({"Enter the name of the component.", "The name is only used internal."})
        @MessageComment("Text description for component name")
        NameDescription,
    }
    
}
