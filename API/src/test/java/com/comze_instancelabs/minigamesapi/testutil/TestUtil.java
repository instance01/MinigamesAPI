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

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.After;
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
@PrepareForTest({MinigamesAPI.class})
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
     * Tear down all plugins and players.
     */
    @After
    public void teardownMinigamesAndPlayers()
    {
        this.teardownPlugins();
        this.teardownPlayers();
        teardownConfigFiles();
        this.teardownScoreboards();
        this.teardownTasks();
    }
    
    /**
     * Setup a minigame.
     * @param name minigame plugin name
     * @return the minigame data
     */
    protected Minigame setupMinigame(String name)
    {
        return setupMinigame(name, null);
    }
    
    /**
     * Setup a minigame and perform some setup before loading via minigames api.
     * @param name minigame plugin name
     * @param preSetup setup function
     * @return the minigame data
     */
    protected Minigame setupMinigame(String name, Consumer<Minigame> preSetup)
    {
        final Minigame minigame = new Minigame();
        final YamlConfiguration config = mockFileConfig();
        final JavaPlugin plugin = this.mockPlugin(name, "1.0", config); //$NON-NLS-1$
        minigame.javaPlugin = plugin;
        minigame.achievementsYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "achievements.yml"))); //$NON-NLS-1$
        minigame.arenasYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "arenas.yml"))); //$NON-NLS-1$
        minigame.classesYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "classes.yml"))); //$NON-NLS-1$
        minigame.gunsYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "guns.yml"))); //$NON-NLS-1$
        minigame.hologramsYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "holograms.yml"))); //$NON-NLS-1$
        minigame.messagesYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "messages.yml"))); //$NON-NLS-1$
        minigame.shopYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "shop.yml"))); //$NON-NLS-1$
        minigame.statsYml = setupConfigFile((file) -> file.equals(new File(plugin.getDataFolder(), "stats.yml"))); //$NON-NLS-1$
        if (preSetup != null)
        {
            preSetup.accept(minigame);
        }
        MinigamesAPI.setupAPI(
                plugin,
                name,
                Arena.class,
                new ArenasConfig(plugin),
                new MessagesConfig(plugin),
                new ClassesConfig(plugin, true),
                new StatsConfig(plugin, true),
                new DefaultConfig(plugin, true),
                false);
        minigame.pluginInstance = MinigamesAPI.getAPI().getPluginInstance(plugin);
        return minigame;
    }
    
    /**
     * Helper class for minigame api access.
     * @author mepeisen
     */
    public static final class MinigameTestHelper
    {
        public MinigamesAPI api;
        public Map<String, Minigame> minigames = new HashMap<>();
    }
    
    /**
     * data of a single minigame
     * @author mepeisen
     */
    public static final class Minigame
    {
        public YamlConfiguration statsYml;
        public YamlConfiguration shopYml;
        public YamlConfiguration messagesYml;
        public YamlConfiguration hologramsYml;
        public YamlConfiguration classesYml;
        public YamlConfiguration gunsYml;
        public YamlConfiguration arenasYml;
        public YamlConfiguration achievementsYml;
        public PluginInstance pluginInstance;
        public JavaPlugin javaPlugin;
    }
    
}
