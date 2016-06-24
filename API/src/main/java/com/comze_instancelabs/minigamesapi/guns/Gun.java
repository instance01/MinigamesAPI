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
package com.comze_instancelabs.minigamesapi.guns;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Gun
{
    
    public double                      speed                = 1D;                            // the higher the faster
    public int                         shoot_amount         = 1;
    public int                         max_durability       = 50;
    public int                         durability           = 50;
    public Class<? extends Projectile> bullet               = Egg.class;
    public JavaPlugin                  plugin;
    public double                      knockback_multiplier = 1.1D;
    public String                      name                 = "Gun";
    
    boolean                            canshoot             = true;
    public HashMap<String, Boolean>    canshoot_            = new HashMap<>();
    
    ArrayList<ItemStack>               items;
    ArrayList<ItemStack>               icon;
    
    public Gun(final JavaPlugin plugin, final String name, final double speed, final int shoot_amount, final int durability, final double knockback_multiplier,
            final Class<? extends Projectile> bullet, final ArrayList<ItemStack> items, final ArrayList<ItemStack> icon)
    {
        this.plugin = plugin;
        this.name = name;
        this.speed = speed;
        this.shoot_amount = shoot_amount;
        this.durability = durability;
        this.max_durability = durability;
        this.bullet = bullet;
        this.knockback_multiplier = knockback_multiplier;
        this.items = items;
        this.icon = icon;
        if (name.equalsIgnoreCase("grenade"))
        {
            this.bullet = Snowball.class;
        }
    }
    
    public Gun(final JavaPlugin plugin, final ArrayList<ItemStack> items, final ArrayList<ItemStack> icon)
    {
        this(plugin, "Gun", 1D, 1, 50, 1.1D, Egg.class, items, icon);
    }
    
    public void shoot(final Player p)
    {
        if (this.canshoot)
        {
            for (int i = 0; i < this.shoot_amount; i++)
            {
                p.launchProjectile(this.bullet);
                this.durability -= 1;
            }
            this.canshoot = false;
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> Gun.this.canshoot = true, (long) (20D / this.speed));
        }
    }
    
    public void shoot(final Player p, final int shoot_amount, final int durability, final int speed)
    {
        if (!this.canshoot_.containsKey(p.getName()))
        {
            this.canshoot_.put(p.getName(), true);
        }
        if (this.canshoot_.get(p.getName()))
        {
            for (int i = 0; i < shoot_amount + 1; i++)
            {
                p.launchProjectile(this.bullet);
                this.durability -= (int) (10D / durability);
            }
            this.canshoot_.put(p.getName(), false);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> Gun.this.canshoot_.put(p.getName(), true), (long) (60D / speed));
        }
    }
    
    public void onHit(final Entity ent, final int knockback_multiplier)
    {
        if (this.name.equalsIgnoreCase("freeze"))
        {
            final Player p = (Player) ent;
            p.setWalkSpeed(0.0F);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> p.setWalkSpeed(0.2F), 20L + 20L * knockback_multiplier);
        }
        else
        {
            ent.setVelocity(ent.getLocation().getDirection().multiply((-1D) * knockback_multiplier));
        }
    }
    
    public void reloadGun()
    {
        this.durability = this.max_durability;
        this.canshoot = true;
    }
    
}
