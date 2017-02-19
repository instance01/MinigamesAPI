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
import de.minigameslib.mclib.api.cmd.LocalizedPagableCommand;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
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
        command.checkMinArgCount(1, Mg2Command.Messages.MinigameNameMissing, Messages.Usage);
        final MinigameInterface minigame = command.fetch(Mg2Command::getMinigame).get();
        command.checkMaxArgCount(1, CommonMessages.TooManyArguments);
        
        if (command.getArgs().length == 1)
        {
            final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
            switch (command.getArgs()[0])
            {
                case "arenas": //$NON-NLS-1$
                    new ArenaListCommand(
                            () -> mglib.getArenaCount(minigame.getPlugin()),
                            () -> mglib.getArenas(minigame.getPlugin(), 0, Integer.MAX_VALUE).stream(),
                            Messages.ArenasPagedHeader.toArg(minigame.getDisplayName())
                            ).handle(command.consumeArgs(1));
                    return;
                case "manual": //$NON-NLS-1$
                    new LocalizedPagableCommand(
                            minigame.getManual(),
                            Messages.ManualPagedHeader.toArg(minigame.getDisplayName())
                            ).handle(command.consumeArgs(1));
                    return;
                default:
                    command.send(CommonMessages.CompositeUnknownSubCommand, command.getCommandPath(), command.getArgs()[1]);
                    return;
            }
        }
        
        // print Info
        final String version = minigame.getPlugin().getDescription().getVersion();
        command.send(Messages.CommandOutput,
                minigame.getDisplayName(),
                minigame.getShortDescription(),
                version,
                minigame.getDescription().toListArg(),
                command.getCommandPath(),
                minigame.getName()
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
            return Arrays.asList("arenas", "manual").stream().filter(p -> p.startsWith(lastArg.toLowerCase())).collect(Collectors.toList());  //$NON-NLS-1$//$NON-NLS-2$
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
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 info minigame <name>")
        @MessageComment({"Usage of /mg2 info minigame"})
        Usage,
        
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
            "  " + LocalizedMessage.CODE_COLOR + "%5$s %6$s arenas " + LocalizedMessage.INFORMATION_COLOR + " to list the minigame arenas.",
            "  " + LocalizedMessage.CODE_COLOR + "%5$s %6$s manual " + LocalizedMessage.INFORMATION_COLOR + " to display a manual."
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
        
        /**
         * Header line for /mg2 minigame ... arenas
         */
        @LocalizedMessageList({
            "Arenas for minigame %1$s."
        })
        @MessageComment(value = {
            "Header line for /mg2 minigame ... arenas"
        },args = {
                @Argument("minigame name"),
                })
        ArenasPagedHeader,
        
        /**
         * Header line for /mg2 minigame ... manual
         */
        @LocalizedMessageList({
            "Manual for minigame %1$s."
        })
        @MessageComment(value = {
            "Header line for /mg2 minigame ... manual"
        },args = {
                @Argument("minigame name"),
                })
        ManualPagedHeader,
        
    }
    
}
