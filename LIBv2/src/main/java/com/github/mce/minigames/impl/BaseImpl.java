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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mce.minigames.impl.msg.MessagesConfig;

import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeProvider;

/**
 * Basic support for extensions and minigames.
 * 
 * @author mepeisen
 */
class BaseImpl implements ArenaTypeProvider
{
    
    /**
     * the messages configuration.
     */
    private final MessagesConfig                           messages;
    
    /**
     * The declaring java plugin.
     */
    protected final JavaPlugin                             plugin;
    
    /**
     * The configuration files.
     */
    private final Map<String, FileConfiguration>           configurations = new HashMap<>();
    
    /**
     * The default configurations.
     */
    private Map<String, List<ConfigurationValueInterface>> defaultConfigs;
    
    /** the minigames plugin. */
    protected final MinigamesPlugin                        mgplugin;
    
    /**
     * Constructor to create the component.
     * 
     * @param mgplugin
     *            minigames plugin
     * @param plugin
     *            the java plugin.
     */
    public BaseImpl(MinigamesPlugin mgplugin, JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.messages = new MessagesConfig(this.plugin);
        this.mgplugin = mgplugin;
    }
    
    /**
     * Returns the messages config interface
     * 
     * @return messages config
     */
    public MessagesConfigInterface getMessages()
    {
        return this.messages;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaTypeProvider#getDisplayName()
     */
    @Override
    public LocalizedMessageInterface getDisplayName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaTypeProvider#getShortDescription()
     */
    @Override
    public LocalizedMessageInterface getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaTypeProvider#getDescription()
     */
    @Override
    public LocalizedMessageInterface getDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
//    @Override
//    public FileConfiguration getConfig(String file)
//    {
//        if (file.contains("/") || file.contains("..") || file.contains(":") || file.contains("\\")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//        {
//            throw new IllegalArgumentException("Invalid character in file name."); //$NON-NLS-1$
//        }
//        if (file.equals("messages.yml")) //$NON-NLS-1$
//        {
//            throw new IllegalArgumentException("Invalid file name."); //$NON-NLS-1$
//        }
//        return this.configurations.computeIfAbsent(file, (f) -> {
//            FileConfiguration fileConfig = null;
//            final File fobj = new File(this.plugin.getDataFolder(), file);
//            if (file.equals("config.yml")) //$NON-NLS-1$
//            {
//                fileConfig = this.plugin.getConfig();
//            }
//            else
//            {
//                fileConfig = YamlConfiguration.loadConfiguration(fobj);
//            }
//            
//            final List<ConfigurationValueInterface> list = this.defaultConfigs.get(file);
//            if (list != null)
//            {
//                for (final ConfigurationValueInterface cfg : list)
//                {
//                    try
//                    {
//                        final ConfigurationValues clazzDef = cfg.getClass().getAnnotation(ConfigurationValues.class);
//                        final Field field = cfg.getClass().getDeclaredField(((Enum<?>) cfg).name());
//                        // final ConfigurationValue valueDef = .getAnnotation(LocalizedMessage.class);
//                        if (clazzDef == null)
//                        {
//                            throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationBool.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationBool.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationBoolList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationBoolList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationByte.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationByte.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationByteList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationByteList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationCharacter.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationCharacter.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationCharacterList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationCharacterList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationDouble.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationDouble.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationDoubleList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationDoubleList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationFloat.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationFloat.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationFloatList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationFloatList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationInt.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationInt.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationIntList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationIntList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationLong.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationLong.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationLongList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationLongList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationShort.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationShort.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationShortList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationShortList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationString.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), field.getAnnotation(ConfigurationString.class).defaultValue());
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationStringList.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationStringList.class).defaultValue()));
//                        }
//                        
//                        if (field.getAnnotation(ConfigurationColor.class) != null)
//                        {
//                            fileConfig.addDefault(cfg.path(), Color.fromRGB((field.getAnnotation(ConfigurationColor.class).defaultRgb())));
//                        }
//                    }
//                    catch (NoSuchFieldException ex)
//                    {
//                        throw new IllegalStateException(ex);
//                    }
//                }
//                fileConfig.options().copyDefaults(true);
//                try
//                {
//                    fileConfig.save(fobj);
//                }
//                catch (IOException e)
//                {
//                    // TODO logging
//                    e.printStackTrace();
//                }
//            }
//            
//            return fileConfig;
//        });
//    }
    
//    @Override
//    public void saveConfig(String file)
//    {
//        final File fobj = new File(this.plugin.getDataFolder(), file);
//        try
//        {
//            this.getConfig(file).save(fobj);
//        }
//        catch (IOException e)
//        {
//            // TODO logging
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * Initializes the messages with given localized messages.
//     * 
//     * @param msgs
//     */
//    void initMessage(List<LocalizedMessageInterface> msgs)
//    {
//        this.messages.initMessage(msgs);
//    }
//    
//    /**
//     * Initializes the configuration files.
//     * 
//     * @param configs
//     */
//    void initConfgurations(Map<String, List<ConfigurationValueInterface>> configs)
//    {
//        this.defaultConfigs = configs;
//    }
//
//    /**
//     * @param rules
//     */
//    public void initRules(List<RuleId> rules)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * @param components
//     */
//    public void initComponents(List<ComponentId> components)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * @param plist
//     */
//    public void initPhases(List<MatchPhaseId> plist)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * @param tlist
//     */
//    public void initTeams(List<TeamId> tlist)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getDeclaredTypes()
//     */
//    @Override
//    public Iterable<ArenaTypeDeclarationInterface> getDeclaredTypes()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getDefaultType()
//     */
//    @Override
//    public ArenaTypeDeclarationInterface getDefaultType()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getType(java.lang.String)
//     */
//    @Override
//    public ArenaTypeDeclarationInterface getType(String name)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getAdminRules()
//     */
//    @Override
//    public Iterable<AdminRuleId> getAdminRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getMatchRules()
//     */
//    @Override
//    public Iterable<MatchRuleId> getMatchRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getPlayerRules()
//     */
//    @Override
//    public Iterable<PlayerRuleId> getPlayerRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getTeamRules()
//     */
//    @Override
//    public Iterable<TeamRuleId> getTeamRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getComponentRules()
//     */
//    @Override
//    public Iterable<ComponentRuleId> getComponentRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getArenaRules()
//     */
//    @Override
//    public Iterable<ArenaRuleId> getArenaRules()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getComponents()
//     */
//    @Override
//    public Iterable<ComponentId> getComponents()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getMatchPhases()
//     */
//    @Override
//    public Iterable<MatchPhaseId> getMatchPhases()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /* (non-Javadoc)
//     * @see com.github.mce.minigames.api.arena.ArenaTypeProvider#getTeams()
//     */
//    @Override
//    public Iterable<TeamId> getTeams()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
}
