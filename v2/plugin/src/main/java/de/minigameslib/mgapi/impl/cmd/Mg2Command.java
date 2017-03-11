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

package de.minigameslib.mgapi.impl.cmd;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.shared.api.com.UniqueEnumerationValue;
import de.minigameslib.mgapi.api.ExtensionInterface;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;

/**
 * The mg2 main command
 * 
 * @author mepeisen
 */
public class Mg2Command extends AbstractCompositeCommandHandler
{
    
    /**
     * Constructor to register the sub commands
     */
    public Mg2Command()
    {
        this.subCommands.put("help", new HelpCommandHandler(this)); //$NON-NLS-1$
        
        this.subCommands.put("info", new InfoCommand()); //$NON-NLS-1$
        this.subCommands.put("arenas", new ArenasCommand()); //$NON-NLS-1$
        this.subCommands.put("arena", new ArenaCommand()); //$NON-NLS-1$
        this.subCommands.put("join", new JoinCommand()); //$NON-NLS-1$
        this.subCommands.put("spectate", new SpectateCommand()); //$NON-NLS-1$
        this.subCommands.put("leave", new LeaveCommand()); //$NON-NLS-1$
        this.subCommands.put("admin", new AdminCommand()); //$NON-NLS-1$
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws McException
    {
        // allowed from everywhere
        return true;
    }

    @Override
    protected void sendUsage(CommandInterface cmd)
    {
        cmd.send(Messages.Usage);
    }
    
    /**
     * Maps argument to arena interface; returns players arena if no argument was given
     * @param command
     * @param usage 
     * @return arena instance
     * @throws McException thrown if arena was not found
     */
    public static ArenaInterface getArenaFromPlayer(CommandInterface command, LocalizedMessageInterface usage) throws McException
    {
        if (command.getArgs().length == 0)
        {
            if (command.isPlayer())
            {
                final ArenaPlayerInterface player = MinigamesLibInterface.instance().getPlayer(command.getPlayer());
                if (!player.inArena())
                {
                    throw new McException(Messages.ArenaNameMissing, usage);
                }
                return player.getArena();
            }
            throw new McException(Messages.ArenaNameMissing, usage);
        }
        return getArena(command, command.fetchString(Messages.ArenaNameMissing, usage));
    }
    
    /**
     * Maps argument to sign interface
     * @param arena
     * @param command
     * @param usage 
     * @return sign instance
     * @throws McException thrown if sign was not found
     */
    public static SignInterface getSign(ArenaInterface arena, CommandInterface command, LocalizedMessageInterface usage) throws McException
    {
        final String name = command.fetchString(Messages.ComponentNameMissing, usage);
        @SuppressWarnings("cast")
        final Optional<ArenaSignHandler> handler = arena.getSigns().stream().
                map(s -> (ArenaSignHandler) arena.getHandler(s)).
                filter(s -> name.equals(s.getName())).
                findFirst();
        if (handler.isPresent())
        {
            return handler.get().getSign();
        }
        throw new McException(Messages.ComponentNotFound, usage);
    }
    
    /**
     * Maps argument to zone interface
     * @param arena
     * @param command
     * @param usage 
     * @return sign instance
     * @throws McException thrown if zone was not found
     */
    public static ZoneInterface getZone(ArenaInterface arena, CommandInterface command, LocalizedMessageInterface usage) throws McException
    {
        final String name = command.fetchString(Messages.ComponentNameMissing, usage);
        @SuppressWarnings("cast")
        final Optional<ArenaZoneHandler> handler = arena.getZones().stream().
                map(s -> (ArenaZoneHandler) arena.getHandler(s)).
                filter(s -> name.equals(s.getName())).
                findFirst();
        if (handler.isPresent())
        {
            return handler.get().getZone();
        }
        throw new McException(Messages.ComponentNotFound, usage);
    }
    
    /**
     * Maps argument to component interface
     * @param arena
     * @param command
     * @param usage 
     * @return sign instance
     * @throws McException thrown if zone was not found
     */
    public static ComponentInterface getComponent(ArenaInterface arena, CommandInterface command, LocalizedMessageInterface usage) throws McException
    {
        final String name = command.fetchString(Messages.ComponentNameMissing, usage);
        @SuppressWarnings("cast")
        final Optional<ArenaComponentHandler> handler = arena.getComponents().stream().
                map(s -> (ArenaComponentHandler) arena.getHandler(s)).
                filter(s -> name.equals(s.getName())).
                findFirst();
        if (handler.isPresent())
        {
            return handler.get().getComponent();
        }
        throw new McException(Messages.ComponentNotFound, usage);
    }
    
    /**
     * Maps argument to arena interface
     * @param command
     * @param usage
     * @return arena instance
     * @throws McException thrown if arena was not found
     */
    public static ArenaInterface getArena(CommandInterface command, LocalizedMessageInterface usage) throws McException
    {
        return getArena(command, command.fetchString(Messages.ArenaNameMissing, usage));
    }
    
    /**
     * Maps argument to arena interface
     * @param command
     * @param usage
     * @return arena instance
     */
    public static ArenaInterface getArenaOptional(CommandInterface command, LocalizedMessageInterface usage)
    {
        if (command.getArgs().length == 0) return null;
        try
        {
            return getArenaOptional(command, command.fetchString(Messages.ArenaNameMissing, usage));
        }
        catch (@SuppressWarnings("unused") McException e)
        {
            // silently ignore (will not happen)
            return null;
        }
    }
    
    /**
     * Maps argument to arena interface
     * @param command
     * @param arenaName
     * @return arena instance
     * @throws McException thrown if arena was not found
     */
    public static ArenaInterface getArena(CommandInterface command, String arenaName) throws McException
    {
        final ArenaInterface arena = getArenaOptional(command, arenaName);
        if (arena == null)
        {
            throw new McException(Messages.ArenaNotFound, arenaName);
        }
        return arena;
    }
    
    /**
     * Maps argument to arena interface
     * @param command
     * @param arenaName
     * @return arena instance or {@code null} if it was not found
     */
    public static ArenaInterface getArenaOptional(CommandInterface command, String arenaName)
    {
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        final ArenaInterface arena = mglib.getArena(arenaName);
        return arena;
    }
    
    /**
     * Maps argument to minigame interface
     * @param command
     * @param minigameName
     * @return minigame instance
     * @throws McException thrown if minigame was not found
     */
    public static MinigameInterface getMinigame(CommandInterface command, String minigameName) throws McException
    {
        final MinigameInterface minigame = getMinigameOptional(command, minigameName);
        if (minigame == null)
        {
            throw new McException(Messages.MinigameNotFound, minigameName);
        }
        return minigame;
    }
    
    /**
     * Maps argument to minigame interface
     * @param command
     * @param minigameName
     * @return minigame instance or {@code null} if it was not found
     */
    public static MinigameInterface getMinigameOptional(CommandInterface command, String minigameName)
    {
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        final MinigameInterface minigame = mglib.getMinigame(minigameName);
        return minigame;
    }
    
    /**
     * Maps argument to unique enum value
     * @param command
     * @param clazz
     * @param typeName
     * @return type enum
     * @throws McException thrown if type enum was not found
     */
    public static <T extends UniqueEnumerationValue> T getEnum(CommandInterface command, Class<T> clazz, String typeName) throws McException
    {
        final String[] splitted = typeName.split("\\/", 2); //$NON-NLS-1$
        if (splitted.length == 1)
        {
            throw new McException(Messages.ComponentTypeNotFound, typeName);
        }
        final T result = EnumServiceInterface.instance().getEnumValue(clazz, splitted[0], splitted[1]);
        if (result == null)
        {
            throw new McException(Messages.ComponentTypeNotFound, typeName);
        }
        return result;
    }
    
    /**
     * Maps argument to arena type interface
     * @param command
     * @param typeName
     * @param minigame
     * @return type instance
     * @throws McException thrown if type was not found
     */
    public static ArenaTypeInterface getType(CommandInterface command, String typeName, MinigameInterface minigame) throws McException
    {
        final ArenaTypeInterface type = getTypeOptional(command, typeName, minigame);
        if (type == null)
        {
            throw new McException(Messages.TypeNotFound, typeName);
        }
        return type;
    }
    
    /**
     * Maps argument to arena type interface
     * @param command
     * @param typeName
     * @param minigame 
     * @return type instance or {@code null} if it was not found
     */
    public static ArenaTypeInterface getTypeOptional(CommandInterface command, String typeName, MinigameInterface minigame)
    {
        final ArenaTypeInterface type = minigame.getType(typeName);
        return type;
    }
    
    /**
     * Maps argument to extension interface
     * @param command
     * @param extensionName
     * @return extension instance
     * @throws McException thrown if extension was not found
     */
    public static ExtensionInterface getExtension(CommandInterface command, String extensionName) throws McException
    {
        final ExtensionInterface extension = getExtensionOptional(command, extensionName);
        if (extension == null)
        {
            throw new McException(Messages.ExtensionNotFound, extensionName);
        }
        return extension;
    }
    
    /**
     * Maps argument to extension interface
     * @param command
     * @param extensionName
     * @return extension instance or {@code null} if it was not found
     */
    public static ExtensionInterface getExtensionOptional(CommandInterface command, String extensionName)
    {
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        final ExtensionInterface extension = mglib.getExtension(extensionName);
        return extension;
    }
    
    /**
     * Maps argument to player interface
     * @param command
     * @param playerName
     * @return player instance
     * @throws McException thrown if player was not found
     */
    public static ArenaPlayerInterface getPlayer(CommandInterface command, String playerName) throws McException
    {
        final ArenaPlayerInterface player = getPlayerOptional(command, playerName);
        if (player == null)
        {
            throw new McException(Messages.PlayerNotFound, playerName);
        }
        return player;
    }
    
    /**
     * Maps argument to player interface
     * @param command
     * @param playerName
     * @return player instance or {@code null} if it was not found
     */
    public static ArenaPlayerInterface getPlayerOptional(CommandInterface command, String playerName)
    {
        final Player bukkitPlayer = Bukkit.getPlayerExact(playerName);
        if (bukkitPlayer != null)
        {
            final McPlayerInterface mcPlayer = ObjectServiceInterface.instance().getPlayer(bukkitPlayer);
            final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
            final ArenaPlayerInterface player = mglib.getPlayer(mcPlayer);
            return player;
        }
        return null;
    }
    
    /**
     * The /mg2 messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Usage for command /mg2
         */
        @LocalizedMessage(defaultMessage = "Enter " + LocalizedMessage.CODE_COLOR + "/mg2 help" + LocalizedMessage.INFORMATION_COLOR + " for detailed help")
        @MessageComment({"Usage for command /mg2"})
        Usage,
        
        /**
         * Arena was not found
         */
        @LocalizedMessage(defaultMessage = "Arena " + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"arena was not found"}, args = @Argument("arena name"))
        ArenaNotFound,
        
        /**
         * Player was not found
         */
        @LocalizedMessage(defaultMessage = "Player " + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"player was not found"}, args = @Argument("player name"))
        PlayerNotFound,
        
        /**
         * Minigame was not found
         */
        @LocalizedMessage(defaultMessage = "Minigame " + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"minigame was not found"}, args = @Argument("minigame name"))
        MinigameNotFound,
        
        /**
         * Arena type was not found
         */
        @LocalizedMessage(defaultMessage = "Arena type " + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"arena type was not found"}, args = @Argument("type name"))
        TypeNotFound,
        
        /**
         * Extension was not found
         */
        @LocalizedMessage(defaultMessage = "Extension " + LocalizedMessage.CODE_COLOR + "%1$s" + LocalizedMessage.ERROR_COLOR + " not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"extension was not found"}, args = @Argument("extension name"))
        ExtensionNotFound,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessageList(value = {"Missing player name", "%1$s"}, severity = MessageSeverityType.Error)
        @MessageComment(value = {"Name argument is missing"}, args = @Argument("command usage"))
        PlayerNameMissing,
        
        /**
         * Component argument is missing
         */
        @LocalizedMessageList(value = {"Missing component name", "%1$s"}, severity = MessageSeverityType.Error)
        @MessageComment(value = {"Component argument is missing"}, args = @Argument("command usage"))
        ComponentNameMissing,
        
        /**
         * Component already exists
         */
        @LocalizedMessage(defaultMessage = "Component " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.ERROR_COLOR + "already exists", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Component already exists"}, args = @Argument("component name"))
        ComponentAlreadyExists,
        
        /**
         * Component not found
         */
        @LocalizedMessage(defaultMessage = "Component " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.ERROR_COLOR + "not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Component not found"}, args = @Argument("component name"))
        ComponentNotFound,
        
        /**
         * Component type argument is missing
         */
        @LocalizedMessageList(value = {"Missing component type name", "%1$s"}, severity = MessageSeverityType.Error)
        @MessageComment(value = {"Component type argument is missing"}, args = @Argument("command usage"))
        ComponentTypeNameMissing,
        
        /**
         * Component type not found
         */
        @LocalizedMessage(defaultMessage = "Component type " + LocalizedMessage.CODE_COLOR + "%1$s " + LocalizedMessage.ERROR_COLOR + "not found", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Component type not found"}, args = @Argument("component type name"))
        ComponentTypeNotFound,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessageList(value = {"Missing arena name", "%1$s"}, severity = MessageSeverityType.Error)
        @MessageComment(value = {"Name argument is missing"}, args = @Argument("command usage"))
        ArenaNameMissing,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessageList(value = {"Missing minigame name", "%1$s"}, severity = MessageSeverityType.Error)
        @MessageComment(value = {"Name argument is missing"}, args = @Argument("command usage"))
        MinigameNameMissing,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessage(defaultMessage = "Missing extension name", severity = MessageSeverityType.Error)
        @MessageComment({"Name argument is missing"})
        ExtensionNameMissing,
        
        /**
         * Name argument is missing
         */
        @LocalizedMessageList(value = {"Missing arena type", "%1$s"}, severity = MessageSeverityType.Error)
        @MessageComment(value = {"Name argument is missing"}, args = @Argument("command usage"))
        TypeNameMissing,
        
    }
    
}
