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
 * Adminitartion command
 * 
 * @author mepeisen
 */
public class AdminCommand extends AbstractCompositeCommandHandler implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.checkOpPermission(MglibPerms.CommandAdmin);
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdmin, command.getCommandPath());
        return true;
    }

    /**
     * Constructor to register sub commands.
     */
    public AdminCommand()
    {
        this.subCommands.put("help", new HelpCommandHandler((AbstractCompositeCommandHandler) this)); //$NON-NLS-1$
        this.subCommands.put("create", new AdminCreateCommand()); //$NON-NLS-1$
        this.subCommands.put("delete", new AdminDeleteCommand()); //$NON-NLS-1$
        this.subCommands.put("enable", new AdminEnableCommand()); //$NON-NLS-1$
        this.subCommands.put("disable", new AdminDisableCommand()); //$NON-NLS-1$
        this.subCommands.put("check", new AdminCheckCommand()); //$NON-NLS-1$
        this.subCommands.put("maintain", new AdminMaintainCommand()); //$NON-NLS-1$
        this.subCommands.put("start", new AdminStartCommand()); //$NON-NLS-1$
        this.subCommands.put("stop", new AdminStopCommand()); //$NON-NLS-1$
        this.subCommands.put("test", new AdminTestCommand()); //$NON-NLS-1$
        this.subCommands.put("invite", new AdminInviteCommand()); //$NON-NLS-1$
        this.subCommands.put("sign", new AdminSignCommand()); //$NON-NLS-1$
        this.subCommands.put("zone", new AdminZoneCommand()); //$NON-NLS-1$
        this.subCommands.put("comp", new AdminComponentCommand()); //$NON-NLS-1$
        this.subCommands.put("gui", new AdminGuiCommand()); //$NON-NLS-1$
        this.subCommands.put("sgui", new AdminSGuiCommand()); //$NON-NLS-1$
        // TODO missing command: change display name/ short desc/ desc/ manual
        // TODO missing command: export/ import
        // TODO missing command: marketplace
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
    @LocalizedMessages(value = "cmd.mg2_admin")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin
         */
        @LocalizedMessage(defaultMessage = "Administration commands.")
        @MessageComment({"Short description of /mg2 admin"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin
         */
        @LocalizedMessage(defaultMessage = "Administration commands.")
        @MessageComment({"Long description of /mg2 admin"})
        Description,
        
        /**
         * Usage of /mg2 admin
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin <sub-command>")
        @MessageComment({"Usage of /mg2 admin"})
        Usage,
    }
    
}
