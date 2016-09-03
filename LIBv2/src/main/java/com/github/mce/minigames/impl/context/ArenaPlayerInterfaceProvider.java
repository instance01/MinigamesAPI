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

package com.github.mce.minigames.impl.context;

import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.context.ContextHandlerInterface;
import com.github.mce.minigames.api.context.MinigameContext;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * Provider to calculate arena players for context.
 * 
 * @author mepeisen
 */
public class ArenaPlayerInterfaceProvider implements ContextHandlerInterface<ArenaPlayerInterface>
{
    
    @Override
    public ArenaPlayerInterface calculateFromCommand(CommandInterface command, MinigameContext context)
    {
        // simply return the command sender
        return command.getPlayer();
    }
    
    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.ContextHandlerInterface#calculateFromEvent(com.github.mce.minigames.api.arena.rules.MinigameEvent, com.github.mce.minigames.api.MinigameContext)
     */
    @Override
    public ArenaPlayerInterface calculateFromEvent(MinigameEvent<?> event, MinigameContext context)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
