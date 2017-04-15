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
import java.util.stream.Collectors;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mgapi.impl.cmd.marker.MarkerColorInterface;
import de.minigameslib.mgapi.impl.cmd.marker.MarkerColorProvider;
import de.minigameslib.mgapi.impl.cmd.tool.MarkerToolHelper;

/**
 * Page to set display markers
 * 
 * @author mepeisen
 */
public class SelectMarkerPage extends AbstractPage<MarkerColorInterface>
{
    
    /** previous page */
    private ClickGuiPageInterface prev;

    /** the target component. */
    private ComponentInterface component;

    /** the target sign. */
    private SignInterface sign;

    /** the target zone. */
    private ZoneInterface zone;

    /** the underlying player */
    private McPlayerInterface player;

    /**
     * @param player 
     * @param sign
     * @param prev
     */
    public SelectMarkerPage(McPlayerInterface player, SignInterface sign, ClickGuiPageInterface prev)
    {
        this.player = player;
        this.sign = sign;
        this.prev = prev;
    }

    /**
     * @param player 
     * @param zone
     * @param prev
     */
    public SelectMarkerPage(McPlayerInterface player, ZoneInterface zone, ClickGuiPageInterface prev)
    {
        this.player = player;
        this.zone = zone;
        this.prev = prev;
    }

    /**
     * @param player 
     * @param component
     * @param prev
     */
    public SelectMarkerPage(McPlayerInterface player, ComponentInterface component, ClickGuiPageInterface prev)
    {
        this.player = player;
        this.component = component;
        this.prev = prev;
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(page(), count());
    }

    @Override
    protected int count()
    {
        return MarkerColorProvider.getColors().size();
    }

    @Override
    protected List<MarkerColorInterface> getElements(int start, int limit)
    {
        return MarkerColorProvider.getColors().
                stream().
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, MarkerColorInterface elm)
    {
        final MarkerToolHelper helper = MarkerToolHelper.instance(this.player);
        final ItemStack icon = elm.getIcon();
        
        final boolean isSet = (this.sign != null && helper.getColor(this.sign) == elm) ||
                (this.component != null && helper.getColor(this.component) == elm) ||
                (this.zone != null && helper.getColor(this.zone) == elm);
        
        if (isSet)
        {
            final ItemMeta meta = icon.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            icon.setItemMeta(meta);
        }
        
        return new ClickGuiItem(icon, elm.getTitle(), (p, s, g) -> {
            if (isSet)
            {
                this.onRemove(p, s, g, elm);
            }
            else
            {
                this.onChoose(p, s, g, elm);
            }
        });
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
     * color chosen
     * @param p
     * @param session
     * @param gui
     * @param color
     * @throws McException 
     */
    private void onChoose(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui, MarkerColorInterface color) throws McException
    {
        final MarkerToolHelper helper = MarkerToolHelper.instance(p);
        if (this.sign != null)
        {
            helper.createMarker(this.sign, color);
        }
        else if (this.component != null)
        {
            helper.createMarker(this.component, color);
        }
        else if (this.zone != null)
        {
            helper.createMarker(this.zone, color);
        }
        p.openClickGui(new Main(this.prev));
    }
    
    /**
     * color remove
     * @param p
     * @param session
     * @param gui
     * @param color
     * @throws McException 
     */
    private void onRemove(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui, MarkerColorInterface color) throws McException
    {
        final MarkerToolHelper helper = MarkerToolHelper.instance(p);
        if (this.sign != null)
        {
            helper.clearMarker(this.sign);
        }
        else if (this.component != null)
        {
            helper.clearMarker(this.component);
        }
        else if (this.zone != null)
        {
            helper.clearMarker(this.zone);
        }
        session.setNewPage(this.prev);
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.select_marker")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (marker colors page)
         */
        @LocalizedMessage(defaultMessage = "Select marker color - page %1$d of %2$d")
        @MessageComment(value = {"Gui title (marker colors page)"}, args = {@Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The Cancel
         */
        @LocalizedMessage(defaultMessage = "Cancel creation")
        @MessageComment({"cancel icon"})
        IconCancel,
        
        /**
         * The sign icon
         */
        @LocalizedMessage(defaultMessage = "type %1$s")
        @MessageComment(value = {"sign type icon"}, args=@Argument("sign name"))
        IconSign,
        
        /**
         * Text description
         */
        @LocalizedMessageList({"Enter the name of the new sign.", "The name is only used internal."})
        @MessageComment("Text description for sign name")
        TextDescription,
    }
    
}
