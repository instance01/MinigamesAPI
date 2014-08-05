package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.GunsConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.guns.Gun;
import com.comze_instancelabs.minigamesapi.sql.MainSQL;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;

public class PluginInstance {

	public HashMap<String, Arena> global_players = new HashMap<String, Arena>();
	public HashMap<String, Arena> global_lost = new HashMap<String, Arena>();

	private ArenaListener arenalistener = null;
	private ArenasConfig arenasconfig = null;
	private ClassesConfig classesconfig = null;
	private MessagesConfig messagesconfig = null;
	private StatsConfig statsconfig = null;
	private GunsConfig gunsconfig = null;
	private JavaPlugin plugin = null;
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	private HashMap<String, AClass> pclass = new HashMap<String, AClass>();
	private HashMap<String, AClass> aclasses = new HashMap<String, AClass>();
	private HashMap<String, Gun> guns = new HashMap<String, Gun>();
	private Rewards rew = null;
	private MainSQL sql = null;
	private Stats stats = null;
	private Classes classes = null;

	// TODO remove that
	public boolean pvp;
	public boolean die_when_falling;
	public boolean show_scoreboard;

	public ArenaScoreboard scoreboardManager = new ArenaScoreboard();
	public ArenaSetup arenaSetup = new ArenaSetup();

	int lobby_countdown = 30;
	int ingame_countdown = 10;

	public PluginInstance(JavaPlugin plugin, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig, StatsConfig statsconfig, ArrayList<Arena> arenas) {
		this.arenasconfig = arenasconfig;
		this.messagesconfig = messagesconfig;
		this.classesconfig = classesconfig;
		this.statsconfig = statsconfig;
		this.gunsconfig = new GunsConfig(plugin, false);
		this.arenas = arenas;
		this.plugin = plugin;
		rew = new Rewards(plugin);
		stats = new Stats(plugin);
		sql = new MainSQL(plugin, false);
		classes = new Classes(plugin);
		lobby_countdown = plugin.getConfig().getInt("config.lobby_countdown") + 1;
		ingame_countdown = plugin.getConfig().getInt("config.ingame_countdown") + 1;
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

	public HashMap<String, Gun> getAllGuns() {
		return this.guns;
	}

	public void addGun(String name, Gun g) {
		this.guns.put(name, g);
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

	public GunsConfig getGunsConfig() {
		return gunsconfig;
	}

	public Rewards getRewardsInstance() {
		return rew;
	}

	public void setRewardsInstance(Rewards r) {
		rew = r;
	}

	public MainSQL getSQLInstance() {
		return sql;
	}

	public Stats getStatsInstance() {
		return stats;
	}

	public ArenaListener getArenaListener() {
		return this.arenalistener;
	}

	public void setArenaListener(ArenaListener al) {
		this.arenalistener = al;
	}

	public Classes getClassesHandler() {
		return this.classes;
	}

	public void setClassesHandler(Classes c) {
		this.classes = c;
	}

	public int getIngameCountdown() {
		return this.ingame_countdown;
	}

	public int getLobbyCountdown() {
		return this.lobby_countdown;
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public void clearArenas() {
		arenas.clear();
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

	public Arena removeArenaByName(String arenaname) {
		Arena torem = null;
		for (Arena a : getArenas()) {
			if (a.getName().equalsIgnoreCase(arenaname)) {
				torem = a;
			}
		}
		if (torem != null) {
			removeArena(torem);
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
