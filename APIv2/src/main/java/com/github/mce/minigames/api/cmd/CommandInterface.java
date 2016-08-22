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

package com.github.mce.minigames.api.cmd;

import java.io.Serializable;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;

/**
 * Interface for commands passed to command handler.
 * 
 * @author mepeisen
 */
public interface CommandInterface
{
    
    /**
     * Returns the bukkit command sender.
     * 
     * @return bukkit command sender.
     */
    CommandSender getSender();
    
    /**
     * Returns the arena player for this command/ the command sender.
     * 
     * @return the arena player or {@code null} if the sender is not a regular bukkit player.
     */
    ArenaPlayerInterface getPlayer();
    
    /**
     * Returns the command.
     * 
     * @return command
     */
    Command getCommand();
    
    /**
     * Returns the label
     * 
     * @return label
     */
    String getLabel();
    
    /**
     * Returns the command arguments
     * 
     * @return arguments.
     */
    String[] getArgs();
    
    /**
     * Returns a new command interface consuming given amount of arguments.
     * 
     * @param count
     *            number of arguments to consume
     * @return new command interface containing remaining arguments.
     */
    CommandInterface consumeArgs(int count);

    /**
     * Returns the command path being used before the arguments.
     * @return current command path.
     */
    String getCommandPath();
    
    /**
     * Sends a message to command sender
     * @param msg message to send
     * @param args message arguments
     */
    default void send(LocalizedMessageInterface msg, Serializable... args)
    {
        if (this.getPlayer() != null)
        {
            this.getPlayer().sendMessage(msg, args);
        }
        else
        {
            final Locale locale = Locale.ENGLISH;
            final boolean isAdmin = this.getSender().isOp();
            final String msg2 = msg.toArg(args).apply(locale, isAdmin);
            switch (msg.getSeverity())
            {
                default:
                case Error:
                    this.getSender().sendMessage(ChatColor.DARK_RED + msg2);
                    break;
                case Information:
                    this.getSender().sendMessage(ChatColor.WHITE + msg2);
                    break;
                case Loser:
                    this.getSender().sendMessage(ChatColor.RED + msg2);
                    break;
                case Success:
                    this.getSender().sendMessage(ChatColor.GREEN + msg2);
                    break;
                case Warning:
                    this.getSender().sendMessage(ChatColor.YELLOW + msg2);
                    break;
                case Winner:
                    this.getSender().sendMessage(ChatColor.GOLD + msg2);
                    break;
            }

        }
    }
    
    /**
     * Checks this command for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the command matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws MinigameException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    MgOutgoingStubbing<CommandInterface> when(MgPredicate<CommandInterface> test) throws MinigameException;
    
    /**
     * Returns a test function to check if the command sender is a player.
     * 
     * @return predicate to return {@code true} if the command sender is a player.
     */
    static MgPredicate<CommandInterface> isPlayer()
    {
        return (cmd) -> cmd.getSender() instanceof Player;
    }
    
}
