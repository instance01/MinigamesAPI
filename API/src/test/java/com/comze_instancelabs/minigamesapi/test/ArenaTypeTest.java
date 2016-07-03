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

import com.comze_instancelabs.minigamesapi.ArenaType;

/**
 * Test case for arena types enumeration.
 * 
 * @author mepeisen
 * 
 * @see ArenaType
 */
public class ArenaTypeTest
{
    
    /**
     * Test the ordinals
     */
    @Test
    public void testOrdinals()
    {
        assertEquals(0, ArenaType.DEFAULT.ordinal());
        assertEquals(1, ArenaType.JUMPNRUN.ordinal());
        assertEquals(2, ArenaType.REGENERATION.ordinal());
    }
    
    /**
     * Test the values method
     */
    @Test
    public void testValues()
    {
        final ArenaType[] types = ArenaType.values();
        assertEquals(3, types.length);
        assertEquals(ArenaType.DEFAULT, types[0]);
        assertEquals(ArenaType.JUMPNRUN, types[1]);
        assertEquals(ArenaType.REGENERATION, types[2]);
    }
    
    /**
     * Test the valueOf method
     */
    @Test
    public void testValueOf()
    {
        assertEquals(ArenaType.DEFAULT, ArenaType.valueOf("DEFAULT")); //$NON-NLS-1$
        assertEquals(ArenaType.JUMPNRUN, ArenaType.valueOf("JUMPNRUN")); //$NON-NLS-1$
        assertEquals(ArenaType.REGENERATION, ArenaType.valueOf("REGENERATION")); //$NON-NLS-1$
    }
    
}
