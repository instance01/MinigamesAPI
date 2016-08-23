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
import com.github.mce.minigames.api.locale.LocalizedMessageList;
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
    @LocalizedMessage(defaultMessage = "Arena %1$s was started by player: %2$s.", severity = MessageSeverityType.Success)
    ArenaStartedByCommand,
    
    // ***** commands, usages etc.
    
    /**
     * Mg2 command usage.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "§7Type §9/mg2 help §7for detailed help", severity = MessageSeverityType.Success)
    Mg2CommandUsage,
    
    /**
     * Invalid sub command in composite command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * <li>String: current sub command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command §9%1$s %2$s", severity = MessageSeverityType.Error)
    CompositeUnknownSubCommand,
    
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
    @LocalizedMessage(defaultMessage = "§7=====§9%1$s§7====§9Page §1%2$d §9 from §1%2$d§7=====")
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
    @LocalizedMessage(defaultMessage = " §7")
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
    @LocalizedMessage(defaultMessage = "Page %1$d out of range. Only values from 1 to %2$d allowed.", severity = MessageSeverityType.Error)
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
    @LocalizedMessage(defaultMessage = "§7Usage: §9%1$s [page] §7Display the given page.")
    PageUsage,
    
    /**
     * Help command usage.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "§9%1$s help [page] Display help with given page number.")
    HelpPagedUsage,
    
    /**
     * Help command usage.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "§9%1$s help [page] Display the help with given page number.")
    HelpCommandUsage,
    
    /**
     * Help command short description.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "§7Display command help")
    HelpShortDescription,
    
    /**
     * Help command long description.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * <li>String: The usage of this command</li>
     * </ol>
     */
    @LocalizedMessageList({
        @LocalizedMessage(defaultMessage = "§7Displays command help for command §9%1$s"),
        @LocalizedMessage(defaultMessage = "§7Usage: §9%1$s [page] §7Display the given help page."),
        @LocalizedMessage(defaultMessage = "§7The argument §9page is optional. If not entered it"),
        @LocalizedMessage(defaultMessage = "§7will always display the first help page."),
    })
    HelpLongDescription,
    
}
