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

package com.github.mce.minigames.api.test.cmd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;
import com.github.mce.minigames.api.perms.CommonPermissions;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.FalseStub;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;
import com.github.mce.minigames.api.util.function.TrueStub;

/**
 * Test case for {@link CommandInterface}.
 * 
 * @author mepeisen
 */
public class CommandInterfaceTest
{
    
    /** the messages. */
    private MessagesConfigInterface messages;
    /** library. */
    private MglibInterface lib;

    /**
     * Some setup.
     */
    @Before
    public void setup()
    {
        this.lib = mock(MglibInterface.class);
        Whitebox.setInternalState(MglibInterface.INSTANCE.class, "CACHED", this.lib); //$NON-NLS-1$
        when(this.lib.resolveContextVar(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(0, String.class);
            }
        });
        this.messages = mock(MessagesConfigInterface.class);
        when(this.lib.getMessagesFromMsg(anyObject())).thenReturn(this.messages);
    }
    
    /**
     * Tests {@link CommandInterface#isOp()}
     */
    @Test
    public void testIsOp()
    {
        final CommandSender senderNormal = mock(CommandSender.class);
        when(senderNormal.isOp()).thenReturn(false);
        final CommandSender senderOp = mock(CommandSender.class);
        when(senderOp.isOp()).thenReturn(true);
        
        assertFalse(new Command(senderNormal, null, null, null, null).isOp());
        assertTrue(new Command(senderOp, null, null, null, null).isOp());
    }
    
    /**
     * Tests {@link CommandInterface#isPlayer()}
     * @throws MinigameException 
     */
    @Test
    public void testIsPlayer() throws MinigameException
    {
        final CommandSender senderNormal = mock(CommandSender.class);
        final Player senderPlayer = mock(Player.class);
        
        assertFalse(CommandInterface.isPlayer().test(new Command(senderNormal, null, null, null, null)));
        assertTrue(CommandInterface.isPlayer().test(new Command(senderPlayer, null, null, null, null)));
    }
    
    /**
     * Tests {@link CommandInterface#permThrowException(com.github.mce.minigames.api.perms.PermissionsInterface, String)}
     * @throws MinigameException 
     */
    @Test
    public void testPermThrowExceptionOK() throws MinigameException
    {
        final ArenaPlayerInterface playerTrue = mock(ArenaPlayer.class);
        when(playerTrue.checkPermission(anyObject())).thenReturn(true);
        when(playerTrue.when(anyObject())).thenCallRealMethod();
        
        final Command command = new Command(null, playerTrue, null, null, null);
        command.permThrowException(CommonPermissions.Info, null);
    }
    
    /**
     * Tests {@link CommandInterface#permThrowException(com.github.mce.minigames.api.perms.PermissionsInterface, String)}
     * @throws MinigameException 
     */
    @Test(expected = MinigameException.class)
    public void testPermThrowExceptionFailed() throws MinigameException
    {
        final ArenaPlayerInterface playerFalse = mock(ArenaPlayer.class);
        when(playerFalse.checkPermission(anyObject())).thenReturn(false);
        when(playerFalse.when(anyObject())).thenCallRealMethod();
        
        final Command command = new Command(null, playerFalse, null, null, null);
        command.permThrowException(CommonPermissions.Info, null);
    }
    
    /**
     * Tests {@link CommandInterface#send(com.github.mce.minigames.api.locale.LocalizedMessageInterface, java.io.Serializable...)}
     */
    @Test
    public void testSend()
    {
        final CommandSender sender = mock(CommandSender.class);
        final Command command = new Command(sender, null, null, null, null);
        when(this.messages.getString(anyObject(), anyObject(), anyObject())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(2, String.class);
            }
        });
        
        command.send(CommonMessages.HelpHeader);
        
        verify(sender, times(1)).sendMessage("§fhelp"); //$NON-NLS-1$
    }
    
    /**
     * arena player dummy impl.
     * @author mepeisen
     */
    private static abstract class ArenaPlayer implements ArenaPlayerInterface
    {

        @Override
        public MgOutgoingStubbing<ArenaPlayerInterface> when(MgPredicate<ArenaPlayerInterface> test) throws MinigameException
        {
            if (test.test(this))
            {
                return new TrueStub<>(this);
            }
            return new FalseStub<>(this);
        }
        
    }
    
    /**
     * Sample Command impl
     */
    private static final class Command implements CommandInterface
    {
        
        /** sender */
        private final CommandSender sender;
        /** player */
        private final ArenaPlayerInterface player;
        /** command */
        private final org.bukkit.command.Command command;
        /** label */
        private final String label;
        /** args */
        private final String[] args;

        /**
         * @param sender
         * @param player
         * @param command
         * @param label
         * @param args
         */
        public Command(CommandSender sender, ArenaPlayerInterface player, org.bukkit.command.Command command, String label, String[] args)
        {
            this.sender = sender;
            this.player = player;
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
            return this.player;
        }

        @Override
        public org.bukkit.command.Command getCommand()
        {
            return this.command;
        }

        @Override
        public String getLabel()
        {
            return this.label;
        }

        @Override
        public String[] getArgs()
        {
            return this.args;
        }

        @Override
        public CommandInterface consumeArgs(int count)
        {
            // dummy
            return null;
        }

        @Override
        public String getCommandPath()
        {
            // dummy
            return null;
        }

        @Override
        public Locale getLocale()
        {
            // dummy
            return Locale.ENGLISH;
        }

        @Override
        public MgOutgoingStubbing<CommandInterface> when(MgPredicate<CommandInterface> test) throws MinigameException
        {
            // dummy
            return null;
        }
        
    }
    
}
