package ca.yum.yum.yelp;

/**
 * Created by nijan.
 */
public class SearchOptions {
	public enum SortBy {
		SORT_BEST_MATCH("best_match"),
		SORT_RATING("rating"),
		SORT_REVIEW_COUNT("review_count"),
		SORT_DISTANCE("distance");
		String value;

		SortBy(String value) {
			this.value = value;
		}
	}

	String searchTerm;
	String location;
	int limit;
	SortBy sortBy = SortBy.SORT_BEST_MATCH;

	public SearchOptions setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
		return this;
	}

	public SearchOptions setLocation(String location) {
		this.location = location;
		return this;
	}

	public SearchOptions setSortBy(SortBy sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public SearchOptions setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public String getLocation() {
		return location;
	}

	public int getLimit() {
		return limit;
	}

	public SortBy getSortBy() {
		return sortBy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof SearchOptions) {
			SearchOptions ops = (SearchOptions) obj;
			return searchTerm.equals(ops.searchTerm)
					&& location.equals(ops.location)
					&& limit == ops.limit
					&& sortBy == ops.sortBy;
		}
		return super.equals(obj);
	}
}
