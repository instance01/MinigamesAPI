/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.rules;

import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftEvent;
import de.minigameslib.mgapi.api.obj.BasicMatchConfig;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * The implementation of BasicMatch rule set
 * 
 * @see BasicArenaRuleSets#BasicMatch
 * 
 * @author mepeisen
 */
public class BasicMatch implements ArenaRuleSetInterface
{
    
    /**
     * the underlying arena.
     */
    private final ArenaInterface arena;
    
    /**
     * rule set type.
     */
    private final ArenaRuleSetType type;
    
    /**
     * Min players
     */
    private final int minPlayers;
    
    /**
     * Max players
     */
    private final int maxPlayers;
    
    /**
     * Lobby countdown in seconds
     */
    private final int lobbyCountdown;
    
    /**
     * The countdown timer
     */
    private int countdownTimer;
    
    /**
     * The countdown task
     */
    private BukkitTask countdownTask;
    
    /**
     * @param type
     * @param arena
     * @throws McException thrown if config is invalid
     */
    public BasicMatch(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        this.type = type;
        this.arena = arena;
        this.minPlayers = BasicMatchConfig.MinPlayers.getInt();
        this.maxPlayers = BasicMatchConfig.MaxPlayers.getInt();
        this.lobbyCountdown = BasicMatchConfig.LobbyCountdown.getInt();
        if (this.minPlayers <= 0)
        {
            throw new McException(Messages.InvalidConfigMinPlayers, this.minPlayers);
        }
        if (this.maxPlayers > 100)
        {
            throw new McException(Messages.InvalidConfigMaxPlayers, this.maxPlayers);
        }
        if (this.minPlayers > this.maxPlayers)
        {
            throw new McException(Messages.InvalidConfigMinMaxPlayers, this.minPlayers, this.maxPlayers);
        }
        if (this.lobbyCountdown <= 1)
        {
            throw new McException(Messages.InvalidConfigLobbyCountdown, this.lobbyCountdown);
        }
        if (this.lobbyCountdown > 60)
        {
            throw new McException(Messages.InvalidConfigLobbyCountdown, this.lobbyCountdown);
        }
    }

    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }

    @Override
    public ArenaInterface getArena()
    {
        return this.arena;
    }
    
    /**
     * Invoked once a player tries to join.
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoin(ArenaPlayerJoinEvent evt)
    {
        // check max players
        if (this.arena.getPlayerCount() >= this.maxPlayers)
        {
            evt.setCancelled(Messages.MaxPlayersReached, this.maxPlayers);
        }
    }
    
    /**
     * Invoked once a player successfully joined the arena
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoined(ArenaPlayerJoinedEvent evt)
    {
        // check min players
        if (this.arena.getPlayerCount() == this.minPlayers)
        {
            // start lobby countdown
            this.countdownTimer = this.lobbyCountdown - 1;
            McLibInterface.instance().runTaskTimer(MinigamesPlugin.instance(), 20, 20, this::onCountdown);
            
            // notify all players
            this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownStarted, this.lobbyCountdown));
            this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownStarted, this.lobbyCountdown));
        }
    }
    
    /**
     * Invoked once a player left.
     * @param evt
     */
    @McEventHandler
    public void onPlayerLeft(ArenaPlayerLeftEvent evt)
    {
        // check min players
        if (this.arena.getPlayerCount() < this.minPlayers && this.countdownTask != null)
        {
            this.countdownTask.cancel();
            this.countdownTask = null;
            
            // notify all players
            this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownAborted));
            this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownAborted));
        }
    }
    
    /**
     * On lobby countdown
     * @param task
     */
    private void onCountdown(BukkitTask task)
    {
        if (this.countdownTimer <= 0)
        {
            try
            {
                this.arena.start();
            }
            catch (McException e)
            {
                // should never happen because the countdown gets cancelled before all players are left.
                // TODO Logging
            }
            this.countdownTask.cancel();
            this.countdownTask = null;
        }
        
        if (this.countdownTimer <= 10 || this.countdownTimer % 10 == 0)
        {
            this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownTick, this.countdownTimer));
            this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownTick, this.countdownTimer));
        }
        
        this.countdownTimer--;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.rules.BasicMatch")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Max player count reached
         */
        @LocalizedMessage(defaultMessage = "You cannot join because there are already %1$d players in arena.", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Max player count reached"}, args = {@Argument(type = "Numeric", value = "configured max players.")})
        MaxPlayersReached,
        
        /**
         * Countdown started
         */
        @LocalizedMessage(defaultMessage = "Countdown started. Match will start in " + LocalizedMessage.BLUE + "%1$d " + LocalizedMessage.GRAY + "seconds.", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Lobby countdown started"}, args = {@Argument(type = "Numeric", value = "countdown seconds")})
        CountdownStarted,
        
        /**
         * Countdown tick
         */
        @LocalizedMessage(defaultMessage = "Match will start in " + LocalizedMessage.BLUE + "%1$d " + LocalizedMessage.GRAY + "seconds.", severity = MessageSeverityType.Information)
        @MessageComment(value = {"countdown tick"}, args = {@Argument(type = "Numeric", value = "countdown seconds")})
        CountdownTick,
        
        /**
         * Countdown aborted
         */
        @LocalizedMessage(defaultMessage = "Countdown aborted because too many players left the arena.", severity = MessageSeverityType.Error)
        @MessageComment(value = {"countdown aborted"})
        CountdownAborted,
        
        /**
         * Invalid config value (min players)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (min players): " + LocalizedMessage.BLUE + "%1$d", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Invalid config value (min players)"}, args = {@Argument(type = "Numeric", value = "min players config value")})
        InvalidConfigMinPlayers,
        
        /**
         * Invalid config value (min players > max players)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (min players > max players): " + LocalizedMessage.BLUE + "%1$d > %2$d", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Invalid config value (min players > max players)"}, args = {@Argument(type = "Numeric", value = "min players config value"), @Argument(type = "Numeric", value = "max players config value")})
        InvalidConfigMinMaxPlayers,
        
        /**
         * Invalid config value (max players)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (max players): " + LocalizedMessage.BLUE + "%1$d", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Invalid config value (max players)"}, args = {@Argument(type = "Numeric", value = "max players config value")})
        InvalidConfigMaxPlayers,
        
        /**
         * Invalid config value (lobby countdown)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (lobby countdown): " + LocalizedMessage.BLUE + "%1$d", severity = MessageSeverityType.Information)
        @MessageComment(value = {"Invalid config value (lobby countdown)"}, args = {@Argument(type = "Numeric", value = "lobby countdown config value")})
        InvalidConfigLobbyCountdown
        
    }
    
}
