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
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getShortDescription()
     */
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getDescription()
     */
    @Override
    public LocalizedMessageInterface[] getDescription(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getUsage()
     */
    @Override
    public LocalizedMessageInterface getUsage(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)+,
     * 3
     * @see com.github.mce.minigames.api.cmd.AbstractPagableCommandHandler#getLineCount(com.github.mce.minigames.api.cmd.CommandInterface)
     */
    @Override
    protected int getLineCount(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.AbstractPagableCommandHandler#getHeader(com.github.mce.minigames.api.cmd.CommandInterface)
     */
    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.AbstractPagableCommandHandler#getLines(com.github.mce.minigames.api.cmd.CommandInterface, int, int)
     */
    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
