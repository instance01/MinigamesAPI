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

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.PluginProviderInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;

/**
 * The minigames plugin impl.
 * 
 * @author mepeisen
 */
class MinigamePluginImpl implements MinigamePluginInterface
{

    /**
     * @param name
     * @param provider
     */
    public MinigamePluginImpl(String name, PluginProviderInterface provider)
    {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getMessages()
     */
    @Override
    public MessagesConfigInterface getMessages()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigamePluginInterface#init()
     */
    @Override
    public void init()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getDeclaredTypes()
     */
    @Override
    public Iterable<ArenaTypeInterface> getDeclaredTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getArenas()
     */
    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getArenas(java.lang.String)
     */
    @Override
    public ArenaInterface getArenas(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigamePluginInterface#createArenaType(com.github.mce.minigames.api.arena.ArenaTypeInterface, boolean)
     */
    @Override
    public ArenaTypeBuilderInterface createArenaType(ArenaTypeInterface type, boolean isDefault) throws MinigameException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
