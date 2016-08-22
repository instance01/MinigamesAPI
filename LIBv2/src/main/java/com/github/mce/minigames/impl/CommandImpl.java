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

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;
import com.github.mce.minigames.impl.stubs.FalseStub;
import com.github.mce.minigames.impl.stubs.TrueStub;

/**
 * Implementation of command interface.
 * 
 * @author mepeisen
 */
public class CommandImpl implements CommandInterface
{
    
    /**
     * the command sender.
     */
    private final CommandSender   sender;
    
    /**
     * The main minigames plugin.
     */
    private final MinigamesPlugin plugin;
    
    /**
     * The original command.
     */
    private final Command         command;
    
    /**
     * The original label.
     */
    private final String          label;
    
    /**
     * The original command line arguments.
     */
    private final String[]        args;
    
    /**
     * Constructor to create the command.
     * 
     * @param sender
     *            the command sender.
     * @param plugin
     *            The main minigames plugin.
     * @param command
     *            the original command
     * @param label
     *            the command label
     * @param args
     *            the command arguments
     */
    public CommandImpl(CommandSender sender, MinigamesPlugin plugin, Command command, String label, String[] args)
    {
        this.sender = sender;
        this.plugin = plugin;
        this.command = command;
        this.label = label;
        this.args = args;
    }
    
    @Override
    public CommandSender getSender()
    {
        return this.sender;
    }
    
    @Override
    public ArenaPlayerInterface getPlayer()
    {
        if (this.getSender() instanceof Player)
        {
            return this.plugin.getPlayer((Player) this.getSender());
        }
        return null;
    }
    
    @Override
    public Command getCommand()
    {
        return this.command;
    }
    
    @Override
    public String getLabel()
    {
        return this.label;
    }

    @Override
    public CommandInterface consumeArgs(int count)
    {
        final String[] args2 = Arrays.copyOfRange(this.args, count, this.args.length);
        return new CommandImpl(this.sender, this.plugin, this.command, this.label, args2);
    }
    
    @Override
    public String[] getArgs()
    {
        return this.args;
    }
    
    @Override
    public MgOutgoingStubbing<CommandInterface> when(MgPredicate<CommandInterface> test) throws MinigameException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
