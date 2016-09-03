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

package com.github.mce.minigames.api;

import java.io.Serializable;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

/**
 * An interface that should be implemented by the {@link JavaPlugin} of a specific minigame.
 * 
 * @author mepeisen
 */
public interface PluginProviderInterface extends CommonProviderInterface
{
    
    /**
     * Returns the technical name of the minigame.
     * 
     * @return minigame name.
     */
    String getName();

    /**
     * Returns the short description of the minigame.
     * 
     * @return A short description; use method {@link LocalizedMessageInterface#toArg(Serializable...)} on the message.
     */
    Serializable getShortDescription();

    /**
     * Returns the long multi line description of the minigame.
     * 
     * @return A long multi line description; use method {@link LocalizedMessageInterface#toListArg(Serializable...)} on the message.
     */
    Serializable getDescription();
    
}
