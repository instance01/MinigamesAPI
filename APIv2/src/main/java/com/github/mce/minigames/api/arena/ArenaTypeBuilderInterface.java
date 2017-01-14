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

package com.github.mce.minigames.api.arena;

import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.team.TeamId;
import com.github.mce.minigames.api.team.TeamRuleSet;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.impl.comp.ComponentId;

/**
 * A builder to create arena types.
 * 
 * <p>
 * Get an instance of this object via {@link MinigamePluginInterface#createArenaType(String, ArenaTypeInterface, boolean)}.
 * </p>
 * 
 * @author mepeisen
 */
public interface ArenaTypeBuilderInterface
{
    
//    /**
//     * Applies rule sets to this arena type.
//     * 
//     * @param set
//     *            the arena rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the arena rule set was invalid.
//     */
//    ArenaTypeBuilderInterface applyRulesets(ArenaRuleSet... set) throws McException;
//    
//    /**
//     * Applies rule sets for administration.
//     * 
//     * @param set
//     *            the admin rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the admin rule set was invalid.
//     */
//    ArenaTypeBuilderInterface applyRulesets(AdminRuleSet... set) throws McException;
//    
//    /**
//     * Applies rule sets for components.
//     * 
//     * @param set
//     *            the component rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the component rule set was invalid.
//     */
//    ArenaTypeBuilderInterface applyRulesets(ComponentRuleSet... set) throws McException;
//    
//    /**
//     * Applies rule sets for running match.
//     * 
//     * @param set
//     *            the match rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the match rule set was invalid.
//     */
//    ArenaTypeBuilderInterface applyRulesets(MatchRuleSet... set) throws McException;
//    
//    /**
//     * Applies rule sets for players within arenas.
//     * 
//     * @param set
//     *            the player rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the player rule set was invalid.
//     */
//    ArenaTypeBuilderInterface applyRulesets(PlayerRuleSet... set) throws McException;
//    
//    /**
//     * Applies rule sets for teams within arenas.
//     * 
//     * @param set
//     *            the team rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the team rule set was invalid.
//     */
//    ArenaTypeBuilderInterface applyRulesets(TeamRuleSet... set) throws McException;
//    
//    /**
//     * Applies components to this arena type.
//     * 
//     * @param ids
//     *            the components set.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the components set was invalid.
//     */
//    ArenaTypeBuilderInterface applyComponents(ComponentId... ids) throws McException;
//    
//    /**
//     * Applies teams to this arena type.
//     * 
//     * @param ids
//     *            the teams.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if the teams set was invalid.
//     */
//    ArenaTypeBuilderInterface applyTeam(TeamId... ids) throws McException;
//    
//    /**
//     * Returns the currently applied teams.
//     * 
//     * @return components set; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<TeamId> getTeams() throws McException;
//    
//    /**
//     * Removes components from this team.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the components.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeTeams(TeamId... set) throws McException;
//    
//    /**
//     * Returns the currently applied components.
//     * 
//     * @return components set; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<ComponentId> getComponents() throws McException;
//    
//    /**
//     * Removes components from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the components.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeComponents(ComponentId... set) throws McException;
//    
//    /**
//     * Returns the currently applied rule sets.
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<ArenaRuleSet> getArenaRuleSets() throws McException;
//    
//    /**
//     * Removes rule sets from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the arena rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeRulesets(ArenaRuleSet... set) throws McException;
//    
//    /**
//     * Returns the currently applied rule sets.
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<AdminRuleSet> getAdminRuleSets() throws McException;
//    
//    /**
//     * Removes rule sets from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the admin rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeRulesets(AdminRuleSet... set) throws McException;
//
//    /**
//     * Returns the currently component rule sets.
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<ComponentRuleSet> getComponentRuleSets() throws McException;
//    
//    /**
//     * Removes rule sets from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the component rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeRulesets(ComponentRuleSet... set) throws McException;
//    
//    /**
//     * Returns the currently applied rule sets.
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<MatchRuleSet> getMatchRuleSets() throws McException;
//    
//    /**
//     * Removes rule sets from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the arena rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeRulesets(MatchRuleSet... set) throws McException;
//    
//    /**
//     * Returns the currently applied rule sets.
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<PlayerRuleSet> getPlayerRuleSets() throws McException;
//    
//    /**
//     * Removes rule sets from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the arena rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeRulesets(PlayerRuleSet... set) throws McException;
//    
//    /**
//     * Returns the currently applied rule sets.
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<TeamRuleSet> getTeamRuleSets() throws McException;
//    
//    /**
//     * Removes rule sets from this arena type.
//     * 
//     * <p>
//     * If this method is called with rule sets that are not applied to this type this method silently ignores it. That means: Removing unknown rule sets does not throw exceptions.
//     * </p>
//     * 
//     * @param set
//     *            the arena rule sets.
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface removeRulesets(TeamRuleSet... set) throws McException;
//    
//    /**
//     * Selects the starting phase for this arena type.
//     * 
//     * @param phase
//     *            starting phase
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface startsWith(MatchPhaseId phase) throws McException;
//    
//    /**
//     * Adds a match phase
//     * 
//     * @param phases
//     *            match phase
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    ArenaTypeBuilderInterface addPhases(MatchPhaseId... phases) throws McException;
//    
//    /**
//     * Returns the starting phase.
//     * 
//     * @return starting phase.
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    MatchPhaseId getStartingPhase() throws McException;
//    
//    /**
//     * Returns the currently applied phases (excluding start phase).
//     * 
//     * @return rule sets; changes (remove) will be reflected back to this builder
//     * @throws McException
//     *             thrown if this method is called after {@link MinigamePluginInterface#init()} was called.
//     */
//    Iterable<MatchPhaseId> getPhases() throws McException;
//    
//    /**
//     * Inherits all phases (including start phase) from given arena; this method must be called at first before manipulating the phases.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritAllPhasesAndStartPhase(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all phases (excluding start phase) from given arena; this method must be called at first before manipulating the phases.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritAllPhases(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits the start phase but not the other phases from given arena; this method must be called at first before manipulating the phases.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritStartPhase(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all arena rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritArenaRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all match rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritMatchRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all player rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritPlayerRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all team rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritTeamRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all admin rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritAdminRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all component rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritComponentRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all rules from given arena; this method must be called at first before manipulating the rule sets.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritAllRules(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all components from given arena; this method must be called at first before manipulating the components.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritComponents(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits all teams from given arena; this method must be called at first before manipulating the components.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritTeams(ArenaTypeInterface type) throws McException;
//    
//    /**
//     * Inherits everything (all phases and all rules) from given arena.
//     * 
//     * @param type the arena type to inherit from
//     * @return this object for chaining.
//     * @throws McException
//     *             thrown if this method is called twice or too late.
//     */
//    ArenaTypeBuilderInterface inheritAll(ArenaTypeInterface type) throws McException;
    
}
