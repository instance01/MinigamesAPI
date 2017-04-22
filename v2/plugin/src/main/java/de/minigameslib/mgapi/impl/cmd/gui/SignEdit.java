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
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;

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
            // TODO export/import signs
            // TODO copy&paste signs
            // TODO move/relocate signs with content (destroy original)/ without content (leave original untouched)
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
                this.sign.getName(),
                this.sign.getSign().getTypeId().getPluginName(),
                this.sign.getSign().getTypeId().name());
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
                this.sign.getName(),
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
        if (name.equals(this.sign.getName()))
        {
            player.openClickGui(new Main(this));
            return;
        }
        
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
        
        this.sign.setName(name);
        this.sign.getSign().saveConfig();
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
            session.setNewPage(new SelectMarkerPage(player, this.sign.getSign(), this));
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
        player.getBukkitPlayer().teleport(this.sign.getSign().getLocation());
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
        session.setNewPage(new RulesPage<>(this.getPageName(), this.sign, this, rt -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaSignHandler.class, this.sign);
            McLibInterface.instance().setContext(SignRuleSetInterface.class, this.sign.getRuleSet(rt));
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
        this.sign.getSign().delete();
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
        @MessageComment(value = {"Gui title (sign edit)"}, args = {@Argument("arena internal name"), @Argument("sign name")})
        Title,
        
        /**
         * back to arenas
         */
        @LocalizedMessage(defaultMessage = "Back to signs list")
        @MessageComment({"back to signs"})
        IconBack,
        
        /**
         * info
         */
        @LocalizedMessage(defaultMessage = "Sign info")
        @MessageComment({"info"})
        IconInfo,
        
        /**
         * name
         */
        @LocalizedMessage(defaultMessage = "Sign name")
        @MessageComment({"sign name"})
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
        @LocalizedMessage(defaultMessage = "Teleport to sign")
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
            "sign-name: %3$s",
            "sign-type: %4$s/%5$s"
        })
        @MessageComment(value = {
            "The info"
        },args = {
                @Argument("arena display name"),
                @Argument("arena short description"),
                @Argument("sign name"),
                @Argument("sign type plugin"),
                @Argument("sign type name"),
                })
        InfoOutput,
        
        /**
         * Name description
         */
        @LocalizedMessageList({"Enter the name of the sign.", "The name is only used internal."})
        @MessageComment("Text description for sign name")
        NameDescription,
    }
    
}
