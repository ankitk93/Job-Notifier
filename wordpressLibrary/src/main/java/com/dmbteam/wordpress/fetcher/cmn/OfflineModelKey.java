package com.dmbteam.wordpress.fetcher.cmn;

public class OfflineModelKey {

	private String mUrl;
	private String mCategoryName;
	private int mPage;

	public OfflineModelKey(String url, String categoryName, int page) {
		super();
		mUrl = url;
		mCategoryName = categoryName;
		mPage = page;
	}

	public OfflineModelKey(String url) {
		super();
		mUrl = url;
	}

	public String getUniqueKey() {
		return mUrl + mCategoryName + mPage;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getCategoryName() {
		return mCategoryName;
	}

	public void setCategoryName(String categoryName) {
		mCategoryName = categoryName;
	}

	public int getPage() {
		return mPage;
	}

	public void setPage(int page) {
		mPage = page;
	}

}
