package com.comze_instancelabs.minigamesapi;

import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.sql.MainSQL;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class PluginInstance {

	private ArenasConfig arenasconfig = null;
	private ClassesConfig classesconfig = null;
	private MessagesConfig messagesconfig = null;
	private StatsConfig statsconfig = null;
	private JavaPlugin plugin = null;
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	private HashMap<String, AClass> pclass = new HashMap<String, AClass>();
	private HashMap<String, AClass> aclasses = new HashMap<String, AClass>();
	private Rewards rew = null;
	private MainSQL sql = null;
	private Stats stats = null;

	// TODO remove that
	public boolean pvp;
	public boolean die_when_falling;
	public boolean show_scoreboard;

	public ArenaScoreboard scoreboardManager = new ArenaScoreboard();
	public ArenaSetup arenaSetup = new ArenaSetup();

	public PluginInstance(JavaPlugin plugin, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig, StatsConfig statsconfig, ArrayList<Arena> arenas) {
		this.arenasconfig = arenasconfig;
		this.messagesconfig = messagesconfig;
		this.classesconfig = classesconfig;
		this.statsconfig = statsconfig;
		this.arenas = arenas;
		this.plugin = plugin;
		rew = new Rewards(plugin);
		stats = new Stats(plugin);
		sql = new MainSQL(plugin, false);
	}

	public PluginInstance(JavaPlugin plugin, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig, StatsConfig statsconfig) {
		this(plugin, arenasconfig, messagesconfig, classesconfig, statsconfig, new ArrayList<Arena>());
	}

	public HashMap<String, AClass> getAClasses() {
		return this.aclasses;
	}

	public HashMap<String, AClass> getPClasses() {
		return this.pclass;
	}

	public void addAClass(String name, AClass a) {
		this.aclasses.put(name, a);
	}

	public void setPClass(String player, AClass a) {
		this.pclass.put(player, a);
	}

	public ArenasConfig getArenasConfig() {
		return arenasconfig;
	}

	public MessagesConfig getMessagesConfig() {
		return messagesconfig;
	}

	public ClassesConfig getClassesConfig() {
		return classesconfig;
	}

	public StatsConfig getStatsConfig() {
		return statsconfig;
	}

	public Rewards getRewardsInstance() {
		return rew;
	}

	public MainSQL getSQLInstance() {
		return sql;
	}

	public Stats getStatsInstance() {
		return stats;
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public ArrayList<Arena> addArena(Arena arena) {
		arenas.add(arena);
		return getArenas();
	}

	public Arena getArenaByName(String arenaname) {
		for (Arena a : getArenas()) {
			if (a.getName().equalsIgnoreCase(arenaname)) {
				return a;
			}
		}
		return null;
	}

	public boolean removeArena(Arena arena) {
		if (arenas.contains(arena)) {
			arenas.remove(arena);
			return true;
		}
		return false;
	}

	public void addLoadedArenas(ArrayList<Arena> arenas) {
		this.arenas = arenas;
	}

}
