# Minigames-Lib 2.0 - Development

## About the arena

The Minigame-API itself handles everything by so called arenas. [Wikipedia](https://en.wikipedia.org/wiki/Arena) quotes that an arena is an enclosed area where the action goes on.

And that is excatly what an arena is for Minigames-API.

It is up to the administrator to build some nice building or similar things for the arena. For example he can create a colosseum.

The library will identify the arena by name. There are some arena names that are preserved. You will find them in the following constant:

    ArenaInterface.ILLEGAL_NAMES

Beside this preserved names you can choose any name. The name must be unique per minigame. It is possible to use the same name (f.e. "default") for different game.
However this is not recommended. The minigames lib won't be confused if the same name is used twiced across multiple minigames.

## The display name and some additional information

Arenas have a display name. That's a human readable name used within messages or on signs. You may not want to let users see the technical name 'pvp1'.
But you may want to display the name 'Ice-Battlefield'. The display name is not localized.

There is some additional data, such as authors and descriptions. You may fill them or simply leave them empty.

## The arena state

The arena can have some core states influencing their common behavior. Except the new maintenance state those states match the behavior in version 1 of the lib.

### Join

The arena is free for play. Players can join and matches can be started. Notice: Due to the new waiting queues players are still able to join even if the arena has a
different state. They are placed into a waiting queue and once the arena allows new player to join the waiting list is emptied in FIFO order till the next match is full.

### Starting

A match will be started soon. The arena is about to "start". This takes part if enough players joined the waiting queue to let a match start. 

### InGame

A match is being played. The players are fighting till the match ends/ someone wins.

### Resetting

The arena is resetting after a match. Blocks are restored etc.

### Maintenance

The arena is under maintenance. No players are allowed to join (except administrators). The players even cannot join the waiting queue.
Every change an administrator makes to the arena needs to be done in maintenance state. This ensures no one enters the arena as long as an
administrator works on it.

There will be a special test mode of the arena. This test mode lets administrators start a match and invite players to the match. This
switches to the 'Starting', 'InGame', 'Resetting' state but does never lead to 'Join' state. Instead after the match ends the 'Maintenance'
state is set to let the administrator continue the work.

## The arena type

A minigame supports one or more arena types.

The arena types contains rule sets used for the arena. They are declared by java code. So the basic rule set a game is using cannot be changed.
Rules are explained in another document in details.

A minigame can declare multiple arena types. They are identified by a java enumeration internally and by unique name for administration.

## Arena contents

An arena contains multiple elements based on the gaming rules.

### Main lobby

In Minigames Lib v1 there was a location called "main lobby". It was one location per game. However we decided to allow one main lobby per arena.

But there may always be a default main lobby for the whole minigame. So it does not break compatibility.

The main lobby is the location a player is ported once the match ends or he leaves the arena.

In v2 there is a new feature allowing an administrator to create lobbys on other bungee servers.

### Waiting lobby

The waiting lobby is a location the players are teleported once they joined an arena.

Originally there was only one waiting lobby per arena. It was meant to be a small room where the player does not
run away and where he can wait for other players to join.

In v2 the join concept is different. It allows you to declare waiting queues and arena groups. The player may join a waiting queue even if a match
is being processed. The administrator may choose to port them into "lobby #1" to represent the waiting queue. And he may choose to port players
waiting for a new match to "lobby #2" right before the game starts.

In v2 the game can have multiple waiting lobbies for different situations based on game rules. A game developer may decide that the match
has multiple rounds. Between each round the user is taken into a new waiting lobby (that will be the third one based on our example).

### Spawns

The spawns are associated with players or teams. Nothing changed on this concept.

However v1 did not have direct support for team spawns or shared/ random spawns. In v2 it is up to a game rule to choose the correct spawn for players.
The classic rule says "associate the spawn to a player during startup".

### Teams

In v2 we add a complete team support in the API.

An arena type can declare teams and handle them in a clean way.

However there is team support for the arena components too. Spawns and other arena components can be associated with teams.

### Arena bounds

The minigames lib needs to know in which area your minigame is located. This influences the arena resetting as well as the gaming rules.

For example a gaming rule may refer the arena bounds by defining "If the player leaves the arena he loses the game".

The smart reset features trys to detect which blocks are changed during a match. It remembers those blocks so that it can reset the original state
after a match ends. Every change during the match will be replayed so that the next match starts with the same arena and environment.

### Objects

Some minigames require special objects within the arena. For example BedWars requires a bed per team, an merchant npc and resource spawns
represented by clay, iron and gold blocks.

Minigames lib v2 will support those objects directly in API.

### Mobs, random spawns etc.

Minigames may require to spawn additional mobs during the match. In Version 2 we will support a clean API so that it is not required
to depend on any miniecraft/spigot NMS class.

We support
* merchant NPCs
* chests
* sheeps
* fallen blocks
* wither
* enderdragon

### Arena zones

A very new feature is the arena zone. A classical arena in version 1 of the library only has one zone: The arena bounds.

However the most games will be happy to manage only the arena bounds as a zone. But let us think of a pvp game where we want to
have a hospital to heal your wounds. To represent the hospital you can create a "hospital zone" within the arena bounds. The
hospital will heal wounds and there is no pvp allowed. Leaving the hospital zone will allow pvp. Within hospital a timer will be
running and being too long in the hospital will kick you out of the hospital.

A game developer may decide to create zone types and zone rules within the java code since this feature is related to the
minigame mechanic itself.

### Spectating

A spceial optional component is the spectating spawn and the spectating bound. After a player loses the match he can stay within
a special are of the arena. He can visit the remaining match till it ends.

