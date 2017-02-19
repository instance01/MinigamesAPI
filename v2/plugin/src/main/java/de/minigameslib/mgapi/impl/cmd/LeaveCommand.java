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

import java.util.Collections;
import java.util.List;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class LeaveCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOnline() && command.checkOpPermission(MglibPerms.CommandLeave);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandLeave, command.getCommandPath());
        command.checkOnline();

        command.checkMaxArgCount(0, CommonMessages.TooManyArguments);
        
        final ArenaPlayerInterface player = MinigamesLibInterface.instance().getPlayer(command.getPlayer());
        if (player.inArena())
        {
            player.getArena().leave(player);
        }
        else
        {
            throw new McException(Messages.NotWithinArena);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        return Collections.emptyList();
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
    @LocalizedMessages(value = "cmd.mg2_leave")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 leave
         */
        @LocalizedMessage(defaultMessage = "Leave current arena.")
        @MessageComment({"Short description of /mg2 leave"})
        ShortDescription,
        
        /**
         * Long description of /mg2 leave
         */
        @LocalizedMessage(defaultMessage = "Leave current arena.")
        @MessageComment({"Long description of /mg2 leave"})
        Description,
        
        /**
         * Usage of /mg2 leave
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 leave")
        @MessageComment({"Usage of /mg2 leave"})
        Usage,
        
        /**
         * You are not inside an arena
         */
        @LocalizedMessage(defaultMessage = "You are not inside any arena", severity = MessageSeverityType.Error)
        @MessageComment({"You are not inside an arena"})
        NotWithinArena,
    }
    
}
