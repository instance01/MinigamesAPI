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

package com.github.mce.minigames.api.test.locale;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.locale.LocalizedMessage;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.LocalizedMessageList;
import com.github.mce.minigames.api.locale.LocalizedMessages;
import com.github.mce.minigames.api.locale.MessageSeverityType;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;

/**
 * Test for {@link LocalizedMessageInterface}
 * 
 * @author mepeisen
 */
public class LocalizedMessageInterfaceTest
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
                return "core." + invocation.getArgumentAt(0, String.class); //$NON-NLS-1$
            }
        });
        this.messages = mock(MessagesConfigInterface.class);
        when(this.lib.getMessagesFromMsg(anyObject())).thenReturn(this.messages);
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#isSingleLine()}
     */
    @Test
    public void testIsSingleLine()
    {
        assertTrue(TestMessages.FooSingleLine.isSingleLine());
        assertFalse(TestMessages.FooMultiLine.isSingleLine());
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#isMultiLine()}
     */
    @Test
    public void testIsMultiLine()
    {
        assertFalse(TestMessages.FooSingleLine.isMultiLine());
        assertTrue(TestMessages.FooMultiLine.isMultiLine());
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#getSeverity}
     */
    @Test
    public void testGetSeverity()
    {
        assertEquals(MessageSeverityType.Information, TestMessages.FooSingleLine.getSeverity());
        assertEquals(MessageSeverityType.Information, TestMessages.FooMultiLine.getSeverity());
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#toUserMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToUserMessage()
    {
        when(this.messages.getString(Locale.GERMAN, "msg.FooSingleLine", "FooSingleLine")).thenReturn("FooSingleLine2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("FooSingleLine2", TestMessages.FooSingleLine.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#toAdminMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessage()
    {
        when(this.messages.getAdminString(Locale.GERMAN, "msg.FooSingleLine", "")).thenReturn("FooSingleLineAdmin"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("FooSingleLineAdmin", TestMessages.FooSingleLine.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#toUserMessageLine(java.util.Locale, java.io.Serializable...)}
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     */
    @Test
    public void testToUserMessageLine() throws NoSuchFieldException, SecurityException
    {
        final String[] defValue = TestMessages.class.getField("FooMultiLine").getAnnotation(LocalizedMessageList.class).value(); //$NON-NLS-1$
        when(this.messages.getStringList(Locale.GERMAN, "msg.FooMultiLine", defValue)).thenReturn(new String[]{"FooMultiLine 1b", "FooMultiLine 2b"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertArrayEquals(new String[]{"FooMultiLine 1b", "FooMultiLine 2b"}, TestMessages.FooMultiLine.toUserMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests {@link LocalizedMessageInterface#toAdminMessageLine(java.util.Locale, java.io.Serializable...)}
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     */
    @Test
    public void testToAdminMessageLine() throws NoSuchFieldException, SecurityException
    {
        when(this.messages.getAdminStringList(Locale.GERMAN, "msg.FooMultiLine", null)).thenReturn(new String[]{"FooMultiLine 1c", "FooMultiLine 2c"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertArrayEquals(new String[]{"FooMultiLine 1c", "FooMultiLine 2c"}, TestMessages.FooMultiLine.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Helper enum
     */
    @LocalizedMessages("msg")
    public enum TestMessages implements LocalizedMessageInterface
    {
        
        /** single line */
        @LocalizedMessage(defaultMessage = "FooSingleLine")
        FooSingleLine,
        
        /** multi line */
        @LocalizedMessageList({"FooMultiLine 1", "FooMultiLine 2"})
        FooMultiLine,
        
    }
    
}
