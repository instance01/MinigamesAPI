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

package com.github.mce.minigames.api.cmd;

import java.io.Serializable;


/**
 * Extends the command handler interface to be placed as a sub command in {@link AbstractCompositeCommandHandler}
 * 
 * @author mepeisen
 */
public interface SubCommandHandlerInterface extends CommandHandlerInterface
{
    
    /**
     * Returns a short description line.
     * 
     * @param command
     *            the command to be used.
     * @return short description line for command help.
     */
    Serializable getShortDescription(CommandInterface command);
    
    /**
     * Returns a usage information.
     * 
     * @param command
     *            the command to be used.
     * @return short usage information for command help.
     */
    Serializable getUsage(CommandInterface command);
    
    /**
     * Returns a long description.
     * 
     * @param command
     *            the command to be used.
     * @return long description line for command details.
     */
    Serializable[] getDescription(CommandInterface command);
    
}
