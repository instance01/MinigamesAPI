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
package com.comze_instancelabs.minigamesapi.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ChannelStrings;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeUtil
{
    
    public static void connectToServer(final JavaPlugin plugin, final String player, final String server)
    {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(stream);
        try
        {
            out.writeUTF("Connect");
            out.writeUTF(server);
        }
        catch (final IOException e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
        Bukkit.getPlayer(player).sendPluginMessage(plugin, ChannelStrings.CHANNEL_BUNGEE_CORD, stream.toByteArray());
    }
    
    public static void sendSignUpdateRequest(final JavaPlugin plugin, final String minigame, final Arena arena)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        MinigamesAPI.getAPI().sendSignUpdate(pli, arena);
    }
    
}
