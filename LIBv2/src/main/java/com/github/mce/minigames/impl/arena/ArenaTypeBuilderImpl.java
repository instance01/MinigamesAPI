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

import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.arena.rules.ArenaRuleSet;
import com.github.mce.minigames.impl.MinigamePluginImpl;
import com.github.mce.minigames.impl.component.ComponentRegistry;

/**
 * Implementation of an arena type builder.
 * 
 * @author mepeisen
 */
public class ArenaTypeBuilderImpl implements ArenaTypeBuilderInterface
{
    
    /** name of the type. */
    private String                 typename;
    /** the enum type. */
    private ArenaTypeInterface     type;
    /** {@code true} if this the default */
    private boolean                isDefault;
    
    /** the arenas. */
    private Map<String, ArenaImpl> arenas;
    
    /** the registry. */
    private ComponentRegistry      registry;
    
    /** the minigames plugin that declared this arena type. */
    private MinigamePluginImpl     plugin;
    
    /**
     * @param typename
     * @param type
     * @param isDefault
     * @param registry
     * @param arenas
     * @param plugin
     */
    public ArenaTypeBuilderImpl(String typename, ArenaTypeInterface type, boolean isDefault, ComponentRegistry registry, Map<String, ArenaImpl> arenas, MinigamePluginImpl plugin)
    {
        this.typename = typename;
        this.type = type;
        this.isDefault = isDefault;
        this.registry = registry;
        this.plugin = plugin;
        this.arenas = arenas;
    }
    
    /**
     * Creates the arena type declaration.
     * 
     * @return arena type declaration.
     */
    public ArenaTypeDeclarationInterface build()
    {
        return new ArenaTypeDeclarationImpl(this.typename, this.type, this.isDefault, this.arenas, this.registry, this.plugin);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyRulesets(com.github.mce.minigames.api.arena.rules.ArenaRuleSet[])
     */
    @Override
    public void applyRulesets(ArenaRuleSet... set)
    {
        // TODO Auto-generated method stub
        
    }
    
}
