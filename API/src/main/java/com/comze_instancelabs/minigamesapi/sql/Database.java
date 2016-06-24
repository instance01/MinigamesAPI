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
package com.comze_instancelabs.minigamesapi.sql; // replace this with yours

import java.sql.Connection;

public abstract class Database
{
    protected boolean    connected;
    protected Connection connection;
    public int           lastUpdate;
    
    public Database()
    {
        this.connected = false;
        this.connection = null;
    }
    
    protected Statements getStatement(final String query)
    {
        final String trimmedQuery = query.trim();
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("SELECT"))
        {
            return Statements.SELECT;
        }
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("INSERT"))
        {
            return Statements.INSERT;
        }
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("UPDATE"))
        {
            return Statements.UPDATE;
        }
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("DELETE"))
        {
            return Statements.DELETE;
        }
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("CREATE"))
        {
            return Statements.CREATE;
        }
        if (trimmedQuery.substring(0, 5).equalsIgnoreCase("ALTER"))
        {
            return Statements.ALTER;
        }
        if (trimmedQuery.substring(0, 4).equalsIgnoreCase("DROP"))
        {
            return Statements.DROP;
        }
        if (trimmedQuery.substring(0, 8).equalsIgnoreCase("TRUNCATE"))
        {
            return Statements.TRUNCATE;
        }
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("RENAME"))
        {
            return Statements.RENAME;
        }
        if (trimmedQuery.substring(0, 2).equalsIgnoreCase("DO"))
        {
            return Statements.DO;
        }
        if (trimmedQuery.substring(0, 7).equalsIgnoreCase("REPLACE"))
        {
            return Statements.REPLACE;
        }
        if (trimmedQuery.substring(0, 4).equalsIgnoreCase("LOAD"))
        {
            return Statements.LOAD;
        }
        if (trimmedQuery.substring(0, 7).equalsIgnoreCase("HANDLER"))
        {
            return Statements.HANDLER;
        }
        if (trimmedQuery.substring(0, 4).equalsIgnoreCase("CALL"))
        {
            return Statements.CALL;
        }
        return Statements.SELECT;
    }
    
    protected static enum Statements
    {
        SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, CREATE, ALTER, DROP, TRUNCATE, RENAME, START, COMMIT, ROLLBACK, SAVEPOINT, LOCK, UNLOCK, PREPARE, EXECUTE, DEALLOCATE, SET, SHOW, DESCRIBE, EXPLAIN, HELP, USE, ANALYZE, ATTACH, BEGIN, DETACH, END, INDEXED, ON, PRAGMA, REINDEX, RELEASE, VACUUM;
    }
}
