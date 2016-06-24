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
package com.comze_instancelabs.minigamesapi.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSQL
{
    
    // used for rewards and stats
    
    JavaPlugin      plugin    = null;
    private boolean mysql     = true; // false for sqlite
    MySQL           MySQL;
    SQLite          SQLite;
    
    // Set to true if tables don't contain UUIDs
    boolean         oldFormat = false;
    
    public MainSQL(final JavaPlugin plugin, final boolean mysql)
    {
        this.plugin = plugin;
        this.mysql = mysql;
        
        if (mysql)
        {
            this.MySQL = new MySQL(plugin.getConfig().getString("mysql.host"), "3306", plugin.getConfig().getString("mysql.database"), plugin.getConfig().getString("mysql.user"),
                    plugin.getConfig().getString("mysql.pw"));
        }
        else
        {
            this.SQLite = new SQLite(plugin.getConfig().getString("mysql.database"), plugin.getConfig().getString("mysql.user"), plugin.getConfig().getString("mysql.pw"));
        }
        
        if (plugin.getConfig().getBoolean("mysql.enabled") && this.MySQL != null)
        {
            try
            {
                this.createTables();
            }
            catch (final Exception e)
            {
                System.out.println("Failed initializing MySQL. Disabling!");
                plugin.getConfig().set("mysql.enabled", false);
                plugin.saveConfig();
            }
        }
        else if (plugin.getConfig().getBoolean("mysql.enabled") && this.MySQL == null)
        {
            System.out.println("Failed initializing MySQL. Disabling!");
            plugin.getConfig().set("mysql.enabled", false);
            plugin.saveConfig();
        }
    }
    
    public void createTables()
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final Connection c = this.MySQL.open();
        
        try
        {
            c.createStatement().execute("CREATE DATABASE IF NOT EXISTS `" + this.plugin.getConfig().getString("mysql.database") + "`");
            c.createStatement()
                    .execute("CREATE TABLE IF NOT EXISTS " + this.plugin.getName() + " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, player VARCHAR(100), points INT, wins INT, loses INT, kills INT)");
            final ResultSet res = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'kills'");
            if (!res.isBeforeFirst())
            {
                // old table format without kills column -> add kills column
                c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD kills INT");
            }
            final ResultSet res2 = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'deaths'");
            if (!res2.isBeforeFirst())
            {
                // old table format without deaths column -> add deaths column
                c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD deaths INT");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'uuid'");
            if (!res3.isBeforeFirst())
            {
                // old table format without uuid column -> add uuid column
                c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD uuid VARCHAR(100)");
                this.oldFormat = true;
            }
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void updateWinnerStats(final Player p, final int reward, final boolean addwin)
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final String uuid = p.getUniqueId().toString();
        final Connection c = this.MySQL.open();
        
        final int wincount = addwin ? 1 : 0;
        
        try
        {
            if (this.oldFormat)
            {
                c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'");
            if (!res3.isBeforeFirst())
            {
                // there's no such user
                c.createStatement().executeUpdate("INSERT INTO " + this.plugin.getName() + " VALUES('0', '" + p.getName() + "', '" + Integer.toString(reward) + "', '" + Integer.toString(wincount)
                        + "', '0', '0', '0', '" + uuid + "')");
                return;
            }
            res3.next();
            final int points = res3.getInt("points") + reward;
            final int wins = res3.getInt("wins") + wincount;
            
            c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET points='" + Integer.toString(points) + "', wins='" + Integer.toString(wins) + "' WHERE uuid='" + uuid + "'");
            
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void updateLoserStats(final Player p)
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final String uuid = p.getUniqueId().toString();
        final Connection c = this.MySQL.open();
        
        try
        {
            if (this.oldFormat)
            {
                c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'");
            if (!res3.isBeforeFirst())
            {
                // there's no such user
                c.createStatement().executeUpdate("INSERT INTO " + this.plugin.getName() + " VALUES('0', '" + p.getName() + "', '0', '0', '1', '0', '0', '" + uuid + "')");
                return;
            }
            res3.next();
            final int loses = res3.getInt("loses") + 1;
            
            c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET loses='" + Integer.toString(loses) + "' WHERE uuid='" + uuid + "'");
            
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void updateKillerStats(final Player p, final int kills_)
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final String uuid = p.getUniqueId().toString();
        final Connection c = this.MySQL.open();
        
        try
        {
            if (this.oldFormat)
            {
                c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'");
            if (!res3.isBeforeFirst())
            {
                // there's no such user
                c.createStatement().executeUpdate("INSERT INTO " + this.plugin.getName() + " VALUES('0', '" + p.getName() + "', '0', '0', '0', '1', '0', '" + uuid + "')");
                return;
            }
            res3.next();
            final int kills = res3.getInt("kills") + kills_;
            
            c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET kills='" + Integer.toString(kills) + "' WHERE uuid='" + uuid + "'");
            
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void updateDeathStats(final Player p, final int deaths_)
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final String uuid = p.getUniqueId().toString();
        final Connection c = this.MySQL.open();
        
        try
        {
            if (this.oldFormat)
            {
                c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'");
            if (!res3.isBeforeFirst())
            {
                // there's no such user
                c.createStatement().executeUpdate("INSERT INTO " + this.plugin.getName() + " VALUES('0', '" + p.getName() + "', '0', '0', '0', '0', '1', '" + uuid + "')");
                return;
            }
            res3.next();
            final int deaths = res3.getInt("deaths") + deaths_;
            
            c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET deaths='" + Integer.toString(deaths) + "' WHERE uuid='" + uuid + "'");
            
        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public int getPoints(final Player p)
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return -1;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final String uuid = p.getUniqueId().toString();
        final Connection c = this.MySQL.open();
        
        try
        {
            if (this.oldFormat)
            {
                c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'");
            
            if (res3.isBeforeFirst())
            {
                res3.next();
                final int credits = res3.getInt("points");
                return credits;
            }
            else
            {
                // System.out.println("New User detected.");
            }
        }
        catch (final SQLException e)
        {
            //
        }
        return -1;
    }
    
    public int getWins(final Player p)
    {
        if (!this.plugin.getConfig().getBoolean("mysql.enabled"))
        {
            return -1;
        }
        if (!this.mysql)
        {
            // TODO SQLite
        }
        final String uuid = p.getUniqueId().toString();
        final Connection c = this.MySQL.open();
        
        try
        {
            if (this.oldFormat)
            {
                c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'");
            }
            final ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'");
            
            if (res3.isBeforeFirst())
            {
                res3.next();
                final int wins = res3.getInt("wins");
                return wins;
            }
            else
            {
                // System.out.println("New User detected.");
            }
        }
        catch (final SQLException e)
        {
            //
        }
        return -1;
    }
    
}
