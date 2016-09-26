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

package com.github.mce.minigames.impl.test.nms.v1_10_1.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.impl.nms.v1_10_1.event.MgBlockPistonRetractEvent;

/**
 * Test for {@link MgBlockPistonRetractEvent}
 * 
 * @author mepeisen
 *
 */
public class MgBlockPistonRetractEventTest
{
    
    /**
     * Tests constructor
     */
    @Test
    public void testConstructor()
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final MinigameInterface minigame = mock(MinigameInterface.class);
        when(arena.getMinigame()).thenReturn(minigame);
        
        final MglibInterface lib = mock(MglibInterface.class);
        Whitebox.setInternalState(MglibInterface.INSTANCE.class, "CACHED", lib); //$NON-NLS-1$
        final Location loc = new Location(null, 1, 2, 3);
        when(lib.getArenaFromLocation(loc)).thenReturn(arena);
        
        final Block block = mock(Block.class);
        when(block.getLocation()).thenReturn(loc);
        final BlockPistonRetractEvent evt = new BlockPistonRetractEvent(block, new ArrayList<>(), BlockFace.DOWN);
        final MgBlockPistonRetractEvent mgevt = new MgBlockPistonRetractEvent(evt);
        
        assertEquals(evt, mgevt.getBukkitEvent());
        assertEquals(arena, mgevt.getArena());
        assertEquals(minigame, mgevt.getMinigame());
        assertNull(mgevt.getPlayer());
    }
    
}
