package ca.yum.yum;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ca.yum.yum.businesses.BusinessesAdapter;
import ca.yum.yum.businesses.DataFragment;
import ca.yum.yum.model.CategorizedBusinesses;
import ca.yum.yum.yelp.YelpController;

public class RestaurantsActivity extends AppCompatActivity implements DataFragment.FetchDataListener {

	SearchView searchView;
	DataFragment dataFragment;
	RecyclerView recyclerView;
	BusinessesAdapter businessesAdapter;
	ProgressBar progressBar;
	View emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurants);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initializeDataFragment();
		dataFragment.setFetchDataListener(this);
		progressBar = (ProgressBar) findViewById(R.id.businesses_loading_bar);
		recyclerView = (RecyclerView) findViewById(R.id.businesses_recycler_view);
		emptyView = findViewById(R.id.empty);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
		recyclerView.setHasFixedSize(true);
		businessesAdapter = new BusinessesAdapter(dataFragment.getCategorizedBusinesses());
		recyclerView.setAdapter(businessesAdapter);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search_location);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		if(dataFragment.getLastQuery() == null) {
			beginSearch("Ethiopian", "Toronto");
		}
	}

	private void initializeDataFragment() {
		FragmentManager fm = getSupportFragmentManager();
		dataFragment = (DataFragment) fm.findFragmentByTag("data");
		if(dataFragment == null) {
			dataFragment = new DataFragment();
			dataFragment.initialize(this);
			fm.beginTransaction().add(dataFragment, "data").commit();
			//todo add controller here
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
				beginSearch(query, "Toronto");
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

	private void beginSearch(String query, String location) {
		progressBar.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		recyclerView.setVisibility(View.GONE);
		YelpController.SearchOptions searchOptions = new YelpController.SearchOptions();
		searchOptions
				.setSearchTerm(query)
				.setLimit(3)
				.setLocation(location)
				.setSortBy(YelpController.SearchOptions.SortBy.SORT_BEST_MATCH);
		if(!searchOptions.equals(dataFragment.getLastQuery())) {
			dataFragment.beginSearch(searchOptions);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_sort) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataFragment.setFetchDataListener(null);
	}

	@Override
	public void onComplete() {
		if(dataFragment.getCategorizedBusinesses().getTotal() == 0) {
			emptyView.setVisibility(View.VISIBLE);
		}
		businessesAdapter.notifyDataSetChanged();
		progressBar.setVisibility(View.GONE);
		recyclerView.setVisibility(View.VISIBLE);
	}
}
