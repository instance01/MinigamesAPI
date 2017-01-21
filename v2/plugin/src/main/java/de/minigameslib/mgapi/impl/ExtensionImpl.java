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

package de.minigameslib.mgapi.impl;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.ExtensionInterface;
import de.minigameslib.mgapi.api.ExtensionProvider;

/**
 * @author mepeisen
 *
 */
class ExtensionImpl implements ExtensionInterface
{

    /** owning plugin. */
    private final Plugin plugin;
    
    /** extension provider. */
    private final ExtensionProvider provider;

    /**
     * @param plugin
     * @param provider
     */
    public ExtensionImpl(Plugin plugin, ExtensionProvider provider)
    {
        this.plugin = plugin;
        this.provider = provider;
    }

    @Override
    public String getName()
    {
        return this.provider.getName();
    }

    @Override
    public LocalizedMessageInterface getDisplayName()
    {
        return this.provider.getDisplayName();
    }

    @Override
    public LocalizedMessageInterface getShortDescription()
    {
        return this.provider.getShortDescription();
    }

    @Override
    public LocalizedMessageInterface getDescription()
    {
        return this.provider.getDescription();
    }

    @Override
    public LocalizedMessageInterface getManual()
    {
        return this.provider.getManual();
    }

    @Override
    public Plugin getPlugin()
    {
        return this.plugin;
    }
    
}
