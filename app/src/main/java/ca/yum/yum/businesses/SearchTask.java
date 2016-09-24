package ca.yum.yum.businesses;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ca.yum.yum.model.Business;
import ca.yum.yum.model.BusinessWithReviews;
import ca.yum.yum.model.CategorizedBusinesses;
import ca.yum.yum.model.Review;
import ca.yum.yum.yelp.YelpController;

/**
 * Created by nijan.
 */

public class SearchTask extends AsyncTask<YelpController.SearchOptions, Void, List<BusinessWithReviews>> {

	public interface SearchCompleteListener {
		void onSearchComplete(List<BusinessWithReviews> businessWithReviewsList);
	}

	WeakReference<SearchCompleteListener> searchCompleteListener;
	YelpController yelpController;
	public SearchTask(YelpController yelpController, SearchCompleteListener searchCompleteListener) {
		this.yelpController = yelpController;
		this.searchCompleteListener = new WeakReference<>(searchCompleteListener);
	}

	@Override
	protected List<BusinessWithReviews> doInBackground(YelpController.SearchOptions... params) {
		List<BusinessWithReviews> businessWithReviewsList = new ArrayList<>();
		YelpController.SearchOptions searchOptions = params[0];
		try {
			List<Business> businesses = yelpController.fetchBusinesses(searchOptions);
			for(Business business : businesses) {
				if(isCancelled()) break;
				List<Review> reviews = yelpController.fetchReviews(business.getId());
				BusinessWithReviews businessWithReviews = new BusinessWithReviews();
				businessWithReviews.setBusiness(business);
				businessWithReviews.setReviews(reviews);
				Log.e("SearchTask", "gotBusiness: " + businessWithReviews.getBusiness().getId());
				businessWithReviewsList.add(businessWithReviews);
			}
		} catch (IOException e) {
			Log.e("SearchTask", "Error fetching search data", e);
		}
		return businessWithReviewsList;
	}

	@Override
	protected void onPostExecute(List<BusinessWithReviews> result) {
		if(searchCompleteListener.get() != null) {
			searchCompleteListener.get().onSearchComplete(result);
		}
	}
}
