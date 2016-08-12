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

import java.io.Serializable;

import com.github.mce.minigames.api.MinigameException;
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
    Serializable getDisplayName();
    
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
    
    /**
     * Starts the arena (forces the start if needed).
     */
    void start();
    
}
