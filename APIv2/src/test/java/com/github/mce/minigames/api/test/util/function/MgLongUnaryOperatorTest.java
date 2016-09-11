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

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgLongUnaryOperator;

/**
 * Tests case for {@link MgLongUnaryOperator}
 * 
 * @author mepeisen
 */
public class MgLongUnaryOperatorTest
{
    
    /**
     * Tests method {@link MgLongUnaryOperator#compose(MgLongUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws MinigameException
    {
        final AtomicLong result1 = new AtomicLong(0);
        final MgLongUnaryOperator func = (l) -> l * 2;
        final MgLongUnaryOperator func2 = (l) -> { result1.set(l); return l * 3; };
        
        assertEquals(24, func.compose(func2).applyAsLong(4));
        assertEquals(4, result1.get());
    }
    
    /**
     * Tests method {@link MgLongUnaryOperator#andThen(MgLongUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final AtomicLong result1 = new AtomicLong(0);
        final MgLongUnaryOperator func = (l) -> l * 2;
        final MgLongUnaryOperator func2 = (l) -> { result1.set(l); return l * 3; };
        
        assertEquals(24, func.andThen(func2).applyAsLong(4));
        assertEquals(8, result1.get());
    }
    
    /**
     * Tests method {@link MgLongUnaryOperator#identity()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws MinigameException
    {
        final MgLongUnaryOperator func = MgLongUnaryOperator.identity();
        assertEquals(10, func.applyAsLong(10));
    }
    
}
