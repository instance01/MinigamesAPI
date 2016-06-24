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

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.bungee.BungeeSocket;
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
            e.printStackTrace();
        }
        Bukkit.getPlayer(player).sendPluginMessage(plugin, "BungeeCord", stream.toByteArray());
    }
    
    public static void sendSignUpdateRequest(final JavaPlugin plugin, final String minigame, final Arena arena)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        BungeeSocket.sendSignUpdate(pli, arena);
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        try
        {
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("MinigamesLib");
            
            final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            final DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeUTF(minigame + ":" + arena.getInternalName() + ":" + arena.getArenaState().toString() + ":" + Integer.toString(arena.getAllPlayers().size()) + ":"
                    + Integer.toString(arena.getMaxPlayers()));
            
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
            
            Bukkit.getServer().sendPluginMessage(MinigamesAPI.getAPI(), "BungeeCord", out.toByteArray());
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
