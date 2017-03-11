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

package de.minigameslib.mgapi.api.events;

import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.AbstractVetoEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Event fired before an arena player joined.
 * 
 * @author mepeisen
 * 
 * @see ArenaMatchInterface#join(ArenaPlayerInterface, TeamIdType)
 * @see ArenaInterface#join(ArenaPlayerInterface)
 */
public class ArenaPlayerJoinEvent extends AbstractVetoEvent implements MinecraftEvent<ArenaPlayerJoinEvent, ArenaPlayerJoinEvent>
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the arena instance. */
    private final ArenaInterface arena;
    
    /** the arena player. */
    private final ArenaPlayerInterface player;
    
    /** the pre-selected team */
    private TeamIdType preSelectedTeam;

    /**
     * @param arena
     * @param player
     * @param preSelectedTeam
     */
    public ArenaPlayerJoinEvent(ArenaInterface arena, ArenaPlayerInterface player, TeamIdType preSelectedTeam)
    {
        this.arena = arena;
        this.player = player;
        this.preSelectedTeam = preSelectedTeam;
    }

    /**
     * @return the arena
     */
    public ArenaInterface getArena()
    {
        return this.arena;
    }

    /**
     * @return the arena player
     */
    public ArenaPlayerInterface getArenaPlayer()
    {
        return this.player;
    }

    /**
     * @return the preSelectedTeam
     */
    public TeamIdType getPreSelectedTeam()
    {
        return this.preSelectedTeam;
    }

    /**
     * @param preSelectedTeam the preSelectedTeam to set
     */
    public void setPreSelectedTeam(TeamIdType preSelectedTeam)
    {
        this.preSelectedTeam = preSelectedTeam;
    }

    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override
    public ArenaPlayerJoinEvent getBukkitEvent()
    {
        return this;
    }

    @Override
    public ObjectInterface getObject()
    {
        return this.arena.getObject();
    }

    @Override
    public McPlayerInterface getPlayer()
    {
        return this.player.getMcPlayer();
    }

    @Override
    public McOutgoingStubbing<ArenaPlayerJoinEvent> when(McPredicate<ArenaPlayerJoinEvent> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
