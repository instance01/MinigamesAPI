package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaListener implements Listener{

	//TODO there's still much to do here
	
	JavaPlugin plugin = null;
	
	public ArenaListener(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (MinigamesAPI.global_players.containsKey(p.getName())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		if(MinigamesAPI.global_players.containsKey(event.getEntity().getName())){
			event.getEntity().setHealth(20D);
			Player p = event.getEntity();
			
			MinigamesAPI.global_lost.put(p.getName(), MinigamesAPI.global_players.get(p.getName()));
			final Player p__ = p;
			final Arena arena = MinigamesAPI.global_players.get(p.getName());
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					try {
						//TODO spectate or leave game?
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 5);

			int count = 0;

			for (String p_ : MinigamesAPI.global_players.keySet()) {
				if(Validator.isPlayerOnline(p_)){
					if (MinigamesAPI.global_players.get(p_).getName().equalsIgnoreCase(arena.getName())) {
						if (!MinigamesAPI.global_lost.containsKey(p_)) {
							count++;
						}
					}
				}
			}

			if (count < 2) {
				// last man standing!
				arena.stop();
			}
		}
	}
	
	
	//TODO sign listeners
	@EventHandler
	public void onSignUse(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
				final Sign s = (Sign) event.getClickedBlock().getState();
				//TODO sign stuffs
				if (s.getLine(0).toLowerCase().contains("")) {
					//if (s.getLine(1).equalsIgnoreCase("§2[join]")) {
					/*if(arenastate.get(s.getLine(2)).equalsIgnoreCase("join")){
						if(isValidArena(s.getLine(2))){
							joinLobby(event.getPlayer(), s.getLine(2));
						}else{
							event.getPlayer().sendMessage(arena_invalid);
						}
					}*/
					//}
				}
			}
		}
		
		// netherstar to open kit gui; needs customizable item id
		if (event.hasItem()) {
			final Player p = event.getPlayer();
			if(!MinigamesAPI.global_players.containsKey(p.getName())){
				return;
			}
			if(event.getItem().getTypeId() == 399){
				//Util.openGUI(m, p.getName());
			}
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		//TODO sign stuffs
		/*if (event.getLine(0).toLowerCase().equalsIgnoreCase("")) {
			if (event.getPlayer().hasPermission("mgapi.sign") || event.getPlayer().isOp()) {
				if (!event.getLine(2).equalsIgnoreCase("")) {
					String arena = event.getLine(2);
					if (isValidArena(arena)) {
						MinigamesAPI.arenasconfig.getConfig().set(arena + ".sign.world", p.getWorld().getName());
						MinigamesAPI.arenasconfig.getConfig().set(arena + ".sign.loc.x", event.getBlock().getLocation().getBlockX());
						MinigamesAPI.arenasconfig.getConfig().set(arena + ".sign.loc.y", event.getBlock().getLocation().getBlockY());
						MinigamesAPI.arenasconfig.getConfig().set(arena + ".sign.loc.z", event.getBlock().getLocation().getBlockZ());
						this.saveConfig();
						p.sendMessage("§2Successfully created arena sign.");
					} else {
						p.sendMessage(arena_invalid_component);
						event.getBlock().breakNaturally();
					}
					
					Sign s = (Sign) event.getBlock().getState();
					
					m.updateSign(arena, "join", 0, getArenaMaxPlayers(arena), event);
					
					
					event.setLine(1, "§2[Join]");
					event.setLine(2, arena);
					event.setLine(3, "0/" + Integer.toString(getArenaMaxPlayers(arena)));
					
				}
			}
		}*/
	}
	
	//TODO player join/leave listeners
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (MinigamesAPI.getAPI().global_leftplayers.contains(event.getPlayer().getName())) {
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable() {
				public void run() {
					//TODO save mainlobby somewhere?
					//p.teleport(getMainLobby());
					p.setFlying(false);
				}
			}, 5);
			MinigamesAPI.getAPI().global_leftplayers.remove(event.getPlayer().getName());
		}
		
		
		if(MinigamesAPI.arenasconfig.getConfig().getBoolean("config.game_on_join")){
			int c = 0;
			final List<String> arenas = new ArrayList<String>();
			for (String arena : MinigamesAPI.arenasconfig.getConfig().getKeys(false)) {
				if (!arena.equalsIgnoreCase("mainlobby") && !arena.equalsIgnoreCase("strings") && !arena.equalsIgnoreCase("config")) {
					c++;
					arenas.add(arena);
				}
			}
			if(c < 1){
				MinigamesAPI.getAPI().getLogger().severe("Couldn't find any arena even though game_on_join was turned on. Please setup an arena to fix this!");
				return;
			}
			
			Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), new Runnable(){
				public void run(){
					//TODO need to save global_arenas in main class to get it here
					//joinLobby(p, arenas.get(0));
				}
			}, 30L);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (MinigamesAPI.global_players.containsKey(event.getPlayer().getName())) {
			Arena arena = MinigamesAPI.global_players.get(event.getPlayer().getName());
			MinigamesAPI.getAPI().getLogger().info(arena.getName());
			int count = 0;
			for (String p_ : MinigamesAPI.global_players.keySet()) {
				if (MinigamesAPI.global_players.get(p_).getName().equalsIgnoreCase(arena.getName())) {
					count++;
				}
			}

			try {
				//updateSign(arena, "join", count, getArenaMaxPlayers(arena));
				/*Sign s = this.getSignFromArena(arena);
				if (s != null) {
					s.setLine(1, "§2[Join]");
					s.setLine(3, Integer.toString(count - 1) + "/" + Integer.toString(getArenaMaxPlayers(arena)));
					s.update();
				}*/
			} catch (Exception e) {
				MinigamesAPI.getAPI().getLogger().warning("You forgot to set a sign for arena " + arena + "! This might lead to errors.");
			}

			arena.leavePlayer(event.getPlayer().getName(), true);
			MinigamesAPI.getAPI().global_leftplayers.add(event.getPlayer().getName());
		}
	}
	
	
	
	//TODO player command listener
	@EventHandler
   	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().equalsIgnoreCase("/leave")){
   			if(MinigamesAPI.global_players.containsKey(event.getPlayer().getName())){
   				Arena arena = MinigamesAPI.global_players.get(event.getPlayer().getName());
   				arena.leavePlayer(event.getPlayer().getName(), true);
   				event.setCancelled(true);
   				return;
   			}
   		}
		if(MinigamesAPI.global_players.containsKey(event.getPlayer().getName()) && !event.getPlayer().isOp()){
       		if(!event.getMessage().startsWith("/sw") && !event.getMessage().startsWith("/skywars")){
       			event.getPlayer().sendMessage("");
        		event.setCancelled(true);
       			return;
        	}
       	}
    }
	
}
