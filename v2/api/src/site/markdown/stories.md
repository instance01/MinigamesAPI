# User Stories

## Table of Contents

* [USER0010: Create a party](#user0001-create-a-party)
* [USER0002: Invite a party member](#user0002-invite-a-party-member)
* [USER0003: Accept party invitation](#admin0003-disband-party)
* [USER0004: Decline party invitation](#user0004-decline-party-invitation)
* [USER0005: Leave party](#user0005-leave-party)
* [USER0006: Disband party](#user0006-disband-party)
* [USER0007: Kick a party member](#user0007-kick-a-party-member)
* [USER0008: Join a queue with party](#user0008-join-a-queue-with-party)
* [USER0009: Leave a queue with party](#user0009-leave-a-queue-with-party)
* [USER0010: Display party scoreboard](#user0010-display-party-scoreboard)
* [USER0100: Join a queue](#user0100-join-a-queue)
* [USER0101: Leave a queue](#user0101-leave-a-queue)
* [USER0102: Display queue scoreboard](#user0102-display-queue-scoreboard)
* [USER0103: Join a waiting lobby](#user0103-join-a-waiting-lobby)
* [USER0104: Leave a waiting lobby](#user0104-leave-a-waiting-lobby)
* [USER0105: Display waiting lobby scoreboard](#user0105-display-waiting-lobby-scoreboard)
* [USER0106: Join a match](#user0106-join-a-match)
* [USER0107: Lose a match](#user0107-lose-a-match)
* [USER0108: Win a match](#user0108-win-a-match)
* [USER0109: Display match scoreboard](#user0109-display-match-scoreboard)
* [USER0110: Spectate a match](#user0110-spectate-a-match)
* [USER0111: Leave spectator match](#user0111-leave-spectator-match)
* [USER0112: Disconnect](#user0112-disconnect)
* [USER0113: Play a match](#user0113-play-a-match)
* [USER0114: Display animations](#user0114-display-animations)
* [USER0200: Display preferred locale](#user0200-display-preferred-locale)
* [USER0201: Use preferred locale](#user0201-use-preferred-locale)
* [USER0202: Change preferred locale](#user0202-change-preferred-locale)
* [USER0300: Display common information](#user0300-display-common-information)
* [USER0301: Display score list](#user0301-display-score-list)
* [USER0400: Open match shop](#user0400-open-match-shop)
* [USER0401: Select KIT](#user0401-select-kit)
* [USER0500: Accept admin test match invitation](#user0500-accept-admin-test-match-invitation)
* [USER0600: Joining queue as VIP](#user0600-joining-queue-as-vip)
* [USER0601: Joining queue as VIP with party](#user0601-joining-queue-as-vip-with-party)
* [USER0700: Displaying statistics](#user0700-displaying-statistics)
* [USER0701: Displaying achievements](#user0701-displaying-achievements)
* [USER0702: Getting achievements](#user0702-getting-achievements)
* [USER0703: Getting rewards](#user0703-getting-rewards)
* [USER0704: Playing arcade](#user0704-playing-arcade)
* [USER0705: Selecting guns and effects](#user0705-selecting-guns-and-effects)
* [USER0706: Joining waiting queue on different world](#user0706-joining-waiting-queue-on-different-world)
* [USER0707: Joining waiting queue on different server](#user0707-joining-waiting-queue-on-different-server)
* [USER0708: Displaying statistics](#user0708-displaying-statistics)
* [USER0800: Enter arena](#user0800-enter-arena)
* [USER0801: Entering Zone](#user0801-entering-zone)
* [USER0802: Leaving Zone](#user0802-leaving-zone)
* [USER0803: Switching Zone](#user0803-switching-zone)
* [USER0900: Changing blocks, Influencing game](#user0900-changing-blocks-influencing-game)
* [USER0901: Changing Game Mode](#user0901-changing-game-mode)
* [ADMIN0001: Display Information](#admin0001-display-information)
* [ADMIN0002: Debug](#admin0002-debug)
* [ADMIN0003: Disband Party](#admin0003-disband-party)
* [ADMIN0004: Kick party members](#admin0004-kick-party-members)
* [ADMIN0005: Display default language](#admin0005-display-default-language)
* [ADMIN0006: Change default language](#admin0006-change-default-language)
* [ADMIN0007: Display configuration options](#admin0007-display-configuration-options)
* [ADMIN0008: Change configuration options](#admin0008-change-configuration-options)
* [ADMIN0009: Enable Minigames Lib](#admin0009-enable-minigames-lib)
* [ADMIN0010: Disable Minigames lib](#admin0010-disable-minigames-lib)
* [ADMIN0011: Enable minigame](#admin0011-enable-minigame)
* [ADMIN0012: Disable minigame](#admin0012-disable-minigame)
* [ADMIN0014: Display language strings](#admin0014-display-language-strings)
* [ADMIN0015: Edit language string](#admin0015-edit-language-string)
* [ADMIN0100: Create arena](#admin0100-create-arena)
* [ADMIN0101: Edit arena](#admin0101-edit-arena)
* [ADMIN0102: Disable arena](#admin0102-disable-arena)
* [ADMIN0103: Enable arena](#admin0103-enable-arena)
* [ADMIN0104: Delete arena](#admin0104-delete-arena)
* [ADMIN0105: Check arena](#admin0105-check-arena)
* [ADMIN0106: Undo changes](#admin0106-undo-changes)
* [ADMIN0107: Save arena](#admin0107-save-arena)
* [ADMIN0108: Invite for Test match](#admin0108-invite-for-test-match)
* [ADMIN0109: Start test match](#admin0109-start-test-match)
* [ADMIN0110: Stop test match](#admin0110-stop-test-match)
* [ADMIN0111: Change arena state](#admin0111-change-arena-state)
* [ADMIN0200: Create arena zone](#admin0200-create-arena-zone)
* [ADMIN0201: Edit arena zone](#admin0201-edit-arena-zone)
* [ADMIN0202: Delete arena zone](#admin0202-delete-arena-zone)
* [ADMIN0300: Create arena component](#admin0300-create-arena-component)
* [ADMIN0301: Edit arena component](#admin0301-edit-arena-component)
* [ADMIN0302: Delete arena component](#admin0302-delete-arena-component)
* [ADMIN0400: Display minigame configuration options](#admin0400-display-minigame-configuration-options)
* [ADMIN0401: Edit minigame configuration options](#admin0401-edit-minigame-configuration-options)
* [ADMIN0402: Display minigame language strings](#admin0402-display-minigame-language-strings)
* [ADMIN0403: Edit minigame language strings](#admin0403-edit-minigame-language-strings)
* [ADMIN0500: Edit minigame kits](#admin0500-edit-minigame-kits)
* [ADMIN0600: Edit minigame shop](#admin0600-edit-minigame-shop)
* [ADMIN0700: Create queue](#admin0700-create-queue)
* [ADMIN0701: Edit arena queue](#admin0701-edit-arena-queue)
* [ADMIN0702: Delete arena queue](#admin0702-delete-arena-queue)
* [ADMIN0703: Enable arena queue](#admin0703-enable-arena-queue)
* [ADMIN0704: Disable arena queue](#admin0704-disable-arena-queue)
* [ADMIN0705: Create arcade queue](#admin0705-create-arcade-queue)
* [ADMIN0706: Edit arcade queue](#admin0706-edit-arcade-queue)
* [ADMIN0707: Delete arcade queue](#admin0707-delete-arcade-queue)
* [ADMIN0708: Enable arcade queue](#admin0708-enable-arcade-queue)
* [ADMIN0709: Disable arcade queue](#admin0709-disable-arcade-queue)
* [ADMIN0710: Create map rotation queue](#admin0710-create-map-rotation-queue)
* [ADMIN0711: Edit map rotation queue](#admin0711-edit-map-rotation-queue)
* [ADMIN0712: Delete map rotation queue](#admin0712-delete-map-rotation-queue)
* [ADMIN0713: Enable map rotation queue](#admin0713-enable-map-rotation-queue)
* [ADMIN0714: Disable map rotation queue](#admin0714-disable-map-rotation-queue)
* [ADMIN0800: Changing blocks, Influencing game](#admin0800-changing-blocks-influencing-game)
* [ADMIN0801: Changing Game Mode](#admin0801-changing-game-mode)
* [MC0001: Spawning mobs](#mc0001-spawning-mobs)
* [MC0002: Changing time](#mc0002-changing-time)
* [MC0003: Changing weather](#mc0003-changing-weather)
* [MC0004: Spawning mobs](#mc0004-spawning-mobs)
* [MC0005: Changing blocks, influencing game](#mc0005-changing-blocks-influencing-game)
* [MC0006: Mob enters arena](#mc0006-mob-enters-arena)
* [MC0007: Mob leaves arena](#mc0007-mob-leaves-arena)
* [MC0008: Mob enters zone](#mc0008-mob-enters-zone)
* [MC0009: Mob leaves arena](#mc0009-mob-leaves-arena)
* [MC0010: Mob attacks players](#mc0010-mob-attacks-players)

## Community edition

### Core

#### User

##### USER0001: Create a party

TODO

##### USER0002: Invite a party member

TODO

##### USER0003: Accept party invitation

TODO

##### USER0004: Decline party invitation

TODO

##### USER0005: Leave party

TODO

##### USER0006: Disband party

TODO

##### USER0007: Kick a party member

TODO

##### USER0008: Join a queue with party

TODO

##### USER0009: Leave a queue with party

TODO

##### USER0010: Display party scoreboard

TODO

##### USER0100: Join a queue

TODO

##### USER0101: Leave a queue

TODO

##### USER0102: Display queue scoreboard

TODO

##### USER0103: Join a waiting lobby

TODO

##### USER0104: Leave a waiting lobby

TODO

##### USER0105: Display waiting lobby scoreboard

TODO

##### USER0106: Join a match

TODO

##### USER0107: Lose a match

TODO

##### USER0108: Win a match

TODO

##### USER0109: Display match scoreboard

TODO

##### USER0110: Spectate a match

TODO

##### USER0111: Leave spectator match

TODO

##### USER0112: Disconnect

TODO

##### USER0113: Play a match

TODO

##### USER0114: Display animations

TODO

##### USER0200: Display preferred locale

TODO

##### USER0201: Use preferred locale

TODO

##### USER0202: Change preferred locale

TODO

##### USER0300: Display common information

TODO

##### USER0301: Display score list

TODO

##### USER0400: Open match shop

TODO

##### USER0401: Select KIT

TODO

##### USER0500: Accept admin test match invitation

TODO

##### USER0600: Joining queue as VIP

TODO

##### USER0601: Joining queue as VIP with party

TODO

##### USER0700: Displaying statistics

TODO

##### USER0701: Displaying achievements

TODO

##### USER0702: Getting achievements

TODO

##### USER0703: Getting rewards

TODO

##### USER0704: Playing arcade

TODO

##### USER0705: Selecting guns and effects

TODO

##### USER0706: Joining waiting queue on different world

TODO

##### USER0707: Joining waiting queue on different server

TODO

##### USER0708: Displaying statistics

TODO

##### USER0800: Enter arena

TODO

##### USER0801: Entering Zone

TODO

##### USER0802: Leaving Zone

TODO

##### USER0803: Switching Zone

TODO

##### USER0900: Changing blocks, Influencing game

TODO

##### USER0901: Changing Game Mode

TODO

#### Admin

##### ADMIN0001: Display Information

TODO

##### ADMIN0002: Debug

TODO

##### ADMIN0003: Disband Party

TODO

##### ADMIN0004: Kick party members

TODO

##### ADMIN0005: Display default language

TODO

##### ADMIN0006: Change default language

TODO

##### ADMIN0007: Display configuration options

TODO

##### ADMIN0008: Change configuration options

TODO

##### ADMIN0009: Enable Minigames Lib

TODO

##### ADMIN0010: Disable Minigames lib

TODO

##### ADMIN0011: Enable minigame

TODO

##### ADMIN0012: Disable minigame

TODO

##### ADMIN0014: Display language strings

TODO

##### ADMIN0015: Edit language string

TODO

##### ADMIN0100: Create arena

TODO

##### ADMIN0101: Edit arena

TODO

##### ADMIN0102: Disable arena

TODO

##### ADMIN0103: Enable arena

TODO

##### ADMIN0104: Delete arena

TODO

##### ADMIN0105: Check arena

TODO

##### ADMIN0106: Undo changes

TODO

##### ADMIN0107: Save arena

TODO

##### ADMIN0108: Invite for Test match

TODO

##### ADMIN0109: Start test match

TODO

##### ADMIN0110: Stop test match

TODO

##### ADMIN0111: Change arena state

TODO

##### ADMIN0200: Create arena zone

TODO

##### ADMIN0201: Edit arena zone

TODO

##### ADMIN0202: Delete arena zone

TODO

##### ADMIN0300: Create arena component

TODO

##### ADMIN0301: Edit arena component

TODO

##### ADMIN0302: Delete arena component

TODO

##### ADMIN0400: Display minigame configuration options

TODO

##### ADMIN0401: Edit minigame configuration options

TODO

##### ADMIN0402: Display minigame language strings

TODO

##### ADMIN0403: Edit minigame language strings

TODO

##### ADMIN0500: Edit minigame kits

TODO

##### ADMIN0600: Edit minigame shop

TODO

##### ADMIN0700: Create queue

TODO

##### ADMIN0701: Edit arena queue

TODO

##### ADMIN0702: Delete arena queue

TODO

##### ADMIN0703: Enable arena queue

TODO

##### ADMIN0704: Disable arena queue

TODO

##### ADMIN0705: Create arcade queue

TODO

##### ADMIN0706: Edit arcade queue

TODO

##### ADMIN0707: Delete arcade queue

TODO

##### ADMIN0708: Enable arcade queue

TODO

##### ADMIN0709: Disable arcade queue

TODO

##### ADMIN0710: Create map rotation queue

TODO

##### ADMIN0711: Edit map rotation queue

TODO

##### ADMIN0712: Delete map rotation queue

TODO

##### ADMIN0713: Enable map rotation queue

TODO

##### ADMIN0714: Disable map rotation queue

TODO

##### ADMIN0800: Changing blocks, Influencing game

TODO

##### ADMIN0801: Changing Game Mode

TODO

#### System/Minecraft

##### MC0001: Spawning mobs

TODO

##### MC0002: Changing time

TODO

##### MC0003: Changing weather

TODO

##### MC0004: Spawning mobs

TODO

##### MC0005: Changing blocks, influencing game

TODO

##### MC0006: Mob enters arena

TODO

##### MC0007: Mob leaves arena

TODO

##### MC0008: Mob enters zone

TODO

##### MC0009: Mob leaves arena

TODO

##### MC0010: Mob attacks players

TODO

## Premium edition

TODO
