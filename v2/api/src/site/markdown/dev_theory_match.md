# Minigames-Lib 2.0 - Development

## About the match internals

A minigame match is a gaming session within an arena.

There can only be one match in a single arena at the same time. The next match can only be started if the current one ends and the arena
was reseted. A player can only be present in one arena match at the same time. He cannot be in multiple matches.

## Match phases

A match can be made of several phases. Each phase contains own rule set.

In classic minigames from version 1 we have the following "match phases"

* Pre-Match: The players are ported to their spawns, no movement, match countdown...
* Match: the players are plaing...
* Post-Match: the players are back ported to the main lobby and the winning titles/ animations are played.

In version 2 we can add more phases to a match. Let us for example think of a pvp game. We will divide the match phase:

* Match: Normal PvP, the first one receiving 10 kills will win, a timer is running (4 minutes).
* Deatch-Math: If the timer ends (after 4 minutes) and no one wins a death-match is started. Now each kill lets you lose the game and the last man standing wins.


