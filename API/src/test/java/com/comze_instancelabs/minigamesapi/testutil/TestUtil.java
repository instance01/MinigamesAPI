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

package com.comze_instancelabs.minigamesapi.testutil;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.spigottest.SpigotTestSupport;

/**
 * Test tooling for minigames API.
 * 
 * @author mepeisen
 */
@PrepareForTest({ MinigamesAPI.class })
public abstract class TestUtil extends SpigotTestSupport
{
    
    /** test helper. */
    protected MinigameTestHelper minigameTest;
    
    /**
     * Setup the minigame test framework.
     */
    @Before
    public void setupMinigameTest()
    {
        this.minigameTest = new MinigameTestHelper();
        
        final MinigamesAPI api = mock(MinigamesAPI.class);
        mockStatic(MinigamesAPI.class);
        when(MinigamesAPI.getAPI()).thenReturn(api);
        when(api.getPluginInstance(any())).thenCallRealMethod();
        when(MinigamesAPI.setupAPI(any(), anyString(), any(), any(), any(), any(), any(), any(), anyBoolean())).thenCallRealMethod();
        
        this.minigameTest.api = api;
    }
    
    /**
     * Helper class for minigame api access.
     * 
     * @author mepeisen
     */
    public final class MinigameTestHelper
    {
        /** the minigames api. */
        public MinigamesAPI          api;
        /** hashmap with minigames by internal name. */
        public Map<String, Minigame> minigames = new HashMap<>();
        
        /**
         * Setup a minigame.
         * 
         * @param name
         *            minigame plugin name
         * @return the minigame data
         */
        public Minigame setupMinigame(String name)
        {
            return setupMinigame(name, null);
        }
        
        /**
         * Setup a minigame and perform some setup before loading via minigames api.
         * 
         * @param name
         *            minigame plugin name
         * @param preSetup
         *            setup function
         * @return the minigame data
         */
        public Minigame setupMinigame(String name, Consumer<Minigame> preSetup)
        {
            final Minigame minigame = new Minigame();
            this.minigames.put(name, minigame);
            final YamlConfiguration config = mockFileConfig();
            final JavaPlugin plugin = mockPlugin(name, "1.0", config); //$NON-NLS-1$
            minigame.javaPlugin = plugin;
            minigame.achievementsYml = setupConfigFile(new File(plugin.getDataFolder(), "achievements.yml")); //$NON-NLS-1$
            minigame.arenasYml = setupConfigFile(new File(plugin.getDataFolder(), "arenas.yml")); //$NON-NLS-1$
            minigame.classesYml = setupConfigFile(new File(plugin.getDataFolder(), "classes.yml")); //$NON-NLS-1$
            minigame.gunsYml = setupConfigFile(new File(plugin.getDataFolder(), "guns.yml")); //$NON-NLS-1$
            minigame.hologramsYml = setupConfigFile(new File(plugin.getDataFolder(), "holograms.yml")); //$NON-NLS-1$
            minigame.messagesYml = setupConfigFile(new File(plugin.getDataFolder(), "messages.yml")); //$NON-NLS-1$
            minigame.shopYml = setupConfigFile(new File(plugin.getDataFolder(), "shop.yml")); //$NON-NLS-1$
            minigame.statsYml = setupConfigFile(new File(plugin.getDataFolder(), "stats.yml")); //$NON-NLS-1$
            if (preSetup != null)
            {
                preSetup.accept(minigame);
            }
            MinigamesAPI.setupAPI(plugin, name, Arena.class, new ArenasConfig(plugin), new MessagesConfig(plugin), new ClassesConfig(plugin, true), new StatsConfig(plugin, true),
                    new DefaultConfig(plugin, true), false);
            minigame.pluginInstance = MinigamesAPI.getAPI().getPluginInstance(plugin);
            return minigame;
        }
    }
    
    /**
     * data of a single minigame
     * 
     * @author mepeisen
     */
    public static final class Minigame
    {
        /** stats.yml */
        public YamlConfiguration statsYml;
        /** shop.yml */
        public YamlConfiguration shopYml;
        /** messages.yml */
        public YamlConfiguration messagesYml;
        /** holograms.yml */
        public YamlConfiguration hologramsYml;
        /** classes.yml */
        public YamlConfiguration classesYml;
        /** guns.yml */
        public YamlConfiguration gunsYml;
        /** arenas.yml */
        public YamlConfiguration arenasYml;
        /** achievements.yml */
        public YamlConfiguration achievementsYml;
        /** this minigame plugin instance. */
        public PluginInstance    pluginInstance;
        /** the minigame java plugin. */
        public JavaPlugin        javaPlugin;
        
        /**
         * Adds a location component to config
         * 
         * @param arenaName
         *            arena name
         * @param component
         *            component path (f.e. "lobby")
         * @param world
         *            the world
         * @param x
         *            the x coordinate
         * @param y
         *            the y coordinate
         * @param z
         *            the z coordinate
         * @param pitch
         *            the pitch
         * @param yaw
         *            the yaw
         */
        public void addArenaComponentToConfig(String arenaName, String component, String world, double x, double y, double z, double pitch, double yaw)
        {
            ConfigurationSection arenas = this.arenasYml.getConfigurationSection("arenas"); //$NON-NLS-1$
            if (arenas == null)
            {
                arenas = this.arenasYml.createSection("arenas"); //$NON-NLS-1$
            }
            
            ConfigurationSection carena = arenas.getConfigurationSection(arenaName);
            if (carena == null)
            {
                carena = arenas.createSection(arenaName);
            }
            
            ConfigurationSection ccomponent = carena;
            for (String path : component.split("\\.")) //$NON-NLS-1$
            {
                ConfigurationSection csect = ccomponent.getConfigurationSection(path);
                if (csect == null)
                {
                    csect = ccomponent.createSection(path);
                }
                ccomponent = csect;
            }
            
            ccomponent.set("world", world); //$NON-NLS-1$
            final Map<String, Object> location = new HashMap<>();
            location.put("x", Double.valueOf(x)); //$NON-NLS-1$
            location.put("y", Double.valueOf(y)); //$NON-NLS-1$
            location.put("z", Double.valueOf(z)); //$NON-NLS-1$
            location.put("pitch", Double.valueOf(pitch)); //$NON-NLS-1$
            location.put("yaw", Double.valueOf(yaw)); //$NON-NLS-1$
            ccomponent.createSection("location", location); //$NON-NLS-1$
        }
    }
    
}
