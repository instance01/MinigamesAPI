package com.comze_instancelabs.minigamesapi.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class Util {

	public static void clearInv(Player p){
		p.getInventory().clear();
		p.updateInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.updateInventory();
	}

	public static void teleportPlayerFixed(Player p, Location l) {
		//TODO might need Runnable fix?
		p.teleport(l, TeleportCause.PLUGIN);
		l.getWorld().refreshChunk(l.getChunk().getX(), l.getChunk().getZ());
	}
	

	public static void teleportAllPlayers(ArrayList<String> players, final Location l){
		Long delay = 1L;
		for(String pl : players){
			if(!Validator.isPlayerOnline(pl)){
				continue;
			}
			final Player p = Bukkit.getPlayer(pl);
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable(){
				public void run(){
					Util.teleportPlayerFixed(p, l);
				}
			}, delay);
			delay++;
		}
	}
	

	public static void teleportAllPlayers(ArrayList<String> players, ArrayList<Location> locs){
		//TODO 
	}
	
	public static Location getComponentForArena(String arenaname, String component, String count){
		if(Validator.isArenaValid(arenaname)){
			String base = "arenas." + arenaname + "." + component + count;
			return new Location(Bukkit.getWorld(MinigamesAPI.getAPI().arenasconfig.getConfig().getString(base + ".world")), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt(base + ".location.x"), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt(base + ".location.y"), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt(base + ".location.z"), (float)MinigamesAPI.getAPI().arenasconfig.getConfig().getDouble(base + ".location.yaw"), (float)MinigamesAPI.getAPI().arenasconfig.getConfig().getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static Location getComponentForArena(String arenaname, String component){
		if(Validator.isArenaValid(arenaname)){
			String base = "arenas." + arenaname + "." + component;
			return new Location(Bukkit.getWorld(MinigamesAPI.getAPI().arenasconfig.getConfig().getString(base + ".world")), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt(base + ".location.x"), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt(base + ".location.y"), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt(base + ".location.z"), (float)MinigamesAPI.getAPI().arenasconfig.getConfig().getDouble(base + ".location.yaw"), (float)MinigamesAPI.getAPI().arenasconfig.getConfig().getDouble(base + ".location.pitch"));
		}
		return null;
	}

	public static void saveComponentForArena(String arenaname, String component, Location comploc){
		String base = "arenas." + arenaname + "." + component;
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".world", comploc.getWorld().getName());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.x", comploc.getBlockX());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.y", comploc.getBlockY());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.z", comploc.getBlockZ());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.yaw", comploc.getYaw());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.pitch", comploc.getPitch());
		MinigamesAPI.getAPI().arenasconfig.saveConfig();
	}
	
	public static void saveMainLobby(Location comploc){
		String base = "mainlobby";
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".world", comploc.getWorld().getName());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.x", comploc.getBlockX());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.y", comploc.getBlockY());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.z", comploc.getBlockZ());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.yaw", comploc.getYaw());
		MinigamesAPI.getAPI().arenasconfig.getConfig().set(base + ".location.pitch", comploc.getPitch());
		MinigamesAPI.getAPI().arenasconfig.saveConfig();
	}
	

    public static void saveArenaToFile(JavaPlugin plugin, String arena){
    	File f = new File(plugin.getDataFolder() + "/" + arena);
    	Cuboid c = new Cuboid(Util.getComponentForArena(arena, "boundary", "1"), Util.getComponentForArena(arena, "boundary", "2"));
    	Location start = c.getLowLoc();
    	Location end = c.getHighLoc();

		int width = end.getBlockX() - start.getBlockX();
		int length = end.getBlockZ() - start.getBlockZ();
		int height = end.getBlockY() - start.getBlockY();
		
		MinigamesAPI.getAPI().getLogger().info("BOUNDS: " + Integer.toString(width) + " " + Integer.toString(height) +  " " + Integer.toString(length)); 
		MinigamesAPI.getAPI().getLogger().info("BLOCKS TO SAVE: " + Integer.toString(width * height * length));
		
		FileOutputStream fos;
		ObjectOutputStream oos = null;
		try{
			fos = new FileOutputStream(f);
			oos = new BukkitObjectOutputStream(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= height; j++) {
				for(int k = 0; k <= length; k++){
					Block change = c.getWorld().getBlockAt(start.getBlockX() + i, start.getBlockY() + j, start.getBlockZ() + k);
					
					//if(change.getType() != Material.AIR){
						ArenaBlock bl = new ArenaBlock(change);

						try {
							oos.writeObject(bl);
						} catch (IOException e) {
							e.printStackTrace();
						}	
					//}

				}
			}
		}
		
		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MinigamesAPI.getAPI().getLogger().info("saved");
    }

    public static void loadArenaFromFileSYNC(JavaPlugin plugin, Arena arena){
    	int failcount = 0;
    	final ArrayList<ArenaBlock> failedblocks = new ArrayList<ArenaBlock>();
    	
    	File f = new File(plugin.getDataFolder() + "/" + arena.getName());
		FileInputStream fis = null;
		BukkitObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			ois = new BukkitObjectInputStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while(true)  
			{ 
				Object b = null;
				try{
					b = ois.readObject();
				}catch(EOFException e){
					MinigamesAPI.getAPI().getLogger().info("Finished restoring map for " + arena.getName() + ".");
					
					//TODO update sign
					/*Sign s = Util.getSignFromArena(arena);
					s.setLine(2, "§2[Join]");
					s.setLine(3, "0/" + Integer.toString(this.maxplayers_perteam * 2));
					s.update();*/
					arena.setArenaState(ArenaState.JOIN);
					
				}
				
				if(b != null){
					ArenaBlock ablock = (ArenaBlock) b;
					try{
						if(!ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).getType().toString().equalsIgnoreCase(ablock.getMaterial().toString())){
							ablock.getBlock().getWorld().getBlockAt(ablock.getBlock().getLocation()).setType(ablock.getMaterial());
						}
					}catch(IllegalStateException e){
						failcount += 1;
						failedblocks.add(ablock);
					}
				}else{
					break;
				}
			} 

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
            

		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MinigamesAPI.getAPI(), new Runnable() {
			public void run() {
				// restore spigot blocks!
				for(ArenaBlock ablock : failedblocks){
					Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).setType(Material.WOOL);
					Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).getTypeId();
					Bukkit.getServer().getWorld(ablock.world).getBlockAt(new Location(Bukkit.getServer().getWorld(ablock.world), ablock.x, ablock.y, ablock.z)).setType(ablock.getMaterial());
				}

			}
		}, 40L);
		MinigamesAPI.getAPI().getLogger().info("Successfully finished!");

		return;
    }
    
    public Sign getSignFromArena(String arena) {
		Location b_ = new Location(Bukkit.getServer().getWorld(MinigamesAPI.getAPI().arenasconfig.getConfig().getString("arenas." + arena + ".sign.world")), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt("arenas." + arena + ".sign.loc.x"), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt("arenas." + arena + ".sign.loc.y"), MinigamesAPI.getAPI().arenasconfig.getConfig().getInt("arenas." + arena + ".sign.loc.z"));
		BlockState bs = b_.getBlock().getState();
		Sign s_ = null;
		if (bs instanceof Sign) {
			s_ = (Sign) bs;
		} else {
		}
		return s_;
	}
    
	public void updateSign(Arena arena){
		Sign s = getSignFromArena(arena.getName());
		int count = arena.getAllPlayers().size();
		int maxcount = arena.getMaxPlayers();
		if(s != null){
			s.setLine(0, MinigamesAPI.getAPI().messagesconfig.getConfig().getString("signs." + arena.getArenaState().toString() + ".0").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()));
			s.setLine(1, MinigamesAPI.getAPI().messagesconfig.getConfig().getString("signs." + arena.getArenaState().toString() + ".1").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()));
			s.setLine(2, MinigamesAPI.getAPI().messagesconfig.getConfig().getString("signs." + arena.getArenaState().toString() + ".2").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()));
			s.setLine(3, MinigamesAPI.getAPI().messagesconfig.getConfig().getString("signs." + arena.getArenaState().toString() + ".3").replaceAll("&", "§").replace("<count>", Integer.toString(count)).replace("<maxcount>", Integer.toString(maxcount)).replace("<arena>", arena.getName()));
			s.update();
		}
	}
	
	
}
