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

package com.github.mce.minigames.api.test.context;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;

import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.component.ComponentInterface;
import com.github.mce.minigames.api.context.MinigameContext;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.zones.ZoneInterface;

/**
 * test case for {@link MinigameContext}
 * 
 * @author mepeisen
 */
public class MinigameContextTest
{
    
    /**
     * Tests {@link MinigameContext#getCurrentArena()}
     */
    @Test
    public void getCurrentArenaTest()
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final MgContextImpl ctx = mock(MgContextImpl.class);
        when(ctx.getCurrentArena()).thenCallRealMethod();
        when(ctx.getContext(ArenaInterface.class)).thenReturn(arena);
        
        assertSame(arena, ctx.getCurrentArena());
    }
    
    /**
     * Tests {@link MinigameContext#getCurrentComponent()}
     */
    @Test
    public void getCurrentComponentTest()
    {
        final ComponentInterface comp = mock(ComponentInterface.class);
        final MgContextImpl ctx = mock(MgContextImpl.class);
        when(ctx.getCurrentComponent()).thenCallRealMethod();
        when(ctx.getContext(ComponentInterface.class)).thenReturn(comp);
        
        assertSame(comp, ctx.getCurrentComponent());
    }
    
    /**
     * Tests {@link MinigameContext#getCurrentZone()}
     */
    @Test
    public void getCurrentZoneTest()
    {
        final ZoneInterface zone = mock(ZoneInterface.class);
        final MgContextImpl ctx = mock(MgContextImpl.class);
        when(ctx.getCurrentZone()).thenCallRealMethod();
        when(ctx.getContext(ZoneInterface.class)).thenReturn(zone);
        
        assertSame(zone, ctx.getCurrentZone());
    }
    
    /**
     * Tests {@link MinigameContext#getCurrentMinigame()}
     */
    @Test
    public void getCurrentMinigameTest()
    {
        final MinigameInterface mg = mock(MinigameInterface.class);
        final MgContextImpl ctx = mock(MgContextImpl.class);
        when(ctx.getCurrentMinigame()).thenCallRealMethod();
        when(ctx.getContext(MinigameInterface.class)).thenReturn(mg);
        
        assertSame(mg, ctx.getCurrentMinigame());
    }
    
    /**
     * Tests {@link MinigameContext#getCurrentPlayer()}
     */
    @Test
    public void getCurrentPlayerTest()
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final MgContextImpl ctx = mock(MgContextImpl.class);
        when(ctx.getCurrentPlayer()).thenCallRealMethod();
        when(ctx.getContext(ArenaPlayerInterface.class)).thenReturn(player);
        
        assertSame(player, ctx.getCurrentPlayer());
    }
    
    /** helper. */
    private abstract class MgContextImpl implements MinigameContext
    {
        // empty
    }
    
}
