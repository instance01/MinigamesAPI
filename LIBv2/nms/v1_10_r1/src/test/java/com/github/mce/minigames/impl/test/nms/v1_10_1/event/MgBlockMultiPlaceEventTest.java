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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.v1_10_1.event.MgBlockMultiPlaceEvent;

/**
 * Test for {@link MgBlockMultiPlaceEvent}
 * 
 * @author mepeisen
 *
 */
public class MgBlockMultiPlaceEventTest
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
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        
        final MglibInterface lib = mock(MglibInterface.class);
        Whitebox.setInternalState(MglibInterface.INSTANCE.class, "CACHED", lib); //$NON-NLS-1$
        final Location loc = new Location(null, 1, 2, 3);
        when(lib.getArenaFromLocation(loc)).thenReturn(arena);
        when(lib.getPlayer(any(Player.class))).thenReturn(player);
        
        final Block block = mock(Block.class);
        when(block.getLocation()).thenReturn(loc);
        final List<BlockState> states = new ArrayList<>();
        final BlockState state = mock(BlockState.class);
        when(state.getBlock()).thenReturn(block);
        states.add(state);
        final BlockMultiPlaceEvent evt = new BlockMultiPlaceEvent(states, block, new ItemStack(Material.ACACIA_DOOR), mock(Player.class), true);
        final MgBlockMultiPlaceEvent mgevt = new MgBlockMultiPlaceEvent(evt);
        
        assertEquals(evt, mgevt.getBukkitEvent());
        assertEquals(arena, mgevt.getArena());
        assertEquals(minigame, mgevt.getMinigame());
        assertEquals(player, mgevt.getPlayer());
    }
    
}
