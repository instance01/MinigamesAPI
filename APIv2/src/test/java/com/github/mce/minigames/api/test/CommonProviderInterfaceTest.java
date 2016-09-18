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

package com.github.mce.minigames.api.test;

import static org.junit.Assert.assertNull;

import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Test;

import com.github.mce.minigames.api.CommonProviderInterface;

/**
 * Test for {@link CommonProviderInterface}.
 * 
 * @author mepeisen
 */
public class CommonProviderInterfaceTest
{
    
    /**
     * tests default methods.
     */
    @Test
    public void testDefaults()
    {
        final CommonProviderInterface provider = new CommonProviderInterface() {
            
            @Override
            public JavaPlugin getJavaPlugin()
            {
                return null;
            }
        };
        
        assertNull(provider.getAdminRuleIds());
        assertNull(provider.getArenaRuleIds());
        assertNull(provider.getBukkitCommands());
        assertNull(provider.getComponentIds());
        assertNull(provider.getComponentRuleIds());
        assertNull(provider.getConfigurations());
        assertNull(provider.getGuiIds());
        assertNull(provider.getMatchPhaseIds());
        assertNull(provider.getMatchRuleIds());
        assertNull(provider.getMessageClasses());
        assertNull(provider.getPermissions());
        assertNull(provider.getPlayerRuleIds());
        assertNull(provider.getTeamIds());
        assertNull(provider.getTeamRuleIds());
    }
    
}
