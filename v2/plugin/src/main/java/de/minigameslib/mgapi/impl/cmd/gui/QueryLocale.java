/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.cmd.gui;

import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AnvilGuiId;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * Query a locale.
 * 
 * @author mepeisen
 */
public class QueryLocale implements AnvilGuiInterface
{
    
    /** cancel func */
    private McRunnable onCancel;
    
    /** input func */
    private McConsumer<Locale> onInput;
    
    /** description */
    private String[] description;
    
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(QueryLocale.class.getName());

    /**
     * Constructor
     * @param onCancel
     * @param onInput
     * @param description 
     */
    public QueryLocale(McRunnable onCancel, McConsumer<Locale> onInput, String[] description)
    {
        this.onCancel = onCancel;
        this.onInput = onInput;
        this.description = description;
    }

    @Override
    public ItemStack getItem()
    {
        final ItemStack stack = ItemServiceInterface.instance().createItem(CommonItems.App_Text, "iso-code"); //$NON-NLS-1$
        final ItemMeta meta = stack.getItemMeta();
        meta.setLore(Arrays.asList(this.description));
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public AnvilGuiId getUniqueId()
    {
        return MgAnvilGuis.QueryLocale;
    }

    @Override
    public void onCancel()
    {
        try
        {
            if (this.onCancel != null)
            {
                this.onCancel.run();
            }
        }
        catch (McException e)
        {
            LOGGER.log(Level.WARNING, "Problems on cancelling query locale", e); //$NON-NLS-1$
        }
    }

    @Override
    public void onInput(String arg0) throws McException
    {
        this.onInput.accept(new Locale(arg0));
    }
    
}
