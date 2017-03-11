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
import de.minigameslib.mclib.api.cmd.LocalizedPagableCommand;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * Prints info on arenas.
 * 
 * @author mepeisen
 */
public class ManualCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOp() || command.isPlayer() && command.getPlayer().checkPermission(MglibPerms.CommandManual);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandManual, command.getCommandPath());
        
        command.checkMinArgCount(1, Mg2Command.Messages.ArenaNameMissing, Messages.Usage);
        final ArenaInterface arena = command.fetch(Mg2Command::getArena).get();
        command.checkMaxArgCount(0, CommonMessages.TooManyArguments);
        
        // print manual
        new LocalizedPagableCommand(
                arena.getManual() == null ? arena.getMinigame().getHowToPlay() : arena.getManual(),
                Messages.ManualPagedHeader.toArg(arena.getDisplayName())
                ).handle(command.consumeArgs(1));
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return MinigamesLibInterface.instance().getArenas(lastArg, 0, Integer.MAX_VALUE).stream()
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
    @LocalizedMessages(value = "cmd.mg2_manual")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 manual
         */
        @LocalizedMessage(defaultMessage = "Prints how-to-play manual on arenas.")
        @MessageComment({"Short description of /mg2 manual"})
        ShortDescription,
        
        /**
         * Long description of /mg2 manual
         */
        @LocalizedMessage(defaultMessage = "Prints how-to-play manual on arenas.")
        @MessageComment({"Long description of /mg2 manual"})
        Description,
        
        /**
         * Usage of /mg2 manual
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 manual <name>")
        @MessageComment({"Usage of /mg2 manual"})
        Usage,
        
        /**
         * Header line for /mg2 manual ...
         */
        @LocalizedMessageList({
            "How-to-play for arena %1$s."
        })
        @MessageComment(value = {
            "Header line for /mg2 manual ..."
        },args = {
                @Argument("arena display name"),
                })
        ManualPagedHeader,
        
    }
    
}
