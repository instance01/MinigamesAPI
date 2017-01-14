/*
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

package com.github.mce.minigames.impl.cmd;

import java.util.ArrayList;
import java.util.List;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.perms.CommonPermissions;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * Command to create a new arena.
 * 
 * @author mepeisen
 */
public class CreateCommandHandler implements SubCommandHandlerInterface
{
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
//        command.permThrowException(CommonPermissions.Create, command.getCommandPath());
//        
//        if (command.getArgs().length < 2)
//        {
//            command.send(CommonMessages.CreateCommandUsage, command.getCommandPath());
//            return;
//        }
//        
//        final MglibInterface lib = MglibInterface.INSTANCE.get();
//        final MinigameInterface minigame = lib.getMinigame(command.getArgs()[0]);
//        if (minigame == null)
//        {
//            command.send(CommonMessages.MinigameNotFound, command.getArgs()[0]);
//            command.send(CommonMessages.CreateCommandUsage, command.getCommandPath());
//            return;
//        }
//        
//        ArenaTypeDeclarationInterface type = null;
//        String arenaName = null;
//        if (command.getArgs().length == 2)
//        {
//            type = minigame.getDefaultType();
//            if (type == null)
//            {
//                command.send(CommonMessages.DefaultArenaTypeNotFound, command.getArgs()[0]);
//                command.send(CommonMessages.CreateCommandUsage, command.getCommandPath());
//                return;
//            }
//            arenaName = command.getArgs()[1];
//        }
//        else
//        {
//            type = minigame.getType(command.getArgs()[1]);
//            if (type == null)
//            {
//                command.send(CommonMessages.ArenaTypeNotFound, command.getArgs()[0], command.getArgs()[1]);
//                command.send(CommonMessages.CreateCommandUsage, command.getCommandPath());
//                return;
//            }
//            arenaName = command.getArgs()[2];
//        }
//        
//        type.createArena(arenaName);
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
//        final MglibInterface lib = MglibInterface.INSTANCE.get();
//        if (command.getArgs().length == 0)
//        {
//            final List<String> result = new ArrayList<>();
//            for (final MinigameInterface mg : lib.getMinigames())
//            {
//                if (lastArg == null)
//                {
//                    result.add(mg.getName());
//                }
//                else if (mg.getName().startsWith(lastArg))
//                {
//                    result.add(mg.getName());
//                }
//            }
//            return result;
//        }
//        else if (command.getArgs().length == 1)
//        {
//            final List<String> result = new ArrayList<>();
//            final MinigameInterface mg = lib.getMinigame(command.getArgs()[0]);
//            if (mg != null)
//            {
//                for (final ArenaTypeDeclarationInterface type : mg.getDeclaredTypes())
//                {
//                    if (lastArg == null)
//                    {
//                        result.add(type.getName());
//                    }
//                    else if (type.getName().startsWith(lastArg))
//                    {
//                        result.add(type.getName());
//                    }
//                }
//            }
//            return result;
//        }
        return null;
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return CommonMessages.InfoCommandShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return CommonMessages.InfoCommandDescription;
    }
    
}
