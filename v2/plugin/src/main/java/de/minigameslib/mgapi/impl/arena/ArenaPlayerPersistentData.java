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

package de.minigameslib.mgapi.impl.arena;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Persistent player data.
 * 
 * @author mepeisen
 */
public class ArenaPlayerPersistentData extends AnnotatedDataFragment
{
    
    /**
     * The arena this player has joined
     */
    @PersistentField
    protected String arenaName;
    
    /**
     * Spectator flag.
     */
    @PersistentField
    protected boolean isSpectator;

    /**
     * @return the arenaName
     */
    public String getArenaName()
    {
        return this.arenaName;
    }

    /**
     * @param arenaName the arenaName to set
     */
    public void setArenaName(String arenaName)
    {
        this.arenaName = arenaName;
    }

    /**
     * @return the isSpectator
     */
    public boolean isSpectator()
    {
        return this.isSpectator;
    }

    /**
     * @param isSpectator the isSpectator to set
     */
    public void setSpectator(boolean isSpectator)
    {
        this.isSpectator = isSpectator;
    }
    
}
