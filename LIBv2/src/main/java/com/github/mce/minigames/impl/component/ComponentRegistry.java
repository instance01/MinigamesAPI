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

package com.github.mce.minigames.impl.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A registry for locating and managing components.
 * 
 * @author mepeisen
 */
public class ComponentRegistry
{
    
    /** the registered components per chunk. */
    private final Map<WorldChunk, Set<AbstractComponent>> components = new HashMap<>();
    
    /**
     * Registers a component in a world chunk.
     * 
     * @param chunks
     *            Chunks to register
     * @param component
     *            component to register.
     */
    public void register(Set<WorldChunk> chunks, AbstractComponent component)
    {
        for (final WorldChunk chunk : chunks)
        {
            this.components.computeIfAbsent(chunk, (c) -> new HashSet<>()).add(component);
        }
    }
    
    /**
     * Removes a component from world chunks.
     * 
     * @param chunks
     *            Chunks to unregister
     * @param component
     *            component to unregister
     */
    public void unregister(Set<WorldChunk> chunks, AbstractComponent component)
    {
        for (final WorldChunk chunk : chunks)
        {
            final Set<AbstractComponent> set = this.components.get(chunk);
            if (set != null)
            {
                set.remove(component);
            }
        }
    }
    
    /**
     * Fetches components within a world chunk.
     * 
     * @param chunk
     *            world chunk
     * @return components being in this world chunk.
     */
    public Set<AbstractComponent> fetch(WorldChunk chunk)
    {
        final Set<AbstractComponent> result = this.components.get(chunk);
        return result == null ? Collections.emptySet() : result;
    }
    
    /**
     * Fetches components within a world chunk and filters by given class
     * 
     * @param clazz
     *            the class filter
     * @param chunk
     *            world chunk
     * @return components being in this world chunk.
     */
    public <T extends AbstractComponent> Set<T> fetch(Class<T> clazz, WorldChunk chunk)
    {
        final Set<T> result = new HashSet<>();
        for (final AbstractComponent c : this.fetch(chunk))
        {
            if (clazz.isInstance(c))
            {
                result.add(clazz.cast(c));
            }
        }
        return result;
    }
    
}
