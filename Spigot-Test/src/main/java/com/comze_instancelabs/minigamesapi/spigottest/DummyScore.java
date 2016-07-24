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

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author mepeisen
 *
 */
public class DummyScore implements Score
{
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#getEntry()
     */
    @Override
    public String getEntry()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#getObjective()
     */
    @Override
    public Objective getObjective()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#getPlayer()
     */
    @Override
    public OfflinePlayer getPlayer()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#getScore()
     */
    @Override
    public int getScore() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#getScoreboard()
     */
    @Override
    public Scoreboard getScoreboard()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#isScoreSet()
     */
    @Override
    public boolean isScoreSet() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Score#setScore(int)
     */
    @Override
    public void setScore(int arg0) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }
    
}
