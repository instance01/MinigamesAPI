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

package com.github.mce.minigames.impl.test.nms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.AbstractMinigameEvent;

/**
 * Test for {@link AbstractMinigameEvent}
 * 
 * @author mepeisen
 *
 */
public class AbstractMinigameEventTest
{
    
    /**
     * Tests constructors
     */
    @Test
    public void testConstructors()
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final MinigameInterface minigame = mock(MinigameInterface.class);
        when(arena.getMinigame()).thenReturn(minigame);
        final ArenaInterface parena = mock(ArenaInterface.class);
        final MinigameInterface pminigame = mock(MinigameInterface.class);
        when(parena.getMinigame()).thenReturn(pminigame);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        when(player.getArena()).thenReturn(parena);

        final ArenaEvent evt1 = new ArenaEvent(arena);
        final MgArenaEvent mgevt1 = new MgArenaEvent(evt1);
        assertEquals(evt1, mgevt1.getBukkitEvent());
        assertEquals(arena, mgevt1.getArena());
        assertEquals(minigame, mgevt1.getMinigame());
        assertNull(mgevt1.getPlayer());

        final ArenaPlayerEvent evt2 = new ArenaPlayerEvent(player, arena);
        final MgArenaPlayerEvent mgevt2 = new MgArenaPlayerEvent(evt2);
        assertEquals(evt2, mgevt2.getBukkitEvent());
        assertEquals(arena, mgevt2.getArena());
        assertEquals(minigame, mgevt2.getMinigame());
        assertEquals(player, mgevt2.getPlayer());

        final PlayerEvent evt3 = new PlayerEvent(player);
        final MgPlayerEvent mgevt3 = new MgPlayerEvent(evt3);
        assertEquals(evt3, mgevt3.getBukkitEvent());
        assertEquals(parena, mgevt3.getArena());
        assertEquals(pminigame, mgevt3.getMinigame());
        assertEquals(player, mgevt3.getPlayer());

        final MglibInterface lib = mock(MglibInterface.class);
        Whitebox.setInternalState(MglibInterface.INSTANCE.class, "CACHED", lib); //$NON-NLS-1$
        final Location loc = new Location(null, 1, 2, 3);
        when(lib.getArenaFromLocation(loc)).thenReturn(arena);
        final PlayerLocationEvent evt4 = new PlayerLocationEvent(player, loc);
        final MgPlayerLocationEvent mgevt4 = new MgPlayerLocationEvent(evt4);
        assertEquals(evt4, mgevt4.getBukkitEvent());
        assertEquals(arena, mgevt4.getArena());
        assertEquals(minigame, mgevt4.getMinigame());
        assertEquals(player, mgevt4.getPlayer());
    }
    
    /**
     * Tests when
     * @throws MinigameException 
     */
    @Test
    public void testWhen1() throws MinigameException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final MinigameInterface minigame = mock(MinigameInterface.class);
        when(arena.getMinigame()).thenReturn(minigame);
        
        final ArenaEvent evt1 = new ArenaEvent(arena);
        final MgArenaEvent mgevt1 = new MgArenaEvent(evt1);
        
        final AtomicBoolean result = new AtomicBoolean(true);
        mgevt1.when((e) -> true).then((e) -> result.set(false))._elseThrow(CommonErrors.CannotStart);
        assertFalse(result.get());
    }
    
    /**
     * Tests when
     * @throws MinigameException 
     */
    @Test
    public void testWhen2() throws MinigameException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final MinigameInterface minigame = mock(MinigameInterface.class);
        when(arena.getMinigame()).thenReturn(minigame);
        
        final ArenaEvent evt1 = new ArenaEvent(arena);
        final MgArenaEvent mgevt1 = new MgArenaEvent(evt1);
        
        final AtomicBoolean result = new AtomicBoolean(true);
        mgevt1.when((e) -> false).thenThrow(CommonErrors.CannotStart)._else((e) -> result.set(false));
        assertFalse(result.get());
    }

    /**
     * helper class
     */
    private static final class ArenaEvent extends Event
    {
        
        /** handlers list. */
        private static final HandlerList handlers = new HandlerList();
        
        /** the arena we created. */
        private final ArenaInterface     arena;
        
        /**
         * Constructor.
         * 
         * @param arena
         *            the created arena.
         */
        public ArenaEvent(ArenaInterface arena)
        {
            this.arena = arena;
        }
        
        /**
         * Returns the arena that was created
         * 
         * @return the created arena
         */
        public ArenaInterface getArena()
        {
            return this.arena;
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
    }

    /**
     * helper class
     */
    private static final class PlayerEvent extends Event
    {
        
        /** handlers list. */
        private static final HandlerList handlers = new HandlerList();
        
        /** the arena we created. */
        private final ArenaPlayerInterface     player;
        
        /**
         * Constructor.
         * 
         * @param player
         *            the created arena.
         */
        public PlayerEvent(ArenaPlayerInterface player)
        {
            this.player = player;
        }
        
        /**
         * Returns the arena that was created
         * 
         * @return the created arena
         */
        public ArenaPlayerInterface getPlayer()
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
    }

    /**
     * helper class
     */
    private static final class PlayerLocationEvent extends Event
    {
        
        /** handlers list. */
        private static final HandlerList handlers = new HandlerList();
        
        /** the arena we created. */
        private final ArenaPlayerInterface     player;

        /** the location. */
        private final Location location;
        
        /**
         * Constructor.
         * 
         * @param player
         *            the created arena.
         * @param location
         *            the created arena.
         */
        public PlayerLocationEvent(ArenaPlayerInterface player, Location location)
        {
            this.player = player;
            this.location = location;
        }
        
        /**
         * Returns the arena that was created
         * 
         * @return the created arena
         */
        public ArenaPlayerInterface getPlayer()
        {
            return this.player;
        }
        
        /**
         * Returns the arena that was created
         * 
         * @return the created arena
         */
        public Location getLocation()
        {
            return this.location;
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
    }

    /**
     * helper class
     */
    private static final class ArenaPlayerEvent extends Event
    {
        
        /** handlers list. */
        private static final HandlerList handlers = new HandlerList();
        
        /** the arena we created. */
        private final ArenaPlayerInterface     player;
        
        /** the arena we created. */
        private final ArenaInterface     arena;
        
        /**
         * Constructor.
         * 
         * @param player
         *            the created arena.
         * @param arena
         *            the created arena.
         */
        public ArenaPlayerEvent(ArenaPlayerInterface player, ArenaInterface arena)
        {
            this.player = player;
            this.arena = arena;
        }
        
        /**
         * Returns the arena that was created
         * 
         * @return the created arena
         */
        public ArenaPlayerInterface getPlayer()
        {
            return this.player;
        }
        
        /**
         * Returns the arena that was created
         * 
         * @return the created arena
         */
        public ArenaInterface getArena()
        {
            return this.arena;
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
    }
    
    /**
     * helper interface
     */
    private interface MinigameArenaEvent extends MinigameEvent<ArenaEvent, MinigameArenaEvent>
    {
        // empty
    }
    
    /**
     * helper interface
     */
    private interface MinigamePlayerEvent extends MinigameEvent<PlayerEvent, MinigamePlayerEvent>
    {
        // empty
    }
    
    /**
     * helper interface
     */
    private interface MinigamePlayerLocationEvent extends MinigameEvent<PlayerLocationEvent, MinigamePlayerLocationEvent>
    {
        // empty
    }
    
    /**
     * helper interface
     */
    private interface MinigameArenaPlayerEvent extends MinigameEvent<ArenaPlayerEvent, MinigameArenaPlayerEvent>
    {
        // empty
    }
    
    /**
     * helper class.
     */
    private static final class MgArenaEvent extends AbstractMinigameEvent<ArenaEvent, MinigameArenaEvent> implements MinigameArenaEvent
    {

        /**
         * @param event
         */
        public MgArenaEvent(ArenaEvent event)
        {
            super(event, event.getArena());
        }
        
    }
    
    /**
     * helper class.
     */
    private static final class MgArenaPlayerEvent extends AbstractMinigameEvent<ArenaPlayerEvent, MinigameArenaPlayerEvent> implements MinigameArenaPlayerEvent
    {

        /**
         * @param event
         */
        public MgArenaPlayerEvent(ArenaPlayerEvent event)
        {
            super(event, event.getPlayer(), event.getArena());
        }
        
    }
    
    /**
     * helper class.
     */
    private static final class MgPlayerEvent extends AbstractMinigameEvent<PlayerEvent, MinigamePlayerEvent> implements MinigamePlayerEvent
    {

        /**
         * @param event
         */
        public MgPlayerEvent(PlayerEvent event)
        {
            super(event, event.getPlayer());
        }
        
    }
    
    /**
     * helper class.
     */
    private static final class MgPlayerLocationEvent extends AbstractMinigameEvent<PlayerLocationEvent, MinigamePlayerLocationEvent> implements MinigamePlayerLocationEvent
    {

        /**
         * @param event
         */
        public MgPlayerLocationEvent(PlayerLocationEvent event)
        {
            super(event, event.getPlayer(), event.getLocation());
        }
        
    }
    
}
