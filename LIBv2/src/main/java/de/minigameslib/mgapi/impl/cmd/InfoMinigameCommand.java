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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;

/**
 * Handle the minigame command info
 * @author mepeisen
 */
public class InfoMinigameCommand implements SubCommandHandlerInterface
{

    @Override
    public void handle(CommandInterface command) throws McException
    {
        if (command.getArgs().length == 0)
        {
            command.send(Messages.NameMissing);
            command.send(Messages.Usage);
            return;
        }
        
        final String name = command.getArgs()[0];
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        final MinigameInterface minigame = mglib.getMinigame(name);
        if (minigame == null)
        {
            command.send(Messages.MinigameNotFound, name);
            return;
        }
        
        if (command.getArgs().length > 2)
        {
            command.send(CommonMessages.TooManyArguments);
            command.send(Messages.Usage);
            return;
        }
        if (command.getArgs().length == 2)
        {
            switch (command.getArgs()[1])
            {
                case "arenas": //$NON-NLS-1$
                    new MinigameArenaListCommand(() -> mglib.getArenaCount(minigame.getPlugin()), () -> mglib.getArenas(minigame.getPlugin(), 0, Integer.MAX_VALUE).stream()).handle(command.consumeArgs(1));
                    break;
                case "manual": //$NON-NLS-1$
                    // new InfoMinigameManualCommand(minigame).handle(command.consumeArgs(1));
                    break;
                default:
                    break;
            }
        }
        
        // print Info
        final String version = minigame.getPlugin().getDescription().getVersion();
        command.send(Messages.CommandOutput,
                minigame.getDisplayName().toArg(),
                minigame.getShortDescription().toArg(),
                version,
                minigame.getDescription().toListArg(),
                command.getCommandPath(),
                name
                );
    }

    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return MinigamesLibInterface.instance().getMinigames(lastArg, 0, Integer.MAX_VALUE).stream().map(MinigameInterface::getName).collect(Collectors.toList());
        }
        if (command.getArgs().length == 1)
        {
            return Arrays.asList("arenas", "manual").stream().filter(p -> p.startsWith(lastArg)).collect(Collectors.toList());  //$NON-NLS-1$//$NON-NLS-2$
        }
        return Collections.emptyList();
    }

    @Override
    public LocalizedMessageInterface getDescription(CommandInterface cmd)
    {
        return Messages.Description;
    }

    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface cmd)
    {
        return Messages.ShortDescription;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_info_minigame")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 info minigame
         */
        @LocalizedMessage(defaultMessage = "Prints details on given minigame.")
        @MessageComment({"Short description of /mg2 info minigame"})
        ShortDescription,
        
        /**
         * Long description of /mg2 info minigame
         */
        @LocalizedMessage(defaultMessage = "Prints details of given minigame.")
        @MessageComment({"Long description of /mg2 info minigame"})
        Description,
        
        /**
         * Usage of /mg2 info minigame
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.BLUE + "/mg2 info minigame <name>")
        @MessageComment({"Usage of /mg2 info minigame"})
        Usage,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessage(defaultMessage = "Missing minigame name", severity = MessageSeverityType.Error)
        @MessageComment({"Name argument is missing"})
        NameMissing,
        
        /**
         * Minigame was not found
         */
        @LocalizedMessage(defaultMessage = "Minigame " + LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.DARK_RED + " not found", severity = MessageSeverityType.Error)
        @MessageComment({"Name argument is missing"})
        MinigameNotFound,
        
        /**
         * The command output of /mg2 info minigame
         */
        @LocalizedMessageList({
            "minigame %1$s (%2$s)",
            "version: %3$s",
            "----------",
            "%4$s",
            "----------",
            "Run for additional information:",
            "  " + LocalizedMessage.BLUE + "%5$s %6$s arenas " + LocalizedMessage.GRAY + " to list the minigame arenas.",
            "  " + LocalizedMessage.BLUE + "%5$s %6$s manual " + LocalizedMessage.GRAY + " to display a manual."
        })
        @MessageComment(value = {
            "The command output of /mg2 info minigame"
        },args = {
                @Argument("display name"),
                @Argument("short description"),
                @Argument("version number"),
                @Argument("description"),
                @Argument("command path"),
                @Argument("minigame name"),
                })
        CommandOutput,
    }
    
}
