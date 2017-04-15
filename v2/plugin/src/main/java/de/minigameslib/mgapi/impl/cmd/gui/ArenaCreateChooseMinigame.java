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

import de.minigameslib.mclib.api.McException;
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
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

/**
 * Page with minigames; choose minigame for new arena
 * 
 * @author mepeisen
 */
public class ArenaCreateChooseMinigame extends AbstractPage<MinigameInterface>
{
    
    /** the arena */
    private McBiConsumer<ArenaTypeInterface, String> onSave;
    
    /** previous page */
    private ClickGuiPageInterface prev;

    /** 
     * @param onSave
     * @param prev
     */
    public ArenaCreateChooseMinigame(McBiConsumer<ArenaTypeInterface, String> onSave, ClickGuiPageInterface prev)
    {
        this.onSave = onSave;
        this.prev = prev;
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        return MinigamesLibInterface.instance().getMinigameCount();
    }

    @Override
    protected List<MinigameInterface> getElements(int start, int limit)
    {
        final Set<MinigameInterface> result = new TreeSet<>((a, b) -> a.getName().compareTo(b.getName()));
        result.addAll(MinigamesLibInterface.instance().getMinigames(0, Integer.MAX_VALUE));
        return result.
                stream().
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, MinigameInterface elm)
    {
        return Main.itemMinigame(elm, (p, s, g) -> this.onChoose(p, s, g, elm));
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
     * minigame chosen
     * @param player
     * @param session
     * @param gui
     * @param mg
     * @throws McException 
     */
    private void onChoose(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, MinigameInterface mg) throws McException
    {
        session.setNewPage(new ArenaCreateChooseArenaType(mg, this.onSave, this.prev));
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.arena_create_choose_minigame")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (minigames page)
         */
        @LocalizedMessage(defaultMessage = "Minigame for new arena (page %1$d from %2$d)")
        @MessageComment(value = {"Gui title (minigames page)"}, args = {@Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The Cancel
         */
        @LocalizedMessage(defaultMessage = "Cancel creation")
        @MessageComment({"cancel icon"})
        IconCancel,
    }
    
}
