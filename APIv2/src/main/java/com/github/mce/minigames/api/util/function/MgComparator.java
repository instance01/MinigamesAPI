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
 * Similar to {@link java.util.Comparator} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 * @param <T>
 *            the type of objects that may be compared by this comparator
 */
@FunctionalInterface
public interface MgComparator<T>
{
    
    /**
     * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * 
     * <p>
     * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i> is negative, zero or positive.
     * </p>
     * 
     * <p>
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies that <tt>compare(x, y)</tt> must throw an exception if and only if <tt>compare(y, x)</tt> throws an exception.)
     * </p>
     * 
     * <p>
     * The implementor must also ensure that the relation is transitive: <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies <tt>compare(x, z)&gt;0</tt>.
     * </p>
     * 
     * <p>
     * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt> implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all <tt>z</tt>.
     * </p>
     * 
     * <p>
     * It is generally the case, but <i>not</i> strictly required that <tt>(compare(x, y)==0) == (x.equals(y))</tt>. Generally speaking, any comparator that violates this condition should clearly
     * indicate this fact. The recommended language is "Note: this comparator imposes orderings that are inconsistent with equals."
     * </p>
     *
     * @param o1
     *            the first object to be compared.
     * @param o2
     *            the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * @throws NullPointerException
     *             if an argument is null and this comparator does not permit null arguments
     * @throws ClassCastException
     *             if the arguments' types prevent them from being compared by this comparator.
     * @throws MinigameException
     *             thrown on problems, f.e. networking errors.
     */
    int compare(T o1, T o2) throws MinigameException;
    
    /**
     * Returns a comparator that imposes the reverse ordering of this comparator.
     *
     * @return a comparator that imposes the reverse ordering of this comparator.
     * @since 1.8
     */
    default MgComparator<T> reversed()
    {
        return new MgReverseComparator<>(this);
    }
    
    /**
     * Reverse comparator.
     * 
     * @param <T>
     *            the type of objects that may be compared by this comparator
     */
    public static final class MgReverseComparator<T> implements MgComparator<T>
    {
        
        /**
         * The comparator specified in the static factory. This will never be null, as the static factory returns a ReverseComparator instance if its argument is null.
         */
        final MgComparator<T> cmp;
        
        /**
         * Constructor.
         * 
         * @param cmp
         *            comparator
         */
        MgReverseComparator(MgComparator<T> cmp)
        {
            // assert cmp != null;
            this.cmp = cmp;
        }
        
        @Override
        public int compare(T t1, T t2) throws MinigameException
        {
            return this.cmp.compare(t2, t1);
        }
        
        @Override
        public MgComparator<T> reversed()
        {
            return this.cmp;
        }
    }
    
    /**
     * Returns a lexicographic-order comparator with another comparator.
     * 
     * <p>
     * If this {@code Comparator} considers two elements equal, i.e. {@code compare(a, b) == 0}, {@code other} is used to determine the order.
     * </p>
     *
     * <p>
     * The returned comparator is serializable if the specified comparator is also serializable.
     * </p>
     *
     * @param other
     *            the other comparator to be used when this comparator compares two objects that are equal.
     * @return a lexicographic-order comparator composed of this and then the other comparator
     * @throws NullPointerException
     *             if the argument is null.
     * @since 1.8
     */
    default MgComparator<T> thenComparing(MgComparator<? super T> other)
    {
        Objects.requireNonNull(other);
        return (MgComparator<T>) (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }
    
    /**
     * Returns a lexicographic-order comparator with a function that extracts a key to be compared with the given {@code Comparator}.
     *
     * @param <U>
     *            the type of the sort key
     * @param keyExtractor
     *            the function used to extract the sort key
     * @param keyComparator
     *            the {@code Comparator} used to compare the sort key
     * @return a lexicographic-order comparator composed of this comparator and then comparing on the key extracted by the keyExtractor function
     * @throws NullPointerException
     *             if either argument is null.
     * @see #comparing(MgFunction, MgComparator)
     * @see #thenComparing(MgComparator)
     * @since 1.8
     */
    default <U> MgComparator<T> thenComparing(MgFunction<? super T, ? extends U> keyExtractor, MgComparator<? super U> keyComparator)
    {
        return thenComparing(comparing(keyExtractor, keyComparator));
    }
    
    /**
     * Returns a lexicographic-order comparator with a function that extracts a {@code Comparable} sort key.
     *
     * @param <U>
     *            the type of the {@link Comparable} sort key
     * @param keyExtractor
     *            the function used to extract the {@link Comparable} sort key
     * @return a lexicographic-order comparator composed of this and then the {@link Comparable} sort key.
     * @throws NullPointerException
     *             if the argument is null.
     * @see #comparing(MgFunction)
     * @see #thenComparing(MgComparator)
     * @since 1.8
     */
    default <U extends Comparable<? super U>> MgComparator<T> thenComparing(MgFunction<? super T, ? extends U> keyExtractor)
    {
        return thenComparing(comparing(keyExtractor));
    }
    
    /**
     * Returns a lexicographic-order comparator with a function that extracts a {@code int} sort key.
     *
     * @param keyExtractor
     *            the function used to extract the integer sort key
     * @return a lexicographic-order comparator composed of this and then the {@code int} sort key
     * @throws NullPointerException
     *             if the argument is null.
     * @see #comparingInt(MgToIntFunction)
     * @see #thenComparing(MgComparator)
     * @since 1.8
     */
    default MgComparator<T> thenComparingInt(MgToIntFunction<? super T> keyExtractor)
    {
        return thenComparing(comparingInt(keyExtractor));
    }
    
    /**
     * Returns a lexicographic-order comparator with a function that extracts a {@code long} sort key.
     *
     * @param keyExtractor
     *            the function used to extract the long sort key
     * @return a lexicographic-order comparator composed of this and then the {@code long} sort key
     * @throws NullPointerException
     *             if the argument is null.
     * @see #comparingLong(MgToLongFunction)
     * @see #thenComparing(MgComparator)
     * @since 1.8
     */
    default MgComparator<T> thenComparingLong(MgToLongFunction<? super T> keyExtractor)
    {
        return thenComparing(comparingLong(keyExtractor));
    }
    
    /**
     * Returns a lexicographic-order comparator with a function that extracts a {@code double} sort key.
     *
     * @param keyExtractor
     *            the function used to extract the double sort key
     * @return a lexicographic-order comparator composed of this and then the {@code double} sort key
     * @throws NullPointerException
     *             if the argument is null.
     * @see #comparingDouble(MgToDoubleFunction)
     * @see #thenComparing(MgComparator)
     * @since 1.8
     */
    default MgComparator<T> thenComparingDouble(MgToDoubleFunction<? super T> keyExtractor)
    {
        return thenComparing(comparingDouble(keyExtractor));
    }
    
    /**
     * Returns a comparator that imposes the reverse of the <em>natural ordering</em>.
     *
     * <p>
     * The returned comparator is serializable and throws {@link NullPointerException} when comparing {@code null}.
     * </p>
     *
     * @param <T>
     *            the {@link Comparable} type of element to be compared
     * @return a comparator that imposes the reverse of the <i>natural ordering</i> on {@code Comparable} objects.
     * @see Comparable
     * @since 1.8
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> MgComparator<T> reverseOrder()
    {
        return (MgComparator<T>) ReverseComparator.INSTANCE;
    }
    
    /**
     * The reverse comparator.
     */
    enum ReverseComparator implements MgComparator<Comparable<Object>>
    {
        
        /** instance. */
        INSTANCE;
        
        @Override
        public int compare(Comparable<Object> c1, Comparable<Object> c2)
        {
            return c2.compareTo(c1);
        }
        
        @Override
        public MgComparator<Comparable<Object>> reversed()
        {
            return NaturalOrderComparator.INSTANCE;
        }
    }
    
    /**
     * Compares {@link Comparable} objects in natural order.
     *
     * @see Comparable
     */
    enum NaturalOrderComparator implements MgComparator<Comparable<Object>>
    {
        /** instance. */
        INSTANCE;
        
        @Override
        public int compare(Comparable<Object> c1, Comparable<Object> c2)
        {
            return c1.compareTo(c2);
        }
        
        @Override
        public MgComparator<Comparable<Object>> reversed()
        {
            return ReverseComparator.INSTANCE;
        }
    }
    
    /**
     * Null-friendly comparators.
     * 
     * @param <T>
     *            type
     */
    public static final class NullComparator<T> implements MgComparator<T>
    {

        /** flag nullFirst. */
        private final boolean             nullFirst;
        
        /** real comparator. */
        private final MgComparator<T> real;
        
        /**
         * Constructor.
         * 
         * @param nullFirst
         *            flag nullFirst.
         * @param real
         *            real cmoparator.
         */
        @SuppressWarnings("unchecked")
        NullComparator(boolean nullFirst, MgComparator<? super T> real)
        {
            this.nullFirst = nullFirst;
            this.real = (MgComparator<T>) real;
        }
        
        @Override
        public int compare(T arg1, T arg2) throws MinigameException
        {
            if (arg1 == null)
            {
                return (arg2 == null) ? 0 : (this.nullFirst ? -1 : 1);
            }
            else if (arg2 == null)
            {
                return this.nullFirst ? 1 : -1;
            }
            else
            {
                return (this.real == null) ? 0 : this.real.compare(arg1, arg2);
            }
        }
        
        @Override
        public MgComparator<T> thenComparing(MgComparator<? super T> other)
        {
            Objects.requireNonNull(other);
            return new NullComparator<>(this.nullFirst, this.real == null ? other : this.real.thenComparing(other));
        }
        
        @Override
        public MgComparator<T> reversed()
        {
            return new NullComparator<>(!this.nullFirst, this.real == null ? null : this.real.reversed());
        }
    }
    
    /**
     * Returns a comparator that compares {@link Comparable} objects in natural order.
     *
     * <p>
     * The returned comparator is serializable and throws {@link NullPointerException} when comparing {@code null}.
     * </p>
     *
     * @param <T>
     *            the {@link Comparable} type of element to be compared
     * @return a comparator that imposes the <i>natural ordering</i> on {@code
     *         Comparable} objects.
     * @see Comparable
     * @since 1.8
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> MgComparator<T> naturalOrder()
    {
        return (MgComparator<T>) NaturalOrderComparator.INSTANCE;
    }
    
    /**
     * Returns a null-friendly comparator that considers {@code null} to be less than non-null. When both are {@code null}, they are considered equal. If both are non-null, the specified
     * {@code Comparator} is used to determine the order. If the specified comparator is {@code null}, then the returned comparator considers all non-null values to be equal.
     *
     * <p>
     * The returned comparator is serializable if the specified comparator is serializable.
     * </p>
     *
     * @param <T>
     *            the type of the elements to be compared
     * @param comparator
     *            a {@code Comparator} for comparing non-null values
     * @return a comparator that considers {@code null} to be less than non-null, and compares non-null objects with the supplied {@code Comparator}.
     * @since 1.8
     */
    public static <T> MgComparator<T> nullsFirst(MgComparator<? super T> comparator)
    {
        return new NullComparator<>(true, comparator);
    }
    
    /**
     * Returns a null-friendly comparator that considers {@code null} to be greater than non-null. When both are {@code null}, they are considered equal. If both are non-null, the specified
     * {@code Comparator} is used to determine the order. If the specified comparator is {@code null}, then the returned comparator considers all non-null values to be equal.
     *
     * <p>
     * The returned comparator is serializable if the specified comparator is serializable.
     * </p>
     *
     * @param <T>
     *            the type of the elements to be compared
     * @param comparator
     *            a {@code Comparator} for comparing non-null values
     * @return a comparator that considers {@code null} to be greater than non-null, and compares non-null objects with the supplied {@code Comparator}.
     * @since 1.8
     */
    public static <T> MgComparator<T> nullsLast(MgComparator<? super T> comparator)
    {
        return new NullComparator<>(false, comparator);
    }
    
    /**
     * Accepts a function that extracts a sort key from a type {@code T}, and returns a {@code Comparator<T>} that compares by that sort key using the specified {@link MgComparator}.
     *
     * <p>
     * The returned comparator is serializable if the specified function and comparator are both serializable.
     * </p>
     *
     * @param <T>
     *            the type of element to be compared
     * @param <U>
     *            the type of the sort key
     * @param keyExtractor
     *            the function used to extract the sort key
     * @param keyComparator
     *            the {@code Comparator} used to compare the sort key
     * @return a comparator that compares by an extracted key using the specified {@code Comparator}
     * @throws NullPointerException
     *             if either argument is null
     * @since 1.8
     */
    public static <T, U> MgComparator<T> comparing(MgFunction<? super T, ? extends U> keyExtractor, MgComparator<? super U> keyComparator)
    {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (MgComparator<T>) (c1, c2) -> keyComparator.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
    }
    
    /**
     * Accepts a function that extracts a {@link java.lang.Comparable Comparable} sort key from a type {@code T}, and returns a {@code
     * Comparator<T>} that compares by that sort key.
     *
     * <p>
     * The returned comparator is serializable if the specified function is also serializable.
     * </p>
     *
     * @param <T>
     *            the type of element to be compared
     * @param <U>
     *            the type of the {@code Comparable} sort key
     * @param keyExtractor
     *            the function used to extract the {@link Comparable} sort key
     * @return a comparator that compares by an extracted key
     * @throws NullPointerException
     *             if the argument is null
     * @since 1.8
     */
    public static <T, U extends Comparable<? super U>> MgComparator<T> comparing(MgFunction<? super T, ? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (MgComparator<T>) (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }
    
    /**
     * Accepts a function that extracts an {@code int} sort key from a type {@code T}, and returns a {@code Comparator<T>} that compares by that sort key.
     *
     * <p>
     * The returned comparator is serializable if the specified function is also serializable.
     * </p>
     *
     * @param <T>
     *            the type of element to be compared
     * @param keyExtractor
     *            the function used to extract the integer sort key
     * @return a comparator that compares by an extracted key
     * @see #comparing(MgFunction)
     * @throws NullPointerException
     *             if the argument is null
     * @since 1.8
     */
    public static <T> MgComparator<T> comparingInt(MgToIntFunction<? super T> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (MgComparator<T>) (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
    }
    
    /**
     * Accepts a function that extracts a {@code long} sort key from a type {@code T}, and returns a {@code Comparator<T>} that compares by that sort key.
     *
     * <p>
     * The returned comparator is serializable if the specified function is also serializable.
     * </p>
     *
     * @param <T>
     *            the type of element to be compared
     * @param keyExtractor
     *            the function used to extract the long sort key
     * @return a comparator that compares by an extracted key
     * @see #comparing(MgFunction)
     * @throws NullPointerException
     *             if the argument is null
     * @since 1.8
     */
    public static <T> MgComparator<T> comparingLong(MgToLongFunction<? super T> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (MgComparator<T>) (c1, c2) -> Long.compare(keyExtractor.applyAsLong(c1), keyExtractor.applyAsLong(c2));
    }
    
    /**
     * Accepts a function that extracts a {@code double} sort key from a type {@code T}, and returns a {@code Comparator<T>} that compares by that sort key.
     *
     * <p>
     * The returned comparator is serializable if the specified function is also serializable.
     * </p>
     *
     * @param <T>
     *            the type of element to be compared
     * @param keyExtractor
     *            the function used to extract the double sort key
     * @return a comparator that compares by an extracted key
     * @see #comparing(MgFunction)
     * @throws NullPointerException
     *             if the argument is null
     * @since 1.8
     */
    public static <T> MgComparator<T> comparingDouble(MgToDoubleFunction<? super T> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (MgComparator<T>) (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c1), keyExtractor.applyAsDouble(c2));
    }
    
}
