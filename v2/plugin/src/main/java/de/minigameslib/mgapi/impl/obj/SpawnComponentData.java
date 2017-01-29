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

import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * @author mepeisen
 *
 */
public class SpawnComponentData extends AbstractObjectData<ComponentRuleSetType>
{
    
    /**
     * The team this spawn is associated to
     */
    @PersistentField
    protected TeamIdType team;
    
    /**
     * the spawn ordering to sort multiple spawns
     */
    @PersistentField
    protected int spawnOrdering;

    /**
     * @return the team
     */
    public TeamIdType getTeam()
    {
        return this.team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(TeamIdType team)
    {
        this.team = team;
    }

    /**
     * @return the spawnOrdering
     */
    public int getSpawnOrdering()
    {
        return this.spawnOrdering;
    }

    /**
     * @param spawnOrdering the spawnOrdering to set
     */
    public void setSpawnOrdering(int spawnOrdering)
    {
        this.spawnOrdering = spawnOrdering;
    }
    
}
