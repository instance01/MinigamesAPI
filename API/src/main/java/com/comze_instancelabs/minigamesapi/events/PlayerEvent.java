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
package com.comze_instancelabs.minigamesapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;

public abstract class PlayerEvent extends Event
{
    private final Arena              arena;
    private final JavaPlugin         plugin;
    private final Player             player;

    public PlayerEvent(final Player p, final JavaPlugin plugin, final Arena a)
    {
        this.arena = a;
        this.plugin = plugin;
        this.player = p;
    }

    public Arena getArena()
    {
        return this.arena;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public JavaPlugin getPlugin()
    {
        return this.plugin;
    }
}
