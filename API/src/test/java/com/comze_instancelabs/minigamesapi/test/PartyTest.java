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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.Party;
import com.comze_instancelabs.minigamesapi.config.PartyMessagesConfig;

/**
 * Test for the party class
 * 
 * @author mepeisen
 * 
 * @see Party
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, MinigamesAPI.class})
public class PartyTest
{
    
    /** owner name. */
    private static final UUID OWNER = UUID.randomUUID();
    
    /** friend name. */
    private static final UUID FRIEND_1 = UUID.randomUUID();
    
    /** friend name. */
    private static final UUID FRIEND_2 = UUID.randomUUID();
    
    /**
     * Test that owner name is returned.
     */
    @Test
    public void testReturnsOwner()
    {
        final Party party = new Party(OWNER);
        assertEquals(OWNER, party.getOwner());
    }
    
    /**
     * Test that party is empty upon creation.
     */
    @Test
    public void testEmpty()
    {
        final Party party = new Party(OWNER);
        assertEquals(0, party.getPlayers().size());
    }
    
    /**
     * Simple Mock minigames api.
     */
    private void mockAPI()
    {
        final MinigamesAPI api = mock(MinigamesAPI.class);
        final PartyMessagesConfig messages = mock(PartyMessagesConfig.class);
        Whitebox.setInternalState(api, "partymessages", messages); //$NON-NLS-1$
        messages.you_joined_party = "JOINED PARTY <player>"; //$NON-NLS-1$
        messages.player_joined_party = "PLAYER <player> JOINED"; //$NON-NLS-1$
        messages.you_left_party = "LEFT PARTY <player>"; //$NON-NLS-1$
        messages.player_left_party = "PLAYER <player> LEFT"; //$NON-NLS-1$
        messages.party_disbanded = "DISBAND"; //$NON-NLS-1$
        Whitebox.setInternalState(api, "global_party", new HashMap<>()); //$NON-NLS-1$
        
        mockStatic(MinigamesAPI.class);
        when(MinigamesAPI.getAPI()).thenReturn(api);

        when(api.createParty(any(UUID.class))).thenCallRealMethod();
        when(api.getParties()).thenCallRealMethod();
        when(api.hasParty(any(UUID.class))).thenCallRealMethod();
        when(api.getParty(any(UUID.class))).thenCallRealMethod();
        doCallRealMethod().when(api).removeParty(any(UUID.class));
    }
    
    /**
     * Test that player is added to party
     */
    @Test
    public void testPlayerAdded()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        this.mockAPI();
        
        // test
        final Party party = new Party(OWNER);
        party.addPlayer(FRIEND_1);
        assertEquals(1, party.getPlayers().size());
        assertEquals(FRIEND_1, party.getPlayers().get(0));
        
        // epilog
        verify(player1, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
    }
    
    /**
     * Test that player is contained in party
     */
    @Test
    public void testPlayerContains()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        this.mockAPI();
        
        // test
        final Party party = new Party(OWNER);
        party.addPlayer(FRIEND_1);
        assertTrue(party.containsPlayer(FRIEND_1));
        assertFalse(party.containsPlayer(FRIEND_2));
    }
    
    /**
     * Test that two players are added to party
     */
    @Test
    public void testPlayer2Added()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(FRIEND_2.toString());
        when(player2.getUniqueId()).thenReturn(FRIEND_2);
        
        when(Bukkit.getPlayer(FRIEND_2)).thenReturn(player2);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        this.mockAPI();
        
        // test
        final Party party = new Party(OWNER);
        party.addPlayer(FRIEND_1);
        party.addPlayer(FRIEND_2);
        assertEquals(2, party.getPlayers().size());
        assertEquals(FRIEND_1, party.getPlayers().get(0));
        assertEquals(FRIEND_2, party.getPlayers().get(1));
        
        // epilog
        verify(player1, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player1, times(1)).sendMessage("PLAYER " + FRIEND_2.toString() + " JOINED"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Test that player hast left the party
     */
    @Test
    public void testPlayerLeft()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        this.mockAPI();
        
        // test
        final Party party = new Party(OWNER);
        party.addPlayer(FRIEND_1);
        party.removePlayer(FRIEND_1);
        assertEquals(0, party.getPlayers().size());
        
        // epilog
        verify(player1, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player1, times(1)).sendMessage("LEFT PARTY " + OWNER.toString()); //$NON-NLS-1$
    }
    
    /**
     * Test that two players are added and one left the party
     */
    @Test
    public void testPlayer2Left()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        final Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(FRIEND_2.toString());
        when(player2.getUniqueId()).thenReturn(FRIEND_2);
        
        when(Bukkit.getPlayer(FRIEND_2)).thenReturn(player2);
        
        this.mockAPI();
        
        // test
        final Party party = new Party(OWNER);
        party.addPlayer(FRIEND_1);
        party.addPlayer(FRIEND_2);
        assertTrue(party.removePlayer(FRIEND_1));
        assertEquals(1, party.getPlayers().size());
        assertEquals(FRIEND_2, party.getPlayers().get(0));
        
        // epilog
        verify(player1, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player1, times(1)).sendMessage("PLAYER " + FRIEND_2.toString() + " JOINED"); //$NON-NLS-1$ //$NON-NLS-2$
        verify(player1, times(1)).sendMessage("LEFT PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage("PLAYER " + FRIEND_1.toString() + " LEFT"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Test that players are informed about disband
     */
    @Test
    public void testDisbandClearsList()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(FRIEND_2.toString());
        when(player2.getUniqueId()).thenReturn(FRIEND_2);
        
        when(Bukkit.getPlayer(FRIEND_2)).thenReturn(player2);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        this.mockAPI();
        
        // test
        // TODO clearing the list should not depend on finding it in global_party
        final Party party = MinigamesAPI.getAPI().createParty(OWNER);
        party.addPlayer(FRIEND_1);
        party.addPlayer(FRIEND_2);
        party.disband();
        assertEquals(0, party.getPlayers().size());
    }
    
    /**
     * Test that players are informed about disband
     */
    @Test
    public void testDisbandInformsPlayers()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(FRIEND_2.toString());
        when(player2.getUniqueId()).thenReturn(FRIEND_2);
        
        when(Bukkit.getPlayer(FRIEND_2)).thenReturn(player2);
        
        final Player owner = mock(Player.class);
        when(owner.getName()).thenReturn(OWNER.toString());
        when(owner.getUniqueId()).thenReturn(OWNER);
        
        when(Bukkit.getPlayer(OWNER)).thenReturn(owner);
        
        this.mockAPI();
        
        // test
        final Party party = new Party(OWNER);
        party.addPlayer(FRIEND_1);
        party.addPlayer(FRIEND_2);
        party.disband();
        
        // epilog
        verify(player1, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage("JOINED PARTY " + OWNER.toString()); //$NON-NLS-1$
        verify(player1, times(1)).sendMessage("PLAYER " + FRIEND_2.toString() + " JOINED"); //$NON-NLS-1$ //$NON-NLS-2$
        verify(player1, times(1)).sendMessage("DISBAND"); //$NON-NLS-1$
        verify(player2, times(1)).sendMessage("DISBAND"); //$NON-NLS-1$
    }
    
    /**
     * Test if disband removes from global party map
     */
    @Test
    public void testDisbandRemovesFromApi()
    {
        // prolog
        final Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(FRIEND_1.toString());
        when(player1.getUniqueId()).thenReturn(FRIEND_1);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(FRIEND_1)).thenReturn(player1);
        
        final Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(FRIEND_2.toString());
        when(player2.getUniqueId()).thenReturn(FRIEND_2);
        
        when(Bukkit.getPlayer(FRIEND_2)).thenReturn(player2);
        
        this.mockAPI();
        
        // test
        final Party party = MinigamesAPI.getAPI().createParty(OWNER);
        party.disband();
        assertFalse(MinigamesAPI.getAPI().hasParty(OWNER));
    }
    
}
