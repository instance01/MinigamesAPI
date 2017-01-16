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

package com.github.mce.minigames.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.PluginProviderInterface;
import com.github.mce.minigames.api.config.CommonConfig;
import com.github.mce.minigames.api.perms.CommonPermissions;
import com.github.mce.minigames.impl.cmd.Mg2CommandHandler;
import com.github.mce.minigames.impl.cmd.PartyCommandHandler;
import com.github.mce.minigames.impl.cmd.StartCommandHandler;

import de.minigameslib.mclib.api.cmd.CommandHandlerInterface;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.perms.PermissionsInterface;

/**
 * @author mepeisen
 *
 */
final class CoreMinigame implements PluginProviderInterface
{
    
    /** the core plugin. */
    private final MinigamesPlugin plugin;
    
    /**
     * Constructor.
     * @param plugin
     */
    public CoreMinigame(MinigamesPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public String getName()
    {
//        return MglibInterface.CORE_MINIGAME;
        return null;
    }
    
    @Override
    public Iterable<Class<? extends LocalizedMessageInterface>> getMessageClasses()
    {
        final List<Class<? extends LocalizedMessageInterface>> result = new ArrayList<>();
        result.add(CommonErrors.class);
        result.add(CommonMessages.class);
        return result;
    }
    
    @Override
    public JavaPlugin getJavaPlugin()
    {
        return this.plugin;
    }
    
    @Override
    public Map<String, CommandHandlerInterface> getBukkitCommands()
    {
        final Map<String, CommandHandlerInterface> result = new HashMap<>();
        result.put("start", new StartCommandHandler()); //$NON-NLS-1$
        result.put("party", new PartyCommandHandler()); //$NON-NLS-1$
        result.put("mg2", new Mg2CommandHandler()); //$NON-NLS-1$
        return result;
    }
    
    @Override
    public Iterable<Class<? extends PermissionsInterface>> getPermissions()
    {
        final List<Class<? extends PermissionsInterface>> result = new ArrayList<>();
        result.add(CommonPermissions.class);
        return result;
    }
    
    @Override
    public Iterable<Class<? extends ConfigurationValueInterface>> getConfigurations()
    {
        final List<Class<? extends ConfigurationValueInterface>> result = new ArrayList<>();
        result.add(CommonConfig.class);
        return result;
    }
    
    @Override
    public Serializable getShortDescription()
    {
        return CommonMessages.CoreMinigameDescription.toArg();
    }
    
    @Override
    public Serializable getDescription()
    {
        return CommonMessages.CoreMinigameLongDescription.toListArg();
    }
}