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

package de.minigameslib.mgapi.impl.cmd.marker;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;

/**
 * A helper class to create marker colors
 * 
 * @author mepeisen
 */
public class MarkerColorProvider
{
    
    /**
     * Returns the available colors.
     * @return available colors.
     */
    public static List<MarkerColorInterface> getColors()
    {
        return Arrays.asList(MarkerColor.values());
    }
    
    /**
     * The marker colors.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.marker_colors")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * white marker color
         */
        @LocalizedMessage(defaultMessage = "white")
        @MessageComment({"white marker color"})
        White,
        /**
         * orange marker color
         */
        @LocalizedMessage(defaultMessage = "orange")
        @MessageComment({"orange marker color"})
        Orange,
        /**
         * magenta marker color
         */
        @LocalizedMessage(defaultMessage = "magenta")
        @MessageComment({"magenta marker color"})
        Magenta,
        /**
         * light blue marker color
         */
        @LocalizedMessage(defaultMessage = "light blue")
        @MessageComment({"light blue marker color"})
        LightBlue,
        /**
         * yellow marker color
         */
        @LocalizedMessage(defaultMessage = "yellow")
        @MessageComment({"yellow marker color"})
        Yellow,
        /**
         * lime marker color
         */
        @LocalizedMessage(defaultMessage = "lime")
        @MessageComment({"lime marker color"})
        Lime,
        /**
         * pink marker color
         */
        @LocalizedMessage(defaultMessage = "pink")
        @MessageComment({"pink marker color"})
        Pink,
        /**
         * gray marker color
         */
        @LocalizedMessage(defaultMessage = "gray")
        @MessageComment({"gray marker color"})
        Gray,
        /**
         * silver marker color
         */
        @LocalizedMessage(defaultMessage = "silver")
        @MessageComment({"silver marker color"})
        Silver,
        /**
         * cyan marker color
         */
        @LocalizedMessage(defaultMessage = "cyan")
        @MessageComment({"cyan marker color"})
        Cyan,
        /**
         * purple marker color
         */
        @LocalizedMessage(defaultMessage = "purple")
        @MessageComment({"purple marker color"})
        Purple,
        /**
         * blue marker color
         */
        @LocalizedMessage(defaultMessage = "blue")
        @MessageComment({"blue marker color"})
        Blue,
        /**
         * brown marker color
         */
        @LocalizedMessage(defaultMessage = "brown")
        @MessageComment({"brown marker color"})
        Brown,
        /**
         * green marker color
         */
        @LocalizedMessage(defaultMessage = "green")
        @MessageComment({"green marker color"})
        Green,
        /**
         * red marker color
         */
        @LocalizedMessage(defaultMessage = "white")
        @MessageComment({"white marker color"})
        Red,
        /**
         * white marker color
         */
        @LocalizedMessage(defaultMessage = "white")
        @MessageComment({"white marker color"})
        Black
    }
    
    /**
     * marker colors.
     * @author mepeisen
     */
    private enum MarkerColor implements MarkerColorInterface
    {

        /**
         * Represents white dye.
         */
        WHITE(Messages.White, DyeColor.WHITE),
        /**
         * Represents orange dye.
         */
        ORANGE(Messages.Orange, DyeColor.ORANGE),
        /**
         * Represents magenta dye.
         */
        MAGENTA(Messages.Magenta, DyeColor.MAGENTA),
        /**
         * Represents light blue dye.
         */
        LIGHT_BLUE(Messages.LightBlue, DyeColor.LIGHT_BLUE),
        /**
         * Represents yellow dye.
         */
        YELLOW(Messages.Yellow, DyeColor.YELLOW),
        /**
         * Represents lime dye.
         */
        LIME(Messages.Lime, DyeColor.LIME),
        /**
         * Represents pink dye.
         */
        PINK(Messages.Pink, DyeColor.PINK),
        /**
         * Represents gray dye.
         */
        GRAY(Messages.Gray, DyeColor.GRAY),
        /**
         * Represents silver dye.
         */
        SILVER(Messages.Silver, DyeColor.SILVER),
        /**
         * Represents cyan dye.
         */
        CYAN(Messages.Cyan, DyeColor.CYAN),
        /**
         * Represents purple dye.
         */
        PURPLE(Messages.Purple, DyeColor.PURPLE),
        /**
         * Represents blue dye.
         */
        BLUE(Messages.Blue, DyeColor.BLUE),
        /**
         * Represents brown dye.
         */
        BROWN(Messages.Brown, DyeColor.BROWN),
        /**
         * Represents green dye.
         */
        GREEN(Messages.Green, DyeColor.GREEN),
        /**
         * Represents red dye.
         */
        RED(Messages.Red, DyeColor.RED),
        /**
         * Represents black dye.
         */
        BLACK(Messages.Black, DyeColor.BLACK);
        
        /** red */
        private final int r;
        
        /** green */
        private final int g;
        
        /** blue */
        private final int b;
        
        /** alpha value */
        private final int alpha;
        
        /** name */
        private LocalizedMessageInterface name;
        
        /** wool color */
        private DyeColor color;

        /**
         * @param name
         * @param color
         */
        private MarkerColor(LocalizedMessageInterface name, DyeColor color)
        {
            this.r = color.getColor().getRed();
            this.g = color.getColor().getGreen();
            this.b = color.getColor().getBlue();
            this.alpha = 0x7f;
            this.name = name;
            this.color = color;
        }

        @Override
        public LocalizedMessageInterface getTitle()
        {
            return this.name;
        }

        @Override
        public int getR()
        {
            return this.r;
        }

        @Override
        public int getG()
        {
            return this.g;
        }

        @Override
        public int getB()
        {
            return this.b;
        }

        @Override
        public int getAlpha()
        {
            return this.alpha;
        }

        @SuppressWarnings("deprecation")
        @Override
        public ItemStack getIcon()
        {
            return new ItemStack(Material.WOOL, 1, this.color.getWoolData());
        }
        
    }
    
}
