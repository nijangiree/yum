package ca.yum.yum.businesses;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ca.yum.yum.BusinessDetailsActivity;
import ca.yum.yum.R;
import ca.yum.yum.model.Business;
import ca.yum.yum.model.BusinessWithReviews;

/**
 * Created by nijan.
 */

public class BusinessRatingViewHolder extends RecyclerView.ViewHolder {

	ReviewHolder reviewHolder;
	ImageView image;
	TextView name;
	TextView noReview;
	Drawable placeholder;
	BusinessClickListener listener;

	public BusinessRatingViewHolder(View itemView, BusinessClickListener listener) {
		super(itemView);
		View reviewView = itemView.findViewById(R.id.review);
		reviewHolder = new ReviewHolder(reviewView);
		image = (ImageView) itemView.findViewById(R.id.business_image);
		name = (TextView) itemView.findViewById(R.id.business_name);
		noReview = (TextView) itemView.findViewById(R.id.no_review);
		placeholder = VectorDrawableCompat.create(itemView.getResources(), R.drawable.ic_image_grey_24dp, null);
		this.listener = listener;
	}

	public void update(final BusinessWithReviews businessWithReviews) {
		Business business = businessWithReviews.getBusiness();
		name.setText(business.getName());
		if(businessWithReviews.getReviews().isEmpty()) {
			reviewHolder.itemView.setVisibility(View.GONE);
			noReview.setVisibility(View.VISIBLE);
		} else {
			noReview.setVisibility(View.GONE);
			reviewHolder.itemView.setVisibility(View.VISIBLE);
			reviewHolder.update(businessWithReviews.getReviews().get(0));
		}
		Glide.with(itemView.getContext())
				.load(businessWithReviews.getBusiness().getImageUrl())
				.placeholder(placeholder)
				.into(image);
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(businessWithReviews);
			}
		});
	}
}
