package ca.yum.yum.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by nijan.
 */

public class CategorizedBusinesses {
	Map<String, List<BusinessWithReviews>> categoryToBusiness = new TreeMap<>();
	int total = 0;

	public int getTotal() {
		return total;
	}

	public void add(BusinessWithReviews business) {
		String category = business.business.getCategories().get(0).getTitle();
		List<BusinessWithReviews> businesses = getOrPut(category);
		businesses.add(business);
		total++;
	}

	private List<BusinessWithReviews> getOrPut(String category) {
		List<BusinessWithReviews> businesses;
		if(categoryToBusiness.containsKey(category)) {
			businesses = categoryToBusiness.get(category);
		} else {
			businesses = new ArrayList<>();
			categoryToBusiness.put(category, businesses);
			total++;
		}
		return businesses;
	}

	public boolean isCategory(int index) {
		Set<String> keys = categoryToBusiness.keySet();
		int curIndex = 0;
		for(String key : keys) {
			if(index == curIndex) {
				return true;
			} else if (curIndex > index) {
				return false;
			}
			int numBusinesses = categoryToBusiness.get(key).size();
			curIndex += 1 + numBusinesses;
		}
		return false;
	}

	public String getCategory(int index) {
		Set<String> keys = categoryToBusiness.keySet();
		int curIndex = 0;
		for(String key : keys) {
			if(index == curIndex) {
				return key;
			}
			int numBusinesses = categoryToBusiness.get(key).size();
			curIndex += 1 + numBusinesses;
		}
		return null;
	}

	public int getTotalItemsInCategory(String category) {
		return categoryToBusiness.get(category).size();
	}

	//O(m) worst case where m = # categories...should be okay for 10 items..
	public BusinessWithReviews getBusinessWithReviews(int index) {
		Set<String> keys = categoryToBusiness.keySet();
		int curIndex = 0;
		for(String key : keys) {
			curIndex++;
			int numBusinesses = categoryToBusiness.get(key).size();
			if(index < curIndex + numBusinesses) {
				int subIndex = index - curIndex;
				return categoryToBusiness.get(key).get(subIndex);
			}
			curIndex += numBusinesses;
		}
		return null;
	}

	public void clear() {
		categoryToBusiness.clear();
		total = 0;
	}

}
