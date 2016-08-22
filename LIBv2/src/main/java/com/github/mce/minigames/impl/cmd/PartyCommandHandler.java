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
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.cmd.AbstractCompositeCommandHandler;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.cmd.SubCommandHandlerInterface;
import com.github.mce.minigames.api.config.CommonConfig;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

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
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws MinigameException
    {
        if (!CommonConfig.PartyCommandEnabled.getBoolean())
        {
            throw new MinigameException(CommonErrors.PartyCommandDisabled);
        }
        
        return super.pre(command);
    }

    @Override
    protected void sendUsage(CommandInterface command)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getShortDescription()
     */
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getDescription()
     */
    @Override
    public LocalizedMessageInterface[] getDescription(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getUsage()
     */
    @Override
    public LocalizedMessageInterface getUsage(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
