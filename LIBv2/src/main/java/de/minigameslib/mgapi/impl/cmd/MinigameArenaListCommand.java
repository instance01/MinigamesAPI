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
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;

/**
 * Handle the minigame arenas list
 * 
 * @author mepeisen
 */
public class MinigameArenaListCommand extends AbstractPagableCommandHandler
{
    
    /**
     * @param count
     * @param arenas
     */
    public MinigameArenaListCommand(IntSupplier count, Supplier<Stream<ArenaInterface>> arenas)
    {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler#getLineCount(de.minigameslib.mclib.api.cmd.CommandInterface)
     */
    @Override
    protected int getLineCount(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler#getHeader(de.minigameslib.mclib.api.cmd.CommandInterface)
     */
    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler#getLines(de.minigameslib.mclib.api.cmd.CommandInterface, int, int)
     */
    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
