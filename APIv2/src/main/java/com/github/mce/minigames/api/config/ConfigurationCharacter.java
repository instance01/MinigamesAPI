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

package com.github.mce.minigames.api.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A single configuration value.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ConfigurationCharacter
{
    
    /**
     * The name of the configuration value.
     * 
     * <p>
     * The name is appended to the path of the {@link ConfigurationValues} annotation on the enum class.
     * </p>
     * 
     * @return Name of configuration value; empty string to use the constant name.
     */
    String name() default "";
    
    /**
     * The default value of this configuration option.
     * 
     * @return default value.
     */
    char defaultValue() default ' ';
    
}
