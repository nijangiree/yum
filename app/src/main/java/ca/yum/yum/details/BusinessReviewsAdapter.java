package ca.yum.yum.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.yum.yum.R;
import ca.yum.yum.businesses.ReviewHolder;
import ca.yum.yum.model.Review;

/**
 * Created by nijan.
 */

public class BusinessReviewsAdapter extends RecyclerView.Adapter<ReviewHolder> {

	List<Review> reviews;

	public BusinessReviewsAdapter(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View layout = inflater.inflate(R.layout.review_in_details, parent, false);
		ReviewHolder reviewHolder = new ReviewHolder(layout);
		return reviewHolder;
	}

	@Override
	public void onBindViewHolder(ReviewHolder holder, int position) {
		Review review = reviews.get(position);
		holder.update(review);
	}

	@Override
	public int getItemCount() {
		return reviews.size();
	}
}
