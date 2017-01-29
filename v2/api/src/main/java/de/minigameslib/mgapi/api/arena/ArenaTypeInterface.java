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

package de.minigameslib.mgapi.api.arena;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.McUniqueEnumInterface;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;

/**
 * Interface implemented by enumerations for building arena types.
 * 
 * @author mepeisen
 */
public interface ArenaTypeInterface extends McUniqueEnumInterface
{
    
    /**
     * Returns the arena type provider for this type.
     * @return arena type provider class
     */
    default Class<? extends ArenaTypeProvider> getProvider()
    {
        try
        {
            final ArenaType type = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(ArenaType.class);
            return type.value();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Creates arena type provider.
     * @return arena type provider.
     * @throws McException
     */
    default ArenaTypeProvider safeCreateProvider() throws McException
    {
        try
        {
            return this.getProvider().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | IllegalStateException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
    }
    
    /**
     * Returns the minigame owning this type.
     * @return minigame.
     */
    default MinigameInterface getMinigame()
    {
        return MinigamesLibInterface.instance().getMinigame(getPlugin());
    }
    
}
