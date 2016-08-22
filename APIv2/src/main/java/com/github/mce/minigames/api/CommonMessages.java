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

package com.github.mce.minigames.api;

import com.github.mce.minigames.api.locale.LocalizedMessage;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.LocalizedMessages;
import com.github.mce.minigames.api.locale.MessageSeverityType;

/**
 * Common messages within minigames lib.
 * 
 * @author mepeisen
 */
@LocalizedMessages("messages")
public enum CommonMessages implements LocalizedMessageInterface
{
    
    /**
     * Game was started by command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * <li>String: player name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Arena %0$s was started by player: %1$s.", severity = MessageSeverityType.Success)
    ArenaStartedByCommand,
    
    // ***** commands, usages etc.
    
    /**
     * Mg2 command usage.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "&7Type &9/mg2 help &7for detailed help", severity = MessageSeverityType.Success)
    Mg2CommandUsage,
    
    /**
     * Paged output; header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: info text</li>
     * <li>Integer: current page</li>
     * <li>Integer: total pages</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "&7=====&9%0$s&7====&9Page &1%1$d &9 from &1%2$d&7=====")
    PagedHeader,
    
    /**
     * Paged output; line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: line text</li>
     * <li>Integer: line number</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = " &7")
    PagedLine,
    
    /**
     * Paged output; wrong page number.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>Integer: Page number that was entered by user</li>
     * <li>Integer: Total page count being available</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Page %0$d out of range. Only values from 1 to %1$d allowed.", severity = MessageSeverityType.Error)
    PagedWrongPageNum,
    

    /**
     * Paged output; invalid page number (not numeric).
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "Invalid page number/ number format error.", severity = MessageSeverityType.Error)
    PagedInvalidNumber,
    
    /**
     * Paged output; usage information
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "&7Usage: &9%0$s [page] &7Display the given page.")
    PageUsage,
    
}
