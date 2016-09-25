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

import com.github.mce.minigames.api.arena.rules.MinigameEvent;

/**
 * Helper interface for minigame event listeners.
 * 
 * @author mepeisen
 */
public interface MgEventListener
{
    
    /**
     * Handles given event.
     * @param eventClass
     * @param event
     */
    void handle(Class<?> eventClass, MinigameEvent<?, ?> event);
    
}
