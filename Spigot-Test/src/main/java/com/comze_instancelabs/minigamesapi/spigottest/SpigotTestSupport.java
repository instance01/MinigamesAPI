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

import static org.junit.Assert.fail;
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
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
import org.spigotmc.SpigotConfig;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.minecraft.server.v1_12_R1.DispenserRegistry;

/**
 * Test tooling for minigames API.
 * 
 * @author mepeisen
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ YamlConfiguration.class, CraftServer.class, CraftScoreboardManager.class, CraftScoreboard.class })
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
        SpigotConfig.config = mockFileConfig();
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
        this.teardownMessages();
    }
    
    /**
     * Initializes a new dummy world.
     * 
     * @param name
     *            world name
     * @return World instance.
     */
    public World initWorld(String name)
    {
        return getDummyServer().initWorld(name);
    }
    
    /**
     * Initializes a flat world hold in memory.
     * 
     * @param name
     *            world name
     * @return world instance.
     */
    public World initFlatWorld(String name)
    {
        return getDummyServer().initWorld(name, WorldType.FLAT, 123);
    }
    
    /**
     * Returns the dummy server.
     * 
     * @return dummy server
     */
    static DummyServer getDummyServer()
    {
//        final Server server = Bukkit.getServer();
//        return (DummyServer) Proxy.getInvocationHandler(server);
        return DummyServer.DUMMY_SERVER;
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
     * Tear down all messages.
     */
    protected void teardownMessages()
    {
        getDummyServer().clearMessages();
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
        getDummyServer().teardownScoreboards();
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
        final DummyServer server = getDummyServer();
        final Player player = mock(Player.class);
        final PlayerInventory inv = new DummyPlayerInventory();
        when(player.getName()).thenReturn(name);
        when(player.getUniqueId()).thenReturn(uuid);
        server.addMockedPlayer(player);
        when(player.getInventory()).thenReturn(inv);
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
        return mockPlugin(name, version, config, JavaPlugin.class, null);
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
     * @param clazz
     *            plugin class
     * @param answers
     *            additional answers for mocking
     * @return mocked java plugin.
     */
    public <T extends JavaPlugin> T mockPlugin(String name, String version, FileConfiguration config, Class<T> clazz, Map<Method, Answer<?>> answers)
    {
        final T plugin = mock(clazz, (Answer<?>) invocation -> {
            if (answers != null && answers.containsKey(invocation.getMethod()))
            {
                return answers.get(invocation.getMethod()).answer(invocation);
            }
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
        getDummyServer().tick();
    }
    
    /**
     * Assert that a given message was sent.
     * 
     * @param senderPlugin
     * @param channel
     * @param message
     */
    public static void verifyPluginMessage(JavaPlugin senderPlugin, String channel, Object... message)
    {
        final byte[] bytes = toByteArray(message);
        final PluginMessage msg = new PluginMessage(senderPlugin, channel, bytes);
        
        final StringBuilder builder = new StringBuilder();
        builder.append("Expected plugin message not sent.\nplugin: ").append(senderPlugin.getName()); //$NON-NLS-1$
        builder.append("\nchannel: ").append(channel); //$NON-NLS-1$
        for (Object obj : message)
        {
            builder.append("\n  arg:").append(obj); //$NON-NLS-1$
        }
        builder.append("\n  bytes: ").append(Arrays.toString(bytes)); //$NON-NLS-1$
        
        for (final PluginMessage sent : getDummyServer().getMessages())
        {
            if (sent.equals(msg))
            {
                // we found the message
                return;
            }
            builder.append("\n candidate: ").append(sent.getPlugin().getName()).append("/").append(sent.getChannel()).append("/").append(Arrays.toString(sent.getData())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        fail(builder.toString());
    }
    
    private static byte[] toByteArray(Object... message)
    {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (Object obj : message)
        {
            if (obj instanceof String)
            {
                out.writeUTF((String) obj);
            }
            else if (obj instanceof Boolean)
            {
                out.writeBoolean((Boolean) obj);
            }
            else if (obj instanceof Byte)
            {
                out.writeByte((Byte) obj);
            }
            else if (obj instanceof Character)
            {
                out.writeChar((Character) obj);
            }
            else if (obj instanceof Double)
            {
                out.writeDouble((Double) obj);
            }
            else if (obj instanceof Float)
            {
                out.writeFloat((Float) obj);
            }
            else if (obj instanceof Integer)
            {
                out.writeInt((Integer) obj);
            }
            else if (obj instanceof Long)
            {
                out.writeLong((Long) obj);
            }
            else if (obj instanceof Short)
            {
                out.writeShort((Short) obj);
            }
            else if (obj instanceof Object[])
            {
                final byte[] arr = toByteArray((Object[]) obj);
                out.writeShort(arr.length);
                out.write(arr);
            }
            else
            {
                fail("Unknown object type for assert message"); //$NON-NLS-1$
            }
        }
        final byte[] bytes = out.toByteArray();
        return bytes;
    }
    
}
