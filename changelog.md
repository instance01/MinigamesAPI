# Changelog


#### 1.14.9

###### All

* Using same version numbers (Games and minigamesAPI share same version number to make dependencies more intuitive)
* Support for Spigot 1.10

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

