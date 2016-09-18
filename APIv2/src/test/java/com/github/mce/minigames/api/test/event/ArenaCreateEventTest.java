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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.event.ArenaCreateEvent;

/**
 * test case for {@link ArenaCreateEvent}
 * 
 * @author mepeisen
 *
 */
public class ArenaCreateEventTest
{
    
    /**
     * Tests the event class
     */
    @Test
    public void testMe()
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaCreateEvent evt = new ArenaCreateEvent(arena);
        
        assertEquals(evt.getHandlers(), ArenaCreateEvent.getHandlerList());
        assertEquals(arena, evt.getArena());
        assertFalse(evt.isCancelled());
    }
    
}
