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
        // user commands
        this.injectSubCommand("party", new PartyCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("start", new StartCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("help", new HelpCommandHandler(this)); //$NON-NLS-1$
        this.injectSubCommand("info", new InfoCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("list", new ListCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("debug", new DebugCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("join", new JoinCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("leave", new LeaveCommandHandler()); //$NON-NLS-1$
        
        // admin commands
        this.injectSubCommand("test", new TestCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("admin", new AdminCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("create", new CreateCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("add", new AddCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("edit", new EditCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("remove", new RemoveCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("setopt", new SetOptCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("getopt", new GetOptCommandHandler()); //$NON-NLS-1$
        this.injectSubCommand("save", new SaveCommandHandler()); //$NON-NLS-1$
    }
    
    @Override
    protected void sendUsage(CommandInterface command)
    {
        command.send(CommonMessages.Mg2CommandUsage);
    }
    
}
