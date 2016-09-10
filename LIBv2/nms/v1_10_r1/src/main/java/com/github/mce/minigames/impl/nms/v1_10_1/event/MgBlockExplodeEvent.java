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

package com.github.mce.minigames.impl.nms.v1_10_1.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;

import com.github.mce.minigames.api.arena.rules.bevents.MinigameBlockExplodeEvent;
import com.github.mce.minigames.impl.nms.AbstractMinigameEvent;

/**
 * Minigame event implementation
 * 
 * @author mepeisen
 */
public class MgBlockExplodeEvent extends AbstractMinigameEvent<BlockExplodeEvent, MinigameBlockExplodeEvent> implements MinigameBlockExplodeEvent
{
    
    /**
     * Constructor
     * 
     * @param event
     */
    public MgBlockExplodeEvent(BlockExplodeEvent event)
    {
        super(event, null, event.getBlock().getLocation(), locations(event));
    }
    
    /**
     * Gets locations from affected blocks.
     * 
     * @param event
     * @return locations.
     */
    private static Location[] locations(BlockExplodeEvent event)
    {
        final Location[] result = new Location[event.blockList().size()];
        int i = 0;
        for (final Block block : event.blockList())
        {
            result[i] = block.getLocation();
            i++;
        }
        return result;
    }
    
}
