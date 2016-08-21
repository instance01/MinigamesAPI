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

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;

/**
 * A read-only wrapper around minigame plugin impl
 * 
 * @author mepeisen
 *
 */
class MinigameWrapper implements MinigameInterface
{
    
    /**
     * The underlying minigame plugin impl.
     */
    private MinigamePluginImpl delegate;
    
    /**
     * @param delegate The underlying minigame plugin impl.
     */
    public MinigameWrapper(MinigamePluginImpl delegate)
    {
        this.delegate = delegate;
    }
    
    @Override
    public String getName()
    {
        return this.delegate.getName();
    }
    
    @Override
    public MessagesConfigInterface getMessages()
    {
        return this.delegate.getMessages();
    }

    @Override
    public Iterable<ArenaTypeInterface> getDeclaredTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArenaInterface getArenas(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getLogger()
     */
    @Override
    public Logger getLogger()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getConfig(java.lang.String)
     */
    @Override
    public ConfigurationSection getConfig(String file)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#saveConfig(java.lang.String)
     */
    @Override
    public void saveConfig(String file)
    {
        // TODO Auto-generated method stub
        
    }
    
}
