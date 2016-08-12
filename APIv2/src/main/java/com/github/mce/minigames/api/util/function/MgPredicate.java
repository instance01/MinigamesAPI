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
 * Similar to {@link java.util.function.Predicate} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 *
 * @param <T>
 *            the type of the input to the predicate
 */
@FunctionalInterface
public interface MgPredicate<T>
{
    
    /**
     * Evaluates this predicate on the given argument.
     *
     * @param arg
     *            the input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    boolean test(T arg) throws MinigameException;
    
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
    default MgPredicate<T> and(MgPredicate<? super T> other)
    {
        Objects.requireNonNull(other);
        return (arg) -> test(arg) && other.test(arg);
    }
    
    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default MgPredicate<T> negate()
    {
        return (arg) -> !test(arg);
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
    default MgPredicate<T> or(MgPredicate<? super T> other)
    {
        Objects.requireNonNull(other);
        return (arg) -> test(arg) || other.test(arg);
    }
    
    /**
     * Returns a predicate that tests if two arguments are equal according to {@link Objects#equals(Object, Object)}.
     *
     * @param <T>
     *            the type of arguments to the predicate
     * @param targetRef
     *            the object reference with which to compare for equality, which may be {@code null}
     * @return a predicate that tests if two arguments are equal according to {@link Objects#equals(Object, Object)}
     */
    static <T> MgPredicate<T> isEqual(Object targetRef)
    {
        return (null == targetRef) ? Objects::isNull : object -> targetRef.equals(object);
    }
    
}
