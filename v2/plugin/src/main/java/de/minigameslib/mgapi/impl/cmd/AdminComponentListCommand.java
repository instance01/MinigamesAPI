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
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminComponentListCommand extends AbstractPagableCommandHandler implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandAdminComp);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdminComp, command.getCommandPath());
        
        final ArenaInterface arena = Mg2Command.getArena(command, Messages.Usage);
        
        McLibInterface.instance().setContext(ArenaInterface.class, arena);
        super.handle(command);
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return MinigamesLibInterface.instance().getArenas(lastArg, 0, Integer.MAX_VALUE).stream()
                    .filter(a -> a.getState() == ArenaState.Maintenance)
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

    @Override
    protected int getLineCount(CommandInterface command)
    {
        final ArenaInterface arena = McLibInterface.instance().getContext(ArenaInterface.class);
        return arena.getZones().size();
    }

    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        final ArenaInterface arena = McLibInterface.instance().getContext(ArenaInterface.class);
        return Messages.Header.toArg(arena.getInternalName());
    }

    @SuppressWarnings("cast")
    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        final ArenaInterface arena = McLibInterface.instance().getContext(ArenaInterface.class);
        return arena.getComponents().stream().
                skip(start).
                limit(count).
                map(s -> (ArenaComponentHandler) arena.getHandler(s)).
                map(ArenaComponentHandler::getName).
                toArray(Serializable[]::new);
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_admin_component_list")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin comp list
         */
        @LocalizedMessage(defaultMessage = "lists arena components")
        @MessageComment({"Short description of /mg2 admin comp list"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin zone list
         */
        @LocalizedMessage(defaultMessage = "lists arena components")
        @MessageComment({"Long description of /mg2 admin comp list"})
        Description,
        
        /**
         * Usage of /mg2 admin zone list
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin comp list <arena-name> [page]")
        @MessageComment({"Usage of /mg2 admin comp list"})
        Usage,
        
        /**
         * Pageable header line
         */
        @LocalizedMessage(defaultMessage = "Components of " + LocalizedMessage.CODE_COLOR + "%1$s")
        @MessageComment(value = {"Pageable header line of /mg2 admin comp list"}, args=@Argument("arena internal name"))
        Header
        
    }
    
}
