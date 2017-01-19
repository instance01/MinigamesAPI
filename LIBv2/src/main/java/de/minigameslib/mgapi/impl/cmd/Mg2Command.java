/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.cmd;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;

/**
 * The mg2 main command
 * 
 * @author mepeisen
 */
public class Mg2Command extends AbstractCompositeCommandHandler
{
    
    /**
     * Constructor to register the sub commands
     */
    public Mg2Command()
    {
        this.subCommands.put("help", new HelpCommandHandler(this)); //$NON-NLS-1$
        
        this.subCommands.put("info", new InfoCommand()); //$NON-NLS-1$
        this.subCommands.put("arenas", new ArenasCommand()); //$NON-NLS-1$
        this.subCommands.put("arena", new ArenaCommand()); //$NON-NLS-1$
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws McException
    {
        // allowed from everywhere
        return true;
    }

    @Override
    protected void sendUsage(CommandInterface cmd)
    {
        cmd.send(Messages.Usage);
    }
    
    /**
     * The /mg2 messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Usage for command /mg2
         */
        @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Enter " + LocalizedMessage.BLUE + "/mg2 help" + LocalizedMessage.GRAY + " for detailed help")
        @MessageComment({"Usage for command /mg2"})
        Usage
        
    }
    
}
