# Minigames-Lib 2.0 - Features (user/admin view)

## Existing/ old features

### Easy Minigame installation

The original Minigames-Lib V1 already introduces a smart way: Plug & Play.

The library itself and the minigames can be used without any additional configuration. All features that
require additional configuration are optional. For example a missing mysql configuration means "we do not
need mysql".

The games are Plug & Play too. After putting the jar into your server start it, go to the game world,
build up an arena and let the games begin.

### Arenasystem

The arena system provides a way to declare zones (with or without bounds) for a minigame. Within this
zones a minigame match is talking part.

In 2.0 this won't change. However it is changed under the hood. We will divide the arena from
zones and gaming rules. Old arenas from v1 can be migrated to the new semantic (backward compatibility).
But new arenas can contain more than one zone combining different gaming rules.

Arenas are identified by internal names. The following names are preserved by the library itself:

* core
* join
* leave
* random
* mglib* (Everything starting with mglib).

Creating arenas with these keywords will fail in 2.0.

### Customizable arena system

InstanceLabs already created a highly configurable arena system. Most aspects of arenas can be changed.

However the customization options will be increased in v2.0. A minigame developer can make gaming rules static,
customizable or optional.

Customizable gaming rules can be changed by administrators or during player votes.

Optional gaming rules can be removed completly by administrators or during player votes.

### Different minigame types

InstanceLabs already introduces the following gaming types:

* Arcade
* Jump&Run
* Default/ Regeneration

We will follow this principle but internally the code is refactored. In 2.0 the arcade system will be completly
isolated from the arena system. The arcade represents a way how the players join the arena and what is happening
after the game ends/ they leave. It is not meant to be a minigame type at all.

In 2.0 we will add two new features beside the arcade:

* Arena groups: Grouping arenas will result in additional features. For example you can group arenas of multiple
  minigames to let the player join a random game of this group. An arena group can be created so that the players
  can stay within this group after match completes and vote for a new game or map within a special group lobby.
* Map rotation: Map rotation was already part of v1.0. However this feature was very simple. Within 2.0 we will
  add more options to customize map rotation.

### VIP arenas

Arenas can be marked as VIP-Arenas. A VIP-Arena requires a special permission to join. VIPs can join a waiting
lobby even if there are too many players. A non-vip is kicked.

We do not change this behavior in version 2.0

### Economy, command and item rewards

The reward system allows to configure economy, command or item rewards upon game events.

In 1.0 one gets rewards on following events:
* win
* participating (lose)
* kills

There are boost items you can buy inside the shop. Those ensure the rewards are multiplied by 2 oder 3.

In version 2 we do not change this feature. We extend it a little bit to give the administrator more control
over the rewards.

The rewards itself are using a new API in version 2.0. A minigame writer can introduce more reward types if needed.
For example the developer may decide to create rewards for reaching achievements.

### Overall Customization/Attributes like player counts, countdowns, spawns, boundaries etc.

The Minigames Lib already has a huge set of configuration properties. There will be no changes at this point.

Each configuration property is migrated to the new game rules. There will be some more configuration options
on the gaming rules in version 2.0.

### Signs and corresponding Arena states

TODO

### Fast map regeneration mechanism

TODO

### Kits/Classes and Classes Gui

TODO

### Arcade system

TODO

### MySQL/SQLite support

TODO

### Guns API (unfinished and only in use in Warlock Tactical right now)

TODO

### Bungee support including signs

TODO

### Party (/party)

TODO

### Achievements

TODO

### Additional shops with Gui

TODO

### Included effects like blood or dead bodies lying around

TODO

### Holograms and Scoreboards

TODO

## New features of the library in version 2.0:

###  Multi-language support

TODO

### Declarative arena and game types, use game rules instead of writing code

TODO

### Multiple arena zones/locations with individual rules

TODO 

### Smart administration Gui

TODO

### Persistent game states

TODO

### Better support for Bungee-Clusters and Multi-World systems

TODO
