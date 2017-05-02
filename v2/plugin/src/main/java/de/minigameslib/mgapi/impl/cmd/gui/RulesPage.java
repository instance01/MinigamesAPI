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
import java.util.Set;
import java.util.TreeSet;
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
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.rules.RuleSetContainerInterface;
import de.minigameslib.mgapi.api.rules.RuleSetInterface;
import de.minigameslib.mgapi.api.rules.RuleSetType;

/**
 * Page with object rules
 * 
 * @author mepeisen
 * @param <T> rule set type class
 * @param <Q> rule set class
 */
public class RulesPage<T extends RuleSetType, Q extends RuleSetInterface<T>> extends AbstractPage<T>
{
    
    /** previous page */
    private ClickGuiPageInterface prev;
    
    /** container */
    private RuleSetContainerInterface<T, Q> container;
    
    /** page title */
    private Serializable title;

    /** context provider */
    private McConsumer<T> contextProvider;

    /**
     * @param title 
     * @param container 
     * @param prev 
     * @param contextProvider 
     */
    public RulesPage(Serializable title, RuleSetContainerInterface<T, Q> container, ClickGuiPageInterface prev, McConsumer<T> contextProvider)
    {
        this.title = title;
        this.container = container;
        this.prev = prev;
        this.contextProvider = contextProvider;
    }
    
    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.title, this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        return this.container.getAppliedRuleSetTypes().size() + this.container.getAvailableRuleSetTypes().size();
    }
    
    /**
     * Converts ruleset type to string
     * @param type
     * @return ruleset type
     */
    private String toString(T type)
    {
        return type.getPluginName() + "/" + type.name(); //$NON-NLS-1$
    }

    @Override
    protected List<T> getElements(int start, int limit)
    {
        final Set<T> result = new TreeSet<>((a, b) -> toString(a).compareTo(toString(b)));
        result.addAll(this.container.getAppliedRuleSetTypes());
        result.addAll(this.container.getAvailableRuleSetTypes());
        return result.
                stream().
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, T elm)
    {
        final ItemStack item = ItemServiceInterface.instance().createItem(CommonItems.App_Script);
        final boolean isSet = this.container.isApplied(elm);
        
        if (isSet)
        {
            final ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        
        return new ClickGuiItem(item, Messages.IconRule, (p, s, g) -> {
            if (isSet)
            {
                this.onRule(p, s, g, elm);
            }
            else
            {
                this.onCreate(p, s, g, elm);
            }
        }, toString(elm));
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                Main.itemHome(),
                Main.itemBack((p, s, g) -> s.setNewPage(this.prev), Messages.IconBack, this.title),
                Main.itemRefresh(this::onRefresh),
                this.itemPrevPage(),
                this.itemNextPage(),
                null,
                null,
                null,
                Main.itemCloseGui()
                };
    }
    
    /**
     * rule
     * @param player
     * @param session
     * @param gui
     * @param rule
     * @throws McException 
     */
    @SuppressWarnings("unchecked")
    private void onCreate(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, T rule) throws McException
    {
        this.container.applyRuleSets(rule);
        session.setNewPage(new RuleEdit<>(this.title, this.container, rule, this, () -> this.contextProvider.accept(rule)));
    }
    
    /**
     * rule
     * @param player
     * @param session
     * @param gui
     * @param rule
     */
    private void onRule(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, T rule)
    {
        session.setNewPage(new RuleEdit<>(this.title, this.container, rule, this, () -> this.contextProvider.accept(rule)));
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.rules")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (rules page)
         */
        @LocalizedMessage(defaultMessage = "Rules of %1$s (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (rules page)"}, args = {@Argument("title"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The rules icon
         */
        @LocalizedMessage(defaultMessage = "rule %1$s")
        @MessageComment(value = {"rule icon"}, args={@Argument("rule type name")})
        IconRule,
        
        /**
         * The back icon
         */
        @LocalizedMessage(defaultMessage = "back to %1$s")
        @MessageComment(value = {"back icon"}, args = {@Argument("title")})
        IconBack,
    }
    
}
