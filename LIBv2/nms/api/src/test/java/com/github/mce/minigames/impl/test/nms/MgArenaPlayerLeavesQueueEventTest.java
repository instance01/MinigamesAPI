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

package com.github.mce.minigames.impl.test.nms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;

import com.github.mce.minigames.api.arena.WaitQueue;
import com.github.mce.minigames.api.event.ArenaPlayerLeavesQueueEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.MgArenaPlayerLeavesQueueEvent;

/**
 * Test for {@link MgArenaPlayerLeavesQueueEvent}
 * 
 * @author mepeisen
 *
 */
public class MgArenaPlayerLeavesQueueEventTest
{
    
    /**
     * Tests constructor
     */
    @Test
    public void testConstructor()
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final WaitQueue queue = mock(WaitQueue.class);
        final ArenaPlayerLeavesQueueEvent evt = new ArenaPlayerLeavesQueueEvent(queue, player);
        final MgArenaPlayerLeavesQueueEvent mgevt = new MgArenaPlayerLeavesQueueEvent(evt);
        
        assertEquals(evt, mgevt.getBukkitEvent());
        assertNull(mgevt.getArena());
        assertNull(mgevt.getMinigame());
        assertEquals(player, mgevt.getPlayer());
    }
    
}
