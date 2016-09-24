package ca.yum.yum.model;

import java.util.List;

/**
 * Created by nijan.
 */

public class BusinessWithReviews {
	Business business;
	List<Review> reviews;

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
