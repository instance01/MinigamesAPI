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
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.impl.cmd.ArenaCommand;

/**
 * Click gui for editing arenas.
 * 
 * @author mepeisen
 */
public class ArenaEdit implements ClickGuiPageInterface
{

    /** arena to be edited. */
    private ArenaInterface arena;
    
    /** previous page. */
    private ClickGuiPageInterface prevPage;

    /**
     * @param arena
     * @param prevPage
     */
    public ArenaEdit(ArenaInterface arena, ClickGuiPageInterface prevPage)
    {
        this.arena = arena;
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
                Main.itemRefresh(this::onRefresh),
                null,
                null,
                null,
                null,
                Main.itemCloseGui()
            },
            null,
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Info), Messages.IconInfo, this::onInfo), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconDisplayName, this::onDisplayName), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconShortDescription, this::onShortDescription), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconLongDescription, this::onLongDescription), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconManual, this::onManual), 
            },
            {
                this.arena.isDisabled() || this.arena.isMaintenance() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Question), Messages.IconCheck, this::onCheck) : null, 
                this.arena.isDisabled() || this.arena.isMaintenance() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Apply), Messages.IconEnable, this::onEnable) : null, 
                !this.arena.isDisabled() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Flag), Messages.IconDisable, this::onDisable) : null, 
                this.arena.isDisabled() || this.arena.isMaintenance() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Save), Messages.IconExport, this::onExport) : null, //,
                !this.arena.isDisabled() && !this.arena.isMaintenance() && !this.arena.isMatch() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Play), Messages.IconStart, this::onStart) : null, 
                this.arena.isMatch() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Stop), Messages.IconStop, this::onStop) : null, 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Redo), Messages.IconHardReset, this::onHardReset), 
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_People), Messages.IconPlayers, this::onPlayers), 
            },
            {
                !this.arena.isMaintenance() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Modify), Messages.IconMaintenance, this::onMaintenance) : null, 
                null,
                this.arena.isMaintenance() ? Main.itemDelete(this::onDelete, Messages.IconDelete) : null, 
                this.arena.isMaintenance() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Retort), Messages.IconTest, this::onTest) : null, 
                this.arena.getState() == ArenaState.Join ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Person), Messages.IconInvite, this::onInvite) : null, 
                null,
                this.arena.getTeams().size() > 0 ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_User_group), Messages.IconTeams, this::onTeams) : null, 
            },
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Script), Messages.IconRules, this::onRules), 
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Wrench), Messages.IconComponents, this::onComponents), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Wrench), Messages.IconZones, this::onZones), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Wrench), Messages.IconSigns, this::onSigns), 
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Wrench), Messages.IconEntities, this::onEntities), 
            }
        }, 6);
    }
    
    /**
     * entites
     * @param player
     * @param session
     * @param gui
     */
    private void onEntities(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui edit entities
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * signs
     * @param player
     * @param session
     * @param gui
     */
    private void onSigns(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new SignsPage(this.arena, this));
    }
    
    /**
     * zones
     * @param player
     * @param session
     * @param gui
     */
    private void onZones(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new ZonesPage(this.arena, this));
    }
    
    /**
     * components
     * @param player
     * @param session
     * @param gui
     */
    private void onComponents(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new ComponentsPage(this.arena, this));
    }
    
    /**
     * rules
     * @param player
     * @param session
     * @param gui
     */
    private void onRules(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new RulesPage<>(this.getPageName(), this.arena, this, rt -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaRuleSetInterface.class, this.arena.getRuleSet(rt));
        }));
    }
    
    /**
     * teams
     * @param player
     * @param session
     * @param gui
     */
    private void onTeams(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui edit teams
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * arena invite
     * @param player
     * @param session
     * @param gui
     */
    private void onInvite(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui invite
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * test mode
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onTest(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.setTestState();
        this.onRefresh(player, session, gui);
    }
    
    /**
     * delete
     * @param player
     * @param session
     * @param gui
     */
    private void onDelete(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new YesNoQuestion(Messages.QuestionReallyDelete, this::onDeleteEx, this::onRefresh, Messages.QuestionReallyDeleteDetails));
    }
    
    /**
     * delete
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onDeleteEx(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.delete();
        session.setNewPage(this.prevPage);
    }
    
    /**
     * Maintenance
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onMaintenance(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        session.setNewPage(new YesNoQuestion(
                Messages.QuestionMaintenanceKickPlayers,
                this::onMaintenanceForce,
                this::onMaintenanceAfterMatch,
                Messages.QuestionMaintenanceKickPlayersDetails));
    }
    
    /**
     * Maintenance
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onMaintenanceForce(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.setMaintenance(true);
        this.onRefresh(player, session, gui);
    }
    
    /**
     * Maintenance
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onMaintenanceAfterMatch(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.setMaintenance(false);
        this.onRefresh(player, session, gui);
    }
    
    /**
     * players overview
     * @param player
     * @param session
     * @param gui
     */
    private void onPlayers(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui edit players
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * hard reset arena
     * @param player
     * @param session
     * @param gui
     */
    private void onHardReset(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui edit hard reset
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * stop match
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onStop(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.abort();
        this.onRefresh(player, session, gui);
    }
    
    /**
     * start match
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onStart(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.forceStart();
        this.onRefresh(player, session, gui);
    }
    
    /**
     * disable arena
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onDisable(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        session.setNewPage(new YesNoQuestion(
                Messages.QuestionDisableKickPlayers,
                this::onDisableForce,
                this::onDisableAfterMatch,
                Messages.QuestionDisableKickPlayersDetails));
    }
    
    /**
     * disable arena
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onDisableForce(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.setDisabledState(true);
        this.onRefresh(player, session, gui);
    }
    
    /**
     * disable arena
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onDisableAfterMatch(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.setDisabledState(false);
        this.onRefresh(player, session, gui);
    }
    
    /**
     * enable arena
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onEnable(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.arena.setEnabledState();
        this.onRefresh(player, session, gui);
    }
    
    /**
     * check arena
     * @param player
     * @param session
     * @param gui
     */
    private void onCheck(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support check
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * edit manual
     * @param player
     * @param session
     * @param gui
     */
    private void onManual(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new LocalizedLinesList(
                Messages.IconManual,
                this.arena.getManual(), (s) -> { this.arena.saveData(); },
                this));
    }
    
    /**
     * edit long description
     * @param player
     * @param session
     * @param gui
     */
    private void onLongDescription(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new LocalizedLinesList(
                Messages.IconLongDescription,
                this.arena.getDescription(), (s) -> { this.arena.saveData(); },
                this));
    }
    
    /**
     * edit short description
     * @param player
     * @param session
     * @param gui
     */
    private void onShortDescription(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new LocalizedStringList(
                Messages.IconShortDescription,
                this.arena.getShortDescription(), (s) -> { this.arena.saveData(); },
                this));
    }
    
    /**
     * edit display name
     * @param player
     * @param session
     * @param gui
     */
    private void onDisplayName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new LocalizedStringList(
                Messages.IconDisplayName,
                this.arena.getDisplayName(), (s) -> { this.arena.saveData(); },
                this));
    }
    
    /**
     * arena info
     * @param player
     * @param session
     * @param gui
     */
    private void onInfo(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        final MinigameInterface minigame = this.arena.getMinigame();
        final Serializable mgName = minigame == null ? "<invalid>" : this.arena.getMinigame().getDisplayName(); //$NON-NLS-1$
        player.sendMessage(Messages.InfoOutput,
                mgName,
                this.arena.getDisplayName(),
                this.arena.getShortDescription(),
                ArenaCommand.toString(this.arena.getState()),
                this.arena.getDescription()
                );
    }
    
    /**
     * arena export
     * @param player
     * @param session
     * @param gui
     */
    private void onExport(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support export
        player.sendMessage(Main.Messages.NotAvailable);
    }
    
    /**
     * refresh gui
     * @param player
     * @param session
     * @param gui
     */
    private void onRefresh(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this);
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
        return Messages.Title.toArg(this.arena.getInternalName(), this.arena.getDisplayName());
    }
    
    /**
     * The arena create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.arena_edit")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (arena edit page)
         */
        @LocalizedMessage(defaultMessage = "Arena %1$s - %2$s")
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
         * question: kick players
         */
        @LocalizedMessage(defaultMessage = "Kick players in lobby/match?")
        @MessageComment({"question: kick players"})
        QuestionMaintenanceKickPlayers,
        
        /**
         * question: kick players
         */
        @LocalizedMessageList({"Do you want to kick all players?", "If you choose yes all players are kicked and the arena is going to maintenance asap.", "If you choose no the arena is going to maintenance after current match."})
        @MessageComment({"question: kick players"})
        QuestionMaintenanceKickPlayersDetails,
        
        /**
         * question: kick players
         */
        @LocalizedMessage(defaultMessage = "Kick players in lobby/match?")
        @MessageComment({"question: kick players"})
        QuestionDisableKickPlayers,
        
        /**
         * question: kick players
         */
        @LocalizedMessageList({"Do you want to kick all players?", "If you choose yes all players are kicked and the arena is disabled asap.", "If you choose no the arena is disabled after current match."})
        @MessageComment({"question: kick players"})
        QuestionDisableKickPlayersDetails,
        
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
