# Minigames-Lib 2.0 - MigrationList

## Arena.java

- [ ] ArcadeInstance ai
- [ ] boolean isArcadeMain
- [ ] boolean isSuccessfullyInitialized
- [ ] ArrayList&lt;Location> spawns
- [ ] HashMap&lt;String, Location> pspawnloc
- [ ] HashMap&lt;String, String> lastdamager
- [ ] HashMap&lt;String, Integer> temp\_kill\_count
- [ ] HashMap&lt;String, Integer> temp\_death\_count
- [ ] Location mainlobby
- [ ] Location waitinglobby
- [ ] Location specspawn
- [ ] Location signloc
- [ ] int max\_players
- [ ] int min\_players
- [ ] boolean viparena
- [ ] ArrayList&lt;String> players
- [ ] ArrayList&lt;String> temp\_players
- [ ] ArenaType type
- [ ] ArenaState currentstate
- [X] String name
    > com.github.mce.minigames.api.arena.ArenaInterface.getInternalName()
- [X] String displayname
    > com.github.mce.minigames.api.arena.ArenaInterface.getDisplayName()
    > com.github.mce.minigames.api.arena.ArenaInterface.getDisplayName(Locale)
- [ ] boolean started
- [ ] boolean startedIngameCountdown
- [ ] boolean showArenascoreboard
- [ ] boolean alwaysPvP
- [ ] SmartReset sr
- [ ] Cuboid boundaries
- [ ] Cuboid lobby\_boundaries
- [ ] Cuboid spec\_boundaries
- [ ] boolean temp\_countdown
- [ ] boolean skip\_join\_lobby
- [ ] int currentspawn
- [ ] int global\_coin\_multiplier
- [ ] BukkitTask maximum\_game\_time
- [ ] ArrayList&lt;ItemStack> global\_drops
- [ ] int currentlobbycount
- [ ] int currentingamecount
- [ ] int currenttaskid
- [ ] boolean temp\_delay\_stopped
- [ ] ArenaLogger logger
- [ ] Arena(final JavaPlugin plugin, final String name)
- [ ] Arena(final JavaPlugin plugin, final String name, final ArenaType type)
- [ ] public void init(final Location signloc, final ArrayList&lt;Location> spawns, final Location mainlobby, final Location waitinglobby, final int max\_players, final int min\_players, final boolean viparena)
- [ ] Arena initArena(final Location signloc, final ArrayList&lt;Location> spawn, final Location mainlobby, final Location waitinglobby, final int max\_players, final int min\_players, final boolean viparena)
- [X] Arena getArena()
    > removed (does not make sense)
- [ ] SmartReset getSmartReset()
- [ ] boolean getShowScoreboard()
- [ ] boolean getAlwaysPvP()
- [ ] void setAlwaysPvP(final boolean t)
- [ ] Location getSignLocation()
- [ ] void setSignLocation(final Location l)
- [ ] ArrayList&lt;Location> getSpawns()
- [ ] Cuboid getBoundaries()
- [ ] Cuboid getLobbyBoundaries()
- [ ] Cuboid getSpecBoundaries()
- [X] String getInternalName()
    > com.github.mce.minigames.api.arena.ArenaInterface.getInternalName()
- [X] String getDisplayName()
    > com.github.mce.minigames.api.arena.ArenaInterface.getDisplayName()
    > com.github.mce.minigames.api.arena.ArenaInterface.getDisplayName(Locale)
- [X] String getName()
    > com.github.mce.minigames.api.arena.ArenaInterface.getInternalName()
- [ ] int getMaxPlayers()
- [ ] int getMinPlayers()
- [ ] void setMinPlayers(final int i)
- [ ] void setMaxPlayers(final int i)
- [ ] boolean isVIPArena()
- [ ] void setVIPArena(final boolean t)
- [ ] ArrayList&lt;String> getAllPlayers()
- [ ] boolean containsPlayer(final String playername)
- [ ] boolean addPlayer(final String playername)
- [ ] boolean removePlayer(final String playername)
- [ ] void joinPlayerLobby(final String playername)
- [ ] void joinPlayerLobby(final UUID playerUuid)
- [ ] void joinPlayerLobby(final String playername, final boolean countdown)
- [ ] void joinPlayerLobby(final String playername, final ArcadeInstance arcade, final boolean countdown, final boolean skip\_lobby)
- [ ] ArenaState getArenaState()
- [ ] void setArenaState(final ArenaState s)
- [ ] ArenaType getArenaType()
- [ ] void leavePlayer(final String playername, final boolean fullLeave)
- [ ] void leavePlayer(final String playername, final boolean fullLeave, final boolean endofGame)
- [ ] void leavePlayerRaw(final String playername, final boolean fullLeave)
- [ ] void playLeaveCommand(final Player p)
- [ ] boolean hasLeaveCommand()
- [ ] spectateGame(final String playername)
- [ ] void spectate(final String playername)
- [ ] void spectateRaw(final Player p)
- [ ] void spectateArcade(final String playername)
- [ ] void setTaskId(final int id)
- [ ] int getTaskId()
- [ ] void startLobby(final boolean countdown)
- [ ] void start(final boolean tp)
    > com.github.mce.minigames.api.arena.ArenaInterface.start()
- [ ] void startRaw()
- [ ] void stopArena()
- [ ] List&lt;Entity> getResetEntitiesOnPlayerLeave(String player)
- [ ] boolean isEntityResetOnPlayerLeave(String player, Entity e)
- [ ] List&lt;Entity> getResetEntities(String player)
- [ ] boolean isEntityReset(String player, Entity e)
- [ ] void stop()
- [ ] void reset()
- [ ] void onEliminated(final String playername)
- [ ] void nextArenaOnMapRotation(final ArrayList&lt;String> players)
- [ ] String getPlayerCount()
- [ ] int getPlayerAlive()
- [ ] Location getWaitingLobbyTemp()
- [ ] Location getWaitingLobby()
- [ ] Location getMainLobbyTemp()
- [ ] Location getMainLobby()
- [ ] ArcadeInstance getArcadeInstance()
- [ ] boolean isArcadeMain()
- [ ] void setArcadeMain(final boolean t)
- [ ] HashMap&lt;String, Location> getPSpawnLocs()
- [ ] JavaPlugin getPlugin()
- [ ] PluginInstance getPluginInstance()
- [ ] int getCurrentIngameCountdownTime()
- [ ] int getCurrentLobbyCountdownTime()
- [ ] boolean getIngameCountdownStarted()
- [ ] boolean isSuccessfullyInit()
- [ ] void setLastDamager(final String targetPlayer, final String damager)

## ArenaConfigStrings.java

- [ ] String BOUNDS\_LOW
- [ ] String BOUNDS\_HIGH
- [ ] String LOBBY\_BOUNDS\_LOW
- [ ] String LOBBY\_BOUNDS\_HIGH
- [ ] String SPEC\_BOUNDS\_LOW
- [ ] String SPEC\_BOUNDS\_HIGH
- [ ] String SPEC\_SPAWN
- [ ] String ARENAS\_PREFIX
- [ ] String DISPLAYNAME\_SUFFIX
- [ ] String AUTHOR\_SUFFIX
- [ ] String DESCRIPTION\_SUFFIX
- [ ] String CONFIG\_CLASS\_SELECTION\_ITEM
- [ ] String CONFIG\_EXIT\_ITEM
- [ ] String CONFIG\_ACHIEVEMENT\_ITEMS
- [ ] String CONFIG\_SPECTATOR\_ITEM
- [ ] String CONFIG\_SHOP\_SELECTION\_ITEM
- [ ] String CONFIG\_CLASSES\_GUI\_ROWS
- [ ] String CONFIG\_SHOP\_GUI\_ROWS
- [ ] String CONFIG\_SPECTATOR\_AFTER\_FALL\_OR\_DEATH
- [ ] String CONFIG\_SPECTATOR\_MOVE\_Y\_LOCK
- [ ] String CONFIG\_DEFAULT\_MAX\_PLAYERS
- [ ] String CONFIG\_DEFAULT\_MIN\_PLAYERS
- [ ] String CONFIG\_DEFAULT\_MAX\_GAME\_TIME\_IN\_MINUTES
- [ ] String CONFIG\_LOBBY\_COUNTDOWN
- [ ] String CONFIG\_INGAME\_COUNTDOWN
- [ ] String CONFIG\_INGAME\_COUNTDOWN\_ENABLED
- [ ] String CONFIG\_SKIP\_LOBBY
- [ ] String CONFIG\_CLEANINV\_WHILE\_INGAMECOUNTDOWN
- [ ] String CONFIG\_CLASSES\_ENABLED
- [ ] String CONFIG\_SHOP\_ENABLED
- [ ] String CONFIG\_USE\_CREADITS\_INSTEAD\_MONEY\_FOR\_KITS
- [ ] String CONFIG\_RESET\_INV\_WHEN\_LEAVING\_SERVER
- [ ] String CONFIG\_COLOR\_BACKGROUND\_WOOL
- [ ] String CONFIG\_SHOW\_CLASSES\_WITHOUT\_PERM
- [ ] String CONFIG\_REWARDS\_ECONOMY
- [ ] String CONFIG\_REWARDS\_ECONOMY\_REWARD
- [ ] String CONFIG\_REWARDS\_ITEM\_REWARD
- [ ] String CONFIG\_REWARDS\_ITEM\_REWARD\_IDS
- [ ] String CONFIG\_REWARDS\_COMMAND\_REWARD
- [ ] String CONFIG\_REWARDS\_COMMAND
- [ ] String CONFIG\_REWARDS\_ECONOMY\_FOR\_KILLS
- [ ] String CONFIG\_REWARDS\_ECONOMY\_REWARD\_FOR\_KILLS
- [ ] String CONFIG\_REWARDS\_COMMAND\_REWARD\_FOR\_KILLS
- [ ] String CONFIG\_REWARDS\_COMMAND\_FOR\_KILLS
- [ ] String CONFIG\_REWARDS\_ECONOMY\_FOR\_PARTICIPATION
- [ ] String CONFIG\_REWARDS\_ECONOMY\_REWARD\_FOR\_PARTICIPATION
- [ ] String CONFIG\_REWARDS\_COMMAND\_REWARD\_FOR\_PARTICIPATION
- [ ] String CONFIG\_REWARDS\_COMMAND\_FOR\_PARTICIPATION
- [ ] String CONFIG\_STATS\_POINTS\_FOR\_KILL
- [ ] String CONFIG\_STATS\_POINTS\_FOR\_WIN
- [ ] String CONFIG\_ARCADE\_ENABLED
- [ ] String CONFIG\_ARCADE\_MIN\_PLAYERS
- [ ] String CONFIG\_ARCADE\_MAX\_PLAYERS
- [ ] String CONFIG\_ARCADE\_ARENA\_TO\_PREFER\_ENABLED
- [ ] String CONFIG\_ARCADE\_ARENA\_TO\_PREFER\_ARENA
- [ ] String CONFIG\_ARCADE\_LOBBY\_COUNTDOWN
- [ ] String CONFIG\_ARCADE\_SHOW\_EACH\_LOBBY\_COUNTDOWN
- [ ] String CONFIG\_ARCADE\_INFINITE\_ENABLED
- [ ] String CONFIG\_ARCADE\_INFINITE\_SECONDS\_TO\_NEW\_ROUND
- [ ] String CONFIG\_BUNGEE\_GAME\_ON\_JOIN
- [ ] String CONFIG\_BUNGEE\_TELEPORT\_ALL\_TO\_SERVER\_ON\_STOP\_TP
- [ ] String CONFIG\_BUNGEE\_TELEPORT\_ALL\_TO\_SERVER\_ON\_STOP\_SERVER
- [ ] String CONFIG\_BUNGEE\_WHITELIST\_WHILE\_GAME\_RUNNING
- [ ] String CONFIG\_EXECUTE\_CMDS\_ON\_STOP
- [ ] String CONFIG\_CMDS
- [ ] String CONFIG\_CMDS\_AFTER
- [ ] String CONFIG\_MAP\_ROTATION
- [ ] String CONFIG\_BROADCAST\_WIN
- [ ] String CONFIG\_BUY\_CLASSES\_FOREVER
- [ ] String CONFIG\_DISABLE\_COMMANDS\_IN\_ARENA
- [ ] String CONFIG\_COMMAND\_WHITELIST
- [ ] String CONFIG\_LEAVE\_COMMAND
- [ ] String CONFIG\_SPAWN\_FIREWORKS\_FOR\_WINNERS
- [ ] String CONFIG\_POWERUP\_BROADCAST
- [ ] String CONFIG\_POWERUP\_FIREWORKS
- [ ] String CONFIG\_USE\_CUSTOM\_SCOREBOARD
- [ ] String CONFIG\_DELAY\_ENABLED
- [ ] String CONFIG\_DELAY\_AMOUNT\_SECONDS
- [ ] String CONFIG\_SEND\_GAME\_STARTED\_MSG
- [ ] String CONFIG\_AUTO\_ADD\_DEFAULT\_KIT
- [ ] String CONFIG\_LAST\_MAN\_STANDING\_WINS
- [ ] String CONFIG\_EFFECTS\_BLOOD
- [ ] String CONFIG\_EFFECTS\_DMG\_IDENTIFIER\_HOLO
- [ ] String CONFIG\_EFFECTS\_DEAD\_IN\_FAKE\_BED
- [ ] String CONFIG\_EFFECTS\_1\_8\_TITLES
- [ ] String CONFIG\_EFFECTS\_1\_8\_SPECTATOR\_MODE
- [ ] String CONFIG\_SOUNDS\_LOBBY\_COUNTDOWN
- [ ] String CONFIG\_SOUNDS\_INGAME\_COUNTDOWN
- [ ] String CONFIG\_CHAT\_PER\_ARENA\_ONLY
- [ ] String CONFIG\_CHAT\_SHOW\_SCORE\_IN\_ARENA
- [ ] String CONFIG\_COMPASS\_TRACKING\_ENABLED
- [ ] String CONFIG\_ALLOW\_CLASSES\_SELECTION\_OUT\_OF\_ARENAS
- [ ] String CONFIG\_SEND\_STATS\_ON\_STOP
- [ ] String CONFIG\_USE\_XP\_BAR\_LEVEL
- [ ] String CONFIG\_USE\_OLD\_RESET\_METHOD
- [ ] String CONFIG\_CHAT\_ENABLED
- [ ] String CONFIG\_EXTRA\_LOBBY\_ITEM\_PREFIX
- [ ] String CONFIG\_EXTRA\_LOBBY\_ITEM\_ENABLED\_SUFFIX
- [ ] String CONFIG\_EXTRA\_LOBBY\_ITEM\_ITEM\_SUFFIX
- [ ] String CONFIG\_EXTRA\_LOBBY\_ITEM\_NAME\_SUFFIX
- [ ] String CONFIG\_EXTRA\_LOBBY\_ITEM\_COMMAND\_SUFFIX
- [ ] String CONFIG\_MYSQL\_ENABLED
- [ ] String CONFIG\_MYSQL\_HOST
- [ ] String CONFIG\_MYSQL\_USER
- [ ] String CONFIG\_MYSQL\_PW
- [ ] String CONFIG\_MYSQL\_DATABASE
- [ ] String RESET\_INVENTORY
- [ ] String RESET\_XP
- [ ] String RESET\_GAMEMODE

## ArenaListener.java

- [ ] ArrayList&lt;String> cmds
- [ ] String leave\_cmd
- [ ] int loseY
- [ ] ArenaListener(final JavaPlugin plugin, final PluginInstance pinstance, final String minigame)
- [ ] ArenaListener(final JavaPlugin plugin, final PluginInstance pinstance, final String minigame, final ArrayList&lt;String> cmds)
- [ ] void onExplode(final EntityExplodeEvent event)
- [ ] void onExplode2(final BlockExplodeEvent event)
- [ ] void onBlockFromTo(final BlockFromToEvent event)
- [ ] void onBlockFade(final BlockFadeEvent event)
- [ ] void onBlockPhysics(final BlockPhysicsEvent event)
- [ ] void onBlockRedstone(final BlockRedstoneEvent event)
- [ ] void onBlockSpread(final BlockSpreadEvent event)
- [ ] void onEntityChangeBlock(final EntityChangeBlockEvent event)
- [ ] void onLeavesDecay(final LeavesDecayEvent event)
- [ ] void onBlockBurn(final BlockBurnEvent event)
- [ ] void onStructureGrow(final StructureGrowEvent event)
- [ ] void onBlockBreak2(final BlockBreakEvent event)
- [ ] void onPlayerDrop(final PlayerDropItemEvent event)
- [ ] void onPlayerPickupItem(final PlayerPickupItemEvent event)
- [ ] void onInventoryClick(final InventoryClickEvent event)
- [ ] void onHunger(final FoodLevelChangeEvent event)
- [ ] void NoDamageEntityInLobby(final EntityDamageByEntityEvent event)
- [ ] void NoClickEntityInLobby(final PlayerInteractEntityEvent event) throws IOException
- [ ] void onPaintingBreak(final HangingBreakByEntityEvent event)
- [ ] void Space(final PlayerMoveEvent event)
- [ ] void onMove(final PlayerMoveEvent event)
- [ ] void onVMove(final VehicleMoveEvent event)
- [ ] void onPlayerDeath(final PlayerDeathEvent event)
- [ ] void onEntityDamage(final EntityDamageEvent event)
- [ ] void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
- [ ] void onBlockBreak(final BlockBreakEvent event)
- [ ] void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event)
- [ ] void onBlockPlace(final BlockPlaceEvent event)
- [ ] void onSignUse(final PlayerInteractEvent event)
- [ ] void onSignChange(final SignChangeEvent event)
- [ ] void onPlayerJoin(final PlayerJoinEvent event)
- [ ] void onPlayerLeave(final PlayerQuitEvent event)
- [ ] void onChat(final AsyncPlayerChatEvent event)
- [ ] void onPlayerCommandPreprocessEvent(final PlayerCommandPreprocessEvent event)
- [ ] void onPlayerTeleport(final PlayerTeleportEvent event)
- [ ] void onMobSpawn(CreatureSpawnEvent evt)
- [ ] void onMobTarget(EntityTargetEvent evt)
- [ ] boolean isSpectating(final Player p)
- [ ] List&lt;Entity> getEntitiesByLocation(final Location loc, final double d)
- [ ] boolean checkLocationMatchesSign(final Location l, final Sign s)
- [ ] void updateEntities(final List&lt;Player> players, final boolean visible)
- [ ] List&lt;Player> getPlayersWithin(final Player player, final int distance)
- [ ] String getName()
- [ ] void setName(final String minigame)

## ArenaLogger.java

- [X] logging methods
    > com.github.mce.minigames.api.arena.ArenaInterface.getLogger()

## ArenaMessageStrings.java

- [ ] String ARENA
- [ ] String ACTION
- [ ] String AUTHOR
- [ ] String DESCRIPTION
- [ ] String PLAYER
- [ ] String KILLER
- [ ] String COUNT
- [ ] String MAXCOUNT

## ArenaPermissionStrings.java

- [ ] String VIP
- [ ] String PREFIX

## ArenaPlayer.java

- [X] String playername
    > com.github.mce.minigames.api.player.ArenaPlayerInterface.getName()
- [ ] ItemStack[] inv
- [ ] ItemStack[] armor\_inv
- [ ] GameMode original\_gamemode
- [ ] int original\_xplvl
- [ ] boolean noreward
- [ ] Arena currentArena
- [ ] AClass currentClass
- [X] HashMap&lt;String, ArenaPlayer> players
    > no direct replacement, see com.github.mce.minigames.api.MglibInterface.getPlayer(OfflinePlayer)
- [X] ArenaPlayer getPlayerInstance(final String playername)
    > no direct replacement, see com.github.mce.minigames.api.MglibInterface.getPlayer(OfflinePlayer)
- [X] ArenaPlayer(final String playername)
    > no direct replacement, see com.github.mce.minigames.api.MglibInterface.getPlayer(OfflinePlayer)
- [X] Player getPlayer()
    > com.github.mce.minigames.api.player.ArenaPlayerInterface.getOfflinePlayer()
    > com.github.mce.minigames.api.player.ArenaPlayerInterface.getBukkitPlayer()
- [ ] void setInventories(final ItemStack[] inv, final ItemStack[] armor\_inv)
- [ ] ItemStack[] getInventory()
- [ ] ItemStack[] getArmorInventory()
- [ ] GameMode getOriginalGamemode()
- [ ] void setOriginalGamemode(final GameMode original\_gamemode)
- [ ] int getOriginalXplvl()
- [ ] void setOriginalXplvl(final int original\_xplvl)
- [ ] boolean isNoReward()
- [ ] void setNoReward(final boolean noreward)
- [ ] Arena getCurrentArena()
- [ ] void setCurrentArena(final Arena currentArena)
- [ ] AClass getCurrentClass()
- [ ] void setCurrentClass(final AClass currentClass)

## ArenaSetup.java

- [ ] void setSpawn(final JavaPlugin plugin, final String arenaname, final Location l)
- [ ] int autoSetSpawn(final JavaPlugin plugin, final String arenaname, final Location l)
- [ ] void setSpawn(final JavaPlugin plugin, final String arenaname, final Location l, final int count)
- [ ] boolean removeSpawn(final JavaPlugin plugin, final String arenaname, final int count)
- [ ] void setLobby(final JavaPlugin plugin, final String arenaname, final Location l)
- [ ] void setMainLobby(final JavaPlugin plugin, final Location l)
- [ ] void setBoundaries(final JavaPlugin plugin, final String arenaname, final Location l, final boolean low)
- [ ] void setBoundaries(final JavaPlugin plugin, final String arenaname, final Location l, final boolean low, final String extra\_component)
- [ ] Arena saveArena(final JavaPlugin plugin, final String arenaname)
- [ ] void setPlayerCount(final JavaPlugin plugin, final String arena, final int count, final boolean max)
- [ ] int getPlayerCount(final JavaPlugin plugin, final String arena, final boolean max)
- [ ] void setArenaVIP(final JavaPlugin plugin, final String arena, final boolean vip)
- [ ] boolean getArenaVIP(final JavaPlugin plugin, final String arena)
- [ ] void setArenaEnabled(final JavaPlugin plugin, final String arena, final boolean enabled)
- [ ] boolean getArenaEnabled(final JavaPlugin plugin, final String arena)
- [ ] void setShowScoreboard(final JavaPlugin plugin, final String arena, final boolean enabled)
- [ ] boolean getShowScoreboard(final JavaPlugin plugin, final String arena)

## ArenaState.java
    
- [ ] JOIN
- [ ] STARTING
- [ ] INGAME
- [ ] RESTARTING

## ArenaType.java

- [ ] DEFAULT
- [ ] JUMPNRUN
- [ ] REGENERATION

## ChannelStrings.java

- [ ] CHANNEL\_BUNGEE\_CORD
- [ ] SUBCHANNEL\_MINIGAMESLIB\_BACK
- [ ] SUBCHANNEL\_MINIGAMESLIB\_REQUEST
- [ ] SUBCHANNEL\_MINIGAMESLIB\_SIGN

## Classes.java

- [ ] public HashMap&lt;String, IconMenu> lasticonm = new HashMap&lt;>();
- [ ] Classes(final JavaPlugin plugin)
- [ ] public Classes(final PluginInstance pli, final JavaPlugin plugin)
- [ ] public void openGUI(final String p)
- [ ] void getClass(final String player)
- [ ] void setClass(String iname, final String player, final boolean money)
- [ ] String getInternalNameByName(final String name)
- [ ] AClass getClassByInternalname(final String internalname)
- [ ] boolean hasClass(final String player)
- [ ] String getSelectedClass(final String player)
- [ ] void loadClasses()
- [ ] void loadClasses(final JavaPlugin plugin)
- [ ] boolean kitRequiresMoney(final String kit)
- [ ] boolean kitTakeMoney(final Player p, final String kit)
- [ ] boolean kitPlayerHasPermission(final String kit, final Player p)

## CommandStrings.java

- [ ] String START
- [ ] String GAME\_START
- [ ] String GAME\_SET\_SPAWN
- [ ] String GAME\_SET\_SPEC\_SPAWN
- [ ] String GAME\_SET\_LOBBY
- [ ] String GAME\_SET\_MAINLOBBY
- [ ] String GAME\_SET\_BOUNDS
- [ ] String GAME\_SET\_LOBBY\_BOUNDS
- [ ] String GAME\_SET\_SPEC\_BOUNDS
- [ ] String GAME\_SAVE\_ARENA
- [ ] String GAME\_SAVE
- [ ] String GAME\_SET\_MAX\_PLAYERS
- [ ] String GAME\_SET\_MIN\_PLAYERS
- [ ] String GAME\_SET\_ARENA\_VIP
- [ ] String GAME\_SET\_VIP
- [ ] String GAME\_JOIN
- [ ] String GAME\_LEAVE
- [ ] String GAME\_STOP
- [ ] String GAME\_STOP\_ALL
- [ ] String GAME\_REMOVE\_ARENA
- [ ] String GAME\_REMOVE\_SPAWN
- [ ] String GAME\_SET\_SKULL
- [ ] String GAME\_SET\_ENABLED
- [ ] String GAME\_SET\_SHOW\_SCOREBOARD
- [ ] String GAME\_RESET
- [ ] String GAME\_SET\_AUTHOR
- [ ] String GAME\_SET\_DESCRIPTION
- [ ] String GAME\_SET\_DISPLAYNAME
- [ ] String GAME\_KIT
- [ ] String GAME\_SPECTATE
- [ ] String GAME\_SHOP
- [ ] String GAME\_LEADER\_BOARDS
- [ ] String GAME\_LB
- [ ] String GAME\_TOP
- [ ] String GAME\_STATS
- [ ] String GAME\_SET\_HOLOGRAM
- [ ] String GAME\_LIST\_HOLOGRAMS
- [ ] String GAME\_REMOVE\_HOLOGRAM
- [ ] String GAME\_HELP
- [ ] String GAME\_LIST
- [ ] String GAME\_RELOAD
- [ ] String GAME\_CREATE\_ARENA
- [ ] String GAME\_END\_ALL
- [ ] String PARTY
- [ ] String PARTY\_INVITE
- [ ] String PARTY\_ACCEPT
- [ ] String PARTY\_KICK
- [ ] String PARTY\_LIST
- [ ] String PARTY\_DISBAND
- [ ] String PARTY\_LEAVE
- [ ] String MAPI
- [ ] String MGAPI
- [ ] String MGLIB
- [ ] String MGLIB\_INFO
- [ ] String MGLIB\_DEBUG
- [ ] String MGLIB\_LIST
- [ ] String MGLIB\_TITLE
- [ ] String MGLIB\_SUBTITLE
- [ ] String MGLIB\_SIGNS
- [ ] String MGLIB\_HOLOGRAM
- [ ] String MGLIB\_STATS\_HOLOGRAM
- [ ] String MGLIB\_GAMEMODE\_TEST
- [ ] String MGLIB\_BUNGEE\_TEST
- [ ] String MGLIB\_JOIN

## Effects.java

- [ ] void playBloodEffect(final Player p)
- [ ] void playEffect(final Arena a, final Location l, final String effectname)
- [ ] BukkitTask playFakeBed(final Arena a, final Player p)
- [ ] BukkitTask playFakeBed(final Arena a, final Player p, final int x, final int y, final int z)
- [ ] void setValue(final Object instance, final String fieldName, final Object value) throws Exception
- [ ] void playRespawn(final Player p, final JavaPlugin plugin)
- [ ] void playTitle(final Player player, final String title, int eindex)
- [ ] HashMap&lt;Integer, Integer> effectlocd
- [ ] HashMap&lt;Integer, Integer> effectlocd\_taskid
- [ ] ArrayList&lt;Integer> playHologram(final Player p, final Location l, final String text, final boolean moveDown, final boolean removeAfterCooldown)
- [ ] void sendGameModeChange(final Player p, final int gamemode)

## Messages.java

- [ ] migrate nls messages

## MinecraftVersionsType.java

- [X] migrate version types
    > com.github.mce.minigames.api.MinecraftVersionsType

## MinigamesAPI.java

- [X] MinecraftVersionsType SERVER\_VERSION
    > com.github.mce.minigames.api.MglibInterface.getMinecraftVersion()
- [ ] Locale LOCALE
- [X] MinigamesAPI instance
    > com.github.mce.minigames.api.MglibInterface.INSTANCE.get()
- [ ] Economy econ
- [ ] boolean economy
- [ ] boolean crackshot
- [X] boolean debug
    > com.github.mce.minigames.api.MglibInterface.debug()
- [ ] int updatetime
- [ ] HashMap&lt;String, Party> global\_party
- [ ] HashMap&lt;String, ArrayList&lt;Party>> global\_party\_invites
- [ ] HashMap&lt;JavaPlugin, PluginInstance> pinstances
- [ ] PartyMessagesConfig partymessages
- [ ] StatsGlobalConfig statsglobal
- [ ] String internalServerVersion = "";
- [X] boolean below1710
    > no replacement, check com.github.mce.minigames.api.MglibInterface.getMinecraftVersion() against 1.7.10 
- [ ] Metrics metrics
- [ ] String motd
- [ ] Iterator motdStrings
- [ ] void onEnable()
- [ ] boolean crackshotAvailable()
- [ ] boolean economyAvailable()
- [ ] String getPermissionPrefix()
- [ ] String getPermissionKitPrefix()
- [ ] String getPermissionGunPrefix()
- [ ] String getPermissionShopPrefix()
- [ ] String getPermissionGamePrefix(String game)
- [X] MinecraftVersionsType getServerVersion()
    > com.github.mce.minigames.api.MglibInterface.getMinecraftVersion()
- [ ] void onDisable()
- [ ] MinigamesAPI setupAPI(final JavaPlugin plugin\_, final String minigame, final Class&lt;?> arenaclass, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig, final DefaultConfig defaultconfig, final boolean customlistener)
- [ ] void registerArenaListenerLater(final JavaPlugin plugin\_, final ArenaListener arenalistener)
- [ ] void registerArenaSetup(final JavaPlugin plugin\_, final ArenaSetup arenasetup)
- [ ] void registerScoreboard(final JavaPlugin plugin\_, final ArenaScoreboard board)
- [ ] MinigamesAPI setupAPI(final JavaPlugin plugin\_, final String minigame, final Class&lt;?> arenaclass)
- [ ] MinigamesAPI setupAPI(final JavaPlugin plugin\_, final String minigame)
- [ ] PluginInstance setupRaw(final JavaPlugin plugin\_, final String minigame)
- [X] MinigamesAPI getAPI()
    > com.github.mce.minigames.api.MglibInterface.INSTANCE.get()
- [ ] CommandHandler getCommandHandler()
- [ ] boolean setupEconomy()
- [ ] boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
- [ ] void connectToServer(JavaPlugin plugin, String player, String server)
- [ ] void onPluginMessageReceived(final String channel, final Player player, final byte[] message)
- [ ] PluginInstance getPluginInstance(final JavaPlugin plugin)
- [X] UUID playerToUUID(String playername)
    > no direct replacement, we do not use player names any more
    > com.github.mce.minigames.api.MglibInterface.getPlayer(Player)
    > com.github.mce.minigames.api.player.ArenaPlayerInterface.getPlayerUUID()
- [X] UUID playerToUUID(Player player)
    > no direct replacement, we do not use player names any more
    > com.github.mce.minigames.api.MglibInterface.getPlayer(Player)
    > com.github.mce.minigames.api.player.ArenaPlayerInterface.getPlayerUUID()
- [X] Player uuidToPlayer(UUID uuid)
    > com.github.mce.minigames.api.MglibInterface.getPlayer(UUID)
- [ ] onBreak(BlockBreakEvent event)
- [ ] onSignUse(PlayerInteractEvent event)
- [ ] onSignChange(SignChangeEvent event)
- [ ] requestServerSign(String mg\_key, String arena\_key)
- [ ] getSignFromArena(String mg, String arena)
- [ ] void updateSign(String mg, String arenaname, String arenastate, int count, int maxcount)
- [ ] void sendSignUpdate(final PluginInstance pli, final Arena a)
- [ ] void updateSign(String mg, String arenaname, String arenastate, SignChangeEvent event)
- [ ] String getServerBySignLocation(Location sign)
- [ ] String getInfoBySignLocation(Location sign)
- [ ] void onServerPing(ServerListPingEvent evt)

## Party.java

- [ ] String            owner;
- [ ] ArrayList&lt;String> players = new ArrayList&lt;>();
- [ ] Party(final String owner)
- [ ] String getOwner()
- [ ] ArrayList&lt;String> getPlayers()
- [ ] void addPlayer(final String p)
- [ ] boolean removePlayer(final String p)
- [ ] containsPlayer(final String p)
- [ ] disband()
- [ ] tellAll(final String msg)

## PermissionStrings.java

- [ ] String MINIGAMES\_PARTY
- [ ] String MINIGAMES\_START

## PluginConfigStrings.java

- [ ] String DEBUG
- [ ] String PARTY\_COMMAND\_ENABLED
- [ ] String SIGNS\_UPDATE\_TIME
- [ ] String AUTO\_UPDATING
- [ ] String POST\_METRICS
- [ ] String PERMISSION\_PREFIX
- [ ] String PERMISSION\_KITS\_PREFIX
- [ ] String PERMISSION\_GUN\_PREFIX
- [ ] String PERMISSION\_SHOP\_PREFIX
- [ ] String PERMISSION\_GAME\_PREFIX
- [ ] String MOTD\_ENABLED
- [ ] String MOTD\_ROTATION\_SECONDS
- [ ] String MOTD\_TEXT\_SECONDS
- [ ] String MOTD\_STATE\_JOIN
- [ ] String MOTD\_STATE\_STARTING
- [ ] String MOTD\_STATE\_INGAME
- [ ] String MOTD\_STATE\_RESETTING
- [ ] String MOTD\_STATE\_DISABLED

## PluginInstance.java

- [ ] HashMap&lt;String, Arena> global\_players
- [ ] HashMap&lt;String, Arena> global\_lost
- [ ] HashMap&lt;String, Arena> global\_arcade\_spectator
- [ ] ArenaListener arenalistener
- [ ] ArenasConfig arenasconfig
- [ ] ClassesConfig classesconfig
- [ ] MessagesConfig messagesconfig
- [ ] StatsConfig statsconfig
- [ ] GunsConfig gunsconfig
- [ ] AchievementsConfig achievementsconfig
- [ ] ShopConfig shopconfig
- [ ] HologramsConfig hologramsconfig
- [ ] JavaPlugin plugin
- [ ] ArrayList&lt;Arena> arenas
- [ ] HashMap&lt;String, AClass> pclass
- [ ] LinkedHashMap&lt;String, AClass> aclasses
- [ ] HashMap&lt;String, Gun> guns
- [ ] Rewards rew
- [ ] MainSQL sql
- [ ] Stats stats
- [ ] Classes classes
- [ ] Shop shop
- [ ] SpectatorManager spectatormanager
- [ ] ArenaAchievements achievements
- [ ] Holograms holograms
- [ ] boolean achievement\_gui\_enabled
- [ ] ArenaScoreboard scoreboardManager
- [ ] ArenaLobbyScoreboard scoreboardLobbyManager
- [ ] ArenaSetup arenaSetup
- [ ] int lobby\_countdown
- [ ] int ingame\_countdown
- [ ] boolean spectator\_move\_y\_lock
- [ ] boolean use\_xp\_bar\_level
- [ ] boolean blood\_effects
- [ ] boolean dead\_in\_fake\_bed\_effects
- [ ] boolean spectator\_mode\_1\_8
- [ ] boolean damage\_identifier\_effects
- [ ] boolean color\_background\_wool\_of\_signs;
- [ ] boolean last\_man\_standing
- [ ] boolean old\_reset
- [ ] boolean show\_classes\_without\_usage\_permission
- [ ] boolean chat\_enabled
- [ ] HashMap&lt;String, ArrayList&lt;String>> cached\_sign\_states
- [ ] PluginInstance(final JavaPlugin plugin, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig, final ArrayList&lt;Arena> arenas)
- [ ] PluginInstance(final JavaPlugin plugin, final ArenasConfig arenasconfig, final MessagesConfig messagesconfig, final ClassesConfig classesconfig, final StatsConfig statsconfig)
- [ ] void reloadVariables()
- [X] JavaPlugin getPlugin()
    > com.github.mce.minigames.api.PluginProviderInterface.getJavaPlugin()
- [ ] HashMap&lt;String, AClass> getAClasses()
- [ ] HashMap&lt;String, AClass> getPClasses()
- [ ] addAClass(final String name, final AClass a)
- [ ] setPClass(final String player, final AClass a)
- [ ] HashMap&lt;String, Gun> getAllGuns()
- [ ] void addGun(final String name, final Gun g)
- [ ] ArenasConfig getArenasConfig()
- [ ] MessagesConfig getMessagesConfig()
- [ ] ClassesConfig getClassesConfig()
- [ ] StatsConfig getStatsConfig()
- [ ] GunsConfig getGunsConfig()
- [ ] AchievementsConfig getAchievementsConfig()
- [ ] ShopConfig getShopConfig()
- [ ] void setShopConfig(final ShopConfig shopconfig)
- [ ] HologramsConfig getHologramsConfig()
- [ ] Rewards getRewardsInstance()
- [ ] void setRewardsInstance(final Rewards r)
- [ ] MainSQL getSQLInstance()
- [ ] Stats getStatsInstance()
- [ ] ArenaListener getArenaListener()
- [ ] void setArenaListener(final ArenaListener al)
- [ ] Classes getClassesHandler()
- [ ] void setClassesHandler(final Classes c)
- [ ] Shop getShopHandler()
- [ ] SpectatorManager getSpectatorManager()
- [ ] void setSpectatorManager(final SpectatorManager s)
- [ ] ArenaAchievements getArenaAchievements()
- [ ] Holograms getHologramsHandler()
- [ ] int getIngameCountdown()
- [ ] int getLobbyCountdown()
- [ ] ArrayList&lt;Arena> getArenas()
- [ ] void clearArenas()
- [ ] ArrayList&lt;Arena> addArena(final Arena arena)
- [ ] Arena getArenaByName(final String arenaname)
- [ ] Arena removeArenaByName(final String arenaname)
- [ ] boolean removeArena(final Arena arena)
- [ ] void addLoadedArenas(final ArrayList&lt;Arena> arenas)
- [ ] void addArenas(final Iterable&lt;Arena> arenaList)
- [ ] void addArenas(final Arena... arenaList)
- [ ] void setArenas(final Iterable&lt;Arena> arenaList)
- [ ] void setArenas(final Arena... arenaList)
- [ ] boolean isAchievementGuiEnabled()
- [ ] void setAchievementGuiEnabled(final boolean achievement\_gui\_enabled)
- [ ] void reloadAllArenas()
- [ ] void reloadArena(final String arenaname)
- [ ] boolean containsGlobalPlayer(final String playername)
- [ ] boolean containsGlobalLost(final String playername)
- [ ] Arena getArenaByGlobalPlayer(final String playername)

## PrivateUtil.java

- [ ] void loadArenaFromFileSYNC(final JavaPlugin plugin, final Arena arena)

## Rewards.java

- [ ] JavaPlugin plugin
- [ ] boolean     economyrewards
- [ ] boolean     itemrewards
- [ ] boolean     commandrewards
- [ ] boolean     kill_economyrewards
- [ ] boolean     kill_commandrewards
- [ ] boolean     participation_economyrewards
- [ ] boolean     participation_commandrewards
- [ ] int         econ_reward
- [ ] int         kill_econ_reward
- [ ] int         participation_econ_reward
- [ ] String      command
- [ ] String      kill_command
- [ ] String      participation_command
- [ ] ItemStack[] items
- [ ] Rewards(final JavaPlugin plugin)
- [ ] void reloadVariables()
- [ ] void giveRewardsToWinners(final Arena arena)
- [ ] void giveReward(final String p\_)
- [ ] void giveKillReward(final String p\_)
- [ ] void giveKillReward(final String p\_, final int reward)
- [ ] void giveAchievementReward(final String p\_, final boolean econ, final boolean isCommand, final int money\_reward, final String cmd)
- [ ] void giveWinReward(final String p\_, final Arena a)
- [ ] void giveWinReward(final String p\_, final Arena a, final int global\_multiplier)
- [ ] void giveWinReward(final String p\_, final Arena a, final ArrayList&lt;String> players, final int global\_multiplier)

## Shop.java

- [ ] JavaPlugin                             plugin
- [ ] PluginInstance                         pli
- [ ] HashMap&lt;String, IconMenu>       lasticonm
- [ ] LinkedHashMap&lt;String, ShopItem> shopitems
- [ ] Shop(final PluginInstance pli, final JavaPlugin plugin)
- [ ] void openGUI(final String p)
- [ ] void loadShopItems()
- [ ] boolean buy(final Player p, final String item_displayname)
- [ ] boolean buyByInternalName(final Player p, final String item\_name)
- [ ] boolean hasItemBought(final String p, final String item)
- [ ] boolean requiresMoney(final String item)
- [ ] boolean takeMoney(final Player p, final String item)
- [ ] void giveShopItems(final Player p)

## SmartReset.java

- [ ] SmartBlockMap              changed
- [ ] Arena                            a
- [ ] ArrayList&lt;SmartArenaBlock> failedblocks
- [ ] long                             time
- [ ] SmartReset(final Arena a)
- [ ] SmartArenaBlock addChanged(final Block b)
- [ ] void addChanged(Block[] loc)
- [ ] SmartArenaBlock addChanged(Block b, BlockState blockReplacedState)
- [ ] SmartArenaBlock addChanged(final Block b, final boolean isChest)
- [ ] SmartArenaBlock addChanged(final Block b, final boolean isChest, final ChangeCause cause)
- [ ] void addChanged(final Location l)
- [ ] SmartArenaBlock addChanged(final Location l, final Material m, final byte data)
- [ ] void run()
- [ ] void reset()
- [ ] void resetRaw()
- [ ] void resetSmartResetBlock(final SmartArenaBlock ablock)
- [ ] void saveSmartBlocksToFile()
- [ ] void loadSmartBlocksFromFile()
- [ ] final class SmartBlockMap extends TreeMap&lt;Integer, Map&lt;Location, SmartArenaBlock>>
- [ ] void putBlock(Location l, SmartArenaBlock block)
- [ ] boolean hasBlock(Location l)
- [ ] Iterable&lt;SmartArenaBlock> getBlocks()
- [ ] NestedIterator&lt;K, T>

## SpectatorManager.java

- [ ] JavaPlugin                              plugin
- [ ] HashMap&lt;String, IconMenu> lasticonm
- [ ] SpectatorManager(final JavaPlugin plugin)
- [ ] void setup()
- [ ] void setSpectate(final Player p, final boolean spectate)
- [ ] boolean isSpectating(final Player p)
- [ ] void clear()
- [ ] void openSpectatorGUI(final Player p, final Arena a)
- [ ] HashMap&lt;String, ArrayList&lt;String>> pspecs
- [ ] HashMap&lt;String, ArrayList&lt;String>> splayers
- [ ] void hideSpectator(final Player spec, final ArrayList&lt;String> players)
- [ ] void showSpectator(final Player spec)
- [ ] void showSpectators(final Player p)

## Stats.java

- [ ] JavaPlugin       plugin
- [ ] PluginInstance           pli
- [ ] ArrayList&lt;String> skullsetup
- [ ] int                      stats\_kill\_points
- [ ] int                      stats\_win\_points
- [ ] Stats(final PluginInstance pli, final JavaPlugin plugin)
- [ ] void reloadVariables()
- [ ] void win(final String playername, final int count)
- [ ] void lose(final String playername)
- [ ] void update(final String playername)
- [ ] void updateSQLKillsDeathsAfter(final Player p, final Arena a)
- [ ] void setWins(final String playername, final int count)
- [ ] void setPoints(final String playername, final int count)
- [ ] void addWin(final String playername)
- [ ] void addLose(final String playername)
- [ ] void addKill(final String playername)
- [ ] void addDeath(final String playername)
- [ ] void addPoints(final String playername, final int count)
- [ ] int getPoints(final String playername)
- [ ] int getWins(final String playername)
- [ ] int getLoses(final String playername)
- [ ] int getKills(final String playername)
- [ ] int getDeaths(final String playername)
- [ ] TreeMap&lt;String, Double> getTop(final int count, final boolean wins)
- [ ] TreeMap&lt;String, Double> getTop()
- [ ] ItemStack giveSkull(final String name)
- [ ] void saveSkull(final Location t, final int count)
- [ ] void updateSkulls()

## AAchievement.java

- [ ] String  name
- [ ] boolean done
- [ ] String  playername
- [ ] AAchievement(final String name, final String playername, final boolean done)
- [ ] boolean isDone()
- [ ] setDone(final boolean t)
- [ ] getAchievementNameRaw()

## ArenaAchievements.java

- [ ] JavaPlugin                       plugin
- [ ] PluginInstance                   pli
- [ ] HashMap&lt;String, IconMenu> lasticonm
- [ ] ArenaAchievements(final PluginInstance pli, final JavaPlugin plugin)
- [ ] void openGUI(final String p, final boolean sql)
- [ ] ArrayList&lt;AAchievement> loadPlayerAchievements(final String playername, final boolean sql)
- [ ] void setAchievementDone(final String playername, final String achievement, final boolean sql)
- [ ] void addDefaultAchievement(final String internalname, final String name, final int default\_money\_reward)
- [ ] isEnabled()
- [ ] setEnabled(final boolean t)

## ArcadeInstance.java

- [ ] ArrayList&lt;PluginInstance> minigames
- [ ] int                              currentindex
- [ ] ArrayList&lt;String>         players
- [ ] Arena                            arena
- [ ] JavaPlugin                       plugin
- [ ] boolean                          in\_a\_game
- [ ] Arena                            currentarena
- [ ] boolean                          started
- [ ] ArcadeInstance(final JavaPlugin plugin, final ArrayList&lt;PluginInstance> minigames, final Arena arena)
- [ ] void joinArcade(final String playername)
- [ ] void leaveArcade(final String playername)
- [ ] void leaveArcade(final String playername, final boolean endOfGame)
- [ ] int currentlobbycount
- [ ] int currenttaskid
- [ ] void startArcade()
- [ ] void stopArcade(final boolean stopOfGame)
- [ ] void stopArcade()
- [ ] void stopCurrentMinigame()
- [ ] void nextMinigame()
- [ ] void nextMinigame(final long delay)
- [ ] void clean()

## CommandHandler.java

- [ ] boolean handleArgs(final JavaPlugin plugin, final String uber_permission, final String cmd, final CommandSender sender, final String args[])
- [ ] LinkedHashMap&lt;String, String> cmddesc
- [ ] void sendHelp(final String cmd, final CommandSender sender)
- [ ] LinkedHashMap&lt;String, String> cmdpartydesc
- [ ] void sendPartyHelp(final String cmd, final CommandSender sender)
- [ ] boolean setSpawn(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setSpecSpawn(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setLobby(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setMainLobby(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setBounds(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setLobbyBounds(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setSpecBounds(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean saveArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setMaxPlayers(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setMinPlayers(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setArenaVIP(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean joinArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean leaveArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean startArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean stopArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean stopAllArenas(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean removeArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean removeSpawn(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setSkull(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setEnabled(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setShowScoreboard(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean resetArena(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setAuthor(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setDescription(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setArenaDisplayName(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean spectate(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean setKit(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, Player player)
- [ ] boolean openShop(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean getStats(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean getLeaderboards(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] void sendLeaderboards(final PluginInstance pli, final CommandSender sender, final int count, final boolean wins)
- [ ] boolean setHologram(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean listHolograms(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean removeHologram(final PluginInstance pli, final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean partyInvite(final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean partyAccept(final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean partyKick(final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean partyList(final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean partyDisband(final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)
- [ ] boolean partyLeave(final CommandSender sender, final String[] args, final String uber\_permission, final String cmd, final String action, final JavaPlugin plugin, final Player p)

## AchievementsConfig.java

- [ ] migrate

## ArenasConfig.java

- [ ] migrate

## ClassesConfig.java

- [ ] migrate

## DefaultConfig.java

- [ ] migrate

## GunsConfig.java

- [ ] migrate

## HologramsConfig.java

- [ ] migrate

## MessagesConfig.java

- [ ] migrate
- [ ] String no\_perm
- [ ] String successfully\_reloaded
- [ ] String successfully\_set
- [ ] String successfully\_saved\_arena
- [ ] String failed\_saving\_arena
- [ ] String failed\_removing\_arena
- [ ] String arena\_invalid
- [ ] String broadcast\_players\_left
- [ ] String broadcast\_player\_joined
- [ ] String player\_died
- [ ] String arena\_action
- [ ] String you\_joined\_arena
- [ ] String you\_already\_are\_in\_arena
- [ ] String arena\_not\_initialized
- [ ] String not\_in\_arena
- [ ] String teleporting\_to\_arena\_in
- [ ] String starting\_in
- [ ] String successfully\_removed
- [ ] String failed\_removing\_component
- [ ] String joined\_arena
- [ ] String you\_won
- [ ] String you\_lost
- [ ] String you\_got\_a\_kill
- [ ] String player\_was\_killed\_by
- [ ] String attributelevel\_increased
- [ ] String not\_enough\_credits
- [ ] String too\_many\_main\_guns
- [ ] String successfully\_set\_main\_gun
- [ ] String arcade\_next\_minigame
- [ ] String arena\_disabled
- [ ] String all\_guns
- [ ] String you\_can\_leave\_with
- [ ] String arcade\_joined\_spectator
- [ ] String arcade\_joined\_waiting
- [ ] String arcade\_new\_round
- [ ] String no\_perm\_to\_join\_arena
- [ ] String set\_kit
- [ ] String classes\_item
- [ ] String achievement\_item
- [ ] String shop\_item
- [ ] String spectator\_item
- [ ] String server\_broadcast\_winner
- [ ] String exit\_item
- [ ] String successfully\_bought\_kit
- [ ] String scoreboard\_title
- [ ] String scoreboard\_lobby\_title
- [ ] String you\_got\_kicked\_because\_vip\_joined
- [ ] String powerup\_spawned
- [ ] String you\_got\_the\_achievement
- [ ] String game\_started
- [ ] String author\_of\_the\_map
- [ ] String description\_of\_the\_map
- [ ] String not\_enough\_money
- [ ] String possible\_kits
- [ ] String possible\_shopitems
- [ ] String cancelled\_starting
- [ ] String minigame\_description
- [ ] String successfully\_bought\_shopitem
- [ ] String already\_bought\_shopitem
- [ ] String you\_received\_rewards
- [ ] String you\_received\_rewards\_2
- [ ] String you\_received\_rewards\_3
- [ ] String already\_in\_arena
- [ ] String stop\_cause\_maximum\_game\_time
- [ ] String compass\_no\_player\_found
- [ ] String compass\_player\_found
- [ ] String you\_got\_a\_participation\_reward
- [ ] String kit\_warning

## PartyMessagesConfig.java

- [ ] migrate
- [ ] String cannot\_invite\_yourself
- [ ] String player\_not\_online
- [ ] String you\_invited
- [ ] String you\_were\_invited
- [ ] String not\_invited\_to\_any\_party
- [ ] String not\_invited\_to\_players\_party
- [ ] String player\_not\_in\_party
- [ ] String you\_joined\_party
- [ ] String player\_joined\_party
- [ ] String you\_left\_party
- [ ] String player\_left\_party
- [ ] String party\_disbanded
- [ ] String party\_too\_big\_to\_join

## ShopConfig.java

- [ ] migrate

## StatsConfig.java

- [ ] migrate

## StatsGlobalConfig.java

- [ ] migrate

## ArenaEvent.java

- [ ] migrate

## ArenaStartedEvent.java

- [ ] migrate

## ArenaStartEvent.java

- [ ] migrate

## ArenaStopEvent.java

- [ ] migrate

## PlayerEvent.java

- [ ] migrate

## PlayerJoinLobbyEvent.java

- [ ] migrate

## PlayerLeaveArenaEvent.java

- [ ] migrate

## Gun.java

- [ ] double                      speed
- [ ] int                         shoot\_amount
- [ ] int                         max\_durability
- [ ] int                         durability
- [ ] Class&lt;? extends Projectile> bullet
- [ ] JavaPlugin                  plugin
- [ ] double                      knockback\_multiplier
- [ ] String                      name
- [ ] boolean                            canshoot
- [ ] HashMap&lt;String, Boolean>    canshoot\_
- [ ] ArrayList&lt;ItemStack>               items
- [ ] ArrayList&lt;ItemStack>               icon
- [ ] Gun(final JavaPlugin plugin, final String name, final double speed, final int shoot\_amount, final int durability, final double knockback\_multiplier, final Class&lt;? extends Projectile> bullet, final ArrayList&lt;ItemStack> items, final ArrayList&lt;ItemStack> icon)
- [ ] Gun(final JavaPlugin plugin, final ArrayList&lt;ItemStack> items, final ArrayList&lt;ItemStack> icon)
- [ ] void shoot(final Player p)
- [ ] void shoot(final Player p, final int shoot\_amount, final int durability, final int speed)
- [ ] void onHit(final Entity ent, final int knockback\_multiplier)
- [ ] void reloadGun()

## Guns.java


- [ ] double                               level\_multiplier
- [ ] int                                  speed\_cost
- [ ] int                                  durability\_cost
- [ ] int                                  shoot\_amount\_cost
- [ ] int                                  knockback\_multiplier\_cost
- [ ] HashMap&lt;String, IconMenu>            lastmainiconm
- [ ] HashMap&lt;String, IconMenu>            lastmainediticonm
- [ ] HashMap&lt;String, IconMenu>            lastupgradeiconm
- [ ] HashMap&lt;String, HashMap&lt;Gun, int[]>> pgunattributes
- [ ] JavaPlugin                           plugin
- [ ] Guns(final JavaPlugin plugin)
- [ ] void openGUI(final String p)
- [ ] int[] getPlayerGunAttributeLevels(final JavaPlugin plugin, final String p, final Gun g)
- [ ] void setPlayerGunLevel(final JavaPlugin plugin, final String p, final String g, final String attribute, final int level, final double cost)
- [ ] void setPlayerGunMain(final JavaPlugin plugin, final String p, final String g, final boolean val)
- [ ] int getPlayerAllMainGunsCount(final JavaPlugin plugin, final String p)
- [ ] void openGunMainEditGUI(final String p, final String g)
- [ ] void openUpgradeGUI(final String p, final String g, final String attribute, final int level, final double cost)
- [ ] void loadGuns(final JavaPlugin plugin)
- [ ] ArrayList&lt;String> getAllMainGuns(final Player p)
- [ ] void giveMainGuns(final Player p)

## Database.java

- [ ] migrate

## MainSQL.java

- [ ] migrate

## MySQL.java

- [ ] migrate

## SQLite.java

- [ ] migrate

## Hologram.java

- [ ] migrate

## Holograms.java

- [ ] PluginInstance              pli
- [ ] HashMap&lt;Location, Hologram> holo = new HashMap&lt;>()
- [ ] Holograms(final PluginInstance pli)
- [ ] void loadHolograms()
- [ ] void sendAllHolograms(final Player p)
- [ ] void addHologram(final Location l)
- [ ] boolean removeHologram(final Location ploc)
- [ ] void destroyHologram(final Player p, final Hologram h)
- [ ] int[] convertIntegers(final ArrayList&lt;Integer> integers)

## AClass.java

- [ ] JavaPlugin     plugin
- [ ] String         name
- [ ] String         internalname
- [ ] ArrayList&lt;ItemStack> items
- [ ] ItemStack      icon
- [ ] boolean              enabled
- [ ] AClass(final JavaPlugin plugin, final String name, final ArrayList&lt;ItemStack> items)
- [ ] AClass(final JavaPlugin plugin, final String name, final String internalname, final ArrayList&lt;ItemStack> items)
- [ ] AClass(final JavaPlugin plugin, final String name, final String internalname, final boolean enabled, final ArrayList&lt;ItemStack> items)
- [ ] AClass(final JavaPlugin plugin, final String name, final String internalname, final boolean enabled, final ArrayList&lt;ItemStack> items, final ItemStack icon)
- [ ] ItemStack[] getItems()
- [ ] ItemStack getIcon()
- [ ] String getName()
- [ ] String getInternalName()
- [ ] boolean isEnabled()

## ArenaBlock.java

- [ ] int           x, y, z
- [ ] String        world
- [ ] Material      m
- [ ] byte                data
- [ ] ArrayList&lt;Material> item_mats
- [ ] ArrayList&lt;Byte>     item_data
- [ ] ArrayList&lt;Integer>  item_amounts
- [ ] ArrayList&lt;String>   item_displaynames
- [ ] ArrayList&lt;Boolean>  item_splash
- [ ] ItemStack[]         inv
- [ ] ArenaBlock(final Block b, final boolean c)
- [ ] ArenaBlock(final Location l)
- [ ] Block getBlock()
- [ ] Material getMaterial()
- [ ] Byte getData()
- [ ] ItemStack[] getInventory()
- [ ] ArrayList&lt;ItemStack> getNewInventory()
- [ ] ItemStack getEnchantmentBook(final Map&lt;Enchantment, Integer> t)

## ArenaLobbyScoreboard.java

- [ ] HashMap&lt;String, Scoreboard> ascore
- [ ] HashMap&lt;String, Objective>  aobjective
- [ ] int                         initialized
- [ ] boolean                     custom
- [ ] PluginInstance              pli
- [ ] ArrayList&lt;String>           loaded\_custom\_strings
- [ ] ArenaLobbyScoreboard(final PluginInstance pli, final JavaPlugin plugin)
- [ ] void updateScoreboard(final JavaPlugin plugin, final Arena arena)
- [ ] void removeScoreboard(final String arena, final Player p)
- [ ] void clearScoreboard(final String arenaname)

## ArenaScoreboard.java

- [ ] HashMap&lt;String, Scoreboard> ascore
- [ ] HashMap&lt;String, Objective>  aobjective
- [ ] HashMap&lt;String, Integer>    currentscore
- [ ] int                         initialized
- [ ] boolean                     custom
- [ ] PluginInstance              pli
- [ ] ArrayList&lt;String>           loaded\_custom\_strings
- [ ] ArenaScoreboard()
- [ ] ArenaScoreboard(final PluginInstance pli, final JavaPlugin plugin)
- [ ] void updateScoreboard(final JavaPlugin plugin, final Arena arena)
- [ ] void removeScoreboard(final String arena, final Player p)
- [ ] void clearScoreboard(final String arenaname)
- [ ] void setCurrentScoreMap(final HashMap&lt;String, Integer> newcurrentscore)

## BungeeUtil.java

- [ ] void connectToServer(final JavaPlugin plugin, final String player, final String server)
- [ ] void sendSignUpdateRequest(final JavaPlugin plugin, final String minigame, final Arena arena)

## ChangeCause.java

- [ ] migrate

## Cuboid.java

- [ ] Location highPoints
- [ ] Location lowPoints
- [ ] Cuboid(final Location startLoc, final Location endLoc)
- [ ] boolean isAreaWithinArea(final Cuboid area)
- [ ] boolean containsLoc(final Location loc)
- [ ] boolean containsLocWithoutY(final Location loc)
- [ ] boolean containsLocWithoutYD(final Location loc)
- [ ] long getSize()
- [ ] Location getRandomLocation()
- [ ] Location getRandomLocationForMobs()
- [ ] int getXSize()
- [ ] int getYSize()
- [ ] int getZSize()
- [ ] Location getHighLoc()
- [ ] Location getLowLoc()
- [ ] World getWorld()
- [ ] Map&lt;String, Object> save()
- [ ] Cuboid load(final Map&lt;String, Object> root) throws IllegalArgumentException
- [ ] String toString()
- [ ] String toRaw()

## IconMenu.java

- [ ] String            name
- [ ] int               size
- [ ] OptionClickEventHandler handler
- [ ] Plugin                  plugin
- [ ] Player                  player
- [ ] String[]                optionNames
- [ ] ItemStack[]             optionIcons
- [ ] IconMenu(final String name, final int size, final OptionClickEventHandler handler, final Plugin plugin)
- [ ] IconMenu setOption(int pos, final ItemStack icon, final String name, final String... info)
- [ ] void setSpecificTo(final Player player)
- [ ] boolean isSpecific()
- [ ] int getSize()
- [ ] void open(final Player player)
- [ ] void destroy()
- [ ] void clear()
- [ ] onInventoryClick(final InventoryClickEvent event)
- [ ] interface OptionClickEventHandler
- [ ] class OptionClickEvent
- [ ] ItemStack setItemNameAndLore(final ItemStack item, final String name, final String[] lore)


## InventoryManager.java

- [ ] HashMap&lt;String, ItemStack[]> armourContents
- [ ] HashMap&lt;String, ItemStack[]> inventoryContents
- [ ] HashMap&lt;String, Location>    locations
- [ ] HashMap&lt;String, Integer>     xplevel
- [ ] HashMap&lt;String, GameMode>    gamemode
- [ ] void saveInventory(final Player player)
- [ ] void restoreInventory(final Player player)

## Leaderboard.java

- [ ] Object convertUUID(final String s)
- [ ] Map&lt;Integer, String> sortByComparator(final Map&lt;String, Integer> unsortMap, final boolean order)
- [ ] Leaderboard(final Location firstPlace, final Location secondPlace, final Location thirdPlace, final Material firstBlockType, final Material secondBlockType, final Material thirdBlockType, final BlockFace direction, final FileConfiguration configurationFile, final String configurationFormat)

## Metrics.java

- [ ] migrate

## ParticleEffectNew.java

- [ ] migrate

## ShopItem.java

- [ ] migrate

## Signs.java

- [ ] migrate

## SmartArenaBlock.java

- [ ] migrate

## UpdaterBukkit.java

- [ ] migrate

## UpdaterNexus.java

- [ ] migrate

## Util.java

- [ ] HashMap&lt;String, ItemStack[]> armourContents
- [ ] HashMap&lt;String, ItemStack[]> inventoryContents
- [ ] HashMap&lt;String, Location>    locations
- [ ] HashMap&lt;String, Integer>     xplevel
- [ ] HashMap&lt;String, GameMode>    gamemode
- [ ] void clearInv(final Player p)
- [ ] void teleportPlayerFixed(final Player p, final Location l)
- [ ] void teleportAllPlayers(final ArrayList&lt;String> players, final Location l)
- [ ] HashMap&lt;String, Location> teleportAllPlayers(final ArrayList&lt;String> players, final ArrayList&lt;Location> locs)
- [ ] Location getComponentForArena(final JavaPlugin plugin, final String arenaname, final String component, final String count)
- [ ] Location getComponentForArena(final JavaPlugin plugin, final String arenaname, final String component)
- [ ] Location getComponentForArenaRaw(final JavaPlugin plugin, final String arenaname, final String component)
- [ ] boolean isComponentForArenaValid(final JavaPlugin plugin, final String arenaname, final String component)
- [ ] boolean isComponentForArenaValidRaw(final JavaPlugin plugin, final String arenaname, final String component)
- [ ] void saveComponentForArena(final JavaPlugin plugin, final String arenaname, final String component, final Location comploc)
- [ ] void saveMainLobby(final JavaPlugin plugin, final Location comploc)
- [ ] Location getMainLobby(final JavaPlugin plugin)
- [ ] ArrayList&lt;Location> getAllSpawns(final JavaPlugin plugin, final String arena)
- [ ] saveArenaToFile(final JavaPlugin plugin, final String arena)
- [ ] Sign getSignFromArena(final JavaPlugin plugin, final String arena)
- [ ] Location getSignLocationFromArena(final JavaPlugin plugin, final String arena)
- [ ] Arena getArenaBySignLocation(final JavaPlugin plugin, final Location sign)
- [ ] void updateSign(final JavaPlugin plugin, final Arena arena)
- [ ] void updateSign(final JavaPlugin plugin, final Arena arena, final SignChangeEvent event)
- [ ] void updateSign(final JavaPlugin plugin, final SignChangeEvent event, final String arenastate)
- [ ] ArrayList&lt;Arena> loadArenas(final JavaPlugin plugin, final ArenasConfig cf)
- [ ] Arena initArena(final JavaPlugin plugin, final String arena)
- [ ] boolean isNumeric(final String s)
- [ ] ArrayList&lt;ItemStack> parseItems(final String rawitems)
- [ ] void giveLobbyItems(final JavaPlugin plugin, final Player p)
- [ ] void giveSpectatorItems(final JavaPlugin plugin, final Player p)
- [ ] void sendMessage(final Player p, final String arenaname, final String msgraw)
- [ ] void sendMessage(final JavaPlugin plugin, final Player p, final String msgraw)
- [ ] ItemStack getCustomHead(final String name)
- [ ] void spawnPowerup(final JavaPlugin plugin, final Arena a, final Location l, final ItemStack item)
- [ ] Random r
- [ ] void spawnFirework(final Player p)
- [ ] void spawnFirework(final Location l)
- [ ] Color hexToRgb(final String colorStr)
- [ ] class ValueComparator
- [ ] class CompassPlayer
- [ ] CompassPlayer getNearestPlayer(final Player p, final Arena a)
- [ ] void sendStatsMessage(final PluginInstance pli, final Player p)
- [ ] void pushBack(final Location l, final Player p)
- [ ] Score getScore(final Objective obj, final String text)
- [ ] void resetScores(final Scoreboard obj, final String text)
- [ ] void saveInventory(final Player player)
- [ ] void restoreInventory(final Player player)

## Validator.java

- [ ] boolean isPlayerOnline(final String player)
- [ ] boolean isPlayerValid(final JavaPlugin plugin, final String player, final Arena arena)
- [ ] boolean isPlayerValid(final JavaPlugin plugin, final String player, final String arena)
- [ ] boolean isArenaValid(final JavaPlugin plugin, final Arena arena)
- [ ] boolean isArenaValid(final JavaPlugin plugin, final String arena)
- [ ] boolean isArenaValid(final JavaPlugin plugin, final String arena, final FileConfiguration cf)
