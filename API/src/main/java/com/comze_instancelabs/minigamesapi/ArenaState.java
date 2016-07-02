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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The states for an arena.
 * 
 * @author instancelabs
 */
public enum ArenaState
{
    
    /** the default state; players can join. */
    JOIN("&a"), //$NON-NLS-1$
    
    /** the game is starting. */
    STARTING("&a"), //$NON-NLS-1$
    
    /** the game is running. */
    INGAME("&4"), //$NON-NLS-1$
    
    /** the arena is restarting after game ended; for example the broken blocks are replaced. */
    RESTARTING("&e"); //$NON-NLS-1$
    
    /** the states color code. */
    private String colorCode;
    
    /**
     * Constructor.
     * 
     * @param colorCode
     *            the states color code
     */
    private ArenaState(String colorCode)
    {
        this.colorCode = colorCode;
    }
    
    /**
     * Returns the color code for this state.
     * 
     * @return color code used to display this state.
     */
    public String getColorCode()
    {
        return this.colorCode;
    }
    
    /**
     * Returns a list with all state names.
     * 
     * @return list with all state names.
     */
    public static Iterable<String> getAllStateNames()
    {
        final List<String> result = new ArrayList<>();
        for (final ArenaState state : ArenaState.values())
        {
            result.add(state.name());
        }
        return result;
    }
    
    /**
     * Returns a map from state name to color code.
     * 
     * @return map from state name to color code.
     */
    public static Map<String, String> getAllStateNameColors()
    {
        final Map<String, String> result = new HashMap<>();
        for (final ArenaState state : ArenaState.values())
        {
            result.put(state.name(), state.getColorCode());
        }
        return result;
    }
}
