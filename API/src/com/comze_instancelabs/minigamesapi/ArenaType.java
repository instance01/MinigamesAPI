package com.comze_instancelabs.minigamesapi;

public enum ArenaType {

    /**
     * Standard arena with lobby + spawn and lobby countdown; can have multiple spawns too
     */
    DEFAULT,

    /**
     * Players just join the game whenever they like, no lobby countdowns or arena/sign states; doesn't allow multiple spawns
     */
    JUMPNRUN,

    /**
     * Default arena + automatic regeneration
     */
    REGENERATION

}
