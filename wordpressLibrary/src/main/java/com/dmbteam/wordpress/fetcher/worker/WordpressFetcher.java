package com.dmbteam.wordpress.fetcher.worker;

import java.util.ArrayList;
import java.util.List;

import com.dmbteam.wordpress.fetcher.cmn.Category;

import android.content.Context;

public class WordpressFetcher {

	private String mServerUrl;

	// forbidden categories
	private List<String> mForbiddenCategories;

	private int mPostCountResult;

	private Context mContext;

	private static WordpressFetcher mInstance;

	private WordpressFetcher(Context context) {
		this.mContext = context;
		mPostCountResult = 10;
	}

	public static WordpressFetcher getInstance(Context context) {
		if (mInstance == null) {
			synchronized (WordpressFetcher.class) {
				if (mInstance == null) {
					mInstance = new WordpressFetcher(context);
				}
			}
		}

		return mInstance;
	}

	public void fetchAllCategoriesWithCallback(WordpressFetcherable callback) {
		new FetchAllCategories(mContext, callback).execute();
	}

	public void fetchPostsForCategoryWithCallback(
			WordpressFetcherable callback, Category category, int page) {
		new FetchRecentPostsByCategory(mContext, callback, category, page)
				.execute();
	}

	public List<String> getForbiddenCategories() {

		if (mForbiddenCategories == null) {
			mForbiddenCategories = new ArrayList<String>();
		}

		return mForbiddenCategories;
	}

	public void setForbiddenCategories(List<String> forbiddenCategories) {
		mForbiddenCategories = forbiddenCategories;
	}

	public String getServerUrl() {
		return mServerUrl;
	}

	public void setServerUrl(String serverUrl) {
		mServerUrl = serverUrl;
	}

	public int getPostCountResult() {
		return mPostCountResult;
	}

	public void setPostCountResult(int postCountResult) {
		mPostCountResult = postCountResult;
	}

}
