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

import java.io.Serializable;

import de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mgapi.api.MinigamesLibInterface;

/**
 * Handle the extensions command info
 * @author mepeisen
 */
public class InfoExtensionsCommand extends AbstractPagableCommandHandler implements SubCommandHandlerInterface
{

    @Override
    protected Serializable getHeader(CommandInterface cmd)
    {
        return Messages.Header;
    }

    @Override
    protected int getLineCount(CommandInterface cmd)
    {
        return MinigamesLibInterface.instance().getExtensionCount();
    }

    @Override
    protected Serializable[] getLines(CommandInterface cmd, int start, int limit)
    {
        return MinigamesLibInterface.instance().getExtensions(start, limit).stream()
                .map(ext -> ext.getDisplayName()).toArray(Serializable[]::new);
    }

    @Override
    public LocalizedMessageInterface getDescription(CommandInterface cmd)
    {
        return Messages.Description;
    }

    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface cmd)
    {
        return Messages.ShortDescription;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_info_extensions")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 info extensions
         */
        @LocalizedMessage(defaultMessage = "Prints a list of registered extensions.")
        @MessageComment({"Short description of /mg2 info extensions"})
        ShortDescription,
        
        /**
         * Long description of /mg2 info extensions
         */
        @LocalizedMessage(defaultMessage = "Prints a list of registered extensions.")
        @MessageComment({"Long description of /mg2 info extensions"})
        Description,
        
        /**
         * Paging header text
         */
        @LocalizedMessage(defaultMessage = "Extensions")
        @MessageComment({"Paging header text"})
        Header,
    }
    
}
