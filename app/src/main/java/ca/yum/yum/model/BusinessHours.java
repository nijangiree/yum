package ca.yum.yum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nijan.
 */

public class BusinessHours {

	@JsonProperty("is_open_now")
	private boolean isOpenNow;
	@JsonProperty("hours_type")
	private String hoursType;
	private List<Hours> open = new ArrayList<>();

	public boolean isOpenNow() {
		return isOpenNow;
	}

	public void setOpenNow(boolean openNow) {
		isOpenNow = openNow;
	}

	public String getHoursType() {
		return hoursType;
	}

	public void setHoursType(String hoursType) {
		this.hoursType = hoursType;
	}

	public List<Hours> getOpen() {
		return open;
	}

	public void setOpen(List<Hours> open) {
		this.open = open;
	}
}
