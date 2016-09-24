package ca.yum.yum.yelp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import ca.yum.yum.model.AccessToken;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by nijan.
 */

public class YelpOAuth {
	private static final String CLIENT_ID = "5t2Ok23jYsblMdAdYb_QTA";
	private static final String CLIENT_SECRET = "3R2YaFSD6OjB0RCZxhDnKwUwFntdFaMUQhjvzZ1M4StJIBWbfgt2GjivOfV3flDP";

	private OkHttpClient httpClient;
	private ObjectMapper objectMapper;
	private AccessToken accessToken;

	public YelpOAuth(OkHttpClient httpClient, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.httpClient = httpClient;
	}

	public boolean authenticate() throws IOException {
		RequestBody requestBody = new FormBody.Builder()
				.add("grant_type", "client_credentials")
				.add("client_id", CLIENT_ID)
				.add("client_secret", CLIENT_SECRET)
				.build();
		Request request = new Request.Builder()
				.url("https://api.yelp.com/oauth2/token")
				.post(requestBody)
				.build();
		Response response = httpClient.newCall(request).execute();
		if(response.isSuccessful()) {
			storeAccessToken(response);
			return true;
		} else {
			return false;
		}
	}

	private void storeAccessToken(Response response) throws IOException {
		String accessTokenString = response.body().string();
		accessToken = objectMapper.readValue(accessTokenString, AccessToken.class);
		long expires = System.currentTimeMillis() + accessToken.getExpiresIn() * 1000;
		accessToken.setExpiresAbsolute(expires);
	}

	void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public boolean isValid() {
		return accessToken != null && accessToken.getExpiresAbsolute() > System.currentTimeMillis();
	}
}
