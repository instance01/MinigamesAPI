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

package com.github.mce.minigames.api.arena.rules.mevents;

import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.event.ArenaPlayerLeavesQueueEvent;

/**
 * Minigame event representation of corresponding minigame event.
 * 
 * <h3>Using this event as a rule</h3>
 * 
 * <dl>
 * <dt>Player-Rule</dt>
 * <dd>Passed to the affected player</dd>
 * <dt>Arena-Rule</dt>
 * <dd>not supported</dd>
 * <dt>Minigame-Rule</dt>
 * <dd>not supported</dd>
 * </dl>
 * 
 * @author mepeisen
 */
public interface MinigameArenaPlayerLeavesQueueEvent extends MinigameEvent<ArenaPlayerLeavesQueueEvent, MinigameArenaPlayerLeavesQueueEvent>
{
    
    // TODO default stubbings
    
}
