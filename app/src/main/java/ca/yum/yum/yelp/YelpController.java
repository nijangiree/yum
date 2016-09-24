package ca.yum.yum.yelp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.yum.yum.model.AccessToken;
import ca.yum.yum.model.Business;
import ca.yum.yum.model.Review;
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
		public enum SortBy {
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

		public SearchOptions setSortBy(SortBy sortBy) {
			this.sortBy = sortBy;
			return this;
		}

		public SearchOptions setLimit(int limit) {
			this.limit = limit;
			return this;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(obj instanceof SearchOptions) {
				SearchOptions ops = (SearchOptions) obj;
				return searchTerm.equals(ops.searchTerm)
						&& location.equals(ops.location)
						&& limit == ops.limit
						&& sortBy == ops.sortBy;
			}
			return super.equals(obj);
		}
	}

	public YelpController(OkHttpClient client, ObjectMapper objectMapper, YelpOAuth yelpOAuth) {
		this.objectMapper = objectMapper;
		this.client = client;
		this.yelpOAuth = yelpOAuth;
	}

	public List<Business> fetchBusinesses(SearchOptions searchOptions) throws IOException {
		Request.Builder builder = preFetch();
		HttpUrl httpUrl = asSearchUrl(createBaseHttpUrl(), searchOptions);
		Request request = builder.url(httpUrl).get().build();
		Response response = client.newCall(request).execute();
		BusinessSearchResult result = objectMapper.readValue(response.body().byteStream(), BusinessSearchResult.class);
		return result.businesses;
	}

	public List<Review> fetchReviews(String businessId) throws IOException {
		Request.Builder builder = preFetch();
		HttpUrl httpUrl = asReviewUrl(createBaseHttpUrl(), businessId);
		Request request = builder.url(httpUrl).get().build();
		Response response = client.newCall(request).execute();
		BusinessReviewsResult result = objectMapper.readValue(response.body().byteStream(), BusinessReviewsResult.class);
		return result.getReviews();
	}

	private Request.Builder preFetch() throws IOException {
		confirmAuthenticate();
		Request.Builder builder = new Request.Builder();
		addAuth(builder);
		return builder;
	}

	private void confirmAuthenticate() throws IOException {
		if(!yelpOAuth.isValid()) {
			yelpOAuth.authenticate();
		}
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
		builder.addPathSegments("businesses/search")
				.addEncodedQueryParameter("term", searchOptions.searchTerm)
				.addEncodedQueryParameter("location", searchOptions.location)
				.addEncodedQueryParameter("sort_by", searchOptions.sortBy.value)
				.addQueryParameter("limit", String.valueOf(searchOptions.limit));
		return builder.build();
	}

	private HttpUrl asReviewUrl(HttpUrl.Builder builder, String businessId) {
		builder.addPathSegment("businesses")
				.addPathSegment(businessId)
				.addPathSegment("reviews");
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

	static class BusinessReviewsResult {
		int total;
		List<Review> reviews = new ArrayList<>();

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<Review> getReviews() {
			return reviews;
		}

		public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
		}
	}
}
