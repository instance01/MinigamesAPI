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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.comze_instancelabs.minigamesapi.MinecraftVersionsType;

/**
 * Test for the version types.
 * 
 * @author mepeisen
 * @see MinecraftVersionsType
 */
public class VersionTypeTest
{
    
    /**
     * tests the ordinal values.
     */
    @Test
    public void testOrdinals()
    {
        assertEquals(0, MinecraftVersionsType.Unknown.ordinal());
        assertEquals(1, MinecraftVersionsType.V1_7.ordinal());
        assertEquals(2, MinecraftVersionsType.V1_7_R1.ordinal());
        assertEquals(3, MinecraftVersionsType.V1_7_R2.ordinal());
        assertEquals(4, MinecraftVersionsType.V1_7_R3.ordinal());
        assertEquals(5, MinecraftVersionsType.V1_7_R4.ordinal());
        assertEquals(6, MinecraftVersionsType.V1_8.ordinal());
        assertEquals(7, MinecraftVersionsType.V1_8_R1.ordinal());
        assertEquals(8, MinecraftVersionsType.V1_8_R2.ordinal());
        assertEquals(9, MinecraftVersionsType.V1_8_R3.ordinal());
        assertEquals(10, MinecraftVersionsType.V1_9.ordinal());
        assertEquals(11, MinecraftVersionsType.V1_9_R1.ordinal());
        assertEquals(12, MinecraftVersionsType.V1_9_R2.ordinal());
        assertEquals(13, MinecraftVersionsType.V1_10.ordinal());
        assertEquals(14, MinecraftVersionsType.V1_10_R1.ordinal());
    }
    
    /**
     * Tests the supported flags.
     */
    @Test
    public void testSupported()
    {
        assertFalse(MinecraftVersionsType.Unknown.isSupported());
        assertTrue(MinecraftVersionsType.V1_7.isSupported());
        assertTrue(MinecraftVersionsType.V1_7_R1.isSupported());
        assertTrue(MinecraftVersionsType.V1_7_R2.isSupported());
        assertTrue(MinecraftVersionsType.V1_7_R3.isSupported());
        assertTrue(MinecraftVersionsType.V1_7_R4.isSupported());
        assertTrue(MinecraftVersionsType.V1_8.isSupported());
        assertTrue(MinecraftVersionsType.V1_8_R1.isSupported());
        assertTrue(MinecraftVersionsType.V1_8_R2.isSupported());
        assertTrue(MinecraftVersionsType.V1_9.isSupported());
        assertTrue(MinecraftVersionsType.V1_9_R1.isSupported());
        assertTrue(MinecraftVersionsType.V1_9_R2.isSupported());
        assertTrue(MinecraftVersionsType.V1_10.isSupported());
        assertTrue(MinecraftVersionsType.V1_10_R1.isSupported());
    }
    
    /**
     * tests the isEqual method
     */
    @Test
    public void testEquals()
    {
        assertTrue(MinecraftVersionsType.V1_7.isEqual(MinecraftVersionsType.V1_7_R1));
        assertTrue(MinecraftVersionsType.V1_7.isEqual(MinecraftVersionsType.V1_7_R2));
        assertTrue(MinecraftVersionsType.V1_7.isEqual(MinecraftVersionsType.V1_7_R3));
        assertTrue(MinecraftVersionsType.V1_7.isEqual(MinecraftVersionsType.V1_7_R4));
        assertTrue(MinecraftVersionsType.V1_7_R1.isEqual(MinecraftVersionsType.V1_7_R1));
        assertTrue(MinecraftVersionsType.V1_7_R1.isEqual(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.V1_7_R2.isEqual(MinecraftVersionsType.V1_7_R2));
        assertTrue(MinecraftVersionsType.V1_7_R2.isEqual(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.V1_7_R3.isEqual(MinecraftVersionsType.V1_7_R3));
        assertTrue(MinecraftVersionsType.V1_7_R3.isEqual(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.V1_7_R4.isEqual(MinecraftVersionsType.V1_7_R4));
        assertTrue(MinecraftVersionsType.V1_7_R4.isEqual(MinecraftVersionsType.V1_7));

        assertTrue(MinecraftVersionsType.V1_8.isEqual(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.V1_8.isEqual(MinecraftVersionsType.V1_8_R2));
        assertTrue(MinecraftVersionsType.V1_8_R1.isEqual(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.V1_8_R1.isEqual(MinecraftVersionsType.V1_8));
        assertTrue(MinecraftVersionsType.V1_8_R2.isEqual(MinecraftVersionsType.V1_8_R2));
        assertTrue(MinecraftVersionsType.V1_8_R2.isEqual(MinecraftVersionsType.V1_8));

        assertTrue(MinecraftVersionsType.V1_9.isEqual(MinecraftVersionsType.V1_9_R1));
        assertTrue(MinecraftVersionsType.V1_9.isEqual(MinecraftVersionsType.V1_9_R2));
        assertTrue(MinecraftVersionsType.V1_9_R1.isEqual(MinecraftVersionsType.V1_9_R1));
        assertTrue(MinecraftVersionsType.V1_9_R1.isEqual(MinecraftVersionsType.V1_9));
        assertTrue(MinecraftVersionsType.V1_9_R2.isEqual(MinecraftVersionsType.V1_9_R2));
        assertTrue(MinecraftVersionsType.V1_9_R2.isEqual(MinecraftVersionsType.V1_9));

        assertTrue(MinecraftVersionsType.V1_10.isEqual(MinecraftVersionsType.V1_10_R1));
        assertTrue(MinecraftVersionsType.V1_10_R1.isEqual(MinecraftVersionsType.V1_10_R1));
        assertTrue(MinecraftVersionsType.V1_10_R1.isEqual(MinecraftVersionsType.V1_10));

        assertTrue(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.Unknown));
        
        assertFalse(MinecraftVersionsType.V1_7.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_8.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_9.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_10.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_7_R1.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_8_R2.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_9_R1.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_10_R1.isEqual(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_9));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_10));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.Unknown.isEqual(MinecraftVersionsType.V1_10_R1));

        assertFalse(MinecraftVersionsType.V1_7.isEqual(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_9.isEqual(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_8.isEqual(MinecraftVersionsType.V1_10));
        assertFalse(MinecraftVersionsType.V1_7_R1.isEqual(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7_R4.isEqual(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.V1_10_R1.isEqual(MinecraftVersionsType.V1_9_R2));
    }
    
    /**
     * tests the isBelow method
     */
    @Test
    public void testIsBelow()
    {
        assertFalse(MinecraftVersionsType.V1_7.isBelow(MinecraftVersionsType.V1_7_R1));
        assertFalse(MinecraftVersionsType.V1_7.isBelow(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7.isBelow(MinecraftVersionsType.V1_7_R3));
        assertFalse(MinecraftVersionsType.V1_7.isBelow(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.V1_7_R1.isBelow(MinecraftVersionsType.V1_7_R1));
        assertFalse(MinecraftVersionsType.V1_7_R1.isBelow(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.V1_7_R2.isBelow(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7_R2.isBelow(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.V1_7_R3.isBelow(MinecraftVersionsType.V1_7_R3));
        assertFalse(MinecraftVersionsType.V1_7_R3.isBelow(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.V1_7_R4.isBelow(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.V1_7_R4.isBelow(MinecraftVersionsType.V1_7));

        assertFalse(MinecraftVersionsType.V1_8.isBelow(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.V1_8.isBelow(MinecraftVersionsType.V1_8_R2));
        assertFalse(MinecraftVersionsType.V1_8_R1.isBelow(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.V1_8_R1.isBelow(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_8_R2.isBelow(MinecraftVersionsType.V1_8_R2));
        assertFalse(MinecraftVersionsType.V1_8_R2.isBelow(MinecraftVersionsType.V1_8));

        assertFalse(MinecraftVersionsType.V1_9.isBelow(MinecraftVersionsType.V1_9_R1));
        assertFalse(MinecraftVersionsType.V1_9.isBelow(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.V1_9_R1.isBelow(MinecraftVersionsType.V1_9_R1));
        assertFalse(MinecraftVersionsType.V1_9_R1.isBelow(MinecraftVersionsType.V1_9));
        assertFalse(MinecraftVersionsType.V1_9_R2.isBelow(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.V1_9_R2.isBelow(MinecraftVersionsType.V1_9));

        assertFalse(MinecraftVersionsType.V1_10.isBelow(MinecraftVersionsType.V1_10_R1));
        assertFalse(MinecraftVersionsType.V1_10_R1.isBelow(MinecraftVersionsType.V1_10_R1));
        assertFalse(MinecraftVersionsType.V1_10_R1.isBelow(MinecraftVersionsType.V1_10));

        assertFalse(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.Unknown));
        
        assertFalse(MinecraftVersionsType.V1_7.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_8.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_9.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_10.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_7_R1.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_8_R2.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_9_R1.isBelow(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.V1_10_R1.isBelow(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_8));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_9));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_10));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_7_R4));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_9_R2));
        assertTrue(MinecraftVersionsType.Unknown.isBelow(MinecraftVersionsType.V1_10_R1));

        assertTrue(MinecraftVersionsType.V1_7.isBelow(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_9.isBelow(MinecraftVersionsType.V1_8));
        assertTrue(MinecraftVersionsType.V1_8.isBelow(MinecraftVersionsType.V1_10));
        assertTrue(MinecraftVersionsType.V1_7_R1.isBelow(MinecraftVersionsType.V1_7_R2));
        assertTrue(MinecraftVersionsType.V1_7_R4.isBelow(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.V1_10_R1.isBelow(MinecraftVersionsType.V1_9_R2));
    }
    
    /**
     * tests the isAfter method
     */
    @Test
    public void testIsAfter()
    {
        assertFalse(MinecraftVersionsType.V1_7.isAfter(MinecraftVersionsType.V1_7_R1));
        assertFalse(MinecraftVersionsType.V1_7.isAfter(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7.isAfter(MinecraftVersionsType.V1_7_R3));
        assertFalse(MinecraftVersionsType.V1_7.isAfter(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.V1_7_R1.isAfter(MinecraftVersionsType.V1_7_R1));
        assertFalse(MinecraftVersionsType.V1_7_R1.isAfter(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.V1_7_R2.isAfter(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7_R2.isAfter(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.V1_7_R3.isAfter(MinecraftVersionsType.V1_7_R3));
        assertFalse(MinecraftVersionsType.V1_7_R3.isAfter(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.V1_7_R4.isAfter(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.V1_7_R4.isAfter(MinecraftVersionsType.V1_7));

        assertFalse(MinecraftVersionsType.V1_8.isAfter(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.V1_8.isAfter(MinecraftVersionsType.V1_8_R2));
        assertFalse(MinecraftVersionsType.V1_8_R1.isAfter(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.V1_8_R1.isAfter(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_8_R2.isAfter(MinecraftVersionsType.V1_8_R2));
        assertFalse(MinecraftVersionsType.V1_8_R2.isAfter(MinecraftVersionsType.V1_8));

        assertFalse(MinecraftVersionsType.V1_9.isAfter(MinecraftVersionsType.V1_9_R1));
        assertFalse(MinecraftVersionsType.V1_9.isAfter(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.V1_9_R1.isAfter(MinecraftVersionsType.V1_9_R1));
        assertFalse(MinecraftVersionsType.V1_9_R1.isAfter(MinecraftVersionsType.V1_9));
        assertFalse(MinecraftVersionsType.V1_9_R2.isAfter(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.V1_9_R2.isAfter(MinecraftVersionsType.V1_9));

        assertFalse(MinecraftVersionsType.V1_10.isAfter(MinecraftVersionsType.V1_10_R1));
        assertFalse(MinecraftVersionsType.V1_10_R1.isAfter(MinecraftVersionsType.V1_10_R1));
        assertFalse(MinecraftVersionsType.V1_10_R1.isAfter(MinecraftVersionsType.V1_10));

        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.Unknown));
        
        assertTrue(MinecraftVersionsType.V1_7.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_8.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_9.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_10.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_7_R1.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_8_R2.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_9_R1.isAfter(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_10_R1.isAfter(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_9));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_10));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.Unknown.isAfter(MinecraftVersionsType.V1_10_R1));

        assertFalse(MinecraftVersionsType.V1_7.isAfter(MinecraftVersionsType.V1_8));
        assertTrue(MinecraftVersionsType.V1_9.isAfter(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_8.isAfter(MinecraftVersionsType.V1_10));
        assertFalse(MinecraftVersionsType.V1_7_R1.isAfter(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7_R4.isAfter(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.V1_10_R1.isAfter(MinecraftVersionsType.V1_9_R2));
    }
    
    /**
     * tests the isAtLeast method
     */
    @Test
    public void testIsAtLeast()
    {
        assertTrue(MinecraftVersionsType.V1_7.isAtLeast(MinecraftVersionsType.V1_7_R1));
        assertTrue(MinecraftVersionsType.V1_7.isAtLeast(MinecraftVersionsType.V1_7_R2));
        assertTrue(MinecraftVersionsType.V1_7.isAtLeast(MinecraftVersionsType.V1_7_R3));
        assertTrue(MinecraftVersionsType.V1_7.isAtLeast(MinecraftVersionsType.V1_7_R4));
        assertTrue(MinecraftVersionsType.V1_7_R1.isAtLeast(MinecraftVersionsType.V1_7_R1));
        assertTrue(MinecraftVersionsType.V1_7_R1.isAtLeast(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.V1_7_R2.isAtLeast(MinecraftVersionsType.V1_7_R2));
        assertTrue(MinecraftVersionsType.V1_7_R2.isAtLeast(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.V1_7_R3.isAtLeast(MinecraftVersionsType.V1_7_R3));
        assertTrue(MinecraftVersionsType.V1_7_R3.isAtLeast(MinecraftVersionsType.V1_7));
        assertTrue(MinecraftVersionsType.V1_7_R4.isAtLeast(MinecraftVersionsType.V1_7_R4));
        assertTrue(MinecraftVersionsType.V1_7_R4.isAtLeast(MinecraftVersionsType.V1_7));

        assertTrue(MinecraftVersionsType.V1_8.isAtLeast(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.V1_8.isAtLeast(MinecraftVersionsType.V1_8_R2));
        assertTrue(MinecraftVersionsType.V1_8_R1.isAtLeast(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.V1_8_R1.isAtLeast(MinecraftVersionsType.V1_8));
        assertTrue(MinecraftVersionsType.V1_8_R2.isAtLeast(MinecraftVersionsType.V1_8_R2));
        assertTrue(MinecraftVersionsType.V1_8_R2.isAtLeast(MinecraftVersionsType.V1_8));

        assertTrue(MinecraftVersionsType.V1_9.isAtLeast(MinecraftVersionsType.V1_9_R1));
        assertTrue(MinecraftVersionsType.V1_9.isAtLeast(MinecraftVersionsType.V1_9_R2));
        assertTrue(MinecraftVersionsType.V1_9_R1.isAtLeast(MinecraftVersionsType.V1_9_R1));
        assertTrue(MinecraftVersionsType.V1_9_R1.isAtLeast(MinecraftVersionsType.V1_9));
        assertTrue(MinecraftVersionsType.V1_9_R2.isAtLeast(MinecraftVersionsType.V1_9_R2));
        assertTrue(MinecraftVersionsType.V1_9_R2.isAtLeast(MinecraftVersionsType.V1_9));

        assertTrue(MinecraftVersionsType.V1_10.isAtLeast(MinecraftVersionsType.V1_10_R1));
        assertTrue(MinecraftVersionsType.V1_10_R1.isAtLeast(MinecraftVersionsType.V1_10_R1));
        assertTrue(MinecraftVersionsType.V1_10_R1.isAtLeast(MinecraftVersionsType.V1_10));

        assertTrue(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.Unknown));
        
        assertTrue(MinecraftVersionsType.V1_7.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_8.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_9.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_10.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_7_R1.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_8_R2.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_9_R1.isAtLeast(MinecraftVersionsType.Unknown));
        assertTrue(MinecraftVersionsType.V1_10_R1.isAtLeast(MinecraftVersionsType.Unknown));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_7));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_9));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_10));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_7_R4));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_8_R1));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_9_R2));
        assertFalse(MinecraftVersionsType.Unknown.isAtLeast(MinecraftVersionsType.V1_10_R1));

        assertFalse(MinecraftVersionsType.V1_7.isAtLeast(MinecraftVersionsType.V1_8));
        assertTrue(MinecraftVersionsType.V1_9.isAtLeast(MinecraftVersionsType.V1_8));
        assertFalse(MinecraftVersionsType.V1_8.isAtLeast(MinecraftVersionsType.V1_10));
        assertFalse(MinecraftVersionsType.V1_7_R1.isAtLeast(MinecraftVersionsType.V1_7_R2));
        assertFalse(MinecraftVersionsType.V1_7_R4.isAtLeast(MinecraftVersionsType.V1_8_R1));
        assertTrue(MinecraftVersionsType.V1_10_R1.isAtLeast(MinecraftVersionsType.V1_9_R2));
    }
    
}
