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
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.github.mce.minigames.api.event.PlayerDisplayGuiPageEvent;
import com.github.mce.minigames.api.gui.ClickGuiInterface;
import com.github.mce.minigames.api.gui.ClickGuiPageInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * test case for {@link PlayerDisplayGuiPageEvent}
 * 
 * @author mepeisen
 *
 */
public class PlayerDisplayGuiPageEventTest
{
    
    /**
     * Tests the event class
     */
    @Test
    public void testMe()
    {
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final ClickGuiPageInterface page = mock(ClickGuiPageInterface.class);
        final PlayerDisplayGuiPageEvent evt = new PlayerDisplayGuiPageEvent(gui, player, page);
        
        assertEquals(evt.getHandlers(), PlayerDisplayGuiPageEvent.getHandlerList());
        assertEquals(gui, evt.getGui());
        assertEquals(player, evt.getPlayer());
        assertEquals(page, evt.getPage());
    }
    
}
