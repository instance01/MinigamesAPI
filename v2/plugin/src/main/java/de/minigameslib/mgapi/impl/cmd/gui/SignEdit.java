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
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.impl.cmd.ArenaCommand;

/**
 * Click gui for editing signs.
 * 
 * @author mepeisen
 */
public class SignEdit implements ClickGuiPageInterface
{

    /** arena to be edited. */
    private ArenaInterface arena;
    
    /** previous page. */
    private ClickGuiPageInterface prevPage;

    /** sign to be edited. */
    private ArenaSignHandler sign;

    /**
     * @param arena
     * @param sign
     * @param prevPage
     */
    public SignEdit(ArenaInterface arena, ArenaSignHandler sign, ClickGuiPageInterface prevPage)
    {
        this.arena = arena;
        this.sign = sign;
        this.prevPage = prevPage;
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return Main.withFillers(new ClickGuiItem[][]{
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
        });
    }
    
    /**
     * info
     * @param player
     * @param session
     * @param gui
     */
    private void onInfo(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * name
     * @param player
     * @param session
     * @param gui
     */
    private void onName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * display marker
     * @param player
     * @param session
     * @param gui
     */
    private void onDisplayMarker(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * teleport
     * @param player
     * @param session
     * @param gui
     */
    private void onTeleport(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * rules
     * @param player
     * @param session
     * @param gui
     */
    private void onRules(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * delete
     * @param player
     * @param session
     * @param gui
     */
    private void onDelete(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO
        player.sendMessage(Main.Messages.NotAvailable);
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
        return Messages.Title.toArg(this.arena.getInternalName(), this.sign.getName());
    }
    
    /**
     * The sign create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.signs_edit")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (sign edit page)
         */
        @LocalizedMessage(defaultMessage = "Sign %1$s - %2$s")
        @MessageComment(value = {"Gui title (arena edit)"}, args = {@Argument("internal name"), @Argument("display name")})
        Title,
        
        /**
         * back to arenas
         */
        @LocalizedMessage(defaultMessage = "Back to arenas list")
        @MessageComment({"back to arenas"})
        IconBack,
        
        /**
         * info
         */
        @LocalizedMessage(defaultMessage = "Arena info")
        @MessageComment({"info"})
        IconInfo,
        
        /**
         * display name
         */
        @LocalizedMessage(defaultMessage = "Display name")
        @MessageComment({"display name"})
        IconDisplayName,
        
        /**
         * short description
         */
        @LocalizedMessage(defaultMessage = "Short description")
        @MessageComment({"short description"})
        IconShortDescription,
        
        /**
         * long description
         */
        @LocalizedMessage(defaultMessage = "Long description")
        @MessageComment({"long description"})
        IconLongDescription,
        
        /**
         * Manual
         */
        @LocalizedMessage(defaultMessage = "Manual")
        @MessageComment({"manual"})
        IconManual,
        
        /**
         * check
         */
        @LocalizedMessage(defaultMessage = "Arena check")
        @MessageComment({"check"})
        IconCheck,
        
        /**
         * enable
         */
        @LocalizedMessage(defaultMessage = "Enable")
        @MessageComment({"enable"})
        IconEnable,
        
        /**
         * disable
         */
        @LocalizedMessage(defaultMessage = "Disable")
        @MessageComment({"disable"})
        IconDisable,
        
        /**
         * start match
         */
        @LocalizedMessage(defaultMessage = "Start match")
        @MessageComment({"start match"})
        IconStart,
        
        /**
         * stop match
         */
        @LocalizedMessage(defaultMessage = "Stop match")
        @MessageComment({"stop match"})
        IconStop,
        
        /**
         * hard reset
         */
        @LocalizedMessage(defaultMessage = "Hard reset")
        @MessageComment({"hard reset"})
        IconHardReset,
        
        /**
         * players
         */
        @LocalizedMessage(defaultMessage = "Players")
        @MessageComment({"players"})
        IconPlayers,
        
        /**
         * maintenance
         */
        @LocalizedMessage(defaultMessage = "Maintenance (edit arena)")
        @MessageComment({"maintenance"})
        IconMaintenance,
        
        /**
         * delete
         */
        @LocalizedMessage(defaultMessage = "Delete")
        @MessageComment({"delete"})
        IconDelete,
        
        /**
         * test
         */
        @LocalizedMessage(defaultMessage = "Test mode")
        @MessageComment({"test"})
        IconTest,
        
        /**
         * invite
         */
        @LocalizedMessage(defaultMessage = "Invite")
        @MessageComment({"invite"})
        IconInvite,
        
        /**
         * rules
         */
        @LocalizedMessage(defaultMessage = "Rules")
        @MessageComment({"rules"})
        IconRules,
        
        /**
         * components
         */
        @LocalizedMessage(defaultMessage = "Components")
        @MessageComment({"components"})
        IconComponents,
        
        /**
         * zones
         */
        @LocalizedMessage(defaultMessage = "Zones")
        @MessageComment({"zones"})
        IconZones,
        
        /**
         * signs
         */
        @LocalizedMessage(defaultMessage = "Signs")
        @MessageComment({"Signs"})
        IconSigns,
        
        /**
         * entites
         */
        @LocalizedMessage(defaultMessage = "Entities")
        @MessageComment({"entities"})
        IconEntities,
        
        /**
         * teams
         */
        @LocalizedMessage(defaultMessage = "Teams")
        @MessageComment({"teams"})
        IconTeams,
        
        /**
         * export
         */
        @LocalizedMessage(defaultMessage = "Export")
        @MessageComment({"export"})
        IconExport,
        
        /**
         * question: really delete arena
         */
        @LocalizedMessage(defaultMessage = "Really delete arena?")
        @MessageComment({"question: Really delete arena"})
        QuestionReallyDelete,
        
        /**
         * question: really delete arena
         */
        @LocalizedMessageList({"Do you really want to delete this arena?", "The deletion can not be undone.", "If you want to use the arena later please export it first."})
        @MessageComment({"question: Really delete arena"})
        QuestionReallyDeleteDetails,
        
        /**
         * The command output of /mg2 arena
         * @see ArenaEdit (onInfo)
         */
        @LocalizedMessageList({
            "minigame: %1$s",
            "arena: %2$s - %3$s",
            "state: %4$s",
            "----------",
            "%5$s"
        })
        @MessageComment(value = {
            "The command output of /mg2 arena"
        },args = {
                @Argument("minigame display name"),
                @Argument("arena display name"),
                @Argument("arena short description"),
                @Argument("arena state"),
                @Argument("arena long description"),
                })
        InfoOutput,
    }
    
}
