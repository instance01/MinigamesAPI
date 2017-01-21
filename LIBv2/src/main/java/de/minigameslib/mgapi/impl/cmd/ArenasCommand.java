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
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * Arena list command
 * 
 * @author mepeisen
 */
public class ArenasCommand extends ArenaListCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandArenas);
    }

    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandArenas, command.getCommandPath());
        super.handle(command);
    }

    /**
     * Constructor
     */
    public ArenasCommand()
    {
        super(
                () -> MinigamesLibInterface.instance().getArenaCount(),
                () -> MinigamesLibInterface.instance().getArenas(0, Integer.MAX_VALUE).stream(),
                Messages.Header);
    }

    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return Messages.ShortDescription;
    }

    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return Messages.Description;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_arenas")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 arenas
         */
        @LocalizedMessage(defaultMessage = "Prints a list of arenas.")
        @MessageComment({"Short description of /mg2 arenas"})
        ShortDescription,
        
        /**
         * Long description of /mg2 arenas
         */
        @LocalizedMessage(defaultMessage = "Prints a list of arenas.")
        @MessageComment({"Long description of /mg2 arenas"})
        Description,
        
        /**
         * Paging header text
         */
        @LocalizedMessage(defaultMessage = "Arenas")
        @MessageComment({"Paging header text"})
        Header,
    }
    
}
