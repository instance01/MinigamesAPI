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

import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageSeverityType;

/**
 * Common messages within minigames lib.
 * 
 * @author mepeisen
 */
@LocalizedMessages("messages")
public enum CommonMessages implements LocalizedMessageInterface
{
    
    /**
     * Game was started by command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * <li>String: player name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Arena %1$s was started by player: %2$s.", severity = MessageSeverityType.Success)
    ArenaStartedByCommand,

    /**
     * arena join state
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GREEN + "Join")
    AraneStateJoin,

    /**
     * arena join state
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.YELLOW + "Starting")
    AraneStateStarting,

    /**
     * arena ingame state
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.YELLOW + "InGame")
    AraneStateInGame,

    /**
     * arena restarting state
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.YELLOW + "Restarting")
    AraneStateRestarting,

    /**
     * arena disabled state
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.RED + "Disabled")
    AraneStateDisabled,

    /**
     * arena meintenance state
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.RED + "Maintenance")
    AraneStateMeintenance,
    
    /**
     * The core minigame description
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "CORE (MinigamesLib)")
    CoreMinigameDescription,
    
    /**
     * The core minigame description
     * 
     * <p>no arguments.</p>
     */
    @LocalizedMessageList({
        "The CORE minigame does not provide any game.",
        "It is part of the library itself.",
        "You cannot create arenas for minigame CORE."
    })
    CoreMinigameLongDescription,
    
    // ***** commands, usages etc.

    /**
     * Usage of party command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s [subcommand] " + LocalizedMessage.GRAY + "Call party sub commands.")
    PartyCommandUsage,
    
    /**
     * Short description of party command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Control your party and invites.")
    PartyCommandShortDescription,
    
    /**
     * Long description of party command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessageList({
        "Build a party with your friends.",
        "One player can create a party by starting invites.",
        "As soon as all friends are invited and accepted the invite",
        "the party owner can join an arena with the entire party."
    })
    PartyCommandDescription,
    
    /**
     * Minigame was not found.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Minigame %1$s not found.", severity = MessageSeverityType.Error)
    MinigameNotFound,
    
    /**
     * Default arena type was not found.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Minigame %1$s does not declare a default arena type.", severity = MessageSeverityType.Error)
    DefaultArenaTypeNotFound,
    
    /**
     * Default arena type was not found.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: minigame name</li>
     * <li>String: arena type name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Minigame %1$s does not declare arena type %2$s.", severity = MessageSeverityType.Error)
    ArenaTypeNotFound,
    
    /**
     * Usage of create command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s [minigame] [type] [name] " + LocalizedMessage.GRAY + "Create a new arena.")
    CreateCommandUsage,
    
    /**
     * Short description of create command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Create a new arena.")
    CreateCommandShortDescription,
    
    /**
     * Long description of create command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessageList({
        "Creates a new arena.",
        "As first argument give the internal name of the minigame.",
        "As second argument give the arena type name.",
        "As third argument give the unique name of the arena.",
        "The arena type is optional. If it is missing the default type is used.",
        "  Example: " + LocalizedMessage.BLUE + "%1$s Snake Sheeps Snake1",
        "The new arena will automatically be in maintenance mode."
    })
    CreateCommandDescription,
    
    /**
     * The command output of /mg2 info.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * <li>String: spigot version</li>
     * <li>String: minigames version</li>
     * <li>String: mode</li>
     * <li>String: debug</li>
     * </ol>
     */
    @LocalizedMessageList({
        "minigames lib version %3$s (%4$s)",
        "running on minecraft %2$s",
        "debugging: %5$s",
        "----------",
        "Run for additional information:",
        "  " + LocalizedMessage.BLUE + "%1$s extensions " + LocalizedMessage.GRAY + " to list the extensions.",
        "  " + LocalizedMessage.BLUE + "%1$s minigames " + LocalizedMessage.GRAY + " to list the minigames.",
        "  " + LocalizedMessage.BLUE + "%1$s arenas " + LocalizedMessage.GRAY + " to list the arenas."
    })
    InfoCommandOutput,
    
    /**
     * Usage of info command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "Information on minigames lib.")
    InfoCommandUsage,
    
    /**
     * Short description of info command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Information on minigames lib.")
    InfoCommandShortDescription,
    
    /**
     * Long description of info command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessageList({
        "Display some useful information on the minigames library.",
        "Version information, important options etc.",
        "This command requires an administrator permission.",
        "  " + LocalizedMessage.BLUE + "%1$s extensions " + LocalizedMessage.GRAY + " to list the extensions.",
        "  " + LocalizedMessage.BLUE + "%1$s minigames " + LocalizedMessage.GRAY + " to list the minigames.",
        "  " + LocalizedMessage.BLUE + "%1$s arenas " + LocalizedMessage.GRAY + " to list the arenas."
    })
    InfoCommandDescription,
    
    /**
     * Info with unknown sub command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command line</li>
     * <li>String: The sub command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    InfoUnknownSubCommand,
    
    /**
     * Info minigames command header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "minigames")
    InfoMinigamesHeader,
    
    /**
     * Info minigames line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The minigame name</li>
     * <li>String: The short description</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "%2$s")
    InfoMinigamesLine,
    
    /**
     * Info extensions command header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "extensions")
    InfoExtensionsHeader,
    
    /**
     * Info extensions line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The extension name</li>
     * <li>String: The short description</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "%2$s")
    InfoExtensionLine,
    
    /**
     * Info arenas command header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "arenas")
    InfoArenasHeader,
    
    /**
     * Info arenas line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The minigame name</li>
     * <li>String: The internal name</li>
     * <li>String: The arena state</li>
     * <li>String: The display name</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "%1$s %2$s " + LocalizedMessage.GRAY + "[%3$s" + LocalizedMessage.GRAY + "] %4$s")
    InfoArenaLine,
    
    /**
     * Usage of start command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "Force the current arena to start.")
    StartCommandUsage,
    
    /**
     * Short description of start command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Force the current arena to start.")
    StartCommandShortDescription,
    
    /**
     * Long description of start command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * </ol>
     */
    @LocalizedMessageList({
        "Forces the arena you are part of to start.",
        "The arena can only be started while you are in the waiting lobby.",
        "This command requires a start permission."
    })
    StartCommandDescription,
    
    /**
     * Error message for invalid commands (too many arguments)
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "Too many arguments", severity = MessageSeverityType.Error)
    TooManyArguments,
    
    /**
     * Mg2 command usage.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Type " + LocalizedMessage.BLUE + "/mg2 help " + LocalizedMessage.GRAY + "for detailed help", severity = MessageSeverityType.Success)
    Mg2CommandUsage,
    
    /**
     * Invalid sub command in composite command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: current command path</li>
     * <li>String: current sub command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    CompositeUnknownSubCommand,
    
    /**
     * Paged output; header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: info text</li>
     * <li>Integer: current page</li>
     * <li>Integer: total pages</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "=====" + LocalizedMessage.BLUE + "%1$s" + LocalizedMessage.GRAY + "====" + LocalizedMessage.BLUE + "Page " + LocalizedMessage.DARK_BLUE + "%2$d " + LocalizedMessage.BLUE + " from " + LocalizedMessage.DARK_BLUE + "%3$d" + LocalizedMessage.GRAY + "=====")
    PagedHeader,
    
    /**
     * Paged output; line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: line text</li>
     * <li>Integer: line number</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = " " + LocalizedMessage.GRAY + "%1$s")
    PagedLine,
    
    /**
     * Paged output; wrong page number.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>Integer: Page number that was entered by user</li>
     * <li>Integer: Total page count being available</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Page %1$d out of range. Only values from 1 to %2$d allowed.", severity = MessageSeverityType.Error)
    PagedWrongPageNum,
    

    /**
     * Paged output; invalid page number (not numeric).
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = "Invalid page number/ number format error.", severity = MessageSeverityType.Error)
    PagedInvalidNumber,
    
    /**
     * Paged output; usage information
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Usage: " + LocalizedMessage.BLUE + "%1$s [page] " + LocalizedMessage.GRAY + "Display the given page.")
    PageUsage,
    
    /**
     * Help command header.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "help")
    HelpHeader,
    
    /**
     * Help line.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command line</li>
     * <li>String: The short description</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.BLUE + "%1$s " + LocalizedMessage.GRAY + "%2$s")
    HelpLineUsage,
    
    /**
     * Help command short description.
     * 
     * <p>No arguments.</p>
     */
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "Display command help")
    HelpShortDescription,
    
    /**
     * Help on unknown sub command.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command line</li>
     * <li>String: The sub command</li>
     * </ol>
     */
    @LocalizedMessage(defaultMessage = "Unknown command " + LocalizedMessage.BLUE + "%1$s %2$s", severity = MessageSeverityType.Error)
    HelpUnknownSubCommand,
    
    /**
     * Help command long description.
     * 
     * <p>Arguments:</p>
     * 
     * <ol>
     * <li>String: The command that was entered</li>
     * <li>String: The usage of this command</li>
     * </ol>
     */
    @LocalizedMessageList({
        LocalizedMessageList.GRAY + "Displays command help for command " + LocalizedMessageList.BLUE + "%1$s",
        LocalizedMessageList.GRAY + "Usage: " + LocalizedMessageList.BLUE + "%1$s [command] [page] " + LocalizedMessageList.GRAY + "Display the given help page.",
        LocalizedMessageList.GRAY + "The argument " + LocalizedMessageList.BLUE + "page is optional. If not entered it",
        LocalizedMessageList.GRAY + "will always display the first help page.",
        LocalizedMessageList.GRAY + "If a command name is given it will display help on that command.",
    })
    HelpLongDescription;
    
}
