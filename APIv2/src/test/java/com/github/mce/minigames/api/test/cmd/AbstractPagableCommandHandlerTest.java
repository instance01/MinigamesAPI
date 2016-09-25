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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.CommandSender;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.cmd.AbstractPagableCommandHandler;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;
import com.github.mce.minigames.api.util.function.MgOutgoingStubbing;
import com.github.mce.minigames.api.util.function.MgPredicate;

/**
 * Tests for {@link AbstractPagableCommandHandler}
 * 
 * @author mepeisen
 *
 */
public class AbstractPagableCommandHandlerTest
{
    
    /**
     * tests the first page
     * @throws MinigameException
     */
    @Test
    public void testFirstPage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{});
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 1, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 1", 1); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 2", 2); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 3", 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 4", 4); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 5", 5); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 6", 6); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 7", 7); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 8", 8); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 9", 9); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 10", 10); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the second page
     * @throws MinigameException
     */
    @Test
    public void testFirstPage2() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"1"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 1, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 1", 1); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 2", 2); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 3", 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 4", 4); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 5", 5); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 6", 6); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 7", 7); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 8", 8); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 9", 9); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 10", 10); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the second page
     * @throws MinigameException
     */
    @Test
    public void testSecondPage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"2"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 2, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 11", 11); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 12", 12); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 13", 13); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 14", 14); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 15", 15); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 16", 16); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 17", 17); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 18", 18); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 19", 19); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 20", 20); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the third page
     * @throws MinigameException
     */
    @Test
    public void testThirdPage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"3"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "DUMMY-HEADER", 3, 3); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 21", 21); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 22", 22); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 23", 23); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 24", 24); //$NON-NLS-1$
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedLine, "Line 25", 25); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the negative page
     * @throws MinigameException
     */
    @Test
    public void testNegativePage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"-1"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedWrongPageNum, -1, 3);
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the zero page
     * @throws MinigameException
     */
    @Test
    public void testZeroPage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"0"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedWrongPageNum, 0, 3);
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the fourth page
     * @throws MinigameException
     */
    @Test
    public void testFourthPage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"4"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedWrongPageNum, 4, 3);
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the number format exception
     * @throws MinigameException
     */
    @Test
    public void testNFE() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final DummyHandler dummy = new DummyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"foo"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedInvalidNumber);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PageUsage, "path"); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the first page
     * @throws MinigameException
     */
    @Test
    public void testEmptyFirstPage() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final EmptyHandler dummy = new EmptyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{});
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "EMPTY-HEADER", 1, 1); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
    }
    
    /**
     * tests the first page
     * @throws MinigameException
     */
    @Test
    public void testEmptyFirstPage2() throws MinigameException
    {
        final CommandSender sender = mock(CommandSender.class);
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final EmptyHandler dummy = new EmptyHandler();
        final Command cmd = new Command(sender, player, null, null, new String[]{"1"}); //$NON-NLS-1$
        
        dummy.handle(cmd);
        
        InOrder inOrder = Mockito.inOrder(player);
        inOrder.verify(player, times(1)).sendMessage(CommonMessages.PagedHeader, "EMPTY-HEADER", 1, 1); //$NON-NLS-1$
        inOrder.verifyNoMoreInteractions();
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
            return "path"; //$NON-NLS-1$
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
    
    /**
     * Test Helper
     */
    private static class DummyHandler extends AbstractPagableCommandHandler
    {
        
        /**
         * Constructor
         */
        public DummyHandler()
        {
            // empty
        }

        @Override
        protected int getLineCount(CommandInterface command)
        {
            return 25;
        }

        @Override
        protected Serializable getHeader(CommandInterface command)
        {
            return "DUMMY-HEADER"; //$NON-NLS-1$
        }

        @Override
        protected Serializable[] getLines(CommandInterface command, int start, int count)
        {
            final List<Serializable> result = new ArrayList<>();
            for (int i = start; i < 25 && result.size() < count; i++)
            {
                result.add("Line " + (i + 1)); //$NON-NLS-1$
            }
            return result.toArray(new Serializable[result.size()]);
        }
        
    }
    
    /**
     * Test Helper
     */
    private static class EmptyHandler extends AbstractPagableCommandHandler
    {
        
        /**
         * Constructor
         */
        public EmptyHandler()
        {
            // empty
        }

        @Override
        protected int getLineCount(CommandInterface command)
        {
            return 0;
        }

        @Override
        protected Serializable getHeader(CommandInterface command)
        {
            return "EMPTY-HEADER"; //$NON-NLS-1$
        }

        @Override
        protected Serializable[] getLines(CommandInterface command, int start, int count)
        {
            return new Serializable[0];
        }
        
    }
    
}
