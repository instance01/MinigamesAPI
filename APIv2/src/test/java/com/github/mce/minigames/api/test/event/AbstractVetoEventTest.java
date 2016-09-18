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

package com.github.mce.minigames.api.test.event;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.bukkit.event.HandlerList;
import org.junit.Test;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.event.AbstractVetoEvent;

/**
 * test case for {@link AbstractVetoEvent}
 * 
 * @author mepeisen
 *
 */
public class AbstractVetoEventTest
{
    
    /**
     * Tests the event class
     */
    @Test
    public void testMe()
    {
        final VetoEvent evt = new VetoEvent();
        
        assertFalse(evt.isCancelled());
        assertNull(evt.getVetoReason());
        assertNull(evt.getVetoReasonArgs());
        
        evt.setCancelled(CommonMessages.AraneStateDisabled);
        assertTrue(evt.isCancelled());
        assertEquals(CommonMessages.AraneStateDisabled, evt.getVetoReason());
        assertArrayEquals(new Serializable[0], evt.getVetoReasonArgs());
        
        evt.setCancelled(CommonMessages.AraneStateDisabled, Integer.MAX_VALUE);
        assertTrue(evt.isCancelled());
        assertEquals(CommonMessages.AraneStateDisabled, evt.getVetoReason());
        assertArrayEquals(new Serializable[]{Integer.MAX_VALUE}, evt.getVetoReasonArgs());
    }
    
    /**
     * @author mepeisen
     *
     */
    public static class VetoEvent extends AbstractVetoEvent
    {
        
        /** handlers list. */
        private static final HandlerList handlers = new HandlerList();
        
        /**
         * Constructor.
         */
        public VetoEvent()
        {
            // empty
        }
        
        /**
         * Returns the handlers list
         * 
         * @return handlers
         */
        @Override
        public HandlerList getHandlers()
        {
            return handlers;
        }
        
        /**
         * Returns the handlers list
         * 
         * @return handlers
         */
        public static HandlerList getHandlerList()
        {
            return handlers;
        }
        
    }
    
}
