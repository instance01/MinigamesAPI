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

/**
 * An interface to build a smart gui.
 * 
 * @author mepeisen
 */
public interface ClickGuiInterface
{
    
    /**
     * Returns an internal unique id that is used to identify this gui.
     * 
     * @return internal id to identify this gui.
     */
    ClickGuiId getUniqueId();
    
    /**
     * Returns the initial page for the gui.
     * 
     * @return initial page.
     */
    ClickGuiPageInterface getInitialPage();
    
    /**
     * Returns the line count of this gui.
     * @return line count; must be a value between 1 and (TODO)
     */
    int getLineCount();
    
}
