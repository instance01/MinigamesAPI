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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.mclib.api.CommonMessages;
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
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.MglibMessages.MglibCoreErrors;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;
import de.minigameslib.mgapi.impl.arena.ArenaPlayerImpl;
import de.minigameslib.mgapi.impl.arena.ArenaPlayerPersistentData;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;
import de.minigameslib.mgapi.impl.internal.TaskManager;
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
    
    /** arenas per name. */
    private Map<String, ArenaImpl> arenasPerName = new TreeMap<>();
    
    // TODO Watch for disabled plugins
    
    /** arena name check pattern */
    private static final Pattern ARENA_NAME_PATTERN = Pattern.compile("[^\\d\\p{L}-]"); //$NON-NLS-1$

    @Override
    public void onEnable()
    {
        if (McLibInterface.instance().getApiVersion() != McLibInterface.APIVERSION_1_0_0)
        {
            throw new IllegalStateException("Cannot enable minigameslib. You installed an incompatible McLib-Version. " + McLibInterface.instance().getApiVersion()); //$NON-NLS-1$
        }
        
        EnumServiceInterface.instance().registerEnumClass(this, MglibConfig.class);
        EnumServiceInterface.instance().registerEnumClass(this, MglibMessages.class);
        EnumServiceInterface.instance().registerEnumClass(this, MglibPerms.class);
        
        Bukkit.getServicesManager().register(MinigamesLibInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(TaskManager.class, new TaskManager(), this, ServicePriority.Highest);
        
        final String[] arenas = MglibConfig.Arenas.getStringList();
        for (final String arena : arenas)
        {
            try
            {
                this.checkArenaName(arena);
                this.getLogger().log(Level.INFO, "Found arena " + arena); //$NON-NLS-1$
                final ArenaImpl arenaImpl = new ArenaImpl(new File(this.getDataFolder(), "arenas/" + arena + ".yml"));  //$NON-NLS-1$//$NON-NLS-2$
                if (!arenaImpl.getInternalName().equals(arena))
                {
                    throw new McException(MglibCoreErrors.ArenaNameMismatch, arena);
                }
                this.arenasPerName.put(arena, arenaImpl);
            }
            catch (McException ex)
            {
                this.getLogger().log(Level.WARNING, "Problems loading arena", ex); //$NON-NLS-1$
            }
        }
        this.getLogger().log(Level.INFO, "Enabled mglib. Loaded arenas: " + this.arenasPerName.size()); //$NON-NLS-1$
        
        new InitTask().runTaskLater(this, 10);
    }
    
    /**
     * Checks for valid arena name
     * @param arena
     * @throws McException thrown for invalid arena names.
     */
    private void checkArenaName(String arena) throws McException
    {
        if (ARENA_NAME_PATTERN.matcher(arena).find())
        {
            throw new McException(MglibCoreErrors.InvalidArenaName, arena);
        }
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
            throw new McException(MglibCoreErrors.LibInWrongState);
        }
        if (this.minigamesPerPlugin.containsKey(plugin.getName()))
        {
            throw new McException(MglibCoreErrors.PluginMinigameDuplicate, plugin.getName());
        }
        if (this.minigamesPerName.containsKey(provider.getName()))
        {
            throw new McException(MglibCoreErrors.MinigameAlreadyRegistered, provider.getName());
        }
        final MinigameImpl minigame = new MinigameImpl(plugin, provider);
        this.minigamesPerPlugin.put(plugin.getName(), minigame);
        this.minigamesPerName.put(minigame.getName(), minigame);
    }

    @Override
    public MinigameInterface getMinigame(String name)
    {
        return this.minigamesPerName.get(name);
    }

    @Override
    public MinigameInterface getMinigame(Plugin plugin)
    {
        return this.minigamesPerPlugin.get(plugin.getName());
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
            throw new McException(MglibCoreErrors.LibInWrongState);
        }
        if (this.extensionsPerPlugin.containsKey(plugin.getName()))
        {
            throw new McException(MglibCoreErrors.PluginExtensionDuplicate, plugin.getName());
        }
        if (this.extensionsPerName.containsKey(provider.getName()))
        {
            throw new McException(MglibCoreErrors.ExtensionAlreadyRegistered, provider.getName());
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

    @Override
    public ExtensionInterface getExtension(String name)
    {
        return this.extensionsPerName.get(name);
    }

    @Override
    public int getArenaCount()
    {
        return this.arenasPerName.size();
    }

    @Override
    public int getArenaCount(String prefix)
    {
        return (int) this.arenasPerName.keySet().stream().filter(p -> p.startsWith(prefix)).count();
    }

    @Override
    public int getArenaCount(Plugin plugin)
    {
        return (int) this.arenasPerName.values().stream().filter(p -> p.getPlugin() == plugin).count();
    }

    @Override
    public int getArenaCount(Plugin plugin, String prefix)
    {
        return (int) this.arenasPerName.values().stream().filter(p -> p.getPlugin() == plugin).filter(p -> p.getInternalName().startsWith(prefix)).count();
    }

    @Override
    public int getArenaCount(ArenaTypeInterface type)
    {
        return (int) this.arenasPerName.values().stream().filter(p -> p.getType() == type).count();
    }

    @Override
    public int getArenaCount(ArenaTypeInterface type, String prefix)
    {
        return (int) this.arenasPerName.values().stream().filter(p -> p.getType() == type).filter(p -> p.getInternalName().startsWith(prefix)).count();
    }

    @Override
    public Collection<ArenaInterface> getArenas(int index, int limit)
    {
        return this.arenasPerName.values().stream().skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaInterface> getArenas(String prefix, int index, int limit)
    {
        return this.arenasPerName.values().stream().filter(p -> p.getInternalName().startsWith(prefix)).skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaInterface> getArenas(Plugin plugin, int index, int limit)
    {
        return this.arenasPerName.values().stream().filter(p -> p.getPlugin() == plugin).skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaInterface> getArenas(Plugin plugin, String prefix, int index, int limit)
    {
        return this.arenasPerName.values().stream().filter(p -> p.getPlugin() == plugin).filter(p -> p.getInternalName().startsWith(prefix)).skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaInterface> getArenas(ArenaTypeInterface type, int index, int limit)
    {
        return this.arenasPerName.values().stream().filter(p -> p.getType() == type).skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaInterface> getArenas(ArenaTypeInterface type, String prefix, int index, int limit)
    {
        return this.arenasPerName.values().stream().filter(p -> p.getType() == type).filter(p -> p.getInternalName().startsWith(prefix)).skip(index).limit(limit).collect(Collectors.toList());
    }

    @Override
    public ArenaInterface getArena(String name)
    {
        return this.arenasPerName.get(name);
    }

    @Override
    public ArenaInterface create(String name, ArenaTypeInterface type) throws McException
    {
        if (type == null)
        {
            throw new McException(CommonMessages.InternalError, "arena type must not be null"); //$NON-NLS-1$
        }
        if (this.arenasPerName.containsKey(name))
        {
            throw new McException(MglibCoreErrors.ArenaDuplicate, name);
        }
        
        final ArenaImpl arena = new ArenaImpl(name, type, new File(this.getDataFolder(), "arenas/" + name + ".yml")); //$NON-NLS-1$ //$NON-NLS-2$
        arena.saveData();
        
        final Set<String> arenas = new HashSet<>(this.arenasPerName.keySet());
        arenas.add(name);
        MglibConfig.Arenas.setStringList(arenas.toArray(new String[arenas.size()]));
        MglibConfig.Arenas.saveConfig();
        
        // everything ok, now we can add the arena to our internal map.
        this.arenasPerName.put(name, arena);
        return arena;
    }

    @Override
    public ArenaPlayerInterface getPlayer(McPlayerInterface player)
    {
        ArenaPlayerImpl impl = player.getSessionStorage().get(ArenaPlayerImpl.class);
        if (impl == null)
        {
            ArenaPlayerPersistentData persistent = player.getPersistentStorage().get(ArenaPlayerPersistentData.class);
            if (persistent == null)
            {
                persistent = new ArenaPlayerPersistentData();
                player.getPersistentStorage().set(ArenaPlayerPersistentData.class, persistent);
            }
            impl = new ArenaPlayerImpl(player, persistent);
            player.getSessionStorage().set(ArenaPlayerImpl.class, impl);
        }
        return impl;
    }
    
}
