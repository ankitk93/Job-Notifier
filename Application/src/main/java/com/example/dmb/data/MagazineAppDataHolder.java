package com.example.dmb.data;

import java.util.ArrayList;
import java.util.List;

import com.dmbteam.wordpress.fetcher.cmn.Post;

/**
 * The Class MagazineAppDataHolder.
 */
public class MagazineAppDataHolder {

	/** The instance. */
	private static MagazineAppDataHolder instance;

	/** The All posts. */
	private List<Post> mAllPosts;

	/**
	 * Instantiates a new magazine app data holder.
	 */
	private MagazineAppDataHolder() {
		this.mAllPosts = new ArrayList<Post>();
	}

	/**
	 * Gets the single instance of MagazineAppDataHolder.
	 *
	 * @return single instance of MagazineAppDataHolder
	 */
	public static MagazineAppDataHolder getInstance() {
		if (instance == null) {
			synchronized (MagazineAppDataHolder.class) {
				if (instance == null) {
					instance = new MagazineAppDataHolder();
				}
			}
		}

		return instance;
	}

	/**
	 * Adds the to current posts.
	 *
	 * @param posts the posts
	 */
	public void addToCurrentPosts(List<Post> posts) {
		this.mAllPosts.addAll(posts);
	}

	/**
	 * Gets the all posts.
	 *
	 * @return the all posts
	 */
	public List<Post> getAllPosts() {
		return mAllPosts;
	}

}
