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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.perms.CommonPermissions;
import com.github.mce.minigames.api.services.ExtensionInterface;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractPagableCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;

/**
 * Command to display useful information.
 * 
 * @author mepeisen
 */
public class InfoExtensionsCommandHandler extends AbstractPagableCommandHandler
{
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permThrowException(CommonPermissions.InfoExtensions, command.getCommandPath() + " extensions"); //$NON-NLS-1$
        super.handle(command);
    }

    @Override
    protected int getLineCount(CommandInterface command)
    {
        return MglibInterface.INSTANCE.get().getExtensionsCount();
    }

    @Override
    protected Serializable getHeader(CommandInterface command)
    {
        return CommonMessages.InfoExtensionsHeader.toArg(command.getCommandPath());
    }

    @Override
    protected Serializable[] getLines(CommandInterface command, int start, int count)
    {
        final Iterator<ExtensionInterface> extensions = MglibInterface.INSTANCE.get().getExtensions().iterator();
        int i = 0;
        while (i < start && extensions.hasNext())
        {
            extensions.next();
            i++;
        }
        final List<Serializable> result = new ArrayList<>();
        for (i = 0; i < count; i++)
        {
            if (extensions.hasNext())
            {
                final ExtensionInterface extension = extensions.next();
                result.add(CommonMessages.InfoExtensionLine.toArg(extension.getName(), extension.getShortDescription()));
            }
        }
        return result.toArray(new Serializable[result.size()]);
    }
    
}
