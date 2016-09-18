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

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgDoubleUnaryOperator;
import com.google.common.util.concurrent.AtomicDouble;

/**
 * Tests case for {@link MgDoubleUnaryOperator}
 * 
 * @author mepeisen
 */
public class MgDoubleUnaryOperatorTest
{
    
    /**
     * Tests method {@link MgDoubleUnaryOperator#compose(MgDoubleUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws MinigameException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final MgDoubleUnaryOperator func = (l) -> l * 2;
        final MgDoubleUnaryOperator func2 = (l) -> { result1.set(l); return l * 3; };
        
        assertEquals(24, func.compose(func2).applyAsDouble(4), 0);
        assertEquals(4, result1.get(), 0);
    }
    
    /**
     * Tests method {@link MgDoubleUnaryOperator#andThen(MgDoubleUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final MgDoubleUnaryOperator func = (l) -> l * 2;
        final MgDoubleUnaryOperator func2 = (l) -> { result1.set(l); return l * 3; };
        
        assertEquals(24, func.andThen(func2).applyAsDouble(4), 0);
        assertEquals(8, result1.get(), 0);
    }
    
    /**
     * Tests method {@link MgDoubleUnaryOperator#identity()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws MinigameException
    {
        final MgDoubleUnaryOperator func = MgDoubleUnaryOperator.identity();
        assertEquals(10, func.applyAsDouble(10), 0);
    }
    
}
