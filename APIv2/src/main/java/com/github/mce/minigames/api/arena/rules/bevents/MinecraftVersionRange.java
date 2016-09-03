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

package com.github.mce.minigames.api.arena.rules.bevents;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.mce.minigames.api.MinecraftVersionsType;

/**
 * Annotation to indicate a version range for minecraft events.
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MinecraftVersionRange
{
    
    /**
     * Minimum version for minecraft servers.
     * @return minimum versions.
     */
    MinecraftVersionsType min() default MinecraftVersionsType.Unknown;
    
    /**
     * maximum version for minecraft servers.
     * @return maximum version.
     */
    MinecraftVersionsType max() default MinecraftVersionsType.Unknown;
    
}
