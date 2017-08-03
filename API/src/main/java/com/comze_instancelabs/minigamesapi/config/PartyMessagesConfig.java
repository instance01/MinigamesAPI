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
package com.comze_instancelabs.minigamesapi.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyMessagesConfig
{
    
    private FileConfiguration messagesConfig = null;
    private File              messagesFile   = null;
    private JavaPlugin        plugin         = null;
    
    public PartyMessagesConfig(final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.init();
    }
    
    public void init()
    {
        this.getConfig().addDefault("messages.cannot_invite_yourself", this.cannot_invite_yourself);
        this.getConfig().addDefault("messages.player_not_online", this.player_not_online);
        this.getConfig().addDefault("messages.you_invited", this.you_invited);
        this.getConfig().addDefault("messages.you_were_invited", this.you_were_invited);
        this.getConfig().addDefault("messages.not_invited_to_any_party", this.not_invited_to_any_party);
        this.getConfig().addDefault("messages.not_invited_to_players_party", this.not_invited_to_players_party);
        this.getConfig().addDefault("messages.player_not_in_party", this.player_not_in_party);
        this.getConfig().addDefault("messages.you_joined_party", this.you_joined_party);
        this.getConfig().addDefault("messages.player_joined_party", this.player_joined_party);
        this.getConfig().addDefault("messages.you_left_party", this.you_left_party);
        this.getConfig().addDefault("messages.player_left_party", this.player_left_party);
        this.getConfig().addDefault("messages.party_disbanded", this.party_disbanded);
        this.getConfig().addDefault("messages.party_too_big_to_join", this.party_too_big_to_join);
        
        // save
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        // load
        this.cannot_invite_yourself = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.cannot_invite_yourself"));
        this.player_not_online = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.player_not_online"));
        this.you_invited = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.you_invited"));
        this.you_were_invited = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.you_were_invited"));
        this.not_invited_to_any_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.not_invited_to_any_party"));
        this.not_invited_to_players_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.not_invited_to_players_party"));
        this.player_not_in_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.player_not_in_party"));
        this.you_joined_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.you_joined_party"));
        this.player_joined_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.player_joined_party"));
        this.you_left_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.you_left_party"));
        this.player_left_party = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.player_left_party"));
        this.party_disbanded = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.party_disbanded"));
        this.party_too_big_to_join = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.party_too_big_to_join"));
        
    }
    
    public String cannot_invite_yourself       = "&cYou cannot invite yourself!";
    public String player_not_online            = "&4<player> &cis not online!";
    public String you_invited                  = "&aYou invited &2<player>&a!";
    public String you_were_invited             = "&2<player> &ainvited you to join his/her party! Type &2/party accept <player> &ato accept.";
    public String not_invited_to_any_party     = "&cYou are not invited to any party.";
    public String not_invited_to_players_party = "&cYou are not invited to the party of &4<player>&c.";
    public String player_not_in_party          = "&4<player> &cis not in your party.";
    public String you_joined_party             = "&7You joined the party of &8<player>&7.";
    public String player_joined_party          = "&2<player> &ajoined the party.";
    public String you_left_party               = "&7You left the party of &8<player>&7.";
    public String player_left_party            = "&4<player> &cleft the party.";
    public String party_disbanded              = "&cThe party was disbanded.";
    public String party_too_big_to_join        = "&cYour party is too big to join this arena.";
    
    public FileConfiguration getConfig()
    {
        if (this.messagesConfig == null)
        {
            this.reloadConfig();
        }
        return this.messagesConfig;
    }
    
    public void saveConfig()
    {
        if (this.messagesConfig == null || this.messagesFile == null)
        {
            return;
        }
        try
        {
            this.getConfig().save(this.messagesFile);
        }
        catch (final IOException ex)
        {
            // silently ignore
        }
    }
    
    public void reloadConfig()
    {
        if (this.messagesFile == null)
        {
            this.messagesFile = new File(this.plugin.getDataFolder(), "partymessages.yml");
        }
        this.messagesConfig = YamlConfiguration.loadConfiguration(this.messagesFile);
        
        final InputStream defConfigStream = this.plugin.getResource("partymessages.yml");
        if (defConfigStream != null)
        {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.messagesConfig.setDefaults(defConfig);
        }
    }
    
}
