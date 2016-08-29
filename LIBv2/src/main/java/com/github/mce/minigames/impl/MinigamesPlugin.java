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

package com.github.mce.minigames.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.LibState;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinecraftVersionsType;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.PluginProviderInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.cmd.AbstractCompositeCommandHandler;
import com.github.mce.minigames.api.cmd.CommandHandlerInterface;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.cmd.SubCommandHandlerInterface;
import com.github.mce.minigames.api.config.CommonConfig;
import com.github.mce.minigames.api.config.ConfigInterface;
import com.github.mce.minigames.api.config.ConfigurationValueInterface;
import com.github.mce.minigames.api.config.ConfigurationValues;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.services.ExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;
import com.github.mce.minigames.api.sign.SignInterface;
import com.github.mce.minigames.api.util.function.MgRunnable;
import com.github.mce.minigames.api.util.function.MgSupplier;
import com.github.mce.minigames.api.zones.ZoneInterface;
import com.github.mce.minigames.impl.component.ComponentRegistry;
import com.github.mce.minigames.impl.context.ArenaInterfaceProvider;
import com.github.mce.minigames.impl.context.ArenaPlayerInterfaceProvider;
import com.github.mce.minigames.impl.context.DefaultResolver;
import com.github.mce.minigames.impl.context.MinigameContextImpl;
import com.github.mce.minigames.impl.context.MinigameInterfaceProvider;
import com.github.mce.minigames.impl.nms.EventSystemInterface;
import com.github.mce.minigames.impl.nms.NmsFactory;
import com.github.mce.minigames.impl.nms.v1_10_1.NmsFactory1_10_1;
import com.github.mce.minigames.impl.nms.v1_8_1.NmsFactory1_8_1;
import com.github.mce.minigames.impl.nms.v1_8_2.NmsFactory1_8_2;
import com.github.mce.minigames.impl.nms.v1_8_3.NmsFactory1_8_3;
import com.github.mce.minigames.impl.nms.v1_9_1.NmsFactory1_9_1;
import com.github.mce.minigames.impl.nms.v1_9_2.NmsFactory1_9_2;
import com.github.mce.minigames.impl.player.ArenaPlayerImpl;
import com.github.mce.minigames.impl.player.PlayerRegistry;
import com.github.mce.minigames.impl.services.PremiumServiceProviderInterface;

/**
 * A plugin for minigames.
 * 
 * @author mepeisen
 *
 */
public class MinigamesPlugin extends JavaPlugin implements MglibInterface, Listener
{
    
    /** the overall minecraft server versioon. */
    public static final MinecraftVersionsType              SERVER_VERSION      = MinigamesPlugin.getServerVersion();
    
    /** the well known minigames. */
    private final Map<String, MinigamePluginImpl>          minigames           = new TreeMap<>();
    
    /** the well known extensions. */
    private final Map<String, ExtensionImpl>               extensions          = new TreeMap<>();
    
    /** Current library state. */
    private LibState                                       state               = LibState.Initializing;
    
    /** known command handlers by name. */
    private final Map<String, CommandHandlerInterface>     commands            = new HashMap<>();
    
    /** messages to minigames. */
    private final Map<LocalizedMessageInterface, String>   messagesToMinigame  = new HashMap<>();
    
    /** options to minigames. */
    private final Map<ConfigurationValueInterface, String> optionsToMinigame   = new HashMap<>();
    
    /** messages to extensions. */
    private final Map<LocalizedMessageInterface, String>   messagesToExtension = new HashMap<>();
    
    /** options to extensions. */
    private final Map<ConfigurationValueInterface, String> optionsToExtension  = new HashMap<>();
    
    /**
     * the players registry.
     */
    private final PlayerRegistry                           players             = new PlayerRegistry();
    
    /**
     * the minigame context implementation.
     */
    private MinigameContextImpl                            contextImpl         = new MinigameContextImpl();
    
    /**
     * the premium extension (if available)
     */
    private PremiumServiceProviderInterface                premium;
    
    /**
     * The component registry.
     */
    private final ComponentRegistry                        components          = new ComponentRegistry();
    
    private final EventSystemInterface                     events;
    
    private final NmsFactory                               nmsFactory;
    
    /**
     * Constructor to create the plugin.
     */
    public MinigamesPlugin()
    {
        // registers the core minigame.
        try
        {
            final MinigamePluginInterface mg2 = this.register(new CoreMinigame(this));
            
            // context provider
            mg2.registerContextHandler(ArenaPlayerInterface.class, new ArenaPlayerInterfaceProvider());
            mg2.registerContextHandler(MinigameInterface.class, new MinigameInterfaceProvider());
            mg2.registerContextHandler(ArenaInterface.class, new ArenaInterfaceProvider());
            ArenaPlayerImpl.registerProvider(mg2);
            
            // resolver
            mg2.registerContextResolver(new DefaultResolver());
            
            mg2.init();
            
            this.getLogger().log(Level.INFO, "MinigamesLib2 finihes initialization. Minecraft version: " + this.getMinecraftVersion()); //$NON-NLS-1$
        }
        catch (MinigameException ex)
        {
            // log it, although this should never happen
            // because in constructor we neither are in wrong state
            // nor do we already know the 'core' minigame
            this.getLogger().log(Level.SEVERE, "Error registering core minigame", ex); //$NON-NLS-1$
        }
        
        switch (SERVER_VERSION)
        {
            case V1_10:
            case V1_10_R1:
                this.nmsFactory = new NmsFactory1_10_1();
                break;
            case V1_8:
            case V1_8_R1:
                this.nmsFactory = new NmsFactory1_8_1();
                break;
            case V1_8_R2:
                this.nmsFactory = new NmsFactory1_8_2();
                break;
            case V1_8_R3:
                this.nmsFactory = new NmsFactory1_8_3();
                break;
            case V1_9:
            case V1_9_R1:
                this.nmsFactory = new NmsFactory1_9_1();
                break;
            case V1_9_R2:
                this.nmsFactory = new NmsFactory1_9_2();
                break;
            case Unknown:
            case V1_7:
            case V1_7_R1:
            case V1_7_R2:
            case V1_7_R3:
            case V1_7_R4:
            default:
                // no initialization.
                this.nmsFactory = null;
                break;
        }
        
        this.events = this.nmsFactory == null ? null : this.nmsFactory.create(EventSystemInterface.class);
    }
    
    @Override
    public void onDisable()
    {
        this.state = LibState.Terminating;
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onEnable()
    {
        if (this.nmsFactory == null)
        {
            this.getLogger().severe("Running on non-supported minecraft version. Disabling minigames."); //$NON-NLS-1$
        }
        else
        {
            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(this.events, this);
            Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
                this.state = LibState.Running;
            }, 1L);
        }
    }
    
    /**
     * Calculates the minecraft server version.
     * 
     * @return Minecraft server version.
     */
    private static MinecraftVersionsType getServerVersion()
    {
        try
        {
            final String v = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1); //$NON-NLS-1$
            if (v.startsWith("v1_7_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R1;
            }
            if (v.startsWith("v1_7_R2")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R2;
            }
            if (v.startsWith("v1_7_R3")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R3;
            }
            if (v.startsWith("v1_7_R4")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_7_R4;
            }
            if (v.startsWith("v1_8_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_8_R1;
            }
            if (v.startsWith("v1_8_R2")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_8_R2;
            }
            if (v.startsWith("v1_8_R3")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_8_R3;
            }
            if (v.startsWith("v1_9_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_9_R1;
            }
            if (v.startsWith("v1_9_R2")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_9_R2;
            }
            if (v.startsWith("v1_10_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_10_R1;
            }
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
        }
        return MinecraftVersionsType.Unknown;
    }
    
    // event handlersEventHandler
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (this.getState() != LibState.Running)
        {
            sender.sendMessage("MinigamesLib is disabled!"); //$NON-NLS-1$
            return false;
        }
        final CommandHandlerInterface handler = this.commands.get(command.getName().toLowerCase());
        if (handler != null)
        {
            if (this.debug())
            {
                this.getLogger().log(Level.FINE, sender + " calls command " + command.getName() + " - " + Arrays.toString(args)); //$NON-NLS-1$//$NON-NLS-2$
            }
            final CommandInterface cmd = new CommandImpl(sender, this, command, label, args, '/' + command.getName());
            
            this.contextImpl.runInContext(cmd, () -> {
                try
                {
                    handler.handle(cmd);
                }
                catch (MinigameException ex)
                {
                    if (this.debug())
                    {
                        this.getLogger().log(Level.FINE, "Caught minigame exception during command execution: " + command.getName() + " - " + Arrays.toString(args), ex); //$NON-NLS-1$//$NON-NLS-2$
                    }
                    final Locale locale = this.getDefaultLocale();
                    final boolean isAdmin = sender.isOp();
                    String[] msgs = null;
                    if (ex.getCode().isSingleLine())
                    {
                        msgs = new String[] { isAdmin ? (ex.getCode().toAdminMessage(locale, ex.getArgs())) : (ex.getCode().toUserMessage(locale, ex.getArgs())) };
                    }
                    else
                    {
                        msgs = isAdmin ? (ex.getCode().toAdminMessageLine(locale, ex.getArgs())) : (ex.getCode().toUserMessageLine(locale, ex.getArgs()));
                    }
                    for (final String msg : msgs)
                    {
                        switch (ex.getCode().getSeverity())
                        {
                            default:
                            case Error:
                                sender.sendMessage(ChatColor.DARK_RED + msg);
                                break;
                            case Information:
                                sender.sendMessage(ChatColor.WHITE + msg);
                                break;
                            case Loser:
                                sender.sendMessage(ChatColor.RED + msg);
                                break;
                            case Success:
                                sender.sendMessage(ChatColor.GREEN + msg);
                                break;
                            case Warning:
                                sender.sendMessage(ChatColor.YELLOW + msg);
                                break;
                            case Winner:
                                sender.sendMessage(ChatColor.GOLD + msg);
                                break;
                        }
                    }
                }
            });
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (this.getState() != LibState.Running)
        {
            return null;
        }
        String lastArg = null;
        String[] newArgs = null;
        if (args.length > 0)
        {
            lastArg = args[args.length - 1].toLowerCase();
            newArgs = Arrays.copyOf(args, args.length - 1);
        }
        final CommandHandlerInterface handler = this.commands.get(command.getName().toLowerCase());
        if (handler != null)
        {
            final CommandInterface cmd = new CommandImpl(sender, this, command, null, newArgs, '/' + command.getName());
            final AtomicReference<List<String>> ref = new AtomicReference<>();
            final String la = lastArg;
            this.contextImpl.runInContext(cmd, () -> {
                try
                {
                    ref.set(handler.onTabComplete(cmd, la));
                }
                catch (MinigameException ex)
                {
                    if (this.debug())
                    {
                        this.getLogger().log(Level.FINE, "Caught minigame exception during tab completion: " + command.getName() + " " + la + " - " + Arrays.toString(args), ex); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                    }
                }
            });
            return ref.get();
        }
        return null;
    }
    
    // api methods
    
    @Override
    public LibState getState()
    {
        return this.state;
    }
    
    @Override
    public MinecraftVersionsType getMinecraftVersion()
    {
        return SERVER_VERSION;
    }
    
    @Override
    public MinigamePluginInterface register(PluginProviderInterface provider) throws MinigameException
    {
        final String name = provider.getName();
        
        MinigamePluginImpl impl;
        
        synchronized (this.minigames)
        {
            if (this.state != LibState.Initializing && this.state != LibState.Sleeping)
            {
                throw new MinigameException(CommonErrors.Cannot_Create_Game_Wrong_State, name, this.state.name());
            }
            if (this.minigames.containsKey(name.toLowerCase()))
            {
                throw new MinigameException(CommonErrors.DuplicateMinigame, name);
            }
            
            impl = new MinigamePluginImpl(this, name, provider, this.components);
            
            // register commands
            final Map<String, CommandHandlerInterface> mgCommands = provider.getBukkitCommands();
            if (mgCommands != null)
            {
                for (final Map.Entry<String, CommandHandlerInterface> entry : mgCommands.entrySet())
                {
                    if (this.commands.containsKey(entry.getKey()))
                    {
                        this.getLogger().warning("Duplicate command registration for command \"" + entry.getKey() + "\". Ignoring command from minigame \"" + name + "\""); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                    }
                    else
                    {
                        this.commands.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            
            // register messages
            final List<LocalizedMessageInterface> messages = new ArrayList<>();
            final Iterable<Class<? extends LocalizedMessageInterface>> messageClasses = provider.getMessageClasses();
            if (messageClasses != null)
            {
                for (final Class<? extends LocalizedMessageInterface> msgClazz : messageClasses)
                {
                    for (final LocalizedMessageInterface msg : msgClazz.getEnumConstants())
                    {
                        this.messagesToMinigame.put(msg, name);
                        messages.add(msg);
                    }
                }
            }
            impl.initMessage(messages);
            
            // register configurations
            final Map<String, List<ConfigurationValueInterface>> configs = new HashMap<>();
            final Iterable<Class<? extends ConfigurationValueInterface>> configClasses = provider.getConfigurations();
            if (configClasses != null)
            {
                for (final Class<? extends ConfigurationValueInterface> cfgClazz : configClasses)
                {
                    for (final ConfigurationValueInterface cfg : cfgClazz.getEnumConstants())
                    {
                        this.optionsToMinigame.put(cfg, name);
                        configs.computeIfAbsent(cfgClazz.getAnnotation(ConfigurationValues.class).file(), (key) -> new ArrayList<>()).add(cfg);
                    }
                }
            }
            impl.initConfgurations(configs);
            
            // finally register it
            this.minigames.put(name.toLowerCase(), impl);
        }
        
        return impl;
    }
    
    @Override
    public MinigameInterface getMinigame(String minigame)
    {
        final MinigamePluginImpl impl = this.minigames.get(minigame.toLowerCase());
        return impl == null ? null : new MinigameWrapper(impl);
    }
    
    @Override
    public int getMinigamesCount()
    {
        return this.minigames.size();
    }
    
    @Override
    public Iterable<MinigameInterface> getMinigames()
    {
        final List<MinigameInterface> result = new ArrayList<>();
        for (final MinigamePluginImpl plugin : this.minigames.values())
        {
            result.add(new MinigameWrapper(plugin));
        }
        return result;
    }
    
    @Override
    public ArenaPlayerInterface getPlayer(Player player)
    {
        return this.players.getPlayer(player);
    }
    
    @Override
    public ArenaPlayerInterface getPlayer(OfflinePlayer player)
    {
        return this.players.getPlayer(player);
    }
    
    @Override
    public ArenaPlayerInterface getPlayer(UUID uuid)
    {
        return this.players.getPlayer(uuid);
    }
    
    @Override
    public MessagesConfigInterface getMessagesFromMsg(LocalizedMessageInterface item)
    {
        String name = this.messagesToMinigame.get(item);
        if (name != null)
        {
            return this.minigames.get(name).getMessages();
        }
        name = this.messagesToExtension.get(item);
        return name == null ? null : this.extensions.get(name).getMessages();
    }
    
    @Override
    public ConfigInterface getConfigFromCfg(ConfigurationValueInterface item)
    {
        final ConfigurationValues values = item.getClass().getAnnotation(ConfigurationValues.class);
        if (values.fixed())
        {
            String name = this.optionsToMinigame.get(item);
            if (name != null)
            {
                return this.minigames.get(name);
            }
            name = this.optionsToExtension.get(item);
            return name == null ? null : this.extensions.get(name);
        }
        final MinigameInterface minigame = this.getContext(MinigameInterface.class);
        return minigame;
    }
    
    /**
     * Returns the global context implementation.
     * 
     * @return global context implementation.
     */
    public MinigameContextImpl getApiContext()
    {
        return this.contextImpl;
    }
    
    @Override
    public <T> T getContext(Class<T> clazz)
    {
        return this.contextImpl.getContext(clazz);
    }
    
    @Override
    public String resolveContextVar(String src)
    {
        return this.contextImpl.resolveContextVar(src);
    }
    
    @Override
    public <T> void setContext(Class<T> clazz, T value)
    {
        this.contextImpl.setContext(clazz, value);
    }
    
    @Override
    public void runInNewContext(MgRunnable runnable) throws MinigameException
    {
        this.contextImpl.runInNewContext(runnable);
    }
    
    @Override
    public void runInCopiedContext(MgRunnable runnable) throws MinigameException
    {
        this.contextImpl.runInCopiedContext(runnable);
    }
    
    @Override
    public <T> T calculateInNewContext(MgSupplier<T> runnable) throws MinigameException
    {
        return this.contextImpl.calculateInNewContext(runnable);
    }
    
    @Override
    public <T> T calculateInCopiedContext(MgSupplier<T> runnable) throws MinigameException
    {
        return this.contextImpl.calculateInCopiedContext(runnable);
    }
    
    @Override
    public Serializable getLibVersionString()
    {
        return this.getDescription().getVersion();
    }
    
    @Override
    public boolean debug()
    {
        return CommonConfig.DebugEnabled.getBoolean();
    }
    
    @Override
    public Locale getDefaultLocale()
    {
        return new Locale(CommonConfig.DefaultLocale.getString(), ""); //$NON-NLS-1$
    }
    
    /**
     * Returns the mode as string
     * 
     * @return either "OS" or "PREMIUM"
     */
    public String getModeString()
    {
        return this.premium == null ? "OS" : "PREMIUM"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Override
    public int getExtensionsCount()
    {
        return this.extensions.size();
    }
    
    @Override
    public Iterable<ExtensionInterface> getExtensions()
    {
        return new ArrayList<>(this.extensions.values());
    }
    
    @Override
    public MinigameExtensionInterface register(MinigameExtensionProviderInterface provider) throws MinigameException
    {
        final String name = provider.getName();
        
        ExtensionImpl impl;
        
        synchronized (this.extensions)
        {
            if (this.state != LibState.Initializing && this.state != LibState.Sleeping)
            {
                throw new MinigameException(CommonErrors.Cannot_Create_Extension_Wrong_State, name, this.state.name());
            }
            if (this.extensions.containsKey(name))
            {
                throw new MinigameException(CommonErrors.DuplicateExtension, name);
            }
            
            impl = new ExtensionImpl(this, name, provider);
            
            // register commands
            final Map<String, CommandHandlerInterface> mgCommands = provider.getBukkitCommands();
            if (mgCommands != null)
            {
                for (final Map.Entry<String, CommandHandlerInterface> entry : mgCommands.entrySet())
                {
                    if (this.commands.containsKey(entry.getKey()))
                    {
                        this.getLogger().warning("Duplicate command registration for command \"" + entry.getKey() + "\". Ignoring command from extension \"" + name + "\""); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                    }
                    else
                    {
                        this.commands.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            
            // register messages
            final List<LocalizedMessageInterface> messages = new ArrayList<>();
            final Iterable<Class<? extends LocalizedMessageInterface>> messageClasses = provider.getMessageClasses();
            if (messageClasses != null)
            {
                for (final Class<? extends LocalizedMessageInterface> msgClazz : messageClasses)
                {
                    for (final LocalizedMessageInterface msg : msgClazz.getEnumConstants())
                    {
                        this.messagesToExtension.put(msg, name);
                        messages.add(msg);
                    }
                }
            }
            impl.initMessage(messages);
            
            // register configurations
            final Map<String, List<ConfigurationValueInterface>> configs = new HashMap<>();
            final Iterable<Class<? extends ConfigurationValueInterface>> configClasses = provider.getConfigurations();
            if (configClasses != null)
            {
                for (final Class<? extends ConfigurationValueInterface> cfgClazz : configClasses)
                {
                    for (final ConfigurationValueInterface cfg : cfgClazz.getEnumConstants())
                    {
                        this.optionsToExtension.put(cfg, name);
                        configs.computeIfAbsent(cfgClazz.getAnnotation(ConfigurationValues.class).file(), (key) -> new ArrayList<>()).add(cfg);
                    }
                }
            }
            impl.initConfgurations(configs);
            
            if (provider instanceof PremiumServiceProviderInterface)
            {
                this.premium = (PremiumServiceProviderInterface) provider;
                
                for (final Map.Entry<String, SubCommandHandlerInterface> entry : this.premium.getAdditionalCommands().entrySet())
                {
                    final String[] path = entry.getKey().split("\\."); //$NON-NLS-1$
                    AbstractCompositeCommandHandler cur = (AbstractCompositeCommandHandler) this.commands.get("mg2"); //$NON-NLS-1$
                    for (int i = 0; i < path.length - 1; i++)
                    {
                        cur = (AbstractCompositeCommandHandler) cur.getSubCommand(path[i]);
                    }
                    cur.injectSubCommand(path[path.length - 1], entry.getValue());
                }
            }
            
            // finally register it
            this.extensions.put(name, impl);
        }
        
        return impl;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#findZone(org.bukkit.Location)
     */
    @Override
    public ZoneInterface findZone(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#findZones(org.bukkit.Location)
     */
    @Override
    public Iterable<ZoneInterface> findZones(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Iterable<ArenaTypeInterface> getArenaTypes()
    {
        final List<ArenaTypeInterface> result = new ArrayList<>();
        for (final MinigamePluginImpl mg : this.minigames.values())
        {
            for (final ArenaTypeDeclarationInterface atdi : mg.getDeclaredTypes())
            {
                result.add(atdi.getType());
            }
        }
        return result;
    }
    
    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        final List<ArenaInterface> result = new ArrayList<>();
        for (final MinigamePluginImpl mg : this.minigames.values())
        {
            for (final ArenaInterface arena : mg.getArenas())
            {
                result.add(arena);
            }
        }
        return result;
    }
    
    @Override
    public Iterable<ArenaInterface> getArenas(ArenaTypeInterface type)
    {
        final String minigame = type.getMinigame();
        final MinigamePluginImpl impl = this.minigames.get(minigame);
        if (impl != null)
        {
            final List<ArenaInterface> arenas = new ArrayList<>();
            for (final ArenaInterface arena : impl.getArenas())
            {
                if (arena.getArenaType() == type)
                {
                    arenas.add(arena);
                }
            }
            return arenas;
        }
        return Collections.emptyList();
    }
    
    @Override
    public int getArenaCount()
    {
        int count = 0;
        for (final MinigamePluginImpl mg : this.minigames.values())
        {
            count += mg.getArenaCount();
        }
        return count;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getSigns()
     */
    @Override
    public Iterable<SignInterface> getSigns()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getSignForLocation(org.bukkit.Location)
     */
    @Override
    public SignInterface getSignForLocation(Location l)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getSigns(com.github.mce.minigames.api.arena.ArenaTypeInterface)
     */
    @Override
    public Iterable<SignInterface> getSigns(ArenaTypeInterface type)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getSigns(com.github.mce.minigames.api.arena.ArenaInterface)
     */
    @Override
    public Iterable<SignInterface> getSigns(ArenaInterface arena)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getSigns(com.github.mce.minigames.api.MinigameInterface)
     */
    @Override
    public Iterable<SignInterface> getSigns(MinigameInterface minigame)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    // events
    
    /**
     * Player online event.
     * 
     * @param evt
     *            player online event.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt)
    {
        this.players.onPlayerJoin(evt);
    }
    
    /**
     * Player online event.
     * 
     * @param evt
     *            player online event.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        this.players.onPlayerQuit(evt);
        // TODO hard close gui
    }
    
    /**
     * Inventory close event
     * 
     * @param evt
     *            inventory close event
     */
    public void onInventoryClose(InventoryCloseEvent evt)
    {
        // TODO
    }
    
    /**
     * Inventory click event
     * 
     * @param evt
     *            inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt)
    {
        // TODO
    }
    
    /**
     * Inventory drag event
     * 
     * @param evt
     *            inventory drag event
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent evt)
    {
        // TODO
    }
    
}
