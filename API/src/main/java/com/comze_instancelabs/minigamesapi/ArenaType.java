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

/**
 * Arena types.
 */
public enum ArenaType
{
    
    /**
     * Standard arena with lobby + spawn and lobby countdown; can have multiple spawns too
     */
    DEFAULT,
    
    /**
     * Players just join the game whenever they like, no lobby countdowns or arena/sign states; doesn't allow multiple spawns
     */
    JUMPNRUN,
    
    /**
     * Default arena + automatic regeneration
     */
    REGENERATION
    
}
