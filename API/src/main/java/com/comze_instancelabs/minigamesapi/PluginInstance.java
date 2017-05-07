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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.achievements.ArenaAchievements;
import com.comze_instancelabs.minigamesapi.config.AchievementsConfig;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.GunsConfig;
import com.comze_instancelabs.minigamesapi.config.HologramsConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.ShopConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.guns.Gun;
import com.comze_instancelabs.minigamesapi.sql.MainSQL;
import com.comze_instancelabs.minigamesapi.statsholograms.Holograms;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.ArenaLobbyScoreboard;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * Internal representation of a minigames plugin.
 * 
 * @author instancelabs
 */
public class PluginInstance
{
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public HashMap<String, Arena>               global_players                        = new HashMap<>();
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public HashMap<String, Arena>               global_lost                           = new HashMap<>();
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public HashMap<String, Arena>               global_arcade_spectator               = new HashMap<>();
    
    /**
     * The arena listener for this plugin.
     */
    private ArenaListener                       arenalistener                         = null;
    
    /**
     * The arenas configuration (arenas.yml).
     */
    private ArenasConfig                        arenasconfig                          = null;
    
    /**
     * The classes configuration (classes.yml).
     */
    private ClassesConfig                       classesconfig                         = null;
    
    /**
     * The messages configuration (messages.yml).
     */
    private MessagesConfig                      messagesconfig                        = null;
    
    /**
     * The stats configuration (stats.yml).
     */
    private StatsConfig                         statsconfig                           = null;
    
    /**
     * The guns configuration (guns.yml).
     */
    private GunsConfig                          gunsconfig                            = null;
    
    /**
     * The achievements configuration (achievements.yml)
     */
    private AchievementsConfig                  achievementsconfig                    = null;
    
    /**
     * The shops config (shops.yml)
     */
    private ShopConfig                          shopconfig                            = null;
    
    /**
     * The holograms config (holograms.yml)
     */
    private HologramsConfig                     hologramsconfig                       = null;
    
    /**
     * The minigames spigot/java plugin
     */
    private JavaPlugin                          plugin                                = null;
    
    /**
     * The known/loaded arenas.
     */
    private ArrayList<Arena>                    arenas                                = new ArrayList<>();
    
    /**
     * The classes per player.
     */
    private final HashMap<String, AClass>       pclass                                = new HashMap<>();
    
    /**
     * The current configured classes from classes config.
     */
    private final LinkedHashMap<String, AClass> aclasses                              = new LinkedHashMap<>();
    
    /**
     * The configured guns from guns config.
     */
    private final HashMap<String, Gun>          guns                                  = new HashMap<>();
    
    /**
     * The rewards.
     */
    private Rewards                             rew                                   = null;
    
    /**
     * The main sql reference for database support.
     */
    private MainSQL                             sql                                   = null;
    
    /**
     * The stats from stats config.
     */
    private Stats                               stats                                 = null;
    
    /**
     * The classes gui.
     */
    private Classes                             classes                               = null;
    
    /**
     * The shop gui.
     */
    private Shop                                shop                                  = null;
    
    /**
     * The spectator manager for controlling spectators.
     */
    private SpectatorManager                    spectatormanager                      = null;
    
    /**
     * The achivements manager.
     */
    private ArenaAchievements                   achievements                          = null;
    
    /**
     * The holograms manager.
     */
    private Holograms                           holograms                             = null;
    
    /**
     * Flag to enable the achivements gui.
     */
    private boolean                             achievement_gui_enabled               = false;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public ArenaScoreboard                      scoreboardManager;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public ArenaLobbyScoreboard                 scoreboardLobbyManager;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public ArenaSetup                           arenaSetup                            = new ArenaSetup();
    
    /**
     * Default lobby cooldown in seconds.
     * 
     * <p>
     * TODO Allow override in arena config.
     * </p>
     */
    private int                                 lobby_countdown                       = 30;
    
    /**
     * Default ingame cooldown in seconds.
     * 
     * <p>
     * TODO Allow override in arena config.
     * </p>
     */
    private int                                 ingame_countdown                      = 10;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     spectator_move_y_lock                 = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     use_xp_bar_level                      = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     blood_effects                         = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     dead_in_fake_bed_effects              = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     spectator_mode_1_8                    = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     damage_identifier_effects             = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public boolean                              color_background_wool_of_signs;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    boolean                                     last_man_standing                     = true;
    
    /**
     * {@code true} for old reset method.
     * 
     * @deprecated will be removed in 1.4.10
     */
    @Deprecated
    boolean                                     old_reset                             = false;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public boolean                              show_classes_without_usage_permission = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public boolean                              chat_enabled                          = true;
    
    /**
     * TODO describe field.
     * 
     * @deprecated will be private in 1.5.0; replaced by new methods.
     */
    @Deprecated
    public HashMap<String, ArrayList<String>>   cached_sign_states                    = new HashMap<>();
    
    /**
     * Constructor to create a new plugin instance.
     * 
     * @param plugin
     *            java plugin.
     * @param arenasconfig
     *            arenas config.
     * @param messagesconfig
     *            messages config.
     * @param classesconfig
     *            classes config.
     * @param statsconfig
     *            stats config.
     * @param arenas
     *            loaded arenas.
     */
    PluginInstance(final JavaPlugin plugin, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig,
            final ArrayList<Arena> arenas)
    {
        this.arenasconfig = arenasconfig;
        this.messagesconfig = messagesconfig;
        this.classesconfig = classesconfig;
        this.statsconfig = statsconfig;
        this.gunsconfig = new GunsConfig(plugin, false);
        this.achievementsconfig = new AchievementsConfig(plugin);
        this.shopconfig = new ShopConfig(plugin, false);
        this.hologramsconfig = new HologramsConfig(plugin, false);
        this.arenas = arenas;
        this.plugin = plugin;
        this.rew = new Rewards(plugin);
        this.stats = new Stats(this, plugin);
        this.sql = new MainSQL(plugin);
        this.classes = new Classes(this, plugin);
        this.shop = new Shop(this, plugin);
        this.spectatormanager = new SpectatorManager(plugin);
        this.achievements = new ArenaAchievements(this, plugin);
        this.holograms = new Holograms(this);
        this.scoreboardManager = new ArenaScoreboard(this, plugin);
        this.scoreboardLobbyManager = new ArenaLobbyScoreboard(this, plugin);
        this.reloadVariables();
    }
    
    /**
     * Constructor to create a new plugin instance.
     * 
     * @param plugin
     *            java plugin.
     * @param arenasconfig
     *            arenas config.
     * @param messagesconfig
     *            messages config.
     * @param classesconfig
     *            classes config.
     * @param statsconfig
     *            stats config.
     */
    PluginInstance(final JavaPlugin plugin, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig)
    {
        this(plugin, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>());
    }
    
    /**
     * Reloads variables from plugins config.yml and caches sign states.
     */
    public void reloadVariables()
    {
        this.lobby_countdown = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_LOBBY_COUNTDOWN) + 1;
        this.ingame_countdown = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_INGAME_COUNTDOWN) + 1;
        this.spectator_move_y_lock = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SPECTATOR_MOVE_Y_LOCK);
        this.use_xp_bar_level = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_XP_BAR_LEVEL);
        this.blood_effects = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EFFECTS_BLOOD);
        this.damage_identifier_effects = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EFFECTS_DMG_IDENTIFIER_HOLO);
        this.dead_in_fake_bed_effects = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EFFECTS_DEAD_IN_FAKE_BED);
        this.color_background_wool_of_signs = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_COLOR_BACKGROUND_WOOL);
        this.spectator_mode_1_8 = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EFFECTS_1_8_SPECTATOR_MODE);
        this.last_man_standing = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_LAST_MAN_STANDING_WINS);
        this.old_reset = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_OLD_RESET_METHOD);
        if (this.old_reset)
        {
            this.plugin.getLogger().severe("SEVERE! The old reset method will be removed in next version! Check if your arenas work with smart reset."); //$NON-NLS-1$
        }
        this.show_classes_without_usage_permission = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SHOW_CLASSES_WITHOUT_PERM);
        this.chat_enabled = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_CHAT_ENABLED);
        
        // Cache sign configuration
        for (final String state : ArenaState.getAllStateNames())
        {
            this.cached_sign_states.put(state,
                    new ArrayList<>(Arrays.asList(this.messagesconfig.getConfig().getString("signs." + state.toLowerCase() + ".0"),
                            this.messagesconfig.getConfig().getString("signs." + state.toLowerCase() + ".1"), this.messagesconfig.getConfig().getString("signs." + state.toLowerCase() + ".2"),
                            this.messagesconfig.getConfig().getString("signs." + state.toLowerCase() + ".3"))));
        }
        this.cached_sign_states.put("spec",
                new ArrayList<>(Arrays.asList(this.messagesconfig.getConfig().getString("signs.spec.0"),
                        this.messagesconfig.getConfig().getString("signs.spec.1"), this.messagesconfig.getConfig().getString("signs.spec.2"),
                        this.messagesconfig.getConfig().getString("signs.spec.3"))));
        
    }
    
    /**
     * Returns the minigames java plugin.
     * 
     * @return java plugin of this minigame.
     */
    public JavaPlugin getPlugin()
    {
        return this.plugin;
    }
    
    /**
     * Returns the known classes.
     * 
     * @return known classes map.
     * @deprecated method signature will change to java.util.Map; will return a read-only map; manipulation is done by a new method if needed
     */
    @Deprecated
    public HashMap<String, AClass> getAClasses()
    {
        return this.aclasses;
    }
    
    /**
     * Returns the classes per player map.
     * 
     * @return classes per players.
     * @deprecated method signature will change to java.util.Map; will return a read-only map; manipulation is done by a new method if needed
     */
    @Deprecated
    public HashMap<String, AClass> getPClasses()
    {
        return this.pclass;
    }
    
    /**
     * Adds/Sets a new class.
     * 
     * @param name
     *            class name
     * @param a
     *            class
     */
    public void addAClass(final String name, final AClass a)
    {
        this.aclasses.put(name, a);
    }
    
    /**
     * Sets a class for given player
     * 
     * @param player
     *            player name
     * @param a
     *            class
     */
    public void setPClass(final String player, final AClass a)
    {
        this.pclass.put(player, a);
    }
    
    /**
     * Returns all guns.
     * 
     * @return guns of this minigame.
     * @deprecated method signature will change to java.util.Map; will return a read-only map; manipulation is done by a new method if needed
     */
    @Deprecated
    public HashMap<String, Gun> getAllGuns()
    {
        return this.guns;
    }
    
    /**
     * Adds/Sets a new gun.
     * 
     * @param name
     *            gun name
     * @param g
     *            gun
     */
    public void addGun(final String name, final Gun g)
    {
        this.guns.put(name, g);
    }
    
    /**
     * Returns the arenas config.
     * 
     * @return arenas config.
     */
    public ArenasConfig getArenasConfig()
    {
        return this.arenasconfig;
    }
    
    /**
     * Returns the messages config.
     * 
     * @return messages config.
     */
    public MessagesConfig getMessagesConfig()
    {
        return this.messagesconfig;
    }
    
    /**
     * Returns the classes config.
     * 
     * @return classes config
     */
    public ClassesConfig getClassesConfig()
    {
        return this.classesconfig;
    }
    
    /**
     * Returns the stats config.
     * 
     * @return stats config
     */
    public StatsConfig getStatsConfig()
    {
        return this.statsconfig;
    }
    
    /**
     * Returns the guns config
     * 
     * @return guns config.
     */
    public GunsConfig getGunsConfig()
    {
        return this.gunsconfig;
    }
    
    /**
     * Returns the achievement config
     * 
     * @return achievement config
     */
    public AchievementsConfig getAchievementsConfig()
    {
        return this.achievementsconfig;
    }
    
    /**
     * Returns the shop config.
     * 
     * @return shop config.
     */
    public ShopConfig getShopConfig()
    {
        return this.shopconfig;
    }
    
    /**
     * Sets the shop config.
     * 
     * @param shopconfig
     *            new shop config.
     */
    public void setShopConfig(final ShopConfig shopconfig)
    {
        this.shopconfig = shopconfig;
    }
    
    /**
     * Returns the holograms config.
     * 
     * @return holograms config.
     */
    public HologramsConfig getHologramsConfig()
    {
        return this.hologramsconfig;
    }
    
    /**
     * Returns the reward instance.
     * 
     * @return rewards.
     */
    public Rewards getRewardsInstance()
    {
        return this.rew;
    }
    
    /**
     * Sets the reward instance.
     * 
     * @param r
     *            rewards to be used.
     */
    public void setRewardsInstance(final Rewards r)
    {
        this.rew = r;
    }
    
    /**
     * Returns the main sql instance for database support.
     * 
     * @return database support.
     */
    public MainSQL getSQLInstance()
    {
        return this.sql;
    }
    
    /**
     * Returns the stats instance.
     * 
     * @return stats instance.
     */
    public Stats getStatsInstance()
    {
        return this.stats;
    }
    
    /**
     * The arena listener.
     * 
     * @return arena listener.
     */
    public ArenaListener getArenaListener()
    {
        return this.arenalistener;
    }
    
    /**
     * Sets the arena listener.
     * 
     * @param al
     *            new custom arena listener.
     */
    public void setArenaListener(final ArenaListener al)
    {
        this.arenalistener = al;
    }
    
    /**
     * Returns the classes manager.
     * 
     * @return classes manager.
     */
    public Classes getClassesHandler()
    {
        return this.classes;
    }
    
    /**
     * Sets the classes manager.
     * 
     * @param c
     *            classes manager.
     */
    public void setClassesHandler(final Classes c)
    {
        this.classes = c;
    }
    
    /**
     * Returns the shop handler.
     * 
     * @return shop handler.
     */
    public Shop getShopHandler()
    {
        return this.shop;
    }
    
    /**
     * Returns the spectator manager.
     * 
     * @return spectator manager.
     */
    public SpectatorManager getSpectatorManager()
    {
        return this.spectatormanager;
    }
    
    /**
     * Sets the spectator manager.
     * 
     * @param s
     *            spectator manager.
     */
    public void setSpectatorManager(final SpectatorManager s)
    {
        this.spectatormanager = s;
    }
    
    /**
     * Returns the arena achievements.
     * 
     * @return arena achievments.
     */
    public ArenaAchievements getArenaAchievements()
    {
        return this.achievements;
    }
    
    /**
     * Returns the holograms handler.
     * 
     * @return holograms handler.
     */
    public Holograms getHologramsHandler()
    {
        return this.holograms;
    }
    
    /**
     * Returns the ingame cooldown
     * 
     * @return ingame cooldown
     */
    public int getIngameCountdown()
    {
        return this.ingame_countdown;
    }
    
    /**
     * Returns the lobby cooldown
     * 
     * @return lobby cooldown
     */
    public int getLobbyCountdown()
    {
        return this.lobby_countdown;
    }
    
    /**
     * Returns the arenas.
     * 
     * @return arenas
     * @deprecated will be changed in 1.5.0; returning list interface and returning a read-only copy
     */
    public ArrayList<Arena> getArenas()
    {
        return this.arenas;
    }
    
    /**
     * Clears the arena list
     */
    public void clearArenas()
    {
        this.arenas.clear();
    }
    
    /**
     * Adds a new arena
     * 
     * @param arena
     * @return new arena list
     * @deprecated will be changed in 1.5.0; returning list interface and returning a read-only copy
     */
    @Deprecated
    public ArrayList<Arena> addArena(final Arena arena)
    {
        this.arenas.add(arena);
        return this.getArenas();
    }
    
    /**
     * Returns arena by given name
     * 
     * @param arenaname
     *            name of the arena to be searched for
     * @return arena or {@code null} if the arena does not exist.
     */
    public Arena getArenaByName(final String arenaname)
    {
        for (final Arena a : this.getArenas())
        {
            if (a.getInternalName().equalsIgnoreCase(arenaname))
            {
                return a;
            }
        }
        return null;
    }
    
    /**
     * Removes arena by name.
     * 
     * @param arenaname
     *            name of the arena to be removed
     * @return removed arena or {@code null} if the arena does not exist.
     */
    public Arena removeArenaByName(final String arenaname)
    {
        Arena torem = null;
        for (final Arena a : this.getArenas())
        {
            if (a.getInternalName().equalsIgnoreCase(arenaname))
            {
                torem = a;
            }
        }
        if (torem != null)
        {
            this.removeArena(torem);
        }
        return null;
    }
    
    /**
     * Remove arena by instance.
     * 
     * @param arena
     *            arena to be removed
     * @return {@code true} if the arena was caontained in the list
     */
    public boolean removeArena(final Arena arena)
    {
        if (this.arenas.contains(arena))
        {
            this.arenas.remove(arena);
            return true;
        }
        return false;
    }
    
    /**
     * Adds given list of arenas
     * 
     * @param arenas
     *            arenas to add.
     * @deprecated will be removed in 1.5.0; replaced by setArenas and addArenas
     */
    @Deprecated
    public void addLoadedArenas(final ArrayList<Arena> arenas)
    {
        this.arenas = arenas;
    }
    
    /**
     * Adds given list of arenas
     * 
     * @param arenaList
     *            arenas to add.
     */
    public void addArenas(final Iterable<Arena> arenaList)
    {
        for (final Arena arena : arenaList)
        {
            this.arenas.add(arena);
        }
    }
    
    /**
     * Adds given list of arenas
     * 
     * @param arenaList
     *            arenas to add.
     */
    public void addArenas(final Arena... arenaList)
    {
        for (final Arena arena : arenaList)
        {
            this.arenas.add(arena);
        }
    }
    
    /**
     * Replaces the arenas with given list of arenas
     * 
     * @param arenaList
     *            arenas to set.
     */
    public void setArenas(final Iterable<Arena> arenaList)
    {
        this.clearArenas();
        this.addArenas(arenaList);
    }
    
    /**
     * Replaces the arenas with given list of arenas
     * 
     * @param arenaList
     *            arenas to set.
     */
    public void setArenas(final Arena... arenaList)
    {
        this.clearArenas();
        this.addArenas(arenaList);
    }
    
    /**
     * Returns the achievement gui flag.
     * 
     * @return achievement gui flag.
     */
    public boolean isAchievementGuiEnabled()
    {
        return this.achievement_gui_enabled;
    }
    
    /**
     * Sets the achievement gui flag
     * 
     * @param achievement_gui_enabled
     *            achievement gui flag.
     */
    public void setAchievementGuiEnabled(final boolean achievement_gui_enabled)
    {
        this.achievement_gui_enabled = achievement_gui_enabled;
    }
    
    /**
     * Reloads all existing arenas.
     */
    public void reloadAllArenas()
    {
        for (final Arena a : this.getArenas())
        {
            if (a != null)
            {
                final String arenaname = a.getInternalName();
                final ArenaSetup s = this.arenaSetup;
                a.init(Util.getSignLocationFromArena(this.plugin, arenaname), Util.getAllSpawns(this.plugin, arenaname), Util.getMainLobby(this.plugin),
                        Util.getComponentForArena(this.plugin, arenaname, "lobby"), s.getPlayerCount(this.plugin, arenaname, true), s.getPlayerCount(this.plugin, arenaname, false),
                        s.getArenaVIP(this.plugin, arenaname));
                if (a.isSuccessfullyInit())
                {
                    Util.updateSign(this.plugin, a);
                }
            }
        }
    }
    
    /**
     * Reload arena by name
     * 
     * @param arenaname
     *            arena to be reloaded
     */
    public void reloadArena(final String arenaname)
    {
        if (Validator.isArenaValid(this.plugin, arenaname))
        {
            final Arena a = this.getArenaByName(arenaname);
            if (a != null)
            {
                final ArenaSetup s = this.arenaSetup;
                a.init(Util.getSignLocationFromArena(this.plugin, arenaname), Util.getAllSpawns(this.plugin, arenaname), Util.getMainLobby(this.plugin),
                        Util.getComponentForArena(this.plugin, arenaname, "lobby"), s.getPlayerCount(this.plugin, arenaname, true), s.getPlayerCount(this.plugin, arenaname, false),
                        s.getArenaVIP(this.plugin, arenaname));
            }
        }
    }
    
    /**
     * Checks if the player is contained in given arena.
     * 
     * @param playername
     *            player name.
     * @return {@code true} if the player is contained in arena.
     */
    public boolean containsGlobalPlayer(final String playername)
    {
        return this.global_players.containsKey(playername);
    }
    
    /**
     * Checks if the player already lost.
     * 
     * @param playername
     *            player name
     * @return {@code true} if the player already lost.
     */
    public boolean containsGlobalLost(final String playername)
    {
        return this.global_lost.containsKey(playername);
    }
    
    /**
     * Returns the arena by player name.
     * 
     * @param playername
     *            player to be searched for.
     * @return arena or {@code null} if the player is not present in any arena.
     */
    public Arena getArenaByGlobalPlayer(final String playername)
    {
        if (this.containsGlobalPlayer(playername))
        {
            return this.global_players.get(playername);
        }
        else
        {
            return null;
        }
    }
    
}
