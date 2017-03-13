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
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaLoseEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicWinningRuleSets;

/**
 * The implementation of LastManStanding rule set
 * 
 * @see BasicWinningRuleSets#LastManStanding
 * 
 * @author mepeisen
 */
public class LastManStanding extends AbstractArenaRule
{
    
    /**
     * @param type
     * @param arena
     * @throws McException thrown if config is invalid
     */
    public LastManStanding(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
    }
    
    /**
     * On player lose.
     * @param evt 
     * @throws McException 
     */
    @McEventHandler
    public void onLose(ArenaLoseEvent evt) throws McException
    {
        final ArenaMatchInterface match = evt.getArena().getCurrentMatch();
        if (match.getPlayerCount() == 1)
        {
            match.setWinner(match.getPlayers().iterator().next());
        }
    }
    
}
