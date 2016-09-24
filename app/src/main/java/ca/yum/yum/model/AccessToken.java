package ca.yum.yum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by nijan.
 */

public class AccessToken {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("expires_in")
	private int expiresIn;
	private long expiresAbsolute;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public long getExpiresAbsolute() {
		return expiresAbsolute;
	}

	public void setExpiresAbsolute(long expiresAbsolute) {
		this.expiresAbsolute = expiresAbsolute;
	}
}
