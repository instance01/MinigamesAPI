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

package de.minigameslib.mgapi.api;

import java.util.Collection;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

/**
 * @author mepeisen
 *
 */
public interface MinigameInterface
{
    
    /**
     * returns the minigames internal/ technical name.
     * @return minigame name.
     */
    String getName();
    
    /**
     * Returns a display name for the minigame.
     * @return minigame display name.
     */
    LocalizedMessageInterface getDisplayName();
    
    /**
     * Returns a short single-line description of the minigame
     * @return short single-line description
     */
    LocalizedMessageInterface getShortDescription();
    
    /**
     * Returns a multi-line description of the minigame
     * @return multi-line description
     */
    LocalizedMessageInterface getDescription();
    
    /**
     * Returns a how-to-use manual, mainly for administrators
     * @return how-to-use manual, mainly for adminstrators
     */
    LocalizedMessageInterface getManual();
    
    /**
     * Returns a how-to-play manual, default for arenas without customized manual
     * @return how-to-play manual, default for arenas without customized manual
     */
    LocalizedMessageInterface getHowToPlay();

    /**
     * Returns the bukkit plugin owning the minigame.
     * @return bukkit plugin.
     */
    Plugin getPlugin();

    /**
     * Returns the arena type per name.
     * @param typeName
     * @return type instance or {@code null} if the type was not found
     */
    ArenaTypeInterface getType(String typeName);
    
    /**
     * Returns the type count.
     * @return type count
     */
    int getTypeCount();

    /**
     * Returns the arena types
     * @param start starting index
     * @param limit maximum countt return
     * @return collection of arena types.
     */
    Collection<ArenaTypeInterface> getTypes(int start, int limit);

    /**
     * Returns the arena types
     * @param prefix type name prefix
     * @param start starting index
     * @param limit maximum countt return
     * @return collection of arena types.
     */
    Collection<ArenaTypeInterface> getTypes(String prefix, int start, int limit);
    
}
