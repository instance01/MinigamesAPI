# Minigames-Lib 2.0 - Development

## What is a minigame?

A 'minigame' is a unique game definition including rules and behavior definitions.

## Obtaining your minigame name

Each minigame has an internal technical name that identifies the game. You cannot register minigames of the same name twice.
To ensure a minigame name is not taken please contact the minigame lib authors.

They provide a package called 'minigames-all'. It includes the library and the minigames built on the library. You can share
your minigame through this package and from this point of view ensure that no other guy takes your minigame name.

## An example

To clear the term 'minigame' we will investigate a common 'last man standing' arena. So let us call the minigame 'lms'.

A minigame has an unique name (='lms') to identify it internally in the minecraft server. It is declared by a call to the method:
    
    com.github.mce.minigames.api.MglibInterface.register(PluginProviderInterface)
    
A plugin must not create multiple minigames.

Having a look at the PluginProviderInterface you will see that a minigame is made of several helpers.

### The rules

Most important to our minigame are the gaming rules. In our 'lms' example you can sum up the rules in word:

* No PvP in lobbs
* No PvP during warmup
* Player spawn at fixed spawn locations
* PvP during game
* You lose after being killed
* Last surrender wins the game
* Game stops if someone wins
* leaving arena let you lose

However there are more game rules under the hood, maybe related to ingame shop, to guns etc.

### Game variant: Deathmatch

Now let us think of a second game, the 'deathmatch' or 'dm'

We can re-use the rules from 'lms' except some differences:

* Team ends after 10 minutes
* After death the last damager gets a score point
* After death the player respawns but is invulnerable for some seconds
* The player with most points wins the game

### Game variant: Team deathmatch

The third game variant, we call it 'tdm', has the following difference compared to 'dm':

* Players are in balanced teams (red, blue etc.)
* Not only a single player gets a score point but the team gets it too
* The team with most poins wins
* The player with most points gets a special reward

### One minigame with three game variants

As a new feature the minigames library v2 allows you to create multiple arenas and configurations within the
same minigame.

The new features (arena types and configurable/ votable rules) are explained in additional chapters.

We can have a single minigame called 'pvparena'. This minigame allows the administrator to choose from three
arena types 'last-man-standing', 'deathmatch' or 'team-deathmatch'. It is even possible to allow the players
to choose there favorite game mode by voting.

## Summary

* Normally you have one minigame per plugin but you are allowed to create multiple minigames within the same plugin.
* A minigame has an internal/ technical name and is registered using a plugin provider.
* Minigames and arenas are build on different game rules.
* You may use the arena type feature or the rule customization feature to create different variants of the same minigame.
