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

package com.comze_instancelabs.minigamesapi.testutil;

import static org.mockito.Matchers.argThat;

import java.util.function.Supplier;

import org.mockito.ArgumentMatcher;

/**
 * @author mepeisen
 *
 */
public class ArgUtil
{
    
    /**
     * Returns a argument matcher that matches a value argument
     * @param expected expected value
     * @return matcher
     */
    public static <T> T argEquals(T expected)
    {
        return argThat(new ArgumentMatcher<T>() {

            @Override
            public boolean matches(Object argument)
            {
                @SuppressWarnings("unchecked")
                final T actual = (T) argument;
                return expected.equals(actual);
            }
            
        });
    }
    
    /**
     * Returns a argument matcher that matches a lambda supplier argument
     * @param expected expected value returned by supplier
     * @return matcher
     */
    public static <T> Supplier<T> argSupplier(T expected)
    {
        return argThat(new ArgumentMatcher<Supplier<T>>() {

            @Override
            public boolean matches(Object argument)
            {
                @SuppressWarnings("unchecked")
                final T actual = ((Supplier<T>) argument).get();
                return expected.equals(actual);
            }
            
        });
    }
    
}
