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

package com.github.mce.minigames.impl.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.mce.minigames.api.ContextHandlerInterface;
import com.github.mce.minigames.api.MinigameContext;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.cmd.CommandInterface;

/**
 * Implementation of minigame context.
 * 
 * @author mepeisen
 */
public class MinigameContextImpl implements MinigameContext
{
    
    /** the registered context handlers. */
    private final Map<Class<?>, List<ContextHandlerInterface<?>>> handlers = new HashMap<>();
    
    /** the thread local storage. */
    private final ThreadLocal<TLD> tls = ThreadLocal.withInitial(() -> new TLD());
    
    /**
     * Registers a context handler to calculate context variables.
     * 
     * @param clazz
     *            context class.
     * @param handler
     *            the context handler.
     * @throws MinigameException
     *             thrown if the class to register is already registered.
     * @param <T>
     *            context class to register
     */
    public <T> void registerContextHandler(Class<T> clazz, ContextHandlerInterface<T> handler) throws MinigameException
    {
        this.handlers.computeIfAbsent(clazz, (key) -> new ArrayList<>()).add(handler);
    }
    
    /**
     * Runs given runnable with clean context.
     * @param command
     * @param runnable
     */
    public void runInContext(CommandInterface command, Runnable runnable)
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = command;
            tld.event = null;
            runnable.run();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }
    
    /**
     * Runs given runnable with clean context.
     * @param event
     * @param runnable
     */
    public void runInContext(MinigameEvent<?> event, Runnable runnable)
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = null;
            tld.event = event;
            runnable.run();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getContext(Class<T> clazz)
    {
        final TLD data = this.tls.get();
        if (data.containsKey(clazz))
        {
            return (T) data.get(clazz);
        }
        if (data.computeStack.contains(clazz))
        {
            return null;
        }
        data.computeStack.add(clazz);
        try
        {
            if (this.handlers.containsKey(clazz))
            {
                for (final ContextHandlerInterface<?> handler : this.handlers.get(clazz))
                {
                    Object result = null;
                    if (data.command != null)
                    {
                        result = handler.calculateFromCommand(data.command, this);
                    }
                    else if (data.event != null)
                    {
                        result = handler.calculateFromEvent(data.event, this);
                    }
                    if (result != null)
                    {
                        data.put(clazz, result);
                        return (T) result;
                    }
                }
            }
            data.put(clazz, null);
            return null;
        }
        finally
        {
            data.computeStack.remove(clazz);
        }
    }
    
    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameContext#resolveContextVar(java.lang.String)
     */
    @Override
    public String resolveContextVar(String src)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * thread local data
     */
    private static final class TLD extends HashMap<Class<?>, Object>
    {

        /**
         * serial version uid.
         */
        private static final long serialVersionUID = 8827911884838600137L;
        
        /** the underlying command being executed. */
        public CommandInterface command;
        /** the underlying event being executed. */
        public MinigameEvent<?> event;
        
        /** stack of computes to detect endless loops. */
        public Set<Class<?>> computeStack = new HashSet<>();
        
        /**
         * Constructor.
         */
        public TLD()
        {
            // empty
        }
    }
    
}
