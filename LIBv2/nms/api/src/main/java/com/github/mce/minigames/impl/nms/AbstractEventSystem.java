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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.event.ArenaCreateEvent;
import com.github.mce.minigames.api.event.ArenaCreatedEvent;
import com.github.mce.minigames.api.event.ArenaDeleteEvent;
import com.github.mce.minigames.api.event.ArenaDeletedEvent;
import com.github.mce.minigames.api.event.ArenaMaintenanceEvent;
import com.github.mce.minigames.api.event.ArenaPlayerJoinedEvent;
import com.github.mce.minigames.api.event.ArenaPlayerJoinedQueueEvent;
import com.github.mce.minigames.api.event.ArenaPlayerLeavesQueueEvent;
import com.github.mce.minigames.api.event.ArenaStateEvent;
import com.github.mce.minigames.api.event.PlayerCloseGuiEvent;
import com.github.mce.minigames.api.event.PlayerDisplayGuiPageEvent;
import com.github.mce.minigames.api.event.PlayerGuiClickEvent;
import com.github.mce.minigames.api.event.PlayerOpenGuiEvent;

/**
 * Abstract base class for event systems.
 * 
 * @author mepeisen
 */
public abstract class AbstractEventSystem implements EventSystemInterface
{
    
    /**
     * The common event handlers per event class.
     */
    private final Map<Class<? extends Event>, MinigameEventHandler<?>> eventHandlers = new HashMap<>();
    
    /**
     * Constructor.
     */
    public AbstractEventSystem()
    {
        this.registerHandler(ArenaCreateEvent.class, (evt) -> new MgArenaCreateEvent(evt));
        this.registerHandler(ArenaCreatedEvent.class, (evt) -> new MgArenaCreatedEvent(evt));
        this.registerHandler(ArenaDeleteEvent.class, (evt) -> new MgArenaDeleteEvent(evt));
        this.registerHandler(ArenaDeletedEvent.class, (evt) -> new MgArenaDeletedEvent(evt));
        this.registerHandler(ArenaMaintenanceEvent.class, (evt) -> new MgArenaMaintenanceEvent(evt));
        this.registerHandler(ArenaPlayerJoinedEvent.class, (evt) -> new MgArenaPlayerJoinedEvent(evt));
        this.registerHandler(ArenaPlayerJoinedQueueEvent.class, (evt) -> new MgArenaPlayerJoinedQueueEvent(evt));
        this.registerHandler(ArenaPlayerLeavesQueueEvent.class, (evt) -> new MgArenaPlayerLeavesQueueEvent(evt));
        this.registerHandler(ArenaStateEvent.class, (evt) -> new MgArenaStateEvent(evt));
        this.registerHandler(PlayerGuiClickEvent.class, (evt) -> new MgPlayerGuiClickEvent(evt));
        this.registerHandler(PlayerCloseGuiEvent.class, (evt) -> new MgPlayerCloseGuiEvent(evt));
        this.registerHandler(PlayerDisplayGuiPageEvent.class, (evt) -> new MgPlayerDisplayGuiPageEvent(evt));
        this.registerHandler(PlayerOpenGuiEvent.class, (evt) -> new MgPlayerOpenGuiEvent(evt));
    }
    
    /**
     * Returns the minigame event handler for given class.
     * 
     * @param clazz
     *            event class.
     * @return event handler.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Event> MinigameEventHandler<T> getHandler(Class<T> clazz)
    {
        return (MinigameEventHandler<T>) this.eventHandlers.get(clazz);
    }
    
    /**
     * Registers the minigame event handler for given class.
     * 
     * @param clazz
     *            event class.
     * @param factory
     *            the factory to create minigame events.
     */
    protected <T extends Event> void registerHandler(Class<T> clazz, MinigameEventFactory<T> factory)
    {
        this.eventHandlers.put(clazz, new MinigameEventHandler<>(clazz, factory));
    }
    
    /**
     * Event handler for ArenaCreatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaCreated(ArenaCreatedEvent evt)
    {
        this.getHandler(ArenaCreatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaCreateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaCreate(ArenaCreateEvent evt)
    {
        this.getHandler(ArenaCreateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaDeletedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaDeleted(ArenaDeletedEvent evt)
    {
        this.getHandler(ArenaDeletedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaDeleteEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaDelete(ArenaDeleteEvent evt)
    {
        this.getHandler(ArenaDeleteEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaMaintenanceEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaMaintenance(ArenaMaintenanceEvent evt)
    {
        this.getHandler(ArenaMaintenanceEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaPlayerJoinedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaPlayerJoined(ArenaPlayerJoinedEvent evt)
    {
        this.getHandler(ArenaPlayerJoinedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaPlayerJoinedQueueEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaPlayerJoinedQueue(ArenaPlayerJoinedQueueEvent evt)
    {
        this.getHandler(ArenaPlayerJoinedQueueEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaPlayerLeavesQueueEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaPlayerLeavesQueue(ArenaPlayerLeavesQueueEvent evt)
    {
        this.getHandler(ArenaPlayerLeavesQueueEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ArenaStateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onArenaState(ArenaStateEvent evt)
    {
        this.getHandler(ArenaStateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerCloseGuiEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerCloseGui(PlayerCloseGuiEvent evt)
    {
        this.getHandler(PlayerCloseGuiEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerDisplayGuiPageEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerDisplayGuiPage(PlayerDisplayGuiPageEvent evt)
    {
        this.getHandler(PlayerDisplayGuiPageEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerGuiClickEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerClickGui(PlayerGuiClickEvent evt)
    {
        this.getHandler(PlayerGuiClickEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerOpenGuiEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerOpenGui(PlayerOpenGuiEvent evt)
    {
        this.getHandler(PlayerOpenGuiEvent.class).handle(evt);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <Evt extends Event> MinigameEvent<Evt> createEvent(Evt bukkitEvent)
    {
        return ((MinigameEventHandler<Evt>) this.getHandler(bukkitEvent.getClass())).createMgEvent(bukkitEvent);
    }
    
    /**
     * The minigame event handler.
     * 
     * @author mepeisen
     * @param <T>
     *            event clazz for handling the events.
     */
    protected final class MinigameEventHandler<T extends Event>
    {
        
        public MinigameEventHandler(Class<T> clazz, MinigameEventFactory<T> factory)
        {
            // TODO
        }
        
        /**
         * @param evt
         */
        public void handle(T evt)
        {
            // TODO Auto-generated method stub
        }
        
        public MinigameEvent<T> createMgEvent(T evt)
        {
            // TODO
            return null;
        }
        
    }
    
    /**
     * Interface for creating minigame event classes from given bukkit event.
     *
     * @param <Evt>
     */
    @FunctionalInterface
    public interface MinigameEventFactory<Evt extends Event>
    {
        
        /**
         * Creates the minigame event.
         * 
         * @param event
         *            bukkit event.
         * @return the minigame event object.
         */
        MinigameEvent<Evt> create(Evt event);
        
    }
    
}
