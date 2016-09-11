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
import com.github.mce.minigames.api.util.function.MgConsumer;

/**
 * Tests case for {@link MgConsumer}
 * 
 * @author mepeisen
 */
public class MgConsumerTest
{
    
    /**
     * Tests method {@link MgConsumer#andThen(MgConsumer)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final AtomicInteger result2 = new AtomicInteger(0);
        final MgConsumer<Integer> func = (l) -> result1.set(l);
        final MgConsumer<Integer> func2 = (l) -> result2.set(l + result1.get());
        
        func.andThen(func2).accept(5);
        
        assertEquals(5, result1.get());
        assertEquals(10, result2.get());
    }
    
}
