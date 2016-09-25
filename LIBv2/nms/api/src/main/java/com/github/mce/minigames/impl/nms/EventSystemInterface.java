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

package com.github.mce.minigames.impl.nms;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.github.mce.minigames.api.arena.rules.MinigameEvent;

/**
 * Generic event system.
 * 
 * @author mepeisen
 */
public interface EventSystemInterface extends Listener
{
    
    /**
     * Creates a minigame event from given bukkit event.
     * 
     * @param bukkitEvent
     * @return minigame event.
     */
    <Evt extends Event, MgEvt extends MinigameEvent<Evt, MgEvt>> MgEvt createEvent(Evt bukkitEvent);
    
    /**
     * Adds a new event listener.
     * @param listener
     */
    void addEventListener(MgEventListener listener);
    
}
