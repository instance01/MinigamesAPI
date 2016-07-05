package com.comze_instancelabs.minigamesapi.spigottest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_10_R1.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_10_R1.util.Versioning;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * originally taken from spigot test sources
 */
class DummyServer implements InvocationHandler {
    private static interface MethodHandler {
        Object handle(DummyServer server, Object[] args);
    }
    private static final HashMap<Method, MethodHandler> methods = new HashMap<Method, MethodHandler>();
    static {
        try {
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
            Bukkit.setServer(Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class).newInstance(new DummyServer()));
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {
        // dummy
    }
    
    private List<Player> onlineList = new ArrayList<>();
    private DummyPluginManager pluginManager = new DummyPluginManager();
    private DummyScoreboardManager scoreboardManager = new DummyScoreboardManager();
    private CraftScheduler scheduler = new CraftScheduler();
    
    private int tick = 1;

    private DummyServer() {
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

    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        switch (method.getName())
        {
            case "getPluginManager":
                return this.pluginManager;
            case "getScoreboardManager":
                return this.scoreboardManager;
            case "getOnlinePlayers":
                return Collections.unmodifiableList(this.onlineList);
            case "getScheduler":
                return this.scheduler;
            case "getPlayer":
                for (final Player player : onlineList)
                {
                    if (args[0] instanceof String)
                    {
                        if (args[0].toString().equalsIgnoreCase(player.getName()))
                        {
                            return player;
                        }
                    }
                    if (args[0] instanceof UUID)
                    {
                        if (((UUID) args[0]).equals(player.getUniqueId()))
                        {
                            return player;
                        }
                    }
                }
                return null;
            case "getPlayerExact":
                for (final Player player : onlineList)
                {
                    if (args[0].toString().equals(player.getName()))
                    {
                        return player;
                    }
                }
                return null;
            case "matchPlayer":
                final List<Player> result = new ArrayList<>();
                for (final Player player : onlineList)
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
        throw new UnsupportedOperationException(String.valueOf(method));
    }

    /**
     * clears all players
     */
    public void clearPlayers()
    {
        this.onlineList.clear();
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
}
