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

import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.enums.McUniqueEnumInterface;

/**
 * Base interface for rule sets
 * 
 * @author mepeisen
 */
public interface RuleSetType extends McUniqueEnumInterface
{
    
    /**
     * Returns the configuration class used for rule set configuration.
     * @return confuiguration class or {@code null} if this types has no configuration.
     */
    @SuppressWarnings("unchecked")
    default <T extends Enum<?> & ConfigurationValueInterface> Class<T> getConfigClass()
    {
        RuleSetConfigurable result;
        try
        {
            result = this.getClass().getDeclaredField(this.name()).getAnnotation(RuleSetConfigurable.class);
            if (result != null)
            {
                return (Class<T>) result.config();
            }
        }
        catch (@SuppressWarnings("unused") NoSuchFieldException | SecurityException e)
        {
            // silently ignore
        }
        return null;
    }
    
}
