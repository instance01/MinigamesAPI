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
public final class Cuboid {
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
	public Cuboid(Location startLoc, Location endLoc) {

		if (startLoc != null && endLoc != null) {
			final int lowx = Math.min(startLoc.getBlockX(), endLoc.getBlockX());
			final int lowy = Math.min(startLoc.getBlockY(), endLoc.getBlockY());
			final int lowz = Math.min(startLoc.getBlockZ(), endLoc.getBlockZ());

			final int highx = Math.max(startLoc.getBlockX(), endLoc.getBlockX());
			final int highy = Math.max(startLoc.getBlockY(), endLoc.getBlockY());
			final int highz = Math.max(startLoc.getBlockZ(), endLoc.getBlockZ());

			highPoints = new Location(startLoc.getWorld(), highx, highy, highz);
			lowPoints = new Location(startLoc.getWorld(), lowx, lowy, lowz);
		} else {
			highPoints = null;
			lowPoints = null;
		}

	}

	/**
	 * Determines whether the passed area is within this area.
	 * 
	 * @param area
	 *            the area to check
	 * @return true if the area is within this area, otherwise false
	 */
	public boolean isAreaWithinArea(Cuboid area) {
		return (containsLoc(area.highPoints) && containsLoc(area.lowPoints));
	}

	/**
	 * Determines whether the this cuboid contains the passed location.
	 * 
	 * @param loc
	 *            the location to check
	 * @return true if the location is within this cuboid, otherwise false
	 */
	public boolean containsLoc(Location loc) {
		if (loc == null || !loc.getWorld().equals(highPoints.getWorld())) {
			return false;
		}

		return lowPoints.getBlockX() <= loc.getBlockX() && highPoints.getBlockX() >= loc.getBlockX() && lowPoints.getBlockZ() <= loc.getBlockZ() && highPoints.getBlockZ() >= loc.getBlockZ() && lowPoints.getBlockY() <= loc.getBlockY() && highPoints.getBlockY() >= loc.getBlockY();
	}

	public boolean containsLocWithoutY(Location loc) {
		if (highPoints == null || lowPoints == null) {
			return false;
		}
		if (loc == null || !loc.getWorld().equals(highPoints.getWorld())) {
			return false;
		}

		return lowPoints.getBlockX() <= loc.getBlockX() && highPoints.getBlockX() >= loc.getBlockX() && lowPoints.getBlockZ() <= loc.getBlockZ() && highPoints.getBlockZ() >= loc.getBlockZ();
	}

	/**
	 * Returns the volume of this cuboid.
	 * 
	 * @return the volume of this cuboid
	 */
	public long getSize() {
		return Math.abs(getXSize() * getYSize() * getZSize());
	}

	/**
	 * Determines a random location inside the cuboid and returns it.
	 * 
	 * @return a random location within the cuboid
	 */
	public Location getRandomLocation() {
		final World world = getWorld();
		final Random randomGenerator = new Random();

		Location result = new Location(world, highPoints.getBlockX(), highPoints.getBlockY(), highPoints.getZ());

		if (getSize() > 1) {
			final double randomX = lowPoints.getBlockX() + randomGenerator.nextInt(getXSize());
			final double randomY = lowPoints.getBlockY() + randomGenerator.nextInt(getYSize());
			final double randomZ = lowPoints.getBlockZ() + randomGenerator.nextInt(getZSize());

			result = new Location(world, randomX, randomY, randomZ);
		}

		return result;
	}

	/**
	 * Determines a random location inside the cuboid that is suitable for mob spawning and returns it.
	 * 
	 * @return a random location inside the cuboid that is suitable for mob spawning
	 */
	public Location getRandomLocationForMobs() {
		final Location temp = getRandomLocation();

		return new Location(temp.getWorld(), temp.getBlockX() + 0.5, temp.getBlockY() + 0.5, temp.getBlockZ() + 0.5);
	}

	/**
	 * Returns the x span of this cuboid.
	 * 
	 * @return the x span of this cuboid
	 */
	public int getXSize() {
		return (highPoints.getBlockX() - lowPoints.getBlockX()) + 1;
	}

	/**
	 * Returns the y span of this cuboid.
	 * 
	 * @return the y span of this cuboid
	 */
	public int getYSize() {
		return (highPoints.getBlockY() - lowPoints.getBlockY()) + 1;
	}

	/**
	 * Returns the z span of this cuboid.
	 * 
	 * @return the z span of this cuboid
	 */
	public int getZSize() {
		return (highPoints.getBlockZ() - lowPoints.getBlockZ()) + 1;
	}

	/**
	 * Returns the higher location of this cuboid.
	 * 
	 * @return the higher location of this cuboid
	 */
	public Location getHighLoc() {
		return highPoints;
	}

	/**
	 * Returns the lower location of this cuboid.
	 * 
	 * @return the lower location of this cuboid
	 */
	public Location getLowLoc() {
		return lowPoints;
	}

	/**
	 * Returns the world this cuboid is in.
	 * 
	 * @return the world this cuboid is in
	 */
	public World getWorld() {
		return highPoints.getWorld();
	}

	/**
	 * Saves the cuboid to a Map.
	 * 
	 * @return the cuboid in a Map
	 */
	public Map<String, Object> save() {
		Map<String, Object> root = new LinkedHashMap<String, Object>();

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
	 * @param world
	 *            the world it should be in
	 * @return the cuboid
	 * @throws IllegalArgumentException
	 */
	public static Cuboid load(Map<String, Object> root) throws IllegalArgumentException {
		if (root == null) {
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
	public String toString() {
		return new StringBuilder("(").append(lowPoints.getBlockX()).append(", ").append(lowPoints.getBlockY()).append(", ").append(lowPoints.getBlockZ()).append(") to (").append(highPoints.getBlockX()).append(", ").append(highPoints.getBlockY()).append(", ").append(highPoints.getBlockZ()).append(")").toString();
	}

	/**
	 * Returns a raw representation that is easy to read for Java.
	 * 
	 * @return a raw representation of this cuboid
	 */
	public String toRaw() {
		return new StringBuilder(getWorld().getName()).append(",").append(lowPoints.getBlockX()).append(",").append(lowPoints.getBlockY()).append(",").append(lowPoints.getBlockZ()).append(",").append(highPoints.getBlockX()).append(",").append(highPoints.getBlockY()).append(",").append(highPoints.getBlockZ()).toString();
	}
}