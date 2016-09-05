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

import org.bukkit.event.Event;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * Base Implementation for minigame events.
 * 
 * @author mepeisen
 * @param <Evt> event class
 */
public abstract class AbstractMinigameEvent<Evt extends Event> implements MinigameEvent<Evt>
{
    
    /** the bukkit event object. */
    private Evt event;
    
    /** the player for this event. */
    private ArenaPlayerInterface player;

    /**
     * Abstract minigame event.
     * @param event the event.
     * @param player
     */
    public AbstractMinigameEvent(Evt event, ArenaPlayerInterface player)
    {
        this.event = event;
        this.player = player;
    }

    @Override
    public Evt getBukkitEvent()
    {
        return this.event;
    }

    @Override
    public MglibInterface getLib()
    {
        return MglibInterface.INSTANCE.get();
    }

    @Override
    public MinigameInterface getMinigame()
    {
        final ArenaInterface arena = this.player.getArena();
        return arena == null ? null : arena.getMinigame();
    }

    @Override
    public ArenaInterface getArena()
    {
        return this.player.getArena();
    }

    @Override
    public ArenaPlayerInterface getPlayer()
    {
        return this.player;
    }
    
}
