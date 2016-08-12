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
 * Similar to {@link java.util.function.BiConsumer} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 * @param <T>
 *            the type of the first argument to the operation
 * @param <U>
 *            the type of the second argument to the operation
 */
@FunctionalInterface
public interface MgBiConsumer<T, U>
{
    
    /**
     * Performs this operation on the given arguments.
     *
     * @param arg1
     *            the first input argument
     * @param arg2
     *            the second input argument
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    void accept(T arg1, U arg2) throws MinigameException;
    
    /**
     * Returns a composed {@code BiConsumer} that performs, in sequence, this operation followed by the {@code after} operation. If performing either operation throws an exception, it is relayed to
     * the caller of the composed operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after
     *            the operation to perform after this operation
     * @return a composed {@code BiConsumer} that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException
     *             if {@code after} is null
     */
    default MgBiConsumer<T, U> andThen(MgBiConsumer<? super T, ? super U> after)
    {
        Objects.requireNonNull(after);
        
        return (arg1, arg2) -> {
            accept(arg1, arg2);
            after.accept(arg1, arg2);
        };
    }
    
}
