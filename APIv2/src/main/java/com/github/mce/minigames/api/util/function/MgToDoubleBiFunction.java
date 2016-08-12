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
 * Similar to {@link java.util.function.ToDoubleBiFunction} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 *
 */
@FunctionalInterface
public interface MgToDoubleBiFunction<T, U>
{

    /**
     * Applies this function to the given arguments.
     *
     * @param arg1 the first function argument
     * @param arg2 the second function argument
     * @return the function result
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    double applyAsDouble(T arg1, U arg2) throws MinigameException;
    
}
