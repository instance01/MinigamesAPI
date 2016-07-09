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

package com.comze_instancelabs.minigamesapi.util;

import java.util.Arrays;

/**
 * Utility class for signs.
 * 
 * @author mepeisen
 */
public class Signs
{
    
    /** a string of sqaures. */
    private static final String squares;
    
    /** mid size squares. */
    private static final char[] squares_mid    = new char[10];
    /** full size squares. */
    private static final char[] squares_full   = new char[10];
    /** medium size squares. */
    private static final char[] squares_medium = new char[10];
    /** light size squares. */
    private static final char[] squares_light  = new char[10];
    
    /** mid size squares. */
    private static final String ssquares_mid;
    /** full size squares. */
    private static final String ssquares_full;
    /** medium size squares. */
    private static final String ssquares_medium;
    /** light size squares. */
    private static final String ssquares_light;
    
    static
    {
        Arrays.fill(squares_mid, (char) 0x25A0);
        Arrays.fill(squares_full, (char) 0x2588);
        Arrays.fill(squares_medium, (char) 0x2592);
        Arrays.fill(squares_light, (char) 0x2591);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 11; i++)
        {
            builder.append((char) 0x25A0);
        }
        squares = builder.toString();
        ssquares_mid = new String(squares_mid);
        ssquares_full = new String(squares_full);
        ssquares_medium = new String(squares_medium);
        ssquares_light = new String(squares_light);
    }
    
    /**
     * Formats the source string from messages config to be used ingame.
     * @param src source string
     * @return formatted string.
     */
    public static final String format(String src)
    {
        return replaceSquares(replaceColorCodes(src));
    }
    
    /**
     * Replaces color codes from message strings.
     * @param src source string
     * @return formatted string.
     */
    public static final String replaceColorCodes(String src)
    {
        return src.replaceAll("&", "§"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Replaces "[...]" characters with squares of different sizes.
     * @param src source string.
     * @return formatted string.
     */
    public static final String replaceSquares(String src)
    {
        return src.replace("[]", new String(ssquares_mid)). //$NON-NLS-1$
                replace("[1]", new String(ssquares_full). //$NON-NLS-1$
                replace("[2]", new String(ssquares_medium)). //$NON-NLS-1$
                replace("[3]", new String(ssquares_light))); //$NON-NLS-1$
    }
    
}
