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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.arena.ArenaInterface;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.cmd.CommandImpl;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mgapi.api.ExtensionInterface;
import de.minigameslib.mgapi.api.ExtensionProvider;
import de.minigameslib.mgapi.api.LibState;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigameProvider;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;
import de.minigameslib.mgapi.impl.tasks.InitTask;

/**
 * Implementation of minigames plugin
 * 
 * @author mepeisen
 */
public class MinigamesPlugin extends JavaPlugin implements MinigamesLibInterface
{
    
    /**
     * the current library state.
     */
    private LibState state = LibState.Initializing;
    
    /**
     * the registered minigames per plugin
     */
    private Map<String, MinigameImpl> minigamesPerPlugin = new HashMap<>();
    
    /**
     * the registered minigames per name
     */
    private Map<String, MinigameImpl> minigamesPerName = new TreeMap<>();
    
    /**
     * the registered extensions per plugin
     */
    private Map<String, ExtensionImpl> extensionsPerPlugin = new HashMap<>();
    
    /**
     * the registered extensions per name
     */
    private Map<String, ExtensionImpl> extensionsPerName = new TreeMap<>();
    
    /** the console commands. */
    private Mg2Command mg2Command = new Mg2Command();

    @Override
    public void onEnable()
    {
        // TODO check api version
        EnumServiceInterface.instance().registerEnumClass(this, MglibConfig.class);
        EnumServiceInterface.instance().registerEnumClass(this, MglibMessages.class);
        EnumServiceInterface.instance().registerEnumClass(this, MglibPerms.class);
        
        Bukkit.getServicesManager().register(MinigamesLibInterface.class, this, this, ServicePriority.Highest);
        
        new InitTask().runTaskLater(this, 10);
    }
    
    @Override
    public void onDisable()
    {
        EnumServiceInterface.instance().unregisterAllEnumerations(this);
    }

    @Override
    public int getApiVersion()
    {
        return APIVERSION_1_0_0;
    }

    @Override
    public boolean debug()
    {
        return MglibConfig.Debug.getBoolean();
    }
    
    @Override
    public LibState getState()
    {
        return this.state;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        switch (command.getName())
        {
            case "mg2": //$NON-NLS-1$
                final CommandImpl cmd = new CommandImpl(sender, command, label, args, "/mg2"); //$NON-NLS-1$
                try
                {
                    McLibInterface.instance().runInNewContext(null, cmd, cmd.getPlayer(), null, null, () -> {
                        this.mg2Command.handle(cmd);
                    });
                }
                catch (McException e)
                {
                    cmd.send(e.getErrorMessage(), e.getArgs());
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        String lastArg = null;
        String[] newArgs = null;
        if (args.length > 0)
        {
            lastArg = args[args.length - 1].toLowerCase();
            newArgs = Arrays.copyOf(args, args.length - 1);
        }

        switch (command.getName())
        {
            case "mg2": //$NON-NLS-1$
                final CommandImpl cmd = new CommandImpl(sender, command, null, newArgs, "/im"); //$NON-NLS-1$
                final String last = lastArg;
                try
                {
                    
                    return McLibInterface.instance().calculateInNewContext(null, cmd, cmd.getPlayer(), null, null, () -> {
                        McLibInterface.instance().setContext(McPlayerInterface.class, cmd.getPlayer());
                        return this.mg2Command.onTabComplete(cmd, last);
                    });
                }
                catch (McException e)
                {
                    cmd.send(e.getErrorMessage(), e.getArgs());
                }
                break;
            default:
                break;
        }
        return Collections.emptyList();
    }
    
    @Override
    public void initMinigame(Plugin plugin, MinigameProvider provider) throws McException
    {
        if (this.state != LibState.Initializing)
        {
            throw new McException(MglibMessages.LibInWrongState);
        }
        if (this.minigamesPerPlugin.containsKey(plugin.getName()))
        {
            throw new McException(MglibMessages.PluginMinigameDuplicate, plugin.getName());
        }
        if (this.minigamesPerName.containsKey(provider.getName()))
        {
            throw new McException(MglibMessages.MinigameAlreadyRegistered, provider.getName());
        }
        final MinigameImpl minigame = new MinigameImpl(plugin, provider);
        this.minigamesPerPlugin.put(plugin.getName(), minigame);
        this.minigamesPerName.put(minigame.getName(), minigame);
    }

    @Override
    public int getMinigameCount()
    {
        return this.minigamesPerName.size();
    }

    @Override
    public int getMinigameCount(String prefix)
    {
        return (int) this.minigamesPerName.keySet().stream().filter(p -> p.startsWith(prefix)).count();
    }

    @Override
    public Collection<MinigameInterface> getMinigames(int index, int limit)
    {
        return this.minigamesPerName.values().stream().skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<MinigameInterface> getMinigames(String prefix, int index, int limit)
    {
        return this.minigamesPerName.values().stream().filter(p -> p.getName().startsWith(prefix)).skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public void initExtension(Plugin plugin, ExtensionProvider provider) throws McException
    {
        if (this.state != LibState.Initializing)
        {
            throw new McException(MglibMessages.LibInWrongState);
        }
        if (this.extensionsPerPlugin.containsKey(plugin.getName()))
        {
            throw new McException(MglibMessages.PluginExtensionDuplicate, plugin.getName());
        }
        if (this.extensionsPerName.containsKey(provider.getName()))
        {
            throw new McException(MglibMessages.ExtensionAlreadyRegistered, provider.getName());
        }
        final ExtensionImpl extension = new ExtensionImpl(plugin, provider);
        this.extensionsPerPlugin.put(plugin.getName(), extension);
        this.extensionsPerName.put(extension.getName(), extension);
    }

    @Override
    public int getExtensionCount()
    {
        return this.extensionsPerName.size();
    }

    @Override
    public int getExtensionCount(String prefix)
    {
        return (int) this.extensionsPerName.keySet().stream().filter(p -> p.startsWith(prefix)).count();
    }

    @Override
    public Collection<ExtensionInterface> getExtensions(int index, int limit)
    {
        return this.extensionsPerName.values().stream().skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ExtensionInterface> getExtensions(String prefix, int index, int limit)
    {
        return this.extensionsPerName.values().stream().filter(p -> p.getName().startsWith(prefix)).skip(index).limit(limit).collect(Collectors.toList());
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getMinigame(java.lang.String)
     */
    @Override
    public MinigameInterface getMinigame(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getExtension(java.lang.String)
     */
    @Override
    public ExtensionInterface getExtension(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenaCount()
     */
    @Override
    public int getArenaCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenaCount(java.lang.String)
     */
    @Override
    public int getArenaCount(String prefix)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenaCount(org.bukkit.plugin.Plugin)
     */
    @Override
    public int getArenaCount(Plugin plugin)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenaCount(org.bukkit.plugin.Plugin, java.lang.String)
     */
    @Override
    public int getArenaCount(Plugin plugin, String prefix)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenaCount(de.minigameslib.mgapi.api.arena.ArenaTypeInterface)
     */
    @Override
    public int getArenaCount(ArenaTypeInterface type)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenaCount(de.minigameslib.mgapi.api.arena.ArenaTypeInterface, java.lang.String)
     */
    @Override
    public int getArenaCount(ArenaTypeInterface type, String prefix)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenas(int, int)
     */
    @Override
    public Collection<ArenaInterface> getArenas(int index, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenas(java.lang.String, int, int)
     */
    @Override
    public Collection<ArenaInterface> getArenas(String prefix, int index, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenas(org.bukkit.plugin.Plugin, int, int)
     */
    @Override
    public Collection<ArenaInterface> getArenas(Plugin plugin, int index, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenas(org.bukkit.plugin.Plugin, java.lang.String, int, int)
     */
    @Override
    public Collection<ArenaInterface> getArenas(Plugin plugin, String prefix, int index, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenas(de.minigameslib.mgapi.api.arena.ArenaTypeInterface, int, int)
     */
    @Override
    public Collection<ArenaInterface> getArenas(ArenaTypeInterface type, int index, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArenas(de.minigameslib.mgapi.api.arena.ArenaTypeInterface, java.lang.String, int, int)
     */
    @Override
    public Collection<ArenaInterface> getArenas(ArenaTypeInterface type, String prefix, int index, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#getArena(java.lang.String)
     */
    @Override
    public ArenaInterface getArena(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.MinigamesLibInterface#create(java.lang.String, de.minigameslib.mgapi.api.arena.ArenaTypeInterface)
     */
    @Override
    public ArenaInterface create(String name, ArenaTypeInterface type) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
