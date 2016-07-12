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

package com.comze_instancelabs.minigamesapi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.junit.Test;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaPlayer;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.ChannelStrings;
import com.comze_instancelabs.minigamesapi.testutil.TestUtil;

/**
 * The tester for the arena class.
 * 
 * @author mepeisen
 * 
 * @see Arena
 */
public class ArenaTest extends TestUtil
{
    
    /** the default arena name. */
    private static final String ARENA = "junit-arena"; //$NON-NLS-1$
    
    /** the default arena name. */
    private static final String ARENA2 = "junit-arena2"; //$NON-NLS-1$
    
    /** the default arena name. */
    private static final String ARENA3 = "junit-arena3"; //$NON-NLS-1$
    
    /** the player name. */
    private static final String PLAYER1 = "player1"; //$NON-NLS-1$
    
    /** the player name. */
    private static final String PLAYER2 = "player2"; //$NON-NLS-1$
    
    /** the player name. */
    private static final String PLAYER3 = "player3"; //$NON-NLS-1$
    
    /** the junit minigame. */
    private static final String MINIGAME = "$JUNIT-ARENA-TEST"; //$NON-NLS-1$
    
    /**
     * Tests the arena class constructor.
     */
    @Test
    public void testConstructor()
    {
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "Constructor"); //$NON-NLS-1$
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        
        assertSame(arena.getPlugin(), minigame.javaPlugin);
        assertSame(arena.getPluginInstance(), minigame.pluginInstance);
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getName());
        assertEquals(ArenaType.DEFAULT, arena.getArenaType());
        
        assertSame(arena, arena.getArena());
    }
    
    /**
     * Tests the arena class constructor.
     */
    @Test
    public void testConstructorType()
    {
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "ConstructorType"); //$NON-NLS-1$
        final Arena arena = new Arena(minigame.javaPlugin, ARENA, ArenaType.JUMPNRUN);
        
        assertSame(arena.getPlugin(), minigame.javaPlugin);
        assertSame(arena.getPluginInstance(), minigame.pluginInstance);
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getInternalName());
        assertEquals(ARENA, arena.getName());
        assertEquals(ArenaType.JUMPNRUN, arena.getArenaType());
    }
    
    /**
     * Tests the init method call.
     */
    @Test
    public void testInit()
    {
        this.initWorld("world"); //$NON-NLS-1$
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "Init", (mg) -> { //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, "lobby", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            mg.addArenaComponentToConfig(ARENA3, "spawns.spawn0", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.BOUNDS_LOW, "world", 1, 2, 3, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.BOUNDS_HIGH, "world", 2, 3, 4, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.LOBBY_BOUNDS_LOW, "world", 3, 4, 5, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.LOBBY_BOUNDS_HIGH, "world", 4, 5, 6, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.SPEC_BOUNDS_LOW, "world", 5, 6, 7, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.SPEC_BOUNDS_HIGH, "world", 6, 7, 8, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA3, ArenaConfigStrings.SPEC_SPAWN, "world", 9, 10, 11, 80, 80); //$NON-NLS-1$
            
            mg.arenasYml.getConfigurationSection("arenas").getConfigurationSection(ARENA3).set("displayname", "FOO"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            mg.arenasYml.getConfigurationSection("arenas").getConfigurationSection(ARENA3).set("showscoreboard", Boolean.FALSE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        
        // 1) normal init with unknown arena
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        
        final Location signLoc = new Location(null, 1, 1, 1);
        
        final ArrayList<Location> spawns = new ArrayList<>();
        spawns.add(new Location(null, 2, 2, 2));
        spawns.add(new Location(null, 3, 3, 3));
        
        final Location mainlobby = new Location(null, 4, 4, 4);
        
        final Location waitinglobby = new Location(null, 5, 5, 5);
        
        arena.init(signLoc, spawns, mainlobby, waitinglobby, 10, 2, false);
        
        assertEquals(1, arena.getSignLocation().getX(), 0);
        assertEquals(1, arena.getSignLocation().getY(), 0);
        assertEquals(1, arena.getSignLocation().getZ(), 0);
        
        assertEquals(2, arena.getSpawns().size());
        assertEquals(2, arena.getSpawns().get(0).getX(), 0);
        assertEquals(2, arena.getSpawns().get(0).getY(), 0);
        assertEquals(2, arena.getSpawns().get(0).getZ(), 0);
        assertEquals(3, arena.getSpawns().get(1).getX(), 0);
        assertEquals(3, arena.getSpawns().get(1).getY(), 0);
        assertEquals(3, arena.getSpawns().get(1).getZ(), 0);
        
        assertEquals(4, arena.getMainLobby().getX(), 0);
        assertEquals(4, arena.getMainLobby().getY(), 0);
        assertEquals(4, arena.getMainLobby().getZ(), 0);
        
        assertEquals(5, arena.getWaitingLobby().getX(), 0);
        assertEquals(5, arena.getWaitingLobby().getY(), 0);
        assertEquals(5, arena.getWaitingLobby().getZ(), 0);
        
        assertFalse(arena.isVIPArena());
        assertEquals(2, arena.getMinPlayers());
        assertEquals(10, arena.getMaxPlayers());

        assertTrue(arena.isSuccessfullyInit());
        
        // default values
        assertTrue(arena.getShowScoreboard());
        assertFalse(arena.getAlwaysPvP());
        
        // setters
        arena.setAlwaysPvP(true);
        assertTrue(arena.getAlwaysPvP());
        
        final Location l2 = new Location(null,  -1, -2, -3);
        arena.setSignLocation(l2);
        assertEquals(-1, arena.getSignLocation().getX(), 0);
        assertEquals(-2, arena.getSignLocation().getY(), 0);
        assertEquals(-3, arena.getSignLocation().getZ(), 0);
        
        arena.setMinPlayers(17);
        arena.setMaxPlayers(25);
        assertEquals(17, arena.getMinPlayers());
        assertEquals(25, arena.getMaxPlayers());
        
        arena.setVIPArena(true);
        assertTrue(arena.isVIPArena());
        assertEquals(ArenaState.JOIN, arena.getArenaState());
        
        // player management
        assertEquals(0, arena.getAllPlayers().size());
        arena.addPlayer(PLAYER1);
        assertEquals(1, arena.getAllPlayers().size());
        assertEquals(PLAYER1, arena.getAllPlayers().get(0));
        assertTrue(arena.containsPlayer(PLAYER1));
        assertFalse(arena.containsPlayer(PLAYER2));
        assertFalse(arena.containsPlayer(PLAYER3));
        arena.addPlayer(PLAYER2);
        assertEquals(2, arena.getAllPlayers().size());
        assertEquals(PLAYER1, arena.getAllPlayers().get(0));
        assertEquals(PLAYER2, arena.getAllPlayers().get(1));
        assertTrue(arena.containsPlayer(PLAYER1));
        assertTrue(arena.containsPlayer(PLAYER2));
        assertFalse(arena.containsPlayer(PLAYER3));
        arena.addPlayer(PLAYER3);
        assertEquals(3, arena.getAllPlayers().size());
        assertEquals(PLAYER1, arena.getAllPlayers().get(0));
        assertEquals(PLAYER2, arena.getAllPlayers().get(1));
        assertEquals(PLAYER3, arena.getAllPlayers().get(2));
        assertTrue(arena.containsPlayer(PLAYER1));
        assertTrue(arena.containsPlayer(PLAYER2));
        assertTrue(arena.containsPlayer(PLAYER3));
        arena.removePlayer(PLAYER2);
        assertEquals(2, arena.getAllPlayers().size());
        assertEquals(PLAYER1, arena.getAllPlayers().get(0));
        assertEquals(PLAYER3, arena.getAllPlayers().get(1));
        assertTrue(arena.containsPlayer(PLAYER1));
        assertFalse(arena.containsPlayer(PLAYER2));
        assertTrue(arena.containsPlayer(PLAYER3));
        
        
        
        // 2) init without spawns, deprecated method (should be changed to init as soon as we remove the method)
        final Arena arena2 = new Arena(minigame.javaPlugin, ARENA2);
        arena2.initArena(signLoc, null, mainlobby, waitinglobby, 5, 4, true);
        
        assertEquals(1, arena2.getSignLocation().getX(), 0);
        assertEquals(1, arena2.getSignLocation().getY(), 0);
        assertEquals(1, arena2.getSignLocation().getZ(), 0);
        
        assertEquals(0, arena2.getSpawns().size());
        assertEquals(4, arena2.getMainLobby().getX(), 0);
        assertEquals(4, arena2.getMainLobby().getY(), 0);
        assertEquals(4, arena2.getMainLobby().getZ(), 0);
        
        assertEquals(5, arena2.getWaitingLobby().getX(), 0);
        assertEquals(5, arena2.getWaitingLobby().getY(), 0);
        assertEquals(5, arena2.getWaitingLobby().getZ(), 0);
        
        assertTrue(arena2.isVIPArena());
        assertEquals(4, arena2.getMinPlayers());
        assertEquals(5, arena2.getMaxPlayers());
        
        assertTrue(arena2.isSuccessfullyInit());
        
        
        
        // 3) init configured arena
        final Arena arena3 = new Arena(minigame.javaPlugin, ARENA3);
        arena3.init(signLoc, null, mainlobby, waitinglobby, 5, 4, true);
        
        assertEquals(1, arena3.getBoundaries().getLowLoc().getX(), 0);
        assertEquals(2, arena3.getBoundaries().getLowLoc().getY(), 0);
        assertEquals(3, arena3.getBoundaries().getLowLoc().getZ(), 0);
        assertEquals(2, arena3.getBoundaries().getHighLoc().getX(), 0);
        assertEquals(3, arena3.getBoundaries().getHighLoc().getY(), 0);
        assertEquals(4, arena3.getBoundaries().getHighLoc().getZ(), 0);
        
        assertEquals(3, arena3.getLobbyBoundaries().getLowLoc().getX(), 0);
        assertEquals(4, arena3.getLobbyBoundaries().getLowLoc().getY(), 0);
        assertEquals(5, arena3.getLobbyBoundaries().getLowLoc().getZ(), 0);
        assertEquals(4, arena3.getLobbyBoundaries().getHighLoc().getX(), 0);
        assertEquals(5, arena3.getLobbyBoundaries().getHighLoc().getY(), 0);
        assertEquals(6, arena3.getLobbyBoundaries().getHighLoc().getZ(), 0);
        
        assertEquals(5, arena3.getSpecBoundaries().getLowLoc().getX(), 0);
        assertEquals(6, arena3.getSpecBoundaries().getLowLoc().getY(), 0);
        assertEquals(7, arena3.getSpecBoundaries().getLowLoc().getZ(), 0);
        assertEquals(6, arena3.getSpecBoundaries().getHighLoc().getX(), 0);
        assertEquals(7, arena3.getSpecBoundaries().getHighLoc().getY(), 0);
        assertEquals(8, arena3.getSpecBoundaries().getHighLoc().getZ(), 0);
    
        // additional configs
        assertEquals("FOO", arena3.getDisplayName()); //$NON-NLS-1$
        assertFalse(arena3.getShowScoreboard());
    }
    
    /**
     * Tests joining the lobby and waiting for the game to start.
     */
    @Test
    public void testJoinLobbyAndWait()
    {
        final World world = this.initWorld("world"); //$NON-NLS-1$
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "NormalGame", (mg) -> { //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA, "lobby", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            mg.addArenaComponentToConfig(ARENA, "spawns.spawn0", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.BOUNDS_LOW, "world", 1, 2, 3, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.BOUNDS_HIGH, "world", 2, 3, 4, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.LOBBY_BOUNDS_LOW, "world", 3, 4, 5, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.LOBBY_BOUNDS_HIGH, "world", 4, 5, 6, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.SPEC_BOUNDS_LOW, "world", 5, 6, 7, 80, 80); //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.SPEC_BOUNDS_HIGH, "world", 6, 7, 8, 80, 80); //$NON-NLS-1$
            
            mg.addArenaComponentToConfig(ARENA, ArenaConfigStrings.SPEC_SPAWN, "world", 9, 10, 11, 80, 80); //$NON-NLS-1$
            
            mg.arenasYml.getConfigurationSection("arenas").getConfigurationSection(ARENA).set("author", "JUNIT-AUTHOR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            mg.arenasYml.getConfigurationSection("arenas").getConfigurationSection(ARENA).set("description", "JUNIT TEST CASE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        
        minigame.pluginInstance.getMessagesConfig().you_joined_arena = "<player> joined <arena> of <game>"; //$NON-NLS-1$
        minigame.pluginInstance.getMessagesConfig().minigame_description = "description"; //$NON-NLS-1$
        minigame.pluginInstance.getMessagesConfig().author_of_the_map = "<arena> was created by <author>"; //$NON-NLS-1$
        minigame.pluginInstance.getMessagesConfig().description_of_the_map = "<arena> description: <description>"; //$NON-NLS-1$
        minigame.pluginInstance.getMessagesConfig().broadcast_player_joined = "<player> joined arena <count>/<maxcount>"; //$NON-NLS-1$
        
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        final Location signLoc = new Location(world, 1, 1, 1);
        final ArrayList<Location> spawns = new ArrayList<>();
        spawns.add(new Location(world, 2, 2, 2));
        spawns.add(new Location(world, 3, 3, 3));
        final Location mainlobby = new Location(world, 4, 4, 4);
        final Location waitinglobby = new Location(world, 5, 5, 5);
        arena.init(signLoc, null, mainlobby, waitinglobby, 5, 3, false);

        final Player player1 = this.mockOnlinePlayer(PLAYER1, UUID.randomUUID());
        final Player player2 = this.mockOnlinePlayer(PLAYER2, UUID.randomUUID());
        player1.getInventory().addItem(new ItemStack(Material.APPLE, 12));
        when(player1.getGameMode()).thenReturn(GameMode.CREATIVE);
        when(player1.getLevel()).thenReturn(16);
        when(player2.getGameMode()).thenReturn(GameMode.ADVENTURE);
        when(player2.getLevel()).thenReturn(17);
        when(player2.isInsideVehicle()).thenReturn(true);
        final Entity vehicle = mock(Entity.class);
        when(player2.getVehicle()).thenReturn(vehicle);
        
        // join the lobby
        arena.joinPlayerLobby(PLAYER1);
        arena.joinPlayerLobby(PLAYER2);
        assertTrue(arena.containsPlayer(PLAYER1));
        assertTrue(arena.containsPlayer(PLAYER2));
        
        // verify messages
        verify(player1, times(1)).sendMessage(PLAYER1 + " joined " + ARENA + " of " + MINIGAME + "NormalGame"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        verify(player2, times(1)).sendMessage(PLAYER2 + " joined " + ARENA + " of " + MINIGAME + "NormalGame"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        verify(player1, times(1)).sendMessage("description"); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage("description"); //$NON-NLS-1$
        verify(player1, times(1)).sendMessage(ARENA + " was created by JUNIT-AUTHOR"); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage(ARENA + " was created by JUNIT-AUTHOR"); //$NON-NLS-1$
        verify(player1, times(1)).sendMessage(ARENA + " description: JUNIT TEST CASE"); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage(ARENA + " description: JUNIT TEST CASE"); //$NON-NLS-1$
        
        verify(player1, times(1)).sendMessage(PLAYER2 + " joined arena 2/5"); //$NON-NLS-1$
        
        for (int i = 0; i < 15; i++) this.tick();
        
        // verify saved inv/gamemode etc.
        final ArenaPlayer aplayer1 = ArenaPlayer.getPlayerInstance(PLAYER1);
        final ArenaPlayer aplayer2 = ArenaPlayer.getPlayerInstance(PLAYER2);
        assertEquals(GameMode.CREATIVE, aplayer1.getOriginalGamemode());
        assertEquals(16, aplayer1.getOriginalXplvl());
        assertEquals(GameMode.ADVENTURE, aplayer2.getOriginalGamemode());
        assertEquals(17, aplayer2.getOriginalXplvl());
        // TODO inventory

        // verify set game mode survival
        verify(player1, times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player2, times(1)).setGameMode(GameMode.SURVIVAL);
        
        // verify set potion effect (heal)
        // TODO Why are there three heal invocations?
        verify(player1, times(2)).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 50));
        verify(player2, times(2)).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 50));
        verify(player1, times(1)).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 30)); // From Util.teleportFixed
        verify(player2, times(1)).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 2, 30)); // From Util.teleportFixed
        
        // verify teleport to lobby
        verify(player1, times(1)).teleport(waitinglobby, TeleportCause.PLUGIN);
        verify(player1, times(1)).setFallDistance(-1F);
        verify(player1, times(1)).setVelocity(new Vector(0D, 0D, 0D));
        verify(player1, times(1)).setFireTicks(0);
        verify(player2, times(1)).teleport(waitinglobby, TeleportCause.PLUGIN);
        verify(player2, times(1)).setFallDistance(-1F);
        verify(player2, times(1)).setVelocity(new Vector(0D, 0D, 0D));
        verify(player2, times(1)).setFireTicks(0);
        verify(player2, times(1)).leaveVehicle();
        verify(vehicle, times(1)).eject();
        
        // sign updates (bungee)
        verifyPluginMessage(this.minigameTest.api, ChannelStrings.CHANNEL_BUNGEE_CORD,
                "Forward", //$NON-NLS-1$
                "ALL", //$NON-NLS-1$
                ChannelStrings.SUBCHANNEL_MINIGAMESLIB_SIGN,
                new Object[]{MINIGAME + "NormalGame:" + ARENA + ":" + ArenaState.JOIN.name() + ":1:5"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        verifyPluginMessage(this.minigameTest.api, ChannelStrings.CHANNEL_BUNGEE_CORD,
                "Forward", //$NON-NLS-1$
                "ALL", //$NON-NLS-1$
                ChannelStrings.SUBCHANNEL_MINIGAMESLIB_SIGN,
                new Object[]{MINIGAME + "NormalGame:" + ARENA + ":" + ArenaState.JOIN.name() + ":2:5"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        // TODO sendAllHolograms
        // TODO Util.updateSign
        
        // TODO Saved inventory (ArenaPlayer)
        
        // TODO Add Task to clear inv (Arena:968)
        // TODO giveLobbyItems (Arena:973)
    }
    
}
