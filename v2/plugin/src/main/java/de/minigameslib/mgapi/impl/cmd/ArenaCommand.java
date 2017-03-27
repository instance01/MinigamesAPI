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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.impl.MglibMessages;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * Prints info on arenas.
 * 
 * @author mepeisen
 */
public class ArenaCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandArena);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandArena, command.getCommandPath());
        
        command.checkMinArgCount(1, Mg2Command.Messages.ArenaNameMissing, Messages.Usage);
        final ArenaInterface arena = command.fetch(Mg2Command::getArena).get();
        
        // print Info
        final MinigameInterface minigame = arena.getMinigame();
        final Serializable mgName = minigame == null ? "<invalid>" : arena.getMinigame().getDisplayName(); //$NON-NLS-1$
        command.send(Messages.CommandOutput,
                mgName,
                arena.getDisplayName(),
                arena.getShortDescription(),
                toString(arena.getState()),
                arena.getDescription(),
                arena.getInternalName()
                );
    }

    /**
     * Returns the string message for given arena state
     * @param state
     * @return string message
     */
    public static LocalizedMessageInterface toString(ArenaState state)
    {
        switch (state)
        {
            default:
            case Booting:
                return MglibMessages.ArenaStateBooting;
            case Disabled:
                return MglibMessages.ArenaStateDisabled;
            case Join:
                return MglibMessages.ArenaStateJoin;
            case Starting:
                return MglibMessages.ArenaStateStarting;
            case PreMatch:
                return MglibMessages.ArenaStatePreMatch;
            case Match:
                return MglibMessages.ArenaStateMatch;
            case PostMatch:
                return MglibMessages.ArenaStatePostMatch;
            case Restarting:
                return MglibMessages.ArenaStateRestarting;
            case Maintenance:
                return MglibMessages.ArenaStateMaintenance;
        }
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
    @LocalizedMessages(value = "cmd.mg2_arena")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 arena
         */
        @LocalizedMessage(defaultMessage = "Prints detailed info on arenas.")
        @MessageComment({"Short description of /mg2 arena"})
        ShortDescription,
        
        /**
         * Long description of /mg2 arena
         */
        @LocalizedMessage(defaultMessage = "Prints detailed info on arenas.")
        @MessageComment({"Long description of /mg2 arena"})
        Description,
        
        /**
         * Usage of /mg2 arena
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 arena <name>")
        @MessageComment({"Usage of /mg2 arena"})
        Usage,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessage(defaultMessage = "Missing arena name", severity = MessageSeverityType.Error)
        @MessageComment({"Name argument is missing"})
        NameMissing,
        
        /**
         * Arena was not found
         */
        @LocalizedMessage(defaultMessage = "Arena " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"arena was not found"}, args = @Argument("arena name"))
        ArenaNotFound,
        
        /**
         * The command output of /mg2 arena
         */
        @LocalizedMessageList({
            "minigame: %1$s",
            "arena: %2$s - %3$s",
            "state: %4$s",
            "----------",
            "%5$s",
            "----------",
            "Additional commands:",
            "  " + LocalizedMessage.CODE_COLOR + "/mg2 join %6$s " + LocalizedMessage.INFORMATION_COLOR + " to join the arena.",
            "  " + LocalizedMessage.CODE_COLOR + "/mg2 spectate %6$s " + LocalizedMessage.INFORMATION_COLOR + " to spectate an active match.",
            "  " + LocalizedMessage.CODE_COLOR + "/mg2 manual %6$s " + LocalizedMessage.INFORMATION_COLOR + " to display a manual."
        })
        @MessageComment(value = {
            "The command output of /mg2 arena"
        },args = {
                @Argument("minigame display name"),
                @Argument("arena display name"),
                @Argument("arena short description"),
                @Argument("arena state"),
                @Argument("arena long description"),
                @Argument("arena internal name"),
                })
        CommandOutput,
    }
    
}
