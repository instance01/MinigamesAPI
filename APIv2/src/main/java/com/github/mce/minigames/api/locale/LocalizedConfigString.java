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

package com.github.mce.minigames.api.locale;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.github.mce.minigames.api.config.Configurable;

/**
 * A localized string placed within configuration files (not from messages.yml).
 * 
 * @author mepeisen
 */
public class LocalizedConfigString implements Configurable, LocalizedMessageInterface
{
    
    /**
     * serial version uid.
     */
    private static final long   serialVersionUID = -2121306517414707420L;
    
    /**
     * The default locale.
     */
    private Locale              defaultLocale    = Locale.ENGLISH;
    
    /**
     * The user strings per locale.
     */
    private Map<Locale, String> userStrings      = new HashMap<>();
    
    /**
     * The admin strings per locale.
     */
    private Map<Locale, String> adminStrings     = new HashMap<>();
    
    /**
     * Sets the user message.
     * @param locale locale to use
     * @param message message to be used.
     */
    public void setUserMessage(Locale locale, String message)
    {
        if (message == null)
        {
            this.userStrings.remove(locale);
        }
        else
        {
            this.userStrings.put(locale, message);
        }
    }
    
    /**
     * Sets the admin message.
     * @param locale locale to use
     * @param message message array to be used.
     */
    public void setAdminMessage(Locale locale, String message)
    {
        if (message == null)
        {
            this.adminStrings.remove(locale);
        }
        else
        {
            this.adminStrings.put(locale, message);
        }
    }
    
    @Override
    public void readFromConfig(ConfigurationSection section)
    {
        if (section.contains("default_locale")) //$NON-NLS-1$
        {
            this.defaultLocale = new Locale(section.getString("default_locale"), ""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.userStrings.clear();
        if (section.contains("user")) //$NON-NLS-1$
        {
            for (final String key : section.getConfigurationSection("user").getKeys(false)) //$NON-NLS-1$
            {
                final Locale locale = new Locale(key, ""); //$NON-NLS-1$
                this.userStrings.put(locale, section.getString("user." + key)); //$NON-NLS-1$
            }
        }
        this.adminStrings.clear();
        if (section.contains("admin")) //$NON-NLS-1$
        {
            for (final String key : section.getConfigurationSection("admin").getKeys(false)) //$NON-NLS-1$
            {
                final Locale locale = new Locale(key, ""); //$NON-NLS-1$
                this.adminStrings.put(locale, section.getString("admin." + key)); //$NON-NLS-1$
            }
        }
    }
    
    @Override
    public void writeToConfig(ConfigurationSection section)
    {
        section.set("default_locale", this.defaultLocale.toString()); //$NON-NLS-1$
        for (final Map.Entry<Locale, String> userStr : this.userStrings.entrySet())
        {
            section.set("user." + userStr.getKey().toString(), userStr.getValue()); //$NON-NLS-1$
        }
        for (final Map.Entry<Locale, String> userStr : this.adminStrings.entrySet())
        {
            section.set("admin." + userStr.getKey().toString(), userStr.getValue()); //$NON-NLS-1$
        }
    }
    
    @Override
    public boolean isSingleLine()
    {
        return true;
    }
    
    @Override
    public boolean isMultiLine()
    {
        return false;
    }
    
    @Override
    public MessageSeverityType getSeverity()
    {
        return MessageSeverityType.Information;
    }
    
    @Override
    public String toUserMessage(Locale locale, Serializable... args)
    {
        String smsg = this.userStrings.get(locale);
        if (smsg == null)
        {
            smsg = this.userStrings.get(this.defaultLocale);
        }
        if (smsg == null)
        {
            return null;
        }
        return String.format(locale, smsg, (Object[]) MessageTool.convertArgs(locale, false, args));
    }
    
    @Override
    public String[] toUserMessageLine(Locale locale, Serializable... args)
    {
        throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
    }
    
    @Override
    public String toAdminMessage(Locale locale, Serializable... args)
    {
        String smsg = this.adminStrings.get(locale);
        if (smsg == null || smsg.length() == 0)
        {
            smsg = this.userStrings.get(locale);
        }
        if (smsg == null)
        {
            smsg = this.adminStrings.get(this.defaultLocale);
        }
        if (smsg == null || smsg.length() == 0)
        {
            smsg = this.userStrings.get(this.defaultLocale);
        }
        if (smsg == null)
        {
            return null;
        }
        return String.format(locale, smsg, (Object[]) MessageTool.convertArgs(locale, true, args));
    }
    
    @Override
    public String[] toAdminMessageLine(Locale locale, Serializable... args)
    {
        throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
    }
    
}
