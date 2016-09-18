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
    aInt with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.github.mce.minigames.api.test.util.function;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgBiConsumer;

/**
 * Tests case for {@link MgBiConsumer}
 * 
 * @author mepeisen
 */
public class MgBiConsumerTest
{
    
    /**
     * Tests method {@link MgBiConsumer#andThen(MgBiConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final AtomicInteger result2 = new AtomicInteger(0);
        final AtomicInteger result3 = new AtomicInteger(0);
        final AtomicInteger result4 = new AtomicInteger(0);
        final MgBiConsumer<Integer, Integer> func = (i, j) -> { result1.set(i); result2.set(j + 10); };
        final MgBiConsumer<Integer, Integer> func2 = (i, j) -> { result3.set(i + result1.get()); result4.set(j + result2.get()); };
        
        func.andThen(func2).accept(5, 7);
        
        assertEquals(5, result1.get());
        assertEquals(17, result2.get());
        assertEquals(10, result3.get());
        assertEquals(24, result4.get());
    }
    
}
