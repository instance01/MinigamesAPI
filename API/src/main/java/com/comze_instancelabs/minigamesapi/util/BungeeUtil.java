package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtil {

	// will be used by Arcade for connections between servers

	public static void connectToServer(JavaPlugin plugin, String player, String server) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.getPlayer(player).sendPluginMessage(plugin, "BungeeCord", stream.toByteArray());
	}

}
