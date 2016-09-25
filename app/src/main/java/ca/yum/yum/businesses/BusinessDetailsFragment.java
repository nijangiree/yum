package ca.yum.yum.businesses;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ca.yum.yum.model.BusinessWithReviews;

/**
 * Created by nijan.
 */

public class BusinessDetailsFragment extends BaseDataFragment
		implements BusinessDetailsTask.BusinessDetailsCompleteListener {

	public interface FetchBusinessDetailsListener {
		void onComplete(BusinessWithReviews businessWithReviews);
	}

	FetchBusinessDetailsListener fetchBusinessDetailsListener;
	BusinessWithReviews businessWithReviews;
	BusinessDetailsTask businessDetailsTask;
	String lastBusinessId;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public FetchBusinessDetailsListener getFetchBusinessDetailsListener() {
		return fetchBusinessDetailsListener;
	}

	public void setFetchBusinessDetailsListener(FetchBusinessDetailsListener fetchBusinessDetailsListener) {
		this.fetchBusinessDetailsListener = fetchBusinessDetailsListener;
	}

	public BusinessWithReviews getBusinessWithReviews() {
		return businessWithReviews;
	}

	public void setBusinessWithReviews(BusinessWithReviews businessWithReviews) {
		this.businessWithReviews = businessWithReviews;
	}

	public void beginFetchingBusinessDetails(String businessId) {
		if(businessDetailsTask != null) {
			businessDetailsTask.cancel(true);
		}
		businessDetailsTask = new BusinessDetailsTask(yelpController, this);
		businessDetailsTask.execute(businessId);
		lastBusinessId = businessId;
	}

	@Override
	public void onComplete(BusinessWithReviews businessWithReviews) {
		businessDetailsTask = null;
		this.businessWithReviews = businessWithReviews;
		if(fetchBusinessDetailsListener != null) {
			fetchBusinessDetailsListener.onComplete(businessWithReviews);
		}
	}

	public boolean isFetching() {
		return businessDetailsTask != null;
	}

	public String getLastBusinessId() {
		return lastBusinessId;
	}
}
