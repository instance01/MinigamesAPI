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

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.rules.bevents.MinigameVehicleDamageEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.AbstractMinigameEvent;

/**
 * Minigame event implementation
 * 
 * @author mepeisen
 */
public class MgVehicleDamageEvent extends AbstractMinigameEvent<VehicleDamageEvent, MinigameVehicleDamageEvent> implements MinigameVehicleDamageEvent
{

    /**
     * Constructor
     * @param event
     */
    public MgVehicleDamageEvent(VehicleDamageEvent event)
    {
        super(event, passenger(event), location(event));
    }

    /**
     * @param event
     * @return passanger
     */
    private static ArenaPlayerInterface passenger(VehicleDamageEvent event)
    {
        final Entity passenger = event.getVehicle().getPassenger();
        return passenger instanceof Player ? MglibInterface.INSTANCE.get().getPlayer((Player) passenger) : null;
    }

    /**
     * @param event
     * @return passanger
     */
    private static ArenaInterface location(VehicleDamageEvent event)
    {
        final Entity passenger = event.getVehicle().getPassenger();
        if (passenger instanceof Player)
        {
            return null; // will force to calculate from player
        }
        return MglibInterface.INSTANCE.get().getArenaFromLocation(event.getVehicle().getLocation());
    }
    
}
