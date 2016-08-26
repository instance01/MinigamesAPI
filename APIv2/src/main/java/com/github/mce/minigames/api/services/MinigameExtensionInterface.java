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

package com.github.mce.minigames.api.services;

import java.io.Serializable;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.config.ConfigInterface;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

/**
 * A special extension for the minigames library.
 * 
 * <p>
 * Extensions can be installed through {@link MglibInterface#register(MinigameExtensionProviderInterface)}.
 * They behave different from minigames. Mainly they are responsible to add additional features to
 * the library. They do not install any arena type nor do they provide any game.
 * </p>
 * 
 * @author mepeisen
 */
public interface MinigameExtensionInterface extends ConfigInterface
{

    /**
     * Returns the name of the extension.
     * 
     * @return extension name.
     */
    String getName();

    /**
     * Returns the short description of the extension.
     * 
     * @return A short description; use method {@link LocalizedMessageInterface#toArg(Serializable...)} on the message.
     */
    Serializable getShortDescription();
    
    // TODO
    
}
