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

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

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

//    @Override
//    public LocalizedMessageInterface getUsage(CommandInterface command)
//    {
//        if (this.compositeCommand != null)
//        {
//            return CommonMessages.HelpPagedUsage;
//        }
//        return CommonMessages.HelpCommandUsage;
//    }

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
                result.add(CommonMessages.HelpLineUsage.toArg(key, this.compositeCommand.subCommands.get(key).getShortDescription(command).toArg(command.getCommandPath())));
            }
            return result.toArray(new Serializable[result.size()]);
        }
        return this.subCommand.getDescription(command).toListArg(start, count, new Serializable[]{command.getCommandPath()}).apply(command.getLocale(), command.isOp());
    }

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws MinigameException
    {
        return Collections.emptyList();
    }
    
}
