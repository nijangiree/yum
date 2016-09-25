package ca.yum.yum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;

/**
 * Created by nijan.
 */
public class Hours {

	public static final Comparator<Hours> DAY_COMPARATOR = new Comparator<Hours>() {
		@Override
		public int compare(Hours o1, Hours o2) {
			int day1 = o1.getDay();
			int day2 = o2.getDay();
			return (day1 < day2) ? -1 : ((day1 == day2) ? 0 : 1);
		}
	};

	int day;
	String start;
	String end;
	@JsonProperty("is_overnight")
	boolean isOverNight;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isOverNight() {
		return isOverNight;
	}

	public void setOverNight(boolean overNight) {
		isOverNight = overNight;
	}
}
