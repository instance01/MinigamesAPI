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

package com.github.mce.minigames.api.test.player;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.entity.Player;
import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.perms.PermissionsInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * test case for {@link ArenaPlayerInterface}
 * 
 * @author mepeisen
 */
public class ArenaPlayerInterfaceTest
{
    
    /**
     * Tests {@link ArenaPlayerInterface#hasPerm(com.github.mce.minigames.api.perms.PermissionsInterface)}
     * 
     * @throws MinigameException
     *             thrown on errors
     */
    @Test
    public void hasPermTestTrue() throws MinigameException
    {
        final PermissionsInterface perm = mock(PermissionsInterface.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        when(player.checkPermission(perm)).thenReturn(Boolean.TRUE);
        
        assertTrue(ArenaPlayerInterface.hasPerm(perm).test(player));
    }
    
    /**
     * Tests {@link ArenaPlayerInterface#hasPerm(com.github.mce.minigames.api.perms.PermissionsInterface)}
     * 
     * @throws MinigameException
     *             thrown on errors
     */
    @Test
    public void hasPermTestFalse() throws MinigameException
    {
        final PermissionsInterface perm = mock(PermissionsInterface.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        when(player.checkPermission(perm)).thenReturn(Boolean.FALSE);
        
        assertFalse(ArenaPlayerInterface.hasPerm(perm).test(player));
    }
    
    /**
     * Tests {@link ArenaPlayerInterface#hasPerm(com.github.mce.minigames.api.perms.PermissionsInterface)}
     * 
     * @throws MinigameException
     *             thrown on ok
     */
    @SuppressWarnings("unchecked")
    @Test(expected = MinigameException.class)
    public void hasPermTestExc() throws MinigameException
    {
        final PermissionsInterface perm = mock(PermissionsInterface.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        when(player.checkPermission(perm)).thenThrow(MinigameException.class);
        
        ArenaPlayerInterface.hasPerm(perm).test(player);
    }
    
    // *****
    
    /**
     * Tests {@link ArenaPlayerInterface#isInArena()}
     * 
     * @throws MinigameException
     *             thrown on errors
     */
    @Test
    public void isInArenaTestTrue() throws MinigameException
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        when(player.getArena()).thenReturn(arena);
        
        assertTrue(ArenaPlayerInterface.isInArena().test(player));
    }
    
    /**
     * Tests {@link ArenaPlayerInterface#isInArena()}
     * 
     * @throws MinigameException
     *             thrown on errors
     */
    @Test
    public void isInArenaTestFalse() throws MinigameException
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        when(player.getArena()).thenReturn(null);
        
        assertFalse(ArenaPlayerInterface.isInArena().test(player));
    }
    
    // *****
    
    /**
     * Tests {@link ArenaPlayerInterface#isOnline()}
     * 
     * @throws MinigameException
     *             thrown on errors
     */
    @Test
    public void isOnlineTestTrue() throws MinigameException
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final Player bukkit = mock(Player.class);
        when(player.getBukkitPlayer()).thenReturn(bukkit);
        
        assertTrue(ArenaPlayerInterface.isOnline().test(player));
    }
    
    /**
     * Tests {@link ArenaPlayerInterface#isOnline()}
     * 
     * @throws MinigameException
     *             thrown on errors
     */
    @Test
    public void isOnlineTestFalse() throws MinigameException
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        when(player.getBukkitPlayer()).thenReturn(null);
        
        assertFalse(ArenaPlayerInterface.isOnline().test(player));
    }
    
}
