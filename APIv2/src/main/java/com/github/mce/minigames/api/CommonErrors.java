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
import com.github.mce.minigames.api.locale.LocalizedMessages;
import com.github.mce.minigames.api.locale.MessageSeverityType;

/**
 * Common errors within minigames lib.
 * 
 * @author mepeisen
 */
@LocalizedMessages(minigame = "core", path = "errors")
public enum CommonErrors implements MinigameErrorCode
{
    
    /**
     * Game cannot be initialized because minigames-lib has wrong state.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * <li>String: current plugin lib state</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create minigame %0$s because of wrong MinigamesLib plugin state: %1$s.", severity = MessageSeverityType.Error)
    Cannot_Create_Game_Wrong_State,
    
    /**
     * Game cannot be initialized because it already exists.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create minigame %0$s because it already exists.", severity = MessageSeverityType.Error)
    DuplicateMinigame,
    
    /**
     * Command must be executed in game.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "Invoke this command in-game.", severity = MessageSeverityType.Error)
    InvokeIngame,
    
}
