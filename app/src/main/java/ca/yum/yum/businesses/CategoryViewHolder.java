package ca.yum.yum.businesses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by nijan.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {

	private TextView category;

	public CategoryViewHolder(View itemView) {
		super(itemView);
		category = (TextView) itemView;
	}

	public void update(String categoryTitle, int children) {
		category.setText(categoryTitle + " (" + children + ")");
	}
}
