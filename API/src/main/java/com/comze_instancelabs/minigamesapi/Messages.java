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

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Localized message helper.
 * 
 * @author mepeisen
 */
public class Messages
{
    
    /** the resource bundle name. */
    private static final String                      BUNDLE_NAME     = "com.comze_instancelabs.minigamesapi.messages"; //$NON-NLS-1$
    
    /** the default resource bundle; used as fallback. */
    private static final ResourceBundle              RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    
    /** the bundles by locales. */
    private static final Map<Locale, ResourceBundle> BUNDLES         = new ConcurrentHashMap<>();
    
    /**
     * hidden constructor.
     */
    private Messages()
    {
    }
    
    /**
     * Returns the localized string
     * 
     * @param key
     *            string key
     * @param locale
     *            locale to be used.
     * @return localized string.
     */
    public static String getString(String key, Locale locale)
    {
        try
        {
            final ResourceBundle res = BUNDLES.computeIfAbsent(locale, (l) -> {
                try
                {
                    return ResourceBundle.getBundle(BUNDLE_NAME, l);
                }
                catch (@SuppressWarnings("unused") MissingResourceException ex)
                {
                    return RESOURCE_BUNDLE;
                }
            });
            return res.getString(key);
        }
        catch (@SuppressWarnings("unused") MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
    
}
