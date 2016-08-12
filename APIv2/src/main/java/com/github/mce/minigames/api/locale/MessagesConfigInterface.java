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

import java.util.Locale;

/**
 * Helper interface for accessing messages configuration files.
 * 
 * @author mepeisen
 */
public interface MessagesConfigInterface
{
    
    /**
     * Returns a string configuration for given option path.
     * 
     * @param locale
     *            the locale to be used
     * @param path
     *            configuration option path
     * @param defaultValue
     *            default value to return
     * @return option value.
     */
    String getString(Locale locale, String path, String defaultValue);
    
    /**
     * Returns a string configuration for given option path.
     * 
     * @param locale
     *            the locale to be used
     * @param path
     *            configuration option path
     * @param defaultValue
     *            default value to return
     * @return option value.
     */
    String getAdminString(Locale locale, String path, String defaultValue);
    
}
