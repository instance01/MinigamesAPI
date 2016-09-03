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

/**
 * An interface for enumerations that represent localized messages.
 * 
 * @author mepeisen
 */
public interface LocalizedMessageInterface extends Serializable
{
    
    /**
     * Checks if this is a single line message.
     * 
     * @return {@code true} for single line messages.
     */
    default boolean isSingleLine()
    {
        try
        {
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessage.class);
            return msg != null;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Checks if this is a multi line message.
     * 
     * @return {@code true} for multi line messages.
     */
    default boolean isMultiLine()
    {
        try
        {
            final LocalizedMessageList msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessageList.class);
            return msg != null;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the message severity type.
     * 
     * @return severity type.
     */
    default MessageSeverityType getSeverity()
    {
        try
        {
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessage.class);
            return msg == null ? MessageSeverityType.Information : msg.severity();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns a human readable text for this message; this message will be displayed to common users.
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
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessage.class);
            if (msgs == null || msg == null)
            {
                throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
            }
            final MglibInterface mglib = MglibInterface.INSTANCE.get();
            final MessagesConfigInterface messages = mglib.getMessagesFromMsg(this);
            if (messages == null)
            {
                throw new IllegalStateException("minigame not found or inactive."); //$NON-NLS-1$
            }
            
            final String smsg = messages.getString(locale, msgs.value() + "." + ((Enum<?>) this).name(), msg.defaultMessage()); //$NON-NLS-1$
            return String.format(locale, smsg, (Object[]) MessageTool.convertArgs(locale, false, args));
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns an array of human readable texts for this message; this message will be displayed to common users.
     * 
     * @param locale
     *            locale to be used.
     * @param args
     *            object arguments that can be used to build the message.
     * @return message string array.
     */
    default String[] toUserMessageLine(Locale locale, Serializable... args)
    {
        try
        {
            final LocalizedMessages msgs = this.getClass().getAnnotation(LocalizedMessages.class);
            final LocalizedMessageList msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessageList.class);
            if (msgs == null || msg == null)
            {
                throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
            }
            final MglibInterface mglib = MglibInterface.INSTANCE.get();
            final MessagesConfigInterface messages = mglib.getMessagesFromMsg(this);
            if (messages == null)
            {
                throw new IllegalStateException("minigame not found or inactive."); //$NON-NLS-1$
            }
            
            final String[] smsg = messages.getStringList(locale, msgs.value() + "." + ((Enum<?>) this).name(), msg.value()); //$NON-NLS-1$
            final String[] result = new String[smsg.length];
            int i = 0;
            for (final String lmsg : smsg)
            {
                result[i] = String.format(locale, lmsg, (Object[]) MessageTool.convertArgs(locale, false, args));
                i++;
            }
            return result;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns an array of human readable texts for this message; the message will be displayed to administrators only.
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
            final LocalizedMessage msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessage.class);
            if (msgs == null || msg == null)
            {
                throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
            }
            final MglibInterface mglib = MglibInterface.INSTANCE.get();
            final MessagesConfigInterface messages = mglib.getMessagesFromMsg(this);
            if (messages == null)
            {
                throw new IllegalStateException("minigame not found or inactive."); //$NON-NLS-1$
            }
            
            String smsg = messages.getAdminString(locale, msgs.value() + "." + ((Enum<?>) this).name(), msg.defaultAdminMessage()); //$NON-NLS-1$
            if (smsg.length() == 0)
            {
                smsg = messages.getString(locale, msgs.value() + "." + ((Enum<?>) this).name(), msg.defaultMessage()); //$NON-NLS-1$
            }
            return String.format(locale, smsg, (Object[]) MessageTool.convertArgs(locale, false, args));
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns an array of human readable texts for this message; the message will be displayed to administrators only.
     * 
     * @param locale
     *            locale to be used.
     * @param args
     *            object arguments that can be used to build the message.
     * @return message string.
     */
    default String[] toAdminMessageLine(Locale locale, Serializable... args)
    {
        try
        {
            final LocalizedMessages msgs = this.getClass().getAnnotation(LocalizedMessages.class);
            final LocalizedMessageList msg = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(LocalizedMessageList.class);
            if (msgs == null || msg == null)
            {
                throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
            }
            final MglibInterface mglib = MglibInterface.INSTANCE.get();
            final MessagesConfigInterface messages = mglib.getMessagesFromMsg(this);
            if (messages == null)
            {
                throw new IllegalStateException("minigame not found or inactive."); //$NON-NLS-1$
            }
            
            String[] smsg = messages.getAdminStringList(locale, msgs.value() + "." + ((Enum<?>) this).name(), msg.adminMessages().length == 0 ? null : msg.adminMessages()); //$NON-NLS-1$
            if (smsg == null || smsg.length == 0)
            {
                smsg = messages.getStringList(locale, msgs.value() + "." + ((Enum<?>) this).name(), msg.value()); //$NON-NLS-1$
            }
            final String[] result = new String[smsg.length];
            int i = 0;
            for (final String lmsg : smsg)
            {
                result[i] = String.format(locale, lmsg, (Object[]) MessageTool.convertArgs(locale, false, args));
                i++;
            }
            return result;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Converts this message to a string function
     * 
     * @param args
     *            arguments to use.
     * @return ths string function
     */
    default DynamicArg toArg(Serializable... args)
    {
        return (loc, isAdmin) -> isAdmin ? this.toAdminMessage(loc, args) : this.toUserMessage(loc, args);
    }
    
    /**
     * Converts this message to a string function
     * 
     * @param startLine
     *            starting line
     * @param lineLimit
     *            limit of lines
     * @param args
     *            arguments to use.
     * @return ths string function
     */
    default DynamicListArg toListArg(int startLine, int lineLimit, Serializable... args)
    {
        return (loc, isAdmin) -> isAdmin ? this.toAdminMessageLine(loc, args) : this.toUserMessageLine(loc, args);
    }
    
    /**
     * Converts this message to a string function
     * 
     * @param args
     *            arguments to use.
     * @return ths string function
     */
    default DynamicListArg toListArg(Serializable... args)
    {
        return (loc, isAdmin) -> isAdmin ? this.toAdminMessageLine(loc, args) : this.toUserMessageLine(loc, args);
    }
    
    /**
     * Helper interface for dynamic arguments.
     */
    @FunctionalInterface
    interface DynamicArg extends BiFunction<Locale, Boolean, String>, Serializable
    {
        // marker only
    }
    
    /**
     * Helper interface for dynamic arguments.
     */
    @FunctionalInterface
    interface DynamicListArg extends BiFunction<Locale, Boolean, String[]>, Serializable
    {
        // marker only
    }
    
}
