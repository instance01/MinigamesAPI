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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.api.ContextHandlerInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.MinigamePluginInterface;
import com.github.mce.minigames.api.PluginProviderInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.arena.ArenaTypeBuilderInterface;
import com.github.mce.minigames.api.arena.ArenaTypeDeclarationInterface;
import com.github.mce.minigames.api.arena.ArenaTypeInterface;
import com.github.mce.minigames.api.config.ConfigurationBool;
import com.github.mce.minigames.api.config.ConfigurationBoolList;
import com.github.mce.minigames.api.config.ConfigurationByte;
import com.github.mce.minigames.api.config.ConfigurationByteList;
import com.github.mce.minigames.api.config.ConfigurationCharacter;
import com.github.mce.minigames.api.config.ConfigurationCharacterList;
import com.github.mce.minigames.api.config.ConfigurationColor;
import com.github.mce.minigames.api.config.ConfigurationDouble;
import com.github.mce.minigames.api.config.ConfigurationDoubleList;
import com.github.mce.minigames.api.config.ConfigurationFloat;
import com.github.mce.minigames.api.config.ConfigurationFloatList;
import com.github.mce.minigames.api.config.ConfigurationInt;
import com.github.mce.minigames.api.config.ConfigurationIntList;
import com.github.mce.minigames.api.config.ConfigurationLong;
import com.github.mce.minigames.api.config.ConfigurationLongList;
import com.github.mce.minigames.api.config.ConfigurationShort;
import com.github.mce.minigames.api.config.ConfigurationShortList;
import com.github.mce.minigames.api.config.ConfigurationString;
import com.github.mce.minigames.api.config.ConfigurationStringList;
import com.github.mce.minigames.api.config.ConfigurationValueInterface;
import com.github.mce.minigames.api.config.ConfigurationValues;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;
import com.github.mce.minigames.api.locale.MessagesConfigInterface;
import com.github.mce.minigames.impl.msg.MessagesConfig;

/**
 * The minigames plugin impl.
 * 
 * @author mepeisen
 */
class MinigamePluginImpl implements MinigamePluginInterface
{
    
    /**
     * the messages configuration.
     */
    private final MessagesConfig                                         messages;
    
    /**
     * The minigame name.
     */
    private final String                                                 name;
    
    /**
     * The declaring java plugin.
     */
    private final JavaPlugin                                             plugin;
    
    /**
     * The configuration files.
     */
    private final Map<String, FileConfiguration>                         configurations   = new HashMap<>();
    
    /**
     * The default configurations.
     */
    private Map<String, List<ConfigurationValueInterface>>               defaultConfigs;
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<ArenaTypeInterface, ArenaTypeDeclarationInterface> arenaTypes       = new HashMap<>();
    
    /**
     * the known arena types of this minigame.
     */
    private final Map<String, ArenaTypeInterface>                        arenaTypesByName = new HashMap<>();
    
    /**
     * the default arena type to use.
     */
    private ArenaTypeDeclarationInterface                                defaultType;
    
    /** the minigames plugin. */
    private final MinigamesPlugin                                        mgplugin;
    
    /**
     * Constructor to create a minigame.
     * 
     * @param mgplugin
     *            minigames plugin
     * @param name
     *            internal name of the minigame.
     * @param provider
     *            the provider.
     */
    public MinigamePluginImpl(MinigamesPlugin mgplugin, String name, PluginProviderInterface provider)
    {
        this.plugin = provider.getJavaPlugin();
        this.messages = new MessagesConfig(this.plugin);
        this.name = name;
        this.mgplugin = mgplugin;
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public MessagesConfigInterface getMessages()
    {
        return this.messages;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigamePluginInterface#init()
     */
    @Override
    public void init()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigameInterface#getDeclaredTypes()
     */
    @Override
    public Iterable<ArenaTypeDeclarationInterface> getDeclaredTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigameInterface#getArenas()
     */
    @Override
    public Iterable<ArenaInterface> getArenas()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigameInterface#getArenas(java.lang.String)
     */
    @Override
    public ArenaInterface getArenas(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.MinigamePluginInterface#createArenaType(com.github.mce.minigames.api.arena.ArenaTypeInterface, boolean)
     */
    @Override
    public ArenaTypeBuilderInterface createArenaType(ArenaTypeInterface type, boolean isDefault) throws MinigameException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Logger getLogger()
    {
        return this.plugin.getLogger();
    }
    
    @Override
    public FileConfiguration getConfig(String file)
    {
        if (file.contains("/") || file.contains("..") || file.contains(":") || file.contains("\\")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        {
            throw new IllegalArgumentException("Invalid character in file name."); //$NON-NLS-1$
        }
        if (file.equals("messages.yml")) //$NON-NLS-1$
        {
            throw new IllegalArgumentException("Invalid file name."); //$NON-NLS-1$
        }
        return this.configurations.computeIfAbsent(file, (f) -> {
            FileConfiguration fileConfig = null;
            final File fobj = new File(this.plugin.getDataFolder(), file);
            if (file.equals("config.yml")) //$NON-NLS-1$
            {
                fileConfig = this.plugin.getConfig();
            }
            else
            {
                fileConfig = YamlConfiguration.loadConfiguration(fobj);
            }
            
            final List<ConfigurationValueInterface> list = this.defaultConfigs.get(file);
            if (list != null)
            {
                for (final ConfigurationValueInterface cfg : list)
                {
                    try
                    {
                        final ConfigurationValues clazzDef = cfg.getClass().getAnnotation(ConfigurationValues.class);
                        final Field field = cfg.getClass().getDeclaredField(((Enum<?>) cfg).name());
                        // final ConfigurationValue valueDef = .getAnnotation(LocalizedMessage.class);
                        if (clazzDef == null)
                        {
                            throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
                        }
                        
                        if (field.getAnnotation(ConfigurationBool.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationBool.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationBoolList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationBoolList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationByte.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationByte.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationByteList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationByteList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationCharacter.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationCharacter.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationCharacterList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationCharacterList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationDouble.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationDouble.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationDoubleList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationDoubleList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationFloat.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationFloat.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationFloatList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationFloatList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationInt.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationInt.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationIntList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationIntList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationLong.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationLong.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationLongList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationLongList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationShort.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationShort.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationShortList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationShortList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationString.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationString.class).defaultValue());
                        }
                        
                        if (field.getAnnotation(ConfigurationStringList.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationStringList.class).defaultValue()));
                        }
                        
                        if (field.getAnnotation(ConfigurationColor.class) != null)
                        {
                            fileConfig.addDefault(cfg.path(), Color.fromRGB((field.getAnnotation(ConfigurationColor.class).defaultRgb())));
                        }
                    }
                    catch (NoSuchFieldException ex)
                    {
                        throw new IllegalStateException(ex);
                    }
                }
                fileConfig.options().copyDefaults(true);
                try
                {
                    fileConfig.save(fobj);
                }
                catch (IOException e)
                {
                    // TODO logging
                    e.printStackTrace();
                }
            }
            
            return fileConfig;
        });
    }
    
    @Override
    public void saveConfig(String file)
    {
        final File fobj = new File(this.plugin.getDataFolder(), file);
        try
        {
            this.getConfig(file).save(fobj);
        }
        catch (IOException e)
        {
            // TODO logging
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the messages with given localized messages.
     * 
     * @param msgs
     */
    void initMessage(List<LocalizedMessageInterface> msgs)
    {
        this.messages.initMessage(msgs);
    }
    
    /**
     * Initializes the configuration files.
     * 
     * @param configs
     */
    void initConfgurations(Map<String, List<ConfigurationValueInterface>> configs)
    {
        this.defaultConfigs = configs;
    }
    
    @Override
    public <T> void registerContextHandler(Class<T> clazz, ContextHandlerInterface<T> handler) throws MinigameException
    {
        this.mgplugin.getApiContext().registerContextHandler(clazz, handler);
    }
    
}
