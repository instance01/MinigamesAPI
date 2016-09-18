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
import com.github.mce.minigames.api.util.function.MgBiFunction;
import com.github.mce.minigames.api.util.function.MgFunction;

/**
 * Tests case for {@link MgBiFunction}
 * 
 * @author mepeisen
 */
public class MgBiFunctionTest
{
    
    /**
     * Tests method {@link MgBiFunction#andThen(com.github.mce.minigames.api.util.function.MgFunction)}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws MinigameException
    {
        final MgFunction<String, Integer> func = Integer::valueOf;
        final MgBiFunction<String, String, String> biFunc = (a1, a2) -> a1.concat(a2);
        
        final MgBiFunction<String, String, Integer> biFunc2 = biFunc.andThen(func);
        
        assertEquals(10, biFunc2.apply("1", "0").intValue()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
