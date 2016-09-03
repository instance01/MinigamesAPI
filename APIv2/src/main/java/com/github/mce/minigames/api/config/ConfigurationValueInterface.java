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

package com.github.mce.minigames.api.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * An interface for enumerations that represent entries in configuration files.
 * 
 * @author mepeisen
 */
public interface ConfigurationValueInterface
{
    
    /**
     * Checks if this configuration variable is a boolean.
     * @return {@code true} if this is a boolean
     */
    default boolean isBoolean()
    {
        return ConfigurationTool.isType(this, ConfigurationBool.class);
    }
    
    /**
     * Checks if this configuration variable is a boolean list.
     * @return {@code true} if this is a boolean list
     */
    default boolean isBooleanList()
    {
        return ConfigurationTool.isType(this, ConfigurationBoolList.class);
    }
    
    /**
     * Checks if this configuration variable is a byte.
     * @return {@code true} if this is a byte
     */
    default boolean isByte()
    {
        return ConfigurationTool.isType(this, ConfigurationByte.class);
    }
    
    /**
     * Checks if this configuration variable is a byte list.
     * @return {@code true} if this is a byte list
     */
    default boolean isByteList()
    {
        return ConfigurationTool.isType(this, ConfigurationByteList.class);
    }
    
    /**
     * Checks if this configuration variable is a character.
     * @return {@code true} if this is a character
     */
    default boolean isCharacter()
    {
        return ConfigurationTool.isType(this, ConfigurationCharacter.class);
    }
    
    /**
     * Checks if this configuration variable is a character list.
     * @return {@code true} if this is a character list
     */
    default boolean isCharacterList()
    {
        return ConfigurationTool.isType(this, ConfigurationCharacterList.class);
    }
    
    /**
     * Checks if this configuration variable is a color list.
     * @return {@code true} if this is a color list
     */
    default boolean isColorList()
    {
        return ConfigurationTool.isType(this, ConfigurationColorList.class);
    }
    
    /**
     * Checks if this configuration variable is a color.
     * @return {@code true} if this is a color
     */
    default boolean isColor()
    {
        return ConfigurationTool.isType(this, ConfigurationColor.class);
    }
    
    /**
     * Checks if this configuration variable is a double list.
     * @return {@code true} if this is a double list
     */
    default boolean isDoubleList()
    {
        return ConfigurationTool.isType(this, ConfigurationDoubleList.class);
    }
    
    /**
     * Checks if this configuration variable is a double.
     * @return {@code true} if this is a double
     */
    default boolean isDouble()
    {
        return ConfigurationTool.isType(this, ConfigurationDouble.class);
    }
    
    /**
     * Checks if this configuration variable is a float list.
     * @return {@code true} if this is a float list
     */
    default boolean isFloatList()
    {
        return ConfigurationTool.isType(this, ConfigurationFloatList.class);
    }
    
    /**
     * Checks if this configuration variable is a float.
     * @return {@code true} if this is a float
     */
    default boolean isFloat()
    {
        return ConfigurationTool.isType(this, ConfigurationFloat.class);
    }
    
    /**
     * Checks if this configuration variable is a int list.
     * @return {@code true} if this is a int list
     */
    default boolean isIntList()
    {
        return ConfigurationTool.isType(this, ConfigurationIntList.class);
    }
    
    /**
     * Checks if this configuration variable is a int.
     * @return {@code true} if this is a int
     */
    default boolean isInt()
    {
        return ConfigurationTool.isType(this, ConfigurationInt.class);
    }
    
    /**
     * Checks if this configuration variable is an item stack list.
     * @return {@code true} if this is an item stack list
     */
    default boolean isItemStackList()
    {
        return ConfigurationTool.isType(this, ConfigurationItemStackList.class);
    }
    
    /**
     * Checks if this configuration variable is an item stack.
     * @return {@code true} if this is an item stack
     */
    default boolean isItemStack()
    {
        return ConfigurationTool.isType(this, ConfigurationItemStack.class);
    }
    
    /**
     * Checks if this configuration variable is a section.
     * @return {@code true} if this is a section
     */
    default boolean isSection()
    {
        return ConfigurationTool.isType(this, ConfigurationSection.class);
    }
    
    /**
     * Checks if this configuration variable is a long list.
     * @return {@code true} if this is a long list
     */
    default boolean isLongList()
    {
        return ConfigurationTool.isType(this, ConfigurationLongList.class);
    }
    
    /**
     * Checks if this configuration variable is a long.
     * @return {@code true} if this is a long
     */
    default boolean isLong()
    {
        return ConfigurationTool.isType(this, ConfigurationLong.class);
    }
    
    /**
     * Checks if this configuration variable is an object list.
     * @return {@code true} if this is an object list
     */
    default boolean isObjectList()
    {
        return ConfigurationTool.isType(this, ConfigurationObjectList.class);
    }
    
    /**
     * Checks if this configuration variable is an object.
     * @return {@code true} if this is an object
     */
    default boolean isObject()
    {
        return ConfigurationTool.isType(this, ConfigurationObject.class);
    }
    
    /**
     * Checks if this configuration variable is a player list.
     * @return {@code true} if this is a player list
     */
    default boolean isPlayerList()
    {
        return ConfigurationTool.isType(this, ConfigurationPlayerList.class);
    }
    
    /**
     * Checks if this configuration variable is a player.
     * @return {@code true} if this is a player
     */
    default boolean isPlayer()
    {
        return ConfigurationTool.isType(this, ConfigurationPlayer.class);
    }
    
    /**
     * Checks if this configuration variable is a short list.
     * @return {@code true} if this is a short list
     */
    default boolean isShortList()
    {
        return ConfigurationTool.isType(this, ConfigurationShortList.class);
    }
    
    /**
     * Checks if this configuration variable is a short.
     * @return {@code true} if this is a short
     */
    default boolean isShort()
    {
        return ConfigurationTool.isType(this, ConfigurationShort.class);
    }
    
    /**
     * Checks if this configuration variable is a string list.
     * @return {@code true} if this is a string list
     */
    default boolean isStringList()
    {
        return ConfigurationTool.isType(this, ConfigurationStringList.class);
    }
    
    /**
     * Checks if this configuration variable is a string.
     * @return {@code true} if this is a string
     */
    default boolean isString()
    {
        return ConfigurationTool.isType(this, ConfigurationString.class);
    }
    
    /**
     * Checks if this configuration variable is a vector list.
     * @return {@code true} if this is a vector list
     */
    default boolean isVectorList()
    {
        return ConfigurationTool.isType(this, ConfigurationVectorList.class);
    }
    
    /**
     * Checks if this configuration variable is a vector.
     * @return {@code true} if this is a vector
     */
    default boolean isVector()
    {
        return ConfigurationTool.isType(this, ConfigurationVector.class);
    }
    
    /**
     * Returns the configuration path of this option
     * @return configuration path
     */
    default String path()
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(((Enum<?>) this).name());
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            
            {
                final ConfigurationBool config = field.getAnnotation(ConfigurationBool.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationBoolList config = field.getAnnotation(ConfigurationBoolList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationByte config = field.getAnnotation(ConfigurationByte.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationByteList config = field.getAnnotation(ConfigurationByteList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationCharacter config = field.getAnnotation(ConfigurationCharacter.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationCharacterList config = field.getAnnotation(ConfigurationCharacterList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationColor config = field.getAnnotation(ConfigurationColor.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationColorList config = field.getAnnotation(ConfigurationColorList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationDouble config = field.getAnnotation(ConfigurationDouble.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationDoubleList config = field.getAnnotation(ConfigurationDoubleList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationFloat config = field.getAnnotation(ConfigurationFloat.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationFloatList config = field.getAnnotation(ConfigurationFloatList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationInt config = field.getAnnotation(ConfigurationInt.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationIntList config = field.getAnnotation(ConfigurationIntList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationItemStack config = field.getAnnotation(ConfigurationItemStack.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationItemStackList config = field.getAnnotation(ConfigurationItemStackList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationLong config = field.getAnnotation(ConfigurationLong.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationLongList config = field.getAnnotation(ConfigurationLongList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationObject config = field.getAnnotation(ConfigurationObject.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationObjectList config = field.getAnnotation(ConfigurationObjectList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationPlayer config = field.getAnnotation(ConfigurationPlayer.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationPlayerList config = field.getAnnotation(ConfigurationPlayerList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationSection config = field.getAnnotation(ConfigurationSection.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
                    return path;
                }
            }
            {
                final ConfigurationShort config = field.getAnnotation(ConfigurationShort.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationShortList config = field.getAnnotation(ConfigurationShortList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationString config = field.getAnnotation(ConfigurationString.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationStringList config = field.getAnnotation(ConfigurationStringList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationVector config = field.getAnnotation(ConfigurationVector.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            {
                final ConfigurationVectorList config = field.getAnnotation(ConfigurationVectorList.class);
                if (config != null)
                {
                    final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
                    return path;
                }
            }
            throw new IllegalStateException("Invalid configuration option"); //$NON-NLS-1$
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Checks if this configuration value is set.
     * 
     * @return {@code true} if this configuraiton value is set.
     */
    default boolean isset()
    {
        final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
        final MglibInterface lib = MglibInterface.INSTANCE.get();
        final ConfigInterface minigame = lib.getConfigFromCfg(this);
        
        return minigame.getConfig(configs.file()).isSet(this.path());
    }
    
    /**
     * Checks if this configuration value is set.
     * 
     * @param path
     *            sub path of configuration section
     * @return {@code true} if this configuraiton value is set.
     */
    default boolean isset(String path)
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(((Enum<?>) this).name());
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final ConfigInterface minigame = lib.getConfigFromCfg(this);
            final ConfigurationSection config = field.getAnnotation(ConfigurationSection.class);
            if (config != null)
            {
                final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value())) + '.' + path;
                return minigame.getConfig(configs.file()).isSet(mpath);
            }
            throw new IllegalStateException("Invalid configuration option"); //$NON-NLS-1$
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setObject(Configurable value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    final org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(path);
                    value.writeToConfig(section);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setObjectList(Configurable[] value, String subpath)
    {
        ConfigurationTool.consumeList(
                this, subpath, value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    element.writeToConfig(section.getConfigurationSection(path));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setBoolean(boolean value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Boolean.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setBooleanList(boolean[] value, String subpath)
    {
        final List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setByte(byte value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Byte.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setByteList(byte[] value, String subpath)
    {
        final List<Byte> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setCharacter(char value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, String.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setCharacterList(char[] value, String subpath)
    {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setColor(Color value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setColorList(Color[] value, String subpath)
    {
        ConfigurationTool.consumeList(
                this, subpath, value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setDouble(double value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Double.valueOf(value));
                });
    }

    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setDoubleList(double[] value, String subpath)
    {
        final List<Double> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setFloat(float value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Float.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setFloatList(float[] value, String subpath)
    {
        final List<Float> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setInt(int value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Integer.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setIntList(int[] value, String subpath)
    {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setItemStack(ItemStack value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setItemStackList(ItemStack[] value, String subpath)
    {
        ConfigurationTool.consumeList(
                this, subpath, value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setLong(long value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Long.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setLongList(long[] value, String subpath)
    {
        final List<Long> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setPlayer(ArenaPlayerInterface value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value.getOfflinePlayer());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setPlayerList(ArenaPlayerInterface[] value, String subpath)
    {
        ConfigurationTool.consumeList(
                this, subpath, value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element.getOfflinePlayer());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setShort(short value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Short.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setShortList(short[] value, String subpath)
    {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(Integer.valueOf(value[i]));
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setString(String value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setStringList(String[] value, String subpath)
    {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setVector(Vector value, String subpath)
    {
        ConfigurationTool.consume(
                this, subpath,
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     */
    default void setVectorList(Vector[] value, String subpath)
    {
        ConfigurationTool.consumeList(
                this, subpath, value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setObject(Configurable value)
    {
        ConfigurationTool.consume(
                this, ConfigurationObject.class, ConfigurationTool.objectPath(),
                (val, configs, config, lib, minigame, path) -> {
                    final org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(path);
                    value.writeToConfig(section);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setObjectList(Configurable[] value)
    {
        ConfigurationTool.consumeList(
                this, ConfigurationObjectList.class, ConfigurationTool.objectListPath(), value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    element.writeToConfig(section.getConfigurationSection(path));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setBoolean(boolean value)
    {
        ConfigurationTool.consume(
                this, ConfigurationBool.class, ConfigurationTool.boolPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Boolean.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setBooleanList(boolean[] value)
    {
        final List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationBoolList.class, ConfigurationTool.boolListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setByte(byte value)
    {
        ConfigurationTool.consume(
                this, ConfigurationByte.class, ConfigurationTool.bytePath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Byte.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setByteList(byte[] value)
    {
        final List<Byte> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationByteList.class, ConfigurationTool.byteListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setCharacter(char value)
    {
        ConfigurationTool.consume(
                this, ConfigurationCharacter.class, ConfigurationTool.charPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, String.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setCharacterList(char[] value)
    {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationCharacterList.class, ConfigurationTool.charListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setColor(Color value)
    {
        ConfigurationTool.consume(
                this, ConfigurationColor.class, ConfigurationTool.colorPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setColorList(Color[] value)
    {
        ConfigurationTool.consumeList(
                this, ConfigurationColorList.class, ConfigurationTool.colorListPath(), value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setDouble(double value)
    {
        ConfigurationTool.consume(
                this, ConfigurationDouble.class, ConfigurationTool.doublePath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Double.valueOf(value));
                });
    }

    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setDoubleList(double[] value)
    {
        final List<Double> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationDoubleList.class, ConfigurationTool.doubleListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setFloat(float value)
    {
        ConfigurationTool.consume(
                this, ConfigurationFloat.class, ConfigurationTool.floatPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Float.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setFloatList(float[] value)
    {
        final List<Float> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationFloatList.class, ConfigurationTool.floatListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setInt(int value)
    {
        ConfigurationTool.consume(
                this, ConfigurationInt.class, ConfigurationTool.intPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Integer.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setIntList(int[] value)
    {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationIntList.class, ConfigurationTool.intListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setItemStack(ItemStack value)
    {
        ConfigurationTool.consume(
                this, ConfigurationItemStack.class, ConfigurationTool.itemStackPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setItemStackList(ItemStack[] value)
    {
        ConfigurationTool.consumeList(
                this, ConfigurationItemStackList.class, ConfigurationTool.itemStackListPath(), value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setLong(long value)
    {
        ConfigurationTool.consume(
                this, ConfigurationLong.class, ConfigurationTool.longPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Long.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setLongList(long[] value)
    {
        final List<Long> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationLongList.class, ConfigurationTool.longListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setPlayer(ArenaPlayerInterface value)
    {
        ConfigurationTool.consume(
                this, ConfigurationPlayer.class, ConfigurationTool.playerPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value.getOfflinePlayer());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setPlayerList(ArenaPlayerInterface[] value)
    {
        ConfigurationTool.consumeList(
                this, ConfigurationPlayerList.class, ConfigurationTool.playerListPath(), value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element.getOfflinePlayer());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setShort(short value)
    {
        ConfigurationTool.consume(
                this, ConfigurationShort.class, ConfigurationTool.shortPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, Short.valueOf(value));
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setShortList(short[] value)
    {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(Integer.valueOf(value[i]));
        ConfigurationTool.consume(
                this, ConfigurationShortList.class, ConfigurationTool.shortListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setString(String value)
    {
        ConfigurationTool.consume(
                this, ConfigurationString.class, ConfigurationTool.stringPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setStringList(String[] value)
    {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++) list.add(value[i]);
        ConfigurationTool.consume(
                this, ConfigurationStringList.class, ConfigurationTool.stringListPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, list);
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setVector(Vector value)
    {
        ConfigurationTool.consume(
                this, ConfigurationVector.class, ConfigurationTool.vectorPath(),
                (val, configs, config, lib, minigame, path) -> {
                    minigame.getConfig(configs.file()).set(path, value.clone());
                });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    default void setVectorList(Vector[] value)
    {
        ConfigurationTool.consumeList(
                this, ConfigurationVector.class, ConfigurationTool.vectorPath(), value,
                (val, configs, config, lib, minigame, section, path, element) -> {
                    section.set(path, element.clone());
                });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    @SuppressWarnings("unchecked")
    default <T extends Configurable> T getObject()
    {
        return (T) ConfigurationTool.calculate(
                this, ConfigurationObject.class, ConfigurationTool.objectPath(),
                (val, configs, config, lib, minigame, path) -> {
                    final org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(path);
                    final Configurable result = config.clazz().newInstance();
                    result.readFromConfig(section);
                    return result;
                });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default byte getByte()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationByte.class, ConfigurationTool.bytePath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getInt(path, config.defaultValue())).byteValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default Color getColor()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationColor.class, ConfigurationTool.colorPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getColor(path, Color.fromRGB(config.defaultRgb())));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default ItemStack getItemStack()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationItemStack.class, ConfigurationTool.itemStackPath(),
                (val, configs, config, lib, minigame, path) -> {
                    final ItemStack stack = minigame.getConfig(configs.file()).getItemStack(path);
                    return stack == null ? null : stack.clone();
                });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default Vector getVector()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationVector.class, ConfigurationTool.vectorPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getVector(path).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default ArenaPlayerInterface getPlayer()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationPlayer.class, ConfigurationTool.playerPath(),
                (val, configs, config, lib, minigame, path) -> lib.getPlayer(minigame.getConfig(configs.file()).getOfflinePlayer(path)));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default char getCharacter()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationCharacter.class, ConfigurationTool.charPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getString(path, "" + config.defaultValue())).charAt(0); //$NON-NLS-1$
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default boolean getBoolean()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationBool.class, ConfigurationTool.boolPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getBoolean(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default boolean[] getBooleanList()
    {
        final List<Boolean> list = ConfigurationTool.calculate(
                this, ConfigurationBoolList.class, ConfigurationTool.boolListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getBooleanList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final boolean[] result = new boolean[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default byte[] getByteList()
    {
        final List<Byte> list = ConfigurationTool.calculate(
                this, ConfigurationByteList.class, ConfigurationTool.byteListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getByteList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final byte[] result = new byte[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default char[] getCharacterList()
    {
        final List<Character> list = ConfigurationTool.calculate(
                this, ConfigurationCharacterList.class, ConfigurationTool.charListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getCharacterList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final char[] result = new char[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default double getDouble()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationDouble.class, ConfigurationTool.doublePath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getDouble(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default float getFloat()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationFloat.class, ConfigurationTool.floatPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getDouble(path, config.defaultValue())).floatValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default double[] getDoubleList()
    {
        final List<Double> list = ConfigurationTool.calculate(
                this, ConfigurationDoubleList.class, ConfigurationTool.doubleListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getDoubleList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final double[] result = new double[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default float[] getFloatList()
    {
        final List<Float> list = ConfigurationTool.calculate(
                this, ConfigurationFloatList.class, ConfigurationTool.floatListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getFloatList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final float[] result = new float[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default int getInt()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationInt.class, ConfigurationTool.intPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getInt(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default short getShort()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationShort.class, ConfigurationTool.shortPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getInt(path, config.defaultValue())).shortValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default int[] getIntList()
    {
        final List<Integer> list = ConfigurationTool.calculate(
                this, ConfigurationIntList.class, ConfigurationTool.intListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getIntegerList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default long getLong()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationLong.class, ConfigurationTool.longPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getLong(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default long[] getLongList()
    {
        final List<Long> list = ConfigurationTool.calculate(
                this, ConfigurationLongList.class, ConfigurationTool.longListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getLongList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final long[] result = new long[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default short[] getShortList()
    {
        final List<Short> list = ConfigurationTool.calculate(
                this, ConfigurationShortList.class, ConfigurationTool.shortListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getShortList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue()))
                );
        final short[] result = new short[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default String getString()
    {
        return ConfigurationTool.calculate(
                this, ConfigurationString.class, ConfigurationTool.stringPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getString(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default String[] getStringList()
    {
        final List<String> list = ConfigurationTool.calculate(
                this, ConfigurationStringList.class, ConfigurationTool.stringListPath(),
                (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getStringList(path),
                (val, configs, config, lib, minigame, path) -> Arrays.asList(config.defaultValue())
                );
        return list.toArray(new String[list.size()]);
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default Vector[] getVectorList()
    {
        return ConfigurationTool.calculateList(
                this, ConfigurationVectorList.class, Vector.class, ConfigurationTool.vectorListPath(),
                (val, configs, config, lib, minigame, section, key) -> section.getVector(key).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default ItemStack[] getItemStackList()
    {
        return ConfigurationTool.calculateList(
                this, ConfigurationItemStackList.class, ItemStack.class, ConfigurationTool.itemStackListPath(),
                (val, configs, config, lib, minigame, section, key) -> section.getItemStack(key).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param clazz
     * @return value.
     */
    default <T extends Configurable> T[] getObjectList(Class<T> clazz)
    {
        return ConfigurationTool.calculateList(
                this, ConfigurationObjectList.class, clazz, ConfigurationTool.objectListPath(),
                (val, configs, config, lib, minigame, section, key) -> {
                    final T ret = clazz.newInstance();
                    ret.readFromConfig(section.getConfigurationSection(key));
                    return ret;
                });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default Color[] getColorList()
    {
        return ConfigurationTool.calculateList(
                this, ConfigurationColorList.class, Color.class, ConfigurationTool.colorListPath(),
                (val, configs, config, lib, minigame, section, key) -> section.getColor(key));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default ArenaPlayerInterface[] getPlayerList()
    {
        return ConfigurationTool.calculateList(
                this, ConfigurationPlayerList.class, ArenaPlayerInterface.class, ConfigurationTool.playerListPath(),
                (val, configs, config, lib, minigame, section, key) -> lib.getPlayer(section.getOfflinePlayer(key)));
    }
    
    /**
     * Returns the keys of given configuration section.
     * 
     * @param deep
     * 
     * @return value.
     */
    default String[] getKeys(boolean deep)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final ConfigInterface minigame = lib.getConfigFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final Set<String> result = minigame.getConfig(configs.file()).getConfigurationSection(path).getKeys(deep);
            return result.toArray(new String[result.size()]);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param clazz
     * @param path
     *            sub path of configuration section
     * @return value.
     */
    @SuppressWarnings("unchecked")
    default <T extends Configurable> T getObject(Class<T> clazz, String path)
    {
        return (T) ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> {
                    final org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(spath);
                    final Configurable result = clazz.newInstance();
                    result.readFromConfig(section);
                    return result;
                });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default byte getByte(String path, byte defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getInt(spath, defaultValue)).byteValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default boolean getBoolean(String path, boolean defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getBoolean(spath, defaultValue));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default boolean[] getBooleanList(String path, boolean[] defaultValue)
    {
        final List<Boolean> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getBooleanList(spath));
        if (list.size() == 0) return defaultValue;
        final boolean[] result = new boolean[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default byte[] getByteList(String path, byte[] defaultValue)
    {
        final List<Byte> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getByteList(spath));
        if (list.size() == 0) return defaultValue;
        final byte[] result = new byte[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default char[] getCharacterList(String path, char[] defaultValue)
    {
        final List<Character> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getCharacterList(spath));
        if (list.size() == 0) return defaultValue;
        final char[] result = new char[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default double getDouble(String path, double defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getDouble(spath, defaultValue));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default float getFloat(String path, float defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getDouble(spath, defaultValue)).floatValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default double[] getDoubleList(String path, double[] defaultValue)
    {
        final List<Double> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getDoubleList(spath));
        if (list.size() == 0) return defaultValue;
        final double[] result = new double[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default float[] getFloatList(String path, float[] defaultValue)
    {
        final List<Float> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getFloatList(spath));
        if (list.size() == 0) return defaultValue;
        final float[] result = new float[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default int getInt(String path, int defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getInt(spath, defaultValue));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default int[] getIntList(String path, int[] defaultValue)
    {
        final List<Integer> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getIntegerList(spath));
        if (list.size() == 0) return defaultValue;
        final int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default long getLong(String path, long defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getLong(spath, defaultValue));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default short getShort(String path, short defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getInt(spath, defaultValue)).shortValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default long[] getLongList(String path, long[] defaultValue)
    {
        final List<Long> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getLongList(spath));
        if (list.size() == 0) return defaultValue;
        final long[] result = new long[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default short[] getShortList(String path, short[] defaultValue)
    {
        final List<Short> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getShortList(spath));
        if (list.size() == 0) return defaultValue;
        final short[] result = new short[list.size()];
        for (int i = 0; i < result.length; i++) result[i] = list.get(i);
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default String getString(String path, String defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getString(spath, defaultValue));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default char getCharacter(String path, char defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getString(spath, "" + defaultValue)).charAt(0); //$NON-NLS-1$
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default Color getColor(String path, Color defaultValue)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getColor(spath));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ItemStack getItemStack(String path)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getItemStack(spath).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default Vector getVector(String path)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getVector(spath).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default Vector[] getVectorList(String path)
    {
        return ConfigurationTool.calculateList(
                this, path, Vector.class,
                (val, configs, config, lib, minigame, section, key) -> section.getVector(key).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ItemStack[] getItemStackList(String path)
    {
        return ConfigurationTool.calculateList(
                this, path, ItemStack.class,
                (val, configs, config, lib, minigame, section, key) -> section.getItemStack(key).clone());
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param clazz
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default <T extends Configurable> T[] getObjectList(Class<T> clazz, String path)
    {
        return ConfigurationTool.calculateList(
                this, path, clazz,
                (val, configs, config, lib, minigame, section, key) -> {
                    final T result = clazz.newInstance();
                    result.readFromConfig(section.getConfigurationSection(key));
                    return result;
                });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default Color[] getColorList(String path)
    {
        return ConfigurationTool.calculateList(
                this, path, Color.class,
                (val, configs, config, lib, minigame, section, key) -> section.getColor(key));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ArenaPlayerInterface[] getPlayerList(String path)
    {
        return ConfigurationTool.calculateList(
                this, path, ArenaPlayerInterface.class,
                (val, configs, config, lib, minigame, section, key) -> lib.getPlayer(section.getOfflinePlayer(key)));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ArenaPlayerInterface getPlayer(String path)
    {
        return ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> lib.getPlayer(minigame.getConfig(configs.file()).getOfflinePlayer(spath)));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default String[] getStringList(String path, String[] defaultValue)
    {
        final List<String> list = ConfigurationTool.calculate(
                this, path,
                (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getStringList(spath));
        if (list.size() == 0) return defaultValue;
        return list.toArray(new String[list.size()]);
    }
    
    /**
     * Saves the configuration file this option belongs to
     */
    default void saveConfig()
    {
        final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
        final MglibInterface lib = MglibInterface.INSTANCE.get();
        final ConfigInterface minigame = lib.getConfigFromCfg(this);
        minigame.saveConfig(configs.file());
    }
    
}
