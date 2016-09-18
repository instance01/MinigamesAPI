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

package com.github.mce.minigames.api.event;

import java.io.Serializable;

import org.bukkit.event.Event;

import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

/**
 * An event that can be cancelled.
 * 
 * @author mepeisen
 */
public abstract class AbstractVetoEvent extends Event
{
    
    /** the veto flag. */
    private boolean cancelled;
    
    /** the veto reason. */
    private LocalizedMessageInterface vetoReason;
    
    /** message arguments. */
    private Serializable[] vetoReasonArgs;

    /**
     * Returns the veto reason
     * @return the vetoReason
     */
    public LocalizedMessageInterface getVetoReason()
    {
        return this.vetoReason;
    }

    /**
     * Checks if the event was cancelled.
     * @return the veto flag
     */
    public boolean isCancelled()
    {
        return this.cancelled;
    }

    /**
     * Returns the message arguments to format the veto reason message
     * @return the vetoReasonArgs
     */
    public Serializable[] getVetoReasonArgs()
    {
        return this.vetoReasonArgs;
    }

    /**
     * Sets the event cancelled.
     * @param reason the reason text.
     * @param args message arguments
     */
    public void setCancelled(LocalizedMessageInterface reason, Serializable... args)
    {
        this.cancelled = true;
        this.vetoReason = reason;
        this.vetoReasonArgs = args;
    }
    
}
