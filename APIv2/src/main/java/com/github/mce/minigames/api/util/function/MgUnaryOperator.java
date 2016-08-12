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

package com.github.mce.minigames.api.util.function;

/**
 * Similar to {@link java.util.function.UnaryOperator} but is able to throw MinigameExceptions.
 * 
 * @author mepeisen
 * 
 * @param <T>
 *            the type of the operand and result of the operator
 */
@FunctionalInterface
public interface MgUnaryOperator<T> extends MgFunction<T, T>
{
    
    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @param <T>
     *            the type of the input and output of the operator
     * @return a unary operator that always returns its input argument
     */
    static <T> MgUnaryOperator<T> identity()
    {
        return t -> t;
    }
    
}
