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

/**
 * Utility class for messages.
 * 
 * @author mepeisen
 */
class MessageTool
{
    
    /**
     * Converts the given source array to serialized target array that can be passed to String.format
     * 
     * @param locale
     * @param isAdmin
     * @param src
     * @return converted array
     */
    public static Serializable[] convertArgs(Locale locale, boolean isAdmin, Serializable[] src)
    {
        final Serializable[] result = new Serializable[src.length];
        for (int i = 0; i < src.length; i++)
        {
            final Serializable srcelm = src[i];
            if (srcelm instanceof LocalizedMessageInterface.DynamicArg)
            {
                result[i] = ((LocalizedMessageInterface.DynamicArg) srcelm).apply(locale, isAdmin);
            }
            else if (srcelm instanceof LocalizedMessageInterface.DynamicListArg)
            {
                final StringBuilder builder = new StringBuilder();
                for (final String line : ((LocalizedMessageInterface.DynamicListArg) srcelm).apply(locale, isAdmin))
                {
                    if (builder.length() > 0)
                    {
                        builder.append("\n"); //$NON-NLS-1$
                    }
                    builder.append(line);
                }
                builder.append("\n"); //$NON-NLS-1$
                result[i] = builder.toString();
            }
            else
            {
                result[i] = src[i];
            }
        }
        return result;
    }
    
}
