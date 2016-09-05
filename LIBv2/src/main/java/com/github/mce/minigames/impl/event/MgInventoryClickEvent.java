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

package com.github.mce.minigames.impl.event;

import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.mce.minigames.api.arena.rules.bevents.MinigameInventoryClickEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * @author mepeisen
 *
 */
public class MgInventoryClickEvent extends AbstractMinigameEvent<InventoryClickEvent> implements MinigameInventoryClickEvent
{

    /**
     * @param event
     * @param player
     */
    public MgInventoryClickEvent(InventoryClickEvent event, ArenaPlayerInterface player)
    {
        super(event, player);
    }
    
}
