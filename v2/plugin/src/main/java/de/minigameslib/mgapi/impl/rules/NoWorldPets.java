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

package de.minigameslib.mgapi.impl.rules;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McCreatureSpawnEvent;
import de.minigameslib.mclib.api.event.McEntityTeleportEvent;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.AbstractZoneRule;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
public class NoWorldPets extends AbstractZoneRule
{
    
    /**
     * @param type
     * @param zone
     * @throws McException thrown if config is invalid
     */
    public NoWorldPets(ZoneRuleSetType type, ArenaZoneHandler zone) throws McException
    {
        super(type, zone);
    }
    
    /**
     * Invoked on admin spawn events within this zone
     * @param evt
     */
    @McEventHandler
    public void onPetSpawn(McCreatureSpawnEvent evt)
    {
        final LivingEntity entity = evt.getBukkitEvent().getEntity();
        if (entity instanceof Animals)
        {
            // TODO check for minigames forced spawns
            evt.getBukkitEvent().setCancelled(true);
        }
    }
    
    /**
     * Invoked on animal teleports
     * @param evt
     */
    @McEventHandler
    public void onPetTeleport(McEntityTeleportEvent evt)
    {
        final Entity entity = evt.getBukkitEvent().getEntity();
        if (entity instanceof Animals)
        {
            if (this.zone.getZone().containsLoc(evt.getBukkitEvent().getTo()))
            {
                // this zone is target zone
                // TODO check for minigames forced teleports
                evt.getBukkitEvent().setCancelled(true);
            }
        }
    }
    
}
