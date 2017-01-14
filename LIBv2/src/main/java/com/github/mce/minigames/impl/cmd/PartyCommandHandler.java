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

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.config.CommonConfig;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * A handler for the /party command.
 * 
 * @author mepeisen
 */
public class PartyCommandHandler extends AbstractCompositeCommandHandler implements SubCommandHandlerInterface
{
    
    /**
     * Constructor
     */
    public PartyCommandHandler()
    {
        this.subCommands.put("invite", new PartyInviteCommandHandler()); //$NON-NLS-1$
        this.subCommands.put("accept", new PartyAcceptCommandHandler()); //$NON-NLS-1$
        this.subCommands.put("kick", new PartyKickCommandHandler()); //$NON-NLS-1$
        this.subCommands.put("list", new PartyListCommandHandler()); //$NON-NLS-1$
        this.subCommands.put("disband", new PartyDisbandCommandHandler()); //$NON-NLS-1$
        this.subCommands.put("leave", new PartyLeaveCommandHandler()); //$NON-NLS-1$
        this.subCommands.put("help", new HelpCommandHandler((AbstractCompositeCommandHandler) this)); //$NON-NLS-1$
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws McException
    {
        if (!CommonConfig.PartyCommandEnabled.getBoolean())
        {
            throw new McException(CommonErrors.PartyCommandDisabled);
        }
        
        return super.pre(command);
    }

    @Override
    protected void sendUsage(CommandInterface command)
    {
        command.send(CommonMessages.PartyCommandUsage, command.getCommandPath());
    }

    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return CommonMessages.PartyCommandShortDescription;
    }

    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return CommonMessages.PartyCommandDescription;
    }
    
}
