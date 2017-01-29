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

package de.minigameslib.mgapi.impl.obj;

import java.util.HashSet;
import java.util.Set;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.rules.RuleSetType;

/**
 * The abstract data fragment to hold persistent object data. 
 * 
 * @author mepeisen
 * @param <T> 
 */
public abstract class AbstractObjectData<T extends RuleSetType> extends AnnotatedDataFragment
{
    
    /**
     * fixed rule set types.
     */
    @PersistentField
    protected Set<T> fixedRules = new HashSet<>();
    
    /**
     * optional rule set types.
     */
    @PersistentField
    protected Set<T> optionalRules = new HashSet<>();
    
    /**
     * The object name
     */
    @PersistentField
    protected String name;

    /**
     * @return the fixedRules
     */
    public Set<T> getFixedRules()
    {
        return this.fixedRules;
    }

    /**
     * @return the optionalRules
     */
    public Set<T> getOptionalRules()
    {
        return this.optionalRules;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
}
