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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.bukkit.Location;
import org.junit.Test;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.testutil.TestUtil;

/**
 * The tester for the arena class.
 * 
 * @author mepeisen
 * 
 * @see Arena
 */
public class ArenaTest extends TestUtil
{
    
    /** the default arena name. */
    private static final String ARENA = "junit-arena"; //$NON-NLS-1$
    
    /** the junit minigame. */
    private static final String MINIGAME = "$JUNIT-MINIGAME"; //$NON-NLS-1$
    
    /**
     * Tests the arena class constructor.
     */
    @Test
    public void testConstructor()
    {
        final Minigame minigame = this.setupMinigame(MINIGAME);
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        
        assertSame(arena.getPlugin(), minigame.javaPlugin);
        assertSame(arena.getPluginInstance(), minigame.pluginInstance);
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getName());
        assertEquals(ArenaType.DEFAULT, arena.getArenaType());
    }
    
    /**
     * Tests the arena class constructor.
     */
    @Test
    public void testConstructorType()
    {
        final Minigame minigame = this.setupMinigame(MINIGAME);
        final Arena arena = new Arena(minigame.javaPlugin, ARENA, ArenaType.JUMPNRUN);
        
        assertSame(arena.getPlugin(), minigame.javaPlugin);
        assertSame(arena.getPluginInstance(), minigame.pluginInstance);
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getName());
        assertEquals(ArenaType.JUMPNRUN, arena.getArenaType());
    }
    
    /**
     * Tests the init method call.
     */
    @Test
    public void testInit()
    {
        final Minigame minigame = this.setupMinigame(MINIGAME);
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        
        final Location signLoc = new Location(null, 1, 1, 1);
        
        final ArrayList<Location> spawns = new ArrayList<>();
        spawns.add(new Location(null, 2, 2, 2));
        spawns.add(new Location(null, 3, 3, 3));
        
        final Location mainlobby = new Location(null, 4, 4, 4);
        
        final Location waitinglobby = new Location(null, 5, 5, 5);
        
        arena.init(signLoc, spawns, mainlobby, waitinglobby, 10, 2, false);
        
        assertEquals(1, arena.getSignLocation().getX(), 0);
        assertEquals(1, arena.getSignLocation().getY(), 0);
        assertEquals(1, arena.getSignLocation().getZ(), 0);
        
        assertEquals(2, arena.getSpawns().size());
        assertEquals(2, arena.getSpawns().get(0).getX(), 0);
        assertEquals(2, arena.getSpawns().get(0).getY(), 0);
        assertEquals(2, arena.getSpawns().get(0).getZ(), 0);
        assertEquals(3, arena.getSpawns().get(1).getX(), 0);
        assertEquals(3, arena.getSpawns().get(1).getY(), 0);
        assertEquals(3, arena.getSpawns().get(1).getZ(), 0);
        
        assertEquals(4, arena.getMainLobby().getX(), 0);
        assertEquals(4, arena.getMainLobby().getY(), 0);
        assertEquals(4, arena.getMainLobby().getZ(), 0);
        
        assertEquals(5, arena.getWaitingLobby().getX(), 0);
        assertEquals(5, arena.getWaitingLobby().getY(), 0);
        assertEquals(5, arena.getWaitingLobby().getZ(), 0);
        
        assertFalse(arena.isVIPArena());
        assertEquals(2, arena.getMinPlayers());
        assertEquals(10, arena.getMaxPlayers());
    }
    
}
