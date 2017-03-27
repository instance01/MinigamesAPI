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

package de.minigameslib.mgapi.impl.rules;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.AbstractZoneRule;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
public class LoseOnLeave extends AbstractZoneRule
{
    
    /**
     * @param type
     * @param zone
     * @throws McException thrown if config is invalid
     */
    public LoseOnLeave(ZoneRuleSetType type, ArenaZoneHandler zone) throws McException
    {
        super(type, zone);
    }
    
    /**
     * Event on player zone leave
     * @param evt
     * @throws McException 
     */
    @McEventHandler
    public void onLeave(PlayerLeftZoneEvent evt) throws McException
    {
        if (this.arena.isMatch())
        {
            if (this.arena.isPlaying(evt.getPlayer()))
            {
                MinigamesLibInterface.instance().getPlayer(evt.getPlayer()).lose();
            }
        }
    }
    
}
