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
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

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
import com.comze_instancelabs.minigamesapi.util.Signs;
import com.comze_instancelabs.minigamesapi.util.UpdaterNexus;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.milkbowl.vault.economy.Economy;

/**
 * Main minigames API; plugin mplementation.
 * 
 * @author instancelabs
 *
 */
public class MinigamesAPI extends JavaPlugin implements PluginMessageListener, Listener
{
    
    /** the overall minecraft server versioon. */
    public static final MinecraftVersionsType         SERVER_VERSION        = MinigamesAPI.getServerVersion();
    
    /** the locale to be used. TODO: Change via config */
    public static Locale                              LOCALE                = Locale.ENGLISH;
    
    /** the minigames plugin instance. */
    private static MinigamesAPI                       instance              = null;
    
    /**
     * Vault economy instance.
     * 
     * @deprecated will be private and non-static in 1.5.0; replaced by new method
     */
    @Deprecated
    public static Economy                             econ                  = null;
    
    /**
     * {@code true} if economy is installed.
     * 
     * @deprecated will be private and non-static in 1.5.0, replace by {@link #economyAvailable()}
     */
    @Deprecated
    public static boolean                             economy               = true;
    
    /**
     * {@code true} if crackshot is installed.
     * 
     * @deprecated will be private in 1.5.0, replace by {@link #crackshotAvailable()}
     */
    @Deprecated
    public boolean                                    crackshot             = false;
    
    /** a global debug flag; controls the output of finer debug messages. */
    public static boolean                             debug                 = false;
    
    /**
     * @deprecated will be removed in 1.5.0
     */
    @Deprecated
    int                                               updatetime            = 20 * 10;
    
    /**
     * TODO decribe this field.
     * 
     * @deprecated will be be private in 1.5.0; replaced by new method
     */
    @Deprecated
    public HashMap<String, Party>                     global_party          = new HashMap<>();
    
    /**
     * TODO decribe this field.
     * 
     * @deprecated will be be private in 1.5.0; replaced by new method
     */
    @Deprecated
    public HashMap<String, ArrayList<Party>>          global_party_invites  = new HashMap<>();
    
    /**
     * Hash map with internal plugin representations of each registered minigame.
     * 
     * @deprecated will be private in 1.5.0; replaced by {@link #getPluginInstance(JavaPlugin)}
     */
    @Deprecated
    public static HashMap<JavaPlugin, PluginInstance> pinstances            = new HashMap<>();
    
    /**
     * The party messages.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public PartyMessagesConfig                        partymessages;
    
    /**
     * The stats config.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public StatsGlobalConfig                          statsglobal;
    
    /**
     * textual server version.
     * 
     * @deprecated will be removed in 1.5.0; replaced by SERVER_VERSION enumeration.
     */
    @Deprecated
    public String                                     internalServerVersion = "";                             //$NON-NLS-1$
    
    /**
     * {@code true} if this is below 1.7.10
     * 
     * @deprecated will be removed in 1.5.0; replaced by SERVER_VERSION enumeration.
     */
    public boolean                                    below1710             = false;
    
    /**
     * The plugin metrics report.
     */
    Metrics                                           metrics;
    
    /** the current motd */
    private String motd;
    
    /** suppliers or the motd strings. */
    private Iterator<Supplier<String>> motdStrings = Collections.emptyIterator();
    
    @Override
    public void onEnable()
    {
        MinigamesAPI.instance = this;
        
        this.internalServerVersion = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1); //$NON-NLS-1$
        this.below1710 = MinigamesAPI.SERVER_VERSION.isBelow(MinecraftVersionsType.V1_7_R4);
        this.getLogger().info(String.format("§c§lLoaded MinigamesAPI. We're on %0$s.", MinigamesAPI.SERVER_VERSION.name())); //$NON-NLS-1$
        
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, ChannelStrings.CHANNEL_BUNGEE_CORD);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, ChannelStrings.CHANNEL_BUNGEE_CORD, this);
        
        if (!this.setupEconomy())
        {
            this.getLogger().severe(String.format("[%s] - No Economy (Vault) dependency found! Disabling Economy.", this.getDescription().getName())); //$NON-NLS-1$
            MinigamesAPI.economy = false;
        }
        
        this.getConfig().options().header("Want bugfree versions? Set this to true for automatic updates:"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.AUTO_UPDATING, true);
        this.getConfig().addDefault(PluginConfigStrings.POST_METRICS, true);
        this.getConfig().addDefault(PluginConfigStrings.SIGNS_UPDATE_TIME, 20);
        this.getConfig().addDefault(PluginConfigStrings.PARTY_COMMAND_ENABLED, true);
        this.getConfig().addDefault(PluginConfigStrings.DEBUG, false);
        
        this.getConfig().addDefault(PluginConfigStrings.PERMISSION_PREFIX, "ancient.core"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.PERMISSION_KITS_PREFIX, "ancient.core.kits"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.PERMISSION_GUN_PREFIX, "ancient.core.guns"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.PERMISSION_SHOP_PREFIX, "ancient.core.shopitems"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.PERMISSION_GAME_PREFIX, "ancient."); //$NON-NLS-1$
        
        this.getConfig().addDefault(PluginConfigStrings.MOTD_ENABLED, false);
        this.getConfig().addDefault(PluginConfigStrings.MOTD_ROTATION_SECONDS, 15);
        this.getConfig().addDefault(PluginConfigStrings.MOTD_TEXT, "<minigame> arena <arena> <state>: <players>/<maxplayers>"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.MOTD_STATE_JOIN, "JOIN"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.MOTD_STATE_STARTING, "STARTING"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.MOTD_STATE_INGAME, "INGAME"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.MOTD_STATE_RESTARTING, "RESTARTING"); //$NON-NLS-1$
        this.getConfig().addDefault(PluginConfigStrings.MOTD_STATE_DISABLED, "DISABLED"); //$NON-NLS-1$
        
        for (final ArenaState state : ArenaState.values())
        {
            this.getConfig().addDefault("signs." + state.name().toLowerCase() + ".0", state.getColorCode() + "<minigame>");
            this.getConfig().addDefault("signs." + state.name().toLowerCase() + ".1", state.getColorCode() + "<arena>");
            this.getConfig().addDefault("signs." + state.name().toLowerCase() + ".2", state.getColorCode() + "<count>/<maxcount>");
            this.getConfig().addDefault("signs." + state.name().toLowerCase() + ".3", state.getColorCode() + "");
        }
        this.getConfig().addDefault("signs.spec.0", "&aSPECTATE");
        this.getConfig().addDefault("signs.spec.1", "&a<minigame>");
        this.getConfig().addDefault("signs.spec.2", "&a<arena>");
        this.getConfig().addDefault("signs.spec.3", "&a<count>/<maxcount>");
        
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        this.partymessages = new PartyMessagesConfig(this);
        this.statsglobal = new StatsGlobalConfig(this, false);
        
        MinigamesAPI.debug = this.getConfig().getBoolean(PluginConfigStrings.DEBUG);
        
        if (this.getConfig().getBoolean(PluginConfigStrings.POST_METRICS, true))
        {
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
        }
        
        if (this.getConfig().getBoolean(PluginConfigStrings.AUTO_UPDATING))
        {
            // new UpdaterBukkit(this, 83025, this.getFile(), UpdaterBukkit.UpdateType.DEFAULT, false);
            new UpdaterNexus(this, this.getFile());
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
        
        if (this.getConfig().getBoolean(PluginConfigStrings.MOTD_ENABLED))
        {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                if (this.motdStrings.hasNext())
                {
                    this.motd = this.motdStrings.next().get();
                }
                else
                {
                    final List<Supplier<String>> suppliers = new ArrayList<>();
                    for (final PluginInstance pli : MinigamesAPI.pinstances.values())
                    {
                        for (final Arena arena : pli.getArenas())
                        {
                            suppliers.add(() -> {
                                String motdString = getConfig().getString(PluginConfigStrings.MOTD_TEXT);
                                String stateString = null;
                                if (arena.isSuccessfullyInit() && pli.arenaSetup.getArenaEnabled(pli.getPlugin(), arena.getInternalName()))
                                {
                                    switch (arena.getArenaState())
                                    {
                                        case INGAME:
                                            stateString = getConfig().getString(PluginConfigStrings.MOTD_STATE_INGAME);
                                            break;
                                        case JOIN:
                                            stateString = getConfig().getString(PluginConfigStrings.MOTD_STATE_JOIN);
                                            break;
                                        case RESTARTING:
                                            stateString = getConfig().getString(PluginConfigStrings.MOTD_STATE_RESTARTING);
                                            break;
                                        case STARTING:
                                            stateString = getConfig().getString(PluginConfigStrings.MOTD_STATE_STARTING);
                                            break;
                                        default:
                                            stateString = getConfig().getString(PluginConfigStrings.MOTD_STATE_DISABLED);
                                            break;
                                    }
                                }
                                else
                                {
                                    stateString = getConfig().getString(PluginConfigStrings.MOTD_STATE_DISABLED);
                                }
                                motdString = motdString.replace("<minigame>", arena.getPluginInstance().getPlugin().getDescription().getName());
                                motdString = motdString.replace("<arena>", arena.getDisplayName());
                                motdString = motdString.replace("<state>", stateString);
                                motdString = motdString.replace("<players>", String.valueOf(arena.getAllPlayers().size()));
                                motdString = motdString.replace("<minplayers>", String.valueOf(arena.getMinPlayers()));
                                motdString = motdString.replace("<maxplayers>", String.valueOf(arena.getMaxPlayers()));
                                return motdString;
                            });
                        }
                    }
                    this.motdStrings = suppliers.iterator();
                    if (this.motdStrings.hasNext())
                    {
                        this.motd = this.motdStrings.next().get();
                    }
                    else
                    {
                        this.motd = null;
                    }
                }
            }, 0, 20 * this.getConfig().getInt(PluginConfigStrings.MOTD_ROTATION_SECONDS));
        }
        
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    /**
     * Returns the arena for player if already playing
     * @param playerName players name
     * @return arena or {@code null} if not inside an arena
     */
    public Arena getArenaForPlayer(String playerName)
    {
        for (final PluginInstance pli : this.pinstances.values())
        {
            final Arena arena = pli.global_players.get(playerName);
            if (arena != null) return arena;
        }
        return null;
    }
    
    /**
     * Checks if crackshot is available.
     * 
     * @return {@code true} if crackshot is available.
     */
    public boolean crackshotAvailable()
    {
        return this.crackshot;
    }
    
    /**
     * Checks if economy is available.
     * 
     * @return {@code true} if economy is available.
     */
    public boolean economyAvailable()
    {
        return this.economy;
    }
    
    /**
     * Returns the permission prefix for minigames lib itself.
     * @return permission prefix minigames lib.
     */
    public String getPermissionPrefix()
    {
        return this.getConfig().getString(PluginConfigStrings.PERMISSION_PREFIX, "minigames.core"); //$NON-NLS-1$
    }
    
    /**
     * Returns the permission prefix for minigames lib itself.
     * @return permission prefix minigames lib.
     */
    public String getPermissionKitPrefix()
    {
        return this.getConfig().getString(PluginConfigStrings.PERMISSION_KITS_PREFIX, "minigames.core.kits"); //$NON-NLS-1$
    }
    
    /**
     * Returns the permission prefix for minigames lib itself.
     * @return permission prefix minigames lib.
     */
    public String getPermissionGunPrefix()
    {
        return this.getConfig().getString(PluginConfigStrings.PERMISSION_GUN_PREFIX, "minigames.core.guns"); //$NON-NLS-1$
    }
    
    /**
     * Returns the permission prefix for minigames lib itself.
     * @return permission prefix minigames lib.
     */
    public String getPermissionShopPrefix()
    {
        return this.getConfig().getString(PluginConfigStrings.PERMISSION_SHOP_PREFIX, "minigames.core.shopitems"); //$NON-NLS-1$
    }
    
    /**
     * Returns the permission prefix for a minigame.
     * @param game the minigame name.
     * @return permission prefix
     */
    public String getPermissionGamePrefix(String game)
    {
        return this.getConfig().getString(PluginConfigStrings.PERMISSION_GAME_PREFIX, "minigames.") + game; //$NON-NLS-1$
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
            if (v.startsWith("v1_11_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_11_R1;
            }
            if (v.startsWith("v1_12_R1")) //$NON-NLS-1$
            {
                return MinecraftVersionsType.V1_12_R1;
            }
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
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
     *            the class implementing/ overriding the arena class; {@link Arena}. TODO currently not in use
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
        new DefaultConfig(plugin_, false); // force initialization of config.yml
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
            if (!sender.hasPermission(getPermissionPrefix() + PermissionStrings.MINIGAMES_START))
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
                        sender.sendMessage(pli.getMessagesConfig().arena_action.replaceAll(ArenaMessageStrings.ARENA, a.getDisplayName()).replaceAll(ArenaMessageStrings.ACTION,
                                Messages.getString("MinigamesAPI.Started", LOCALE))); //$NON-NLS-1$
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
                    cmdhandler.partyInvite(sender, args, getPermissionPrefix() + PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_ACCEPT))
                {
                    cmdhandler.partyAccept(sender, args, getPermissionPrefix() + PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_KICK))
                {
                    cmdhandler.partyKick(sender, args, getPermissionPrefix() + PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_LIST))
                {
                    cmdhandler.partyList(sender, args, getPermissionPrefix() + PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_DISBAND))
                {
                    cmdhandler.partyDisband(sender, args, getPermissionPrefix() + PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
                }
                else if (action.equalsIgnoreCase(CommandStrings.PARTY_LEAVE))
                {
                    cmdhandler.partyLeave(sender, args, getPermissionPrefix() + PermissionStrings.MINIGAMES_PARTY, "/" + cmd.getName(), action, this, p); //$NON-NLS-1$
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
                                sender.sendMessage(String.format(Messages.getString("MinigamesAPI.DebugArenasLine", LOCALE), pli.global_players.get(p).getInternalName(), //$NON-NLS-1$
                                        pli.global_players.get(p).getArenaState().name()));
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
                                
                                Effects.playHologram(p, p.getLocation().add(0D, 1D, 0D), ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)]
                                        + Messages.getString("MinigamesAPI.StatsWins", LOCALE) + pli.getStatsInstance().getWins(p.getName()), false, false); //$NON-NLS-1$
                                Effects.playHologram(p, p.getLocation().add(0D, 0.75D, 0D), ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)]
                                        + Messages.getString("MinigamesAPI.StatsPotions", LOCALE) + pli.getStatsInstance().getPoints(p.getName()), false, false); //$NON-NLS-1$
                                Effects.playHologram(p, p.getLocation().add(0D, 0.5D, 0D), ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)]
                                        + Messages.getString("MinigamesAPI.StatsKills", LOCALE) + pli.getStatsInstance().getKills(p.getName()), false, false); //$NON-NLS-1$
                                Effects.playHologram(p, p.getLocation().add(0D, 0.25D, 0D), ChatColor.values()[(int) (Math.random() * ChatColor.values().length - 1)]
                                        + Messages.getString("MinigamesAPI.StatsDeaths", LOCALE) + pli.getStatsInstance().getDeaths(p.getName()), false, false); //$NON-NLS-1$
                                return false;
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_JOIN))
                    {
                        if (args.length > 3)
                        {
                            String game = args[1];
                            String arena = args[2];
                            String server = args[3];
                            
                            Player p = null;
                            
                            if (sender instanceof Player)
                            {
                                p = (Player) sender;
                            }
                            if (args.length > 3)
                            {
                                // TODO permission to teleport foreign players
                                p = Bukkit.getPlayer(args[3]);
                            }
                            if (p == null)
                            {
                                return true;
                            }
                            
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            try
                            {
                                out.writeUTF("Forward");
                                out.writeUTF("ALL");
                                out.writeUTF(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_BACK);
                                
                                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                                DataOutputStream msgout = new DataOutputStream(msgbytes);
                                String info = game + ":" + arena + ":" + p.getName();
                                getLogger().info("player join: " + info); //$NON-NLS-1$
                                msgout.writeUTF(info);
                                
                                out.writeShort(msgbytes.toByteArray().length);
                                out.write(msgbytes.toByteArray());
                                
                                Bukkit.getServer().sendPluginMessage(this, ChannelStrings.CHANNEL_BUNGEE_CORD, out.toByteArray());
                            }
                            catch (Exception e)
                            {
                                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                            }
                            connectToServer(this, p.getName(), server);
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.GRAY + "Usage: /join <game> <arena> <server> [player]");
                            sender.sendMessage(ChatColor.GRAY + "[player] is optional.");
                        }
                    }
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_GAMEMODE_TEST)) // TODO remove
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
                    else if (args[0].equalsIgnoreCase(CommandStrings.MGLIB_BUNGEE_TEST)) // TODO remove
                    {
                        if (sender instanceof Player)
                        {
                            final Player p = (Player) sender;
                            if (p.isOp())
                            {
                                // final PluginInstance pli = MinigamesAPI.pinstances.get(Bukkit.getPluginManager().getPlugin("MGSkyWars")); //$NON-NLS-1$
                                // BungeeSocket.sendSignUpdate(pli, pli.getArenas().get(0));
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
                    // TODO Subcommand join
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
    
    private void connectToServer(JavaPlugin plugin, String player, String server)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try
        {
            out.writeUTF("Connect");
            out.writeUTF(server);
        }
        catch (IOException e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        Bukkit.getPlayer(player).sendPluginMessage(plugin, ChannelStrings.CHANNEL_BUNGEE_CORD, stream.toByteArray());
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
                final String playername = split.length >=4 ? split[3] : split[2];
                final String mode = split.length >=4 ? split[2] : "join";
                
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
                                switch (mode)
                                {
                                    case "join":
                                    default:
                                        if (!a.containsPlayer(playername))
                                        {
                                            a.joinPlayerLobby(playername);
                                        }
                                        break;
                                    case "spec":
                                        if (!a.containsPlayer(playername))
                                        {
                                            final ArenaPlayer ap = ArenaPlayer.getPlayerInstance(playername);
                                            a.joinSpectate(ap.getPlayer());
                                        }
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
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
        else if (subchannel.equals(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_REQUEST))
        {
            // Lobby requests sign data
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
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
        else if (subchannel.equals(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_SIGN))
        {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                final String signData = msgin.readUTF();
                final String[] splitted = signData.split(":"); //$NON-NLS-1$
                final String plugin_ = splitted[0];
                final String arena = splitted[1];
                final String arenastate = splitted[2];
                final int count = Integer.parseInt(splitted[3]);
                final int maxcount = Integer.parseInt(splitted[4]);
                
                if (debug)
                {
                    this.getLogger().info("channel message: " + ChannelStrings.SUBCHANNEL_MINIGAMESLIB_SIGN + " -> " + signData); //$NON-NLS-1$ //$NON-NLS-2$
                }

                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    public void run() {
                        updateSign(plugin_, arena, arenastate, count, maxcount);
                    }
                }, 10L);
            } catch (IOException e) {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
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
    
    // Bungee-support
    
    /**
     * Sign break event.
     * 
     * <p>
     * TODO description.
     * </p>
     * 
     * @param event
     */
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.WALL_SIGN)
        {
            if (getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX))
            {
                for (String mg_key : getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false))
                {
                    for (String arena_key : getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + mg_key + ".").getKeys(false))
                    {
                        final ConfigurationSection section = getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key);
                        if (section.contains("world"))
                        {
                            Location l = new Location(Bukkit.getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".world")),
                                    getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.x"),
                                    getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.y"),
                                    getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.z"));
                            if (l.distance(event.getBlock().getLocation()) < 1)
                            {
                                getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".server", null);
                                getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".world", null);
                                getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc", null);
                                saveConfig();
                                return;
                            }
                        }
                        if (section.contains("specworld"))
                        {
                            Location l = new Location(Bukkit.getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specworld")),
                                    getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc.x"),
                                    getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc.y"),
                                    getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc.z"));
                            if (l.distance(event.getBlock().getLocation()) < 1)
                            {
                                getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specserver", null);
                                getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specworld", null);
                                getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc", null);
                                saveConfig();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Sign use event.
     * 
     * <p>
     * TODO description.
     * </p>
     * 
     * @param event
     */
    @EventHandler
    public void onSignUse(PlayerInteractEvent event)
    {
        if (event.hasBlock())
        {
            if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)
            {
                if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
                {
                    return;
                }
                final Sign s = (Sign) event.getClickedBlock().getState();
                String server = getServerBySignLocation(s.getLocation());
                if (server != null && server != "")
                {
                    final Player player = event.getPlayer();
                    final String signInfo = getInfoBySignLocation(s.getLocation());
                    
                    if (MinigamesAPI.getAPI().global_party.containsKey(player.getName()))
                    {
                        final Party party = MinigamesAPI.getAPI().global_party.remove(player.getName());
                        for (final String p_ : party.getPlayers())
                        {
                            if (Validator.isPlayerOnline(p_))
                            {
                                boolean cont = true;
                                MinigamesAPI.getAPI();
                                for (final PluginInstance pli_ : MinigamesAPI.pinstances.values())
                                {
                                    if (pli_.containsGlobalPlayer(p_))
                                    {
                                        cont = false;
                                    }
                                }
                                if (cont)
                                {
                                    letPlayerJoinServer(server, Bukkit.getPlayer(p_), signInfo);
                                }
                            }
                        }
                    }
                    
                    letPlayerJoinServer(server, player, signInfo);
                }
            }
        }
        
    }

    /**
     * Let a player join a server over bungeecord network
     * @param server
     * @param player
     * @param signInfo
     */
    private void letPlayerJoinServer(String server, final Player player, final String signInfo)
    {
        try
        {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try
            {
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_BACK);
                
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                String info = signInfo + ":" + player.getName();
                msgout.writeUTF(info);
                
                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());
                
                Bukkit.getServer().sendPluginMessage(this, ChannelStrings.CHANNEL_BUNGEE_CORD, out.toByteArray());
            }
            catch (Exception e)
            {
                this.getLogger().log(Level.WARNING, "error sending message", e);
            }
        }
        catch (Exception e)
        {
            this.getLogger().log(Level.WARNING, "Error occurred while sending first sign request - Invalid server/minigame/arena?", e);
        }
        connectToServer(this, player.getName(), server);
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {
        Player p = event.getPlayer();
        if (event.getLine(0).toLowerCase().equalsIgnoreCase("mglib"))
        {
            if (event.getPlayer().hasPermission(getPermissionPrefix() + ".sign") || event.getPlayer().isOp())
            {
                if (!event.getLine(1).equalsIgnoreCase("") && !event.getLine(2).equalsIgnoreCase("") && !event.getLine(3).equalsIgnoreCase(""))
                {
                    String mg = event.getLine(1);
                    String arena = event.getLine(2);
                    String server = event.getLine(3);
                    
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".server", server);
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".world", p.getWorld().getName());
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".loc.x", event.getBlock().getLocation().getBlockX());
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".loc.y", event.getBlock().getLocation().getBlockY());
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".loc.z", event.getBlock().getLocation().getBlockZ());
                    saveConfig();
                    
                    p.sendMessage(ChatColor.GREEN + "Successfully set sign.");
                    
                    updateSign(mg, arena, "JOIN", event);
                    
                    requestServerSign(mg, arena);
                    
                }
            }
        }
        else if (event.getLine(0).toLowerCase().equalsIgnoreCase("mglibspec"))
        {
            if (event.getPlayer().hasPermission(getPermissionPrefix() + ".sign") || event.getPlayer().isOp())
            {
                if (!event.getLine(1).equalsIgnoreCase("") && !event.getLine(2).equalsIgnoreCase("") && !event.getLine(3).equalsIgnoreCase(""))
                {
                    String mg = event.getLine(1);
                    String arena = event.getLine(2);
                    String server = event.getLine(3);
                    
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specserver", server);
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specworld", p.getWorld().getName());
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specloc.x", event.getBlock().getLocation().getBlockX());
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specloc.y", event.getBlock().getLocation().getBlockY());
                    getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specloc.z", event.getBlock().getLocation().getBlockZ());
                    saveConfig();
                    
                    p.sendMessage(ChatColor.GREEN + "Successfully set sign.");
                    
                    updateSign(mg, arena, "SPEC", event);
                    
                    requestServerSign(mg, arena);
                    
                }
            }
        }
    }
    
    public void requestServerSign(String mg_key, String arena_key)
    {
        try
        {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try
            {
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_REQUEST);
                
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(mg_key + ":" + arena_key);
                
                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());
                
                Bukkit.getServer().sendPluginMessage(this, ChannelStrings.CHANNEL_BUNGEE_CORD, out.toByteArray());
                
                // TODO if no answer after 2 seconds, server empty!
                
            }
            catch (Exception e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
        catch (Exception e)
        {
            this.getLogger().log(Level.WARNING,"Error occurred while sending extra sign request: ", e);
        }
    }

    private Sign getSignFromArena(String mg, String arena) {
        if (!getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".world")) {
            return null;
        }
        Location b_ = new Location(Bukkit.getServer().getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".world")), getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".loc.x"), getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".loc.y"), getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".loc.z"));
        if (b_ != null) {
            if (b_.getWorld() != null) {
                if (b_.getBlock().getState() != null) {
                    BlockState bs = b_.getBlock().getState();
                    Sign s_ = null;
                    if (bs instanceof Sign) {
                        s_ = (Sign) bs;
                    }
                    return s_;
                }
            }
        }
        return null;
    }

    private Sign getSpecSignFromArena(String mg, String arena) {
        if (!getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specworld")) {
            return null;
        }
        Location b_ = new Location(Bukkit.getServer().getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specworld")), getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specloc.x"), getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specloc.y"), getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg + "." + arena + ".specloc.z"));
        if (b_ != null) {
            if (b_.getWorld() != null) {
                if (b_.getBlock().getState() != null) {
                    BlockState bs = b_.getBlock().getState();
                    Sign s_ = null;
                    if (bs instanceof Sign) {
                        s_ = (Sign) bs;
                    }
                    return s_;
                }
            }
        }
        return null;
    }

    public void updateSign(String mg, String arenaname, String arenastate, int count, int maxcount) {
        Sign s = getSignFromArena(mg, arenaname);
        if (s != null) {
            s.getBlock().getChunk().load();
            s.setLine(0, Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".0").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.setLine(1, Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".1").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.setLine(2, Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".2").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.setLine(3, Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".3").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.getBlock().getChunk().load();
            s.update();
        }s = getSpecSignFromArena(mg, arenaname);
        if (s != null) {
            s.getBlock().getChunk().load();
            s.setLine(0, Signs.format(getConfig().getString("signs.spec.0").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.setLine(1, Signs.format(getConfig().getString("signs.spec.1").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.setLine(2, Signs.format(getConfig().getString("signs.spec.2").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.setLine(3, Signs.format(getConfig().getString("signs.spec.3").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arenaname).replace("<minigame>", mg)));
            s.getBlock().getChunk().load();
            s.update();
        }
    }
    
    public void sendSignUpdate(final PluginInstance pli, final Arena a)
    {
        String signString;

        if (a == null)
        {
            signString = pli.getPlugin().getName() + ":null:JOIN:0:0";
        }
        else
        {
            signString = pli.getPlugin().getName() + ":" + a.getInternalName() + ":" + a.getArenaState().toString() + ":" + a.getAllPlayers().size() + ":" + a.getMaxPlayers();
        }
        
        try
        {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try
            {
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_SIGN);
                
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(signString);
                
                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());
                
                Bukkit.getServer().sendPluginMessage(this, ChannelStrings.CHANNEL_BUNGEE_CORD, out.toByteArray());
            }
            catch (Exception e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
            }
        }
        catch (Exception e)
        {
            this.getLogger().log(Level.WARNING,"Error occurred while sending extra sign request: ", e);
        }
    }

    public void updateSign(String mg, String arenaname, String arenastate, SignChangeEvent event)
    {
        int count = 0;
        int maxcount = 10;
        event.setLine(0,
                Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".0").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                        .replace("<arena>", arenaname).replace("<minigame>", mg)));
        event.setLine(1,
                Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".1").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                        .replace("<arena>", arenaname).replace("<minigame>", mg)));
        event.setLine(2,
                Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".2").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                        .replace("<arena>", arenaname).replace("<minigame>", mg)));
        event.setLine(3,
                Signs.format(getConfig().getString("signs." + arenastate.toLowerCase() + ".3").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount))
                        .replace("<arena>", arenaname).replace("<minigame>", mg)));
    }
    
    public String getServerBySignLocation(Location sign)
    {
        if (getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX))
        {
            for (String mg_key : getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false))
            {
                for (String arena_key : getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + mg_key + ".").getKeys(false))
                {
                    Location l = new Location(Bukkit.getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".world")),
                            getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.x"),
                            getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.y"),
                            getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.z"));
                    if (l.distance(sign) < 1)
                    {
                        return getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".server");
                    }
                }
            }
        }
        return "";
    }
    
    public String getInfoBySignLocation(Location sign)
    {
        if (getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX))
        {
            for (String mg_key : getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false))
            {
                for (String arena_key : getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + mg_key + ".").getKeys(false))
                {
                    final ConfigurationSection section = getConfig().getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key);
                    if (section.contains("world"))
                    {
                        Location l = new Location(Bukkit.getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".world")),
                                getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.x"),
                                getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.y"),
                                getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".loc.z"));
                        if (l.distance(sign) < 1)
                        {
                            return mg_key + ":" + arena_key + ":join";
                        }
                    }
                    if (section.contains("specworld"))
                    {
                        Location l = new Location(Bukkit.getWorld(getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specworld")),
                                getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc.x"),
                                getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc.y"),
                                getConfig().getInt(ArenaConfigStrings.ARENAS_PREFIX + mg_key + "." + arena_key + ".specloc.z"));
                        if (l.distance(sign) < 1)
                        {
                            return mg_key + ":" + arena_key + ":spec";
                        }
                    }
                }
            }
        }
        return "";
    }
    
    @EventHandler
    public void onServerPing(ServerListPingEvent evt)
    {
        if (this.motd != null)
        {
            evt.setMotd(this.motd);
        }
    }
    
}
