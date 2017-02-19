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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.cmd.AbstractCompositeCommandHandler;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.HelpCommandHandler;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mgapi.api.LibState;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.impl.MglibMessages;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * Common information on minigames library
 * 
 * @author mepeisen
 */
public class InfoCommand extends AbstractCompositeCommandHandler implements SubCommandHandlerInterface
{
    
    /**
     * Constructor to add sub commands
     */
    public InfoCommand()
    {
        this.subCommands.put("help", new HelpCommandHandler((AbstractCompositeCommandHandler) (this))); //$NON-NLS-1$
        this.subCommands.put("minigames", new InfoMinigamesCommand()); //$NON-NLS-1$
        this.subCommands.put("extensions", new InfoExtensionsCommand()); //$NON-NLS-1$
        this.subCommands.put("minigame", new InfoMinigameCommand()); //$NON-NLS-1$
        // this.subCommands.put("extension", new InfoExtensionCommand()); //$NON-NLS-1$
    }
    
    @Override
    protected boolean pre(CommandInterface command) throws McException
    {
        return true;
    }

    @Override
    public void handle(CommandInterface cmd) throws McException
    {
        cmd.permOpThrowException(MglibPerms.CommandInfo, cmd.getCommandPath());
        
        if (cmd.getArgs().length > 0)
        {
            // pass to composite
            super.handle(cmd);
            return;
        }
        
        // no argument, let us display info
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        final McLibInterface mclib = McLibInterface.instance();
        
        final String minigamesVersion = getPluginVersion("MinigamesLib"); //$NON-NLS-1$
        final String mclibVersion = getPluginVersion("mclib"); //$NON-NLS-1$
        final String bukkitVersion = Bukkit.getBukkitVersion();
        
        // TODO get premium flag
        final boolean isPremium = false;
        final LocalizedMessageInterface mode = isPremium ? MglibMessages.ModePremium : MglibMessages.ModeStandard;
        final LocalizedMessageInterface state = toString(mglib.getState());
       
        cmd.send(Messages.CommandOutput,
                cmd.getCommandPath(),
                mclib.getMinecraftVersion().toString(),
                minigamesVersion,
                mode,
                mglib.debug() ? MglibMessages.FlagTrue : MglibMessages.FlagFalse,
                mglib.getApiVersion(),
                mclib.getApiVersion(),
                state,
                mclibVersion,
                bukkitVersion
                );
    }

    /**
     * Returns the string message for given lib state
     * @param state
     * @return string message
     */
    private LocalizedMessageInterface toString(LibState state)
    {
        switch (state)
        {
            default:
            case Initializing:
                return MglibMessages.StateInitializing;
            case Running:
                return MglibMessages.StateRunning;
            case Sleeping:
                return MglibMessages.StateSleeping;
            case Terminating:
                return MglibMessages.StateTerminating;
        }
    }
    
    /**
     * Returns plugin version for given plugin.
     * @param name
     * @return the plugin version.
     */
    private String getPluginVersion(String name)
    {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null || plugin.getDescription() == null) return "?"; //$NON-NLS-1$
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface cmd)
    {
        return Messages.Description;
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface cmd)
    {
        return Messages.ShortDescription;
    }

    @Override
    protected void sendUsage(CommandInterface cmd)
    {
        cmd.send(Messages.Usage);
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_info")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 info
         */
        @LocalizedMessage(defaultMessage = "Prints common information on minigames library.")
        @MessageComment({"Short description of /mg2 info"})
        ShortDescription,
        
        /**
         * Long description of /mg2 info
         */
        @LocalizedMessage(defaultMessage = "Prints common information on minigames library.")
        @MessageComment({"Long description of /mg2 info"})
        Description,
        
        /**
         * The command output of /mg2 info
         * 
         * <p>Arguments:</p>
         * 
         * <ol>
         * <li>String: current command path</li>
         * <li>String: spigot version</li>
         * <li>String: minigames version</li>
         * <li>String: mode</li>
         * <li>String: debug</li>
         * <li>Number: api version</li>
         * <li>Number: mclib api version</li>
         * <li>String: minigames lib state</li>
         * <li>String: mclib version</li>
         * <li>String: bukkit version</li>
         * </ol>
         */
        @LocalizedMessageList({
            "minigames lib version %3$s (%4$s/%10$s)",
            "state: %8$s",
            "api version: %6$d",
            "mclib version: %7$d (%9$s)",
            "running on minecraft %2$s",
            "debugging: %5$s",
            "----------",
            "Run for additional information:",
            "  " + LocalizedMessage.CODE_COLOR + "%1$s extensions " + LocalizedMessage.INFORMATION_COLOR + " to list the extensions.",
            "  " + LocalizedMessage.CODE_COLOR + "%1$s minigames " + LocalizedMessage.INFORMATION_COLOR + " to list the minigames."
        })
        @MessageComment(value = {
            "The command output of /mg2 info"
        },args = {
                @Argument("current command path"),
                @Argument("spigot version name"),
                @Argument("minigames version"),
                @Argument("minigames mode"),
                @Argument("debug flag"),
                @Argument(value="api version", type="number"),
                @Argument(value="mclib api version", type="number"),
                @Argument("minigames lib state"),
                @Argument("mclib version"),
                @Argument("bukkit version"),
                })
        CommandOutput,
        
        /**
         * Usage for command /mg2 info
         */
        @LocalizedMessage(defaultMessage = "Enter " + LocalizedMessage.CODE_COLOR + "/mg2 info help" + LocalizedMessage.INFORMATION_COLOR + " for detailed help")
        @MessageComment({"Usage for command /mg2 info"})
        Usage
        
    }
    
}
