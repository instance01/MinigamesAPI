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

import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.services.MinigameExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;

import de.minigameslib.mclib.api.McException;

/**
 * 
 * 
 * @author mepeisen
 */
class ExtensionImpl extends BaseImpl implements MinigameExtensionInterface
{
    
    /** the extension name. */
    private final String name;
    
    /** the extension description. */
    private Serializable description;
    
    /**
     * Constructor.
     * 
     * @param name
     * @param mgplugin
     * @param provider
     */
    public ExtensionImpl(MinigamesPlugin mgplugin, String name, MinigameExtensionProviderInterface provider)
    {
        super(mgplugin, provider.getJavaPlugin());
        this.name = name;
        this.description = provider.getShortDescription();
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public Serializable getShortDescription()
    {
        return this.description;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.services.MinigameExtensionInterface#disable()
     */
    @Override
    public void disable() throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.services.MinigameExtensionInterface#createArenaType(java.lang.String, com.github.mce.minigames.api.arena.ArenaTypeInterface, boolean)
     */
    @Override
    public ArenaTypeBuilderInterface createArenaType(String name, ArenaTypeInterface type, boolean isDefault) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.services.MinigameExtensionInterface#init()
     */
    @Override
    public void init() throws McException
    {
        // TODO Auto-generated method stub
        
    }

//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getDeclaredTypes()
//     */
//    @Override
//    public Iterable<ArenaTypeDeclarationInterface> getDeclaredTypes()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getDefaultType()
//     */
//    @Override
//    public ArenaTypeDeclarationInterface getDefaultType()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getType(java.lang.String)
//     */
//    @Override
//    public ArenaTypeDeclarationInterface getType(String name)
//    {
//        // TODO Auto-generated method stub
//        return null;
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
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.services.MinigameExtensionInterface#createArenaType(java.lang.String, com.github.mce.minigames.api.arena.ArenaTypeInterface, boolean)
//     */
//    @Override
//    public ArenaTypeBuilderInterface createArenaType(String name, ArenaTypeInterface type, boolean isDefault) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.services.MinigameExtensionInterface#init()
//     */
//    @Override
//    public void init() throws McException
//    {
//        // TODO Auto-generated method stub
//        
//    }
    
}
