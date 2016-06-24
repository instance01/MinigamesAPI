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
package com.comze_instancelabs.minigamesapi.bungee;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class BungeeSocket
{
    
    // Here sits the lovely MGLib server waiting for requests from our Lobby slaves >:D
    // Socket server moved to lobby, dis our slave now :(
    
    // We're gonna send simple strings like: sign:<minigame>:<arena>:<state>:<players>/<amxplayers>
    
    public static String signUpdateString(final PluginInstance pli, final Arena a)
    {
        if (a == null)
        {
            return "sign:" + pli.getPlugin().getName() + ":null:JOIN:0:0";
        }
        return "sign:" + pli.getPlugin().getName() + ":" + a.getInternalName() + ":" + a.getArenaState().toString() + ":" + a.getAllPlayers().size() + ":" + a.getMaxPlayers();
    }
    
    static ArrayList<Integer> portsUp      = new ArrayList<>();
    static boolean            init         = false;
    static boolean            initializing = false;
    
    public static void sendSignUpdate(final PluginInstance pli, final Arena a)
    {
        try
        {
            if (BungeeSocket.init)
            {
                for (final int i : BungeeSocket.portsUp)
                {
                    ArenaLogger.debug("Sending to port " + i);
                    final Socket socket = new Socket("127.0.0.1", i);
                    final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(BungeeSocket.signUpdateString(pli, a));
                    socket.close();
                }
                return;
            }
            // Of course we'll have lags at the first sign update as we check through 20 ports
            if (!BungeeSocket.initializing)
            {
                BungeeSocket.initializing = true;
                new Thread(() -> {
                    for (int i = 13380; i < 13400; i++)
                    {
                        try
                        {
                            ArenaLogger.debug("Trying port " + i);
                            final Socket socket = new Socket("127.0.0.1", i);
                            if (socket.isConnected())
                            {
                                BungeeSocket.portsUp.add(i);
                            }
                            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            out.println(BungeeSocket.signUpdateString(pli, a));
                            socket.close();
                        }
                        catch (final Exception e)
                        {
                            ArenaLogger.debug("Could not connect to port " + i);
                        }
                    }
                    BungeeSocket.init = true;
                }).start();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
