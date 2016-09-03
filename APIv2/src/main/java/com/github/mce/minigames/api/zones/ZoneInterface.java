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

package com.github.mce.minigames.api.zones;

import org.bukkit.Location;

import com.github.mce.minigames.api.component.Cuboid;

/**
 * A zone/ cuboid component.
 * 
 * @author mepeisen
 *
 */
public interface ZoneInterface
{

    /**
     * Returns the cuboid.
     * @return cuboid of this component.
     */
    Cuboid getCuboid();
    
    /**
     * Sets the cuboid
     * @param cub cuboid of the component.
     */
    void setCuboid(Cuboid cub);
    
    /**
     * Determines whether the this cuboid contains the passed location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid, otherwise false
     */
    boolean containsLoc(final Location loc);
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    boolean containsLocWithoutY(final Location loc);
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord and by including the 2 blocks beyond the location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    boolean containsLocWithoutYD(final Location loc);
    
}
