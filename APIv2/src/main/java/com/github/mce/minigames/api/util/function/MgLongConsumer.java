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
 * Similar to {@link java.util.function.LongConsumer} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 *
 */
@FunctionalInterface
public interface MgLongConsumer
{
    
    /**
     * Performs this operation on the given argument.
     *
     * @param value
     *            the input argument
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    void accept(long value) throws MinigameException;
    
    /**
     * Returns a composed {@code LongConsumer} that performs, in sequence, this operation followed by the {@code after} operation. If performing either operation throws an exception, it is relayed to
     * the caller of the composed operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after
     *            the operation to perform after this operation
     * @return a composed {@code LongConsumer} that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException
     *             if {@code after} is null
     */
    default MgLongConsumer andThen(MgLongConsumer after)
    {
        Objects.requireNonNull(after);
        return (long value) -> {
            accept(value);
            after.accept(value);
        };
    }
    
}
