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

package com.comze_instancelabs.minigamesapi.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaPlayer;
import com.comze_instancelabs.minigamesapi.util.AClass;

/**
 * Test case for the arena player class.
 * 
 * @author mepeisen
 * @see ArenaPlayer
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class ArenaPlayerTest
{
    
    /**
     * tests if the player instances are created as needed.
     */
    @Test
    public void testCreatePlayerInstance()
    {
        // prolog
        final String name = "ArenaPlayerTest#testCreatePlayerInstance"; //$NON-NLS-1$

        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertEquals(name, arenaPlayer.getPlayer().getName());
    }
    
    /**
     * tests if the player instances are returned when already known.
     */
    @Test
    public void testCreatePlayerInstanceTwice()
    {
        // prolog
        final String name = "ArenaPlayerTest#testCreatePlayerInstanceTwice"; //$NON-NLS-1$

        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer1 = ArenaPlayer.getPlayerInstance(name);
        final ArenaPlayer arenaPlayer2 = ArenaPlayer.getPlayerInstance(name);
        assertSame(arenaPlayer1, arenaPlayer2);
    }
    
    /**
     * tests the inventory setting
     */
    @Test
    public void testInventories()
    {
        // prolog
        final String name = "ArenaPlayerTest#testInventories"; //$NON-NLS-1$

        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertNull(arenaPlayer.getInventory());
        assertNull(arenaPlayer.getArmorInventory());
        
        final ItemStack[] inv = new ItemStack[]{new ItemStack(Material.AIR, 1)};
        final ItemStack[] armor_inv = new ItemStack[]{new ItemStack(Material.STONE, 1), new ItemStack(Material.WOOD, 1)};
        arenaPlayer.setInventories(inv, armor_inv);
        
        assertArrayEquals(inv, arenaPlayer.getInventory());
        assertArrayEquals(armor_inv, arenaPlayer.getArmorInventory());
    }
    
    /**
     * tests the original gamemode setting
     */
    @Test
    public void testOriginalGamemode()
    {
        // prolog
        final String name = "ArenaPlayerTest#testOriginalGamemode"; //$NON-NLS-1$

        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertEquals(GameMode.SURVIVAL, arenaPlayer.getOriginalGamemode());
        arenaPlayer.setOriginalGamemode(GameMode.CREATIVE);
        assertEquals(GameMode.CREATIVE, arenaPlayer.getOriginalGamemode());
    }
    
    /**
     * tests the original xp level
     */
    @Test
    public void testOriginalXpLevel()
    {
        // prolog
        final String name = "ArenaPlayerTest#testOriginalXpLevel"; //$NON-NLS-1$

        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertEquals(0, arenaPlayer.getOriginalXplvl());
        arenaPlayer.setOriginalXplvl(100);
        assertEquals(100, arenaPlayer.getOriginalXplvl());
    }
    
    /**
     * tests the no reward flag
     */
    @Test
    public void testNoReward()
    {
        // prolog
        final String name = "ArenaPlayerTest#testNoReward"; //$NON-NLS-1$

        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertFalse(arenaPlayer.isNoReward());
        arenaPlayer.setNoReward(true);
        assertTrue(arenaPlayer.isNoReward());
    }
    
    /**
     * tests the arena field
     */
    @Test
    public void testArena()
    {
        // prolog
        final String name = "ArenaPlayerTest#testArena"; //$NON-NLS-1$

        final Arena arena = mock(Arena.class);
        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertNull(arenaPlayer.getCurrentArena());
        arenaPlayer.setCurrentArena(arena);
        assertSame(arena, arenaPlayer.getCurrentArena());
    }
    
    /**
     * tests the class field
     */
    @Test
    public void testClass()
    {
        // prolog
        final String name = "ArenaPlayerTest#testArena"; //$NON-NLS-1$

        final AClass clazz = mock(AClass.class);
        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        
        // test
        final ArenaPlayer arenaPlayer = ArenaPlayer.getPlayerInstance(name);
        assertNull(arenaPlayer.getCurrentClass());
        arenaPlayer.setCurrentClass(clazz);
        assertSame(clazz, arenaPlayer.getCurrentClass());
    }
    
}
