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

import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.cmd.CommandInterface;

/**
 * An interface to calculate context variables.
 * 
 * @author mepeisen
 * @param <T>
 *            context class
 */
public interface ContextHandlerInterface<T>
{
    
    /**
     * Calculates the context object from command.
     * 
     * @param command
     *            command to process
     * @param context
     *            current minigame context
     * @return context object or {@code null} if it cannot be calculated
     */
    T calculateFromCommand(CommandInterface command, MinigameContext context);
    
    /**
     * Calculates the context object from minigame event.
     * 
     * @param event
     *            event to process
     * @param context
     *            current minigame context
     * @return context object or {@code null} if it cannot be calculated
     */
    T calculateFromEvent(MinigameEvent<?, ?> event, MinigameContext context);
    
}
