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
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;

public class Guns
{
    
    // Four gun types:
    // - default pistol
    // - sniper
    // - freeze gun
    // - grenade launcher
    
    // Four upgradable attributes:
    // - speed
    // - durability
    // - shoot_amount
    // - knockback_multiplier
    
    // each attribute has 3 levels
    // that gives us 4*4*3 = 48 levels
    
    // how much more the next level of an attribute will cost
    public double                               level_multiplier          = 3D;
    
    // attribute base costs
    public int                                  speed_cost                = 40;
    public int                                  durability_cost           = 30;
    public int                                  shoot_amount_cost         = 70;
    public int                                  knockback_multiplier_cost = 100;
    
    public HashMap<String, IconMenu>            lastmainiconm             = new HashMap<>();
    public HashMap<String, IconMenu>            lastmainediticonm         = new HashMap<>();
    public HashMap<String, IconMenu>            lastupgradeiconm          = new HashMap<>();
    
    // TODO this means only for one plugin for now
    public HashMap<String, HashMap<Gun, int[]>> pgunattributes            = new HashMap<>();
    
    public JavaPlugin                           plugin;
    
    public Guns(final JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    public void openGUI(final String p)
    {
        final int credits = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getStatsInstance().getPoints(p);
        IconMenu iconm;
        if (this.lastmainiconm.containsKey(p))
        {
            iconm = this.lastmainiconm.get(p);
        }
        else
        {
            iconm = new IconMenu("Gun Upgrades (Credits: " + credits + ")", 36, event -> {
                final String d = event.getName();
                final Player p1 = event.getPlayer();
                if (MinigamesAPI.getAPI().getPluginInstance(Guns.this.plugin).getAllGuns().containsKey(d))
                {
                    Guns.this.openGunMainEditGUI(p1.getName(), d);
                }
                else
                {
                    final String raw = event.getItem().getItemMeta().getLore().get(0);
                    final String gun = raw.substring(0, raw.indexOf(" "));
                    final Gun g = MinigamesAPI.getAPI().getPluginInstance(Guns.this.plugin).getAllGuns().get(gun);
                    if (g != null)
                    {
                        final int[] pattributes = Guns.this.getPlayerGunAttributeLevels(Guns.this.plugin, p1.getName(), g);
                        HashMap<Gun, int[]> t;
                        if (Guns.this.pgunattributes.containsKey(p1.getName()))
                        {
                            t = Guns.this.pgunattributes.get(p1.getName());
                            t.put(g, pattributes);
                        }
                        else
                        {
                            t = new HashMap<>();
                            t.put(g, pattributes);
                        }
                        Guns.this.pgunattributes.put(p1.getName(), t);
                        boolean done = false;
                        double cost = 0.0D;
                        if (d.startsWith("Speed"))
                        {
                            final int i1 = pattributes[0];
                            cost = Math.pow(Guns.this.level_multiplier, i1) * Guns.this.speed_cost;
                            if (i1 < 3 && credits >= cost)
                            {
                                Guns.this.openUpgradeGUI(p1.getName(), gun, "speed", pattributes[0] + 1, cost);
                                done = true;
                                // setPlayerGunLevel(plugin, p.getName(), gun, "speed", pattributes[0] + 1);
                            }
                        }
                        else if (d.startsWith("Durability"))
                        {
                            final int i2 = pattributes[1];
                            cost = Math.pow(Guns.this.level_multiplier, i2) * Guns.this.durability_cost;
                            if (i2 < 3 && credits >= cost)
                            {
                                Guns.this.openUpgradeGUI(p1.getName(), gun, "durability", pattributes[1] + 1, cost);
                                done = true;
                            }
                        }
                        else if (d.startsWith("Shoot"))
                        {
                            final int i3 = pattributes[2];
                            cost = Math.pow(Guns.this.level_multiplier, i3) * Guns.this.shoot_amount_cost;
                            if (i3 < 3 && credits >= cost)
                            {
                                Guns.this.openUpgradeGUI(p1.getName(), gun, "shoot", pattributes[2] + 1, cost);
                                done = true;
                            }
                        }
                        else if (d.startsWith("Knockback"))
                        {
                            final int i4 = pattributes[3];
                            cost = Math.pow(Guns.this.level_multiplier, i4) * Guns.this.knockback_multiplier_cost;
                            if (i4 < 3 && credits >= cost)
                            {
                                Guns.this.openUpgradeGUI(p1.getName(), gun, "knockback", pattributes[3] + 1, cost);
                                done = true;
                            }
                        }
                        if (!done)
                        {
                            p1.sendMessage(MinigamesAPI.getAPI().getPluginInstance(Guns.this.plugin).getMessagesConfig().not_enough_credits.replaceAll("<credits>", Double.toString(cost)));
                        }
                    }
                }
                event.setWillClose(false);
            }, this.plugin);
            this.lastmainiconm.put(p, iconm);
        }
        
        int c = 0;
        for (final String ac : MinigamesAPI.getAPI().getPluginInstance(this.plugin).getAllGuns().keySet())
        {
            final Gun ac_ = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getAllGuns().get(ac);
            final int[] pattributes = this.getPlayerGunAttributeLevels(this.plugin, p, ac_);
            iconm.setOption(c, ac_.icon.get(0), ac, MinigamesAPI.getAPI().getPluginInstance(this.plugin).getGunsConfig().getConfig().getString("config.guns." + ac + ".lore"));
            iconm.setOption(c + 2, new ItemStack(Material.SUGAR), "Speed Lv " + ChatColor.DARK_RED + pattributes[0], ac + " Speed Upgrade");
            iconm.setOption(c + 3, new ItemStack(Material.DIAMOND), "Durability Lv " + ChatColor.DARK_RED + pattributes[1], ac + " Durability Upgrade");
            iconm.setOption(c + 4, new ItemStack(Material.EGG), "Shoot Lv " + ChatColor.DARK_RED + pattributes[2], ac + " Shoot amount Upgrade");
            iconm.setOption(c + 5, new ItemStack(Material.STICK), "Knockback Lv " + ChatColor.DARK_RED + pattributes[3], ac + " Knockback Upgrade");
            c += 9;
        }
        
        iconm.open(Bukkit.getPlayerExact(p));
    }
    
    public int[] getPlayerGunAttributeLevels(final JavaPlugin plugin, final String p, final Gun g)
    {
        final int[] ret = new int[4];
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().getConfig();
        final String path = "players." + p + "." + g.name + ".";
        ret[0] = config.isSet(path + "speed") ? config.getInt(path + "speed") : 0;
        ret[1] = config.isSet(path + "durability") ? config.getInt(path + "durability") : 0;
        ret[2] = config.isSet(path + "shoot") ? config.getInt(path + "shoot") : 0;
        ret[3] = config.isSet(path + "knockback") ? config.getInt(path + "knockback") : 0;
        HashMap<Gun, int[]> t;
        if (this.pgunattributes.containsKey(p))
        {
            t = this.pgunattributes.get(p);
            t.put(g, ret);
        }
        else
        {
            t = new HashMap<>();
            t.put(g, ret);
        }
        this.pgunattributes.put(p, t);
        return ret;
    }
    
    public void setPlayerGunLevel(final JavaPlugin plugin, final String p, final String g, final String attribute, final int level, final double cost)
    {
        final int credits = MinigamesAPI.getAPI().getPluginInstance(plugin).getStatsInstance().getPoints(p);
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().getConfig();
        final String path = "players." + p + "." + g + ".";
        config.set(path + attribute, level);
        MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().saveConfig();
        MinigamesAPI.getAPI().getPluginInstance(plugin).getStatsInstance().setPoints(p, (int) (credits - cost));
    }
    
    public void setPlayerGunMain(final JavaPlugin plugin, final String p, final String g, final boolean val)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().getConfig();
        final String path = "players." + p + "." + g + ".main";
        if (this.getPlayerAllMainGunsCount(plugin, p) > 1 && val)
        {
            Bukkit.getPlayer(p).sendMessage(MinigamesAPI.getAPI().getPluginInstance(plugin).getMessagesConfig().too_many_main_guns);
            return;
        }
        config.set(path, val);
        MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().saveConfig();
    }
    
    public int getPlayerAllMainGunsCount(final JavaPlugin plugin, final String p)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().getConfig();
        final String path = "players." + p + ".";
        int ret = 0;
        if (config.isSet(path))
        {
            for (final String g : config.getConfigurationSection(path).getKeys(false))
            {
                if (config.isSet(path + g + ".main"))
                {
                    if (config.getBoolean(path + g + ".main"))
                    {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }
    
    public void openGunMainEditGUI(final String p, final String g)
    {
        IconMenu iconm;
        final Player p_ = Bukkit.getPlayer(p);
        String guns = "";
        for (final String gun : this.getAllMainGuns(p_))
        {
            guns += gun + ", ";
        }
        if (guns.equalsIgnoreCase(""))
        {
            guns = "--";
        }
        guns = guns.substring(0, guns.length() - 2);
        p_.sendMessage(MinigamesAPI.getAPI().getPluginInstance(this.plugin).getMessagesConfig().all_guns.replaceAll("<guns>", guns));
        
        if (this.lastmainediticonm.containsKey(p))
        {
            iconm = this.lastmainediticonm.get(p);
        }
        else
        {
            iconm = new IconMenu("Set Main Gun", 9, event -> {
                final String d = event.getName();
                final Player p1 = event.getPlayer();
                if (d.startsWith("Set"))
                {
                    Guns.this.setPlayerGunMain(Guns.this.plugin, p1.getName(), g, true);
                }
                else if (d.startsWith("Remove"))
                {
                    Guns.this.setPlayerGunMain(Guns.this.plugin, p1.getName(), g, false);
                }
                Guns.this.openGUI(p1.getName());
                event.setWillClose(false);
                event.setWillDestroy(true);
            }, this.plugin);
        }
        
        iconm.setOption(0, new ItemStack(Material.WOOL, 1, (short) 5), "Set " + g + " as Main/Secondary Gun", "");
        iconm.setOption(8, new ItemStack(Material.WOOL, 1, (short) 14), "Remove " + g + " as Main/Secondary Gun", "");
        
        iconm.open(Bukkit.getPlayerExact(p));
    }
    
    public void openUpgradeGUI(final String p, final String g, final String attribute, final int level, final double cost)
    {
        IconMenu iconm;
        if (this.lastupgradeiconm.containsKey(p))
        {
            iconm = this.lastupgradeiconm.get(p);
        }
        else
        {
            iconm = new IconMenu("Upgrade?", 9, event -> {
                final String d = event.getName();
                final Player p1 = event.getPlayer();
                if (d.startsWith("Buy"))
                {
                    Guns.this.setPlayerGunLevel(Guns.this.plugin, p1.getName(), g, attribute, level, cost);
                    p1.sendMessage(MinigamesAPI.getAPI().getPluginInstance(Guns.this.plugin).getMessagesConfig().attributelevel_increased.replaceAll("<attribute>", attribute));
                }
                Guns.this.openGUI(p1.getName());
                event.setWillClose(false);
                event.setWillDestroy(true);
            }, this.plugin);
        }
        
        iconm.setOption(0, new ItemStack(Material.WOOL, 1, (short) 5), "Buy " + attribute + " Upgrade", "");
        iconm.setOption(8, new ItemStack(Material.WOOL, 1, (short) 14), "DON'T buy " + attribute + " Upgrade", "");
        
        iconm.open(Bukkit.getPlayerExact(p));
    }
    
    public static void loadGuns(final JavaPlugin plugin)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getGunsConfig().getConfig();
        if (config.isSet("config.guns"))
        {
            for (final String gun : config.getConfigurationSection("config.guns.").getKeys(false))
            {
                final String path = "config.guns." + gun + ".";
                final Gun n = new Gun(plugin, gun, config.getDouble(path + "speed"), config.getInt(path + "durability"), config.getInt(path + "shoot_amount"),
                        config.getDouble(path + "knockback_multiplier"), Egg.class, Util.parseItems(config.getString(path + "items")), Util.parseItems(config.getString(path + "icon")));
                MinigamesAPI.getAPI().getPluginInstance(plugin).addGun(gun, n);
            }
        }
    }
    
    public ArrayList<String> getAllMainGuns(final Player p)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getGunsConfig().getConfig();
        final ArrayList<String> ret = new ArrayList<>();
        if (config.isSet("players." + p.getName()))
        {
            for (final String gun : config.getConfigurationSection("players." + p.getName() + ".").getKeys(false))
            {
                final String path = "players." + p.getName() + "." + gun + ".main";
                if (config.isSet(path))
                {
                    if (config.getBoolean(path))
                    {
                        ret.add(gun);
                    }
                }
            }
        }
        return ret;
    }
    
    public void giveMainGuns(final Player p)
    {
        final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getGunsConfig().getConfig();
        if (config.isSet("players." + p.getName() + "."))
        {
            int count = 0;
            for (final String gun : config.getConfigurationSection("players." + p.getName() + ".").getKeys(false))
            {
                final String path = "players." + p.getName() + "." + gun + ".main";
                if (config.isSet(path))
                {
                    if (config.getBoolean(path))
                    {
                        // main gun
                        final Gun g = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getAllGuns().get(gun);
                        if (g != null)
                        {
                            p.updateInventory();
                            for (final ItemStack i : g.items)
                            {
                                final ItemStack temp = i;
                                final ItemMeta itemm = temp.getItemMeta();
                                itemm.setDisplayName(gun);
                                temp.setItemMeta(itemm);
                                p.getInventory().addItem(temp);
                            }
                            p.updateInventory();
                            count++;
                        }
                    }
                }
            }
            if (count < 1)
            {
                // player doesn't have any main, give random gun
                final HashMap<String, Gun> guns = MinigamesAPI.getAPI().getPluginInstance(this.plugin).getAllGuns();
                final List<String> t = new ArrayList<>(guns.keySet());
                final String gun = t.get((new Random()).nextInt(t.size()));
                final Gun g = guns.get(gun);
                if (g != null)
                {
                    p.updateInventory();
                    for (final ItemStack i : g.items)
                    {
                        final ItemStack temp = i;
                        final ItemMeta itemm = temp.getItemMeta();
                        itemm.setDisplayName(gun);
                        temp.setItemMeta(itemm);
                        p.getInventory().addItem(temp);
                    }
                    p.updateInventory();
                    count++;
                }
            }
        }
    }
    
}
