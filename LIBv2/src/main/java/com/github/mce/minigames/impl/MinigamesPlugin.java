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
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.cmd.CommandHandlerInterface;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.config.CommonConfig;
import com.github.mce.minigames.api.config.ConfigInterface;
import com.github.mce.minigames.api.config.ConfigurationValueInterface;
import com.github.mce.minigames.api.config.ConfigurationValues;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;
import com.github.mce.minigames.api.perms.PermissionsInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.services.MinigameExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;
import com.github.mce.minigames.api.sign.SignInterface;
import com.github.mce.minigames.api.zones.ZoneInterface;
import com.github.mce.minigames.impl.context.ArenaPlayerInterfaceProvider;
import com.github.mce.minigames.impl.context.MinigameContextImpl;
import com.github.mce.minigames.impl.player.PlayerRegistry;
import com.github.mce.minigames.impl.services.PremiumServiceProviderInterface;

/**
 * A plugin for minigames.
 * 
 * @author mepeisen
 *
 */
public class MinigamesPlugin extends JavaPlugin implements MglibInterface
{
    
    /** the overall minecraft server versioon. */
    public static final MinecraftVersionsType         SERVER_VERSION        = MinigamesPlugin.getServerVersion();
    
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
        final CommandHandlerInterface handler = this.commands.get(command.getName().toLowerCase());
        if (handler != null)
        {
            try
            {
                if (this.debug())
                {
                    this.getLogger().log(Level.FINE, sender + " calls command " + command.getName() + " - " + Arrays.toString(args));  //$NON-NLS-1$//$NON-NLS-2$
                }
                final CommandInterface cmd = new CommandImpl(sender, this, command, label, args, '/' + command.getName());
                handler.handle(cmd);
            }
            catch (MinigameException ex)
            {
                if (this.debug())
                {
                    this.getLogger().log(Level.FINE, "Caught minigame exception during command execution: " + command.getName() + " - " + Arrays.toString(args), ex);  //$NON-NLS-1$//$NON-NLS-2$
                }
                final Locale locale = this.getDefaultLocale();
                final boolean isAdmin = sender.isOp();
                String[] msgs = null;
                if (ex.getCode().isSingleLine())
                {
                    msgs = new String[]{isAdmin ? (ex.getCode().toAdminMessage(locale, ex.getArgs())) : (ex.getCode().toUserMessage(locale, ex.getArgs()))};
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
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
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
            try
            {
                final CommandInterface cmd = new CommandImpl(sender, this, command, null, newArgs, '/' + command.getName());
                return handler.onTabComplete(cmd, lastArg);
            }
            catch (MinigameException ex)
            {
                if (this.debug())
                {
                    this.getLogger().log(Level.FINE, "Caught minigame exception during tab completion: " + command.getName() + " " + lastArg + " - " + Arrays.toString(args), ex);  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                }
            }
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
            if (this.minigames.containsKey(name))
            {
                throw new MinigameException(CommonErrors.DuplicateMinigame, name);
            }
            
            impl = new MinigamePluginImpl(this, name, provider);
            
            // register commands
            final Map<String, CommandHandlerInterface> mgCommands = provider.getBukkitCommands();
            if (mgCommands != null)
            {
                for (final Map.Entry<String, CommandHandlerInterface> entry : mgCommands.entrySet())
                {
                    if (this.commands.containsKey(entry.getKey()))
                    {
                        this.getLogger().warning("Duplicate command registration for command \"" + entry.getKey() + "\". Ignoring command from minigame \"" + name + "\"");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                    }
                    else
                    {
                        this.commands.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            
            // register messages
            final List<LocalizedMessageInterface> messages = new ArrayList<>();
            final Iterable<Class<? extends Enum<?>>> messageClasses = provider.getMessageClasses();
            if (messageClasses != null)
            {
                for (final Class<? extends Enum<?>> msgClazz : messageClasses)
                {
                    for (final Enum<?> value : msgClazz.getEnumConstants())
                    {
                        if (!(value instanceof LocalizedMessageInterface))
                        {
                            this.getLogger().warning("Message class \"" + msgClazz.getName() + "\" does not implement LocalizedMessageInterface");   //$NON-NLS-1$//$NON-NLS-2$
                            throw new MinigameException(CommonErrors.MinigameRegistrationError, name);
                        }
                        final LocalizedMessageInterface msg = (LocalizedMessageInterface) value;
                        this.messagesToMinigame.put(msg, name);
                        messages.add(msg);
                    }
                }
            }
            impl.initMessage(messages);
            
            // register configurations
            final Map<String, List<ConfigurationValueInterface>> configs = new HashMap<>();
            final Iterable<Class<? extends Enum<?>>> configClasses = provider.getConfigurations();
            if (configClasses != null)
            {
                for (final Class<? extends Enum<?>> cfgClazz : configClasses)
                {
                    for (final Enum<?> value : cfgClazz.getEnumConstants())
                    {
                        if (!(value instanceof ConfigurationValueInterface))
                        {
                            this.getLogger().warning("Configuration class \"" + cfgClazz.getName() + "\" does not implement ConfigurationValueInterface");   //$NON-NLS-1$//$NON-NLS-2$
                            throw new MinigameException(CommonErrors.MinigameRegistrationError, name);
                        }
                        final ConfigurationValueInterface cfg = (ConfigurationValueInterface) value;
                        this.optionsToMinigame.put(cfg, name);
                        configs.computeIfAbsent(cfgClazz.getAnnotation(ConfigurationValues.class).file(), (key) -> new ArrayList<>()).add(cfg);
                    }
                }
            }
            impl.initConfgurations(configs);
            
            // finally register it
            this.minigames.put(name, impl);
        }
        
        return impl;
    }
    
    @Override
    public MinigameInterface getMinigame(String minigame)
    {
        final MinigamePluginImpl impl = this.minigames.get(minigame);
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
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getMinigameFromPerm(com.github.mce.minigames.api.perms.PermissionsInterface)
     */
    @Override
    public MinigameInterface getMinigameFromPerm(PermissionsInterface item)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ConfigInterface getConfigFromCfg(ConfigurationValueInterface item)
    {
        String name = this.optionsToMinigame.get(item);
        if (name != null)
        {
            return this.minigames.get(name);
        }
        name = this.optionsToExtension.get(item);
        return name == null ? null : this.extensions.get(name);
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
    public Iterable<MinigameExtensionInterface> getExtensions()
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
                        this.getLogger().warning("Duplicate command registration for command \"" + entry.getKey() + "\". Ignoring command from extension \"" + name + "\"");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                    }
                    else
                    {
                        this.commands.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            
            // register messages
            final List<LocalizedMessageInterface> messages = new ArrayList<>();
            final Iterable<Class<? extends Enum<?>>> messageClasses = provider.getMessageClasses();
            if (messageClasses != null)
            {
                for (final Class<? extends Enum<?>> msgClazz : messageClasses)
                {
                    for (final Enum<?> value : msgClazz.getEnumConstants())
                    {
                        if (!(value instanceof LocalizedMessageInterface))
                        {
                            this.getLogger().warning("Message class \"" + msgClazz.getName() + "\" does not implement LocalizedMessageInterface");   //$NON-NLS-1$//$NON-NLS-2$
                            throw new MinigameException(CommonErrors.ExtensionRegistrationError, name);
                        }
                        final LocalizedMessageInterface msg = (LocalizedMessageInterface) value;
                        this.messagesToExtension.put(msg, name);
                        messages.add(msg);
                    }
                }
            }
            impl.initMessage(messages);
            
            // register configurations
            final Map<String, List<ConfigurationValueInterface>> configs = new HashMap<>();
            final Iterable<Class<? extends Enum<?>>> configClasses = provider.getConfigurations();
            if (configClasses != null)
            {
                for (final Class<? extends Enum<?>> cfgClazz : configClasses)
                {
                    for (final Enum<?> value : cfgClazz.getEnumConstants())
                    {
                        if (!(value instanceof ConfigurationValueInterface))
                        {
                            this.getLogger().warning("Configuration class \"" + cfgClazz.getName() + "\" does not implement ConfigurationValueInterface");   //$NON-NLS-1$//$NON-NLS-2$
                            throw new MinigameException(CommonErrors.ExtensionRegistrationError, name);
                        }
                        final ConfigurationValueInterface cfg = (ConfigurationValueInterface) value;
                        this.optionsToExtension.put(cfg, name);
                        configs.computeIfAbsent(cfgClazz.getAnnotation(ConfigurationValues.class).file(), (key) -> new ArrayList<>()).add(cfg);
                    }
                }
            }
            impl.initConfgurations(configs);
            
            if (provider instanceof PremiumServiceProviderInterface)
            {
                this.premium = (PremiumServiceProviderInterface) provider;
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
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getArenaTypes()
     */
    @Override
    public Iterable<ArenaTypeInterface> getArenaTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getArenas()
     */
    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getArenas(com.github.mce.minigames.api.arena.ArenaTypeInterface)
     */
    @Override
    public Iterable<ArenaInterface> getArenas(ArenaTypeInterface type)
    {
        // TODO Auto-generated method stub
        return null;
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
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MglibInterface#getArenaCount()
     */
    @Override
    public int getArenaCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
