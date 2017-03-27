/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mgapi.api.player;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;

/**
 * @author mepeisen
 *
 */
public interface ArenaPlayerInterface
{
    
    // common methods (player info)
    
    /**
     * Returns the bukkit player (if this player is online).
     * 
     * @return bukkit player.
     */
    default Player getBukkitPlayer()
    {
        return this.getMcPlayer().getBukkitPlayer();
    }
    
    /**
     * Returns the name of the player.
     * 
     * @return name of the player.
     */
    default String getName()
    {
        return this.getMcPlayer().getName();
    }
    
    /**
     * Returns the bukkit offline player.
     * 
     * @return bukkit offline player.
     */
    default OfflinePlayer getOfflinePlayer()
    {
        return this.getMcPlayer().getOfflinePlayer();
    }
    
    /**
     * Returns the mclib player.
     * 
     * @return mclib player.
     */
    McPlayerInterface getMcPlayer();
    
    /**
     * Returns the players uuid.
     * 
     * @return uuid.
     */
    default UUID getPlayerUUID()
    {
        return this.getMcPlayer().getPlayerUUID();
    }
    
    // arena data and match
    
    /**
     * Returns the arena this player is currently in; within a match or waiting lobby.
     * 
     * @return arena or {@code null} if this player is currently not within any arena.
     */
    ArenaInterface getArena();
    
    /**
     * Let the player die; does nothing if there is no game pending.
     * Adds a new kill to kill statistic and performs a kill.
     * @throws McException thrown if no match is running.
     */
    void die() throws McException;
    
    /**
     * Let the player die; does nothing if there is no game pending.
     * Adds a new kill to kill statistic and performs a kill while the killer gets a statistic bonus
     * for killing this player.
     * @param killer the opposite player causing the death
     * @throws McException thrown if no match is running.
     */
    void die(ArenaPlayerInterface killer) throws McException;
    
    /**
     * Let the player lose the game; does nothing if there is no game pending.
     * May cause to end the game (depending on gaming rules).
     * Identical to invoke {@link ArenaMatchInterface#setLoser(UUID...)} with this players uuid.
     * @throws McException thrown if no match is running.
     */
    void lose() throws McException;
    
    /**
     * Let the player win the game; does nothing if there is no game pending
     * May cause to end the game (depending on gaming rules).
     * Identical to invoke {@link ArenaMatchInterface#setWinner(UUID...)} with this players uuid.
     * @throws McException thrown if no match is running.
     */
    void win() throws McException;
    
    // stubbing
    
    /**
     * Checks this player for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the player matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws McException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    McOutgoingStubbing<ArenaPlayerInterface> when(McPredicate<ArenaPlayerInterface> test) throws McException;
    
    /**
     * Checks if the player is online
     * 
     * @return true if player is online
     */
    default boolean isOnline()
    {
        return this.getBukkitPlayer() != null;
    }
    
    /**
     * Checks if player is within arena
     * 
     * @return true if player is within arena
     */
    default boolean inArena()
    {
        return this.getArena() != null;
    }
    
    /**
     * Checks if player is within arena and if he is spectating
     * @return {@code true} if player is spectating an arena
     */
    boolean isSpectating();
    
    /**
     * Checks if player is within arena and if he is playing a match (or waiting in the lobby)
     * @return {@code true} if player is playing a match or waiting inside an arena
     */
    boolean isPlaying();
    
}
