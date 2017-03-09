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

package de.minigameslib.mgapi.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;

/**
 * Test case for {@link ArenaMatchImpl}
 * 
 * @author mepeisen
 */
public class ArenaMatchImplTest
{
    
    /** plugin manager mock. */
    private PluginManager pluginManager;

    /**
     * Tests constructor defaults
     */
    @Test
    public void testConstructorDefaults()
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        assertFalse(spmatch.isTeamMatch());
        
        assertFalse(spmatch.isAborted());
        assertNotNull(spmatch.get(CommonTeams.Unknown));
        assertNotNull(spmatch.get(CommonTeams.Winners));
        assertNotNull(spmatch.get(CommonTeams.Losers));
        assertNotNull(spmatch.get(CommonTeams.Spectators));
        assertNotNull(spmatch.getCreated());
        assertNull(spmatch.getStarted());
        assertNull(spmatch.getFinished());
        assertNull(spmatch.getPreferredTeam());
        assertEquals(0, spmatch.getParticipants(false).size());
        assertEquals(0, spmatch.getParticipants(true).size());
        assertEquals(0, spmatch.getParticipantCount(false));
        assertEquals(0, spmatch.getParticipantCount(true));
        assertEquals(0, spmatch.getWinners().size());
        assertEquals(0, spmatch.getLosers().size());
        assertEquals(0, spmatch.getResults().size());
        assertEquals(0, spmatch.getResultCount());
        assertEquals(0, spmatch.get(CommonTeams.Unknown).getMembers().size());
        assertEquals(0, spmatch.get(CommonTeams.Winners).getMembers().size());
        assertEquals(0, spmatch.get(CommonTeams.Losers).getMembers().size());
        assertEquals(0, spmatch.get(CommonTeams.Spectators).getMembers().size());
        
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(null, true);
        assertTrue(teamMatch.isTeamMatch());
        
        assertFalse(teamMatch.isAborted());
        assertNotNull(teamMatch.get(CommonTeams.Unknown));
        assertNotNull(teamMatch.get(CommonTeams.Winners));
        assertNotNull(teamMatch.get(CommonTeams.Losers));
        assertNotNull(teamMatch.get(CommonTeams.Spectators));
        assertNotNull(teamMatch.getCreated());
        assertNull(teamMatch.getStarted());
        assertNull(teamMatch.getFinished());
        assertNull(teamMatch.getPreferredTeam());
        assertEquals(0, teamMatch.getParticipants(false).size());
        assertEquals(0, teamMatch.getParticipants(true).size());
        assertEquals(0, teamMatch.getParticipantCount(false));
        assertEquals(0, teamMatch.getParticipantCount(true));
        assertEquals(0, teamMatch.getWinners().size());
        assertEquals(0, teamMatch.getLosers().size());
        assertEquals(0, teamMatch.getResults().size());
        assertEquals(0, teamMatch.getResultCount());
        assertEquals(0, teamMatch.get(CommonTeams.Unknown).getMembers().size());
        assertEquals(0, teamMatch.get(CommonTeams.Winners).getMembers().size());
        assertEquals(0, teamMatch.get(CommonTeams.Losers).getMembers().size());
        assertEquals(0, teamMatch.get(CommonTeams.Spectators).getMembers().size());
    }
    
    /**
     * Tests the match date filling
     * @throws McException 
     */
    @Test
    public void testMatchFinishDates() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        spmatch.start();
        sleep(50);
        
        spmatch.finish();
        assertNotNull(spmatch.getStarted());
        assertNotNull(spmatch.getFinished());
        assertFalse(spmatch.isAborted());
        final long units = spmatch.getStarted().until(spmatch.getFinished(), ChronoUnit.NANOS);
        assertTrue(0 < units);
    }
    
    /**
     * Tests the match date filling
     * @throws McException 
     */
    @Test
    public void testMatchAbortedDates() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        spmatch.start();
        sleep(50);
        
        spmatch.abort();
        assertNotNull(spmatch.getStarted());
        assertNotNull(spmatch.getFinished());
        assertTrue(spmatch.isAborted());
        final long units = spmatch.getStarted().until(spmatch.getFinished(), ChronoUnit.NANOS);
        assertTrue(0 < units);
    }
    
    /**
     * Tests the team creation
     * @throws McException 
     */
    @Test
    public void testTeamCreation() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(null, true);
        assertEquals(0, teamMatch.getTeams().size());
        
        teamMatch.getOrCreate(CommonTeams.Aqua);
        teamMatch.getOrCreate(CommonTeams.Black);
        assertEquals(2, teamMatch.getTeams().size());
        
        assertTrue(teamMatch.getTeams().contains(CommonTeams.Aqua));
        assertTrue(teamMatch.getTeams().contains(CommonTeams.Black));
    }
    
    /**
     * Tests the getTeam method
     * @throws McException 
     */
    @Test
    public void testGetTeam() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(null, true);
        teamMatch.getOrCreate(CommonTeams.Aqua);
        teamMatch.getOrCreate(CommonTeams.Black);
        
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        final ArenaPlayerInterface player3 = this.createPlayer();
        final ArenaPlayerInterface player4 = this.createPlayer();
        
        assertNull(teamMatch.getTeam(player1.getPlayerUUID()));
        assertNull(teamMatch.getTeam(player2.getPlayerUUID()));
        assertNull(teamMatch.getTeam(player3.getPlayerUUID()));
        assertNull(teamMatch.getTeam(player4.getPlayerUUID()));
        
        teamMatch.join(player1, CommonTeams.Aqua);
        assertEquals(CommonTeams.Aqua, teamMatch.getTeam(player1.getPlayerUUID()));
        
        teamMatch.join(player2, CommonTeams.Black);
        assertEquals(CommonTeams.Black, teamMatch.getTeam(player2.getPlayerUUID()));
        
        teamMatch.join(player3, CommonTeams.Black);
        assertEquals(CommonTeams.Black, teamMatch.getTeam(player3.getPlayerUUID()));
        
        teamMatch.join(player3, CommonTeams.Blue);
        assertEquals(CommonTeams.Blue, teamMatch.getTeam(player3.getPlayerUUID()));
        
        teamMatch.join(player4, CommonTeams.Aqua);
        assertEquals(CommonTeams.Aqua, teamMatch.getTeam(player4.getPlayerUUID()));
        
    }
    
    /**
     * Tests the getPreferredTeam method
     * @throws McException 
     */
    @Test
    public void testGetPreferredTeam() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(null, true);
        teamMatch.getOrCreate(CommonTeams.Aqua);
        teamMatch.getOrCreate(CommonTeams.Black);
        
        TeamIdType team = teamMatch.getPreferredTeam();
        assertTrue(team == CommonTeams.Aqua || team == CommonTeams.Black);
        
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        final ArenaPlayerInterface player3 = this.createPlayer();
        final ArenaPlayerInterface player4 = this.createPlayer();
        
        teamMatch.join(player1, CommonTeams.Aqua);
        team = teamMatch.getPreferredTeam();
        assertTrue(team == CommonTeams.Black);
        
        teamMatch.join(player2, CommonTeams.Black);
        team = teamMatch.getPreferredTeam();
        assertTrue(team == CommonTeams.Aqua || team == CommonTeams.Black);
        
        teamMatch.join(player3, CommonTeams.Black);
        team = teamMatch.getPreferredTeam();
        assertTrue(team == CommonTeams.Aqua);
        
        teamMatch.join(player3, CommonTeams.Blue);
        team = teamMatch.getPreferredTeam();
        assertTrue(team == CommonTeams.Aqua || team == CommonTeams.Blue);
        
        teamMatch.join(player4, CommonTeams.Aqua);
        team = teamMatch.getPreferredTeam();
        assertTrue(team == CommonTeams.Blue);
    }
    
    /**
     * Tests the getTeam method
     * @throws McException 
     */
    @Test
    public void testGetTeamMembers() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(null, true);
        teamMatch.getOrCreate(CommonTeams.Aqua);
        teamMatch.getOrCreate(CommonTeams.Black);
        assertEquals(0, teamMatch.getTeamMembers(CommonTeams.Aqua).size());
        assertEquals(0, teamMatch.getTeamMembers(CommonTeams.Black).size());
        assertEquals(0, teamMatch.getTeamMembers(CommonTeams.Blue).size());
        
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        final ArenaPlayerInterface player3 = this.createPlayer();
        
        teamMatch.join(player1, CommonTeams.Aqua);
        teamMatch.join(player2, CommonTeams.Black);
        teamMatch.join(player3, CommonTeams.Black);
        teamMatch.join(player3, CommonTeams.Blue);
        
        // blue was created on demand, we have 3 teams
        assertEquals(3, teamMatch.getTeams().size());
        assertTrue(teamMatch.getTeams().contains(CommonTeams.Aqua));
        assertTrue(teamMatch.getTeams().contains(CommonTeams.Black));
        assertTrue(teamMatch.getTeams().contains(CommonTeams.Blue));
        
        assertEquals(1, teamMatch.get(CommonTeams.Aqua).getMembers().size());
        assertTrue(teamMatch.get(CommonTeams.Aqua).getMembers().contains(player1.getPlayerUUID()));
        assertEquals(2, teamMatch.get(CommonTeams.Black).getMembers().size());
        assertTrue(teamMatch.get(CommonTeams.Black).getMembers().contains(player2.getPlayerUUID()));
        assertTrue(teamMatch.get(CommonTeams.Black).getMembers().contains(player3.getPlayerUUID()));
        assertEquals(1, teamMatch.get(CommonTeams.Blue).getMembers().size());
        assertTrue(teamMatch.get(CommonTeams.Blue).getMembers().contains(player3.getPlayerUUID()));
        
        assertEquals(1, teamMatch.getTeamMembers(CommonTeams.Aqua).size());
        assertTrue(teamMatch.getTeamMembers(CommonTeams.Aqua).contains(player1.getPlayerUUID()));
        assertEquals(2, teamMatch.getTeamMembers(CommonTeams.Black).size());
        assertTrue(teamMatch.getTeamMembers(CommonTeams.Black).contains(player2.getPlayerUUID()));
        assertTrue(teamMatch.getTeamMembers(CommonTeams.Black).contains(player3.getPlayerUUID()));
        assertEquals(1, teamMatch.getTeamMembers(CommonTeams.Blue).size());
        assertTrue(teamMatch.getTeamMembers(CommonTeams.Blue).contains(player3.getPlayerUUID()));
        
        assertEquals(CommonTeams.Aqua, teamMatch.get(player1.getPlayerUUID()).getTeam());
        assertEquals(CommonTeams.Black, teamMatch.get(player2.getPlayerUUID()).getTeam());
        assertEquals(CommonTeams.Blue, teamMatch.get(player3.getPlayerUUID()).getTeam());
    }
    
    /**
     * Tests the spectate method
     * @throws McException 
     */
    @Test
    public void testSpectate() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        spmatch.start();
        sleep(50);
        
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        spmatch.join(player1, CommonTeams.Unknown);
        
        sleep(50);
        spmatch.spectate(player1);
        spmatch.spectate(player2);
        
        assertFalse(spmatch.get(player1.getPlayerUUID()).isPlaying());
        assertFalse(spmatch.get(player2.getPlayerUUID()).isPlaying());
        assertTrue(spmatch.get(player1.getPlayerUUID()).isSpec());
        assertTrue(spmatch.get(player2.getPlayerUUID()).isSpec());
        assertEquals(CommonTeams.Unknown, spmatch.getTeam(player1.getPlayerUUID()));
        assertEquals(CommonTeams.Spectators, spmatch.getTeam(player2.getPlayerUUID()));
        
        assertEquals(1, spmatch.getTeamMembers(CommonTeams.Unknown).size());
        assertTrue(spmatch.getTeamMembers(CommonTeams.Unknown).contains(player1.getPlayerUUID()));

        assertTrue(50 <= spmatch.getPlayTime(player1.getPlayerUUID()));
        assertEquals(0, spmatch.getPlayTime(player2.getPlayerUUID()));

        assertEquals(2, spmatch.getTeamMembers(CommonTeams.Spectators).size());
        assertTrue(spmatch.getTeamMembers(CommonTeams.Spectators).contains(player1.getPlayerUUID()));
        assertTrue(spmatch.getTeamMembers(CommonTeams.Spectators).contains(player2.getPlayerUUID()));
    }
    
    /**
     * tests winners and losers
     * @throws McException 
     */
    @Test
    public void testWinningAndLosing() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        spmatch.start();
        final ArenaPlayerInterface winner1 = this.createPlayer();
        final ArenaPlayerInterface winner2a = this.createPlayer();
        final ArenaPlayerInterface winner2b = this.createPlayer();
        final ArenaPlayerInterface winner3 = this.createPlayer();
        final ArenaPlayerInterface loser4 = this.createPlayer();
        final ArenaPlayerInterface loser5a = this.createPlayer();
        final ArenaPlayerInterface loser5b = this.createPlayer();
        final ArenaPlayerInterface loser6 = this.createPlayer();
        final ArenaPlayerInterface loser7a = this.createPlayer();
        final ArenaPlayerInterface loser7b = this.createPlayer();
        spmatch.join(winner1, CommonTeams.Unknown);
        spmatch.join(winner2a, CommonTeams.Unknown);
        spmatch.join(winner2b, CommonTeams.Unknown);
        spmatch.join(winner3, CommonTeams.Unknown);
        spmatch.join(loser4, CommonTeams.Unknown);
        spmatch.join(loser5a, CommonTeams.Unknown);
        spmatch.join(loser5b, CommonTeams.Unknown);
        spmatch.join(loser6, CommonTeams.Unknown);
        spmatch.join(loser7a, CommonTeams.Unknown);
        spmatch.join(loser7b, CommonTeams.Unknown);
        
        // let the players win or lose
        spmatch.setWinner(winner1.getPlayerUUID());
        spmatch.setLoser(loser7a.getPlayerUUID(), loser7b.getPlayerUUID());
        spmatch.setWinner(winner2a.getPlayerUUID(), winner2b.getPlayerUUID());
        spmatch.setLoser(loser6.getPlayerUUID());
        spmatch.setLoser(loser5a.getPlayerUUID(), loser5b.getPlayerUUID());
        spmatch.setWinner(winner3.getPlayerUUID());
        spmatch.setLoser(loser4.getPlayerUUID());
        
        // dummy method invocation
        spmatch.setWinner(new UUID[0]);
        spmatch.setWinner((UUID[]) null);
        spmatch.setLoser(new UUID[0]);
        spmatch.setLoser((UUID[]) null);
        // TODO following two lines should work (marking non playing players as winner/losers should be silently ignored)...
//        spmatch.setWinner(UUID.randomUUID());
//        spmatch.setLoser(UUID.randomUUID());
        // TODO check with players only being a spectator
        
        // check results
        assertEquals(7, spmatch.getResultCount());
        assertEquals(7, spmatch.getResults().size());
        
        ArenaMatchInterface.MatchResult result = spmatch.getResult(1);
        assertTrue(result.isWin());
        assertEquals(1, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(winner1.getPlayerUUID()));
        
        result = spmatch.getResult(2);
        assertTrue(result.isWin());
        assertEquals(2, result.getPlace());
        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(winner2a.getPlayerUUID()));
        assertTrue(result.getPlayers().contains(winner2b.getPlayerUUID()));
        
        result = spmatch.getResult(3);
        assertTrue(result.isWin());
        assertEquals(3, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(winner3.getPlayerUUID()));
        
        result = spmatch.getResult(4);
        assertFalse(result.isWin());
        assertEquals(4, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser4.getPlayerUUID()));
        
        result = spmatch.getResult(5);
        assertFalse(result.isWin());
        assertEquals(5, result.getPlace());
        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser5a.getPlayerUUID()));
        assertTrue(result.getPlayers().contains(loser5b.getPlayerUUID()));
        
        result = spmatch.getResult(6);
        assertFalse(result.isWin());
        assertEquals(6, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser6.getPlayerUUID()));
        
        result = spmatch.getResult(7);
        assertFalse(result.isWin());
        assertEquals(7, result.getPlace());
        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser7a.getPlayerUUID()));
        assertTrue(result.getPlayers().contains(loser7b.getPlayerUUID()));
        
        assertNull(spmatch.getResult(0));
        assertNull(spmatch.getResult(8));
        
        assertEquals(4, spmatch.getWinnerCount());
        assertEquals(6, spmatch.getLoserCount());
    }
    
    /**
     * tests winning and losing for teams
     * @throws McException 
     */
    @Test
    public void testTeamWinningAndLosing() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(null, true);
        teamMatch.start();
        final ArenaPlayerInterface winner1 = this.createPlayer();
        final ArenaPlayerInterface winner2a = this.createPlayer();
        final ArenaPlayerInterface winner2b = this.createPlayer();
        final ArenaPlayerInterface winner3 = this.createPlayer();
        final ArenaPlayerInterface loser4 = this.createPlayer();
        final ArenaPlayerInterface loser5a = this.createPlayer();
        final ArenaPlayerInterface loser5b = this.createPlayer();
        final ArenaPlayerInterface loser6 = this.createPlayer();
        final ArenaPlayerInterface loser7a = this.createPlayer();
        final ArenaPlayerInterface loser7b = this.createPlayer();
        teamMatch.join(winner1, CommonTeams.Aqua);
        teamMatch.join(winner2a, CommonTeams.Black);
        teamMatch.join(winner2b, CommonTeams.Black);
        teamMatch.join(winner3, CommonTeams.Blue);
        teamMatch.join(loser4, CommonTeams.Fuchsia);
        teamMatch.join(loser5a, CommonTeams.Gray);
        teamMatch.join(loser5b, CommonTeams.Gray);
        teamMatch.join(loser6, CommonTeams.Green);
        teamMatch.join(loser7a, CommonTeams.Lime);
        teamMatch.join(loser7b, CommonTeams.Lime);
        
        // let the players win or lose
        teamMatch.setWinner(CommonTeams.Aqua);
        teamMatch.setLoser(CommonTeams.Lime);
        teamMatch.setWinner(CommonTeams.Black);
        teamMatch.setLoser(CommonTeams.Green);
        teamMatch.setLoser(CommonTeams.Gray);
        teamMatch.setWinner(CommonTeams.Blue);
        teamMatch.setLoser(CommonTeams.Fuchsia);
        
        // dummy method invocation
        teamMatch.setWinner(new TeamIdType[0]);
        teamMatch.setWinner((TeamIdType[]) null);
        teamMatch.setLoser(new TeamIdType[0]);
        teamMatch.setLoser((TeamIdType[]) null);
        teamMatch.setWinner(CommonTeams.White);
        teamMatch.setLoser(CommonTeams.White);
        
        // check results
        assertEquals(7, teamMatch.getResultCount());
        assertEquals(7, teamMatch.getResults().size());
        
        ArenaMatchInterface.MatchResult result = teamMatch.getResult(1);
        assertTrue(result.isWin());
        assertEquals(1, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(winner1.getPlayerUUID()));
        
        result = teamMatch.getResult(2);
        assertTrue(result.isWin());
        assertEquals(2, result.getPlace());
        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(winner2a.getPlayerUUID()));
        assertTrue(result.getPlayers().contains(winner2b.getPlayerUUID()));
        
        result = teamMatch.getResult(3);
        assertTrue(result.isWin());
        assertEquals(3, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(winner3.getPlayerUUID()));
        
        result = teamMatch.getResult(4);
        assertFalse(result.isWin());
        assertEquals(4, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser4.getPlayerUUID()));
        
        result = teamMatch.getResult(5);
        assertFalse(result.isWin());
        assertEquals(5, result.getPlace());
        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser5a.getPlayerUUID()));
        assertTrue(result.getPlayers().contains(loser5b.getPlayerUUID()));
        
        result = teamMatch.getResult(6);
        assertFalse(result.isWin());
        assertEquals(6, result.getPlace());
        assertEquals(1, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser6.getPlayerUUID()));
        
        result = teamMatch.getResult(7);
        assertFalse(result.isWin());
        assertEquals(7, result.getPlace());
        assertEquals(2, result.getPlayers().size());
        assertTrue(result.getPlayers().contains(loser7a.getPlayerUUID()));
        assertTrue(result.getPlayers().contains(loser7b.getPlayerUUID()));
        
        assertNull(teamMatch.getResult(0));
        assertNull(teamMatch.getResult(8));
        
        assertEquals(4, teamMatch.getWinnerCount());
        assertEquals(6, teamMatch.getLoserCount());
    }
    
    /**
     * Tests the statistics
     * @throws McException 
     */
    @Test
    public void testTeamStatistics() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, true);
        spmatch.start();
        spmatch.getOrCreate(CommonTeams.Aqua);
        spmatch.getOrCreate(CommonTeams.Black);
        
        assertEquals(0, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2));
        
        assertEquals(2, spmatch.addStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 2));
        assertEquals(3, spmatch.addStatistic(CommonTeams.Aqua, DummyStatistics.Stat2, 3));
        assertEquals(4, spmatch.addStatistic(CommonTeams.Black, DummyStatistics.Stat1, 4));
        assertEquals(5, spmatch.addStatistic(CommonTeams.Black, DummyStatistics.Stat2, 5));
        assertEquals(0, spmatch.addStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1, 6));
        assertEquals(0, spmatch.addStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2, 7));
        assertEquals(2, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat1));
        assertEquals(3, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat2));
        assertEquals(4, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat1));
        assertEquals(5, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2));
        
        assertEquals(4, spmatch.addStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 2));
        assertEquals(6, spmatch.addStatistic(CommonTeams.Aqua, DummyStatistics.Stat2, 3));
        assertEquals(8, spmatch.addStatistic(CommonTeams.Black, DummyStatistics.Stat1, 4));
        assertEquals(10, spmatch.addStatistic(CommonTeams.Black, DummyStatistics.Stat2, 5));
        assertEquals(0, spmatch.addStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1, 6));
        assertEquals(0, spmatch.addStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2, 7));
        assertEquals(4, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat1));
        assertEquals(6, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat2));
        assertEquals(8, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat1));
        assertEquals(10, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2));
        
        assertEquals(3, spmatch.decStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1));
        assertEquals(4, spmatch.decStatistic(CommonTeams.Aqua, DummyStatistics.Stat2, 2));
        assertEquals(5, spmatch.decStatistic(CommonTeams.Black, DummyStatistics.Stat1, 3));
        assertEquals(6, spmatch.decStatistic(CommonTeams.Black, DummyStatistics.Stat2, 4));
        assertEquals(0, spmatch.decStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1, 5));
        assertEquals(0, spmatch.decStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2, 6));
        assertEquals(3, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat1));
        assertEquals(4, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat2));
        assertEquals(5, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat1));
        assertEquals(6, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2));
        
        spmatch.setStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
        spmatch.setStatistic(CommonTeams.Aqua, DummyStatistics.Stat2, 2);
        spmatch.setStatistic(CommonTeams.Black, DummyStatistics.Stat1, 3);
        spmatch.setStatistic(CommonTeams.Black, DummyStatistics.Stat2, 4);
        spmatch.setStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1, 5);
        spmatch.setStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2, 6);
        assertEquals(1, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat1));
        assertEquals(2, spmatch.getStatistic(CommonTeams.Aqua, DummyStatistics.Stat2));
        assertEquals(3, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat1));
        assertEquals(4, spmatch.getStatistic(CommonTeams.Black, DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(CommonTeams.Fuchsia, DummyStatistics.Stat2));
    }
    
    /**
     * Tests the statistics
     * @throws McException 
     */
    @Test
    public void testStatistics() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        spmatch.start();
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        final ArenaPlayerInterface player3 = this.createPlayer();
        spmatch.join(player1, CommonTeams.Unknown);
        spmatch.join(player2, CommonTeams.Unknown);
        
        assertEquals(0, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2));
        
        assertEquals(2, spmatch.addStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1, 2));
        assertEquals(3, spmatch.addStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2, 3));
        assertEquals(4, spmatch.addStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1, 4));
        assertEquals(5, spmatch.addStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2, 5));
        assertEquals(0, spmatch.addStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1, 6));
        assertEquals(0, spmatch.addStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2, 7));
        assertEquals(2, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(3, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(4, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(5, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2));
        
        assertEquals(4, spmatch.addStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1, 2));
        assertEquals(6, spmatch.addStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2, 3));
        assertEquals(8, spmatch.addStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1, 4));
        assertEquals(10, spmatch.addStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2, 5));
        assertEquals(0, spmatch.addStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1, 6));
        assertEquals(0, spmatch.addStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2, 7));
        assertEquals(4, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(6, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(8, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(10, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2));
        
        assertEquals(3, spmatch.decStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1, 1));
        assertEquals(4, spmatch.decStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2, 2));
        assertEquals(5, spmatch.decStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1, 3));
        assertEquals(6, spmatch.decStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2, 4));
        assertEquals(0, spmatch.decStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1, 5));
        assertEquals(0, spmatch.decStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2, 6));
        assertEquals(3, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(4, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(5, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(6, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2));
        
        spmatch.setStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1, 1);
        spmatch.setStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2, 2);
        spmatch.setStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1, 3);
        spmatch.setStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2, 4);
        spmatch.setStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1, 5);
        spmatch.setStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2, 6);
        assertEquals(1, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(2, spmatch.getStatistic(player1.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(3, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(4, spmatch.getStatistic(player2.getPlayerUUID(), DummyStatistics.Stat2));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat1));
        assertEquals(0, spmatch.getStatistic(player3.getPlayerUUID(), DummyStatistics.Stat2));
    }
    
    /**
     * Tests the get play time
     * @throws McException 
     */
    @Test
    public void testGetPlayTime() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        final ArenaPlayerInterface player1 = this.createPlayer(); // joined before start
        final ArenaPlayerInterface player2 = this.createPlayer(); // joined after start
        final ArenaPlayerInterface player3 = this.createPlayer(); // spectator
        final ArenaPlayerInterface player4 = this.createPlayer(); // unknown player
        final ArenaPlayerInterface player5 = this.createPlayer(); // joined and left before start
        spmatch.join(player1, CommonTeams.Unknown);
        spmatch.join(player5, CommonTeams.Unknown);
        sleep(20);
        spmatch.leave(player5);
        sleep(30);
        
        assertEquals(0, spmatch.getPlayTime(player1.getPlayerUUID()));
        assertEquals(0, spmatch.getPlayTime(player2.getPlayerUUID()));
        assertEquals(0, spmatch.getPlayTime(player3.getPlayerUUID()));
        assertEquals(0, spmatch.getPlayTime(player4.getPlayerUUID()));
        assertEquals(0, spmatch.getPlayTime(player5.getPlayerUUID()));
        
        spmatch.start();
        sleep(10);
        spmatch.join(player2, CommonTeams.Unknown);
        
        // match is in progress
        sleep(10);
        
        // player2 joined 10 milliseconds after player1
        // on a slow ide the test may measure some more milliseconds...
        // the asser ensures that the 50 millis before the match starts are not respected by getPlayTime.
        assertTrue(15 >= spmatch.getPlayTime(player2.getPlayerUUID()) - spmatch.getPlayTime(player2.getPlayerUUID()));
        
        sleep(50);
        spmatch.spectate(player3);
        sleep(50);
        spmatch.setWinner(player2.getPlayerUUID(), player1.getPlayerUUID());
        
        // player 3 was only spectating (no play time)
        assertEquals(0, spmatch.getPlayTime(player3.getPlayerUUID()));
        
        // match is finished/ players have left
        
        // player2 joined 10 milliseconds after player1
        // on a slow ide the test may measure some more milliseconds...
        // the asser ensures that the 50 millis before the match starts are not respected by getPlayTime.
        assertTrue(15 >= spmatch.getPlayTime(player2.getPlayerUUID()) - spmatch.getPlayTime(player2.getPlayerUUID()));
        
        // unknown player
        assertEquals(0, spmatch.getPlayTime(player4.getPlayerUUID()));
        
        // player left before start
        assertEquals(0, spmatch.getPlayTime(player5.getPlayerUUID()));
    }
    
    /**
     * Tests the killer tracking
     * @throws McException 
     */
    @Test
    public void testKillerTracking() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(null, false);
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        final ArenaPlayerInterface player3 = this.createPlayer();
        final ArenaPlayerInterface player4 = this.createPlayer();
        final ArenaPlayerInterface player5 = this.createPlayer();
        final ArenaPlayerInterface player6 = this.createPlayer();
        spmatch.start();
        spmatch.join(player1, CommonTeams.Unknown);
        spmatch.join(player2, CommonTeams.Unknown);
        spmatch.join(player3, CommonTeams.Unknown);
        spmatch.join(player4, CommonTeams.Unknown);
        spmatch.join(player5, CommonTeams.Unknown);
        
        spmatch.trackDamageForKill(player1.getPlayerUUID(), player2.getPlayerUUID());
        sleep(10);
        spmatch.trackDamageForKill(player2.getPlayerUUID(), player3.getPlayerUUID());
        sleep(10);
        spmatch.trackDamageForKill(player1.getPlayerUUID(), player3.getPlayerUUID());
        sleep(10);
        spmatch.trackDamageForKill(player2.getPlayerUUID(), player4.getPlayerUUID());
        sleep(10);
        spmatch.trackDamageForKill(player2.getPlayerUUID(), player4.getPlayerUUID());
        sleep(10);
        spmatch.trackDamageForKill(player2.getPlayerUUID(), player4.getPlayerUUID());
        spmatch.trackDamageForKill(player5.getPlayerUUID(), player4.getPlayerUUID());
        sleep(10);
        spmatch.trackDamageForKill(player6.getPlayerUUID(), player4.getPlayerUUID());
        
        final LocalDateTime now = LocalDateTime.now();
        
        assertEquals(player3.getPlayerUUID(), spmatch.getKillerTracking(player1.getPlayerUUID()).getLastDamager());
        assertEquals(player4.getPlayerUUID(), spmatch.getKillerTracking(player2.getPlayerUUID()).getLastDamager());
        assertNull(spmatch.getKillerTracking(player3.getPlayerUUID()).getLastDamager());
        assertNull(spmatch.getKillerTracking(player4.getPlayerUUID()).getLastDamager());
        assertEquals(player4.getPlayerUUID(), spmatch.getKillerTracking(player5.getPlayerUUID()).getLastDamager());
        
        assertNull(spmatch.getKillerTracking(player6.getPlayerUUID()));
        
        assertTrue(15 >= spmatch.getKillerTracking(player5.getPlayerUUID()).getDamageTimestamp().until(now, ChronoUnit.MILLIS));
        assertTrue(15 >= spmatch.getKillerTracking(player2.getPlayerUUID()).getDamageTimestamp().until(now, ChronoUnit.MILLIS));
        assertTrue(45 >= spmatch.getKillerTracking(player1.getPlayerUUID()).getDamageTimestamp().until(now, ChronoUnit.MILLIS));
        
        sleep(10);
        
        spmatch.resetKillerTracking(player1.getPlayerUUID());
        spmatch.resetKillerTracking(player6.getPlayerUUID());
        
        assertNull(spmatch.getKillerTracking(player6.getPlayerUUID()));
    }
    
    /**
     * Failes with exception on duplicate startup.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failDoubleStart() throws McException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(this.createDummyArena(), false);
        try
        {
            spmatch.start();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        
        spmatch.start();
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failStatisticsAfterFinish1() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        try
        {
            teamMatch.getOrCreate(CommonTeams.Aqua);
            teamMatch.start();
            teamMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        teamMatch.setStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failStatisticsAfterFinish2() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        try
        {
            teamMatch.getOrCreate(CommonTeams.Aqua);
            teamMatch.start();
            teamMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        teamMatch.addStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failStatisticsAfterFinish3() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        try
        {
            teamMatch.getOrCreate(CommonTeams.Aqua);
            teamMatch.start();
            teamMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        teamMatch.decStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failStatisticsAfterFinish4() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.setStatistic(player.getPlayerUUID(), DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failStatisticsAfterFinish5() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.addStatistic(player.getPlayerUUID(), DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failStatisticsAfterFinish6() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.decStatistic(player.getPlayerUUID(), DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failJoinAfterFinish1() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        try
        {
            spMatch.join(player1);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.join(player2);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failJoinAfterFinish2() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.join(player, CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSwitchAfterFinish() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.getOrCreate(CommonTeams.Aqua);
            spMatch.getOrCreate(CommonTeams.Black);
            spMatch.join(player, CommonTeams.Aqua);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.switchTeam(player, CommonTeams.Black);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSpectateAfterFinish() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.spectate(player);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failLeaveAfterFinish1() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.leave(player);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failLeaveAfterFinish2() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player, CommonTeams.Aqua);
            spMatch.join(player, CommonTeams.Black);
            spMatch.start();
            spMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.leave(player, CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamAfterFinish() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        try
        {
            teamMatch.getOrCreate(CommonTeams.Aqua);
            teamMatch.start();
            teamMatch.finish();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        teamMatch.getOrCreate(CommonTeams.Black);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failAbortBeforeStart() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        spMatch.abort();
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failFinishBeforeStart() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        spMatch.finish();
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failResetKillerBeforeStart() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.resetKillerTracking(player.getPlayerUUID());
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSetKillerBeforeStart() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player1 = this.createPlayer();
        final ArenaPlayerInterface player2 = this.createPlayer();
        try
        {
            spMatch.join(player1);
            spMatch.join(player2);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.trackDamageForKill(player1.getPlayerUUID(), player2.getPlayerUUID());
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSetLoserBeforeStart1() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.setLoser(player.getPlayerUUID());
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSetLoserBeforeStart2() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            teamMatch.join(player, CommonTeams.Aqua);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        teamMatch.setLoser(CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSetWinnerBeforeStart1() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.setWinner(player.getPlayerUUID());
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failSetWinnerBeforeStart2() throws McException
    {
        final ArenaMatchImpl teamMatch = new ArenaMatchImpl(this.createDummyArena(), true);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            teamMatch.join(player, CommonTeams.Aqua);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        teamMatch.setWinner(CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP1() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        try
        {
            spMatch.start();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.addStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP2() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        try
        {
            spMatch.start();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.decStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP3() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        try
        {
            spMatch.start();
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.setStatistic(CommonTeams.Aqua, DummyStatistics.Stat1, 1);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP4() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        spMatch.getOrCreate(CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP5() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        spMatch.join(player, CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP6() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.join(player, CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP7() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.leave(player, CommonTeams.Aqua);
    }
    
    /**
     * Failes with exception
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void failTeamMethodOnSP8() throws McException
    {
        final ArenaMatchImpl spMatch = new ArenaMatchImpl(this.createDummyArena(), false);
        final ArenaPlayerInterface player = this.createPlayer();
        try
        {
            spMatch.join(player);
        }
        catch (McException e)
        {
            e.printStackTrace();
            fail("Unexpected exception"); //$NON-NLS-1$
        }
        spMatch.switchTeam(player, CommonTeams.Aqua);
    }
    
    /**
     * Mock mglib interface
     * @throws ClassNotFoundException 
     */
    @Before
    public void mockMglib() throws ClassNotFoundException
    {
        MinigamesLibInterface mglib = mock(MinigamesLibInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mgapi.api.MglibCache"), "SERVICES", mglib); //$NON-NLS-1$ //$NON-NLS-2$
        when(mglib.isSpecialTeam(CommonTeams.Unknown)).thenReturn(true);
        when(mglib.isSpecialTeam(CommonTeams.Winners)).thenReturn(true);
        when(mglib.isSpecialTeam(CommonTeams.Losers)).thenReturn(true);
        when(mglib.isSpecialTeam(CommonTeams.Spectators)).thenReturn(true);
    }
    
    /**
     * Mock bukkit server
     */
    @Before
    public void mockServer()
    {
        final Server server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", server); //$NON-NLS-1$
        this.pluginManager = mock(PluginManager.class);
        when(server.getPluginManager()).thenReturn(this.pluginManager);
    }
    
    /**
     * Creates an arena mock with dummy name
     * @return dummy arena
     */
    private ArenaInterface createDummyArena()
    {
        return this.createArena("dummy"); //$NON-NLS-1$
    }
    
    /**
     * Creates an arena mock
     * @param name
     * @return arena mock
     */
    private ArenaInterface createArena(String name)
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final LocalizedConfigString namestring = new LocalizedConfigString();
        namestring.setUserMessage(Locale.ENGLISH, name);
        when(arena.getDisplayName()).thenReturn(namestring);
        return arena;
    }
    
    /**
     * Creates a random arena player
     * @return arena player
     */
    private ArenaPlayerInterface createPlayer()
    {
        final ArenaPlayerInterface result = mock(ArenaPlayerInterface.class);
        when(result.getPlayerUUID()).thenReturn(UUID.randomUUID());
        return result;
    }

    /**
     * Sleeps for given millis and ignores any error
     * @param millis 
     */
    private void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (@SuppressWarnings("unused") InterruptedException e)
        {
            // silently ignore
        }
    }
    
    /**
     * Some statistics helper
     */
    private enum DummyStatistics implements MatchStatisticId
    {
        /** dummy statistic */
        Stat1,
        /** dummy statistic */
        Stat2
    }
    
}
