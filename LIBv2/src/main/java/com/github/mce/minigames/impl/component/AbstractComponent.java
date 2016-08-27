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

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for all minigame components.
 * 
 * @author mepeisen
 */
public abstract class AbstractComponent
{
    
    /** the underlying component registry owning this component. */
    private final ComponentRegistry registry;
    
    /** the current world chunks this component is located in. */
    private final Set<WorldChunk> currentChunks = new HashSet<>();
    
    /**
     * Constructor to create the component.
     * 
     * @param registry
     *            the owning registry.
     */
    public AbstractComponent(ComponentRegistry registry)
    {
        this.registry = registry;
    }
    
    /**
     * Sets/Changes the world chunks this component is located in.
     * @param chunks
     */
    public void setWorldChunks(Set<WorldChunk> chunks)
    {
        final Set<WorldChunk> removed = new HashSet<>(this.currentChunks);
        removed.removeAll(chunks);
        final Set<WorldChunk> added = new HashSet<>(chunks);
        added.removeAll(this.currentChunks);
        if (removed.size() > 0)
        {
            this.registry.unregister(removed, this);
        }
        if (added.size() > 0)
        {
            this.registry.register(added, this);
        }
        this.currentChunks.clear();
        this.currentChunks.addAll(chunks);
    }
    
}
