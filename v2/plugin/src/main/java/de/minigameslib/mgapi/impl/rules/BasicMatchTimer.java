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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicMatchTimerConfig;
import de.minigameslib.mgapi.api.rules.BasicMatchTimerRuleInterface;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * The implementation of BasicMatchTime rule set
 * 
 * @see BasicArenaRuleSets#BasicMatchTimer
 * 
 * @author mepeisen
 */
public class BasicMatchTimer extends AbstractArenaRule implements BasicMatchTimerRuleInterface
{
    
    /**
     * maximum seconds
     */
    private int seconds;
    
    /**
     * the current match duration in millis
     */
    private long matchDuration;
    
    /**
     * the current match maxmimum time
     */
    private long matchTime;
    
    /**
     * Flag for paused or running timers; {@code true} if timer is paused
     */
    private boolean paused;
    
    /**
     * Last start of timer
     */
    private LocalDateTime lastStart = LocalDateTime.now();
    
    /**
     * The timer task
     */
    private BukkitTask timerTask;
    
    /**
     * @param type
     * @param arena
     * @throws McException thrown if config is invalid
     */
    public BasicMatchTimer(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            this.seconds = BasicMatchTimerConfig.MaxSeconds.getInt();
            this.matchTime = this.seconds * 1000L;
        });
    }

    @Override
    public int getMaxSeconds()
    {
        return this.seconds;
    }

    @Override
    public void setMaxSeconds(int seconds) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicMatchTimerConfig.MaxSeconds.setInt(this.seconds);
        });
        this.arena.reconfigure(this.type);
    }

    @Override
    public void pause()
    {
        if (!this.paused)
        {
            this.paused = true;
            this.matchDuration += this.lastStart.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        }
    }

    @Override
    public void resume()
    {
        if (this.paused)
        {
            this.paused = false;
            this.lastStart = LocalDateTime.now();
        }
    }

    @Override
    public void resetAndResume()
    {
        this.paused = false;
        this.lastStart = LocalDateTime.now();
        this.matchDuration = 0;
    }

    @Override
    public void resetAndPause()
    {
        this.paused = true;
        this.matchDuration = 0;
    }

    @Override
    public long getDurationMillis()
    {
        if (this.paused)
        {
            return this.matchDuration;
        }
        return this.matchDuration + this.lastStart.until(LocalDateTime.now(), ChronoUnit.MILLIS);
    }

    @Override
    public long getMaxMillis()
    {
        return this.matchTime;
    }

    @Override
    public void addMaxMillis(long millis)
    {
        this.matchTime += millis;
        if (this.matchTime > 0 && this.timerTask == null)
        {
            this.startTimer();
        }
    }

    @Override
    public void substractMaxMillis(long millis)
    {
        this.matchTime -= millis;
        if (this.matchTime <= 0)
        {
            this.stopTimer();
        }
    }

    @Override
    public void setMaxMillis(long millis)
    {
        this.matchTime = millis;
        if (this.matchTime <= 0)
        {
            this.stopTimer();
        }
    }

    @Override
    public void addDurationMillis(long millis)
    {
        this.matchDuration += millis;
    }

    @Override
    public void substractDurationMillis(long millis)
    {
        this.matchDuration -= millis;
    }

    @Override
    public void setDurationMillis(long millis)
    {
        this.matchDuration = millis;
    }
    
    /**
     * Arena state change
     * @param evt
     */
    @McEventHandler
    public void onArenaState(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() == ArenaState.Match)
        {
            this.matchTime = this.seconds * 1000L;
            this.matchDuration = 0;
            this.lastStart = LocalDateTime.now();
            this.paused = false;
            if (this.matchTime > 0)
            {
                this.startTimer();
            }
        }
        else
        {
            this.stopTimer();
        }
    }

    /**
     * Starts the bukkit timer task
     */
    private void startTimer()
    {
        if (this.timerTask == null)
        {
            this.timerTask = McLibInterface.instance().runTaskTimer(MinigamesPlugin.instance().getPlugin(), 20, 20, this::onTimer);
        }
    }

    /**
     * Stops the bukkit timer task
     */
    private void stopTimer()
    {
        if (this.timerTask != null)
        {
            this.timerTask.cancel();
            this.timerTask = null;
        }
    }
    
    /**
     * On timer tick
     * 
     * @param task
     */
    private void onTimer(BukkitTask task)
    {
        if (!this.paused)
        {
            long delta = this.getMaxMillis() - this.getDurationMillis();
            if (delta <= 0)
            {
                this.timerTask.cancel();
                this.timerTask = null;
                try
                {
                    this.arena.abort();
                }
                catch (McException e)
                {
                    // TODO logging
                }
            }
            else
            {
                // TODO send messages to players about the pending game time
                // TODO interval: before ending: 1 minute, 30 seconds, 20 seconds, 10 seconds, 5 seconds, 4 seconds, 3 seconds, 2 seconds, 1 second... Respect game time changes...
            }
        }
    }
    
}
