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
import java.util.logging.Logger;

import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;

import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * A read-only wrapper around minigame plugin impl
 * 
 * @author mepeisen
 *
 */
public class MinigameWrapper implements MinigameInterface
{
    
    /**
     * The underlying minigame plugin impl.
     */
    private MinigamePluginInterface delegate;
    
    /**
     * @param delegate The underlying minigame plugin impl.
     */
    public MinigameWrapper(MinigamePluginInterface delegate)
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
     * @see com.github.mce.minigames.api.MinigameInterface#getArenaCount()
     */
    @Override
    public int getArenaCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getArena(java.lang.String)
     */
    @Override
    public ArenaInterface getArena(String name)
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
     * @see com.github.mce.minigames.api.MinigameInterface#getShortDescription()
     */
    @Override
    public Serializable getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.MinigameInterface#getLongDescription()
     */
    @Override
    public Serializable getLongDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
//    public Iterable<ArenaTypeDeclarationInterface> getDeclaredTypes()
//    {
//        return this.delegate.getDeclaredTypes();
//    }
//
//    @Override
//    public Iterable<ArenaInterface> getArenas()
//    {
//        return this.delegate.getArenas();
//    }
//
//    @Override
//    public ArenaInterface getArena(String name)
//    {
//        return this.delegate.getArena(name);
//    }
//
//    @Override
//    public Logger getLogger()
//    {
//        return this.delegate.getLogger();
//    }
//
//    @Override
//    public ConfigurationSection getConfig(String file)
//    {
//        return this.delegate.getConfig(file);
//    }
//
//    @Override
//    public void saveConfig(String file)
//    {
//        this.delegate.saveConfig(file);
//    }
//
//    @Override
//    public Serializable getShortDescription()
//    {
//        return this.delegate.getShortDescription();
//    }
//
//    @Override
//    public Serializable getLongDescription()
//    {
//        return this.delegate.getLongDescription();
//    }
//
//    @Override
//    public int getArenaCount()
//    {
//        return this.delegate.getArenaCount();
//    }
//
//    @Override
//    public ArenaTypeDeclarationInterface getDefaultType()
//    {
//        return this.delegate.getDefaultType();
//    }
//
//    @Override
//    public ArenaTypeDeclarationInterface getType(String name)
//    {
//        return this.delegate.getType(name);
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getAdminRules()
//     */
//    @Override
//    public Iterable<AdminRuleId> getAdminRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getMatchRules()
//     */
//    @Override
//    public Iterable<MatchRuleId> getMatchRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getPlayerRules()
//     */
//    @Override
//    public Iterable<PlayerRuleId> getPlayerRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getTeamRules()
//     */
//    @Override
//    public Iterable<TeamRuleId> getTeamRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getComponentRules()
//     */
//    @Override
//    public Iterable<ComponentRuleId> getComponentRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getArenaRules()
//     */
//    @Override
//    public Iterable<ArenaRuleId> getArenaRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getComponents()
//     */
//    @Override
//    public Iterable<ComponentId> getComponents()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getMatchPhases()
//     */
//    @Override
//    public Iterable<MatchPhaseId> getMatchPhases()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getTeams()
//     */
//    @Override
//    public Iterable<TeamId> getTeams()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
}
