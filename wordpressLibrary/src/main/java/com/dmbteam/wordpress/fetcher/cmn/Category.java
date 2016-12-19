package com.dmbteam.wordpress.fetcher.cmn;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;

/**
 * This class represents a Category from Wordpress
 * 
 * @author |dmb TEAM|
 * 
 */
public class Category implements Serializable {

	/**
	 * UID version for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identifier of the Category
	 */
	private Integer mId;
	/**
	 * Slug of the Category
	 */
	private String mSlug;

	/**
	 * Title of the Category
	 */
	private String mTitle;

	private int mPostCount;

	/**
	 * Class constructor
	 * 
	 * @param id
	 *            Identifier of the Category
	 * @param title
	 *            Title of the Category
	 */
	public Category(Integer id, String title) {
		super();
		this.mId = id;
		this.mTitle = title;
	}

	/**
	 * Class constructor
	 * 
	 * @param jsonObject
	 *            JSON Object
	 * @throws JSONException
	 *             Exception to be thrown
	 */
	public Category(JSONObject jsonObject) throws JSONException {
		try {
			this.mId = Integer.valueOf(jsonObject.getString("id"));
			this.mPostCount = jsonObject.getInt("post_count");
			this.mTitle = Html.fromHtml(jsonObject.getString("title"))
					.toString();
			this.mSlug = Html.fromHtml(jsonObject.getString("slug")).toString();
		} catch (NumberFormatException e) {
			//
		}
	}

	/**
	 * Getter of the Category identifier
	 * 
	 * @return the Category identifier
	 */
	public Integer getId() {
		return mId;
	}

	public int getPostCount() {
		return mPostCount;
	}

	public void setPostCount(int postCount) {
		mPostCount = postCount;
	}

	/**
	 * Getter of the Category Slug
	 * 
	 * @return the Category Slug
	 */
	public String getSlug() {

		return mSlug;
	}

	/**
	 * Getter of the Category title
	 * 
	 * @return the Category title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Returns a string representation of the object
	 */
	@Override
	public String toString() {
		return getTitle();
	}
}
