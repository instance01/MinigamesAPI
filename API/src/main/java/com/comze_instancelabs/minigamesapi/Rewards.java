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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Rewards
{
    
    private JavaPlugin  plugin                    = null;
    
    private boolean     economyrewards;
    private boolean     itemrewards;
    private boolean     commandrewards;
    private boolean     kill_economyrewards;
    private boolean     kill_commandrewards;
    private boolean     participation_economyrewards;
    private boolean     participation_commandrewards;
    
    private int         econ_reward               = 0;
    private int         kill_econ_reward          = 0;
    private int         participation_econ_reward = 0;
    private String      command                   = "";
    private String      kill_command              = "";
    private String      participation_command     = "";
    private ItemStack[] items                     = null;
    
    public Rewards(final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.reloadVariables();
        
        if (!MinigamesAPI.economy)
        {
            this.economyrewards = false;
            this.kill_economyrewards = false;
            this.participation_economyrewards = false;
        }
    }
    
    public void reloadVariables()
    {
        this.economyrewards = this.plugin.getConfig().getBoolean("config.rewards.economy");
        this.itemrewards = this.plugin.getConfig().getBoolean("config.rewards.item_reward");
        this.commandrewards = this.plugin.getConfig().getBoolean("config.rewards.command_reward");
        this.kill_economyrewards = this.plugin.getConfig().getBoolean("config.rewards.economy_for_kills");
        this.kill_commandrewards = this.plugin.getConfig().getBoolean("config.rewards.command_reward_for_kills");
        this.participation_economyrewards = this.plugin.getConfig().getBoolean("config.rewards.economy_for_participation");
        this.participation_commandrewards = this.plugin.getConfig().getBoolean("config.rewards.command_reward_for_participation");
        
        this.econ_reward = this.plugin.getConfig().getInt("config.rewards.economy_reward");
        this.command = this.plugin.getConfig().getString("config.rewards.command");
        this.items = Util.parseItems(this.plugin.getConfig().getString("config.rewards.item_reward_ids")).toArray(new ItemStack[0]);
        this.kill_econ_reward = this.plugin.getConfig().getInt("config.rewards.economy_reward_for_kills");
        this.kill_command = this.plugin.getConfig().getString("config.rewards.command_for_kills");
        this.participation_econ_reward = this.plugin.getConfig().getInt("config.rewards.economy_reward_for_participation");
        this.participation_command = this.plugin.getConfig().getString("config.rewards.command_for_participation");
    }
    
    /**
     * Give all win rewards to players who won the game
     * 
     * @param arena
     *            Arena
     */
    public void giveRewardsToWinners(final Arena arena)
    {
        for (final String p_ : arena.getAllPlayers())
        {
            this.giveWinReward(p_, arena);
        }
    }
    
    @Deprecated
    public void giveReward(final String p_)
    {
        if (Validator.isPlayerOnline(p_))
        {
            final Player p = Bukkit.getPlayer(p_);
            
            if (this.economyrewards)
            {
                MinigamesAPI.getAPI();
                MinigamesAPI.econ.depositPlayer(p.getName(), this.econ_reward);
            }
            
            MinigamesAPI.getAPI().getPluginInstance(this.plugin).getStatsInstance().win(p_, 10);
        }
    }
    
    /**
     * Give a player a kill reward
     * 
     * @param p_
     *            Playername
     * @param reward
     *            Amount of statistics points the player gets
     */
    public void giveKillReward(final String p_)
    {
        if (Validator.isPlayerOnline(p_))
        {
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(this.plugin);
            final Player p = Bukkit.getPlayer(p_);
            
            if (this.kill_economyrewards && MinigamesAPI.economy)
            {
                MinigamesAPI.getAPI();
                MinigamesAPI.econ.depositPlayer(p.getName(), this.kill_econ_reward);
            }
            if (this.kill_commandrewards)
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.kill_command.replaceAll("<player>", p_));
            }
            
            pli.getStatsInstance().addPoints(p_, pli.getStatsInstance().stats_kill_points);
            pli.getStatsInstance().addKill(p_);
            pli.getSQLInstance().updateWinnerStats(p, pli.getStatsInstance().stats_kill_points, false);
        }
    }
    
    @Deprecated
    public void giveKillReward(final String p_, final int reward)
    {
        this.giveKillReward(p_);
    }
    
    /**
     * Gives a player an achievement reward
     * 
     * @param p_
     *            Playername
     * @param econ
     *            Whether economy rewards are enabled
     * @param command
     *            Whether command rewards are enabled
     * @param money_reward
     *            Amount of money to reward if economy rewards are enabled
     * @param cmd
     *            Command to execute if command rewards are enabled
     */
    public void giveAchievementReward(final String p_, final boolean econ, final boolean command, final int money_reward, final String cmd)
    {
        if (Validator.isPlayerOnline(p_))
        {
            final Player p = Bukkit.getPlayer(p_);
            
            if (econ && MinigamesAPI.economy)
            {
                MinigamesAPI.getAPI();
                MinigamesAPI.econ.depositPlayer(p.getName(), money_reward);
            }
            if (command)
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<player>", p_));
            }
        }
    }
    
    public void giveWinReward(final String p_, final Arena a)
    {
        this.giveWinReward(p_, a, 1);
    }
    
    public void giveWinReward(final String p_, final Arena a, final int global_multiplier)
    {
        this.giveWinReward(p_, a, a.getAllPlayers(), global_multiplier);
    }
    
    /**
     * Gives all rewards to a player who won and sends reward messages/win broadcasts
     * 
     * @param p_
     *            Playername
     * @param a
     *            Arena
     * @param players
     *            Optional array of players to send win broadcast to
     * @param global_multiplier
     *            Money reward multiplier (default: 1)
     */
    public void giveWinReward(final String p_, final Arena a, final ArrayList<String> players, final int global_multiplier)
    {
        if (Validator.isPlayerOnline(p_))
        {
            final PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(this.plugin);
            final Player p = Bukkit.getPlayer(p_);
            if (!pli.global_lost.containsKey(p_))
            {
                String received_rewards_msg = pli.getMessagesConfig().you_received_rewards;
                if (this.economyrewards && MinigamesAPI.economy)
                {
                    int multiplier = global_multiplier;
                    if (pli.getShopHandler().hasItemBought(p_, "coin_boost2_solo"))
                    {
                        multiplier = 2;
                    }
                    if (pli.getShopHandler().hasItemBought(p_, "coin_boost3_solo"))
                    {
                        multiplier = 3;
                    }
                    MinigamesAPI.getAPI();
                    MinigamesAPI.econ.depositPlayer(p.getName(), this.econ_reward * multiplier);
                    received_rewards_msg = received_rewards_msg.replaceAll("<economyreward>", Integer.toString(this.econ_reward * multiplier) + " " + MinigamesAPI.econ.currencyNamePlural());
                }
                else
                {
                    received_rewards_msg = received_rewards_msg.replaceAll("<economyreward>", "");
                }
                if (this.itemrewards)
                {
                    p.getInventory().addItem(this.items);
                    p.updateInventory();
                    String items_str = "";
                    for (final ItemStack i : this.items)
                    {
                        items_str += Integer.toString(i.getAmount()) + " " + Character.toUpperCase(i.getType().toString().charAt(0)) + i.getType().toString().toLowerCase().substring(1) + ", ";
                    }
                    if (items_str.length() > 2)
                    {
                        items_str = items_str.substring(0, items_str.length() - 2);
                    }
                    if (this.economyrewards && MinigamesAPI.economy)
                    {
                        received_rewards_msg += pli.getMessagesConfig().you_received_rewards_2;
                        received_rewards_msg += pli.getMessagesConfig().you_received_rewards_3.replaceAll("<itemreward>", items_str);
                    }
                    else
                    {
                        received_rewards_msg += pli.getMessagesConfig().you_received_rewards_3.replaceAll("<itemreward>", items_str);
                    }
                }
                else
                {
                    received_rewards_msg += pli.getMessagesConfig().you_received_rewards_3.replaceAll("<itemreward>", "");
                }
                if (this.commandrewards)
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replaceAll("<player>", p_));
                }
                
                pli.getStatsInstance().win(p_, pli.getStatsInstance().stats_win_points);
                
                try
                {
                    if (this.plugin.getConfig().getBoolean("config.broadcast_win"))
                    {
                        final String msgs[] = pli.getMessagesConfig().server_broadcast_winner.replaceAll("<player>", p_).replaceAll("<arena>", a.getInternalName()).split(";");
                        for (final String msg : msgs)
                        {
                            Bukkit.getServer().broadcastMessage(msg);
                        }
                    }
                    else
                    {
                        final String msgs[] = pli.getMessagesConfig().server_broadcast_winner.replaceAll("<player>", p_).replaceAll("<arena>", a.getInternalName()).split(";");
                        for (final String playername : players)
                        {
                            if (Validator.isPlayerOnline(playername))
                            {
                                Bukkit.getPlayer(playername).sendMessage(msgs);
                            }
                        }
                    }
                }
                catch (final Exception e)
                {
                    System.out.println("Could not find arena for broadcast. " + e.getMessage());
                }
                
                Util.sendMessage(this.plugin, p, pli.getMessagesConfig().you_won);
                Util.sendMessage(this.plugin, p, received_rewards_msg);
                if (this.plugin.getConfig().getBoolean("config.effects.1_8_titles") && MinigamesAPI.SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
                {
                    Effects.playTitle(p, pli.getMessagesConfig().you_won, 0);
                }
                
                // Participation Rewards
                if (this.participation_economyrewards)
                {
                    MinigamesAPI.getAPI();
                    MinigamesAPI.econ.depositPlayer(p.getName(), this.participation_econ_reward);
                    Util.sendMessage(this.plugin, p, pli.getMessagesConfig().you_got_a_participation_reward.replaceAll("<economyreward>",
                            Integer.toString(this.participation_econ_reward) + " " + MinigamesAPI.econ.currencyNamePlural()));
                }
                if (this.participation_commandrewards)
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replaceAll("<player>", p_));
                }
                
                if (this.plugin.getConfig().getBoolean("config.spawn_fireworks_for_winners"))
                {
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> Util.spawnFirework(p), 20L);
                }
            }
            else
            {
                Util.sendMessage(this.plugin, p, pli.getMessagesConfig().you_lost);
                if (this.plugin.getConfig().getBoolean("config.effects.1_8_titles") && MinigamesAPI.SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
                {
                    Effects.playTitle(p, pli.getMessagesConfig().you_lost, 0);
                }
                MinigamesAPI.getAPI().getPluginInstance(this.plugin).getStatsInstance().lose(p_);
            }
        }
    }
    
}
