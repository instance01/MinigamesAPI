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
package com.comze_instancelabs.minigamesapi;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

/**
 * Logging helper for arenas.
 * 
 * @author instancelabs
 */
public class ArenaLogger
{
    
    /** the plugin logger. */
    private Logger pluginLogger;
    
    /** the arena name to be used. */
    private String arenaName;
    
    /**
     * The plugin logger to be used.
     * 
     * @param logger
     *            logger to be used.
     * @param arenaName
     *            arena name to use for logging.
     */
    public ArenaLogger(Logger logger, String arenaName)
    {
        this.pluginLogger = logger;
        this.arenaName = arenaName;
    }
    
    /**
     * Returns the arena prefix string.
     * 
     * @return arena prefix string for logging.
     */
    private String getArenaPrefix()
    {
        return "[arena:" + this.arenaName + "] "; //$NON-NLS-1$//$NON-NLS-2$
    }
    
    /**
     * Log a SEVERE message.
     * <p>
     * If the logger is currently enabled for the SEVERE message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void severe(String msg)
    {
        log(Level.SEVERE, msg);
    }
    
    /**
     * Log a WARNING message.
     * <p>
     * If the logger is currently enabled for the WARNING message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void warning(String msg)
    {
        log(Level.WARNING, msg);
    }
    
    /**
     * Log an INFO message.
     * <p>
     * If the logger is currently enabled for the INFO message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void info(String msg)
    {
        log(Level.INFO, msg);
    }
    
    /**
     * Log a CONFIG message.
     * <p>
     * If the logger is currently enabled for the CONFIG message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void config(String msg)
    {
        log(Level.CONFIG, msg);
    }
    
    /**
     * Log a FINE message.
     * <p>
     * If the logger is currently enabled for the FINE message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void fine(String msg)
    {
        log(Level.FINE, msg);
    }
    
    /**
     * Log a FINER message.
     * <p>
     * If the logger is currently enabled for the FINER message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void finer(String msg)
    {
        log(Level.FINER, msg);
    }
    
    /**
     * Log a FINEST message.
     * <p>
     * If the logger is currently enabled for the FINEST message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void finest(String msg)
    {
        log(Level.FINEST, msg);
    }
    
    /**
     * Log a message, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void log(Level level, String msg)
    {
        this.pluginLogger.log(level, getArenaPrefix() + msg);
    }
    
    /**
     * Log a message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the given message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     */
    public void log(Level level, Supplier<String> msgSupplier)
    {
        this.pluginLogger.log(level, () -> (getArenaPrefix() + msgSupplier.get()));
    }
    
    /**
     * Log a message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message level then a corresponding LogRecord is created and forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param msg
     *            The string message (or a key in the message catalog)
     * @param param1
     *            parameter to the message
     */
    public void log(Level level, String msg, Object param1)
    {
        this.pluginLogger.log(level, getArenaPrefix() + msg, param1);
    }
    
    /**
     * Log a message, with an array of object arguments.
     * <p>
     * If the logger is currently enabled for the given message level then a corresponding LogRecord is created and forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param msg
     *            The string message (or a key in the message catalog)
     * @param params
     *            array of parameters to the message
     */
    public void log(Level level, String msg, Object params[])
    {
        this.pluginLogger.log(level, getArenaPrefix() + msg, params);
    }
    
    /**
     * Log a message, with associated Throwable information.
     * <p>
     * If the logger is currently enabled for the given message level then the given arguments are stored in a LogRecord which is forwarded to all registered output handlers.
     * <p>
     * Note that the thrown argument is stored in the LogRecord thrown property, rather than the LogRecord parameters property. Thus it is processed specially by output Formatters and is not treated
     * as a formatting parameter to the LogRecord message property.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param msg
     *            The string message (or a key in the message catalog)
     * @param thrown
     *            Throwable associated with log message.
     */
    public void log(Level level, String msg, Throwable thrown)
    {
        this.pluginLogger.log(level, getArenaPrefix() + msg, thrown);
    }
    
    /**
     * Log a lazily constructed message, with associated Throwable information.
     * <p>
     * If the logger is currently enabled for the given message level then the message is constructed by invoking the provided supplier function. The message and the given {@link Throwable} are then
     * stored in a {@link LogRecord} which is forwarded to all registered output handlers.
     * <p>
     * Note that the thrown argument is stored in the LogRecord thrown property, rather than the LogRecord parameters property. Thus it is processed specially by output Formatters and is not treated
     * as a formatting parameter to the LogRecord message property.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param thrown
     *            Throwable associated with log message.
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void log(Level level, Throwable thrown, Supplier<String> msgSupplier)
    {
        this.pluginLogger.log(level, thrown, () -> (getArenaPrefix() + msgSupplier.get()));
    }
    
    /**
     * Log a message, specifying source class and method, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that issued the logging request
     * @param msg
     *            The string message (or a key in the message catalog)
     */
    public void logp(Level level, String sourceClass, String sourceMethod, String msg)
    {
        this.pluginLogger.logp(level, sourceClass, sourceMethod, getArenaPrefix() + msg);
    }
    
    /**
     * Log a lazily constructed message, specifying source class and method, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that issued the logging request
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void logp(Level level, String sourceClass, String sourceMethod, Supplier<String> msgSupplier)
    {
        this.pluginLogger.logp(level, sourceClass, sourceMethod, () -> (getArenaPrefix() + msgSupplier.get()));
    }
    
    /**
     * Log a message, specifying source class and method, with a single object parameter to the log message.
     * <p>
     * If the logger is currently enabled for the given message level then a corresponding LogRecord is created and forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that issued the logging request
     * @param msg
     *            The string message (or a key in the message catalog)
     * @param param1
     *            Parameter to the log message.
     */
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1)
    {
        this.pluginLogger.logp(level, sourceClass, sourceMethod, getArenaPrefix() + msg, param1);
    }
    
    /**
     * Log a message, specifying source class and method, with an array of object arguments.
     * <p>
     * If the logger is currently enabled for the given message level then a corresponding LogRecord is created and forwarded to all the registered output Handler objects.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that issued the logging request
     * @param msg
     *            The string message (or a key in the message catalog)
     * @param params
     *            Array of parameters to the message
     */
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object params[])
    {
        this.pluginLogger.logp(level, sourceClass, sourceMethod, getArenaPrefix() + msg, params);
    }
    
    /**
     * Log a message, specifying source class and method, with associated Throwable information.
     * <p>
     * If the logger is currently enabled for the given message level then the given arguments are stored in a LogRecord which is forwarded to all registered output handlers.
     * <p>
     * Note that the thrown argument is stored in the LogRecord thrown property, rather than the LogRecord parameters property. Thus it is processed specially by output Formatters and is not treated
     * as a formatting parameter to the LogRecord message property.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that issued the logging request
     * @param msg
     *            The string message (or a key in the message catalog)
     * @param thrown
     *            Throwable associated with log message.
     */
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown)
    {
        this.pluginLogger.logp(level, sourceClass, sourceMethod, getArenaPrefix() + msg, thrown);
    }
    
    /**
     * Log a lazily constructed message, specifying source class and method, with associated Throwable information.
     * <p>
     * If the logger is currently enabled for the given message level then the message is constructed by invoking the provided supplier function. The message and the given {@link Throwable} are then
     * stored in a {@link LogRecord} which is forwarded to all registered output handlers.
     * <p>
     * Note that the thrown argument is stored in the LogRecord thrown property, rather than the LogRecord parameters property. Thus it is processed specially by output Formatters and is not treated
     * as a formatting parameter to the LogRecord message property.
     * <p>
     * 
     * @param level
     *            One of the message level identifiers, e.g., SEVERE
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that issued the logging request
     * @param thrown
     *            Throwable associated with log message.
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void logp(Level level, String sourceClass, String sourceMethod, Throwable thrown, Supplier<String> msgSupplier)
    {
        this.pluginLogger.logp(level, sourceClass, sourceMethod, thrown, () -> (getArenaPrefix() + msgSupplier.get()));
    }
    
    /**
     * Log a method entry.
     * <p>
     * This is a convenience method that can be used to log entry to a method. A LogRecord with message "ENTRY", log level FINER, and the given sourceMethod and sourceClass is logged.
     * <p>
     * 
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that is being entered
     */
    public void entering(String sourceClass, String sourceMethod)
    {
        this.pluginLogger.logp(Level.FINER, sourceClass, sourceMethod, getArenaPrefix() + "ENTRY"); //$NON-NLS-1$
    }
    
    /**
     * Log a method entry, with one parameter.
     * <p>
     * This is a convenience method that can be used to log entry to a method. A LogRecord with message "ENTRY {0}", log level FINER, and the given sourceMethod, sourceClass, and parameter is logged.
     * <p>
     * 
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that is being entered
     * @param param1
     *            parameter to the method being entered
     */
    public void entering(String sourceClass, String sourceMethod, Object param1)
    {
        this.pluginLogger.logp(Level.FINER, sourceClass, sourceMethod, getArenaPrefix() + "ENTRY {0}", param1); //$NON-NLS-1$
    }
    
    /**
     * Log a method entry, with an array of parameters.
     * <p>
     * This is a convenience method that can be used to log entry to a method. A LogRecord with message "ENTRY" (followed by a format {N} indicator for each entry in the parameter array), log level
     * FINER, and the given sourceMethod, sourceClass, and parameters is logged.
     * <p>
     * 
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of method that is being entered
     * @param params
     *            array of parameters to the method being entered
     */
    public void entering(String sourceClass, String sourceMethod, Object params[])
    {
        String msg = getArenaPrefix() + "ENTRY"; //$NON-NLS-1$
        if (params == null)
        {
            logp(Level.FINER, sourceClass, sourceMethod, msg);
            return;
        }
        if (!isLoggable(Level.FINER))
            return;
        for (int i = 0; i < params.length; i++)
        {
            msg = msg + " {" + i + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.pluginLogger.logp(Level.FINER, sourceClass, sourceMethod, msg, params);
    }
    
    /**
     * Log a method return.
     * <p>
     * This is a convenience method that can be used to log returning from a method. A LogRecord with message "RETURN", log level FINER, and the given sourceMethod and sourceClass is logged.
     * <p>
     * 
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of the method
     */
    public void exiting(String sourceClass, String sourceMethod)
    {
        this.pluginLogger.logp(Level.FINER, sourceClass, sourceMethod, getArenaPrefix() + "RETURN"); //$NON-NLS-1$
    }
    
    /**
     * Log a method return, with result object.
     * <p>
     * This is a convenience method that can be used to log returning from a method. A LogRecord with message "RETURN {0}", log level FINER, and the gives sourceMethod, sourceClass, and result object
     * is logged.
     * <p>
     * 
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of the method
     * @param result
     *            Object that is being returned
     */
    public void exiting(String sourceClass, String sourceMethod, Object result)
    {
        this.pluginLogger.logp(Level.FINER, sourceClass, sourceMethod, getArenaPrefix() + "RETURN {0}", result); //$NON-NLS-1$
    }
    
    /**
     * Log throwing an exception.
     * <p>
     * This is a convenience method to log that a method is terminating by throwing an exception. The logging is done using the FINER level.
     * <p>
     * If the logger is currently enabled for the given message level then the given arguments are stored in a LogRecord which is forwarded to all registered output handlers. The LogRecord's message
     * is set to "THROW".
     * <p>
     * Note that the thrown argument is stored in the LogRecord thrown property, rather than the LogRecord parameters property. Thus it is processed specially by output Formatters and is not treated
     * as a formatting parameter to the LogRecord message property.
     * <p>
     * 
     * @param sourceClass
     *            name of class that issued the logging request
     * @param sourceMethod
     *            name of the method.
     * @param thrown
     *            The Throwable that is being thrown.
     */
    public void throwing(String sourceClass, String sourceMethod, Throwable thrown)
    {
        if (!isLoggable(Level.FINER))
        {
            return;
        }
        LogRecord lr = new LogRecord(Level.FINER, getArenaPrefix() + "THROW"); //$NON-NLS-1$
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        lr.setThrown(thrown);
        this.pluginLogger.log(lr);
    }
    
    /**
     * Log a SEVERE message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the SEVERE message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void severe(Supplier<String> msgSupplier)
    {
        log(Level.SEVERE, msgSupplier);
    }
    
    /**
     * Log a WARNING message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the WARNING message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void warning(Supplier<String> msgSupplier)
    {
        log(Level.WARNING, msgSupplier);
    }
    
    /**
     * Log a INFO message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the INFO message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void info(Supplier<String> msgSupplier)
    {
        log(Level.INFO, msgSupplier);
    }
    
    /**
     * Log a CONFIG message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the CONFIG message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void config(Supplier<String> msgSupplier)
    {
        log(Level.CONFIG, msgSupplier);
    }
    
    /**
     * Log a FINE message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the FINE message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void fine(Supplier<String> msgSupplier)
    {
        log(Level.FINE, msgSupplier);
    }
    
    /**
     * Log a FINER message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the FINER message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void finer(Supplier<String> msgSupplier)
    {
        log(Level.FINER, msgSupplier);
    }
    
    /**
     * Log a FINEST message, which is only to be constructed if the logging level is such that the message will actually be logged.
     * <p>
     * If the logger is currently enabled for the FINEST message level then the message is constructed by invoking the provided supplier function and forwarded to all the registered output Handler
     * objects.
     * <p>
     * 
     * @param msgSupplier
     *            A function, which when called, produces the desired log message
     * @since 1.8
     */
    public void finest(Supplier<String> msgSupplier)
    {
        log(Level.FINEST, msgSupplier);
    }
    
    /**
     * Get the log Level that has been specified for this Logger. The result may be null, which means that this logger's effective level will be inherited from its parent.
     *
     * @return this Logger's level
     */
    public Level getLevel()
    {
        return this.pluginLogger.getLevel();
    }
    
    /**
     * Check if a message of the given level would actually be logged by this logger. This check is based on the Loggers effective level, which may be inherited from its parent.
     *
     * @param level
     *            a message logging level
     * @return true if the given message level is currently being logged.
     */
    public boolean isLoggable(Level level)
    {
        return this.pluginLogger.isLoggable(level);
    }
    
    /**
     * Debug some string.
     * 
     * @param msg
     *            debug message.
     */
    public static void debug(final String msg)
    {
        if (MinigamesAPI.debug)
        {
            Bukkit.getConsoleSender().sendMessage("[" + System.currentTimeMillis() + " MGLIB-DBG] " + msg); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
}
