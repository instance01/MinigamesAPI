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

import org.bukkit.inventory.ItemStack;

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
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

/**
 * Page with arena type; choose type for new arena
 * 
 * @author mepeisen
 */
public class ArenaCreateChooseArenaType extends AbstractPage<ArenaTypeInterface>
{

    /** the arena */
    private McBiConsumer<ArenaTypeInterface, String> onSave;
    
    /** previous page */
    private ClickGuiPageInterface prev;

    /** the underlying minigame. */
    private MinigameInterface minigame;

    /**
     * @param minigame 
     * @param onSave
     * @param prev
     */
    public ArenaCreateChooseArenaType(MinigameInterface minigame, McBiConsumer<ArenaTypeInterface, String> onSave, ClickGuiPageInterface prev)
    {
        this.minigame = minigame;
        this.onSave = onSave;
        this.prev = prev;
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.minigame.getName(), this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        return this.minigame.getTypeCount();
    }
    
    /**
     * Converts arena type to string
     * @param compType
     * @return arena type
     */
    private String toString(ArenaTypeInterface compType)
    {
        return compType.getPluginName() + "/" + compType.name(); //$NON-NLS-1$
    }

    @Override
    protected List<ArenaTypeInterface> getElements(int start, int limit)
    {
        final Set<ArenaTypeInterface> result = new TreeSet<>((a, b) -> toString(a).compareTo(toString(b)));
        result.addAll(this.minigame.getTypes(0, Integer.MAX_VALUE));
        return result.
                stream().
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ArenaTypeInterface elm)
    {
        final ItemStack item = ItemServiceInterface.instance().createItem(CommonItems.App_Component);
        return new ClickGuiItem(item, Messages.IconComponent, (p, s, g) -> this.onChoose(p, s, g, elm), toString(elm));
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
     * Returns a free name with given prefix.
     * @param prefix
     * @return free name
     */
    private String getFreeName(String prefix)
    {
        int i = 1;
        while (true)
        {
            final String name = i == 1 ? prefix : prefix + "-" + i; //$NON-NLS-1$

            if (MinigamesLibInterface.instance().getArena(name) == null)
            {
                return name;
            }
            
            i++;
        }
    }
    
    /**
     * type chosen
     * @param player
     * @param session
     * @param gui
     * @param type
     * @throws McException 
     */
    private void onChoose(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ArenaTypeInterface type) throws McException
    {
        final String text = this.getFreeName(type.name().toLowerCase());

        player.openAnvilGui(new QueryText(
                text,
                () -> {player.openClickGui(new Main(this.prev));},
                (s) -> this.onName(player, session, gui, type, s),
                player.encodeMessage(Messages.TextDescription)));
    }
    
    /**
     * name selected
     * @param player
     * @param session
     * @param gui
     * @param type
     * @param name 
     * @throws McException 
     */
    private void onName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ArenaTypeInterface type, String name) throws McException
    {
        this.onSave.accept(type, name);
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.arena_create_choose_type")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (arena types page)
         */
        @LocalizedMessage(defaultMessage = "Type for new %1$s arena (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (arena types page)"}, args = {@Argument("minigame name"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The Cancel
         */
        @LocalizedMessage(defaultMessage = "Cancel creation")
        @MessageComment({"cancel icon"})
        IconCancel,
        
        /**
         * The component icon
         */
        @LocalizedMessage(defaultMessage = "type %1$s")
        @MessageComment(value = {"component type icon"}, args=@Argument("type name"))
        IconComponent,
        
        /**
         * Text description
         */
        @LocalizedMessageList({"Enter the name of the new arena.", "The name is only used internal."})
        @MessageComment("Text description for arena name")
        TextDescription,
    }
    
}
