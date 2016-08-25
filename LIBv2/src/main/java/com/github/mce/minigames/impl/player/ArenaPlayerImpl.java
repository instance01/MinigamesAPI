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

package com.github.mce.minigames.impl.player;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.WaitQueue;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.perms.PermissionsInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;
import com.github.mce.minigames.impl.stubs.FalseStub;
import com.github.mce.minigames.impl.stubs.TrueStub;

/**
 * Implementation of arena players.
 * 
 * @author mepeisen
 *
 */
public class ArenaPlayerImpl implements ArenaPlayerInterface
{
    
    /** players uuid. */
    private UUID uuid;
    
    /** the players name. */
    private String name;

    /**
     * Constructor
     * @param uuid players uuid
     */
    public ArenaPlayerImpl(UUID uuid)
    {
        this.uuid = uuid;
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player != null)
        {
            this.name = player.getName();
        }
    }

    @Override
    public Player getBukkitPlayer()
    {
        return Bukkit.getPlayer(this.uuid);
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
    
    @Override
    public UUID getPlayerUUID()
    {
        return this.uuid;
    }
    
    @Override
    public void sendMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        final Player player = this.getBukkitPlayer();
        if (player != null)
        {
            if (player.isOp())
            {
                player.sendMessage(msg.toAdminMessage(this.getPreferredLocale(), args));
            }
            else
            {
                player.sendMessage(msg.toUserMessage(this.getPreferredLocale(), args));
            }
        }
    }
    
    @Override
    public Locale getPreferredLocale()
    {
        // TODO Auto-generated method stub
        return Locale.ENGLISH;
    }
    
    @Override
    public void setPreferredLocale(Locale locale) throws MinigameException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public ArenaInterface getArena()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Iterable<WaitQueue> getWaitingQueues()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void join(WaitQueue queue)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean checkPermission(PermissionsInterface perm)
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public MgOutgoingStubbing<ArenaPlayerInterface> when(MgPredicate<ArenaPlayerInterface> test) throws MinigameException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
