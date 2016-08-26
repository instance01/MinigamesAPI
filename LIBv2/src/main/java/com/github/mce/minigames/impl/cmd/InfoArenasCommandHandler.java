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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.cmd.AbstractPagableCommandHandler;
import com.github.mce.minigames.api.cmd.CommandInterface;

/**
 * Command to display useful information.
 * 
 * @author mepeisen
 */
public class InfoArenasCommandHandler extends AbstractPagableCommandHandler
{
    
    @Override
    protected int getLineCount(CommandInterface command)
    {
        return MglibInterface.INSTANCE.get().getArenaCount();
    }

    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return CommonMessages.InfoArenasHeader.toArg(command.getCommandPath());
    }

    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        final Iterator<ArenaInterface> arenas = MglibInterface.INSTANCE.get().getArenas().iterator();
        int i = 0;
        while (i < start && arenas.hasNext())
        {
            arenas.next();
            i++;
        }
        final List<Serializable> result = new ArrayList<>();
        for (i = 0; i < count; i++)
        {
            if (arenas.hasNext())
            {
                final ArenaInterface arena = arenas.next();
                Serializable state = null;
                switch (arena.getState())
                {
                    case Disabled:
                    default:
                        state = CommonMessages.AraneStateDisabled.toArg();
                        break;
                    case InGame:
                        state = CommonMessages.AraneStateInGame.toArg();
                        break;
                    case Join:
                        state = CommonMessages.AraneStateJoin.toArg();
                        break;
                    case Maintenance:
                        state = CommonMessages.AraneStateMeintenance.toArg();
                        break;
                    case Restarting:
                        state = CommonMessages.AraneStateRestarting.toArg();
                        break;
                    case Starting:
                        state = CommonMessages.AraneStateStarting.toArg();
                        break;
                }
                result.add(CommonMessages.InfoArenaLine.toArg(
                        arena.getMinigame().getName(),
                        arena.getInternalName(),
                        state,
                        arena.getDisplayName()
                        ));
            }
        }
        return result.toArray(new Serializable[result.size()]);
    }
    
}
