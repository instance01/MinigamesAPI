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

package de.minigameslib.mgapi.api.team;

import org.bukkit.Color;

/**
 * Common team enumeration
 * 
 * @author mepeisen
 */
public enum CommonTeams implements TeamIdType
{
    
    /**
     * this is an unknown team. This is used for games the players do not belong to any team.
     */
    Unknown,
    
    /**
     * Special team for spectators.
     */
    Spectators,
    
    /**
     * A team with players losing the current match.
     */
    Losers,
    
    /**
     * A team with playrs winning the current match.
     */
    Winners,
    
    // common colored teams
    
    /**
     * White
     * @see Color#WHITE
     */
    White,
    
    /**
     * White
     * @see Color#SILVER
     */
    Silver,
    
    /**
     * White
     * @see Color#GRAY
     */
    Gray,
    
    /**
     * White
     * @see Color#BLACK
     */
    Black,
    
    /**
     * White
     * @see Color#RED
     */
    Red,
    
    /**
     * White
     * @see Color#MAROON
     */
    Maroon,
    
    /**
     * White
     * @see Color#YELLOW
     */
    Yellow,
    
    /**
     * White
     * @see Color#OLIVE
     */
    Olive,
    
    /**
     * White
     * @see Color#LIME
     */
    Lime,
    
    /**
     * White
     * @see Color#GREEN
     */
    Green,
    
    /**
     * White
     * @see Color#AQUA
     */
    Aqua,
    
    /**
     * White
     * @see Color#TEAL
     */
    Teal,
    
    /**
     * White
     * @see Color#BLUE
     */
    Blue,
    
    /**
     * White
     * @see Color#NAVY
     */
    Navy,
    
    /**
     * White
     * @see Color#FUCHSIA
     */
    Fuchsia,
    
    /**
     * White
     * @see Color#PURPLE
     */
    Purple,
    
    /**
     * White
     * @see Color#ORANGE
     */
    Orange,
    
}
