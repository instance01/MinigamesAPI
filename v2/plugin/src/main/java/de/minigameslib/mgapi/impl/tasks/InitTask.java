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

import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.internal.TaskManager;

/**
 * Initialization task for minigames.
 * 
 * @author mepeisen
 */
public class InitTask extends BukkitRunnable
{
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(InitTask.class.getName());

    @Override
    public void run()
    {
        for (final ArenaInterface arena : MinigamesLibInterface.instance().getArenas(0, Integer.MAX_VALUE))
        {
            LOGGER.log(Level.INFO, "initializing arena " + arena.getInternalName()); //$NON-NLS-1$
            if (arena.getState() == ArenaState.Booting)
            {
                final ArenaImpl impl = (ArenaImpl) arena;
                if (impl.isMatchPending())
                {
                    LOGGER.log(Level.INFO, "trying crash recovery of arena " + arena.getInternalName()); //$NON-NLS-1$
                    TaskManager.instance().queue(new AsyncArenaRecoverCrashTask((ArenaImpl) arena));
                }
                else
                {
                    LOGGER.log(Level.INFO, "trying to start arena " + arena.getInternalName()); //$NON-NLS-1$
                    TaskManager.instance().queue(new AsyncArenaStartTask((ArenaImpl) arena));
                }
            }
            else
            {
                LOGGER.log(Level.WARNING, "Wrong arena state. Expected booting for arena " + arena.getInternalName()); //$NON-NLS-1$
            }
        }
    }
    
}
