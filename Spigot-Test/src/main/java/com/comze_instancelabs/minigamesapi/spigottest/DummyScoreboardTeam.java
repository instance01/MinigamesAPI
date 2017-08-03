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

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author mepeisen
 *
 */
class DummyScoreboardTeam implements Team
{
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getName()
     */
    @Override
    public String getName() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getDisplayName()
     */
    @Override
    public String getDisplayName() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setDisplayName(java.lang.String)
     */
    @Override
    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getPrefix()
     */
    @Override
    public String getPrefix() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setPrefix(java.lang.String)
     */
    @Override
    public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getSuffix()
     */
    @Override
    public String getSuffix() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setSuffix(java.lang.String)
     */
    @Override
    public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#allowFriendlyFire()
     */
    @Override
    public boolean allowFriendlyFire() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setAllowFriendlyFire(boolean)
     */
    @Override
    public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#canSeeFriendlyInvisibles()
     */
    @Override
    public boolean canSeeFriendlyInvisibles() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setCanSeeFriendlyInvisibles(boolean)
     */
    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getNameTagVisibility()
     */
    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setNameTagVisibility(org.bukkit.scoreboard.NameTagVisibility)
     */
    @Override
    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getPlayers()
     */
    @Override
    public Set<OfflinePlayer> getPlayers() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return new HashSet<>();
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getEntries()
     */
    @Override
    public Set<String> getEntries() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getSize()
     */
    @Override
    public int getSize() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getScoreboard()
     */
    @Override
    public Scoreboard getScoreboard()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#addPlayer(org.bukkit.OfflinePlayer)
     */
    @Override
    public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#addEntry(java.lang.String)
     */
    @Override
    public void addEntry(String entry) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#removePlayer(org.bukkit.OfflinePlayer)
     */
    @Override
    public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#removeEntry(java.lang.String)
     */
    @Override
    public boolean removeEntry(String entry) throws IllegalStateException, IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#unregister()
     */
    @Override
    public void unregister() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#hasPlayer(org.bukkit.OfflinePlayer)
     */
    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#hasEntry(java.lang.String)
     */
    @Override
    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getOption(org.bukkit.scoreboard.Team.Option)
     */
    @Override
    public OptionStatus getOption(Option option) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setOption(org.bukkit.scoreboard.Team.Option, org.bukkit.scoreboard.Team.OptionStatus)
     */
    @Override
    public void setOption(Option option, OptionStatus status) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#getColor()
     */
    @Override
    public ChatColor getColor() throws IllegalStateException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.scoreboard.Team#setColor(org.bukkit.ChatColor)
     */
    @Override
    public void setColor(ChatColor paramChatColor)
    {
        // TODO Auto-generated method stub
        
    }
    
}
