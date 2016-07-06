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

package com.comze_instancelabs.minigamesapi.spigottest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import net.minecraft.server.v1_10_R1.DispenserRegistry;

/**
 * Test tooling for minigames API.
 * 
 * @author mepeisen
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(YamlConfiguration.class)
@PowerMockIgnore({ "org.apache.*", "com.sun.*", "javax.*" })
public abstract class SpigotTestSupport
{
    
    /**
     * Initializes the dummy server.
     */
    @Before
    public void setupServer()
    {
        DispenserRegistry.c();
        DummyServer.setup();
        DummyEnchantments.setup();
        mockStatic(YamlConfiguration.class);
        when(YamlConfiguration.loadConfiguration(any(File.class))).thenAnswer(new Answer<YamlConfiguration>() {
            
            @Override
            public YamlConfiguration answer(InvocationOnMock invocation) throws Throwable
            {
                final File arg = invocation.getArgumentAt(0, File.class);
                return getDummyServer().getConfigFiles().get(arg);
            }
            
        });
    }

    
    /**
     * Tear down all plugins and players.
     */
    @After
    public void teardownMinigamesAndPlayers()
    {
        this.teardownPlugins();
        this.teardownPlayers();
        this.teardownConfigFiles();
        this.teardownScoreboards();
        this.teardownTasks();
        this.teardownWorlds();
    }
    
    /**
     * Initializes a new dummy world.
     * @param name world name
     * @return World instance.
     */
    public World initWorld(String name)
    {
        return getDummyServer().initWorld(name);
    }
    
    /**
     * Returns the dummy server.
     * 
     * @return summy server
     */
    static DummyServer getDummyServer()
    {
        final Server server = Bukkit.getServer();
        return (DummyServer) Proxy.getInvocationHandler(server);
    }
    
    /**
     * Setup a config file for being returned by {@link YamlConfiguration#load(File)}
     * 
     * @param file
     *            the file path
     * @return config file
     */
    public static YamlConfiguration setupConfigFile(File file)
    {
        final YamlConfiguration config = mockFileConfig();
        getDummyServer().getConfigFiles().put(file, config);
        file.deleteOnExit();
        return config;
    }
    
    /**
     * Tear down all config files.
     */
    protected void teardownConfigFiles()
    {
        getDummyServer().getConfigFiles().clear();
    }
    
    /**
     * Removes all plugins.
     */
    protected void teardownPlugins()
    {
        Bukkit.getServer().getPluginManager().clearPlugins();
    }
    
    /**
     * Removes all players.
     */
    protected void teardownPlayers()
    {
        getDummyServer().clearPlayers();
    }
    
    /**
     * Removes all players.
     */
    protected void teardownWorlds()
    {
        getDummyServer().clearWorlds();
    }
    
    /**
     * Teardown the score board support.
     */
    protected void teardownScoreboards()
    {
        ((DummyScoreboardManager) Bukkit.getScoreboardManager()).teardown();
    }
    
    /**
     * Teardown the server tasks and ticks.
     */
    protected void teardownTasks()
    {
        this.getDummyServer().clearTicks();
    }
    
    /**
     * Mocks a player and adds it to the server.
     * 
     * @param name
     *            players name
     * @param uuid
     *            unique player id.
     * @return mocked player object.
     */
    protected Player mockOnlinePlayer(String name, UUID uuid)
    {
        final DummyServer server = this.getDummyServer();
        final Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        when(player.getUniqueId()).thenReturn(uuid);
        server.addMockedPlayer(player);
        return player;
    }
    
    /**
     * mocks a file configuration to not load/write from/to disk.
     * 
     * @return yaml file configuration
     */
    public static YamlConfiguration mockFileConfig()
    {
        final YamlConfiguration config = spy(YamlConfiguration.class);
        try
        {
            doNothing().when(config).load(any(File.class));
            doNothing().when(config).load(any(InputStream.class));
            doNothing().when(config).load(any(Reader.class));
            doNothing().when(config).load(anyString());
            doNothing().when(config).loadFromString(anyString());
            doNothing().when(config).save(any(File.class));
            doNothing().when(config).save(anyString());
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore; should never happen because we config a mock here.
        }
        return config;
    }
    
    /**
     * Mocks a plugin and returns it.
     * 
     * @param name
     *            plugin name
     * @param version
     *            plugin version
     * @param config
     *            plugin config
     * @return mocked java plugin.
     */
    public JavaPlugin mockPlugin(String name, String version, FileConfiguration config)
    {
        final JavaPlugin plugin = mock(JavaPlugin.class, (Answer<?>) invocation -> {
            if (invocation.getMethod().getName().equals("getResource")) //$NON-NLS-1$
                return null;
            return invocation.callRealMethod();
        });
        final PluginLogger logger = mock(PluginLogger.class);
        final File dataFolder = new File(".$" + name); //$NON-NLS-1$
        final PluginDescriptionFile description = new PluginDescriptionFile(name, version, "foo"); //$NON-NLS-1$
        Whitebox.setInternalState(plugin, "newConfig", config); //$NON-NLS-1$
        Whitebox.setInternalState(plugin, "logger", logger); //$NON-NLS-1$
        Whitebox.setInternalState(plugin, "dataFolder", dataFolder); //$NON-NLS-1$
        Whitebox.setInternalState(plugin, "description", description); //$NON-NLS-1$
        Whitebox.setInternalState(plugin, "isEnabled", true); //$NON-NLS-1$
        
        ((DummyPluginManager) Bukkit.getPluginManager()).addMockedPlugin(plugin);
        
        return plugin;
    }
    
    /**
     * Performs the main tick.
     */
    protected void tick()
    {
        this.getDummyServer().tick();
    }
    
}
