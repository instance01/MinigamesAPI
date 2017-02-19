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

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;

/**
 * A task to check and start a arena.
 * 
 * @author mepeisen
 */
public class AsyncArenaStartTask extends AbstractAsyncArenaTask
{
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(AsyncArenaStartTask.class.getName());
    
    /**
     * Constructor
     * @param arenaImpl
     */
    public AsyncArenaStartTask(ArenaImpl arenaImpl)
    {
        super(arenaImpl);
    }

    @Override
    public void run() throws McException
    {
        if (this.arena.getState() == ArenaState.Booting)
        {
            LOGGER.log(Level.INFO, "loading arena " + this.arena.getInternalName()); //$NON-NLS-1$
            this.arena.resume();
        }
        
        LOGGER.log(Level.INFO, "arena " + this.arena.getInternalName() + " config loaded."); //$NON-NLS-1$ //$NON-NLS-2$
        
        if (this.arena.getState() == ArenaState.Starting)
        {
            LOGGER.log(Level.INFO, "checking arena " + this.arena.getInternalName() + " for errors..."); //$NON-NLS-1$ //$NON-NLS-2$
            
            final Collection<CheckFailure> checks = this.arena.check();
            if (!checks.isEmpty())
            {
                final StringBuilder builder = new StringBuilder();
                boolean hadError = false;
                builder.append("arena ").append(this.arena.getInternalName()).append(" maybe broken. Got followng results:\n"); //$NON-NLS-1$ //$NON-NLS-2$
                for (final CheckFailure failure : checks)
                {
                    builder.append("--> ").append(failure.getSeverity()).append(": ").append(failure.getTitle()).append("\n").append(failure.getDetails()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    hadError |= failure.getSeverity() == CheckSeverity.Error;
                }
                LOGGER.log(Level.WARNING, builder.toString());
                if (hadError)
                {
                    LOGGER.log(Level.WARNING, "disabling arena " + this.arena.getInternalName() + " caused by errors."); //$NON-NLS-1$ //$NON-NLS-2$
                    this.arena.setDisabled0();
                }
            }
        }
        
        LOGGER.log(Level.INFO, "arena " + this.arena.getInternalName() + " loaded. State: " + this.arena.getState()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
