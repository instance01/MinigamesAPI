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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.PluginProviderInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.impl.arena.ArenaImpl;
import com.github.mce.minigames.impl.arena.ArenaTypeBuilderImpl;
import com.github.mce.minigames.impl.component.ComponentRegistry;

import de.minigameslib.mclib.api.McContext.ContextHandlerInterface;
import de.minigameslib.mclib.api.McContext.ContextResolverInterface;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

/**
 * The minigames plugin impl.
 * 
 * @author mepeisen
 */
public class MinigamePluginImpl extends BaseImpl implements MinigamePluginInterface
{
    
    /**
     * The minigame name.
     */
    private final String                                                 name;
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<ArenaTypeInterface, ArenaTypeDeclarationInterface> arenaTypes              = new HashMap<>();
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<String, ArenaTypeInterface>                        arenaTypesByName        = new HashMap<>();
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<String, ArenaTypeBuilderImpl>                      arenaTypeBuildersByName = new HashMap<>();
    
    /**
     * the default arena type to use.
     */
    private ArenaTypeDeclarationInterface                                defaultType;
    
    /**
     * Short description
     */
    private Serializable                                                 shortDescription;
    
    /**
     * Long multi line description
     */
    private Serializable                                                 longDescription;
    
    /**
     * The component registry to be used for registering components
     */
    private final ComponentRegistry                                      components;
    
    /**
     * {@code true} if this plugin is already initialized.
     */
    private boolean                                                      initialized;
    
    /** the arenas. */
    private final Map<String, ArenaImpl>                                 arenas                  = new TreeMap<>();
    
    /** a counter used for arena restart after booting. */
    private static int                                                   restartArenaTaskCount   = 1;
    
    /**
     * Constructor to create a minigame.
     * 
     * @param mgplugin
     *            minigames plugin
     * @param name
     *            internal name of the minigame.
     * @param provider
     *            the provider.
     * @param components
     *            the component registry to use for registering components
     */
    public MinigamePluginImpl(MinigamesPlugin mgplugin, String name, PluginProviderInterface provider, ComponentRegistry components)
    {
        super(mgplugin, provider.getJavaPlugin());
        this.name = name;
        this.shortDescription = provider.getShortDescription();
        this.longDescription = provider.getDescription();
        this.components = components;
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public void init()
    {
        for (final Map.Entry<String, ArenaTypeBuilderImpl> entry : this.arenaTypeBuildersByName.entrySet())
        {
            final ArenaTypeDeclarationInterface type = entry.getValue().build();
            if (type.isDefault())
            {
                this.defaultType = type;
            }
            this.arenaTypesByName.put(entry.getKey(), type.getType());
            this.arenaTypes.put(type.getType(), type);
        }
        this.arenaTypesByName.clear();
        
        // load arenas from config.
//        final ConfigurationSection arenasSection = this.getConfig("arenas.yml").getConfigurationSection("arenas"); //$NON-NLS-1$ //$NON-NLS-2$
//        if (arenasSection != null)
//        {
//            for (final String key : arenasSection.getKeys(false))
//            {
//                this.plugin.getLogger().log(Level.INFO, "Reloading arena " + key + " from config."); //$NON-NLS-1$ //$NON-NLS-2$
//                try
//                {
//                    final ArenaImpl arena = new ArenaImpl(key, this, this.components);
//                    this.arenas.put(key.toLowerCase(), arena);
//                    if (arena.isEnabled())
//                    {
//                        synchronized (this)
//                        {
//                            Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
//                                arena.tryRestart();
//                            }, 10L * restartArenaTaskCount);
//                            restartArenaTaskCount++;
//                        }
//                    }
//                }
//                catch (Exception ex)
//                {
//                    this.plugin.getLogger().log(Level.SEVERE, "Failed loading arena " + key + " from config.", ex); //$NON-NLS-1$ //$NON-NLS-2$
//                }
//            }
//        }
        
        this.initialized = true;
    }
    
    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        return new ArrayList<>(this.arenas.values());
    }
    
    @Override
    public ArenaInterface getArena(String arenaName)
    {
        return this.arenas.get(arenaName);
    }
    
    @Override
    public int getArenaCount()
    {
        return this.arenas.size();
    }
    
    @Override
    public ArenaTypeBuilderInterface createArenaType(String typename, ArenaTypeInterface type, boolean isDefault) throws McException
    {
        if (this.initialized)
        {
            throw new McException(CommonErrors.Cannot_Create_ArenaType_Wrong_State, typename, this.name);
        }
        if (this.arenaTypeBuildersByName.containsKey(typename.toLowerCase()))
        {
            throw new McException(CommonErrors.DuplicateArenaType, typename, this.name);
        }
        
        final ArenaTypeBuilderImpl builder = new ArenaTypeBuilderImpl(typename, type, isDefault, this.components, this.arenas, this);
        this.arenaTypeBuildersByName.put(typename.toLowerCase(), builder);
        return builder;
    }
    
    @Override
    public Logger getLogger()
    {
        return this.plugin.getLogger();
    }
    
    @Override
    public <T> void registerContextHandler(Class<T> clazz, ContextHandlerInterface<T> handler) throws McException
    {
//        this.mgplugin.getApiContext().registerContextHandler(clazz, handler);
    }
    
    @Override
    public void registerContextResolver(ContextResolverInterface resolver) throws McException
    {
//        this.mgplugin.getApiContext().registerContextResolver(resolver);
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription()
    {
        return null;
    }
    
    @Override
    public Serializable getLongDescription()
    {
        return this.longDescription;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigamePluginInterface#disable()
     */
    @Override
    public void disable() throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.config.ConfigInterface#getConfig(java.lang.String)
     */
    @Override
    public DataSection getConfig(String file)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.config.ConfigInterface#saveConfig(java.lang.String)
     */
    @Override
    public void saveConfig(String file)
    {
        // TODO Auto-generated method stub
        
    }
    
}
