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
package com.comze_instancelabs.minigamesapi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.comze_instancelabs.minigamesapi.bungee.BungeeSocket;
import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.PartyMessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.config.StatsGlobalConfig;
import com.comze_instancelabs.minigamesapi.guns.Guns;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;
import com.comze_instancelabs.minigamesapi.util.BungeeUtil;
import com.comze_instancelabs.minigamesapi.util.Metrics;
import com.comze_instancelabs.minigamesapi.util.Metrics.Graph;
import com.comze_instancelabs.minigamesapi.util.ParticleEffectNew;
import com.comze_instancelabs.minigamesapi.util.Updater;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.milkbowl.vault.economy.Economy;

/**
 * Main minigames API; plugin mplementation.
 * 
 * @author instancelabs
 *
 */
public class MinigamesAPI extends JavaPlugin implements PluginMessageListener
{
    
    /** the overall minecraft server versioon. */
    public static final MinecraftVersionsType         SERVER_VERSION        = MinigamesAPI.getServerVersion();
    
    /** the locale to be used. TODO: Change via config */
    public static Locale                              LOCALE                = Locale.ENGLISH;
    
    /** the minigames plugin instance. */
    private static MinigamesAPI                       instance              = null;
    
    public static Economy                             econ                  = null;
    public static boolean                             economy               = true;
    public boolean                                    crackshot             = false;
    
    /** a global debug flag; controls the output of finer debug messages. */
    public static boolean                             debug                 = false;
    
    int                                               updatetime            = 20 * 10;
    
    public HashMap<String, Party>                     global_party          = new HashMap<>();
    public HashMap<String, ArrayList<Party>>          global_party_invites  = new HashMap<>();
    
    public static HashMap<JavaPlugin, PluginInstance> pinstances            = new HashMap<>();
    
    public PartyMessagesConfig                        partymessages;
    public StatsGlobalConfig                          statsglobal;
    
    /**
     * textual server version.
     * 
     * @deprecated will be removed in 1.5.0; replaced by SERVER_VERSION enumeration.
     */
    @Deprecated
    public String                                     internalServerVersion = ""; //$NON-NLS-1$
    
    /**
     * {@code true} if this is below 1.7.10
     * 
     * @deprecated will be removed in 1.5.0; replaced by SERVER_VERSION enumeration.
     */
    public boolean                                    below1710             = false;                          // Used for scoreboard function (wether to use getScore(OfflinePlayer) or
                                                                                                              // getScore(String))
    /**
     * The plugin metrics report.
     */
    Metrics                                           metrics;
    
    @Override
    public void onEnable()
    {
        MinigamesAPI.instance = this;
        
        this.internalServerVersion = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1); //$NON-NLS-1$
        this.below1710 = MinigamesAPI.SERVER_VERSION.isBelow(MinecraftVersionsType.V1_7_R4);
        this.getLogger().info(String.format("§c§lLoaded MinigamesAPI. We're on %0$s.", MinigamesAPI.SERVER_VERSION.name())); //$NON-NLS-1$
        
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, ChannelStrings.CHANNEL_BUNGEE_CORD);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, ChannelStrings.CHANNEL_BUNGEE_CORD, this);
        
        if (MinigamesAPI.economy)
        {
            if (!this.setupEconomy())
            {
                this.getLogger().severe(String.format("[%s] - No Economy (Vault) dependency found! Disabling Economy.", this.getDescription().getName())); //$NON-NLS-1$
                MinigamesAPI.economy = false;
            }
        }
        
        this.getConfig().options().header("Want bugfree versions? Set this to true for automatic updates:"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.AUTO_UPDATING, false);
        this.getConfig().addDefault(PluginConfigStrings.SIGNS_UPDATE_TIME, 20);
        this.getConfig().addDefault(PluginConfigStrings.PARTY_COMMAND_ENABLED, true);
        this.getConfig().addDefault(PluginConfigStrings.DEBUG, false);
        
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        this.partymessages = new PartyMessagesConfig(this);
        this.statsglobal = new StatsGlobalConfig(this, false);
        
        MinigamesAPI.debug = this.getConfig().getBoolean(PluginConfigStrings.DEBUG);
        
        Bukkit.getScheduler().runTaskLater(this, () -> {
            try
            {
                MinigamesAPI.this.metrics = new Metrics(MinigamesAPI.instance);
                
                final Graph components = MinigamesAPI.this.metrics.createGraph("Minigames"); //$NON-NLS-1$
                for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                {
                    components.addPlotter(new Metrics.Plotter(pli.getPlugin().getName()) {
                        @Override
                        public int getValue()
                        {
                            return 1;
                        }
                    });
                    if (MinigamesAPI.debug)
                    {
                        this.getLogger().fine("Loaded Graph for: " + pli.getPlugin().getName()); //$NON-NLS-1$
                    }
                }
                
                MinigamesAPI.this.metrics.start();
            }
            catch (final IOException e)
            {
                this.getLogger().log(Level.WARNING, "Exception while updating metrics", e); //$NON-NLS-1$
            }
        }, 60L);
        
        if (this.getConfig().getBoolean(PluginConfigStrings.AUTO_UPDATING))
        {
            new Updater(this, 83025, this.getFile(), Updater.UpdateType.DEFAULT, false);
        }
        
        if (this.getServer().getPluginManager().getPlugin("CrackShot") != null) //$NON-NLS-1$
        {
            this.crackshot = true;
        }
        
        Bukkit.getScheduler().runTaskLater(this, () -> {
            // Reset all arena signs and check if any arena was interrupted
            int i = 0;
            MinigamesAPI.getAPI();
            for (final PluginInstance pli : MinigamesAPI.pinstances.values())
            {
                for (final Arena a : pli.getArenas())
                {
                    if (a != null)
                    {
                        if (a.isSuccessfullyInit())
                        {
                            Util.updateSign(pli.getPlugin(), a);
                            a.getSmartReset().loadSmartBlocksFromFile();
                        }
                        else
                        {
                            this.getLogger().log(Level.WARNING, "Arena " + pli.getPlugin().getName() + "/" + a.getInternalName() + " not initialized at onEnable."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        }
                    }
                    i++;
                }
            }
            this.getLogger().info("Found " + i + " arenas."); //$NON-NLS-1$//$NON-NLS-2$
        }, 50L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            MinigamesAPI.getAPI();
            for (final PluginInstance pli : MinigamesAPI.pinstances.values())
            {
                for (final Arena a : pli.getArenas())
                {
                    Util.updateSign(pli.getPlugin(), a);
                    
                }
            }
        }, 0, 20 * this.getConfig().getInt(PluginConfigStrings.SIGNS_UPDATE_TIME));
    }
    
    /**
     * Calculates the minecraft server version.
     * 
     * @return Minecraft server version.
     */
    private static MinecraftVersionsType getServerVersion()
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
        return MinecraftVersionsType.Unknown;
    }
    
    @Override
    public void onDisable()
    {
        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
        {
            // Reset arenas
            for (final Arena a : pli.getArenas())
            {
                if (a != null)
                {
                    if (a.isSuccessfullyInit())
                    {
                        if (a.getArenaState() != ArenaState.JOIN)
                        {
                            a.getSmartReset().saveSmartBlocksToFile();
                        }
                        final ArrayList<String> temp = new ArrayList<>(a.getAllPlayers());
                        for (final String p_ : temp)
                        {
                            a.leavePlayer(p_, true);
                        }
                        try
                        {
                            a.getSmartReset().resetRaw();
                        }
                        catch (final Exception e)
                        {
                            this.getLogger().log(Level.WARNING, "Failed resetting arena " + pli.getPlugin().getName() + "/" + a.getInternalName() + " at onDisable.", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        }
                    }
                    else
                    {
                        this.getLogger().log(Level.WARNING, "Arena " + pli.getPlugin().getName() + "/" + a.getInternalName() + " not initialized thus not reset at onDisable."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                }
            }
            
            // Save important configs
            pli.getArenasConfig().saveConfig();
            pli.getPlugin().saveConfig();
            pli.getMessagesConfig().saveConfig();
            pli.getClassesConfig().saveConfig();
        }
        
    }
    
    /**
     * Sets up the API allowing to override all configs.
     * 
     * <p>
     * This method is meant to be called within concrete minigame plugin onEable.
     * </p>
     * 
     * @param plugin_
     *            the java plugin representing the minigame.
     * @param minigame
     *            internal name of the minigame.
     * @param arenaclass
     *            the class implementing/ overriding the arena class; {@link Arena}.
     * @param arenasconfig
     *            the arenas config store.
     * @param messagesconfig
     *            the messages config store.
     * @param classesconfig
     *            the classes config store.
     * @param statsconfig
     *            the statistics store.
     * @param defaultconfig
     *            the default plugin config. TODO variable is never read?
     * @param customlistener
     *            {@code true} if there is a custom listener handling the arenas; {@code false} to register the default arena listener; {@link ArenaListener}.
     * @return the api instance (this plugin).
     */
    public static MinigamesAPI setupAPI(final JavaPlugin plugin_, final String minigame, final Class<?> arenaclass, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig,
            final ClassesConfig classesconfig, final StatsConfig statsconfig, final DefaultConfig defaultconfig, final boolean customlistener)
    {
        MinigamesAPI.pinstances.put(plugin_, new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>()));
        if (!customlistener)
        {
            final ArenaListener al = new ArenaListener(plugin_, MinigamesAPI.pinstances.get(plugin_), minigame);
            MinigamesAPI.pinstances.get(plugin_).setArenaListener(al);
            Bukkit.getPluginManager().registerEvents(al, plugin_);
        }
        Classes.loadClasses(plugin_);
        Guns.loadGuns(plugin_);
        MinigamesAPI.pinstances.get(plugin_).getShopHandler().loadShopItems();
        return MinigamesAPI.instance;
    }
    
    /**
     * Use this to register your custom arena listener.
     * 
     * @param plugin_
     *            the minigames plugin.
     * @param arenalistener
     *            the arena listener.
     */
    public static void registerArenaListenerLater(final JavaPlugin plugin_, final ArenaListener arenalistener)
    {
        // TODO check plugin code; should we invoke MinigamesAPI.pinstances.get(plugin_).setArenaListener(arenalistener);
        Bukkit.getPluginManager().registerEvents(arenalistener, plugin_);
    }
    
    /**
     * registers additional arena setup
     * 
     * <p>
     * TODO: Do we still need this?
     * </p>
     * 
     * @param plugin_
     *            the minigames plugin.
     * @param arenasetup
     *            the arena setup.
     */
    public static void registerArenaSetup(final JavaPlugin plugin_, final ArenaSetup arenasetup)
    {
        MinigamesAPI.pinstances.get(plugin_).arenaSetup = arenasetup;
    }
    
    /**
     * registers additional scoreboard
     * 
     * <p>
     * TODO: Do we still need this?
     * </p>
     * 
     * @param plugin_
     *            the minigames plugin.
     * @param board
     *            the arena scoreboard.
     */
    public static void registerScoreboard(final JavaPlugin plugin_, final ArenaScoreboard board)
    {
        MinigamesAPI.pinstances.get(plugin_).scoreboardManager = board;
    }
    
    /**
     * Sets up the API and prepare for manual setup.
     * 
     * <p>
     * This method is meant to be called within concrete minigame plugin onEable.
     * </p>
     * 
     * <p>
     * Allow loading of arenas with own extended arena class into PluginInstance: after this setup, get the PluginInstance, load the arenas by yourself and add the loaded arenas w/ custom arena class
     * into the PluginInstance
     * </p>
     * 
     * @param plugin_
     *            the java plugin representing the minigame.
     * @param minigame
     *            internal name of the minigame.
     * @param arenaclass
     *            the class implementing/ overriding the arena class; {@link Arena}.
     * @return the api instance (this plugin).
     * @deprecated will be removed in 1.5.0
     */
    @Deprecated
    public static MinigamesAPI setupAPI(final JavaPlugin plugin_, final String minigame, final Class<?> arenaclass)
    {
        MinigamesAPI.setupRaw(plugin_, minigame);
        return MinigamesAPI.instance;
    }
    
    /**
     * Sets up the API.
     * 
     * <p>
     * This method is meant to be called within concrete minigame plugin onEable. Loads all arenas (default implementation).
     * </p>
     * 
     * <p>
     * TODO: Compare to {@link #setupAPI(JavaPlugin, String, Class, ArenasConfig, MessagesConfig, ClassesConfig, StatsConfig, DefaultConfig, boolean)}
     * </p>
     * 
     * @param plugin_
     *            the java plugin representing the minigame.
     * @param minigame
     *            internal name of the minigame.
     * @return the api instance (this plugin).
     * @deprecated will be removed in 1.5.0
     */
    @Deprecated
    public static MinigamesAPI setupAPI(final JavaPlugin plugin_, final String minigame)
    {
        final PluginInstance pli = MinigamesAPI.setupRaw(plugin_, minigame);
        pli.addLoadedArenas(Util.loadArenas(plugin_, pli.getArenasConfig()));
        return MinigamesAPI.instance;
    }
    
    /**
     * Raw (internal) setup method.
     * 
     * @param plugin_
     *            the java plugin representing the minigame.
     * @param minigame
     *            internal name of the minigame.
     * @return internal plugin representation of the minigame.
     * @deprecated will be removed in 1.5.0
     */
    @Deprecated
    public static PluginInstance setupRaw(final JavaPlugin plugin_, final String minigame)
    {
        final ArenasConfig arenasconfig = new ArenasConfig(plugin_);
        final MessagesConfig messagesconfig = new MessagesConfig(plugin_);
        final ClassesConfig classesconfig = new ClassesConfig(plugin_, false);
        final StatsConfig statsconfig = new StatsConfig(plugin_, false);
        DefaultConfig.init(plugin_, false);
        final PluginInstance pli = new PluginInstance(plugin_, arenasconfig, messagesconfig, classesconfig, statsconfig);
        MinigamesAPI.pinstances.put(plugin_, pli);
        final ArenaListener al = new ArenaListener(plugin_, MinigamesAPI.pinstances.get(plugin_), minigame);
        MinigamesAPI.pinstances.get(plugin_).setArenaListener(al);
        Bukkit.getPluginManager().registerEvents(al, plugin_);
        Classes.loadClasses(plugin_);
        pli.getShopHandler().loadShopItems();
        Guns.loadGuns(plugin_);
        return pli;
    }
    
    /**
     * Returns the minigames API plugin.
     * 
     * @return minigames API plugin.
     */
    public static MinigamesAPI getAPI()
    {
        return MinigamesAPI.instance;
    }
    
    /**
     * Creates a new Command handler.
     * 
     * @return command handler.
     * @deprecated removed in 1.5.0
     */
    @Deprecated
    public static CommandHandler getCommandHandler()
    {
        return new CommandHandler();
    }
    
    /**
     * Setup the economy vault.
     * 
     * @return {@code true} if vault was initialized.
     */
    private boolean setupEconomy()
    {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) //$NON-NLS-1$
        {
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        MinigamesAPI.econ = rsp.getProvider();
        return MinigamesAPI.econ != null;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (cmd.getName().equalsIgnoreCase(CommandStrings.START))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(Messages.getString("MinigamesAPI.ExecuteIngame", LOCALE)); //$NON-NLS-1$
                return true;
            }
            if (!sender.hasPermission(PermissionStrings.MINIGAMES_START))
            {
                sender.sendMessage(Messages.getString("MinigamesAPI.NoPermissionForStart", LOCALE)); //$NON-NLS-1$
                return true;
            }
            final Player p = (Player) sender;
            MinigamesAPI.getAPI();
            for (final PluginInstance pli : MinigamesAPI.pinstances.values())
            {
                if (pli.containsGlobalPlayer(p.getName()))
                {
                    final Arena a = pli.global_players.get(p.getName());
                    this.getLogger().info("Arena " + a.getInternalName() + " started because of start command from player " + p.getName()); //$NON-NLS-1$//$NON-NLS-2$
                    if (a.getArenaState() == ArenaState.JOIN || (a.getArenaState() == ArenaState.STARTING && !a.getIngameCountdownStarted()))
                    {
                        a.start(true);
                        sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll(ArenaMessageStrings.ARENA, a.getDisplayName()).replaceAll(ArenaMessageStrings.ACTION, Messages.getString("MinigamesAPI.Started", LOCALE))); //$NON-NLS-1$
                        return true;
                    }
                }
            }
            sender.sendMessage(Messages.getString("MinigamesAPI.StartNotWithinArena", LOCALE)); //$NON-NLS-1$
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase(CommandStrings.PARTY))
        {
            if (!this.getConfig().getBoolean(PluginConfigStrings.PARTY_COMMAND_ENABLED))
            {
                return true;
            }
            final CommandHandler cmdhandler = new CommandHandler();
            if (!(sender instanceof Player))
            {
                sender.sendMessage(Messages.getString("MinigamesAPI.ExecuteIngame", LOCALE)); //$NON-NLS-1$
                return true;
            }
            final Player p = (Player) sender;
            if (args.length > 0)
            {
                final String action = args[0];
                if (action.equalsIgnoreCase(CommandStrings.PARTY_INVITE))
                {
                    cmdhandler.partyInvite(sender, args, PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_ACCEPT))
                {
                    cmdhandler.partyAccept(sender, args, PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_KICK))
                {
                    cmdhandler.partyKick(sender, args, PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_LIST))
                {
                    cmdhandler.partyList(sender, args, PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_DISBAND))
                {
                    cmdhandler.partyDisband(sender, args, PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_LEAVE))
                {
                    cmdhandler.partyLeave(sender, args, PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else
                {
                    CommandHandler.sendPartyHelp("/" + cmd.getName(), sender); //$NON-NLS-1$
                }
            }
            else
            {
                CommandHandler.sendPartyHelp("/" + cmd.getName(), sender); //$NON-NLS-1$
            }
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase(CommandStrings.MGAPI) || cmd.getName().equalsIgnoreCase(CommandStrings.MGLIB) || cmd.getName().equalsIgnoreCase(CommandStrings.MAPI))
        {
            if (args.length > 0)
            {
                if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_INFO))
                {
                    if (args.length > 1)
                    {
                        final String p = args[1];
                        sender.sendMessage(Messages.getString("MinigamesAPI.DebugInfoHeader", LOCALE) + p); //$NON-NLS-1$
                        sender.sendMessage(Messages.getString("MinigamesAPI.DebugGlobalPlayers", LOCALE)); //$NON-NLS-1$
                        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                        {
                            if (pli.global_players.containsKey(p))
                            {
                                sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugGlobalPlayersLine", LOCALE), pli.getPlugin().getName())); //$NON-NLS-1$
                            }
                        }
                        sender.sendMessage(Messages.getString("MinigamesAPI.DebugGlobalLost", LOCALE)); //$NON-NLS-1$
                        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                        {
                            if (pli.global_lost.containsKey(p))
                            {
                                sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugGlobalLostLine", LOCALE), pli.getPlugin().getName())); //$NON-NLS-1$
                            }
                        }
                        sender.sendMessage(Messages.getString("MinigamesAPI.DebugSpectatorManager", LOCALE)); //$NON-NLS-1$
                        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                        {
                            pli.getSpectatorManager();
                            if (SpectatorManager.isSpectating(Bukkit.getPlayer(p)))
                            {
                                sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugSpectatorManagerLine", LOCALE), pli.getPlugin().getName())); //$NON-NLS-1$
                            }
                        }
                        sender.sendMessage(Messages.getString("MinigamesAPI.DebugArenas", LOCALE)); //$NON-NLS-1$
                        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                        {
                            if (pli.global_players.containsKey(p))
                            {
                                sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugArenasLine", LOCALE), pli.global_players.get(p).getInternalName(), pli.global_players.get(p).getArenaState().name())); //$NON-NLS-1$
                            }
                        }
                    }
                    else
                    {
                        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                        {
                            sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugAllPlayers", LOCALE), pli.getPlugin().getName())); //$NON-NLS-1$
                            for (final Arena a : pli.getArenas())
                            {
                                if (a != null)
                                {
                                    for (final String p_ : a.getAllPlayers())
                                    {
                                        sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugAllPlayersLine", LOCALE), pli.getPlugin().getName(), a.getInternalName(), p_)); //$NON-NLS-1$
                                    }
                                }
                            }
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_DEBUG))
                {
                    MinigamesAPI.debug = !MinigamesAPI.debug;
                    this.getConfig().set(PluginConfigStrings.DEBUG, MinigamesAPI.debug);
                    this.saveConfig();
                    sender.sendMessage(String.format(Messages.getString("MinigamesAPI.SetDebugMode", LOCALE), String.valueOf(MinigamesAPI.debug))); //$NON-NLS-1$
                }
                else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_LIST))
                {
                    int c = 0;
                    MinigamesAPI.getAPI();
                    for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                    {
                        c++;
                        sender.sendMessage(String.format(Messages.getString("MinigamesAPI.ListArenasLine", LOCALE), pli.getPlugin().getName(), pli.getArenas().size())); //$NON-NLS-1$
                        return false;
                    }
                    if (c < 1)
                    {
                        sender.sendMessage(Messages.getString("MinigamesAPI.NoMinigamesFound", LOCALE)); //$NON-NLS-1$
                    }
                }
                else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_TITLE))
                {
                    if (args.length > 1)
                    {
                        if (sender instanceof Player)
                        {
                            Effects.playTitle((Player) sender, args[1], 0);
                            return false;
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_SUBTITLE))
                {
                    if (args.length > 1)
                    {
                        if (sender instanceof Player)
                        {
                            Effects.playTitle((Player) sender, args[1], 1);
                            return false;
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_SIGNS))
                {
                    if (sender instanceof Player)
                    {
                        MinigamesAPI.getAPI();
                        for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                        {
                            for (final Arena a : pli.getArenas())
                            {
                                Util.updateSign(pli.getPlugin(), a);
                                sender.sendMessage(Messages.getString("MinigamesAPI.AllSignsUpdated", LOCALE)); //$NON-NLS-1$
                                return false;
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_HOLOGRAM))
                    {
                        if (sender instanceof Player)
                        {
                            final Player p = (Player) sender;
                            p.sendMessage(Messages.getString("MinigamesAPI.PlayingHologram", LOCALE)); //$NON-NLS-1$
                            Effects.playHologram(p, p.getLocation(), ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)] + "TEST", true, true); //$NON-NLS-1$
                            return false;
                        }
                    }
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_STATS_HOLOGRAM))
                    {
                        if (sender instanceof Player)
                        {
                            final Player p = (Player) sender;
                            if (args.length > 1)
                            {
                                final PluginInstance pli = this.getPluginInstance((JavaPlugin) Bukkit.getPluginManager().getPlugin(args[1]));
                                p.sendMessage(Messages.getString("MinigamesAPI.PlayingStatsHologram", LOCALE)); //$NON-NLS-1$
                                
                                Effects.playHologram(p, p.getLocation().add(0D, 1D, 0D),
                                        ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)] + Messages.getString("MinigamesAPI.StatsWins", LOCALE) + pli.getStatsInstance().getWins(p.getName()), false, false); //$NON-NLS-1$
                                Effects.playHologram(p, p.getLocation().add(0D, 0.75D, 0D),
                                        ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)] + Messages.getString("MinigamesAPI.StatsPotions", LOCALE) + pli.getStatsInstance().getPoints(p.getName()), false, false); //$NON-NLS-1$
                                Effects.playHologram(p, p.getLocation().add(0D, 0.5D, 0D),
                                        ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)] + Messages.getString("MinigamesAPI.StatsKills", LOCALE) + pli.getStatsInstance().getKills(p.getName()), false, false); //$NON-NLS-1$
                                Effects.playHologram(p, p.getLocation().add(0D, 0.25D, 0D),
                                        ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)] + Messages.getString("MinigamesAPI.StatsDeaths", LOCALE) + pli.getStatsInstance().getDeaths(p.getName()), false, false); //$NON-NLS-1$
                                return false;
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_GAMEMODE_TEST))
                    {
                        if (sender instanceof Player)
                        {
                            final Player p = (Player) sender;
                            if (p.isOp())
                            {
                                Effects.sendGameModeChange(p, 3);
                                return false;
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_BUNGEE_TEST))
                    {
                        if (sender instanceof Player)
                        {
                            final Player p = (Player) sender;
                            if (p.isOp())
                            {
                                // TODO Why we have sky wars hard coded???
                                final PluginInstance pli = MinigamesAPI.pinstances.get(Bukkit.getPluginManager().getPlugin("MGSkyWars")); //$NON-NLS-1$
                                BungeeSocket.sendSignUpdate(pli, pli.getArenas().get(0));
                                return false;
                            }
                        }
                    }
                    return true;
                }
                if (args.length < 1)
                {
                    sender.sendMessage(String.format(Messages.getString("MinigamesAPI.MinigamesLibHeader", LOCALE), this.getDescription().getVersion())); //$NON-NLS-1$
                    int c = 0;
                    MinigamesAPI.getAPI();
                    for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                    {
                        c++;
                        sender.sendMessage(String.format(Messages.getString("MinigamesAPI.PluginArenaCount", LOCALE), pli.getPlugin().getName(), pli.getArenas().size())); //$NON-NLS-1$
                    }
                    if (c < 1)
                    {
                        sender.sendMessage(Messages.getString("MinigamesAPI.NoMinigamesFound", LOCALE)); //$NON-NLS-1$
                    }
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommands", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandInfo", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandDebug", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandList", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandTitle", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandSubtitle", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandHologram", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandSigns", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandPotionEffect", LOCALE)); //$NON-NLS-1$
                    sender.sendMessage(Messages.getString("MinigamesAPI.MgApiSubcommandStatsHologram", LOCALE)); //$NON-NLS-1$
                }
                if (sender instanceof Player && args.length > 0)
                {
                    final Player p = (Player) sender;
                    boolean cont = false;
                    for (final ParticleEffectNew f : ParticleEffectNew.values())
                    {
                        if (f.name().equalsIgnoreCase(args[0]))
                        {
                            cont = true;
                        }
                    }
                    if (!cont)
                    {
                        sender.sendMessage(Messages.getString("MinigamesAPI.CannotFindParticleEffect", LOCALE)); //$NON-NLS-1$
                        return true;
                    }
                    final ParticleEffectNew eff = ParticleEffectNew.valueOf(args[0]);
                    eff.setId(152);
                    
                    for (float i = 0; i < 10; i++)
                    {
                        eff.animateReflected(p, p.getLocation().clone().add(i / 5F, i / 5F, i / 5F), 1F, 2);
                    }
                    
                    p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 152);
                    p.getWorld().playEffect(p.getLocation().add(0D, 1D, 0D), Effect.STEP_SOUND, 152);
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message)
    {
        if (!channel.equals(ChannelStrings.CHANNEL_BUNGEE_CORD))
        {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();
        System.out.println(subchannel);
        if (subchannel.equals(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_BACK))
        {
            final short len = in.readShort();
            final byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);
            
            final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try
            {
                final String playerData = msgin.readUTF();
                final String[] split = playerData.split(":"); //$NON-NLS-1$
                final String plugin_ = split[0];
                final String arena = split[1];
                final String playername = split[2];
                
                if (debug)
                {
                    this.getLogger().info("channel message: " + ChannelStrings.SUBCHANNEL_MINIGAMESLIB_BACK + " -> " + playerData); //$NON-NLS-1$ //$NON-NLS-2$
                }
                
                JavaPlugin plugin = null;
                for (final JavaPlugin pl : MinigamesAPI.pinstances.keySet())
                {
                    if (pl.getName().contains(plugin_))
                    {
                        plugin = pl;
                        break;
                    }
                }
                if (plugin != null)
                {
                    final Arena a = MinigamesAPI.pinstances.get(plugin).getArenaByName(arena);
                    if (a != null)
                    {
                        if (a.getArenaState() != ArenaState.INGAME && a.getArenaState() != ArenaState.RESTARTING && !a.containsPlayer(playername))
                        {
                            Bukkit.getScheduler().runTaskLater(this, () -> {
                                if (!a.containsPlayer(playername))
                                {
                                    a.joinPlayerLobby(playername);
                                }
                            }, 20L);
                        }
                    }
                    else
                    {
                        this.getLogger().warning("Arena " + arena + " for MINIGAMESLIB_BACK couldn't be found, please fix your setup."); //$NON-NLS-1$//$NON-NLS-2$
                    }
                }
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (subchannel.equals(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_REQUEST))
        { // Lobby requests sign data
            final short len = in.readShort();
            final byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);
            
            final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try
            {
                final String requestData = msgin.readUTF();
                final String[] split = requestData.split(":"); //$NON-NLS-1$
                final String plugin_ = split[0];
                final String arena = split[1];
                
                if (debug)
                {
                    this.getLogger().info("channel message: " + ChannelStrings.SUBCHANNEL_MINIGAMESLIB_REQUEST + " -> " + requestData); //$NON-NLS-1$ //$NON-NLS-2$
                }
                
                for (final JavaPlugin pl : MinigamesAPI.pinstances.keySet())
                {
                    if (pl.getName().contains(plugin_))
                    {
                        final Arena a = MinigamesAPI.pinstances.get(pl).getArenaByName(arena);
                        if (a != null)
                        {
                            BungeeUtil.sendSignUpdateRequest(pl, pl.getName(), a);
                        }
                        else
                        {
                            this.getLogger().warning("Arena " + arena + " for MINIGAMESLIB_REQUEST couldn't be found, please fix your setup."); //$NON-NLS-1$//$NON-NLS-2$
                        }
                        break;
                    }
                }
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Returns the minigames lib representation of a minigame plugin.
     * 
     * @param plugin
     *            minigame java plugin.
     * @return plugin instance or {@code null} if this is not a minigames plugin or if setupAPI was not called.
     */
    public PluginInstance getPluginInstance(final JavaPlugin plugin)
    {
        return MinigamesAPI.pinstances.get(plugin);
    }
    
    /**
     * Version safe conversion utility (temporary workaround)
     * 
     * @param playername
     * @return uuid of the player
     * @deprecated starting with 1.5.0 (and by dropping support for spigot 1.7.x) you should directly use {@link Player#getUniqueId()}
     */
    @Deprecated
    public static UUID playerToUUID(String playername)
    {
        final Player player = Bukkit.getPlayer(playername);
        if (player != null)
        {
            return playerToUUID(player);
        }
        return null;
    }
    
    /**
     * Version safe conversion utility (temporary workaround)
     * 
     * @param player
     * @return uuid of the player
     * @deprecated starting with 1.5.0 (and by dropping support for spigot 1.7.x) you should directly use {@link Player#getUniqueId()}
     */
    @Deprecated
    public static UUID playerToUUID(Player player)
    {
        if (SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
        {
            return player.getUniqueId();
        }
        
        try
        {
            final Object handle = player.getClass().getDeclaredMethod("getHandle").invoke(player); //$NON-NLS-1$
            return (UUID) handle.getClass().getDeclaredMethod("getUniqueID").invoke(handle); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            instance.getLogger().log(Level.SEVERE, "Problems converting player to uuid.", ex); //$NON-NLS-1$
            return null;
        }
    }
    
    /**
     * Version safe conversion utility (temporary workaround)
     * 
     * @param uuid
     * @return player object
     * @deprecated starting with 1.5.0 (and by dropping support for spigot 1.7.x) you should directly use {@link Bukkit#getPlayer(UUID)}
     */
    @Deprecated
    public static Player uuidToPlayer(UUID uuid)
    {
        if (SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
        {
            return Bukkit.getPlayer(uuid);
        }
        
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        {
            if (uuid.equals(playerToUUID(p)))
            {
                return p;
            }
        }
        return null;
    }
    
}
