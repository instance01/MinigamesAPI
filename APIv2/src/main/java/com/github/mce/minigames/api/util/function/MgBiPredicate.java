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
 * Similar to {@link java.util.function.BiPredicate} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 *
 * @param <T>
 *            the type of the first argument to the predicate
 * @param <U>
 *            the type of the second argument the predicate
 */
@FunctionalInterface
public interface MgBiPredicate<T, U>
{
    
    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param arg1
     *            the first input argument
     * @param arg2
     *            the second input argument
     * @return {@code true} if the input arguments match the predicate, otherwise {@code false}
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    boolean test(T arg1, U arg2) throws MinigameException;
    
    /**
     * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another. When evaluating the composed predicate, if this predicate is {@code false}, then the
     * {@code other} predicate is not evaluated.
     *
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated.
     * </p>
     *
     * @param other
     *            a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the {@code other} predicate
     * @throws NullPointerException
     *             if other is null
     */
    default MgBiPredicate<T, U> and(MgBiPredicate<? super T, ? super U> other)
    {
        Objects.requireNonNull(other);
        return (T arg1, U arg2) -> test(arg1, arg2) && other.test(arg1, arg2);
    }
    
    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default MgBiPredicate<T, U> negate()
    {
        return (T arg1, U arg2) -> !test(arg1, arg2);
    }
    
    /**
     * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another. When evaluating the composed predicate, if this predicate is {@code true}, then the
     * {@code other} predicate is not evaluated.
     *
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated.
     * </p>
     *
     * @param other
     *            a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the {@code other} predicate
     * @throws NullPointerException
     *             if other is null
     */
    default MgBiPredicate<T, U> or(MgBiPredicate<? super T, ? super U> other)
    {
        Objects.requireNonNull(other);
        return (T arg1, U arg2) -> test(arg1, arg2) || other.test(arg1, arg2);
    }
}
