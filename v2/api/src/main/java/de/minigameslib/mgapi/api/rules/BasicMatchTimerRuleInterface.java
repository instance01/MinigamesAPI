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

package de.minigameslib.mgapi.api.rules;

import de.minigameslib.mclib.api.McException;

/**
 * @author mepeisen
 *
 */
public interface BasicMatchTimerRuleInterface extends ArenaRuleSetInterface
{
    
    /**
     * Returns the max seconds before the game will be ended
     * @return the max seconds; 0 or negative value means: time is inactive
     */
    int getMaxSeconds();

    /**
     * Sets the max seconds value the game will end
     * @param seconds
     * @throws McException thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setMaxSeconds(int seconds) throws McException;
    
    // following methods are callable DURING running matches to influence the timer, for example from other threads
    
    /**
     * Pauses the current match; causing the timer to halt
     */
    void pause();
    
    /**
     * Resumes the current match; causing the timer to restart after {@link #pause()} was called
     */
    void resume();
    
    /**
     * Resets the timer; starting the timer from zero
     */
    void resetAndResume();
    
    /**
     * Resets the timer; pausing the timer at zero
     */
    void resetAndPause();
    
    /**
     * Returns the current match time in millis
     * @return match time; respects any call to the methods on this interface
     */
    long getDurationMillis();
    
    /**
     * Returns the maximum time in millis
     * @return maximum time; respects any call to the methods on this interface
     */
    long getMaxMillis();
    
    /**
     * Adds milliseconds to maximum time
     * @param millis
     */
    void addMaxMillis(long millis);
    
    /**
     * Substracts milliseconds from maximum time; if duration gets below max millis the game ends
     * @param millis
     */
    void substractMaxMillis(long millis);
    
    /**
     * Sets the maximum time milliseconds
     * @param millis new millis; zero or below zero to stop timer; if set to positive value and previous value was set to zero or below zero the timer starts
     */
    void setMaxMillis(long millis);
    
    /**
     * Adds milliseconds to played time
     * @param millis
     */
    void addDurationMillis(long millis);
    
    /**
     * Substracts milliseconds from played time; if duration gets below max millis the game ends
     * @param millis
     */
    void substractDurationMillis(long millis);
    
    /**
     * Sets the played time milliseconds
     * @param millis new millis
     */
    void setDurationMillis(long millis);

}
