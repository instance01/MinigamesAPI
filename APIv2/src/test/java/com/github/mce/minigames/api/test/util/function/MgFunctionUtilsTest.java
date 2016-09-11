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

package com.github.mce.minigames.api.test.util.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgBiConsumer;
import com.github.mce.minigames.api.util.function.MgBiFunction;
import com.github.mce.minigames.api.util.function.MgBiPredicate;
import com.github.mce.minigames.api.util.function.MgBinaryOperator;
import com.github.mce.minigames.api.util.function.MgBooleanSupplier;
import com.github.mce.minigames.api.util.function.MgComparator;
import com.github.mce.minigames.api.util.function.MgConsumer;
import com.github.mce.minigames.api.util.function.MgDoubleBinaryOperator;
import com.github.mce.minigames.api.util.function.MgDoubleConsumer;
import com.github.mce.minigames.api.util.function.MgDoubleFunction;
import com.github.mce.minigames.api.util.function.MgDoublePredicate;
import com.github.mce.minigames.api.util.function.MgDoubleSupplier;
import com.github.mce.minigames.api.util.function.MgDoubleToIntFunction;
import com.github.mce.minigames.api.util.function.MgDoubleToLongFunction;
import com.github.mce.minigames.api.util.function.MgDoubleUnaryOperator;
import com.github.mce.minigames.api.util.function.MgFunction;
import com.github.mce.minigames.api.util.function.MgFunctionUtils;
import com.github.mce.minigames.api.util.function.MgIntBinaryOperator;
import com.github.mce.minigames.api.util.function.MgIntConsumer;
import com.github.mce.minigames.api.util.function.MgIntFunction;
import com.github.mce.minigames.api.util.function.MgIntPredicate;
import com.github.mce.minigames.api.util.function.MgIntSupplier;
import com.github.mce.minigames.api.util.function.MgIntToDoubleFunction;
import com.github.mce.minigames.api.util.function.MgIntToLongFunction;
import com.github.mce.minigames.api.util.function.MgIntUnaryOperator;
import com.github.mce.minigames.api.util.function.MgLongBinaryOperator;
import com.github.mce.minigames.api.util.function.MgLongConsumer;
import com.github.mce.minigames.api.util.function.MgLongFunction;
import com.github.mce.minigames.api.util.function.MgLongPredicate;
import com.github.mce.minigames.api.util.function.MgLongSupplier;
import com.github.mce.minigames.api.util.function.MgLongToDoubleFunction;
import com.github.mce.minigames.api.util.function.MgLongToIntFunction;
import com.github.mce.minigames.api.util.function.MgLongUnaryOperator;
import com.github.mce.minigames.api.util.function.MgObjDoubleConsumer;
import com.github.mce.minigames.api.util.function.MgObjIntConsumer;
import com.github.mce.minigames.api.util.function.MgObjLongConsumer;
import com.github.mce.minigames.api.util.function.MgPredicate;
import com.github.mce.minigames.api.util.function.MgSupplier;
import com.github.mce.minigames.api.util.function.MgToDoubleBiFunction;
import com.github.mce.minigames.api.util.function.MgToDoubleFunction;
import com.github.mce.minigames.api.util.function.MgToIntBiFunction;
import com.github.mce.minigames.api.util.function.MgToIntFunction;
import com.github.mce.minigames.api.util.function.MgToLongBiFunction;
import com.github.mce.minigames.api.util.function.MgToLongFunction;
import com.github.mce.minigames.api.util.function.MgUnaryOperator;
import com.google.common.util.concurrent.AtomicDouble;

/**
 * Tests case for {@link MgFunctionUtils}
 * 
 * @author mepeisen
 */
public class MgFunctionUtilsTest
{
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBiConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testBiConsumerOk() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final AtomicInteger result2 = new AtomicInteger(0);
        final MgBiConsumer<Integer, Integer> func = (i, j) -> { result1.set(i); result2.set(j + 10); };
        
        MgFunctionUtils.wrap(func).accept(5, 7);
        
        assertEquals(5, result1.get());
        assertEquals(17, result2.get());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBiConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testBiConsumerFailed() throws MinigameException
    {
        final MgBiConsumer<Integer, Integer> func = (i, j) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(5, 7);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBiFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testBiFunctionOk() throws MinigameException
    {
        final MgBiFunction<String, String, String> func = (a1, a2) -> a1.concat(a2);
        
        assertEquals("10", MgFunctionUtils.wrap(func).apply("1", "0")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBiFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testBiFunctionFailed() throws MinigameException
    {
        final MgBiFunction<String, String, String> func = (a1, a2) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply("1", "0"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testBinaryOperatorOk() throws MinigameException
    {
        final MgBinaryOperator<Integer> func = MgBinaryOperator.minBy(Integer::compareTo);
        
        assertEquals(Integer.valueOf(10), MgFunctionUtils.wrap(func).apply(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testBinaryOperatorFailed() throws MinigameException
    {
        final MgBinaryOperator<Integer> func = (a, b) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply(Integer.valueOf(10), Integer.valueOf(20));
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBiPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testBiPredicateOk() throws MinigameException
    {
        final MgBiPredicate<Integer, Integer> func = (i, j) -> i > 10 && j > 10;
        
        assertTrue(MgFunctionUtils.wrap(func).test(15, 15));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBiPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testBiPredicateFailed() throws MinigameException
    {
        final MgBiPredicate<Integer, Integer> func = (a, b) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).test(15, 15);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBooleanSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testBooleanSupplierOk() throws MinigameException
    {
        final MgBooleanSupplier func = () -> true;
        
        assertTrue(MgFunctionUtils.wrap(func).getAsBoolean());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgBooleanSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testBooleanSupplierFailed() throws MinigameException
    {
        final MgBooleanSupplier func = () -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).getAsBoolean();
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testComparatorOk() throws MinigameException
    {
        final MgComparator<Integer> func = Integer::compareTo;
        
        assertEquals(0, MgFunctionUtils.wrap(func).compare(Integer.valueOf(10), Integer.valueOf(10)));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testComparatorFailed() throws MinigameException
    {
        final MgComparator<Integer> func = (a, b) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).compare(Integer.valueOf(10), Integer.valueOf(10));
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testConsumerOk() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final MgConsumer<Integer> func = (l) -> result1.set(l);
        
        MgFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testConsumerFailed() throws MinigameException
    {
        final MgConsumer<Integer> func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleConsumerOk() throws MinigameException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final MgDoubleConsumer func = (l) -> result1.set(l);
        
        MgFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get(), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleConsumerFailed() throws MinigameException
    {
        final MgDoubleConsumer func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntConsumerOk() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final MgIntConsumer func = (l) -> result1.set(l);
        
        MgFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntConsumerFailed() throws MinigameException
    {
        final MgIntConsumer func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongConsumerOk() throws MinigameException
    {
        final AtomicLong result1 = new AtomicLong(0);
        final MgLongConsumer func = (l) -> result1.set(l);
        
        MgFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongConsumerFailed() throws MinigameException
    {
        final MgLongConsumer func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleFunctionOk() throws MinigameException
    {
        final MgDoubleFunction<Long> func = (d) -> Math.round(d);
        
        assertEquals(10, MgFunctionUtils.wrap(func).apply(9.5d).longValue());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleFunctionFailed() throws MinigameException
    {
        final MgDoubleFunction<Long> func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleSupplierOk() throws MinigameException
    {
        final MgDoubleSupplier func = () -> 9.5d;
        
        assertEquals(9.5d, MgFunctionUtils.wrap(func).getAsDouble(), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleSupplierFailed() throws MinigameException
    {
        final MgDoubleSupplier func = () -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).getAsDouble();
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoublePredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoublePredicateOk() throws MinigameException
    {
        final MgDoublePredicate func = (d) -> d == 9.5d;
        
        assertTrue(MgFunctionUtils.wrap(func).test(9.5));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoublePredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoublePredicateFailed() throws MinigameException
    {
        final MgDoublePredicate func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).test(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleToIntFunctionOk() throws MinigameException
    {
        final MgDoubleToIntFunction func = (d) -> (int) Math.round(d);
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsInt(9.5d));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleToIntFunctionFailed() throws MinigameException
    {
        final MgDoubleToIntFunction func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsInt(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleToLongFunctionOk() throws MinigameException
    {
        final MgDoubleToLongFunction func = (d) -> Math.round(d);
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsLong(9.5d));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleToLongFunctionFailed() throws MinigameException
    {
        final MgDoubleToLongFunction func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsLong(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleUnaryOperatorOk() throws MinigameException
    {
        final MgDoubleUnaryOperator func = (d) -> Math.round(d);
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsDouble(9.5d), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleUnaryOperatorFailed() throws MinigameException
    {
        final MgDoubleUnaryOperator func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsDouble(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testDoubleBinaryOperatorOk() throws MinigameException
    {
        final MgDoubleBinaryOperator func = (d, e) -> d + e;
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testDoubleBinaryOperatorFailed() throws MinigameException
    {
        final MgDoubleBinaryOperator func = (a, b) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testFunctionOk() throws MinigameException
    {
        final MgFunction<Double, Double> func = (d) -> (double) Math.round(d);
        
        assertEquals(10, MgFunctionUtils.wrap(func).apply(9.5d), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testFunctionFailed() throws MinigameException
    {
        final MgFunction<Double, Double> func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntFunctionOk() throws MinigameException
    {
        final MgIntFunction<Long> func = (d) -> (long) d;
        
        assertEquals(10, MgFunctionUtils.wrap(func).apply(10).longValue());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntFunctionFailed() throws MinigameException
    {
        final MgIntFunction<Long> func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntSupplierOk() throws MinigameException
    {
        final MgIntSupplier func = () -> 10;
        
        assertEquals(10, MgFunctionUtils.wrap(func).getAsInt());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntSupplierFailed() throws MinigameException
    {
        final MgIntSupplier func = () -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).getAsInt();
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntPredicateOk() throws MinigameException
    {
        final MgIntPredicate func = (d) -> d == 10;
        
        assertTrue(MgFunctionUtils.wrap(func).test(10));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntPredicateFailed() throws MinigameException
    {
        final MgIntPredicate func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).test(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntToDoubleFunctionOk() throws MinigameException
    {
        final MgIntToDoubleFunction func = (d) -> d + 0.5d;
        
        assertEquals(9.5d, MgFunctionUtils.wrap(func).applyAsDouble(9), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntToDoubleFunctionFailed() throws MinigameException
    {
        final MgIntToDoubleFunction func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsDouble(9);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntToLongFunctionOk() throws MinigameException
    {
        final MgIntToLongFunction func = (d) -> d;
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsLong(10));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntToLongFunctionFailed() throws MinigameException
    {
        final MgIntToLongFunction func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsLong(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntUnaryOperatorOk() throws MinigameException
    {
        final MgIntUnaryOperator func = (d) -> d + 1;
        
        assertEquals(11, MgFunctionUtils.wrap(func).applyAsInt(10));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntUnaryOperatorFailed() throws MinigameException
    {
        final MgIntUnaryOperator func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsInt(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIntBinaryOperatorOk() throws MinigameException
    {
        final MgIntBinaryOperator func = (d, e) -> d + e;
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsInt(9, 1));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testIntBinaryOperatorFailed() throws MinigameException
    {
        final MgIntBinaryOperator func = (a, b) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsInt(9, 1);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongFunctionOk() throws MinigameException
    {
        final MgLongFunction<Long> func = (d) -> (long) d;
        
        assertEquals(10, MgFunctionUtils.wrap(func).apply(10).longValue());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongFunctionFailed() throws MinigameException
    {
        final MgLongFunction<Long> func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongSupplierOk() throws MinigameException
    {
        final MgLongSupplier func = () -> 10;
        
        assertEquals(10, MgFunctionUtils.wrap(func).getAsLong());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongSupplierFailed() throws MinigameException
    {
        final MgLongSupplier func = () -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).getAsLong();
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongPredicateOk() throws MinigameException
    {
        final MgLongPredicate func = (d) -> d == 10;
        
        assertTrue(MgFunctionUtils.wrap(func).test(10));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongPredicateFailed() throws MinigameException
    {
        final MgLongPredicate func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).test(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongToDoubleFunctionOk() throws MinigameException
    {
        final MgLongToDoubleFunction func = (d) -> d + 0.5d;
        
        assertEquals(9.5d, MgFunctionUtils.wrap(func).applyAsDouble(9), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongToDoubleFunctionFailed() throws MinigameException
    {
        final MgLongToDoubleFunction func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsDouble(9);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongToLongFunctionOk() throws MinigameException
    {
        final MgLongToIntFunction func = (d) -> (int) d;
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsInt(10));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongToLongFunctionFailed() throws MinigameException
    {
        final MgLongToIntFunction func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsInt(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongUnaryOperatorOk() throws MinigameException
    {
        final MgLongUnaryOperator func = (d) -> d + 1;
        
        assertEquals(11, MgFunctionUtils.wrap(func).applyAsLong(10));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongUnaryOperatorFailed() throws MinigameException
    {
        final MgLongUnaryOperator func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsLong(10);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testLongBinaryOperatorOk() throws MinigameException
    {
        final MgLongBinaryOperator func = (d, e) -> d + e;
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsLong(9, 1));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongBinaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testLongBinaryOperatorFailed() throws MinigameException
    {
        final MgLongBinaryOperator func = (a, b) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsLong(9, 1);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testPredicateOk() throws MinigameException
    {
        final MgPredicate<Double> func = (d) -> d == 9.5d;
        
        assertTrue(MgFunctionUtils.wrap(func).test(9.5));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testPredicateFailed() throws MinigameException
    {
        final MgPredicate<Double> func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).test(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testSupplierOk() throws MinigameException
    {
        final MgSupplier<Double> func = () -> 9.5d;
        
        assertEquals(9.5d, MgFunctionUtils.wrap(func).get(), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testSupplierFailed() throws MinigameException
    {
        final MgSupplier<Double> func = () -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).get();
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testToDoubleFunctionOk() throws MinigameException
    {
        final MgToDoubleFunction<Double> func = (d) -> d;
        
        assertEquals(9.5d, MgFunctionUtils.wrap(func).applyAsDouble(9.5d), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testToDoubleFunctionFailed() throws MinigameException
    {
        final MgToDoubleFunction<Double> func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsDouble(9.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testToIntFunctionOk() throws MinigameException
    {
        final MgToIntFunction<Integer> func = (d) -> d;
        
        assertEquals(9, MgFunctionUtils.wrap(func).applyAsInt(9));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testToIntFunctionFailed() throws MinigameException
    {
        final MgToIntFunction<Integer> func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsInt(9);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testToLongFunctionOk() throws MinigameException
    {
        final MgToLongFunction<Long> func = (d) -> d;
        
        assertEquals(9, MgFunctionUtils.wrap(func).applyAsLong(9l));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testToLongFunctionFailed() throws MinigameException
    {
        final MgToLongFunction<Long> func = (d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsLong(9l);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgToDoubleBiFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testToDoubleBiFunctionOk() throws MinigameException
    {
        final MgToDoubleBiFunction<Double, Double> func = (d, e) -> d + e;
        
        assertEquals(10d, MgFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testToDoubleBiFunctionFailed() throws MinigameException
    {
        final MgToDoubleBiFunction<Double, Double> func = (d, e) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgToIntBiFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testToIntBiFunctionOk() throws MinigameException
    {
        final MgToIntBiFunction<Integer, Integer> func = (d, e) -> d + e;
        
        assertEquals(9, MgFunctionUtils.wrap(func).applyAsInt(8, 1));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testToIntBiFunctionFailed() throws MinigameException
    {
        final MgToIntBiFunction<Integer, Integer> func = (d, e) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsInt(9, 1);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgToLongBiFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testToLongBiFunctionOk() throws MinigameException
    {
        final MgToLongBiFunction<Long, Long> func = (d, e) -> d + e;
        
        assertEquals(10, MgFunctionUtils.wrap(func).applyAsLong(9l, 1l));
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testToLongBiFunctionFailed() throws MinigameException
    {
        final MgToLongBiFunction<Long, Long> func = (d,e) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).applyAsLong(9l, 1l);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgObjDoubleConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testObjDoubleConsumerOk() throws MinigameException
    {
        final MgObjDoubleConsumer<AtomicDouble> func = (a, d) -> a.set(d + a.get());
        final AtomicDouble val = new AtomicDouble(9d);
        MgFunctionUtils.wrap(func).accept(val, 0.5d);
        
        assertEquals(9.5d, val.get(), 0);
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgDoubleSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testObjDoubleConsumerFailed() throws MinigameException
    {
        final MgObjDoubleConsumer<AtomicDouble> func = (a, d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(new AtomicDouble(), 0.5d);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgObjIntConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testObjIntConsumerOk() throws MinigameException
    {
        final MgObjIntConsumer<AtomicInteger> func = (a, d) -> a.set(d + a.get());
        final AtomicInteger val = new AtomicInteger(9);
        MgFunctionUtils.wrap(func).accept(val, 1);
        
        assertEquals(10, val.get());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgIntSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testObjIntConsumerFailed() throws MinigameException
    {
        final MgObjIntConsumer<AtomicInteger> func = (a, d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(new AtomicInteger(), 1);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgObjLongConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testObjLongConsumerOk() throws MinigameException
    {
        final MgObjLongConsumer<AtomicLong> func = (a, d) -> a.set(d + a.get());
        final AtomicLong val = new AtomicLong(9);
        MgFunctionUtils.wrap(func).accept(val, 1);
        
        assertEquals(10, val.get());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testObjLongConsumerFailed() throws MinigameException
    {
        final MgObjLongConsumer<AtomicLong> func = (a, d) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).accept(new AtomicLong(), 1);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testUnaryOperatorOk() throws MinigameException
    {
        final MgUnaryOperator<Long> func = (a) -> a + 1;
        
        assertEquals(10, MgFunctionUtils.wrap(func).apply(9l).longValue());
    }
    
    /**
     * Tests method {@link MgFunctionUtils#wrap(com.github.mce.minigames.api.util.function.MgLongSupplier)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test(expected = MinigameException.class)
    public void testUnaryOperatorFailed() throws MinigameException
    {
        final MgUnaryOperator<Long> func = (a) -> { throw new MinigameException(CommonErrors.CannotStart); };
        
        try
        {
            MgFunctionUtils.wrap(func).apply(9l);
        }
        catch (MgFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Constructor test for code coverage
     */
    @SuppressWarnings("unused")
    @Test
    public void testConstructor()
    {
        new MgFunctionUtils();
    }
    
}
