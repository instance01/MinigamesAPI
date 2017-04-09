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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Abstract base class for pagable elements.
 * 
 * @author mepeisen
 * @param <T> element type
 */
public abstract class AbstractPage<T> implements ClickGuiPageInterface
{
    
    /** number of items per page */
    protected static final int ITEMS_PER_LINE = Main.COL_COUNT;
    
    /** number of items per page */
    protected static final int ITEMS_PER_PAGE = ITEMS_PER_LINE * 4;
    
    /** numeric page num */
    private int pageNum = 1;
    
    /**
     * Constructor to create the first page
     */
    public AbstractPage()
    {
        // empty
    }
    
    /**
     * Constructor to create given page
     * @param pageNum
     */
    public AbstractPage(int pageNum)
    {
        this.pageNum = pageNum;
    }
    
    /**
     * Returns the number of elements
     * @return count of elements
     */
    protected abstract int count();
    
    /**
     * Returns total number of pages.
     * @return total page number.
     */
    protected int totalPages()
    {
        return (int) Math.ceil(this.count() / ITEMS_PER_PAGE);
    }
    
    /**
     * Returns current page
     * @return current page.
     */
    protected int page()
    {
        return this.pageNum;
    }
    
    /**
     * Returns the elements for this page
     * @param start start index
     * @param limit maximum limit
     * @return list of elements to be displayed 
     */
    protected abstract List<T> getElements(int start, int limit);
    
    /**
     * Maps elements to click gui item
     * @param line 
     * @param col 
     * @param index 
     * @param elm
     * @return click gui item
     */
    protected abstract ClickGuiItem map(int line, int col, int index, T elm);

    @Override
    public ClickGuiItem[][] getItems()
    {
        final List<T> list = this.getElements((this.pageNum - 1) * ITEMS_PER_PAGE, ITEMS_PER_PAGE);
        return Main.withFillers(new ClickGuiItem[][]{
            firstLine(),
            null,
            // arenas
            itemsLine(list, 0),
            itemsLine(list, ITEMS_PER_LINE),
            itemsLine(list, ITEMS_PER_LINE * 2),
            itemsLine(list, ITEMS_PER_LINE * 3),
        });
    }
    
    /**
     * Returns the first line
     * @return first line
     */
    protected abstract ClickGuiItem[] firstLine();

    /**
     * @param items
     * @param start
     * @return line of icons
     */
    private ClickGuiItem[] itemsLine(Collection<T> items, int start)
    {
        int col = 0;
        int i = start + (this.pageNum - 1) * ITEMS_PER_PAGE;
        final List<ClickGuiItem> result = new ArrayList<>();
        final Iterator<T> iter = items.stream().skip(start).limit(ITEMS_PER_LINE).iterator();
        while (iter.hasNext())
        {
            result.add(this.map(start / ITEMS_PER_LINE, col, i, iter.next()));
            col++;
            i++;
        }
        return result.toArray(new ClickGuiItem[result.size()]);
    }
    
    /**
     * refresh gui
     * @param player
     * @param session
     * @param gui
     */
    protected void onRefresh(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this);
    }

    /**
     * prev page
     * @param player
     * @param session
     * @param gui
     */
    protected void onPrevPage(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (this.pageNum > 1)
        {
            this.pageNum--;
            session.setNewPage(this);
        }
    }
    
    /**
     * next page
     * @param player
     * @param session
     * @param gui
     */
    protected void onNextPage(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (this.pageNum < this.totalPages())
        {
            this.pageNum++;
            session.setNewPage(this);
        }
    }
    
}
