/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.minigameslib.mclib.spigottest.SpigotInject;
import de.minigameslib.mclib.spigottest.SpigotJunit4Runner;
import de.minigameslib.mclib.spigottest.SpigotServer;
import de.minigameslib.mclib.spigottest.SpigotTest;

/**
 * Test case for arena impl.
 * 
 * @author mepeisen
 */
@RunWith(SpigotJunit4Runner.class)
@SpigotTest(all = true)
public class ArenaImplTest
{
    
    /**
     * the spigot server.
     */
    @SpigotInject
    private SpigotServer server;
    
    /**
     * Loads the test plugin.
     */
    @Before
    public void loadPlugin()
    {
        // TODO assertTrue(this.server.loadTestPlugin("JUNIT", TestPlugin.class)); //$NON-NLS-1$
    }
    
    /**
     * Tests constructor to create new arenas
     */
    @Test
    public void testConstructorNew()
    {
        // TODO test case
    }
    
}
