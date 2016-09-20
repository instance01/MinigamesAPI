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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.WaitQueue;
import com.github.mce.minigames.api.arena.rules.MinigameEvent;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.config.Configurable;
import com.github.mce.minigames.api.context.ContextHandlerInterface;
import com.github.mce.minigames.api.context.MinigameContext;
import com.github.mce.minigames.api.context.MinigameStorage;
import com.github.mce.minigames.api.gui.ClickGuiInterface;
import com.github.mce.minigames.api.gui.GuiSessionInterface;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.perms.PermissionsInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.FalseStub;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;
import com.github.mce.minigames.api.util.function.TrueStub;
import com.github.mce.minigames.impl.gui.GuiSessionImpl;

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
    
    /** the session storage. */
    private StorageImpl sessionStorage = new StorageImpl();
    
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
    public void sendMessage(LocalizedMessageInterface msg, Serializable... args)
    {
        final Player player = this.getBukkitPlayer();
        if (player != null)
        {
            
            String[] msgs = null;
            if (msg.isSingleLine())
            {
                msgs = new String[] { player.isOp() ? (msg.toAdminMessage(this.getPreferredLocale(), args)) : (msg.toUserMessage(this.getPreferredLocale(), args)) };
            }
            else
            {
                msgs = player.isOp() ? (msg.toAdminMessageLine(this.getPreferredLocale(), args)) : (msg.toUserMessageLine(this.getPreferredLocale(), args));
            }
            
            for (final String smsg : msgs)
            {
                switch (msg.getSeverity())
                {
                    default:
                    case Error:
                        player.sendMessage(ChatColor.DARK_RED + smsg);
                        break;
                    case Information:
                        player.sendMessage(ChatColor.WHITE + smsg);
                        break;
                    case Loser:
                        player.sendMessage(ChatColor.RED + smsg);
                        break;
                    case Success:
                        player.sendMessage(ChatColor.GREEN + smsg);
                        break;
                    case Warning:
                        player.sendMessage(ChatColor.YELLOW + smsg);
                        break;
                    case Winner:
                        player.sendMessage(ChatColor.GOLD + smsg);
                        break;
                }
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
        final Player player = this.getBukkitPlayer();
        return player == null ? false : player.hasPermission(perm.resolveName());
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
    
    @Override
    public MinigameStorage getContextStorage()
    {
        return MglibInterface.INSTANCE.get().getContext(ContextStorage.class).computeIfAbsent(this.uuid, (key) -> new StorageImpl());
    }
    
    @Override
    public MinigameStorage getSessionStorage()
    {
        // TODO Clear on offline/online events
        return this.sessionStorage;
    }
    
    @Override
    public MinigameStorage getPersistentStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Helper for context storage.
     * 
     * @author mepeisen
     */
    private static final class ContextStorage extends HashMap<UUID, StorageImpl>
    {
        
        /**
         * serial version uid
         */
        private static final long serialVersionUID = 3803764167708189047L;
        
        /**
         * Constructor
         */
        public ContextStorage()
        {
            // empty
        }
        
    }
    
    /**
     * Simple implementation of storage map.
     * 
     * @author mepeisen
     */
    private static final class StorageImpl implements MinigameStorage
    {
        
        /** the underlying data map. */
        private final Map<Class<?>, Configurable> data = new HashMap<>();
        
        /**
         * Constructor.
         */
        public StorageImpl()
        {
            // empty
        }
        
        @Override
        public <T extends Configurable> T get(Class<T> clazz)
        {
            return clazz.cast(this.data.get(clazz));
        }
        
        @Override
        public <T extends Configurable> void set(Class<T> clazz, T value)
        {
            this.data.put(clazz, value);
        }
        
    }
    
    /**
     * Registers the storage context provider.
     * 
     * @param mg2
     * @throws MinigameException
     */
    public static void registerProvider(MinigamePluginInterface mg2) throws MinigameException
    {
        mg2.registerContextHandler(ContextStorage.class, new ContextHandlerInterface<ContextStorage>() {
            
            @Override
            public ContextStorage calculateFromCommand(CommandInterface command, MinigameContext context)
            {
                return new ContextStorage();
            }
            
            @Override
            public ContextStorage calculateFromEvent(MinigameEvent<?, ?> event, MinigameContext context)
            {
                return new ContextStorage();
            }
        });
    }

    @Override
    public GuiSessionInterface getGuiSession()
    {
        return this.getSessionStorage().get(GuiSessionInterface.class);
    }

    @Override
    public GuiSessionInterface openGui(ClickGuiInterface gui) throws MinigameException
    {
        final MinigameStorage storage = this.getSessionStorage();
        final GuiSessionInterface oldSession = storage.get(GuiSessionInterface.class);
        if (oldSession != null)
        {
            oldSession.close();
        }
        final GuiSessionInterface newSession = new GuiSessionImpl(gui, this);
        storage.set(GuiSessionInterface.class, newSession);
        return newSession;
    }

    /**
     * Player quit event
     */
    public void onPlayerQuit()
    {
        // clear session storage
        this.sessionStorage = new StorageImpl();
    }

    /**
     * Player join event
     */
    public void onPlayerJoin()
    {
        // clear session storage
        this.sessionStorage = new StorageImpl();
    }

    /**
     * Client closed the gui.
     */
    public void onCloseGui()
    {
        final MinigameStorage storage = this.getSessionStorage();
        storage.set(GuiSessionInterface.class, null);
    }
    
}
