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

/**
 * Rewards helper for minigames.
 * 
 * @author instancelabs
 */
public class Rewards
{
    
    /** minigame java plugin. */
    private JavaPlugin  plugin                    = null;
    
    /** {@code true} for activated economy rewards. */
    private boolean     economyrewards;
    /** {@code true} for activated item rewards. */
    private boolean     itemrewards;
    /** {@code true} for activated command rewards. */
    private boolean     commandrewards;
    /** {@code true} for activated kill economy rewards. */
    private boolean     kill_economyrewards;
    /** {@code true} for activated kill command rewards. */
    private boolean     kill_commandrewards;
    /** {@code true} for activated participation economy rewards. */
    private boolean     participation_economyrewards;
    /** {@code true} for activated participation command rewards. */
    private boolean     participation_commandrewards;
    
    /** money to give for winning. */
    private int         econ_reward               = 0;
    /** money to give per kill. */
    private int         kill_econ_reward          = 0;
    /** money to give for participation. */
    private int         participation_econ_reward = 0;
    /** command to execute for winning. */
    private String      command                   = "";  //$NON-NLS-1$
    /** command to execute per kill. */
    private String      kill_command              = "";  //$NON-NLS-1$
    /** command to execute for participation. */
    private String      participation_command     = "";  //$NON-NLS-1$
    /** items to reward for winning. */
    private ItemStack[] items                     = null;
    
    /**
     * Constructor to create a rewards helper.
     * 
     * @param plugin
     *            minigame java plugin.
     */
    public Rewards(final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.reloadVariables();
        
        if (!MinigamesAPI.getAPI().economyAvailable())
        {
            this.economyrewards = false;
            this.kill_economyrewards = false;
            this.participation_economyrewards = false;
        }
    }
    
    /**
     * Reload variables from config.
     */
    public void reloadVariables()
    {
        this.economyrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY);
        this.itemrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_ITEM_REWARD);
        this.commandrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_REWARD);
        this.kill_economyrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_FOR_KILLS);
        this.kill_commandrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_REWARD_FOR_KILLS);
        this.participation_economyrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_FOR_PARTICIPATION);
        this.participation_commandrewards = this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_REWARD_FOR_PARTICIPATION);
        
        this.econ_reward = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_REWARD);
        this.command = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_REWARDS_COMMAND);
        this.items = Util.parseItems(this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_REWARDS_ITEM_REWARD_IDS)).toArray(new ItemStack[0]);
        this.kill_econ_reward = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_REWARD_FOR_KILLS);
        this.kill_command = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_FOR_KILLS);
        this.participation_econ_reward = this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_REWARD_FOR_PARTICIPATION);
        this.participation_command = this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_FOR_PARTICIPATION);
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
    
    /**
     * Give reward for given player.
     * 
     * @param p_
     *            player name
     * @deprecated will be removed in 1.5.0
     */
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
            
            if (this.kill_economyrewards && MinigamesAPI.getAPI().economyAvailable())
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
    
    /**
     * Give a player a kill reward
     * 
     * @param p_
     *            Playername
     * @param reward
     *            Amount of statistics points the player gets; will be ignored
     * @deprecated will be removed in 1.5.0; replaced by {@link #giveKillReward(String)}
     */
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
     * @param isCommand
     *            Whether command rewards are enabled
     * @param money_reward
     *            Amount of money to reward if economy rewards are enabled
     * @param cmd
     *            Command to execute if command rewards are enabled
     */
    public void giveAchievementReward(final String p_, final boolean econ, final boolean isCommand, final int money_reward, final String cmd)
    {
        if (Validator.isPlayerOnline(p_))
        {
            final Player p = Bukkit.getPlayer(p_);
            
            if (econ && MinigamesAPI.getAPI().economyAvailable())
            {
                MinigamesAPI.getAPI();
                MinigamesAPI.econ.depositPlayer(p.getName(), money_reward);
            }
            if (isCommand)
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<player>", p_));
            }
        }
    }
    
    /**
     * Give win rewards to given player
     * 
     * @param p_
     *            player name
     * @param a
     *            Arena
     */
    private void giveWinReward(final String p_, final Arena a)
    {
        this.giveWinReward(p_, a, 1);
    }
    
    /**
     * Give all win rewards to players who won the game
     * 
     * @param p_
     *            player name
     * @param a
     *            Arena
     * @param global_multiplier
     */
    private void giveWinReward(final String p_, final Arena a, final int global_multiplier)
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
                if (this.economyrewards && MinigamesAPI.getAPI().economyAvailable())
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
                    if (this.economyrewards && MinigamesAPI.getAPI().economyAvailable())
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
                    if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BROADCAST_WIN))
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
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EFFECTS_1_8_TITLES) && MinigamesAPI.SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
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
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.participation_command.replaceAll("<player>", p_));
                }
                
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SPAWN_FIREWORKS_FOR_WINNERS))
                {
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> Util.spawnFirework(p), 20L);
                }
            }
            else
            {
                Util.sendMessage(this.plugin, p, pli.getMessagesConfig().you_lost);
                if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_EFFECTS_1_8_TITLES) && MinigamesAPI.SERVER_VERSION.isAfter(MinecraftVersionsType.V1_7))
                {
                    Effects.playTitle(p, pli.getMessagesConfig().you_lost, 0);
                }
                MinigamesAPI.getAPI().getPluginInstance(this.plugin).getStatsInstance().lose(p_);
            }
        }
    }
    
}
