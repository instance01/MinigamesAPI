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

package com.github.mce.minigames.api.perms;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A list of permissions; should be used by enumerations that implement the {@link PermissionsInterface}.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Permissions
{
    
    /**
     * The default path used as a prefix for the permissions.
     * 
     * <p>
     * Within path the following variables can be used:
     * </p>
     * 
     * <ul>
     * <li>"$MGLIB$" : will be replaced with minigame libs permission prefix.</li>
     * <li>"$MINIGAME$" : will be replaced by the current minigame name.</li>
     * <li>"$ARENA$" : will be replaces by the current arena name.</li>
     * </ul>
     * 
     * @return default path used as a prefix.
     */
    String value();
    
}
