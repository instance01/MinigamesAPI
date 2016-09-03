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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface.DynamicArg;

/**
 * Prints help based on single sub command handlers.
 * 
 * @author mepeisen
 *
 */
public class HelpCommandHandler extends AbstractPagableCommandHandler implements SubCommandHandlerInterface
{
    
    /** help on sub command. */
    private SubCommandHandlerInterface subCommand;
    
    /** help on composite command. */
    private AbstractCompositeCommandHandler compositeCommand;
    
    @Override
    public void handle(CommandInterface command) throws MinigameException
    {
        if (command.getArgs().length >= 1 && this.compositeCommand != null)
        {
            try
            {
                Integer.parseInt(command.getArgs()[0]);
            }
            catch (@SuppressWarnings("unused") NumberFormatException ex)
            {
                // assume we have a command name given to us.
                final String name = command.getArgs()[0].toLowerCase();
                final SubCommandHandlerInterface sub = this.compositeCommand.subCommands.get(name);
                if (sub == null)
                {
                    command.send(CommonMessages.HelpUnknownSubCommand, command.getCommandPath(), name);
                    return;
                }
                new HelpCommandHandler(sub).handle(command.consumeArgs(1));
            }
        }
        super.handle(command);
        return;
    }

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws MinigameException
    {
        if (command.getArgs().length > 0 || this.compositeCommand == null)
        {
            return Collections.emptyList();
        }
        return new ArrayList<>(this.compositeCommand.subCommands.keySet()).stream().filter(elm -> elm.startsWith(lastArg)).collect(Collectors.toList());
    }

    /**
     * Constructor.
     * 
     * @param command
     *            the underlying command for the help.
     */
    public HelpCommandHandler(SubCommandHandlerInterface command)
    {
        this.subCommand = command;
    }
    
    /**
     * Constructor.
     * 
     * @param command
     *            the underlying command for the help.
     */
    public HelpCommandHandler(AbstractCompositeCommandHandler command)
    {
        this.compositeCommand = command;
    }
    
   @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return CommonMessages.HelpShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return CommonMessages.HelpLongDescription;
    }

    @Override
    protected int getLineCount(CommandInterface command)
    {
        if (this.compositeCommand != null)
        {
            return this.compositeCommand.subCommands.size();
        }
        return this.subCommand.getDescription(command).toListArg(command.getCommandPath()).apply(command.getLocale(), command.isOp()).length;
    }

    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return CommonMessages.HelpHeader.toArg(command.getCommandPath());
    }

    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        if (this.compositeCommand != null)
        {
            final List<String> keys = new ArrayList<>(this.compositeCommand.subCommands.keySet());
            final List<Serializable> result = new ArrayList<>();
            for (final String key : keys.subList(start, start + count))
            {
                DynamicArg shortDesc = null;
                final SubCommandHandlerInterface sch = this.compositeCommand.subCommands.get(key);
                try
                {
                    shortDesc = sch.getShortDescription(command).toArg(command.getCommandPath());
                }
                catch (Throwable t)
                {
                    MglibInterface.INSTANCE.get().getLogger().log(Level.WARNING, "Exception fetching short description on command " + sch, t); //$NON-NLS-1$
                }
                result.add(CommonMessages.HelpLineUsage.toArg(key, shortDesc));
            }
            return result.toArray(new Serializable[result.size()]);
        }
        return this.subCommand.getDescription(command).toListArg(start, count, new Serializable[]{command.getCommandPath()}).apply(command.getLocale(), command.isOp());
    }
    
}
