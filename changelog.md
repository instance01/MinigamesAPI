# Changelog

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

###### BedWars

* Spawning villagers inside BedWars arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)

###### BowBash

* fixed console spams of "Cancelled powerup task"
* fixed powerup task error (https://github.com/MysticCity/MinigamesAPI/issues/104)

###### HorseRacingPlus

* Spawning horses inside HroseRacingPlus arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)

###### MobEscape

* Spawning witches and dragons inside MobEscape arena is allowed (https://github.com/MysticCity/MinigamesAPI/issues/79)

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
