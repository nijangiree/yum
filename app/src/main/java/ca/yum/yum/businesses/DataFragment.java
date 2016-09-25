package ca.yum.yum.businesses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ca.yum.yum.model.BusinessWithReviews;
import ca.yum.yum.model.CategorizedBusinesses;
import ca.yum.yum.yelp.SearchOptions;
import ca.yum.yum.yelp.YelpController;
import ca.yum.yum.yelp.YelpOAuth;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by nijan.
 */

public class DataFragment extends Fragment implements SearchTask.SearchCompleteListener {

	public interface FetchDataListener {
		void onComplete();
	}

	ObjectMapper objectMapper;
	OkHttpClient httpClient;
	CategorizedBusinesses categorizedBusinesses;
	FetchDataListener fetchDataListener;
	YelpController yelpController;
	SearchTask searchTask;
	SearchOptions lastQuery;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public void initialize(Context context) {
		objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
		Cache cache = new Cache(context.getCacheDir(), 20 * 1024 * 1024);
		httpClient = new OkHttpClient.Builder().cache(cache).build();
		categorizedBusinesses = new CategorizedBusinesses();
		YelpOAuth yelpOAuth = new YelpOAuth(httpClient, objectMapper);
		yelpController = new YelpController(httpClient, objectMapper, yelpOAuth);
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public OkHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(OkHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public CategorizedBusinesses getCategorizedBusinesses() {
		return categorizedBusinesses;
	}

	public void setCategorizedBusinesses(CategorizedBusinesses categorizedBusinesses) {
		this.categorizedBusinesses = categorizedBusinesses;
	}

	public YelpController getYelpController() {
		return yelpController;
	}

	public void setYelpController(YelpController yelpController) {
		this.yelpController = yelpController;
	}

	public SearchOptions getLastQuery() {
		return lastQuery;
	}

	public FetchDataListener getFetchDataListener() {
		return fetchDataListener;
	}

	public void setFetchDataListener(FetchDataListener fetchDataListener) {
		this.fetchDataListener = fetchDataListener;
	}

	@Override
	public void onSearchComplete(List<BusinessWithReviews> businessWithReviewsList) {
		categorizedBusinesses.clear();
		for(BusinessWithReviews businessWithReviews : businessWithReviewsList) {
			categorizedBusinesses.add(businessWithReviews);
			Log.e("DataFragment", "onSearchComplete: " + businessWithReviews.getBusiness().getId());
		}
		String s = "";
		CategorizedBusinesses cb = categorizedBusinesses;
		for(int i = 0; i < cb.getTotal(); i++) {
			if(cb.isCategory(i)) {
				s += "Cat: " + cb.getCategory(i);
			} else {
				s += "Bus: " + cb.getBusinessWithReviews(i).getBusiness().getId();
			}
			s += "\n";
		}
		Log.w("DataFragment", "preOnComplete: " + s);
		if(fetchDataListener != null) {
			fetchDataListener.onComplete();
		}
	}

	public void beginSearch(SearchOptions searchOptions) {
		lastQuery = searchOptions;

		if(searchTask != null) {
			searchTask.cancel(true);
		}
		searchTask = new SearchTask(yelpController, this);
		searchTask.execute(searchOptions);
	}
}
