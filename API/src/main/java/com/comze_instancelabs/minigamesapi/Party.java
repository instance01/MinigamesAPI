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
package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A (temporary) party for group playing with friends.
 * 
 * @author instancelabs
 */
public class Party
{
    
    /** party owner. */
    private String            owner;
    
    /** party members. */
    private ArrayList<String> players = new ArrayList<>();
    
    /**
     * Constructor.
     * 
     * @param owner
     *            party owner (player name)
     */
    public Party(final String owner)
    {
        this.owner = owner;
    }
    
    /**
     * returns the owner player name
     * 
     * @return player name
     */
    public String getOwner()
    {
        return this.owner;
    }
    
    /**
     * Returns the party members.
     * 
     * @return party members.
     */
    public ArrayList<String> getPlayers()
    {
        return this.players;
    }
    
    /**
     * Adds a players (invite).
     * 
     * @param p
     *            player to add.
     */
    public void addPlayer(final String p)
    {
        if (!this.players.contains(p))
        {
            this.players.add(p);
        }
        Bukkit.getPlayer(p).sendMessage(MinigamesAPI.getAPI().partymessages.you_joined_party.replaceAll("<player>", this.getOwner()));
        this.tellAll(MinigamesAPI.getAPI().partymessages.player_joined_party.replaceAll("<player>", p));
    }
    
    /**
     * Removes a player.
     * 
     * @param p
     *            player to remove
     * @return {@code true} if the player was contained in the list
     */
    public boolean removePlayer(final String p)
    {
        if (this.players.contains(p))
        {
            this.players.remove(p);
            final Player p___ = Bukkit.getPlayer(p);
            if (p___ != null)
            {
                p___.sendMessage(MinigamesAPI.getAPI().partymessages.you_left_party.replaceAll("<player>", this.getOwner()));
            }
            this.tellAll(MinigamesAPI.getAPI().partymessages.player_left_party.replaceAll("<player>", p));
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a player is present in party list.
     * 
     * @param p
     *            playeer to be checked.
     * @return {@code true} if the player is within the party list.
     */
    public boolean containsPlayer(final String p)
    {
        return this.players.contains(p);
    }
    
    /**
     * Disband/Delete the party.
     */
    public void disband()
    {
        this.tellAll(MinigamesAPI.getAPI().partymessages.party_disbanded);
        if (MinigamesAPI.getAPI().global_party.containsKey(this.owner))
        {
            this.players.clear();
            MinigamesAPI.getAPI().global_party.remove(this.owner);
        }
    }
    
    /**
     * Internal chat message for all players within the list.
     * 
     * @param msg
     *            message
     */
    private void tellAll(final String msg)
    {
        for (final String p_ : this.getPlayers())
        {
            final Player p__ = Bukkit.getPlayer(p_);
            if (p__ != null)
            {
                p__.sendMessage(msg);
            }
        }
        final Player p___ = Bukkit.getPlayer(this.getOwner());
        if (p___ != null)
        {
            p___.sendMessage(msg);
        }
    }
    
}
