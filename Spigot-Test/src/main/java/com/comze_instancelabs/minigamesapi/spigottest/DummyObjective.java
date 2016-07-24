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
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author mepeisen
 *
 */
public class DummyObjective implements Objective
{
    
    private final String name;
    
    private final Map<String, Score> scores = new HashMap<>();
    
    /**
     * @param name
     */
    public DummyObjective(String name)
    {
        this.name = name;
    }

    @Override
    public String getName() throws IllegalStateException
    {
        return this.name;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#getDisplayName()
     */
    @Override
    public String getDisplayName() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#setDisplayName(java.lang.String)
     */
    @Override
    public void setDisplayName(String arg0) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#getCriteria()
     */
    @Override
    public String getCriteria() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean isModifiable() throws IllegalStateException
    {
        return true;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#getScoreboard()
     */
    @Override
    public Scoreboard getScoreboard()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#unregister()
     */
    @Override
    public void unregister() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#setDisplaySlot(org.bukkit.scoreboard.DisplaySlot)
     */
    @Override
    public void setDisplaySlot(DisplaySlot arg0) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Objective#getDisplaySlot()
     */
    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Score getScore(OfflinePlayer arg0) throws IllegalArgumentException, IllegalStateException
    {
        return this.getScore(arg0.getName());
    }
    
    @Override
    public Score getScore(String arg0) throws IllegalArgumentException, IllegalStateException
    {
        return this.scores.computeIfAbsent(arg0, (k) -> new DummyScore());
    }
    
}
