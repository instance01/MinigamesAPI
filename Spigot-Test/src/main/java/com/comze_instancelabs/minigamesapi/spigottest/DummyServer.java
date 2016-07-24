package com.comze_instancelabs.minigamesapi.spigottest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_10_R1.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_10_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_10_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_10_R1.util.Versioning;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.DedicatedPlayerList;
import net.minecraft.server.v1_10_R1.DefinedStructureManager;
import net.minecraft.server.v1_10_R1.EnumGamemode;
import net.minecraft.server.v1_10_R1.ExceptionWorldConflict;
import net.minecraft.server.v1_10_R1.IChunkLoader;
import net.minecraft.server.v1_10_R1.IDataManager;
import net.minecraft.server.v1_10_R1.IPlayerFileData;
import net.minecraft.server.v1_10_R1.MethodProfiler;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.PlayerChunkMap;
import net.minecraft.server.v1_10_R1.PlayerList;
import net.minecraft.server.v1_10_R1.WorldData;
import net.minecraft.server.v1_10_R1.WorldProvider;
import net.minecraft.server.v1_10_R1.WorldServer;
import net.minecraft.server.v1_10_R1.WorldSettings;

/**
 * originally taken from spigot test sources
 */
class DummyServer implements InvocationHandler, Answer {
    /**
     * 
     */
    static final DummyServer DUMMY_SERVER = new DummyServer();

    private static interface MethodHandler {
        Object handle(DummyServer server, Object[] args);
    }
    private static final HashMap<Method, MethodHandler> methods = new HashMap<Method, MethodHandler>();
    static {
        try {
            methods.put(
                    CraftServer.class.getMethod("addWorld", World.class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            // silently ignore
                            return null;
                        }
                    });
            methods.put(
                    CraftServer.class.getMethod("getHandle"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return server.playerList;
                        }
                    });
            methods.put(
                    CraftServer.class.getMethod("getServer"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return null;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getViewDistance"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return 10;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getTicksPerAnimalSpawns"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return 50;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getTicksPerMonsterSpawns"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return 50;
                        }
                    });
            methods.put(
                    Server.class.getMethod("sendPluginMessage", Plugin.class, String.class, byte[].class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            server.messages.add(new PluginMessage((Plugin) args[0], (String) args[1], (byte[]) args[2]));
                            return null;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getPluginManager"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return server.pluginManager;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getScoreboardManager"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return server.scoreboardManager;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getOnlinePlayers"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return Collections.unmodifiableList(server.onlineList);
                        }
                    });
            methods.put(
                    Server.class.getMethod("getScheduler"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return server.scheduler;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getWorld", String.class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return server.worlds.get((String) args[0]);
                        }
                    });
            methods.put(
                    Server.class.getMethod("getPlayer", String.class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            for (final Player player : server.onlineList)
                            {
                                if (args[0].toString().equalsIgnoreCase(player.getName()))
                                {
                                    return player;
                                }
                            }
                            return null;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getPlayer", UUID.class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            for (final Player player : server.onlineList)
                            {
                                if (((UUID)args[0]).equals(player.getUniqueId()))
                                {
                                    return player;
                                }
                            }
                            return null;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getPlayerExact", String.class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            for (final Player player : server.onlineList)
                            {
                                if (args[0].toString().equals(player.getName()))
                                {
                                    return player;
                                }
                            }
                            return null;
                        }
                    });
            methods.put(
                    Server.class.getMethod("matchPlayer", String.class),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            final List<Player> result = new ArrayList<>();
                            for (final Player player : server.onlineList)
                            {
                                if (args[0].toString().equals(player.getName()))
                                {
                                    return Collections.singletonList(player);
                                }
                                if (player.getName().toLowerCase().contains(args[0].toString().toLowerCase()))
                                {
                                    result.add(player);
                                }
                            }
                            return result;
                        }
                    });
            methods.put(
                    Server.class.getMethod("getItemFactory"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return CraftItemFactory.instance();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getName"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return DummyServer.class.getName();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getVersion"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return DummyServer.class.getPackage().getImplementationVersion();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getBukkitVersion"),
                    new MethodHandler() {
                        public Object handle(DummyServer server, Object[] args) {
                            return Versioning.getBukkitVersion();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getLogger"),
                    new MethodHandler() {
                        final Logger logger = Logger.getLogger(DummyServer.class.getCanonicalName());
                        public Object handle(DummyServer server, Object[] args) {
                            return logger;
                        }
                    }
                );
            final CraftServer server = mock(CraftServer.class, DUMMY_SERVER);
            Bukkit.setServer(server);
            //Bukkit.setServer(Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class).newInstance(DUMMY_SERVER));
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {
        // dummy
    }
    
    private List<Player> onlineList = new ArrayList<>();
    private DummyPluginManager pluginManager = new DummyPluginManager();
    private CraftScoreboardManager scoreboardManager;
    private CraftScoreboard mainScoreboard = mock(CraftScoreboard.class);
    private CraftScheduler scheduler = new CraftScheduler();
    private Map<String, World> worlds = new HashMap<>();
    private List<PluginMessage> messages = new ArrayList<>();
    private DedicatedPlayerList playerList = mock(DedicatedPlayerList.class);
    
    private final Map<File, YamlConfiguration> configFiles = new HashMap<>();
    
    private int tick = 1;

    private DummyServer() {
        when(this.playerList.d()).thenReturn(PlayerChunkMap.getFurthestViewableBlock(10));
        this.mainScoreboard = mockScoreboard();
        this.scoreboardManager = mock(CraftScoreboardManager.class);
        when(this.scoreboardManager.getMainScoreboard()).thenReturn(this.mainScoreboard);
        when(this.scoreboardManager.getNewScoreboard()).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return mockScoreboard();
            }
        });
    }
    
    CraftScoreboard mockScoreboard()
    {
        final CraftScoreboard result = mock(CraftScoreboard.class);
        final Map<String, Team> teams = new HashMap<>();
        final Map<String, List<Objective>> objetivesByCriteria = new HashMap<>();
        final Map<String, Objective> objectiveByName = new HashMap<>();
        when(result.getTeam(anyString())).thenAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return teams.get(invocation.getArgumentAt(0, String.class));
            }});
        when(result.getTeams()).thenAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return new HashSet<>(teams.values());
            }});
        when(result.registerNewTeam(anyString())).thenAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                final Team team = new DummyScoreboardTeam();
                teams.put(invocation.getArgumentAt(0,  String.class), team);
                return team;
            }});
        when(result.registerNewObjective(anyString(), anyString())).thenAnswer(new Answer(){
            
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                final String name = invocation.getArgumentAt(0, String.class);
                final String criteria = invocation.getArgumentAt(1, String.class);
                if (objectiveByName.containsKey(name))
                {
                    throw new IllegalArgumentException("An objective with the name \'" + name + "\' already exists!");
                }
                final Objective obj = new DummyObjective(name);
                List<Objective> list = objetivesByCriteria.get(criteria);
                if (list == null)
                {
                    list = new ArrayList<>();
                    objetivesByCriteria.put(criteria, list);
                }
                list.add(obj);
                objectiveByName.put(name, obj);
                return obj;
            }});
        when(result.getObjective(anyString())).thenAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return objectiveByName.get(invocation.getArgumentAt(0, String.class));
            }});

        when(result.getObjectivesByCriteria(anyString())).thenAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                final String criteria = invocation.getArgumentAt(0, String.class);
                if (objetivesByCriteria.containsKey(criteria))
                {
                    return new HashSet<>(objetivesByCriteria.get(criteria));
                }
                return Collections.emptySet();
            }});

        when(result.getObjectives()).thenAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return new HashSet<>(objectiveByName.values());
            }});
        return result;
    }
    
    void teardownScoreboards()
    {
        this.mainScoreboard = mockScoreboard();
    }
    
    void addMockedPlayer(Player player)
    {
        this.onlineList.add(player);
    }
    
    void tick()
    {
        this.scheduler.mainThreadHeartbeat(this.tick);
        this.tick++;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable
    {
        Method method = invocation.getMethod();
        try
        {
            method = Server.class.getMethod(method.getName(), method.getParameterTypes());
        }
        catch (@SuppressWarnings("unused") NoSuchMethodException ex)
        {
            // silently ignore; use the CraftServer method instead for fetching the handler.
        }
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, invocation.getArguments());
        }
        throw new UnsupportedOperationException(String.valueOf(method) + " not within methods: " + Arrays.toString(methods.keySet().toArray()));
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        throw new UnsupportedOperationException(String.valueOf(method) + " not within methods: " + Arrays.toString(methods.keySet().toArray()));
    }

    /**
     * clears all players
     */
    public void clearPlayers()
    {
        this.onlineList.clear();
    }
    
    public Map<File, YamlConfiguration> getConfigFiles()
    {
        return this.configFiles;
    }
    
    public void clearWorlds()
    {
        this.worlds.clear();
    }

    /**
     * 
     */
    public void clearTicks()
    {
        this.scheduler.cancelAllTasks();
        this.tick = 1;
        this.scheduler = new CraftScheduler();
    }
    
    /**
     * 
     */
    public void clearMessages()
    {
        this.messages.clear();
    }
    
    /**
     * Returns the plugin messages
     * @return plugin messages
     */
    public Iterable<PluginMessage> getMessages()
    {
        return this.messages;
    }

    /**
     * Initializes a new dummy world.
     * @param name
     * @return world instance
     */
    public World initWorld(String name)
    {
        return this.worlds.computeIfAbsent(name, (n) -> new DummyWorld());
    }

    /**
     * Initializes a new dummy world.
     * @param name
     * @param type
     * @param seed
     * @return world instance
     */
    public World initWorld(String name, WorldType type, long seed)
    {
        return this.worlds.computeIfAbsent(name, (n) -> {
            final UUID uuid = UUID.randomUUID();
            final ChunkGenerator generator = null;
            int dimension = this.worlds.size() + 1;
            net.minecraft.server.v1_10_R1.WorldType mctype = null;
            switch (type)
            {
                case AMPLIFIED:
                    mctype = net.minecraft.server.v1_10_R1.WorldType.AMPLIFIED;
                    break;
                case CUSTOMIZED:
                    mctype = net.minecraft.server.v1_10_R1.WorldType.CUSTOMIZED;
                    break;
                default:
                case FLAT:
                    mctype = net.minecraft.server.v1_10_R1.WorldType.FLAT;
                    break;
                case LARGE_BIOMES:
                    mctype = net.minecraft.server.v1_10_R1.WorldType.LARGE_BIOMES;
                    break;
                case NORMAL:
                    mctype = net.minecraft.server.v1_10_R1.WorldType.NORMAL;
                    break;
                case VERSION_1_1:
                    mctype = net.minecraft.server.v1_10_R1.WorldType.NORMAL_1_1;
                    break;
                
            }
            final WorldSettings worldsettings = new WorldSettings(seed, EnumGamemode.SURVIVAL, true, false, mctype);
            final WorldData worlddata = new WorldData(worldsettings, name);
            final Map<Integer, Map<Integer, Chunk>> chunks = new HashMap<>();
            
            final MinecraftServer mock = mock(MinecraftServer.class);
            when(mock.getPlayerList()).thenReturn(playerList);
            final WorldServer internal = (WorldServer) (new WorldServer(mock, new IDataManager(){

                @Override
                public WorldData getWorldData()
                {
                    return worlddata;
                }

                @Override
                public void checkSession() throws ExceptionWorldConflict
                {
                    // ignore
                }

                @Override
                public IChunkLoader createChunkLoader(WorldProvider arg0)
                {
                    return new IChunkLoader(){
                        @Override
                        public Chunk a(net.minecraft.server.v1_10_R1.World arg0, int arg1, int arg2) throws IOException
                        {
                            final Map<Integer, Chunk> map = chunks.get(arg1);
                            return map == null ? null : map.get(arg2);
                        }

                        @Override
                        public void a(net.minecraft.server.v1_10_R1.World arg0, Chunk arg1) throws IOException, ExceptionWorldConflict
                        {
                            final Map<Integer, Chunk> map = chunks.computeIfAbsent(arg1.locX, (x) -> new HashMap<>());
                            map.put(arg1.locZ, arg1);
                        }

                        @Override
                        public void b(net.minecraft.server.v1_10_R1.World arg0, Chunk arg1) throws IOException
                        {
                            // ignore
                        }

                        @Override
                        public void a()
                        {
                            // ignore
                        }

                        @Override
                        public void b()
                        {
                            // ignore
                        }
                    };
                }

                @Override
                public void saveWorldData(WorldData arg0, NBTTagCompound arg1)
                {
                    // ignore
                }

                @Override
                public void saveWorldData(WorldData arg0)
                {
                    // ignore
                }

                @Override
                public IPlayerFileData getPlayerFileData()
                {
                    // ignore
                    return null;
                }

                @Override
                public void a()
                {
                    // ignore
                }

                @Override
                public File getDirectory()
                {
                    // ignore
                    return null;
                }

                @Override
                public File getDataFile(String arg0)
                {
                    // ignore
                    return null;
                }

                @Override
                public DefinedStructureManager h()
                {
                    // ignore
                    return null;
                }

                @Override
                public UUID getUUID()
                {
                    return uuid;
                }
                
            }, worlddata, dimension,
                    mock(MethodProfiler.class), Environment.NORMAL, generator)).b();
            return new CraftWorld(internal, generator, Environment.NORMAL);
        });
    }
    
}
