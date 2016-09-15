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

import java.util.Locale;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.Test;

import com.github.mce.minigames.api.locale.LocalizedConfigLine;
import com.github.mce.minigames.api.locale.MessageSeverityType;

/**
 * test case for {@link LocalizedConfigLine}
 * 
 * @author mepeisen
 */
public class LocalizedConfigLineTest
{
    
    /**
     * Tests the argument substitution
     */
    @Test
    public void testArgs()
    {
        final LocalizedConfigLine line = new LocalizedConfigLine();
        line.setUserMessages(Locale.ENGLISH, new String[]{"foo %2$s %1$s", "foo2 %2$s2 %1$s2"}); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo bar baz", "foo2 bar2 baz2"}, line.toUserMessageLine(Locale.GERMAN, "baz", "bar")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
    /**
     * Tests {@link LocalizedConfigLine#writeToConfig(org.bukkit.configuration.ConfigurationSection)} and {@link LocalizedConfigLine#readFromConfig(org.bukkit.configuration.ConfigurationSection)}
     */
    @Test
    public void testConfig()
    {
        final LocalizedConfigLine line = new LocalizedConfigLine();
        line.setUserMessages(Locale.ENGLISH, new String[]{"foo", "bar"}); //$NON-NLS-1$ //$NON-NLS-2$
        line.setUserMessages(Locale.GERMAN, new String[]{"foo2", "bar2"}); //$NON-NLS-1$ //$NON-NLS-2$
        line.setAdminMessages(Locale.ENGLISH, new String[]{"foo3", "bar3"}); //$NON-NLS-1$ //$NON-NLS-2$
        line.setAdminMessages(Locale.GERMAN, new String[]{});
        
        final ConfigurationSection section = new MemoryConfiguration();
        line.writeToConfig(section);
        
        assertEquals("en", section.getString("default_locale")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(2, section.getStringList("user.en").size()); //$NON-NLS-1$
        assertEquals("foo", section.getStringList("user.en").get(0)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("bar", section.getStringList("user.en").get(1)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(2, section.getStringList("user.de").size()); //$NON-NLS-1$
        assertEquals("foo2", section.getStringList("user.de").get(0)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("bar2", section.getStringList("user.de").get(1)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(2, section.getStringList("admin.en").size()); //$NON-NLS-1$
        assertEquals("foo3", section.getStringList("admin.en").get(0)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("bar3", section.getStringList("admin.en").get(1)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, section.getStringList("admin.de").size()); //$NON-NLS-1$
        
        final LocalizedConfigLine line2 = new LocalizedConfigLine();
        line2.readFromConfig(section);
        
        assertArrayEquals(new String[]{"foo", "bar"}, line2.toUserMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo2", "bar2"}, line2.toUserMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo3", "bar3"}, line2.toAdminMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo2", "bar2"}, line2.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests the {@link LocalizedConfigLine#toUserMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToUserMessageLine()
    {
        // empty message object
        final LocalizedConfigLine line = new LocalizedConfigLine();
        assertArrayEquals(new String[0], line.toUserMessageLine(Locale.GERMAN));
        
        // setting default locale
        line.setUserMessages(Locale.ENGLISH, new String[]{"foo", "bar"}); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toUserMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toUserMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
        
        // setting german
        line.setUserMessages(Locale.GERMAN, new String[]{"foo2", "bar2"}); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toUserMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo2", "bar2"}, line.toUserMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
        
        // clearing german
        line.setUserMessages(Locale.GERMAN, null);
        assertArrayEquals(new String[]{"foo", "bar"}, line.toUserMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toUserMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests the {@link LocalizedConfigLine#toAdminMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessageLine()
    {
        // empty message object
        final LocalizedConfigLine line = new LocalizedConfigLine();
        assertArrayEquals(new String[0], line.toAdminMessageLine(Locale.GERMAN));
        
        // setting default locale
        line.setAdminMessages(Locale.ENGLISH, new String[]{"foo", "bar"}); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
        
        // setting german
        line.setAdminMessages(Locale.GERMAN, new String[]{"foo2", "bar2"}); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo2", "bar2"}, line.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
        
        // clearing german
        line.setAdminMessages(Locale.GERMAN, null);
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests the {@link LocalizedConfigLine#toAdminMessageLine(java.util.Locale, java.io.Serializable...)}
     */
    @Test
    public void testToAdminMessageLineReturningUserMsg()
    {
        // empty message object
        final LocalizedConfigLine line = new LocalizedConfigLine();
        line.setUserMessages(Locale.ENGLISH, new String[]{"foo", "bar"}); //$NON-NLS-1$ //$NON-NLS-2$
        line.setAdminMessages(Locale.GERMAN, new String[]{"foo2", "bar2"}); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.ENGLISH)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"foo2", "bar2"}, line.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$

        line.setAdminMessages(Locale.GERMAN, new String[]{});
        assertArrayEquals(new String[]{"foo", "bar"}, line.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$

        line.setUserMessages(Locale.ENGLISH, new String[]{"foo3", "bar3"}); //$NON-NLS-1$ //$NON-NLS-2$
        line.setAdminMessages(Locale.ENGLISH, new String[]{});
        assertArrayEquals(new String[]{"foo3", "bar3"}, line.toAdminMessageLine(Locale.GERMAN)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests standard methods.
     */
    @Test
    public void testMe()
    {
        final LocalizedConfigLine line = new LocalizedConfigLine();
        
        assertFalse(line.isSingleLine());
        assertTrue(line.isMultiLine());
        
        assertEquals(MessageSeverityType.Information, line.getSeverity());
    }
    
    /**
     * Tests {@link LocalizedConfigLine#toAdminMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test(expected = IllegalStateException.class)
    public void testToAdminMsg()
    {
        new LocalizedConfigLine().toAdminMessage(Locale.GERMAN);
    }
    
    /**
     * Tests {@link LocalizedConfigLine#toUserMessage(java.util.Locale, java.io.Serializable...)}
     */
    @Test(expected = IllegalStateException.class)
    public void testToUserMsg()
    {
        new LocalizedConfigLine().toUserMessage(Locale.GERMAN);
    }
    
}
