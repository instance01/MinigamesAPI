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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.bukkit.event.Event;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaState;
import com.github.mce.minigames.api.arena.WaitQueue;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.event.ArenaCreateEvent;
import com.github.mce.minigames.api.event.ArenaCreatedEvent;
import com.github.mce.minigames.api.event.ArenaDeleteEvent;
import com.github.mce.minigames.api.event.ArenaDeletedEvent;
import com.github.mce.minigames.api.event.ArenaMaintenanceEvent;
import com.github.mce.minigames.api.event.ArenaPlayerJoinEvent;
import com.github.mce.minigames.api.event.ArenaPlayerJoinedEvent;
import com.github.mce.minigames.api.event.ArenaPlayerJoinedQueueEvent;
import com.github.mce.minigames.api.event.ArenaPlayerLeavesQueueEvent;
import com.github.mce.minigames.api.event.ArenaStateEvent;
import com.github.mce.minigames.api.event.PlayerCloseGuiEvent;
import com.github.mce.minigames.api.event.PlayerDisplayGuiPageEvent;
import com.github.mce.minigames.api.event.PlayerGuiClickEvent;
import com.github.mce.minigames.api.event.PlayerOpenGuiEvent;
import com.github.mce.minigames.api.gui.ClickGuiInterface;
import com.github.mce.minigames.api.gui.ClickGuiItem;
import com.github.mce.minigames.api.gui.ClickGuiPageInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.impl.nms.AbstractEventSystem;
import com.github.mce.minigames.impl.nms.MgEventListener;

/**
 * Test for {@link AbstractEventSystem}
 * 
 * @author mepeisen
 *
 */
public class AbstractEventSystemTest
{
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaStateEvent()
    {
        this.testMgListenerCalled(ArenaStateEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaStateEvent(evt.arena, ArenaState.Disabled, ArenaState.InGame), (sys, evt) -> sys.onArenaState(evt));
        this.testArenaListenerCalled(ArenaStateEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaStateEvent(evt.arena, ArenaState.Disabled, ArenaState.InGame), (sys, evt) -> sys.onArenaState(evt));
        this.testMinigameListenerCalled(ArenaStateEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaStateEvent(evt.arena, ArenaState.Disabled, ArenaState.InGame), (sys, evt) -> sys.onArenaState(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaCreateEvent()
    {
        this.testMgListenerCalled(ArenaCreateEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaCreateEvent(evt.arena), (sys, evt) -> sys.onArenaCreate(evt));
        this.testArenaListenerCalled(ArenaCreateEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaCreateEvent(evt.arena), (sys, evt) -> sys.onArenaCreate(evt));
        this.testMinigameListenerCalled(ArenaCreateEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaCreateEvent(evt.arena), (sys, evt) -> sys.onArenaCreate(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaCreatedEvent()
    {
        this.testMgListenerCalled(ArenaCreatedEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaCreatedEvent(evt.arena), (sys, evt) -> sys.onArenaCreated(evt));
        this.testArenaListenerCalled(ArenaCreatedEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaCreatedEvent(evt.arena), (sys, evt) -> sys.onArenaCreated(evt));
        this.testMinigameListenerCalled(ArenaCreatedEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaCreatedEvent(evt.arena), (sys, evt) -> sys.onArenaCreated(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaDeleteEvent()
    {
        this.testMgListenerCalled(ArenaDeleteEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaDeleteEvent(evt.arena), (sys, evt) -> sys.onArenaDelete(evt));
        this.testArenaListenerCalled(ArenaDeleteEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaDeleteEvent(evt.arena), (sys, evt) -> sys.onArenaDelete(evt));
        this.testMinigameListenerCalled(ArenaDeleteEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaDeleteEvent(evt.arena), (sys, evt) -> sys.onArenaDelete(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaDeletedEvent()
    {
        this.testMgListenerCalled(ArenaDeletedEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaDeletedEvent(evt.arena), (sys, evt) -> sys.onArenaDeleted(evt));
        this.testArenaListenerCalled(ArenaDeletedEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaDeletedEvent(evt.arena), (sys, evt) -> sys.onArenaDeleted(evt));
        this.testMinigameListenerCalled(ArenaDeletedEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaDeletedEvent(evt.arena), (sys, evt) -> sys.onArenaDeleted(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaMaintenanceEvent()
    {
        this.testMgListenerCalled(ArenaMaintenanceEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaMaintenanceEvent(evt.arena), (sys, evt) -> sys.onArenaMaintenance(evt));
        this.testArenaListenerCalled(ArenaMaintenanceEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaMaintenanceEvent(evt.arena), (sys, evt) -> sys.onArenaMaintenance(evt));
        this.testMinigameListenerCalled(ArenaMaintenanceEvent.class, (evt) -> evt.arena, (evt) -> null, (evt) -> evt.minigame, (evt) -> new ArenaMaintenanceEvent(evt.arena), (sys, evt) -> sys.onArenaMaintenance(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaPlayerJoinedEvent()
    {
        this.testMgListenerCalled(ArenaPlayerJoinedEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinedEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoined(evt));
        this.testArenaListenerCalled(ArenaPlayerJoinedEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinedEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoined(evt));
        this.testPlayerListenerCalled(ArenaPlayerJoinedEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinedEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoined(evt));
        this.testMinigameListenerCalled(ArenaPlayerJoinedEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinedEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoined(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaPlayerJoinEvent()
    {
        this.testMgListenerCalled(ArenaPlayerJoinEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoin(evt));
        this.testArenaListenerCalled(ArenaPlayerJoinEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoin(evt));
        this.testPlayerListenerCalled(ArenaPlayerJoinEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoin(evt));
        this.testMinigameListenerCalled(ArenaPlayerJoinEvent.class, (evt) -> evt.arena, (evt) -> evt.player, (evt) -> evt.minigame, (evt) -> new ArenaPlayerJoinEvent(evt.arena, evt.player), (sys, evt) -> sys.onArenaPlayerJoin(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaPlayerJoinedQueueEvent()
    {
        this.testMgListenerCalled(ArenaPlayerJoinedQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerJoinedQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerJoinedQueue(evt));
        this.testPArenaListenerCalled(ArenaPlayerJoinedQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerJoinedQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerJoinedQueue(evt));
        this.testPlayerListenerCalled(ArenaPlayerJoinedQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerJoinedQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerJoinedQueue(evt));
        this.testPMinigameListenerCalled(ArenaPlayerJoinedQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerJoinedQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerJoinedQueue(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testArenaPlayerLeavesQueueEvent()
    {
        this.testMgListenerCalled(ArenaPlayerLeavesQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerLeavesQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerLeavesQueue(evt));
        this.testPArenaListenerCalled(ArenaPlayerLeavesQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerLeavesQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerLeavesQueue(evt));
        this.testPlayerListenerCalled(ArenaPlayerLeavesQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerLeavesQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerLeavesQueue(evt));
        this.testPMinigameListenerCalled(ArenaPlayerLeavesQueueEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new ArenaPlayerLeavesQueueEvent(mock(WaitQueue.class), evt.player), (sys, evt) -> sys.onArenaPlayerLeavesQueue(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testPlayerCloseGuiEvent()
    {
        this.testMgListenerCalled(PlayerCloseGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerCloseGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerCloseGui(evt));
        this.testPArenaListenerCalled(PlayerCloseGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerCloseGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerCloseGui(evt));
        this.testPlayerListenerCalled(PlayerCloseGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerCloseGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerCloseGui(evt));
        this.testPMinigameListenerCalled(PlayerCloseGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerCloseGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerCloseGui(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testPlayerOpenGuiEvent()
    {
        this.testMgListenerCalled(PlayerOpenGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerOpenGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerOpenGui(evt));
        this.testPArenaListenerCalled(PlayerOpenGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerOpenGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerOpenGui(evt));
        this.testPlayerListenerCalled(PlayerOpenGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerOpenGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerOpenGui(evt));
        this.testPMinigameListenerCalled(PlayerOpenGuiEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerOpenGuiEvent(mock(ClickGuiInterface.class), evt.player), (sys, evt) -> sys.onPlayerOpenGui(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testPlayerDisplayGuiPageEvent()
    {
        this.testMgListenerCalled(PlayerDisplayGuiPageEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerDisplayGuiPageEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiPageInterface.class)), (sys, evt) -> sys.onPlayerDisplayGuiPage(evt));
        this.testPArenaListenerCalled(PlayerDisplayGuiPageEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerDisplayGuiPageEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiPageInterface.class)), (sys, evt) -> sys.onPlayerDisplayGuiPage(evt));
        this.testPlayerListenerCalled(PlayerDisplayGuiPageEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerDisplayGuiPageEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiPageInterface.class)), (sys, evt) -> sys.onPlayerDisplayGuiPage(evt));
        this.testPMinigameListenerCalled(PlayerDisplayGuiPageEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerDisplayGuiPageEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiPageInterface.class)), (sys, evt) -> sys.onPlayerDisplayGuiPage(evt));
    }
    
    /**
     * Tests that listeners are called.
     */
    @Test
    public void testPlayerGuiClickEvent()
    {
        this.testMgListenerCalled(PlayerGuiClickEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerGuiClickEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiItem.class)), (sys, evt) -> sys.onPlayerGuiClick(evt));
        this.testPArenaListenerCalled(PlayerGuiClickEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerGuiClickEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiItem.class)), (sys, evt) -> sys.onPlayerGuiClick(evt));
        this.testPlayerListenerCalled(PlayerGuiClickEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerGuiClickEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiItem.class)), (sys, evt) -> sys.onPlayerGuiClick(evt));
        this.testPMinigameListenerCalled(PlayerGuiClickEvent.class, (evt) -> evt.parena, (evt) -> evt.player, (evt) -> evt.pminigame, (evt) -> new PlayerGuiClickEvent(mock(ClickGuiInterface.class), evt.player, mock(ClickGuiItem.class)), (sys, evt) -> sys.onPlayerGuiClick(evt));
    }
    
    /**
     * Tests that mg listener is called.
     * @param clazz 
     * @param arena 
     * @param player 
     * @param minigame 
     * @param eventFactory 
     * @param eventCaller 
     */
    private <Evt extends Event> void testMgListenerCalled(
            final Class<Evt> clazz,
            final Function<EvtHelper, ArenaInterface> arena,
            final Function<EvtHelper, ArenaPlayerInterface> player,
            final Function<EvtHelper, MinigameInterface> minigame,
            final Function<EvtHelper, Evt> eventFactory,
            final BiConsumer<EventSystem, Evt> eventCaller)
    {
        final EvtHelper helper = new EvtHelper();
        
        final EventSystem sys = new EventSystem();
        final AtomicBoolean result = new AtomicBoolean(false);
        sys.addEventListener(new MgEventListener() {
            
            @Override
            public void handle(Class<?> eventClass, MinigameEvent<?, ?> event)
            {
                assertEquals(clazz, eventClass);
                assertEquals(arena.apply(helper), event.getArena());
                assertEquals(player.apply(helper), event.getPlayer());
                assertEquals(minigame.apply(helper), event.getMinigame());
                result.set(true);
            }
        });
        
        eventCaller.accept(sys, eventFactory.apply(helper));
        
        assertTrue(result.get());
    }
    
    /**
     * Tests that player listener is called.
     * @param clazz 
     * @param arena 
     * @param player 
     * @param minigame 
     * @param eventFactory 
     * @param eventCaller 
     */
    private <Evt extends Event> void testPlayerListenerCalled(
            final Class<Evt> clazz,
            final Function<EvtHelper, ArenaInterface> arena,
            final Function<EvtHelper, ArenaPlayerInterface> player,
            final Function<EvtHelper, MinigameInterface> minigame,
            final Function<EvtHelper, Evt> eventFactory,
            final BiConsumer<EventSystem, Evt> eventCaller)
    {
        final EvtHelper helper = new EvtHelper();
        
        final EventSystem sys = new EventSystem();
        final AtomicBoolean result = new AtomicBoolean(false);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                final Class<?> eventClass = invocation.getArgumentAt(0, Class.class);
                final MinigameEvent<?, ?> event = invocation.getArgumentAt(1, MinigameEvent.class);
                assertEquals(clazz, eventClass);
                assertEquals(arena.apply(helper), event.getArena());
                assertEquals(player.apply(helper), event.getPlayer());
                assertEquals(minigame.apply(helper), event.getMinigame());
                result.set(true);
                return null;
            }}).when((MgEventListener) helper.player).handle(anyObject(), anyObject());
        
        eventCaller.accept(sys, eventFactory.apply(helper));
        
        assertTrue(result.get());
    }
    
    /**
     * Tests that minigame listener is called.
     * @param clazz 
     * @param arena 
     * @param player 
     * @param minigame 
     * @param eventFactory 
     * @param eventCaller 
     */
    private <Evt extends Event> void testMinigameListenerCalled(
            final Class<Evt> clazz,
            final Function<EvtHelper, ArenaInterface> arena,
            final Function<EvtHelper, ArenaPlayerInterface> player,
            final Function<EvtHelper, MinigameInterface> minigame,
            final Function<EvtHelper, Evt> eventFactory,
            final BiConsumer<EventSystem, Evt> eventCaller)
    {
        final EvtHelper helper = new EvtHelper();
        
        final EventSystem sys = new EventSystem();
        final AtomicBoolean result = new AtomicBoolean(false);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                final Class<?> eventClass = invocation.getArgumentAt(0, Class.class);
                final MinigameEvent<?, ?> event = invocation.getArgumentAt(1, MinigameEvent.class);
                assertEquals(clazz, eventClass);
                assertEquals(arena.apply(helper), event.getArena());
                assertEquals(player.apply(helper), event.getPlayer());
                assertEquals(minigame.apply(helper), event.getMinigame());
                result.set(true);
                return null;
            }}).when((MgEventListener) helper.minigame).handle(anyObject(), anyObject());
        
        eventCaller.accept(sys, eventFactory.apply(helper));
        
        assertTrue(result.get());
    }
    
    /**
     * Tests that minigame listener is called.
     * @param clazz 
     * @param arena 
     * @param player 
     * @param minigame 
     * @param eventFactory 
     * @param eventCaller 
     */
    private <Evt extends Event> void testPMinigameListenerCalled(
            final Class<Evt> clazz,
            final Function<EvtHelper, ArenaInterface> arena,
            final Function<EvtHelper, ArenaPlayerInterface> player,
            final Function<EvtHelper, MinigameInterface> minigame,
            final Function<EvtHelper, Evt> eventFactory,
            final BiConsumer<EventSystem, Evt> eventCaller)
    {
        final EvtHelper helper = new EvtHelper();
        
        final EventSystem sys = new EventSystem();
        final AtomicBoolean result = new AtomicBoolean(false);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                final Class<?> eventClass = invocation.getArgumentAt(0, Class.class);
                final MinigameEvent<?, ?> event = invocation.getArgumentAt(1, MinigameEvent.class);
                assertEquals(clazz, eventClass);
                assertEquals(arena.apply(helper), event.getArena());
                assertEquals(player.apply(helper), event.getPlayer());
                assertEquals(minigame.apply(helper), event.getMinigame());
                result.set(true);
                return null;
            }}).when((MgEventListener) helper.pminigame).handle(anyObject(), anyObject());
        
        eventCaller.accept(sys, eventFactory.apply(helper));
        
        assertTrue(result.get());
    }
    
    /**
     * Tests that arena listener is called.
     * @param clazz 
     * @param arena 
     * @param player 
     * @param minigame 
     * @param eventFactory 
     * @param eventCaller 
     */
    private <Evt extends Event> void testArenaListenerCalled(
            final Class<Evt> clazz,
            final Function<EvtHelper, ArenaInterface> arena,
            final Function<EvtHelper, ArenaPlayerInterface> player,
            final Function<EvtHelper, MinigameInterface> minigame,
            final Function<EvtHelper, Evt> eventFactory,
            final BiConsumer<EventSystem, Evt> eventCaller)
    {
        final EvtHelper helper = new EvtHelper();
        
        final EventSystem sys = new EventSystem();
        final AtomicBoolean result = new AtomicBoolean(false);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                final Class<?> eventClass = invocation.getArgumentAt(0, Class.class);
                final MinigameEvent<?, ?> event = invocation.getArgumentAt(1, MinigameEvent.class);
                assertEquals(clazz, eventClass);
                assertEquals(arena.apply(helper), event.getArena());
                assertEquals(player.apply(helper), event.getPlayer());
                assertEquals(minigame.apply(helper), event.getMinigame());
                result.set(true);
                return null;
            }}).when((MgEventListener) helper.arena).handle(anyObject(), anyObject());
        
        eventCaller.accept(sys, eventFactory.apply(helper));
        
        assertTrue(result.get());
    }
    
    /**
     * Tests that arena listener is called.
     * @param clazz 
     * @param arena 
     * @param player 
     * @param minigame 
     * @param eventFactory 
     * @param eventCaller 
     */
    private <Evt extends Event> void testPArenaListenerCalled(
            final Class<Evt> clazz,
            final Function<EvtHelper, ArenaInterface> arena,
            final Function<EvtHelper, ArenaPlayerInterface> player,
            final Function<EvtHelper, MinigameInterface> minigame,
            final Function<EvtHelper, Evt> eventFactory,
            final BiConsumer<EventSystem, Evt> eventCaller)
    {
        final EvtHelper helper = new EvtHelper();
        
        final EventSystem sys = new EventSystem();
        final AtomicBoolean result = new AtomicBoolean(false);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                final Class<?> eventClass = invocation.getArgumentAt(0, Class.class);
                final MinigameEvent<?, ?> event = invocation.getArgumentAt(1, MinigameEvent.class);
                assertEquals(clazz, eventClass);
                assertEquals(arena.apply(helper), event.getArena());
                assertEquals(player.apply(helper), event.getPlayer());
                assertEquals(minigame.apply(helper), event.getMinigame());
                result.set(true);
                return null;
            }}).when((MgEventListener) helper.parena).handle(anyObject(), anyObject());
        
        eventCaller.accept(sys, eventFactory.apply(helper));
        
        assertTrue(result.get());
    }
    
    /**
     * Helper class
     */
    private static final class EvtHelper
    {
        
        /** data. */
        public final ArenaInterface arena;
        /** data. */
        public final MinigameInterface minigame;
        /** data. */
        public final ArenaInterface parena;
        /** data. */
        public final MinigameInterface pminigame;
        /** data. */
        public final ArenaPlayerInterface player;

        /**
         * Constructor
         */
        public EvtHelper()
        {
            this.arena = mock(ArenaInterface.class, withSettings().extraInterfaces(MgEventListener.class));
            this.minigame = mock(MinigameInterface.class, withSettings().extraInterfaces(MgEventListener.class));
            when(this.arena.getMinigame()).thenReturn(this.minigame);
            this.parena = mock(ArenaInterface.class, withSettings().extraInterfaces(MgEventListener.class));
            this.pminigame = mock(MinigameInterface.class, withSettings().extraInterfaces(MgEventListener.class));
            when(this.parena.getMinigame()).thenReturn(this.pminigame);
            this.player = mock(ArenaPlayerInterface.class, withSettings().extraInterfaces(MgEventListener.class));
            when(this.player.getArena()).thenReturn(this.parena);
        }
        
    }

    /**
     * helper class
     */
    private static final class EventSystem extends AbstractEventSystem
    {
        
        /**
         * Constructor
         */
        public EventSystem()
        {
            // empty
        }
        
    }
    
}
