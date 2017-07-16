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
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

/**
 * Main sql interface.
 */
public class MainSQL
{
    
    // used for rewards and stats
    
    /**
     * plugin
     */
    JavaPlugin      plugin    = null;
    
    /**
     * mysql connection
     */
    MySQL           MySQL;
    
    /**
     * sqlite connection
     */
    SQLite          SQLite;
    
    /**
     * Set to true if tables don't contain UUIDs
     */
    boolean         oldFormat = false;
    
    /**
     * Constructor
     * @param plugin
     */
    public MainSQL(final JavaPlugin plugin)
    {
        this.plugin = plugin;
        
        if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_MYSQL_ENABLED))
        {
            this.MySQL = new MySQL(plugin.getConfig().getString(ArenaConfigStrings.CONFIG_MYSQL_HOST), "3306", plugin.getConfig().getString(ArenaConfigStrings.CONFIG_MYSQL_DATABASE), plugin.getConfig().getString(ArenaConfigStrings.CONFIG_MYSQL_USER), //$NON-NLS-1$
                    plugin.getConfig().getString(ArenaConfigStrings.CONFIG_MYSQL_PW));
        }
        else if (plugin.getConfig().getBoolean(ArenaConfigStrings.CONFIG_SQLITE_ENABLED))
        {
            this.SQLite = new SQLite(plugin.getDataFolder() + "/" + plugin.getConfig().getString(ArenaConfigStrings.CONFIG_SQLITE_DATABASE), plugin.getConfig().getString(ArenaConfigStrings.CONFIG_SQLITE_USER), plugin.getConfig().getString(ArenaConfigStrings.CONFIG_SQLITE_PW)); //$NON-NLS-1$
        }
        
        if (this.MySQL != null)
        {
            try
            {
                this.createTables();
            }
            catch (final Exception e)
            {
                this.MySQL = null;
                plugin.getLogger().log(Level.SEVERE, "Failed initializing MySQL. Disabling!", e); //$NON-NLS-1$
                plugin.getConfig().set(ArenaConfigStrings.CONFIG_MYSQL_ENABLED, false);
                plugin.saveConfig();
            }
        }
        else if (this.SQLite != null)
        {
            try
            {
                this.createTables();
            }
            catch (final Exception e)
            {
                this.SQLite = null;
                plugin.getLogger().log(Level.SEVERE, "Failed initializing SqLite. Disabling!", e); //$NON-NLS-1$
                plugin.getConfig().set(ArenaConfigStrings.CONFIG_SQLITE_ENABLED, false);
                plugin.saveConfig();
            }
        }
    }
    
    /**
     * Creates database tables
     */
    private void createTables()
    {
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                c.createStatement().execute("CREATE DATABASE IF NOT EXISTS `" + this.plugin.getConfig().getString(ArenaConfigStrings.CONFIG_MYSQL_DATABASE) + "`"); //$NON-NLS-1$ //$NON-NLS-2$
                c.createStatement()
                        .execute("CREATE TABLE IF NOT EXISTS " + this.plugin.getName() + " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, player VARCHAR(100), points INT, wins INT, loses INT, kills INT)");  //$NON-NLS-1$//$NON-NLS-2$
                try (final ResultSet res = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'kills'")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    if (!res.isBeforeFirst())
                    {
                        // old table format without kills column -> add kills column
                        c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD kills INT"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                try (final ResultSet res2 = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'deaths'")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    if (!res2.isBeforeFirst())
                    {
                        // old table format without deaths column -> add deaths column
                        c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD deaths INT"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                try (final ResultSet res3 = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'uuid'")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    if (!res3.isBeforeFirst())
                    {
                        // old table format without uuid column -> add uuid column
                        c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD uuid VARCHAR(100)"); //$NON-NLS-1$ //$NON-NLS-2$
                        this.oldFormat = true;
                    }
                }
                try (final ResultSet res3 = c.createStatement().executeQuery("SHOW COLUMNS FROM `" + this.plugin.getName() + "` LIKE 'gamepoints'")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    if (!res3.isBeforeFirst())
                    {
                        // old table format without gamepoints column -> add gamepoints column
                        c.createStatement().execute("ALTER TABLE " + this.plugin.getName() + " ADD gamepoints INT"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                c.createStatement()
                        .execute("CREATE TABLE IF NOT EXISTS " + this.plugin.getName() + " (id INTEGER PRIMARY KEY AUTOINCREMENT, player VARCHAR(100), points INT, wins INT, loses INT, kills INT, deaths INT, uuid VARCHAR(100), gamepoints INT)"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Update old format database
     * @param p
     * @param uuid
     * @param c
     * @throws SQLException
     */
    private void updateOldFormat(final Player p, final String uuid, final Connection c) throws SQLException
    {
        if (this.oldFormat)
        {
            try (final Statement stmt = c.createStatement())
            {
                stmt.executeUpdate("UPDATE " + this.plugin.getName() + " SET uuid='" + uuid + "' WHERE player='" + p.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            }
        }
    }
    
    /**
     * Updates winner stats
     * @param p
     * @param reward
     * @param addwin
     */
    public void updateWinnerStats(final Player p, final int reward, final boolean addwin)
    {
        final String uuid = p.getUniqueId().toString();
        
        final int wincount = addwin ? 1 : 0;
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                updateWinnerStats(p, reward, uuid, wincount, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                updateWinnerStats(p, reward, uuid, wincount, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Update winner stats
     * @param p
     * @param reward
     * @param uuid
     * @param wincount
     * @param c
     * @throws SQLException
     */
    private void updateWinnerStats(final Player p, final int reward, final String uuid, final int wincount, final Connection c) throws SQLException
    {
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (!res3.isBeforeFirst())
                {
                    // there's no such user
                    c.createStatement().executeUpdate("INSERT INTO `" + this.plugin.getName() + "` (`player`, `points`, `wins`, `loses`, `kills`, `deaths`, `uuid`, `gamepoints`) " //$NON-NLS-1$ //$NON-NLS-2$
                            + "VALUES('" + p.getName() + "', " + reward + ", " + wincount   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                            + ", 0, 0, 0, '" + uuid + "', 0)"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                else
                {
                    res3.next();
                    final int points = res3.getInt("points") + reward; //$NON-NLS-1$
                    final int wins = res3.getInt("wins") + wincount; //$NON-NLS-1$
                    
                    c.createStatement().executeUpdate("UPDATE " + this.plugin.getName() + " SET points=" + points + ", wins=" + wins + " WHERE uuid='" + uuid + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                }
            }
        }
    }
    
    /**
     * Update loser stats
     * @param p
     */
    public void updateLoserStats(final Player p)
    {
        final String uuid = p.getUniqueId().toString();
        if (this.MySQL != null)
        {
            try (Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                updateLosterStats(p, uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (Connection c = this.SQLite.open())
            {
                updateLosterStats(p, uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Update loser stats
     * @param p
     * @param uuid
     * @param c
     * @throws SQLException
     */
    private void updateLosterStats(final Player p, final String uuid, final Connection c) throws SQLException
    {
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (!res3.isBeforeFirst())
                {
                    // there's no such user
                    stmt.executeUpdate("INSERT INTO `" + this.plugin.getName() + "` (`player`, `points`, `wins`, `loses`, `kills`, `deaths`, `uuid`, `gamepoints`) " //$NON-NLS-1$ //$NON-NLS-2$
                            + "VALUES('" + p.getName() + "', 0, 0, 1, 0, 0, '" + uuid + "', 0)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                else
                {
                    res3.next();
                    final int loses = res3.getInt("loses") + 1; //$NON-NLS-1$
                    
                    stmt.executeUpdate("UPDATE " + this.plugin.getName() + " SET loses=" + loses + " WHERE uuid='" + uuid + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
        }
    }
    
    /**
     * Update killer stats
     * @param p
     * @param kills_
     */
    public void updateKillerStats(final Player p, final int kills_)
    {
        final String uuid = p.getUniqueId().toString();
        if (this.MySQL != null)
        {
            try (Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                updateKillerStats(p, kills_, uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (Connection c = this.SQLite.open())
            {
                updateKillerStats(p, kills_, uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Update killer stats
     * @param p
     * @param kills_
     * @param uuid
     * @param c
     * @throws SQLException
     */
    private void updateKillerStats(final Player p, final int kills_, final String uuid, final Connection c) throws SQLException
    {
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (!res3.isBeforeFirst())
                {
                    // there's no such user
                    stmt.executeUpdate("INSERT INTO " + this.plugin.getName() + " (`player`, `points`, `wins`, `loses`, `kills`, `deaths`, `uuid`, `gamepoints`) " //$NON-NLS-1$ //$NON-NLS-2$
                            + "VALUES('" + p.getName() + "', 0, 0, 0, 1, 0, '" + uuid + "', 0)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                else
                {
                    res3.next();
                    final int kills = res3.getInt("kills") + kills_; //$NON-NLS-1$
                    
                    stmt.executeUpdate("UPDATE " + this.plugin.getName() + " SET kills=" + kills + " WHERE uuid='" + uuid + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
        }
    }
    
    /**
     * update killer stats
     * @param p
     * @param deaths_
     */
    public void updateDeathStats(final Player p, final int deaths_)
    {
        final String uuid = p.getUniqueId().toString();
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                updateDeathStats(p, deaths_, uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                updateDeathStats(p, deaths_, uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Update death stats
     * @param p
     * @param deaths_
     * @param uuid
     * @param c
     * @throws SQLException
     */
    private void updateDeathStats(final Player p, final int deaths_, final String uuid, final Connection c) throws SQLException
    {
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (!res3.isBeforeFirst())
                {
                    // there's no such user
                    stmt.executeUpdate("INSERT INTO " + this.plugin.getName() + " (`player`, `points`, `wins`, `loses`, `kills`, `deaths`, `uuid`, `gamepoints`) "  //$NON-NLS-1$//$NON-NLS-2$
                            + "VALUES('" + p.getName() + "', 0, 0, 0, 0, 1, '" + uuid + "', 0)");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                }
                else
                {
                    res3.next();
                    final int deaths = res3.getInt("deaths") + deaths_; //$NON-NLS-1$
                    
                    stmt.executeUpdate("UPDATE " + this.plugin.getName() + " SET deaths=" + Integer.toString(deaths) + " WHERE uuid='" + uuid + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
        }
    }
    
    /**
     * Get points
     * @param p
     * @return points
     */
    public int getPoints(final Player p)
    {
        final String uuid = p.getUniqueId().toString();
        int result = -1;
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                result = getPoints(uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                result = getPoints(uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        return result;
    }

    /**
     * Get points
     * @param uuid
     * @param c
     * @return points
     * @throws SQLException
     */
    private int getPoints(final String uuid, final Connection c) throws SQLException
    {
        int result = -1;
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (res3.isBeforeFirst())
                {
                    res3.next();
                    result = res3.getInt("points"); //$NON-NLS-1$
                }
            }
        }
        return result;
    }
    
    /**
     * Get wins
     * @param p
     * @return wins
     */
    public int getWins(final Player p)
    {
        final String uuid = p.getUniqueId().toString();
        int result = -1;
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                result = getWins(uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                result = getWins(uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        return result;
    }

    /**
     * Get wins
     * @param uuid
     * @param c
     * @return wins
     * @throws SQLException
     */
    private int getWins(final String uuid, final Connection c) throws SQLException
    {
        int result = -1;
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (res3.isBeforeFirst())
                {
                    res3.next();
                    result = res3.getInt("wins"); //$NON-NLS-1$
                }
            }
        }
        return result;
    }
    
    /**
     * Get game points
     * @param p
     * @return wins
     */
    public int getGamePoints(final Player p)
    {
        final String uuid = p.getUniqueId().toString();
        int result = -1;
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                result = getGamePoints(uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                result = getGamePoints(uuid, c);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        return result;
    }

    /**
     * Get game points
     * @param uuid
     * @param c
     * @return wins
     * @throws SQLException
     */
    private int getGamePoints(final String uuid, final Connection c) throws SQLException
    {
        int result = -1;
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (res3.isBeforeFirst())
                {
                    res3.next();
                    result = res3.getInt("gamepoints"); //$NON-NLS-1$
                }
            }
        }
        return result;
    }
    
    /**
     * Set game points
     * @param p
     * @param points
     */
    public void setGamePoints(final Player p, int points)
    {
        final String uuid = p.getUniqueId().toString();
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                setGamePoints(p, c, points);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                setGamePoints(p, c, points);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Set game points
     * @param p
     * @param c
     * @param points
     * @throws SQLException
     */
    private void setGamePoints(final Player p, final Connection c, int points) throws SQLException
    {
        final String uuid = p.getUniqueId().toString();
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (!res3.isBeforeFirst())
                {
                    // there's no such user
                    stmt.executeUpdate("INSERT INTO " + this.plugin.getName() + " (`player`, `points`, `wins`, `loses`, `kills`, `deaths`, `uuid`, `gamepoints`) "  //$NON-NLS-1$//$NON-NLS-2$
                            + "VALUES('" + p.getName() + "', 0, 0, 0, 0, 1, '" + uuid + "', " + points + ")");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
                else
                {
                    stmt.executeUpdate("UPDATE " + this.plugin.getName() + " SET gamepoints=" + points + " WHERE uuid='" + uuid + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
        }
    }
    
    /**
     * Set game points
     * @param p
     * @param points
     */
    public void addGamePoints(final Player p, int points)
    {
        final String uuid = p.getUniqueId().toString();
        if (this.MySQL != null)
        {
            try (final Connection c = this.MySQL.open())
            {
                updateOldFormat(p, uuid, c);
                addGamePoints(p, c, points);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
        else if (this.SQLite != null)
        {
            try (final Connection c = this.SQLite.open())
            {
                addGamePoints(p, c, points);
            }
            catch (final SQLException e)
            {
                MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Set game points
     * @param p
     * @param c
     * @param points
     * @throws SQLException
     */
    private void addGamePoints(final Player p, final Connection c, int points) throws SQLException
    {
        final String uuid = p.getUniqueId().toString();
        try (final Statement stmt = c.createStatement())
        {
            try (final ResultSet res3 = stmt.executeQuery("SELECT * FROM " + this.plugin.getName() + " WHERE uuid='" + uuid + "'")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                if (!res3.isBeforeFirst())
                {
                    // there's no such user
                    stmt.executeUpdate("INSERT INTO " + this.plugin.getName() + " (`player`, `points`, `wins`, `loses`, `kills`, `deaths`, `uuid`, `gamepoints`) "  //$NON-NLS-1$//$NON-NLS-2$
                            + "VALUES('" + p.getName() + "', 0, 0, 0, 0, 1, '" + uuid + "', " + points + ")");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
                else
                {
                    stmt.executeUpdate("UPDATE " + this.plugin.getName() + " SET gamepoints=" + (points + res3.getInt("gamepoints") ) + " WHERE uuid='" + uuid + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                }
            }
        }
    }
    
}
