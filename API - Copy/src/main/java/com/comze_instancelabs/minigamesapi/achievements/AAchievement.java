package com.comze_instancelabs.minigamesapi.achievements;

public class AAchievement {

	String name;
	boolean done;
	String playername;

	public AAchievement(String name, String playername, boolean done) {
		this.name = name;
		this.playername = playername;
		this.done = done;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean t) {
		this.done = t;
	}

	public String getAchievementNameRaw() {
		return name;
	}
}
