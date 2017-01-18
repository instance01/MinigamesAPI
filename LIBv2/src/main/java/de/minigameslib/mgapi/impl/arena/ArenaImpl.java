/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.arena;

import java.io.File;
import java.util.Collection;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;

/**
 * Arena data.
 * 
 * @author mepeisen
 */
public class ArenaImpl implements ArenaInterface
{
    
    /**
     * The associated arena data
     */
    private ArenaData arenaData;
    
    /**
     * the arena data file.
     */
    private File dataFile;
    
    /** arena type. */
    private ArenaTypeInterface type;
    
    /** current arena state. */
    private ArenaState state;
    
    /**
     * Constructor to create an arena by using given data file.
     * @param dataFile
     * @throws McException thrown if data file is invalid.
     */
    public ArenaImpl(File dataFile) throws McException
    {
        this.dataFile = dataFile;
        // TODO yml file
        // TODO provide a way to detect server crashes during games and set a state to request hard reset asap
        if (!this.arenaData.isEnabled())
        {
            this.state = ArenaState.Disabled;
        }
        else if (this.arenaData.isMaintenance())
        {
            this.state = ArenaState.Maintenance;
        }
        else
        {
            this.state = ArenaState.Starting;
        }
    }
    
    /**
     * Constructor to create a new arena.
     * @param name 
     * @param type 
     * @param dataFile 
     * @throws McException thrown if data file is invalid.
     */
    public ArenaImpl(String name, ArenaTypeInterface type, File dataFile) throws McException
    {
        this.dataFile = dataFile;
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getInternalName()
    {
        return this.arenaData.getName();
    }
    
    @Override
    public ArenaTypeInterface getType()
    {
        return this.type;
    }

    @Override
    public LocalizedConfigString getDisplayName()
    {
        return this.arenaData.getDisplayName();
    }

    @Override
    public LocalizedConfigString getShortDescription()
    {
        return this.arenaData.getShortDescription();
    }

    @Override
    public LocalizedConfigLine getDescription()
    {
        return this.arenaData.getDescription();
    }

    @Override
    public LocalizedConfigLine getManual()
    {
        return this.arenaData.getManual();
    }

    @Override
    public void saveData() throws McException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ArenaState getState()
    {
        return this.state;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#join(de.minigameslib.mgapi.api.player.ArenaPlayerInterface)
     */
    @Override
    public void join(ArenaPlayerInterface player) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#spectate(de.minigameslib.mgapi.api.player.ArenaPlayerInterface)
     */
    @Override
    public void spectate(ArenaPlayerInterface player) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#setEnabledState()
     */
    @Override
    public void setEnabledState() throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#setDisabledState(boolean)
     */
    @Override
    public void setDisabledState(boolean force) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#setMaintenance(boolean)
     */
    @Override
    public void setMaintenance(boolean force) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#isMaintenance()
     */
    @Override
    public boolean isMaintenance()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#check()
     */
    @Override
    public Collection<CheckFailure> check()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return
     */
    public Plugin getPlugin()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
