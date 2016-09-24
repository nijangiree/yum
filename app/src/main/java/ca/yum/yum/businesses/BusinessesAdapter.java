package ca.yum.yum.businesses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.yum.yum.R;
import ca.yum.yum.model.BusinessWithReviews;
import ca.yum.yum.model.CategorizedBusinesses;

/**
 * Created by nijan.
 */

public class BusinessesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_CATEGORY = 1;
	private static final int TYPE_BUSINESS = 2;

	CategorizedBusinesses categorizedBusinesses;

	public BusinessesAdapter(CategorizedBusinesses categorizedBusinesses) {
		this.categorizedBusinesses = categorizedBusinesses;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		if(viewType == TYPE_CATEGORY) {
			View itemView = inflater.inflate(R.layout.category_item, parent, false);
			CategoryViewHolder categoryViewHolder = new CategoryViewHolder(itemView);
			return categoryViewHolder;
		} else {
			View itemView = inflater.inflate(R.layout.business_item, parent, false);
			BusinessRatingViewHolder businessHolder = new BusinessRatingViewHolder(itemView);
			return businessHolder;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof CategoryViewHolder) {
			String category = categorizedBusinesses.getCategory(position);
			int total = categorizedBusinesses.getTotalItemsInCategory(category);
			((CategoryViewHolder) holder).update(category, total);
		} else {
			BusinessWithReviews item = categorizedBusinesses.getBusinessWithReviews(position);
			((BusinessRatingViewHolder) holder).update(item);
		}
	}

	@Override
	public int getItemCount() {
		return categorizedBusinesses.getTotal();
	}

	@Override
	public int getItemViewType(int position) {
		if(categorizedBusinesses.isCategory(position)) {
			return TYPE_CATEGORY;
		} else {
			return TYPE_BUSINESS;
		}
	}

	@Override
	public long getItemId(int position) {
		if(categorizedBusinesses.isCategory(position)) {
			String category = categorizedBusinesses.getCategory(position);
			return category.hashCode();
		} else {
			return categorizedBusinesses
					.getBusinessWithReviews(position)
					.getBusiness()
					.getId()
					.hashCode();
		}
	}
}
