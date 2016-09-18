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

package com.github.mce.minigames.api.test.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.component.Cuboid;

/**
 * Tests {@link Cuboid}
 * 
 * @author mepeisen
 *
 */
public class CuboidTest
{
    
    /**
     * Tests constructors
     */
    @Test
    public void testConstructor()
    {
        final Cuboid cub1 = new Cuboid();
        assertNull(cub1.getLowLoc());
        assertNull(cub1.getHighLoc());
        
        final Cuboid cub2 = new Cuboid(new Location(null, 1, 1, 1), null);
        assertNull(cub2.getLowLoc());
        assertNull(cub2.getHighLoc());
        
        final Cuboid cub2b = new Cuboid(null, new Location(null, 1, 1, 1));
        assertNull(cub2b.getLowLoc());
        assertNull(cub2b.getHighLoc());
        
        final Cuboid cub3 = new Cuboid(new Location(null, 1, 1, 1), new Location(null, 2, 2, 2));
        assertNotNull(cub3.getLowLoc());
        assertNotNull(cub3.getHighLoc());
        assertEquals(new Location(null, 1, 1, 1), cub3.getLowLoc());
        assertEquals(new Location(null, 2, 2, 2), cub3.getHighLoc());
    }
    
    /**
     * Tests constructors
     */
    @Test
    public void testConstructorNormalization()
    {
        final Cuboid cub1 = new Cuboid(new Location(null, 1, 2, 2), new Location(null, 2, 1, 1));
        assertEquals(new Location(null, 1, 1, 1), cub1.getLowLoc());
        assertEquals(new Location(null, 2, 2, 2), cub1.getHighLoc());
        
        final Cuboid cub2 = new Cuboid(new Location(null, 2, 1, 2), new Location(null, 1, 2, 1));
        assertEquals(new Location(null, 1, 1, 1), cub2.getLowLoc());
        assertEquals(new Location(null, 2, 2, 2), cub2.getHighLoc());
    }
    
    /**
     * Tests {@link Cuboid#setLowLoc(Location)}
     */
    @Test
    public void testSetLowLoc()
    {
        final Cuboid cub1 = new Cuboid(new Location(null, 1, 1, 1), new Location(null, 2, 2, 2)).setLowLoc(new Location(null, 3, 3, 3));
        assertEquals(new Location(null, 2, 2, 2), cub1.getLowLoc());
        assertEquals(new Location(null, 3, 3, 3), cub1.getHighLoc());
        
        final Cuboid cub2 = new Cuboid().setLowLoc(new Location(null, 1, 1, 1));
        assertEquals(new Location(null, 1, 1, 1), cub2.getLowLoc());
        assertEquals(new Location(null, 1, 1, 1), cub2.getHighLoc());
    }
    
    /**
     * Tests {@link Cuboid#setHighLoc(Location)}
     */
    @Test
    public void testSetHighLoc()
    {
        final Cuboid cub1 = new Cuboid(new Location(null, 1, 1, 1), new Location(null, 2, 2, 2)).setHighLoc(new Location(null, 0, 0, 0));
        assertEquals(new Location(null, 0, 0, 0), cub1.getLowLoc());
        assertEquals(new Location(null, 1, 1, 1), cub1.getHighLoc());
        
        final Cuboid cub2 = new Cuboid().setHighLoc(new Location(null, 1, 1, 1));
        assertEquals(new Location(null, 1, 1, 1), cub2.getLowLoc());
        assertEquals(new Location(null, 1, 1, 1), cub2.getHighLoc());
    }
    
    /**
     * Tests {@link Cuboid#isAreaWithinArea(Cuboid)}
     */
    @Test
    public void testIsAreaWithinArea()
    {
        final World world = mock(World.class);
        final World world2 = mock(World.class);
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 5, 5));
        assertTrue(cub1.isAreaWithinArea(new Cuboid(new Location(world, 2, 2, 2), new Location(world, 3, 3, 3))));
        assertFalse(cub1.isAreaWithinArea(new Cuboid(new Location(world, 0, 2, 2), new Location(world, 3, 3, 3))));
        assertFalse(cub1.isAreaWithinArea(new Cuboid(new Location(world, 2, 2, 2), new Location(world, 8, 3, 3))));
        assertFalse(cub1.isAreaWithinArea(new Cuboid(new Location(world, 8, 2, 2), new Location(world, 8, 3, 3))));
        assertFalse(cub1.isAreaWithinArea(new Cuboid(new Location(world2, 2, 2, 2), new Location(world2, 3, 3, 3))));
        assertFalse(cub1.isAreaWithinArea(new Cuboid()));
    }
    
    /**
     * Tests {@link Cuboid#containsLoc(Location)}
     */
    @Test
    public void testContainsLoc()
    {
        final World world = mock(World.class);
        final World world2 = mock(World.class);
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 5, 5));
        assertTrue(cub1.containsLoc(new Location(world, 2, 2, 2)));
        assertFalse(cub1.containsLoc(new Location(world2, 2, 2, 2)));
        assertFalse(cub1.containsLoc(null));
        assertFalse(cub1.containsLoc(new Location(world, 0, 2, 2)));
        assertFalse(cub1.containsLoc(new Location(world, 2, 0, 2)));
        assertFalse(cub1.containsLoc(new Location(world, 2, 2, 0)));
        assertFalse(cub1.containsLoc(new Location(world, 6, 2, 2)));
        assertFalse(cub1.containsLoc(new Location(world, 2, 6, 2)));
        assertFalse(cub1.containsLoc(new Location(world, 2, 2, 6)));
        
        assertFalse(new Cuboid().containsLoc(new Location(world, 2, 2, 2)));
    }
    
    /**
     * Tests {@link Cuboid#containsLocWithoutY(Location)}
     */
    @Test
    public void testContainsLocWithoutY()
    {
        final World world = mock(World.class);
        final World world2 = mock(World.class);
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 5, 5));
        assertTrue(cub1.containsLocWithoutY(new Location(world, 2, 2, 2)));
        assertFalse(cub1.containsLocWithoutY(new Location(world2, 2, 2, 2)));
        assertFalse(cub1.containsLocWithoutY(null));
        assertFalse(cub1.containsLocWithoutY(new Location(world, 0, 2, 2)));
        assertTrue(cub1.containsLocWithoutY(new Location(world, 2, 0, 2)));
        assertFalse(cub1.containsLocWithoutY(new Location(world, 2, 2, 0)));
        assertFalse(cub1.containsLocWithoutY(new Location(world, 6, 2, 2)));
        assertTrue(cub1.containsLocWithoutY(new Location(world, 2, 6, 2)));
        assertFalse(cub1.containsLocWithoutY(new Location(world, 2, 2, 6)));
        
        assertFalse(new Cuboid().containsLocWithoutY(new Location(world, 2, 2, 2)));
    }
    
    /**
     * Tests {@link Cuboid#containsLocWithoutYD(Location)}
     */
    @Test
    public void containsLocWithoutYD()
    {
        final World world = mock(World.class);
        final World world2 = mock(World.class);
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 5, 5));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 2, 2)));
        assertFalse(cub1.containsLocWithoutYD(new Location(world2, 2, 2, 2)));
        assertFalse(cub1.containsLocWithoutYD(null));
        
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 0, 2, 2)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, -1, 2, 2)));
        assertFalse(cub1.containsLocWithoutYD(new Location(world, -2, 2, 2)));
        
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 0, 2)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, -1, 2)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, -2, 2)));
        
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 2, 0)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 2, -1)));
        assertFalse(cub1.containsLocWithoutYD(new Location(world, 2, 2, -2)));
        
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 6, 2, 2)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 7, 2, 2)));
        assertFalse(cub1.containsLocWithoutYD(new Location(world, 8, 2, 2)));
        
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 6, 2)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 7, 2)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 8, 2)));
        
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 2, 6)));
        assertTrue(cub1.containsLocWithoutYD(new Location(world, 2, 2, 7)));
        assertFalse(cub1.containsLocWithoutYD(new Location(world, 2, 2, 8)));
        
        assertFalse(new Cuboid().containsLocWithoutYD(new Location(world, 2, 2, 2)));
    }
    
    /**
     * Tests sizes
     */
    @Test
    public void testSize()
    {
        final Cuboid cub1 = new Cuboid(new Location(null, 1, 1, 1), new Location(null, 5, 4, 3));
        assertEquals(60, cub1.getSize());
        assertEquals(5, cub1.getXSize());
        assertEquals(4, cub1.getYSize());
        assertEquals(3, cub1.getZSize());
        
        final Cuboid cub2 = new Cuboid(new Location(null, -1, -1, -1), new Location(null, -5, -4, -3));
        assertEquals(60, cub2.getSize());
        assertEquals(5, cub2.getXSize());
        assertEquals(4, cub2.getYSize());
        
        final Cuboid cub3 = new Cuboid(new Location(null, -1, -1, -1), new Location(null, 1, 1, 1));
        assertEquals(27, cub3.getSize());
        assertEquals(3, cub3.getXSize());
        assertEquals(3, cub3.getYSize());
        assertEquals(3, cub3.getZSize());
    }
    
    /**
     * Tests  {@link Cuboid#getWorld()}
     */
    @Test
    public void testGetWorld()
    {
        final World world = mock(World.class);
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 4, 3));
        assertEquals(world, cub1.getWorld());
        assertNull(new Cuboid().getWorld());
    }
    
    /**
     * Tests  {@link Cuboid#toRaw()}
     */
    @Test
    public void testToRaw()
    {
        final World world = mock(World.class);
        when(world.getName()).thenReturn("foo"); //$NON-NLS-1$
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 4, 3));
        final Cuboid cub2 = new Cuboid(new Location(null, 1, 1, 1), new Location(null, 5, 4, 3));

        assertEquals("null", new Cuboid().toRaw()); //$NON-NLS-1$
        assertEquals("foo,1,1,1,5,4,3", cub1.toRaw()); //$NON-NLS-1$
        assertEquals("null,1,1,1,5,4,3", cub2.toRaw()); //$NON-NLS-1$
    }
    
    /**
     * Tests  {@link Cuboid#toString()}
     */
    @Test
    public void testToString()
    {
        final World world = mock(World.class);
        when(world.getName()).thenReturn("foo"); //$NON-NLS-1$
        final Cuboid cub1 = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 4, 3));
        final Cuboid cub2 = new Cuboid(new Location(null, 1, 1, 1), new Location(null, 5, 4, 3));

        assertEquals("(null) to (null)", new Cuboid().toString()); //$NON-NLS-1$
        assertEquals("(1, 1, 1) to (5, 4, 3)", cub1.toString()); //$NON-NLS-1$
        assertEquals("(1, 1, 1) to (5, 4, 3)", cub2.toString()); //$NON-NLS-1$
    }
    
    /**
     * Tests  {@link Cuboid#getRandomLocation()}
     */
    @Test
    public void testGetRandomLocation()
    {
        final World world = mock(World.class);
        final Cuboid cub = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 4, 3));
        for (int i = 1; i < 100; i++)
        {
            assertTrue(cub.containsLoc(cub.getRandomLocation()));
        }
    }
    
    /**
     * Tests  {@link Cuboid#getRandomLocation()}
     */
    @Test
    public void testGetRandomLocation1()
    {
        final World world = mock(World.class);
        final Cuboid cub = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 1, 1, 1));
        for (int i = 1; i < 100; i++)
        {
            assertTrue(cub.containsLoc(cub.getRandomLocation()));
        }
    }
    
    /**
     * Tests  {@link Cuboid#getRandomLocationForMobs()}
     */
    @Test
    public void testGetRandomLocationForMobs()
    {
        final World world = mock(World.class);
        final Cuboid cub = new Cuboid(new Location(world, 1, 1, 1), new Location(world, 5, 4, 3));
        for (int i = 1; i < 100; i++)
        {
            assertTrue(cub.containsLoc(cub.getRandomLocationForMobs().subtract(0.5,  0.5, 0.5)));
        }
    }
    
    /**
     * Tests  {@link Cuboid#readFromConfig(org.bukkit.configuration.ConfigurationSection)} and {@link Cuboid#writeToConfig(org.bukkit.configuration.ConfigurationSection)}
     */
    @Test
    public void testConfigurable()
    {
        final World world = mock(World.class);
        when(world.getName()).thenReturn("foo"); //$NON-NLS-1$
        final Cuboid cub = new Cuboid(new Location(world, 1, 2, 3), new Location(world, 5, 6, 7));
        final ConfigurationSection section = new MemoryConfiguration();
        
        cub.writeToConfig(section);
        
        assertEquals("foo", section.getString("World")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, section.getInt("X1")); //$NON-NLS-1$
        assertEquals(2, section.getInt("Y1")); //$NON-NLS-1$
        assertEquals(3, section.getInt("Z1")); //$NON-NLS-1$
        assertEquals(5, section.getInt("X2")); //$NON-NLS-1$
        assertEquals(6, section.getInt("Y2")); //$NON-NLS-1$
        assertEquals(7, section.getInt("Z2")); //$NON-NLS-1$
        
        final Server server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", server); //$NON-NLS-1$
        when(server.getWorld("foo")).thenReturn(world); //$NON-NLS-1$
        
        final Cuboid cub2 = new Cuboid();
        cub2.readFromConfig(section);
        
        assertEquals("foo", cub2.getWorld().getName()); //$NON-NLS-1$
        assertEquals(1, cub2.getLowLoc().getBlockX());
        assertEquals(2, cub2.getLowLoc().getBlockY());
        assertEquals(3, cub2.getLowLoc().getBlockZ());
        assertEquals(5, cub2.getHighLoc().getBlockX());
        assertEquals(6, cub2.getHighLoc().getBlockY());
        assertEquals(7, cub2.getHighLoc().getBlockZ());
    }
    
    /**
     * Tests configurable (invalid)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConfigurableInvalid()
    {
        new Cuboid().readFromConfig(new MemoryConfiguration());
    }
    
}
