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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.junit.Test;

import com.comze_instancelabs.minigamesapi.SmartReset;

/**
 * Test case for smart resets nested iterator
 * 
 * @author mepeisen
 */
public class NestedIteratorTest
{
    
    /**
     * Tests an empty nested map.
     */
    @Test
    public void testEmpty()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        assertFalse(iter.hasNext());
    }
    
    /**
     * Tests an empty nested map.
     */
    @Test(expected=NoSuchElementException.class)
    public void testEmpty2()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.next();
    }
    
    /**
     * Tests an empty nested map.
     */
    @Test(expected=IllegalStateException.class)
    public void testEmpty3()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.remove();
    }
    
    /**
     * Tests a simple map.
     */
    @Test
    public void testSimple()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        map1.put(Integer.valueOf(10), Integer.valueOf(10));
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(10), iter.next());
        assertFalse(iter.hasNext());
    }
    
    /**
     * Tests a simple map.
     */
    @Test(expected=NoSuchElementException.class)
    public void testSimple2()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        map1.put(Integer.valueOf(10), Integer.valueOf(10));
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.next();
        iter.next();
    }
    
    /**
     * Tests a simple map.
     */
    @Test(expected=NoSuchElementException.class)
    public void testSimple3()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        map1.put(Integer.valueOf(10), Integer.valueOf(10));
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.next();
        iter.remove();
        assertEquals(0, map1.size());
        iter.next();
    }
    
    /**
     * Tests a simple map.
     */
    @Test(expected=IllegalStateException.class)
    public void testSimple4()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        map1.put(Integer.valueOf(10), Integer.valueOf(10));
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.remove();
    }
    
    /**
     * Tests a simple map.
     */
    @Test(expected=IllegalStateException.class)
    public void testSimple5()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        map1.put(Integer.valueOf(10), Integer.valueOf(10));
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.next();
        iter.remove();
        iter.remove();
    }
    
    /**
     * Tests a empty nested map.
     */
    @Test
    public void testNestEmpty()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        assertFalse(iter.hasNext());
    }
    
    /**
     * Tests a empty nested map.
     */
    @Test(expected=NoSuchElementException.class)
    public void testNestEmpty2()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.next();
    }
    
    /**
     * Tests a empty nested map.
     */
    @Test(expected=IllegalStateException.class)
    public void testNestEmpty3()
    {
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        final Map<Integer, Integer> map1 = new HashMap<>();
        map.put(Integer.valueOf(10), map1);
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        iter.remove();
    }
    
    /**
     * Tests a complex nested map.
     */
    @Test
    public void testNestComplex()
    {
        final Map<Integer, Map<Integer, Integer>> map = new TreeMap<>();
        final Map<Integer, Integer> map1 = new TreeMap<>();
        final Map<Integer, Integer> map2 = new TreeMap<>();
        final Map<Integer, Integer> map3 = new TreeMap<>();
        map.put(Integer.valueOf(10), map1);
        map.put(Integer.valueOf(20), map2);
        map.put(Integer.valueOf(30), map3);
        map1.put(Integer.valueOf(10), Integer.valueOf(10));
        map1.put(Integer.valueOf(11), Integer.valueOf(11));
        map1.put(Integer.valueOf(12), Integer.valueOf(12));
        map3.put(Integer.valueOf(30), Integer.valueOf(30));
        map3.put(Integer.valueOf(31), Integer.valueOf(31));
        
        final Iterator<Integer> iter = new SmartReset.NestedIterator<>(map.values().iterator());
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(10), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(11), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(12), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(30), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(31), iter.next());
        assertFalse(iter.hasNext());
    }
    
}
