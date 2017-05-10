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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.MinecraftVersionsType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ArenaScoreboard
{
    
    HashMap<String, Scoreboard> ascore                = new HashMap<>();
    HashMap<String, Objective>  aobjective            = new HashMap<>();
    HashMap<String, Integer>    currentscore          = new HashMap<>();
    
    int                         initialized           = 0;                                // 0 = false; 1 = true;
    boolean                     custom                = false;
    
    PluginInstance              pli;
    
    ArrayList<String>           loaded_custom_strings = new ArrayList<>();
    
    public static Scoreboard getMainScoreboard()
    {
        final ScoreboardManager sbm = Bukkit.getScoreboardManager();
        return sbm == null ? null : sbm.getMainScoreboard();
    }
    
    public static Team getMainScoreboardTeam(String team)
    {
        final Scoreboard main = getMainScoreboard();
        return main == null ? null : main.getTeam(team);
    }
    
    public static boolean mainScoreboardHasPlayer(String team, Player p)
    {
        final Team t = getMainScoreboardTeam(team);
        return t == null ? false : t.hasPlayer(p);
    }
    
    public static void mainScoreboardAddPlayer(String team, Player p)
    {
        final Team t = getMainScoreboardTeam(team);
        if (t != null)
        {
            t.addPlayer(p);
        }
    }
    
    public static void mainScoreboardRemovePlayer(String team, Player p)
    {
        final Team t = getMainScoreboardTeam(team);
        if (t != null)
        {
            t.removePlayer(p);
        }
    }
    
    public static Team mainScoreboardRegisterTeam(String team)
    {
        final Scoreboard main = getMainScoreboard();
        if (main != null)
        {
            final Team t = main.getTeam(team);
            if (t == null)
            {
                main.registerNewTeam(team);
            }
        }
        return getMainScoreboardTeam(team);
    }
    
    public ArenaScoreboard()
    {
        //
    }
    
    public ArenaScoreboard(final PluginInstance pli, final JavaPlugin plugin)
    {
        this.custom = plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_CUSTOM_SCOREBOARD);
        this.initialized = 1;
        this.pli = pli;
        if (pli.getMessagesConfig().getConfig().isSet("messages.custom_scoreboard."))
        {
            for (final String configline : pli.getMessagesConfig().getConfig().getConfigurationSection("messages.custom_scoreboard.").getKeys(false))
            {
                final String line = ChatColor.translateAlternateColorCodes('&', pli.getMessagesConfig().getConfig().getString("messages.custom_scoreboard." + configline));
                this.loaded_custom_strings.add(line);
            }
        }
    }
    
    public void updateScoreboard(final JavaPlugin plugin, final Arena arena)
    {
        if (!arena.getShowScoreboard())
        {
            return;
        }
        
        if (this.initialized != 1)
        {
            this.custom = plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_CUSTOM_SCOREBOARD);
        }
        
        if (this.pli == null)
        {
            this.pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
        }
        
        Bukkit.getScheduler().runTask(MinigamesAPI.getAPI(), () -> {
            for (final String playername : arena.getAllPlayers())
            {
                if (!Validator.isPlayerValid(plugin, playername, arena))
                {
                    return;
                }
                final Player p = Bukkit.getPlayer(playername);
                final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                if (!ArenaScoreboard.this.custom)
                {
                    if (!ArenaScoreboard.this.ascore.containsKey(arena.getInternalName()))
                    {
                        if (scoreboardManager != null)
                        {
                            ArenaScoreboard.this.ascore.put(arena.getInternalName(), scoreboardManager.getNewScoreboard());
                        }
                    }
                    if (!ArenaScoreboard.this.aobjective.containsKey(arena.getInternalName()))
                    {
                        ArenaScoreboard.this.aobjective.put(arena.getInternalName(), ArenaScoreboard.this.ascore.get(arena.getInternalName()).registerNewObjective(arena.getInternalName(), "dummy"));
                        ArenaScoreboard.this.aobjective.get(arena.getInternalName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                        ArenaScoreboard.this.aobjective.get(arena.getInternalName())
                                .setDisplayName(ArenaScoreboard.this.pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getDisplayName()));
                    }
                }
                else
                {
                    if (!ArenaScoreboard.this.ascore.containsKey(playername))
                    {
                        if (scoreboardManager != null)
                        {
                            ArenaScoreboard.this.ascore.put(playername, scoreboardManager.getNewScoreboard());
                        }
                    }
                    if (!ArenaScoreboard.this.aobjective.containsKey(playername))
                    {
                        ArenaScoreboard.this.aobjective.put(playername, ArenaScoreboard.this.ascore.get(playername).registerNewObjective(playername, "dummy"));
                        ArenaScoreboard.this.aobjective.get(playername).setDisplaySlot(DisplaySlot.SIDEBAR);
                        ArenaScoreboard.this.aobjective.get(playername).setDisplayName(ArenaScoreboard.this.pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getDisplayName()));
                    }
                    else
                    {
                        ArenaScoreboard.this.aobjective.get(playername).setDisplayName(ArenaScoreboard.this.pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getDisplayName()));
                    }
                }
                
                if (ArenaScoreboard.this.custom)
                {
                    try
                    {
                        for (final String line : ArenaScoreboard.this.loaded_custom_strings)
                        {
                            final String[] line_arr = line.split(":");
                            final String line_ = line_arr[0];
                            final String score_identifier = line_arr[1];
                            int score1 = 0;
                            if (score_identifier.equalsIgnoreCase("<playercount>"))
                            {
                                score1 = arena.getAllPlayers().size();
                            }
                            else if (score_identifier.equalsIgnoreCase("<lostplayercount>"))
                            {
                                score1 = arena.getAllPlayers().size() - arena.getPlayerAlive();
                            }
                            else if (score_identifier.equalsIgnoreCase("<playeralivecount>"))
                            {
                                score1 = arena.getPlayerAlive();
                            }
                            else if (score_identifier.equalsIgnoreCase("<points>"))
                            {
                                score1 = ArenaScoreboard.this.pli.getStatsInstance().getPoints(playername);
                            }
                            else if (score_identifier.equalsIgnoreCase("<wins>"))
                            {
                                score1 = ArenaScoreboard.this.pli.getStatsInstance().getWins(playername);
                            }
                            else if (score_identifier.equalsIgnoreCase("<money>"))
                            {
                                score1 = (int) MinigamesAPI.econ.getBalance(playername);
                            }
                            if (line_.length() < 15)
                            {
                                Util.resetScores(ArenaScoreboard.this.ascore.get(playername), ChatColor.GREEN + line_);
                                Util.getScore(ArenaScoreboard.this.aobjective.get(playername), ChatColor.GREEN + line_).setScore(score1);
                            }
                            else
                            {
                                Util.resetScores(ArenaScoreboard.this.ascore.get(playername), ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13)));
                                Util.getScore(ArenaScoreboard.this.aobjective.get(playername), ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13))).setScore(score1);
                            }
                        }
                        
                        if (ArenaScoreboard.this.ascore.get(playername) != null)
                        {
                            p.setScoreboard(ArenaScoreboard.this.ascore.get(playername));
                        }
                    }
                    catch (final Exception e1)
                    {
                        pli.getPlugin().getLogger().log(Level.SEVERE, "Failed to set custom scoreboard", e1);
                    }
                }
                else
                {
                    for (final String playername_ : arena.getAllPlayers())
                    {
                        if (!Validator.isPlayerOnline(playername_))
                        {
                            continue;
                        }
                        final Player p_ = Bukkit.getPlayer(playername_);
                        if (!ArenaScoreboard.this.pli.global_lost.containsKey(playername_))
                        {
                            int score2 = 0;
                            if (ArenaScoreboard.this.currentscore.containsKey(playername_))
                            {
                                final int oldscore = ArenaScoreboard.this.currentscore.get(playername_);
                                if (score2 > oldscore)
                                {
                                    ArenaScoreboard.this.currentscore.put(playername_, score2);
                                }
                                else
                                {
                                    score2 = oldscore;
                                }
                            }
                            else
                            {
                                ArenaScoreboard.this.currentscore.put(playername_, score2);
                            }
                            try
                            {
                                if (p_.getName().length() < 15)
                                {
                                    Util.getScore(ArenaScoreboard.this.aobjective.get(arena.getInternalName()), ChatColor.GREEN + p_.getName()).setScore(0);
                                }
                                else
                                {
                                    Util.getScore(ArenaScoreboard.this.aobjective.get(arena.getInternalName()), ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3)).setScore(0);
                                }
                            }
                            catch (final Exception e2)
                            {
                                // silently ignore
                            }
                        }
                        else if (ArenaScoreboard.this.pli.global_lost.containsKey(playername_))
                        {
                            try
                            {
                                if (ArenaScoreboard.this.currentscore.containsKey(playername_))
                                {
                                    final int score3 = ArenaScoreboard.this.currentscore.get(playername_);
                                    if (p_.getName().length() < 15)
                                    {
                                        Util.resetScores(ArenaScoreboard.this.ascore.get(arena.getInternalName()), ChatColor.GREEN + p_.getName());
                                        Util.getScore(ArenaScoreboard.this.aobjective.get(arena.getInternalName()), ChatColor.RED + p_.getName()).setScore(0);
                                    }
                                    else
                                    {
                                        Util.resetScores(ArenaScoreboard.this.ascore.get(arena.getInternalName()), ChatColor.GREEN + p_.getName().substring(0, p_.getName().length() - 3));
                                        Util.getScore(ArenaScoreboard.this.aobjective.get(arena.getInternalName()), ChatColor.RED + p_.getName().substring(0, p_.getName().length() - 3)).setScore(0);
                                    }
                                }
                            }
                            catch (final Exception e3)
                            {
                                // silently ignore
                            }
                        }
                    }
                    
                    if (ArenaScoreboard.this.ascore.get(arena.getInternalName()) != null)
                    {
                        p.setScoreboard(ArenaScoreboard.this.ascore.get(arena.getInternalName()));
                    }
                }
                
            }
        });
    }
    
    public void removeScoreboard(final String arena, final Player p)
    {
        try
        {
            final ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager != null)
            {
                final Scoreboard sc = manager.getNewScoreboard();
                
                p.setScoreboard(sc);
            }
        }
        catch (final Exception e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
        }
    }
    
    public static Score get(Objective objective, String name)
    {
        if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_7_R3))
        {
            return objective.getScore(name);
        }
        return objective.getScore(Bukkit.getOfflinePlayer(name));
    }
    
    public static void reset(Scoreboard sb, String name)
    {
        if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_7_R3))
        {
            sb.resetScores(name);
        }
        else
        {
            sb.resetScores(Bukkit.getOfflinePlayer(name));
        }
    }
    
    public void clearScoreboard(final String arenaname)
    {
        if (this.ascore.containsKey(arenaname))
        {
            try
            {
                final Scoreboard sc = this.ascore.get(arenaname);
                if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_7_R3))
                {
                    for (final String player : sc.getEntries())
                    {
                        sc.resetScores(player);
                    }
                }
                else
                {
                    for (final OfflinePlayer player : sc.getPlayers())
                    {
                        sc.resetScores(player);
                    }
                }
            }
            catch (final Exception e)
            {
                if (MinigamesAPI.debug)
                {
                	MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
                }
            }
            this.ascore.remove(arenaname);
        }
        if (this.aobjective.containsKey(arenaname))
        {
            this.aobjective.remove(arenaname);
        }
        
        // ascore.put(arenaname, Bukkit.getScoreboardManager().getNewScoreboard());
    }
    
    public void setCurrentScoreMap(final HashMap<String, Integer> newcurrentscore)
    {
        this.currentscore = newcurrentscore;
    }
}
