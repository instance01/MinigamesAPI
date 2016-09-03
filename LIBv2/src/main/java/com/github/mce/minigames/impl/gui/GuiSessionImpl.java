/*
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

package com.github.mce.minigames.impl.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.mce.minigames.api.context.MinigameStorage;
import com.github.mce.minigames.api.gui.ClickGuiInterface;
import com.github.mce.minigames.api.gui.ClickGuiItem;
import com.github.mce.minigames.api.gui.ClickGuiPageInterface;
import com.github.mce.minigames.api.gui.GuiSessionInterface;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.MinigamesPlugin;
import com.github.mce.minigames.impl.nms.InventoryManagerInterface;

/**
 * Implementation of a gui session
 * 
 * @author mepeisen
 */
public class GuiSessionImpl implements GuiSessionInterface
{
    
    /** the gui interface. */
    private ClickGuiInterface         gui;
    /** the arena player. */
    private ArenaPlayerInterface      player;
    /** the current inventory name. */
    private LocalizedMessageInterface currentName;
    /** the current items. */
    private ClickGuiItem[][]          currentItems;
    /** the line count. */
    private int                       lineCount;
    
    /**
     * Constructor
     * 
     * @param gui
     *            the gui to be used
     * @param player
     *            the arena player to be used
     */
    public GuiSessionImpl(ClickGuiInterface gui, ArenaPlayerInterface player)
    {
        this.gui = gui;
        this.player = player;
        this.currentName = gui.getInitialPage().getPageName();
        this.currentItems = gui.getInitialPage().getItems();
        this.lineCount = gui.getLineCount();
        
        final String name = player.getBukkitPlayer().isOp() ? this.currentName.toAdminMessage(player.getPreferredLocale()) : this.currentName.toUserMessage(player.getPreferredLocale());
        final ItemStack[] items = this.toItemStack();
        MinigamesPlugin.nms().create(InventoryManagerInterface.class).openInventory(player.getBukkitPlayer(), name, items);
    }
    
    /**
     * Converts the existing items to item stack.
     * @return item stack.
     */
    private ItemStack[] toItemStack()
    {
        final List<ItemStack> result = new ArrayList<>();
        for (int line = 0; line < this.lineCount; line++)
        {
            final ClickGuiItem[] itemline = (this.currentItems == null || this.currentItems.length <= line) ? null : this.currentItems[line];
            for (int column = 0; column < 9; column++)
            {
                if (itemline == null || itemline.length <= column)
                {
                    result.add(new ItemStack(Material.AIR));
                }
                else
                {
                    final ItemStack stack = itemline[column].getItemStack().clone();
                    final ItemMeta meta = stack.getItemMeta();
                    final String displayName = toColorsString(
                            this.player.getBukkitPlayer().isOp() ? itemline[column].getDisplayName().toAdminMessage(this.player.getPreferredLocale()) : itemline[column].getDisplayName().toUserMessage(this.player.getPreferredLocale()),
                            line + ":" + column //$NON-NLS-1$
                            );
                    meta.setDisplayName(displayName);
                    stack.setItemMeta(meta);
                    result.add(stack);
                }
            }
        }
        return result.toArray(new ItemStack[result.size()]);
    }
    
    /**
     * Returns a colored string that hides data from users view.
     * @param name item name
     * @param hiddenString the hidden string
     * @return colored hidden text
     */
    private static String toColorsString(String name, String hiddenString)
    {
        final String target = toHexString(hiddenString);
        final StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(' ');
        for (int i = 0; i < target.length(); i++)
        {
            builder.append('§');
            builder.append(target.charAt(i));
        }
        return builder.toString();
    }
    
    /**
     * Strips the string that was originally encoded by toColorsString
     * @param src
     * @return hiddenString
     */
    private static String stripColoredString(String src)
    {
        int index = src.lastIndexOf(' ');
        final StringBuilder hex = new StringBuilder();
        for (int i = index + 1; i < src.length(); i+=3)
        {
            hex.append(src.substring(i + 1,  i + 3));
        }
        return fromHexString(hex.toString());
    }
    
    /**
     * Converts given string to hey code.
     * 
     * @param src
     *            source string
     * @return hex string
     */
    private static String toHexString(String src)
    {
        final byte[] ba = src.getBytes();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++)
        {
            str.append(String.format("%02x", ba[i])); //$NON-NLS-1$
        }
        return str.toString();
    }
    
    /**
     * Converts given hex string to a normal string.
     * 
     * @param hex
     *            hex string
     * @return normal string.
     */
    private static String fromHexString(String hex)
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2)
        {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }
    
    @Override
    public void readFromConfig(ConfigurationSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void writeToConfig(ConfigurationSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ArenaPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    @Override
    public ClickGuiInterface getGui()
    {
        return this.gui;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#setNewPage(com.github.mce.minigames.api.gui.ClickGuiPageInterface)
     */
    @Override
    public void setNewPage(ClickGuiPageInterface page)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void close()
    {
        this.player.getBukkitPlayer().closeInventory();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#getGuiStorage()
     */
    @Override
    public MinigameStorage getGuiStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#getPlayerSessionStorage()
     */
    @Override
    public MinigameStorage getPlayerSessionStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#getPlayerPersistentStorage()
     */
    @Override
    public MinigameStorage getPlayerPersistentStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
