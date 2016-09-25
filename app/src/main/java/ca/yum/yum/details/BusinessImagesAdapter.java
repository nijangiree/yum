package ca.yum.yum.details;

import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import ca.yum.yum.R;

/**
 * Created by nijan.
 */

public class BusinessImagesAdapter extends RecyclerView.Adapter<BusinessImagesAdapter.ImageViewHolder> {

	static class ImageViewHolder extends RecyclerView.ViewHolder {

		Drawable placeholder;
		ImageView imageView;
		public ImageViewHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView;
			placeholder = VectorDrawableCompat.create(itemView.getResources(), R.drawable.ic_image_grey_24dp, null);
		}

		public void update(String url) {
			Glide.with(itemView.getContext())
					.load(url)
					.placeholder(placeholder)
					.into(imageView);
		}
	}

	List<String> businessImages;

	public BusinessImagesAdapter(List<String> businessImages) {
		this.businessImages = businessImages;
	}

	@Override
	public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View layout = inflater.inflate(R.layout.business_image, parent, false);
		return new ImageViewHolder(layout);
	}

	@Override
	public void onBindViewHolder(ImageViewHolder holder, int position) {
		holder.update(businessImages.get(position));
	}

	@Override
	public int getItemCount() {
		return businessImages.size();
	}
}
