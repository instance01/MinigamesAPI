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

package com.github.mce.minigames.api.gui;

import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

/**
 * A single gui page.
 * 
 * @author mepeisen
 *
 */
public interface ClickGuiPageInterface
{
    
    /**
     * Returns the name of the inventory.
     * 
     * @return inventory name.
     */
    LocalizedMessageInterface getPageName();
    
    /**
     * Returns the click items.
     * @return click items; first array dimension is the line; second the column.
     */
    ClickGuiItem[][] getItems();
    
}
