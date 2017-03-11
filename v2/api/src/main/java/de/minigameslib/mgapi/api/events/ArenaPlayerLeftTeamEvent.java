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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
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
 * Event fired after a player left a team.
 * 
 * @author mepeisen
 * 
 * @see ArenaMatchInterface#leave(ArenaPlayerInterface, TeamIdType)
 */
public class ArenaPlayerLeftTeamEvent extends Event implements MinecraftEvent<ArenaPlayerLeftTeamEvent, ArenaPlayerLeftTeamEvent>
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the arena instance. */
    private final ArenaInterface arena;
    
    /** the arena player. */
    private final ArenaPlayerInterface player;
    
    /** pre selected team. */
    private final TeamIdType team; 

    /**
     * @param arena
     * @param player
     * @param team
     */
    public ArenaPlayerLeftTeamEvent(ArenaInterface arena, ArenaPlayerInterface player, TeamIdType team)
    {
        this.arena = arena;
        this.player = player;
        this.team = team;
    }

    /**
     * @return the team
     */
    public TeamIdType getTeam()
    {
        return this.team;
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
    public ArenaPlayerLeftTeamEvent getBukkitEvent()
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
    public McOutgoingStubbing<ArenaPlayerLeftTeamEvent> when(McPredicate<ArenaPlayerLeftTeamEvent> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
