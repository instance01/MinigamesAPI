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
package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.Util.ValueComparator;

/**
 * Statistics helper.
 * 
 * @author instancelabs
 */
public class Stats
{
    
    // used for wins and points
    // you can get points for pretty much everything in the games,
    // but these points are just for top stats, nothing more
    
    private JavaPlugin       plugin            = null;
    PluginInstance           pli               = null;
    
    public ArrayList<String> skullsetup        = new ArrayList<>();
    int                      stats_kill_points = 2;
    int                      stats_win_points  = 10;
    
    public Stats(final PluginInstance pli, final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.reloadVariables();
        this.pli = pli;
    }
    
    public void reloadVariables()
    {
        this.stats_kill_points = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_STATS_POINTS_FOR_KILL);
        this.stats_win_points = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_STATS_POINTS_FOR_WIN);
    }
    
    public void win(final String playername, final int count)
    {
        this.addWin(playername);
        this.addPoints(playername, count);
        final Player p = Bukkit.getPlayer(playername);
        if (p != null)
        {
            this.pli.getSQLInstance().updateWinnerStats(p, count, true);
        }
        else
        {
            if (MinigamesAPI.debug)
            {
                this.plugin.getLogger().fine("Failed updating SQL Stats as the player is not online anymore!");
            }
        }
    }
    
    public void lose(final String playername)
    {
        this.addLose(playername);
        final Player p = Bukkit.getPlayer(playername);
        if (p != null)
        {
            this.pli.getSQLInstance().updateLoserStats(p);
        }
        else
        {
            if (MinigamesAPI.debug)
            {
                this.plugin.getLogger().fine("Failed updating SQL Stats as the player is not online anymore!");
            }
        }
    }
    
    /**
     * Gets called on player join to ensure file stats are up to date (with mysql stats)
     * 
     * @param playername
     */
    public void update(final String playername)
    {
        final Player p = Bukkit.getPlayer(playername);
        final String uuid = p.getUniqueId().toString();
        if (this.pli.getStatsConfig().getConfig().isSet("players." + uuid + ".wins"))
        {
            final int wins = this.getWins(playername);
            final int sqlwins = this.pli.getSQLInstance().getWins(p);
            this.setWins(playername, Math.max(wins, sqlwins));
        }
        if (this.pli.getStatsConfig().getConfig().isSet("players." + uuid + ".points"))
        {
            final int points = this.getPoints(playername);
            final int sqlpoints = this.pli.getSQLInstance().getPoints(p);
            this.setPoints(playername, Math.max(points, sqlpoints));
        }
    }
    
    public void updateSQLKillsDeathsAfter(final Player p, final Arena a)
    {
        if (!a.getPlugin().isEnabled())
        {
            this.plugin.getLogger().fine("Couldn't save Death/Kill SQL stats as the server stopped/restarted.");
            return;
        }
        // Update sql server with kills stats at the end
        if (a.temp_kill_count.containsKey(p.getName()))
        {
            if (MinigamesAPI.debug)
            {
                this.plugin.getLogger().fine("" + a.temp_kill_count.get(p.getName())); //$NON-NLS-1$
            }
            this.pli.getSQLInstance().updateKillerStats(p, a.temp_kill_count.get(p.getName()));
            a.temp_kill_count.remove(p.getName());
        }
        // death stats
        if (a.temp_death_count.containsKey(p.getName()))
        {
            if (MinigamesAPI.debug)
            {
                this.plugin.getLogger().fine("" + a.temp_death_count.get(p.getName())); //$NON-NLS-1$
            }
            this.pli.getSQLInstance().updateDeathStats(p, a.temp_death_count.get(p.getName()));
            a.temp_death_count.remove(p.getName());
        }
    }
    
    public void setWins(final String playername, final int count)
    {
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        this.pli.getStatsConfig().getConfig().set("players." + uuid + ".wins", count);
        this.pli.getStatsConfig().saveConfig();
    }
    
    public void setPoints(final String playername, final int count)
    {
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        this.pli.getStatsConfig().getConfig().set("players." + uuid + ".points", count);
        this.pli.getStatsConfig().saveConfig();
    }
    
    public void addWin(final String playername)
    {
        final StatsConfig config = this.pli.getStatsConfig();
        int temp = 0;
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.getConfig().isSet("players." + uuid + ".wins"))
        {
            temp = config.getConfig().getInt("players." + uuid + ".wins");
        }
        temp++;
        this.pli.getArenaAchievements().setAchievementDone(playername, "first_win", false);
        if (temp >= 10)
        {
            this.pli.getArenaAchievements().setAchievementDone(playername, "ten_wins", false);
        }
        config.getConfig().set("players." + uuid + ".wins", temp);
        config.getConfig().set("players." + uuid + ".playername", playername);
        config.saveConfig();
    }
    
    public void addLose(final String playername)
    {
        final StatsConfig config = this.pli.getStatsConfig();
        int temp = 0;
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.getConfig().isSet("players." + uuid + ".loses"))
        {
            temp = config.getConfig().getInt("players." + uuid + ".loses");
        }
        config.getConfig().set("players." + uuid + ".loses", temp + 1);
        config.getConfig().set("players." + uuid + ".playername", playername);
        config.saveConfig();
    }
    
    public void addKill(final String playername)
    {
        final StatsConfig config = this.pli.getStatsConfig();
        int temp = 0;
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.getConfig().isSet("players." + uuid + ".kills"))
        {
            temp = config.getConfig().getInt("players." + uuid + ".kills");
        }
        temp++;
        config.getConfig().set("players." + uuid + ".kills", temp);
        config.getConfig().set("players." + uuid + ".playername", playername);
        config.saveConfig();
        this.pli.getArenaAchievements().setAchievementDone(playername, "first_blood", false);
        if (temp >= 10 && temp < 100)
        {
            this.pli.getArenaAchievements().setAchievementDone(playername, "ten_kills", false);
        }
        else if (temp >= 100)
        {
            this.pli.getArenaAchievements().setAchievementDone(playername, "hundred_kills", false);
        }
        // Moved to Rewards.java:257
        // pli.getSQLInstance().updateKillerStats(Bukkit.getPlayer(playername));
    }
    
    public void addDeath(final String playername)
    {
        final StatsConfig config = this.pli.getStatsConfig();
        int temp = 0;
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.getConfig().isSet("players." + uuid + ".deaths"))
        {
            temp = config.getConfig().getInt("players." + uuid + ".deaths");
        }
        temp++;
        config.getConfig().set("players." + uuid + ".deaths", temp);
        config.getConfig().set("players." + uuid + ".playername", playername);
        config.saveConfig();
        // Moved to Rewards.java:265
        // pli.getSQLInstance().updateDeathStats(Bukkit.getPlayer(playername));
    }
    
    public void addPoints(final String playername, final int count)
    {
        final StatsConfig config = this.pli.getStatsConfig();
        int temp = 0;
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.getConfig().isSet("players." + uuid + ".points"))
        {
            temp = config.getConfig().getInt("players." + uuid + ".points");
        }
        int temp_ = 0;
        if (MinigamesAPI.getAPI().statsglobal.getConfig().isSet("players." + uuid + ".points"))
        {
            temp_ = MinigamesAPI.getAPI().statsglobal.getConfig().getInt("players." + uuid + ".points");
        }
        else
        {
            temp_ = temp;
        }
        MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", temp_ + count);
        MinigamesAPI.getAPI().statsglobal.saveConfig();
        config.getConfig().set("players." + uuid + ".points", temp + count);
        config.saveConfig();
    }
    
    public int getPoints(final String playername)
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.isSet("players." + uuid + ".points"))
        {
            final int points = config.getInt("players." + uuid + ".points");
            return points;
        }
        return 0;
    }
    
    public int getWins(final String playername)
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.isSet("players." + uuid + ".wins"))
        {
            return config.getInt("players." + uuid + ".wins");
        }
        return 0;
    }
    
    public int getLoses(final String playername)
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.isSet("players." + uuid + ".loses"))
        {
            return config.getInt("players." + uuid + ".loses");
        }
        return 0;
    }
    
    public int getKills(final String playername)
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.isSet("players." + uuid + ".kills"))
        {
            return config.getInt("players." + uuid + ".kills");
        }
        return 0;
    }
    
    public int getDeaths(final String playername)
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final String uuid = Bukkit.getPlayer(playername).getUniqueId().toString();
        if (config.isSet("players." + uuid + ".deaths"))
        {
            return config.getInt("players." + uuid + ".deaths");
        }
        return 0;
    }
    
    public TreeMap<String, Double> getTop(final int count, final boolean wins)
    {
        int c = 0;
        String key = "wins";
        if (!wins)
        {
            key = "points";
        }
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final HashMap<String, Double> pwins = new HashMap<>();
        if (config.isSet("players."))
        {
            for (final String p : config.getConfigurationSection("players.").getKeys(false))
            {
                c++;
                if (c > 100)
                {
                    break;
                }
                pwins.put(config.getString("players." + p + ".playername"), (double) config.getInt("players." + p + "." + key));
            }
        }
        final ValueComparator bvc = new ValueComparator(pwins);
        final TreeMap<String, Double> sorted_wins = new TreeMap<>(bvc);
        sorted_wins.putAll(pwins);
        return sorted_wins;
    }
    
    public TreeMap<String, Double> getTop()
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final HashMap<String, Double> pwins = new HashMap<>();
        if (config.isSet("players."))
        {
            for (final String p : config.getConfigurationSection("players.").getKeys(false))
            {
                pwins.put(config.getString("players." + p + ".playername"), (double) config.getInt("players." + p + ".wins"));
            }
        }
        final ValueComparator bvc = new ValueComparator(pwins);
        final TreeMap<String, Double> sorted_wins = new TreeMap<>(bvc);
        sorted_wins.putAll(pwins);
        return sorted_wins;
    }
    
    public static ItemStack giveSkull(final String name)
    {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        final SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
        skullmeta.setDisplayName(name);
        skullmeta.setOwner(name);
        item.setItemMeta(skullmeta);
        return item;
    }
    
    public void saveSkull(final Location t, final int count)
    {
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        final String base = "skulls." + UUID.randomUUID().toString() + ".";
        config.set(base + "world", t.getWorld().getName());
        config.set(base + "x", t.getBlockX());
        config.set(base + "y", t.getBlockY());
        config.set(base + "z", t.getBlockZ());
        config.set(base + "pos", count);
        final BlockState state = t.getBlock().getState();
        if (state instanceof Skull)
        {
            final Skull skull_ = (Skull) state;
            config.set(base + "dir", skull_.getRotation().toString());
        }
        else
        {
            config.set(base + "dir", "SELF");
        }
        
        this.pli.getStatsConfig().saveConfig();
    }
    
    /*
     * Since Mojangs new public API this seems to be lagging hardcore (and they're disallowing more than 1 connection per minute, gg)
     */
    public void updateSkulls()
    {
        final TreeMap<String, Double> sorted_wins = this.getTop();
        final FileConfiguration config = this.pli.getStatsConfig().getConfig();
        if (config.isSet("skulls."))
        {
            for (final String skull : config.getConfigurationSection("skulls.").getKeys(false))
            {
                final String base = "skulls." + skull;
                final Location t = new Location(Bukkit.getWorld(config.getString(base + ".world")), config.getDouble(base + ".x"), config.getDouble(base + ".y"), config.getDouble(base + ".z"));
                t.getBlock().setData((byte) 0x1);
                final BlockState state = t.getBlock().getState();
                
                final int pos = config.getInt(base + ".pos");
                final String dir = config.getString(base + ".dir");
                
                if (state instanceof Skull)
                {
                    final Skull skull_ = (Skull) state;
                    skull_.setRotation(BlockFace.valueOf(dir));
                    skull_.setSkullType(SkullType.PLAYER);
                    this.plugin.getLogger().fine(pos + " " + sorted_wins.keySet().size());
                    if (pos <= sorted_wins.keySet().size())
                    {
                        final String name = (String) sorted_wins.keySet().toArray()[pos - 1];
                        skull_.setOwner(name);
                        this.plugin.getLogger().fine(name);
                    }
                    skull_.update();
                }
            }
        }
    }
}
