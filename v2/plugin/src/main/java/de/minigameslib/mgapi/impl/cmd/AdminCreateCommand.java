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
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminCreateCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandAdminCreate);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdminCreate, command.getCommandPath());

        command.checkMaxArgCount(3, CommonMessages.TooManyArguments);
        
        final String arenaName = command.fetchString(Mg2Command.Messages.ArenaNameMissing, Messages.Usage);
        command.checkMinArgCount(1, Mg2Command.Messages.MinigameNameMissing, Messages.Usage);
        final MinigameInterface minigame = command.fetch(Mg2Command::getMinigame).get();
        command.checkMinArgCount(1, Mg2Command.Messages.TypeNameMissing, Messages.Usage);
        final ArenaTypeInterface type = command.fetch((c, n) -> Mg2Command.getType(c, n, minigame)).get();
        
        final ArenaInterface arena = MinigamesLibInterface.instance().create(arenaName, type);
        command.send(Messages.ArenaCreated, arena.getInternalName());
        // TODO Start creation guide.
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 1)
        {
            return MinigamesLibInterface.instance().getMinigames(lastArg, 0, Integer.MAX_VALUE).stream().map(MinigameInterface::getName).collect(Collectors.toList());
        }
        if (command.getArgs().length == 2)
        {
            final String minigameName = command.getArgs()[1];
            final MinigameInterface minigame = MinigamesLibInterface.instance().getMinigame(minigameName);
            if (minigame != null)
            {
                return minigame.getTypes(lastArg, 0, Integer.MAX_VALUE).stream().map(ArenaTypeInterface::name).collect(Collectors.toList());
            }
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
    @LocalizedMessages(value = "cmd.mg2_admin_create")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin create
         */
        @LocalizedMessage(defaultMessage = "Creates a new arena")
        @MessageComment({"Short description of /mg2 admin create"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin create
         */
        @LocalizedMessage(defaultMessage = "Creates a new arena")
        @MessageComment({"Long description of /mg2 admin create"})
        Description,
        
        /**
         * Usage of /mg2 admin create
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin create <internal-name> <minigame> <type>")
        @MessageComment({"Usage of /mg2 admin create"})
        Usage,
        
        /**
         * Type was not found
         */
        @LocalizedMessage(defaultMessage = "Arena type " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Type was not found"}, args = @Argument("type name"))
        TypeNotFound,
        
        /**
         * Arena created
         */
        @LocalizedMessage(defaultMessage = "Arena " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.SUCCESS_COLOR + " was created.", severity = MessageSeverityType.Success)
        @MessageComment(value = {"Arena created"}, args = @Argument("type name"))
        ArenaCreated,
        
    }
    
}
