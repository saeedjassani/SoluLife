package com.sapa.solulife.data;

import java.util.Date;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class ScheduleData {

	private String type, description, quantity;
	private Date startDate;

	public ScheduleData() {
	}

	public ScheduleData(String type, String description, String quantity, Date startDate) {
		this.type = type;
		this.description = description;
		this.quantity = quantity;
		this.startDate = startDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
