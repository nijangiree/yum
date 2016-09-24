package ca.yum.yum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by nijan.
 */

public class User {
	String name;
	@JsonProperty("image_url")
	String imageUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
