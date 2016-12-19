package com.dmbteam.wordpress.fetcher.util;

import java.util.Comparator;

import com.dmbteam.wordpress.fetcher.cmn.Category;

/**
 * This class is used to compare two <code>Category</code> objects by their Slug property.
 * @author |dmb TEAM|
 *
 */
public class CategoryIndexComparator implements Comparator<Category> {

	/**
	 * Compares two <code>Category</code> objects by their Slug property.
	 */
	@Override
	public int compare(Category c1, Category c2) {
		return c1.getTitle().compareTo(c2.getTitle());
	}

}
