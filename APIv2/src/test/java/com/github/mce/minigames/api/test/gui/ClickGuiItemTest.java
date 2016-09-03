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

package com.github.mce.minigames.api.test.gui;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.gui.ClickGuiItem;
import com.github.mce.minigames.api.locale.LocalizedMessageInterface;

/**
 * test case for {@link ClickGuiItem}
 * 
 * @author mepeisen
 */
public class ClickGuiItemTest
{
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(org.bukkit.inventory.ItemStack, com.github.mce.minigames.api.locale.LocalizedMessageInterface, com.github.mce.minigames.api.gui.ClickGuiItem.GuiItemHandler)}
     */
    @Test
    public void constructorTest()
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        final ClickGuiItem.GuiItemHandler handler = mock(ClickGuiItem.GuiItemHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, handler);
        
        verify(item, times(1)).clone();
        
        assertSame(item, guiItem.getItemStack());
        assertSame(name, guiItem.getDisplayName());
    }
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(org.bukkit.inventory.ItemStack, com.github.mce.minigames.api.locale.LocalizedMessageInterface, com.github.mce.minigames.api.gui.ClickGuiItem.GuiItemHandler)}
     * @throws MinigameException thrown on errors
     */
    @Test
    public void handlerTest() throws MinigameException
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        final ClickGuiItem.GuiItemHandler handler = mock(ClickGuiItem.GuiItemHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, handler);
        guiItem.handle(null, null, null);
        
        verify(handler, times(1)).handle(null, null, null);
    }
    
}
