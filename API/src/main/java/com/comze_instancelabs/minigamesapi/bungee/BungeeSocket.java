package com.comze_instancelabs.minigamesapi.bungee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaLogger;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class BungeeSocket {

	// Here sits the lovely MGLib server waiting for requests from our Lobby slaves >:D

	// We're gonna send simple strings like: sign:<minigame>:<arena>:<state>:<players>/<amxplayers>

	PluginInstance pli;

	public ArrayList<Socket> clients = new ArrayList<Socket>();

	public BungeeSocket(PluginInstance pli) {
		this.pli = pli;
	}

	public String signUpdateString(Arena a) {
		if (a == null) {
			return "sign:" + pli.getPlugin().getName() + ":null:JOIN:0:0";
		}
		return "sign:" + pli.getPlugin().getName() + ":" + a.getInternalName() + ":" + a.getArenaState().toString() + ":" + a.getAllPlayers().size() + ":" + a.getMaxPlayers();
	}

	/*
	 * public void sendSingUpdate(String arenaname) { for (Socket s : clients) { try { PrintWriter out = new PrintWriter(s.getOutputStream(), true);
	 * out.println(signUpdateString(pli.getArenaByName(arenaname))); } catch (Exception e) { e.printStackTrace(); } } }
	 */

	ArrayList<Integer> portsUp = new ArrayList<Integer>();
	boolean init = false;
	boolean initializing = false;

	public void sendSignUpdate(final Arena a) {
		try {
			System.out.println("" + init);
			if (init) {
				for (int i : portsUp) {
					ArenaLogger.debug("Sending to port " + i);
					Socket socket = new Socket("127.0.0.1", i);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(signUpdateString(a));
					socket.close();
				}
				return;
			}
			// Of course we'll have lags at the first sign update as we check through 20 ports
			if (!initializing) {
				new Thread(new Runnable() {
					public void run() {
						for (int i = 13380; i < 13400; i++) {
							try {
								ArenaLogger.debug("Trying port " + i);
								Socket socket = new Socket("127.0.0.1", i);
								if (socket.isConnected()) {
									portsUp.add(i);
								}
								PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
								out.println(signUpdateString(a));
								socket.close();
							} catch (Exception e) {
								ArenaLogger.debug("Could not connect to port " + i);
							}
						}
						init = true;
					}
				}).start();
				initializing = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
