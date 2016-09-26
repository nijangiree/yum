package ca.yum.yum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ca.yum.yum.businesses.BusinessClickListener;
import ca.yum.yum.businesses.BusinessesAdapter;
import ca.yum.yum.businesses.DataFragment;
import ca.yum.yum.model.BusinessWithReviews;
import ca.yum.yum.yelp.SearchOptions;

public class RestaurantsActivity extends AppCompatActivity implements DataFragment.FetchSearchDataListener {

	SearchView searchView;
	DataFragment dataFragment;
	RecyclerView recyclerView;
	BusinessesAdapter businessesAdapter;
	ProgressBar progressBar;
	View emptyView;
	View searchLocationView;
	ImageButton locationSearchButton;
	EditText locationEditText;
	FloatingActionButton locationFab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurants);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initializeDataFragment();
		dataFragment.setFetchSearchDataListener(this);
		progressBar = (ProgressBar) findViewById(R.id.businesses_loading_bar);
		recyclerView = (RecyclerView) findViewById(R.id.businesses_recycler_view);
		emptyView = findViewById(R.id.empty);
		final int spans = getResources().getInteger(R.integer.grid_size);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spans);
		gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				int type = businessesAdapter.getItemViewType(position);
				if(type == BusinessesAdapter.TYPE_CATEGORY) {
					return spans;
				} else {
					return 1;
				}
			}
		});
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.setHasFixedSize(true);
		businessesAdapter = new BusinessesAdapter(dataFragment.getCategorizedBusinesses(), new BusinessClickListener() {
			@Override
			public void onClick(BusinessWithReviews businessWithReviews) {
				Intent intent = new Intent(getApplicationContext(), BusinessDetailsActivity.class);
				intent.putExtra(BusinessDetailsActivity.INTENT_BUSINESS_DETAILS, businessWithReviews.getBusiness().getId());
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		recyclerView.setAdapter(businessesAdapter);
		locationFab = (FloatingActionButton) findViewById(R.id.fab_search_location);
		locationFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showSearchLocation(true);
			}
		});
		setupLocationSearch();

		if(dataFragment.getLastQuery() == null) {
			beginSearch("Ethiopian", "Toronto", SearchOptions.SortBy.SORT_BEST_MATCH);
		} else {
			if(dataFragment.isFetching()) {
				showProgress(true);
			}
		}
	}

	private void initializeDataFragment() {
		FragmentManager fm = getSupportFragmentManager();
		dataFragment = (DataFragment) fm.findFragmentByTag("data");
		if(dataFragment == null) {
			dataFragment = new DataFragment();
			dataFragment.initialize(this);
			fm.beginTransaction().add(dataFragment, "data").commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_restaurants, menu);
		final MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				beginSearch(query, "Toronto", SearchOptions.SortBy.SORT_BEST_MATCH);
				searchItem.collapseActionView();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		return true;
	}

	private void beginSearch(String query, String location, SearchOptions.SortBy order) {
		SearchOptions searchOptions = new SearchOptions();
		searchOptions
				.setSearchTerm(query)
				.setLimit(10)
				.setLocation(location)
				.setSortBy(order);
		if(!searchOptions.equals(dataFragment.getLastQuery())) {
			showProgress(true);
			dataFragment.beginSearch(searchOptions);
		}
	}

	private void showProgress(boolean visible) {
		if(visible) {
			locationFab.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			locationFab.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_sort_best_match:
				beginSort(SearchOptions.SortBy.SORT_BEST_MATCH);
				return true;
			case R.id.action_sort_distance:
				beginSort(SearchOptions.SortBy.SORT_DISTANCE);
				return true;
			case R.id.action_sort_rating:
				beginSort(SearchOptions.SortBy.SORT_RATING);
				return true;
			case R.id.action_sort_review_count:
				beginSort(SearchOptions.SortBy.SORT_REVIEW_COUNT);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataFragment.setFetchSearchDataListener(null);
	}

	@Override
	public void onComplete() {
		showProgress(false);
		if(dataFragment.getCategorizedBusinesses().getTotal() == 0) {
			emptyView.setVisibility(View.VISIBLE);
		}
		businessesAdapter.notifyDataSetChanged();
	}

	private void setupLocationSearch() {
		searchLocationView = findViewById(R.id.search_location);
		locationSearchButton = (ImageButton) findViewById(R.id.search_location_button);
		locationEditText = (EditText) findViewById(R.id.search_location_entry);
		locationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
					beginLocationSearch();
					return true;
				}
				return false;
			}
		});
		locationSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				beginLocationSearch();
			}
		});
	}

	private void beginLocationSearch() {
		String location = locationEditText.getText().toString();
		if(location.isEmpty()) {
			locationEditText.setError(getString(R.string.error_search_location_empty));
			locationEditText.requestFocus();
		} else {
			showSearchLocation(false);
			beginSearch("", location, SearchOptions.SortBy.SORT_BEST_MATCH);
		}
	}

	private void showSearchLocation(boolean visible) {
		if(visible) {
			locationFab.setVisibility(View.GONE);
			searchLocationView.setVisibility(View.VISIBLE);
		} else {
			locationFab.setVisibility(View.VISIBLE);
			searchLocationView.setVisibility(View.GONE);
		}
	}

	private void beginSort(SearchOptions.SortBy sortBy) {
		if(dataFragment.getLastQuery().getSortBy() != sortBy) {
			beginSearch(dataFragment.getLastQuery().getSearchTerm(),
					dataFragment.getLastQuery().getLocation(),
					sortBy);
		}
	}
}
