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

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.v1_10_1.event.MgAsyncPlayerChatEvent;

/**
 * Test for {@link MgAsyncPlayerChatEvent}
 * 
 * @author mepeisen
 *
 */
public class MgAsyncPlayerChatEventTest
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
        when(lib.getPlayer(any(Player.class))).thenReturn(player);
        when(player.getArena()).thenReturn(arena);
        
        final AsyncPlayerChatEvent evt = new AsyncPlayerChatEvent(true, mock(Player.class), "FOO", new HashSet<>()); //$NON-NLS-1$
        final MgAsyncPlayerChatEvent mgevt = new MgAsyncPlayerChatEvent(evt);
        
        assertEquals(evt, mgevt.getBukkitEvent());
        assertEquals(arena, mgevt.getArena());
        assertEquals(minigame, mgevt.getMinigame());
        assertEquals(player, mgevt.getPlayer());
    }
    
}
