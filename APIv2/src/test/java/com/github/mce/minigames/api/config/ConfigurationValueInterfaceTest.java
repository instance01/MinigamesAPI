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

package com.github.mce.minigames.api.config;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import java.lang.reflect.Constructor;
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
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import com.github.mce.minigames.api.MglibInterface;
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
     * Checks the save config call.
     */
    @Test
    public void saveConfigTest()
    {
        TestOptions.SomeByte1.saveConfig();
        
        verify(this.config, times(1)).saveConfig("config.yml"); //$NON-NLS-1$
    }
    
    /**
     * Invoke constructor for code coverage.
     * @throws Exception thrown on exceptions
     */
    @Test
    public void constructorTest() throws Exception
    {
        final Constructor<?> ctor = Class.forName("com.github.mce.minigames.api.config.ConfigurationTool").getDeclaredConstructor(); //$NON-NLS-1$
        ctor.setAccessible(true);
        ctor.newInstance();
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
        
        assertEquals(player1, TestOptions.SomePlayer.getPlayer());
        assertEquals(player2, TestOptions.SomeOtherPlayer.getPlayer());
        assertArrayEquals(new ArenaPlayerInterface[]{player3, player4}, TestOptions.SomePlayerList.getPlayerList());
        assertArrayEquals(new ArenaPlayerInterface[]{player5, player6}, TestOptions.SomeOtherPlayerList.getPlayerList());
        
        // vector
        assertNull(TestOptions.SomeVector.getVector());
        assertNull(TestOptions.SomeOtherVector.getVector());
        assertArrayEquals(new Vector[]{}, TestOptions.SomeVectorList.getVectorList());
        assertArrayEquals(new Vector[]{}, TestOptions.SomeOtherVectorList.getVectorList());
        
        TestOptions.SomeVector.setVector(new Vector(1, 2, 3));
        TestOptions.SomeOtherVector.setVector(new Vector(2, 3, 4));
        TestOptions.SomeVectorList.setVectorList(new Vector[]{new Vector(3, 4, 5), new Vector(4, 5, 6)});
        TestOptions.SomeOtherVectorList.setVectorList(new Vector[]{new Vector(5, 6, 7), new Vector(6, 7, 8)});
        
        assertEquals(new Vector(1, 2, 3), this.file.get(TestOptions.SomeVector.path()));
        assertEquals(new Vector(2, 3, 4), this.file.get(TestOptions.SomeOtherVector.path()));
        
        assertEquals(new Vector(1, 2, 3), TestOptions.SomeVector.getVector());
        assertEquals(new Vector(2, 3, 4), TestOptions.SomeOtherVector.getVector());
        assertArrayEquals(new Vector[]{new Vector(3, 4, 5), new Vector(4, 5, 6)}, TestOptions.SomeVectorList.getVectorList());
        assertArrayEquals(new Vector[]{new Vector(5, 6, 7), new Vector(6, 7, 8)}, TestOptions.SomeOtherVectorList.getVectorList());
        
        // object
        assertNull(TestOptions.SomeObject.getObject());
        assertNull(TestOptions.SomeOtherObject.getObject());
        assertArrayEquals(new FooObject[]{}, TestOptions.SomeObjectList.getObjectList(FooObject.class));
        assertArrayEquals(new FooObject[]{}, TestOptions.SomeOtherObjectList.getObjectList(FooObject.class));
        
        final FooObject obj1 = new FooObject(1);
        final FooObject obj2 = new FooObject(2);
        final FooObject obj3 = new FooObject(3);
        final FooObject obj4 = new FooObject(4);
        final FooObject obj5 = new FooObject(5);
        final FooObject obj6 = new FooObject(6);
        
        TestOptions.SomeObject.setObject(obj1);
        TestOptions.SomeOtherObject.setObject(obj2);
        TestOptions.SomeObjectList.setObjectList(new FooObject[]{obj3, obj4});
        TestOptions.SomeOtherObjectList.setObjectList(new FooObject[]{obj5, obj6});
        
        assertEquals(obj1, TestOptions.SomeObject.getObject());
        assertEquals(obj2, TestOptions.SomeOtherObject.getObject());
        assertArrayEquals(new FooObject[]{obj3, obj4}, TestOptions.SomeObjectList.getObjectList(FooObject.class));
        assertArrayEquals(new FooObject[]{obj5, obj6}, TestOptions.SomeOtherObjectList.getObjectList(FooObject.class));
        
        // sections
        // boolean
        assertFalse(TestOptions.SomeSection.getBoolean("Boolean", false)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Boolean")); //$NON-NLS-1$
        TestOptions.SomeSection.setBoolean(true, "Boolean"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Boolean")); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.getBoolean("Boolean", false)); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getBooleanList("BooleanList", null)); //$NON-NLS-1$
        assertArrayEquals(new boolean[]{true, true}, TestOptions.SomeSection.getBooleanList("BooleanList", new boolean[]{true, true})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("BooleanList")); //$NON-NLS-1$
        TestOptions.SomeSection.setBooleanList(new boolean[]{true, false}, "BooleanList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("BooleanList")); //$NON-NLS-1$
        assertArrayEquals(new boolean[]{true, false}, TestOptions.SomeSection.getBooleanList("BooleanList", new boolean[]{true, true})); //$NON-NLS-1$

        assertFalse(TestOptions.SomeOtherSection.getBoolean("Boolean", false)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeOtherSection.isset("Boolean")); //$NON-NLS-1$
        TestOptions.SomeOtherSection.setBoolean(true, "Boolean"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeOtherSection.isset("Boolean")); //$NON-NLS-1$
        assertTrue(TestOptions.SomeOtherSection.getBoolean("Boolean", false)); //$NON-NLS-1$

        assertNull(TestOptions.SomeOtherSection.getBooleanList("BooleanList", null)); //$NON-NLS-1$
        assertArrayEquals(new boolean[]{true, true}, TestOptions.SomeOtherSection.getBooleanList("BooleanList", new boolean[]{true, true})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeOtherSection.isset("BooleanList")); //$NON-NLS-1$
        TestOptions.SomeOtherSection.setBooleanList(new boolean[]{true, false}, "BooleanList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeOtherSection.isset("BooleanList")); //$NON-NLS-1$
        assertArrayEquals(new boolean[]{true, false}, TestOptions.SomeOtherSection.getBooleanList("BooleanList", new boolean[]{true, true})); //$NON-NLS-1$
        
        //getKeys
        assertArrayEquals(new String[]{"Boolean", "BooleanList"}, TestOptions.SomeSection.getKeys(false)); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[]{"Boolean", "BooleanList"}, TestOptions.SomeOtherSection.getKeys(false)); //$NON-NLS-1$ //$NON-NLS-2$
        
        // byte
        assertEquals(1, TestOptions.SomeSection.getByte("Byte", (byte) 1)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Byte")); //$NON-NLS-1$
        TestOptions.SomeSection.setByte((byte) 2, "Byte"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Byte")); //$NON-NLS-1$
        assertEquals(2, TestOptions.SomeSection.getByte("Byte", (byte) 1)); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getByteList("ByteList", null)); //$NON-NLS-1$
        assertArrayEquals(new byte[]{1, 2}, TestOptions.SomeSection.getByteList("ByteList", new byte[]{1, 2})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("ByteList")); //$NON-NLS-1$
        TestOptions.SomeSection.setByteList(new byte[]{2, 3}, "ByteList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("ByteList")); //$NON-NLS-1$
        assertArrayEquals(new byte[]{2, 3}, TestOptions.SomeSection.getByteList("ByteList", new byte[]{1, 2})); //$NON-NLS-1$
        
        // character
        assertEquals('a', TestOptions.SomeSection.getCharacter("Character", 'a')); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Character")); //$NON-NLS-1$
        TestOptions.SomeSection.setCharacter('b', "Character"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Character")); //$NON-NLS-1$
        assertEquals('b', TestOptions.SomeSection.getCharacter("Character", 'a')); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getCharacterList("CharacterList", null)); //$NON-NLS-1$
        assertArrayEquals(new char[]{'a', 'b'}, TestOptions.SomeSection.getCharacterList("CharacterList", new char[]{'a', 'b'})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("CharacterList")); //$NON-NLS-1$
        TestOptions.SomeSection.setCharacterList(new char[]{'b', 'c'}, "CharacterList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("CharacterList")); //$NON-NLS-1$
        assertArrayEquals(new char[]{'b', 'c'}, TestOptions.SomeSection.getCharacterList("CharacterList", new char[]{'a', 'b'})); //$NON-NLS-1$
        
        // color
        assertEquals(Color.RED, TestOptions.SomeSection.getColor("Color", Color.RED)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Color")); //$NON-NLS-1$
        TestOptions.SomeSection.setColor(Color.BLUE, "Color"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Color")); //$NON-NLS-1$
        assertEquals(Color.BLUE, TestOptions.SomeSection.getColor("Color", Color.RED)); //$NON-NLS-1$

        assertArrayEquals(new Color[0], TestOptions.SomeSection.getColorList("ColorList")); //$NON-NLS-1$
        // assertArrayEquals(new Color[]{Color.RED, Color.BLUE}, TestOptions.SomeSection.getColorList("ColorList", new Color[]{Color.RED, Color.BLUE})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("ColorList")); //$NON-NLS-1$
        TestOptions.SomeSection.setColorList(new Color[]{Color.BLUE, Color.GREEN}, "ColorList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("ColorList")); //$NON-NLS-1$
        assertArrayEquals(new Color[]{Color.BLUE, Color.GREEN}, TestOptions.SomeSection.getColorList("ColorList")); //$NON-NLS-1$
        
        // double
        assertEquals(0.2, TestOptions.SomeSection.getDouble("Double", 0.2), 0); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Double")); //$NON-NLS-1$
        TestOptions.SomeSection.setDouble(0.3, "Double"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Double")); //$NON-NLS-1$
        assertEquals(0.3, TestOptions.SomeSection.getDouble("Double", 0.2), 0); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getDoubleList("DoubleList", null)); //$NON-NLS-1$
        assertArrayEquals(new double[]{0.3, 0.4}, TestOptions.SomeSection.getDoubleList("DoubleList", new double[]{0.3, 0.4}), 0); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("DoubleList")); //$NON-NLS-1$
        TestOptions.SomeSection.setDoubleList(new double[]{0.4, 0.5}, "DoubleList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("DoubleList")); //$NON-NLS-1$
        assertArrayEquals(new double[]{0.4, 0.5}, TestOptions.SomeSection.getDoubleList("DoubleList", new double[]{0.3, 0.4}), 0); //$NON-NLS-1$
        
        // float
        assertEquals(0.2f, TestOptions.SomeSection.getFloat("Float", 0.2f), 0); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Float")); //$NON-NLS-1$
        TestOptions.SomeSection.setFloat(0.3f, "Float"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Float")); //$NON-NLS-1$
        assertEquals(0.3f, TestOptions.SomeSection.getFloat("Float", 0.2f), 0); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getFloatList("FloatList", null)); //$NON-NLS-1$
        assertArrayEquals(new float[]{0.3f, 0.4f}, TestOptions.SomeSection.getFloatList("FloatList", new float[]{0.3f, 0.4f}), 0); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("FloatList")); //$NON-NLS-1$
        TestOptions.SomeSection.setFloatList(new float[]{0.4f, 0.5f}, "FloatList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("FloatList")); //$NON-NLS-1$
        assertArrayEquals(new float[]{0.4f, 0.5f}, TestOptions.SomeSection.getFloatList("FloatList", new float[]{0.3f, 0.4f}), 0); //$NON-NLS-1$
        
        // int
        assertEquals(2, TestOptions.SomeSection.getInt("Int", 2)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Int")); //$NON-NLS-1$
        TestOptions.SomeSection.setInt(3, "Int"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Int")); //$NON-NLS-1$
        assertEquals(3, TestOptions.SomeSection.getInt("Int", 2)); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getIntList("IntList", null)); //$NON-NLS-1$
        assertArrayEquals(new int[]{3, 4}, TestOptions.SomeSection.getIntList("IntList", new int[]{3, 4})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("IntList")); //$NON-NLS-1$
        TestOptions.SomeSection.setIntList(new int[]{4, 5}, "IntList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("IntList")); //$NON-NLS-1$
        assertArrayEquals(new int[]{4, 5}, TestOptions.SomeSection.getIntList("IntList", new int[]{3, 4})); //$NON-NLS-1$

        // ItemStack
        assertNull(TestOptions.SomeSection.getItemStack("ItemStack")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("ItemStack")); //$NON-NLS-1$
        TestOptions.SomeSection.setItemStack(new ItemStack(Material.ACACIA_DOOR), "ItemStack"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("ItemStack")); //$NON-NLS-1$
        assertEquals(new ItemStack(Material.ACACIA_DOOR), TestOptions.SomeSection.getItemStack("ItemStack")); //$NON-NLS-1$

        assertArrayEquals(new ItemStack[]{}, TestOptions.SomeSection.getItemStackList("ItemStackList")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("ItemStackList")); //$NON-NLS-1$
        TestOptions.SomeSection.setItemStackList(new ItemStack[]{new ItemStack(Material.ACACIA_DOOR_ITEM), new ItemStack(Material.ACACIA_FENCE)}, "ItemStackList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("ItemStackList")); //$NON-NLS-1$
        assertArrayEquals(new ItemStack[]{new ItemStack(Material.ACACIA_DOOR_ITEM), new ItemStack(Material.ACACIA_FENCE)}, TestOptions.SomeSection.getItemStackList("ItemStackList")); //$NON-NLS-1$
        
        // Long
        assertEquals(2, TestOptions.SomeSection.getLong("Long", 2)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Long")); //$NON-NLS-1$
        TestOptions.SomeSection.setLong(3, "Long"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Long")); //$NON-NLS-1$
        assertEquals(3, TestOptions.SomeSection.getLong("Long", 2)); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getLongList("LongList", null)); //$NON-NLS-1$
        assertArrayEquals(new long[]{3, 4}, TestOptions.SomeSection.getLongList("LongList", new long[]{3, 4})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("LongList")); //$NON-NLS-1$
        TestOptions.SomeSection.setLongList(new long[]{4, 5}, "LongList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("LongList")); //$NON-NLS-1$
        assertArrayEquals(new long[]{4, 5}, TestOptions.SomeSection.getLongList("LongList", new long[]{3, 4})); //$NON-NLS-1$

        // Object
        assertNull(TestOptions.SomeSection.getObject(FooObject.class, "Object")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Object")); //$NON-NLS-1$
        TestOptions.SomeSection.setObject(obj1, "Object"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Object")); //$NON-NLS-1$
        assertEquals(obj1, TestOptions.SomeSection.getObject(FooObject.class, "Object")); //$NON-NLS-1$

        assertArrayEquals(new FooObject[]{}, TestOptions.SomeSection.getObjectList(FooObject.class, "ObjectList")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("ObjectList")); //$NON-NLS-1$
        TestOptions.SomeSection.setObjectList(new FooObject[]{obj2, obj3}, "ObjectList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("ObjectList")); //$NON-NLS-1$
        assertArrayEquals(new FooObject[]{obj2, obj3}, TestOptions.SomeSection.getObjectList(FooObject.class, "ObjectList")); //$NON-NLS-1$

        // Player
        assertNull(TestOptions.SomeSection.getPlayer("Player")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Player")); //$NON-NLS-1$
        TestOptions.SomeSection.setPlayer(player1, "Player"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Player")); //$NON-NLS-1$
        assertEquals(player1, TestOptions.SomeSection.getPlayer("Player")); //$NON-NLS-1$

        assertArrayEquals(new ArenaPlayerInterface[]{}, TestOptions.SomeSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("PlayerList")); //$NON-NLS-1$
        TestOptions.SomeSection.setPlayerList(new ArenaPlayerInterface[]{player3, player4}, "PlayerList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("PlayerList")); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{player3, player4}, TestOptions.SomeSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        
        // Short
        assertEquals(2, TestOptions.SomeSection.getShort("Short", (short) 2)); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Short")); //$NON-NLS-1$
        TestOptions.SomeSection.setShort((short) 3, "Short"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Short")); //$NON-NLS-1$
        assertEquals(3, TestOptions.SomeSection.getShort("Short", (short) 2)); //$NON-NLS-1$

        assertNull(TestOptions.SomeSection.getShortList("ShortList", null)); //$NON-NLS-1$
        assertArrayEquals(new short[]{3, 4}, TestOptions.SomeSection.getShortList("ShortList", new short[]{3, 4})); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("ShortList")); //$NON-NLS-1$
        TestOptions.SomeSection.setShortList(new short[]{4, 5}, "ShortList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("ShortList")); //$NON-NLS-1$
        assertArrayEquals(new short[]{4, 5}, TestOptions.SomeSection.getShortList("ShortList", new short[]{3, 4})); //$NON-NLS-1$
        
        // String
        assertEquals("2", TestOptions.SomeSection.getString("String", "2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertFalse(TestOptions.SomeSection.isset("String")); //$NON-NLS-1$
        TestOptions.SomeSection.setString("3", "String"); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(TestOptions.SomeSection.isset("String")); //$NON-NLS-1$
        assertEquals("3", TestOptions.SomeSection.getString("String", "2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        assertNull(TestOptions.SomeSection.getStringList("StringList", null)); //$NON-NLS-1$
        assertArrayEquals(new String[]{"3", "4"}, TestOptions.SomeSection.getStringList("StringList", new String[]{"3", "4"})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        assertFalse(TestOptions.SomeSection.isset("StringList")); //$NON-NLS-1$
        TestOptions.SomeSection.setStringList(new String[]{"4", "5"}, "StringList"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(TestOptions.SomeSection.isset("StringList")); //$NON-NLS-1$
        assertArrayEquals(new String[]{"4", "5"}, TestOptions.SomeSection.getStringList("StringList", new String[]{"3", "4"})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

        // Vector
        assertNull(TestOptions.SomeSection.getVector("Vector")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("Vector")); //$NON-NLS-1$
        TestOptions.SomeSection.setVector(new Vector(1, 2, 3), "Vector"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("Vector")); //$NON-NLS-1$
        assertEquals(new Vector(1, 2, 3), TestOptions.SomeSection.getVector("Vector")); //$NON-NLS-1$

        assertArrayEquals(new Vector[]{}, TestOptions.SomeSection.getVectorList("VectorList")); //$NON-NLS-1$
        assertFalse(TestOptions.SomeSection.isset("VectorList")); //$NON-NLS-1$
        TestOptions.SomeSection.setVectorList(new Vector[]{new Vector(2, 3, 4), new Vector(3, 4, 5)}, "VectorList"); //$NON-NLS-1$
        assertTrue(TestOptions.SomeSection.isset("VectorList")); //$NON-NLS-1$
        assertArrayEquals(new Vector[]{new Vector(2, 3, 4), new Vector(3, 4, 5)}, TestOptions.SomeSection.getVectorList("VectorList")); //$NON-NLS-1$
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
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid1()
    {
        InvalidConfig.Invalid.getBoolean();
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid2()
    {
        InvalidConfig.Dummy.getBoolean();
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid3()
    {
        TestOptions.SomeDummy.getBoolean();
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid1b()
    {
        InvalidConfig.Invalid.setBoolean(true);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid2b()
    {
        InvalidConfig.Dummy.setBoolean(true);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid3b()
    {
        TestOptions.SomeDummy.setBoolean(true);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid1c()
    {
        InvalidConfig.Invalid.getKeys(false);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid2c()
    {
        InvalidConfig.Dummy.getKeys(false);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalid3c()
    {
        TestOptions.SomeDummy.getKeys(false);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidField()
    {
        new InvalidConfig2().isBoolean();
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidField2()
    {
        new InvalidConfig2().getKeys(false);
    }
    
    /**
     * Tests invalid config.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidField3()
    {
        new InvalidConfig2().path();
    }
    
    /**
     * Tests the list resetting
     */
    @Test
    public void testResettingList()
    {
        final ArenaPlayerInterface player1 = createPlayer();
        final ArenaPlayerInterface player2 = createPlayer();
        final ArenaPlayerInterface player3 = createPlayer();
        final ArenaPlayerInterface player4 = createPlayer();
        
        TestOptions.SomePlayerList.setPlayerList(new ArenaPlayerInterface[]{player3, player4});
        assertArrayEquals(new ArenaPlayerInterface[]{player3, player4}, TestOptions.SomePlayerList.getPlayerList());
        TestOptions.SomePlayerList.setPlayerList(new ArenaPlayerInterface[]{player1, player2});
        assertArrayEquals(new ArenaPlayerInterface[]{player1, player2}, TestOptions.SomePlayerList.getPlayerList());
        TestOptions.SomePlayerList.setPlayerList(new ArenaPlayerInterface[]{});
        assertArrayEquals(new ArenaPlayerInterface[]{}, TestOptions.SomePlayerList.getPlayerList());
        
        TestOptions.SomeSection.setPlayerList(new ArenaPlayerInterface[]{player3, player4}, "PlayerList"); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{player3, player4}, TestOptions.SomeSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        TestOptions.SomeSection.setPlayerList(new ArenaPlayerInterface[]{player1, player2}, "PlayerList"); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{player1, player2}, TestOptions.SomeSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        TestOptions.SomeSection.setPlayerList(new ArenaPlayerInterface[]{}, "PlayerList"); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{}, TestOptions.SomeSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        
        TestOptions.SomeOtherSection.setPlayerList(new ArenaPlayerInterface[]{player3, player4}, "PlayerList"); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{player3, player4}, TestOptions.SomeOtherSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        TestOptions.SomeOtherSection.setPlayerList(new ArenaPlayerInterface[]{player1, player2}, "PlayerList"); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{player1, player2}, TestOptions.SomeOtherSection.getPlayerList("PlayerList")); //$NON-NLS-1$
        TestOptions.SomeOtherSection.setPlayerList(new ArenaPlayerInterface[]{}, "PlayerList"); //$NON-NLS-1$
        assertArrayEquals(new ArenaPlayerInterface[]{}, TestOptions.SomeOtherSection.getPlayerList("PlayerList")); //$NON-NLS-1$
    }
    
    /**
     * Some invalid config.
     * @author mepeisen
     */
    public static final class InvalidConfig2 implements ConfigurationValueInterface
    {
        // empty
    }
    
    /**
     * Some invalid config.
     * @author mepeisen
     */
    public static enum InvalidConfig implements ConfigurationValueInterface
    {
        /** invlid config. */
        Invalid,
        /** dummy config. */
        @ConfigurationBool
        Dummy
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

        /** obj value. */
        private int i;
        
        /**
         * Constructor.
         */
        @SuppressWarnings("unused")
        public FooObject()
        {
            // empty
        }

        /**
         * @param i
         */
        public FooObject(int i)
        {
            this.i = i;
        }

        @Override
        public void readFromConfig(org.bukkit.configuration.ConfigurationSection section)
        {
            this.i = section.getInt("i"); //$NON-NLS-1$
        }

        @Override
        public void writeToConfig(org.bukkit.configuration.ConfigurationSection section)
        {
            section.set("i", this.i); //$NON-NLS-1$
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.i;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FooObject other = (FooObject) obj;
            if (this.i != other.i)
                return false;
            return true;
        }
        
    }
    
}
