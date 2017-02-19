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
public class ArenaListCommand extends AbstractPagableCommandHandler
{
    
    /** supplier for arena count. */
    private IntSupplier count;
    /** supplier for arena stream. */
    private Supplier<Stream<ArenaInterface>> arenas;
    /** header line. */
    private Serializable header;

    /**
     * @param count
     * @param arenas
     * @param header 
     */
    public ArenaListCommand(IntSupplier count, Supplier<Stream<ArenaInterface>> arenas, Serializable header)
    {
        this.count = count;
        this.arenas = arenas;
        this.header = header;
    }

    @Override
    protected int getLineCount(CommandInterface command)
    {
        return this.count.getAsInt();
    }
    
    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return this.header;
    }
    
    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int limit)
    {
        return this.arenas.get().skip(start).limit(limit).map(p -> p.getInternalName()).toArray(Serializable[]::new);
    }
    
}
