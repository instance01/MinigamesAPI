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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.test.SharedUtil;
import com.github.mce.minigames.api.util.function.MgComparator;
import com.google.common.util.concurrent.AtomicDouble;

/**
 * Tests case for {@link MgComparator}
 * 
 * @author mepeisen
 */
public class MgComparatorTest
{
    
    /**
     * Tests method {@link MgComparator#reversed()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testReversed() throws MinigameException
    {
        final MgComparator<Integer> func = Integer::compareTo;
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.reversed().reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link MgComparator#reverseOrder()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testReverseOrder() throws MinigameException
    {
        final MgComparator<Integer> func = MgComparator.reverseOrder();
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link MgComparator#naturalOrder()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNaturalOrder() throws MinigameException
    {
        final MgComparator<Integer> func = MgComparator.naturalOrder();
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link MgComparator#nullsFirst(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst() throws MinigameException
    {
        final MgComparator<Integer> func = MgComparator.nullsFirst(MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsLast(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast() throws MinigameException
    {
        final MgComparator<Integer> func = MgComparator.nullsLast(MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsFirst(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst2() throws MinigameException
    {
        final MgComparator<Integer> func = MgComparator.nullsFirst(null);
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsLast(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast2() throws MinigameException
    {
        final MgComparator<Integer> func = MgComparator.nullsLast(null);
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsFirst(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst3() throws MinigameException
    {
        MgComparator<Integer> func = MgComparator.nullsFirst(MgComparator.naturalOrder());
        func = func.thenComparing(MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsLast(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast3() throws MinigameException
    {
        MgComparator<Integer> func = MgComparator.nullsLast(MgComparator.naturalOrder());
        func = func.thenComparing(MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsFirst(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst4() throws MinigameException
    {
        MgComparator<Integer> func = MgComparator.nullsFirst(null);
        func = func.thenComparing(MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#nullsLast(MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast4() throws MinigameException
    {
        MgComparator<Integer> func = MgComparator.nullsLast(null);
        func = func.thenComparing(MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link MgComparator#comparingLong(com.github.mce.minigames.api.util.function.MgToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testComparingLong() throws MinigameException
    {
        final MgComparator<AtomicLong> func = MgComparator.comparingLong((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicLong(10), new AtomicLong(10)));
        assertEquals(1, func.compare(new AtomicLong(20), new AtomicLong(10)));
        assertEquals(-1, func.compare(new AtomicLong(10), new AtomicLong(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicLong(10), new AtomicLong(10)));
        assertEquals(-1, func.reversed().compare(new AtomicLong(20), new AtomicLong(10)));
        assertEquals(1, func.reversed().compare(new AtomicLong(10), new AtomicLong(20)));
    }
    
    /**
     * Tests method {@link MgComparator#comparingInt(com.github.mce.minigames.api.util.function.MgToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testComparingInt() throws MinigameException
    {
        final MgComparator<AtomicInteger> func = MgComparator.comparingInt((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicInteger(10), new AtomicInteger(10)));
        assertEquals(1, func.compare(new AtomicInteger(20), new AtomicInteger(10)));
        assertEquals(-1, func.compare(new AtomicInteger(10), new AtomicInteger(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicInteger(10), new AtomicInteger(10)));
        assertEquals(-1, func.reversed().compare(new AtomicInteger(20), new AtomicInteger(10)));
        assertEquals(1, func.reversed().compare(new AtomicInteger(10), new AtomicInteger(20)));
    }
    
    /**
     * Tests method {@link MgComparator#comparingDouble(com.github.mce.minigames.api.util.function.MgToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testComparingDouble() throws MinigameException
    {
        final MgComparator<AtomicDouble> func = MgComparator.comparingDouble((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(1, func.compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(-1, func.compare(new AtomicDouble(10), new AtomicDouble(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(-1, func.reversed().compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(1, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(20)));
    }
    
    /**
     * Tests method {@link MgComparator#comparing(com.github.mce.minigames.api.util.function.MgFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testComparing1() throws MinigameException
    {
        final MgComparator<AtomicDouble> func = MgComparator.comparing((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(1, func.compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(-1, func.compare(new AtomicDouble(10), new AtomicDouble(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(-1, func.reversed().compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(1, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(20)));
    }
    
    /**
     * Tests method {@link MgComparator#comparing(com.github.mce.minigames.api.util.function.MgFunction, MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testComparing2() throws MinigameException
    {
        final MgComparator<AtomicDouble> func = MgComparator.comparing((d) -> d.get(), MgComparator.naturalOrder());
        
        assertEquals(0, func.compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(1, func.compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(-1, func.compare(new AtomicDouble(10), new AtomicDouble(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(-1, func.reversed().compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(1, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(20)));
    }
    
    /**
     * Tests method {@link MgComparator#thenComparing(com.github.mce.minigames.api.util.function.MgFunction, MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testThenComparing1() throws MinigameException
    {
        final MgComparator<FooInt> func = MgComparator.comparing((d) -> d.getA());
        final MgComparator<FooInt> func2 = func.thenComparing((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func2.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func2.compare(new FooInt(10, 10), new FooInt(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
    }
    
    /**
     * Tests method {@link MgComparator#thenComparing(com.github.mce.minigames.api.util.function.MgFunction, MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testThenComparing2() throws MinigameException
    {
        final MgComparator<FooInt> func = MgComparator.comparing((d) -> d.getA());
        final MgComparator<FooInt> func2 = func.thenComparing((d) -> d.getB(), MgComparator.naturalOrder());
        
        assertEquals(0, func2.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func2.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func2.compare(new FooInt(10, 10), new FooInt(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
    }
    
    /**
     * Tests method {@link MgComparator#thenComparing(com.github.mce.minigames.api.util.function.MgFunction, MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testThenComparing3() throws MinigameException
    {
        final MgComparator<FooInt> func = MgComparator.comparing((d) -> d.getA());
        final MgComparator<FooInt> func2 = (d, e) -> Integer.compare(d.getB(), e.getB());
        final MgComparator<FooInt> func3 = func.thenComparing(func2);
        
        assertEquals(0, func3.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func3.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func3.compare(new FooInt(10, 10), new FooInt(10, 20)));
        assertEquals(1, func3.compare(new FooInt(20, 10), new FooInt(10, 10)));
        assertEquals(-1, func3.compare(new FooInt(10, 10), new FooInt(20, 10)));

        assertEquals(0, func3.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func3.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func3.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
        assertEquals(-1, func3.reversed().compare(new FooInt(20, 10), new FooInt(10, 10)));
        assertEquals(1, func3.reversed().compare(new FooInt(10, 10), new FooInt(20, 10)));
    }
    
    /**
     * Tests method {@link MgComparator#thenComparingInt(com.github.mce.minigames.api.util.function.MgToIntFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testThenComparingInt() throws MinigameException
    {
        final MgComparator<FooInt> func = MgComparator.comparing((d) -> d.getA());
        final MgComparator<FooInt> func2 = func.thenComparingInt((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func2.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func2.compare(new FooInt(10, 10), new FooInt(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
    }
    
    /**
     * Tests method {@link MgComparator#thenComparingLong(com.github.mce.minigames.api.util.function.MgToLongFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testThenComparingLong() throws MinigameException
    {
        final MgComparator<FooLong> func = MgComparator.comparing((d) -> d.getA());
        final MgComparator<FooLong> func2 = func.thenComparingLong((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooLong(10, 10), new FooLong(10, 10)));
        assertEquals(1, func2.compare(new FooLong(10, 20), new FooLong(10, 10)));
        assertEquals(-1, func2.compare(new FooLong(10, 10), new FooLong(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooLong(10, 10), new FooLong(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooLong(10, 20), new FooLong(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooLong(10, 10), new FooLong(10, 20)));
    }
    
    /**
     * Tests method {@link MgComparator#thenComparingDouble(com.github.mce.minigames.api.util.function.MgToDoubleFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testThenComparingDouble() throws MinigameException
    {
        final MgComparator<FooDouble> func = MgComparator.comparing((d) -> d.getA());
        final MgComparator<FooDouble> func2 = func.thenComparingDouble((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooDouble(10, 10), new FooDouble(10, 10)));
        assertEquals(1, func2.compare(new FooDouble(10, 20), new FooDouble(10, 10)));
        assertEquals(-1, func2.compare(new FooDouble(10, 10), new FooDouble(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooDouble(10, 10), new FooDouble(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooDouble(10, 20), new FooDouble(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooDouble(10, 10), new FooDouble(10, 20)));
    }
    
    /**
     * Tests the enums
     */
    @Test
    public void enumTest()
    {
        SharedUtil.testEnumClass(MgComparator.ReverseComparator.class);
        SharedUtil.testEnumClass(MgComparator.NaturalOrderComparator.class);
    }
    
    /**
     * Helper class.
     */
    private static final class FooInt
    {
        /** a value. */
        private final int a;
        /** a value. */
        private final int b;
        
        /**
         * Constructor
         * @param a
         * @param b
         */
        public FooInt(int a, int b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the a
         */
        public int getA()
        {
            return this.a;
        }

        /**
         * @return the b
         */
        public int getB()
        {
            return this.b;
        }
    }
    
    /**
     * Helper class.
     */
    private static final class FooLong
    {
        /** a value. */
        private final long a;
        /** a value. */
        private final long b;
        
        /**
         * Constructor
         * @param a
         * @param b
         */
        public FooLong(long a, long b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the a
         */
        public long getA()
        {
            return this.a;
        }

        /**
         * @return the b
         */
        public long getB()
        {
            return this.b;
        }
    }
    
    /**
     * Helper class.
     */
    private static final class FooDouble
    {
        /** a value. */
        private final double a;
        /** a value. */
        private final double b;
        
        /**
         * Constructor
         * @param a
         * @param b
         */
        public FooDouble(double a, double b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the a
         */
        public double getA()
        {
            return this.a;
        }

        /**
         * @return the b
         */
        public double getB()
        {
            return this.b;
        }
    }
    
}
