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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MinigameException;

/**
 * A handler for enabling sub commands.
 * 
 * @author mepeisen
 */
public abstract class AbstractCompositeCommandHandler implements CommandHandlerInterface
{
    
    /**
     * the configured sub commands (ordered).
     */
    protected Map<String, SubCommandHandlerInterface> subCommands = new TreeMap<>();
    
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
            sendUsage(command);
            return;
        }
        
        final String name = command.getArgs()[0].toLowerCase();
        final CommandHandlerInterface handler = this.subCommands.get(name);
        if (handler == null)
        {
            command.send(CommonMessages.CompositeUnknownSubCommand, command.getCommandPath(), command.getArgs()[0]);
            sendUsage(command);
            return;
        }
        
        handler.handle(command.consumeArgs(1));
    }
    
    /**
     * Injects a new sub command.
     * 
     * @param name
     *            sub command name
     * @param handler
     *            handler
     * @return {@code true} if the sub command was added, {@code false} if it already exists.
     */
    public boolean injectSubCommand(String name, SubCommandHandlerInterface handler)
    {
        if (this.subCommands.containsKey(name.toLowerCase()))
        {
            // TODO logging
            return false;
        }
        this.subCommands.put(name.toLowerCase(), handler);
        return true;
    }
    
    /**
     * Sends usage information.
     * 
     * @param command
     *            the command to be used.
     */
    protected abstract void sendUsage(CommandInterface command);

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws MinigameException
    {
        if (command.getArgs().length > 0)
        {
            final String name = command.getArgs()[0].toLowerCase();
            final CommandHandlerInterface handler = this.subCommands.get(name);
            if (handler == null)
            {
                return Collections.emptyList();
            }
            return handler.onTabComplete(command.consumeArgs(1), lastArg);
        }
        return new ArrayList<>(this.subCommands.keySet()).stream().filter(elm -> elm.startsWith(lastArg)).collect(Collectors.toList());
    }

    /**
     * Returns the sub command by name.
     * @param key name of the sub command
     * @return the sub command.
     */
    public SubCommandHandlerInterface getSubCommand(String key)
    {
        return this.subCommands.get(key);
    }
    
}
