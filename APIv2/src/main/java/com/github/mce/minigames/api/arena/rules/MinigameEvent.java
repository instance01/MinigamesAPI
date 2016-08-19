/*
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

package com.github.mce.minigames.api.arena.rules;

import org.bukkit.event.Event;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * Minigame event helper.
 * 
 * @author mepeisen
 * 
 * @param <Evt> Event class
 */
public interface MinigameEvent<Evt extends Event>
{
    
    /**
     * Returns the original event
     * @return original event this rule 
     */
    Evt getBukkitEvent();
    
    /**
     * Returns the library API.
     * @return fast access to library API.
     */
    MglibInterface getLib();
    
    /**
     * Returns the minigame (if any) associated with this event.
     * @return minigame interface or {@code null} if this event was outside any minigame.
     */
    MinigameInterface getMinigame();
    
    /**
     * Returns the arena causing this event.
     * @return arena causing this event or {@code null} if this event was outside any arena.
     */
    ArenaInterface getArena();
    
    /**
     * Returns the player causing this event.
     * @return player causing this event or {@code null} if this event was not caused by any player.
     */
    ArenaPlayerInterface getPlayer();
    
    // TODO default stubbings
    
}
