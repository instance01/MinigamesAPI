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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.util.AClass;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;
import com.shampaggon.crackshot.CSUtility;

import net.milkbowl.vault.economy.EconomyResponse;

public class Classes
{
    
    JavaPlugin                       plugin;
    PluginInstance                   pli;
    public HashMap<String, IconMenu> lasticonm = new HashMap<>();
    
    public Classes(final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
    }
    
    public Classes(final PluginInstance pli, final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.pli = pli;
    }
    
    public void openGUI(final String p)
    {
        final Classes cl = this;
        IconMenu iconm;
        final int mincount = this.pli.getAClasses().keySet().size();
        if (!Validator.isPlayerOnline(p))
        {
            return;
        }
        final Player player = Bukkit.getPlayerExact(p);
        if (this.lasticonm.containsKey(p))
        {
            iconm = this.lasticonm.get(p);
        }
        else
        {
            iconm = new IconMenu(this.pli.getMessagesConfig().classes_item, (9 * this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_CLASSES_GUI_ROWS) > mincount - 1)
                    ? 9 * this.plugin.getConfig().getInt(ArenaConfigStrings.CONFIG_CLASSES_GUI_ROWS) : Math.round(mincount / 9) * 9 + 9, event -> {
                        if (event.getPlayer().getName().equalsIgnoreCase(p))
                        {
                            if (Classes.this.pli.global_players.containsKey(p))
                            {
                                if (Classes.this.pli.getArenas().contains(Classes.this.pli.global_players.get(p)))
                                {
                                    final String d = event.getName();
                                    final Player p1 = event.getPlayer();
                                    if (Classes.this.pli.getAClasses().containsKey(d))
                                    {
                                        cl.setClass(Classes.this.pli.getClassesHandler().getInternalNameByName(d), p1.getName(), true);
                                    }
                                }
                            }
                        }
                        event.setWillClose(true);
                    }, this.plugin);
            
            int c = 0;
            for (final String ac : this.pli.getAClasses().keySet())
            {
                final AClass ac_ = this.pli.getAClasses().get(ac);
                if (ac_.isEnabled())
                {
                    if (!this.pli.show_classes_without_usage_permission)
                    {
                        if (!this.kitPlayerHasPermission(ac_.getInternalName(), player))
                        {
                            continue;
                        }
                    }
                    int slot = c;
                    if (this.pli.getClassesConfig().getConfig().isSet("config.kits." + ac_.getInternalName() + ".slot"))
                    {
                        slot = this.pli.getClassesConfig().getConfig().getInt("config.kits." + ac_.getInternalName() + ".slot");
                        if (slot < 0 || slot > iconm.getSize() - 1)
                        {
                            slot = c;
                        }
                    }
                    iconm.setOption(slot, ac_.getIcon().clone(), ac_.getName(), this.pli.getClassesConfig().getConfig().getString("config.kits." + ac_.getInternalName() + ".lore").split(";"));
                    c++;
                }
            }
        }
        
        iconm.open(player);
        this.lasticonm.put(p, iconm);
    }
    
    public void getClass(final String player)
    {
        if (!this.pli.getPClasses().containsKey(player))
        {
            ArenaLogger.debug(player + " didn't select any kit and the auto_add_default_kit option might be turned off, thus he won't get any starting items.");
            return;
        }
        final AClass c = this.pli.getPClasses().get(player);
        final Player p = Bukkit.getServer().getPlayer(player);
        Util.clearInv(p);
        final ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(c.getItems()));
        final ArrayList<ItemStack> temp = new ArrayList<>(Arrays.asList(c.getItems()));
        final ArrayList<String> tempguns = new ArrayList<>();
        final ArrayList<PotionEffectType> temppotions = new ArrayList<>();
        final ArrayList<Integer> temppotions_lv = new ArrayList<>();
        final ArrayList<Integer> temppotions_duration = new ArrayList<>();
        
        // crackshot support
        for (final ItemStack item : temp)
        {
            if (item != null)
            {
                if (item.hasItemMeta())
                {
                    if (item.getItemMeta().hasDisplayName())
                    {
                        if (item.getItemMeta().getDisplayName().startsWith("crackshot:"))
                        {
                            items.remove(item);
                            tempguns.add(item.getItemMeta().getDisplayName().split(":")[1]);
                        }
                        else if (item.getItemMeta().getDisplayName().startsWith("potioneffect:"))
                        {
                            items.remove(item);
                            final String potioneffect = item.getItemMeta().getDisplayName().split(":")[1];
                            final String data = item.getItemMeta().getDisplayName().split(":")[2];
                            final Integer time = Integer.parseInt(data.substring(0, data.indexOf("#")));
                            final Integer lv = Integer.parseInt(data.split("#")[1]);
                            if (PotionEffectType.getByName(potioneffect) != null)
                            {
                                temppotions.add(PotionEffectType.getByName(potioneffect));
                                temppotions_lv.add(lv);
                                temppotions_duration.add(time);
                            }
                        }
                    }
                }
            }
        }
        
        for (final ItemStack item : items)
        {
            if (item != null)
            {
                Color c_ = null;
                if (item.hasItemMeta())
                {
                    if (item.getItemMeta().hasDisplayName())
                    {
                        if (item.getItemMeta().getDisplayName().startsWith("#") && item.getItemMeta().getDisplayName().length() == 7)
                        {
                            c_ = Util.hexToRgb(item.getItemMeta().getDisplayName());
                        }
                    }
                }
                if (item.getTypeId() == 298 || item.getTypeId() == 302 || item.getTypeId() == 306 || item.getTypeId() == 310 || item.getTypeId() == 314)
                {
                    if (item.getTypeId() == 298)
                    {
                        final LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
                        if (c_ != null)
                        {
                            lam.setColor(c_);
                        }
                        item.setItemMeta(lam);
                    }
                    p.getInventory().setHelmet(item);
                    continue;
                }
                if (item.getTypeId() == 299 || item.getTypeId() == 303 || item.getTypeId() == 307 || item.getTypeId() == 311 || item.getTypeId() == 315)
                {
                    if (item.getTypeId() == 299)
                    {
                        final LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
                        if (c_ != null)
                        {
                            lam.setColor(c_);
                        }
                        item.setItemMeta(lam);
                    }
                    p.getInventory().setChestplate(item);
                    continue;
                }
                if (item.getTypeId() == 300 || item.getTypeId() == 304 || item.getTypeId() == 308 || item.getTypeId() == 312 || item.getTypeId() == 316)
                {
                    if (item.getTypeId() == 300)
                    {
                        final LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
                        if (c_ != null)
                        {
                            lam.setColor(c_);
                        }
                        item.setItemMeta(lam);
                    }
                    p.getInventory().setLeggings(item);
                    continue;
                }
                if (item.getTypeId() == 301 || item.getTypeId() == 305 || item.getTypeId() == 309 || item.getTypeId() == 313 || item.getTypeId() == 317)
                {
                    if (item.getTypeId() == 301)
                    {
                        final LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
                        if (c_ != null)
                        {
                            lam.setColor(c_);
                        }
                        item.setItemMeta(lam);
                    }
                    p.getInventory().setBoots(item);
                    continue;
                }
                if (item.getType() != Material.AIR)
                {
                    p.getInventory().addItem(item);
                }
            }
        }
        // p.getInventory().setContents((ItemStack[]) items.toArray(new ItemStack[items.size()]));
        p.updateInventory();
        
        if (MinigamesAPI.getAPI().crackshotAvailable())
        {
            for (final String t : tempguns)
            {
                final CSUtility cs = new CSUtility();
                cs.giveWeapon(p, t, 1);
            }
        }
        
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            if (p != null)
            {
                int index = 0;
                for (final PotionEffectType t : temppotions)
                {
                    p.addPotionEffect(new PotionEffect(t, temppotions_duration.get(index), temppotions_lv.get(index)));
                    index++;
                }
            }
        }, 10L);
        
    }
    
    /**
     * Sets the current class of a player
     * 
     * @param iname
     *            the INTERNAL classname
     * @param player
     */
    public void setClass(String iname, final String player, final boolean money)
    {
        String internalname = iname;
        for (final String c : this.pli.getAClasses().keySet())
        {
            if (c.toLowerCase().equalsIgnoreCase(internalname.toLowerCase()))
            {
                internalname = c;
            }
        }
        if (!this.kitPlayerHasPermission(internalname, Bukkit.getPlayer(player)))
        {
            Bukkit.getPlayer(player).sendMessage(this.pli.getMessagesConfig().no_perm);
            return;
        }
        boolean continue_ = true;
        if (money)
        {
            if (this.kitRequiresMoney(internalname))
            {
                continue_ = this.kitTakeMoney(Bukkit.getPlayer(player), internalname);
            }
        }
        if (continue_)
        {
            this.pli.setPClass(player, this.getClassByInternalname(internalname));
            Bukkit.getPlayer(player)
                    .sendMessage(this.pli.getMessagesConfig().set_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', this.getClassByInternalname(internalname).getName())));
        }
    }
    
    public String getInternalNameByName(final String name)
    {
        for (final AClass ac : this.pli.getAClasses().values())
        {
            if (ac.getName().equalsIgnoreCase(name))
            {
                return ac.getInternalName();
            }
        }
        return "default";
    }
    
    public AClass getClassByInternalname(final String internalname)
    {
        for (final AClass ac : this.pli.getAClasses().values())
        {
            if (ac.getInternalName().equalsIgnoreCase(internalname))
            {
                return ac;
            }
        }
        return null;
    }
    
    public boolean hasClass(final String player)
    {
        return this.pli.getPClasses().containsKey(player);
    }
    
    public String getSelectedClass(final String player)
    {
        if (this.hasClass(player))
        {
            return this.pli.getPClasses().get(player).getInternalName();
        }
        return "default";
    }
    
    public void loadClasses()
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final FileConfiguration config = Classes.this.pli.getClassesConfig().getConfig();
            if (config.isSet("config.kits"))
            {
                for (final String aclass : config.getConfigurationSection("config.kits.").getKeys(false))
                {
                    AClass n;
                    if (config.isSet("config.kits." + aclass + ".icon"))
                    {
                        n = new AClass(Classes.this.plugin, config.getString("config.kits." + aclass + ".name"), aclass,
                                config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true,
                                Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
                    }
                    else
                    {
                        n = new AClass(Classes.this.plugin, config.getString("config.kits." + aclass + ".name"), aclass,
                                config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true,
                                Util.parseItems(config.getString("config.kits." + aclass + ".items")));
                    }
                    Classes.this.pli.addAClass(config.getString("config.kits." + aclass + ".name"), n);
                    if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore"))
                    {
                        Classes.this.plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
                    }
                }
            }
        }, 20L);
    }
    
    /**
     * Please use new Classes().loadClasses();
     * 
     * @param plugin
     */
    @Deprecated
    public static void loadClasses(final JavaPlugin plugin)
    {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            final FileConfiguration config = MinigamesAPI.getAPI().getPluginInstance(plugin).getClassesConfig().getConfig();
            if (config.isSet("config.kits"))
            {
                for (final String aclass : config.getConfigurationSection("config.kits.").getKeys(false))
                {
                    AClass n;
                    if (config.isSet("config.kits." + aclass + ".icon"))
                    {
                        n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass,
                                config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true,
                                Util.parseItems(config.getString("config.kits." + aclass + ".items")), Util.parseItems(config.getString("config.kits." + aclass + ".icon")).get(0));
                    }
                    else
                    {
                        n = new AClass(plugin, config.getString("config.kits." + aclass + ".name"), aclass,
                                config.isSet("config.kits." + aclass + ".enabled") ? config.getBoolean("config.kits." + aclass + ".enabled") : true,
                                Util.parseItems(config.getString("config.kits." + aclass + ".items")));
                    }
                    // pli.addAClass(aclass, n);
                    MinigamesAPI.getAPI().getPluginInstance(plugin).addAClass(config.getString("config.kits." + aclass + ".name"), n);
                    if (!config.isSet("config.kits." + aclass + ".items") || !config.isSet("config.kits." + aclass + ".lore"))
                    {
                        plugin.getLogger().warning("One of the classes found in the config file is invalid: " + aclass + ". Missing itemid or lore!");
                    }
                }
            }
        }, 20L);
    }
    
    /**
     * Returns whether the kit requires money to use it
     * 
     * @param kit
     *            Internal name of the kit
     * @return
     */
    public boolean kitRequiresMoney(final String kit)
    {
        return this.pli.getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_money");
    }
    
    /**
     * Gives the player the kit if he has enough money to buy it
     * 
     * @param p
     *            Player to give the kit to
     * @param kit
     *            Internal name of the kit
     * @return
     */
    public boolean kitTakeMoney(final Player p, final String kit)
    {
        // Credits
        if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_USE_CREADITS_INSTEAD_MONEY_FOR_KITS))
        {
            final String uuid = p.getUniqueId().toString();
            int points = 0;
            if (!MinigamesAPI.getAPI().statsglobal.getConfig().isSet("players." + uuid + ".points"))
            {
                points = this.pli.getStatsInstance().getPoints(p.getName());
                MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", points);
                MinigamesAPI.getAPI().statsglobal.saveConfig();
            }
            else
            {
                points = MinigamesAPI.getAPI().statsglobal.getConfig().getInt("players." + uuid + ".points");
            }
            if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUY_CLASSES_FOREVER))
            {
                final ClassesConfig cl = this.pli.getClassesConfig();
                if (!cl.getConfig().isSet("players.bought_kits." + p.getName() + "." + kit))
                {
                    final int money = this.pli.getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount");
                    if (points >= money)
                    {
                        MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", points - money);
                        MinigamesAPI.getAPI().statsglobal.saveConfig();
                        cl.getConfig().set("players.bought_kits." + p.getName() + "." + kit, true);
                        cl.saveConfig();
                        p.sendMessage(this.pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', this.getClassByInternalname(kit).getName()))
                                .replaceAll("<money>", Integer.toString(money)));
                    }
                    else
                    {
                        p.sendMessage(this.pli.getMessagesConfig().not_enough_money);
                        return false;
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                if (this.hasClass(p.getName()))
                {
                    if (this.getSelectedClass(p.getName()).equalsIgnoreCase(kit))
                    {
                        return false;
                    }
                }
                final ClassesConfig config = this.pli.getClassesConfig();
                final int money = config.getConfig().getInt("config.kits." + kit + ".money_amount");
                if (points >= money)
                {
                    MinigamesAPI.getAPI().statsglobal.getConfig().set("players." + uuid + ".points", points - money);
                    MinigamesAPI.getAPI().statsglobal.saveConfig();
                    p.sendMessage(this.pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', this.getClassByInternalname(kit).getName()))
                            .replaceAll("<money>", Integer.toString(money)));
                }
                else
                {
                    p.sendMessage(this.pli.getMessagesConfig().not_enough_money);
                    return false;
                }
            }
            return true;
        }
        
        MinigamesAPI.getAPI();
        // Money (economy)
        if (!MinigamesAPI.getAPI().economyAvailable())
        {
            this.plugin.getLogger().warning("Economy is turned OFF. You can turn it on in the config.");
            return false;
        }
        if (MinigamesAPI.getAPI().economyAvailable())
        {
            if (this.plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_BUY_CLASSES_FOREVER))
            {
                final ClassesConfig cl = this.pli.getClassesConfig();
                if (!cl.getConfig().isSet("players.bought_kits." + p.getName() + "." + kit))
                {
                    final int money = this.pli.getClassesConfig().getConfig().getInt("config.kits." + kit + ".money_amount");
                    MinigamesAPI.getAPI();
                    if (MinigamesAPI.econ.getBalance(p.getName()) >= money)
                    {
                        MinigamesAPI.getAPI();
                        final EconomyResponse r = MinigamesAPI.econ.withdrawPlayer(p.getName(), money);
                        if (!r.transactionSuccess())
                        {
                            p.sendMessage(String.format("An error occured: %s", r.errorMessage));
                            return false;
                        }
                        cl.getConfig().set("players.bought_kits." + p.getName() + "." + kit, true);
                        cl.saveConfig();
                        p.sendMessage(this.pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', this.getClassByInternalname(kit).getName()))
                                .replaceAll("<money>", Integer.toString(money)));
                    }
                    else
                    {
                        p.sendMessage(this.pli.getMessagesConfig().not_enough_money);
                        return false;
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                if (this.hasClass(p.getName()))
                {
                    if (this.getSelectedClass(p.getName()).equalsIgnoreCase(kit))
                    {
                        return false;
                    }
                    if (this.kitRequiresMoney(kit))
                    {
                        Util.sendMessage(this.plugin, p, this.pli.getMessagesConfig().kit_warning);
                    }
                }
                final ClassesConfig config = this.pli.getClassesConfig();
                final int money = config.getConfig().getInt("config.kits." + kit + ".money_amount");
                MinigamesAPI.getAPI();
                if (MinigamesAPI.econ.getBalance(p.getName()) >= money)
                {
                    MinigamesAPI.getAPI();
                    final EconomyResponse r = MinigamesAPI.econ.withdrawPlayer(p.getName(), money);
                    if (!r.transactionSuccess())
                    {
                        p.sendMessage(String.format("An error occured: %s", r.errorMessage));
                        return false;
                    }
                    p.sendMessage(this.pli.getMessagesConfig().successfully_bought_kit.replaceAll("<kit>", ChatColor.translateAlternateColorCodes('&', this.getClassByInternalname(kit).getName()))
                            .replaceAll("<money>", Integer.toString(money)));
                }
                else
                {
                    p.sendMessage(this.pli.getMessagesConfig().not_enough_money);
                    return false;
                }
            }
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    public boolean kitPlayerHasPermission(final String kit, final Player p)
    {
        if (!this.pli.getClassesConfig().getConfig().getBoolean("config.kits." + kit + ".requires_permission"))
        {
            return true;
        }
        else
        {
            if (p.hasPermission(this.pli.getClassesConfig().getConfig().getString("config.kits." + kit + ".permission_node")))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
}
