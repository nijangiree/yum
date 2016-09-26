package ca.yum.yum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nijan.
 */

public class BusinessWithReviews {
	private Business business;
	private List<Review> reviews = new ArrayList<>();

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}
