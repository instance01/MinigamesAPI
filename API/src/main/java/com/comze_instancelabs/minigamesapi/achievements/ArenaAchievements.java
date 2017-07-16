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
package com.comze_instancelabs.minigamesapi.achievements;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.util.IconMenu;

public class ArenaAchievements
{
    
    JavaPlugin                       plugin;
    PluginInstance                   pli;
    public HashMap<String, IconMenu> lasticonm = new HashMap<>();
    
    public ArenaAchievements(final PluginInstance pli, final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.pli = pli;
    }
    
    public void openGUI(final String p, final boolean sql)
    {
        IconMenu iconm;
        final ArrayList<AAchievement> alist = this.loadPlayerAchievements(p, sql);
        final int mincount = alist.size();
        final MessagesConfig messagesConfig = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig();
        if (this.lasticonm.containsKey(p))
        {
            iconm = this.lasticonm.get(p);
        }
        else
        {
            iconm = new IconMenu(messagesConfig.achievement_item, (9 > mincount - 1) ? 9 * 1 : Math.round(mincount / 9) * 9 + 9,
                    event -> event.setWillClose(true), this.plugin);
        }
        
        int c = 0;
        for (final AAchievement aa : alist)
        {
            ItemStack icon = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
            if (aa.isDone())
            {
                icon = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
            }
            iconm.setOption(
                    c,
                    icon,
                    ChatColor.translateAlternateColorCodes('&', this.pli.getAchievementsConfig().getConfig().getString("config.achievements." + aa.getAchievementNameRaw() + ".name")),
                    aa.isDone() ? messagesConfig.achievement_done_true : messagesConfig.achievement_done_false);
            c++;
        }
        
        iconm.open(Bukkit.getPlayerExact(p));
        this.lasticonm.put(p, iconm);
    }
    
    public ArrayList<AAchievement> loadPlayerAchievements(final String playername, final boolean sql)
    {
        final ArrayList<AAchievement> ret = new ArrayList<>();
        if (sql)
        {
            // TODO MySQL Support
        }
        else
        {
            for (final String achievement : this.pli.getAchievementsConfig().getConfig().getConfigurationSection("config.achievements").getKeys(false))
            {
                final AAchievement ac = new AAchievement(achievement, playername, this.pli.getAchievementsConfig().getConfig().isSet("players." + playername + "." + achievement + ".done")
                        ? this.pli.getAchievementsConfig().getConfig().getBoolean("players." + playername + "." + achievement + ".done") : false);
                ret.add(ac);
            }
        }
        return ret;
    }
    
    public void setAchievementDone(final String playername, final String achievement, final boolean sql)
    {
        if (sql)
        {
            // TODO
        }
        else
        {
            if (!this.pli.getAchievementsConfig().getConfig().isSet("players." + playername + "." + achievement + ".done"))
            {
                this.pli.getAchievementsConfig().getConfig().set("players." + playername + "." + achievement + ".done", true);
                this.pli.getAchievementsConfig().saveConfig();
                final ArrayList<AAchievement> alist = this.loadPlayerAchievements(playername, sql);
                boolean allDone = true;
                AAchievement a;
                for (final AAchievement aac : alist)
                {
                    if (aac.getAchievementNameRaw().equalsIgnoreCase(achievement))
                    {
                        a = aac;
                    }
                    if (!aac.isDone() && !aac.getAchievementNameRaw().equalsIgnoreCase("achievement_guy"))
                    {
                        allDone = false;
                    }
                }
                final String base = "config.achievements." + achievement;
                this.pli.getRewardsInstance().giveAchievementReward(playername, this.pli.getAchievementsConfig().getConfig().getBoolean(base + ".reward.economy_reward"),
                        this.pli.getAchievementsConfig().getConfig().getBoolean(base + ".reward.command_reward"),
                        this.pli.getAchievementsConfig().getConfig().getInt(base + ".reward.econ_reward_amount"), this.pli.getAchievementsConfig().getConfig().getString(base + ".reward.cmd"));
                Bukkit.getPlayer(playername).sendMessage(this.pli.getMessagesConfig().you_got_the_achievement.replaceAll("<achievement>",
                        ChatColor.translateAlternateColorCodes('&', this.pli.getAchievementsConfig().getConfig().getString("config.achievements." + achievement + ".name"))));
                
                if (allDone)
                {
                    this.setAchievementDone(playername, "achievement_guy", sql);
                }
            }
        }
    }
    
    public void addDefaultAchievement(final String internalname, final String name, final int default_money_reward)
    {
        this.pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".enabled", false);
        this.pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".name", name);
        this.pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.economy_reward", true);
        this.pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.econ_reward_amount", default_money_reward);
        this.pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.command_reward", false);
        this.pli.getAchievementsConfig().getConfig().addDefault("config.achievements." + internalname + ".reward.cmd", "tell <player> Good job!");
    }
    
    public boolean isEnabled()
    {
        return this.pli.getAchievementsConfig().getConfig().getBoolean("config.enabled");
    }
    
    public void setEnabled(final boolean t)
    {
        this.pli.getAchievementsConfig().getConfig().set("config.enabled", t);
        this.pli.getAchievementsConfig().saveConfig();
    }
    
}
