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

package de.minigameslib.mgapi.api.arena;

import java.util.Collection;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;

/**
 * Basic interface for arenas.
 * 
 * @author mepeisen
 */
public interface ArenaInterface
{
    
    /**
     * Returns the arena name.
     * 
     * @return internal arena name
     */
    String getInternalName();
    
    /**
     * Returns the arena type.
     * 
     * @return arena type.
     */
    ArenaTypeInterface getType();
    
    /**
     * Returns the display name of this arena
     * @return display name
     */
    LocalizedConfigString getDisplayName();
    
    /**
     * Returns a one line short description
     * @return short arena description
     */
    LocalizedConfigString getShortDescription();
    
    /**
     * Returns a multi-line description
     * @return description of the arena
     */
    LocalizedConfigLine getDescription();
    
    /**
     * Returns the how-to-play manual
     * @return arena manual
     */
    LocalizedConfigLine getManual();
    
    /**
     * Saves arena core data.
     * @throws McException thrown if arena is in wrong state or saving data failed.
     */
    void saveData() throws McException;
    
    /**
     * Returns the current arena state.
     * @return arena state.
     */
    ArenaState getState();
    
    /**
     * Let the given player join the waiting lobby.
     * @param player
     * @throws McException thrown if the arena is in any other state except {@link ArenaState#Join}
     */
    void join(ArenaPlayerInterface player) throws McException;
    
    /**
     * Let the given player join the spectation area.
     * @param player
     * @throws McException thrown if the arena does not run games or is not in state {@link ArenaState#Join}
     */
    void spectate(ArenaPlayerInterface player) throws McException;
    
    /**
     * Sets enabled state; only allowed if arena is currently in states {@link ArenaState#Disabled} or {@link ArenaState#Maintenance}.
     * @throws McException thrown if arena is in invalid state
     */
    void setEnabledState() throws McException;

    /**
     * Sets arena to disabled state; only allowed in states {@link ArenaState#Starting}, {@link ArenaState#Join} or {@link ArenaState#Maintenance}.
     * If used in other situations the argument {@code force} must be set to {@code true}.
     * @param force forces arena disable; if a match is running all players are kicked and the match is aborted.
     * @throws McException thrown if arena is in invalid state or if config cannot be saved.
     */
    void setDisabledState(boolean force) throws McException;
    
    /**
     * Sets arena to maintenance state; only allowed in states {@link ArenaState#Starting}, {@link ArenaState#Join} or {@link ArenaState#Disabled}.
     * If there is a running match the arena will be switching to disabled mode as soon as the match ended.
     * @param force forces arena maintenance mode; if a match is running all players are kicked and the match is aborted.
     * @throws McException thrown if arena is in invalid state or if config cannot be saved.
     */
    void setMaintenance(boolean force) throws McException;
    
    /**
     * Checks if arena is in maintenance mode or is going to maintenance mode as soon as possible.
     * @return {@code true} if setMaintenance was called before or the arena is still in maintenance mode.
     */
    boolean isMaintenance();
    
    /**
     * Checks for administration failures.
     * @return list of arena failures.
     */
    Collection<CheckFailure> check();
    
}
