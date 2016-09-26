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

import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.rules.bevents.MinigameProjectileHitEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.AbstractMinigameEvent;

/**
 * Minigame event implementation
 * 
 * @author mepeisen
 */
public class MgProjectileHitEvent extends AbstractMinigameEvent<ProjectileHitEvent, MinigameProjectileHitEvent> implements MinigameProjectileHitEvent
{

    /**
     * Constructor
     * @param event
     */
    public MgProjectileHitEvent(ProjectileHitEvent event)
    {
        super(event, player(event), location(event));
    }

    /**
     * @param event
     * @return player
     */
    private static ArenaPlayerInterface player(ProjectileHitEvent event)
    {
        final ProjectileSource source = event.getEntity().getShooter();
        return source instanceof Player ? MglibInterface.INSTANCE.get().getPlayer((Player) source) : null;
    }

    /**
     * @param event
     * @return arena
     */
    private static ArenaInterface location(ProjectileHitEvent event)
    {
        final ProjectileSource source = event.getEntity().getShooter();
        if (source instanceof Player)
        {
            return null; // will force to calculate from player
        }
        return MglibInterface.INSTANCE.get().getArenaFromLocation(event.getEntity().getLocation());
    }
    
}
