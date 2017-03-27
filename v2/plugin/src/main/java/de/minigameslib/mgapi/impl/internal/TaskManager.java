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

package de.minigameslib.mgapi.impl.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.impl.tasks.AsyncTaskInterface;

/**
 * A task manager to queue periodic work tasks.
 * 
 * @author mepeisen
 */
public class TaskManager
{
    
    /** concurrent executors. */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(TaskManager.class.getName());
    
    /**
     * Returns the task manager instance.
     * @return task manager
     */
    public static TaskManager instance()
    {
        return TaskManagerCache.get();
    }

    /**
     * Queues a new task to be runned asap.
     * @param task
     */
    public void queue(AsyncTaskInterface task)
    {
        this.executor.execute(() -> {
            try
            {
                task.run();
            }
            catch (McException ex)
            {
                LOGGER.log(Level.WARNING, "Caught exception while executing task: " + task, ex); //$NON-NLS-1$
            }
        });
    }
    
}
