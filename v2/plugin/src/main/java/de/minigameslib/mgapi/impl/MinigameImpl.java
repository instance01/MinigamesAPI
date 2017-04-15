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

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigameProvider;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

/**
 * @author mepeisen
 *
 */
class MinigameImpl implements MinigameInterface
{
    
    /** owning plugin. */
    private final Plugin plugin;
    
    /** minigame provider. */
    private final MinigameProvider provider;

    /**
     * @param plugin
     * @param provider
     */
    public MinigameImpl(Plugin plugin, MinigameProvider provider)
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
    public LocalizedMessageInterface getHowToPlay()
    {
        return this.provider.getHowToPlay();
    }

    @Override
    public Plugin getPlugin()
    {
        return this.plugin;
    }

    @Override
    public ArenaTypeInterface getType(String typeName)
    {
        for (final ArenaTypeInterface type : EnumServiceInterface.instance().getEnumValues(this.getPlugin(), ArenaTypeInterface.class))
        {
            if (type.name().equals(typeName))
            {
                return type;
            }
        }
        return null;
    }

    @Override
    public int getTypeCount()
    {
        return EnumServiceInterface.instance().getEnumValues(this.getPlugin(), ArenaTypeInterface.class).size();
    }

    @Override
    public Collection<ArenaTypeInterface> getTypes(String prefix, int start, int limit)
    {
        return EnumServiceInterface.instance().getEnumValues(this.getPlugin(), ArenaTypeInterface.class).stream().filter(t -> t.name().toLowerCase().startsWith(prefix.toLowerCase())).skip(start).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaTypeInterface> getTypes(int start, int limit)
    {
        return EnumServiceInterface.instance().getEnumValues(this.getPlugin(), ArenaTypeInterface.class);
    }
    
}
