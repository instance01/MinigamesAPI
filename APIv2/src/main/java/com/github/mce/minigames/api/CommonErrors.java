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
@LocalizedMessages("errors")
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
    @LocalizedMessage(defaultMessage = "Unable to create minigame %1$s because of wrong MinigamesLib plugin state: %2$s.", severity = MessageSeverityType.Error)
    Cannot_Create_Game_Wrong_State,
    
    /**
     * Extension cannot be initialized because minigames-lib has wrong state.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: extension name</li>
     * <li>String: current plugin lib state</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create extension %1$s because of wrong MinigamesLib plugin state: %2$s.", severity = MessageSeverityType.Error)
    Cannot_Create_Extension_Wrong_State,
    
    /**
     * Game cannot be initialized because of internal errors.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create minigame %1$s because of internal errors, see previous log output.", severity = MessageSeverityType.Error)
    MinigameRegistrationError,
    
    /**
     * Extension cannot be initialized because of internal errors.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: extension name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create extension %1$s because of internal errors, see previous log output.", severity = MessageSeverityType.Error)
    ExtensionRegistrationError,
    
    /**
     * Game cannot be initialized because it already exists.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create minigame %1$s because it already exists.", severity = MessageSeverityType.Error)
    DuplicateMinigame,
    
    /**
     * Extension cannot be initialized because it already exists.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: extension name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unable to create extension %1$s because it already exists.", severity = MessageSeverityType.Error)
    DuplicateExtension,
    
    /**
     * Command must be executed in game.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "Invoke this command in-game.", severity = MessageSeverityType.Error)
    InvokeIngame,
    
    /**
     * No permissions for start command.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "No permission for /start command.", severity = MessageSeverityType.Error)
    NoPermissionForStart,
    
    /**
     * Start command outside arena.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "You are not inside an arena.", severity = MessageSeverityType.Error)
    StartNotWithinArena,
    
    /**
     * Arena cannot be started.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "Arena cannot be started directly.", severity = MessageSeverityType.Error)
    CannotStart,
    
    /**
     * The party command is disabled.
     * 
     * <p>No Arguments</p>
     */
    @LocalizedMessage(defaultMessage = "Party command disabled.", severity = MessageSeverityType.Error)
    PartyCommandDisabled,
    
}
