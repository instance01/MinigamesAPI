package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.Util.ValueComparator;

public class Stats {

	// used for wins and points
	// you can get points for pretty much everything in the games,
	// but these points are just for top stats, nothing more

	// TODO Add MySQL support

	private JavaPlugin plugin = null;

	public ArrayList<String> skullsetup = new ArrayList<String>();
	
	public Stats(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void win(String playername, int count) {
		addWin(playername);
		addPoints(playername, count);
		MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().updateWinnerStats(playername, count);
	}

	public void lose(String playername) {
		addLose(playername);
		MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().updateLoserStats(playername);
	}

	/**
	 * Gets called on player join to ensure file stats are up to date (with mysql stats)
	 * 
	 * @param playername
	 */
	public void update(String playername) {
		if (plugin.getConfig().getBoolean("mysql.enabled")) {
			setWins(playername, MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().getWins(playername));
			setPoints(playername, MinigamesAPI.getAPI().pinstances.get(plugin).getSQLInstance().getPoints(playername));
		}
	}

	public void setWins(String playername, int count) {
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig().set("players." + uuid + ".wins", count);
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().saveConfig();
	}

	public void setPoints(String playername, int count) {
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig().set("players." + uuid + ".points", count);
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().saveConfig();
	}

	public void addWin(String playername) {
		StatsConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig();
		int temp = 0;
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.getConfig().isSet("players." + uuid + ".wins")) {
			temp = config.getConfig().getInt("players." + uuid + ".wins");
		}
		config.getConfig().set("players." + uuid + ".wins", temp + 1);
		config.getConfig().set("players." + uuid + ".playername", playername);
		config.saveConfig();
	}

	public void addLose(String playername) {
		StatsConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig();
		int temp = 0;
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.getConfig().isSet("players." + uuid + ".loses")) {
			temp = config.getConfig().getInt("players." + uuid + ".loses");
		}
		config.getConfig().set("players." + uuid + ".loses", temp + 1);
		config.getConfig().set("players." + uuid + ".playername", playername);
		config.saveConfig();
	}

	public void addPoints(String playername, int count) {
		StatsConfig config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig();
		int temp = 0;
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.getConfig().isSet("players." + uuid + ".points")) {
			temp = config.getConfig().getInt("players." + uuid + ".points");
		}
		config.getConfig().set("players." + uuid + ".points", temp + count);
		config.saveConfig();
	}

	public int getPoints(String playername) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.isSet("players." + uuid + ".points")) {
			return config.getInt("players." + uuid + ".points");
		}
		return 0;
	}

	public int getWins(String playername) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
		if (config.isSet("players." + uuid + ".wins")) {
			return config.getInt("players." + uuid + ".wins");
		}
		return 0;
	}

	public void getTop(int count) {
		// TODO top wins
	}

	public TreeMap<String, Double> getTop() {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		HashMap<String, Double> pwins = new HashMap<String, Double>();
		if (config.isSet("players.")) {
			for (String p : config.getConfigurationSection("players.").getKeys(false)) {
				pwins.put(config.getString("players." + p + ".playername"), (double) config.getInt("players." + p + ".wins"));
			}
		}
		ValueComparator bvc = new ValueComparator(pwins);
		TreeMap<String, Double> sorted_wins = new TreeMap<String, Double>(bvc);
		sorted_wins.putAll(pwins);
		return sorted_wins;
	}

	public static ItemStack giveSkull(String name) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
		skullmeta.setDisplayName(name);
		skullmeta.setOwner(name);
		item.setItemMeta(skullmeta);
		return item;
	}
	
	public void saveSkull(Location t, int count) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		String base = "skulls." + UUID.randomUUID().toString() + ".";
		config.set(base + "world", t.getWorld().getName());
		config.set(base + "x", t.getBlockX());
		config.set(base + "y", t.getBlockY());
		config.set(base + "z", t.getBlockZ());
		config.set(base + "pos", count);
		BlockState state = t.getBlock().getState();
		if(state instanceof Skull){
			Skull skull_ = (Skull) state;
			config.set(base + "dir", skull_.getRotation().toString());
		}else{
			config.set(base + "dir", "SELF");
		}
		
		MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().saveConfig();
	}

	/*
	 * Since Mojangs new public API this seems to be lagging hardcore (and they're disallowing more than 1 connection per minute, gg)
	 */
	public void updateSkulls() {
		TreeMap<String, Double> sorted_wins = getTop();
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getStatsConfig().getConfig();
		if (config.isSet("skulls.")) {
			for (String skull : config.getConfigurationSection("skulls.").getKeys(false)) {
				String base = "skulls." + skull;
				Location t = new Location(Bukkit.getWorld(config.getString(base + ".world")), config.getDouble(base + ".x"), config.getDouble(base + ".y"), config.getDouble(base + ".z"));
				t.getBlock().setData((byte) 0x1);
				BlockState state = t.getBlock().getState();

				int pos = config.getInt(base + ".pos");
				String dir = config.getString(base + ".dir");

				if (state instanceof Skull) {
					Skull skull_ = (Skull) state;
					skull_.setRotation(BlockFace.valueOf(dir));
					skull_.setSkullType(SkullType.PLAYER);
					System.out.println(pos + " " + sorted_wins.keySet().size());
					if (pos <= sorted_wins.keySet().size()) {
						String name = (String) sorted_wins.keySet().toArray()[pos - 1];
						skull_.setOwner(name);
						System.out.println(name);
					}
					skull_.update();
				}
			}
		}
	}

}
