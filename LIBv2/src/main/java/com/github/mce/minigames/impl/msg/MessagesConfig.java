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

package com.github.mce.minigames.impl.msg;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.locale.LocalizedMessage;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.LocalizedMessages;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;

/**
 * Implementation of messages configuration
 * 
 * @author mepeisen
 */
public class MessagesConfig implements MessagesConfigInterface
{
    
    // TODO check if there are messages that are not needed any more.
    // TODO user defined messages for scripting, shops, classes etc.
    
    /** the file configuration. */
    private FileConfiguration config = null;
    /** the yml file. */
    private File              file   = null;
    /** the java plugin. */
    private JavaPlugin        plugin = null;
    
    /** the defaults for this messages. */
    private List<LocalizedMessageInterface> defaults;
    
    /**
     * Constructor to create the messages config.
     * 
     * @param plugin
     *            java plugin
     */
    public MessagesConfig(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    /**
     * Initializes the messages with given localized messages.
     * @param msgs
     */
    public void initMessage(List<LocalizedMessageInterface> msgs)
    {
        this.defaults = msgs;
    }
    
    @Override
    public String getString(Locale locale, String path, String defaultValue)
    {
        final FileConfiguration config1 = this.getConfig();
        String result = config1.getString(path + ".user." + locale.toString()); //$NON-NLS-1$
        if (result == null)
        {
            final String defaultLocale = config1.getString(path + ".default_locale"); //$NON-NLS-1$
            if (defaultLocale != null)
            {
                result = config1.getString(path + ".user." + defaultLocale); //$NON-NLS-1$
            }
        }
        return result == null ? defaultValue : result;
    }
    
    @Override
    public String getAdminString(Locale locale, String path, String defaultValue)
    {
        final FileConfiguration config1 = this.getConfig();
        String result = config1.getString(path + ".admin." + locale.toString()); //$NON-NLS-1$
        if (result == null)
        {
            final String defaultLocale = config1.getString(path + ".default_locale"); //$NON-NLS-1$
            if (defaultLocale != null)
            {
                result = config1.getString(path + ".admin." + defaultLocale); //$NON-NLS-1$
            }
        }
        return result == null ? defaultValue : result;
    }
    
    /**
     * Returns the file configuration.
     * 
     * @return file configuration.
     */
    public FileConfiguration getConfig()
    {
        if (this.config == null)
        {
            this.reloadConfig();
        }
        return this.config;
    }
    
    /**
     * Saves the configuration.
     */
    public void saveConfig()
    {
        if (this.config == null || this.file == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.file);
        }
        catch (final IOException ex)
        {
            this.plugin.getLogger().log(Level.WARNING, "Cannot save messages configuration", ex); //$NON-NLS-1$
        }
    }
    
    /**
     * Reloads the configuration file.
     */
    public void reloadConfig()
    {
        if (this.file == null)
        {
            this.file = new File(this.plugin.getDataFolder(), "messages.yml"); //$NON-NLS-1$
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        
        // add the defaults.
        for (final LocalizedMessageInterface msg : this.defaults)
        {
            try
            {
                final LocalizedMessages clazzDef = msg.getClass().getAnnotation(LocalizedMessages.class);
                final LocalizedMessage valueDef = msg.getClass().getDeclaredField(((Enum<?>)msg).name()).getAnnotation(LocalizedMessage.class);
                if (clazzDef == null || valueDef == null)
                {
                    throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
                }
                final String path =  clazzDef.value() + "." + ((Enum<?>)msg).name(); //$NON-NLS-1$
                this.config.addDefault(path + ".default_locale", clazzDef.defaultLocale()); //$NON-NLS-1$
                this.config.addDefault(path + ".user." + clazzDef.defaultLocale(), valueDef.defaultMessage()); //$NON-NLS-1$
                if (valueDef.defaultAdminMessage().length() > 0)
                {
                    this.config.addDefault(path + ".admin." + clazzDef.defaultLocale(), valueDef.defaultAdminMessage()); //$NON-NLS-1$
                }
            }
            catch (NoSuchFieldException ex)
            {
                throw new IllegalStateException(ex);
            }
        }
        
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
}
