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

package com.comze_instancelabs.minigamesapi.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.comze_instancelabs.minigamesapi.ArenaState;

/**
 * Test case for arena states enumeration.
 * 
 * @author mepeisen
 * 
 * @see ArenaState
 */
public class ArenaStateTest
{
    
    /**
     * Test the ordinals
     */
    @Test
    public void testOrdinals()
    {
        assertEquals(0, ArenaState.JOIN.ordinal());
        assertEquals(1, ArenaState.STARTING.ordinal());
        assertEquals(2, ArenaState.INGAME.ordinal());
        assertEquals(3, ArenaState.RESTARTING.ordinal());
    }
    
    /**
     * Test the values method
     */
    @Test
    public void testValues()
    {
        final ArenaState[] types = ArenaState.values();
        assertEquals(4, types.length);
        assertEquals(ArenaState.JOIN, types[0]);
        assertEquals(ArenaState.STARTING, types[1]);
        assertEquals(ArenaState.INGAME, types[2]);
        assertEquals(ArenaState.RESTARTING, types[3]);
    }
    
    /**
     * Test the valueOf method
     */
    @Test
    public void testValueOf()
    {
        assertEquals(ArenaState.JOIN, ArenaState.valueOf("JOIN")); //$NON-NLS-1$
        assertEquals(ArenaState.STARTING, ArenaState.valueOf("STARTING")); //$NON-NLS-1$
        assertEquals(ArenaState.INGAME, ArenaState.valueOf("INGAME")); //$NON-NLS-1$
        assertEquals(ArenaState.RESTARTING, ArenaState.valueOf("RESTARTING")); //$NON-NLS-1$
    }
    
}
