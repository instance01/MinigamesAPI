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

import org.bukkit.Location;
import org.bukkit.event.Event;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.FalseStub;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;
import com.github.mce.minigames.api.util.function.TrueStub;

/**
 * Base Implementation for minigame events.
 * 
 * @author mepeisen
 * @param <Evt> event class
 * @param <MgEvt> minigame event class
 */
public abstract class AbstractMinigameEvent<Evt extends Event, MgEvt extends MinigameEvent<Evt, MgEvt>> implements MinigameEvent<Evt, MgEvt>
{
    
    /** the bukkit event object. */
    private Evt event;
    
    /** the player for this event. */
    private ArenaPlayerInterface player;
    
    /** the arena interface. */
    private ArenaInterface arena;

    /**
     * Abstract minigame event.
     * @param event the event.
     * @param player
     */
    public AbstractMinigameEvent(Evt event, ArenaPlayerInterface player)
    {
        this.event = event;
        this.player = player;
        this.arena = player == null ? null : this.player.getArena();
    }

    /**
     * Abstract minigame event.
     * @param event the event.
     * @param player
     * @param arena
     */
    public AbstractMinigameEvent(Evt event, ArenaPlayerInterface player, ArenaInterface arena)
    {
        this.event = event;
        this.player = player;
        this.arena = arena;
    }

    /**
     * Abstract minigame event.
     * @param event the event.
     * @param player
     * @param location
     */
    public AbstractMinigameEvent(Evt event, ArenaPlayerInterface player, Location location)
    {
        this.event = event;
        this.player = player;
        this.arena = this.getLib().getArenaFromLocation(location);
    }

    /**
     * Abstract minigame event.
     * @param event the event.
     * @param player
     * @param location
     * @param affectedLocations
     */
    public AbstractMinigameEvent(Evt event, ArenaPlayerInterface player, Location location, Location[] affectedLocations)
    {
        this.event = event;
        this.player = player;
        this.arena = this.getLib().getArenaFromLocation(location);
        // TODO
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
        return this.arena == null ? null : this.arena.getMinigame();
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

    @SuppressWarnings("unchecked")
    @Override
    public MgOutgoingStubbing<MgEvt> when(MgPredicate<MgEvt> test) throws MinigameException
    {
        if (test.test((MgEvt) this))
        {
            return new TrueStub<>((MgEvt) this);
        }
        return new FalseStub<>((MgEvt) this);
    }
    
}
