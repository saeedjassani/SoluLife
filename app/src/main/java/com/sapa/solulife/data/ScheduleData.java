package com.sapa.solulife.data;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class ScheduleData {

	private String category, desc, qty;
	private String start_time;

	public ScheduleData() {
	}

	public ScheduleData(String type, String description, String quantity, String start_time) {
		this.category = type;
		this.desc = description;
		this.qty = quantity;
		this.start_time = start_time;
	}

	public String getType() {
		return category;
	}

	public void setType(String type) {
		this.category = type;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String description) {
		this.desc = description;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
}
