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

import java.util.Collection;
import java.util.Collections;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;

/**
 * Basic class for classic arenas behaving similar to minigameslib version 1.
 * 
 * @author mepeisen
 */
public abstract class ClassicTeamArena implements ArenaTypeProvider
{

    @Override
    public Collection<ArenaRuleSetType> getFixedArenaRules()
    {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }

    @Override
    public Collection<ArenaRuleSetType> getOptionalArenaRules()
    {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }

    @Override
    public void configure(ArenaRuleSetInterface ruleSet) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
}
