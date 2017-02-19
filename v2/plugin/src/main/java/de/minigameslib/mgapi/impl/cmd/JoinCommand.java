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
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * Let users join an arena.
 * 
 * @author mepeisen
 */
public class JoinCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOnline() && command.checkOpPermission(MglibPerms.CommandJoin);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandJoin, command.getCommandPath());
        command.checkOnline();
        
        command.checkMinArgCount(1, Mg2Command.Messages.ArenaNameMissing, Messages.Usage);
        final ArenaInterface arena = command.fetch(Mg2Command::getArena).get();
        command.checkMaxArgCount(0, CommonMessages.TooManyArguments);
        
        if (arena.isMaintenance())
        {
            command.send(Messages.ArenaUnderMaintenance, arena.getDisplayName());
            return;
        }
        
        // do the join
        arena.join(MinigamesLibInterface.instance().getPlayer(command.getPlayer()));
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return MinigamesLibInterface.instance().getArenas(lastArg, 0, Integer.MAX_VALUE).stream().map(ArenaInterface::getInternalName).collect(Collectors.toList());
        }
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
    @LocalizedMessages(value = "cmd.mg2_join")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 join
         */
        @LocalizedMessage(defaultMessage = "Joins an arena.")
        @MessageComment({"Short description of /mg2 join"})
        ShortDescription,
        
        /**
         * Long description of /mg2 join
         */
        @LocalizedMessage(defaultMessage = "Joins an arena.")
        @MessageComment({"Long description of /mg2 join"})
        Description,
        
        /**
         * Usage of /mg2 join
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 join <name>")
        @MessageComment({"Usage of /mg2 join"})
        Usage,
        
        /**
         * Arena is under maintenance
         */
        @LocalizedMessage(defaultMessage = "Cannot join. Arena is under maintenance.")
        @MessageComment({"Arena is under maintenance"})
        ArenaUnderMaintenance
    }
    
}
