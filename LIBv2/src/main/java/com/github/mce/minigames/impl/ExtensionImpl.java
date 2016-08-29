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

package com.github.mce.minigames.impl;

import java.io.Serializable;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.services.MinigameExtensionInterface;
import com.github.mce.minigames.api.services.MinigameExtensionProviderInterface;

/**
 * @author mepeisen
 *
 */
class ExtensionImpl extends BaseImpl implements MinigameExtensionInterface
{
    
    /** the extension name. */
    private final String name;
    
    /** the extension description. */
    private Serializable description;
    
    /**
     * Constructor.
     * 
     * @param name
     * @param mgplugin
     * @param provider
     */
    public ExtensionImpl(MinigamesPlugin mgplugin, String name, MinigameExtensionProviderInterface provider)
    {
        super(mgplugin, provider.getJavaPlugin());
        this.name = name;
        this.description = provider.getShortDescription();
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public Serializable getShortDescription()
    {
        return this.description;
    }

    /* (non-Javadoc)
     * @see com.github.mce.minigames.api.services.MinigameExtensionInterface#disable()
     */
    @Override
    public void disable() throws MinigameException
    {
        // TODO Auto-generated method stub
        
    }
    
}
