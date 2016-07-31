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

###### Minigames-API-Bungee
* removed everything (now part of Minigames-API)

