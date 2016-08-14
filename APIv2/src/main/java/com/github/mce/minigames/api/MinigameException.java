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

package com.github.mce.minigames.api;

import java.io.Serializable;

/**
 * A minigame exception.
 * 
 * @author mepeisen
 */
public class MinigameException extends Exception
{
    
    /**
     * serial version uid.
     */
    private static final long       serialVersionUID = 4482601783924644721L;
    
    /** the error code. */
    private final MinigameErrorCode code;
    
    /** the message arguments. */
    private final Serializable[]    args;
    
    /**
     * Constructor.
     * 
     * @param code
     *            the error code
     * @param args
     *            the arguments for building the message.
     */
    public MinigameException(MinigameErrorCode code, Serializable... args)
    {
        super(toString(code, args));
        this.code = code;
        this.args = args;
    }
    
    /**
     * Constructor.
     * 
     * @param code
     *            the error code
     * @param cause
     *            the underlying exception (cause)
     * @param args
     *            the arguments for building the message.
     */
    public MinigameException(MinigameErrorCode code, Throwable cause, Serializable... args)
    {
        super(toString(code, args), cause);
        this.code = code;
        this.args = args;
    }
    
    /**
     * The error code
     * 
     * @return the code
     */
    public MinigameErrorCode getCode()
    {
        return this.code;
    }
    
    /**
     * the arguments to build a human readable string
     * 
     * @return the args
     */
    public Serializable[] getArgs()
    {
        return this.args;
    }
    
    /**
     * Converts given code and args to a loggable text.
     * 
     * @param code2
     * @param args2
     * @return exception string for logging.
     */
    private static String toString(MinigameErrorCode code2, Serializable[] args2)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(code2.toName());
        builder.append(", args: ["); //$NON-NLS-1$
        if (args2 != null)
        {
            for (final Serializable arg : args2)
            {
                builder.append("\n  "); //$NON-NLS-1$
                builder.append(arg);
            }
        }
        builder.append("]"); //$NON-NLS-1$
        return builder.toString();
    }
    
}
