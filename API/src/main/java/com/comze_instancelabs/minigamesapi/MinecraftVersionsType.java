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

/**
 * Supported versions of minecraft.
 *
 * @author mepeisen
 */
public enum MinecraftVersionsType
{
    /** an unknown version/ not supported. */
    Unknown(false),
    
    /** any 1.7 version. */
    V1_7(true),
    
    /** V1.7 R1 */
    V1_7_R1(true),
    
    /** V1.7 R2 */
    V1_7_R2(true),
    
    /** V1.7 R3 */
    V1_7_R3(true),
    
    /** V1.7 R4 */
    V1_7_R4(true),
    
    /** any 1.8 version. */
    V1_8(true),
    
    /** V1.8 R1 */
    V1_8_R1(true),
    
    /** V1.8 R2 */
    V1_8_R2(true),
    
    /** any 1.9 version. */
    V1_9(true),
    
    /** V1.9 R1 */
    V1_9_R1(true),
    
    /** V1.9 R2 */
    V1_9_R2(true),
    
    /** any 1.10 version. */
    V1_10(true),
    
    /** V1.10 R1 */
    V1_10_R1(true);
    
    /**
     * {@code true} if this version is still supported.
     */
    private final boolean isSupported;
    
    /**
     * Constructor to create a version.
     * 
     * @param supported
     *            true for support.
     */
    private MinecraftVersionsType(final boolean supported)
    {
        this.isSupported = supported;
    }
    
    /**
     * {@code true} if this version is still supported.
     * 
     * @return {@code true} if this version is still supported.
     */
    public boolean isSupported()
    {
        return this.isSupported;
    }
    
    /**
     * Checks if this version equals given version.
     * 
     * <p>
     * Notice: Pseudo versions (V1_7) will match every V1_7_R* version.
     * </p>
     * 
     * @param type
     *            version to compare to.
     * @return {@code true} if this version matches given version.
     */
    public boolean isEqual(final MinecraftVersionsType type)
    {
        switch (this)
        {
            case V1_10:
                return type == V1_10 || type == V1_10_R1;
            case V1_7:
                return type == V1_7 || type == V1_7_R1 || type == V1_7_R2 || type == V1_7_R3 || type == V1_7_R4;
            case V1_8:
                return type == V1_8 || type == V1_8_R1 || type == V1_8_R2;
            case V1_9:
                return type == V1_9 || type == V1_9_R1 || type == V1_9_R2;
                //$CASES-OMITTED$
            default:
                return type == this;
        }
    }
    
    /**
     * Checks if this version is below given version.
     * 
     * <ul>
     * <li>V1_7 will be below V1_8*.</li>
     * <li>V1_7_R3 will be below V1_7_R4.</li>
     * </ul>
     * 
     * @param type
     *            version to compare to.
     * @return {@code true} if this version matches given version.
     */
    public boolean isBelow(final MinecraftVersionsType type)
    {
        if (this.isEqual(type))
        {
            return false;
        }
        return this.ordinal() < type.ordinal();
    }
    
    /**
     * Checks if this version is after given version.
     * 
     * <ul>
     * <li>V1_8 will be after V1_7*.</li>
     * <li>V1_7_R4 will be after V1_7_R3.</li>
     * </ul>
     * 
     * @param type
     *            version to compare to.
     * @return {@code true} if this version matches given version.
     */
    public boolean isAfter(final MinecraftVersionsType type)
    {
        if (this.isEqual(type))
        {
            return false;
        }
        return this.ordinal() > type.ordinal();
    }
    
    /**
     * Checks if this version is at least given version.
     * 
     * <ul>
     * <li>V1_7_R4 will be at least V1_7.</li>
     * <li>V1_7_R3 will be after V1_7_R2.</li>
     * </ul>
     * 
     * @param type
     *            version to compare to.
     * @return {@code true} if this version matches given version.
     */
    public boolean isAtLeast(final MinecraftVersionsType type)
    {
        if (this.isEqual(type))
        {
            return true;
        }
        return this.ordinal() > type.ordinal();
    }
    
}
