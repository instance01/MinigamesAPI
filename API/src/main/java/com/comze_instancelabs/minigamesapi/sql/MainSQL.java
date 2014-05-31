package com.comze_instancelabs.minigamesapi.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

public class MainSQL {

	// used for rewards and stats
	
	JavaPlugin plugin = null;
	private boolean mysql = true; // false for sqlite
	MySQL MySQL;
	
	public MainSQL(JavaPlugin plugin, boolean mysql){
		this.plugin = plugin;
		this.mysql = mysql;
		
		if(mysql){
			//TODO add into default config
			MySQL = new MySQL(plugin.getConfig().getString("mysql.host"), "3306", plugin.getConfig().getString("mysql.database"), plugin.getConfig().getString("mysql.user"), plugin.getConfig().getString("mysql.pw"));
		}
	}
	
	public void createTables(){
		//TODO auto create tables
	}
	
	public void updateWinnerStatsMySQL(String p_, int reward){
		if(!plugin.getConfig().getBoolean("mysql.enabled")){
			return;
		}
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM minigames WHERE player='" + p_ + "'");
			if(!res3.isBeforeFirst()){
				// there's no such user
				c.createStatement().executeUpdate("INSERT INTO minigames VALUES('0', '" + p_ + "', '" + Integer.toString(reward) + "', '1')");
				return;
			}
			res3.next();
			int credits = res3.getInt("credits") + reward;
			int wins = res3.getInt("wins") + 1;
			
			c.createStatement().executeUpdate("UPDATE minigames SET credits='" + Integer.toString(credits) + "', wins='" + Integer.toString(wins) + "' WHERE player='" + p_ + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public int getCreditsMySQL(String p_){
		if(!plugin.getConfig().getBoolean("mysql.enabled")){
			return -1;
		}
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM minigames WHERE player='" + p_ + "'");

			/*if(res3.next()){
				int credits = res3.getInt("credits");
				return credits;
			}
			return -1;*/
			
			res3.next();
			int credits = res3.getInt("credits");
			return credits;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("New User detected.");
		}
		return -1;
	}
	
	public int getWinsMySQL(String p_){
		if(!plugin.getConfig().getBoolean("mysql.enabled")){
			return -1;
		}
    	Connection c = null;
    	c = MySQL.open();
		
		try {
			ResultSet res3 = c.createStatement().executeQuery("SELECT * FROM minigames WHERE player='" + p_ + "'");

			res3.next();
			int wins = res3.getInt("wins");
			return wins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
