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

import com.github.mce.minigames.api.event.PlayerOpenGuiEvent;
import com.github.mce.minigames.api.gui.ClickGuiInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.MgPlayerOpenGuiEvent;

/**
 * Test for {@link MgPlayerOpenGuiEvent}
 * 
 * @author mepeisen
 *
 */
public class MgPlayerOpenGuiEventTest
{
    
    /**
     * Tests constructor
     */
    @Test
    public void testConstructor()
    {
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final PlayerOpenGuiEvent evt = new PlayerOpenGuiEvent(gui, player);
        final MgPlayerOpenGuiEvent mgevt = new MgPlayerOpenGuiEvent(evt);
        
        assertEquals(evt, mgevt.getBukkitEvent());
        assertNull(mgevt.getArena());
        assertNull(mgevt.getMinigame());
        assertEquals(player, mgevt.getPlayer());
    }
    
}
