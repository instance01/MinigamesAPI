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

package com.github.mce.minigames.impl.nms.v1_8_2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.github.mce.minigames.impl.nms.EventSystemInterface;
import com.github.mce.minigames.impl.nms.NmsFactory;

/**
 * Factory to create NMS relevant classes.
 * 
 * @author mepeisen
 */
public class NmsFactory1_8_2 implements NmsFactory
{
    
    /** the implementation classes. */
    private final Map<Class<?>, Supplier<?>> impls = new HashMap<>();
    
    /**
     * Constructor.
     */
    public NmsFactory1_8_2()
    {
        this.impls.put(EventSystemInterface.class, () -> new EventSystem1_8_2());
    }
    
    @Override
    public <T> T create(Class<T> clazz)
    {
        final Supplier<?> supplier = this.impls.get(clazz);
        if (supplier != null)
        {
            return clazz.cast(supplier.get());
        }
        return null;
    }
    
}
