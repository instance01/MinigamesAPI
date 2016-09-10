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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.context.ContextHandlerInterface;
import com.github.mce.minigames.api.context.ContextResolverInterface;
import com.github.mce.minigames.api.context.MinigameContext;
import com.github.mce.minigames.api.util.function.MgRunnable;
import com.github.mce.minigames.api.util.function.MgSupplier;

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
    
    /** context resolve helper. */
    private final List<ContextResolverInterface> resolvers = new ArrayList<>();
    
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
     * 
     * @param resolver
     */
    public void registerContextResolver(ContextResolverInterface resolver)
    {
        this.resolvers.add(resolver);
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
    public void runInContext(MinigameEvent<?, ?> event, Runnable runnable)
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

    @Override
    public <T> void setContext(Class<T> clazz, T value)
    {
        final TLD data = this.tls.get();
        data.put(clazz, value);
    }
    
    @Override
    public String resolveContextVar(String src)
    {
        if (!src.contains("$")) return src; //$NON-NLS-1$
        final StringBuilder builder = new StringBuilder();
        int start = src.indexOf('$');
        if (start > 0) builder.append(src, 0, start);
        int end = src.indexOf('$', start + 1);
        final String varWithArgs = src.substring(start + 1, end - 1);
        final String[] splitted = varWithArgs.split(":"); //$NON-NLS-1$
        builder.append(resolve(splitted));
        builder.append(this.resolveContextVar(src.substring(end + 1)));
        return builder.toString();
    }

    /**
     * Resolve context var
     * @param splitted
     * @return resolved var
     */
    private String resolve(String[] splitted)
    {
        final String varName = splitted[0];
        final String[] args = splitted.length == 1 ? new String[0] : Arrays.copyOfRange(splitted, 1, splitted.length);
        for (final ContextResolverInterface resolver : this.resolvers)
        {
            final String result = resolver.resolve(varName, args, this);
            if (result != null) return result;
        }
        return "?"; //$NON-NLS-1$
    }

    @Override
    public void runInNewContext(MgRunnable runnable) throws MinigameException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = old.command;
            tld.event = old.event;
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

    @Override
    public void runInCopiedContext(MgRunnable runnable) throws MinigameException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.putAll(old);
            tld.command = old.command;
            tld.event = old.event;
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

    @Override
    public <T> T calculateInNewContext(MgSupplier<T> runnable) throws MinigameException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.command = old.command;
            tld.event = old.event;
            return runnable.get();
        }
        finally
        {
            this.tls.set(old);
            tld.clear();
            tld.command = null;
            tld.event = null;
        }
    }

    @Override
    public <T> T calculateInCopiedContext(MgSupplier<T> runnable) throws MinigameException
    {
        final TLD old = this.tls.get();
        final TLD tld = new TLD();
        this.tls.set(tld);
        try
        {
            tld.clear();
            tld.putAll(old);
            tld.command = old.command;
            tld.event = old.event;
            return runnable.get();
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
        public MinigameEvent<?, ?> event;
        
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
