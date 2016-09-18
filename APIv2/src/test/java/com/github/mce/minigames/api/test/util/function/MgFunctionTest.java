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

import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.util.function.MgFunction;

/**
 * Tests case for {@link MgFunction}
 * 
 * @author mepeisen
 */
public class MgFunctionTest
{
    
    /**
     * Tests method {@link MgFunction#andThen(com.github.mce.minigames.api.util.function.MgFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final MgFunction<String, Integer> func = Integer::valueOf;
        final MgFunction<String, String> func2 = (a1) -> a1.concat("0"); //$NON-NLS-1$
        
        final MgFunction<String, Integer> func3 = func2.andThen(func);
        
        assertEquals(10, func3.apply("1").intValue()); //$NON-NLS-1$
    }
    
    /**
     * Tests method {@link MgFunction#compose(MgFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws MinigameException
    {
        final MgFunction<String, Integer> func = Integer::valueOf;
        final MgFunction<String, String> func2 = (a1) -> a1.concat("0"); //$NON-NLS-1$
        
        final MgFunction<String, Integer> func3 = func.compose(func2);
        
        assertEquals(10, func3.apply("1").intValue()); //$NON-NLS-1$
    }
    
    /**
     * Tests method {@link MgFunction#identity()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws MinigameException
    {
        final MgFunction<Integer, Integer> func = MgFunction.identity();
        assertEquals(10, func.apply(10).intValue());
    }
    
}
