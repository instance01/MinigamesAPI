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
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminInviteCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandAdminInvite);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdminInvite, command.getCommandPath());

        command.checkMinArgCount(1, Mg2Command.Messages.PlayerNameMissing, Messages.Usage);
        final ArenaPlayerInterface player = command.fetch(Mg2Command::getPlayer).get();
        
        final ArenaInterface arena = Mg2Command.getArenaFromPlayer(command, Messages.Usage);
        
        arena.join(player);
        command.send(Messages.PlayerInvited, arena.getInternalName());
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return null; // player list
        }
        if (command.getArgs().length == 1)
        {
            return MinigamesLibInterface.instance().getArenas(lastArg, 0, Integer.MAX_VALUE).stream()
                    .filter(a -> a.getState() == ArenaState.Join && a.isMaintenance())
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
    @LocalizedMessages(value = "cmd.mg2_admin_invite")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin invite
         */
        @LocalizedMessage(defaultMessage = "invites an existing arena")
        @MessageComment({"Short description of /mg2 admin invite"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin invite
         */
        @LocalizedMessage(defaultMessage = "invites an existing arena")
        @MessageComment({"Long description of /mg2 admin invite"})
        Description,
        
        /**
         * Usage of /mg2 admin invite
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin invite <player> <internal-name>")
        @MessageComment({"Usage of /mg2 admin invite"})
        Usage,
        
        /**
         * Arena invite succeded
         */
        @LocalizedMessage(defaultMessage = "Player " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.GREEN + " was invited to arena " + LocalizedMessage.CODE_COLOR + "%2$s " + LocalizedMessage.SUCCESS_COLOR + ".", severity = MessageSeverityType.Success)
        @MessageComment(value = {"Arena invite succeeded"}, args = {@Argument("player name"), @Argument("arena name")})
        PlayerInvited,
        
    }
    
}
