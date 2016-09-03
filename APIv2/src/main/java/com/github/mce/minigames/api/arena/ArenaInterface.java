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

package com.github.mce.minigames.api.arena;

import java.util.logging.Logger;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;

/**
 * A arena inside the gaming world.
 * 
 * @author mepeisen
 */
public interface ArenaInterface
{
    
    /**
     * List of illegal arena names; names starting with on of these strings are disallowed.
     */
    String[] ILLEGAL_NAMES = {"core", "join", "leave", "mg"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    
    // arena information
    
    /**
     * Returns the internal name of this arena.
     * 
     * @return internal name.
     */
    String getInternalName();
    
    /**
     * Returns the display name of this arena.
     * 
     * @return arena display name; defaults to internal name.
     */
    String getDisplayName();
    
    /**
     * Returns the author of this arena.
     * @return author or {@code null} if no author was set.
     */
    String getAuthor();
    
    /**
     * Returns a short description (single line) of the arena.
     * @return short description or {@code null} if no short description was set.
     */
    LocalizedMessageInterface getShortDescription();
    
    /**
     * Returns a description (multi line) of the arena.
     * @return multi line description or {@code null} if no description was set.
     */
    LocalizedMessageInterface getDescription();
    
    /**
     * Changes the display name
     * 
     * @param name
     * @throws MinigameException
     *             thrown if save failed.
     */
    void setDisplayName(String name) throws MinigameException;
    
    // states
    
    /**
     * Starts the arena (forces the start if needed).
     */
    void start();
    
    // common methods
    
    /**
     * Returns a logger for the library.
     * 
     * @return logger instance.
     */
    Logger getLogger();
    
    /**
     * Returns the declaring minigame
     * 
     * @return minigame
     */
    MinigameInterface getMinigame();
    
    /**
     * Returns the arena state.
     * 
     * <p>
     * WARNING: You should not use this method to query the arena state. Instead either use the predicates or use the {@link #getRealState()} method.
     * </p>
     * 
     * @return arena state
     */
    ArenaState getState();
    
    /**
     * Returns the real arena state.
     * 
     * <p>
     * While {@link #getState()} will return the public visible state this method returns the real state for gaming rules etc. While the arena state being MAINTENANCE the administrator is still able
     * to start a single match to test the arena. Thus the real state may be {@link ArenaState#InGame} during tests while the arena state still shows up {@link ArenaState#Maintenance}.
     * </p>
     * 
     * @return arena state
     */
    ArenaState getRealState();
    
    /**
     * Returns the arena type for this arena.
     * 
     * @return arena type.
     */
    ArenaTypeInterface getArenaType();
    
    /**
     * Checks if the arena is enabled.
     * 
     * @return {@code true} if the arena is enabled.
     */
    boolean isEnabled();

    /**
     * Checks if the arena is in maintenance mode.
     * 
     * @return {@code true} if the arena is under maintenance.
     */
    boolean isMaintenance();
    
    /**
     * Checks if the arena can be started by command; checks the gaming rules.
     * 
     * @return {@code true} if the arena can be started.
     */
    boolean canStart();
    
    void delete() throws MinigameException;
    
    // stubbing
    
    /**
     * Checks this arena for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the arena matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws MinigameException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    MgOutgoingStubbing<ArenaInterface> when(MgPredicate<ArenaInterface> test) throws MinigameException;
    
}
