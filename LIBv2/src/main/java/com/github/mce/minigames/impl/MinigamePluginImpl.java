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

package com.github.mce.minigames.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.github.mce.minigames.api.ContextHandlerInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.PluginProviderInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;

/**
 * The minigames plugin impl.
 * 
 * @author mepeisen
 */
class MinigamePluginImpl extends BaseImpl implements MinigamePluginInterface
{
    
    /**
     * The minigame name.
     */
    private final String                                                 name;
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<ArenaTypeInterface, ArenaTypeDeclarationInterface> arenaTypes       = new HashMap<>();
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<String, ArenaTypeInterface>                        arenaTypesByName = new HashMap<>();
    
    /**
     * the default arena type to use.
     */
    private ArenaTypeDeclarationInterface                                defaultType;

    /**
     * Short description
     */
    private Serializable shortDescription;

    /**
     * Long multi line description
     */
    private Serializable longDescription;
    
    /**
     * Constructor to create a minigame.
     * 
     * @param mgplugin
     *            minigames plugin
     * @param name
     *            internal name of the minigame.
     * @param provider
     *            the provider.
     */
    public MinigamePluginImpl(MinigamesPlugin mgplugin, String name, PluginProviderInterface provider)
    {
        super(mgplugin, provider.getJavaPlugin());
        this.name = name;
        this.shortDescription = provider.getShortDescription();
        this.longDescription = provider.getDescription();
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigamePluginInterface#init()
     */
    @Override
    public void init()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigameInterface#getDeclaredTypes()
     */
    @Override
    public Iterable<ArenaTypeDeclarationInterface> getDeclaredTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigameInterface#getArenas()
     */
    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigameInterface#getArenas(java.lang.String)
     */
    @Override
    public ArenaInterface getArenas(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigamePluginInterface#createArenaType(com.github.mce.minigames.api.arena.ArenaTypeInterface, boolean)
     */
    @Override
    public ArenaTypeBuilderInterface createArenaType(ArenaTypeInterface type, boolean isDefault) throws MinigameException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Logger getLogger()
    {
        return this.plugin.getLogger();
    }
    
    @Override
    public <T> void registerContextHandler(Class<T> clazz, ContextHandlerInterface<T> handler) throws MinigameException
    {
        this.mgplugin.getApiContext().registerContextHandler(clazz, handler);
    }

    @Override
    public Serializable getShortDescription()
    {
        return this.shortDescription;
    }

    @Override
    public Serializable getLongDescription()
    {
        return this.longDescription;
    }
    
}
