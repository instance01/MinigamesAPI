package com.comze_instancelabs.minigamesapi;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comze_instancelabs.minigamesapi.util.ParticleEffectNew;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Effects {

	public static void playBloodEffect(Player p) {
		p.getWorld().playEffect(p.getLocation().add(0D, 1D, 0D), Effect.STEP_SOUND, 152);
	}

	public static void playEffect(Arena a, Location l, String effectname) {
		for (String p_ : a.getAllPlayers()) {
			if (Validator.isPlayerOnline(p_)) {
				Player p = Bukkit.getPlayer(p_);
				ParticleEffectNew eff = ParticleEffectNew.valueOf(effectname);
				eff.setId(152);
				eff.animateReflected(p, l, 1F, 3);
			}
		}
	}

}
