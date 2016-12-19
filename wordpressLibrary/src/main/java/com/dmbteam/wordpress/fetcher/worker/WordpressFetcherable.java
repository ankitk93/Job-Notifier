package com.dmbteam.wordpress.fetcher.worker;

import java.util.List;

import com.dmbteam.wordpress.fetcher.cmn.Category;
import com.dmbteam.wordpress.fetcher.cmn.Post;

public interface WordpressFetcherable {

	public void resultAllCategories(List<Category> categories);

	public void resultAllPostsForCategory(List<Post> categories);
}
