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
package com.comze_instancelabs.minigamesapi.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Modified by:
 *
 * @author instancelabs
 *
 *         Original version by:
 * @author Pandemoneus - https://github.com/Pandemoneus
 */
public final class Cuboid
{
    private final Location highPoints;
    private final Location lowPoints;
    
    /**
     * Constructs a new cuboid.
     * 
     * @param startLoc
     *            the first point
     * @param endLoc
     *            the second point
     */
    public Cuboid(final Location startLoc, final Location endLoc)
    {
        
        if (startLoc != null && endLoc != null)
        {
            final int lowx = Math.min(startLoc.getBlockX(), endLoc.getBlockX());
            final int lowy = Math.min(startLoc.getBlockY(), endLoc.getBlockY());
            final int lowz = Math.min(startLoc.getBlockZ(), endLoc.getBlockZ());
            
            final int highx = Math.max(startLoc.getBlockX(), endLoc.getBlockX());
            final int highy = Math.max(startLoc.getBlockY(), endLoc.getBlockY());
            final int highz = Math.max(startLoc.getBlockZ(), endLoc.getBlockZ());
            
            this.highPoints = new Location(startLoc.getWorld(), highx, highy, highz);
            this.lowPoints = new Location(startLoc.getWorld(), lowx, lowy, lowz);
        }
        else
        {
            this.highPoints = null;
            this.lowPoints = null;
        }
        
    }
    
    /**
     * Determines whether the passed area is within this area.
     * 
     * @param area
     *            the area to check
     * @return true if the area is within this area, otherwise false
     */
    public boolean isAreaWithinArea(final Cuboid area)
    {
        return (this.containsLoc(area.highPoints) && this.containsLoc(area.lowPoints));
    }
    
    /**
     * Determines whether the this cuboid contains the passed location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid, otherwise false
     */
    public boolean containsLoc(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || loc.getWorld() == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() && this.highPoints.getBlockX() >= loc.getBlockX() && this.lowPoints.getBlockZ() <= loc.getBlockZ()
                && this.highPoints.getBlockZ() >= loc.getBlockZ() && this.lowPoints.getBlockY() <= loc.getBlockY() && this.highPoints.getBlockY() >= loc.getBlockY();
    }
    
    public boolean containsLocWithoutY(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || loc.getWorld() == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() && this.highPoints.getBlockX() >= loc.getBlockX() && this.lowPoints.getBlockZ() <= loc.getBlockZ()
                && this.highPoints.getBlockZ() >= loc.getBlockZ();
    }
    
    public boolean containsLocWithoutYD(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || loc.getWorld() == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() + 2 && this.highPoints.getBlockX() >= loc.getBlockX() - 2 && this.lowPoints.getBlockZ() <= loc.getBlockZ() + 2
                && this.highPoints.getBlockZ() >= loc.getBlockZ() - 2;
    }
    
    /**
     * Returns the volume of this cuboid.
     * 
     * @return the volume of this cuboid
     */
    public long getSize()
    {
        return Math.abs(this.getXSize() * this.getYSize() * this.getZSize());
    }
    
    /**
     * Determines a random location inside the cuboid and returns it.
     * 
     * @return a random location within the cuboid
     */
    public Location getRandomLocation()
    {
        final World world = this.getWorld();
        final Random randomGenerator = new Random();
        
        Location result = new Location(world, this.highPoints.getBlockX(), this.highPoints.getBlockY(), this.highPoints.getZ());
        
        if (this.getSize() > 1)
        {
            final double randomX = this.lowPoints.getBlockX() + randomGenerator.nextInt(this.getXSize());
            final double randomY = this.lowPoints.getBlockY() + randomGenerator.nextInt(this.getYSize());
            final double randomZ = this.lowPoints.getBlockZ() + randomGenerator.nextInt(this.getZSize());
            
            result = new Location(world, randomX, randomY, randomZ);
        }
        
        return result;
    }
    
    /**
     * Determines a random location inside the cuboid that is suitable for mob spawning and returns it.
     * 
     * @return a random location inside the cuboid that is suitable for mob spawning
     */
    public Location getRandomLocationForMobs()
    {
        final Location temp = this.getRandomLocation();
        
        return new Location(temp.getWorld(), temp.getBlockX() + 0.5, temp.getBlockY() + 0.5, temp.getBlockZ() + 0.5);
    }
    
    /**
     * Returns the x span of this cuboid.
     * 
     * @return the x span of this cuboid
     */
    public int getXSize()
    {
        return (this.highPoints.getBlockX() - this.lowPoints.getBlockX()) + 1;
    }
    
    /**
     * Returns the y span of this cuboid.
     * 
     * @return the y span of this cuboid
     */
    public int getYSize()
    {
        return (this.highPoints.getBlockY() - this.lowPoints.getBlockY()) + 1;
    }
    
    /**
     * Returns the z span of this cuboid.
     * 
     * @return the z span of this cuboid
     */
    public int getZSize()
    {
        return (this.highPoints.getBlockZ() - this.lowPoints.getBlockZ()) + 1;
    }
    
    /**
     * Returns the higher location of this cuboid.
     * 
     * @return the higher location of this cuboid
     */
    public Location getHighLoc()
    {
        return this.highPoints;
    }
    
    /**
     * Returns the lower location of this cuboid.
     * 
     * @return the lower location of this cuboid
     */
    public Location getLowLoc()
    {
        return this.lowPoints;
    }
    
    /**
     * Returns the world this cuboid is in.
     * 
     * @return the world this cuboid is in
     */
    public World getWorld()
    {
        return this.highPoints.getWorld();
    }
    
    /**
     * Saves the cuboid to a Map.
     * 
     * @return the cuboid in a Map
     */
    public Map<String, Object> save()
    {
        final Map<String, Object> root = new LinkedHashMap<>();
        
        root.put("World", this.highPoints.getWorld().getName());
        root.put("X1", this.highPoints.getBlockX());
        root.put("Y1", this.highPoints.getBlockY());
        root.put("Z1", this.highPoints.getBlockZ());
        root.put("X2", this.lowPoints.getBlockX());
        root.put("Y2", this.lowPoints.getBlockY());
        root.put("Z2", this.lowPoints.getBlockZ());
        
        return root;
    }
    
    /**
     * Loads the cuboid from a Map.
     * 
     * @param root
     *            the Map
     * @return the cuboid
     * @throws IllegalArgumentException
     */
    public static Cuboid load(final Map<String, Object> root) throws IllegalArgumentException
    {
        if (root == null)
        {
            throw new IllegalArgumentException("Invalid root map!");
        }
        
        final String owner = (String) root.get("Owner");
        final World world = Bukkit.getServer().getWorld((String) root.get("World"));
        final int x1 = (Integer) root.get("X1");
        final int y1 = (Integer) root.get("Y1");
        final int z1 = (Integer) root.get("Z1");
        final int x2 = (Integer) root.get("X2");
        final int y2 = (Integer) root.get("Y2");
        final int z2 = (Integer) root.get("Z2");
        
        final Cuboid newArea = new Cuboid(new Location(world, x1, y1, z1), new Location(world, x2, y2, z2));
        
        return newArea;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new StringBuilder("(").append(this.lowPoints.getBlockX()).append(", ").append(this.lowPoints.getBlockY()).append(", ").append(this.lowPoints.getBlockZ()).append(") to (")
                .append(this.highPoints.getBlockX()).append(", ").append(this.highPoints.getBlockY()).append(", ").append(this.highPoints.getBlockZ()).append(")").toString();
    }
    
    /**
     * Returns a raw representation that is easy to read for Java.
     * 
     * @return a raw representation of this cuboid
     */
    public String toRaw()
    {
        return new StringBuilder(this.getWorld().getName()).append(",").append(this.lowPoints.getBlockX()).append(",").append(this.lowPoints.getBlockY()).append(",").append(this.lowPoints.getBlockZ())
                .append(",").append(this.highPoints.getBlockX()).append(",").append(this.highPoints.getBlockY()).append(",").append(this.highPoints.getBlockZ()).toString();
    }
}
