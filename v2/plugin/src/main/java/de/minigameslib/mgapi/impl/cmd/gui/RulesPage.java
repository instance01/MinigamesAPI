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
import java.util.Collections;
import java.util.List;

import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
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

    /**
     * @param title 
     * @param container 
     * @param prev 
     */
    public RulesPage(Serializable title, RuleSetContainerInterface<T, Q> container, ClickGuiPageInterface prev)
    {
        this.title = title;
        this.container = container;
        this.prev = prev;
    }
    
    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.title, this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        // TODO
        return 0;
    }

    @Override
    protected List<T> getElements(int start, int limit)
    {
        // TODO
        return Collections.emptyList();
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, T elm)
    {
        // return Main.itemArena(elm, (p, s, g) -> onArena(p, s, g, elm));
        // TODO
        return null;
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                Main.itemHome(),
                Main.itemBack((p, s, g) -> s.setNewPage(this.prev), Messages.IconBack, this.title),
                Main.itemRefresh(this::onRefresh),
                Main.itemPrevPage(this::onPrevPage),
                Main.itemNextPage(this::onNextPage),
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
     */
    private void onRule(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, T rule)
    {
        // TODO session.setNewPage(new ArenaEdit(arena, this));
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
        @LocalizedMessage(defaultMessage = "rule %1$s/%2$s")
        @MessageComment(value = {"rule icon"}, args={@Argument("rule plugin name"), @Argument("rule enum name")})
        IconRule,
        
        /**
         * The back icon
         */
        @LocalizedMessage(defaultMessage = "back to %1$s")
        @MessageComment(value = {"back icon"}, args = {@Argument("title")})
        IconBack,
    }
    
}
