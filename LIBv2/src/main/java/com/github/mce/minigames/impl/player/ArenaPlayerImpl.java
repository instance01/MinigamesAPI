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

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.WaitQueue;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;

/**
 * Implementation of arena players.
 * 
 * @author mepeisen
 *
 */
public class ArenaPlayerImpl implements ArenaPlayerInterface
{
    
    /** players uuid. */
    private UUID   uuid;
    
    /** the players name. */
    private String name;
    
    /**
     * Constructor
     * 
     * @param uuid
     *            players uuid
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
    public McOutgoingStubbing<ArenaPlayerInterface> when(McPredicate<ArenaPlayerInterface> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
    /**
     * Registers the storage context provider.
     * 
     * @param mg2
     * @throws McException
     */
    public static void registerProvider(MinigamePluginInterface mg2) throws McException
    {
//        mg2.registerContextHandler(ContextStorage.class, new ContextHandlerInterface<ContextStorage>() {
//            
//            @Override
//            public ContextStorage calculateFromCommand(CommandInterface command, McContext context)
//            {
//                return new ContextStorage();
//            }
//            
//            @Override
//            public ContextStorage calculateFromEvent(MinigameEvent<?, ?> event, McContext context)
//            {
//                return new ContextStorage();
//            }
//        });
    }

    /**
     * Player quit event
     */
    public void onPlayerQuit()
    {
        // TODO
    }

    /**
     * Player join event
     */
    public void onPlayerJoin()
    {
        // TODO
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.player.ArenaPlayerInterface#getMcPlayer()
     */
    @Override
    public McPlayerInterface getMcPlayer()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
