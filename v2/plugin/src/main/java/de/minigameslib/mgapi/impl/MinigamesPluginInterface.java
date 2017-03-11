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

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.util.function.McBiFunction;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * An internal plugin interface
 * 
 * @author mepeisen
 */
public interface MinigamesPluginInterface
{
    
    /**
     * Returns the create function for given rule set type
     * 
     * @param type
     * @return creator function
     */
    McBiFunction<ArenaRuleSetType, ArenaInterface, ArenaRuleSetInterface> creator(ArenaRuleSetType type);
    
    /**
     * Returns the create function for given rule set type
     * 
     * @param type
     * @return creator function
     */
    McBiFunction<ComponentRuleSetType, ArenaComponentHandler, ComponentRuleSetInterface> creator(ComponentRuleSetType type);
    
    /**
     * Returns the create function for given rule set type
     * 
     * @param type
     * @return creator function
     */
    McBiFunction<SignRuleSetType, ArenaSignHandler, SignRuleSetInterface> creator(SignRuleSetType type);
    
    /**
     * Returns the create function for given rule set type
     * 
     * @param type
     * @return creator function
     */
    McBiFunction<ZoneRuleSetType, ArenaZoneHandler, ZoneRuleSetInterface> creator(ZoneRuleSetType type);
    
    /**
     * Returns the create function for given type
     * 
     * @param type
     * @return creator function
     */
    McSupplier<? extends ArenaComponentHandler> creator(ComponentTypeId type);
    
    /**
     * Returns the create function for given type
     * 
     * @param type
     * @return creator function
     */
    McSupplier<? extends ArenaZoneHandler> creator(ZoneTypeId type);
    
    /**
     * Returns the create function for given type
     * 
     * @param type
     * @return creator function
     */
    McSupplier<? extends ArenaSignHandler> creator(SignTypeId type);
    
    /**
     * Returns the plugin logger
     * @return plugin logger
     */
    Logger getLogger();
    
    /**
     * Returns the java plugin.
     * @return java plugin
     */
    Plugin getPlugin();
    
    /**
     * Returns the data folder.
     * @return data folder.
     */
    File getDataFolder();
    
}
