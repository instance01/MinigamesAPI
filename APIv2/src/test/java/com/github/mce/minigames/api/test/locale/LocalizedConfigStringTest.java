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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.Test;

import com.github.mce.minigames.api.locale.LocalizedConfigString;
import com.github.mce.minigames.api.locale.MessageSeverityType;

/**
 * test case for {@link LocalizedConfigString}
 * 
 * @author mepeisen
 */
public class LocalizedConfigStringTest
{
    
    /**
     * Tests the argument substitution
     */
    @Test
    public void testArgs()
    {
        final LocalizedConfigString line = new LocalizedConfigString();
        line.setUserMessage(Locale.ENGLISH, "foo %2$s %1$s"); //$NON-NLS-1$
        assertEquals("foo bar baz", line.toUserMessage(Locale.GERMAN, "baz", "bar")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests {@link LocalizedConfigString#writeToConfig(org.bukkit.configuration.ConfigurationSection)} and {@link LocalizedConfigString#readFromConfig(org.bukkit.configuration.ConfigurationSection)}
     */
    @Test
    public void testConfig()
    {
        final LocalizedConfigString line = new LocalizedConfigString();
        line.setUserMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        line.setUserMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        line.setAdminMessage(Locale.ENGLISH, "foo3"); //$NON-NLS-1$
        line.setAdminMessage(Locale.GERMAN, ""); //$NON-NLS-1$
        
        final ConfigurationSection section = new MemoryConfiguration();
        line.writeToConfig(section);
        
        assertEquals("en", section.getString("default_locale")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("foo", section.getString("user.en")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("foo2", section.getString("user.de")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("foo3", section.getString("admin.en")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("", section.getString("admin.de")); //$NON-NLS-1$ //$NON-NLS-2$
        
        final LocalizedConfigString line2 = new LocalizedConfigString();
        line2.readFromConfig(section);
        
        assertEquals("foo", line2.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line2.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        assertEquals("foo3", line2.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line2.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests the {@link LocalizedConfigString#toUserMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToUserMessage()
    {
        // empty message object
        final LocalizedConfigString line = new LocalizedConfigString();
        assertNull(line.toUserMessage(Locale.GERMAN));
        
        // setting default locale
        line.setUserMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // setting german
        line.setUserMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // clearing german
        line.setUserMessage(Locale.GERMAN, null);
        assertEquals("foo", line.toUserMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toUserMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests the {@link LocalizedConfigString#toAdminMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessage()
    {
        // empty message object
        final LocalizedConfigString line = new LocalizedConfigString();
        assertNull(line.toAdminMessage(Locale.GERMAN));
        
        // setting default locale
        line.setAdminMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // setting german
        line.setAdminMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
        
        // clearing german
        line.setAdminMessage(Locale.GERMAN, null);
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests the {@link LocalizedConfigString#toAdminMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessageReturningUserMsg()
    {
        // empty message object
        final LocalizedConfigString line = new LocalizedConfigString();
        line.setUserMessage(Locale.ENGLISH, "foo"); //$NON-NLS-1$
        line.setAdminMessage(Locale.GERMAN, "foo2"); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.ENGLISH)); //$NON-NLS-1$
        assertEquals("foo2", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$

        line.setAdminMessage(Locale.GERMAN, ""); //$NON-NLS-1$
        assertEquals("foo", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$

        line.setUserMessage(Locale.ENGLISH, "foo3"); //$NON-NLS-1$
        line.setAdminMessage(Locale.ENGLISH, ""); //$NON-NLS-1$
        assertEquals("foo3", line.toAdminMessage(Locale.GERMAN)); //$NON-NLS-1$
    }
    
    /**
     * Tests standard methods.
     */
    @Test
    public void testMe()
    {
        final LocalizedConfigString line = new LocalizedConfigString();
        
        assertTrue(line.isSingleLine());
        assertFalse(line.isMultiLine());
        
        assertEquals(MessageSeverityType.Information, line.getSeverity());
    }
    
    /**
     * Tests {@link LocalizedConfigString#toAdminMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test(expected = IllegalStateException.class)
    public void testToAdminMsgLine()
    {
        new LocalizedConfigString().toAdminMessageLine(Locale.GERMAN);
    }
    
    /**
     * Tests {@link LocalizedConfigString#toUserMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test(expected = IllegalStateException.class)
    public void testToUserMsgLine()
    {
        new LocalizedConfigString().toUserMessageLine(Locale.GERMAN);
    }
    
}
