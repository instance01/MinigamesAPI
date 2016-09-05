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

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.mce.minigames.api.MglibInterface;

/**
 * Helper class for configuration variables.
 * 
 * @author mepeisen
 */
class ConfigurationTool
{
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param clazz
     * @param calculator
     * @return calculator func.
     */
    static <Ret, Annot extends Annotation> Ret calculate(ConfigurationValueInterface val, Class<Annot> clazz, Calculator<Ret, Annot> calculator)
    {
        try
        {
            final ConfigurationValues configs = val.getClass().getAnnotation(ConfigurationValues.class);
            final Annot config = val.getClass().getDeclaredField(((Enum<?>) val).name()).getAnnotation(clazz);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final ConfigInterface minigame = lib.getConfigFromCfg(val);
            return calculator.supply(val, configs, config, lib, minigame);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param clazz
     * @param path
     * @param calculator
     * @return calculator func.
     */
    static <Ret, Annot extends Annotation> Ret calculate(ConfigurationValueInterface val, Class<Annot> clazz, PathCalculator<Annot> path, ValueCalculator<Ret, Annot> calculator)
    {
        final Calculator<Ret, Annot> calc = (val2, configs, config, lib, minigame) -> calculator.supply(val, configs, config, lib, minigame, path.supply(val, configs, config, lib));
        return calculate(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param clazz
     * @param path
     * @param calculator
     * @param defaultValue
     * @return calculator func.
     */
    static <Ret, Annot extends Annotation> Ret calculate(ConfigurationValueInterface val, Class<Annot> clazz, PathCalculator<Annot> path, ValueCalculator<Ret, Annot> calculator, ValueCalculator<Ret, Annot> defaultValue)
    {
        final Calculator<Ret, Annot> calc = (val2, configs, config, lib, minigame) -> {
            final String spath = path.supply(val, configs, config, lib);
            Ret res = minigame.getConfig(configs.file()).isSet(spath) ? calculator.supply(val, configs, config, lib, minigame, spath) : null;
            if (res == null)
            {
                res = defaultValue.supply(val, configs, config, lib, minigame, spath);
            }
            return res;
        };
        return calculate(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param subpath
     * @param calculator
     * @return calculator func.
     */
    static <Ret> Ret calculate(ConfigurationValueInterface val, String subpath, ValueCalculator<Ret, ConfigurationSection> calculator)
    {
        final Calculator<Ret, ConfigurationSection> calc = (val2, configs, config, lib, minigame) -> calculator.supply(val, configs, config, lib, minigame, sectionPath().supply(val, configs, config, lib) + '.' + subpath);
        return calculate(val, ConfigurationSection.class, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param retClazz
     * @param clazz
     * @param path
     * @param calculator
     * @return calculator func.
     */
    static <Ret, Annot extends Annotation> Ret[] calculateList(ConfigurationValueInterface val, Class<Annot> clazz, Class<Ret> retClazz, PathCalculator<Annot> path, ArrayValueCalculator<Ret, Annot> calculator)
    {
        @SuppressWarnings("unchecked")
        final Calculator<Ret[], Annot> calc = (val2, configs, config, lib, minigame) -> {
            final org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(path.supply(val, configs, config, lib));
            final List<Ret> list = new ArrayList<>();
            if (section != null)
            {
                for (final String key : section.getKeys(false))
                {
                    list.add(calculator.supply(val, configs, config, lib, minigame, section, key));
                }
            }
            return list.toArray((Ret[]) Array.newInstance(retClazz, list.size()));
        };
        return calculate(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param retClazz
     * @param subpath
     * @param calculator
     * @return calculator func.
     */
    static <Ret> Ret[] calculateList(ConfigurationValueInterface val, String subpath, Class<Ret> retClazz, ArrayValueCalculator<Ret, ConfigurationSection> calculator)
    {
        @SuppressWarnings("unchecked")
        final Calculator<Ret[], ConfigurationSection> calc = (val2, configs, config, lib, minigame) -> {
            final org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(sectionPath().supply(val, configs, config, lib) + '.' + subpath);
            final List<Ret> list = new ArrayList<>();
            if (section != null)
            {
                for (final String key : section.getKeys(false))
                {
                    list.add(calculator.supply(val, configs, config, lib, minigame, section, key));
                }
            }
            return list.toArray((Ret[]) Array.newInstance(retClazz, list.size()));
        };
        return calculate(val, ConfigurationSection.class, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param clazz
     * @param consumer
     */
    static <Annot extends Annotation> void consume(ConfigurationValueInterface val, Class<Annot> clazz, Consumer<Annot> consumer)
    {
        try
        {
            final ConfigurationValues configs = val.getClass().getAnnotation(ConfigurationValues.class);
            final Annot config = val.getClass().getDeclaredField(((Enum<?>) val).name()).getAnnotation(clazz);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final MglibInterface lib = MglibInterface.INSTANCE.get();
            final ConfigInterface minigame = lib.getConfigFromCfg(val);
            consumer.apply(val, configs, config, lib, minigame);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param clazz
     * @param path
     * @param consumer
     */
    static <Annot extends Annotation> void consume(ConfigurationValueInterface val, Class<Annot> clazz, PathCalculator<Annot> path, ValueConsumer<Annot> consumer)
    {
        final Consumer<Annot> calc = (val2, configs, config, lib, minigame) -> consumer.apply(val, configs, config, lib, minigame, path.supply(val, configs, config, lib));
        consume(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param subpath
     * @param consumer
     */
    static void consume(ConfigurationValueInterface val, String subpath, ValueConsumer<ConfigurationSection> consumer)
    {
        final Class<ConfigurationSection> clazz = ConfigurationSection.class;
        final Consumer<ConfigurationSection> calc = (val2, configs, config, lib, minigame) -> consumer.apply(val, configs, config, lib, minigame, sectionPath().supply(val, configs, config, lib) + '.' + subpath);
        consume(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param clazz
     * @param path
     * @param value
     * @param consumer
     */
    static <T, Annot extends Annotation> void consumeList(ConfigurationValueInterface val, Class<Annot> clazz, PathCalculator<Annot> path, T[] value, ArrayValueConsumer<T, Annot> consumer)
    {
        final ValueConsumer<Annot> vconsumer = (ConfigurationValueInterface val2, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame, String spath) -> {
            org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(spath);
            if (section == null)
            {
                section = minigame.getConfig(configs.file()).createSection(spath);
            }
            for (final String key : section.getKeys(false))
            {
                section.set(key, null);
            }
            int i = 0;
            for (T v : value)
            {
                consumer.apply(val, configs, config, lib, minigame, section, "e" + i, v); //$NON-NLS-1$
                i++;
            }
        };
        
        consume(val, clazz, path, vconsumer);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     * @param subpath
     * @param value
     * @param consumer
     */
    static <T> void consumeList(ConfigurationValueInterface val, String subpath, T[] value, ArrayValueConsumer<T, ConfigurationSection> consumer)
    {
        final ValueConsumer<ConfigurationSection> vconsumer = (ConfigurationValueInterface val2, ConfigurationValues configs, ConfigurationSection config, MglibInterface lib, ConfigInterface minigame, String spath) -> {
            org.bukkit.configuration.ConfigurationSection section = minigame.getConfig(configs.file()).getConfigurationSection(spath);
            if (section == null)
            {
                section = minigame.getConfig(configs.file()).createSection(spath);
            }
            for (final String key : section.getKeys(false))
            {
                section.set(key, null);
            }
            int i = 0;
            for (T v : value)
            {
                consumer.apply(val, configs, config, lib, minigame, section, "e" + i, v); //$NON-NLS-1$
                i++;
            }
        };
        
        consume(val, subpath, vconsumer);
    }
    
    /**
     * Checks if the given config value has given annotation.
     * 
     * @param val
     * @param clazz
     * @return {qcode true} if the config value has diven annotation
     */
    static boolean isType(ConfigurationValueInterface val, Class<? extends Annotation> clazz)
    {
        try
        {
            final Field field = val.getClass().getDeclaredField(((Enum<?>) val).name());
            return field.getAnnotation(clazz) != null;
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationSection> sectionPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.value().length() == 0 ? ((Enum<?>) val).name() : config.value()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationBool> boolPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationBoolList> boolListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationByte> bytePath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationByteList> byteListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationCharacter> charPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationCharacterList> charListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationColor> colorPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationColorList> colorListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationDouble> doublePath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationDoubleList> doubleListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationFloat> floatPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationFloatList> floatListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationInt> intPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationIntList> intListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationItemStack> itemStackPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationItemStackList> itemStackListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationLong> longPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationLongList> longListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationObject> objectPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationObjectList> objectListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationPlayer> playerPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationPlayerList> playerListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationString> stringPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationStringList> stringListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationShort> shortPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationShortList> shortListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationVector> vectorPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Returns the path calculator for given type
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationVectorList> vectorListPath()
    {
        return (val, configs, config, lib) -> lib.resolveContextVar(configs.path() + '.' + (config.name().length() == 0 ? ((Enum<?>) val).name() : config.name()));
    }
    
    /**
     * Calculator to fetch data.
     * 
     * @param <Ret>
     *            return clazz
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface Calculator<Ret, Annot extends Annotation>
    {
        
        /**
         * Calculates the value from config.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @param minigame
         * @return return value
         * @throws Exception
         */
        Ret supply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame) throws Exception;
        
    }
    
    /**
     * Calculator to fetch data.
     * 
     * @param <Ret>
     *            return clazz
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ValueCalculator<Ret, Annot extends Annotation>
    {
        
        /**
         * Calculates the value from config.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @param minigame
         * @param path
         * @return return value
         * @throws Exception
         */
        Ret supply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame, String path) throws Exception;
        
    }
    
    /**
     * Calculator to fetch data.
     * 
     * @param <Ret>
     *            return clazz
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ArrayValueCalculator<Ret, Annot extends Annotation>
    {
        
        /**
         * Calculates the value from config.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @param minigame
         * @param section
         * @param key
         * @return return value
         * @throws Exception
         */
        Ret supply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame, org.bukkit.configuration.ConfigurationSection section, String key) throws Exception;
        
    }
    
    /**
     * Calculator for config path calculation.
     * 
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface PathCalculator<Annot extends Annotation>
    {
        
        /**
         * Calculates the path from config annotation.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @return return value
         * @throws Exception
         */
        String supply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib) throws Exception;
        
    }
    
    /**
     * Calculator to parse data.
     * 
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface Consumer<Annot extends Annotation>
    {
        
        /**
         * Consume config.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @param minigame
         * @throws Exception
         */
        void apply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame) throws Exception;
        
    }
    
    /**
     * Calculator to parse data.
     * 
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ValueConsumer<Annot extends Annotation>
    {
        
        /**
         * Consume config.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @param minigame
         * @param path
         * @throws Exception
         */
        void apply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame, String path) throws Exception;
        
    }
    
    /**
     * Calculator to parse data.
     * 
     * @param <T>
     *            array element clazz
     * @param <Annot>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ArrayValueConsumer<T, Annot extends Annotation>
    {
        
        /**
         * Consume config.
         * 
         * @param val
         * @param configs
         * @param config
         * @param lib
         * @param minigame
         * @param section
         * @param path
         * @param element
         * @throws Exception
         */
        void apply(ConfigurationValueInterface val, ConfigurationValues configs, Annot config, MglibInterface lib, ConfigInterface minigame, org.bukkit.configuration.ConfigurationSection section, String path, T element)
                throws Exception;
        
    }
    
}
