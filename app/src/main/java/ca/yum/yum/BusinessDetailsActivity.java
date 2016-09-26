package ca.yum.yum;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import ca.yum.yum.businesses.BusinessDetailsFragment;
import ca.yum.yum.details.BusinessImagesAdapter;
import ca.yum.yum.details.BusinessReviewsAdapter;
import ca.yum.yum.model.Business;
import ca.yum.yum.model.BusinessHours;
import ca.yum.yum.model.BusinessWithReviews;
import ca.yum.yum.model.Category;
import ca.yum.yum.model.Coordinates;
import ca.yum.yum.model.Hours;
import ca.yum.yum.model.Review;

public class BusinessDetailsActivity extends AppCompatActivity implements BusinessDetailsFragment.FetchBusinessDetailsListener {

	public static final String INTENT_BUSINESS_DETAILS =
			BusinessDetailsActivity.class.getName() + ".business";

	String businessId;
	BusinessDetailsFragment businessDetailsFragment;
	RatingBar businessRating;
	ProgressBar loadingBar;
	RecyclerView businessImagesRecycler;
	RecyclerView businessReviewsRecycler;
	LinearLayout businessHoursLayout;
	LinearLayout businessCategoriesLayout;
	FloatingActionButton goLocationFab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_details);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		businessRating = (RatingBar) findViewById(R.id.business_rating);
		loadingBar = (ProgressBar) findViewById(R.id.business_progressbar);
		businessImagesRecycler = (RecyclerView) findViewById(R.id.business_images_recycler);
		businessReviewsRecycler = (RecyclerView) findViewById(R.id.business_reviews_recycler);
		businessHoursLayout = (LinearLayout) findViewById(R.id.business_hours);
		businessCategoriesLayout = (LinearLayout) findViewById(R.id.business_categories);
		goLocationFab = (FloatingActionButton) findViewById(R.id.fab_open_location);
		businessImagesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		businessReviewsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		setTitle("");
		initializeDetailsFragment();
		businessDetailsFragment.setFetchBusinessDetailsListener(this);
		handleIntent(getIntent());
		if(businessDetailsFragment.isFetching()) {
			showProgress(true);
		} else {
			onComplete(businessDetailsFragment.getBusinessWithReviews());
		}
	}

	private void handleIntent(Intent intent) {
		businessId = intent.getStringExtra(INTENT_BUSINESS_DETAILS);
		beginFetchingDetails(false);
	}

	private void initializeDetailsFragment() {
		FragmentManager fm = getSupportFragmentManager();
		businessDetailsFragment = (BusinessDetailsFragment) fm.findFragmentByTag("details");
		if(businessDetailsFragment == null) {
			businessDetailsFragment = new BusinessDetailsFragment();
			businessDetailsFragment.initialize(this);
			fm.beginTransaction().add(businessDetailsFragment, "details").commit();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		businessDetailsFragment.setFetchBusinessDetailsListener(null);
	}

	private void beginFetchingDetails(boolean force) {
		if(!force && businessDetailsFragment.getBusinessWithReviews() != null
				&& businessDetailsFragment
				.getBusinessWithReviews()
				.getBusiness()
				.getId()
				.equals(businessId)) {
			return;
		}
		showProgress(true);
		businessDetailsFragment.beginFetchingBusinessDetails(businessId);
	}

	private void openLocation(Coordinates coordinates, String title) {
		try {
			Uri uri = Uri.parse("geo:0,0?q="
					+ coordinates.getLatitude() + ","
					+ coordinates.getLongitude()
					+ "(" + title + ")");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.error_geo_location, Toast.LENGTH_SHORT).show();
		}
	}

	private void showProgress(boolean visible) {
		if(visible) {
			businessRating.setVisibility(View.GONE);
			businessImagesRecycler.setVisibility(View.GONE);
			businessReviewsRecycler.setVisibility(View.GONE);
			businessHoursLayout.setVisibility(View.GONE);
			businessCategoriesLayout.setVisibility(View.GONE);
			goLocationFab.setVisibility(View.GONE);
			loadingBar.setVisibility(View.VISIBLE);
		} else {
			businessRating.setVisibility(View.VISIBLE);
			businessImagesRecycler.setVisibility(View.VISIBLE);
			businessReviewsRecycler.setVisibility(View.VISIBLE);
			businessHoursLayout.setVisibility(View.VISIBLE);
			businessCategoriesLayout.setVisibility(View.VISIBLE);
			loadingBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onComplete(BusinessWithReviews businessWithReviews) {
		Business business = businessWithReviews.getBusiness();
		if(business == null) {
			loadingBar.setVisibility(View.GONE);
			goLocationFab.setVisibility(View.GONE);
			Snackbar.make(businessImagesRecycler,
					getString(R.string.error_fetching),
					Snackbar.LENGTH_INDEFINITE)
					.setAction(getString(R.string.retry),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									beginFetchingDetails(true);
								}
							}).show();
			return;
		}
		showProgress(false);
		setTitle(business.getName());
		businessRating.setRating(business.getRating());
		setupImages(business);
		setupReviews(businessWithReviews.getReviews());
		setupHours(business.getHours());
		setupCategories(business.getCategories());
		final Coordinates coordinates = business.getCoordinates();
		final String title = business.getName();
		if(coordinates != null) {
			goLocationFab.setVisibility(View.VISIBLE);
			goLocationFab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openLocation(coordinates, title);
				}
			});
		}
	}

	private void setupImages(Business business) {
		BusinessImagesAdapter businessImagesAdapter = null;
		if(!business.getPhotos().isEmpty()) {
			businessImagesAdapter = new BusinessImagesAdapter(business.getPhotos());
		}
		businessImagesRecycler.setAdapter(businessImagesAdapter);
	}

	private void setupReviews(List<Review> reviews) {
		BusinessReviewsAdapter businessReviewsAdapter = null;
		if(!reviews.isEmpty()) {
			businessReviewsAdapter = new BusinessReviewsAdapter(reviews);
		}
		businessReviewsRecycler.setAdapter(businessReviewsAdapter);
	}

	private void setupHours(List<BusinessHours> businessHours) {
		businessHoursLayout.removeAllViews();
		if(businessHours.isEmpty()) return;

		List<Hours> hours = businessHours.get(0).getOpen();
		Collections.sort(hours, Hours.DAY_COMPARATOR);

		//Go through the days, for any missing day in the list, mark that day as closed
		int i = 0;
		int j = 0;
		for(;i < 7; i++) {
			if(j >= hours.size()) {
				addHourView(i, null);
			} else {
				Hours hour = hours.get(j);
				if (hour.getDay() == i) {
					addHourView(i, hour);
					j++;
				} else {
					addHourView(i, null);
				}
			}
		}
	}

	private void addHourView(int day, Hours hours) {
		View hoursView = getLayoutInflater().inflate(R.layout.business_hours, businessHoursLayout, false);
		TextView dayView = (TextView) hoursView.findViewById(R.id.day);
		TextView details = (TextView) hoursView.findViewById(R.id.details);
		dayView.setText(getDayString(day));
		if(hours == null) {
			details.setText(getText(R.string.closed));
		} else {
			//todo as AM PM
			String start = timeToAMPM(hours.getStart());
			String end = timeToAMPM(hours.getEnd());
			details.setText(start + " - " + end);
		}
		if(day % 2 == 0) {
			hoursView.setBackgroundColor(getResources().getColor(R.color.lightGrey));
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		businessHoursLayout.addView(hoursView, params);
	}

	private String getDayString(int day) {
		switch (day) {
			case 0:
				return getString(R.string.monday);
			case 1:
				return getString(R.string.tuesday);
			case 2:
				return getString(R.string.wednesday);
			case 3:
				return getString(R.string.thursday);
			case 4:
				return getString(R.string.friday);
			case 5:
				return getString(R.string.saturday);
			case 6:
				return getString(R.string.sunday);
		}
		return ""; //shouldn't have more than 7 days...
	}

	private String timeToAMPM(String time) {
		String hr = time.substring(0, 2);
		String min = time.substring(2, 4);
		Integer hrInt = Integer.parseInt(hr);
		boolean am = false;
		if(hrInt < 12) {
			am = true;
		} else {
			hrInt %= 12;
		}

		if(hrInt == 0) {
			hrInt = 12;
		}
		String stringAmPm = getString(am ? R.string.am : R.string.pm);
		return getString(R.string.hour_am_pm, String.valueOf(hrInt), min, stringAmPm);
	}

	private void setupCategories(List<Category> categories) {
		businessCategoriesLayout.removeAllViews();
		for(Category category : categories) {
			addCategoryView(category);
		}
	}

	private void addCategoryView(Category category) {
		TextView categoryView = (TextView) getLayoutInflater().inflate(R.layout.category, businessCategoriesLayout, false);
		categoryView.setText(category.getTitle());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		businessCategoriesLayout.addView(categoryView, params);
	}
}
