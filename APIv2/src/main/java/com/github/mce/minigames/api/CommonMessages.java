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
    
}
