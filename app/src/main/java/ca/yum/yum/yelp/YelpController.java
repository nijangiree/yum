package ca.yum.yum.yelp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.yum.yum.model.AccessToken;
import ca.yum.yum.model.Business;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nijan.
 */

public class YelpController {
	ObjectMapper objectMapper;
	OkHttpClient client;
	YelpOAuth yelpOAuth;

	public static class SearchOptions {
		enum SortBy {
			SORT_BEST_MATCH("best_match"),
			SORT_RATING("rating"),
			SORT_REVIEW_COUNT("review_count"),
			SORT_DISTANCE("distance");
			String value;
			SortBy(String value) {
				this.value = value;
			}
		}

		String searchTerm;
		String location;
		String country;
		int limit;
		SortBy sortBy = SortBy.SORT_BEST_MATCH;

		public SearchOptions setSearchTerm(String searchTerm) {
			this.searchTerm = searchTerm;
			return this;
		}

		public SearchOptions setLocation(String location) {
			this.location = location;
			return this;
		}

		public SearchOptions setCountry(String country) {
			this.country = country;
			return this;
		}

		public SearchOptions setSortBy(SortBy sortBy) {
			this.sortBy = sortBy;
			return this;
		}

		public SearchOptions setLimit(int limit) {
			this.limit = limit;
			return this;
		}
	}

	public YelpController(OkHttpClient client, ObjectMapper objectMapper, YelpOAuth yelpOAuth) {
		this.objectMapper = objectMapper;
		this.client = client;
		this.yelpOAuth = yelpOAuth;
	}

	public List<Business> fetchBusinesses(SearchOptions searchOptions) throws IOException {
		if(!yelpOAuth.isValid()) {
			yelpOAuth.authenticate();
		}

		Request.Builder builder = new Request.Builder();
		addAuth(builder);
		HttpUrl httpUrl = asSearchUrl(createBaseHttpUrl(), searchOptions);
		Request request = builder.url(httpUrl).get().build();
		Response response = client.newCall(request).execute();
		BusinessSearchResult result = objectMapper.readValue(response.body().byteStream(), BusinessSearchResult.class);
		return result.businesses;
	}

	private Request.Builder addAuth(Request.Builder builder) {
		AccessToken token = yelpOAuth.getAccessToken();
		return builder.header("Authorization", token.getTokenType() + " " + token.getAccessToken());
	}

	private HttpUrl.Builder createBaseHttpUrl() {
		return new HttpUrl.Builder()
				.scheme("https")
				.host("api.yelp.com")
				.addPathSegment("v3");
	}

	private HttpUrl asSearchUrl(HttpUrl.Builder builder, SearchOptions searchOptions) {
		builder
				.addPathSegments("businesses/search")
				.addEncodedQueryParameter("term", searchOptions.searchTerm)
				.addEncodedQueryParameter("location", searchOptions.location + "," + searchOptions.country)
				.addEncodedQueryParameter("sort_by", searchOptions.sortBy.value)
				.addQueryParameter("limit", String.valueOf(searchOptions.limit));
		return builder.build();
	}

	static class BusinessSearchResult {
		int total;
		List<Business> businesses = new ArrayList<>();

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<Business> getBusinesses() {
			return businesses;
		}

		public void setBusinesses(List<Business> businesses) {
			this.businesses = businesses;
		}
	}
}
