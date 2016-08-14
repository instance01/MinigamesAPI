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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A single localized message.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface LocalizedMessage
{
    
    /**
     * Returns the default user message used as fallback; must be in locale returned by {@link LocalizedMessages#defaultLocale()}.
     * 
     * <p>
     * Uses {@link String#format(String, Object...)} to build the message with arguments.
     * </p>
     * 
     * @return default message.
     */
    String defaultMessage();
    
    /**
     * Returns the default administration message used as fallback; must be in locale returned by {@link LocalizedMessages#defaultLocale()}.
     * 
     * <p>
     * Uses {@link String#format(String, Object...)} to build the message with arguments.
     * </p>
     * 
     * @return default message; empty string if it should default to the user message
     */
    String defaultAdminMessage() default "";

    /**
     * Returns the message severity.
     * @return message severity.
     */
    MessageSeverityType severity() default MessageSeverityType.Information;
    
}
