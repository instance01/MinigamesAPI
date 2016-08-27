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

package com.github.mce.minigames.impl.arena;

import java.util.Map;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.impl.MinigamePluginImpl;
import com.github.mce.minigames.impl.component.ComponentRegistry;

/**
 * Internal representation of an arena type.
 * 
 * @author mepeisen
 */
public class ArenaTypeDeclarationImpl implements ArenaTypeDeclarationInterface
{
    
    /**
     * Underlying arena type (enum)
     */
    private ArenaTypeInterface enumType;
    
    /**
     * {@code true} for default arena type.
     */
    private boolean            isDefault;
    
    /**
     * The name of the arena type.
     */
    private String             typename;

    /** the arenas. */
    private Map<String, ArenaImpl> arenas;

    /** the registry. */
    private ComponentRegistry registry;

    /** the minigames plugin. */
    private MinigamePluginImpl plugin;
    
    /**
     * Constructor to create arena type.
     * 
     * @param typename
     *            name of the type.
     * @param enumType
     *            Underlying arena type (enum)
     * @param isDefault
     *            {@code true} for default arena type.
     * @param arenas
     *            the minigame arenas
     * @param registry
     *            the component registry
     * @param plugin 
     */
    public ArenaTypeDeclarationImpl(String typename, ArenaTypeInterface enumType, boolean isDefault, Map<String, ArenaImpl> arenas, ComponentRegistry registry, MinigamePluginImpl plugin)
    {
        this.typename = typename;
        this.enumType = enumType;
        this.isDefault = isDefault;
        this.arenas = arenas;
        this.registry = registry;
        this.plugin = plugin;
    }
    
    @Override
    public ArenaTypeInterface getType()
    {
        return this.enumType;
    }
    
    @Override
    public boolean isDefault()
    {
        return this.isDefault;
    }
    
    @Override
    public String getName()
    {
        return this.typename;
    }
    
    @Override
    public ArenaInterface createArena(String arenaName) throws MinigameException
    {
        if (this.arenas.containsKey(arenaName.toLowerCase()))
        {
            throw new MinigameException(CommonErrors.DuplicateArena, arenaName);
        }
        final ArenaImpl arena = new ArenaImpl(arenaName, this.plugin, this.registry);
        this.arenas.put(arenaName.toLowerCase(), arena);
        return arena;
    }
    
}
