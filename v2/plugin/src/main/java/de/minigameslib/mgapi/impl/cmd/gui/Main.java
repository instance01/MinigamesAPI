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

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.gui.ClickGuiId;
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
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mgapi.api.ExtensionInterface;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;

/**
 * The main menu.
 * 
 * @author mepeisen
 */
public class Main implements ClickGuiInterface, ClickGuiPageInterface
{
    
    /** the max line count. */
    public static int LINE_COUNT = 6;
    
    /** the max col count. */
    public static int COL_COUNT = 9;
    
    /** initial page. */
    private ClickGuiPageInterface initialPage = this;
    
    /**
     * Constructor
     */
    public Main()
    {
        super();
    }

    /**
     * Constructor
     * @param initialPage
     */
    public Main(ClickGuiPageInterface initialPage)
    {
        super();
        this.initialPage = initialPage;
    }

    @Override
    public ClickGuiId getUniqueId()
    {
        return MgClickGuis.Main;
    }
    
    @Override
    public ClickGuiPageInterface getInitialPage()
    {
        return this.initialPage;
    }
    
    @Override
    public int getLineCount()
    {
        return LINE_COUNT;
    }

    @Override
    public LocalizedMessageInterface getPageName()
    {
        return Messages.Title;
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(new ClickGuiItem[][]{
            {
                itemOptions(this::onOptions),
                null,
                itemMinigames(this::onMinigames),
                itemExtensions(this::onExtensions),
                null,
                itemMarketplace(this::onMarketplace),
                null,
                null,
                itemCloseGui()
            },
            null,
            {
                itemArenas(this::onArenas),
            }
        }, 6);
    }
    
    /**
     * Close gui
     * @param player
     * @param session
     * @param gui
     */
    private static void onClose(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.close();
    }
    
    /**
     * Home
     * @param player
     * @param session
     * @param gui
     */
    private static void onHome(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new Main());
    }
    
    /**
     * Arenas icon
     * @param player
     * @param session
     * @param gui
     */
    private void onArenas(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(new ArenasPage());
    }
    
    /**
     * Options icon
     * @param player
     * @param session
     * @param gui
     */
    private void onOptions(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support options
        player.sendMessage(Messages.NotAvailable);
    }
    
    /**
     * Options icon
     * @param player
     * @param session
     * @param gui
     */
    private void onMinigames(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support minigames
        player.sendMessage(Messages.NotAvailable);
    }
    
    /**
     * Options icon
     * @param player
     * @param session
     * @param gui
     */
    private void onExtensions(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support extensions
        player.sendMessage(Messages.NotAvailable);
    }
    
    /**
     * Options icon
     * @param player
     * @param session
     * @param gui
     */
    private void onMarketplace(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // TODO gui support marketplace
        player.sendMessage(Messages.NotAvailable);
    }
    
    /**
     * market place icon
     * @param handler 
     * @return market place icon
     */
    public static ClickGuiItem itemMarketplace(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Euro), Messages.IconMarketplace, handler); 
    }
    
    /**
     * arenas icon
     * @param handler 
     * @return arenas icon
     */
    public static ClickGuiItem itemArenas(ClickGuiItem.GuiItemHandler handler)
    {
        final ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        final ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return new ClickGuiItem(itemStack, Messages.IconArenas, handler);
    }
    
    /**
     * arenas icon
     * @param arena 
     * @param handler 
     * @return arenas icon
     */
    public static ClickGuiItem itemArena(ArenaInterface arena, ClickGuiItem.GuiItemHandler handler)
    {
        final ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        final ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return new ClickGuiItem(itemStack, arena.getDisplayName(), handler);
    }
    
    /**
     * minigames icon
     * @param handler 
     * @return minigames icon
     */
    public static ClickGuiItem itemMinigames(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(new ItemStack(Material.REDSTONE), Messages.IconMinigames, handler);
    }
    
    /**
     * minigames icon
     * @param minigame 
     * @param handler 
     * @return minigames icon
     */
    public static ClickGuiItem itemMinigame(MinigameInterface minigame, ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(new ItemStack(Material.REDSTONE), minigame.getDisplayName(), handler);
    }
    
    /**
     * extensions icon
     * @param handler 
     * @return extensions icon
     */
    public static ClickGuiItem itemExtensions(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Toolbox), Messages.IconExtensions, handler); 
    }
    
    /**
     * extensions icon
     * @param extension
     * @param handler 
     * @return extensions icon
     */
    public static ClickGuiItem itemExtension(ExtensionInterface extension, ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Toolbox), extension.getDisplayName(), handler); 
    }
    
    /**
     * home icon
     * @return home icon
     */
    public static ClickGuiItem itemHome()
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Home), Messages.IconBackToMainMenu, Main::onHome); 
    }
    
    /**
     * options icon
     * @param handler 
     * @return options icon
     */
    public static ClickGuiItem itemOptions(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Pinion), Messages.IconOptions, handler); 
    }
    
    /**
     * close gui icon
     * @return close gui icon
     */
    public static ClickGuiItem itemCloseGui()
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Close), Messages.IconClose, Main::onClose); 
    }
    
    /**
     * delete icon
     * @param handler
     * @param name
     * @param nameargs 
     * @return new icon
     */
    public static ClickGuiItem itemDelete(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Erase), name, handler, nameargs); 
    }
    
    /**
     * cancel icon
     * @param handler
     * @param name
     * @param nameargs 
     * @return new icon
     */
    public static ClickGuiItem itemCancel(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Erase), name, handler, nameargs); 
    }
    
    /**
     * new icon
     * @param handler
     * @param name
     * @param nameargs 
     * @return new icon
     */
    public static ClickGuiItem itemNew(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_New), name, handler, nameargs); 
    }
    
    /**
     * back icon
     * @param handler
     * @param name
     * @param nameargs 
     * @return back icon
     */
    public static ClickGuiItem itemBack(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Back), name, handler, nameargs); 
    }
    
    /**
     * back icon
     * @param handler
     * @return back icon
     */
    public static ClickGuiItem itemRefresh(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Refresh), Messages.IconRefresh, handler); 
    }
    
    /**
     * prev page icon
     * @param handler 
     * @return prev page icon
     */
    public static ClickGuiItem itemPrevPage(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Previous), Messages.IconPreviousPage, handler); 
    }
    
    /**
     * next page icon
     * @param handler 
     * @return next page icon
     */
    public static ClickGuiItem itemNextPage(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Next), Messages.IconNextPage, handler); 
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.main")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (main menu)
         */
        @LocalizedMessage(defaultMessage = "MAIN - Minigames v2")
        @MessageComment({"Gui title (main menu)"})
        Title,
        
        /**
         * The arenas icon
         */
        @LocalizedMessage(defaultMessage = "Arenas")
        @MessageComment({"Arenas icon"})
        IconArenas,
        
        /**
         * The options icon
         */
        @LocalizedMessage(defaultMessage = "Options/Configuration")
        @MessageComment({"Options icon"})
        IconOptions,
        
        /**
         * The minigames icon
         */
        @LocalizedMessage(defaultMessage = "Minigames")
        @MessageComment({"Minigames icon"})
        IconMinigames,
        
        /**
         * The extensions icon
         */
        @LocalizedMessage(defaultMessage = "Extensions")
        @MessageComment({"Extensions icon"})
        IconExtensions,
        
        /**
         * The marketplace icon
         */
        @LocalizedMessage(defaultMessage = "Marketplace")
        @MessageComment({"Marketplace icon"})
        IconMarketplace,
        
        /**
         * option not available
         */
        @LocalizedMessage(defaultMessage = "This option is not available", severity = MessageSeverityType.Error)
        @MessageComment({"Error message: option not available"})
        NotAvailable,
        
        // common messages
        
        /**
         * The main menu icon
         */
        @LocalizedMessage(defaultMessage = "Back to main menu")
        @MessageComment({"main menu icon"})
        IconBackToMainMenu,
        
        /**
         * The close gui icon
         */
        @LocalizedMessage(defaultMessage = "Close GUI")
        @MessageComment({"close icon"})
        IconClose,
        
        /**
         * The prev page icon
         */
        @LocalizedMessage(defaultMessage = "Previous page")
        @MessageComment({"prev page icon"})
        IconPreviousPage,
        
        /**
         * The next page icon
         */
        @LocalizedMessage(defaultMessage = "Next page")
        @MessageComment({"next page icon"})
        IconNextPage,
        
        /**
         * The refresh icon
         */
        @LocalizedMessage(defaultMessage = "Refresh")
        @MessageComment({"refresh icon"})
        IconRefresh,
        
    }
    
}
