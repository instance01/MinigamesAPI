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

package com.github.mce.minigames.api.arena;

import de.minigameslib.mclib.api.McException;

/**
 * Interface for arena type declarations.
 * 
 * @author mepeisen
 */
public interface ArenaTypeDeclarationInterface
{
    
    /**
     * Returns the type of this declaration.
     * 
     * @return arena type
     */
    ArenaTypeInterface getType();
    
    /**
     * Checks if this is the default of declaring minigame.
     * 
     * @return {@code true} if this is the default type.
     */
    boolean isDefault();
    
    /**
     * Creates a new arena
     * 
     * @param arenaName
     *            the internal arena name to be created
     * @return the newly created arena
     * @throws McException
     *             thrown if the name is already in use or if the arena type is invalid
     */
    ArenaInterface createArena(String arenaName) throws McException;
    
    /**
     * Returns the arena type name
     * 
     * @return arena type name
     */
    String getName();
    
}
