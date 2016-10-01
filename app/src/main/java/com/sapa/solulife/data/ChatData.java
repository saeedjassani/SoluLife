package com.sapa.solulife.data;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class ChatData {

	private int user_id;
	private String message;
	private int place;

	public ChatData() {
	}

	public ChatData(int user_id, String message, int place) {
		this.user_id = user_id;
		this.message = message;
		this.place = place;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
