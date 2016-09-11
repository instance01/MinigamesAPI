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

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgIntUnaryOperator;

/**
 * Tests case for {@link MgIntUnaryOperator}
 * 
 * @author mepeisen
 */
public class MgIntUnaryOperatorTest
{
    
    /**
     * Tests method {@link MgIntUnaryOperator#compose(MgIntUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final MgIntUnaryOperator func = (l) -> l * 2;
        final MgIntUnaryOperator func2 = (l) -> { result1.set(l); return l * 3; };
        
        assertEquals(24, func.compose(func2).applyAsInt(4));
        assertEquals(4, result1.get());
    }
    
    /**
     * Tests method {@link MgIntUnaryOperator#andThen(MgIntUnaryOperator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final MgIntUnaryOperator func = (l) -> l * 2;
        final MgIntUnaryOperator func2 = (l) -> { result1.set(l); return l * 3; };
        
        assertEquals(24, func.andThen(func2).applyAsInt(4));
        assertEquals(8, result1.get());
    }
    
    /**
     * Tests method {@link MgIntUnaryOperator#identity()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws MinigameException
    {
        final MgIntUnaryOperator func = MgIntUnaryOperator.identity();
        assertEquals(10, func.applyAsInt(10));
    }
    
}
