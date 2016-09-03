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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.config.ConfigInterface;
import com.github.mce.minigames.api.config.Configurable;
import com.github.mce.minigames.api.config.ConfigurationBool;
import com.github.mce.minigames.api.config.ConfigurationBoolList;
import com.github.mce.minigames.api.config.ConfigurationByte;
import com.github.mce.minigames.api.config.ConfigurationByteList;
import com.github.mce.minigames.api.config.ConfigurationCharacter;
import com.github.mce.minigames.api.config.ConfigurationCharacterList;
import com.github.mce.minigames.api.config.ConfigurationColor;
import com.github.mce.minigames.api.config.ConfigurationColorList;
import com.github.mce.minigames.api.config.ConfigurationDouble;
import com.github.mce.minigames.api.config.ConfigurationDoubleList;
import com.github.mce.minigames.api.config.ConfigurationFloat;
import com.github.mce.minigames.api.config.ConfigurationFloatList;
import com.github.mce.minigames.api.config.ConfigurationInt;
import com.github.mce.minigames.api.config.ConfigurationIntList;
import com.github.mce.minigames.api.config.ConfigurationItemStack;
import com.github.mce.minigames.api.config.ConfigurationItemStackList;
import com.github.mce.minigames.api.config.ConfigurationLong;
import com.github.mce.minigames.api.config.ConfigurationLongList;
import com.github.mce.minigames.api.config.ConfigurationObject;
import com.github.mce.minigames.api.config.ConfigurationObjectList;
import com.github.mce.minigames.api.config.ConfigurationPlayer;
import com.github.mce.minigames.api.config.ConfigurationPlayerList;
import com.github.mce.minigames.api.config.ConfigurationSection;
import com.github.mce.minigames.api.config.ConfigurationShort;
import com.github.mce.minigames.api.config.ConfigurationShortList;
import com.github.mce.minigames.api.config.ConfigurationString;
import com.github.mce.minigames.api.config.ConfigurationStringList;
import com.github.mce.minigames.api.config.ConfigurationValueInterface;
import com.github.mce.minigames.api.config.ConfigurationValues;
import com.github.mce.minigames.api.config.ConfigurationVector;
import com.github.mce.minigames.api.config.ConfigurationVectorList;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * test case for {@link ConfigurationValueInterface}
 * 
 * @author mepeisen
 */
public class ConfigurationValueInterfaceTest
{
    
    /** the config mock. */
    private ConfigInterface config;
    /** mocked config file. */
    private MemoryConfiguration file;
    /** server. */
    private Server server;
    /** library. */
    private MglibInterface lib;

    /**
     * Some setup.
     */
    @Before
    public void setup()
    {
        this.lib = mock(MglibInterface.class);
        Whitebox.setInternalState(MglibInterface.INSTANCE.class, "CACHED", this.lib); //$NON-NLS-1$
        when(this.lib.resolveContextVar(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                return "core." + invocation.getArgumentAt(0, String.class); //$NON-NLS-1$
            }
        });
        this.config = mock(ConfigInterface.class);
        when(this.lib.getConfigFromCfg(anyObject())).thenReturn(this.config);
        this.file = new MemoryConfiguration();
        when(this.config.getConfig(anyString())).thenReturn(this.file);
        this.server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", this.server); //$NON-NLS-1$
        final ItemFactory itemFactory = mock(ItemFactory.class);
        when(itemFactory.equals(anyObject(), anyObject())).thenReturn(Boolean.TRUE);
        when(this.server.getItemFactory()).thenReturn(itemFactory);
    }
    
    /**
     * Tests {@link ConfigurationValueInterface#isset()}
     */
    @Test
    public void issetTest()
    {
        this.file.set("core.config.SomeBooleanFalse", Boolean.FALSE); //$NON-NLS-1$
        this.file.set("core.config.SomeSection.Foo", Boolean.FALSE); //$NON-NLS-1$
        
        assertTrue(TestOptions.SomeBooleanFalse.isset());
        assertFalse(TestOptions.SomeBooleanTrue.isset());
        
        assertTrue(TestOptions.SomeSection.isset());
        assertTrue(TestOptions.SomeSection.isset("Foo")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Bar")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeOtherSection.isset("Foo")); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ConfigurationValueInterface#isset()}
     */
    @Test(expected = IllegalStateException.class)
    public void issetTestInvalie()
    {
        TestOptions.SomeBooleanFalse.isset("Foo"); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ConfigurationValueInterface#path()}
     */
    @Test(expected = IllegalStateException.class)
    public void pathTestInvalid()
    {
        TestOptions.SomeDummy.path();
    }
    
    /**
     * Tests isxxx methods
     */
    @Test
    public void isxxxTestInvalid()
    {
        assertFalse(TestOptions.SomeDummy.isBoolean());
    }
    
    /**
     * Tests for setters and getters
     */
    @Test
    public void getsetTest()
    {
        // boolean
        assertEquals(true, TestOptions.SomeBooleanTrue.getBoolean());
        assertEquals(false, TestOptions.SomeBooleanFalse.getBoolean());
        assertEquals(false, TestOptions.SomeOtherBoolean.getBoolean());
        assertArrayEquals(new boolean[]{true}, TestOptions.SomeBooleanList.getBooleanList());
        assertArrayEquals(new boolean[]{}, TestOptions.SomeOtherBooleanList.getBooleanList());
        
        TestOptions.SomeBooleanTrue.setBoolean(false);
        TestOptions.SomeBooleanFalse.setBoolean(true);
        TestOptions.SomeOtherBoolean.setBoolean(false);
        TestOptions.SomeBooleanList.setBooleanList(new boolean[]{false, false});
        TestOptions.SomeOtherBooleanList.setBooleanList(new boolean[]{false, false});
        
        assertEquals(Boolean.FALSE, this.file.get(TestOptions.SomeBooleanTrue.path()));
        assertEquals(Boolean.TRUE, this.file.get(TestOptions.SomeBooleanFalse.path()));
        assertEquals(Boolean.FALSE, this.file.get(TestOptions.SomeOtherBoolean.path()));
        assertEquals(Arrays.asList(false, false), this.file.get(TestOptions.SomeBooleanList.path()));
        assertEquals(Arrays.asList(false, false), this.file.get(TestOptions.SomeOtherBooleanList.path()));
        
        assertEquals(false, TestOptions.SomeBooleanTrue.getBoolean());
        assertEquals(true, TestOptions.SomeBooleanFalse.getBoolean());
        assertEquals(false, TestOptions.SomeOtherBoolean.getBoolean());
        assertArrayEquals(new boolean[]{false, false}, TestOptions.SomeBooleanList.getBooleanList());
        assertArrayEquals(new boolean[]{false, false}, TestOptions.SomeOtherBooleanList.getBooleanList());
        
        // byte
        assertEquals(1, TestOptions.SomeByte1.getByte());
        assertEquals(2, TestOptions.SomeByte2.getByte());
        assertEquals(0, TestOptions.SomeOtherByte.getByte());
        assertArrayEquals(new byte[]{1, 2}, TestOptions.SomeByteList.getByteList());
        assertArrayEquals(new byte[]{}, TestOptions.SomeOtherByteList.getByteList());
        
        TestOptions.SomeByte1.setByte((byte) 2);
        TestOptions.SomeByte2.setByte((byte) 3);
        TestOptions.SomeOtherByte.setByte((byte) 4);
        TestOptions.SomeByteList.setByteList(new byte[]{(byte) 5, (byte) 6});
        TestOptions.SomeOtherByteList.setByteList(new byte[]{(byte) 5, (byte) 6});
        
        assertEquals(Byte.valueOf((byte) 2), this.file.get(TestOptions.SomeByte1.path()));
        assertEquals(Byte.valueOf((byte) 3), this.file.get(TestOptions.SomeByte2.path()));
        assertEquals(Byte.valueOf((byte) 4), this.file.get(TestOptions.SomeOtherByte.path()));
        assertEquals(Arrays.asList((byte) 5, (byte) 6), this.file.get(TestOptions.SomeByteList.path()));
        assertEquals(Arrays.asList((byte) 5, (byte) 6), this.file.get(TestOptions.SomeOtherByteList.path()));
        
        assertEquals(2, TestOptions.SomeByte1.getByte());
        assertEquals(3, TestOptions.SomeByte2.getByte());
        assertEquals(4, TestOptions.SomeOtherByte.getByte());
        assertArrayEquals(new byte[]{5, 6}, TestOptions.SomeByteList.getByteList());
        assertArrayEquals(new byte[]{5, 6}, TestOptions.SomeOtherByteList.getByteList());
        
        // char
        assertEquals('a', TestOptions.SomeCharacterA.getCharacter());
        assertEquals('b', TestOptions.SomeCharacterB.getCharacter());
        assertEquals(' ', TestOptions.SomeOtherCharacter.getCharacter());
        assertArrayEquals(new char[]{'a', 'b'}, TestOptions.SomeCharacterList.getCharacterList());
        assertArrayEquals(new char[]{}, TestOptions.SomeOtherCharacterList.getCharacterList());
        
        TestOptions.SomeCharacterA.setCharacter('d');
        TestOptions.SomeCharacterB.setCharacter('e');
        TestOptions.SomeOtherCharacter.setCharacter('f');
        TestOptions.SomeCharacterList.setCharacterList(new char[]{'q', 'w'});
        TestOptions.SomeOtherCharacterList.setCharacterList(new char[]{'e', 'r'});
        
        assertEquals("d", this.file.get(TestOptions.SomeCharacterA.path())); //$NON-NLS-1$
        assertEquals("e", this.file.get(TestOptions.SomeCharacterB.path())); //$NON-NLS-1$
        assertEquals("f", this.file.get(TestOptions.SomeOtherCharacter.path())); //$NON-NLS-1$
        assertEquals(Arrays.asList('q', 'w'), this.file.get(TestOptions.SomeCharacterList.path()));
        assertEquals(Arrays.asList('e', 'r'), this.file.get(TestOptions.SomeOtherCharacterList.path()));
        
        assertEquals('d', TestOptions.SomeCharacterA.getCharacter());
        assertEquals('e', TestOptions.SomeCharacterB.getCharacter());
        assertEquals('f', TestOptions.SomeOtherCharacter.getCharacter());
        assertArrayEquals(new char[]{'q', 'w'}, TestOptions.SomeCharacterList.getCharacterList());
        assertArrayEquals(new char[]{'e', 'r'}, TestOptions.SomeOtherCharacterList.getCharacterList());
        
        // color
        assertEquals(Color.fromRGB(0x102030), TestOptions.SomeColorA.getColor());
        assertEquals(Color.fromRGB(0x203040), TestOptions.SomeColorB.getColor());
        assertEquals(Color.fromRGB(0), TestOptions.SomeOtherColor.getColor());
        assertArrayEquals(new Color[]{}, TestOptions.SomeColorList.getColorList());
        assertArrayEquals(new Color[]{}, TestOptions.SomeOtherColorList.getColorList());
        
        TestOptions.SomeColorA.setColor(Color.RED);
        TestOptions.SomeColorB.setColor(Color.BLUE);
        TestOptions.SomeOtherColor.setColor(Color.AQUA);
        TestOptions.SomeColorList.setColorList(new Color[]{Color.BLACK, Color.FUCHSIA});
        TestOptions.SomeOtherColorList.setColorList(new Color[]{Color.GREEN, Color.LIME});
        
        assertEquals(Color.RED, this.file.get(TestOptions.SomeColorA.path()));
        assertEquals(Color.BLUE, this.file.get(TestOptions.SomeColorB.path()));
        assertEquals(Color.AQUA, this.file.get(TestOptions.SomeOtherColor.path()));
//        assertEquals(Arrays.asList(Color.BLACK, Color.FUCHSIA), this.file.get(TestOptions.SomeColorList.path()));
//        assertEquals(Arrays.asList(Color.GREEN, Color.LIME), this.file.get(TestOptions.SomeOtherColorList.path()));
        
        assertEquals(Color.RED, TestOptions.SomeColorA.getColor());
        assertEquals(Color.BLUE, TestOptions.SomeColorB.getColor());
        assertEquals(Color.AQUA, TestOptions.SomeOtherColor.getColor());
        assertArrayEquals(new Color[]{Color.BLACK, Color.FUCHSIA}, TestOptions.SomeColorList.getColorList());
        assertArrayEquals(new Color[]{Color.GREEN, Color.LIME}, TestOptions.SomeOtherColorList.getColorList());
        
        // double
        assertEquals(0.5, TestOptions.SomeDoubleA.getDouble(), 0);
        assertEquals(0.75, TestOptions.SomeDoubleB.getDouble(), 0);
        assertEquals(0d, TestOptions.SomeOtherDouble.getDouble(), 0);
        assertArrayEquals(new double[]{1.1, 1.2}, TestOptions.SomeDoubleList.getDoubleList(), 0);
        assertArrayEquals(new double[]{}, TestOptions.SomeOtherDoubleList.getDoubleList(), 0);
        
        TestOptions.SomeDoubleA.setDouble(2.1);
        TestOptions.SomeDoubleB.setDouble(2.5);
        TestOptions.SomeOtherDouble.setDouble(2.3);
        TestOptions.SomeDoubleList.setDoubleList(new double[]{2.2, 2.4});
        TestOptions.SomeOtherDoubleList.setDoubleList(new double[]{2.6, 2.7});
        
        assertEquals(Double.valueOf(2.1), this.file.get(TestOptions.SomeDoubleA.path()));
        assertEquals(Double.valueOf(2.5), this.file.get(TestOptions.SomeDoubleB.path()));
        assertEquals(Double.valueOf(2.3), this.file.get(TestOptions.SomeOtherDouble.path()));
        assertEquals(Arrays.asList(2.2, 2.4), this.file.get(TestOptions.SomeDoubleList.path()));
        assertEquals(Arrays.asList(2.6, 2.7), this.file.get(TestOptions.SomeOtherDoubleList.path()));
        
        assertEquals(2.1, TestOptions.SomeDoubleA.getDouble(), 0);
        assertEquals(2.5, TestOptions.SomeDoubleB.getDouble(), 0);
        assertEquals(2.3, TestOptions.SomeOtherDouble.getDouble(), 0);
        assertArrayEquals(new double[]{2.2, 2.4}, TestOptions.SomeDoubleList.getDoubleList(), 0);
        assertArrayEquals(new double[]{2.6, 2.7}, TestOptions.SomeOtherDoubleList.getDoubleList(), 0);
        
        // float
        assertEquals(0.5f, TestOptions.SomeFloatA.getFloat(), 0);
        assertEquals(0.75f, TestOptions.SomeFloatB.getFloat(), 0);
        assertEquals(0f, TestOptions.SomeOtherFloat.getFloat(), 0);
        assertArrayEquals(new float[]{1.1f, 1.2f}, TestOptions.SomeFloatList.getFloatList(), 0);
        assertArrayEquals(new float[]{}, TestOptions.SomeOtherFloatList.getFloatList(), 0);
        
        TestOptions.SomeFloatA.setFloat(2.1f);
        TestOptions.SomeFloatB.setFloat(2.5f);
        TestOptions.SomeOtherFloat.setFloat(2.3f);
        TestOptions.SomeFloatList.setFloatList(new float[]{2.2f, 2.4f});
        TestOptions.SomeOtherFloatList.setFloatList(new float[]{2.6f, 2.7f});
        
        assertEquals(Float.valueOf(2.1f), this.file.get(TestOptions.SomeFloatA.path()));
        assertEquals(Float.valueOf(2.5f), this.file.get(TestOptions.SomeFloatB.path()));
        assertEquals(Float.valueOf(2.3f), this.file.get(TestOptions.SomeOtherFloat.path()));
        assertEquals(Arrays.asList(2.2f, 2.4f), this.file.get(TestOptions.SomeFloatList.path()));
        assertEquals(Arrays.asList(2.6f, 2.7f), this.file.get(TestOptions.SomeOtherFloatList.path()));
        
        assertEquals(2.1f, TestOptions.SomeFloatA.getFloat(), 0);
        assertEquals(2.5f, TestOptions.SomeFloatB.getFloat(), 0);
        assertEquals(2.3f, TestOptions.SomeOtherFloat.getFloat(), 0);
        assertArrayEquals(new float[]{2.2f, 2.4f}, TestOptions.SomeFloatList.getFloatList(), 0);
        assertArrayEquals(new float[]{2.6f, 2.7f}, TestOptions.SomeOtherFloatList.getFloatList(), 0);
        
        // int
        assertEquals(1, TestOptions.SomeInt1.getInt());
        assertEquals(2, TestOptions.SomeInt2.getInt());
        assertEquals(0, TestOptions.SomeOtherInt.getInt());
        assertArrayEquals(new int[]{1, 2}, TestOptions.SomeIntList.getIntList());
        assertArrayEquals(new int[]{}, TestOptions.SomeOtherIntList.getIntList());
        
        TestOptions.SomeInt1.setInt(2);
        TestOptions.SomeInt2.setInt(3);
        TestOptions.SomeOtherInt.setInt(4);
        TestOptions.SomeIntList.setIntList(new int[]{5, 6});
        TestOptions.SomeOtherIntList.setIntList(new int[]{5, 6});
        
        assertEquals(Integer.valueOf(2), this.file.get(TestOptions.SomeInt1.path()));
        assertEquals(Integer.valueOf(3), this.file.get(TestOptions.SomeInt2.path()));
        assertEquals(Integer.valueOf(4), this.file.get(TestOptions.SomeOtherInt.path()));
        assertEquals(Arrays.asList(5, 6), this.file.get(TestOptions.SomeIntList.path()));
        assertEquals(Arrays.asList(5, 6), this.file.get(TestOptions.SomeOtherIntList.path()));
        
        assertEquals(2, TestOptions.SomeInt1.getInt());
        assertEquals(3, TestOptions.SomeInt2.getInt());
        assertEquals(4, TestOptions.SomeOtherInt.getInt());
        assertArrayEquals(new int[]{5, 6}, TestOptions.SomeIntList.getIntList());
        assertArrayEquals(new int[]{5, 6}, TestOptions.SomeOtherIntList.getIntList());
        
        // item stack
        assertNull(TestOptions.SomeItemStack.getItemStack());
        assertNull(TestOptions.SomeOtherItemStack.getItemStack());
        assertArrayEquals(new ItemStack[]{}, TestOptions.SomeItemStackList.getItemStackList());
        assertArrayEquals(new ItemStack[]{}, TestOptions.SomeOtherItemStackList.getItemStackList());
        
        TestOptions.SomeItemStack.setItemStack(new ItemStack(Material.AIR));
        TestOptions.SomeOtherItemStack.setItemStack(new ItemStack(Material.ACACIA_DOOR));
        TestOptions.SomeItemStackList.setItemStackList(new ItemStack[]{new ItemStack(Material.ACACIA_DOOR_ITEM), new ItemStack(Material.ACACIA_FENCE)});
        TestOptions.SomeOtherItemStackList.setItemStackList(new ItemStack[]{new ItemStack(Material.ACACIA_FENCE_GATE), new ItemStack(Material.ACACIA_STAIRS)});
        
        assertEquals(new ItemStack(Material.AIR), this.file.get(TestOptions.SomeItemStack.path()));
        assertEquals(new ItemStack(Material.ACACIA_DOOR), this.file.get(TestOptions.SomeOtherItemStack.path()));
//        assertEquals(Arrays.asList(new ItemStack(Material.ACACIA_DOOR_ITEM), new ItemStack(Material.ACACIA_FENCE)), this.file.get(TestOptions.SomeItemStackList.path()));
//        assertEquals(Arrays.asList(new ItemStack(Material.ACACIA_FENCE_GATE), new ItemStack(Material.ACACIA_STAIRS)), this.file.get(TestOptions.SomeOtherItemStackList.path()));
        
        assertEquals(new ItemStack(Material.AIR), TestOptions.SomeItemStack.getItemStack());
        assertEquals(new ItemStack(Material.ACACIA_DOOR), TestOptions.SomeOtherItemStack.getItemStack());
        assertArrayEquals(new ItemStack[]{new ItemStack(Material.ACACIA_DOOR_ITEM), new ItemStack(Material.ACACIA_FENCE)}, TestOptions.SomeItemStackList.getItemStackList());
        assertArrayEquals(new ItemStack[]{new ItemStack(Material.ACACIA_FENCE_GATE), new ItemStack(Material.ACACIA_STAIRS)}, TestOptions.SomeOtherItemStackList.getItemStackList());
        
        // long
        assertEquals(1, TestOptions.SomeLong1.getLong());
        assertEquals(2, TestOptions.SomeLong2.getLong());
        assertEquals(0, TestOptions.SomeOtherLong.getLong());
        assertArrayEquals(new long[]{1, 2}, TestOptions.SomeLongList.getLongList());
        assertArrayEquals(new long[]{}, TestOptions.SomeOtherLongList.getLongList());
        
        TestOptions.SomeLong1.setLong(2);
        TestOptions.SomeLong2.setLong(3);
        TestOptions.SomeOtherLong.setLong(4);
        TestOptions.SomeLongList.setLongList(new long[]{5, 6});
        TestOptions.SomeOtherLongList.setLongList(new long[]{5, 6});
        
        assertEquals(Long.valueOf(2), this.file.get(TestOptions.SomeLong1.path()));
        assertEquals(Long.valueOf(3), this.file.get(TestOptions.SomeLong2.path()));
        assertEquals(Long.valueOf(4), this.file.get(TestOptions.SomeOtherLong.path()));
        assertEquals(Arrays.asList(5l, 6l), this.file.get(TestOptions.SomeLongList.path()));
        assertEquals(Arrays.asList(5l, 6l), this.file.get(TestOptions.SomeOtherLongList.path()));
        
        assertEquals(2, TestOptions.SomeLong1.getLong());
        assertEquals(3, TestOptions.SomeLong2.getLong());
        assertEquals(4, TestOptions.SomeOtherLong.getLong());
        assertArrayEquals(new long[]{5, 6}, TestOptions.SomeLongList.getLongList());
        assertArrayEquals(new long[]{5, 6}, TestOptions.SomeOtherLongList.getLongList());
        
        // short
        assertEquals(1, TestOptions.SomeShort1.getShort());
        assertEquals(2, TestOptions.SomeShort2.getShort());
        assertEquals(0, TestOptions.SomeOtherShort.getShort());
        assertArrayEquals(new short[]{1, 2}, TestOptions.SomeShortList.getShortList());
        assertArrayEquals(new short[]{}, TestOptions.SomeOtherShortList.getShortList());
        
        TestOptions.SomeShort1.setShort((short) 2);
        TestOptions.SomeShort2.setShort((short) 3);
        TestOptions.SomeOtherShort.setShort((short) 4);
        TestOptions.SomeShortList.setShortList(new short[]{5, 6});
        TestOptions.SomeOtherShortList.setShortList(new short[]{5, 6});
        
        assertEquals(Short.valueOf((short) 2), this.file.get(TestOptions.SomeShort1.path()));
        assertEquals(Short.valueOf((short) 3), this.file.get(TestOptions.SomeShort2.path()));
        assertEquals(Short.valueOf((short) 4), this.file.get(TestOptions.SomeOtherShort.path()));
        assertEquals(Arrays.asList(5, 6), this.file.get(TestOptions.SomeShortList.path()));
        assertEquals(Arrays.asList(5, 6), this.file.get(TestOptions.SomeOtherShortList.path()));
        
        assertEquals(2, TestOptions.SomeShort1.getShort());
        assertEquals(3, TestOptions.SomeShort2.getShort());
        assertEquals(4, TestOptions.SomeOtherShort.getShort());
        assertArrayEquals(new short[]{5, 6}, TestOptions.SomeShortList.getShortList());
        assertArrayEquals(new short[]{5, 6}, TestOptions.SomeOtherShortList.getShortList());
        
        // string
        assertEquals("a", TestOptions.SomeStringA.getString()); //$NON-NLS-1$
        assertEquals("b", TestOptions.SomeStringB.getString()); //$NON-NLS-1$
        assertEquals("", TestOptions.SomeOtherString.getString()); //$NON-NLS-1$
        assertArrayEquals(new String[]{"a", "b"}, TestOptions.SomeStringList.getStringList()); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{}, TestOptions.SomeOtherStringList.getStringList());
        
        TestOptions.SomeStringA.setString("d"); //$NON-NLS-1$
        TestOptions.SomeStringB.setString("e"); //$NON-NLS-1$
        TestOptions.SomeOtherString.setString("f"); //$NON-NLS-1$
        TestOptions.SomeStringList.setStringList(new String[]{"q", "w"}); //$NON-NLS-1$ //$NON-NLS-2$
        TestOptions.SomeOtherStringList.setStringList(new String[]{"e", "r"}); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertEquals("d", this.file.get(TestOptions.SomeStringA.path())); //$NON-NLS-1$
        assertEquals("e", this.file.get(TestOptions.SomeStringB.path())); //$NON-NLS-1$
        assertEquals("f", this.file.get(TestOptions.SomeOtherString.path())); //$NON-NLS-1$
        assertEquals(Arrays.asList("q", "w"), this.file.get(TestOptions.SomeStringList.path())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(Arrays.asList("e", "r"), this.file.get(TestOptions.SomeOtherStringList.path())); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertEquals("d", TestOptions.SomeStringA.getString()); //$NON-NLS-1$
        assertEquals("e", TestOptions.SomeStringB.getString()); //$NON-NLS-1$
        assertEquals("f", TestOptions.SomeOtherString.getString()); //$NON-NLS-1$
        assertArrayEquals(new String[]{"q", "w"}, TestOptions.SomeStringList.getStringList()); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"e", "r"}, TestOptions.SomeOtherStringList.getStringList()); //$NON-NLS-1$ //$NON-NLS-2$
        
        // player
        assertNull(TestOptions.SomePlayer.getPlayer());
        assertNull(TestOptions.SomeOtherPlayer.getPlayer());
        assertArrayEquals(new ArenaPlayerInterface[]{}, TestOptions.SomePlayerList.getPlayerList());
        assertArrayEquals(new ArenaPlayerInterface[]{}, TestOptions.SomeOtherPlayerList.getPlayerList());
        
        final ArenaPlayerInterface player1 = createPlayer();
        final ArenaPlayerInterface player2 = createPlayer();
        final ArenaPlayerInterface player3 = createPlayer();
        final ArenaPlayerInterface player4 = createPlayer();
        final ArenaPlayerInterface player5 = createPlayer();
        final ArenaPlayerInterface player6 = createPlayer();
        
        TestOptions.SomePlayer.setPlayer(player1);
        TestOptions.SomeOtherPlayer.setPlayer(player2);
        TestOptions.SomePlayerList.setPlayerList(new ArenaPlayerInterface[]{player3, player4});
        TestOptions.SomeOtherPlayerList.setPlayerList(new ArenaPlayerInterface[]{player5, player6});
        
        assertEquals(player1.getBukkitPlayer(), this.file.get(TestOptions.SomePlayer.path()));
        assertEquals(player2.getBukkitPlayer(), this.file.get(TestOptions.SomeOtherPlayer.path()));
//        assertEquals(Arrays.asList(player3.getBukkitPlayer(), player4.getBukkitPlayer()), this.file.get(TestOptions.SomePlayerList.path()));
//        assertEquals(Arrays.asList(player5.getBukkitPlayer(), player6.getBukkitPlayer()), this.file.get(TestOptions.SomeOtherPlayerList.path()));
        
        assertEquals(player1, TestOptions.SomePlayer.getPlayer());
        assertEquals(player2, TestOptions.SomeOtherPlayer.getPlayer());
        assertArrayEquals(new ArenaPlayerInterface[]{player3, player4}, TestOptions.SomePlayerList.getPlayerList());
        assertArrayEquals(new ArenaPlayerInterface[]{player5, player6}, TestOptions.SomeOtherPlayerList.getPlayerList());
        
        // TODO object
        // TODO vector
        
        // TODO sections
    }
    
    /**
     * Creates a mocked player
     * @return mocked player
     */
    private ArenaPlayerInterface createPlayer()
    {
        final ArenaPlayerInterface player = mock(ArenaPlayerInterface.class);
        final Player bPlayer = mock(Player.class);
        final UUID uuid = UUID.randomUUID();
        when(player.getBukkitPlayer()).thenReturn(bPlayer);
        when(player.getOfflinePlayer()).thenReturn(bPlayer);
        when(player.getPlayerUUID()).thenReturn(uuid);
        when(bPlayer.getUniqueId()).thenReturn(uuid);
        when(this.server.getPlayer(uuid)).thenReturn(bPlayer);
        when(this.lib.getPlayer(uuid)).thenReturn(player);
        when(this.lib.getPlayer(bPlayer)).thenReturn(player);
        when(this.lib.getPlayer((OfflinePlayer) bPlayer)).thenReturn(player);
        return player;
    }

    /**
     * Tests {@link ConfigurationValueInterface#path()}
     */
    @Test
    public void pathTest()
    {
        assertEquals("core.config.SomeBooleanFalse", TestOptions.SomeBooleanFalse.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_boolean", TestOptions.SomeOtherBoolean.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeBooleanList", TestOptions.SomeBooleanList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_boolean_list", TestOptions.SomeOtherBooleanList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeByte1", TestOptions.SomeByte1.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_byte", TestOptions.SomeOtherByte.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeByteList", TestOptions.SomeByteList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_byte_list", TestOptions.SomeOtherByteList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeCharacterA", TestOptions.SomeCharacterA.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_character", TestOptions.SomeOtherCharacter.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeCharacterList", TestOptions.SomeCharacterList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_character_list", TestOptions.SomeOtherCharacterList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeColorA", TestOptions.SomeColorA.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_color", TestOptions.SomeOtherColor.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeColorList", TestOptions.SomeColorList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_color_list", TestOptions.SomeOtherColorList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeDoubleA", TestOptions.SomeDoubleA.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_double", TestOptions.SomeOtherDouble.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeDoubleList", TestOptions.SomeDoubleList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_double_list", TestOptions.SomeOtherDoubleList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeFloatA", TestOptions.SomeFloatA.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_float", TestOptions.SomeOtherFloat.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeFloatList", TestOptions.SomeFloatList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_float_list", TestOptions.SomeOtherFloatList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeInt1", TestOptions.SomeInt1.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_int", TestOptions.SomeOtherInt.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeIntList", TestOptions.SomeIntList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_int_list", TestOptions.SomeOtherIntList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeItemStack", TestOptions.SomeItemStack.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_itemstack", TestOptions.SomeOtherItemStack.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeItemStackList", TestOptions.SomeItemStackList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_itemstack_list", TestOptions.SomeOtherItemStackList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeSection", TestOptions.SomeSection.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_section", TestOptions.SomeOtherSection.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeLong1", TestOptions.SomeLong1.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_long", TestOptions.SomeOtherLong.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeLongList", TestOptions.SomeLongList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_long_list", TestOptions.SomeOtherLongList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeObject", TestOptions.SomeObject.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_object", TestOptions.SomeOtherObject.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeObjectList", TestOptions.SomeObjectList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_object_list", TestOptions.SomeOtherObjectList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomePlayer", TestOptions.SomePlayer.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_player", TestOptions.SomeOtherPlayer.path()); //$NON-NLS-1$
        assertEquals("core.config.SomePlayerList", TestOptions.SomePlayerList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_player_list", TestOptions.SomeOtherPlayerList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeShort1", TestOptions.SomeShort1.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_short", TestOptions.SomeOtherShort.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeShortList", TestOptions.SomeShortList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_short_list", TestOptions.SomeOtherShortList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeStringA", TestOptions.SomeStringA.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_string", TestOptions.SomeOtherString.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeStringList", TestOptions.SomeStringList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_string_list", TestOptions.SomeOtherStringList.path()); //$NON-NLS-1$

        assertEquals("core.config.SomeVector", TestOptions.SomeVector.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_vector", TestOptions.SomeOtherVector.path()); //$NON-NLS-1$
        assertEquals("core.config.SomeVectorList", TestOptions.SomeVectorList.path()); //$NON-NLS-1$
        assertEquals("core.config.some_other_vector_list", TestOptions.SomeOtherVectorList.path()); //$NON-NLS-1$
    }
    
    /**
     * Tests the isxx functions
     */
    @Test
    public void isXXTest()
    {
        assertTrue(TestOptions.SomeBooleanTrue.isBoolean());
        assertFalse(TestOptions.SomeBooleanTrue.isBooleanList());
        assertTrue(TestOptions.SomeBooleanList.isBooleanList());
        assertFalse(TestOptions.SomeBooleanList.isBoolean());
        
        assertTrue(TestOptions.SomeByte1.isByte());
        assertFalse(TestOptions.SomeByte1.isByteList());
        assertTrue(TestOptions.SomeByteList.isByteList());
        assertFalse(TestOptions.SomeByteList.isByte());
        
        assertTrue(TestOptions.SomeCharacterA.isCharacter());
        assertFalse(TestOptions.SomeCharacterA.isCharacterList());
        assertTrue(TestOptions.SomeCharacterList.isCharacterList());
        assertFalse(TestOptions.SomeCharacterList.isCharacter());
        
        assertTrue(TestOptions.SomeColorA.isColor());
        assertFalse(TestOptions.SomeColorA.isColorList());
        assertTrue(TestOptions.SomeColorList.isColorList());
        assertFalse(TestOptions.SomeColorList.isColor());
        
        assertTrue(TestOptions.SomeDoubleA.isDouble());
        assertFalse(TestOptions.SomeDoubleA.isDoubleList());
        assertTrue(TestOptions.SomeDoubleList.isDoubleList());
        assertFalse(TestOptions.SomeDoubleList.isDouble());
        
        assertTrue(TestOptions.SomeFloatA.isFloat());
        assertFalse(TestOptions.SomeFloatA.isFloatList());
        assertTrue(TestOptions.SomeFloatList.isFloatList());
        assertFalse(TestOptions.SomeFloatList.isFloat());
        
        assertTrue(TestOptions.SomeInt1.isInt());
        assertFalse(TestOptions.SomeInt1.isIntList());
        assertTrue(TestOptions.SomeIntList.isIntList());
        assertFalse(TestOptions.SomeIntList.isInt());
        
        assertTrue(TestOptions.SomeItemStack.isItemStack());
        assertFalse(TestOptions.SomeItemStack.isItemStackList());
        assertTrue(TestOptions.SomeItemStackList.isItemStackList());
        assertFalse(TestOptions.SomeItemStackList.isItemStack());
        
        assertTrue(TestOptions.SomeSection.isSection());
        assertFalse(TestOptions.SomeItemStack.isSection());
        
        assertTrue(TestOptions.SomeLong1.isLong());
        assertFalse(TestOptions.SomeLong1.isLongList());
        assertTrue(TestOptions.SomeLongList.isLongList());
        assertFalse(TestOptions.SomeLongList.isLong());
        
        assertTrue(TestOptions.SomeObject.isObject());
        assertFalse(TestOptions.SomeObject.isObjectList());
        assertTrue(TestOptions.SomeObjectList.isObjectList());
        assertFalse(TestOptions.SomeObjectList.isObject());
        
        assertTrue(TestOptions.SomePlayer.isPlayer());
        assertFalse(TestOptions.SomePlayer.isPlayerList());
        assertTrue(TestOptions.SomePlayerList.isPlayerList());
        assertFalse(TestOptions.SomePlayerList.isPlayer());
        
        assertTrue(TestOptions.SomeShort1.isShort());
        assertFalse(TestOptions.SomeShort1.isShortList());
        assertTrue(TestOptions.SomeShortList.isShortList());
        assertFalse(TestOptions.SomeShortList.isShort());
        
        assertTrue(TestOptions.SomeStringA.isString());
        assertFalse(TestOptions.SomeStringA.isStringList());
        assertTrue(TestOptions.SomeStringList.isStringList());
        assertFalse(TestOptions.SomeStringList.isString());
        
        assertTrue(TestOptions.SomeVector.isVector());
        assertFalse(TestOptions.SomeVector.isVectorList());
        assertTrue(TestOptions.SomeVectorList.isVectorList());
        assertFalse(TestOptions.SomeVectorList.isVector());
    }
    
    /**
     * Some test options
     */
    @ConfigurationValues(path = "config")
    public static enum TestOptions implements ConfigurationValueInterface
    {
        /** some value. */
        @ConfigurationBool(defaultValue = false)
        SomeBooleanFalse,
        /** some value. */
        @ConfigurationBool(defaultValue = true)
        SomeBooleanTrue,
        /** some value. */
        @ConfigurationBool(name = "some_other_boolean")
        SomeOtherBoolean,
        
        /** some value list. */
        @ConfigurationBoolList(defaultValue = {true})
        SomeBooleanList,
        /** some value list. */
        @ConfigurationBoolList(name = "some_other_boolean_list")
        SomeOtherBooleanList,
        
        /** some value. */
        @ConfigurationByte(defaultValue = 1)
        SomeByte1,
        /** some value. */
        @ConfigurationByte(defaultValue = 2)
        SomeByte2,
        /** some value. */
        @ConfigurationByte(name = "some_other_byte")
        SomeOtherByte,
        
        /** some value list. */
        @ConfigurationByteList(defaultValue = {1, 2})
        SomeByteList,
        /** some value list. */
        @ConfigurationByteList(name = "some_other_byte_list")
        SomeOtherByteList,
        
        /** some value. */
        @ConfigurationCharacter(defaultValue = 'a')
        SomeCharacterA,
        /** some value. */
        @ConfigurationCharacter(defaultValue = 'b')
        SomeCharacterB,
        /** some value. */
        @ConfigurationCharacter(name = "some_other_character")
        SomeOtherCharacter,
        
        /** some value list. */
        @ConfigurationCharacterList(defaultValue = {'a', 'b'})
        SomeCharacterList,
        /** some value list. */
        @ConfigurationCharacterList(name = "some_other_character_list")
        SomeOtherCharacterList,
        
        /** some value. */
        @ConfigurationColor(defaultRgb = 0x102030)
        SomeColorA,
        /** some value. */
        @ConfigurationColor(defaultRgb = 0x203040)
        SomeColorB,
        /** some value. */
        @ConfigurationColor(name = "some_other_color")
        SomeOtherColor,
        
        /** some value list. */
        @ConfigurationColorList
        SomeColorList,
        /** some value list. */
        @ConfigurationColorList(name = "some_other_color_list")
        SomeOtherColorList,
        
        /** some value. */
        @ConfigurationDouble(defaultValue = 0.5)
        SomeDoubleA,
        /** some value. */
        @ConfigurationDouble(defaultValue = 0.75)
        SomeDoubleB,
        /** some value. */
        @ConfigurationDouble(name = "some_other_double")
        SomeOtherDouble,
        
        /** some value list. */
        @ConfigurationDoubleList(defaultValue = {1.1, 1.2})
        SomeDoubleList,
        /** some value list. */
        @ConfigurationDoubleList(name = "some_other_double_list")
        SomeOtherDoubleList,
        
        /** some value. */
        @ConfigurationFloat(defaultValue = 0.5f)
        SomeFloatA,
        /** some value. */
        @ConfigurationFloat(defaultValue = 0.75f)
        SomeFloatB,
        /** some value. */
        @ConfigurationFloat(name = "some_other_float")
        SomeOtherFloat,
        
        /** some value list. */
        @ConfigurationFloatList(defaultValue = {1.1f, 1.2f})
        SomeFloatList,
        /** some value list. */
        @ConfigurationFloatList(name = "some_other_float_list")
        SomeOtherFloatList,
        
        /** some value. */
        @ConfigurationInt(defaultValue = 1)
        SomeInt1,
        /** some value. */
        @ConfigurationInt(defaultValue = 2)
        SomeInt2,
        /** some value. */
        @ConfigurationInt(name = "some_other_int")
        SomeOtherInt,
        
        /** some value list. */
        @ConfigurationIntList(defaultValue = {1, 2})
        SomeIntList,
        /** some value list. */
        @ConfigurationIntList(name = "some_other_int_list")
        SomeOtherIntList,
        
        /** some value. */
        @ConfigurationItemStack
        SomeItemStack,
        /** some value. */
        @ConfigurationItemStack(name = "some_other_itemstack")
        SomeOtherItemStack,
        
        /** some value list. */
        @ConfigurationItemStackList
        SomeItemStackList,
        /** some value list. */
        @ConfigurationItemStackList(name = "some_other_itemstack_list")
        SomeOtherItemStackList,
        
        /** some value. */
        @ConfigurationSection
        SomeSection,
        /** some value. */
        @ConfigurationSection("some_other_section")
        SomeOtherSection,
        
        /** some value. */
        @ConfigurationLong(defaultValue = 1)
        SomeLong1,
        /** some value. */
        @ConfigurationLong(defaultValue = 2)
        SomeLong2,
        /** some value. */
        @ConfigurationLong(name = "some_other_long")
        SomeOtherLong,
        
        /** some value list. */
        @ConfigurationLongList(defaultValue = {1, 2})
        SomeLongList,
        /** some value list. */
        @ConfigurationLongList(name = "some_other_long_list")
        SomeOtherLongList,
        
        /** some value. */
        @ConfigurationObject(clazz = FooObject.class)
        SomeObject,
        /** some value. */
        @ConfigurationObject(clazz = FooObject.class, name = "some_other_object")
        SomeOtherObject,
        
        /** some value list. */
        @ConfigurationObjectList(clazz = FooObject.class)
        SomeObjectList,
        /** some value list. */
        @ConfigurationObjectList(clazz = FooObject.class, name = "some_other_object_list")
        SomeOtherObjectList,
        
        /** some value. */
        @ConfigurationPlayer
        SomePlayer,
        /** some value. */
        @ConfigurationPlayer(name = "some_other_player")
        SomeOtherPlayer,
        
        /** some value list. */
        @ConfigurationPlayerList
        SomePlayerList,
        /** some value list. */
        @ConfigurationPlayerList(name = "some_other_player_list")
        SomeOtherPlayerList,
        
        /** some value. */
        @ConfigurationShort(defaultValue = 1)
        SomeShort1,
        /** some value. */
        @ConfigurationShort(defaultValue = 2)
        SomeShort2,
        /** some value. */
        @ConfigurationShort(name = "some_other_short")
        SomeOtherShort,
        
        /** some value list. */
        @ConfigurationShortList(defaultValue = {1, 2})
        SomeShortList,
        /** some value list. */
        @ConfigurationShortList(name = "some_other_short_list")
        SomeOtherShortList,
        
        /** some value. */
        @ConfigurationString(defaultValue = "a")
        SomeStringA,
        /** some value. */
        @ConfigurationString(defaultValue = "b")
        SomeStringB,
        /** some value. */
        @ConfigurationString(name = "some_other_string")
        SomeOtherString,
        
        /** some value list. */
        @ConfigurationStringList(defaultValue = {"a", "b"})
        SomeStringList,
        /** some value list. */
        @ConfigurationStringList(name = "some_other_string_list")
        SomeOtherStringList,
        
        /** some value. */
        @ConfigurationVector
        SomeVector,
        /** some value. */
        @ConfigurationVector(name = "some_other_vector")
        SomeOtherVector,
        
        /** some value list. */
        @ConfigurationVectorList
        SomeVectorList,
        /** some value list. */
        @ConfigurationVectorList(name = "some_other_vector_list")
        SomeOtherVectorList,
        
        /** some invalid dummy value. */
        SomeDummy,
    }
    
    /**
     * A sample configurable
     */
    private static final class FooObject implements Configurable
    {

        @Override
        public void readFromConfig(org.bukkit.configuration.ConfigurationSection section)
        {
            // empty
        }

        @Override
        public void writeToConfig(org.bukkit.configuration.ConfigurationSection section)
        {
            // empty
        }
    }
    
}
