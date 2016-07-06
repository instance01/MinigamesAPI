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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.testutil.TestUtil;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * The tester for the validator class.
 * 
 * @author mepeisen
 * 
 * @see Validator
 */
public class ValidatorTest extends TestUtil
{
    
    /** the default arena name. */
    private static final String ARENA = "junit-arena"; //$NON-NLS-1$
    
    /** the default arena name. */
    private static final String ARENA_EXISTING = "existing-arena"; //$NON-NLS-1$
    
    /** the default arena name. */
    private static final String ARENA_VALID = "valid-arena"; //$NON-NLS-1$
    
    /** the junit minigame. */
    private static final String MINIGAME = "$JUNIT-VALIDATOR-TEST"; //$NON-NLS-1$
    
    /**
     * tests the validator isPlayerOnline
     */
    @Test
    public void testIsPlayerOnline()
    {
        this.mockOnlinePlayer("PLAYER_ONLINE", UUID.randomUUID()); //$NON-NLS-1$
        assertTrue(Validator.isPlayerOnline("PLAYER_ONLINE")); //$NON-NLS-1$
        assertFalse(Validator.isPlayerOnline("PLAYER_OFFLINE")); //$NON-NLS-1$
    }
    
    /**
     * tests the validator isPlayerValid
     */
    @Test
    public void testIsPlayerValid()
    {
        this.mockOnlinePlayer("PLAYER_ONLINE", UUID.randomUUID()); //$NON-NLS-1$
        this.mockOnlinePlayer("PLAYER_ARENA", UUID.randomUUID()); //$NON-NLS-1$
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "isPlayerValid"); //$NON-NLS-1$
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        final Arena arenaValid = new Arena(minigame.javaPlugin, ARENA_VALID);
        minigame.pluginInstance.global_players.put("PLAYER_ARENA", arenaValid); //$NON-NLS-1$
        
        assertFalse(Validator.isPlayerValid(minigame.javaPlugin, "PLAYER_OFFLINE", arena)); //$NON-NLS-1$
        assertFalse(Validator.isPlayerValid(minigame.javaPlugin, "PLAYER_OFFLINE", arenaValid)); //$NON-NLS-1$
        assertFalse(Validator.isPlayerValid(minigame.javaPlugin, "PLAYER_ONLINE", arena)); //$NON-NLS-1$
        assertFalse(Validator.isPlayerValid(minigame.javaPlugin, "PLAYER_ONLINE", arenaValid)); //$NON-NLS-1$
        assertFalse(Validator.isPlayerValid(minigame.javaPlugin, "PLAYER_ARENA", arena)); //$NON-NLS-1$
        assertTrue(Validator.isPlayerValid(minigame.javaPlugin, "PLAYER_ARENA", arenaValid)); //$NON-NLS-1$
    }
    
    /**
     * Tests the validator isArenaValid
     */
    @Test
    public void testArenaIsValid()
    {
        final Minigame minigame = this.minigameTest.setupMinigame(MINIGAME + "isArenaValid", (mg) -> { //$NON-NLS-1$
            mg.addArenaComponentToConfig(ARENA_EXISTING, "lobby", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            
            mg.addArenaComponentToConfig(ARENA_VALID, "lobby", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
            mg.addArenaComponentToConfig(ARENA_VALID, "spawns.spawn0", "world", 1, 1, 1, 80, 80); //$NON-NLS-1$ //$NON-NLS-2$
        });
        
        // check if unknown arena is not valid
        final Arena arena = new Arena(minigame.javaPlugin, ARENA);
        assertFalse(Validator.isArenaValid(minigame.javaPlugin, arena));
        
        // check if known arena is not valid
        final Arena arenaExisting = new Arena(minigame.javaPlugin, ARENA_EXISTING);
        assertFalse(Validator.isArenaValid(minigame.javaPlugin, arenaExisting));
        
        // check if unknown arena is not valid
        final Arena arenaValid = new Arena(minigame.javaPlugin, ARENA_VALID);
        assertTrue(Validator.isArenaValid(minigame.javaPlugin, arenaValid));
    }
    
}
