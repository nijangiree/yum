package ca.yum.yum.businesses;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import ca.yum.yum.model.Business;
import ca.yum.yum.model.BusinessWithReviews;
import ca.yum.yum.model.Review;
import ca.yum.yum.yelp.YelpController;

/**
 * Created by nijan.
 */

public class BusinessDetailsTask extends AsyncTask<String, Void, BusinessWithReviews> {

	public interface BusinessDetailsCompleteListener {
		void onComplete(BusinessWithReviews businessWithReviews);
	}

	private WeakReference<BusinessDetailsCompleteListener> listener;
	private YelpController yelpController;
	public BusinessDetailsTask(YelpController yelpController, BusinessDetailsCompleteListener listener) {
		this.yelpController = yelpController;
		this.listener = new WeakReference<>(listener);
	}

	@Override
	protected BusinessWithReviews doInBackground(String... params) {
		String businessId = params[0];
		try {
			Business business = yelpController.fetchBusinessWithDetails(businessId);
			if(isCancelled()) return null;
			List<Review> reviews = yelpController.fetchReviews(businessId);
			BusinessWithReviews businessWithReviews = new BusinessWithReviews();
			businessWithReviews.setBusiness(business);
			businessWithReviews.setReviews(reviews);
			return businessWithReviews;
		} catch (IOException e) {
			Log.e("BusinessDetailsTask", "Error fetching data", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(BusinessWithReviews result) {
		if(listener.get() != null) {
			listener.get().onComplete(result);
		}
	}
}
