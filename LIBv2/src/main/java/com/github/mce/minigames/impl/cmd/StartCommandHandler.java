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

package com.github.mce.minigames.impl.cmd;

import org.bukkit.entity.Player;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.cmd.CommandHandlerInterface;
import com.github.mce.minigames.api.cmd.CommandInterface;

/**
 * A handler for the /start command.
 * 
 * @author mepeisen
 */
public class StartCommandHandler implements CommandHandlerInterface
{
    
    @Override
    public void handle(CommandInterface command) throws MinigameException
    {
        // only in-game
        command.when(command.isPlayer().negate()).thenThrow(CommonErrors.InvokeIngame);
        
        // we are a player
        final Player player = (Player) command.getSender();
        
    }
    
}
