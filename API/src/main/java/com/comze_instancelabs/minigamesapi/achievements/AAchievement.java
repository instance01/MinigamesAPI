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
package com.comze_instancelabs.minigamesapi.achievements;

public class AAchievement
{
    
    String  name;
    boolean done;
    String  playername;
    
    public AAchievement(final String name, final String playername, final boolean done)
    {
        this.name = name;
        this.playername = playername;
        this.done = done;
    }
    
    public boolean isDone()
    {
        return this.done;
    }
    
    public void setDone(final boolean t)
    {
        this.done = t;
    }
    
    public String getAchievementNameRaw()
    {
        return this.name;
    }
}
