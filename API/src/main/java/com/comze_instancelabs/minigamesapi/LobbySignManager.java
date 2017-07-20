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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import com.comze_instancelabs.minigamesapi.util.Signs;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * Manager for cross server lobby signs.
 * 
 * @author mepeisen
 *
 */
public class LobbySignManager
{
    
    /**
     * hash map for signs.
     */
    private final Map<Location, SignData> signs = new HashMap<>();
    
    /**
     * minigames api.
     */
    final Plugin                          plugin;
    
    /**
     * Constructor.
     * 
     * @param plugin
     */
    public LobbySignManager(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    /**
     * Attaches a new sign to the sign manager,
     * 
     * @param location
     *            location
     * @param serverName
     *            server name
     * @param minigameName
     *            minigame name
     * @param arenaName
     *            arena name
     * @param spec
     *            true for spectator sign, false for join sign
     */
    public void attachSign(Location location, String serverName, String minigameName, String arenaName, boolean spec)
    {
        final SignData data = new SignData(location, serverName, minigameName, arenaName, spec);
        this.signs.put(location, data);
        data.setSignData(null);
    }
    
    /**
     * Attaches a new sign to the sign manager,
     * 
     * @param location
     *            location
     * @param serverName
     *            server name
     * @param minigameName
     *            minigame name
     * @param arenaName
     *            arena name
     * @param spec
     *            true for spectator sign, false for join sign
     * @param evt
     *            sign change event
     */
    public void attachSign(Location location, String serverName, String minigameName, String arenaName, boolean spec, SignChangeEvent evt)
    {
        final SignData data = new SignData(location, serverName, minigameName, arenaName, spec);
        this.signs.put(location, data);
        data.setSignData(evt);
    }
    
    /**
     * request sign updates.
     * 
     * @param location
     */
    public void requestSignUpdate(Location location)
    {
        final SignData data = this.signs.get(location);
        if (data != null)
        {
            data.requestServerSign();
        }
    }
    
    /**
     * Detaches a sign from manager.
     * 
     * @param location
     */
    public void detachSign(Location location)
    {
        this.signs.remove(location);
    }
    
    /**
     * @param location
     * @param arenastate
     * @param count
     * @param maxcount
     */
    public void updateSign(Location location, String arenastate, int count, int maxcount)
    {
        final SignData data = this.signs.get(location);
        if (data != null)
        {
            data.setSignData(count, maxcount, arenastate, null);
            data.updateResponseDate();
        }
    }
    
    /**
     * private sign data.
     */
    private final class SignData
    {
        
        /** sign location. */
        private final Location location;
        
        /** server name. */
        private final String   serverName;
        
        /** arena name. */
        private final String   arenaName;
        
        /** minigame name. */
        private final String   minigameName;
        
        /** spectate sign? */
        private final boolean  spec;
        
        /**
         * last max players info.
         */
        int                    lastMaxPlayers = 10;
        
        /** last request of sign update. */
        LocalDateTime          lastRequest    = LocalDateTime.now();
        
        /** last response of sign update. */
        LocalDateTime          lastResponse   = this.lastRequest;
        
        /**
         * timestamp a log warning was posted
         */
        LocalDateTime lastSignWarning = null;
        
        /**
         * @param location
         * @param serverName
         * @param minigameName
         * @param arenaName
         * @param spec
         */
        public SignData(Location location, String serverName, String minigameName, String arenaName, boolean spec)
        {
            this.location = location;
            this.serverName = serverName;
            this.minigameName = minigameName;
            this.arenaName = arenaName;
            this.spec = spec;
        }
        
        /**
         * Updates the last response data.
         */
        public void updateResponseDate()
        {
            this.lastResponse = LocalDateTime.now();
        }
        
        /**
         * Request server sign update.
         */
        public void requestServerSign()
        {
            this.lastRequest = LocalDateTime.now();
            try
            {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                try
                {
                    out.writeUTF("Forward"); //$NON-NLS-1$
                    out.writeUTF(this.serverName);
                    out.writeUTF(ChannelStrings.SUBCHANNEL_MINIGAMESLIB_REQUEST);
                    
                    ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                    DataOutputStream msgout = new DataOutputStream(msgbytes);
                    msgout.writeUTF(this.minigameName + ":" + this.arenaName); //$NON-NLS-1$
                    
                    out.writeShort(msgbytes.toByteArray().length);
                    out.write(msgbytes.toByteArray());
                    
                    Bukkit.getServer().sendPluginMessage(LobbySignManager.this.plugin, ChannelStrings.CHANNEL_BUNGEE_CORD, out.toByteArray());
                    
                    // TODO if no answer after 2 seconds, server empty!
                    
                }
                catch (Exception e)
                {
                    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
                }
            }
            catch (Exception e)
            {
                LobbySignManager.this.plugin.getLogger().log(Level.WARNING, "Error occurred while sending extra sign request: ", e); //$NON-NLS-1$
            }
        }
        
        /**
         * Sets initial sign data.
         * 
         * @param evt
         */
        public void setSignData(SignChangeEvent evt)
        {
            this.setSignData(0, 10, this.spec ? "SPEC" : "JOIN", evt); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        /**
         * Sets sign data.
         * 
         * @param count
         * @param maxcount
         * @param arenastate
         * @param evt
         */
        public void setSignData(int count, int maxcount, String arenastate, SignChangeEvent evt)
        {
            this.lastMaxPlayers = maxcount;
            final FileConfiguration cfg = LobbySignManager.this.plugin.getConfig();
            final String line0 = Signs.format(cfg.getString("signs." + arenastate.toLowerCase() + ".0").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    .replace("<arena>", this.arenaName).replace("<minigame>", this.minigameName)); //$NON-NLS-1$ //$NON-NLS-2$
            final String line1 = Signs.format(cfg.getString("signs." + arenastate.toLowerCase() + ".1").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    .replace("<arena>", this.arenaName).replace("<minigame>", this.minigameName)); //$NON-NLS-1$ //$NON-NLS-2$
            final String line2 = Signs.format(cfg.getString("signs." + arenastate.toLowerCase() + ".2").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    .replace("<arena>", this.arenaName).replace("<minigame>", this.minigameName)); //$NON-NLS-1$ //$NON-NLS-2$
            final String line3 = Signs.format(cfg.getString("signs." + arenastate.toLowerCase() + ".3").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    .replace("<arena>", this.arenaName).replace("<minigame>", this.minigameName)); //$NON-NLS-1$ //$NON-NLS-2$
            if (evt == null)
            {
                final BlockState state = this.location.getBlock().getState();
                if (state instanceof Sign)
                {
                    final Sign sign = (Sign) state;
                    sign.setLine(0, line0);
                    sign.setLine(1, line1);
                    sign.setLine(2, line2);
                    sign.setLine(3, line3);
                    sign.getBlock().getChunk().load();
                    sign.update();
                }
                else
                {
                    if (this.lastSignWarning == null || this.lastSignWarning.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 30)
                    {
                        this.lastSignWarning = LocalDateTime.now();
                        LobbySignManager.this.plugin.getLogger().log(Level.WARNING, "Cannot find lobby sign for " + this.minigameName + "/" + this.arenaName + " at position " + this.location); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    } 
                }
            }
            else
            {
                evt.setLine(0, line0);
                evt.setLine(1, line1);
                evt.setLine(2, line2);
                evt.setLine(3, line3);
            }
        }
        
    }
    
    /**
     * 
     */
    public void ping()
    {
        for (final SignData data : this.signs.values())
        {
            if (data.lastResponse.isBefore(data.lastRequest))
            {
                if (data.lastResponse.until(data.lastRequest, ChronoUnit.SECONDS) > 5)
                {
                    // assume there are no more players
                    data.setSignData(0, data.lastMaxPlayers, "JOIN", null); //$NON-NLS-1$
                    data.updateResponseDate();
                }
            }
            else
            {
                if (data.lastResponse.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 60)
                {
                    data.requestServerSign();
                }
            }
        }
    }
    
}
