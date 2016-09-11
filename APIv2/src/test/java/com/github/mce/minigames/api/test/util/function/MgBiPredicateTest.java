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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgBiPredicate;

/**
 * Tests case for {@link MgBiPredicate}
 * 
 * @author mepeisen
 */
public class MgBiPredicateTest
{
    
    /**
     * Tests method {@link MgBiPredicate#and(MgBiPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAnd() throws MinigameException
    {
        final MgBiPredicate<Integer, Integer> func = (i, j) -> i > 10 && j > 10;
        final MgBiPredicate<Integer, Integer> func2 = (i, j) -> i > 20 && j > 20;

        assertFalse(func.and(func2).test(15, 15));
        assertFalse(func2.and(func).test(15, 15));
        assertTrue(func.and(func2).test(25, 25));
    }
    
    /**
     * Tests method {@link MgBiPredicate#or(MgBiPredicate)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testOr() throws MinigameException
    {
        final MgBiPredicate<Integer, Integer> func = (i, j) -> i > 10 && j > 10;
        final MgBiPredicate<Integer, Integer> func2 = (i, j) -> i < -10 && j < -10;
        
        assertTrue(func.or(func2).test(15, 15));
        assertTrue(func.or(func2).test(-15, -15));
        assertFalse(func.or(func2).test(5, 5));
        assertFalse(func.or(func2).test(-5, -5));
    }
    
    /**
     * Tests method {@link MgBiPredicate#negate()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testNegate() throws MinigameException
    {
        final MgBiPredicate<Integer, Integer> func = (i, j) -> i > 10 && j > 10;
        
        assertFalse(func.negate().test(15, 15));
        assertTrue(func.negate().test(5, 5));
    }
    
}
