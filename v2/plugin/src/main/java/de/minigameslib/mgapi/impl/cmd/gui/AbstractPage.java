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

import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.PagableClickGuiPage;

/**
 * Abstract base class for pagable elements.
 * 
 * @author mepeisen
 * @param <T> element type
 */
public abstract class AbstractPage<T> extends PagableClickGuiPage<T>
{
    
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
        super(pageNum);
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(super.getItems(), 6);
    }
    
    /**
     * prev page icon
     * @return prev page icon
     */
    public ClickGuiItem itemPrevPage()
    {
        return this.page() > 1 ? Main.itemPrevPage(this::onPrevPage) : null;
    }
    
    /**
     * next page icon
     * @return next page icon
     */
    public ClickGuiItem itemNextPage()
    {
        return this.page() < this.totalPages() ? Main.itemNextPage(this::onNextPage) : null;
    }
    
}
