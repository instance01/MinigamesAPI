# Minigames-Lib 2.0 - MigrationList

## Arena.java

- [ ] ArcadeInstance ai
- [ ] boolean isArcadeMain
- [ ] boolean isSuccessfullyInitialized
- [ ] ArrayList<Location> spawns
- [ ] HashMap<String, Location> pspawnloc
- [ ] HashMap<String, String> lastdamager
- [ ] HashMap<String, Integer> temp_kill_count
- [ ] HashMap<String, Integer> temp_death_count
- [ ] Location mainlobby
- [ ] Location waitinglobby
- [ ] Location specspawn
- [ ] Location signloc
- [ ] int max_players
- [ ] int min_players
- [ ] boolean viparena
- [ ] ArrayList<String> players
- [ ] ArrayList<String> temp_players
- [ ] ArenaType type
- [ ] ArenaState currentstate
- [ ] String name
- [ ] String displayname
- [ ] boolean started
- [ ] boolean startedIngameCountdown
- [ ] boolean showArenascoreboard
- [ ] boolean alwaysPvP
- [ ] SmartReset sr
- [ ] Cuboid boundaries
- [ ] Cuboid lobby_boundaries
- [ ] Cuboid spec_boundaries
- [ ] boolean temp_countdown
- [ ] boolean skip_join_lobby
- [ ] int currentspawn
- [ ] int global_coin_multiplier
- [ ] BukkitTask maximum_game_time
- [ ] ArrayList<ItemStack> global_drops
- [ ] int currentlobbycount
- [ ] int currentingamecount
- [ ] int currenttaskid
- [ ] boolean temp_delay_stopped
- [ ] ArenaLogger logger
- [ ] Arena(final JavaPlugin plugin, final String name)
- [ ] Arena(final JavaPlugin plugin, final String name, final ArenaType type)
- [ ] public void init(final Location signloc, final ArrayList<Location> spawns, final Location mainlobby, final Location waitinglobby, final int max_players, final int min_players, final boolean viparena)
- [ ] Arena initArena(final Location signloc, final ArrayList<Location> spawn, final Location mainlobby, final Location waitinglobby, final int max_players, final int min_players, final boolean viparena)
- [ ] Arena getArena()
- [ ] SmartReset getSmartReset()
- [ ] boolean getShowScoreboard()
- [ ] boolean getAlwaysPvP()
- [ ] void setAlwaysPvP(final boolean t)
- [ ] Location getSignLocation()
- [ ] void setSignLocation(final Location l)
- [ ] ArrayList<Location> getSpawns()
- [ ] Cuboid getBoundaries()
- [ ] Cuboid getLobbyBoundaries()
- [ ] Cuboid getSpecBoundaries()
- [ ] String getInternalName()
- [ ] String getDisplayName()
- [ ] String getName()
- [ ] int getMaxPlayers()
- [ ] int getMinPlayers()
- [ ] void setMinPlayers(final int i)
- [ ] void setMaxPlayers(final int i)
- [ ] boolean isVIPArena()
- [ ] void setVIPArena(final boolean t)
- [ ] ArrayList<String> getAllPlayers()
- [ ] boolean containsPlayer(final String playername)
- [ ] boolean addPlayer(final String playername)
- [ ] boolean removePlayer(final String playername)
- [ ] void joinPlayerLobby(final String playername)
- [ ] void joinPlayerLobby(final UUID playerUuid)
- [ ] void joinPlayerLobby(final String playername, final boolean countdown)
- [ ] void joinPlayerLobby(final String playername, final ArcadeInstance arcade, final boolean countdown, final boolean skip_lobby)
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
- [ ] void startRaw()
- [ ] void stopArena()
- [ ] List<Entity> getResetEntitiesOnPlayerLeave(String player)
- [ ] boolean isEntityResetOnPlayerLeave(String player, Entity e)
- [ ] List<Entity> getResetEntities(String player)
- [ ] boolean isEntityReset(String player, Entity e)
- [ ] void stop()
- [ ] void reset()
- [ ] void onEliminated(final String playername)
- [ ] void nextArenaOnMapRotation(final ArrayList<String> players)
- [ ] String getPlayerCount()
- [ ] int getPlayerAlive()
- [ ] Location getWaitingLobbyTemp()
- [ ] Location getWaitingLobby()
- [ ] Location getMainLobbyTemp()
- [ ] Location getMainLobby()
- [ ] ArcadeInstance getArcadeInstance()
- [ ] boolean isArcadeMain()
- [ ] void setArcadeMain(final boolean t)
- [ ] HashMap<String, Location> getPSpawnLocs()
- [ ] JavaPlugin getPlugin()
- [ ] PluginInstance getPluginInstance()
- [ ] int getCurrentIngameCountdownTime()
- [ ] int getCurrentLobbyCountdownTime()
- [ ] boolean getIngameCountdownStarted()
- [ ] boolean isSuccessfullyInit()
- [ ] void setLastDamager(final String targetPlayer, final String damager)

## ArenaConfigStrings.java

- [ ] String BOUNDS_LOW
- [ ] String BOUNDS_HIGH
- [ ] String LOBBY_BOUNDS_LOW
- [ ] String LOBBY_BOUNDS_HIGH
- [ ] String SPEC_BOUNDS_LOW
- [ ] String SPEC_BOUNDS_HIGH
- [ ] String SPEC_SPAWN
- [ ] String ARENAS_PREFIX
- [ ] String DISPLAYNAME_SUFFIX
- [ ] String AUTHOR_SUFFIX
- [ ] String DESCRIPTION_SUFFIX
- [ ] String CONFIG_CLASS_SELECTION_ITEM
- [ ] String CONFIG_EXIT_ITEM
- [ ] String CONFIG_ACHIEVEMENT_ITEMS
- [ ] String CONFIG_SPECTATOR_ITEM
- [ ] String CONFIG_SHOP_SELECTION_ITEM
- [ ] String CONFIG_CLASSES_GUI_ROWS
- [ ] String CONFIG_SHOP_GUI_ROWS
- [ ] String CONFIG_SPECTATOR_AFTER_FALL_OR_DEATH
- [ ] String CONFIG_SPECTATOR_MOVE_Y_LOCK
- [ ] String CONFIG_DEFAULT_MAX_PLAYERS
- [ ] String CONFIG_DEFAULT_MIN_PLAYERS
- [ ] String CONFIG_DEFAULT_MAX_GAME_TIME_IN_MINUTES
- [ ] String CONFIG_LOBBY_COUNTDOWN
- [ ] String CONFIG_INGAME_COUNTDOWN
- [ ] String CONFIG_INGAME_COUNTDOWN_ENABLED
- [ ] String CONFIG_SKIP_LOBBY
- [ ] String CONFIG_CLEANINV_WHILE_INGAMECOUNTDOWN
- [ ] String CONFIG_CLASSES_ENABLED
- [ ] String CONFIG_SHOP_ENABLED
- [ ] String CONFIG_USE_CREADITS_INSTEAD_MONEY_FOR_KITS
- [ ] String CONFIG_RESET_INV_WHEN_LEAVING_SERVER
- [ ] String CONFIG_COLOR_BACKGROUND_WOOL
- [ ] String CONFIG_SHOW_CLASSES_WITHOUT_PERM
- [ ] String CONFIG_REWARDS_ECONOMY
- [ ] String CONFIG_REWARDS_ECONOMY_REWARD
- [ ] String CONFIG_REWARDS_ITEM_REWARD
- [ ] String CONFIG_REWARDS_ITEM_REWARD_IDS
- [ ] String CONFIG_REWARDS_COMMAND_REWARD
- [ ] String CONFIG_REWARDS_COMMAND
- [ ] String CONFIG_REWARDS_ECONOMY_FOR_KILLS
- [ ] String CONFIG_REWARDS_ECONOMY_REWARD_FOR_KILLS
- [ ] String CONFIG_REWARDS_COMMAND_REWARD_FOR_KILLS
- [ ] String CONFIG_REWARDS_COMMAND_FOR_KILLS
- [ ] String CONFIG_REWARDS_ECONOMY_FOR_PARTICIPATION
- [ ] String CONFIG_REWARDS_ECONOMY_REWARD_FOR_PARTICIPATION
- [ ] String CONFIG_REWARDS_COMMAND_REWARD_FOR_PARTICIPATION
- [ ] String CONFIG_REWARDS_COMMAND_FOR_PARTICIPATION
- [ ] String CONFIG_STATS_POINTS_FOR_KILL
- [ ] String CONFIG_STATS_POINTS_FOR_WIN
- [ ] String CONFIG_ARCADE_ENABLED
- [ ] String CONFIG_ARCADE_MIN_PLAYERS
- [ ] String CONFIG_ARCADE_MAX_PLAYERS
- [ ] String CONFIG_ARCADE_ARENA_TO_PREFER_ENABLED
- [ ] String CONFIG_ARCADE_ARENA_TO_PREFER_ARENA
- [ ] String CONFIG_ARCADE_LOBBY_COUNTDOWN
- [ ] String CONFIG_ARCADE_SHOW_EACH_LOBBY_COUNTDOWN
- [ ] String CONFIG_ARCADE_INFINITE_ENABLED
- [ ] String CONFIG_ARCADE_INFINITE_SECONDS_TO_NEW_ROUND
- [ ] String CONFIG_BUNGEE_GAME_ON_JOIN
- [ ] String CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_TP
- [ ] String CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_SERVER
- [ ] String CONFIG_BUNGEE_WHITELIST_WHILE_GAME_RUNNING
- [ ] String CONFIG_EXECUTE_CMDS_ON_STOP
- [ ] String CONFIG_CMDS
- [ ] String CONFIG_CMDS_AFTER
- [ ] String CONFIG_MAP_ROTATION
- [ ] String CONFIG_BROADCAST_WIN
- [ ] String CONFIG_BUY_CLASSES_FOREVER
- [ ] String CONFIG_DISABLE_COMMANDS_IN_ARENA
- [ ] String CONFIG_COMMAND_WHITELIST
- [ ] String CONFIG_LEAVE_COMMAND
- [ ] String CONFIG_SPAWN_FIREWORKS_FOR_WINNERS
- [ ] String CONFIG_POWERUP_BROADCAST
- [ ] String CONFIG_POWERUP_FIREWORKS
- [ ] String CONFIG_USE_CUSTOM_SCOREBOARD
- [ ] String CONFIG_DELAY_ENABLED
- [ ] String CONFIG_DELAY_AMOUNT_SECONDS
- [ ] String CONFIG_SEND_GAME_STARTED_MSG
- [ ] String CONFIG_AUTO_ADD_DEFAULT_KIT
- [ ] String CONFIG_LAST_MAN_STANDING_WINS
- [ ] String CONFIG_EFFECTS_BLOOD
- [ ] String CONFIG_EFFECTS_DMG_IDENTIFIER_HOLO
- [ ] String CONFIG_EFFECTS_DEAD_IN_FAKE_BED
- [ ] String CONFIG_EFFECTS_1_8_TITLES
- [ ] String CONFIG_EFFECTS_1_8_SPECTATOR_MODE
- [ ] String CONFIG_SOUNDS_LOBBY_COUNTDOWN
- [ ] String CONFIG_SOUNDS_INGAME_COUNTDOWN
- [ ] String CONFIG_CHAT_PER_ARENA_ONLY
- [ ] String CONFIG_CHAT_SHOW_SCORE_IN_ARENA
- [ ] String CONFIG_COMPASS_TRACKING_ENABLED
- [ ] String CONFIG_ALLOW_CLASSES_SELECTION_OUT_OF_ARENAS
- [ ] String CONFIG_SEND_STATS_ON_STOP
- [ ] String CONFIG_USE_XP_BAR_LEVEL
- [ ] String CONFIG_USE_OLD_RESET_METHOD
- [ ] String CONFIG_CHAT_ENABLED
- [ ] String CONFIG_EXTRA_LOBBY_ITEM_PREFIX
- [ ] String CONFIG_EXTRA_LOBBY_ITEM_ENABLED_SUFFIX
- [ ] String CONFIG_EXTRA_LOBBY_ITEM_ITEM_SUFFIX
- [ ] String CONFIG_EXTRA_LOBBY_ITEM_NAME_SUFFIX
- [ ] String CONFIG_EXTRA_LOBBY_ITEM_COMMAND_SUFFIX
- [ ] String CONFIG_MYSQL_ENABLED
- [ ] String CONFIG_MYSQL_HOST
- [ ] String CONFIG_MYSQL_USER
- [ ] String CONFIG_MYSQL_PW
- [ ] String CONFIG_MYSQL_DATABASE

## ArenaListener.java

- [ ] ArrayList<String> cmds
- [ ] String leave_cmd
- [ ] int loseY
- [ ] ArenaListener(final JavaPlugin plugin, final PluginInstance pinstance, final String minigame)
- [ ] ArenaListener(final JavaPlugin plugin, final PluginInstance pinstance, final String minigame, final ArrayList<String> cmds)
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
- [ ] boolean isSpectating(final Player p)
- [ ] List<Entity> getEntitiesByLocation(final Location loc, final double d)
- [ ] boolean checkLocationMatchesSign(final Location l, final Sign s)
- [ ] void updateEntities(final List<Player> players, final boolean visible)
- [ ] List<Player> getPlayersWithin(final Player player, final int distance)
- [ ] String getName()
- [ ] void setName(final String minigame)

## ArenaLogger.java

- [ ] logging methods

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

- [ ] String playername
- [ ] ItemStack[] inv
- [ ] ItemStack[] armor_inv
- [ ] GameMode original_gamemode
- [ ] int original_xplvl
- [ ] boolean noreward
- [ ] Arena currentArena
- [ ] AClass currentClass
- [ ] HashMap<String, ArenaPlayer> players
- [ ] ArenaPlayer getPlayerInstance(final String playername)
- [ ] ArenaPlayer(final String playername)
- [ ] Player getPlayer()
- [ ] void setInventories(final ItemStack[] inv, final ItemStack[] armor_inv)
- [ ] ItemStack[] getInventory()
- [ ] ItemStack[] getArmorInventory()
- [ ] GameMode getOriginalGamemode()
- [ ] void setOriginalGamemode(final GameMode original_gamemode)
- [ ] int getOriginalXplvl()
- [ ] void setOriginalXplvl(final int original_xplvl)
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
- [ ] void setBoundaries(final JavaPlugin plugin, final String arenaname, final Location l, final boolean low, final String extra_component)
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

- [ ] CHANNEL_BUNGEE_CORD
- [ ] SUBCHANNEL_MINIGAMESLIB_BACK
- [ ] SUBCHANNEL_MINIGAMESLIB_REQUEST
- [ ] SUBCHANNEL_MINIGAMESLIB_SIGN

## Classes.java

- [ ] public HashMap<String, IconMenu> lasticonm = new HashMap<>();
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

TODO

## Effects.java

TODO

## Messages.java

TODO

## MinecraftVersionsType.java

TODO

## MinigamesAPI.java

TODO

## Party.java

TODO

## PermissionStrings.java

TODO

## PluginConfigStrings.java

TODO

## PluginInstance.java

TODO

## PrivateUtil.java

TODO

## Rewards.java

TODO

## Shop.java

TODO

## SmartReset.java

TODO

## SpectatorManager.java

TODO

## Stats.java

TODO

## AAchievement.java

TODO

## ArenaAchievements.java

TODO

## ArcadeInstance.java

TODO

## CommandHandler.java

TODO

## AchievementsConfig.java

TODO

## ArenasConfig.java

TODO

## ClassesConfig.java

TODO

## DefaultConfig.java

TODO

## GunsConfig.java

TODO

## HologramsConfig.java

TODO

## MessagesConfig.java

TODO

## PartyMessagesConfig.java

TODO

## ShopConfig.java

TODO

## StatsConfig.java

TODO

## StatsGlobalConfig.java

TODO

## ArenaEvent.java

TODO

## ArenaStartedEvent.java

TODO

## ArenaStartEvent.java

TODO

## ArenaStopEvent.java

TODO

## PlayerEvent.java

TODO

## PlayerJoinLobbyEvent.java

TODO

## PlayerLeaveArenaEvent.java

TODO

## Gun.java

TODO

## Guns.java

TODO

## Database.java

TODO

## MainSQL.java

TODO

## MySQL.java

TODO

## SQLite.java

TODO

## Hologram.java

TODO

## Holograms.java

TODO

## AClass.java

TODO

## ArenaBlock.java

TODO

## ArenaLobbyScoreboard.java

TODO

## ArenaScoreboard.java

TODO

## BungeeUtil.java

TODO

## ChangeCause.java

TODO

## Cuboid.java

TODO

## IconMenu.java

TODO

## InventoryManager.java

TODO

## Leaderboard.java

TODO

## Metrics.java

TODO

## ParticleEffectNew.java

TODO

## ShopItem.java

TODO

## Signs.java

TODO

## SmartArenaBlock.java

TODO

## UpdaterBukkit.java

TODO

## UpdaterNexus.java

TODO

## Util.java

TODO

## Validator.java

TODO