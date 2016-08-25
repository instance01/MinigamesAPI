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

/**
 * An interface for enumerations that represent list of permissions.
 * 
 * @author mepeisen
 */
public interface PermissionsInterface
{
    
    /**
     * Returns the full permission name.
     * 
     * @return full permission name.
     */
    default String fullPath()
    {
        try
        {
            final Permissions permissions = this.getClass().getAnnotation(Permissions.class);
            final Permission perm = this.getClass().getDeclaredField(((Enum<?>) this).name()).getAnnotation(Permission.class);
            if (permissions == null || perm == null)
            {
                throw new IllegalStateException("Invalid permission class."); //$NON-NLS-1$
            }
            return permissions.value() + '.' + (perm.value().length() == 0 ? ((Enum<?>) this).name() : perm.value());
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
}
