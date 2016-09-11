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
import com.github.mce.minigames.api.util.function.MgUnaryOperator;

/**
 * Tests case for {@link MgUnaryOperator}
 * 
 * @author mepeisen
 */
public class MgUnaryOperatorTest
{
    
    /**
     * Tests method {@link MgUnaryOperator#identity()}
     * 
     * @throws MinigameException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws MinigameException
    {
        final MgUnaryOperator<Integer> func = MgUnaryOperator.identity();
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(10)));
    }
    
}
