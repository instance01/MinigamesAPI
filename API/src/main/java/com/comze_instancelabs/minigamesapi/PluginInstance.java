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

public class PluginInstance
{
    
    public HashMap<String, Arena>               global_players                        = new HashMap<>();
    public HashMap<String, Arena>               global_lost                           = new HashMap<>();
    public HashMap<String, Arena>               global_arcade_spectator               = new HashMap<>();
    
    private ArenaListener                       arenalistener                         = null;
    private ArenasConfig                        arenasconfig                          = null;
    private ClassesConfig                       classesconfig                         = null;
    private MessagesConfig                      messagesconfig                        = null;
    private StatsConfig                         statsconfig                           = null;
    private GunsConfig                          gunsconfig                            = null;
    private AchievementsConfig                  achievementsconfig                    = null;
    private ShopConfig                          shopconfig                            = null;
    private HologramsConfig                     hologramsconfig                       = null;
    private JavaPlugin                          plugin                                = null;
    private ArrayList<Arena>                    arenas                                = new ArrayList<>();
    private final HashMap<String, AClass>       pclass                                = new HashMap<>();
    private final LinkedHashMap<String, AClass> aclasses                              = new LinkedHashMap<>();
    private final HashMap<String, Gun>          guns                                  = new HashMap<>();
    private Rewards                             rew                                   = null;
    private MainSQL                             sql                                   = null;
    private Stats                               stats                                 = null;
    private Classes                             classes                               = null;
    private Shop                                shop                                  = null;
    private SpectatorManager                    spectatormanager                      = null;
    private ArenaAchievements                   achievements                          = null;
    private Holograms                           holograms                             = null;
    private boolean                             achievement_gui_enabled               = false;
    
    public ArenaScoreboard                      scoreboardManager;
    public ArenaLobbyScoreboard                 scoreboardLobbyManager;
    public ArenaSetup                           arenaSetup                            = new ArenaSetup();
    
    int                                         lobby_countdown                       = 30;
    int                                         ingame_countdown                      = 10;
    
    boolean                                     spectator_move_y_lock                 = true;
    boolean                                     use_xp_bar_level                      = true;
    boolean                                     blood_effects                         = true;
    boolean                                     dead_in_fake_bed_effects              = true;
    boolean                                     spectator_mode_1_8                    = true;
    boolean                                     damage_identifier_effects             = true;
    public boolean                              color_background_wool_of_signs;
    boolean                                     last_man_standing                     = true;
    boolean                                     old_reset                             = false;
    public boolean                              show_classes_without_usage_permission = true;
    public boolean                              chat_enabled                          = true;
    
    public HashMap<String, ArrayList<String>>   cached_sign_states                    = new HashMap<>();
    
    public PluginInstance(final JavaPlugin plugin, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig,
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
        this.sql = new MainSQL(plugin, true);
        this.classes = new Classes(this, plugin);
        this.shop = new Shop(this, plugin);
        this.spectatormanager = new SpectatorManager(plugin);
        this.achievements = new ArenaAchievements(this, plugin);
        this.holograms = new Holograms(this);
        this.scoreboardManager = new ArenaScoreboard(this, plugin);
        this.scoreboardLobbyManager = new ArenaLobbyScoreboard(this, plugin);
        this.reloadVariables();
    }
    
    public PluginInstance(final JavaPlugin plugin, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig)
    {
        this(plugin, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>());
    }
    
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
        
    }
    
    public JavaPlugin getPlugin()
    {
        return this.plugin;
    }
    
    public HashMap<String, AClass> getAClasses()
    {
        return this.aclasses;
    }
    
    public HashMap<String, AClass> getPClasses()
    {
        return this.pclass;
    }
    
    public void addAClass(final String name, final AClass a)
    {
        this.aclasses.put(name, a);
    }
    
    public void setPClass(final String player, final AClass a)
    {
        this.pclass.put(player, a);
    }
    
    public HashMap<String, Gun> getAllGuns()
    {
        return this.guns;
    }
    
    public void addGun(final String name, final Gun g)
    {
        this.guns.put(name, g);
    }
    
    public ArenasConfig getArenasConfig()
    {
        return this.arenasconfig;
    }
    
    public MessagesConfig getMessagesConfig()
    {
        return this.messagesconfig;
    }
    
    public ClassesConfig getClassesConfig()
    {
        return this.classesconfig;
    }
    
    public StatsConfig getStatsConfig()
    {
        return this.statsconfig;
    }
    
    public GunsConfig getGunsConfig()
    {
        return this.gunsconfig;
    }
    
    public AchievementsConfig getAchievementsConfig()
    {
        return this.achievementsconfig;
    }
    
    public ShopConfig getShopConfig()
    {
        return this.shopconfig;
    }
    
    public void setShopConfig(final ShopConfig shopconfig)
    {
        this.shopconfig = shopconfig;
    }
    
    public HologramsConfig getHologramsConfig()
    {
        return this.hologramsconfig;
    }
    
    public Rewards getRewardsInstance()
    {
        return this.rew;
    }
    
    public void setRewardsInstance(final Rewards r)
    {
        this.rew = r;
    }
    
    public MainSQL getSQLInstance()
    {
        return this.sql;
    }
    
    public Stats getStatsInstance()
    {
        return this.stats;
    }
    
    public ArenaListener getArenaListener()
    {
        return this.arenalistener;
    }
    
    public void setArenaListener(final ArenaListener al)
    {
        this.arenalistener = al;
    }
    
    public Classes getClassesHandler()
    {
        return this.classes;
    }
    
    public void setClassesHandler(final Classes c)
    {
        this.classes = c;
    }
    
    public Shop getShopHandler()
    {
        return this.shop;
    }
    
    public SpectatorManager getSpectatorManager()
    {
        return this.spectatormanager;
    }
    
    public void setSpectatorManager(final SpectatorManager s)
    {
        this.spectatormanager = s;
    }
    
    public ArenaAchievements getArenaAchievements()
    {
        return this.achievements;
    }
    
    public Holograms getHologramsHandler()
    {
        return this.holograms;
    }
    
    public int getIngameCountdown()
    {
        return this.ingame_countdown;
    }
    
    public int getLobbyCountdown()
    {
        return this.lobby_countdown;
    }
    
    public ArrayList<Arena> getArenas()
    {
        return this.arenas;
    }
    
    public void clearArenas()
    {
        this.arenas.clear();
    }
    
    public ArrayList<Arena> addArena(final Arena arena)
    {
        this.arenas.add(arena);
        return this.getArenas();
    }
    
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
    
    public boolean removeArena(final Arena arena)
    {
        if (this.arenas.contains(arena))
        {
            this.arenas.remove(arena);
            return true;
        }
        return false;
    }
    
    public void addLoadedArenas(final ArrayList<Arena> arenas)
    {
        this.arenas = arenas;
    }
    
    public boolean isAchievementGuiEnabled()
    {
        return this.achievement_gui_enabled;
    }
    
    public void setAchievementGuiEnabled(final boolean achievement_gui_enabled)
    {
        this.achievement_gui_enabled = achievement_gui_enabled;
    }
    
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
    
    public boolean containsGlobalPlayer(final String playername)
    {
        return this.global_players.containsKey(playername);
    }
    
    public boolean containsGlobalLost(final String playername)
    {
        return this.global_lost.containsKey(playername);
    }
    
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
