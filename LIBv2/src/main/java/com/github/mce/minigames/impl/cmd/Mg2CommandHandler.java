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

package com.github.mce.minigames.impl.cmd;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.cmd.AbstractCompositeCommandHandler;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.cmd.HelpCommandHandler;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * A handler for the /mg2 command.
 * 
 * @author mepeisen
 */
public class Mg2CommandHandler extends AbstractCompositeCommandHandler
{
    
    /**
     * Constructor to create the mg2 command handler.
     */
    public Mg2CommandHandler()
    {
        this.injectSubCommand("party", new PartyCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("help", new HelpCommandHandler(this)); //$NON-NLS-1$
        // TODO additional commands
    }
    
    @Override
    protected void sendUsage(CommandInterface command, ArenaPlayerInterface player)
    {
        player.sendMessage(CommonMessages.Mg2CommandUsage);
    }
    
}
