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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminCheckCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandAdminCheck);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdminCheck, command.getCommandPath());
        
        final ArenaInterface arena = Mg2Command.getArenaFromPlayer(command, Messages.Usage);
        
        // TODO maybe async?
        final Collection<CheckFailure> checks = arena.check();
        command.send(Messages.CheckHeader, arena.getDisplayName());
        for (final CheckFailure failure : checks)
        {
            switch (failure.getSeverity())
            {
                default:
                case Error:
                    command.send(Messages.ArenaError, arena.getDisplayName(), failure.getTitle());
                    break;
                case Info:
                    command.send(Messages.ArenaInfo, arena.getDisplayName(), failure.getTitle());
                    break;
                case Warning:
                    command.send(Messages.ArenaWarning, arena.getDisplayName(), failure.getTitle());
                    break;
            }
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return MinigamesLibInterface.instance().getArenas(lastArg, 0, Integer.MAX_VALUE).stream()
                    .filter(a -> a.getState() == ArenaState.Maintenance || a.getState() == ArenaState.Disabled)
                    .map(ArenaInterface::getInternalName)
                    .filter(a -> a.toLowerCase().startsWith(lastArg))
                    .collect(Collectors.toList());
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
    @LocalizedMessages(value = "cmd.mg2_admin_check")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin check
         */
        @LocalizedMessage(defaultMessage = "checks an existing arena")
        @MessageComment({"Short description of /mg2 admin check"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin check
         */
        @LocalizedMessage(defaultMessage = "checks an existing arena")
        @MessageComment({"Long description of /mg2 admin check"})
        Description,
        
        /**
         * Usage of /mg2 admin check
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin check <internal-name>")
        @MessageComment({"Usage of /mg2 admin check"})
        Usage,
        
        /**
         * Arena check result
         */
        @LocalizedMessage(defaultMessage = "check results (arena " + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.INFORMATION_COLOR + ")", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Arena check result"}, args = {@Argument("arena display name")})
        CheckHeader,
        
        /**
         * Arena check error line
         */
        @LocalizedMessage(defaultMessage = "[" + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.ERROR_COLOR + "] %2$s", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Arena check error line"}, args = {@Argument("arena display name"), @Argument("title text")})
        ArenaError,
        
        /**
         * Arena check warning line
         */
        @LocalizedMessage(defaultMessage = "[" + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.WARNING_COLOR + "] %2$s", severity = MessageSeverityType.Warning)
        @MessageComment(value = {"Arena check warning line"}, args = {@Argument("arena display name"), @Argument("title text")})
        ArenaWarning,
        
        /**
         * Arena check info line
         */
        @LocalizedMessage(defaultMessage = "[" + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.INFORMATION_COLOR + "] %2$s", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Arena check info line"}, args = {@Argument("arena display name"), @Argument("title text")})
        ArenaInfo,
        
    }
    
}
