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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesapi.arcade.ArcadeInstance;
import com.comze_instancelabs.minigamesapi.events.ArenaStartEvent;
import com.comze_instancelabs.minigamesapi.events.ArenaStartedEvent;
import com.comze_instancelabs.minigamesapi.events.ArenaStopEvent;
import com.comze_instancelabs.minigamesapi.events.PlayerJoinLobbyEvent;
import com.comze_instancelabs.minigamesapi.events.PlayerLeaveArenaEvent;
import com.comze_instancelabs.minigamesapi.util.BungeeUtil;
import com.comze_instancelabs.minigamesapi.util.Cuboid;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * This is the base class for arenas.
 *
 * <p>
 * An arena is a place the players fight against and where all the action takes place.
 * </p>
 *
 * <p>
 * Minigames will have to override this class.
 * </p>
 * 
 * <p>
 * NOTICE: There are some race conditions on high load. For example joining VIPs while state switches etc. Spigot itself may invoke the tasks serial meaning one after another but there can be external
 * events from network at the moment (MinigamesAPI-Bungee).
 * </p>
 * 
 * @author instancelabs
 */
public class Arena
{
    
    /**
     * Plugin the arena belongs to.
     */
    private final JavaPlugin              plugin;
    
    /**
     * Minigames lib representation of the minigame plugin.
     */
    private final PluginInstance          pli;
    
    /**
     * The reference to the arcade instance if this arena is part of arcade mode; {@code null} for classic arena.
     * 
     * <p>
     * TODO: Review if we really need a back reference to the arcade instance.
     * </p>
     */
    private ArcadeInstance                ai;
    
    /**
     * {@code true} if this is the main (= first arena) for arcade.
     */
    private boolean                       isArcadeMain              = false;
    
    /**
     * {@code true} {@link #init(Location, ArrayList, Location, Location, int, int, boolean)} was called.
     */
    private boolean                       isSuccessfullyInitialized = false;
    
    /**
     * List of configured spawn locations.
     */
    private final ArrayList<Location>     spawns                    = new ArrayList<>();
    
    /**
     * The player spawn locations.
     */
    private HashMap<String, Location>     pspawnloc                 = new HashMap<>();
    
    /**
     * Match statistics: The last damager of players; helps fetching the player that kills another one.
     */
    private final HashMap<String, String> lastdamager               = new HashMap<>();
    
    /**
     * Match statistics: Kill count of players.
     */
    HashMap<String, Integer>              temp_kill_count           = new HashMap<>();
    
    /**
     * Match statistics: Death count of players.
     */
    HashMap<String, Integer>              temp_death_count          = new HashMap<>();
    
    /**
     * Location of the main lobby.
     */
    private Location                      mainlobby;
    
    /**
     * Location of the waiting lobby.
     */
    private Location                      waitinglobby;
    
    /**
     * Spawn location for spectators.
     */
    private Location                      specspawn;
    
    /**
     * The join sign location.
     */
    private Location                      signloc;
    
    /**
     * Max amount of players for the game.
     */
    private int                           max_players;
    
    /**
     * The minimum amount of players for starting the game.
     */
    private int                           min_players;
    
    /**
     * {@code true} if this is a vip arena.
     * 
     * <p>
     * Players can only join vip arenas if they have permission "arenas.$arenaname$".
     * </p>
     */
    private boolean                       viparena;
    
    /**
     * The players joined the arena and playing.
     */
    private final ArrayList<String>       players                   = new ArrayList<>();
    
    private ArrayList<String>             temp_players              = new ArrayList<>();
    
    /**
     * The arena type.
     */
    private ArenaType                     type                      = ArenaType.DEFAULT;
    
    /**
     * The current arena state.
     */
    private ArenaState                    currentstate              = ArenaState.JOIN;
    
    /**
     * Internal/ technical name of the arena.
     */
    private String                        name                      = "mainarena";
    
    /**
     * The human readable arena name (public name).
     */
    private String                        displayname               = "mainarena";
    
    private boolean                       started                   = false;
    private boolean                       startedIngameCountdown    = false;
    
    /**
     * {@code true} if the arenaa score board will be shown.
     */
    private boolean                       showArenascoreboard       = true;
    
    /**
     * {@code true} if the pvp is allowed in every game state.
     */
    private boolean                       alwaysPvP                 = false;
    
    private SmartReset                    sr                        = null;
    
    /**
     * The arena boundaries.
     */
    private Cuboid                        boundaries;
    
    /**
     * The lobby boundaries.
     */
    private Cuboid                        lobby_boundaries;
    
    /**
     * The spectator boundaries.
     */
    private Cuboid                        spec_boundaries;
    
    private boolean                       temp_countdown            = true;
    boolean                               skip_join_lobby           = false;
    
    private int                           currentspawn              = 0;
    
    int                                   global_coin_multiplier    = 1;
    
    private BukkitTask                    maximum_game_time;
    
    ArrayList<ItemStack>                  global_drops              = new ArrayList<>();
    
    private int                           currentlobbycount         = 10;
    private int                           currentingamecount        = 10;
    
    /**
     * the current task id.
     * 
     * @deprecated remove in 1.5.0; current no one ever sets this, review the trials to cancel tasks to find out what is going on here; possibly a bug.
     */
    @Deprecated
    private int                           currenttaskid             = 0;
    
    boolean                               temp_delay_stopped        = false;
    
    /** the arenam logger. */
    protected ArenaLogger                 logger;
    
    /**
     * Creates a normal singlespawn arena
     * 
     * @param plugin
     *            JavaPlugin the arena belongs to
     * @param name
     *            name of the arena
     */
    public Arena(final JavaPlugin plugin, final String name)
    {
        this.plugin = plugin;
        this.name = name;
        this.sr = new SmartReset(this);
        this.pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        this.logger = new ArenaLogger(this.plugin.getLogger(), this.name);
    }
    
    /**
     * Creates an arena of given arenatype
     * 
     * @param plugin
     *            JavaPlugin the arena belongs to
     * @param name
     *            name of the arena
     * @param type
     *            arena type
     */
    public Arena(final JavaPlugin plugin, final String name, final ArenaType type)
    {
        this(plugin, name);
        this.type = type;
    }
    
    /**
     * This is for loading existing arenas
     * 
     * @param signloc
     *            (optional) Location of join sign; {@link Util#getSignLocationFromArena}
     * @param spawns
     *            (optional) The spawn points; {@link Util#getAllSpawns}
     * @param mainlobby
     *            The main lobby location; {@link Util#getMainLobby}
     * @param waitinglobby
     *            The waiting lobby location; F.e. {@code Util#getComponentForArena} with argument "lobby".
     * @param max_players
     *            The max players count; {@link ArenaSetup#getPlayerCount}
     * @param min_players
     *            The min players count; {@link ArenaSetup#getPlayerCount}
     * @param viparena
     *            {@code true} for vip arena; players will need a permission in vip arenas.
     */
    @SuppressWarnings("hiding")
    public void init(final Location signloc, final ArrayList<Location> spawns, final Location mainlobby, final Location waitinglobby, final int max_players, final int min_players,
            final boolean viparena)
    {
        // TODO Disallow duplicate init; reloading should create a new arena instance.
        this.signloc = signloc;
        this.spawns.clear();
        if (spawns != null)
        {
            this.spawns.addAll(spawns);
        }
        this.mainlobby = mainlobby;
        this.waitinglobby = waitinglobby;
        this.viparena = viparena;
        this.min_players = min_players;
        this.max_players = max_players;
        this.showArenascoreboard = this.pli.arenaSetup.getShowScoreboard(this.plugin, this.getInternalName());
        this.isSuccessfullyInitialized = true;
        if (Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.BOUNDS_LOW)
                && Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.BOUNDS_HIGH))
        {
            try
            {
                final Location low_boundary = Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.BOUNDS_LOW);
                final Location high_boundary = Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.BOUNDS_HIGH);
                if (low_boundary != null && high_boundary != null)
                {
                    this.boundaries = new Cuboid(low_boundary, high_boundary);
                }
                else
                {
                    this.plugin.getServer().getConsoleSender().sendMessage(String.format(Messages.getString("Arena.BoundariesInvalid", MinigamesAPI.LOCALE), this.getInternalName())); //$NON-NLS-1$
                    this.isSuccessfullyInitialized = false;
                }
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Problems checking arena boundaries", e); //$NON-NLS-1$
                this.plugin.getServer().getConsoleSender().sendMessage(String.format(Messages.getString("Arena.SaveFailedBoundaries", MinigamesAPI.LOCALE), e.getMessage())); //$NON-NLS-1$
                this.isSuccessfullyInitialized = false;
            }
        }
        if (Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.LOBBY_BOUNDS_LOW)
                && Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.LOBBY_BOUNDS_HIGH))
        {
            try
            {
                this.lobby_boundaries = new Cuboid(Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.LOBBY_BOUNDS_LOW),
                        Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.LOBBY_BOUNDS_HIGH));
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Problems checking lobby boundaries", e); //$NON-NLS-1$
                this.isSuccessfullyInitialized = false;
            }
        }
        if (Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.SPEC_BOUNDS_LOW)
                && Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.SPEC_BOUNDS_HIGH))
        {
            try
            {
                this.spec_boundaries = new Cuboid(Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.SPEC_BOUNDS_LOW),
                        Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.SPEC_BOUNDS_HIGH));
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Problems checking spectator boundaries", e); //$NON-NLS-1$
                this.isSuccessfullyInitialized = false;
            }
        }
        
        if (Util.isComponentForArenaValid(this.plugin, this.getInternalName(), ArenaConfigStrings.SPEC_SPAWN))
        {
            this.specspawn = Util.getComponentForArena(this.plugin, this.getInternalName(), ArenaConfigStrings.SPEC_SPAWN);
        }
        
        final String path = ArenaConfigStrings.ARENAS_PREFIX + this.name + ArenaConfigStrings.DISPLAYNAME_SUFFIX;
        if (this.pli.getArenasConfig().getConfig().isSet(path))
        {
            this.displayname = ChatColor.translateAlternateColorCodes('&',
                    this.pli.getArenasConfig().getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + this.name + ArenaConfigStrings.DISPLAYNAME_SUFFIX));
        }
        else
        {
            this.pli.getArenasConfig().getConfig().set(path, this.name);
            this.pli.getArenasConfig().saveConfig();
            this.displayname = this.name;
        }
        
    }
    
    /**
     * This is for loading existing arenas
     * 
     * @param signloc
     * @param spawn
     * @param mainlobby
     * @param waitinglobby
     * @param max_players
     * @param min_players
     * @param viparena
     * @return this object
     * @deprecated use {@link #init(Location, ArrayList, Location, Location, int, int, boolean)} instead; will be removed in 1.15.X
     */
    @SuppressWarnings("hiding")
    @Deprecated
    public Arena initArena(final Location signloc, final ArrayList<Location> spawn, final Location mainlobby, final Location waitinglobby, final int max_players, final int min_players,
            final boolean viparena)
    {
        this.init(signloc, spawn, mainlobby, waitinglobby, max_players, min_players, viparena);
        return this;
    }
    
    /**
     * Returns this object
     * 
     * @return this object
     * @deprecated will be removed in 1.15.X
     */
    @Deprecated
    public Arena getArena()
    {
        return this;
    }
    
    /**
     * Returns the smart reset object for resetting blocks that changed during game play.
     * 
     * @return smart reset helper.
     */
    public SmartReset getSmartReset()
    {
        return this.sr;
    }
    
    /**
     * Returns the show scoreboard flag, read from arena setup.
     * 
     * <p>
     * If set to false the score boards are not shown/ updated.
     * </p>
     * 
     * @return {@code true} to show the score oards.
     */
    public boolean getShowScoreboard()
    {
        return this.showArenascoreboard;
    }
    
    /**
     * Returns the always pvp flag that controls the pvp behaviour.
     * 
     * <p>
     * TODO describe what the flag actually does in ArenaListener
     * </p>
     * 
     * @return {@code true} for allowing pvp in any game state.
     */
    public boolean getAlwaysPvP()
    {
        return this.alwaysPvP;
    }
    
    /**
     * Sets the always pvp flag; see {@link #getAlwaysPvP()} for details.
     * 
     * @param t
     *            new value
     */
    public void setAlwaysPvP(final boolean t)
    {
        this.alwaysPvP = t;
    }
    
    /**
     * Returns the join sign location.
     * 
     * @return join sign location; {@code null} if there is no join sign.
     */
    public Location getSignLocation()
    {
        return this.signloc;
    }
    
    /**
     * Sets the join sign location.
     * 
     * @param l
     *            new location.
     */
    public void setSignLocation(final Location l)
    {
        this.signloc = l;
    }
    
    /**
     * Returns the spawns of this arena.
     * 
     * @return spawns.
     * 
     * @deprecated The method signature will change in 1.15.0; will return the List interface instead of ArrayList and will return an immutable list so that spawns cannot be changed from outside.
     */
    @Deprecated
    public ArrayList<Location> getSpawns()
    {
        return this.spawns;
    }
    
    /**
     * Returns the arena boundaries.
     * 
     * @return arena boundaries; may return {@code null} for invalid arena setups.
     */
    public Cuboid getBoundaries()
    {
        return this.boundaries;
    }
    
    /**
     * Returns the lobby boundaries.
     * 
     * @return lobby boundaries; may return {@code null} if no arena lobby exists.
     */
    public Cuboid getLobbyBoundaries()
    {
        return this.lobby_boundaries;
    }
    
    /**
     * Returns the spectator boundaries.
     * 
     * @return spec boundaries; may return {@code null} if spectating is not configured.
     */
    public Cuboid getSpecBoundaries()
    {
        return this.spec_boundaries;
    }
    
    /**
     * Returns the technical/ internal arena name.
     * 
     * @return technical arena name.
     */
    public String getInternalName()
    {
        return this.name;
    }
    
    /**
     * Returns the human readable arena name.
     * 
     * @return arena name used by signs and messages.
     */
    public String getDisplayName()
    {
        return this.displayname;
    }
    
    /**
     * Please use getInternalName() for the internal name and getDisplayName() for the optional displayname
     * 
     * @return Internal name of arena (same as getInternalName())
     * @deprecated replaced by {@link #getInternalName()} or {@link #getDisplayName()}
     */
    @Deprecated
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Returns the maximum players playing a match.
     * 
     * @return maximum players.
     */
    public int getMaxPlayers()
    {
        return this.max_players;
    }
    
    /**
     * Returns the minimum players.
     * 
     * @return minimum players.
     */
    public int getMinPlayers()
    {
        return this.min_players;
    }
    
    /**
     * Sets the minimum players.
     * 
     * <p>
     * Changes are only respected in a new match, cannot influence an already running match.
     * </p>
     * 
     * @param i
     *            new minimum players.
     */
    public void setMinPlayers(final int i)
    {
        this.min_players = i;
    }
    
    /**
     * Sets the maximum players.
     * 
     * <p>
     * Changes are only respected in a new match, cannot influence an already running match.
     * </p>
     * 
     * @param i
     *            new maximum players.
     */
    public void setMaxPlayers(final int i)
    {
        this.max_players = i;
    }
    
    /**
     * Checks if this is a vip arena.
     * 
     * @return {@code true} if the players need a permission to play within this arena.
     */
    public boolean isVIPArena()
    {
        return this.viparena;
    }
    
    /**
     * Sets the vip arena flag.
     * 
     * <p>
     * Changes are only respected in a new match/joins, cannot influence players already being in the waiting lobby.
     * </p>
     * 
     * @param t
     *            {@code true} to make this a vip arena.
     */
    public void setVIPArena(final boolean t)
    {
        this.viparena = t;
    }
    
    /**
     * Returns the players that joined the arena.
     * 
     * @return active players within this arena.
     * 
     * @deprecated The method signature will change in 1.15.0; will return the List interface instead of ArrayList and will return an immutable list so that spawns cannot be changed from outside.
     */
    @Deprecated
    public ArrayList<String> getAllPlayers()
    {
        return this.players;
    }
    
    /**
     * Checks if a player already joined the arena.
     * 
     * @param playername
     *            Name of the player to be searched.
     * @return {@code true} if the player already joined the arena.
     */
    public boolean containsPlayer(final String playername)
    {
        // potential performance lack for huge arenas or massive invocation count; hash set can be better.
        // at the moment we decided to keep up the ordering the players joined the arena.
        return this.players.contains(playername);
    }
    
    /**
     * Please do not use this function to add players
     * 
     * @param playername
     *            name of the player
     * @return true on success
     * 
     * @deprecated will be removed in 1.15.0; replaces by {@link #spectateGame(String)} or {@link #joinPlayerLobby(String)}
     */
    @Deprecated
    public boolean addPlayer(final String playername)
    {
        return this.players.add(playername);
    }
    
    /**
     * Please do not use this function to remove players
     * 
     * @param playername
     *            name of the player
     * @return true on success
     * 
     * @deprecated will be removed in 1.15.0; replaces by {@link #leavePlayer(String, boolean, boolean)}
     */
    @Deprecated
    public boolean removePlayer(final String playername)
    {
        return this.players.remove(playername);
    }
    
    /**
     * Returns the current state of arena.
     * 
     * @return current arena state.
     */
    public ArenaState getArenaState()
    {
        return this.currentstate;
    }
    
    /**
     * Sets the current arena state.
     * 
     * @param s
     *            new arena state.
     */
    void setArenaState(final ArenaState s)
    {
        this.currentstate = s;
    }
    
    /**
     * Returns the arena type.
     * 
     * @return type of this arena.
     */
    public ArenaType getArenaType()
    {
        return this.type;
    }
    
    /**
     * Joins the waiting lobby of an arena
     * 
     * @param playername
     *            the playername
     * @deprecated will be removed in 1.5.0; string names replaced by UUID
     */
    @Deprecated
    public void joinPlayerLobby(final String playername)
    {
        this.joinPlayerLobby(MinigamesAPI.playerToUUID(playername));
    }
    
    /**
     * Joins the waiting lobby of an arena
     * 
     * @param playerUuid
     *            the players uuid
     */
    public void joinPlayerLobby(final UUID playerUuid)
    {
        if (this.getArenaState() != ArenaState.JOIN && this.getArenaState() != ArenaState.STARTING)
        {
            // arena ingame or restarting
            return;
        }
        final Player player = MinigamesAPI.uuidToPlayer(playerUuid);
        if (player == null)
            return;
        
        final String playername = player.getName();
        if (!this.pli.arenaSetup.getArenaEnabled(this.plugin, this.getInternalName()))
        {
            Util.sendMessage(this.plugin, player, this.pli.getMessagesConfig().arena_disabled);
            return;
        }
        if (this.pli.containsGlobalPlayer(playername))
        {
            Util.sendMessage(this.plugin, player, this.pli.getMessagesConfig().already_in_arena);
            return;
        }
        if (this.ai == null && this.isVIPArena())
        {
            if (Validator.isPlayerOnline(playername))
            {
                if (!player.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix(this.plugin.getName()) + ArenaPermissionStrings.PREFIX + this.getInternalName() + ArenaPermissionStrings.VIP))
                {
                    Util.sendMessage(this.plugin, player, this.pli.getMessagesConfig().no_perm_to_join_arena.replaceAll(ArenaMessageStrings.ARENA, this.getInternalName()));
                    return;
                }
            }
        }
        if (this.ai == null && this.getAllPlayers().size() > this.max_players - 1)
        {
            // arena full
            
            // if player vip -> kick someone and continue
            this.logger.fine(playername + " is vip: " + player.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix(this.plugin.getName()) + ArenaPermissionStrings.PREFIX + this.getInternalName() + ArenaPermissionStrings.VIP)); //$NON-NLS-1$
            if (!player.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix(this.plugin.getName()) + ArenaPermissionStrings.PREFIX + this.getInternalName() + ArenaPermissionStrings.VIP))
            {
                // no VIP.
                return;
            }
            
            // player has vip
            boolean noone_found = true;
            final ArrayList<String> temp = new ArrayList<>(this.getAllPlayers());
            for (final String p_ : temp)
            {
                if (Validator.isPlayerOnline(p_))
                {
                    final Player player_ = Bukkit.getPlayer(p_);
                    if (!player_.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix(this.plugin.getName()) + ArenaPermissionStrings.PREFIX + this.getInternalName() + ArenaPermissionStrings.VIP))
                    {
                        this.leavePlayer(p_, false, true);
                        player_.sendMessage(this.pli.getMessagesConfig().you_got_kicked_because_vip_joined);
                        noone_found = false;
                        break;
                    }
                }
            }
            if (noone_found)
            {
                // apparently everyone is vip, can't join
                return;
            }
        }
        
        if (MinigamesAPI.getAPI().global_party.containsKey(playername))
        {
            final Party party = MinigamesAPI.getAPI().global_party.get(playername);
            final int playersize = party.getPlayers().size() + 1;
            if (this.getAllPlayers().size() + playersize > this.max_players)
            {
                player.sendMessage(MinigamesAPI.getAPI().partymessages.party_too_big_to_join);
                return;
            }
            
            for (final String p_ : party.getPlayers())
            {
                if (Validator.isPlayerOnline(p_))
                {
                    boolean cont = true;
                    MinigamesAPI.getAPI();
                    for (final PluginInstance pli_ : MinigamesAPI.pinstances.values())
                    {
                        // if (!pli_.getPlugin().getName().equalsIgnoreCase("MGArcade") && pli_.global_players.containsKey(p_)) {
                        if (pli_.containsGlobalPlayer(p_))
                        {
                            cont = false;
                        }
                    }
                    if (cont)
                    {
                        this.joinPlayerLobby(p_);
                    }
                }
            }
        }
        
        if (this.getAllPlayers().size() == this.max_players - 1)
        {
            if (this.currentlobbycount > 16 && this.getArenaState() == ArenaState.STARTING)
            {
                this.currentlobbycount = 16;
            }
        }
        this.pli.global_players.put(playername, this);
        this.players.add(playername);
        
        if (Validator.isPlayerValid(this.plugin, playername, this))
        {
            final Player p = player;
            final ArenaPlayer ap = ArenaPlayer.getPlayerInstance(playername);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinLobbyEvent(p, this.plugin, this));
            Util.sendMessage(this.plugin, p, this.pli.getMessagesConfig().you_joined_arena.replaceAll(ArenaMessageStrings.ARENA, this.getDisplayName()));
            Util.sendMessage(this.plugin, p, this.pli.getMessagesConfig().minigame_description);
            if (this.pli.getArenasConfig().getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + this.getInternalName() + ArenaConfigStrings.AUTHOR_SUFFIX))
            {
                Util.sendMessage(this.plugin, p, this.pli.getMessagesConfig().author_of_the_map.replaceAll(ArenaMessageStrings.ARENA, this.getDisplayName()).replaceAll(ArenaMessageStrings.AUTHOR,
                        this.pli.getArenasConfig().getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + this.getInternalName() + ArenaConfigStrings.AUTHOR_SUFFIX)));
            }
            if (this.pli.getArenasConfig().getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + this.getInternalName() + ArenaConfigStrings.DESCRIPTION_SUFFIX))
            {
                Util.sendMessage(this.plugin, p,
                        this.pli.getMessagesConfig().description_of_the_map.replaceAll(ArenaMessageStrings.ARENA, this.getDisplayName()).replaceAll(ArenaMessageStrings.DESCRIPTION,
                                this.pli.getArenasConfig().getConfig().getString(ArenaConfigStrings.ARENAS_PREFIX + this.getInternalName() + ArenaConfigStrings.DESCRIPTION_SUFFIX)));
            }
            
            Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> {
                try
                {
                    Arena.this.pli.getHologramsHandler().sendAllHolograms(p);
                }
                catch (final Exception e)
                {
                    this.logger.log(Level.WARNING, "Failed playing hologram: ", e); //$NON-NLS-1$
                }
            }, 15L);
            
            for (final String p_ : this.getAllPlayers())
            {
                if (Validator.isPlayerOnline(p_) && !p_.equalsIgnoreCase(p.getName()))
                {
                    final Player p__ = Bukkit.getPlayer(p_);
                    final int count = this.getAllPlayers().size();
                    final int maxcount = this.getMaxPlayers();
                    Util.sendMessage(this.plugin, p__, this.pli.getMessagesConfig().broadcast_player_joined.replaceAll(ArenaMessageStrings.PLAYER, p.getName())
                            .replace(ArenaMessageStrings.COUNT, Integer.toString(count)).replace(ArenaMessageStrings.MAXCOUNT, Integer.toString(maxcount)));
                }
            }
            Util.updateSign(this.plugin, this);
            
            if (this.ai == null && !this.isArcadeMain())
            {
                this.skip_join_lobby = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SKIP_LOBBY);
            }
            
            final Arena a = this;
            ap.setInventories(p.getInventory().getContents(), p.getInventory().getArmorContents());
            if (this.getArenaType() == ArenaType.JUMPNRUN)
            {
                // jump & run
                Util.teleportPlayerFixed(p, this.spawns.get(this.currentspawn));
                if (this.currentspawn < this.spawns.size() - 1)
                {
                    this.currentspawn++;
                }
                Util.clearInv(p);
                ap.setOriginalGamemode(p.getGameMode());
                ap.setOriginalXplvl(p.getLevel());
                p.setGameMode(GameMode.SURVIVAL);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 50));
                return;
            }
            
            // no jump & run
            if (this.startedIngameCountdown)
            {
                // already in count down
                this.pli.scoreboardLobbyManager.removeScoreboard(this.getInternalName(), p);
                Util.teleportAllPlayers(this.getAllPlayers(), this.spawns);
                p.setFoodLevel(5);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -7)); // -5
                Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> p.setWalkSpeed(0.0F), 1L);
                Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> Util.clearInv(p), 15L);
                ap.setOriginalXplvl(p.getLevel());
                Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
                    if (a.getArenaState() != ArenaState.INGAME)
                    {
                        Util.giveLobbyItems(Arena.this.plugin, p);
                    }
                    ap.setOriginalGamemode(p.getGameMode());
                    p.setGameMode(GameMode.SURVIVAL);
                }, 20L);
                this.pli.scoreboardManager.updateScoreboard(this.plugin, this);
                return;
            }
            
            // no count down
            this.pli.scoreboardLobbyManager.updateScoreboard(this.plugin, this);
            if (!this.skip_join_lobby)
            {
                Util.teleportPlayerFixed(p, this.waitinglobby);
                Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 50)), 3L);
            }
            else
            {
                Util.teleportAllPlayers(this.getAllPlayers(), this.spawns);
            }
            
            Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> Util.clearInv(p), 10L);
            ap.setOriginalXplvl(p.getLevel());
            Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
                if (a.getArenaState() != ArenaState.INGAME)
                {
                    Util.giveLobbyItems(Arena.this.plugin, p);
                }
                ap.setOriginalGamemode(p.getGameMode());
                p.setGameMode(GameMode.SURVIVAL);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 50));
            }, 15L);
            if (!this.skip_join_lobby)
            {
                if (this.ai == null && this.getAllPlayers().size() > this.min_players - 1)
                {
                    this.startLobby(this.temp_countdown);
                }
                else if (this.ai != null)
                {
                    this.startLobby(this.temp_countdown);
                }
            }
            else
            {
                if (this.ai == null && !this.isArcadeMain() && this.getAllPlayers().size() > this.min_players - 1)
                {
                    this.startLobby(false);
                }
            }
        }
    }
    
    /**
     * Primarily used for ArcadeInstance to join a waiting lobby without countdown
     * 
     * @param playername
     *            name of the player
     * @param countdown
     *            count down in seconds.
     */
    public void joinPlayerLobby(final String playername, final boolean countdown)
    {
        this.temp_countdown = countdown;
        this.joinPlayerLobby(playername);
    }
    
    /**
     * Joins the waiting lobby of an arena using given arcade instance
     * 
     * @param playername
     *            the player name
     * @param arcade
     *            the ArcadeInstance
     * @param countdown
     *            count down in seconds.
     * @param skip_lobby
     *            {@code true} to directly get to the spawn by skipping the lobby.
     */
    public void joinPlayerLobby(final String playername, final ArcadeInstance arcade, final boolean countdown, final boolean skip_lobby)
    {
        this.skip_join_lobby = skip_lobby;
        this.ai = arcade;
        this.joinPlayerLobby(playername, countdown); // join playerlobby without lobby countdown
    }
    
    /**
     * Leaves the current arena, won't do anything if not present in any arena
     * 
     * @param playername
     * @param fullLeave
     *            Determines if player left only minigame or the server
     * @deprecated replaced by {@code leavePlayer(playername, fullLeave, false)}; removed in 1.15.0
     */
    @Deprecated
    public void leavePlayer(final String playername, final boolean fullLeave)
    {
        this.leavePlayerRaw(playername, fullLeave);
    }
    
    /**
     * Leaves the current arena, won't do anything if not present in any arena
     * 
     * @param playername
     *            name of the player that left the arena
     * @param fullLeave
     *            Determines if player left only minigame ({@code false}) or the server ({@code true})
     * @param endofGame
     *            {@code true} if the game leave was caused by the end of the game.
     */
    public void leavePlayer(final String playername, final boolean fullLeave, final boolean endofGame)
    {
        if (!endofGame)
        {
            final ArenaPlayer ap = ArenaPlayer.getPlayerInstance(playername);
            ap.setNoReward(true);
        }
        
        // replace with leavePlayerRaw as soon as the method was removed.
        // currently we invoke leavePlayer because some minigames override leavePlayer.
        this.leavePlayer(playername, fullLeave);
        
        if (!endofGame)
        {
            if (this.getAllPlayers().size() < 2)
            {
                if (this.getArenaState() != ArenaState.JOIN)
                {
                    if (this.getArenaState() == ArenaState.STARTING && !this.startedIngameCountdown)
                    {
                        // cancel starting
                        this.setArenaState(ArenaState.JOIN);
                        Util.updateSign(this.plugin, this);
                        try
                        {
                            Bukkit.getScheduler().cancelTask(this.currenttaskid);
                        }
                        catch (@SuppressWarnings("unused") final Exception e)
                        {
                            // silently ignore
                        }
                        for (final String p_ : this.getAllPlayers())
                        {
                            if (Validator.isPlayerOnline(p_))
                            {
                                Util.sendMessage(this.plugin, Bukkit.getPlayer(p_), this.pli.getMessagesConfig().cancelled_starting);
                            }
                        }
                        return;
                    }
                    this.stopArena();
                }
            }
        }
    }
    
    /**
     * Leave the player (real implementation).
     * 
     * @param playername
     *            name of the player
     * @param fullLeave
     *            {@code true} if the player left the server
     */
    private void leavePlayerRaw(final String playername, final boolean fullLeave)
    {
        if (!this.containsPlayer(playername))
        {
            return;
        }
        final Player p = Bukkit.getPlayer(playername);
        final ArenaPlayer ap = ArenaPlayer.getPlayerInstance(playername);
        if (p == null)
        {
            return;
        }
        if (p.isDead())
        {
            this.logger.log(Level.WARNING, p.getName() + " unexpectedly appeared dead! Sending respawn packet."); //$NON-NLS-1$
            Effects.playRespawn(p, this.plugin);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> Arena.this.leavePlayerRaw(playername, fullLeave), 10L);
            return;
        }
        this.players.remove(playername);
        if (this.pli.containsGlobalPlayer(playername))
        {
            this.pli.global_players.remove(playername);
        }
        if (fullLeave)
        {
            this.plugin.getConfig().set("temp.left_players." + playername + ".name", playername);
            this.plugin.getConfig().set("temp.left_players." + playername + ".plugin", this.plugin.getName());
            if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_RESET_INV_WHEN_LEAVING_SERVER))
            {
                for (final ItemStack i : ap.getInventory())
                {
                    if (i != null)
                    {
                        this.plugin.getConfig().set("temp.left_players." + playername + ".items." + Integer.toString((int) Math.round(Math.random() * 10000)) + i.getType().toString(), i);
                    }
                }
            }
            this.plugin.saveConfig();
            
            try
            {
                if (this.pli.global_lost.containsKey(playername))
                {
                    this.pli.getSpectatorManager().showSpectator(p);
                    this.pli.global_lost.remove(playername);
                }
                else
                {
                    this.pli.getSpectatorManager().showSpectators(p);
                }
                if (this.pli.global_arcade_spectator.containsKey(playername))
                {
                    this.pli.global_arcade_spectator.remove(playername);
                }

                p.removePotionEffect(PotionEffectType.JUMP);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                Util.teleportPlayerFixed(p, this.mainlobby);
                p.setFireTicks(0);
                p.setFlying(false);
                if (!p.isOp())
                {
                    p.setAllowFlight(false);
                }
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.RESET_GAMEMMODE)) p.setGameMode(ap.getOriginalGamemode());
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.RESET_XP)) p.setLevel(ap.getOriginalXplvl());
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.RESET_INVENTORY))
                {
                    p.getInventory().setContents(ap.getInventory());
                    p.getInventory().setArmorContents(ap.getArmorInventory());
                    p.updateInventory();
                }
                
                p.setWalkSpeed(0.2F);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 2, 50));
                p.setHealth(20);
                p.removePotionEffect(PotionEffectType.JUMP);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                this.pli.getSpectatorManager().setSpectate(p, false);
                this.pli.getStatsInstance().updateSQLKillsDeathsAfter(p, this);
                
                if (this.pli.getClassesHandler().lasticonm.containsKey(p.getName()))
                {
                    final IconMenu iconm = this.pli.getClassesHandler().lasticonm.get(p.getName());
                    iconm.destroy();
                    this.pli.getClassesHandler().lasticonm.remove(p.getName());
                }
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Failed to log out player out of arena.", e); //$NON-NLS-1$
            }
            return;
        }
        Util.clearInv(p);
        p.setWalkSpeed(0.2F);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.removePotionEffect(PotionEffectType.JUMP);
        this.pli.getSpectatorManager().setSpectate(p, false);
        
        Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveArenaEvent(p, this.plugin, this));
        
        for (final PotionEffect effect : p.getActivePotionEffects())
        {
            if (effect != null)
            {
                p.removePotionEffect(effect.getType());
            }
        }
        
        for (final Entity e : this.getResetEntitiesOnPlayerLeave(playername))
        {
            if (this.isEntityResetOnPlayerLeave(playername, e))
            {
                e.remove();
            }
        }
        
        // pli.global_players.remove(playername);
        if (this.pli.global_arcade_spectator.containsKey(playername))
        {
            this.pli.global_arcade_spectator.remove(playername);
        }
        
        if (this.pli.getPClasses().containsKey(playername))
        {
            this.pli.getPClasses().remove(playername);
        }
        
        Util.updateSign(this.plugin, this);
        
        Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> {
            try
            {
                Arena.this.pli.getHologramsHandler().sendAllHolograms(p);
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Failed playing hologram:", e); //$NON-NLS-1$
            }
        }, 10L);
        
        if (this.pli.getClassesHandler().lasticonm.containsKey(p.getName()))
        {
            final IconMenu iconm = this.pli.getClassesHandler().lasticonm.get(p.getName());
            iconm.destroy();
            this.pli.getClassesHandler().lasticonm.remove(p.getName());
        }
        
        final String arenaname = this.getInternalName();
        final Arena a = this;
        final boolean started_ = this.started;
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            if (Arena.this.ai == null || a.isArcadeMain())
            {
                if (a.mainlobby != null)
                {
                    Util.teleportPlayerFixed(p, a.mainlobby);
                    if (Arena.this.hasLeaveCommand())
                    {
                        Bukkit.getScheduler().runTaskLater(Arena.this.plugin, () -> Arena.this.playLeaveCommand(p), 10L);
                    }
                }
                else if (a.waitinglobby != null)
                {
                    Util.teleportPlayerFixed(p, a.waitinglobby);
                }
            }
            p.setFireTicks(0);
            p.setFlying(false);
            if (!p.isOp())
            {
                p.setAllowFlight(false);
            }
            p.setGameMode(ap.getOriginalGamemode());
            p.setLevel(ap.getOriginalXplvl());
            p.getInventory().setContents(ap.getInventory());
            p.getInventory().setArmorContents(ap.getArmorInventory());
            p.updateInventory();
            p.updateInventory();
            
            if (started_)
            {
                Arena.this.pli.getStatsInstance().updateSQLKillsDeathsAfter(p, a);
                if (!ap.isNoReward())
                {
                    Arena.this.pli.getRewardsInstance().giveWinReward(playername, a, Arena.this.temp_players, Arena.this.global_coin_multiplier);
                }
                else
                {
                    ap.setNoReward(false);
                }
            }
            
            if (Arena.this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SEND_STATS_ON_STOP))
            {
                Util.sendStatsMessage(Arena.this.pli, p);
            }
            
            if (Arena.this.pli.global_lost.containsKey(playername))
            {
                Arena.this.pli.getSpectatorManager().showSpectator(p);
                Arena.this.pli.global_lost.remove(playername);
            }
            else
            {
                Arena.this.pli.getSpectatorManager().showSpectators(p);
            }
            
            try
            {
                Arena.this.pli.scoreboardManager.removeScoreboard(arenaname, p);
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Failed removing scoreboard for player " + p.getName(), e); //$NON-NLS-1$
            }
        }, 5L);
        
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_TP))
        {
            final String server = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_SERVER);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> BungeeUtil.connectToServer(MinigamesAPI.getAPI(), p.getName(), server), 30L);
            return;
        }
    }
    
    /**
     * Plays the leave command for given player.
     * 
     * @param p
     *            leave command.
     */
    void playLeaveCommand(final Player p)
    {
        final String path = ArenaConfigStrings.ARENAS_PREFIX + this.name + ".leavecommand";
        final String leavecommand = this.pli.getArenasConfig().getConfig().getString(path);
        p.getServer().dispatchCommand(p, leavecommand);
    }
    
    /**
     * Checks if there is a leave command.
     * 
     * @return {@code true} if there is a leave command.
     */
    private boolean hasLeaveCommand()
    {
        final String path = ArenaConfigStrings.ARENAS_PREFIX + this.name + ".leavecommand";
        return this.pli.getArenasConfig().getConfig().isSet(path);
    }
    
    /**
     * Spectate the game generally (not specifically after death)
     * 
     * @param playername
     *            name of the player
     */
    public void spectateGame(final String playername)
    {
        final Player p = Bukkit.getPlayer(playername);
        if (p == null)
        {
            return;
        }
        Util.clearInv(p);
        p.setAllowFlight(true);
        p.setFlying(true);
        this.pli.getSpectatorManager().hideSpectator(p, this.getAllPlayers());
        this.pli.scoreboardManager.updateScoreboard(this.plugin, this);
        if (!this.pli.last_man_standing)
        {
            if (this.getPlayerAlive() < 1)
            {
                final Arena a = this;
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> a.stopArena(), 20L);
            }
            else
            {
                this.spectateRaw(p);
            }
        }
        else
        {
            if (this.getPlayerAlive() < 2)
            {
                final Arena a = this;
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> a.stop(), 20L);
            }
            else
            {
                this.spectateRaw(p);
            }
        }
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            Util.clearInv(p);
            Util.giveSpectatorItems(Arena.this.plugin, p);
        }, 3L);
    }
    
    /**
     * Spectate the game after death
     * 
     * @param playername
     *            name of the player
     */
    public void spectate(final String playername)
    {
        if (Validator.isPlayerValid(this.plugin, playername, this))
        {
            this.onEliminated(playername);
            final Player p = Bukkit.getPlayer(playername);
            if (p == null)
            {
                return;
            }
            
            this.pli.global_lost.put(playername, this);
            
            this.pli.getSpectatorManager().setSpectate(p, true);
            if (!this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SPECTATOR_AFTER_FALL_OR_DEATH))
            {
                this.leavePlayer(playername, false, false);
                this.pli.scoreboardManager.updateScoreboard(this.plugin, this);
                return;
            }
            this.spectateGame(playername);
        }
    }
    
    /**
     * Real implementation of joining the arena to spectate.
     * 
     * @param p
     *            player that wants to spectate the arena.
     */
    private void spectateRaw(final Player p)
    {
        if (this.pli.dead_in_fake_bed_effects)
        {
            Effects.playFakeBed(this, p);
        }
        
        if (this.pli.spectator_mode_1_8)
        {
            Effects.sendGameModeChange(p, 3);
        }
        
        final Location temp = this.spawns.get(0);
        try
        {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (Arena.this.specspawn != null)
                {
                    Util.teleportPlayerFixed(p, Arena.this.specspawn);
                }
                else
                {
                    Util.teleportPlayerFixed(p, temp.clone().add(0D, 30D, 0D));
                }
            }, 2L);
        }
        catch (@SuppressWarnings("unused") final Exception e)
        {
            if (this.specspawn != null)
            {
                Util.teleportPlayerFixed(p, this.specspawn);
            }
            else
            {
                Util.teleportPlayerFixed(p, temp.clone().add(0D, 30D, 0D));
            }
        }
    }
    
    /**
     * Let given player spectate the arena; invoked internally by arcade instance.
     * 
     * @param playername
     */
    public void spectateArcade(final String playername)
    {
        // TODO Check why this method is different from spectate
        final Player p = Bukkit.getPlayer(playername);
        this.pli.global_players.put(playername, this);
        this.pli.global_arcade_spectator.put(playername, this);
        Util.teleportPlayerFixed(p, this.getSpawns().get(0).clone().add(0D, 30D, 0D));
        p.setAllowFlight(true);
        p.setFlying(true);
        this.pli.getSpectatorManager().setSpectate(p, true);
    }
    
    /**
     * Sets the task id.
     * 
     * @param id
     *            task id.
     * @deprecated will be removed in 1.5.0; replaced by a more helpful task management, for example to perform tasks during lobby countdown etc.
     */
    @Deprecated
    public void setTaskId(final int id)
    {
        this.currenttaskid = id;
    }
    
    /**
     * Returns the task id
     * 
     * @return task id
     * @deprecated will be removed in 1.5.0; replaced by a more helpful task management, for example to perform tasks during lobby countdown etc.
     */
    @Deprecated
    public int getTaskId()
    {
        return this.currenttaskid;
    }
    
    /**
     * Starts the lobby count down and the arena afterwards
     * 
     * You can instant start an arena by using {@link #start(boolean)}.
     */
    public void startLobby()
    {
        this.startLobby(true);
    }
    
    /**
     * Starts the lobby count down with given amount of seconds.
     * 
     * @param countdown
     *            lobby count down in seconds.
     */
    public void startLobby(final boolean countdown)
    {
        if (this.currentstate != ArenaState.JOIN)
        {
            return;
        }
        this.setArenaState(ArenaState.STARTING);
        Util.updateSign(this.plugin, this);
        this.currentlobbycount = this.pli.getLobbyCountdown();
        final Arena a = this;
        
        // skip countdown
        if (!countdown)
        {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> Arena.this.start(true), 10L);
        }
        
        Sound lobbycountdown_sound_ = null;
        try
        {
            lobbycountdown_sound_ = Sound.valueOf(this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_SOUNDS_LOBBY_COUNTDOWN));
        }
        catch (@SuppressWarnings("unused") final Exception e)
        {
            // silently ignore
        }
        final Sound lobbycountdown_sound = lobbycountdown_sound_;
        
        this.currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), () -> {
            Arena.this.currentlobbycount--;
            if (Arena.this.currentlobbycount == 60 || Arena.this.currentlobbycount == 30 || Arena.this.currentlobbycount == 15 || Arena.this.currentlobbycount == 10
                    || Arena.this.currentlobbycount < 6)
            {
                for (final String p_1 : a.getAllPlayers())
                {
                    if (Validator.isPlayerOnline(p_1))
                    {
                        final Player p1 = Bukkit.getPlayer(p_1);
                        if (countdown)
                        {
                            Util.sendMessage(Arena.this.plugin, p1, Arena.this.pli.getMessagesConfig().teleporting_to_arena_in.replaceAll(ArenaMessageStrings.COUNT, Integer.toString(Arena.this.currentlobbycount)));
                            if (lobbycountdown_sound != null)
                            {
                                p1.playSound(p1.getLocation(), lobbycountdown_sound, 1F, 0F);
                            }
                        }
                    }
                }
            }
            for (final String p_2 : a.getAllPlayers())
            {
                if (Validator.isPlayerOnline(p_2))
                {
                    final Player p2 = Bukkit.getPlayer(p_2);
                    p2.setExp(1F * ((1F * Arena.this.currentlobbycount) / (1F * Arena.this.pli.getLobbyCountdown())));
                    if (Arena.this.pli.use_xp_bar_level)
                    {
                        p2.setLevel(Arena.this.currentlobbycount);
                    }
                }
            }
            if (Arena.this.currentlobbycount < 1)
            {
                Bukkit.getScheduler().runTaskLater(Arena.this.plugin, () -> Arena.this.start(true), 10L);
                try
                {
                    Bukkit.getScheduler().cancelTask(Arena.this.currenttaskid);
                }
                catch (@SuppressWarnings("unused") final Exception e)
                {
                    // silently ignore
                }
            }
        }, 5L, 20).getTaskId();
    }
    
    /**
     * Instantly starts the arena, teleports players and udpates the arena.
     * 
     * @param tp {@code true} to teleport players to spawns
     */
    public void start(final boolean tp)
    {
        try
        {
            Bukkit.getScheduler().cancelTask(this.currenttaskid);
        }
        catch (@SuppressWarnings("unused") final Exception e)
        {
            // silently ignore
        }
        this.currentingamecount = this.pli.getIngameCountdown();
        if (tp)
        {
            this.pspawnloc = Util.teleportAllPlayers(this.getAllPlayers(), this.spawns);
        }
        final boolean clearinv = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_CLEANINV_WHILE_INGAMECOUNTDOWN);
        for (final String p_ : this.getAllPlayers())
        {
            final Player p = Bukkit.getPlayer(p_);
            p.setWalkSpeed(0.0F);
            p.setFoodLevel(5);
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -7)); // -5
            this.pli.scoreboardLobbyManager.removeScoreboard(this.getInternalName(), p);
            if (clearinv)
            {
                Util.clearInv(p);
            }
        }
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> Arena.this.pli.scoreboardManager.updateScoreboard(Arena.this.plugin, this), 20L);
        this.startedIngameCountdown = true;
        if (!this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_INGAME_COUNTDOWN_ENABLED))
        {
            this.startRaw();
            return;
        }
        
        Sound ingamecountdown_sound_ = null;
        try
        {
            ingamecountdown_sound_ = Sound.valueOf(this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_SOUNDS_INGAME_COUNTDOWN));
        }
        catch (@SuppressWarnings("unused") final Exception e)
        {
            // silently ignore
        }
        final Sound ingamecountdown_sound = ingamecountdown_sound_;
        
        this.currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), () -> {
            Arena.this.currentingamecount--;
            if (Arena.this.currentingamecount == 60 || Arena.this.currentingamecount == 30 || Arena.this.currentingamecount == 15 || Arena.this.currentingamecount == 10
                    || Arena.this.currentingamecount < 6)
            {
                for (final String p_1 : Arena.this.getAllPlayers())
                {
                    if (Validator.isPlayerOnline(p_1))
                    {
                        final Player p1 = Bukkit.getPlayer(p_1);
                        Util.sendMessage(Arena.this.plugin, p1, Arena.this.pli.getMessagesConfig().starting_in.replaceAll(ArenaMessageStrings.COUNT, Integer.toString(Arena.this.currentingamecount)));
                        if (ingamecountdown_sound != null)
                        {
                            p1.playSound(p1.getLocation(), ingamecountdown_sound, 1F, 0F);
                        }
                    }
                }
            }
            for (final String p_2 : Arena.this.getAllPlayers())
            {
                if (Validator.isPlayerOnline(p_2))
                {
                    final Player p2 = Bukkit.getPlayer(p_2);
                    p2.setExp(1F * ((1F * Arena.this.currentingamecount) / (1F * Arena.this.pli.getIngameCountdown())));
                    if (Arena.this.pli.use_xp_bar_level)
                    {
                        p2.setLevel(Arena.this.currentingamecount);
                    }
                }
            }
            if (Arena.this.currentingamecount < 1)
            {
                Arena.this.startRaw();
            }
        }, 5L, 20).getTaskId();
        
        for (final String p_ : this.getAllPlayers())
        {
            if (this.pli.getShopHandler().hasItemBought(p_, "coin_boost2"))
            {
                this.global_coin_multiplier = 2;
                break;
            }
            if (this.pli.getShopHandler().hasItemBought(p_, "coin_boost3"))
            {
                this.global_coin_multiplier = 3;
                break;
            }
        }
    }
    
    /**
     * Real implementation of starting the arena.
     */
    void startRaw()
    {
        this.setArenaState(ArenaState.INGAME);
        this.startedIngameCountdown = false;
        Util.updateSign(this.plugin, this);
        Bukkit.getServer().getPluginManager().callEvent(new ArenaStartEvent(this.plugin, this));
        final boolean send_game_started_msg = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SEND_GAME_STARTED_MSG);
        for (final String p_ : this.getAllPlayers())
        {
            try
            {
                if (!this.pli.global_lost.containsKey(p_))
                {
                    final Player p = Bukkit.getPlayer(p_);
                    if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_AUTO_ADD_DEFAULT_KIT))
                    {
                        if (!this.pli.getClassesHandler().hasClass(p_))
                        {
                            this.pli.getClassesHandler().setClass("default", p_, false);
                        }
                        this.pli.getClassesHandler().getClass(p_);
                    }
                    else
                    {
                        Util.clearInv(Bukkit.getPlayer(p_));
                        this.pli.getClassesHandler().getClass(p_);
                    }
                    if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SHOP_ENABLED))
                    {
                        this.pli.getShopHandler().giveShopItems(p);
                    }
                    p.setFlying(false);
                    p.setAllowFlight(false);
                }
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "Failed to set class", e); //$NON-NLS-1$
            }
            final Player p = Bukkit.getPlayer(p_);
            p.setWalkSpeed(0.2F);
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 50));
            p.setFoodLevel(20);
            p.removePotionEffect(PotionEffectType.JUMP);
            if (send_game_started_msg)
            {
                p.sendMessage(this.pli.getMessagesConfig().game_started);
            }
        }
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUNGEE_WHITELIST_WHILE_GAME_RUNNING))
        {
            Bukkit.setWhitelist(true);
        }
        this.started = true;
        Bukkit.getServer().getPluginManager().callEvent(new ArenaStartedEvent(this.plugin, this));
        this.started();
        try
        {
            Bukkit.getScheduler().cancelTask(this.currenttaskid);
        }
        catch (@SuppressWarnings("unused") final Exception e)
        {
            // silently ignore
        }
        
        // Maximum game time:
        this.maximum_game_time = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            for (final String p_ : Arena.this.getAllPlayers())
            {
                if (Validator.isPlayerValid(Arena.this.plugin, p_, Arena.this))
                {
                    Bukkit.getPlayer(p_).sendMessage(Arena.this.pli.getMessagesConfig().stop_cause_maximum_game_time);
                }
            }
            Bukkit.getScheduler().runTaskLater(Arena.this.plugin, () -> this.stopArena(), 5 * 20L);
        }, 20L * 60L * (long) this.plugin.getConfig().getDouble(ArenaConfigStrings.CONFIG_DEFAULT_MAX_GAME_TIME_IN_MINUTES) - 5 * 20L);
    }
    
    /**
     * Gets executed after an arena started (after ingame countdown); can be overridden by minigames.
     */
    public void started()
    {
        this.logger.info("started"); //$NON-NLS-1$
    }
    
    /**
     * Invoked externally to stop the arena; ensures that it only is called once.
     */
    public synchronized void stopArena()
    {
        // TODO eliminate synchronized but check for the current arena state before actually invoking stop
        // TODO check if spigot really invokes the tasks in serial order.
        // This is an implementation detail that may break in future versions.
        this.stop();
    }
    
    /**
     * Returns the possible entites to be removed for reset
     * @param player
     * @return entites
     */
    protected List<Entity> getResetEntitiesOnPlayerLeave(String player)
    {
        return this.getResetEntities(player);
    }
    
    /**
     * Checks if given entity is removed for resetting the map
     * @param player
     * @param e
     * @return {@code true} for removing the entity
     */
    protected boolean isEntityResetOnPlayerLeave(String player, Entity e)
    {
        return this.isEntityReset(player, e);
    }
    
    /**
     * Returns the possible entites to be removed for reset
     * @param player
     * @return entites
     */
    protected List<Entity> getResetEntities(String player)
    {
        return Bukkit.getPlayer(player).getNearbyEntities(50, 50, 50);
    }
    
    /**
     * Checks if given entity is removed for resetting the map
     * @param player
     * @param e
     * @return {@code true} for removing the entity
     */
    protected boolean isEntityReset(String player, Entity e)
    {
        return e.getType() == EntityType.DROPPED_ITEM || e.getType() == EntityType.ENDERMAN || e.getType() == EntityType.SLIME || e.getType() == EntityType.ZOMBIE
                || e.getType() == EntityType.SKELETON || e.getType() == EntityType.SPIDER || e.getType() == EntityType.CREEPER || e.getType() == EntityType.VILLAGER
                || e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.ARROW;
    }
    
    /**
     * Stops the arena and teleports all players to the mainlobby
     */
    protected void stop()
    {
        Bukkit.getServer().getPluginManager().callEvent(new ArenaStopEvent(this.plugin, this));
        final Arena a = this;
        if (this.maximum_game_time != null)
        {
            this.maximum_game_time.cancel();
        }
        this.temp_players = new ArrayList<>(this.players);
        if (!this.temp_delay_stopped)
        {
            if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_DELAY_ENABLED))
            {
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    Arena.this.temp_delay_stopped = true;
                    a.stopArena();
                }, this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_DELAY_AMOUNT_SECONDS) * 20L);
                this.setArenaState(ArenaState.RESTARTING);
                Util.updateSign(this.plugin, this);
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SPAWN_FIREWORKS_FOR_WINNERS))
                {
                    if (this.getAllPlayers().size() > 0)
                    {
                        Util.spawnFirework(Bukkit.getPlayer(this.getAllPlayers().get(0)));
                    }
                }
                return;
            }
        }
        this.temp_delay_stopped = false;
        
        try
        {
            Bukkit.getScheduler().cancelTask(this.currenttaskid);
        }
        catch (@SuppressWarnings("unused") final Exception e)
        {
            // silently ignore
        }
        
        this.setArenaState(ArenaState.RESTARTING);
        
        final ArrayList<String> temp = new ArrayList<>(this.getAllPlayers());
        for (final String p_ : temp)
        {
            try
            {
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    if (Validator.isPlayerOnline(p_))
                    {
                        for (final Entity e : Bukkit.getPlayer(p_).getNearbyEntities(50, 50, 50))
                        {
                            if (this.isEntityReset(p_, e))
                            {
                                e.remove();
                            }
                        }
                    }
                }, 10L);
            }
            catch (final Exception e)
            {
                this.logger.log(Level.WARNING, "failed clearing entities", e); //$NON-NLS-1$
            }
            this.leavePlayer(p_, false, true);
        }
        
        try
        {
            for (final ItemStack item : this.global_drops)
            {
                if (item != null)
                {
                    item.setType(Material.AIR);
                }
            }
        }
        catch (final Exception e)
        {
            this.logger.log(Level.WARNING, "failed clearing entities", e); //$NON-NLS-1$
        }
        
        if (a.getArenaType() == ArenaType.REGENERATION)
        {
            this.reset();
        }
        else
        {
            a.setArenaState(ArenaState.JOIN);
            Util.updateSign(this.plugin, a);
        }
        
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            Arena.this.players.clear();
            for (final IconMenu im : Arena.this.pli.getClassesHandler().lasticonm.values())
            {
                im.destroy();
            }
        }, 10L);
        
        this.started = false;
        this.startedIngameCountdown = false;
        
        this.temp_countdown = true;
        this.skip_join_lobby = false;
        this.currentspawn = 0;
        
        try
        {
            this.pli.scoreboardManager.clearScoreboard(this.getInternalName());
            this.pli.scoreboardLobbyManager.clearScoreboard(this.getInternalName());
        }
        catch (final Exception e)
        {
            this.logger.log(Level.WARNING, "failed clearing score boards", e); //$NON-NLS-1$
        }
        
        /*
         * try { pli.getStatsInstance().updateSkulls(); } catch (Exception e) {
         * 
         * }
         */
        
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUNGEE_WHITELIST_WHILE_GAME_RUNNING))
        {
            Bukkit.setWhitelist(false);
        }
        
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EXECUTE_CMDS_ON_STOP))
        {
            final String[] cmds = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_CMDS).split(";"); //$NON-NLS-1$
            if (cmds.length > 0)
            {
                for (final String cmd : cmds)
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }
        
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_TP))
        {
            final String server = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_SERVER);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                for (final Player p : Bukkit.getOnlinePlayers())
                {
                    BungeeUtil.connectToServer(MinigamesAPI.getAPI(), p.getName(), server);
                }
            }, 30L);
            return;
        }
        
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EXECUTE_CMDS_ON_STOP))
        {
            final String[] cmds = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_CMDS_AFTER).split(";"); //$NON-NLS-1$
            if (cmds.length > 0)
            {
                for (final String cmd : cmds)
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }
        
        if (this.ai != null)
        {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (Arena.this.ai != null)
                {
                    Arena.this.ai.nextMinigame();
                    Arena.this.ai = null;
                }
            }, 10L);
        }
        else
        {
            // Map rotation only works without Arcade
            // check if there is only one player or none left
            if (temp.size() < 2)
            {
                return;
            }
            if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_MAP_ROTATION))
            {
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> a.nextArenaOnMapRotation(temp), 35L);
            }
        }
        
    }
    
    /**
     * Rebuilds an arena from file (only for arenas of REGENERATION type)
     */
    public void reset()
    {
        if (this.pli.old_reset)
        {
            ArenaLogger.debug("Resetting using old method."); //$NON-NLS-1$
            try
            {
                PrivateUtil.loadArenaFromFileSYNC(this.plugin, this);
            }
            catch (final Exception e)
            {
                // TODO Exception log.
                ArenaLogger.debug("Error resetting map using old method. " + e.getMessage()); //$NON-NLS-1$
            }
        }
        else
        {
            this.sr.reset();
        }
    }
    
    /***
     * Use this when someone got killed/pushed down/eliminated in some way by a player
     * 
     * @param playername
     *            The player that got eliminated
     */
    public void onEliminated(final String playername)
    {
        if (this.lastdamager.containsKey(playername))
        {
            final Player killer = Bukkit.getPlayer(this.lastdamager.get(playername));
            if (killer != null)
            {
                this.pli.getStatsInstance().addDeath(playername);
                this.temp_kill_count.put(killer.getName(), this.temp_kill_count.containsKey(killer.getName()) ? this.temp_kill_count.get(killer.getName()) + 1 : 1);
                this.temp_death_count.put(playername, this.temp_death_count.containsKey(playername) ? this.temp_death_count.get(playername) + 1 : 1);
                this.pli.getRewardsInstance().giveKillReward(killer.getName());
                Util.sendMessage(this.plugin, killer, MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().you_got_a_kill.replaceAll(ArenaMessageStrings.PLAYER, playername));
                for (final String p_ : this.getAllPlayers())
                {
                    if (!p_.equalsIgnoreCase(killer.getName()))
                    {
                        if (Validator.isPlayerOnline(p_))
                        {
                            Bukkit.getPlayer(p_).sendMessage(MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().player_was_killed_by.replaceAll(ArenaMessageStrings.PLAYER, playername)
                                    .replaceAll(ArenaMessageStrings.KILLER, killer.getName()));
                        }
                    }
                }
            }
            this.lastdamager.remove(playername);
        }
        else
        {
            this.pli.getStatsInstance().addDeath(playername);
        }
    }
    
    /**
     * Will shuffle all arenas and join the next available arena
     * 
     * @param players
     * @deprecated will be removed in 1.5.0; replaced by some external shuffle support class
     */
    public void nextArenaOnMapRotation(final ArrayList<String> players)
    {
        final ArrayList<Arena> arenas = new ArrayList<>(this.pli.getArenas());
        Collections.shuffle(arenas);
        for (final Arena a : arenas)
        {
            if (a.getArenaState() == ArenaState.JOIN && a != this)
            {
                this.logger.info("Next arena on map rotation: " + a.getInternalName()); //$NON-NLS-1$
                for (final String p_ : players)
                {
                    if (!a.containsPlayer(p_))
                    {
                        a.joinPlayerLobby(p_, false);
                    }
                }
            }
        }
    }
    
    /**
     * Returns a string of players left in the game.
     * 
     * @return string: "alive/total"
     */
    public String getPlayerCount()
    {
        return Integer.toString(this.getPlayerAlive()) + "/" + Integer.toString(this.getAllPlayers().size()); //$NON-NLS-1$
    }
    
    /**
     * Returns the amount of players left in the game.
     * 
     * @return amount of players excluding players that already lost.
     */
    public int getPlayerAlive()
    {
        int alive = 0;
        for (final String p_ : this.getAllPlayers())
        {
            if (this.pli.global_lost.containsKey(p_))
            {
                continue;
            }
            alive++;
        }
        return alive;
    }
    
    /**
     * Returns the waiting lobby location.
     * 
     * @return waiting lobby location; may be {@code null}.
     * @deprecated will be removed in 1.5.0; replaced by {@link #getWaitingLobby()}
     */
    @Deprecated
    public Location getWaitingLobbyTemp()
    {
        return this.waitinglobby;
    }
    
    /**
     * Returns the waiting lobby location.
     * 
     * @return waiting lobby location; may be {@code null}.
     */
    public Location getWaitingLobby()
    {
        return this.waitinglobby;
    }
    
    /**
     * Returns the main lobby location.
     * 
     * @return main lobby location; may be {@code null}.
     * @deprecated will be removed in 1.5.0; replaced by {@link #getMainLobby()}
     */
    @Deprecated
    public Location getMainLobbyTemp()
    {
        return this.mainlobby;
    }
    
    /**
     * Returns the main lobby location.
     * 
     * @return main lobby location; may be {@code null}.
     */
    public Location getMainLobby()
    {
        return this.mainlobby;
    }
    
    /**
     * Returns the arcade instance if this arena is part of arcade.
     * 
     * @return arcade instance or {@code null} for stand alone arenas.
     */
    public ArcadeInstance getArcadeInstance()
    {
        return this.ai;
    }
    
    /**
     * Checks if this is the arcade main arena.
     * 
     * @return {@code true} if this is the arcade main arena.
     */
    public boolean isArcadeMain()
    {
        // TODO review what this flag is meant for.
        // currently no one ever sets this so it is always false; but it is used in some other classes.
        return this.isArcadeMain;
    }
    
    /**
     * Sets the arcade main flag.
     * 
     * @param t
     *            arcade main flag
     */
    public void setArcadeMain(final boolean t)
    {
        this.isArcadeMain = t;
    }
    
    /**
     * Returns the player spawn locations.
     * 
     * @return player spawn locations.
     * 
     * @deprecated will be removed in 1.5.0; seems not very useful; currently only used in SkyWars.
     */
    @Deprecated
    public HashMap<String, Location> getPSpawnLocs()
    {
        return this.pspawnloc;
    }
    
    /**
     * Returns the java plugin owning this arena.
     * 
     * @return java plugin
     */
    public JavaPlugin getPlugin()
    {
        return this.plugin;
    }
    
    /**
     * Returns the plugin instance owning this arena.
     * 
     * @return minigames plugin instance.
     */
    public PluginInstance getPluginInstance()
    {
        return this.pli;
    }
    
    /**
     * Returns the ingame countdown time in seconds
     * 
     * @return countdown time in seconds
     */
    public int getCurrentIngameCountdownTime()
    {
        return this.currentingamecount;
    }
    
    /**
     * Returns the lobby countdown time in seconds
     * 
     * @return countdown time in seconds
     */
    public int getCurrentLobbyCountdownTime()
    {
        return this.currentlobbycount;
    }
    
    /**
     * Returns the ingame countdown started flag.
     * 
     * @return {@code true} if the ingame countdown already started.
     */
    public boolean getIngameCountdownStarted()
    {
        return this.startedIngameCountdown;
    }
    
    /**
     * Returns {@code true} if the arena was initialized.
     * 
     * @return {@code true} for successful init.
     */
    public boolean isSuccessfullyInit()
    {
        return this.isSuccessfullyInitialized;
    }
    
    /**
     * Sets last damager for fetching kill statistics.
     * 
     * @param targetPlayer
     *            target player that was hit
     * @param damager
     *            the damager that shot the profile
     */
    public void setLastDamager(final String targetPlayer, final String damager)
    {
        this.lastdamager.put(targetPlayer, damager);
    }
    
}
