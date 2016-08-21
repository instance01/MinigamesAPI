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
import java.util.Locale;
import java.util.function.BiFunction;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;

/**
 * An interface for enumerations that represent localized messages.
 * 
 * @author mepeisen
 */
public interface LocalizedMessageInterface extends Serializable
{
    
    /**
     * Returns the message severity type.
     * @return severity type.
     */
    default MessageSeverityType getSeverity()
    {
        try
        {
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>)this).name()).getAnnotation(LocalizedMessage.class);
            return msg.severity();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns a human readable message for this message; this message will be displayed to common users.
     * 
     * @param locale
     *            locale to be used.
     * @param args
     *            object arguments that can be used to build the message.
     * @return message string.
     */
    default String toUserMessage(Locale locale, Serializable... args)
    {
        try
        {
            final LocalizedMessages msgs = this.getClass().getAnnotation(LocalizedMessages.class);
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>)this).name()).getAnnotation(LocalizedMessage.class);
            if (msgs == null || msg == null)
            {
                throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
            }
            final MglibInterface mglib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = mglib.getMinigameFromMsg(this);
            if (minigame == null)
            {
                throw new IllegalStateException("minigame not found or inactive."); //$NON-NLS-1$
            }
            
            final String smsg = minigame.getMessages().getString(locale, msgs.value() + "." + ((Enum<?>)this).name(), msg.defaultMessage()); //$NON-NLS-1$
            // TODO Convert DynamicArg, see toArg()
            return String.format(smsg, (Object[])args);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns a human readable message for this message; the message will be displayed to administrators only.
     * 
     * @param locale
     *            locale to be used.
     * @param args
     *            object arguments that can be used to build the message.
     * @return message string.
     */
    default String toAdminMessage(Locale locale, Serializable... args)
    {
        try
        {
            final LocalizedMessages msgs = this.getClass().getAnnotation(LocalizedMessages.class);
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>)this).name()).getAnnotation(LocalizedMessage.class);
            if (msgs == null || msg == null)
            {
                throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
            }
            final MglibInterface mglib = MglibInterface.INSTANCE.get();
            final MinigameInterface minigame = mglib.getMinigameFromMsg(this);
            if (minigame == null)
            {
                throw new IllegalStateException("minigame not found or inactive."); //$NON-NLS-1$
            }
            
            String smsg = minigame.getMessages().getAdminString(locale, msgs.value() + "." + ((Enum<?>)this).name(), msg.defaultAdminMessage()); //$NON-NLS-1$
            if (smsg.length() == 0)
            {
                smsg = minigame.getMessages().getString(locale, msgs.value() + "." + ((Enum<?>)this).name(), msg.defaultMessage()); //$NON-NLS-1$
            }
            // TODO Convert DynamicArg, see toArg()
            return String.format(smsg, (Object[])args);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Converts this message to a string function
     * @param args arguments to use.
     * @return ths string function
     */
    default DynamicArg toArg(Serializable... args)
    {
        return (loc, isAdmin) -> isAdmin ? this.toAdminMessage(loc, args) : this.toUserMessage(loc, args);
    }
    
    /**
     * Helper interface for dynamic arguments.
     */
    @FunctionalInterface
    interface DynamicArg extends BiFunction<Locale, Boolean, String>, Serializable
    {
        // marker only
    }
    
}
