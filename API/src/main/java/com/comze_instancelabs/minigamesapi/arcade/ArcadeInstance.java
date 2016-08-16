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
package com.comze_instancelabs.minigamesapi.arcade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArcadeInstance
{
    
    public ArrayList<PluginInstance> minigames    = new ArrayList<>();
    int                              currentindex = 0;
    public ArrayList<String>         players      = new ArrayList<>();
    Arena                            arena;
    JavaPlugin                       plugin;
    
    boolean                          in_a_game    = false;
    Arena                            currentarena = null;
    boolean                          started;
    
    public ArcadeInstance(final JavaPlugin plugin, final ArrayList<PluginInstance> minigames, final Arena arena)
    {
        this.minigames = minigames;
        this.arena = arena;
        this.plugin = plugin;
    }
    
    // TODO max 16 players!
    public void joinArcade(final String playername)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(this.plugin);
        if (!this.players.contains(playername))
        {
            this.players.add(playername);
            this.arena.addPlayer(playername);
        }
        final Player p = Bukkit.getPlayer(playername);
        if (p == null)
        {
            return;
        }
        if (this.players.size() >= this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_ARCADE_MIN_PLAYERS))
        {
            boolean msg = true;
            if (!this.started)
            {
                this.startArcade();
            }
            else
            {
                if (this.currentindex < this.minigames.size())
                {
                    if (this.in_a_game)
                    {
                        if (this.currentarena != null)
                        {
                            if (p != null)
                            {
                                final PluginInstance pli_ = this.minigames.get(this.currentindex);
                                if (this.currentarena.getArenaState() != ArenaState.INGAME && this.currentarena.getArenaState() != ArenaState.RESTARTING)
                                {
                                    this.currentarena.joinPlayerLobby(playername, this, false, true);
                                }
                                else
                                {
                                    msg = false;
                                    this.currentarena.spectateArcade(playername);
                                }
                                
                                pli_.scoreboardManager.updateScoreboard(pli_.getPlugin(), this.currentarena);
                            }
                        }
                    }
                }
            }
            if (msg)
            {
                p.sendMessage(MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().arcade_joined_waiting.replaceAll("<count>", "0"));
            }
            else
            {
                p.sendMessage(MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().arcade_joined_spectator);
            }
        }
        else
        {
            p.sendMessage(MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().arcade_joined_waiting.replaceAll("<count>",
                    Integer.toString(this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_ARCADE_MIN_PLAYERS) - this.players.size())));
        }
    }
    
    public void leaveArcade(final String playername)
    {
        this.leaveArcade(playername, true);
    }
    
    public void leaveArcade(final String playername, final boolean endOfGame)
    {
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(this.plugin);
        if (this.players.contains(playername))
        {
            this.players.remove(playername);
        }
        if (this.arena.containsPlayer(playername))
        {
            this.arena.removePlayer(playername);
        }
        if (this.minigames.get(this.currentindex).getArenas().size() > 0)
        {
            if (this.minigames.get(this.currentindex).getArenas().get(0).containsPlayer(playername))
            {
                this.minigames.get(this.currentindex).getArenas().get(0).leavePlayer(playername, false, false);
            }
        }
        Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
            final Player p = Bukkit.getPlayer(playername);
            if (p != null)
            {
                Util.teleportPlayerFixed(p, ArcadeInstance.this.arena.getMainLobbyTemp());
                pli.getSpectatorManager().setSpectate(p, false);
                if (!p.isOp())
                {
                    p.setFlying(false);
                    p.setAllowFlight(false);
                }
            }
        }, 20L);
        this.clean();
        
        // This shouldn't be necessary anymore except for arcade spectators
        if (pli.containsGlobalPlayer(playername))
        {
            pli.global_players.remove(playername);
        }
        if (pli.containsGlobalLost(playername))
        {
            pli.global_lost.remove(playername);
        }
        if (this.currentarena != null)
        {
            MinigamesAPI.getAPI();
            final PluginInstance pli_ = MinigamesAPI.pinstances.get(this.currentarena.getPlugin());
            if (pli_ != null)
            {
                if (pli_.containsGlobalPlayer(playername))
                {
                    pli_.global_players.remove(playername);
                }
                if (pli_.containsGlobalLost(playername))
                {
                    pli_.global_lost.remove(playername);
                }
            }
        }
        
        Util.updateSign(this.plugin, this.arena);
        
        if (endOfGame)
        {
            if (this.players.size() < 2)
            {
                this.stopArcade(false);
            }
        }
    }
    
    int currentlobbycount = 31;
    int currenttaskid     = 0;
    
    public void startArcade()
    {
        if (this.started)
        {
            return;
        }
        this.started = true;
        Collections.shuffle(this.minigames);
        
        this.currentlobbycount = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_ARCADE_LOBBY_COUNTDOWN) + 1;
        final ArcadeInstance ai = this;
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(this.plugin);
        
        this.currenttaskid = Bukkit.getScheduler().runTaskTimer(MinigamesAPI.getAPI(), () -> {
            ArcadeInstance.this.currentlobbycount--;
            if (ArcadeInstance.this.currentlobbycount == 60 || ArcadeInstance.this.currentlobbycount == 30 || ArcadeInstance.this.currentlobbycount == 15 || ArcadeInstance.this.currentlobbycount == 10
                    || ArcadeInstance.this.currentlobbycount < 6)
            {
                for (final String p_ : ai.players)
                {
                    if (Validator.isPlayerOnline(p_))
                    {
                        final Player p = Bukkit.getPlayer(p_);
                        p.sendMessage(pli.getMessagesConfig().starting_in.replaceAll("<count>", Integer.toString(ArcadeInstance.this.currentlobbycount)));
                    }
                }
            }
            if (ArcadeInstance.this.currentlobbycount < 1)
            {
                ArcadeInstance.this.currentindex--;
                ai.nextMinigame();
                try
                {
                    Bukkit.getScheduler().cancelTask(ArcadeInstance.this.currenttaskid);
                }
                catch (final Exception e)
                {
                    // silently ignore
                }
            }
        }, 5L, 20).getTaskId();
    }
    
    public void stopArcade(final boolean stopOfGame)
    {
        try
        {
            Bukkit.getScheduler().cancelTask(this.currenttaskid);
        }
        catch (final Exception e)
        {
            // silently ignore
        }
        final ArrayList<String> temp = new ArrayList<>(this.players);
        for (final String p_ : temp)
        {
            this.leaveArcade(p_, false);
        }
        this.players.clear();
        this.started = false;
        this.in_a_game = false;
        this.currentarena = null;
        this.currentindex = 0;
        
        final HashSet hs = new HashSet();
        hs.addAll(temp);
        temp.clear();
        temp.addAll(hs);
        final ArcadeInstance ai = this;
        if (stopOfGame && this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_ARCADE_INFINITE_ENABLED))
        {
            if (temp.size() > 1)
            {
                for (final String p_ : temp)
                {
                    Util.sendMessage(this.plugin, Bukkit.getPlayer(p_), MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().arcade_new_round.replaceAll("<count>",
                            Integer.toString(this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_ARCADE_INFINITE_SECONDS_TO_NEW_ROUND))));
                }
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    for (final String p_ : temp)
                    {
                        if (!ArcadeInstance.this.players.contains(p_))
                        {
                            ArcadeInstance.this.players.add(p_);
                        }
                    }
                    ai.startArcade();
                }, Math.max(40L, 20L * this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_ARCADE_INFINITE_SECONDS_TO_NEW_ROUND)));
            }
        }
    }
    
    public void stopArcade()
    {
        this.stopArcade(false);
    }
    
    public void stopCurrentMinigame()
    {
        if (this.currentindex < this.minigames.size())
        {
            final PluginInstance mg = this.minigames.get(this.currentindex);
            if (mg.getArenas().size() > 0)
            {
                if (mg.getPlugin().getConfig().getBoolean(ArenaConfigStrings.CONFIG_ARCADE_ARENA_TO_PREFER_ENABLED))
                {
                    final String arenaname = mg.getPlugin().getConfig().getString(ArenaConfigStrings.CONFIG_ARCADE_ARENA_TO_PREFER_ARENA);
                    final Arena a = mg.getArenaByName(arenaname);
                    if (a != null)
                    {
                        a.stopArena();
                    }
                }
                else
                {
                    this.minigames.get(this.currentindex).getArenas().get(0).stopArena();
                }
            }
        }
    }
    
    public void nextMinigame()
    {
        this.nextMinigame(30L);
    }
    
    public void nextMinigame(final long delay)
    {
        this.in_a_game = false;
        
        if (this.currentindex < this.minigames.size() - 1)
        {
            this.currentindex++;
        }
        else
        {
            this.arena.stopArena();
            // stopArcade();
            return;
        }
        final ArcadeInstance ai = this;
        Bukkit.getScheduler().runTaskLater(MinigamesAPI.getAPI(), () -> {
            final ArrayList<String> temp = new ArrayList<>(ArcadeInstance.this.players);
            
            final PluginInstance mg = ArcadeInstance.this.minigames.get(ArcadeInstance.this.currentindex);
            if (mg.getPlugin().getConfig().getBoolean(ArenaConfigStrings.CONFIG_ARCADE_ENABLED))
            {
                Arena a = null;
                if (mg.getPlugin().getConfig().getBoolean(ArenaConfigStrings.CONFIG_ARCADE_ARENA_TO_PREFER_ENABLED))
                {
                    final String arenaname = mg.getPlugin().getConfig().getString(ArenaConfigStrings.CONFIG_ARCADE_ARENA_TO_PREFER_ARENA);
                    a = mg.getArenaByName(arenaname);
                    if (a == null)
                    {
                        for (final Arena a_1 : mg.getArenas())
                        {
                            if (a_1.getArenaState() == ArenaState.JOIN || a_1.getArenaState() == ArenaState.STARTING)
                            {
                                a = a_1;
                                break;
                            }
                        }
                    }
                }
                else
                {
                    for (final Arena a_2 : mg.getArenas())
                    {
                        if (a_2.getArenaState() == ArenaState.JOIN || a_2.getArenaState() == ArenaState.STARTING)
                        {
                            a = a_2;
                            break;
                        }
                    }
                }
                if (a != null)
                {
                    ArcadeInstance.this.in_a_game = true;
                    ArcadeInstance.this.currentarena = a;
                    final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(ArcadeInstance.this.plugin);
                    for (final String p_ : temp)
                    {
                        if (Validator.isPlayerOnline(p_))
                        {
                            final String minigame = mg.getArenaListener().getName();
                            if (!a.containsPlayer(p_))
                            {
                                Bukkit.getPlayer(p_)
                                        .sendMessage(mg.getMessagesConfig().arcade_next_minigame.replaceAll("<minigame>", Character.toUpperCase(minigame.charAt(0)) + minigame.substring(1)));
                                a.joinPlayerLobby(p_, ai, ArcadeInstance.this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_ARCADE_SHOW_EACH_LOBBY_COUNTDOWN), false);
                            }
                            pli.getSpectatorManager().setSpectate(Bukkit.getPlayer(p_), false);
                        }
                    }
                }
                else
                {
                    ArcadeInstance.this.nextMinigame(5L);
                }
            }
            else
            {
                ArcadeInstance.this.nextMinigame(5L);
            }
        }, delay);
    }
    
    public void clean()
    {
        final ArrayList<String> rem = new ArrayList<>();
        for (final String p_ : this.players)
        {
            if (!Validator.isPlayerOnline(p_))
            {
                rem.add(p_);
            }
        }
        for (final String r : rem)
        {
            if (this.players.contains(r))
            {
                this.players.remove(r);
            }
        }
    }
    
}
