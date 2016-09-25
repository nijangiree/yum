package ca.yum.yum.businesses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ca.yum.yum.YumApplication;
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

public class DataFragment extends BaseDataFragment implements SearchTask.SearchCompleteListener {

	public interface FetchSearchDataListener {
		void onComplete();
	}

	CategorizedBusinesses categorizedBusinesses;
	FetchSearchDataListener fetchSearchDataListener;
	SearchTask searchTask;
	SearchOptions lastQuery;

	@Override
	public void initialize(Context context) {
		super.initialize(context);
		categorizedBusinesses = new CategorizedBusinesses();
	}

	public CategorizedBusinesses getCategorizedBusinesses() {
		return categorizedBusinesses;
	}

	public void setCategorizedBusinesses(CategorizedBusinesses categorizedBusinesses) {
		this.categorizedBusinesses = categorizedBusinesses;
	}

	public SearchOptions getLastQuery() {
		return lastQuery;
	}

	public FetchSearchDataListener getFetchSearchDataListener() {
		return fetchSearchDataListener;
	}

	public void setFetchSearchDataListener(FetchSearchDataListener fetchSearchDataListener) {
		this.fetchSearchDataListener = fetchSearchDataListener;
	}

	@Override
	public void onSearchComplete(List<BusinessWithReviews> businessWithReviewsList) {
		searchTask = null;
		categorizedBusinesses.clear();
		for(BusinessWithReviews businessWithReviews : businessWithReviewsList) {
			categorizedBusinesses.add(businessWithReviews);
		}
		if(fetchSearchDataListener != null) {
			fetchSearchDataListener.onComplete();
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

	public boolean isFetching() {
		return searchTask != null;
	}
}
