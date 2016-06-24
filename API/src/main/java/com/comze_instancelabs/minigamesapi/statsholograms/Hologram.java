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
package com.comze_instancelabs.minigamesapi.statsholograms;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comze_instancelabs.minigamesapi.Effects;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class Hologram
{
    
    ArrayList<Integer> ids = new ArrayList<>();
    PluginInstance     pli;
    Location           l;
    
    public Hologram(final PluginInstance pli, final Location l)
    {
        this.pli = pli;
        this.l = l;
    }
    
    public void send(final Player p)
    {
        if (this.pli.getMessagesConfig().getConfig().isSet("messages.stats"))
        {
            
            double ydelta = 0.25D;
            
            final int kills_ = this.pli.getStatsInstance().getKills(p.getName());
            final int deaths_ = this.pli.getStatsInstance().getDeaths(p.getName());
            final int money_ = (int) MinigamesAPI.econ.getBalance(p.getName());
            
            final String wins = Integer.toString(this.pli.getStatsInstance().getWins(p.getName()));
            final String loses = Integer.toString(this.pli.getStatsInstance().getLoses(p.getName()));
            final String kills = Integer.toString(kills_);
            final String deaths = Integer.toString(deaths_);
            final String money = Integer.toString(money_);
            final String points = Integer.toString(this.pli.getStatsInstance().getPoints(p.getName()));
            final String kdr = Integer.toString(Math.max(kills_, 1) / Math.max(deaths_, 1));
            final ArrayList<String> s = new ArrayList<>(this.pli.getMessagesConfig().getConfig().getConfigurationSection("messages.stats").getKeys(false));
            Collections.reverse(s);
            for (final String key : s)
            {
                // Each line from the config gets checked for variables like <wins> or <money> and these get replaced by the values calculated above
                final String msg = this.pli.getMessagesConfig().getConfig().getString("messages.stats." + key).replaceAll("<wins>", wins).replaceAll("<loses>", loses)
                        .replaceAll("<alltime_kills>", kills).replaceAll("<alltime_deaths>", deaths).replaceAll("<points>", points).replaceAll("<kdr>", kdr).replaceAll("<money>", money);
                this.ids.addAll(Effects.playHologram(p, this.l.clone().add(0D, ydelta, 0D), ChatColor.translateAlternateColorCodes('&', msg), false, false));
                ydelta += 0.25D;
            }
        }
    }
    
    public ArrayList<Integer> getIds()
    {
        return this.ids;
    }
    
}
