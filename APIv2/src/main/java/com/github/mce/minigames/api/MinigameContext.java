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

import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * The minigame execution context.
 * 
 * <p>
 * The execution context is some kind of thread local session storage. Once a command or
 * event is being processed the context is responsible for providing common minigame objects.
 * For example the default implementation can return the current player being responsible for
 * a command call or event and to return the arena this player is located in.
 * </p>
 * 
 * @author mepeisen
 */
public interface MinigameContext
{
    
    /**
     * Returns a session variable.
     * 
     * @param clazz
     *            the class of the variable to be returned.
     * @return Context variable or {@code null} if the variable was not set.
     */
    <T> T getContext(Class<T> clazz);
    
    /**
     * Resolves a context variable and performs variable substitution.
     * 
     * @param src
     * @return result
     */
    String resolveContextVar(String src);
    
    /**
     * Returns the current player.
     * 
     * @return current player.
     */
    default ArenaPlayerInterface getCurrentPlayer()
    {
        return this.getContext(ArenaPlayerInterface.class);
    }
    
    /**
     * Returns the current arena.
     * 
     * @return current arena.
     */
    default ArenaInterface getCurrentArena()
    {
        return this.getContext(ArenaInterface.class);
    }
    
}
