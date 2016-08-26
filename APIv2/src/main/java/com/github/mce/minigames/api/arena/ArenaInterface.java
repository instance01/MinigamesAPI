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

import java.util.Locale;
import java.util.logging.Logger;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;

/**
 * A arena inside the gaming world.
 * 
 * @author mepeisen
 */
public interface ArenaInterface
{
    
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
     * Returns the display name for given locale.
     * 
     * @param locale
     * @return arena display name; defaults to {@link #getDisplayName()}
     */
    String getDisplayName(Locale locale);
    
    /**
     * Changes the display name
     * 
     * @param name
     * @param locale
     *            the locale to be used or {@code null} to change the default display name.
     * @throws MinigameException
     *             thrown if save failed.
     */
    void setDisplayName(String name, Locale locale) throws MinigameException;
    
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
     * @return minigame
     */
    MinigameInterface getMinigame();

    /**
     * Returns the arena state.
     * 
     * <p>
     * WARNING: You should not use this method to query the arena state.
     * Instead either use the predicates or use the {@link #getRealState()} method.
     * </p>
     * 
     * @return arena state
     */
    ArenaState getState();

    /**
     * Returns the real arena state.
     * 
     * <p>
     * While {@link #getState()} will return the public visible state this method
     * returns the real state for gaming rules etc. While the arena state being MAINTENANCE
     * the administrator is still able to start a single match to test the arena.
     * Thus the real state may be {@link ArenaState#InGame} during tests while
     * the arena state still shows up {@link ArenaState#Maintenance}.
     * </p>
     * 
     * @return arena state
     */
    ArenaState getRealState();
    
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
    
    /**
     * Returns a test function to check if the arena can be started by command; checks the gaming rules.
     * 
     * @return predicate to return {@code true} if the arena can be started.
     */
    MgPredicate<ArenaInterface> canStart();
    
}
