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

package com.github.mce.minigames.api.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.MinigameErrorCode;

/**
 * test case for {@link MinigameErrorCode}
 * 
 * @author mepeisen
 */
public class MinigameErrorCodeTest
{
    
    /**
     * Tests {@link MinigameErrorCode#toName()}
     */
    @Test
    public void toNameTest()
    {
        assertEquals(CommonErrors.class.getName() + ".CannotStart", CommonErrors.CannotStart.toName()); //$NON-NLS-1$
    }
    
}
