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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.bukkit.Location;
import org.junit.Test;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
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
    
    /** the default arena name. */
    private static final String ARENA2 = "junit-arena2"; //$NON-NLS-1$
    
    /** the default arena name. */
    private static final String ARENA3 = "junit-arena3"; //$NON-NLS-1$
    
    /** the junit minigame. */
    private static final String MINIGAME = "$JUNIT-ARENA-TEST"; //$NON-NLS-1$
    
    /**
     * Tests the arena class constructor.
     */
    @Test
    public void testConstructor()
    {
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "Constructor"); //$NON-NLS-1$
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        
        assertSame(arena.getPlugin(), minigame.javaPlugin);
        assertSame(arena.getPluginInstance(), minigame.pluginInstance);
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getName());
        assertEquals(ArenaType.DEFAULT, arena.getArenaType());
        
        assertSame(arena, arena.getArena());
    }
    
    /**
     * Tests the arena class constructor.
     */
    @Test
    public void testConstructorType()
    {
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "ConstructorType"); //$NON-NLS-1$
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
        this.initWorld("world"); //$NON-NLS-1$
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "Init", (mg) -> { //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, "lobby", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            mg.addArenaComponentToConfig(ARENA3, "spawns.spawn0", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.BOUNDS_LOW, "world", 1, 2, 3, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.BOUNDS_HIGH, "world", 2, 3, 4, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.LOBBY_BOUNDS_LOW, "world", 3, 4, 5, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.LOBBY_BOUNDS_HIGH, "world", 4, 5, 6, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.SPEC_BOUNDS_LOW, "world", 5, 6, 7, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.SPEC_BOUNDS_HIGH, "world", 6, 7, 8, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.SPEC_SPAWN, "world", 9, 10, 11, 80, 80); //$NON-NLS-1$
            
            mg.arenasYml.getConfigurationSection("arenas").getConfigurationSection(ARENA3).set("displayname", "FOO"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        
        // 1) normal init with unknown arena
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

        assertTrue(arena.isSuccessfullyInit());
        
        
        
        // 2) init without spawns, deprecated method (should be changed to init as soon as we remove the method)
        final Arena arena2 = new Arena(minigame.javaPlugin, ARENA2);
        arena2.initArena(signLoc, null, mainlobby, waitinglobby, 5, 4, true);
        
        assertEquals(1, arena2.getSignLocation().getX(), 0);
        assertEquals(1, arena2.getSignLocation().getY(), 0);
        assertEquals(1, arena2.getSignLocation().getZ(), 0);
        
        assertEquals(0, arena2.getSpawns().size());
        assertEquals(4, arena2.getMainLobby().getX(), 0);
        assertEquals(4, arena2.getMainLobby().getY(), 0);
        assertEquals(4, arena2.getMainLobby().getZ(), 0);
        
        assertEquals(5, arena2.getWaitingLobby().getX(), 0);
        assertEquals(5, arena2.getWaitingLobby().getY(), 0);
        assertEquals(5, arena2.getWaitingLobby().getZ(), 0);
        
        assertTrue(arena2.isVIPArena());
        assertEquals(4, arena2.getMinPlayers());
        assertEquals(5, arena2.getMaxPlayers());
        
        assertTrue(arena2.isSuccessfullyInit());
        
        
        
        // 3) init configured arena
        final Arena arena3 = new Arena(minigame.javaPlugin, ARENA3);
        arena3.init(signLoc, null, mainlobby, waitinglobby, 5, 4, true);
        assertEquals("FOO", arena3.getDisplayName()); //$NON-NLS-1$
        
        assertEquals(1, arena3.getBoundaries().getLowLoc().getX(), 0);
        assertEquals(2, arena3.getBoundaries().getLowLoc().getY(), 0);
        assertEquals(3, arena3.getBoundaries().getLowLoc().getZ(), 0);
        assertEquals(2, arena3.getBoundaries().getHighLoc().getX(), 0);
        assertEquals(3, arena3.getBoundaries().getHighLoc().getY(), 0);
        assertEquals(4, arena3.getBoundaries().getHighLoc().getZ(), 0);
        
        assertEquals(3, arena3.getLobbyBoundaries().getLowLoc().getX(), 0);
        assertEquals(4, arena3.getLobbyBoundaries().getLowLoc().getY(), 0);
        assertEquals(5, arena3.getLobbyBoundaries().getLowLoc().getZ(), 0);
        assertEquals(4, arena3.getLobbyBoundaries().getHighLoc().getX(), 0);
        assertEquals(5, arena3.getLobbyBoundaries().getHighLoc().getY(), 0);
        assertEquals(6, arena3.getLobbyBoundaries().getHighLoc().getZ(), 0);
        
        assertEquals(5, arena3.getSpecBoundaries().getLowLoc().getX(), 0);
        assertEquals(6, arena3.getSpecBoundaries().getLowLoc().getY(), 0);
        assertEquals(7, arena3.getSpecBoundaries().getLowLoc().getZ(), 0);
        assertEquals(6, arena3.getSpecBoundaries().getHighLoc().getX(), 0);
        assertEquals(7, arena3.getSpecBoundaries().getHighLoc().getY(), 0);
        assertEquals(8, arena3.getSpecBoundaries().getHighLoc().getZ(), 0);
    }
    
}
