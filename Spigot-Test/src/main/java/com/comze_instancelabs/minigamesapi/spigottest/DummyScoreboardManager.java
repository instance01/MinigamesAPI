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

package com.comze_instancelabs.minigamesapi.spigottest;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * A dummy scoreboard manager.
 * 
 * @author mepeisen
 */
class DummyScoreboardManager implements ScoreboardManager
{
    
    /**
     * the main score board.
     */
    private DummyScoreboard main = new DummyScoreboard();

    /**
     * tears down score boards.
     */
    public void teardown()
    {
        this.main = new DummyScoreboard();
    }

    @Override
    public Scoreboard getMainScoreboard()
    {
        return this.main;
    }

    @Override
    public Scoreboard getNewScoreboard()
    {
        return new DummyScoreboard();
    }
    
}
