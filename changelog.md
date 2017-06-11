# Changelog

#### 1.14.17

###### All

* Support for minecraft 1.12 (https://github.com/MysticCity/MinigamesAPI/issues/263)

###### MobEscape

* fixed detecting of first player waypoint (https://github.com/MysticCity/MinigamesAPI/issues/247)

###### OITC

* fixed crushing crops (https://github.com/MysticCity/MinigamesAPI/issues/257)

###### SkyWars

* fixed NPE (https://github.com/MysticCity/MinigamesAPI/issues/262)

#### 1.14.16

###### All

* (Bungeecord) Clicking a join sign will now throw all players of a party to the minigame server (https://github.com/MysticCity/MinigamesAPI/issues/208)
* fixed NullPointerException (https://github.com/MysticCity/MinigamesAPI/issues/243)
* *v2-backport* new chat countdown while reaching max game time (https://github.com/MysticCity/MinigamesAPI/issues/224)
* *v2-backport* api extension for minigames that want to influence the max game time (https://github.com/MysticCity/MinigamesAPI/issues/224)
* *v2-backport* support for unlimited games (max game time set to zero) (https://github.com/MysticCity/MinigamesAPI/issues/224)
* Fixed ArrayIndexOutOfBounds in stop and spectate command
* Fixed exceptions and several bugs related to spectation (https://github.com/MysticCity/MinigamesAPI/issues/230)
* Added SQLite support (https://github.com/MysticCity/MinigamesAPI/issues/220)
* Fixed NullPointerException (https://github.com/MysticCity/MinigamesAPI/issues/249)

###### BedWars

* Option to disable GUI for servers that have there own gui implementation (https://github.com/MysticCity/MinigamesAPI/issues/253)

###### Conquer

* Team selector gui (https://github.com/MysticCity/MinigamesAPI/issues/216)

###### MobEscape

* Fixed runtime error in 1.8.5 to 1.8.9
* Fixed false positive error: Destroy mode invalid (https://github.com/MysticCity/MinigamesAPI/issues/225)

###### SkyWars

* Added new mode 'items' for filling the chests. (https://github.com/MysticCity/MinigamesAPI/issues/254). For details siki wiki at https://github.com/MysticCity/MinigamesAPI/wiki/MGSkyWars

###### Splegg

* Fixed exception plaxying projectile sound in 1.7.X/1.8.X (https://github.com/MysticCity/MinigamesAPI/issues/241)

#### 1.14.15

###### All

* fixed bungeecord signs
* fixed config.yml generation for "old" setupAPI (https://github.com/MysticCity/MinigamesAPI/issues/212)
* fixed specator signs (https://github.com/MysticCity/MinigamesAPI/issues/188)
* fixed hologram removal (https://github.com/MysticCity/MinigamesAPI/issues/191)
* fixed "console spams"/ checking for debug config option (https://github.com/MysticCity/MinigamesAPI/issues/200)
* fixed NPE if main lobby is missing (https://github.com/MysticCity/MinigamesAPI/issues/201)
* some api extension (https://github.com/MysticCity/MinigamesAPI/issues/203)
* compass tracking fixed (https://github.com/MysticCity/MinigamesAPI/issues/206)
* new config flag: "use\_spectator\_scoreboard" (https://github.com/MysticCity/MinigamesAPI/issues/194)

###### BedWars

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)
* fixed unbalanced teams (https://github.com/MysticCity/MinigamesAPI/issues/215)
* fixed localization/ hard coded strings (https://github.com/MysticCity/MinigamesAPI/issues/215)

###### BowBash

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)

###### ColorMatch

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)

###### Conquer

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)

###### GunGame

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)

###### HorseRacing

* moved hard coded strings to config (for translation)
* added config option announce\_title (https://github.com/MysticCity/MinigamesAPI/issues/187)

###### MobEscape

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)
* added scoreboard text to messages.yml (https://github.com/MysticCity/MinigamesAPI/issues/218)
* fixed crack particle spawn (https://github.com/MysticCity/MinigamesAPI/issues/146)
* new config: destroy\_mode with possible values "sphere" and "cuboid" (https://github.com/MysticCity/MinigamesAPI/issues/125)

###### OITC

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)

###### SeaBattle

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)
* fixed boat under water (https://github.com/MysticCity/MinigamesAPI/issues/185)

###### Snake

* fixed lags/performance issues (https://github.com/MysticCity/MinigamesAPI/issues/198, https://github.com/MysticCity/MinigamesAPI/issues/207, https://github.com/MysticCity/MinigamesAPI/issues/213)

#### 1.14.14

###### All

* support for Minecraft 1.11 and 1.11.2
* incompatible change: Players command "<game> spectate <arena>" is now secured by permissions (https://github.com/MysticCity/MinigamesAPI/wiki/Admin-Spectating)
* bugfix by setting spectator bounds: (https://github.com/MysticCity/MinigamesAPI/issues/166)
* improved spectator support (https://github.com/MysticCity/MinigamesAPI/wiki/Admin-Spectating)
* Scoreboards now use display names (https://github.com/MysticCity/MinigamesAPI/issues/178)
* Scoreboards display correct names on multiple arenas of the same game (https://github.com/MysticCity/MinigamesAPI/issues/178)
* Lib now clears up any item on startup/stop, maybe remaining from crashing servers/ games or player interactions

###### BedWars

* fixed minor NPE for selecting classes (bedwars has no classes)
* support for 1.11 (https://github.com/MysticCity/MinigamesAPI/issues/182)
* fixed small memory leak

###### HorseRacingPlus

* bugfix for 1.11 support (https://github.com/MysticCity/MinigamesAPI/issues/177)

###### SeaBattle

* Added config option "die\_below\_bedrock\_level" (fixes arenas where the spawns are over 4 block above the battle sea)
* Really destroys all boats at end (even through disconnects etc.)

#### 1.14.12/1.14.13

###### All

* No more kill rewards for self-kills (https://github.com/MysticCity/MinigamesAPI/issues/11)
* Fixed NPE using spectator sign outside of arena (https://github.com/MysticCity/MinigamesAPI/issues/154)
* Fixed PlayOutFakeBed (https://github.com/MysticCity/MinigamesAPI/issues/161)
* Migrated to eclipse-plugin (http://minecraft.xworlds.eu/eclipse/)

###### BedWars

* Added command "setupbeds" to help page

###### DeathRun

* Config option "die\_below\_bedrock\_level" can now be numeric to hold the minimum y coordinate the player can reach

###### GunGame

* Config option "die\_below\_bedrock\_level" can now be numeric to hold the minimum y coordinate the player can reach

###### MobEscape

* Config option "die\_below\_bedrock\_level" can now be numeric to hold the minimum y coordinate the player can reach

###### Splegg

* Config option "die\_below\_bedrock\_level" can now be numeric to hold the minimum y coordinate the player can reach

###### SkyWars

* Fixed SmartReset logic for user placed blocks (https://github.com/MysticCity/MinigamesAPI/issues/169)

###### Sudoku

* new game sudoku/ migrated to minigames lib

#### 1.14.11

###### All

* removed system.out.println and printstacktrace

###### Minigames-API

* NoSuchMethodError in Effects.playHologram for 1.9 and 1.10
* Spawns of living entites inside arenas is now forbidden. No more need of worldguard etc (https://github.com/MysticCity/MinigamesAPI/issues/79)
* Players being inside arenas or waiting lobby can no longer be attacked by mobs.
* Effects.playOutTitle fixed (https://github.com/MysticCity/MinigamesAPI/issues/112)
* Feature for displaying arena states in MOTD. See config.yml of Minigames-LIB, enable at config.motd.enabled (https://github.com/MysticCity/MinigamesAPI/issues/54)
* Players in vehicles can not longer leave arena bounds (https://github.com/MysticCity/MinigamesAPI/issues/78)
* Config options to disable xp/inventory/gamemode reset on leave (https://github.com/MysticCity/MinigamesAPI/issues/97)

###### BedWars

* Spawning villagers inside BedWars arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)

###### BowBash

* fixed console spams of "Cancelled powerup task"
* fixed powerup task error (https://github.com/MysticCity/MinigamesAPI/issues/104)

###### HorseRacingPlus

* Spawning horses inside HorseRacingPlus arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)
* Started migration to MinigamesLib (https://github.com/MysticCity/MinigamesAPI/issues/12)

###### MobEscape

* Spawning witches and dragons inside MobEscape arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)
* Added new arena config option for falling-block-ratio, see https://github.com/MysticCity/MinigamesAPI/wiki/MGMobEscape for details (https://github.com/MysticCity/MinigamesAPI/issues/64)

###### SkyWars

* Players in vehicles can not longer leave arena bounds (https://github.com/MysticCity/MinigamesAPI/issues/78)

###### Snake

* fixed powerup task error (https://github.com/MysticCity/MinigamesAPI/issues/104)

###### Splegg

* removed arena points, fixes https://github.com/MysticCity/MinigamesAPI/issues/81
* fixed powerup task error (https://github.com/MysticCity/MinigamesAPI/issues/104)
* Spawning slime and sheeps inside Snake arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)

###### SeaBattle

* Workaround for boat under water https://github.com/MysticCity/MinigamesAPI/issues/95
* Workaround for deny boat movement on startup count down https://github.com/MysticCity/MinigamesAPI/issues/107
* Players in vehicles can not longer leave arena bounds (https://github.com/MysticCity/MinigamesAPI/issues/78)

##### TrapdoorSpleef

* fixed non-generated arena on first arena start (https://github.com/MysticCity/MinigamesAPI/issues/83)

##### Warlock

* fixed non-generated arena on first arena start (https://github.com/MysticCity/MinigamesAPI/issues/83)

##### Warlock-Tactical

* fixed non-generated arena on first arena start (https://github.com/MysticCity/MinigamesAPI/issues/83)



#### 1.14.10

###### All

* Changed permissions prefix to "ancient."
* Fixed smart

###### Minigames-API

* Fixed smart reset on big arenas/ hundreds of blocks

###### DeathRun

* Fixed falling blocks animation



#### 1.14.9

###### All

* Using same version numbers (Games and minigamesAPI share same version number to make dependencies more intuitive)
* Support for Spigot 1.10
* Incompatible change: Reworking the permissions, see https://github.com/MysticCity/MinigamesAPI/wiki/Permissions for details

###### Minigames-API
* various JUnit-Tests
* refactoring of MinigamesAPI-Bungee (merge to Minigames-API)
* migration to maven and hudson build
* various refactoring and javadoc
* NPE fix (https://github.com/MysticCity/MinigamesAPI/issues/41)
* new leave command invoked on arena leave (https://github.com/MysticCity/MinigamesAPI/issues/29)
* NPE fix (https://github.com/MysticCity/MinigamesAPI/issues/26)
* Upgraded dependencies to vault and crackshot
* Fix for some race conditions while stopping arena
* Fixed some UTF-8 encoding
* sub command "join" with given player name now is protected by permission "adminjoin"
* sub command "reload" is now protected by permission "reload"
* sub command "setkit" with given player name is now protected by permission "adminkit"
* sub command "spectate" with given player name is now protected by permission "adminspectate"
* sub command "setskull" deactivated. Will be reimplemented later on (https://github.com/MysticCity/MinigamesAPI/issues/59)
* NPE fix (https://github.com/MysticCity/MinigamesAPI/issues/57)
* Fixed disappearing objects (villagers, npc etx.) after teleports (https://github.com/MysticCity/MinigamesAPI/issues/37)
* New updater using a nexus query script (https://github.com/MysticCity/MinigamesAPI/issues/23), NOTICE: It does currently only query newer versions with WARNING log level. It does not yet update automatically.
* added config option to disable metrics updates (https://github.com/MysticCity/MinigamesAPI/issues/10)

###### Minigames-API-Bungee
* removed everything (now part of Minigames-API)

###### Snake
* wool is back again (see wiki)
* fixed problems with sheeps yaw
* fixed problems with "nervous" sheeps
* added tickCount (see wiki)

###### BedWars
* do never remove NPCs on map reset
* removed maxuses of trades/ set to 999999 because of some bugs (see https://github.com/MysticCity/MinigamesAPI/issues/61)
* fixed arena reset, now respects all playerblock events
* removed experience on trade (https://github.com/MysticCity/MinigamesAPI/issues/66)

###### MobEscape
* fixed arena reset, now resetting lower Y levels first (https://github.com/MysticCity/MinigamesAPI/issues/68)
* added jumpFactor configuration value, see wiki for details.

###### Jumper
* disabled the jumper scoreboard

###### Conquer
* Command "setcheckpoint" is now protected by permission "setup"

###### FlyingCars
* NPE fix while leaving minecart

###### Sudoku
* Sudoku join sign now uses "sudoku.sign" permission instead of "horseracingplus.sign" permission.
* Fixed join sign
