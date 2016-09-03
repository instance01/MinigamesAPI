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

package com.github.mce.minigames.api.context;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.component.ComponentInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.MgRunnable;
import com.github.mce.minigames.api.util.function.MgSupplier;
import com.github.mce.minigames.api.zones.ZoneInterface;

/**
 * The minigame execution context.
 * 
 * <p>
 * The execution context is some kind of thread local session storage. Once a command or event is being processed the context is responsible for providing common minigame objects. For example the
 * default implementation can return the current player being responsible for a command call or event and to return the arena this player is located in.
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
     * Sets a context variable.
     * 
     * @param clazz
     *            the class of the variable
     * @param value
     *            the new value
     */
    <T> void setContext(Class<T> clazz, T value);
    
    /**
     * Resolves a context variable and performs variable substitution.
     * 
     * @param src
     * @return result
     */
    String resolveContextVar(String src);
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws MinigameException
     *             rethrown from runnable.
     */
    void runInNewContext(MgRunnable runnable) throws MinigameException;
    
    /**
     * Runs the code in new context but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws MinigameException
     *             rethrown from runnable.
     */
    void runInCopiedContext(MgRunnable runnable) throws MinigameException;
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws MinigameException
     *             rethrown from runnable.
     */
    <T> T calculateInNewContext(MgSupplier<T> runnable) throws MinigameException;
    
    /**
     * Runs the code but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws MinigameException
     *             rethrown from runnable.
     */
    <T> T calculateInCopiedContext(MgSupplier<T> runnable) throws MinigameException;
    
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
    
    /**
     * Returns the current minigame.
     * 
     * @return current minigame.
     */
    default MinigameInterface getCurrentMinigame()
    {
        return this.getContext(MinigameInterface.class);
    }
    
    /**
     * Returns the current zone.
     * 
     * @return current zone.
     */
    default ZoneInterface getCurrentZone()
    {
        return this.getContext(ZoneInterface.class);
    }
    
    /**
     * Returns the current component.
     * 
     * @return current component.
     */
    default ComponentInterface getCurrentComponent()
    {
        return this.getContext(ComponentInterface.class);
    }
    
}
