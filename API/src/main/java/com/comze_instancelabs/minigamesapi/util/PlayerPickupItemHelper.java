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

package com.comze_instancelabs.minigamesapi.util;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import com.comze_instancelabs.minigamesapi.MinecraftVersionsType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

/**
 * @author mepeisen
 *
 */
public class PlayerPickupItemHelper
{
    
    final Consumer<CustomPickupEvent> handler;
    
    public PlayerPickupItemHelper(Plugin plugin, Consumer<CustomPickupEvent> handler)
    {
        this.handler = handler;
        if (MinigamesAPI.SERVER_VERSION.isAtLeast(MinecraftVersionsType.V1_12))
        {
            Bukkit.getPluginManager().registerEvents(new EntityPickup(), plugin);
        }
        else
        {
            Bukkit.getPluginManager().registerEvents(new PlayerPickup(), plugin);
        }
    }

    private final class PlayerPickup implements Listener
    {
        @EventHandler
        public void onPlayerPickupItem(PlayerPickupItemEvent event) {
            handler.accept(new CustomPickupEvent() {

                @Override
                public Player getPlayer()
                {
                    return event.getPlayer();
                }

                @Override
                public Item getItem()
                {
                    return event.getItem();
                }

                @Override
                public int getRemaining()
                {
                    return event.getRemaining();
                }

                @Override
                public boolean isCancelled()
                {
                    return event.isCancelled();
                }

                @Override
                public void setCancelled(boolean cancel)
                {
                    event.setCancelled(cancel);
                }});
        }
    }
    
    private final class EntityPickup implements Listener
    {
        @EventHandler
        public void onPlayerPickupItem(EntityPickupItemEvent event) {
            if (event.getEntity() instanceof Player)
            {
                handler.accept(new CustomPickupEvent() {

                    @Override
                    public Player getPlayer()
                    {
                        return (Player) event.getEntity();
                    }

                    @Override
                    public Item getItem()
                    {
                        return event.getItem();
                    }

                    @Override
                    public int getRemaining()
                    {
                        return event.getRemaining();
                    }

                    @Override
                    public boolean isCancelled()
                    {
                        return event.isCancelled();
                    }

                    @Override
                    public void setCancelled(boolean cancel)
                    {
                        event.setCancelled(cancel);
                    }});
            }
        }
    }
    
    public interface CustomPickupEvent
    {
        Player getPlayer();
        Item getItem();
        int getRemaining();
        boolean isCancelled();
        void setCancelled(boolean cancel);
    }
    
}
