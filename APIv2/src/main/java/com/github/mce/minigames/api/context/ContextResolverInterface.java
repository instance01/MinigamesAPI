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

package com.github.mce.minigames.api.context;

/**
 * An interface being able to resolve variables with contexts.
 * 
 * @author mepeisen
 */
public interface ContextResolverInterface
{
    
    /**
     * Tries to resolve given variable name.
     * 
     * @param varName
     *            variable name to resolve.
     * @param args
     *            arguments for resolve
     * @param context
     *            the context
     * @return the resolved string or {@code null} if the variable cannot be resolved.
     */
    String resolve(String varName, String[] args, MinigameContext context);
    
}
