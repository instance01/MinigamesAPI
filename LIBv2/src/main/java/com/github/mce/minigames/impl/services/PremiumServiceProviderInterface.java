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

package com.github.mce.minigames.impl.services;

import java.util.Map;

import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;

import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;

/**
 * A special extension to provide premium services; there can only be one premium extension.
 * 
 * @author mepeisen
 */
public interface PremiumServiceProviderInterface extends MinigameExtensionProviderInterface
{
    
    /**
     * Returns additional commands being present in the mg2 commands.
     * @return map containing additional mg2 commands; key is the command path and value is the sub command handler.
     */
    Map<String, SubCommandHandlerInterface> getAdditionalCommands();
    
}
