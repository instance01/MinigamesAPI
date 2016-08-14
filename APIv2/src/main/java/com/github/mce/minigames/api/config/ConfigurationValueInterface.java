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

import java.util.List;
import java.util.Set;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;

/**
 * An interface for enumerations that represent entries in configuration files.
 * 
 * @author mepeisen
 */
public interface ConfigurationValueInterface
{
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default boolean getBoolean()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationBool config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationBool.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            return minigame.getConfig(configs.file()).getBoolean(path, config.defaultValue());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default boolean[] getBooleanList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationBoolList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationBoolList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Boolean> result = minigame.getConfig(configs.file()).getBooleanList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final boolean[] res = new boolean[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).booleanValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default byte[] getByteList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationByteList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationByteList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Byte> result = minigame.getConfig(configs.file()).getByteList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final byte[] res = new byte[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).byteValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default char[] getCharacterList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationCharacterList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationCharacterList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Character> result = minigame.getConfig(configs.file()).getCharacterList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final char[] res = new char[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).charValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default double getDouble()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationDouble config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationDouble.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            return minigame.getConfig(configs.file()).getDouble(path, config.defaultValue());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default double[] getDoubleList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationDoubleList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationDoubleList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Double> result = minigame.getConfig(configs.file()).getDoubleList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final double[] res = new double[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).doubleValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default float[] getFloatList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationFloatList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationFloatList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Float> result = minigame.getConfig(configs.file()).getFloatList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final float[] res = new float[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).floatValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default int getInt()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationInt config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationInt.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            return minigame.getConfig(configs.file()).getInt(path, config.defaultValue());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default int[] getIntList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationIntList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationIntList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Integer> result = minigame.getConfig(configs.file()).getIntegerList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final int[] res = new int[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).intValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default long getLong()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationLong config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationLong.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            return minigame.getConfig(configs.file()).getLong(path, config.defaultValue());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default long[] getLongList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationLongList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationLongList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Long> result = minigame.getConfig(configs.file()).getLongList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final long[] res = new long[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).longValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default short[] getShortList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationShortList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationShortList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<Short> result = minigame.getConfig(configs.file()).getShortList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            final short[] res = new short[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).shortValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default String getString()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationString config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationString.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            return minigame.getConfig(configs.file()).getString(path, config.defaultValue());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    default String[] getStringList()
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationStringList config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationStringList.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String path = lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) this).name() : config.name()));
            final List<String> result = minigame.getConfig(configs.file()).getStringList(path);
            if (result.size() == 0)
            {
                return config.defaultValue();
            }
            return result.toArray(new String[result.size()]);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
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
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
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
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default boolean getBoolean(String path, boolean defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            return minigame.getConfig(configs.file()).getConfigurationSection(mpath).getBoolean(path, defaultValue);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default boolean[] getBooleanList(String path, boolean[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Boolean> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getBooleanList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final boolean[] res = new boolean[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).booleanValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default byte[] getByteList(String path, byte[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Byte> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getByteList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final byte[] res = new byte[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).byteValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default char[] getCharacterList(String path, char[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Character> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getCharacterList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final char[] res = new char[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).charValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default double getDouble(String path, double defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            return minigame.getConfig(configs.file()).getConfigurationSection(mpath).getDouble(path, defaultValue);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default double[] getDoubleList(String path, double[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Double> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getDoubleList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final double[] res = new double[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).doubleValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default float[] getFloatList(String path, float[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Float> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getFloatList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final float[] res = new float[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).floatValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default int getInt(String path, int defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            return minigame.getConfig(configs.file()).getConfigurationSection(mpath).getInt(path, defaultValue);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default int[] getIntList(String path, int[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Integer> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getIntegerList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final int[] res = new int[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).intValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default long getLong(String path, long defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            return minigame.getConfig(configs.file()).getConfigurationSection(mpath).getLong(path, defaultValue);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default long[] getLongList(String path, long[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Long> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getLongList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final long[] res = new long[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).longValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default short[] getShortList(String path, short[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<Short> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getShortList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            final short[] res = new short[result.size()];
            for (int i = 0; i < res.length; i++)
            {
                res[i] = result.get(i).shortValue();
            }
            return res;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default String getString(String path, String defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            return minigame.getConfig(configs.file()).getConfigurationSection(mpath).getString(path, defaultValue);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path sub path of configuration section
     * @param defaultValue
     * 
     * @return value.
     */
    default String[] getStringList(String path, String[] defaultValue)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getField(((Enum<?>) this).name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = lib.getMinigameFromCfg(this);
            final String mpath = lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) this).name() : config.value()));
            final List<String> result = minigame.getConfig(configs.file()).getConfigurationSection(mpath).getStringList(path);
            if (result.size() == 0)
            {
                return defaultValue;
            }
            return result.toArray(new String[result.size()]);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
}
