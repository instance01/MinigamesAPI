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

/**
 * Similar to {@link java.util.function.BinaryOperator} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 * @param <T>
 *            the type of the operands and result of the operator
 */
@FunctionalInterface
public interface MgBinaryOperator<T> extends MgBiFunction<T, T, T>
{
    
    /**
     * Returns a {@link MgBinaryOperator} which returns the lesser of two elements according to the specified {@code Comparator}.
     *
     * @param <T>
     *            the type of the input arguments of the comparator
     * @param comparator
     *            a {@code Comparator} for comparing the two values
     * @return a {@code MgBinaryOperator} which returns the lesser of its operands, according to the supplied {@code Comparator}
     * @throws NullPointerException
     *             if the argument is null
     */
    public static <T> MgBinaryOperator<T> minBy(MgComparator<? super T> comparator)
    {
        Objects.requireNonNull(comparator);
        return (arg1, arg2) -> comparator.compare(arg1, arg2) <= 0 ? arg1 : arg2;
    }
    
    /**
     * Returns a {@link MgBinaryOperator} which returns the greater of two elements according to the specified {@code Comparator}.
     *
     * @param <T>
     *            the type of the input arguments of the comparator
     * @param comparator
     *            a {@code Comparator} for comparing the two values
     * @return a {@code MgBinaryOperator} which returns the greater of its operands, according to the supplied {@code Comparator}
     * @throws NullPointerException
     *             if the argument is null
     */
    public static <T> MgBinaryOperator<T> maxBy(MgComparator<? super T> comparator)
    {
        Objects.requireNonNull(comparator);
        return (arg1, arg2) -> comparator.compare(arg1, arg2) >= 0 ? arg1 : arg2;
    }
    
}
