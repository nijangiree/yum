package ca.yum.yum.yelp;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ca.yum.yum.model.AccessToken;
import ca.yum.yum.model.Business;
import ca.yum.yum.model.Review;
import okhttp3.OkHttpClient;

import static org.junit.Assert.*;

/**
 * Created by nijan.
 */
public class YelpControllerTest {

	ObjectMapper objectMapper;
	OkHttpClient client;
	YelpOAuth oAuth;

	@Before
	public void setUp() throws Exception {
		objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
		client = new OkHttpClient();
		oAuth = new YelpOAuth(client, objectMapper);
		oAuth.setAccessToken(new AccessToken());
		oAuth.getAccessToken().setExpiresIn(100000);
		oAuth.getAccessToken().setExpiresAbsolute(System.currentTimeMillis() + 1000000 * 1000);
		oAuth.getAccessToken().setTokenType("Bearer");
		oAuth.getAccessToken().setAccessToken("NAs6J2EX6HhBY9E66WFKKi1LRl5FkUmj5Le2GU8bwQ8cpFb9JSbmYs2rqphJXXq6Iqz-uvb8iyxZWWLwg011T7Bs5_kaeDbjXYN6L68CnZTE45q95dDTB6WbOqHlV3Yx");
	}

	@Test
	public void testFetchBusinesses() throws IOException {
		YelpController controller = new YelpController(client, objectMapper, oAuth);
		YelpController.SearchOptions searchOptions = new YelpController.SearchOptions();
		searchOptions
				.setLocation("Toronto")
				.setSearchTerm("Ethiopian")
				.setLimit(10)
				.setSortBy(YelpController.SearchOptions.SortBy.SORT_BEST_MATCH);
		List<Business> businesses = controller.fetchBusinesses(searchOptions);
		for(Business b : businesses) {
			System.out.println(b.getId());
		}
		assertFalse(businesses.isEmpty());
		assertTrue(businesses.size() <= 10);
	}

	@Test
	public void testFetchBusinessesEmpty() throws IOException {
		YelpController controller = new YelpController(client, objectMapper, oAuth);
		YelpController.SearchOptions searchOptions = new YelpController.SearchOptions();
		searchOptions
				.setLocation("Toronto")
				.setSearchTerm("asdfasdf")
				.setLimit(10)
				.setSortBy(YelpController.SearchOptions.SortBy.SORT_BEST_MATCH);
		List<Business> businesses = controller.fetchBusinesses(searchOptions);
		assertTrue(businesses.isEmpty());
	}

	@Test
	public void testFetchReviews() throws IOException {
		YelpController controller = new YelpController(client, objectMapper, oAuth);
		List<Review> reviews = controller.fetchReviews("nazareth-restaurant-toronto");
		assertTrue(reviews.size() == 3); // the max reviews allowed by the api
	}

}