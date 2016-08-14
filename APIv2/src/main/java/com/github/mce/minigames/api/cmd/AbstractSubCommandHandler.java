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

package com.github.mce.minigames.api.cmd;

import static com.github.mce.minigames.api.cmd.CommandInterface.isPlayer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;

/**
 * A handler for enabling sub commands.
 * 
 * @author mepeisen
 */
public abstract class AbstractSubCommandHandler implements CommandHandlerInterface
{
    
    /**
     * the configured sub commands.
     */
    protected Map<String, CommandHandlerInterface> subCommands = new HashMap<>();
    
    /**
     * pre parse the command.
     * 
     * @param command
     *            command
     * @return {@code true} if the execution can proceed.
     * @throws MinigameException
     *             thrown if there are problems.
     */
    protected boolean pre(CommandInterface command) throws MinigameException
    {
        // only in-game
        command.when(isPlayer().negate()).thenThrow(CommonErrors.InvokeIngame);
        
        return true;
    }
    
    @Override
    public void handle(CommandInterface command) throws MinigameException
    {
        if (!pre(command))
        {
            return;
        }
        
        // check for sub command
        if (command.getArgs().length == 0)
        {
            sendUsage(command.getPlayer());
            return;
        }
        
        final String name = command.getArgs()[0].toLowerCase();
        final CommandHandlerInterface handler = this.subCommands.get(name);
        if (handler == null)
        {
            sendUsage(command.getPlayer());
            return;
        }
        
        handler.handle(new CommandInterface() {
            
            @Override
            public MgOutgoingStubbing<CommandInterface> when(MgPredicate<CommandInterface> test) throws MinigameException
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public CommandSender getSender()
            {
                return command.getSender();
            }
            
            @Override
            public ArenaPlayerInterface getPlayer()
            {
                return command.getPlayer();
            }
            
            @Override
            public String getLabel()
            {
                return command.getLabel();
            }
            
            @Override
            public Command getCommand()
            {
                return command.getCommand();
            }
            
            @Override
            public String[] getArgs()
            {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }
    
    /**
     * Sends usage information.
     * 
     * @param player
     *            the player to send the usage strings to
     */
    protected abstract void sendUsage(ArenaPlayerInterface player);
    
}
