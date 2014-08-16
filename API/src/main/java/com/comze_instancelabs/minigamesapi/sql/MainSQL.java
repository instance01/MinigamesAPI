package com.comze_instancelabs.minigamesapi.sql;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainSQL {

	// used for rewards and stats

	JavaPlugin plugin = null;
	private boolean mysql = true; // false for sqlite
	MySQL MySQL;
	SQLite SQLite;

	public MainSQL(JavaPlugin plugin, boolean mysql) {
		this.plugin = plugin;
		this.mysql = mysql;

		if (mysql) {
			MySQL = new MySQL(plugin.getConfig().getString("mysql.host"), "3306", plugin.getConfig().getString("mysql.database"), plugin.getConfig().getString("mysql.user"), plugin.getConfig().getString("mysql.pw"));
		} else {
			SQLite = new SQLite(plugin.getConfig().getString("mysql.database"), plugin.getConfig().getString("mysql.user"), plugin.getConfig().getString("mysql.pw"));
		}

		if (plugin.getConfig().getBoolean("mysql.enabled") && MySQL != null) {
			this.createTables();
		} else if (plugin.getConfig().getBoolean("mysql.enabled") && MySQL == null) {
			System.out.println("Failed initializing MySQL. Disabling!");
			plugin.getConfig().set("mysql.enabled", false);
			plugin.saveConfig();
		}
	}

	public void createTables() {
		// TODO auto create tables
		if (!plugin.getConfig().getBoolean("mysql.enabled")) {
			return;
		}
		if (!mysql) {
			// TODO SQLite
		}
		Connection c = MySQL.open();

		try {
			c.createStatement().execute("CREATE DATABASE IF NOT EXISTS " + plugin.getConfig().getString("mysql.database"));
			// ResultSet res = c.createStatement().executeQuery("SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_SCHEMA = '" +
			// plugin.getConfig().getString("mysql.database") + "') AND (TABLE_NAME = '" + plugin.getName() + "')");
			// if(!res.isBeforeFirst()){
			// table doesn't exist, let's create it:
			c.createStatement().execute("CREATE TABLE IF NOT EXISTS " + plugin.getName() + " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, player VARCHAR(100), points INT, wins INT, loses INT)");
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateWinnerStats(String p_, int reward) {
		if (!plugin.getConfig().getBoolean("mysql.enabled")) {
			return;
		}
		if (!mysql) {
			// TODO SQLite
		}
		Connection c = MySQL.open();

		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + plugin.getName() + " WHERE player='" + p_ + "'");
			if (!res3.isBeforeFirst()) {
				// there's no such user
				c.createStatement().executeUpdate("INSERT INTO " + plugin.getName() + " VALUES('0', '" + p_ + "', '" + Integer.toString(reward) + "', '1', '0')");
				return;
			}
			res3.next();
			int points = res3.getInt("points") + reward;
			int wins = res3.getInt("wins") + 1;

			c.createStatement().executeUpdate("UPDATE " + plugin.getName() + " SET points='" + Integer.toString(points) + "', wins='" + Integer.toString(wins) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateLoserStats(String p_) {
		if (!plugin.getConfig().getBoolean("mysql.enabled")) {
			return;
		}
		if (!mysql) {
			// TODO SQLite
		}
		Connection c = MySQL.open();

		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + plugin.getName() + " WHERE player='" + p_ + "'");
			if (!res3.isBeforeFirst()) {
				// there's no such user
				c.createStatement().executeUpdate("INSERT INTO " + plugin.getName() + " VALUES('0', '" + p_ + "', '0', '0', '1')");
				return;
			}
			res3.next();
			int loses = res3.getInt("loses") + 1;

			c.createStatement().executeUpdate("UPDATE " + plugin.getName() + " SET loses='" + Integer.toString(loses) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getPoints(String p_) {
		if (!plugin.getConfig().getBoolean("mysql.enabled")) {
			return -1;
		}
		if (!mysql) {
			// TODO SQLite
		}
		Connection c = MySQL.open();

		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + plugin.getName() + " WHERE player='" + p_ + "'");

			/*
			 * if(res3.next()){ int points = res3.getInt("points"); return points; } return -1;
			 */

			res3.next();
			int credits = res3.getInt("points");
			return credits;
		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("New User detected.");
		}
		return -1;
	}

	public int getWins(String p_) {
		if (!plugin.getConfig().getBoolean("mysql.enabled")) {
			return -1;
		}
		if (!mysql) {
			// TODO SQLite
		}
		Connection c = MySQL.open();

		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM " + plugin.getName() + " WHERE player='" + p_ + "'");

			res3.next();
			int wins = res3.getInt("wins");
			return wins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
