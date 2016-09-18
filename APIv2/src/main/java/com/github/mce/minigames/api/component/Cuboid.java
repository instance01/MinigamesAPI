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
package com.github.mce.minigames.api.component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.github.mce.minigames.api.config.Configurable;
import com.github.mce.minigames.api.config.ConfigurationValueInterface;

/**
 * Modified by:
 *
 * @author instancelabs
 *
 *         Original version by:
 * @author Pandemoneus - https://github.com/Pandemoneus
 */
public final class Cuboid implements Configurable
{
    /** high points. */
    private Location highPoints;
    /** low points. */
    private Location lowPoints;
    
    /**
     * Constructor for a null Cuboid; used by {@link ConfigurationValueInterface#getObject()}.
     */
    public Cuboid()
    {
        this.highPoints = null;
        this.lowPoints = null;
    }
    
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
     * Returns a new cuboid with given low locations.
     * 
     * @param lowPoints
     *            the new low location
     * @return new cuboid
     */
    public Cuboid setLowLoc(Location lowPoints)
    {
        return new Cuboid(lowPoints, this.highPoints == null ? lowPoints : this.highPoints);
    }
    
    /**
     * Returns a new cuboid with given high location.
     * 
     * @param highPoints
     *            the new high location
     * @return new cuboid
     */
    public Cuboid setHighLoc(Location highPoints)
    {
        return new Cuboid(this.lowPoints == null ? highPoints : this.lowPoints, highPoints);
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
        if (loc == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() && this.highPoints.getBlockX() >= loc.getBlockX() && this.lowPoints.getBlockZ() <= loc.getBlockZ()
                && this.highPoints.getBlockZ() >= loc.getBlockZ() && this.lowPoints.getBlockY() <= loc.getBlockY() && this.highPoints.getBlockY() >= loc.getBlockY();
    }
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    public boolean containsLocWithoutY(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() && this.highPoints.getBlockX() >= loc.getBlockX() && this.lowPoints.getBlockZ() <= loc.getBlockZ()
                && this.highPoints.getBlockZ() >= loc.getBlockZ();
    }
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord and by including the 2 blocks beyond the location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    public boolean containsLocWithoutYD(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || !loc.getWorld().equals(this.highPoints.getWorld()))
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
        
        Location result;
        
        if (!this.lowPoints.equals(this.highPoints))
        {
            final double randomX = this.lowPoints.getBlockX() + randomGenerator.nextInt(this.getXSize());
            final double randomY = this.lowPoints.getBlockY() + randomGenerator.nextInt(this.getYSize());
            final double randomZ = this.lowPoints.getBlockZ() + randomGenerator.nextInt(this.getZSize());
            
            result = new Location(world, randomX, randomY, randomZ);
        }
        else
        {
            result = this.highPoints.clone();
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
        
        return temp.add(0.5, 0.5, 0.5);
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
        return this.highPoints == null ? null : this.highPoints.getWorld();
    }
    
    /**
     * Saves the cuboid to a Map.
     * 
     * @return the cuboid in a Map
     */
    private Map<String, Object> save()
    {
        final Map<String, Object> root = new LinkedHashMap<>();
        
        root.put("World", this.highPoints.getWorld().getName()); //$NON-NLS-1$
        root.put("X1", this.lowPoints.getBlockX()); //$NON-NLS-1$
        root.put("Y1", this.lowPoints.getBlockY()); //$NON-NLS-1$
        root.put("Z1", this.lowPoints.getBlockZ()); //$NON-NLS-1$
        root.put("X2", this.highPoints.getBlockX()); //$NON-NLS-1$
        root.put("Y2", this.highPoints.getBlockY()); //$NON-NLS-1$
        root.put("Z2", this.highPoints.getBlockZ()); //$NON-NLS-1$
        
        return root;
    }
    
    /**
     * Loads the cuboid from a Map.
     * 
     * @param root
     *            the Map
     * @throws IllegalArgumentException
     */
    private void load(final Map<String, Object> root) throws IllegalArgumentException
    {
        try
        {
            final World world = Bukkit.getServer().getWorld((String) root.get("World")); //$NON-NLS-1$
            final int x1 = (Integer) root.get("X1"); //$NON-NLS-1$
            final int y1 = (Integer) root.get("Y1"); //$NON-NLS-1$
            final int z1 = (Integer) root.get("Z1"); //$NON-NLS-1$
            final int x2 = (Integer) root.get("X2"); //$NON-NLS-1$
            final int y2 = (Integer) root.get("Y2"); //$NON-NLS-1$
            final int z2 = (Integer) root.get("Z2"); //$NON-NLS-1$
            
            this.lowPoints = new Location(world, x1, y1, z1);
            this.highPoints = new Location(world, x2, y2, z2);
        }
        catch (NullPointerException | ClassCastException ex)
        {
            throw new IllegalArgumentException("Invalid root map!", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public void readFromConfig(ConfigurationSection section)
    {
        this.load(section.getValues(false));
    }
    
    @Override
    public void writeToConfig(ConfigurationSection section)
    {
        for (final Map.Entry<String, Object> entry : this.save().entrySet())
        {
            section.set(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return this.lowPoints == null ? "(null) to (null)" //$NON-NLS-1$
                : new StringBuilder("(").append(this.lowPoints.getBlockX()).append(", ").append(this.lowPoints.getBlockY()).append(", ").append(this.lowPoints.getBlockZ()).append(") to (") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        .append(this.highPoints.getBlockX()).append(", ").append(this.highPoints.getBlockY()).append(", ").append(this.highPoints.getBlockZ()).append(")").toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Returns a raw representation that is easy to read for Java.
     * 
     * @return a raw representation of this cuboid
     */
    public String toRaw()
    {
        if (this.lowPoints == null)
            return "null"; //$NON-NLS-1$
        return new StringBuilder(this.getWorld() == null ? "null" : this.getWorld().getName()).append(",").append(this.lowPoints.getBlockX()).append(",").append(this.lowPoints.getBlockY()).append(",") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                .append(this.lowPoints.getBlockZ())
                .append(",").append(this.highPoints.getBlockX()).append(",").append(this.highPoints.getBlockY()).append(",").append(this.highPoints.getBlockZ()).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
}
