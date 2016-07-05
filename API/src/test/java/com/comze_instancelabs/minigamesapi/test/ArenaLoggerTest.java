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

package com.comze_instancelabs.minigamesapi.test;

import static com.comze_instancelabs.minigamesapi.spigottest.ArgUtil.argEquals;
import static com.comze_instancelabs.minigamesapi.spigottest.ArgUtil.argSupplier;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.comze_instancelabs.minigamesapi.ArenaLogger;

/**
 * Test class for the arena logger.
 * 
 * @author mepeisen
 * @see ArenaLogger
 */
public class ArenaLoggerTest
{
    
    /** arena name */
    private static final String ARENA = "ARENA"; //$NON-NLS-1$
    /** some message */
    private static final String MESSAGE = "SOME-MESSAGE"; //$NON-NLS-1$
    /** resulting message */
    private static final String RESULT = "[arena:" + ARENA + "] " + MESSAGE; //$NON-NLS-1$ //$NON-NLS-2$
    /** resulting message */
    private static final String RESULT_ENTRY = "[arena:" + ARENA + "] ENTRY"; //$NON-NLS-1$ //$NON-NLS-2$
    /** resulting message */
    private static final String RESULT_RETURN = "[arena:" + ARENA + "] RETURN"; //$NON-NLS-1$ //$NON-NLS-2$
    /** resulting message */
    private static final String RESULT_THROW = "[arena:" + ARENA + "] THROW"; //$NON-NLS-1$ //$NON-NLS-2$
    /** source class for logp */
    static final String SOURCE_CLASS = ArenaLoggerTest.class.getName();
    /** source method for logp */
    static final String SOURCE_METHOD = "someMethod"; //$NON-NLS-1$

    /**
     * Tests log method
     */
    @Test
    public void testSevere()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.severe(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.SEVERE, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testSevereLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.severe(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.SEVERE), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testWarning()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.warning(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.WARNING, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testWarningLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.warning(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.WARNING), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testInfo()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.info(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.INFO, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testInfoLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.info(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.INFO), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testConfig()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.config(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.CONFIG, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testConfigLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.config(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.CONFIG), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testFine()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.fine(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.FINE, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testFineLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.fine(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.FINE), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testFiner()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.finer(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.FINER, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testFinerLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.finer(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.FINER), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testFinest()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.finest(MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.FINEST, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testFinestLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.finest(() -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.FINEST), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLog()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.log(Level.FINEST, MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(Level.FINEST, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.log(Level.FINEST, () -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.FINEST), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLog2()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object someObject = new Object();
        arenaLogger.log(Level.FINEST, MESSAGE, someObject);
        
        // epilog
        verify(logger, times(1)).log(Level.FINEST, RESULT, someObject);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLog3()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object[] someObject = new Object[]{};
        arenaLogger.log(Level.FINEST, MESSAGE, someObject);
        
        // epilog
        verify(logger, times(1)).log(Level.FINEST, RESULT, someObject);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLog4()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Throwable someObject = new Exception();
        arenaLogger.log(Level.FINEST, MESSAGE, someObject);
        
        // epilog
        verify(logger, times(1)).log(Level.FINEST, RESULT, someObject);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLog4Lambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Throwable someObject = new Exception();
        arenaLogger.log(Level.FINEST, someObject, () -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).log(argEquals(Level.FINEST), argEquals(someObject), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogp()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, MESSAGE);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, RESULT);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogpLambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, () -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).logp(argEquals(Level.FINEST), argEquals(SOURCE_CLASS), argEquals(SOURCE_METHOD), argSupplier(RESULT));
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogp2()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object someObject = new Object();
        arenaLogger.logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, MESSAGE, someObject);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, RESULT, someObject);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogp3()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object[] someObject = new Object[]{};
        arenaLogger.logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, MESSAGE, someObject);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, RESULT, someObject);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogp4()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Throwable someObject = new Exception();
        arenaLogger.logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, MESSAGE, someObject);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, RESULT, someObject);
    }
    
    /**
     * Tests log method
     */
    @Test
    public void testLogp4Lambda()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Throwable someObject = new Exception();
        arenaLogger.logp(Level.FINEST, SOURCE_CLASS, SOURCE_METHOD, someObject, () -> MESSAGE);
        
        // epilog
        verify(logger, times(1)).logp(argEquals(Level.FINEST), argEquals(SOURCE_CLASS), argEquals(SOURCE_METHOD), argEquals(someObject), argSupplier(RESULT));
    }
    
    /**
     * Tests entering method
     */
    @Test
    public void testEntering()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.entering(SOURCE_CLASS, SOURCE_METHOD);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINER, SOURCE_CLASS, SOURCE_METHOD, RESULT_ENTRY);
    }
    
    /**
     * Tests entering method
     */
    @Test
    public void testEntering2()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object someObject = new Object();
        arenaLogger.entering(SOURCE_CLASS, SOURCE_METHOD, someObject);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINER, SOURCE_CLASS, SOURCE_METHOD, RESULT_ENTRY + " {0}", someObject); //$NON-NLS-1$
    }
    
    /**
     * Tests entering method
     */
    @Test
    public void testEntering3()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        when(logger.isLoggable(any())).thenReturn(Boolean.TRUE);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object[] someObject = new Object[]{new Object(), new Object()};
        arenaLogger.entering(SOURCE_CLASS, SOURCE_METHOD, someObject);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINER, SOURCE_CLASS, SOURCE_METHOD, RESULT_ENTRY + " {0} {1}", someObject); //$NON-NLS-1$
    }
    
    /**
     * Tests exiting method
     */
    @Test
    public void testExiting()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        arenaLogger.exiting(SOURCE_CLASS, SOURCE_METHOD);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINER, SOURCE_CLASS, SOURCE_METHOD, RESULT_RETURN);
    }
    
    /**
     * Tests exiting method
     */
    @Test
    public void testExiting2()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Object someObject = new Object();
        arenaLogger.exiting(SOURCE_CLASS, SOURCE_METHOD, someObject);
        
        // epilog
        verify(logger, times(1)).logp(Level.FINER, SOURCE_CLASS, SOURCE_METHOD, RESULT_RETURN + " {0}", someObject); //$NON-NLS-1$
    }
    
    /**
     * Tests throwing method
     */
    @Test
    public void testThrowing()
    {
        // prolog
        final Logger logger = mock(Logger.class);
        when(logger.isLoggable(any())).thenReturn(Boolean.TRUE);
        
        // test
        final ArenaLogger arenaLogger = new ArenaLogger(logger, ARENA);
        final Throwable someObject = new Exception();
        arenaLogger.throwing(SOURCE_CLASS, SOURCE_METHOD, someObject);
        
        // epilog
        verify(logger, times(1)).log(argThat(new ArgumentMatcher<LogRecord>() {

            @Override
            public boolean matches(Object argument)
            {
                final LogRecord lr = (LogRecord) argument;
                final boolean m1 = lr.getSourceClassName().equals(SOURCE_CLASS);
                final boolean m2 = lr.getSourceMethodName().equals(SOURCE_METHOD);
                final boolean m3 = lr.getThrown().equals(someObject);
                final boolean m4 = RESULT_THROW.equals(lr.getMessage());
                return m1 && m2 && m3 && m4(lr) && m4;
            }

            private boolean m4(final LogRecord lr)
            {
                return Level.FINER == lr.getLevel();
            }
        }));
    }
    
}
