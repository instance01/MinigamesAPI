package com.comze_instancelabs.minigamesapi.spigottest;

import net.minecraft.server.v1_12_R1.Enchantments;

/**
 * taken from spigot test sources
 */
class DummyEnchantments {
    static {
        Enchantments.DAMAGE_ALL.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {
        // dummy method
    }
}
