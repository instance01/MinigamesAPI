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

import java.util.Objects;

import com.github.mce.minigames.api.MinigameException;

/**
 * Similar to {@link java.util.function.BiFunction} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 * @param <T>
 *            the type of the first argument to the function
 * @param <U>
 *            the type of the second argument to the function
 * @param <R>
 *            the type of the result of the function
 */
@FunctionalInterface
public interface MgBiFunction<T, U, R>
{
    
    /**
     * Applies this function to the given arguments.
     *
     * @param arg1
     *            the first function argument
     * @param arg2
     *            the second function argument
     * @return the function result
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    R apply(T arg1, U arg2) throws MinigameException;
    
    /**
     * Returns a composed function that first applies this function to its input, and then applies the {@code after} function to the result. If evaluation of either function throws an exception, it is
     * relayed to the caller of the composed function.
     *
     * @param <V>
     *            the type of output of the {@code after} function, and of the composed function
     * @param after
     *            the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException
     *             if after is null
     */
    default <V> MgBiFunction<T, U, V> andThen(MgFunction<? super R, ? extends V> after)
    {
        Objects.requireNonNull(after);
        return (T arg1, U arg2) -> after.apply(apply(arg1, arg2));
    }
    
}
