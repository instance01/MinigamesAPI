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

package de.minigameslib.mgapi.impl.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.internal.TaskManager;

/**
 * A task to check and start a arena.
 * 
 * @author mepeisen
 */
public class AsyncArenaRecoverCrashTask extends AbstractAsyncArenaTask
{
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(AsyncArenaRecoverCrashTask.class.getName());
    
    /**
     * @param arenaImpl
     */
    public AsyncArenaRecoverCrashTask(ArenaImpl arenaImpl)
    {
        super(arenaImpl);
    }

    @Override
    public void run() throws McException
    {
        LOGGER.log(Level.INFO, "loading crashed arena " + this.arena.getInternalName()); //$NON-NLS-1$
        if (this.arena.getState() == ArenaState.Booting)
        {
            this.arena.resume();
        }
        
        // TODO do some crash recovery
        
        TaskManager.instance().queue(new AsyncArenaStartTask(this.arena));
    }
    
}
