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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
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
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mgapi.api.rules.RuleSetContainerInterface;
import de.minigameslib.mgapi.api.rules.RuleSetInterface;
import de.minigameslib.mgapi.api.rules.RuleSetType;

/**
 * Click gui for editing rules.
 * 
 * @author mepeisen
 * @param <T> rule set type class
 * @param <Q> rule set class
 */
public class RuleEdit<T extends RuleSetType, Q extends RuleSetInterface<T>> extends AbstractPage<ConfigurationValueInterface>
{

    /** container to be edited. */
    private RuleSetContainerInterface<T, Q> container;
    
    /** previous page. */
    private ClickGuiPageInterface prevPage;

    /** rule to be edited. */
    private T rule;

    /** the rule set type configuration options. */
    private List<ConfigurationValueInterface> configOptions = new ArrayList<>();

    /** context provider */
    private McRunnable contextProvider;

    /** title */
    private Serializable title;
    
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(RuleEdit.class.getName());

    /**
     * @param title
     * @param container
     * @param rule
     * @param prevPage
     * @param contextProvider
     */
    public RuleEdit(Serializable title, RuleSetContainerInterface<T, Q> container, T rule, ClickGuiPageInterface prevPage, McRunnable contextProvider)
    {
        this.title = title;
        this.container = container;
        this.rule = rule;
        this.prevPage = prevPage;
        
        final Class<? extends ConfigurationValueInterface> clazz = rule.getConfigClass();
        if (clazz != null)
        {
            for (final ConfigurationValueInterface value : clazz.getEnumConstants())
            {
                this.configOptions.add(value);
            }
        }
        this.contextProvider = contextProvider;
    }

    @Override
    protected int count()
    {
        return this.configOptions.size();
    }

    @Override
    protected List<ConfigurationValueInterface> getElements(int start, int limit)
    {
        return this.configOptions.stream().skip(start).limit(limit).collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ConfigurationValueInterface elm)
    {
        try
        {
            return ConfigServiceInterface.instance().createGuiEditorItem(
                    elm,
                    () -> {
                        try
                        {
                            // TODO let components check the rule before being saved to config file
                            this.container.reconfigureRuleSet(this.rule);
                        }
                        catch (McException e)
                        {
                            LOGGER.log(Level.WARNING, "problems while reconfigure rule " + this.rule, e); //$NON-NLS-1$
                        }
                    },
                    this.contextProvider);
        }
        catch (McException e)
        {
            LOGGER.log(Level.WARNING, "problems while mapping config element " + elm, e); //$NON-NLS-1$
            return null;
        }
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
            Main.itemHome(),
            Main.itemBack(this::onBack, Messages.IconBack, this.title),
            null,
            Main.itemDelete(this::onDelete, Messages.IconDelete),
            null,
            null,
            null,
            null,
            Main.itemCloseGui()
        };
    }
    
    /**
     * delete
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    @SuppressWarnings("unchecked")
    private void onDelete(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.container.removeRuleSets(this.rule);
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
        return Messages.Title.toArg(this.title);
    }
    
    /**
     * The sign create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.rules_edit")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (sign edit page)
         */
        @LocalizedMessage(defaultMessage = "Rules for %1$s")
        @MessageComment(value = {"Gui title (rules edit)"}, args = {@Argument("component name")})
        Title,
        
        /**
         * back to prev
         */
        @LocalizedMessage(defaultMessage = "Back to %1$s")
        @MessageComment(value = {"back to prev"}, args = {@Argument("component name")})
        IconBack,
        
        /**
         * delete
         */
        @LocalizedMessage(defaultMessage = "Delete")
        @MessageComment({"delete"})
        IconDelete,
    }
    
}
