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

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.impl.component.ComponentRegistry;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaState;

/**
 * Implementation of arena interface.
 * 
 * @author mepeisen
 */
public class ArenaImpl implements ArenaInterface
{
    
    /** the internal name. */
    private String                        internalName;
    
    /** the display name. */
    private String                        displayName;
    
    /** the plugin. */
    private MinigamePluginInterface       plugin;
    
    /** the component registry. */
    private ComponentRegistry             registry;
    
    /** the used arena declaration. */
    private ArenaTypeDeclarationInterface arenaType;
    
    /** {@code true} if the arena is enabled. */
    private boolean                       enabled;
    
    /** {@code true} if the arena is in maintenance mode. */
    private boolean                       maintenance;
    
    /** current arena state. */
    private ArenaState                    state;
    
    /**
     * Constructor for a new arena.
     * 
     * @param arenaName
     * @param plugin
     * @param registry
     * @param type
     * @throws McException
     */
    public ArenaImpl(String arenaName, MinigamePluginInterface plugin, ComponentRegistry registry, ArenaTypeDeclarationInterface type) throws McException
    {
        this.internalName = arenaName;
        this.plugin = plugin;
        this.registry = registry;
        this.arenaType = type;
        
        final MglibInterface lib = MglibInterface.INSTANCE.get();
//        lib.runInCopiedContext(() -> {
//            lib.setContext(ArenaInterface.class, this);
//            
//            // init minimal values.
//            ArenasConfig.Enabled.setBoolean(false);
//            ArenasConfig.ArenaType.setString(this.arenaType.getName());
//            ArenasConfig.Maintenance.setBoolean(true);
//            
//            // save the config.
//            ArenasConfig.Maintenance.saveConfig();
//        });
        this.maintenance = true;
    }
    
    /**
     * Constructor for an existing arena.
     * 
     * @param arenaName
     * @param plugin
     * @param registry
     * @throws McException
     */
    public ArenaImpl(String arenaName, MinigamePluginInterface plugin, ComponentRegistry registry) throws McException
    {
        this.internalName = arenaName;
        this.plugin = plugin;
        this.registry = registry;
        
        final MglibInterface lib = MglibInterface.INSTANCE.get();
//        lib.runInCopiedContext(() -> {
//            lib.setContext(ArenaInterface.class, this);
//            
//            this.enabled = ArenasConfig.Enabled.getBoolean();
//            this.maintenance = ArenasConfig.Maintenance.getBoolean();
//            this.displayName = ArenasConfig.DisplayName.getString();
//            
//            final String typename = ArenasConfig.ArenaType.getString();
//            this.arenaType = this.plugin.getType(typename);
//            if (this.arenaType == null)
//            {
//                throw new McException(CommonErrors.Cannot_Load_Arena_Unknown_Type, typename, this.plugin.getName(), arenaName);
//            }
//            
//            // TODO Load components, options and rules
//        });
    }
    
//    @Override
//    public String getInternalName()
//    {
//        return this.internalName;
//    }
//    
//    @Override
//    public String getDisplayName()
//    {
//        return this.displayName == null ? this.internalName : this.displayName;
//    }
    
//    @Override
//    public void setDisplayName(String name) throws McException
//    {
//        final MglibInterface lib = MglibInterface.INSTANCE.get();
//        lib.runInCopiedContext(() -> {
//            lib.setContext(ArenaInterface.class, this);
//            ArenasConfig.DisplayName.setString(name);
//            ArenasConfig.DisplayName.saveConfig();
//        });
//        this.displayName = name;
//    }
//    
//    @Override
//    public Logger getLogger()
//    {
//        // TODO Arena Logger
//        return this.plugin.getLogger();
//    }
//    
//    @Override
//    public MinigameInterface getMinigame()
//    {
//        return new MinigameWrapper(this.plugin);
//    }
//    
//    @Override
//    public ArenaState getState()
//    {
//        return this.maintenance ? ArenaState.Maintenance : this.state;
//    }
//    
//    @Override
//    public ArenaState getRealState()
//    {
//        return this.state;
//    }
//    
//    @Override
//    public boolean isEnabled()
//    {
//        // TODO Auto-generated method stub
//        return false;
//    }
//    
//    @Override
//    public boolean isMaintenance()
//    {
//        // TODO Auto-generated method stub
//        return false;
//    }
//    
//    @Override
//    public McOutgoingStubbing<ArenaInterface> when(McPredicate<ArenaInterface> test) throws McException
//    {
//        if (test.test(this))
//        {
//            return new TrueStub<>(this);
//        }
//        return new FalseStub<>(this);
//    }
//    
//    @Override
//    public boolean canStart()
//    {
//        // TODO Auto-generated method stub
//        return false;
//    }
//    
//    @Override
//    public ArenaTypeInterface getArenaType()
//    {
//        return this.arenaType.getType();
//    }
//    
//    /*
//     * (non-Javadoc)
//     * 
//     * @see com.github.mce.minigames.api.arena.ArenaInterface#start()
//     */
//    @Override
//    public void start()
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * Try to restart the arena asynchronous (safe restart)
//     */
//    public void tryRestart()
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaInterface#getAuthor()
//     */
//    @Override
//    public String getAuthor()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaInterface#getShortDescription()
//     */
//    @Override
//    public LocalizedMessageInterface getShortDescription()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaInterface#getDescription()
//     */
//    @Override
//    public LocalizedMessageInterface getDescription()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaInterface#delete()
//     */
//    @Override
//    public void delete() throws McException
//    {
//        // TODO Auto-generated method stub
//        
//    }
    
}
