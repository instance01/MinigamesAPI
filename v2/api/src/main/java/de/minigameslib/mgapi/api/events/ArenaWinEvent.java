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

import java.util.stream.Collectors;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface.MatchResult;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * One or more player were marked as winner
 * 
 * @author mepeisen
 * 
 * @see ArenaMatchInterface#setWinner(TeamIdType...)
 * @see ArenaMatchInterface#setWinner(java.util.UUID...)
 */
public class ArenaWinEvent extends Event implements MinecraftEvent<ArenaWinEvent, ArenaWinEvent>
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the arena instance. */
    private final ArenaInterface arena;
    
    /** the arena players. */
    private final MatchResult result;

    /**
     * @param arena
     * @param result
     */
    public ArenaWinEvent(ArenaInterface arena, MatchResult result)
    {
        this.arena = arena;
        this.result = result;
    }

    /**
     * @return the arena
     */
    public ArenaInterface getArena()
    {
        return this.arena;
    }

    /**
     * @return the match result
     */
    public MatchResult getMatchResult()
    {
        return this.result;
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
    public ArenaWinEvent getBukkitEvent()
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
        return ObjectServiceInterface.instance().getPlayer(this.result.getPlayers().iterator().next());
    }

    @Override
    public Iterable<McPlayerInterface> getPlayers()
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.result.getPlayers().stream().map(osi::getPlayer).collect(Collectors.toList());
    }

    @Override
    public McOutgoingStubbing<ArenaWinEvent> when(McPredicate<ArenaWinEvent> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
