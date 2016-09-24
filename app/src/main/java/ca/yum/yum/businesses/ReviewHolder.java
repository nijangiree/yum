package ca.yum.yum.businesses;

import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ca.yum.yum.R;
import ca.yum.yum.model.Review;

/**
 * Created by nijan.
 */

public class ReviewHolder {

	ImageView userImage;
	RatingBar reviewRating;
	TextView userName;
	TextView userReview;
	View review;
	Drawable placeholder;

	public ReviewHolder(View review) {
		this.review = review;
		userImage = (ImageView) review.findViewById(R.id.user_image);
		reviewRating = (RatingBar) review.findViewById(R.id.review_rating);
		userName = (TextView) review.findViewById(R.id.user_name);
		userReview = (TextView) review.findViewById(R.id.user_review);
		placeholder = VectorDrawableCompat.create(review.getResources(), R.drawable.ic_person_black_24dp, null);
	}

	public void update(Review review) {
		userName.setText(review.getUser().getName());
		userReview.setText(review.getText());
		reviewRating.setRating(review.getRating());
		Glide.with(this.review.getContext())
				.load(review.getUser().getImageUrl())
				.placeholder(placeholder)
				.into(userImage);
	}

	public View getReview() {
		return review;
	}
}
