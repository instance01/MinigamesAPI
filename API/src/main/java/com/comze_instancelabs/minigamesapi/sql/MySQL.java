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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class MySQL
{
    String     user     = "";
    String     database = "";
    String     password = "";
    String     port     = "";
    String     hostname = "";
    Connection c        = null;
    
    public MySQL(final String hostname, final String portnmbr, final String database, final String username, final String password)
    {
        this.hostname = hostname;
        this.port = portnmbr;
        this.database = database;
        this.user = username;
        this.password = password;
    } 
    
    public Connection open()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            this.c = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
            return this.c;
        }
        catch (final SQLException e)
        {
            MinigamesAPI.getAPI().getLogger().log(Level.SEVERE, "Could not connect to MySQL server!", e);
        }
        catch (final ClassNotFoundException e)
        {
            MinigamesAPI.getAPI().getLogger().severe("JDBC Driver not found!");
        }
        return this.c;
    }
    
}
