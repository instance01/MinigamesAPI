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

import java.util.List;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * Command to display useful information.
 * 
 * @author mepeisen
 */
public class SaveCommandHandler implements SubCommandHandlerInterface
{
    
    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.CommandHandlerInterface#handle(com.github.mce.minigames.api.cmd.CommandInterface)
     */
    @Override
    public void handle(CommandInterface command) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.CommandHandlerInterface#onTabComplete(com.github.mce.minigames.api.cmd.CommandInterface, java.lang.String)
     */
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getShortDescription(com.github.mce.minigames.api.cmd.CommandInterface)
     */
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.cmd.SubCommandHandlerInterface#getDescription(com.github.mce.minigames.api.cmd.CommandInterface)
     */
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
