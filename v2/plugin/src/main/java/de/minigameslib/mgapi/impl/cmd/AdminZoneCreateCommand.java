/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-Zoneed commercial license from minigames.de
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
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.impl.MglibPerms;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.cmd.tool.AdminToolHelper;

/**
 * @author mepeisen
 *
 */
public class AdminZoneCreateCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOnline() && command.checkOpPermission(MglibPerms.CommandAdminZone);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOnline();
        command.permOpThrowException(MglibPerms.CommandAdminZone, command.getCommandPath());
        
        final ArenaInterface arena = Mg2Command.getArena(command, Messages.Usage);
        final String name = command.fetchString(Mg2Command.Messages.ComponentNameMissing, Messages.Usage);
        final String typeName = command.fetchString(Mg2Command.Messages.ComponentTypeNameMissing, Messages.Usage);
        
        @SuppressWarnings("cast")
        final Optional<ArenaZoneHandler> handler = arena.getZones().stream().
                map(s -> (ArenaZoneHandler) arena.getHandler(s)).
                filter(s -> name.equals(s.getName())).
                findFirst();
        if (handler.isPresent())
        {
            throw new McException(Mg2Command.Messages.ComponentAlreadyExists, name);
        }
        if (!arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        final ZoneTypeId type = Mg2Command.getEnum(command, ZoneTypeId.class, typeName);
        
        AdminToolHelper.onCreateZone(command.getPlayer(), arena, name, type, null);
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
        if (command.getArgs().length == 2)
        {
            final Set<String> result = new TreeSet<>();
            for (final ZoneTypeId ZoneType : EnumServiceInterface.instance().getEnumValues(ZoneTypeId.class))
            {
                result.add(ZoneType.getPluginName() + "/" + ZoneType.name()); //$NON-NLS-1$
            }
            return result.stream().filter(a -> a.toLowerCase().startsWith(lastArg)).collect(Collectors.toList());
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
    @LocalizedMessages(value = "cmd.mg2_admin_zone_create")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin zone create
         */
        @LocalizedMessage(defaultMessage = "Creates a new arena zone")
        @MessageComment({"Short description of /mg2 zone create"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin zone create
         */
        @LocalizedMessage(defaultMessage = "Creates a new arena zone")
        @MessageComment({"Long description of /mg2 admin zone create"})
        Description,
        
        /**
         * Usage of /mg2 admin zone create
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin zone create <arena> <name> <type>")
        @MessageComment({"Usage of /mg2 admin zone create"})
        Usage,
        
    }
    
}
