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

package com.comze_instancelabs.minigamesapi.spigottest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * A dummy score board.
 * 
 * @author mepeisen
 */
class DummyScoreboard implements Scoreboard
{
    
    /** registered teams. */
    private Map<String, Team> teams = new HashMap<>();
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#registerNewObjective(java.lang.String, java.lang.String)
     */
    @Override
    public Objective registerNewObjective(String name, String criteria) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getObjective(java.lang.String)
     */
    @Override
    public Objective getObjective(String name) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getObjectivesByCriteria(java.lang.String)
     */
    @Override
    public Set<Objective> getObjectivesByCriteria(String criteria) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getObjectives()
     */
    @Override
    public Set<Objective> getObjectives()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getObjective(org.bukkit.scoreboard.DisplaySlot)
     */
    @Override
    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getScores(org.bukkit.OfflinePlayer)
     */
    @Override
    public Set<Score> getScores(OfflinePlayer player) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getScores(java.lang.String)
     */
    @Override
    public Set<Score> getScores(String entry) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#resetScores(org.bukkit.OfflinePlayer)
     */
    @Override
    public void resetScores(OfflinePlayer player) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#resetScores(java.lang.String)
     */
    @Override
    public void resetScores(String entry) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getPlayerTeam(org.bukkit.OfflinePlayer)
     */
    @Override
    public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getEntryTeam(java.lang.String)
     */
    @Override
    public Team getEntryTeam(String entry) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Team getTeam(String teamName) throws IllegalArgumentException
    {
        return this.teams.get(teamName);
    }
    
    @Override
    public Set<Team> getTeams()
    {
        return new HashSet<>(this.teams.values());
    }
    
    @Override
    public Team registerNewTeam(String name) throws IllegalArgumentException
    {
        final Team team = new DummyScoreboardTeam();
        this.teams.put(name, team);
        return team;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getPlayers()
     */
    @Override
    public Set<OfflinePlayer> getPlayers()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#getEntries()
     */
    @Override
    public Set<String> getEntries()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Scoreboard#clearSlot(org.bukkit.scoreboard.DisplaySlot)
     */
    @Override
    public void clearSlot(DisplaySlot slot) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
}
