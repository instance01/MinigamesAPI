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

package de.minigameslib.mgapi.impl.cmd;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminSignCommand extends AbstractCompositeCommandHandler implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandAdminSign);
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdminSign, command.getCommandPath());
        return true;
    }

    /**
     * Constructor to register sub commands.
     */
    public AdminSignCommand()
    {
        this.subCommands.put("help", new HelpCommandHandler((AbstractCompositeCommandHandler) this)); //$NON-NLS-1$
        this.subCommands.put("list", new AdminSignListCommand()); //$NON-NLS-1$
        this.subCommands.put("create", new AdminSignCreateCommand()); //$NON-NLS-1$
//        this.subCommands.put("delete", new AdminSignDeleteCommand()); //$NON-NLS-1$
//        this.subCommands.put("move", new AdminSignMoveCommand()); //$NON-NLS-1$
//        this.subCommands.put("tp", new AdminSignTpCommand()); //$NON-NLS-1$
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return Messages.ShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return Messages.Description;
    }
    
    @Override
    protected void sendUsage(CommandInterface command)
    {
        command.send(Messages.Usage);
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_admin_sign")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin sign
         */
        @LocalizedMessage(defaultMessage = "Manipulate arena signs")
        @MessageComment({"Short description of /mg2 admin sign"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin sign
         */
        @LocalizedMessage(defaultMessage = "Display simple admin sign")
        @MessageComment({"Long description of /mg2 admin sign"})
        Description,
        
        /**
         * Usage of /mg2 admin sign
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin sign <sub-command>")
        @MessageComment({"Usage of /mg2 admin sign"})
        Usage
        
    }
    
}
