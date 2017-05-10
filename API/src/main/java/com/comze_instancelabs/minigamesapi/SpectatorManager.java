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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * Spectator manager.
 * 
 * @author instancelabs
 */
public class SpectatorManager
{
    
    JavaPlugin                              plugin;
    private final HashMap<String, IconMenu> lasticonm = new HashMap<>();
    
    private static final Set<UUID> spectators = new HashSet<>();
    
    public SpectatorManager(final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.setup();
    }
    
    public void setup()
    {
        final Team t = ArenaScoreboard.getMainScoreboardTeam("spectators");
        if (t != null)
        {
            t.setCanSeeFriendlyInvisibles(true);
        }
        this.clear();
    }
    
    public void setSpectate(final Player p, final boolean spectate)
    {
        try
        {
            boolean useScoreboard = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_SPECTATOR_SCOREBOARD);
            if (spectate)
            {
                spectators.add(p.getUniqueId());
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 5), true);
                if (useScoreboard)
                {
                    ArenaScoreboard.mainScoreboardAddPlayer("spectators", p);
                }
            }
            else
            {
                if (spectators.remove(p.getUniqueId()))
                {
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    if (useScoreboard)
                    {
                        if (ArenaScoreboard.mainScoreboardHasPlayer("spectators", p))
                        {
                            ArenaScoreboard.mainScoreboardRemovePlayer("spectators", p);
                        }
                    }
                }
            }
        }
        catch (final Exception e)
        {
            // silently ignore
        }
    }
    
    @Deprecated
    public static boolean isSpectating(final Player p)
    {
        return spectators.contains(p.getUniqueId());
    }
    
    private void clear()
    {
        spectators.clear();
        final Team t = ArenaScoreboard.getMainScoreboardTeam("spectators");
        if (t != null)
        {
            final ArrayList<OfflinePlayer> offp_set = new ArrayList<>(t.getPlayers());
            for (final OfflinePlayer offp : offp_set)
            {
                t.removePlayer(offp);
            }
        }
    }
    
    public void openSpectatorGUI(final Player p, final Arena a)
    {
        IconMenu iconm;
        final int mincount = a.getAllPlayers().size();
        if (this.lasticonm.containsKey(p.getName()))
        {
            iconm = this.lasticonm.get(p.getName());
        }
        else
        {
            iconm = new IconMenu(MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().spectator_item, (9 > mincount - 1) ? 9 : Math.round(mincount / 9) * 9 + 9, event -> {
                if (event.getPlayer().getName().equalsIgnoreCase(p.getName()))
                {
                    final String d = event.getName();
                    final Player p1 = event.getPlayer();
                    
                    final Player p_ = Bukkit.getPlayer(d);
                    if (p_ != null && p1 != null)
                    {
                        Util.teleportPlayerFixed(p1, new Location(p1.getWorld(), p_.getLocation().getX(), p1.getLocation().getY(), p_.getLocation().getZ()));
                    }
                }
                event.setWillClose(true);
            }, this.plugin);
        }
        
        iconm.clear();
        
        final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(this.plugin);
        int c = 0;
        for (final String p__ : a.getAllPlayers())
        {
            final Player p_ = Bukkit.getPlayer(p__);
            if (p_ != null)
            {
                if (pli.global_players.containsKey(p__) && !pli.global_lost.containsKey(p__))
                {
                    if (a.getInternalName().equalsIgnoreCase(pli.global_players.get(p__).getInternalName()))
                    {
                        iconm.setOption(c, Util.getCustomHead(p__), p__, "");
                        c++;
                    }
                }
            }
        }
        
        iconm.open(p);
        this.lasticonm.put(p.getName(), iconm);
    }
    
    HashMap<String, ArrayList<String>> pspecs   = new HashMap<>();
    HashMap<String, ArrayList<String>> splayers = new HashMap<>();
    
    public void hideSpectator(final Player spec, final ArrayList<String> players)
    {
        for (final String p_ : players)
        {
            if (Validator.isPlayerOnline(p_))
            {
                final Player p = Bukkit.getPlayer(p_);
                spec.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 600, 1));
                if (this.pspecs.containsKey(p_))
                {
                    final ArrayList<String> t = this.pspecs.get(p_);
                    t.add(spec.getName());
                    this.pspecs.put(p_, t);
                }
                else
                {
                    this.pspecs.put(p_, new ArrayList<>(Arrays.asList(spec.getName())));
                }
            }
        }
        this.splayers.put(spec.getName(), players);
    }
    
    public void showSpectator(final Player spec)
    {
        if (this.splayers.containsKey(spec.getName()))
        {
            for (final String p_ : this.splayers.get(spec.getName()))
            {
                if (Validator.isPlayerOnline(p_))
                {
                    final Player p = Bukkit.getPlayer(p_);
                    if (this.pspecs.containsKey(p_))
                    {
                        final ArrayList<String> t = this.pspecs.get(p_);
                        t.remove(spec.getName());
                        spec.removePotionEffect(PotionEffectType.INVISIBILITY);
                        this.pspecs.put(p_, t);
                    }
                }
            }
            this.splayers.remove(spec.getName());
        }
    }
    
    public void showSpectators(final Player p)
    {
        if (this.pspecs.containsKey(p.getName()))
        {
            for (final String p_ : this.pspecs.get(p.getName()))
            {
                if (Validator.isPlayerOnline(p_))
                {
                    final Player spec = Bukkit.getPlayer(p_);
                    if (this.splayers.containsKey(p_))
                    {
                        final ArrayList<String> t = this.splayers.get(p_);
                        t.remove(spec.getName());
                        spec.removePotionEffect(PotionEffectType.INVISIBILITY);
                        this.splayers.put(p_, t);
                    }
                }
            }
            this.pspecs.remove(p.getName());
        }
    }
}
