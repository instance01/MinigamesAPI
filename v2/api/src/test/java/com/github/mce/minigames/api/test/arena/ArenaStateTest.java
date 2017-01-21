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

package com.github.mce.minigames.api.test.arena;

import org.junit.Test;

import com.github.mce.minigames.api.test.SharedUtil;

import de.minigameslib.mgapi.api.arena.ArenaState;

/**
 * test case for {@link ArenaState}
 * 
 * @author mepeisen
 */
public class ArenaStateTest
{
    
    /**
     * Tests the enum
     */
    @Test
    public void enumTest()
    {
        SharedUtil.testEnumClass(ArenaState.class);
    }
    
}
