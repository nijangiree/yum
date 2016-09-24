package ca.yum.yum.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by nijan.
 */
public class CategorizedBusinessesTest {

	CategorizedBusinesses categorizedBusinesses;

	@Before
	public void setup() {
		categorizedBusinesses = new CategorizedBusinesses();
		categorizedBusinesses.add(generateBusinessWithReviews("food", "some-place-1"));
		categorizedBusinesses.add(generateBusinessWithReviews("food", "some-place-2"));
		categorizedBusinesses.add(generateBusinessWithReviews("tea", "some-tea-place-1"));
		categorizedBusinesses.add(generateBusinessWithReviews("tea", "some-tea-place-2"));
		categorizedBusinesses.add(generateBusinessWithReviews("arcade", "some-arcade-place-1"));
		categorizedBusinesses.add(generateBusinessWithReviews("arcade", "some-arcade-place-2"));
		categorizedBusinesses.add(generateBusinessWithReviews("arcade", "some-arcade-place-3"));
	}

	@Test
	public void testIndexes() {
		//Note: ordered by category natural ordering so 1) arcade, 2) food, 3) tea
		assertTrue(categorizedBusinesses.isCategory(0));
		assertTrue(categorizedBusinesses.isCategory(7));

		assertFalse(categorizedBusinesses.isCategory(1));
		assertFalse(categorizedBusinesses.isCategory(9));

		assertThat(categorizedBusinesses.getBusinessWithReviews(1).getBusiness().getId(),
				is("some-arcade-place-1"));
		assertThat(categorizedBusinesses.getBusinessWithReviews(9).getBusiness().getId(),
				is("some-tea-place-2"));

		assertThat(categorizedBusinesses.getCategory(0), is("arcade"));
		assertThat(categorizedBusinesses.getCategory(7), is("tea"));

		assertThat(categorizedBusinesses.getTotalItemsInCategory("arcade"), is(3));
		assertThat(categorizedBusinesses.getTotal(), is(10));
	}

	private BusinessWithReviews generateBusinessWithReviews(String category, String businessId) {
		List<Category> categories = new ArrayList<>();
		Category businessCategory = new Category();
		Business business = new Business();
		businessCategory.setAlias(category);
		businessCategory.setTitle(category);
		categories.add(businessCategory);
		business.setId(businessId);
		business.setCategories(categories);
		BusinessWithReviews businessWithReviews = new BusinessWithReviews();
		businessWithReviews.setBusiness(business);
		return businessWithReviews;
	}

}