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

package com.github.mce.minigames.impl.arena;

import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;

/**
 * Internal representation of an arena type.
 * 
 * @author mepeisen
 */
public class ArenaTypeDeclarationImpl implements ArenaTypeDeclarationInterface
{
    
    /**
     * Underlying arena type (enum)
     */
    private ArenaTypeInterface enumType;
    
    /**
     * {@code true} for default arena type.
     */
    private boolean            isDefault;
    
    /**
     * Constructor to create arena type.
     * 
     * @param enumType
     *            Underlying arena type (enum)
     * @param isDefault
     *            {@code true} for default arena type.
     */
    public ArenaTypeDeclarationImpl(ArenaTypeInterface enumType, boolean isDefault)
    {
        this.enumType = enumType;
        this.isDefault = isDefault;
    }

    @Override
    public ArenaTypeInterface getType()
    {
        return this.enumType;
    }

    @Override
    public boolean isDefault()
    {
        return this.isDefault;
    }
    
}
