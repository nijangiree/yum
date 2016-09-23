package ca.yum.yum.yelp;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.yum.yum.model.AccessToken;
import okhttp3.OkHttpClient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by nijan.
 */
public class YelpOAuthTest {

	private OkHttpClient client;
	private ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testAuthenticate() throws Exception {
		YelpOAuth auth = new YelpOAuth(client, objectMapper);
		boolean success = auth.authenticate();
		assertTrue("Failed to authenticate", success);
	}

	@Test
	public void testAccessToken() throws Exception {
		YelpOAuth auth = new YelpOAuth(client, objectMapper);
		auth.authenticate();
		AccessToken accessToken = auth.getAccessToken();
		assertNotNull(accessToken);
		assertThat(accessToken.getTokenType(), is("Bearer"));
		assertThat(accessToken.getAccessToken(), notNullValue());
		assertTrue(accessToken.getExpiresIn() > 0);
	}

}