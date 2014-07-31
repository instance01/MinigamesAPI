package com.comze_instancelabs.minigamesapi.guns;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Gun {

	public double speed = 1D; // the higher the faster
	public int shoot_amount = 1;
	public int max_durability = 50;
	public int durability = 50;
	public Class<? extends Projectile> bullet = Egg.class;
	public JavaPlugin plugin;
	public double knockback_multiplier = 1.1D;

	boolean canshoot = true;

	public Gun(JavaPlugin plugin, double speed, int shoot_amount, int durability, double knockback_multiplier, Class<? extends Projectile> bullet, ArrayList<ItemStack> items, ArrayList<ItemStack> icon) {
		this.plugin = plugin;
		this.speed = speed;
		this.shoot_amount = shoot_amount;
		this.durability = durability;
		this.max_durability = durability;
		this.bullet = bullet;
		this.knockback_multiplier = knockback_multiplier;
	}

	public Gun(JavaPlugin plugin, ArrayList<ItemStack> items, ArrayList<ItemStack> icon) {
		this(plugin, 1D, 1, 50, 1.1D, Egg.class, items, icon);
	}

	public void shoot(Player p) {
		if(canshoot){
			for (int i = 0; i < shoot_amount; i++) {
				p.launchProjectile(bullet);
				this.durability -= 1;
			}
			canshoot = false;
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
				public void run(){
					canshoot = true;
				}
			}, (long) (20D / speed));
		}
	}

	public void onHit(Entity ent) {
		ent.setVelocity(ent.getLocation().getDirection().multiply((-1D) * knockback_multiplier));
	}

	public void reloadGun(){
		this.durability = this.max_durability;
		this.canshoot = true;
	}
	
}
