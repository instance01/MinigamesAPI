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

package com.github.mce.minigames.api.util.function;

import com.github.mce.minigames.api.MinigameException;

/**
 * Similar to {@link java.util.function.DoubleSupplier} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 *
 */
@FunctionalInterface
public interface MgDoubleSupplier
{

    /**
     * Gets a result.
     *
     * @return a result
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    double getAsDouble() throws MinigameException;
    
}
