package com.comze_instancelabs.minigamesapi.guns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;

public class Gun {

	public double speed = 1D; // the higher the faster
	public int shoot_amount = 1;
	public int max_durability = 50;
	public int durability = 50;
	public Class<? extends Projectile> bullet = Egg.class;
	public Player p;
	public JavaPlugin plugin;

	boolean canshoot = true;

	public Gun(JavaPlugin plugin, Player p, double speed, int shoot_amount, int durability, Class<? extends Projectile> bullet) {
		this.p = p;
		this.plugin = plugin;
		this.speed = speed;
		this.shoot_amount = shoot_amount;
		this.durability = durability;
		this.max_durability = durability;
		this.bullet = bullet;
	}

	public Gun(JavaPlugin plugin, Player p) {
		this(plugin, p, 1D, 1, 50, Egg.class);
	}

	public void shoot() {
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

	}

	public void reload(){
		this.durability = this.max_durability;
		this.canshoot = true;
	}
	
}
