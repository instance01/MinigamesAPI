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
package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public enum ArenaState
{
    
    JOIN, STARTING, INGAME, RESTARTING;
    
    public static ArrayList<String> getAllStateNames()
    {
        return new ArrayList<>(Arrays.asList("JOIN", "STARTING", "INGAME", "RESTARTING"));
    }
    
    public static HashMap<String, String> getAllStateNameColors()
    {
        final HashMap<String, String> ret = new HashMap<String, String>() {
            /**
             *
             */
            private static final long serialVersionUID = 3343763360644032212L;
            
            {
                this.put("JOIN", "&a");
                this.put("STARTING", "&a");
                this.put("INGAME", "&4");
                this.put("RESTARTING", "&e");
            }
        };
        return ret;
    }
}
