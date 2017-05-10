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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class ArenaLobbyScoreboard
{
    
    HashMap<String, Scoreboard> ascore                = new HashMap<>();
    HashMap<String, Objective>  aobjective            = new HashMap<>();
    
    int                         initialized           = 0;                                // 0 = false; 1 = true;
    boolean                     custom                = false;
    
    PluginInstance              pli;
    
    ArrayList<String>           loaded_custom_strings = new ArrayList<>();
    
    public ArenaLobbyScoreboard(final PluginInstance pli, final JavaPlugin plugin)
    {
        this.custom = plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_CUSTOM_SCOREBOARD);
        this.initialized = 1;
        this.pli = pli;
        if (pli.getMessagesConfig().getConfig().isSet("messages.custom_lobby_scoreboard."))
        {
            for (final String configline : pli.getMessagesConfig().getConfig().getConfigurationSection("messages.custom_lobby_scoreboard.").getKeys(false))
            {
                final String line = ChatColor.translateAlternateColorCodes('&', pli.getMessagesConfig().getConfig().getString("messages.custom_lobby_scoreboard." + configline));
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
                if (!ArenaLobbyScoreboard.this.ascore.containsKey(playername))
                {
                    final ScoreboardManager sbm = Bukkit.getScoreboardManager();
                    if (sbm != null)
                    {
                        ArenaLobbyScoreboard.this.ascore.put(playername, sbm.getNewScoreboard());
                    }
                }
                if (!ArenaLobbyScoreboard.this.aobjective.containsKey(playername))
                {
                    ArenaLobbyScoreboard.this.aobjective.put(playername, ArenaLobbyScoreboard.this.ascore.get(playername).registerNewObjective(playername, "dummy"));
                    ArenaLobbyScoreboard.this.aobjective.get(playername).setDisplaySlot(DisplaySlot.SIDEBAR);
                    ArenaLobbyScoreboard.this.aobjective.get(playername)
                            .setDisplayName(ArenaLobbyScoreboard.this.pli.getMessagesConfig().scoreboard_lobby_title.replaceAll("<arena>", arena.getDisplayName()));
                }
                else
                {
                    ArenaLobbyScoreboard.this.aobjective.get(playername).setDisplayName(ArenaLobbyScoreboard.this.pli.getMessagesConfig().scoreboard_lobby_title.replaceAll("<arena>", arena.getDisplayName()));
                }
                
                try
                {
                    if (ArenaLobbyScoreboard.this.loaded_custom_strings.size() < 1)
                    {
                        return;
                    }
                    for (final String line : ArenaLobbyScoreboard.this.loaded_custom_strings)
                    {
                        final String[] line_arr = line.split(":");
                        String line_ = line_arr[0];
                        final String score_identifier = line_arr[line_arr.length - 1];
                        if (line_arr.length > 2)
                        {
                            line_ += ":" + line_arr[1];
                        }
                        int score = 0;
                        if (score_identifier.equalsIgnoreCase("<playercount>"))
                        {
                            score = arena.getAllPlayers().size();
                        }
                        else if (score_identifier.equalsIgnoreCase("<maxplayercount>"))
                        {
                            score = arena.getMaxPlayers();
                        }
                        else if (score_identifier.equalsIgnoreCase("<points>"))
                        {
                            score = ArenaLobbyScoreboard.this.pli.getStatsInstance().getPoints(playername);
                        }
                        else if (score_identifier.equalsIgnoreCase("<wins>"))
                        {
                            score = ArenaLobbyScoreboard.this.pli.getStatsInstance().getWins(playername);
                        }
                        else if (score_identifier.equalsIgnoreCase("<money>"))
                        {
                            score = (int) MinigamesAPI.econ.getBalance(playername);
                        }
                        else if (score_identifier.equalsIgnoreCase("<kills>"))
                        {
                            score = ArenaLobbyScoreboard.this.pli.getStatsInstance().getKills(playername);
                        }
                        if (line_.length() < 15)
                        {
                            // ascore.get(arena.getInternalName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_));
                            Util.getScore(ArenaLobbyScoreboard.this.aobjective.get(playername), ChatColor.GREEN + line_).setScore(score);
                        }
                        else
                        {
                            // ascore.get(arena.getInternalName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + line_.substring(0,
                            // Math.min(line_.length() - 3, 13))));
                            Util.getScore(ArenaLobbyScoreboard.this.aobjective.get(playername), ChatColor.GREEN + line_.substring(0, Math.min(line_.length() - 3, 13))).setScore(score);
                        }
                    }
                    p.setScoreboard(ArenaLobbyScoreboard.this.ascore.get(playername));
                }
                catch (final Exception e)
                {
                    pli.getPlugin().getLogger().log(Level.SEVERE, "Failed to set custom scoreboard", e);
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
    
    public void clearScoreboard(final String arenaname)
    {
        // TODO
    }
}
