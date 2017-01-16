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
import com.github.mce.minigames.impl.MinigamePluginImpl;
import com.github.mce.minigames.impl.component.ComponentRegistry;

import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

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
    
//    @Override
//    public ArenaTypeBuilderInterface applyRulesets(ArenaRuleSet... set)
//    {
//        // TODO Auto-generated method stub
//        return this;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyRulesets(com.github.mce.minigames.api.arena.rules.AdminRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyRulesets(AdminRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyRulesets(com.github.mce.minigames.api.component.ComponentRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyRulesets(ComponentRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyRulesets(com.github.mce.minigames.api.arena.rules.MatchRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyRulesets(MatchRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyRulesets(com.github.mce.minigames.api.arena.rules.PlayerRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyRulesets(PlayerRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyRulesets(com.github.mce.minigames.api.team.TeamRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyRulesets(TeamRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyComponents(com.github.mce.minigames.api.component.ComponentId[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyComponents(ComponentId... ids) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#applyTeam(com.github.mce.minigames.api.team.TeamId[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface applyTeam(TeamId... ids) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getTeams()
//     */
//    @Override
//    public Iterable<TeamId> getTeams() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeTeams(com.github.mce.minigames.api.team.TeamId[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeTeams(TeamId... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getComponents()
//     */
//    @Override
//    public Iterable<ComponentId> getComponents() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeComponents(com.github.mce.minigames.api.component.ComponentId[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeComponents(ComponentId... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getArenaRuleSets()
//     */
//    @Override
//    public Iterable<ArenaRuleSet> getArenaRuleSets() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeRulesets(com.github.mce.minigames.api.arena.rules.ArenaRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeRulesets(ArenaRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getAdminRuleSets()
//     */
//    @Override
//    public Iterable<AdminRuleSet> getAdminRuleSets() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeRulesets(com.github.mce.minigames.api.arena.rules.AdminRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeRulesets(AdminRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getComponentRuleSets()
//     */
//    @Override
//    public Iterable<ComponentRuleSet> getComponentRuleSets() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeRulesets(com.github.mce.minigames.api.component.ComponentRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeRulesets(ComponentRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getMatchRuleSets()
//     */
//    @Override
//    public Iterable<MatchRuleSet> getMatchRuleSets() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeRulesets(com.github.mce.minigames.api.arena.rules.MatchRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeRulesets(MatchRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getPlayerRuleSets()
//     */
//    @Override
//    public Iterable<PlayerRuleSet> getPlayerRuleSets() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeRulesets(com.github.mce.minigames.api.arena.rules.PlayerRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeRulesets(PlayerRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getTeamRuleSets()
//     */
//    @Override
//    public Iterable<TeamRuleSet> getTeamRuleSets() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#removeRulesets(com.github.mce.minigames.api.team.TeamRuleSet[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface removeRulesets(TeamRuleSet... set) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#startsWith(com.github.mce.minigames.api.arena.MatchPhaseId)
//     */
//    @Override
//    public ArenaTypeBuilderInterface startsWith(MatchPhaseId phase) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#addPhases(com.github.mce.minigames.api.arena.MatchPhaseId[])
//     */
//    @Override
//    public ArenaTypeBuilderInterface addPhases(MatchPhaseId... phases) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getStartingPhase()
//     */
//    @Override
//    public MatchPhaseId getStartingPhase() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#getPhases()
//     */
//    @Override
//    public Iterable<MatchPhaseId> getPhases() throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritAllPhasesAndStartPhase(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritAllPhasesAndStartPhase(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritAllPhases(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritAllPhases(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritStartPhase(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritStartPhase(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritArenaRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritArenaRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritMatchRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritMatchRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritPlayerRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritPlayerRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritTeamRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritTeamRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritAdminRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritAdminRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritComponentRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritComponentRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritAllRules(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritAllRules(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritComponents(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritComponents(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritTeams(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritTeams(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface#inheritAll(com.github.mce.minigames.api.arena.ArenaTypeInterface)
//     */
//    @Override
//    public ArenaTypeBuilderInterface inheritAll(ArenaTypeInterface type) throws McException
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
}
