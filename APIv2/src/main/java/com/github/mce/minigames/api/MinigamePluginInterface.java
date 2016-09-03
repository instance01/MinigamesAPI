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

import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.context.ContextHandlerInterface;
import com.github.mce.minigames.api.context.ContextResolverInterface;

/**
 * The minigame plugin interface; administrational backend for the given minigame.
 * 
 * <p>
 * This interface is returned upon minigame initialization.
 * </p>
 * 
 * @author mepeisen
 */
public interface MinigamePluginInterface extends MinigameInterface
{
    
    // initialization.
    
    /**
     * Creates a new arena type.
     * 
     * @param name
     *            internal name of the arena type.
     * @param type
     *            arena type.
     * @param isDefault
     *            {@code true} if this is the default arena type for this minigame.
     * @return the type builder.
     * @throws MinigameException
     *             thrown if the arena type is invalid or if the name is already taken or if you try to create two default arena types..
     */
    ArenaTypeBuilderInterface createArenaType(String name, ArenaTypeInterface type, boolean isDefault) throws MinigameException;
    
    /**
     * Registers a context handler to calculate context variables.
     * 
     * @param clazz
     *            context class.
     * @param handler
     *            the context handler.
     * @throws MinigameException
     *             thrown if the class to register is already registered.
     * @param <T>
     *            context class to register
     */
    <T> void registerContextHandler(Class<T> clazz, ContextHandlerInterface<T> handler) throws MinigameException;
    
    /**
     * Registers a helper to resolve context variables.
     * 
     * @param resolver
     * @throws MinigameException
     */
    void registerContextResolver(ContextResolverInterface resolver) throws MinigameException;
    
    /**
     * Initialize this minigame.
     * 
     * <p>
     * This method must be called at the end of the initialization process.
     * </p>
     * 
     * @throws MinigameException
     *             thrown if the minigame declarations are not valid.
     */
    void init() throws MinigameException;
    
    /**
     * Will be called from plugin as soon as the plugin is disabled.
     * 
     * @throws MinigameException
     *             thrown if there are problems disabling the plugin.
     */
    void disable() throws MinigameException;
    
}
