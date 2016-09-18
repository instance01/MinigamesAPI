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
import com.github.mce.minigames.api.util.function.MgBinaryOperator;

/**
 * Tests case for {@link MgBinaryOperator}
 * 
 * @author mepeisen
 */
public class MgBinaryOperatorTest
{
    
    /**
     * Tests method {@link MgBinaryOperator#minBy(com.github.mce.minigames.api.util.function.MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testMinBy() throws MinigameException
    {
        final MgBinaryOperator<Integer> func = MgBinaryOperator.minBy(Integer::compareTo);
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(10), Integer.valueOf(20)));
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(10), Integer.valueOf(10)));
    }
    
    /**
     * Tests method {@link MgBinaryOperator#maxBy(com.github.mce.minigames.api.util.function.MgComparator)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testMaxBy() throws MinigameException
    {
        final MgBinaryOperator<Integer> func = MgBinaryOperator.maxBy(Integer::compareTo);
        assertEquals(Integer.valueOf(20), func.apply(Integer.valueOf(10), Integer.valueOf(20)));
        assertEquals(Integer.valueOf(20), func.apply(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(Integer.valueOf(20), func.apply(Integer.valueOf(20), Integer.valueOf(20)));
    }
    
}
