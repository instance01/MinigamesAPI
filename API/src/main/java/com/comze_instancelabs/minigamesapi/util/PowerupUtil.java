package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PowerupUtil {

	public static void spawnPowerup(Location l, ItemStack item) {
		World w = l.getWorld();
		Chicken c = w.spawn(l, Chicken.class);
		c.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
		Item i = w.dropItem(l, item);
		c.setPassenger(i);
	}

}
